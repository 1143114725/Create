package cn.dapchina.newsupper.service;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Notice;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
//import android.util.Log;

public class DiaryService extends Service {

	//推送通知
	private NotificationManager nm;
	
	/** 用于控制线程的开始终止 **/
	public static volatile boolean isRun = false;

	private volatile Timer timer;// 定时器

	private volatile CheckUploadTask checkTask; // 内部类
	// 每5分钟去找
	private final long time =1*60*1000;
	// *60*5
	private MyApp ma;

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		ma = (MyApp) getApplication();
		// Log.i(Cnt.TAG, "DiaryService--->onCreate()");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// Log.i(Cnt.TAG, "DiaryService--->onDestroy()");
		/** 线程停止运行 **/
		isRun = false;
		if (null != timer) {
			if (null != checkTask) {
				checkTask.cancel();
				checkTask = null;
			}
			timer.cancel();
			timer = null;
		}
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Log.i(Cnt.TAG, "DiaryService--->onStart()");
		/** 开启线程 **/
		isRun = true;
		timer = new Timer(true);
		checkTask = new CheckUploadTask();
		timer.schedule(checkTask, 0, time);// 1分钟检测一次
		super.onStart(intent, startId);
	}

	final class CheckUploadTask extends TimerTask {
		@Override
		public void run() {
			//推送 有网请求解析
			if(NetUtil.checkNet(ma)){
				
//				try {
////					String userName = 
//					//真正请求的
//					HashMap<String, Object> params=new HashMap<String, Object>();
//					params.put(Cnt.USER_ID,ma.cfg.getString("UserId", "游客"));
//					InputStream inStream = NetService.openUrl(Cnt.SEND_URL, params, "GET");
//					
////					InputStream inStream = ma.getResources().getAssets().open("notice.xml");
//					XmlService xs = new XmlService();
//					ArrayList<Notice> noticeList = xs.getNotice(inStream);
////					System.out.println("noticeList.size()"+noticeList.size());
//					if(!Util.isEmpty(noticeList)){
//						for(int i=0;i<noticeList.size();i++){
//							//这回响了下回就不去通知了
//							Notice notice = noticeList.get(i);
//							boolean isSend = ma.cfg.getBoolean(notice.getId());
//							if(!isSend){
//								ma.cfg.putBoolean(notice.getId(), true);
//								if (nm == null) {
//									nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//								}
//								Notification notify = new Notification(R.drawable.icon_dap,notice.getTitle(), System.currentTimeMillis());
//								Intent intent = new Intent(ma, SplashActivity.class);
//								PendingIntent pIntent = PendingIntent.getActivity(ma, 0, intent, 0);
//								notify.flags |= Notification.FLAG_AUTO_CANCEL;
//								notify.sound = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, 4);
//								// notify.defaults |= Notification.DEFAULT_VIBRATE;
//								notify.setLatestEventInfo(ma, notice.getTitle(), notice.getContent(), pIntent);
//								// 振动需要设置权限.
////								notify.vibrate = new long[] { 0, 1500, 800, 1500, 800, 1500 };
//								nm.notify(i, notify);
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				//推送结束
				Intent intent=new Intent(DiaryService.this,MyLocation.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startService(intent);
			}
		}
	}


}
