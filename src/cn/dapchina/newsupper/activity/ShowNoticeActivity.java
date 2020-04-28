package cn.dapchina.newsupper.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import cn.dapchina.newsupper.R;

public class ShowNoticeActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_notice);
	}

}
