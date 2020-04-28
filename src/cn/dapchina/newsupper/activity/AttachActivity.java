package cn.dapchina.newsupper.activity;

import java.util.ArrayList;
import java.util.List;

import cn.dapchina.newsupper.R;

import cn.dapchina.newsupper.adapter.NoAttachAdapter;
import cn.dapchina.newsupper.adapter.SubscibeAdapter;
import cn.dapchina.newsupper.bean.MyRecoder;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.Util;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 附件删除
 * @author Administrator
 *
 */
public class AttachActivity extends BaseActivity {

	private RelativeLayout choicell;
	private MyApp ma; 
	private ListView attach_list;
	private NoAttachAdapter attachAdapter;
	private ArrayList<MyRecoder> list=new ArrayList<MyRecoder>();
	private TextView tvNoAuthorList;
	private String sid;//传过来的sid
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attach_activity);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
//		this.setFinishOnTouchOutside(false);
		choicell = (RelativeLayout) findViewById(R.id.my_dialog_ll);
		attach_list=(ListView) findViewById(R.id.attach_list);
		tvNoAuthorList = (TextView) findViewById(R.id.no_author_list_tv);
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (screenWidth / 1.2), (int) (screenHeigh / 1.6));
		choicell.setLayoutParams(params);
		sid=getIntent().getExtras().getString("sid");
		System.out.println("sid:"+sid);
		list=ma.dbService.queryAllDeleteRecode(sid);
	}

	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}

	@Override
	public void init() {
		if(!Util.isEmpty(list)){
			tvNoAuthorList.setVisibility(View.GONE);
			attach_list.setVisibility(View.VISIBLE);
			if(null == attachAdapter){
				attachAdapter = new NoAttachAdapter(AttachActivity.this, list);
				attach_list.setAdapter(attachAdapter);
			}else{
				attachAdapter.refresh(list);
			}
		}else{
			attach_list.setVisibility(View.GONE);
			tvNoAuthorList.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
