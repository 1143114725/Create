package cn.dapchina.newsupper.activity;

import cn.dapchina.newsupper.R;

import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.Util;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyWordActivity extends BaseActivity {

	private RelativeLayout choicell;
	private Survey s;                   // 大树   传递过来的问卷
	private MyApp ma; 
	private TextView surveyTitle;       // 大树   问卷标题
	private TextView surveyId;          // 大树   问卷编号
	private TextView publishTime;       // 大树   发布时间
	private TextView innerDone;         // 大树   内部名单
	private TextView survey_description;// 大树   详细说明

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_word_activity);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
//		this.setFinishOnTouchOutside(false);
		choicell = (RelativeLayout) findViewById(R.id.my_dialog_ll);
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (screenWidth / 1.2), (int) (screenHeigh / 1.6));
		choicell.setLayoutParams(params);
		s=(Survey) getIntent().getSerializableExtra("survey");
		init();
		if (s!=null) {
			if (!Util.isEmpty(s.surveyTitle)) {
				surveyTitle.setText(s.surveyTitle+this.getString(R.string.word_ins));
			}
			if (!Util.isEmpty(s.surveyId)) {
				surveyId.setText(this.getString(R.string.word_num)+s.surveyId);
			}
			if (!Util.isEmpty(s.publishTime)) {
				publishTime.setText(this.getString(R.string.word_time)+s.publishTime);
			}
			if (!Util.isEmpty(s.innerDone+"")) {
				if (s.innerDone==0) {
					innerDone.setText(this.getString(R.string.word_nohave));
				}else
					innerDone.setText(this.getString(R.string.word_have));				
			}
			if (!Util.isEmpty(s.getWord())) {
				Spanned description=Html.fromHtml(s.getWord());
				survey_description.setText(description.toString());
			}
		}
		
	}

	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		surveyTitle=(TextView) findViewById(R.id.survey_title);
		surveyId=(TextView) findViewById(R.id.survey_id);
		survey_description=(TextView) findViewById(R.id.survey_description);
		publishTime=(TextView) findViewById(R.id.publish_time);
		innerDone=(TextView) findViewById(R.id.inner_done);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
