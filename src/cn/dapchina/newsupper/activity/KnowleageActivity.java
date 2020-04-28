package cn.dapchina.newsupper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.adapter.KnowledgeAdapter;
import cn.dapchina.newsupper.bean.Knowledge;
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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class KnowleageActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private ImageView knowleage_left_iv;// 返回
	private MyListView knowleageLv; // 大树  注释   用XLISTVIEW  
	private KnowledgeAdapter knowledgeAdapter;// 大树  知识库 适配器 	
	private boolean isFirst,isRefreshing; //  大树  首次   是否刷新 
	private TextView noKowleageTv;//  大树  没有记录的 显示结果  
	private ArrayList<Knowledge> ss; // 大树  下载到的 所有的知识库集合 
	private MyApp ma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.knowleage_activity);
		knowleage_left_iv=(ImageView) findViewById(R.id.knowleage_left_iv);
		knowleage_left_iv.setOnClickListener(this);
		knowleageLv=(MyListView) findViewById(R.id.konw_listview);				
		//  大树  设置下拉刷新事件 
		noKowleageTv=(TextView) findViewById(R.id.no_knowleage_list_tv);
		knowleageLv.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (!isRefreshing) {
					refreshKnowList();
				}
			}
		});
		knowleageLv.setOnItemClickListener(this);
		ma=(MyApp) getApplication();
		//  大树 以上
		Intent intent=getIntent(); 
		setResult(11, intent);
	}
    
	// 大树  进入界面请求知识库 下载 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(NetUtil.checkNet(this)){
			isFirst=true;
			show();
			refreshKnowList();
		}
		super.onResume();
		
	}
	//  大树   
	private void refreshKnowList() {
		HashMap<String, Object> hm = new HashMap<String, Object>();
//		if (!Util.isEmpty(cfg.getString(Cnt.USER_ID, ""))) {
//			hm.put(Cnt.USER_ID,cfg.getString(Cnt.USER_ID, ""));
//		}else {
//			hm.put(Cnt.USER_ID, "");
//		}
//		if (!Util.isEmpty(cfg.getString(Cnt.USER_PWD, ""))) {
//			hm.put(Cnt.USER_PWD, MD5.Md5Pwd(cfg.getString(Cnt.USER_PWD, "")));
//		}else {
//			hm.put(Cnt.USER_PWD, "");
//		}
		hm.put(Cnt.USER_ID, ma.userId);
		hm.put(Cnt.USER_PWD, ma.userPwd);
		MainService.newTask(new Task(TaskType.TS_GET_KNOW, hm));
		isRefreshing = true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			KnowleageActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.knowleage_left_iv:
			KnowleageActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Knowledge knowledge=(Knowledge) parent.getAdapter().getItem(position);
		Intent intent=new Intent(this,KnowledgeDetailActivity.class);
		intent.putExtra("knowledge", knowledge);
		startActivity(intent);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch ((Integer)param[0]) {
		//  大树 知识库  下载  
		case TaskType.TS_GET_KNOW:
			ss = (ArrayList<Knowledge>) param[1];
			if(!Util.isEmpty(ss)){
				noKowleageTv.setVisibility(View.GONE);
				knowleageLv.setVisibility(View.VISIBLE);
				Log.i("zrl1", "走走这里2:"+ss.size());
				if(null == knowledgeAdapter){
					knowledgeAdapter = new KnowledgeAdapter(ss,KnowleageActivity.this);
					knowleageLv.setAdapter(knowledgeAdapter);
				}else{
					knowledgeAdapter.refresh(ss);
				}
			}else{
				Log.i("zrl1", "走走这里1:");
				Toasts.makeText(KnowleageActivity.this, this.getString(R.string.knowlege_null), Toast.LENGTH_LONG).show();
				knowleageLv.setVisibility(View.GONE);
				noKowleageTv.setVisibility(View.VISIBLE);
			}
			if(isFirst){
				dismiss();
				isFirst = false;
			}else{
				knowleageLv.onRefreshComplete();
			}
			isRefreshing = false;
			break;
			//  大树   知识库 下载附件 
		case TaskType.TS_GET_KNOWFILE_FAIL:
			ss = (ArrayList<Knowledge>) param[1];
			if(!Util.isEmpty(ss)){
				noKowleageTv.setVisibility(View.GONE);
				knowleageLv.setVisibility(View.VISIBLE);
				Log.i("zrl1", "走走这里2:"+ss.size());
				if(null == knowledgeAdapter){
					knowledgeAdapter = new KnowledgeAdapter(ss,KnowleageActivity.this);
					knowleageLv.setAdapter(knowledgeAdapter);
				}else{
					knowledgeAdapter.refresh(ss);
				}
			}else{
				Log.i("zrl1", "走走这里1:");
				Toasts.makeText(KnowleageActivity.this, this.getString(R.string.knowlege_null), Toast.LENGTH_LONG).show();
				knowleageLv.setVisibility(View.GONE);
				noKowleageTv.setVisibility(View.VISIBLE);
			}
			if(isFirst){
				dismiss();
				isFirst = false;
			}else{
				knowleageLv.onRefreshComplete();
			}
			isRefreshing = false;
			
			Toasts.makeText(KnowleageActivity.this, this.getString(R.string.knowlege_accessory_downfail), Toast.LENGTH_LONG).show();
			
			break; 
		}
	}
}
