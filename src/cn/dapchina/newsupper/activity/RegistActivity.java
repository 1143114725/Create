package cn.dapchina.newsupper.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.util.MD5;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;

public class RegistActivity extends BaseActivity implements OnClickListener {

	/**
	 * 大树 修改一下 继承自BaseActivity
	 */
	ImageView leftIv;
	private EditText etUser, etPass;
	private Button payRegist, freeRegist; // 大树 注册 按钮
	private ProgressDialog pd;// 大树 滚动条
	private String strEmail; // 大树 邮箱
	private Dialog alertDialog;// 大 树 对话框
	private String regist_userId, regist_pwd; // 大树 注册用户名 密码
	// 大树 免费注册 激活邮箱

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// 发送邮件
			case 1:
				thSendMail();
				break;
			// 登陆成功
			case 2:
				pd.cancel();
				alertDialog.cancel();
				Toasts.makeText(RegistActivity.this, RegistActivity.this.getString(R.string.file_send_success), Toast.LENGTH_LONG).show();
				RegistActivity.this.finish();
				overridePendingTransition(R.anim.right1, R.anim.left1);
				break;
			// 邮箱不存在
			case 3:
				pd.cancel();
				Toasts.makeText(RegistActivity.this, RegistActivity.this.getString(R.string.email_not_exit), Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.regisit_activity);
		leftIv = (ImageView) findViewById(R.id.login_left_iv);
		leftIv.setOnClickListener(this);
		etUser = (EditText) findViewById(R.id.et_regisit_user);
//		etUser.setInputType(InputType.TYPE_NULL);
//		etUser.setOnTouchListener(new FocusListener());
		userNameLL = (LinearLayout) findViewById(R.id.userNameLL);
		userPassLL = (LinearLayout) findViewById(R.id.userPassLL);
		userNameLL.setOnClickListener(this);
		userPassLL.setOnClickListener(this);
		inm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 大树 添加专业免费 注册事件 regisit_activity 中 用户名 和密码的 id 修改了一下 切记
		etPass = (EditText) findViewById(R.id.et_regisit_pwd);
		payRegist = (Button) findViewById(R.id.button1);
		freeRegist = (Button) findViewById(R.id.button2);
		// 版本兼容 非访问专家版本隐藏免费登录
		if (1 != Cnt.appVersion&&4!=Cnt.appVersion) {
			freeRegist.setVisibility(View.GONE);
		}
		payRegist.setOnClickListener(this);
		freeRegist.setOnClickListener(this);
		// 大树 以上部分
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RegistActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
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

	
	private LinearLayout userNameLL, userPassLL;
	private InputMethodManager inm;
	int userCount = 0;
	int passCount = 0;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userNameLL:
			passCount=0;
			etUser.requestFocus();
			if(0==userCount){
				//显示键盘
				inm.showSoftInput(etUser, 0);
				userCount=1;
			}else{
				//隐藏键盘
				inm.hideSoftInputFromWindow(etUser.getWindowToken(), 0);
				userCount=0;
			}
			break;
		case R.id.userPassLL:
			userCount=0;
			etPass.requestFocus();
			if(0==passCount){
				//显示键盘
				inm.showSoftInput(etPass, 0);
				passCount=1;
			}else{
				//隐藏键盘
				inm.hideSoftInputFromWindow(etPass.getWindowToken(), 0);
				passCount=0;
			}
			
			break;
		case R.id.login_left_iv:
			RegistActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		// 大树 付费版注册
		case R.id.button1:
			if (!NetUtil.checkNet(RegistActivity.this)) {
				Toasts.makeText(RegistActivity.this, R.string.exp_net, Toast.LENGTH_SHORT).show();
				return;
			}
			regist_userId = etUser.getText().toString().trim();
			regist_pwd = etPass.getText().toString().trim();
			if (Util.isEmpty(regist_userId)) {
				Util.viewShake(this, etUser);
				etUser.setHint(getResources().getString(R.string.null_userid));
				etUser.setHintTextColor(Color.RED);
				return;
			}
			if (Util.isEmpty(regist_pwd)) {
				Util.viewShake(this, etPass);
				etPass.setHint(getResources().getString(R.string.null_pwd));
				etPass.setHintTextColor(Color.RED);
				return;
			}

			Cnt.changeNewURL(false, payIp, freeIp, payIp);
			// 如果用户名或则密码长度 太长 那么给出提示， 6-20 位 提示
			  
			if (regist_userId.length() >= 20 || regist_pwd.length() >= 20) {
				Toasts.makeText(RegistActivity.this, RegistActivity.this.getString(R.string.no_meet), Toast.LENGTH_SHORT).show();
				return;
			}
			/**
			 * 注册接口地址
			 * */
			HashMap<String, Object> rhmPay = new HashMap<String, Object>();
			rhmPay.put(Cnt.USER_ID, regist_userId);
			rhmPay.put(Cnt.USER_PWD, MD5.Md5Pwd(regist_pwd));
			// 大树 付费版地址
			show();
			MainService.newTask(new Task(TaskType.TS_PAY_REGIST, rhmPay));
			break;
		// 大树 免费版注册
		case R.id.button2:
			if (!NetUtil.checkNet(RegistActivity.this)) {
				Toasts.makeText(RegistActivity.this, R.string.exp_net, Toast.LENGTH_SHORT).show();
				return;
			}
			regist_userId = etUser.getText().toString().trim();
			regist_pwd = etPass.getText().toString().trim();
			if (Util.isEmpty(regist_userId)) {
				Util.viewShake(this, etUser);
				etUser.setHint(getResources().getString(R.string.null_userid));
				etUser.setHintTextColor(Color.RED);
				return;
			}
			if (Util.isEmpty(regist_pwd)) {
				Util.viewShake(this, etPass);
				etPass.setHint(getResources().getString(R.string.null_pwd));
				etPass.setHintTextColor(Color.RED);
				return;
			}

			// 如果用户名或则密码长度 太长 那么给出提示， 6-20 位 提示
			if (regist_userId.length() >= 20 || regist_pwd.length() >= 20) {
				Toasts.makeText(RegistActivity.this, RegistActivity.this.getString(R.string.no_meet), Toast.LENGTH_SHORT).show();
				return;
			}
			Cnt.changeNewURL(true, freeIp, freeIp, payIp);
			/**
			 * 注册接口地址
			 * */
			HashMap<String, Object> rhmFree = new HashMap<String, Object>();
			rhmFree.put(Cnt.USER_ID, regist_userId);
			rhmFree.put(Cnt.USER_PWD, MD5.Md5Pwd(regist_pwd));
			show();
			MainService.newTask(new Task(TaskType.TS_REGIST, rhmFree));
			break;
		default:
			break;
		}
	}

	// 大树 发送邮箱
	private void thSendMail() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sendMail("depaiceshi", "depaiceshi1", strEmail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 大树 发送邮箱
	private void sendMail(String userName, String passWord, String email) throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.163.com");
		props.setProperty("mail.smtp.auth", "true");
		class MyAuthenticator extends Authenticator {

			private String name;
			private String pass;

			public MyAuthenticator(String username, String password) {
				this.name = username;
				this.pass = password;
			}

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(name, pass);
			}

		}
		Session session = Session.getInstance(props, new MyAuthenticator(userName, passWord));
		MimeMessage message = new MimeMessage(session);
		String emailFrom = userName + "@163.com";
		message.setFrom(InternetAddress.parse(MimeUtility.encodeText("SIM SYSTEM") + "<" + emailFrom + ">")[0]);
		message.setRecipient(RecipientType.TO, new InternetAddress(email));
		message.setSentDate(new Date());
		message.setSubject(this.getString(R.string.mystery_title));

		// 邮件正文
		MimeBodyPart text = new MimeBodyPart();

		text.setContent(
				this.getString(R.string.dear_visite) + "<br><br>" + this.getString(R.string.back_info) + "<br>" + "<a href='" + this.getString(R.string.regist_url, regist_userId, regist_pwd, email)
						+ "'>" + this.getString(R.string.regist_url) + "</a>" + "<br><br>" + this.getString(R.string.edit_survey_help_one) + "<br>" + this.getString(R.string.edit_survey_help_two)
						+ "<br>" + this.getString(R.string.edit_survey_help_three) + "<br>" + this.getString(R.string.edit_survey_help_four) + "<br><br>" + this.getString(R.string.last_info) + "<br>",
				"text/html;charset=gbk");

		// 描述数据关系
		MimeMultipart multipart = new MimeMultipart();

		/** 结束加的附件 */

		multipart.addBodyPart(text);
		multipart.setSubType("related");

		message.setContent(multipart);

		message.saveChanges();
		try {
			Transport.send(message);
			session.getTransport().close();
		} catch (Exception e) {
			// TODO: handle exception
			handler.sendEmptyMessage(3);
			return;
		}
		// 回收垃圾内存
		handler.sendEmptyMessage(2);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch ((Integer) param[0]) {
		// 大树 免费 注册 付费注册 都在这里
		case TaskType.TS_REGIST:
		case TaskType.TS_PAY_REGIST:
			dismiss();
			if (null != param[1]) {
				int state = Integer.valueOf(param[1].toString());
				if (state == 100) {
					Toasts.makeText(this, getString(R.string.sucess_regist), Toast.LENGTH_LONG).show();
					alertDialog = new Dialog(this, R.style.question_ds);
					alertDialog.setTitle(this.getString(R.string.notice));
					alertDialog.setContentView(R.layout.edit_email);
					final EditText editEmail = (EditText) alertDialog.findViewById(R.id.editEmail);
					final Button sendBtn = (Button) alertDialog.findViewById(R.id.sendBtn);
					final Button cancelSendBtn = (Button) alertDialog.findViewById(R.id.cancelSendBtn);
					cancelSendBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							alertDialog.cancel();
						}
					});
					sendBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							strEmail = editEmail.getText().toString();
							if (null == strEmail || "".equals(strEmail.trim())) {
								Toasts.makeText(RegistActivity.this, RegistActivity.this.getString(R.string.write_email_empty), Toast.LENGTH_LONG).show();
								return;
							} else if (!Util.isFormat(strEmail, 4)) {
								Toasts.makeText(RegistActivity.this, RegistActivity.this.getString(R.string.write_email_error), Toast.LENGTH_LONG).show();
								return;
							} else if (!NetUtil.checkNet(RegistActivity.this)) {
								Toasts.makeText(RegistActivity.this, RegistActivity.this.getString(R.string.net_exception), Toast.LENGTH_LONG).show();
								return;
							} else {
								pd = new ProgressDialog(RegistActivity.this);
								pd.setTitle(RegistActivity.this.getString(R.string.notice));
								pd.setMessage(RegistActivity.this.getString(R.string.screen_send_mail));
								pd.setCancelable(false);
								pd.show();
								// 直接发送邮箱
								handler.sendEmptyMessage(1);

							}
						}
					});
					alertDialog.show();

				} else if (state == 98) {
					Toasts.makeText(this, getString(R.string.sucess_fail), Toast.LENGTH_LONG).show();
				} else {
					Toasts.makeText(this, getString(R.string.sucess_fail_reason), Toast.LENGTH_LONG).show();
				}
			} else {
				Toasts.makeText(this, getString(R.string.regisit_fail), Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

}
