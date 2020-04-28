package cn.dapchina.newsupper.activity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.adapter.NoticeAdapter;
import cn.dapchina.newsupper.bean.Quota;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.service.XmlService;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.MD5;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NoticeActivity extends BaseActivity implements OnClickListener {
	private MyApp ma;
	private DisplayMetrics dm;
	private int width, height;
	private RelativeLayout rl_upload, rl_survey, rl_inner, rl_title,rl_quota;
	private ListView listView1, listView2, listView3;
	private NoticeAdapter noticeAdapter1, noticeAdapter2, noticeAdapter3;
	private ImageView noticie_left_iv; 
	private ArrayList<Survey> ss1,ss2,ss3;
	private ArrayList<Survey> tempSS2,tempSS3;
	private ArrayList<Quota> quotalist;
	private String notice;
	private Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_notice);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		boolean isFree = cfg.getBoolean("isFree");
		if(isFree){
			//免费
			System.out.println("freeIp:"+freeIp);
			Cnt.changeNewURL(true, freeIp, freeIp, payIp);
		}else{
			//付费
			System.out.println("payIp:"+payIp);
			Cnt.changeNewURL(false, payIp, freeIp, payIp);
		}
		notice=getIntent().getExtras().getString("notice");
		rl_upload = (RelativeLayout) this.findViewById(R.id.rl_upload);
		rl_survey = (RelativeLayout) this.findViewById(R.id.rl_survey);
		rl_inner = (RelativeLayout) this.findViewById(R.id.rl_inner);
		rl_title = (RelativeLayout) this.findViewById(R.id.rl_title);
		rl_quota = (RelativeLayout) this.findViewById(R.id.rl_quota);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		height = dm.heightPixels;
		LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height / 12);
		rl_upload.setLayoutParams(pa);
		rl_survey.setLayoutParams(pa);
		rl_inner.setLayoutParams(pa);
		rl_title.setLayoutParams(pa);
		//rl_quota.setLayoutParams(pa);
		noticie_left_iv=(ImageView) findViewById(R.id.noticie_left_iv);
		btn_login=(Button) findViewById(R.id.btn_login);
		noticie_left_iv.setOnClickListener(this);
		initList();
	}

	private void initList() {
		//已完成未上报的
		ArrayList<Survey> userSS= ma.dbService.getAllUploadSurvey(ma.userId);
		if(null==ss1){
			ss1=new ArrayList<Survey>();
		}
		if(null==ss2){
			ss2=new ArrayList<Survey>();
		}
		if(null==ss3){
			ss3=new ArrayList<Survey>();
		}
		for(int i=0;i<userSS.size();i++){
			Survey survey=userSS.get(i);
			/**
			 * 已完成未上报的
			 */
			long u = ma.dbService.feedUnUploadCounts(survey.surveyId, ma.userId);
			if(u>0){
				ss1.add(survey);
			}
		}
		//可更新的项目
		tempSS2=ma.dbService.getAllDownloadedSurvey(ma.userId);
		//可更改名单的
		tempSS3=ma.dbService.getAllDownloadedSurveyHaveInner(ma.userId);
		//数据库的配额
		quotalist=ma.dbService.getSurveyQuotaList(ma.userId);
		show();
		if(!Util.isEmpty(quotalist)){
			handler.sendEmptyMessage(40);
		}
		if(!Util.isEmpty(tempSS2)){
			handler.sendEmptyMessage(10);
		}else if(!Util.isEmpty(tempSS3)){
			handler.sendEmptyMessage(20);
		}else{
			handler.sendEmptyMessage(30);
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			NoticeActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				ss1.clear();
				ss1=null;
				ss2.clear();
				ss2=null;
				ss3.clear();
				ss3=null;
				
				//已完成未上报的
				ArrayList<Survey> userSS= ma.dbService.getAllUploadSurvey(ma.userId);
				if(null==ss1){
					ss1=new ArrayList<Survey>();
				}
				if(null==ss2){
					ss2=new ArrayList<Survey>();
				}
				if(null==ss3){
					ss3=new ArrayList<Survey>();
				}
				for(int i=0;i<userSS.size();i++){
					Survey survey=userSS.get(i);
					/**
					 * 已完成未上报的
					 */
					long u = ma.dbService.feedUnUploadCounts(survey.surveyId, ma.userId);
					if(u>0){
						ss1.add(survey);
					}
				}
				//可更新的项目
				tempSS2=ma.dbService.getAllDownloadedSurvey(ma.userId);
				//可更改名单的
				tempSS3=ma.dbService.getAllDownloadedSurveyHaveInner(ma.userId);
				show();
				if(!Util.isEmpty(tempSS2)){
					handler.sendEmptyMessage(10);
				}else if(!Util.isEmpty(tempSS3)){
					handler.sendEmptyMessage(20);
				}else{
					handler.sendEmptyMessage(30);
				}
				break;
			case 10:
				//问卷更新交互
				if(tempSS2.size()>0){
					Survey s2=tempSS2.get(0);
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put(Cnt.USER_ID, ma.userId);
					hm.put("surveyId",s2.surveyId);
					hm.put("time", s2.getGeneratedTime());
//					System.out.println("tempSS2.size():"+tempSS2.size());
					MainService.newTask(new Task(TaskType.TS_NOTICE_SURVEY, hm));
				}else{
					handler.sendEmptyMessage(20);
				}
				break;
			case 20:
				//问卷名单更新交互
				if(tempSS3.size()>0){
					Survey s3=tempSS3.get(0);
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put(Cnt.USER_ID, ma.userId);
					hm.put("surveyId",s3.surveyId);
					long count=ma.dbService.getAllXmlCount(s3.surveyId, ma.userId);
					hm.put("count", count+"");
					MainService.newTask(new Task(TaskType.TS_NOTICE_INNER, hm));
				}else{
					handler.sendEmptyMessage(30);
				}
				break;
			case 40:
				//问卷名单更新交互
				if(quotalist.size()>0){
					Quota quota=quotalist.get(0);
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put("userId", ma.userId);
					hm.put("userPsd", ma.cfg.getString(Cnt.USER_PWD, ""));
					hm.put("surveyId",quota.getQuota_Surveyid());
					String time=quotalist.get(0).getQuotaTime();
					
					try {
						InputStream quotaStream = NetService.openUrl(Cnt.QUOTA_URL, hm, "GET");
						XmlService xs = new XmlService();
						String RegDate=xs.getListQuotaTime(quotaStream);
						boolean flag =Util.getDateCompareByGeTime(time, RegDate, "<");
						if(flag){
							rl_quota.setVisibility(View.VISIBLE);
						}else {
							rl_quota.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					handler.sendEmptyMessage(30);
				}
				break;
			case 30:
				if(Util.isEmpty(ss1)&&Util.isEmpty(ss2)&&Util.isEmpty(ss3) && Util.isEmpty(quotalist)){
					dismiss();
					finish();
				}else{
					//UI更新
					listView1 = (ListView) findViewById(R.id.listView1);
					noticeAdapter1 = new NoticeAdapter(NoticeActivity.this, ma, ss1,1,notice);
					listView1.setAdapter(noticeAdapter1);
					setListViewHeightBasedOnChildren(listView1);

					listView2 = (ListView) findViewById(R.id.listView2);
					noticeAdapter2 = new NoticeAdapter(NoticeActivity.this, ma, ss2,2,notice);
					listView2.setAdapter(noticeAdapter2);
					setListViewHeightBasedOnChildren(listView2);

					
					
					listView3 = (ListView) findViewById(R.id.listView3);
					noticeAdapter3 = new NoticeAdapter(NoticeActivity.this, ma, ss3,3,notice);
					listView3.setAdapter(noticeAdapter3);
					setListViewHeightBasedOnChildren(listView3);
					dismiss();
				}
				break;
			default:
				break;
			}
		};
	};
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 50:
			if(!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)){
				notice="0";
				handler.sendEmptyMessage(100);
			}else{
				
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... param) {
		switch ((Integer) param[0]) {
		case TaskType.TS_NOTICE_QUOTA:
			/*if (null != param[1]) {
				Survey survey=(Survey) param[1];
				if(null!=survey){
					ss2.add(survey);
				}else{
					
				}
			}
			tempSS2.remove(0);
			handler.sendEmptyMessage(10);*/
			break;
		
		
		case TaskType.TS_NOTICE_SURVEY:
			if (null != param[1]) {
				Survey survey=(Survey) param[1];
				if(null!=survey){
					ss2.add(survey);
				}else{
					
				}
			}
			tempSS2.remove(0);
			handler.sendEmptyMessage(10);
			break;
		case TaskType.TS_NOTICE_INNER:
			if (null != param[1]) {
				String notice=(String) param[1];
				if(!Util.isEmpty(notice)){
					if("True".equals(notice)){
						ss3.add(tempSS3.get(0));
					}
				}else{
					
				}
			}
			tempSS3.remove(0);
			handler.sendEmptyMessage(20);
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

	/***
	 * @func 动态设置listview的高度，实现一个activity中多个listview时，滑动时，多个listview整体滑动的效果
	 * @note 两个或多个listview外层必须是ScrollView->LinearLayout-> listview1,listview2
	 *       否则以下的函数会不起作用
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); //在还没有构建View之前无法取得View的度宽。在此之前我们必须选measure 一下
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// params.height += 5;// if without this statement,the listview will be
		// a
		// little short
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.noticie_left_iv:
			NoticeActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.btn_login:
			break;
		}
	}
}
