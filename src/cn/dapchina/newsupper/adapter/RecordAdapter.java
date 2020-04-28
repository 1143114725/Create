/**
 *
 */
package cn.dapchina.newsupper.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.ChoiceModeActivity;
import cn.dapchina.newsupper.activity.RecordActivity;
import cn.dapchina.newsupper.activity.VisitActivity;
import cn.dapchina.newsupper.adapter.VisitAdapter.CstOnLongClick;
import cn.dapchina.newsupper.adapter.VisitAdapter.OnCstClickListener;
import cn.dapchina.newsupper.adapter.VisitAdapter.ViewHolder;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.ReturnType;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.Util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 张树锦与2016-1-27下午5:08:11TODO
 */
public class RecordAdapter extends BaseAdapter{
	private ArrayList<UploadFeed> vector;
	private LayoutInflater inflater;

	
	public RecordAdapter(RecordActivity _c ,ArrayList<UploadFeed> v) {
		this.vector = v;
		inflater = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public Object getItem(int position) {
		return vector.get(position);
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
			vh.tvEndTime = (TextView) convertView.findViewById(R.id.end_time_tv);
			vh.tvState = (TextView) convertView.findViewById(R.id.visit_state_tv);
			
			vh.innerLL = (LinearLayout) convertView.findViewById(R.id.innerLL);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		UploadFeed feed = vector.get(position);
		if(feed!=null){
			vh.tvLocalId.setText(String.valueOf(feed.getId()));
			vh.tvEndTime.setText(0 < feed.getRegTime() ? Util.getTime(feed.getRegTime(), 7) : "");
			vh.tvState.setText("点击播放");
			vh.innerLL.setVisibility(View.GONE);
		}
		return convertView;
	}
	static class ViewHolder {
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
		
		
		private LinearLayout innerLL;// 内部名单布局

		
	}
	
}
