package cn.dapchina.newsupper.activity;


import cn.dapchina.newsupper.R;

import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.view.CustomDialog;
import cn.dapchina.newsupper.view.Toasts;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
/**
 * //单选追加说明方法
 * @author Administrator
 *
 */
public class LogoutDialogActivity extends Activity {
	
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
		setContentView(R.layout.logout_dialog_activity);
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(R.string.exit_fw);
		builder.setTitle(R.string.notice);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ma.userPwd="";
				ma.cfg.putString(Cnt.USER_PWD, "");//userPsd
				Toasts.makeText(LogoutDialogActivity.this, R.string.exit_ok, Toast.LENGTH_LONG).show();
				dialog.dismiss();
				LogoutDialogActivity.this.finish();
			}
		});

		builder.setNegativeButton(R.string.cancle,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						LogoutDialogActivity.this.finish();
					}
				});

		CustomDialog create = builder.create();
		create.setCancelable(false);
		create.show();
	}
	
	
	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}

}
