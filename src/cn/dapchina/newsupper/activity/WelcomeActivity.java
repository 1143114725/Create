package cn.dapchina.newsupper.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Application;
import cn.dapchina.newsupper.bean.Call;
import cn.dapchina.newsupper.bean.HttpBean;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.SurveyQuestion;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.service.XmlService;
import cn.dapchina.newsupper.util.BaseLog;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.GsonUtil;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;
import cn.dapchina.newsupper.view.Toasts;
import cn.dapchina.newsupper.xhttp.Xutils;
import cn.dapchina.newsupper.xhttp.Xutils.XCallBack2;

public class WelcomeActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_dap;
	private Button btn_visit;
	private int width;
	private int height;
	private GestureDetector mGestureDetector;
	private MyApp ma;// 大树 这个类 是移植过来的
	private Config config;// 大树 这个类 是移植过来的
	private String language;// 大树
	private String isLogin;// 大树
	private boolean isDownSuccess; // 大树 是否下载成功

	// 大树 调用 handler 异步 一下
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 123:
				if (1 == msg.arg1) {
					Toasts.makeText(WelcomeActivity.this, R.string.exp_net_no_down, Toast.LENGTH_LONG).show();
				}
				// 这添加判断是不是第一次登录,
				// 判断第一次登录,
				if ("".equals(isLogin)) {
					// 旧新提示
//					Intent it = new Intent(WelcomeActivity.this, ShowNoticeActivity.class);
//					WelcomeActivity.this.startActivity(it);
					if (isDownSuccess) {
						config.putString("isLogin", "true");
					}
				}
				dismiss();
				// app上线更新
				if (NetUtil.checkNet(WelcomeActivity.this)) {
					show();
					// 直接请求是否更新
					MainService.newTask(new Task(TaskType.TS_LOGIN_GET_APP, null));
				}
				break;
			case 456:
				Survey s = (Survey) msg.obj;
				new AuthorDownloadTask(s).execute();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		iv_dap = (ImageView) findViewById(R.id.iv_dap);
		btn_visit = (Button) findViewById(R.id.btnVisit);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		LayoutParams params = new LayoutParams((int) (width / 3.3), (int) (height / 18));
		iv_dap.setLayoutParams(params);
		// 旧新提示
		iv_dap.setOnClickListener(this);
		LayoutParams params1 = new LayoutParams((int) (width / 2.2), height / 11);
		btn_visit.setLayoutParams(params1);
		// 监听手势事件
		mGestureDetector = new GestureDetector(this, onGestureListener);
		btn_visit.setOnClickListener(this);

		// 大树 以下 下载问卷
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		config = new Config(WelcomeActivity.this);
		// 每次从头登录又变成离线登录了
		config.putBoolean(Cnt.LOGIN_MODE, true);
		// 获得什么语言
		language = this.getResources().getConfiguration().locale.getLanguage();
		// app上线更新
		getVersion();
		show();
		// 第一次加载页面 ，请求APP更新 的地址 做一个兼容 ，首先走一个免费地址 目的是获取不同版本的 APP请求更新地址
		Cnt.changeNewURL(true, freeIp, freeIp, payIp);
		downloadFree();
		// 大树 以上
	}

	// 大树 下载免费公共问卷
	private void downloadFree() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				isLogin = config.getString("isLogin", "");
				if ("".equals(isLogin)) {
					if (NetUtil.checkNet(WelcomeActivity.this)) {
						try {
							HashMap<String, Object> hm = new HashMap<String, Object>();
							// 假如中文的，否则其他语言的
							if ("zh".equals(language)) {
								hm.put(Cnt.USER_ID, "test01");
								hm.put(Cnt.USER_PWD, "test01");
							} else {
								hm.put(Cnt.USER_ID, "test02");
								hm.put(Cnt.USER_PWD, "test02");
							}
							InputStream inStream = NetService.openUrl(Cnt.DOWN_FREE_SURVEY_URL, hm, "GET");
							XmlService xs = new XmlService();
							ArrayList<Survey> surveyList = xs.getAllSurvey(inStream);
							for (Survey s : surveyList) {

								String exist = ma.dbService.surveyFreeExsit(s.surveyId);
								s.isTest = 1;
								boolean b = false;
								if (Util.isEmpty(exist)) {
									System.out.println("添加");
									b = ma.dbService.addSurvey(s);
								} else {
									System.out.println("更新");
									// 问卷提醒状态
									b = ma.dbService.updateSurvey(s, 0);
								}
								if (b) {
									// 发给456
									handler.obtainMessage(456, s).sendToTarget();
								} else {
									handler.sendEmptyMessageDelayed(123, 500);
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// 没有网络
						handler.sendMessageDelayed(handler.obtainMessage(123, 1, 1), 1000);
					}
				} else {
					// 并非首次加载
					System.out.println("不去下载");
					handler.sendEmptyMessageDelayed(123, 500);
				}
			}
		}).start();
	}

	// 大树 下载公共问卷 任务 在这里
	private class AuthorDownloadTask extends AsyncTask<Void, Integer, Boolean> {
		private Survey s;
		private SurveyQuestion sq;

		public AuthorDownloadTask(Survey survey) {
			this.s = survey;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean yes = false;
			try {
				HttpBean hb = NetService.obtainHttpBean(s.downloadUrl, null, "GET");
				if (200 == hb.code) {
					File file = new File(Util.getSurveySaveFilePath(WelcomeActivity.this), s.surveyId + ".zip");
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[2048];
					int len = 0;
					while ((len = hb.inStream.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.flush();
					fos.close();
					hb.inStream.close();
					yes = Util.decompress(file.getAbsolutePath(), Util.getSurveyFilePath(WelcomeActivity.this, s.surveyId), s.surveyId, null);
					if (yes) {
						file.delete();
						/**
						 * 假如是原生模式访问则解析原生XML问卷
						 */
						if (1 == this.s.visitMode) {
							
							String xml = Util.getSurveyXML(WelcomeActivity.this, s.surveyId);
							
							FileInputStream inStream = new FileInputStream(xml);
							if (null != inStream) {
								sq = XmlUtil.getSurveyQuestion(inStream, new Call() {
									
									@Override
									public void updateProgress(int curr, int total) {
										
										publishProgress(curr, total);
									}
								});
								ArrayList<Question> qs = sq.getQuestions();
								if (!Util.isEmpty(qs)) {
									for (int i = 0; i < qs.size(); i++) {
										Question q = qs.get(i);
										q.surveyId = s.surveyId;
										if (-1 != q.qOrder) {
											boolean qt = ma.dbService.isQuestionExist(s.surveyId, q.qIndex);
											if (qt) {
												boolean u = ma.dbService.updateQuestion(q);
												if (u) {
													System.out.println("" + q.qIndex + "更新成功.");
												}
											} else {
												System.out.println();
												boolean b = ma.dbService.addQuestion(q);
												if (b) {
													System.out.println("" + q.qIndex + "插入成功.");
												}
											}
										}
									}
								}
							}
						}
					}
				}
				// yes = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return yes;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			isDownSuccess = result;
			if (result) {
				ma.dbService.surveyDownloaded(s.surveyId, (null == sq) ? -1 : sq.getEligible(), "");
				handler.sendEmptyMessageDelayed(123, 500);
			} else {
				// 否则不计数
			}
			dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	};

	OnGestureListener onGestureListener = new OnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			System.out.println(e1.getX());
			System.out.println(e2.getX());
			if (e1.getX() - e2.getX() > 400) {
				// 去Intent操作
				Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
				return true;
			}
			return false;
		}

	};

	// 手势触发
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnVisit:			
			 //去Intent操作
			Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zzright, R.anim.zzleft);
			finish();
			break;
		case R.id.iv_dap:
			// 旧新提示
//			Intent it = new Intent(WelcomeActivity.this, ShowNoticeActivity.class);
//			WelcomeActivity.this.startActivity(it);
			break;
		default:
			break;
		}
	}

	// 大树 销毁方法
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ma.remove(this);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	//app上线更新
		private Application app = null;
		private String mVersion;// 版本号
		/**
		 * 获得版本号
		 * app上线更新
		 */
		private void getVersion() {
			try {
				PackageManager pm = getPackageManager();
				PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
				mVersion = pi.versionName;
			} catch (Exception e) {
				e.printStackTrace();
				mVersion = "";
			}
		}
		public Dialog mDialog;
		private ProgressBar progressBar = null;
		private TextView progressInfo = null;
		private TextView progressDesc = null;
		private Button btnUpdate;
		private File apkFile = null;
		UpdateTask updateTask;
		/**
		 * 更新dialog按钮
		 * app上线更新
		 */
		private void showNewDialog() {
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();
			
			mDialog = new Dialog(this, R.style.question_ds);
			mDialog.setContentView(R.layout.update_dialog);
			LinearLayout progressView=(LinearLayout) mDialog.findViewById(R.id.progress_view);
			FrameLayout.LayoutParams params=new FrameLayout.LayoutParams((int)(width/1.3),FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity=Gravity.CENTER;
			progressView.setLayoutParams(params);
			progressBar = (ProgressBar) mDialog.findViewById(R.id.progressBar);
			progressInfo = (TextView) mDialog.findViewById(R.id.progressInfo);
			progressDesc = (TextView) mDialog.findViewById(R.id.progressDesc);
			btnUpdate = (Button) mDialog.findViewById(R.id.update_ok_btn);
			btnUpdate.setOnClickListener(new CstOnClickListener());
			TextView content=(TextView) mDialog.findViewById(R.id.content);
			String strContent = app.getContent();
			if(!Util.isEmpty(strContent)){
				String replace = strContent.replace("\\n", "\n");
				content.setText(replace);
				content.setVisibility(View.VISIBLE);
			}
			TextView notice=(TextView) mDialog.findViewById(R.id.notice);
			String strNotice = app.getNotice();
			if(!Util.isEmpty(strNotice)){
				String replace = strNotice.replace("\\n", "\n");
				notice.setText(replace);
				notice.setVisibility(View.VISIBLE);
			}
			Button btnCancel = (Button) mDialog.findViewById(R.id.update_cancel_btn);
			btnCancel.setOnClickListener(new CstOnClickListener());
			mDialog.setCancelable(false);
			mDialog.show();
			progressBar.setProgress(0);
			progressDesc.setText(getResources().getString(R.string.version_info, app.getVersion()));
			progressInfo.setText("0/0");
		}
		
		private final class CstOnClickListener implements OnClickListener {
			public CstOnClickListener() {

			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.update_ok_btn:
					if (null == updateTask) {
						updateTask = new UpdateTask();
					}
					if (!AsyncTask.Status.RUNNING.equals(updateTask.getStatus())) {
						updateTask.execute();
					}
					break;

				case R.id.update_cancel_btn:
					if (null == updateTask) {
						/**
						 * 不是在下载状态
						 */
						mDialog.dismiss();
					} else {
						if (null != updateTask) {
							/**
							 * 正在下载的状态
							 */
							// 正在下载的
							updateTask.cancel(true);
							updateTask = null;
						}
						if (null != apkFile && apkFile.exists()) {
							apkFile.delete();
						}
						mDialog.dismiss();
					}
					break;
				}
			}

		}

		private final class UpdateTask extends AsyncTask<Void, Integer, Boolean> {
			public UpdateTask() {

			}

			@Override
			protected Boolean doInBackground(Void... params) {

				try {
					HttpBean hb = NetService.obtainHttpBean(app.getPath(), null, "GET");
					if (200 == hb.code) {
						// substring(path.lastIndexOf("/") + 1);
						apkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), app.getPath().substring(app.getPath().lastIndexOf("/") + 1));
						if (apkFile.exists()) {
							apkFile.delete();
						}
						if (!apkFile.getParentFile().exists()) {
							apkFile.getParentFile().mkdirs();
						}
						FileOutputStream fos = new FileOutputStream(apkFile);
						byte[] buffer = new byte[1024];
						int len = 0;
						int size = 0;
						while ((len = hb.inStream.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
							size += len;
							publishProgress(size, hb.contentLength);
						}
						fos.flush();
						fos.close();
						hb.inStream.close();
						return true;
					} else {
						return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				mDialog.dismiss();
				if (result) {
					app.openFile(WelcomeActivity.this, apkFile);
					if (null != updateTask) {
						/**
						 * 正在下载的状态
						 */
						// 正在下载的
						updateTask.cancel(true);
						updateTask = null;
					}
				} else {

				}
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				if(isCancelled()) {
					return; 
				}
				progressInfo.setText(values[0] + "/" + values[1]);
				if (0 < values[1]) {
					progressBar.setProgress((int) (values[0] * 100) / values[1]);
				}
			}
		}
		
		@Override
		public void refresh(Object... param) {
			//app上线更新
			switch ((Integer) param[0]) {
			case TaskType.TS_LOGIN_GET_APP:
				app = (Application) param[1];
				if (!Util.isEmpty(mVersion)) {
					if (null != app) {
						/**
						 * 获取版本号名称
						 */
						double version = Double.parseDouble(mVersion);
						System.out.println("cccc:"+version+",内存版本号"+app.getVersion());
						if (version < app.getVersion()) {
							/**
							 * 服务器的版本比本地版本大, 则更新.
							 */
							if (Util.isEmpty(app.getPath())) {
								System.out.println("没有更新");
							} else {
								System.out.println("更新");
								// 更新
								showNewDialog();
							}
					}
				} else {
				}
			} else {
			}
			dismiss();
			break;
			}
			dismiss();
		}

	private int time = 0;// 次数
	private long myTime = 0;// 时间

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 按两次并且间隔小于5秒才能退出
			if (0 == time) {
				Toasts.makeText(WelcomeActivity.this, R.string.once_again, Toast.LENGTH_LONG).show();
				myTime = System.currentTimeMillis();
				time = 1;
				return false;
			} else if (1 == time) {
				if (System.currentTimeMillis() - myTime > 5000) {
					time = 1;
					myTime = System.currentTimeMillis();
					Toasts.makeText(WelcomeActivity.this, R.string.once_again, Toast.LENGTH_LONG).show();
					return false;
				} else {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}

		}
		return super.onKeyDown(keyCode, event);
	}
}
