package cn.dapchina.newsupper.adapter;

import java.util.ArrayList;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Knowledge;
import cn.dapchina.newsupper.util.Util;

public class KnowledgeAdapter extends BaseAdapter{
	
	private ArrayList<Knowledge> list;
	private Context context;
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	public void refresh() {
		notifyDataSetChanged();
	}
	
	public void refresh(ArrayList<Knowledge> knowArrayList) {
		if (!Util.isEmpty(knowArrayList)) {
			if (!Util.isEmpty(list)) {
				list.clear();
				list.addAll(knowArrayList);
			}
			notifyDataSetChanged();
		}
	}

	public KnowledgeAdapter(ArrayList<Knowledge> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		Knowledge knowledge = list.get(position);		
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.knowledge_item, null);
			viewHolder=new ViewHolder(convertView);
			convertView.setTag(viewHolder);			
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		// 大树知识库   获取附件数量  
		int accessory_num=0;
		if (knowledge.getAttach().contains(";")) {
			accessory_num=knowledge.getAttach().split(";").length;
		}
		// 
		viewHolder.btn_num.setText(accessory_num+"");
		viewHolder.tvId.setText(knowledge.getId());
		viewHolder.tvTitle.setText(knowledge.getTitle());
		viewHolder.tvContent.setText(Html.fromHtml(knowledge.getContent()));
		
		return convertView;
	}
	private ViewHolder viewHolder;
	
	
	private class ViewHolder {
		
		private TextView tvId; // 知识库ID  
		
		private TextView tvTitle;  //知识库标题 
		
		private TextView tvContent; // 知识库内容
		
		private Button btn_num;  // 知识库附件个数 

		public ViewHolder(View convertView) {
			super();
			this.tvId = (TextView) convertView.findViewById(R.id.tvId);
			this.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			this.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			this.btn_num = (Button) convertView.findViewById(R.id.btn_num);
		}					
		
	}
	
	

}
