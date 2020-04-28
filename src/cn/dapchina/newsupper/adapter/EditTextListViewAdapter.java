package cn.dapchina.newsupper.adapter;

import java.util.List;
import cn.dapchina.newsupper.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EditTextListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<String> data;

	public EditTextListViewAdapter(Context context, List<String> data) {
		this.inflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item, null);
		}

		TextView textView = (TextView) convertView.findViewById(R.id.tvData);
		textView.setText(data.get(position));
		return convertView;
	}


}
