package cn.dapchina.newsupper.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.util.Util;

public class CheckAddrActivity extends Activity {
	/**
	 * 内部名单显示的4个属性
	 */
	private TextView tvTo1, tvTo2, tvTo3, tvTo4;
	private TextView tvAddTo1, tvAddTo2, tvAddTo3, tvAddTo4;
	private TextView add_addr_now;
	private Button left_btn, right_btn;
	private UploadFeed feed;
	private Survey survey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_addr);
		add_addr_now = (TextView) findViewById(R.id.add_addr_now);
		feed=(UploadFeed) getIntent().getExtras().get("feed");
		survey=feed.getSurvey();
		tvAddTo1 = (TextView) findViewById(R.id.addto1_tv);
		tvAddTo2 = (TextView) findViewById(R.id.addto2_tv);
		tvAddTo3 = (TextView) findViewById(R.id.addto3_tv);
		tvAddTo4 = (TextView) findViewById(R.id.addto4_tv);
		tvTo1 = (TextView) findViewById(R.id.tvTo1);
		tvTo2 = (TextView) findViewById(R.id.tvTo2);
		tvTo3 = (TextView) findViewById(R.id.tvTo3);
		tvTo4 = (TextView) findViewById(R.id.tvTo4);
		left_btn = (Button) findViewById(R.id.left_btn);
		right_btn = (Button) findViewById(R.id.right_btn);
		if (null != feed) {
			if (1 == survey.openStatus) {
				HashMap<String, Parameter> hm = feed.getInnerPanel().getPsMap();
				tvAddTo1.setText(null != hm.get("Parameter1") ? hm.get("Parameter1").getContent() : "");
				tvAddTo2.setText(null != hm.get("Parameter2") ? hm.get("Parameter2").getContent() : "");
				tvAddTo3.setText(null != hm.get("Parameter3") ? hm.get("Parameter3").getContent() : "");
				tvAddTo4.setText(null != hm.get("Parameter4") ? hm.get("Parameter4").getContent() : "");
				tvTo1.setText(survey.getParameter1() + ":");
				tvTo2.setText(survey.getParameter2() + ":");
				tvTo3.setText(survey.getParameter3() + ":");
				tvTo4.setText(survey.getParameter4() + ":");
			}
		}
		String addr="";
		if(!Util.isEmpty(feed.getVisitAddress())){
			addr=feed.getVisitAddress();
		}else if(!Util.isEmpty(feed.getLat())&&!Util.isEmpty(feed.getLng())){
			addr=getResources().getString(R.string.get_addr_fail);
		}else{
			addr=getResources().getString(R.string.get_addr_null);
		}
		add_addr_now.setText(addr);
		if(Util.isEmpty(tvAddTo1.getText())){
			tvTo1.setVisibility(View.GONE);
		}
		if(Util.isEmpty(tvAddTo2.getText())){
			tvTo2.setVisibility(View.GONE);
		}
		if(Util.isEmpty(tvAddTo3.getText())){
			tvTo3.setVisibility(View.GONE);
		}
		if(Util.isEmpty(tvAddTo4.getText())){
			tvTo4.setVisibility(View.GONE);
		}
	}
	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.right_btn:
			if (1 == feed.getVisitMode()) {
				Intent it = new Intent(this, NativeModeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				this.startActivity(it);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
			} else {
				// web模式
				Intent it = new Intent(this, WebModeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				this.startActivity(it);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
			}
			break;
		}
	}

}
