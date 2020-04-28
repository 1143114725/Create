package cn.dapchina.newsupper.adapter;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Call;
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
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;
import cn.dapchina.newsupper.view.Toasts;

public class SubscibeAdapter extends BaseAdapter {

	private ArrayList<Survey> ss;
	private Context context;
	private MyApp ma;
	private ArrayList<String> list = new ArrayList<String>();

	@Override
	public int getCount() {
		return ss.size();
	}
	public SubscibeAdapter(Context context, ArrayList<Survey> list, MyApp ma) {
		super();
		this.ss = list;
		this.context = context;
		this.ma = ma;
	}

	@Override
	public Object getItem(int position) {
		return ss.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void refresh(ArrayList<Survey> surveys) {
		if (!Util.isEmpty(surveys)) {
			if (!Util.isEmpty(ss)) {
				ss.clear();
				ss.addAll(surveys);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		convertView = LayoutInflater.from(context).inflate(R.layout.subscibe_item, null);
		vh = new ViewHolder();
		vh.tvSurveyTitle = (TextView) convertView.findViewById(R.id.textView1);
		vh.tvStartTime = (TextView) convertView.findViewById(R.id.textView2);
		vh.tvContent = (TextView) convertView.findViewById(R.id.textView3);
		vh.subscibe_ll = (LinearLayout) convertView.findViewById(R.id.subscibe_ll);
		vh.iv_ll = (LinearLayout) convertView.findViewById(R.id.iv_ll);
		vh.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
		vh.pbDownload = (ProgressBar) convertView.findViewById(R.id.author_list_progress);
		Drawable drawable = context.getResources().getDrawable(R.drawable.dark_gray_background_subscibe);
		if (position % 2 == 1) {
			vh.subscibe_ll.setBackgroundDrawable(drawable);
		}
		Survey s = ss.get(position);
		if (null != s) {
			if (!Util.isEmpty(s.surveyTitle)) {
				if (1 == s.openStatus) {
					String tempStr = s.surveyTitle + context.getString(R.string.inner_name);
					SpannableStringBuilder style = new SpannableStringBuilder(tempStr);
					style.setSpan(new ForegroundColorSpan(Color.RED), s.surveyTitle.length(),tempStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置文字的颜色
					vh.tvSurveyTitle.setText(style);
				} else {
					vh.tvSurveyTitle.setText(s.surveyTitle);
				}
			}
			//图标更换
			if(s.isDowned==1){
				vh.iv_add.setImageResource(R.drawable.ic_checkmark_holo_light);
			}
			
			if (!Util.isEmpty(s.getGeneratedTime())) {
				vh.tvStartTime.setText(s.getGeneratedTime());
			}
			if (!Util.isEmpty(s.getWord())) {
				Spannable sp = (Spannable) Html.fromHtml(s.getWord());
				vh.tvContent.setText(sp.toString());
			}else{
				vh.tvContent.setText(R.string.no_explain);
			}
			vh.iv_ll.setOnClickListener(new DownloadListenner(s, vh.pbDownload, vh.iv_add));
		}
		return convertView;
	}

	private class DownloadListenner implements OnClickListener {
		private Survey s;
		private ProgressBar pb;
		private ImageView iv;

		public DownloadListenner(Survey survey, ProgressBar progressBar, ImageView iv_add) {
			this.s = survey;
			this.pb = progressBar;
			this.iv = iv_add;
		}

		@Override
		public void onClick(View v) {
			if (NetUtil.checkNet(context)) {
				new AuthorDownloadTask(s, pb, iv).execute();
			} else {
				Toasts.makeText(context, R.string.exp_net, Toast.LENGTH_LONG).show();
			}
		}

	}

	private class AuthorDownloadTask extends AsyncTask<Void, Integer, Boolean> {
		private Survey s;
		private ProgressBar pb;
		private ImageView iv;
		SurveyQuestion sq;

		public AuthorDownloadTask(Survey survey, ProgressBar progressBar, ImageView iv) {
			this.s = survey;
			this.pb = progressBar;
			this.iv = iv;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean yes = false;
			try {
				Log.i("zrl1", s.downloadUrl+"downloadUrl:");
				HttpBean hb = NetService.obtainHttpBean(s.downloadUrl, null, "GET");
				if (200 == hb.code) {
					File file = new File(Util.getSurveySaveFilePath(context), s.surveyId + ".zip");
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
					yes = Util.decompress(file.getAbsolutePath(), Util.getSurveyFilePath(context, s.surveyId), s.surveyId, new Call() {

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
							String xml = Util.getSurveyXML(context, s.surveyId);
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
							xml = Util.getSurveyIntervention(context, s.surveyId);
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
				pb.setVisibility(View.GONE);
				iv.setVisibility(View.VISIBLE);
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
				//图标更换
				s.isDowned=1;
				iv.setImageResource(R.drawable.ic_checkmark_holo_light);
				if (-1 == list.indexOf(s.surveyId)) {
					list.add(s.surveyId);
				}
				
				if (1 == s.openStatus) {
					//内部名单开始
					if (null == ma.cfg) {
						ma.cfg = new Config(context);
					}
					if (Util.isEmpty(ma.userId)) {
						ma.userId = ma.cfg.getString("UserId", "");
					}
					String authorId = ma.cfg.getString("authorId", "");

					if (Util.isEmpty(ma.userId) || Util.isEmpty(authorId)) {
						Toasts.makeText(context, R.string.app_data_invalidate, Toast.LENGTH_LONG).show();
						return;
					}
					new InnerTask(authorId, ma.userId, s, iv,pb).execute();
				}
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
			iv.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			super.onPreExecute();
		}
	};
	
	//内部名单方法
	private final class InnerTask extends AsyncTask<Void, Integer, Boolean> {

		private String authorId;
		private String userId;
		private Survey survey;

		private ImageView iv;
		private ProgressBar pb;//进度条

		public InnerTask(String _authorId, String _userId, Survey _survey, ImageView iv,ProgressBar pb) {
			this.authorId = _authorId;
			this.userId = _userId;
			this.survey = _survey;
			this.iv = iv;
			this.pb=pb;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// http://www.dapchina.cn/newsurvey/alisoft/DownloadUser.asp?AuthorID=1514&SurveyID=3076
			try {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("AuthorID", this.authorId);
				hm.put("SurveyID", survey.surveyId);
				InputStream inStream = NetService.openUrl(Cnt.INNER_URL, hm, "GET");
//				InputStream inStream =mContext.getAssets().open("ceshi.xml");
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
						if (ma.dbService.isFeedExist(survey.surveyId, ip.getFeedId())) {
							// 假如服务器上有id 本地上也有id的更新。引用受访者参数
//							System.out.println("更新");
							ma.dbService.updateInnerUploadFeed(survey.surveyId, ip.getPanelID(), str, ip.getFeedId(),pip.getParametersStr());
						} else {
							// 假如服务器上有id 本地上没有id的增加。
							String uuid = UUID.randomUUID().toString();
							String path = Util.getXmlPath(context, survey.surveyId);
							// 增加pid  命名规则
							String name = Util.getXmlName(userId, survey.surveyId, uuid, ip.getPanelID(),content);
//							System.out.println("feedId=" + ip.getFeedId() + ", uuid=" + uuid + ", path=" + path + ", name=" + name + ", userId=" + userId + ", survey.surveyId=" + survey.surveyId);
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
//				if (!Util.isEmpty(dbList)) {
//					for(int z=0;z<dbList.size();z++){
//						String deleteDB=dbList.get(z);
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
//						//进度更新
//						publishProgress((z + 1), dbList.size());
//					}
//				}
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
			iv.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				ma.dbService.updateSurveyInnerDone(survey.surveyId);
				iv.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
			} else {
				iv.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				Toasts.makeText(context, R.string.inner_failure, Toast.LENGTH_LONG).show();
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
	
	

	static class ViewHolder {
		/**
		 * 项目名称
		 */
		private TextView tvSurveyTitle;
		/**
		 * 开始时间
		 */
		private TextView tvStartTime;
		/**
		 * 详细说明
		 */
		private TextView tvContent;
		/**
		 * 颜色布局
		 */
		private LinearLayout subscibe_ll;
		/**
		 * 添加订阅
		 */
		private LinearLayout iv_ll;
		/**
		 * 下载进度
		 */
		private ProgressBar pbDownload;
		/**
		 * 添加按钮
		 */
		private ImageView iv_add;
	}

}
