package cn.dapchina.newsupper.activity;

import java.util.ArrayList;

import java.util.HashMap;

import com.baidu.location.BDLocation;



import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.adapter.SubscibeAdapter;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.util.MD5;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.MyListView;
import cn.dapchina.newsupper.view.MyListView.OnRefreshListener;
import cn.dapchina.newsupper.view.Toasts;

public class SubscibeActivity extends BaseActivity  implements OnClickListener{

	
	ImageView subscibeIv;
	SubscibeAdapter subscibeAdapter;
	private EditText etSurvey;
	private MyListView lvAuthor;
	private TextView tvNoAuthorList;
	private boolean isRefreshing = false;
	private boolean isFirst = true;
	private MyApp ma;
	private ImageView iv_search;//搜索
	private ArrayList<Survey> ss;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.subscibe_activity);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		subscibeIv=(ImageView) findViewById(R.id.subscibe_left_iv);
		subscibeIv.setOnClickListener(this);
		lvAuthor= (MyListView) findViewById(R.id.listView1);
		iv_search=(ImageView) findViewById(R.id.iv_search);
		iv_search.setOnClickListener(this);
		tvNoAuthorList = (TextView) findViewById(R.id.no_author_list_tv);
		lvAuthor.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				if(!isRefreshing){
					refreshAuthorList();
				}
			}
		});
		etSurvey=(EditText) findViewById(R.id.etSurvey);
		etSurvey.setInputType(InputType.TYPE_NULL);
		etSurvey.setOnTouchListener(new FocusListener());
		Intent intent=getIntent();
		setResult(11, intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
				
			SubscibeActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);	
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.subscibe_left_iv:
			SubscibeActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);	
			break;
		case R.id.iv_search:
			//假如问卷为空
			String words=etSurvey.getText().toString();
			if(Util.isEmpty(words)){
				Util.viewShake(this, etSurvey);
				Toasts.makeText(SubscibeActivity.this, R.string.input_name, Toast.LENGTH_LONG).show();
			}
			ss = ma.dbService.searchSurveyByWord(ma.userId,words);
			if(!Util.isEmpty(ss)){
				tvNoAuthorList.setVisibility(View.GONE);
				lvAuthor.setVisibility(View.VISIBLE);
				if(null == subscibeAdapter){
					subscibeAdapter = new SubscibeAdapter(SubscibeActivity.this, ss,ma);
					lvAuthor.setAdapter(subscibeAdapter);
				}else{
					subscibeAdapter.refresh(ss);
				}
			}else{
				lvAuthor.setVisibility(View.GONE);
				tvNoAuthorList.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}
	
	private final class FocusListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				((EditText) v).setInputType(InputType.TYPE_CLASS_TEXT);
			}
			return false;
		}
	}
	
	@Override
	protected void onResume() {
		if(NetUtil.checkNet(this)){
			isFirst=true;
			show();
			refreshAuthorList();
		}
		super.onResume();
	}
	
	private void refreshAuthorList() {
		HashMap<String, Object> hm = new HashMap<String, Object>();
	    //  大树测试   
//		hm.put(Cnt.USER_ID, "dap1");
//		hm.put(Cnt.USER_PWD, MD5.Md5Pwd("dap1"));
		hm.put(Cnt.USER_ID, ma.userId);
		hm.put(Cnt.USER_PWD, MD5.Md5Pwd(ma.userPwd));
		MainService.newTask(new Task(TaskType.TS_AUTHOR, hm));
		isRefreshing = true;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void refresh(Object... param) {
		switch ((Integer)param[0]) {
		case TaskType.TS_AUTHOR:
			ss = (ArrayList<Survey>) param[1];
			if(!Util.isEmpty(ss)){
				tvNoAuthorList.setVisibility(View.GONE);
				lvAuthor.setVisibility(View.VISIBLE);
				if(null == subscibeAdapter){
					subscibeAdapter = new SubscibeAdapter(SubscibeActivity.this, ss,ma);
					lvAuthor.setAdapter(subscibeAdapter);
				}else{
					subscibeAdapter.refresh(ss);
				}
			}else{
				lvAuthor.setVisibility(View.GONE);
				tvNoAuthorList.setVisibility(View.VISIBLE);
			}
			if(isFirst){
				dismiss();
				isFirst = false;
			}else{
				lvAuthor.onRefreshComplete();
			}
			isRefreshing = false;
			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}

	
}
