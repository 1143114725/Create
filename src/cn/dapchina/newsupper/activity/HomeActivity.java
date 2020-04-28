package cn.dapchina.newsupper.activity;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.slide.CenterFagment;
import cn.dapchina.newsupper.slide.LeftFragment;
import cn.dapchina.newsupper.slide.RightFragment;
import cn.dapchina.newsupper.slide.SlidingMenu;
import cn.dapchina.newsupper.util.ComUtil;
import cn.dapchina.newsupper.util.LogUtil;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;

public class HomeActivity extends FragmentActivity {

	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	RightFragment rightFragment;
	CenterFagment centerFagment;
	FragmentTransaction t;
	public static boolean isHide = true;
	private MyApp ma;// 自动转换地址
	private boolean isFree;// false是免费 true是付费
	private String freeIp, payIp;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.slide_activity);
		if (null == ma) {
			ma = (MyApp) getApplication();
		}
		// 假如是访问专家版本,读取是什么版本 注释部分
		// if (Cnt.appVersion == 1 || Cnt.appVersion == 4) {
		// isFree = ma.cfg.getBoolean("isFree");
		// // System.out.println("isFree:"+isFree);
		// } else {
		// // 其他的没有免费版
		// isFree = true;
		// }
		// // 兼容性
		// // 假如是访问专家读取配置的地址
		// if (Cnt.appVersion == 1) {
		// // 设置域名
		// payIp = ma.cfg.getString("payIp", "");
		// freeIp = this.getString(R.string.real_free_ip);
		// if (Util.isEmpty(payIp)) {
		// payIp = this.getString(R.string.real_pay_ip);
		// }
		// } else {
		// // 非访问专家直接使用他们的地址
		// // 设置域名
		// freeIp = this.getString(R.string.real_free_ip);
		// // 美国服务器 版本控制
		// if (Cnt.appVersion == 4) {
		// payIp = this.getString(R.string.real_pay_ipsos_ip);
		// }
		// // IMS
		// if (Cnt.appVersion == 3) {
		// payIp = this.getString(R.string.real_pay_ims_ip);
		// }
		// // IPSOS
		// if (Cnt.appVersion == 2) {
		// payIp = this.getString(R.string.real_pay_ipsos1_ip);
		// }
		// }
		// // 假如是免费的改成免费地址
		// if (!isFree) {
		// Cnt.changeNewURL(true, freeIp, freeIp, payIp);
		// } else {
		// Cnt.changeNewURL(false, payIp, freeIp, payIp);
		// }
		// System.out.println("Cnt.SHOUQUAN_URL:"+Cnt.SHOUQUAN_URL);
		init();
	}

	@Override
	protected void onResume() {
		time = 0;
		isFree = ma.cfg.getBoolean("isFree");
		LogUtil.printfLog("isFree", isFree);
		LogUtil.printfLog("appUrl", Cnt.APP_URL);
		super.onResume();
	}

	private int time = 0;// 次数
	private long myTime = 0;// 时间

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 按两次并且间隔小于5秒才能退出
			if (0 == time) {
				Toasts.makeText(HomeActivity.this, R.string.once_again, Toast.LENGTH_LONG).show();
				myTime = System.currentTimeMillis();
				time = 1;
				return false;
			} else if (1 == time) {
				if (System.currentTimeMillis() - myTime > 5000) {
					time = 1;
					myTime = System.currentTimeMillis();
					Toasts.makeText(HomeActivity.this, R.string.once_again, Toast.LENGTH_LONG).show();
					return false;
				} else {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.center_frame, null));
		/**
		 * 碎片事件 把左边布局 替换成 左 碎片 把右边布局 替换成 右碎片 把中间布局 替换成 中间viewpage 即可
		 */
		t = this.getSupportFragmentManager().beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);

		rightFragment = new RightFragment();
		t.replace(R.id.right_frame, rightFragment);
		centerFagment = new CenterFagment();
		t.replace(R.id.center_frame, centerFagment);
		t.commit();
		mSlidingMenu.setLeftFragment(leftFragment);
	}

	public void showLeft() {
		leftFragment.onChange(isHide);
		isHide = !isHide;
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

	// 跳转 各个 页面
	public <T> void skipActivity(Class<T> cls) {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, cls);
		startActivityForResult(intent, 10);
		overridePendingTransition(R.anim.right, R.anim.left);
	}

	// 跳转 各个 页面
	public <T> void skipActivity(Class<T> cls, int request) {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, cls);
		startActivityForResult(intent, request);
		overridePendingTransition(R.anim.right, R.anim.left);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 10:
			
			break;
		case 20:
			//上传没登陆处理
			if(!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)){
				Intent intent=new Intent(HomeActivity.this,UploadActivity.class);
				startActivity(intent);
			}else{
				
			}
			break;
		case 30:
			//订阅没登陆处理
			if(!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)){
				Intent intent=new Intent(HomeActivity.this,SubscibeActivity.class);
				startActivity(intent);
			}else{
				
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
