package cn.dapchina.newsupper.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.UploadActivity.ContinueFTP;
import cn.dapchina.newsupper.adapter.UploadAdapter;
import cn.dapchina.newsupper.bean.MyRecoder;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.ftp.UploadStatus;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.service.FileUpLoad;
import cn.dapchina.newsupper.service.RecodeXml;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.MD5;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.StreamTool;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;

public class UploadActivity extends BaseActivity implements OnClickListener {

	private ImageView upload_left_iv;
	private ListView lvUpload;
	private UploadAdapter uploadAdapter;
	private CheckBox select_all;// 全选
	private LinearLayout all_upload;// 上传
	private ArrayList<UploadFeed> list;
	private Boolean isAll = false;// false 为空 true为全选
	private TextView tvNoUploadList;// 没上传列表
	private MyApp ma;
	private ArrayList<Survey> ss;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.upload_activity);
		upload_left_iv = (ImageView) findViewById(R.id.upload_left_iv);
		upload_left_iv.setOnClickListener(this);
		lvUpload = (ListView) findViewById(R.id.listView1);
		tvNoUploadList = (TextView) findViewById(R.id.no_upload_list_tv);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		Intent intent = getIntent();
		setResult(11, intent);
		select_all = (CheckBox) findViewById(R.id.select_all);
		select_all.setOnClickListener(this);
		all_upload = (LinearLayout) findViewById(R.id.all_upload);
		all_upload.setOnClickListener(this);
		startUpload();
	}

	@Override
	protected void onResume() {
		if (Util.isEmpty(ma.userId)) {
			ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(this)) : (ma.cfg)).getString("UserId", "");
		}
		ss = ma.dbService.getAllUploadSurvey(ma.userId);
		if (!Util.isEmpty(ss)) {
			tvNoUploadList.setVisibility(View.GONE);
			lvUpload.setVisibility(View.VISIBLE);
			if (null == uploadAdapter) {
				uploadAdapter = new UploadAdapter(UploadActivity.this, ma, ss);
				lvUpload.setAdapter(uploadAdapter);
			} else {
				uploadAdapter.refresh(ss);
			}
		} else {
			lvUpload.setVisibility(View.GONE);
			tvNoUploadList.setVisibility(View.VISIBLE);
			if (null != uploadAdapter) {
				uploadAdapter.notifyDataSetChanged();
			}
		}
		super.onResume();
	}

	private void startUpload() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UploadActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.upload_left_iv:
			UploadActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.select_all:
			// 现在为空变成全选,全选后text设置为反选
			if (!isAll) {
				select_all.setChecked(true);
				//select_all.setText(R.string.select_null);
				isAll = true;
			} else {
				select_all.setChecked(false);
				//select_all.setText(R.string.select_all);
				isAll = false;
			}
			uploadAdapter.check(isAll);
			break;
		// 上传
		case R.id.all_upload:
			if (null != uploadAdapter) {
				ArrayList<Survey> allSurvey = uploadAdapter.getAllSurvey();
				if (Util.isEmpty(allSurvey)) {
					Toasts.makeText(this, this.getString(R.string.no_upload), Toast.LENGTH_LONG).show();
				} else {
					if (!NetUtil.checkNet(UploadActivity.this)) {
						Toasts.makeText(UploadActivity.this, R.string.exp_net, Toast.LENGTH_SHORT).show();
						return;
					}
					UploadActivity.this.show();
					// 计算拼串
					String mySid = "(";
					if (null != allSurvey && allSurvey.size() > 0) {
						for (int i = 0; i < allSurvey.size(); i++) {
							Survey s = allSurvey.get(i);
							String surveyId = s.surveyId;
							// 最后一个时候
							if (i == allSurvey.size() - 1) {
								mySid += "_SurveyId=" + surveyId;
							} else {
								mySid += "_SurveyId=" + surveyId + " or ";
							}
						}
						mySid += ")";
					} else {
						mySid = "";
						Toasts.makeText(UploadActivity.this, R.string.no_reason_reupload, Toast.LENGTH_LONG).show();
						return;
					}
					// 计算拼串结束
					ArrayList<UploadFeed> xmlTempFs = ma.dbService.getAllSurveysCompletedUploadFeed(ma.userId, mySid);// 所有要上传的xml
					for (UploadFeed tenpFeed : xmlTempFs) {
						// 先把要上传的文件置为1（数据和附件）
						ma.dbService.updateFeedStatus(tenpFeed.getUuid(), Cnt.UPLOAD_STATE_UPLOADED);
						// 假如原先是已上传中 争议
						if (2 == tenpFeed.getIsUploaded()) {
							ma.dbService.updateFeedStatusByName(tenpFeed.getName(), 2);
						}
					}
					// 非XML的大小
					ma.dbService.updateFeedStatusBySurveys(ma.userId, 2, mySid);// 要上传的xml上传状态置为2
					ArrayList<UploadFeed> xmlFs = null;
					// 直接上传XML。
					xmlFs = ma.dbService.getAllCompletedSurveysUploadFeedIpsos(mySid, ma.userId);// 获取上传状态为2的所有xml
					Log.i("zrl1", xmlFs.size() + ":tempFs.size()");
					long sizeSum = ma.dbService.getSum(ma.userId);// 查
																	// 出所有上传状态为1的附件大小
					if (Util.isEmpty(xmlFs) && 0 == sizeSum) {
						Toasts.makeText(UploadActivity.this, R.string.null_upload, Toast.LENGTH_SHORT).show();
						dismiss();
					} else {
						if (Util.isEmpty(xmlFs) && 0 < sizeSum) {
							dismiss();
							// 打开进度条
							showDialog(DIALOG_DOWNLOAD_PROGRESS);
							Log.i("zrl1", "测试大小" + sizeSum);
							if (sizeSum > 0) {
								// Toasts.makeText(UploadActivity.this,
								// R.string.finish_survey_starts,
								// Toast.LENGTH_SHORT).show();
								// 先传附件
								// boolean isEmpty =
								// ma.dbService.isEmptyRecordList();
								// if (isEmpty) {
								// Toasts.makeText(UploadActivity.this,getResources().getString(R.string.),Toast.LENGTH_SHORT).show();
								// } else {
								// //
								// Toasts.makeText(UploadActivity.this,getResources().getString(R.string.finish_survey_starts),Toast.LENGTH_SHORT).show();
								prepareUploadRecord();
								// }
								if (null != uploadAdapter) {
									uploadAdapter.notifyDataSetChanged();
								}
							} else {
								dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
							}
						} else {
							uploadFile(xmlFs);
						}
					}
				}
			}
			break;
		default:
			break;
		}
	}

	// 上传开始
	private UploadRecordAndPhotoTask uploadRecordAndPhotoTask;
	private boolean isLast = false;
	static int tmp = 0;
	static int sum = 0;
	static int tmp_1 = 0;
	static int sum_1 = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				if (null != uploadRecordAndPhotoTask) {
					uploadRecordAndPhotoTask.cancel(true);
					uploadRecordAndPhotoTask = null;
				}
				uploadRecordAndPhotoTask = new UploadRecordAndPhotoTask();
				uploadRecordAndPhotoTask.execute();
				break;
			case 67:
				Toasts.makeText(UploadActivity.this, msg.arg1, Toast.LENGTH_LONG).show();
				if (msg.arg1 == R.string.exp_net || msg.arg1 == R.string.no_reason_reupload_exit) {
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				}
				break;
			case 68:
				Toasts.makeText(UploadActivity.this, msg.arg1, Toast.LENGTH_LONG).show();
				break;
			case 998:
				// Toasts.makeText(UploadActivity.this, "上传系统删除文件没有成功",
				// Toast.LENGTH_LONG).show();
				break;
			case 999:
				// Toasts.makeText(UploadActivity.this, "上传系统删除文件成功",
				// Toast.LENGTH_LONG).show();
				break;
			/**
			 * 显示底部文件上传进度条
			 */
			case 1000:// 开始上传
				// System.out.println("开始上传");
				// logView.setVisibility(View.VISIBLE);z1
				// goon.setText(getResources().getString(R.string.pause));z1
				break;
			case 2000:// 开始上传,初始化进度条
				sum = msg.arg2;// 个数
				sum_1 = msg.arg1;// 总大小
				// file_count.setText(getResources().getString(R.string.total_file,
				// "0", String.valueOf(sum_1)));z1
				// sum_of_files.setText(getResources().getString(R.string.total_byte,
				// String.valueOf(msg.arg2)));z1
				// uploadbar.setMax(msg.arg2);z1
//				System.out.println(sum);
				dialog.setMax(sum);
				break;

			case 3000:// 更新已上传文件个数

				// tmp_1 = msg.arg1;z1
				// sum_1 = msg.arg2;z1
				// file_count.setText(getResources().getString(R.string.total_file,
				// String.valueOf(msg.arg1), String.valueOf(msg.arg2)));z1
				break;

			case 10000:// 网络中断
				Toasts.makeText(UploadActivity.this, UploadActivity.this.getResources().getString(R.string.exp_net), Toast.LENGTH_SHORT).show();
				stopUploadTask();
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				break;

			case 0:
				// logView.setVisibility(View.VISIBLE);z1
				// file_count.setText(getResources().getString(R.string.total_file,
				// "0", String.valueOf(msg.arg1)));z1
				// sum_of_files.setText(getResources().getString(R.string.total_byte,
				// String.valueOf(msg.arg2)));z1
				// uploadbar.setMax(msg.arg2);z1
				break;

			case 1:
				dialog.setProgress(tmp);
				break;
			// 上传xml
			case 4000:
				if (!NetUtil.checkNet(UploadActivity.this)) {
					Toasts.makeText(UploadActivity.this, R.string.exp_net, Toast.LENGTH_SHORT).show();
					return;
				}
				// 非XML的大小
				long sizeSum = ma.dbService.getSum();
				if (sizeSum > 0) {
					dismiss();
					Toasts.makeText(UploadActivity.this, R.string.re_upload, Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	};

	private ContinueFTP myFtp;

	/** */
	/**
	 * 内部类 支持断点续传的FTP实用类
	 * 
	 * @author BenZhou http://www.bt285.cn
	 * @version 0.1 实现基本断点上传下载
	 * @version 0.2 实现上传下载进度汇报
	 * @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持
	 */
	public class ContinueFTP {
		public FTPClient ftpClient = new FTPClient();

		public ContinueFTP() {
			// 设置将过程中使用到的命令输出到控制台
			this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		}

		/**
		 * /** 连接到FTP服务器
		 * 
		 * @param hostname
		 *            主机名
		 * @param port
		 *            端口
		 * @param username
		 *            用户名
		 * @param password
		 *            密码
		 * @return 是否连接成功
		 * @throws IOException
		 */
		public boolean connect(String hostname, int port, String username, String password) throws IOException {
			ftpClient.connect(hostname, port);
			ftpClient.setControlEncoding("UTF-8");
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				if (ftpClient.login(username, password)) {
					return true;
				}
			}
			disconnect();
			return false;
		}

		/** */
		/**
		 * 上传文件到FTP服务器，支持断点续传
		 * 
		 * @param local
		 *            本地文件名称，绝对路径
		 * @param remote
		 *            远程文件路径，使用/home/directory1/subdirectory/file.ext或是
		 *            http://www.guihua.org /subdirectory/file.ext
		 *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
		 * @return 上传结果
		 * @throws IOException
		 */
		public UploadStatus upload(String local, String remote) throws IOException {
			// 设置PassiveMode传输
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("UTF-8");
			UploadStatus result;
			// 对远程目录的处理
			String remoteFileName = remote;
			// "/photos/" + fileName
			if (remote.contains("/")) {
				remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
				// 创建服务器远程目录结构，创建失败直接返回
				if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
					return UploadStatus.Create_Directory_Fail;
				}
			}
			// 检查远程是否存在文件
			FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("UTF-8"), "iso-8859-1"));
			if (files.length == 1) {
				long remoteSize = files[0].getSize();
				File f = new File(local);
				long localSize = f.length();
				if (remoteSize == localSize) {
					return UploadStatus.File_Exits;
				} else if (remoteSize > localSize) {
					if(myFtp.ftpClient.deleteFile(remoteFileName)){
						result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
					}else{
						return UploadStatus.Delete_Remote_Faild;
					}
				}
				// 尝试移动文件内读取指针,实现断点续传
				result = uploadFile(remoteFileName, f, ftpClient, remoteSize);
				// 如果断点续传没有成功，则删除服务器上文件，重新上传
				if (result == UploadStatus.Upload_From_Break_Failed) {
					if (!ftpClient.deleteFile(remoteFileName)) {
						return UploadStatus.Delete_Remote_Faild;
					}
					result = uploadFile(remoteFileName, f, ftpClient, 0);
				}
			} else {
				result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
			}
			return result;
		}

		/** */
		/**
		 * 断开与远程服务器的连接
		 * 
		 * @throws IOException
		 */
		public void disconnect() throws IOException {
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
		}

		/** */
		/**
		 * 递归创建远程服务器目录
		 * 
		 * @param remote
		 *            远程服务器文件绝对路径
		 * @param ftpClient
		 *            FTPClient对象
		 * @return 目录创建是否成功
		 * @throws IOException
		 */
		public UploadStatus CreateDirecroty(String remote, FTPClient ftpClient) throws IOException {
			UploadStatus status = UploadStatus.Create_Directory_Success;
			String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
			// 切换photo失败
			if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(new String(directory.getBytes("UTF-8"), "iso-8859-1"))) {
				// 如果远程目录不存在，则递归创建远程服务器目录
				int start = 0;
				int end = 0;
				if (directory.startsWith("/")) {
					start = 1;
				} else {
					start = 0;
				}
				end = directory.indexOf("/", start);
				while (true) {
					String subDirectory = new String(remote.substring(start, end).getBytes("UTF-8"), "iso-8859-1");
					if (!ftpClient.changeWorkingDirectory(subDirectory)) {
						if (ftpClient.makeDirectory(subDirectory)) {
							ftpClient.changeWorkingDirectory(subDirectory);
						} else {
							System.out.println("创建目录失败");
							return UploadStatus.Create_Directory_Fail;
						}
					}

					start = end + 1;
					end = directory.indexOf("/", start);

					// 检查所有目录是否创建完毕
					if (end <= start) {
						break;
					}
				}
			}
			return status;
		}

		/** */
		/**
		 * 上传文件到服务器,新上传和断点续传
		 * 
		 * @param remoteFile
		 *            远程文件名，在上传之前已经将服务器工作目录做了改变
		 * @param localFile
		 *            本地文件File句柄，绝对路径
		 * @param processStep
		 *            需要显示的处理进度步进值
		 * @param ftpClient
		 *            FTPClient引用
		 * @return
		 * @throws IOException
		 */
		public UploadStatus uploadFile(String remoteFile, File localFile, FTPClient ftpClient, long remoteSize) throws IOException {
			UploadStatus status;
			// 显示进度的上传
			long step = localFile.length() / 100;
			long process = 0;
			long localreadbytes = 0L;
			RandomAccessFile raf = new RandomAccessFile(localFile, "r");
			OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("UTF-8"), "iso-8859-1"));
			// 断点续传
			if (remoteSize > 0) {
				// 加的 假如推出去+上断点点
				if (0 == tmp) {
					Message msg1 = Message.obtain();
					tmp += remoteSize;
					msg1.what = 1;
					handler.sendMessage(msg1);
				}
				// 加完
				ftpClient.setRestartOffset(remoteSize);
				process = remoteSize / step;
				raf.seek(remoteSize);
				localreadbytes = remoteSize;
			}
			byte[] bytes = new byte[1024];
			int c;
			while ((c = raf.read(bytes)) != -1) {
				// 加的
				tmp += c;
				Message msg1 = Message.obtain();
				msg1.what = 1;
				msg1.arg1 = c;
				handler.sendMessage(msg1);
				// 加完
				if (null == out) {
					// System.out.println("out为空");
					// System.out.println("remoteFile:"+new
					// String(remoteFile.getBytes("UTF-8"), "iso-8859-1"));
					out = ftpClient.appendFileStream(new String(remoteFile.getBytes("UTF-8"), "iso-8859-1"));
				}
				out.write(bytes, 0, c);
				localreadbytes += c;
				if (localreadbytes / step != process) {
					process = localreadbytes / step;
					System.out.println("上传进度:" + process);
					// TODO 汇报上传状态
				}
			}
			out.flush();
			raf.close();
			out.close();
			boolean result = ftpClient.completePendingCommand();
			if (remoteSize > 0) {
				status = result ? UploadStatus.Upload_From_Break_Success : UploadStatus.Upload_From_Break_Failed;
			} else {
				status = result ? UploadStatus.Upload_New_File_Success : UploadStatus.Upload_New_File_Failed;
			}
			return status;
		}
	}

	// 准备上传录音
	private void prepareUploadRecord() {
		// 为空 实例化
		if (null == myFtp) {
			myFtp = new ContinueFTP();
		}
		// 暂停了
		if (isPause) {

		} else {
			new Thread() {
				@Override
				public void run() {
					try {
						// 创建连接
						// 兼容
						// boolean connect;
						// if(Cnt.appVersion==3){
						// connect = myFtp.connect(Cnt.IMS_RECORD_PHOTO_URL,
						// Cnt.ftpPort, Cnt.ftpName, Cnt.ftpPwd);
						// }else{
						// connect = myFtp.connect(Cnt.RECORD_PHOTO_URL,
						// Cnt.ftpPort, Cnt.ftpName, Cnt.ftpPwd);
						// }
						// 创建连接
						boolean connect = myFtp.connect(Cnt.RECORD_PHOTO_URL, Cnt.ftpPort, Cnt.ftpName, Cnt.ftpPwd);
						if (!connect) {
							Message msg = handler.obtainMessage();
							msg.what = 67;
							msg.arg1 = R.string.ftp_conect_fail;
							handler.sendMessage(msg);
							return;
						}
						// 创建目录
//						myFtp.ftpClient.makeDirectory(new String("pad".getBytes("UTF-8"), "iso-8859-1"));
//						myFtp.ftpClient.changeWorkingDirectory(new String("pad".getBytes("UTF-8"), "iso-8859-1"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 没暂停
					int sum = (int) ma.dbService.getSumAllIpsos();
					Message msg0 = Message.obtain();
					/**
					 * what=2000表示初始化文件的总个数, 还有所有文件的总大小
					 */
					msg0.what = 2000;
					msg0.arg2 = sum;// 所有文件大小之和
					handler.sendMessage(msg0);
					uploadRecordAndPhotoTask = new UploadRecordAndPhotoTask();
					uploadRecordAndPhotoTask.execute();
					super.run();
				}
			}.start();
		}
	}

	/**
	 * 图片录音上传线程
	 */

	class UploadRecordAndPhotoTask extends AsyncTask<Void, Void, Void> {
		boolean isHide = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (null == myFtp) {
				myFtp = new ContinueFTP();
			}
			// 上传文件
			isLast = false;
			ArrayList<UploadFeed> fs = ma.dbService.getRecordList(ma.userId);//取出要上传的并且feedId不是0的附件，
			// System.out.println("fs.size()" + fs.size());
			if (Util.isEmpty(fs)) {
				isLast = true;
				isHide = true;
				return null;
			} else {
				int count = fs.size();
				UploadFeed r = fs.get(0);
				String path = r.getPath();
				String fileName = r.getName();
				String surveyId = r.getSurveyId();
				String feedId=r.getFeedId();
				// 以前的
				try {
					myFtp.ftpClient.changeWorkingDirectory("/");
					// System.out.println("changeWorkingDirectory:"+changeWorkingDirectory);
					Log.e("MM", "************start**************");
					// long t = System.currentTimeMillis();
					// File f = new File(path, uploadFile);
					File f = new File(path, fileName);
					// 假如存在就是直接传，不存在就直接退出。传下一个。记录这个不存在的数据到 不存在表里面
					if (!f.exists()) {
						// 存储不存在的记录
						ma.dbService.saveName(r);
						// 把没有上传的文件先设置已经上传。不会死循环。
						ma.dbService.uploadMp3AndPngIpsos(fileName);
						return null;
					} else {
						if (f.length() == 0) {
							// 存储不存在的记录
							ma.dbService.saveName(r);
							ma.dbService.uploadMp3AndPngIpsos(fileName);
							return null;
						}
					}
					String[] fileStr = fileName.split("_");
					String sid = fileStr[0];
					String pid = fileStr[1];
					//ftp 命名规则 创建目录
					String ftpPath = File.separator + sid + File.separator + feedId + File.separator + fileName;
					Log.i("@@@", "feedId="+feedId);
					// path原目录,后面跟目录
					UploadStatus uploadStatus = myFtp.upload(path + File.separator + fileName, ftpPath);
					if (uploadStatus == UploadStatus.Upload_New_File_Success || uploadStatus == UploadStatus.Upload_From_Break_Success) {
						System.out.println("完成了");
						ma.dbService.uploadMp3AndPngIpsos(fileName);
					} else if (uploadStatus == UploadStatus.Create_Directory_Fail) {
						Message msg = handler.obtainMessage();
						msg.what = 67;
						msg.arg1 = R.string.dire_fail;
						handler.sendMessage(msg);
					} else if (uploadStatus == UploadStatus.Upload_New_File_Failed) {
						Message msg = handler.obtainMessage();
						msg.what = 67;
						msg.arg1 = R.string.file_failed;
						handler.sendMessage(msg);
					} else if (uploadStatus == UploadStatus.File_Exits) {
						Message msg = handler.obtainMessage();
						msg.what = 67;
						msg.arg1 = R.string.file_exist;
						handler.sendMessage(msg);
						ma.dbService.uploadMp3AndPngIpsos(fileName);
					} 
				} catch (Exception e) {
					// System.out.println("e.getMessage():"+e.getMessage());
					if (e instanceof SocketException) {
						isLast = true;
						System.out.println("暂停");
						// System.out.println("e.getMessage():"+e.getMessage());
						// 断开连接
						if (!isNet) {
							
						} else {
							try {
								myFtp.disconnect();
								Message msg = handler.obtainMessage();
								msg.what = 67;
								msg.arg1 = R.string.exp_net;
								handler.sendMessage(msg);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					} else if (e instanceof NullPointerException) {
						isLast = true;
						try {
							myFtp.disconnect();
							Message msg = handler.obtainMessage();
							msg.what = 67;
							msg.arg1 = R.string.no_reason_reupload_exit;
							handler.sendMessage(msg);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						// System.out.println("e.getMessage:"+e.getMessage());
						e.printStackTrace();
						isLast = true;
						handler.sendEmptyMessage(10000);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (isHide) {
				// logView.setVisibility(View.GONE);z1
				// 设置为能点击上传
				// isClickUpload = true;z1
				// 开始走asp程序上传xml
				dialog.dismiss();
				tmp = 0;
				sum = 0;
				tmp_1 = 0;
				sum_1 = 0;
				try {
					// 断开连接
					myFtp.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Toasts.makeText(UploadActivity.this,
				// R.string.media_file_upload_success_start,
				// Toast.LENGTH_LONG).show();z1
				//handler.sendEmptyMessageDelayed(4000, 200);
				Toasts.makeText(UploadActivity.this, R.string.finish_survey_others, Toast.LENGTH_SHORT).show();
				if (null != uploadAdapter) {
					uploadAdapter.notifyDataSetChanged();
				}
			}
			if (!isLast) {
				//不是最后一条则继续传
				System.out.println("发消息");
				handler.sendEmptyMessageDelayed(3, 200);
			} else {

			}
			super.onPostExecute(result);
		}

	}

	private void uploadFile(ArrayList<UploadFeed> xmlFs) {
		if(Util.isEmpty(xmlFs)){
			//完成了
			return;
		}
		UploadFeed feed = xmlFs.get(0);
		if (null == feed) {
			//继续下一个
			xmlFs.remove(0);
			uploadFile(xmlFs);
			return;
		}
		if (Util.isEmpty(ma.userId)) {
			ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(UploadActivity.this)) : (ma.cfg)).getString("UserId", "");
		}
		new UpLoadFileTask(feed,xmlFs).execute(ma.userId, MD5.Md5Pwd(ma.userPwd), feed.getSurveyId(), feed.getPath(), feed.getName(), Cnt.UPLOAD_URL);

	}

	public class UpLoadFileTask extends AsyncTask<String, String, HashMap<String, String>> {
		private UploadFeed feed;
		private ArrayList<UploadFeed> xmlFs;

		public UpLoadFileTask(UploadFeed f,ArrayList<UploadFeed> xmlFs) {
			feed = f;
			this.xmlFs=xmlFs;
		}

		protected HashMap<String, String> doInBackground(String... params) {// userId
																			// userPsd
																			// surveyId
																			// path
																			// filename
																			// URL
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("surveyId", params[2]);
			paramsMap.put(Cnt.USER_ID, params[0]);
			paramsMap.put(Cnt.USER_PWD, params[1]);
			FileUpLoad fupLoad = new FileUpLoad();
			InputStream is = fupLoad.upLoadBase64(params[5], params[3], params[4], paramsMap);
			//OutFile.outToFile(is, "feed");
			if (is == null) {
				Log.e("kjy", "UpLoadErrorX:" + feed.getName());
				return null;
			} else {
				
				return resolvData(is, feed);
			}
		}

		private HashMap<String, String> resolvData(InputStream is, UploadFeed feed) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			HashMap<String, String> rMap = new HashMap<String, String>();
			String state = "0";
			String fid = null;
			String rtp = null;
			// System.out.println("resolvData");
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(is);
				Element element = document.getDocumentElement();
				state = element.getElementsByTagName("S").item(0).getFirstChild().getNodeValue();
				rMap.put("S", state);
				// Log.e("DapDesk", "State:" + state);
				System.out.println("解析之后的状态--->" + state);
				if ("100".equals(state)) {
					fid = element.getElementsByTagName("FID").item(0).getFirstChild().getNodeValue();
					rtp = element.getElementsByTagName("RTP").item(0).getFirstChild().getNodeValue();
					// logic.updateFeedStatus(String.valueOf(feed.id), proId);
					// System.out.println("resolvData--->feed.uuid--->"+feed.getUuid());
					/**
					 * XML报完之后将uploaded=1--->uploaded=1,说明XML报过音频和图片没有报
					 */
					// ma.dbService.updateFeedStatusIpsos(feed.getUuid(), 9);
					// xml上传完成 等候上传照片录音
					Log.e("DapDesk", "FID:" + fid + " RTP:" + rtp);
				} else {
					is.close();
					return rMap;
				}
			} catch (Exception e) {
				Log.e("DapDesk", "Message:" + e.getMessage());
			}
			if (fid != null)
				rMap.put("FID", fid);
			if (rtp != null)
				rMap.put("RTP", rtp);
			return rMap;
		}

		protected void onPostExecute(HashMap<String, String> rMap) {
			if (rMap == null) {
				System.out.println("UpLoadFileTask:onPostExecute--->rMap == null");
				upLoadError(feed.getId(), 0, feed);
			} else {
				String stStr = rMap.get("S");
				int state = 0;
				if (stStr != null)
					state = Integer.parseInt(stStr);
				// System.out.println("XML上传后服务器返回的状态码--->"+state);
				if (state == 100) {
					String fid = rMap.get("FID");
					String rtp = rMap.get("RTP");
					upLoadSuccess(feed, fid, rtp);// 更新xml的数据库数据
					ma.dbService.upDateFeedId(feed);// 修改feedid
					//Log.i("@@@", "feed.getFeedId()=" + feed.getFeedId());
					if (null != uploadAdapter) {
						uploadAdapter.notifyDataSetChanged();
					}
				} else {
					// System.out.println("UpLoadFileTask--->onPostExecute--->state
					// != 100");
					upLoadError(feed.getId(), state, feed);
				}
			}

			xmlFs.remove(0);

			if (0 != xmlFs.size()) {
				uploadFile(xmlFs);
			} else {
				dismiss();
				// 打开进度条
				showDialog(DIALOG_DOWNLOAD_PROGRESS);
				// Toasts.makeText(UploadActivity.this,
				// R.string.finish_survey_starts, Toast.LENGTH_SHORT).show();
				long sizeSum = ma.dbService.getSum(ma.userId);
				Log.i("zrl1", "测试大小" + sizeSum);
				if (sizeSum > 0) {
					// 先传附件
					// boolean isEmpty = ma.dbService.isEmptyRecordList();
					// if (isEmpty) {
					// Toasts.makeText(UploadActivity.this,getResources().getString(R.string.),Toast.LENGTH_SHORT).show();
					// } else {
					// //
					// Toasts.makeText(UploadActivity.this,getResources().getString(R.string.finish_survey_starts),Toast.LENGTH_SHORT).show();
					prepareUploadRecord();
					// }
					if (null != uploadAdapter) {
						uploadAdapter.notifyDataSetChanged();
					}
				} else {
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				}
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (null != uploadAdapter) {
				uploadAdapter.notifyDataSetChanged();
			}
		}

	}

	/**
	 * 上报成功
	 * 
	 * @param pos
	 *            Survey的位置
	 * @param feed
	 *            上报UploadFeed
	 * @param fid
	 *            服务器返回的ID
	 * @param rtp
	 * @param btn
	 */
	private void upLoadSuccess(UploadFeed feed, String fid, String rtp) {
		if (null != feed) {
			feed.setFeedId(fid);
			feed.setReturnType(rtp);
			/**
			 * 上传状态设置为9,
			 */
			feed.setIsUploaded(9);
			ma.dbService.updateUploadFeedStatus(feed);
			ArrayList<Survey> allSurvey = uploadAdapter.getAllSurvey();
			// 计算拼串
			String mySid = "(";
			for (int i = 0; i < allSurvey.size(); i++) {
				Survey s = allSurvey.get(i);
				String surveyId = s.surveyId;
				// 最后一个时候
				if (i == allSurvey.size() - 1) {
					mySid += "_SurveyId=" + surveyId;
				} else {
					mySid += "_SurveyId=" + surveyId + " or ";
				}
			}
			mySid += ")";
			if (ma.dbService.feedUnUploadSurveysCountIpsos(mySid, ma.userId) == 0) {
				Toasts.makeText(UploadActivity.this, getResources().getString(R.string.finish_surveys), Toast.LENGTH_SHORT).show();
				new Thread() {
					public void run() {
						// 查询看数据库有没有没传的记录。有的话传，没的话就不传。
						ArrayList<MyRecoder> reList = ma.dbService.queryDeleteRecodeGroupBy();
						// 假如查到了。就去遍历查出feedId
						if (reList.size() > 0) {
							for (int r = 0; r < reList.size(); r++) {
								MyRecoder myRecoder = reList.get(r);
								HashMap<String, Object> params = new HashMap<String, Object>();
								params.put("surveyID", myRecoder.getSurveyId());
								params.put("feedID", myRecoder.getFeedId());
								params.put("count", myRecoder.getCount());
//								System.out.println("surveyID:"+myRecoder.getSurveyId()+",feedID:"+myRecoder.getFeedId()+",count:"+myRecoder.getCount());
								try {
									// 上传
									InputStream is = NetService.openUrl(Cnt.DELETE_XML, params, "GET");
									// 上传成功后，更改状态。
									DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
									String state = "0";
									DocumentBuilder builder = factory.newDocumentBuilder();
									Document document = builder.parse(is);
									Element element = document.getDocumentElement();
									state = element.getElementsByTagName("S").item(0).getFirstChild().getNodeValue();
									// Log.e("DapDesk", "State:" + state);
									System.out.println("解析之后的状态1--->" + state);
									if ("100".equals(state)) {
										ma.dbService.updateRecodeEnableByUid(myRecoder.getUuid());
									} else {
										// 98没成功
									}
//									if(r==reList.size()-1){
//										handler.sendEmptyMessage(999);
//									}
								} catch (Exception e) {
									Log.e("DapDesk", "Message:" + e.getMessage());
								}
							}
						}
					};
				}.start();
				
			}
		}
	}

	public void upLoadError(long id, int state, UploadFeed feed) {
		if (0 == state) {
			Toasts.makeText(getApplicationContext(), R.string.err_net, Toast.LENGTH_LONG).show();
		} else if (95 == state) {
			ma.dbService.giveUpFeed(feed.getUuid(), feed.getSurveyId());
			if (null != uploadAdapter) {
				uploadAdapter.notifyDataSetChanged();
			}
			return;
		} else {
			Toasts.makeText(getApplicationContext(), R.string.err_upass, Toast.LENGTH_LONG).show();
		}

		// boolean b = ma.dbService.feedAnswerIsHave(feed.getUuid());
		// File file = new File(feed.getPath(), feed.getName());
		/**
		 * 假如是空问卷
		 */
		// if ( !file.exists()) {
		// ma.dbService.updateUploadFeedNoAccessState(feed.getId());
		// }
		if (null != uploadAdapter) {
			uploadAdapter.notifyDataSetChanged();
		}
	}

	// 上传结束

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog dialog = null;
	private boolean isPause;// false是没暂停
	private Button button;
	private boolean isNet = true;// 判断是断网 还是暂停 true是默认状态,false是暂停

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			dialog = new ProgressDialog(this);
			dialog.setMessage("uploading…");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.show();
			button = new Button(this);
			// zz添加
			button.setBackgroundResource(android.R.drawable.dialog_frame);
			button.setText(this.getResources().getString(R.string.pause));
			button.setTextColor(Color.WHITE);
			// zz添加
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Button btn = (Button) v;
					if (isPause) {
						isNet = true;// 重新默认断网
						// 暂停情况变继续
						if (!NetUtil.checkNet(UploadActivity.this)) {
							Toasts.makeText(UploadActivity.this, R.string.exp_net, Toast.LENGTH_SHORT).show();
							dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
						} else {
							btn.setText(UploadActivity.this.getResources().getString(R.string.pause));
							reStartUploadTask();
							dialog.setCancelable(false);
						}
					} else {
						isNet = false;// 不是断网
						if (!NetUtil.checkNet(UploadActivity.this)) {
							Toasts.makeText(UploadActivity.this, R.string.exp_net, Toast.LENGTH_SHORT).show();
							dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
						} else {
							btn.setText(UploadActivity.this.getResources().getString(R.string._continue));
							stopUploadTask();
							dialog.setCancelable(true);
						}
						// 没暂停情况变暂停
					}
					isPause = !isPause;
				}
			});
			dialog.addContentView(button, params);
			return dialog;
		default:
			return null;
		}
	}

	private void stopUploadTask() {
//		System.out.println("停止");
		if (uploadRecordAndPhotoTask != null) {
			// tmp = 0;
			uploadRecordAndPhotoTask.cancel(true);
			uploadRecordAndPhotoTask = null;
			// 断开连接
			try {
				myFtp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("MM", "AAAAAAAAAAAAAAAAAAAAAAAA");
		} else {
			Log.i("MM", "AAAAAAAAAAAAAAAAAAAAAAAA");
		}
	}

	private void reStartUploadTask() {
		if (uploadRecordAndPhotoTask != null) {
			Log.i("MM", "CCCCCCCCCCCCCCCCCCCCCCC");
		} else {
			Log.i("MM", "DDDDDDDDDDDDDDDDDDDD");
		}
		new Thread() {
			public void run() {
				boolean connect;
				try {
					// 创建连接
					connect = myFtp.connect(Cnt.RECORD_PHOTO_URL, Cnt.ftpPort, Cnt.ftpName, Cnt.ftpPwd);
					if (!connect) {
						Message msg = handler.obtainMessage();
						msg.what = 68;
						msg.arg1 = R.string.ftp_conect_fail;
						handler.sendMessage(msg);
						return;
					}
					uploadRecordAndPhotoTask = new UploadRecordAndPhotoTask();
					uploadRecordAndPhotoTask.execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

}
