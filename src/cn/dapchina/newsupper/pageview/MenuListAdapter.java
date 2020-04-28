package cn.dapchina.newsupper.pageview;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;


import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.NativeModeActivity;
import cn.dapchina.newsupper.bean.CstmMatcher;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.Util;




import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * MenuList的适配器
 * 
 */
public class MenuListAdapter extends BaseAdapter {

	private Activity context;
	private LayoutInflater listInflater;
	private boolean isPressed[];
	private int imageId = R.drawable.star_icon;
	private int pressedId;
	private ArrayList<Question> qs;
	private ArrayList<Integer> odList;
	private int width;
	private int height;
	
	//更改的样式
		private MyApp ma;
		private UploadFeed feed;

	/* 一个menu item中包含一个imageView和一个TextView */
	public final class ListItemsView {
		public ImageView menuIcon;
		public TextView menuText;
	}

	/**
	 * 刷新用的
	 * 
	 * @param surveys
	 */
	public void refresh(ArrayList<Integer> orderList) {
		if (!Util.isEmpty(orderList)) {
			if (!Util.isEmpty(odList)) {
				odList = orderList;
				// 大于0在判断哪个选中
				if (this.odList.size() > 0) {
					this.isPressed = new boolean[this.odList.size()];
					for (int i = 0; i < this.odList.size(); i++) {
						this.isPressed[i] = false;
					}
//					this.isPressed[this.pressedId] = true;
				}
			}
			notifyDataSetChanged();
		}
	}

	/**
	 * 刷新位置
	 * 
	 * @param surveys
	 */
	public void refreshIndex(int index) {
		int indexOf = odList.indexOf(index);
		if (-1 != indexOf) {
			pressedId = indexOf;
		} else {
			pressedId = 0;
		}
		// 大于0在判断哪个选中
		if (this.odList.size() > 0) {
			this.isPressed = new boolean[this.odList.size()];
			for (int i = 0; i < this.odList.size(); i++) {
				this.isPressed[i] = false;
			}
			this.isPressed[this.pressedId] = true;
		}
		notifyDataSetChanged();
	}

	//更改的样式
		public MenuListAdapter(Activity context, int pressedId, ArrayList<Question> qsList, ArrayList<Integer> orderList,MyApp ma,UploadFeed feed) {
			this.context = context;
			this.pressedId = pressedId;
			this.qs = qsList;//所有问题集合
			this.odList = orderList;//有效问题顺序号
			this.listInflater = LayoutInflater.from(context);
			//更改的样式
			this.ma=ma;
			this.feed=feed;
			this.init();
		}

	@Override
	public int getCount() {
		// System.out.println("总和:"+odList.size());
		return odList.size();
	}

	@Override
	public Object getItem(int position) {
		return qs.get(odList.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int po = position;
		ListItemsView listItemsView;
		if (convertView == null) {
			listItemsView = new ListItemsView();
			convertView = this.listInflater.inflate(R.layout.menu_list_item, null);
			listItemsView.menuIcon = (ImageView) convertView.findViewById(R.id.menuIcon);
			listItemsView.menuText = (TextView) convertView.findViewById(R.id.menuText);
			convertView.setTag(listItemsView);
		} else {
			listItemsView = (ListItemsView) convertView.getTag();
		}
		
		Integer order = odList.get(position);
		if (0 < qs.size()) {
			Question question = qs.get(order);
		
		LayoutParams params = new LayoutParams(width/15, height/22);
		listItemsView.menuIcon.setLayoutParams(params);
		listItemsView.menuIcon.setBackgroundResource(imageId);
		if(!Util.isEmpty(question.qid)){
			//更改的样式
			String strTilte=question.qid+ "." + question.qTitle;
			/**
			 * 标题逻辑引用
			 */
			CstmMatcher qutoMatcherList = Util.findMatcherItemList(strTilte, ma, feed.getUuid(),question.surveyId);
			boolean qutoHave = Util.isEmpty(qutoMatcherList.getResultStr());
			if (!qutoHave) {
				strTilte = qutoMatcherList.getResultStr();
			}
			/**
			 * 引用受访者参数
			 */
			String parametersStr = feed.getParametersStr();
			ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
			if (!Util.isEmpty(parametersStr)) {
				parameterList.clear();
				ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
				if (!Util.isEmpty(tParameters)) {
					parameterList.addAll(tParameters);
				}
			}
			CstmMatcher parameterMatcherList = Util.findMatcherPropertyItemList(strTilte, parameterList);
			boolean parameterHave = Util.isEmpty(parameterMatcherList.getMis());
			if (!parameterHave) {
				strTilte = parameterMatcherList.getResultStr();
			}
			/**
			 * 引用受访者参数结束
			 */
			Spanned fromHtml=Html.fromHtml(strTilte);
			listItemsView.menuText.setText(fromHtml);
		}else{
			//更改
			if(!Util.isEmpty(question.qTitle)){
				//更改的样式
				String strTilte=question.qTitle;
				/**
				 * 标题逻辑引用
				 */
				CstmMatcher qutoMatcherList = Util.findMatcherItemList(strTilte, ma, feed.getUuid(),question.surveyId);
				boolean qutoHave = Util.isEmpty(qutoMatcherList.getResultStr());
				if (!qutoHave) {
					strTilte = qutoMatcherList.getResultStr();
				}
				/**
				 * 引用受访者参数
				 */
				String parametersStr = feed.getParametersStr();
				ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
				if (!Util.isEmpty(parametersStr)) {
					parameterList.clear();
					ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
					if (!Util.isEmpty(tParameters)) {
						parameterList.addAll(tParameters);
					}
				}
				CstmMatcher parameterMatcherList = Util.findMatcherPropertyItemList(strTilte, parameterList);
				boolean parameterHave = Util.isEmpty(parameterMatcherList.getMis());
				if (!parameterHave) {
					strTilte = parameterMatcherList.getResultStr();
				}
				/**
				 * 引用受访者参数结束
				 */
				Spanned fromHtml = Html.fromHtml(strTilte);
				listItemsView.menuText.setText(fromHtml);
			}else{
				//(西城BUG)
				listItemsView.menuText.setText(context.getString(R.string.no_title));
			}
			
		}}
		if (this.isPressed[position] == true)
			convertView.setBackgroundResource(R.drawable.menu_item_bg_sel);
		else
			convertView.setBackgroundColor(Color.TRANSPARENT);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				changeState(po);
				gotoActivity(po);
				notifyDataSetInvalidated();
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

					}

				});
			}
		});

		return convertView;
	}

	private void gotoActivity(int position) {
		NativeModeActivity activity = (NativeModeActivity) context;
		activity.getScrollView().clickMenuBtn();
		int forwardOrder = this.odList.get(position);
		activity.automaticPage(forwardOrder);
	}

	private void changeState(int position) {
		for (int i = 0; i < this.odList.size(); i++) {
			isPressed[i] = false;
		}
		isPressed[position] = true;
	}

	private void init() {
		// 大于0在判断哪个选中
		if (this.odList.size() > 0) {
			this.isPressed = new boolean[this.odList.size()];
			for (int i = 0; i < this.odList.size(); i++) {
				this.isPressed[i] = false;
			}
			this.isPressed[this.pressedId] = true;
		}
		Display defaultDisplay = context.getWindowManager().getDefaultDisplay();
		width=defaultDisplay.getWidth();
		height=defaultDisplay.getHeight();
		this.listInflater = LayoutInflater.from(context);
	}
}
