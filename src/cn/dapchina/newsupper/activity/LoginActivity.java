package cn.dapchina.newsupper.activity;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.util.MD5;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.SpUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;

public class LoginActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	ImageView leftIv;
	private EditText etUser, etPass;
	TextView registTv; // 大树 记得修改 XML 注册
	private Button payLogin, freeLogin; // 大树 付费登陆 免费 登陆
	private MyApp ma;

	private LinearLayout userNameLL, userPassLL;
	private InputMethodManager inm;
	private CheckBox checkBox;
	private String isMemory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_activity);
		leftIv = (ImageView) findViewById(R.id.login_left_iv);
		leftIv.setOnClickListener(this);
		registTv = (TextView) findViewById(R.id.regist_tv);
		registTv.setOnClickListener(this);
		etPass = (EditText) findViewById(R.id.password_et);
		etUser = (EditText) findViewById(R.id.username_et);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		ma = (MyApp) getApplication();
		if (!Util.isEmpty(ma.cfg.getString(Cnt.USER_ID, ""))) {
			etUser.setText(ma.cfg.getString(Cnt.USER_ID, ""));
		}
		isMemory = ma.cfg.getString("isMemory", "no");
		// 进入界面时，这个if用来判断SharedPreferences里面name和password有没有数据，有的话则直接打在EditText上面
		if (isMemory.equals("yes")) {
			String password = ma.cfg.getString(Cnt.USER_PWD, "");
			if (!Util.isEmpty(password) && !Util.isEmpty(ma.cfg.getString(Cnt.USER_ID, ""))) {
				etPass.setText(password);
			}
			checkBox.setChecked(true);
		}
		checkBox.setOnCheckedChangeListener(this);

		userNameLL = (LinearLayout) findViewById(R.id.userNameLL);
		userPassLL = (LinearLayout) findViewById(R.id.userPassLL);
		userNameLL.setOnClickListener(this);
		userPassLL.setOnClickListener(this);
		inm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

		// etUser.setInputType(InputType.TYPE_NULL);
		// etUser.setOnTouchListener(new FocusListener());
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LoginActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	int userCount = 0;
	int passCount = 0;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.userNameLL:
			passCount = 0;
			etUser.requestFocus();
			if (0 == userCount) {
				// 显示键盘
				inm.showSoftInput(etUser, 0);
				userCount = 1;
			} else {
				// 隐藏键盘
				inm.hideSoftInputFromWindow(etUser.getWindowToken(), 0);
				userCount = 0;
			}
			break;
		case R.id.userPassLL:
			userCount = 0;
			etPass.requestFocus();
			if (0 == passCount) {
				// 显示键盘
				inm.showSoftInput(etPass, 0);
				passCount = 1;
			} else {
				// 隐藏键盘
				inm.hideSoftInputFromWindow(etPass.getWindowToken(), 0);
				passCount = 0;
			}

			break;
		case R.id.login_left_iv:
			LoginActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.regist_tv:
			Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zzright, R.anim.zzleft);
			break;
		// 大树 付费登陆
		case R.id.pay_login_btn:
			if (!NetUtil.checkNet(LoginActivity.this)) {
				Toasts.makeText(LoginActivity.this, R.string.exp_net, Toast.LENGTH_SHORT).show();
				return;
			}
			String userId = etUser.getText().toString().trim().toLowerCase();
			String pwd = etPass.getText().toString().trim();
			if (Util.isEmpty(userId)) {
				Util.viewShake(this, etUser);
				etUser.setError(getResources().getString(R.string.null_userid));
				return;
			}
			if (Util.isEmpty(pwd)) {
				Util.viewShake(this, etPass);
				etPass.setError(getResources().getString(R.string.null_pwd));
				return;
			}
			Cnt.changeNewURL(false, payIp, freeIp, payIp);
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put(Cnt.USER_ID, userId);
			hm.put(Cnt.USER_PWD, MD5.Md5Pwd(pwd));
			hm.put(Cnt.USER_MAC, Util.getLocalMacAddress(LoginActivity.this));
			show();
			MainService.newTask(new Task(TaskType.TS_ONLINE_LOGIN, hm));
			break;
		// 大树 免费 登陆
		case R.id.free_login_btn:
			if (!NetUtil.checkNet(LoginActivity.this)) {
				Toasts.makeText(LoginActivity.this, R.string.exp_net, Toast.LENGTH_SHORT).show();
				return;
			}
			String userIdFree = etUser.getText().toString().trim().toLowerCase();
			String pwdFree = etPass.getText().toString().trim();
			if (Util.isEmpty(userIdFree)) {
				Util.viewShake(this, etUser);
				etUser.setError(getResources().getString(R.string.null_userid));
				return;
			}
			if (Util.isEmpty(pwdFree)) {
				Util.viewShake(this, etPass);
				etPass.setError(getResources().getString(R.string.null_pwd));
				return;
			}
			Cnt.changeNewURL(true, freeIp, freeIp, payIp);
			HashMap<String, Object> hmFree = new HashMap<String, Object>();
			hmFree.put(Cnt.USER_ID, userIdFree);
			hmFree.put(Cnt.USER_PWD, MD5.Md5Pwd(pwdFree));
			hmFree.put(Cnt.USER_MAC, Util.getLocalMacAddress(LoginActivity.this));
			show();
			MainService.newTask(new Task(TaskType.TS_FREE_LOGIN, hmFree));
			break;
		default:
			break;
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		// 大树 登陆 免费 付费 地址
		payLogin = (Button) findViewById(R.id.pay_login_btn);
		freeLogin = (Button) findViewById(R.id.free_login_btn);
		// 版本兼容 非访问专家版本隐藏免费登录
		if (1 != Cnt.appVersion && 4 != Cnt.appVersion) {
			freeLogin.setVisibility(View.GONE);
		}
		payLogin.setOnClickListener(this);
		freeLogin.setOnClickListener(this);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch ((Integer) param[0]) {
		// 大树 免费 付费 登陆
		case TaskType.TS_ONLINE_LOGIN:
			refreshMethod(param);
			// 如果是 付费的那么 isFree==false 大树免费付费 1
			cfg.putBoolean("isFree", false);
			break;
		case TaskType.TS_FREE_LOGIN:
			refreshMethod(param);
			// 如果是 免费的 那么 isFree==true 大树免费付费 2
			cfg.putBoolean("isFree", true);
			break;
		default:
			break;
		}
	}

	private void refreshMethod(Object... param) {
		dismiss();
		if (null != param[1]) {
			HashMap<String, String> hm = (HashMap<String, String>) param[1];
			if (!Util.isEmpty(hm)) {
				// System.out.println(hm);
				String state = hm.get("state");
				if (!Util.isEmpty(state)) {
					int s = Integer.parseInt(state);
					switch (s) {
					case Cnt.STATE_SUCCESS:
						Toasts.makeText(LoginActivity.this, getString(R.string.sucess_login), Toast.LENGTH_LONG).show();
						cfg.putBoolean(Cnt.LOGIN_MODE, false);
						ma.userId = etUser.getText().toString().trim().toLowerCase();
						ma.userPwd = etPass.getText().toString().trim();
						// 大树测试
						// ma.userId="dap1";
						// ma.userPwd="dap1";
						// System.out.println("ma.userId:"+ma.userId);
						// System.out.println("ma.userPwd:"+ma.userPwd);
						cfg.putString(Cnt.USER_ID, ma.userId);
						cfg.putString(Cnt.USER_PWD, ma.userPwd);// userPsd
						
						SpUtil.setParam(LoginActivity.this,"userPsd",etPass.getText().toString().trim());
						
						cfg.putString("memoryPP", ma.userPwd);
						String authorId = hm.get("authorID");
						if (Util.isEmpty(authorId)) {
							Toasts.makeText(LoginActivity.this, getString(R.string.login_error, s), Toast.LENGTH_LONG).show();
							return;
						}
						cfg.putString("authorId", authorId);
						// 大树设置第一次访问标志
						cfg.putBoolean("isFirst", true);
						LoginActivity.this.finish();
						overridePendingTransition(R.anim.right1, R.anim.left1);
						break;
					case 98:
						Toasts.makeText(LoginActivity.this, getString(R.string.account_frozen), Toast.LENGTH_LONG).show();
						break;

					case 97:
						Toasts.makeText(LoginActivity.this, getString(R.string.error_permission), Toast.LENGTH_LONG).show();
						break;
					case 99:
						Toasts.makeText(LoginActivity.this, getString(R.string.user_error), Toast.LENGTH_LONG).show();
						break;
					default:
						Toasts.makeText(LoginActivity.this, getString(R.string.login_error, s), Toast.LENGTH_LONG).show();
						break;
					}
				} else {
					Toasts.makeText(this, R.string.fail_login, Toast.LENGTH_LONG).show();
				}
			} else {
				Toasts.makeText(this, R.string.fail_login, Toast.LENGTH_LONG).show();
			}
		} else {
			Toasts.makeText(this, R.string.fail_login, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			ma.cfg.putString("isMemory", "yes");
		} else {
			ma.cfg.putString("isMemory", "no");
		}
	}

}
