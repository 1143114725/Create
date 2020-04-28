package cn.dapchina.newsupper.activity;


import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.global.MyApp;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
/**
 * //单选追加说明方法
 * @author Administrator
 *
 */
public class MyDialogActivity extends Activity {
	
	private String item;
	private TextView tvPrompt,tvWord;
	private int screen;
	private MyApp ma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		super.onCreate(savedInstanceState);
		ma=(MyApp) getApplication();
		ma.addActivity(this);
		screen = ma.cfg.getInt("ScreenOrientation", 0);
		/**
		 * 初始化问卷字号动态设置完毕
		 */
		if (1 == screen) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setContentView(R.layout.my_dialog_activity);
		item=getIntent().getStringExtra("item");
		tvWord=(TextView) findViewById(R.id.word_tv);
		tvWord.setText(R.string.opt_word);
		tvPrompt=(TextView) findViewById(R.id.prompt);
		tvPrompt.setText(item);
	}
	
	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}

}
