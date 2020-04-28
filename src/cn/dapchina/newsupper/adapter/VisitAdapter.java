package cn.dapchina.newsupper.adapter;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.ChoiceModeActivity;
import cn.dapchina.newsupper.activity.LogoutDialogActivity;
import cn.dapchina.newsupper.activity.VisitActivity;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.ReturnType;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.service.FeedXml;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.CustomDialog;
import cn.dapchina.newsupper.view.Toasts;

public class VisitAdapter extends BaseAdapter {

	private VisitActivity mContext;
	private ArrayList<UploadFeed> fs;
	private LayoutInflater inflater;

	private Double lat, lng;

	private String addr;

	private Survey survey;

	@Override
	public int getCount() {
		return fs.size();
	}
	public int getCompletedCount() {
		int counts=0;
		for(UploadFeed uf:fs){
			if(1==uf.getIsCompleted()){
				counts++;
			}
		}
		return counts;
	}
	public int getUploadedCount() {
		int counts=0;
		for(UploadFeed uf:fs){
			if(9==uf.getIsUploaded()){
				counts++;
			}
		}
		return counts;
	}
	public int getUnUploadedCount() {
		return getCompletedCount()-getUploadedCount();
	}
	
	
	public VisitAdapter(VisitActivity _c, ArrayList<UploadFeed> feeds, Double lat, Double lng, String address, Survey _survey) {
		this.mContext = _c;
		this.fs = feeds;
		this.lat = lat;
		this.lng = lng;
		this.addr = address;
		this.survey = _survey;
		inflater = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getItem(int position) {
		return fs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.visit_item, null);
			vh = new ViewHolder();
			vh.tvLocalId = (TextView) convertView.findViewById(R.id.local_id);
			vh.innerLL = (LinearLayout) convertView.findViewById(R.id.innerLL);
			vh.llTo1 = (LinearLayout) convertView.findViewById(R.id.llTo1);
			vh.llTo2 = (LinearLayout) convertView.findViewById(R.id.llTo2);
			vh.llTo3  = (LinearLayout) convertView.findViewById(R.id.llTo3);
			vh.llTo4  = (LinearLayout) convertView.findViewById(R.id.llTo4);
			vh.tvEndTime = (TextView) convertView.findViewById(R.id.end_time_tv);
			vh.tvState = (TextView) convertView.findViewById(R.id.visit_state_tv);
			vh.visitLL=(LinearLayout) convertView.findViewById(R.id.visitLL);//数据背景
			/**
			 * 内部名单start
			 */
			vh.tvAddTo1 = (TextView) convertView.findViewById(R.id.addto1_tv);
			vh.tvAddTo2 = (TextView) convertView.findViewById(R.id.addto2_tv);
			vh.tvAddTo3 = (TextView) convertView.findViewById(R.id.addto3_tv);
			vh.tvAddTo4 = (TextView) convertView.findViewById(R.id.addto4_tv);

			vh.tvTo1 = (TextView) convertView.findViewById(R.id.tvTo1);
			vh.tvTo2 = (TextView) convertView.findViewById(R.id.tvTo2);
			vh.tvTo3 = (TextView) convertView.findViewById(R.id.tvTo3);
			vh.tvTo4 = (TextView) convertView.findViewById(R.id.tvTo4);

			// 删除匿名数据
			vh.visit_ll = (LinearLayout) convertView.findViewById(R.id.visit_ll);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		UploadFeed feed = fs.get(position);
		if (null != feed) {
			if (1 == survey.openStatus) {
				HashMap<String, Parameter> hm = feed.getInnerPanel().getPsMap();
				vh.tvAddTo1.setText(null != hm.get("Parameter1") ? hm.get("Parameter1").getContent() : "");
				vh.tvAddTo2.setText(null != hm.get("Parameter2") ? hm.get("Parameter2").getContent() : "");
				vh.tvAddTo3.setText(null != hm.get("Parameter3") ? hm.get("Parameter3").getContent() : "");
				vh.tvAddTo4.setText(null != hm.get("Parameter4") ? hm.get("Parameter4").getContent() : "");
				vh.tvTo1.setText(survey.getParameter1() + ":");
				vh.tvTo2.setText(survey.getParameter2() + ":");
				vh.tvTo3.setText(survey.getParameter3() + ":");
				vh.tvTo4.setText(survey.getParameter4() + ":");
			} else {
				vh.innerLL.setVisibility(View.GONE);
			}
			vh.llTo1.setVisibility(View.VISIBLE);
			vh.llTo2.setVisibility(View.VISIBLE);
			vh.llTo3.setVisibility(View.VISIBLE);
			vh.llTo4.setVisibility(View.VISIBLE);
			if(Util.isEmpty(vh.tvAddTo1.getText())){
				vh.llTo1.setVisibility(View.INVISIBLE);
			}
			if(Util.isEmpty(vh.tvAddTo2.getText())){
				vh.llTo2.setVisibility(View.INVISIBLE);
			}
			if(Util.isEmpty(vh.tvAddTo3.getText())){
				vh.llTo3.setVisibility(View.INVISIBLE);
			}
			if(Util.isEmpty(vh.tvAddTo4.getText())){
				vh.llTo4.setVisibility(View.INVISIBLE);
			}
			// 删除匿名数据
			vh.visit_ll.setOnLongClickListener(new CstOnLongClick(feed));
			vh.visit_ll.setOnClickListener(new OnCstClickListener(feed) {
				
			});
			
			vh.tvLocalId.setText(String.valueOf(feed.getId()));
			vh.tvEndTime.setText(0 < feed.getRegTime() ? Util.getTime(feed.getRegTime(), 7) : "");
			if (feed.getIsUploaded() == 9) {
				vh.visitLL.setBackgroundResource(R.drawable.visit_complete_background);
			} else {
				vh.visitLL.setBackgroundResource(R.drawable.visit_background);
			}

			// 大树拒访 如果返回码为0 正常 否则 拒访或则其他状态
			if (feed.getReturnTypeId() == 0 || feed.getReturnTypeId() == -2) {
				switch (feed.getIsCompleted()) {
				case Cnt.VISIT_STATE_NOACCESS:
					vh.tvState.setText(mContext.getResources().getString(R.string.visit_state_noaccess));
					break;

				case Cnt.VISIT_STATE_INTERRUPT:
					vh.tvState.setText(mContext.getResources().getString(R.string.visit_state_interrupt));
					break;

				case Cnt.VISIT_STATE_COMPLETED:
					vh.tvState.setText(mContext.getResources().getString(R.string.visit_state_completed));
					break;
				}
			} else {
				ArrayList<ReturnType> rlist = survey.getRlist();
				String returnState = getReturnState(feed.getReturnTypeId(), rlist);
				vh.tvState.setText(mContext.getResources().getString(R.string.no_fang, returnState));
			}
			// 大树拒访

		}
		return convertView;
	}
	class OnCstClickListener implements OnClickListener {
		private UploadFeed feed;

		public OnCstClickListener() {

		}

		public OnCstClickListener(UploadFeed feed) {
			this.feed = feed;
		}

		@Override
		public void onClick(View v) {
			Intent it = new Intent(v.getContext(), ChoiceModeActivity.class);
			it.putExtra("uf", feed);
			it.putExtra("survey", survey);
			mContext.startActivity(it);
		}
	}

	
	
	// 删除匿名数据
	class CstOnLongClick implements OnLongClickListener {
		private UploadFeed feed;

		public CstOnLongClick() {

		}

		public CstOnLongClick(UploadFeed feed) {
			this.feed = feed;
		}

		@Override
		public boolean onLongClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(mContext.getString(R.string.delete) + feed.getId() + mContext.getString(R.string.notes));
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					MyApp ma = (MyApp) mContext.getApplication();
					// 获取该uuid的所有数据
					ArrayList<UploadFeed> recordList = ma.dbService.getRecordList(feed.getUuid(), feed.getSurveyId());
					ArrayList<Long> idList = new ArrayList<Long>();
					long id = feed.getId();
					idList.add(id);
					if (!Util.isEmpty(recordList)) {
						for (UploadFeed up : recordList) {
							idList.add(up.getId());
						}
					}
					if (0 < feed.getIsUploaded()) {
						// 假如上传就是假删除
						ma.dbService.deleteFakeUploadFeed(idList);
					} else {
						// 假如没有上传就是真删除
						ma.dbService.deleteFakeUploadFeed(idList);
						for (UploadFeed recodeFeed : recordList) {
							String path = recodeFeed.getPath();
							String name = recodeFeed.getName();
							File file=new File(path,name);
							file.delete();
						}
					}
					VisitAdapter.this.fs.remove(feed);
					notifyDataSetChanged();
					dialog.dismiss();
				}
			});

			builder.setNegativeButton(R.string.cancle, new android.content.DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			AlertDialog create = builder.create();
			create.setCancelable(false);
			create.show();
			return false;
		}

	}

	// 访问状态
	private String getReturnState(int returnTypeId, ArrayList<ReturnType> rlist) {
		String returnState = mContext.getString(R.string.no_state);
		for (int i = 0; i < rlist.size(); i++) {
			ReturnType returnType = rlist.get(i);
			if (returnTypeId == returnType.getReturnId()) {
				returnState = returnType.getReturnName();
				break;
			}
		}
		return returnState;
	}

	public void refresh(ArrayList<UploadFeed> feeds, Double lat, Double lng, String address) {
		if (null != lat && null != lng) {
			this.lat = lat;
			this.lng = lng;
		}
		if (!Util.isEmpty(address)) {
			this.addr = address;
		}
		if (!Util.isEmpty(feeds)) {
			if (!Util.isEmpty(fs)) {
				fs.clear();
				fs.addAll(feeds);
			}
		}
		notifyDataSetChanged();
	}

	static class ViewHolder {
		/**
		 * 删除匿名数据
		 */
		private LinearLayout visit_ll;
		private LinearLayout visitLL;

		/**
		 * 项目Id
		 */
		private TextView tvLocalId;
		/**
		 * 问卷的结束时间
		 */
		private TextView tvEndTime;

		/**
		 * 已完成
		 */
		private TextView tvState;

		private LinearLayout innerLL,llTo1,llTo2,llTo3,llTo4;// 内部名单布局

		/**
		 * 内部名单显示的4个属性
		 */
		private TextView tvTo1, tvTo2, tvTo3, tvTo4;
		private TextView tvAddTo1, tvAddTo2, tvAddTo3, tvAddTo4;

	}

}
