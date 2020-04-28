package cn.dapchina.newsupper.adapter;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.MyRecoder;
import cn.dapchina.newsupper.util.Util;

public class NoAttachAdapter extends BaseAdapter{
	
	private List<MyRecoder> list;
	private Context context;
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	public void refresh() {
		notifyDataSetChanged();
	}

	public NoAttachAdapter(Context context,List<MyRecoder> knowList ) {
		super();
		this.list = knowList;
		this.context = context;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	public void refresh(ArrayList<MyRecoder> feeds) {
		if (!Util.isEmpty(feeds)) {
			if (!Util.isEmpty(list)) {
				list.clear();
				list.addAll(feeds);
			}
		}
		notifyDataSetChanged();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		MyRecoder myRecode = list.get(position);		
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.attach_item, null);
			viewHoder=new ViewHoder(convertView);
			convertView.setTag(viewHoder);
		}else {
			viewHoder=(ViewHoder) convertView.getTag();
		}
		viewHoder.accessoryDescription.setText(myRecode.getName());
		viewHoder.accessoryDescription.setSingleLine(false);
		viewHoder.accessoryName.setText("No:"+(position+1));	
		viewHoder.btn_num.setVisibility(View.GONE);
		// 大树 以上 
		return convertView;
	}
	
	private ViewHoder viewHoder;
	
	private class  ViewHoder{
		
		private TextView accessoryName;// 附件名字 
		
		private TextView accessoryDescription;// 附件说明  
		
		private LinearLayout lookAccessoryLL; // 查看附件 线性布局 
		
		private Button btn_num;

		public ViewHoder(View convertView) {
			super();
			this.accessoryName =(TextView) convertView.findViewById(R.id.tvId);
			this.accessoryDescription = (TextView) convertView.findViewById(R.id.tvTitle);
			this.lookAccessoryLL = (LinearLayout) convertView.findViewById(R.id.attah_click);
			this.btn_num=(Button) convertView.findViewById(R.id.btn_num);
		}		
	}
	
	
}
