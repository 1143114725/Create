package cn.dapchina.newsupper.activity;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Application;
import cn.dapchina.newsupper.bean.HttpBean;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;

public class SettingActivity extends BaseActivity implements OnClickListener {

	public Dialog mDialog;
	private ProgressBar progressBar = null;
	private TextView progressInfo = null;
	private TextView progressDesc = null;
	private Button btnUpdate;
	private File apkFile = null;
	UpdateTask updateTask;

	ImageView setting_left_iv;
	private RelativeLayout rl_setting_monitor;// 检测数据
	private RelativeLayout rlLandPort, rlSurveySize, rlCheckUpdate, total_net, down_net, upload_net, inner_size, sdcard_size, clear_data_tv, 
	export_tv,change_save_tv,setting_ip_path,change_mapopen;
	private int screen; // 横竖屏
	private boolean mapMonitor; //地图监控开关
	private MyApp ma;
	private TextView setting_landscape_tv, setting_vetical_tv;
	// 问卷字号动态设置
	private String surveySize;
	private TextView setting_result, setting_current_version, setting_countkb_tv, setting_downkb_tv, setting_uploadkb_tv,setting_mapscape_tv;
	private TextView setting_ramkb_tv, setting_ramcount_tv, setting_sdcardkb_tv, setting_sdcardcount_tv,setting_cata_address_tv,setting_realresult_tv,setting_mapopen_tv;
	private Application app = null;
	ArrayList<UploadFeed> fs;
	ArrayList<UploadFeed> fsRecords;
	private EditText setting_realresult_et;
	private boolean isEdit=false;//是不是编辑状态
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				/**
				 * 假如fs的下标小于fs的大小 最大下标index=fs.size()-1;
				 */
				if (0 < fs.size()) {
					new CopyTask().execute();
				}
				break;

			case 2:
				if (!Util.isEmpty(fsRecords)) {
					new CopyRecordTask().execute();
				}
				break;
			}
		}

	};
	
	@Override
	protected void onResume() {
		super.onResume();
		fsRecords = ma.dbService.getAllInnerMp3OrPng();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.setting_activity);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		if (null == ma.cfg) {
			ma.cfg = new Config(SettingActivity.this);
		}
		initView();
		Intent intent = getIntent();
		setResult(11, intent);
	}

	private void initView() {
		setting_left_iv = (ImageView) findViewById(R.id.setting_left_iv);
		setting_left_iv.setOnClickListener(this);
		rl_setting_monitor = (RelativeLayout) findViewById(R.id.rl_setting_monitor);
		rl_setting_monitor.setOnClickListener(this);
		// 横竖屏
		rlLandPort = (RelativeLayout) findViewById(R.id.change_land_port);
		rlLandPort.setOnClickListener(this);
		setting_landscape_tv = (TextView) findViewById(R.id.setting_landscape_tv);
		setting_vetical_tv = (TextView) findViewById(R.id.setting_vetical_tv);
		// 地图监控开关
		change_mapopen=(RelativeLayout) findViewById(R.id.change_map_port);
		change_mapopen.setOnClickListener(this);
		setting_mapscape_tv = (TextView) findViewById(R.id.setting_mapscape_tv);
		setting_mapopen_tv = (TextView) findViewById(R.id.setting_mapopen_tv);
		/**
		 * 0是纵向 1是横向
		 */
		screen = ma.cfg.getInt("ScreenOrientation", 0);
		if (0 == screen) {
			setting_landscape_tv.setText(this.getString(R.string.land_port_change_p));
			setting_vetical_tv.setText(this.getString(R.string.verital));
		} else {
			setting_landscape_tv.setText(this.getString(R.string.land_port_change_l));
			setting_vetical_tv.setText(this.getString(R.string.prot));
		}
		/**
		 * 地图监控
		 * true是开启,false是关闭
		 * 默认为关闭
		 * */
		mapMonitor = ma.cfg.getBoolean("mapMonitor", false);
		if (mapMonitor) {
			setting_mapscape_tv.setText(this.getString(R.string.survey_mapspace));
			setting_mapopen_tv.setText(this.getString(R.string.survey_mapopen));
		} else {
			setting_mapscape_tv.setText(this.getString(R.string.survey_mapspace));
			setting_mapopen_tv.setText(this.getString(R.string.survey_mapclose));
		}
		// 字体大小
		rlSurveySize = (RelativeLayout) findViewById(R.id.set_survey_text_size);
		rlSurveySize.setOnClickListener(this);
		setting_result = (TextView) findViewById(R.id.setting_result);
		surveySize = ma.cfg.getString("surveySize", "16");
		int intSurveySize = 0;
		try {
			intSurveySize = Integer.parseInt(surveySize);
		} catch (Exception e) {
			intSurveySize = 16;
		}
		if (intSurveySize == 12) {
			setting_result.setText(this.getString(R.string.small));
		} else if (intSurveySize == 16) {
			setting_result.setText(this.getString(R.string.middle));
		} else if (intSurveySize == 24) {
			setting_result.setText(this.getString(R.string.big));
		}
		// 检测更新
		rlCheckUpdate = (RelativeLayout) findViewById(R.id.check_update_tv);
		rlCheckUpdate.setOnClickListener(this);
		setting_current_version = (TextView) findViewById(R.id.setting_current_version);
		setting_current_version.setText(this.getString(R.string.version, getVersion()));
		// 总耗流量
		// 下载流量
		// 上传流量
		total_net = (RelativeLayout) findViewById(R.id.total_net);
		down_net = (RelativeLayout) findViewById(R.id.down_net);
		upload_net = (RelativeLayout) findViewById(R.id.upload_net);
		total_net.setOnClickListener(this);
		down_net.setOnClickListener(this);
		upload_net.setOnClickListener(this);
		setting_countkb_tv = (TextView) findViewById(R.id.setting_countkb_tv);
		setting_downkb_tv = (TextView) findViewById(R.id.setting_downkb_tv);
		setting_uploadkb_tv = (TextView) findViewById(R.id.setting_uploadkb_tv);
		// 内存 sdcard
		inner_size = (RelativeLayout) findViewById(R.id.inner_size);
		sdcard_size = (RelativeLayout) findViewById(R.id.sdcard_size);
		inner_size.setOnClickListener(this);
		sdcard_size.setOnClickListener(this);
		setting_ramkb_tv = (TextView) findViewById(R.id.setting_ramkb_tv);
		setting_ramcount_tv = (TextView) findViewById(R.id.setting_ramcount_tv);
		setting_sdcardkb_tv = (TextView) findViewById(R.id.setting_sdcardkb_tv);
		setting_sdcardcount_tv = (TextView) findViewById(R.id.setting_sdcardcount_tv);
		// 清除缓存
		clear_data_tv = (RelativeLayout) findViewById(R.id.clear_data_tv);
		clear_data_tv.setOnClickListener(this);
		// 导出数据
		export_tv = (RelativeLayout) findViewById(R.id.export_tv);
		export_tv.setOnClickListener(this);
		// 改变存储路径
		change_save_tv= (RelativeLayout) findViewById(R.id.change_save_tv);
		change_save_tv.setOnClickListener(this);
		setting_cata_address_tv=(TextView) findViewById(R.id.setting_cata_address_tv);
		if (ma.cfg.getBoolean("save_inner", false)) {
			/**
			 * 保存在内部存储卡上
			 */
			setting_cata_address_tv.setText(getString(R.string.change_save_inner));
		} else {
			/**
			 * 保存在sdcard上
			 */
			setting_cata_address_tv.setText(getString(R.string.change_save_sdcard));
		}
		//设置域名
		setting_ip_path= (RelativeLayout) findViewById(R.id.setting_ip_path);
		setting_ip_path.setOnClickListener(this);
		//假如非访问专家
		if(1!=Cnt.appVersion){
			setting_ip_path.setVisibility(View.GONE);
		}
		setting_realresult_tv=(TextView) findViewById(R.id.setting_realresult_tv);
		String payIp = ma.cfg.getString("payIp", "");
		if (Util.isEmpty(payIp)) {
			setting_realresult_tv.setText(SettingActivity.this.getString(R.string.real_pay_ip));
		} else {
			setting_realresult_tv.setText(payIp);
		}
		setting_realresult_et=(EditText) findViewById(R.id.setting_realresult_et);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		setting_realresult_et.setMaxWidth(width/2);
		setting_realresult_tv.setMaxWidth(width/2);
	}

	/**
	 * 获得版本号
	 */
	private String getVersion() {
		String mVersion = "";
		try {
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			mVersion = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			mVersion = "";
		}
		return mVersion;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SettingActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_left_iv:
			SettingActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		// 监测数据
		case R.id.rl_setting_monitor:
			Intent intent = new Intent(SettingActivity.this, DataMonitorActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zzright, R.anim.zzleft);
			break;
		// 改变横屏竖屏
		case R.id.change_land_port:
			if (1 == screen) {
				screen = 0;
				setting_landscape_tv.setText(this.getString(R.string.land_port_change_p));
				setting_vetical_tv.setText(this.getString(R.string.verital));
			} else if (0 == screen) {
				screen = 1;
				setting_landscape_tv.setText(this.getString(R.string.land_port_change_l));
				setting_vetical_tv.setText(this.getString(R.string.prot));
			}
			ma.cfg.putInt("ScreenOrientation", screen);
			break;
		//地图监控开关
		case R.id.change_map_port:
			if (mapMonitor) {
				mapMonitor = false;
				setting_mapopen_tv.setText(this.getString(R.string.survey_mapclose));
			} else{
				mapMonitor = true;
				setting_mapopen_tv.setText(this.getString(R.string.survey_mapopen));
			}
			ma.cfg.putBoolean("mapMonitor", mapMonitor);
			break;
		// 字体大小
		case R.id.set_survey_text_size:
			surveySize = ma.cfg.getString("surveySize", "16");
			int intSurveySize = 0;
			try {
				intSurveySize = Integer.parseInt(surveySize);
			} catch (Exception e) {
				intSurveySize = 16;
			}
			if (intSurveySize == 12) {
				setting_result.setText(this.getString(R.string.middle));
				ma.cfg.putString("surveySize", "16");
			} else if (intSurveySize == 16) {
				setting_result.setText(this.getString(R.string.big));
				ma.cfg.putString("surveySize", "24");
			} else if (intSurveySize == 24) {
				setting_result.setText(this.getString(R.string.small));
				ma.cfg.putString("surveySize", "12");
			}
			break;
		/**
		 * 检查升级
		 */
		case R.id.check_update_tv:
			show();
			MainService.newTask(new Task(TaskType.TS_GET_APP, null));
			break;
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
		case R.id.total_net:
		case R.id.down_net:
		case R.id.upload_net:
			refreshNetInfo();
			break;
		case R.id.inner_size:
		case R.id.sdcard_size:
			refreshMemoryInfo();
			break;
		case R.id.clear_data_tv:
			ArrayList<String> list = ma.dbService.getUploadedFeed();
			if (Util.isEmpty(list)) {
				Toasts.makeText(SettingActivity.this, R.string.no_cache, Toast.LENGTH_LONG).show();
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				ma.dbService.clearAnswers(list.get(i));
			}
			refreshMemoryInfo();
			Toasts.makeText(SettingActivity.this, R.string.clear_finish, Toast.LENGTH_LONG).show();
			break;
		case R.id.export_tv:
			/**
			 * 此处处理导出XML功能
			 */
			if (Util.isEmpty(ma.userId)) {
				ma.userId = ma.cfg.getString("UserId", "");
			}

			/**
			 * 用户id为空
			 */
			if (Util.isEmpty(ma.userId)) {
				Toasts.makeText(getApplicationContext(), R.string.userid_null, Toast.LENGTH_LONG).show();
				return;
			}

			/**
			 * 获取该用户对应的所有xml文件
			 */
			fs = ma.dbService.getAllExportXml(ma.userId);
			if (0 == fs.size()) {
				Toasts.makeText(getApplicationContext(), R.string.fs_null, Toast.LENGTH_LONG).show();
				return;
			}
			/**
			 * 获取sdcard的路径
			 */
			show();
			handler.obtainMessage(1).sendToTarget();
			break;
		case R.id.change_save_tv:
			if (ma.cfg.getBoolean("save_inner", false)) {
				/**
				 * 假如是在内部则置为外部
				 */
				ma.cfg.putBoolean("save_inner", false);
			} else {
				/**
				 * 在外部则置为内部
				 */
				ma.cfg.putBoolean("save_inner", true);
			}

			if (ma.cfg.getBoolean("save_inner", false)) {
				/**
				 * 保存在内部存储卡上
				 */
				setting_cata_address_tv.setText(getString(R.string.change_save_inner));
			} else {
				/**
				 * 保存在sdcard上
				 */
				setting_cata_address_tv.setText(getString(R.string.change_save_sdcard));
				/**
				 * 将文件从内部存储空间移动到SDCARD
				 */
				// 1.首先要查询出MP3、PNG文件---->fsRecords
				if (!Util.isEmpty(fsRecords)) {
					show();
					handler.obtainMessage(2).sendToTarget();
				}
			}

			break;
		case R.id.setting_ip_path:
			if(!isEdit){
				setting_realresult_et.setVisibility(View.VISIBLE);
				setting_realresult_tv.setVisibility(View.GONE);
				String payIp = ma.cfg.getString("payIp", "");
				if (Util.isEmpty(payIp)) {
					setting_realresult_et.setText(SettingActivity.this.getString(R.string.real_pay_ip));
				} else {
					setting_realresult_et.setText(payIp);
				}
			}else{
				setting_realresult_et.setVisibility(View.GONE);
				setting_realresult_tv.setVisibility(View.VISIBLE);
				String payIp = setting_realresult_et.getText().toString();
				if(!Util.isEmpty(payIp)){
					ma.cfg.putString("payIp", payIp);
				}
				if (Util.isEmpty(payIp)) {
					setting_realresult_tv.setText(SettingActivity.this.getString(R.string.real_pay_ip));
				} else {
					setting_realresult_tv.setText(payIp);
				}
			}
			isEdit=!isEdit;
			break;
		default:
			break;
		}
	}

	private void refreshMemoryInfo() {
		double[] sdcard = Util.readSDCard();
		double[] sys = Util.readSystem();
		DecimalFormat df = new DecimalFormat(".####");
		setting_ramkb_tv.setText(df.format(sys[1]) + "M/");
		setting_ramcount_tv.setText(df.format(sys[0]) + "M");
		//  地址变更   单位换算 为 G   
		setting_sdcardkb_tv.setText(df.format(sdcard[1]) + "G/");  
		setting_sdcardcount_tv.setText(df.format(sdcard[0]) + "G");
	}
	

	private void refreshNetInfo() {
		DecimalFormat df = new DecimalFormat(".##");
		try {
			setting_countkb_tv.setText(df.format((TrafficStats.getUidTxBytes(android.os.Process.myUid()) + TrafficStats.getUidRxBytes(android.os.Process.myUid())) / 1024d) + "KB");
			setting_downkb_tv.setText(df.format(TrafficStats.getUidRxBytes(android.os.Process.myUid()) / 1024d) + "KB");
			setting_uploadkb_tv.setText(df.format(TrafficStats.getUidTxBytes(android.os.Process.myUid()) / 1024d) + "KB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		refreshMemoryInfo();  
		refreshNetInfo();
	}

	@Override
	public void refresh(Object... param) {
		switch ((Integer) param[0]) {
		case TaskType.TS_GET_APP:
			app = (Application) param[1];
			if (null != app) {
				PackageInfo pi = null;
				try {
					/**
					 * 获取包信息
					 */
					pi = app.getPackageInfo(SettingActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null != pi) {
					/**
					 * 获取版本号名称
					 */
					double version = Double.parseDouble(pi.versionName);
					System.out.println("服务器版本:" + version + "本版本:" + app.getVersion());
					if (version <= app.getVersion()) {
						/**
						 * 服务器的版本比本地版本大, 则更新.
						 */
						if (Util.isEmpty(app.getPath())) {
							Toasts.makeText(this, R.string.fail_update, Toast.LENGTH_LONG).show();
						} else {
							showNewDialog();
						}
					} else {
						Toasts.makeText(SettingActivity.this, R.string.best_new, Toast.LENGTH_SHORT).show();
					}
				} else {
					Toasts.makeText(this, R.string.fail_update, Toast.LENGTH_LONG).show();
				}
			} else {
				Toasts.makeText(this, R.string.fail_update, Toast.LENGTH_LONG).show();
			}
			break;
		}
		dismiss();
	}

	/**
	 * 更新dialog按钮
	 */
	private void showNewDialog() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		mDialog = new Dialog(this, R.style.question_ds);
		mDialog.setContentView(R.layout.update_dialog);
		LinearLayout progressView = (LinearLayout) mDialog.findViewById(R.id.progress_view);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (width / 1.3), FrameLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		progressView.setLayoutParams(params);
		progressBar = (ProgressBar) mDialog.findViewById(R.id.progressBar);
		progressInfo = (TextView) mDialog.findViewById(R.id.progressInfo);
		progressDesc = (TextView) mDialog.findViewById(R.id.progressDesc);
		btnUpdate = (Button) mDialog.findViewById(R.id.update_ok_btn);
		btnUpdate.setOnClickListener(this);
		TextView content = (TextView) mDialog.findViewById(R.id.content);
		String strContent = app.getContent();
		if (!Util.isEmpty(strContent)) {
			String replace = strContent.replace("\\n", "\n");
			content.setText(replace);
			content.setVisibility(View.VISIBLE);
		}
		TextView notice = (TextView) mDialog.findViewById(R.id.notice);
		String strNotice = app.getNotice();
		if (!Util.isEmpty(strNotice)) {
			String replace = strNotice.replace("\\n", "\n");
			notice.setText(replace);
			notice.setVisibility(View.VISIBLE);
		}
		Button btnCancel = (Button) mDialog.findViewById(R.id.update_cancel_btn);
		btnCancel.setOnClickListener(this);
		mDialog.setCancelable(false);
		mDialog.show();
		progressBar.setProgress(0);
		progressDesc.setText(getResources().getString(R.string.version_info, app.getVersion()));
		progressInfo.setText("0/0");
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
				app.openFile(SettingActivity.this, apkFile);
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
			if (isCancelled()) {
				return;
			}
			progressInfo.setText(values[0] + "/" + values[1]);
			if (0 < values[1]) {
				progressBar.setProgress((int) (values[0] * 100) / values[1]);
			}
		}
	}
	
	private final class CopyTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			UploadFeed feed = fs.get(0);
			try {
				FileInputStream fis = new FileInputStream(feed.getPath() + File.separator + feed.getName());
				File file = new File(Util.getAppExtr() + File.separator + feed.getSurveyId() + File.separator + ma.userId);
				if (!file.exists()) {
					file.mkdirs();
				}
				File xmlFile = new File(file, feed.getName());
				FileOutputStream fos = new FileOutputStream(xmlFile);
				byte[] buff = new byte[1024];
				int len = 0;
				while (-1 != (len = fis.read(buff))) {
					fos.write(buff, 0, len);
				}
				fos.flush();
				fos.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			fs.remove(0);
			if (0 < fs.size()) {
				handler.obtainMessage(1).sendToTarget();
			} else {
				Toasts.makeText(SettingActivity.this, R.string.copy_xml_finish, Toast.LENGTH_LONG).show();
				dismiss();
			}
		}
	}

	private final class CopyRecordTask extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			UploadFeed feed = fsRecords.get(0);
			if (feed.getPath().contains(Environment.getExternalStorageDirectory().getAbsolutePath())) {
				return true;
			}
			try {
				File orgFile = new File(feed.getPath(), feed.getName());
				FileInputStream fis = new FileInputStream(orgFile);
				File file = new File(Util.getRecordPath(feed.getSurveyId()));
				if (!file.exists()) {
					file.mkdirs();
				}
				File distFile = new File(file, feed.getName());
				FileOutputStream fos = new FileOutputStream(distFile);
				byte[] buff = new byte[1024];
				int len = 0;
				while (-1 != (len = fis.read(buff))) {
					fos.write(buff, 0, len);
				}
				fos.flush();
				fos.close();
				fis.close();
				// 拷贝成功

				boolean b = ma.dbService.updateMoveToSdcard(feed.getId() + "", file.getAbsolutePath());
				if (b) {/* 新的目录名重置成功 */
					b = orgFile.delete();
					if (b)
						Util.Log("" + orgFile.getAbsolutePath() + "删除成功.");
					else
						Util.Log("" + orgFile.getAbsolutePath() + "删除失败.");
				}
				return b;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			fsRecords.remove(0);
			if (!Util.isEmpty(fsRecords)) {
				handler.obtainMessage(2).sendToTarget();
			} else {
				Toasts.makeText(SettingActivity.this, R.string.move_record_finish, Toast.LENGTH_LONG).show();
				dismiss();
			}

		}

	}
}
