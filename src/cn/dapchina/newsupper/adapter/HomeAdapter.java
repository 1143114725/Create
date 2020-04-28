package cn.dapchina.newsupper.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.VisitActivity;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.Util;

public class HomeAdapter extends BaseAdapter {
	

	
	private Activity activity;
	private ArrayList<Survey> list;
	private MyApp ma;
	private String language;

	public HomeAdapter(Activity activity, ArrayList<Survey> list) {
		super();
		this.activity = activity;
		this.list = list;
		ma = (MyApp) this.activity.getApplication();
		language = activity.getResources().getConfiguration().locale.getLanguage();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void refresh(ArrayList<Survey> surveys) {
		if (!Util.isEmpty(surveys)) {
			if (!Util.isEmpty(list)) {
				list.clear();
				list.addAll(surveys);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.home_grid_item, null);
			vh = new ViewHolder();
			vh.grid_item_ll = (LinearLayout) convertView.findViewById(R.id.grid_item_ll);
			vh.grid_item_top=(LinearLayout) convertView.findViewById(R.id.grid_item_top);
			vh.grid_item_back = (LinearLayout) convertView.findViewById(R.id.grid_item_back);
			vh.grid_item_number = (TextView) convertView.findViewById(R.id.grid_item_number);
			vh.grid_item_title = (TextView) convertView.findViewById(R.id.grid_item_title);
			vh.grid_item_content = (TextView) convertView.findViewById(R.id.grid_item_content);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Drawable background = activity.getResources().getDrawable(R.drawable.border);
		Drawable backgrounds = activity.getResources().getDrawable(R.drawable.borders);
		if (position % 4 == 1) {
			background = activity.getResources().getDrawable(R.drawable.border1);
			backgrounds = activity.getResources().getDrawable(R.drawable.border1s);
		} else if (position % 4 == 2) {
			background = activity.getResources().getDrawable(R.drawable.border2);
			backgrounds = activity.getResources().getDrawable(R.drawable.border2s);
		} else if (position % 4 == 3) {
			background = activity.getResources().getDrawable(R.drawable.border3);
			backgrounds = activity.getResources().getDrawable(R.drawable.border3s);
		}
		vh.grid_item_back.setBackgroundDrawable(backgrounds);
		vh.grid_item_top.setBackgroundDrawable(background);
		Survey s = list.get(position);
		if (null != s) {
			vh.grid_item_title.setText(s.surveyTitle);
			if (Util.isEmpty(ma.userId)) {
				ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(ma)) : (ma.cfg)).getString("UserId", activity.getString(R.string.user_name_test));
			}
			vh.grid_item_number.setText("" + ma.dbService.feedCompletedCount(s.surveyId, ma.userId));
			// 大树 访问前说明 添加
			if (!Util.isEmpty(s.getWord())) {
				Spannable sp = (Spannable) Html.fromHtml(s.getWord());
				vh.grid_item_content.setText(sp.toString());
			}else{
				vh.grid_item_content.setText(R.string.no_explain);
			}
		}
		initKeyTextView(vh.grid_item_title,vh.grid_item_content,position);
		vh.grid_item_ll.setOnClickListener(new GridListener(s));
		return convertView;
	}

	//初始化宽度
	private void initKeyTextView(final View ll1, final View ll2,final int position) {
		ViewTreeObserver vto1 = ll1.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				ll1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				if (position % Cnt.GRIVEW_COLUMN_NUMS == 0) {
					Cnt.TITLE_HEIGHT=0;
				}
				if (ll1.getHeight() > Cnt.TITLE_HEIGHT) {
					Cnt.TITLE_HEIGHT = ll1.getHeight();
				}
				setHeight(ll1, Cnt.TITLE_HEIGHT,1);
			}
		});
		
		ViewTreeObserver vto2 = ll2.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				ll2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				if (position % Cnt.GRIVEW_COLUMN_NUMS == 0) {
					Cnt.INS_HEIGHT=0;
				}
				if (ll2.getHeight() > Cnt.INS_HEIGHT) {
					Cnt.INS_HEIGHT = ll2.getHeight();
				}
				setHeight(ll2, Cnt.INS_HEIGHT,2);
			}
		});
	}
	//设置高度
	public void setHeight(View ll, int height,int flag) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
		layoutParams.leftMargin=activity.getResources().getDimensionPixelSize(R.dimen.login_margin_top);
		layoutParams.topMargin=activity.getResources().getDimensionPixelSize(R.dimen.login_margin_top);
		if(2==flag){
			layoutParams.rightMargin=activity.getResources().getDimensionPixelSize(R.dimen.login_margin_top);
			layoutParams.bottomMargin=activity.getResources().getDimensionPixelSize(R.dimen.login_margin_top);
		}
		ll.setLayoutParams(layoutParams);
	}


	// viewholder
	static class ViewHolder {
		private TextView grid_item_number;// 个数
		private TextView grid_item_title;// 题目
		private TextView grid_item_content;// 说明
		private LinearLayout grid_item_ll;// 外边框
		private LinearLayout grid_item_back;// 局部背景
		private LinearLayout grid_item_top;// 上部背景
	}

	private final class GridListener implements OnClickListener {
		private Survey s;
		public GridListener(Survey survey) {
			this.s = survey;
		}
		@Override
		public void onClick(View v) {
			Intent it = new Intent();
			Bundle bundle = new Bundle();
			if ("zh".equals(language)) {
				// language
				it.setClass(activity, VisitActivity.class);
				s.mapType = "Baidu";
			} else {
				it.setClass(activity, VisitActivity.class);
				s.mapType = "Baidu";
			}
			bundle.putSerializable("s", s);
			it.putExtras(bundle);
			activity.startActivity(it);
			activity.overridePendingTransition(R.anim.zzright, R.anim.zzleft);
		}
	};

}
