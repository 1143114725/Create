package cn.dapchina.newsupper.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.BaseActivity;
import cn.dapchina.newsupper.activity.VisitActivity;
import cn.dapchina.newsupper.bean.HttpBean;
import cn.dapchina.newsupper.bean.Knowledge;
import cn.dapchina.newsupper.bean.Logo;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.Quota;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.bean.option;
import cn.dapchina.newsupper.db.DBService;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.service.XmlService;
import cn.dapchina.newsupper.util.LogUtil;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.OutFile;
import cn.dapchina.newsupper.util.StreamTool;
import cn.dapchina.newsupper.util.Util;


public class MainService extends Service implements Runnable {

	/** 用于控制线程的开始终止 **/
	public static volatile boolean isRun = false;

	/** 封装任务Task **/
	private static ArrayList<Task> mTaskList = new ArrayList<Task>();

	/** 对窗口界面Activity进行封装 **/
	private static ArrayList<BaseActivity> mActivityList = new ArrayList<BaseActivity>();

	private volatile DBService dbService;

	/** 根据Activity类名获取Activity实体 **/
	public static BaseActivity getActivityByName(String activityName) {
		for (BaseActivity activity : mActivityList) {
			if (activity.getClass().getName().indexOf(activityName) >= 0) {
				return activity;
			}
		}
		return null;
	}

	/** 添加Activity **/
	public static void addActivity(BaseActivity grouponActivity) {
		mActivityList.add(grouponActivity);
	}

	/** 移除Activity **/
	public static void removeActivity(BaseActivity grouponActivity) {
		mActivityList.remove(grouponActivity);
	}

	/** 添加任务 **/
	public static void newTask(Task task) {
		mTaskList.add(task);
	}

	/** 录音文件  **/
	public static File recordFile;
	/** 录音对象   **/
	public static MediaRecorder mRecorder;
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (null == dbService) {
			dbService = new DBService(getApplicationContext());
		}
		/** 开启线程 **/
		isRun = true;
		Thread thread = new Thread(this);
		thread.start();
		LogUtil.printfLog("onStart");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		/** 线程停止运行 **/
		isRun = false;
		dbService = null;
		LogUtil.printfLog("onDestroy");
	}

	@Override
	public void run() {
		while (isRun) {
			try {
				if (mTaskList.size() > 0) {
					/** 执行任务 **/
					doTask(mTaskList.get(0));
				} else {
					try {
						Thread.sleep(100);
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

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private synchronized void doTask(Task task) {
		mTaskList.remove(task);
		/** 获取handler传递过来的消息 **/
		Message msg = mHandler.obtainMessage();
		msg.what = task.getTaskId();
		HashMap<String, Object> params = task.getParams();
		switch (msg.what) {
				//配额
				case TaskType.TS_GET_QUOTA:
					try {
						String surveyId = String.valueOf(params.get("surveyId"));
						String userId = String.valueOf(params.get("userId"));
						InputStream quotaStream = NetService.openUrl(Cnt.QUOTA_URL, params, "GET");
						XmlService xs = new XmlService();
						ArrayList<Quota> qList = xs.getListQuota(quotaStream);
						for (Quota quota: qList) {
							// 得到已经存在的配额id
							Quota getQuota = dbService.getQuotaExsit(quota.getQuotaId(),surveyId);
							if (null == getQuota) {
								// 增加新配额
								dbService.addQuota(quota,surveyId,userId);
							} else {
								//更新配额
								dbService.updateQuota(quota,surveyId);// 0代表不用更新提醒字段
							}
						}
						//获取所有配额
						msg.obj = dbService.getSurveyQuotaList(surveyId , userId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
		
		
		case TaskType.TS_NOTICE_SURVEY:
			try {
				String time = (String) params.get("time");
				params.remove("time");
				InputStream is = NetService.openUrl(Cnt.NOTICE_SURVEY, params, "GET");
				XmlService xs = new XmlService();
				Survey survey = xs.getSurvey(is);
				if(null==survey){
					msg.obj = null;
				}else{
					String generatedTime = survey.getGeneratedTime();
					boolean dateCompare = Util.getDateCompareByGeTime(time,generatedTime,">");
					//假如可更新传回去
					if(dateCompare){
						msg.obj=survey;
					}else{
						msg.obj = null;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg.obj = null;
			}
			break;
		case TaskType.TS_NOTICE_INNER:
			try {
				InputStream is = NetService.openUrl(Cnt.NOTICE_INNER, params, "GET");
				BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf8")));   
				String line="";   
				while ( (line = br.readLine()) != null ) {   
					msg.obj = line;
				}  
			} catch (Exception e) {
				e.printStackTrace();
				msg.obj = null;
			}
			break;
		//   大树   重置功能  1
		case TaskType.TS_REDEAL:
			if (null != params) {
				try {
					/** 以流的形式读取服务器数据 **/
					InputStream inStream = NetService.openUrl(Cnt.REDEAL_URL, params, "GET");
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					String state = "0";
					try {
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document document = builder.parse(inStream);
						Element element = document.getDocumentElement();
						state = element.getElementsByTagName("S").item(0).getFirstChild().getNodeValue();
						// Log.e("DapDesk", "State:" + state);
						System.out.println("解析之后的状态1--->" + state);
						if ("100".equals(state)) {
							msg.obj = 100;
						} else {
							msg.obj = 99;
						}
					} catch (Exception e) {
						Log.e("DapDesk", "Message:" + e.getMessage());
					}

				} catch (Exception e) {
					e.printStackTrace();
					msg.obj = 99;
					/** 弹出土司提示失败 **/
				}
			} else {
				msg.obj = 99;
			}
			break;
		// 大树    重置功能  2
		case TaskType.TS_RESET:
			if (null != params) {
				try {
					/** 以流的形式读取服务器数据 **/
					InputStream inStream = NetService.openUrl(Cnt.RESET_URL, params, "GET");
					XmlService xmlService = new XmlService();
					/** 将获取到的数据结合暂存在List集合中 **/
					List<UploadFeed> feeds = xmlService.getAll(inStream);
					/** 取得数据库操作对象 **/
					/** 本地标记医院列表下载和写入成功 **/
					// createShortCut();
					msg.obj = feeds;
				} catch (Exception e) {
					e.printStackTrace();
					msg.obj = null;
					/** 弹出土司提示失败 **/
				}
			} else {
				msg.obj = null;
			}
			break;

		// 自定义logo功能
		case TaskType.TS_GET_LOGO:
			try {
				String logoUserId = String.valueOf(params.get(Cnt.USER_ID));
				// InputStream logoStream = this.getAssets().open("logo.xml");
				InputStream logoStream = NetService.openUrl(Cnt.LOGO_URL, params, "GET");
				XmlService xs = new XmlService();
				Logo logo = xs.getLogo(logoStream);
				logo.setUserId(logoUserId);
				msg.obj = logo;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		// 知识库
		case TaskType.TS_GET_KNOW:
			String uid = String.valueOf(params.get(Cnt.USER_ID));
			uid = uid.trim();
			try {
				InputStream inStream = null;
				// 大树 测试知识库 应该是服务端还没有开这个口子
				// 如果 用户名为空 那么 请求 共用知识库 —————— 如果不是 那么请求 用户所在的知识库
				if (Util.isEmpty(uid)) {
					Log.i("zrl1", uid + "uid=null");
					inStream = this.getAssets().open("know.xml");
				} else {
					Log.i("zrl1", uid + "uid!=null");
					inStream = NetService.openUrl(Cnt.KNOWLEDGE_URL, params, "GET");
				}
				XmlService xs = new XmlService();
				if (inStream != null) {
					ArrayList<Knowledge> kList = xs.getAllKnow(inStream);
					for (Knowledge k : kList) {
						// 得到已经存在的知识库id
						Knowledge knowledge = dbService.getKnowExsit(k.getId());
						if (null == knowledge) {
							k.setUserList(uid);
							// 增加新知识库
							dbService.addKnow(k);
						} else {
							/*
							 * exist不空
							 */
							String exist = knowledge.getUserList();
							if (-1 == exist.indexOf(uid)) {
								k.setUserList(exist + "," + uid);
							} else {
								k.setUserList(exist);
							}
							// 更新知识库
							dbService.updateKnow(k);// 0代表不用更新提醒字段
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 查找有附件 并且 包含 uid 这个用户的 用户列表 的 知识库集合
			ArrayList<Knowledge> fileKnowledge = dbService.getAllHaveFileKnowledge(uid);
			// 查找 所有 包含 uid 这个用户的 知识库集合
			// 大树 如果没有改用户的知识库 ，那么就显示公共知识库 ，如果没有公共则显示Null
			msg.obj = dbService.getAllKnowledge(uid);
			ArrayList<Knowledge> tempKnowList = (ArrayList<Knowledge>) msg.obj;
			Log.i("zrl1", uid + "uid:");
			if (tempKnowList != null && tempKnowList.size() == 0 && !Util.isEmpty(uid)) {
				msg.obj = dbService.getAllKnowledge("");
				Log.i("zrl1", uid + "内部uid:");
			}
			// 知识库附件下载操作
			if (fileKnowledge != null && fileKnowledge.size() > 0) {
				Log.i("zrl1", "fileKnowledge下载走这里");
				new KnowDownloadTask(fileKnowledge, task, msg).execute();
				return;
			} else {
				break;
			}

		case TaskType.TS_AUTHOR:// Cnt.SHOUQUAN_URL
			String userId = String.valueOf(params.get(Cnt.USER_ID));
			try {
				InputStream inStream = NetService.openUrl(Cnt.SHOUQUAN_URL, params, "GET");
//				 InputStream inStream = this.getAssets().open("no.xml");
				XmlService xs = new XmlService();
				ArrayList<Survey> surveyList = xs.getAllSurvey(inStream);
				for (Survey s : surveyList) {
					// 问卷提醒开始
					Survey survey = dbService.getSurveyExsit(s.surveyId);
					// String exist = dbService.surveyExsit(s.surveyId);
					if (null == survey) {
						s.userIdList = userId;
						dbService.addSurvey(s);
					} else {
						/*
						 * exist不空
						 */
						String exist = survey.userIdList;
						if (-1 == exist.indexOf(userId)) {
							s.userIdList = exist + "," + userId;
						}
						// 假如以前下载过了，就比较时间
						if (1 == survey.isDowned) {
							s.isDowned = 1;// 告知这个问卷已经下载了
							String nowGeneratedTime = s.getGeneratedTime();// 现在
							String pastGeneratedTime = survey.getGeneratedTime();// 原来
							long nowLongGeneratedTime = Util.getLongTime(nowGeneratedTime, 3);
							long pastLongGeneratedTime = Util.getLongTime(pastGeneratedTime, 3);
							// 假如现在的生成时间大于存入数据库的时间，证明有更新的了。
							System.out.println("nowLongGeneratedTime:" + nowLongGeneratedTime + ":pastLongGeneratedTime" + pastLongGeneratedTime);
							if (nowLongGeneratedTime > pastLongGeneratedTime) {
								dbService.updateSurvey(s, 1);// 1代表不用更新提醒字段
							} else {
								dbService.updateSurvey(s, 0);// 0代表不用更新提醒字段
							}
						} else {
							dbService.updateSurvey(s, 0);// 0代表不用更新提醒字段
						}
					}
					// 问卷提醒结束
				}
				// 禁用不可用
				// 先查出所有存在该用户的问卷的SruveyId。
				ArrayList<String> surveyIdList = dbService.getAllSurveyId(userId);
				// 存放被停用的surveyId
				ArrayList<String> stopSurveyIdList = new ArrayList<String>();
				// 遍历查出来的surveyId
				for (String sId : surveyIdList) {
					for (int k = 0; k < surveyList.size(); k++) {
						Survey tempSurvey = surveyList.get(k);
						String xmlSurveyId = tempSurvey.surveyId;
						if (sId.equals(xmlSurveyId)) {
							// 跳转查下一个
							break;
						} else {
							// 假如查到最后一个还没有查到,把没查到的+到存放不可用集合里面去
							if (k == surveyList.size() - 1) {
								stopSurveyIdList.add(sId);
							}
						}
					}
				}
				if (stopSurveyIdList.size() > 0) {
					for (String stopId : stopSurveyIdList) {
						// 查出已经完成未上传完的surveyId
						long feedUnUploadCount = dbService.feedUnUploadCount(stopId);
						// 都上传了就停
						if (0 == feedUnUploadCount) {
							// 把不可用在数据库上更新
							dbService.enableSurvey(stopId);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			msg.obj = dbService.getAllSurvey(userId);
			break;
		// 大树 付费登陆
		// 大树 免费登陆
		case TaskType.TS_FREE_LOGIN:
		case TaskType.TS_ONLINE_LOGIN:
			try {
				InputStream is = NetService.openUrl(Cnt.LOGIN_URL, params, "GET");
				XmlService xs = new XmlService();
				msg.obj = xs.parseLoginXml(is);
			} catch (Exception e) {
				e.printStackTrace();
				msg.obj = null;
			}
			break;

		case TaskType.TS_LOGIN_GET_APP:
		case TaskType.TS_GET_APP:
			try {
				InputStream is = NetService.openUrl(Cnt.APP_URL, null, "GET");
				// InputStream is =this.getAssets().open("app_dapsurvey.xml");
				XmlService xs = new XmlService();
				msg.obj = xs.getApplication(is);
			} catch (Exception e) {
				e.printStackTrace();
				msg.obj = null;
			}
			break;
		// 大树 免费版注册
		// 大树 付费版 注册 这里头 要修改一个地址 这个地址要改的 等待服务器开一个口子 ，然后 对接
		case TaskType.TS_PAY_REGIST:
		case TaskType.TS_REGIST:
			try {
				InputStream is = NetService.openUrl(Cnt.REGIST_URL, params, "GET");
				XmlService xs = new XmlService();
				// 看看是否注册成功
				msg.obj = xs.getRegistResponse(is, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
				msg.obj = null;
			}
			break;
			//  全局录音  开启任务   
		case TaskType.TS_RECORED:
			LogUtil.printfLog("全局录音开启任务");
			Boolean isClicked=(Boolean) params.get("isClicked");
		    new RecordTask(isClicked,null,params).execute();
			break; 
		}
		mHandler.sendMessage(msg);
	}

	private class KnowDownloadTask extends AsyncTask<Void, Integer, Boolean> {
		private ArrayList<Knowledge> list;
		private Task task;
		private Message msg;
		// 大树
		private HttpBean hb;

		public KnowDownloadTask(ArrayList<Knowledge> list, Task task, Message msg) {
			this.list = list;
			this.task = task;
			this.msg = msg;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean yes = true;
			try {
				for (int i = 0; i < list.size(); i++) {
					Knowledge knowledge = list.get(i);
					String fileNames = knowledge.getFileName();
					//  大树知识库 
					if (fileNames.contains(";") && !Util.isEmpty(fileNames)) {
						String[] arrFileName = fileNames.split(";");
						for (String fileName : arrFileName) {
							File existfile = new File(Util.getKnowPath(), fileName);
							// 不存在下载
							if (!existfile.exists()) {
								System.out.println(Cnt.KNOWLEDGE_ATTACH_URL + fileName);
								Log.i("zrl1", "下载附件走这里");
								String downFileAddress = Cnt.KNOWLEDGE_ATTACH_URL + fileName;
								hb = NetService.obtainHttpBean(downFileAddress, null, "GET");
								// 大树 下载附件
								if (hb != null) {
									if (200 == hb.code) {
										File file = new File(Util.getKnowPath(), fileName);
										if (!file.getParentFile().exists()) {
											file.getParentFile().mkdirs();
										}
										FileOutputStream fos = new FileOutputStream(file);
										byte[] buffer = new byte[2048];
										int len = 0;
										int currentSize = 0;
										while ((len = hb.inStream.read(buffer)) != -1) {
											fos.write(buffer, 0, len);
											currentSize += len;
											publishProgress(currentSize, hb.contentLength);
										}
										fos.flush();
										fos.close();
										hb.inStream.close();
									}
								} else {
									yes = false;
								}
							}
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				yes = false;
				return yes;
			}
			return yes;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				mTaskList.remove(this.task);
				mHandler.sendMessage(this.msg);
			} else {
				// 大树 调用这里 下载知识库附件失败 走这
				mTaskList.remove(this.task);
				this.msg.what = TaskType.TS_GET_KNOWFILE_FAIL;
				mHandler.sendMessage(this.msg);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case TaskType.TS_GET_QUOTA:
				MainService.getActivityByName("MyQuotaActivity")//
						.refresh(msg.what, msg.obj);
				break;
			case TaskType.TS_AUTHOR:
				MainService.getActivityByName("SubscibeActivity")//
						.refresh(msg.what, msg.obj);
				break;
			// 大树 付费 免费 登陆 在这里
			case TaskType.TS_FREE_LOGIN:
			case TaskType.TS_ONLINE_LOGIN:
				// 统计局专有页面转为登录页
				BaseActivity ba = MainService.getActivityByName("LoginActivity");
				if (null != ba) {
					ba.refresh(msg.what, msg.obj);
				}
				break;

			case TaskType.TS_GET_APP:
				MainService.getActivityByName("SettingActivity")//
						.refresh(msg.what, msg.obj);
				break;

			case TaskType.TS_LOGIN_GET_APP:
				//app上线更新
				MainService.getActivityByName("WelcomeActivity")//
				.refresh(msg.what, msg.obj);
				break;
				// 大树 免费注册 跳转
			case TaskType.TS_REGIST:
				// 大树 付费注册 跳转
			case TaskType.TS_PAY_REGIST:
				MainService.getActivityByName("RegistActivity").refresh(msg.what, msg.obj);
				break;
			// 自定义logo功能
			case TaskType.TS_GET_LOGO:
				MainService.getActivityByName("LocalActivity").refresh(msg.what, msg.obj);
				break;
			// 知识库 大树 下载知识库附件失败 走这里
			case TaskType.TS_GET_KNOWFILE_FAIL:
			case TaskType.TS_GET_KNOW:
				MainService.getActivityByName("KnowleageActivity").refresh(msg.what, msg.obj);
				break;
			// 大树    重置功能  3
			case TaskType.TS_RESET:
			case TaskType.TS_REDEAL:
				MainService.getActivityByName("VisitActivity").refresh(msg.what, msg.obj);
				break;
				//  全局录音  调用 
			case TaskType.TS_RECORED:
				MainService.getActivityByName("NativeModeActivity").refresh(msg.what, msg.obj);
				break;
			case TaskType.TS_NOTICE_SURVEY:
			case TaskType.TS_NOTICE_INNER:
				if (null != MainService.getActivityByName("NoticeActivity")) {
					MainService.getActivityByName("NoticeActivity").refresh(msg.what, msg.obj);
				}
				break;
			}
		}
	};
	
	//  大树  重置   4   
	/** 重置功能**/
	private static VisitActivity visitActivity;
	public static VisitActivity getVisitActivity() {
		return visitActivity;
	}
	public static void setVisitActivity(VisitActivity visitActivity) {
		MainService.visitActivity = visitActivity;
	}
	// 重置   以上   
	
	/**
	 *    开启全局录音   
	 */
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
		private Question q;
		public RecordTask(boolean isClick, String number,HashMap<String, Object> hm) {
			this.click = isClick;
			this.num = number;
			this.hm=hm;
			feed=(UploadFeed) hm.get("feed");
			context=(Context) hm.get("content");
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
			q=(Question) hm.get("q");
		}

		// 大树动画 添加构造
		@Override
		protected Boolean doInBackground(Void... params) {
			LogUtil.printfLog("doInBackground");
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
					dbService.addRecord(//
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
				}
				//  录音已经点击  为  真  
				Message msg = mHandler.obtainMessage();
				msg.what = TaskType.TS_RECORED;
				msg.obj=20;
				mHandler.sendMessage(msg);
			} else {
				/**
				 * 停止录音
				 */
				if (null != recordFile && null != mRecorder) {
					mRecorder.stop();
					mRecorder.release();
					mRecorder = null;
				}
				//  录音按钮 没有点击  
				Message msg = mHandler.obtainMessage();
				msg.what = TaskType.TS_RECORED;
				msg.obj=10;
				mHandler.sendMessage(msg);
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
				//  开启录音 标志   30  
				Message msg = mHandler.obtainMessage();
				msg.what = TaskType.TS_RECORED;
				msg.obj=30;
				mHandler.sendMessage(msg);
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
			    //  关闭  录音 标志   40  
				LogUtil.printfLog("走这里吗告诉我");
				dbService.updateRecord(recordFile.getName(), System.currentTimeMillis(), recordFile.length());
			}
		}

	};   
}
