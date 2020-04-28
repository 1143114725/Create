package cn.dapchina.newsupper.activity;

import android.app.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.CustomProgressDialog;

/**
 * @author kejunyao 自定义Activity基类
 */
public abstract class BaseActivity extends Activity {
	// 手指向右滑动时的最小速度
	private static final int XSPEED_MIN = 1000;
	// 手指向右滑动时的最小距离
	private static final int XDISTANCE_MIN = 250;
	//手势监测
	GestureDetector mGesture = null;
	public String freeIp, payIp;// 大树 免费地址 付费地址
	public Config cfg; // 大树 配置变量

	public volatile CustomProgressDialog progressDialog;

	public Dialog dialog;

	protected TextView msgTitle, msgContent;

	public Button btnOk, btnCancel;
	private MyApp ma;

	// protected int screenWidth;

	// protected int screenHeight;

	// private Display dis;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/** 设置无标题 **/
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** 设置全屏 **/
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		// this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
		// FLAG_HOMEKEY_DISPATCHED);//关键代码
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		MainService.addActivity(this);
		// dis = getWindowManager().getDefaultDisplay();
		dialog = new Dialog(this, R.style.question_ds);
		dialog.setContentView(R.layout.msg_dialog);
		msgTitle = (TextView) dialog.findViewById(R.id.msg_title);
		msgContent = (TextView) dialog.findViewById(R.id.msg_content);
		btnOk = (Button) dialog.findViewById(R.id.ok_btn);
		btnCancel = (Button) dialog.findViewById(R.id.cancel_btn);


		mGesture = new GestureDetector(this, new GestureListener());
		// 大树 共用变量 对象
		cfg = new Config(this);
		// 兼容性
		//假如是访问专家读取配置的地址
		if (Cnt.appVersion==1) {  
			// 设置域名
			payIp = cfg.getString("payIp", "");
			freeIp = this.getString(R.string.real_free_ip);
			if (Util.isEmpty(payIp)) {
				payIp = this.getString(R.string.real_pay_ip);
			}
		} else {
			// 其他版本 
			freeIp = this.getString(R.string.real_free_ip);
			// 美国服务器  版本控制
			if (Cnt.appVersion==4) {
				payIp = this.getString(R.string.real_pay_ipsos_ip);
			}
			//  IMS  
			if (Cnt.appVersion==3) {
				payIp=this.getString(R.string.real_pay_ims_ip);
			}
			// IPSOS
			if (Cnt.appVersion==2) {
				payIp=this.getString(R.string.real_pay_ipsos1_ip);
			}
			//北京农业银行
			if (Cnt.appVersion==5) {
				payIp=this.getString(R.string.real_farmbank_ip);
			}
		}
	}

	/**
	 * 初始化界面
	 */
	public abstract void init();

	public abstract void refresh(Object... param);

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!MainService.isRun) {
			Intent i = new Intent(this, MainService.class);
			this.startService(i);
		}
		init();
	}

	public void show() {
		if (null == progressDialog) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage(this.getResources().getString(R.string.please_wait));
			progressDialog.findViewById(R.id.loadingImageView).setOnLongClickListener(new DismissListener());
			progressDialog.setOnKeyListener(new MyOnKeyListener());
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	public void showWeb() {
		if (null == progressDialog) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage(this.getResources().getString(R.string.please_wait));
			progressDialog.setOnKeyListener(new MyOnKeyListener());
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	public void dismiss() {
		if (null != progressDialog) {
			try {
				progressDialog.dismiss();
				progressDialog = null;
			} catch (Exception e) {

			}
		}

	}

	public void showMessage(int type) {

	}

	public void hideMessage() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	protected class MyOnKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (KeyEvent.KEYCODE_SEARCH == keyCode || KeyEvent.KEYCODE_BACK == keyCode) {
				return true;
			}
			return false;
		}

	}

	public MyOnKeyListener getMyOnKeyListener() {
		return new MyOnKeyListener();
	}

	protected void quit() {
		Intent it = new Intent(BaseActivity.this, MainService.class);
		BaseActivity.this.stopService(it);
		BaseActivity.this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private final class DismissListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			dismiss();
			return true;
		}

	};
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(this.getClass().getName().equals("cn.dapchina.newsupper.activity.NativeModeActivity")){
			return super.dispatchTouchEvent(ev);
		}
		if(this.getClass().getName().equals("cn.dapchina.newsupper.activity.NativeReviewActivity")){
			return super.dispatchTouchEvent(ev);
		}
			this.mGesture.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	




/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return mGesture.onTouchEvent(event);
	}
	
	
	class GestureListener extends SimpleOnGestureListener  
    {
		
        @Override  
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
                float velocityY)  
        {  
        	if(e1!=null&&e2!=null){
        		
        		if(e2.getRawX()-e1.getRawX()>XDISTANCE_MIN&&e2.getRawY()-e1.getRawY()<200 &&Math.abs(velocityX)>XSPEED_MIN&&Math.abs(velocityY)<XSPEED_MIN){
        			finish();
        			overridePendingTransition(R.anim.right1, R.anim.left1);
        			return true;
        		}
        		return false;
        	}  
        	return false;
        	}
  
          
    }  
	
}
