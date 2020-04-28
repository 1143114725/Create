package cn.dapchina.newsupper.main;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.BaseActivity;
import cn.dapchina.newsupper.activity.NativeModeActivity;
import cn.dapchina.newsupper.bean.Knowledge;
import cn.dapchina.newsupper.bean.Logo;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.service.XmlService;
import cn.dapchina.newsupper.util.LogUtil;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;
import android.R.bool;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class AsynckService extends Service implements Runnable {

	
	/** 用于控制线程的开始终止 **/
	public static volatile boolean isRun = false;
	
	/** 封装任务Task **/
	private static ArrayList<Task> mTaskList = new ArrayList<Task>();
	/** 添加任务 **/
	public static void newTask(Task task) {
		mTaskList.add(task);
	}

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		LogUtil.printfLog("onCreate");
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		LogUtil.printfLog("onStart");
		/** 开启线程 **/
		isRun = true;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/** 线程停止运行 **/
		isRun = false;
		LogUtil.printfLog("ondestroy");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			try {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (mTaskList.size() > 0) {
					/** 执行任务 **/
					doTask(mTaskList.get(0));
				} else {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				if (mTaskList.size() > 0) {
					mTaskList.remove(mTaskList.get(0));
				}
				Log.d("error", "------------------" + e);
			}
		}
	}

	private synchronized void doTask(Task task) {
		mTaskList.remove(task);
		/** 获取handler传递过来的消息 **/
		int what=task.getTaskId();
		HashMap<String, Object> params = task.getParams();
		switch (what) {
		case 0:
			// 录音    全局录音  
		    Boolean isClicked=(Boolean) params.get("isClicked");
		    LogUtil.printfLog("走这里"+isClicked);
		    new RecordTask(isClicked,null,params).execute();
			break;
		case 1:
			// 照相  
			break;  
		default:
			break;
		}
		
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class RecordTask extends AsyncTask<Void, Integer, Boolean> {

		public boolean click;
		private String num;
		private HashMap<String, Object> hm;
		/**
		 *   hm  包含 很多东西  :　 
		 *   1  Upload feed  
		 *   2  Context  content  
		 *   3  recordFile  
		 *   4  MyApp  ma  
		 *   5  isClicked   
		 *   6  mRecorder 
		 *   7  q    
		 * @param isClick
		 * @param number
		 * @param hm
		 */
		private UploadFeed feed;
		private Context context;
		private File recordFile;
		private MyApp ma; 
		private MediaRecorder mRecorder;
		private Question q;
		public RecordTask(boolean isClick, String number,HashMap<String, Object> hm) {
			this.click = isClick;
			this.num = number;
			this.hm=hm;
			feed=(UploadFeed) hm.get("feed");
			context=(Context) hm.get("content");
			recordFile=(File) hm.get("recordFile");
			ma=(MyApp) hm.get("ma");
			mRecorder=(MediaRecorder) hm.get("mRecorder");
			q=(Question) hm.get("q");
		}

		// 大树动画 添加构造方法 用于切换图片 8
		private ImageView recordIv;

		public RecordTask(boolean click, String num, ImageView recordIv,HashMap<String, Object> hm) {
			super();
			this.click = click;
			this.num = num;
			this.recordIv = recordIv;
			this.hm=hm;
			feed=(UploadFeed) hm.get("feed");
			context=(Context) hm.get("content");
			recordFile=(File) hm.get("recordFile");
			ma=(MyApp) hm.get("ma");
			mRecorder=(MediaRecorder) hm.get("mRecorder");
			q=(Question) hm.get("q");
		}

		// 大树动画 添加构造
		@Override
		protected Boolean doInBackground(Void... params) {
			LogUtil.printfLog("走这里吗"+click);
			if (!this.click) {
				/**
				 * 录音
				 */
				String path = "";
				int inner = 0;
				/**
				 *   录音地址变更    如果存在 SDCARD 并且  大小有100M 的空间  
				 */
				LogUtil.printfLog("走这里吗1");
				if (Util.readSDCard()[1]>=0.1) {
					//  存到本地 SDcard 中   
					path=Util.getRecordPath(feed.getSurveyId());
					inner=1; 
				}else {  
					//  存到 系统内置卡  里头  
					path = Util.getRecordInnerPath(context, feed.getSurveyId());
				}
				// 增加pid 命名规则
				recordFile = new File(path, // path
						Util.getRecordName(feed.getUserId(), feed.getSurveyId(), Cnt.FILE_TYPE_MP3, feed.getUuid(), null, feed.getPid(), feed.getParametersContent(), (q.qOrder) + ""));
				if (!recordFile.getParentFile().exists()) {
					recordFile.getParentFile().mkdirs();
				}
				try {
					mRecorder = new MediaRecorder();
					mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
					mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					mRecorder.setOutputFile(recordFile.getAbsolutePath());
					mRecorder.prepare();
					mRecorder.start();
					// Log.i("kjy", "路径--->" + recordFile.getAbsolutePath());
					ma.dbService.addRecord(//
							feed.getUserId(), //
							feed.getSurveyId(), //
							feed.getUuid(), //
							recordFile.getParent(), //
							recordFile.getName(), //
							System.currentTimeMillis(), //
							Cnt.FILE_TYPE_MP3, //
							num,//
							inner,
							feed.getFeedId());
				} catch (Exception e) {
					e.printStackTrace();
					/**
					 * 大树录音 捕获异常 处理
					 */
					ma.uncaughtException(new Thread(Thread.currentThread().getName()), e);
				}
//				isClicked = true;
			} else {
				/**
				 * 停止录音
				 */
				if (null != recordFile && null != mRecorder) {
					mRecorder.stop();
					mRecorder.release();
					mRecorder = null;
				}
//				isClicked = false;
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/**
			 * 开始录音,网数据库中插入数据
			 */
			if (!this.click) {
				if (recordIv != null) {
					recordIv.setImageResource(R.drawable.audio_busy_2);
				}
//				Toasts.makeText(context, R.string.record_open, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (this.click) {
				/**
				 * 关闭录音
				 */
				if (recordIv != null) {
					recordIv.setImageResource(R.drawable.ic_btn_speak_now_2);
				}
//				Toasts.makeText(context, R.string.record_close, Toast.LENGTH_SHORT).show();
				ma.dbService.updateRecord(recordFile.getName(), System.currentTimeMillis(), recordFile.length());
			}
		}

	};
	
	
}
