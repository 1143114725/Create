package cn.dapchina.newsupper.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Call;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.db.DBOpenHelper;
import cn.dapchina.newsupper.db.DBSQLService;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.service.FileUpLoad;
import cn.dapchina.newsupper.util.Util;

public class DataMonitorActivity extends Activity implements OnClickListener {

	private ScrollView svMsg;
	private Socket socket;
	private volatile PrintWriter writer;
	private volatile BufferedReader reader;
	private MessageThread messageThread;// 负责接收消息的线程

	private boolean isConnected = false;

	int start, end;
	private FileUpLoad fileUpload;   

	private String name = "";

	private volatile ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();

	private MyApp ma;

	private View charView;
	private Button btnSend;

	private DBSQLService sqlService;

	private ImageView leftIv;
	private EditText etMsg;
	private TextView tvMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.monitor_activity);
		leftIv = (ImageView) findViewById(R.id.data_left_iv);
		leftIv.setOnClickListener(this);
		tvMsg = (TextView) findViewById(R.id.msg_tv);
		etMsg = (EditText) findViewById(R.id.msg_et);
		etMsg.setInputType(InputType.TYPE_NULL);
		etMsg.setOnTouchListener(new FocusListener());
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		sqlService = new DBSQLService(DataMonitorActivity.this);
		svMsg = (ScrollView) findViewById(R.id.msg_sv);
		charView = findViewById(R.id.send_ll);
		btnSend = (Button) findViewById(R.id.send_btn);
		btnSend.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				send();
			}
		});
		// name = getIntent().getStringExtra("UserId");
		name = ma.cfg.getString("UserId", "") + "(" + ma.cfg.getString("authorId", "") + ")";
		Util.Log(ma.cfg.getString("UserId", ""));
		Util.Log(ma.cfg.getString("authorId", ""));
		if (!isConnected)
			Log.i("zrl1","ip:"+Cnt.CLIENT_SERVER_IP+"port:"+Cnt.CLIENT_SERVER_PORT);
			connectServer(Cnt.CLIENT_SERVER_PORT, Cnt.CLIENT_SERVER_IP, name);

		fileUpload = new FileUpLoad();
	}

	private final class FocusListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				((EditText) v).setInputType(InputType.TYPE_CLASS_TEXT);
			}
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			DataMonitorActivity.this.finish();
			closeConnection();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.data_left_iv:
			DataMonitorActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		default:
			break;
		}
	}
	
	
	protected void send() {
		String msg = etMsg.getText().toString().trim();
		if (Util.isEmpty(msg)) {
			Util.viewShake(DataMonitorActivity.this, etMsg);
			return;
		}
		sendMsg(name + "@" + msg);
		tvMsg.append("me:" + msg + "\n");
	}
	
	/**
	 * 连接服务器
	 * 
	 * @param port
	 * @param hostIp
	 * @param name
	 */
	public void connectServer(int port, String hostIp, String name) {
		// 连接服务器
		new ConnectTask(port, hostIp, name).execute();
	}

	class ConnectTask extends AsyncTask<Void, Integer, Boolean> {
		int port;
		String hostIp;
		String name;

		public ConnectTask(int port, String hostIp, String name) {
			this.port = port;
			this.hostIp = hostIp;
			this.name = name;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean isSuccess = false;
			try {
				socket = new Socket(hostIp, port);// 根据端口号和服务器ip建立连接
				// socket.setSoTimeout(5000);
				writer = new PrintWriter(socket.getOutputStream());// UTF-8
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));// ,
																									// UTF-8
																									// "GBK"
				sendMsg(name + "@" + socket.getLocalAddress().toString());
				// 开启接收消息的线程
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 发送客户端用户基本信息(用户名和ip地址)
			return isSuccess;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				messageThread = new MessageThread(reader);
				messageThread.start();
				isConnected = true;
			} else {
				tvMsg.append("Connecting miss.\n");
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	/**
	 * 发送消息
	 * @param message
	 */
	public void sendMsg(final String message) {
		new Thread() {
			public void run() {
				if (null != writer) {
					writer.println(message);
					writer.flush();
				}
			};
		}.start();
	}

	/**
	 * 客户端主动关闭连接
	 */
	public synchronized void closeConnection() {
		new CloseTask().execute();
	}

	private class CloseTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (isConnected) {
					sendMsg("CLOSE");// 发送断开连接命令给服务器
					// messageThread.stop();// 停止接受消息线程
					messageThread.setStop(true);
					// 释放资源
					if (reader != null) {
						reader.close();
					}
					if (writer != null) {
						writer.close();
					}
					if (socket != null) {
						socket.close();
					}
					isConnected = false;
				}
				return Boolean.TRUE;
			} catch (IOException e1) {
				e1.printStackTrace();
				isConnected = true;
				return Boolean.FALSE;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				tvMsg.append("Close successfully!\n");
			} else {
				tvMsg.append("Fail closed!\n");
			}
			finish();
		}

	}

	// 不断接收消息的线程
	class MessageThread extends Thread {
		private BufferedReader reader;
		private boolean isRun = true;
		private boolean isStop = false;

		// 接收消息线程的构造方法
		public MessageThread(BufferedReader reader) {
			this.reader = reader;
		}

		public void run() {
			String message = "";
			while (isRun) {
				try {
					if (isStop) {
						isRun = false;
						throw new InterruptedException();
					}
					if (null != reader) {
						message = reader.readLine();
						if (null != message) {
							StringTokenizer stringTokenizer = new StringTokenizer(message, "/@");
							String command = stringTokenizer.nextToken();// 命令
							if (command.equals("CLOSE"))// 服务器已关闭命令
							{
								
								handler.sendEmptyMessage(4);
								break;// 结束线程
							} else if (command.equals("MAX")) {// 人数已达上限
								Message msg = handler.obtainMessage();
								msg.what = 0;
								msg.obj = stringTokenizer.nextToken() + stringTokenizer.nextToken() + "\n";
								handler.sendMessage(msg);

								msg.what = 1;
								handler.sendMessage(msg);

								msg.what = 0;
								msg.obj = "Server buffer out of memory!";
								handler.sendMessageDelayed(msg, 1000);
								return;// 结束线程
							} else {// 普通消息
								Message msg = handler.obtainMessage();
								msg.what = 0;
								msg.obj = message + "\n";
								handler.sendMessage(msg);
							}
						}
					}
				} catch (InterruptedException e) {
					isRun = false;
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		public void setStop(boolean isStop) {
			this.isStop = isStop;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String message = String.valueOf(msg.obj);
				if (-1 < message.toLowerCase().indexOf("sql:")) {
					tvMsg.append("Monitor_Server:sql:**************************\n");
				} else {
					tvMsg.append(message);
				}
				end = tvMsg.getMeasuredHeight() - svMsg.getHeight();
				svMsg.scrollTo(start, end);
				start = end;
				String msgBody = message.substring(15);
				String body[] = msgBody.split(":");
				if (0 < body.length) {
					if ("file".equals(body[0].trim().toLowerCase())) {
						// 说明是取文件的
						if ("database".equals(body[1].trim().toLowerCase())) {
							new UploadTask(true).execute();
						} else {
							if (0 < fs.size()) {
								sendMsg(name + "@" + "The task is being executed, please wait.");
							} else {
								fs = ma.dbService.getUploadFeedByKey(body[1].trim());
								if (0 < fs.size()) {
									new UploadTask(false).execute();
								} else {
									sendMsg(name + "@" + "no files");
								}
							}
						}
					} else if ("chat".equals(body[0].trim().toLowerCase())) {
						// 说明是聊天的
						if ("open".equals(body[1].trim().toLowerCase())) {
							charView.setVisibility(View.VISIBLE);
						} else if ("close".equals(body[1].trim().toLowerCase())) {
							charView.setVisibility(View.GONE);
						}
					} else if ("android".equals(body[0].trim().toLowerCase())) {
						// 对android进行操作
						if ("close".equals(body[1].trim().toLowerCase())) {
							closeConnection();
						}
					} else if ("sql".equals(body[0].trim().toLowerCase())) {
						String sql = body[1].trim().toLowerCase();
						if (!Util.isEmpty(sql)) {

							if (-1 < sql.toLowerCase().indexOf("select")) {
								sendMsg(name + "@The select statement is not supported.");
							} else {
								try {
									boolean b = sqlService.updateSQL(sql);
									if (b) {
										sendMsg(name + "@" + sql + " Successfully!");
									} else {
										sendMsg(name + "@" + sql + " exception.");
									}
								} catch (Exception e) {
									e.printStackTrace();
									sendMsg(name + "@" + sql + " exception.");
								}
							}
						}
					}
				}
				break;

			case 1:
				closeConnection();// 被动的关闭连接
				break;

			case 2:
				new UploadTask(false).execute();
				break;

			case 3:
				tvMsg.append(String.valueOf(msg.obj));
				break;

			case 4:
				tvMsg.append("Monitor_Server:Server has been closed!\n");
				handler.sendEmptyMessageDelayed(1, 2000);
				break;
			}
		}

	};
	
	private final class UploadTask extends AsyncTask<Void, Integer, Boolean> {
		private boolean is;

		public UploadTask(boolean isDatabase) {
			this.is = isDatabase;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			if (is) {
				UploadFeed feed = new UploadFeed();
				feed.setPath("/data/data/cn.dapchina.newsupper/databases");
				feed.setName(DBOpenHelper.DB_NAME);
				fs.add(feed);
			}
			if (0 < fs.size()) {
				UploadFeed feed = fs.get(0);
				final File file = new File(feed.getPath(), feed.getName());
				fileUpload.uploadFile(file, new Call() {

					@Override
					public void updateProgress(int curr, int total) {
						if (curr == total) {
							writer.println(name + "@" + file.getName() + " done.");
							writer.flush();
						}
					}
				}, name);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (0 < fs.size()) {
				fs.remove(0);
			}
			if (0 < fs.size()) {
				handler.sendEmptyMessage(2);
			}
		}

	}
	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}
}
