package cn.dapchina.newsupper.adapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;

import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.MyQuotaActivity;
import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.Quota;
import cn.dapchina.newsupper.bean.Restriction;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.bean.option;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.Util;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class QuotaAdapter extends BaseAdapter {

	private MyQuotaActivity mContext;
	private ArrayList<Quota> qlist;
	private LayoutInflater inflater;
	private Survey survey;
	private MyApp ma;

	public QuotaAdapter(MyQuotaActivity _c, ArrayList<Quota> qlists, Survey _survey) {
		this.mContext = _c;
		this.qlist = qlists;
		this.survey = _survey;
		inflater = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ma = (MyApp) _c.getApplication();
	}

	@Override
	public int getCount() {
		return qlist.size();
	}

	@Override
	public Object getItem(int position) {
		if (!Util.isEmpty(qlist)) {
			return qlist.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void add(ArrayList<Quota> qlists) {
		if (!Util.isEmpty(qlists)) {
			qlist.addAll(qlists);
			notifyDataSetChanged();
		}
	}

	public void refresh(ArrayList<Quota> quotas) {
		if (!Util.isEmpty(quotas)) {
			if (!Util.isEmpty(qlist)) {
				qlist.clear();
				qlist.addAll(quotas);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.quota_list_item, null);
			vh = new ViewHolder();
			vh.tvQuotaName = (TextView) convertView.findViewById(R.id.quota_name_tv);
			vh.tvQuotasuccess = (TextView) convertView.findViewById(R.id.quota_success_tv);
			vh.tvQuotaText = (TextView) convertView.findViewById(R.id.quota_text_tv);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Quota quota = qlist.get(position);
		if (null != quota) {
			vh.tvQuotaName.setText(quota.getQuotaName());
			int quotaSuccess = getQuotaSuccess(quota.getOptionlist(),survey.surveyId);
			vh.tvQuotasuccess.setText(String.valueOf(quotaSuccess));
			vh.tvQuotaText.setText(quota.getQuotaIns());
		}
		return convertView;
	}

	// 配额成功量获取方法
	private int getQuotaSuccess(ArrayList<option> optionlist,String surveyId) {
		//初始化答案集合变量
		ArrayList<Answer> anlist = new ArrayList<Answer>();
		//初始化合格问题编号集合
		ArrayList<String> answerlist = new ArrayList<String>();
		//最后合格的list
		ArrayList<String> successlist = new ArrayList<String>();
		//用来排除重复选项的list
		ArrayList<String> excludelist = new ArrayList<String>();
		//用来计数
		int success=0;
		if("Access".equals(optionlist.get(0).getType())){
			ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
			fs=ma.dbService.getAllQuotaUploadFeed(surveyId, ma.userId);
			if(fs.size()>0){
				ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
				for (option opt : optionlist) {
					if("Equal".equals(opt.getSymbol())){
						for (UploadFeed uploadFeed : fs) {
							String parametersStr = uploadFeed.getParametersStr();
							if (!Util.isEmpty(uploadFeed.getParametersStr())) {
								parameterList.clear();
								ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
								if (!Util.isEmpty(tParameters)) {
									parameterList.addAll(tParameters);
									for (Parameter parameter : tParameters) {
										if(!Util.isEmpty(parameter.getContent())){
											if(parameter.getSid().equals(opt.getQuestionindex()) ){
												if (Util.isRespondentsMatching(parameter.getContent(),"=", opt.getCondition())) {
													success++;
												}
													
											}
										}
									}
								}
							}
						}
					}
					if("MoreThan".equals(opt.getSymbol())){
						for (UploadFeed uploadFeed : fs) {
							String parametersStr = uploadFeed.getParametersStr();
							if (!Util.isEmpty(uploadFeed.getParametersStr())) {
								parameterList.clear();
								ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
								if (!Util.isEmpty(tParameters)) {
									parameterList.addAll(tParameters);
									for (Parameter parameter : tParameters) {
										if(!Util.isEmpty(parameter.getContent())){
											// 假如是数字
											if (Util.isFormat(parameter.getContent(), 9) && Util.isFormat(opt.getCondition(), 9)) {
												if(Float.parseFloat(parameter.getContent())>Float.parseFloat(opt.getCondition()) && parameter.getSid().equals(opt.getQuestionindex()) ){
													success++;
												}
											}
											// 日期
											else if (parameter.getContent().indexOf("-") != -1 && parameter.getContent().length() > 5) {
												if(parameter.getSid().equals(opt.getQuestionindex())){
													try {
														if (Util.getDateCompare(parameter.getContent(), opt.getCondition(), "<")) {
															success++;
														}
													} catch (ParseException e) {
														e.printStackTrace();
													}
												}
											}
											// 文本格式
											else {
//												if(parameter.getSid().equals(opt.getQuestionindex()) ){
//													if (Util.isRespondentsMatching(parameter.getContent(),"<" ,opt.getCondition() )) {
//														success++;
//													}
//													
//												}
											}
										}
									}
								}
							}
						}
					}
					if("LessThan".equals(opt.getSymbol())){
						for (UploadFeed uploadFeed : fs) {
							String parametersStr = uploadFeed.getParametersStr();
							if (!Util.isEmpty(uploadFeed.getParametersStr())) {
								parameterList.clear();
								ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
								if (!Util.isEmpty(tParameters)) {
									parameterList.addAll(tParameters);
									for (Parameter parameter : tParameters) {
										if(!Util.isEmpty(parameter.getContent())){
											// 假如是数字
											if (Util.isFormat(parameter.getContent(), 9) && Util.isFormat(opt.getCondition(), 9)) {
												if(Float.parseFloat(parameter.getContent())<Float.parseFloat(opt.getCondition()) && parameter.getSid().equals(opt.getQuestionindex()) ){
													success++;
												}
											}
											// 日期
											else if (parameter.getContent().indexOf("-") != -1 && parameter.getContent().length() > 5) {
												if(parameter.getSid().equals(opt.getQuestionindex())){
													try {
														if (Util.getDateCompare(parameter.getContent(), opt.getCondition(), ">")) {
															success++;
														}
													} catch (ParseException e) {
														e.printStackTrace();
													}
												}
											}
											// 文本格式
											else {
//												if(parameter.getSid().equals(opt.getQuestionindex())){
//													if (Util.isRespondentsMatching(parameter.getContent(), ">",opt.getCondition() )) {
//														success++;
//													}
//													
//												}
											}
										}
									}
								}
							}
						}
					}
					if("MoreEqual".equals(opt.getSymbol())){
						for (UploadFeed uploadFeed : fs) {
							String parametersStr = uploadFeed.getParametersStr();
							if (!Util.isEmpty(uploadFeed.getParametersStr())) {
								parameterList.clear();
								ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
								if (!Util.isEmpty(tParameters)) {
									parameterList.addAll(tParameters);
									for (Parameter parameter : tParameters) {
										if(!Util.isEmpty(parameter.getContent())){
											// 假如是数字
											if (Util.isFormat(parameter.getContent(), 9) && Util.isFormat(opt.getCondition(), 9)) {
												if(Float.parseFloat(parameter.getContent())>=Float.parseFloat(opt.getCondition()) && parameter.getSid().equals(opt.getQuestionindex()) ){
													success++;
												}
											}
											// 日期
											else if (parameter.getContent().indexOf("-") != -1 && parameter.getContent().length() > 5) {
												if(parameter.getSid().equals(opt.getQuestionindex())){
													try {
														if (Util.getDateCompare(parameter.getContent(), opt.getCondition(), "<=")) {
															success++;
														}
													} catch (ParseException e) {
														e.printStackTrace();
													}
												}
											}
											// 文本格式
											else {
//												if(parameter.getSid().equals(opt.getQuestionindex()) ){
//													if (Util.isRespondentsMatching(parameter.getContent(), "<=", opt.getCondition())) {
//														success++;
//													}
//													
//												}
											}
										}
									}
								}
							}
						}
					}
					if("LessEqual".equals(opt.getSymbol())){
						for (UploadFeed uploadFeed : fs) {
							String parametersStr = uploadFeed.getParametersStr();
							if (!Util.isEmpty(uploadFeed.getParametersStr())) {
								parameterList.clear();
								ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
								if (!Util.isEmpty(tParameters)) {
									parameterList.addAll(tParameters);
									for (Parameter parameter : tParameters) {
										if(!Util.isEmpty(parameter.getContent())){
											// 假如是数字
											if (Util.isFormat(parameter.getContent(), 9) && Util.isFormat(opt.getCondition(), 9)) {
												if(Float.parseFloat(parameter.getContent())<=Float.parseFloat(opt.getCondition()) && parameter.getSid().equals(opt.getQuestionindex())){
													success++;
												}
											}
											// 日期
											else if (parameter.getContent().indexOf("-") != -1 && parameter.getContent().length() > 5) {
												if(parameter.getSid().equals(opt.getQuestionindex())){
													try {
														if (Util.getDateCompare(parameter.getContent(), opt.getCondition(), ">=")) {
															success++;
														}
													} catch (ParseException e) {
														e.printStackTrace();
													}
												}
											}
											// 文本格式
											else {
//												if(parameter.getSid().equals(opt.getQuestionindex())){
//													if (Util.isRespondentsMatching(parameter.getContent(),">=",  opt.getCondition())) {
//														success++;
//													}
//													
//												}
											}
										}
									}
								}
							}
						}
					}
					//包含
					if("Include".equals(opt.getSymbol())){
						for (UploadFeed uploadFeed : fs) {
							String parametersStr = uploadFeed.getParametersStr();
							if (!Util.isEmpty(uploadFeed.getParametersStr())) {
								parameterList.clear();
								ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
								if (!Util.isEmpty(tParameters)) {
									parameterList.addAll(tParameters);
									for (Parameter parameter : tParameters) {
										if(!Util.isEmpty(parameter.getContent())){
											if(parameter.getSid().equals(opt.getQuestionindex())){
												if (Util.isRespondentsMatching(parameter.getContent(),"2",  opt.getCondition())) {
													success++;
												}
											}
										}
									}
								}
							}
						}
					}
					
				}
			}else {
				
			}
			
			
		}else {
			for (option opt : optionlist) {
				//得到关联的index
				String questionindex=opt.getQuestionindex();
				//根据index和项目ID得到答案集合
				anlist=ma.dbService.getUserAllquotaAnswer(surveyId, questionindex);
				for (Answer answer : anlist) {
					for (int i = 0; i < answer.getAnswerMapArr().size(); i++) {
						if((Integer.parseInt(answer.getAnswerMapArr().get(i).getAnswerValue())+1)==Integer.parseInt(opt.getCondition())){
							answerlist.add(answer.qIndex+"");
						}
					}
				}
			}
			if(optionlist.size()>1 && optionlist.get(0).getMatch().equals("all")){
				int count=0;
				for (int i = 0; i < answerlist.size(); i++) {
					for (int j = 0; j < answerlist.size(); j++) {
						if(answerlist.get(i).equals(answerlist.get(j))){
							count++;
						}
					}
					if(count>=optionlist.size()){
						excludelist.add(answerlist.get(i));
					}
					count=0;
				}
				if(excludelist.size()>=2){
					for(String s: excludelist){
						if(Collections.frequency(successlist, s) < 1) 
							successlist.add(s);
					}
				}
			}else if (optionlist.size()>1 && optionlist.get(0).getMatch().equals("one")) {
				
				for(String s: answerlist){
					if(Collections.frequency(successlist, s) < 1) 
						successlist.add(s);
				}
			}else if (optionlist.size()==1) {
				return optionlist.size();
			}
			
			return successlist.size();
			
		}
		//new isSucsess().execute(optionlist);
		return success;	
	}
	
	static class ViewHolder {

		private TextView tvQuotasuccess;// 配额编号
		private TextView tvQuotaName;// 配额名称
		private TextView tvQuotaText;// 配额说明
	}
	
	private class isSucsess extends AsyncTask<ArrayList<option>, Void, ArrayList<option>>{

		@Override
		protected ArrayList<option> doInBackground(ArrayList<option>... params) {
			ArrayList<option> opList=params[0];
			if(!Util.isEmpty(opList)){
				
				
				
			}
			
			
			return opList;
		
		
		}
		
	}
	
	
	
	
	
}
