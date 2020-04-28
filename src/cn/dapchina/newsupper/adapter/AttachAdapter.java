package cn.dapchina.newsupper.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;

public class AttachAdapter extends BaseAdapter{
	
	private List<String> list;
	private Context context;
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	
	
	public void refresh() {
		notifyDataSetChanged();
	}

	public AttachAdapter(List<String> knowList, Context context) {
		super();
		this.list = knowList;
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
		
		String fileName = list.get(position);		
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.attach_item, null);
			viewHoder=new ViewHoder(convertView);
			convertView.setTag(viewHoder);
		}else {
			viewHoder=(ViewHoder) convertView.getTag();
		}
		viewHoder.accessoryDescription.setText(context.getString(R.string.accessory)+(position+1)+context.getString(R.string.view_attachments));
		viewHoder.accessoryName.setText(fileName);								
		final File file = new File(Util.getKnowPath(), fileName);
		viewHoder.lookAccessoryLL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (file.exists()) {
					Util.openFile(file, context);
					Log.i("zrl1", "文件存在：");
				}else {
					Toasts.makeText(context,context.getString(R.string.knowlege_accessory_not_exsit), Toast.LENGTH_LONG).show();
					Log.i("zrl1", "文件不存在：");
				}
			}
		});
		// 大树 以上 
		return convertView;
	}
	
	private ViewHoder viewHoder;
	
	private class  ViewHoder{
		
		private TextView accessoryName;// 附件名字 
		
		private TextView accessoryDescription;// 附件说明  
		
		private LinearLayout lookAccessoryLL; // 查看附件 线性布局 

		public ViewHoder(View convertView) {
			super();
			this.accessoryName =(TextView) convertView.findViewById(R.id.tvId);
			this.accessoryDescription = (TextView) convertView.findViewById(R.id.tvTitle);;
			this.lookAccessoryLL = (LinearLayout) convertView.findViewById(R.id.attah_click);;
		}		
	}
	
	
}
