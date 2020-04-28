package cn.dapchina.newsupper.slide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.HomeActivity;
import cn.dapchina.newsupper.activity.LoginActivity;
import cn.dapchina.newsupper.activity.NoticeActivity;
import cn.dapchina.newsupper.activity.SubscibeActivity;
import cn.dapchina.newsupper.activity.UploadActivity;
import cn.dapchina.newsupper.adapter.HomeAdapter;
import cn.dapchina.newsupper.bean.Call;
import cn.dapchina.newsupper.bean.DapException;
import cn.dapchina.newsupper.bean.Data;
import cn.dapchina.newsupper.bean.HttpBean;
import cn.dapchina.newsupper.bean.InnerPanel;
import cn.dapchina.newsupper.bean.Intervention;
import cn.dapchina.newsupper.bean.OpenStatus;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.ParameterInnerPanel;
import cn.dapchina.newsupper.bean.QGroup;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.SurveyQuestion;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.mybean.Person;
import cn.dapchina.newsupper.service.AlarmReceiver;
import cn.dapchina.newsupper.service.XmlService;
import cn.dapchina.newsupper.util.ComUtil;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.MD5;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;
import cn.dapchina.newsupper.view.CustomProgressDialog;
import cn.dapchina.newsupper.view.Toasts;

public class CenterFagment extends Fragment implements OnClickListener {

	ImageView leftIv, subscibeIv;
	GridView gridView;
	HomeAdapter adapter;
	private int width; // 屏幕 宽度
	private int height; // 屏幕 高度
	private MyApp ma;
	private ArrayList<Survey> ss;
	private LinearLayout tvNoLoaclList;
	private ImageView ivName;
	private int index = 0;// 问卷索引

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 99:
				/** 更改发送邮箱为成功 */
				String fileName = (String) msg.obj;
				ma.dbService.updateTabToUnEnable(fileName);
				break;
			// 流程
			case 100:
				//只有第一次获取
				if(0==index){
					ss = ma.dbService.getAllDownloadedSurvey(ma.userId);
				}
				if (index == ss.size()) {
//					Toasts.makeText(ma, R.string.update_success, Toast.LENGTH_LONG).show();
					stopProgressDialog();
					return;
				}
				Survey survey = ss.get(index);
				// 判断问卷是否有更新。
				//有更新
				if (2==survey.getNoticeNew()) {
					new AuthorDownloadTask(survey).execute();
				}else{
					//无需更新
					//有内部名单的
					if(1==survey.openStatus){
						// 内部名单开始
						if (null == ma.cfg) {
							ma.cfg = new Config(ma);
						}
						if (Util.isEmpty(ma.userId)) {
							ma.userId = ma.cfg.getString("UserId", "");
						}
						String authorId = ma.cfg.getString("authorId", "");

						if (Util.isEmpty(ma.userId) || Util.isEmpty(authorId)) {
							// Toasts.makeText(ma, R.string.app_data_invalidate,
							// Toast.LENGTH_LONG).show();
							// 结束下一个
							index++;
							handler.sendEmptyMessage(100);
							return;
						}
						new InnerTask(authorId, ma.userId, survey).execute();
					}else{
						// 没更新，没名单的
						index++;
						handler.sendEmptyMessage(100);
					}
					
					
				}
				// 以此类推
				break;
			case 101:
				Thread th = new Thread(new Runnable() {
					public void run() {
						HashMap<String, Object> hm = new HashMap<String, Object>();
						hm.put(Cnt.USER_ID, ma.userId);
						hm.put(Cnt.USER_PWD, MD5.Md5Pwd(ma.userPwd));
						try {
							InputStream inStream = NetService.openUrl(Cnt.SHOUQUAN_URL, hm, "GET");
							// InputStream inStream =
							// this.getAssets().open("author.xml");
							XmlService xs = new XmlService();
							ArrayList<Survey> surveyList = xs.getAllSurvey(inStream);
							for (Survey s : surveyList) {
								// 问卷提醒开始
								Survey surveyTemp = ma.dbService.getSurveyExsit(s.surveyId);
								// String exist =
								// dbService.surveyExsit(s.surveyId);
								if (null == surveyTemp) {
									s.userIdList = ma.userId;
									ma.dbService.addSurvey(s);
								} else {
									/*
									 * exist不空
									 */
									String exist = surveyTemp.userIdList;
									if (-1 == exist.indexOf(ma.userId)) {
										s.userIdList = exist + "," + ma.userId;
									}
									// 假如以前下载过了，就比较时间
									if (1 == surveyTemp.isDowned) {
										s.isDowned = 1;// 告知这个问卷已经下载了
										String nowGeneratedTime = s.getGeneratedTime();// 现在
										String pastGeneratedTime = surveyTemp.getGeneratedTime();// 原来
										long nowLongGeneratedTime = Util.getLongTime(nowGeneratedTime, 3);
										long pastLongGeneratedTime = Util.getLongTime(pastGeneratedTime, 3);
										// 假如现在的生成时间大于存入数据库的时间，证明有更新的了。
										// System.out.println("nowLongGeneratedTime:"
										// + nowLongGeneratedTime +
										// ":pastLongGeneratedTime" +
										// pastLongGeneratedTime);
										if (nowLongGeneratedTime > pastLongGeneratedTime) {
											ma.dbService.updateSurvey(s, 1);// 2代表可更新字段
										} else {
											ma.dbService.updateSurvey(s, 0);// 0代表不用更新提醒字段
										}
									} else {
										ma.dbService.updateSurvey(s, 0);// 0代表不用更新提醒字段
									}
								}
								// 问卷提醒结束
							}
							// 禁用不可用
							// 先查出所有存在该用户的问卷的SruveyId。
							ArrayList<String> surveyIdList = ma.dbService.getAllSurveyId(ma.userId);
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
									long feedUnUploadCount = ma.dbService.feedUnUploadCount(stopId);
									// 都上传了就停
									if (0 == feedUnUploadCount) {
										// 把不可用在数据库上更新
										ma.dbService.enableSurvey(stopId);
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(100);
					}
				});
				th.start();
				break;
			}
		}
	};

	// 流程下载问卷
	private class AuthorDownloadTask extends AsyncTask<Void, Integer, Boolean> {
		private Survey s;
		SurveyQuestion sq;

		public AuthorDownloadTask(Survey survey) {
			this.s = survey;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean yes = false;
			try {
				
				HttpBean hb = NetService.obtainHttpBean(s.downloadUrl, null, "GET");
				if (200 == hb.code) {
					File file = new File(Util.getSurveySaveFilePath(ma), s.surveyId + ".zip");
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
					// String absPath = Util.getSurveyFileAbsolutePath(mContext,
					// s.surveyId);
					yes = Util.decompress(file.getAbsolutePath(), Util.getSurveyFilePath(ma, s.surveyId), s.surveyId, new Call() {

						@Override
						public void updateProgress(int curr, int total) {
							publishProgress(curr, total);
						}
					});
					if (yes) {
						file.delete();
						/**
						 * 假如是原生模式访问则解析原生XML问卷
						 */
						if (1 == this.s.visitMode) {
							String xml = Util.getSurveyXML(ma, s.surveyId);
							FileInputStream inStream = new FileInputStream(xml);
							//数据字典
							ArrayList<String> classIdList=new ArrayList<String>();
							// InputStream
							// inStream=mContext.getAssets().open("70.xml");
							if (null != inStream) {
								sq = XmlUtil.getSurveyQuestion(inStream, new Call() {
									@Override
									public void updateProgress(int curr, int total) {
										
									}
								});
								ArrayList<Question> qs = sq.getQuestions();
								//数据字典
								classIdList=sq.getClassId();
								if (!Util.isEmpty(qs)) {
									for (int i = 0; i < qs.size(); i++) {
										Question q = qs.get(i);
										// q.qSign=1;//模拟单题签名
										q.surveyId = s.surveyId;
										if (-1 != q.qOrder) {
											boolean qt = ma.dbService.isQuestionExist(s.surveyId, q.qIndex);
											if (qt) {
												boolean u = ma.dbService.updateQuestion(q);
												if (u) {
													/**
													 * 问卷的OrderId发生变化或问题类型发生变化更新相应的值
													 * 否则逻辑无法匹配
													 */
													ma.dbService.updateAnswerOrder(q);
													System.out.println("" + q.qIndex + "更新成功.");
												}
											} else {
												System.out.println();
												boolean b = ma.dbService.addQuestion(q);
												if (b) {
													System.out.println("" + q.qIndex + "插入成功.");
												}
											}
										} else {
											/**
											 * 废除的题目
											 */
											ma.dbService.deleteQuestion(q.surveyId, q.qIndex + "");
											/**
											 * 删除答案
											 */
											ma.dbService.deleteAnswer(q.surveyId, q.qIndex + "");
										}
										publishProgress((i + 1), qs.size());
									}

									/**
									 * 将问卷中所有的题组随机置空
									 */
									ma.dbService.updateQuestionGroup2Null(s.surveyId);

									/**
									 * 题组随机json字符串入库
									 */
									if (!Util.isEmpty(sq.getQgs())) {
										for (QGroup qg : sq.getQgs()) {
											ma.dbService.updateQuestionGroup(s.surveyId, qg.getRealIndex(), XmlUtil.parserQGroup2Json(qg));
										}
									}

									/**
									 * //逻辑跳转解析字符json串入库
									 */
									if (null != sq.getLogicList()) {
										ma.dbService.updateLogicListBySurvey(s.surveyId, XmlUtil.parserLogicList2Json(sq.getLogicList()));
									} else {
										ma.dbService.updateLogicListBySurvey(s.surveyId, "");
									}
								}
							}

							/**
							 * 先将干预的字符串标志置为空， 这样为了防止干预有改动
							 */
							ma.dbService.updateAllIntervention2Null(s.surveyId);

							/**
							 * 获取干预文件的绝对路径
							 */
							xml = Util.getSurveyIntervention(ma, s.surveyId);
							File iiFile = new File(xml);
							if (iiFile.exists()) {
								ArrayList<Intervention> iis = XmlUtil.getInterventionList(iiFile, new Call() {
									@Override
									public void updateProgress(int curr, int total) {
										publishProgress(curr, total);
									}
								});
								for (int i = 0; i < iis.size(); i++) {
									// String json = ;
									Intervention ii = iis.get(i);
									ma.dbService.updateQuestionIntervention(s.surveyId, ii.getIndex(), XmlUtil.parserIntervention2Json(ii));
									publishProgress((i + 1), iis.size());
								}

								/**
								 * 处理完干预的xml后, 将其删除, 以免对下一次更改干预的xml造成影响
								 */
								iiFile.delete();
							}
							//数据字典不为空
							if(!Util.isEmpty(classIdList)){
								for(String classId:classIdList){
									//数据字典
									HashMap<String, Object> hmData = new HashMap<String, Object>();
									hmData.put(Cnt.USER_ID, ma.userId);
									hmData.put(Cnt.USER_PWD, ma.userPwd);
									hmData.put("classId", classId);
//									System.out.println("走了几遍");
									InputStream inStreamData = NetService.openUrl(Cnt.DATA_URL, hmData, "GET");
									List<Data> dataList=XmlUtil.parseData(inStreamData);
									for(int z=0;z<dataList.size();z++){
										Data data = dataList.get(z);
										if(!ma.dbService.IsExistData(data.getClassId())){
											//假如不存在就增加
											ma.dbService.addData(data);
										}else{
											//假如存在就更新
											ma.dbService.updateData(data);
										}
									}
								}
							}
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return yes;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				// bt.setText(context.getResources().getString(R.string.update));
				/** 问卷更新清空目录开始 **/
				// Drawable[] compoundDrawables = bt.getCompoundDrawables();
				// for(Drawable dr:compoundDrawables){
				// if(null==dr){
				// continue;
				// }else{
				// ma.dbService.updateListUploadFeed(s.surveyId);
				// break;
				// }
				// }
				// /**问卷更新清空目录结束**/
				// //问卷提醒取消图标
				// bt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				ma.dbService.surveyDownloaded(s.surveyId, (null == sq) ? -1 : sq.getEligible(), (null == sq) ? "" : sq.getWord());
				// 有内部名单的
				if (1 == s.openStatus) {
					// 内部名单开始
					if (null == ma.cfg) {
						ma.cfg = new Config(ma);
					}
					if (Util.isEmpty(ma.userId)) {
						ma.userId = ma.cfg.getString("UserId", "");
					}
					String authorId = ma.cfg.getString("authorId", "");

					if (Util.isEmpty(ma.userId) || Util.isEmpty(authorId)) {
						// Toasts.makeText(ma, R.string.app_data_invalidate,
						// Toast.LENGTH_LONG).show();
						// 结束下一个
						index++;
						handler.sendEmptyMessage(100);
						return;
					}
					new InnerTask(authorId, ma.userId, s).execute();
				} else {
					// 结束下一个
					index++;
					handler.sendEmptyMessage(100);
				}
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	};

	// 流程下载名单
	// 内部名单方法
	private final class InnerTask extends AsyncTask<Void, Integer, Boolean> {

		private String authorId;
		private String userId;
		private Survey survey;

		public InnerTask(String _authorId, String _userId, Survey _survey) {
			this.authorId = _authorId;
			this.userId = _userId;
			this.survey = _survey;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// http://www.dapchina.cn/newsurvey/alisoft/DownloadUser.asp?AuthorID=1514&SurveyID=3076
			try {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("AuthorID", this.authorId);
				hm.put("SurveyID", survey.surveyId);
				InputStream inStream = NetService.openUrl(Cnt.INNER_URL, hm, "GET");
				// InputStream inStream =mContext.getAssets().open("ceshi.xml");
				OpenStatus os = XmlUtil.ParserInnerPanelList(inStream);
				// 遍历下载的内部名单panel号
				ArrayList<String> spList = new ArrayList<String>();
				for (int i = 0; i < os.getIps().size(); i++) {
					InnerPanel ip = os.getIps().get(i);
					// 引用受访者参数
					ParameterInnerPanel pip = os.getParameterIps().get(i);
					ArrayList<Parameter> parameters = pip.getParameters();
					pip.setParameters(parameters);
					// 引用受访者参数结束
					// 命名规则开始
					// 不为空判断
					String content = "";
					if (!Util.isEmpty(os.getParameterName())) {
						for (Parameter parameter : parameters) {
							if (parameter.getSid().equals(os.getParameterName())) {
								content = parameter.getContent();
								// 是中文的就置为空
								if (Util.isContainChinese(content)) {
									content = "";
								}
								break;
							}
						}
					}
					// 命名规则结束
					if (!Util.isEmpty(ip.getPanelID())) {
						spList.add(ip.getPanelID());
						String str = XmlUtil.paserInnerPanel2Json(ip);
						// System.out.println("inner_json==="+str);
						if (ma.dbService.isFeedExist(survey.surveyId, ip.getPanelID())) {
							// 假如服务器上有id 本地上也有id的更新。引用受访者参数
							// System.out.println("更新");
							ma.dbService.updateInnerUploadFeed(survey.surveyId, ip.getPanelID(), str, ip.getFeedId(), pip.getParametersStr());
						} else {
							// 假如服务器上有id 本地上没有id的增加。
							String uuid = UUID.randomUUID().toString();
							String path = Util.getXmlPath(ma, survey.surveyId);
							// 增加pid 命名规则
							String name = Util.getXmlName(userId, survey.surveyId, uuid, ip.getPanelID(), content);
							// System.out.println("feedId=" + ip.getFeedId() +
							// ", uuid=" + uuid + ", path=" + path + ", name=" +
							// name + ", userId=" + userId +
							// ", survey.surveyId=" + survey.surveyId);
							ma.dbService.addInnerUploadFeed(ip.getFeedId(), userId, survey.surveyId, uuid, System.currentTimeMillis(), path, name, survey.visitMode, str, ip.getPanelID(),
									pip.getParametersStr());
						}
					}
				}
				ArrayList<String> dbList = ma.dbService.getListBySurveyId(survey.surveyId, ma.userId);
				/**
				 * 判断服务器上没有 本地上有这个panelid ,剩下的dblist就是剩下的服务器没有的。该删除的
				 * 把查出来的panelId删除(没数据的删除，有数据的不删除?但传不传,不传)
				 */
				for (int i = dbList.size() - 1; i >= 0; i--) {
					String temp = dbList.get(i).split(":::")[0];
					for (int j = 0; j < spList.size(); j++) {
						if (temp.equals(spList.get(j))) {
							dbList.remove(i);
							break; 
						}
					}
				}
				/**
				 * 遍历数据,查出指定的路径xml文件。假如这个文件存在。设置为giveup为2。不存在的话giveup为1。
				 */
				//  大树设置第一次访问标志   
//				if (!Util.isEmpty(dbList)) {
//					for (String deleteDB : dbList) {
//						String panelId = deleteDB.split(":::")[0];
//						String path = deleteDB.split(":::")[1];
//						File file = new File(path);
//						int giveUp = 0;
//						// 存在还能做
//						if (file.exists()) {
//							giveUp = 2;
//							ma.dbService.updateGiveUpByPanelId(panelId, survey.surveyId, giveUp);
//						}
//						// 不存在设置为放弃,查不出来
//						else {
//							giveUp = 1;
//							ma.dbService.updateGiveUpByPanelId(panelId, survey.surveyId, giveUp);
//						}
//					}
//				}
				// 命名规则更改
				ma.dbService.updateSurveyOpenStatus(survey.surveyId, os.getParameter1(), os.getParameter2(), os.getParameter3(), os.getParameter4(), os.getParameterName());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				ma.dbService.updateSurveyInnerDone(survey.surveyId);
			} else {
				// Toasts.makeText(ma, R.string.inner_failure,
				// Toast.LENGTH_LONG).show();
			}
			// 结束下一个
			index++;
			handler.sendEmptyMessage(100);
		}
	}

	public void onResume() {
		// 没登录直接显示
		initSurvey();
		super.onResume();
	};

	// 流程进度条
	private CustomProgressDialog progressDialog = null;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this.getActivity());
			progressDialog.setMessage(this.getActivity().getResources().getString(R.string.compare_survey));
		}
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private Config cfg;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_activity, null);
		leftIv = (ImageView) view.findViewById(R.id.directory);
		subscibeIv = (ImageView) view.findViewById(R.id.head_subscibe_iv);
		gridView = (GridView) view.findViewById(R.id.gridView1);
		tvNoLoaclList = (LinearLayout) view.findViewById(R.id.tvNoLoaclList);
		ivName = (ImageView) view.findViewById(R.id.ivName);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取// 屏幕宽高
		width = dm.widthPixels;
		height = dm.heightPixels;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width / 2.3), (int) (height / 4));
		ivName.setLayoutParams(params);
		ivName.setOnClickListener(this);
		ma = (MyApp) this.getActivity().getApplication();
		cfg = new Config(getActivity().getApplicationContext());
		
		// initSurvey();
		
		if(NetUtil.checkNet(this.getActivity())&&!Util.isEmpty(cfg.getString(Cnt.USER_ID, ""))){
			//提示页面
			Intent in=new Intent(CenterFagment.this.getActivity(),NoticeActivity.class);
			in.putExtra("notice", "1");//1代表不能操作
			startActivity(in);
		}else if(Util.isEmpty(cfg.getString(Cnt.USER_ID, ""))){
			Toast.makeText(getActivity(), R.string.no_login_no_compare, Toast.LENGTH_LONG).show();
		}else if(!NetUtil.checkNet(this.getActivity())){
			Toast.makeText(getActivity(), R.string.no_net_no_compare, Toast.LENGTH_LONG).show();
		}
		return view;
	}

	// 一些初始化操作
	private void initSurvey() {
		// 自定义logo功能 请求下载
		//开关判断地图监控
		if(ma.cfg.getBoolean("mapMonitor",false)){
			sendAlarmBroadCase();
			}
		// getExceptionToEmail();
		if (Util.isEmpty(ma.userId)) {
			ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(this.getActivity())) : (ma.cfg)).getString("userId", getResources().getString(R.string.user_name_test));
			if(Util.isEmpty(ma.userId)){
				ma.userId = getResources().getString(R.string.user_name_test);
			}
		}
		// System.out.println("ma.userId:"+ma.userId);
		ss = ma.dbService.getAllDownloadedSurvey(ma.userId);
		if (Util.isEmpty(ma.userPwd)) {
			Survey survey = ma.dbService.getTextSurvey();// 获得试用问卷
			if (survey.surveyTitle == null) {

			} else {
				ss.add(survey);
			}
		}

		if (!Util.isEmpty(ss)) {
			tvNoLoaclList.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			if (null == adapter) {
				adapter = new HomeAdapter(CenterFagment.this.getActivity(), ss);
				gridView.setAdapter(adapter);
			} else {
				adapter.refresh(ss);
			}
			// 流程
			// 流程 有网登录就更新    大树设置第一次访问标志
//			if (!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma) && ma.cfg.getBoolean("isFirst")==true) {
//				startProgressDialog();
//				// 重新下载重新赋值
//				index = 0;
//				// 是下载这用户的所有订阅项目
//				handler.sendEmptyMessage(101);
//				ma.cfg.putBoolean("isFirst", false); //  设置第一次访问 否   
//				Log.i("zrl1", "是否第一次："+ma.cfg.getBoolean("isFirst"));
//			}else{
//				
//			}
		} else {
			gridView.setVisibility(View.GONE);
			tvNoLoaclList.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		leftIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((HomeActivity) getActivity()).showLeft();
			}
		});

		subscibeIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 流程
				if (!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)) {
					((HomeActivity) getActivity()).skipActivity(SubscibeActivity.class);
				} else if (!NetUtil.checkNet(ma)) {
					// 没网情况下
					Toasts.makeText(ma, R.string.exp_net, Toast.LENGTH_LONG).show();
				} else if (Util.isEmpty(ma.userPwd)) {
					// 用户名为空
					Toasts.makeText(ma, R.string.no_login, Toast.LENGTH_LONG).show();
					((HomeActivity) getActivity()).skipActivity(LoginActivity.class,30);
				}
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	public void sendAlarmBroadCase() {
		Intent intent = new Intent(this.getActivity(), AlarmReceiver.class);
		intent.setAction("arui.alarm.action");
		PendingIntent sender = PendingIntent.getBroadcast(this.getActivity(), 0,//
				intent, 0);
		long firstime = SystemClock.elapsedRealtime();
		AlarmManager am = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
		// 10秒一个周期，不停的发送广播
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
		//
				1 * 60 * 1000, sender);
	}

	/**
	 * 得到异常发送邮件
	 */
	private void getExceptionToEmail() {
		// 获得所有异常
		ArrayList<DapException> exList = ma.dbService.getTab1ByEnable();
		// 假如有异常，发送，否则什么都不干。
		if (exList.size() > 0) {
			// 假如有网
			if (ComUtil.checkNet(this.getActivity())) {
				for (int i = 0; i < exList.size(); i++) {
					sendThreadException(exList.get(i));
				}
				Toasts.makeText(this.getActivity(), R.string.doing_send, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * sendThreadException 发送邮件
	 */
	private void sendThreadException(final DapException dapException) {
		new Thread() {
			public void run() {
				try {
					sendException("depaiceshi", "depaiceshi1", "bug@dapchina.cn", dapException);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 发送错误信息到邮箱
	 */
	private void sendException(String userName, String passWord, String email, DapException dapException) throws Exception {
		// System.out.println("邮件:" + email);
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.163.com");
		props.setProperty("mail.smtp.auth", "true");
		class MyAuthenticator extends Authenticator {

			private String name;
			private String pass;

			public MyAuthenticator(String username, String password) {
				this.name = username;
				this.pass = password;
			}

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(name, pass);// @sohu.com
			}

		}
		Session session = Session.getInstance(props, new MyAuthenticator(userName, passWord));
		// Session session = Session.getInstance(props, new Authenticator() {
		//
		// @Override
		// protected PasswordAuthentication getPasswordAuthentication() {
		// return new PasswordAuthentication("dapserver",
		// "dapchina_2016");//@sohu.com
		// }
		//
		// });
		MimeMessage message = new MimeMessage(session);
		String emailFrom = userName + "@163.com";
		message.setFrom(InternetAddress.parse(MimeUtility.encodeText("SIM SYSTEM") + "<" + emailFrom + ">")[0]);
		message.setRecipient(RecipientType.TO, new InternetAddress(email));
		message.setSentDate(new Date());
		message.setSubject(this.getString(R.string.error_message, dapException.getFileName()));
		// message.setContent("Dear Participant,<br><br>This is a reminder to take medication according to your dosing schedule and complete the appropriate diary pages.Thanks.<br><br>CIMS System<br>",
		// "text/html");

		// 邮件正文
		MimeBodyPart text = new MimeBodyPart();

		text.setContent(this.getString(R.string.get_message, dapException.getUserId(), dapException.getMacAddress(), dapException.getFilePath()), "text/html;charset=gbk");

		// 描述数据关系
		MimeMultipart multipart = new MimeMultipart();
		// 附件
		File log = new File(dapException.getFilePath(), dapException.getFileName());
		/** 开始加的附件 */
		MimeBodyPart mbpFile = new MimeBodyPart();
		// 得到数据源
		FileDataSource fds = new FileDataSource(log);
		// 得到附件本身并至入BodyPart
		mbpFile.setDataHandler(new DataHandler(fds));
		// 得到文件名同样至入BodyPart
		mbpFile.setFileName(fds.getName());
		/** 结束加的附件 */
		multipart.addBodyPart(mbpFile);
		multipart.addBodyPart(text);
		multipart.setSubType("related");

		message.setContent(multipart);

		message.saveChanges();
		Transport.send(message);
		session.getTransport().close();
		// DataHandler dh = new DataHandler("","");
		// 回收垃圾内存
		Message msg = handler.obtainMessage();
		msg.obj = dapException.getFileName();
		msg.what = 99;
		handler.sendMessage(msg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivName:
			// 流程
			if (!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)) {
				Intent intent = new Intent();
				intent.setClass(this.getActivity(), SubscibeActivity.class);
				startActivityForResult(intent, 10);
				this.getActivity().overridePendingTransition(R.anim.right, R.anim.left);
			} else if (!NetUtil.checkNet(ma)) {
				// 没网情况下
				Toasts.makeText(ma, R.string.exp_net, Toast.LENGTH_LONG).show();
			} else if (Util.isEmpty(ma.userPwd)) {
				// 用户名为空
				Toasts.makeText(ma, R.string.no_login, Toast.LENGTH_LONG).show();
				((HomeActivity) getActivity()).skipActivity(LoginActivity.class,30);
			}
			break;
		default:
			break;
		}
	}

}
