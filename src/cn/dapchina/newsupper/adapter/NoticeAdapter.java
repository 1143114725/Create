package cn.dapchina.newsupper.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.util.EventLog.Event;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.LoginActivity;
import cn.dapchina.newsupper.activity.NoticeActivity;
import cn.dapchina.newsupper.bean.Call;
import cn.dapchina.newsupper.bean.Data;
import cn.dapchina.newsupper.bean.HttpBean;
import cn.dapchina.newsupper.bean.InnerPanel;
import cn.dapchina.newsupper.bean.Intervention;
import cn.dapchina.newsupper.bean.MyRecoder;
import cn.dapchina.newsupper.bean.OpenStatus;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.ParameterInnerPanel;
import cn.dapchina.newsupper.bean.QGroup;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.SurveyQuestion;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.ftp.UploadStatus;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.service.FileUpLoad;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.MD5;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;
import cn.dapchina.newsupper.view.Toasts;

public class NoticeAdapter extends BaseAdapter {

	private ArrayList<Survey> ss;
	private LayoutInflater inflater;

	private MyApp ma;
	private int flag;
	private NoticeActivity noticeActivity;
	private String notice;

	@Override
	public int getCount() {
		return ss.size();
	}

	// 刷新
	public void refresh(ArrayList<Survey> surveys, String notice) {
		if (!Util.isEmpty(surveys)) {
			if (!Util.isEmpty(ss)) {
				ss.clear();
				ss.addAll(surveys);
			}
			this.notice = notice;
			notifyDataSetChanged();
		}
	}

	public NoticeAdapter(NoticeActivity _c, MyApp mApp, ArrayList<Survey> surveys, int flag, String notice) {
		this.ma = mApp;
		this.ss = surveys;
		this.noticeActivity = _c;
		this.flag = flag;
		this.notice = notice;
		inflater = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getItem(int position) {
		return ss.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		convertView = inflater.inflate(R.layout.notice_item, null);
		vh = new ViewHolder();
		vh.tvSurveyTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		vh.btnNotice = (Button) convertView.findViewById(R.id.btn_notice);
		vh.pbDownload = (ProgressBar) convertView.findViewById(R.id.author_list_progress);
		Survey s = ss.get(position);

		if (null != s) {
			if (Util.isEmpty(ma.userId)) {
				ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(noticeActivity)) : (ma.cfg)).getString("UserId", "");
			}
			/**
			 * 已完成未上报的
			 */
			long u = ma.dbService.feedUnUploadCounts(s.surveyId, ma.userId);
			if (1 == flag) {
				vh.tvSurveyTitle.setText(Html.fromHtml(s.surveyTitle + "<font color='#1576ce'><b>(" + u + ")</b></font>"));
			} else {
				vh.tvSurveyTitle.setText(Html.fromHtml(s.surveyTitle));
			}

			if ("1".equals(notice)) {
				vh.btnNotice.setBackgroundDrawable(noticeActivity.getResources().getDrawable(R.drawable.bg_skin_navbar_none));
				vh.btnNotice.setClickable(false);
				if (1 == flag) {
					// 上传
					vh.btnNotice.setText(noticeActivity.getString(R.string.upload));
					vh.btnNotice.setOnClickListener(new NoticeOnClickListener(flag));
				} else if (2 == flag) {
					// 更新问卷
					vh.btnNotice.setText(noticeActivity.getString(R.string.update));
					vh.btnNotice.setOnClickListener(new NoticeOnClickListener(flag));
				} else if (3 == flag) {
					// 更新内部名单
					vh.btnNotice.setText(noticeActivity.getString(R.string.update));
					vh.btnNotice.setOnClickListener(new NoticeOnClickListener(flag));
				}
			} else {
				if (1 == flag) {
					// 上传
					vh.btnNotice.setText(noticeActivity.getString(R.string.upload));
					vh.btnNotice.setOnClickListener(new CstOnClickListener(s, flag));
					long ff = ma.dbService.feedUnUploadCounts(s.surveyId, ma.userId);
					if (0 == ff) {
						vh.btnNotice.setBackgroundDrawable(noticeActivity.getResources().getDrawable(R.drawable.bg_skin_navbar_none));
						vh.btnNotice.setOnClickListener(null);
					}
				} else if (2 == flag) {
					// 更新问卷
					vh.btnNotice.setText(noticeActivity.getString(R.string.update));
					vh.btnNotice.setOnClickListener(new CstOnClickListener(s, flag,vh.pbDownload, vh.btnNotice));
				} else if (3 == flag) {
					// 更新内部名单
					vh.btnNotice.setText(noticeActivity.getString(R.string.update));
					vh.btnNotice.setOnClickListener(new CstOnClickListener(s, flag,vh.pbDownload, vh.btnNotice));
				}
			}
		}
		return convertView;
	}

	class NoticeOnClickListener implements OnClickListener {

		private int flag;

		public NoticeOnClickListener(int flag) {
			this.flag = flag;
		}

		@Override
		public void onClick(View v) {
			switch (this.flag) {
			case 1:
				Toasts.makeText(noticeActivity, R.string.login_upload, Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(noticeActivity, LoginActivity.class);
				noticeActivity.startActivityForResult(intent, 50);
				noticeActivity.overridePendingTransition(R.anim.right, R.anim.left);
				break;
			case 2:
				Toasts.makeText(noticeActivity, R.string.login_survey, Toast.LENGTH_LONG).show();
				Intent intent1 = new Intent();
				intent1.setClass(noticeActivity, LoginActivity.class);
				noticeActivity.startActivityForResult(intent1, 50);
				noticeActivity.overridePendingTransition(R.anim.right, R.anim.left);
				break;
			case 3:
				Toasts.makeText(noticeActivity, R.string.login_inner, Toast.LENGTH_LONG).show();
				Intent intent2 = new Intent();
				intent2.setClass(noticeActivity, LoginActivity.class);
				noticeActivity.startActivityForResult(intent2, 50);
				noticeActivity.overridePendingTransition(R.anim.right, R.anim.left);
				break;
			default:
				break;
			}
		}

	}

	private Survey tempSurvey;

	class CstOnClickListener implements OnClickListener {

		private Survey survey;
		private int flag;

		public CstOnClickListener(Survey s, int flag) {
			this.flag = flag;
			this.survey = s;
		}

		/**
		 * 下载问卷构造函数
		 * @param s
		 * @param flag2
		 * @param pbDownload
		 * @param btnNotice
		 */
		private ProgressBar pbDownload;
		private Button btnNotice;
		public CstOnClickListener(Survey s, int flag, ProgressBar pbDownload, Button btnNotice) {
			this.flag = flag;
			this.survey = s;
			this.pbDownload=pbDownload;
			this.btnNotice=btnNotice;
		}

		@Override
		public void onClick(View v) {
			switch (this.flag) {
			case 1:
				// 打开进度条
				onCreateDialog(DIALOG_DOWNLOAD_PROGRESS);
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				if (!NetUtil.checkNet(noticeActivity)) {
					Toasts.makeText(noticeActivity, R.string.exp_net, Toast.LENGTH_SHORT).show();
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
					return;
				}
				// 计算拼串
				String mySid = "(";
				if (null != survey) {
					mySid += "_SurveyId=" + survey.surveyId + ")";
				} else {
					mySid = "";
					Toasts.makeText(noticeActivity, R.string.no_reason_reupload, Toast.LENGTH_LONG).show();
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
					return;
				}
				// 计算拼串结束
				ArrayList<UploadFeed> xmlTempFs = ma.dbService.getAllSurveysCompletedUploadFeed(ma.userId, mySid);
				for (UploadFeed tenpFeed : xmlTempFs) {
					// 先把要上传的文件置为1
					ma.dbService.updateFeedStatus(tenpFeed.getUuid(), Cnt.UPLOAD_STATE_UPLOADED);
					// 假如原先是已上传中 争议
					if (2 == tenpFeed.getIsUploaded()) {
						ma.dbService.updateFeedStatusByName(tenpFeed.getName(), 2);
					}
				}
				ma.dbService.updateFeedStatusBySurveys(ma.userId, 2, mySid);
				// 非XML的大小
				long sizeSum = ma.dbService.getSum(ma.userId);
				ArrayList<UploadFeed> xmlFs = null;
				// 直接上传XML。
				xmlFs = ma.dbService.getAllCompletedSurveysUploadFeedIpsos(mySid, ma.userId);
				if (Util.isEmpty(xmlFs)&&sizeSum > 0) {
					noticeActivity.dismiss();
					dialog.show();
					//Toasts.makeText(noticeActivity, R.string.finish_survey_starts, Toast.LENGTH_SHORT).show();
					tempSurvey = survey;
					prepareUploadRecord();
					notifyDataSetChanged();
				} else {
					noticeActivity.show();
					if (Util.isEmpty(xmlFs)) {
						Toasts.makeText(noticeActivity, R.string.null_upload, Toast.LENGTH_SHORT).show();
						noticeActivity.dismiss();
					} else {
						uploadFile(xmlFs, survey);
					}
				}
				break;
			case 2:
				if (NetUtil.checkNet(noticeActivity)) {
					new AuthorDownloadTask(this.survey, this.pbDownload, this.btnNotice).execute();
				} else {
					Toasts.makeText(noticeActivity, R.string.exp_net, Toast.LENGTH_LONG).show();
				}
				break;
			case 3:
				//内部名单开始
				if (null == ma.cfg) {
					ma.cfg = new Config(noticeActivity);
				}
				if (Util.isEmpty(ma.userId)) {
					ma.userId = ma.cfg.getString("UserId", "");
				}
				String authorId = ma.cfg.getString("authorId", "");

				if (Util.isEmpty(ma.userId) || Util.isEmpty(authorId)) {
					Toasts.makeText(noticeActivity, R.string.app_data_invalidate, Toast.LENGTH_LONG).show();
					return;
				}
				new InnerTask(authorId, ma.userId, survey, btnNotice,pbDownload).execute();
				break;
			default:
				break;
			}
		}

	}
	
	//下载内部名单方法开始
	//内部名单方法
		private final class InnerTask extends AsyncTask<Void, Integer, Boolean> {

			private String authorId;
			private String userId;
			private Survey survey;

			private Button btn;
			private ProgressBar pb;//进度条

			public InnerTask(String _authorId, String _userId, Survey _survey, Button btn,ProgressBar pb) {
				this.authorId = _authorId;
				this.userId = _userId;
				this.survey = _survey;
				this.btn = btn;
				this.pb=pb;
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put("AuthorID", this.authorId);
					hm.put("SurveyID", survey.surveyId);
					InputStream inStream = NetService.openUrl(Cnt.INNER_URL, hm, "GET");
					OpenStatus os = XmlUtil.ParserInnerPanelList(inStream);
					// 遍历下载的内部名单panel号
					ArrayList<String> spList = new ArrayList<String>();
					for (int i = 0; i < os.getIps().size(); i++) {
						InnerPanel ip = os.getIps().get(i);
						//引用受访者参数
						ParameterInnerPanel pip=os.getParameterIps().get(i);
						ArrayList<Parameter> parameters = pip.getParameters();
						pip.setParameters(parameters);
						//引用受访者参数结束
						//命名规则开始
						//不为空判断
						String content="";
						if(!Util.isEmpty(os.getParameterName())){
							for (Parameter parameter : parameters) {
								if (parameter.getSid().equals(os.getParameterName())) {
									content=parameter.getContent();
									//是中文的就置为空
									if(Util.isContainChinese(content)){
										content="";
									}
									break;
								}
							}
						}
						//命名规则结束
						if (!Util.isEmpty(ip.getPanelID())) {
							spList.add(ip.getPanelID());
							String str = XmlUtil.paserInnerPanel2Json(ip);
							// System.out.println("inner_json==="+str);
							if (ma.dbService.isFeedExist(survey.surveyId, ip.getPanelID())) {
								// 假如服务器上有id 本地上也有id的更新。引用受访者参数
//								System.out.println("更新");
								ma.dbService.updateInnerUploadFeed(survey.surveyId, ip.getPanelID(), str, ip.getFeedId(),pip.getParametersStr());
							} else {
								// 假如服务器上有id 本地上没有id的增加。
								String uuid = UUID.randomUUID().toString();
								String path = Util.getXmlPath(noticeActivity, survey.surveyId);
								// 增加pid  命名规则
								String name = Util.getXmlName(userId, survey.surveyId, uuid, ip.getPanelID(),content);
								ma.dbService.addInnerUploadFeed(ip.getFeedId(), userId, survey.surveyId, uuid, System.currentTimeMillis(), path, name, survey.visitMode, str, ip.getPanelID(),pip.getParametersStr());
							}
						}
						//进度更新
						publishProgress((i + 1), os.getIps().size());
					}
					ArrayList<String> dbList = ma.dbService.getListBySurveyId(survey.surveyId,ma.userId);
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
//					if (!Util.isEmpty(dbList)) {
//						for(int z=0;z<dbList.size();z++){
//							String deleteDB=dbList.get(z);
//							String panelId = deleteDB.split(":::")[0];
//							String path = deleteDB.split(":::")[1];
//							File file = new File(path);
//							int giveUp = 0;
//							// 存在还能做
//							if (file.exists()) {
//								giveUp = 2;
//								ma.dbService.updateGiveUpByPanelId(panelId, survey.surveyId, giveUp);
//							}
//							// 不存在设置为放弃,查不出来
//							else {
//								giveUp = 1;
//								ma.dbService.updateGiveUpByPanelId(panelId, survey.surveyId, giveUp);
//							}
//							//进度更新
//							publishProgress((z + 1), dbList.size());
//						}
//					}
					//命名规则更改
					ma.dbService.updateSurveyOpenStatus(survey.surveyId, os.getParameter1(), os.getParameter2(), os.getParameter3(), os.getParameter4(),os.getParameterName());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPreExecute() {
				btn.setVisibility(View.GONE);
				pb.setVisibility(View.VISIBLE);
				pb.setProgress(0);
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					ma.dbService.updateSurveyInnerDone(survey.surveyId);
					btn.setVisibility(View.VISIBLE);
					btn.setBackgroundDrawable(noticeActivity.getResources().getDrawable(R.drawable.bg_skin_navbar_none));
					btn.setClickable(false);
					pb.setVisibility(View.GONE);
				} else {
					btn.setVisibility(View.VISIBLE);
					pb.setVisibility(View.GONE);
					Toasts.makeText(noticeActivity, R.string.inner_failure, Toast.LENGTH_LONG).show();
				}
			}
			
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				if (0 != values[1]) {
					pb.setProgress((int) (values[0] * 1000) / values[1]);
				}
			}
		}
	//下载内部名单方法结束
	
	//下载项目方法开始
	private class AuthorDownloadTask extends AsyncTask<Void, Integer, Boolean> {
		private Survey s;
		private ProgressBar pb;
		private Button btn;
		SurveyQuestion sq;

		public AuthorDownloadTask(Survey survey, ProgressBar progressBar, Button btn) {
			this.s = survey;
			this.pb = progressBar;
			this.btn = btn;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean yes = false;
			try {
				Log.i("zrl1", s.downloadUrl+"downloadUrl:");
				HttpBean hb = NetService.obtainHttpBean(s.downloadUrl, null, "GET");
				if (200 == hb.code) {
					File file = new File(Util.getSurveySaveFilePath(noticeActivity), s.surveyId + ".zip");
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
					yes = Util.decompress(file.getAbsolutePath(), Util.getSurveyFilePath(noticeActivity, s.surveyId), s.surveyId, new Call() {
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
							String xml = Util.getSurveyXML(noticeActivity, s.surveyId);
							FileInputStream inStream = new FileInputStream(xml);
							//数据字典
							ArrayList<String> classIdList=new ArrayList<String>();
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
							xml = Util.getSurveyIntervention(noticeActivity, s.surveyId);
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
//									hmData.put("classId", classId);
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
				ma.dbService.updateSurvey(this.s, 0);// 更新名单
				pb.setVisibility(View.GONE);
				btn.setVisibility(View.VISIBLE);
				btn.setBackgroundDrawable(noticeActivity.getResources().getDrawable(R.drawable.bg_skin_navbar_none));
				
				btn.setOnClickListener(null);
				ma.dbService.surveyDownloaded(s.surveyId, (null == sq) ? -1 : sq.getEligible(), (null == sq) ? "" : sq.getWord());
			}
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (0 != values[1]) {
				pb.setProgress((int) (values[0] * 1000) / values[1]);
			}
		}

		@Override
		protected void onPreExecute() {
			btn.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			super.onPreExecute();
		}
	};
	//下载项目方法结束
	
	//上传方法开始
	private void uploadFile(ArrayList<UploadFeed> xmlFs, Survey survey) {
		if (Util.isEmpty(xmlFs)) {
			return;
		}
		UploadFeed feed = xmlFs.get(0);
		if (null == feed) {
			// 继续下一个
			xmlFs.remove(0);
			uploadFile(xmlFs, survey);
			return;
		}
		if (Util.isEmpty(ma.userId)) {
			ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(noticeActivity)) : (ma.cfg)).getString("UserId", "");
		}
		new UpLoadFileTask(feed, xmlFs, survey).execute(ma.userId, MD5.Md5Pwd(ma.userPwd), feed.getSurveyId(), feed.getPath(), feed.getName(), Cnt.UPLOAD_URL);

	}

	private class UpLoadFileTask extends AsyncTask<String, String, HashMap<String, String>> {
		private UploadFeed feed;
		private ArrayList<UploadFeed> xmlFs;
		private Survey survey;

		public UpLoadFileTask(UploadFeed f, ArrayList<UploadFeed> xmlFs, Survey survey) {
			feed = f;
			this.xmlFs = xmlFs;
			this.survey = survey;
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
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(is);
				Element element = document.getDocumentElement();
				state = element.getElementsByTagName("S").item(0).getFirstChild().getNodeValue();
				rMap.put("S", state);
				System.out.println("解析之后的状态--->" + state);
				if ("100".equals(state)) {
					fid = element.getElementsByTagName("FID").item(0).getFirstChild().getNodeValue();
					rtp = element.getElementsByTagName("RTP").item(0).getFirstChild().getNodeValue();
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
				noticeActivity.dismiss();
				return;
			}
			String stStr = rMap.get("S");
			int state = 0;
			if (stStr != null)
				state = Integer.parseInt(stStr);
			// System.out.println("XML上传后服务器返回的状态码--->"+state);
			if (state == 100) {
				String fid = rMap.get("FID");
				String rtp = rMap.get("RTP");
				upLoadSuccess(feed, fid, rtp, survey);
				ma.dbService.upDateFeedId(feed);
				notifyDataSetChanged();
			} else {
				// System.out.println("UpLoadFileTask--->onPostExecute--->state != 100");
				upLoadError(feed.getId(), state, feed);
			}
			xmlFs.remove(0);
			if (0 != xmlFs.size()) {
				uploadFile(xmlFs, survey);
			} else {
				noticeActivity.dismiss();
				long sizeSum = ma.dbService.getSum(ma.userId);
				if (sizeSum > 0) {
					dialog.show();
					// Toasts.makeText(noticeActivity,
					// R.string.finish_survey_starts,
					// Toast.LENGTH_SHORT).show();
					tempSurvey = survey;
					prepareUploadRecord();
					notifyDataSetChanged();
				}
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			notifyDataSetChanged();
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
	private void upLoadSuccess(UploadFeed feed, String fid, String rtp, Survey survey) {
		if (null != feed) {
			feed.setFeedId(fid);
			feed.setReturnType(rtp);
			/**
			 * 上传状态设置为9,
			 */
			feed.setIsUploaded(9);
			ma.dbService.updateUploadFeedStatus(feed);
			// 计算拼串
			String mySid = "(_SurveyId=" + survey.surveyId + ")";
			// 计算拼串结束
			if (ma.dbService.feedUnUploadSurveysCountIpsos(mySid, ma.userId) == 0) {
				Toasts.makeText(noticeActivity, noticeActivity.getResources().getString(R.string.finish_surveys), Toast.LENGTH_SHORT).show();
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
								// System.out.println("surveyID:"+myRecoder.getSurveyId()+",feedID:"+myRecoder.getFeedId()+",count:"+myRecoder.getCount());
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
									// if(r==reList.size()-1){
									// handler.sendEmptyMessage(999);
									// }
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

	private void upLoadError(long id, int state, UploadFeed feed) {
		if (0 == state) {
			Toasts.makeText(noticeActivity.getApplicationContext(), R.string.err_net, Toast.LENGTH_LONG).show();
		} else if (95 == state) {
			ma.dbService.giveUpFeed(feed.getUuid(), feed.getSurveyId());
			notifyDataSetChanged();
			return;
		} else {
			Toasts.makeText(noticeActivity.getApplicationContext(), R.string.err_upass, Toast.LENGTH_LONG).show();
		}
		notifyDataSetChanged();
	}

	// 上传结束
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog dialog = null;
	private boolean isPause;// false是没暂停
	private Button button;
	private boolean isNet = true;// 判断是断网 还是暂停 true是默认状态,false是暂停

	protected void dismissDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			if (dialog != null) {
				dialog.dismiss();
			}
			break;
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			dialog = new ProgressDialog(noticeActivity);
			dialog.setMessage("uploading…");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.show();
			button = new Button(noticeActivity);
			// zz添加
			button.setBackgroundResource(android.R.drawable.dialog_frame);
			button.setText(noticeActivity.getResources().getString(R.string.pause));
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
						if (!NetUtil.checkNet(noticeActivity)) {
							Toasts.makeText(noticeActivity, R.string.exp_net, Toast.LENGTH_SHORT).show();
							dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
						} else {
							btn.setText(noticeActivity.getResources().getString(R.string.pause));
							reStartUploadTask();
							dialog.setCancelable(false);
						}
					} else {
						isNet = false;// 不是断网
						if (!NetUtil.checkNet(noticeActivity)) {
							Toasts.makeText(noticeActivity, R.string.exp_net, Toast.LENGTH_SHORT).show();
							dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
						} else {
							btn.setText(noticeActivity.getResources().getString(R.string._continue));
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
		System.out.println("停止");
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
					e.printStackTrace();
				}
			};
		}.start();
	}

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
					if (myFtp.ftpClient.deleteFile(remoteFileName)) {
						result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
					} else {
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
						boolean connect = myFtp.connect(Cnt.RECORD_PHOTO_URL, Cnt.ftpPort, Cnt.ftpName, Cnt.ftpPwd);
						if (!connect) {
							Message msg = handler.obtainMessage();
							msg.what = 67;
							msg.arg1 = R.string.ftp_conect_fail;
							handler.sendMessage(msg);
							return;
						}
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
			// 上传文件
			isLast = false;
			// System.out.println("fs.size()" + fs.size());
			ArrayList<UploadFeed> fs = ma.dbService.getRecordList(ma.userId);
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
					Log.e("MM", "************start**************");
					File f = new File(path, fileName);
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
					String ftpPath = File.separator + sid + File.separator + feedId + File.separator + fileName;
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
					if (e instanceof SocketException) {
						isLast = true;
						System.out.println("暂停");
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
				//handler.sendEmptyMessageDelayed(4000, 200);
				notifyDataSetChanged();
				Toasts.makeText(noticeActivity, R.string.finish_survey_others, Toast.LENGTH_SHORT).show();
			}
			if (!isLast) {
				System.out.println("发消息");
				handler.sendEmptyMessageDelayed(3, 200);
			} else {

			}
			super.onPostExecute(result);
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
				Toasts.makeText(noticeActivity, msg.arg1, Toast.LENGTH_LONG).show();
				if (msg.arg1 == R.string.exp_net || msg.arg1 == R.string.no_reason_reupload_exit) {
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				}
				break;
			case 68:
				Toasts.makeText(noticeActivity, msg.arg1, Toast.LENGTH_LONG).show();
				break;
			case 2000:// 开始上传,初始化进度条
				sum = msg.arg2;// 个数
				sum_1 = msg.arg1;// 总大小
				dialog.setMax(sum);
				break;

			case 10000:// 网络中断
				Toasts.makeText(noticeActivity, noticeActivity.getResources().getString(R.string.exp_net), Toast.LENGTH_SHORT).show();
				stopUploadTask();
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				break;

			case 0:
				break;

			case 1:
				dialog.setProgress(tmp);
				break;
			// 上传xml
			case 4000:
				if (!NetUtil.checkNet(noticeActivity)) {
					Toasts.makeText(noticeActivity, R.string.exp_net, Toast.LENGTH_SHORT).show();
					return;
				}
				noticeActivity.show();
				// 非XML的大小
				long sizeSum = ma.dbService.getSum();
				if (sizeSum == 0) {

					// 计算拼串
					String mySid = "(";
					if (null != tempSurvey) {
						mySid += "_SurveyId=" + tempSurvey.surveyId + ")";
					} else {
						mySid = "";
						Toasts.makeText(noticeActivity, R.string.no_reason_reupload, Toast.LENGTH_LONG).show();
						noticeActivity.dismiss();
						return;
					}
					// 计算拼串结束
					ArrayList<UploadFeed> xmlFs = null;
					// 直接上传XML。
					xmlFs = ma.dbService.getAllCompletedSurveysUploadFeedIpsos(mySid, ma.userId);
					if (Util.isEmpty(xmlFs)) {
						Toasts.makeText(noticeActivity, R.string.null_upload, Toast.LENGTH_SHORT).show();
						noticeActivity.dismiss();
					} else {
						Toasts.makeText(noticeActivity, R.string.start_upload_survey, Toast.LENGTH_SHORT).show();
						uploadFile(xmlFs, tempSurvey);
					}
				} else {
					noticeActivity.dismiss();
					Toasts.makeText(noticeActivity, R.string.re_upload, Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	};
	//上传方法结束

	private final class ViewHolder {
		/**
		 * 项目名称
		 */
		private TextView tvSurveyTitle;
		/**
		 * 按钮
		 */
		private Button btnNotice;
		/**
		 * 下载进度
		 */
		private ProgressBar pbDownload;
	}

}
