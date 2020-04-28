package cn.dapchina.newsupper.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.xutils.x;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.DapException;
import cn.dapchina.newsupper.db.DBService;
import cn.dapchina.newsupper.util.ComUtil;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;

/**
 * 全局变量
 */
public class MyApp extends Application implements UncaughtExceptionHandler {

	public Config cfg;

	public DBService dbService;

	// public String currTitle = "";

	/**
	 * 当前登录进来的用户的id号
	 */
	public String userId = "";
	public String authorId = "";

	// public String userName = "";

	public String userPwd = "";

	// public double lat = 0.0;
	//
	// public double lng = 0.0;
	//
	// public User user;

	public String versionCode = "";

	public String macAddress = "";
	private Context applicationContext;

	/**
	 * 捕获异常
	 */
	private Map<String, String> infos = new HashMap<String, String>();
	private UncaughtExceptionHandler mDefaultHandler;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// 全局退出的方法
	private ArrayList<Activity> activitylist = new ArrayList<Activity>();

	/**
	 * 添加ativity
	 * 
	 * @param activity
	 *  全局退出的方法
	 */
	public void addActivity(Activity activity) {
		activitylist.add(activity);
	}

	/**
	 * 如果activity已经 destory了 就移除 
	 *  全局退出的方法
	 * @param activity
	 */
	public void remove(Activity activity) {
		activitylist.remove(activity);
	}
	/** 
	* 遍历  结束activity  并且退出 
	* 全局退出的方法
	*/  
	public void exit() {
		try {
			for (Activity activity : activitylist) {
				if (activity != null)
					activity.finish();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	@Override
	public void onCreate() {
		versionCode = Util.getVersion(this);
		applicationContext = getApplicationContext();
		macAddress = Util.getLocalMacAddress(applicationContext);
		cfg = new Config(applicationContext);
		dbService = new DBService(applicationContext);
		
		 x.Ext.init(this);
	        x.Ext.setDebug(true); //输出debug日志，开启会影响性能
	        
//		startCatch();
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/**
	 * 1.开始捕捉不明异常          
	 */
	private void startCatch() {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 2.捕获异常
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 假如有用户名在抓取,用户名不为空
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// android.os.Process.killProcess(android.os.Process.myPid());
			exit();
			// System.exit(1);
		}
	}

	/**
	 * 3.自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toasts.makeText(MyApp.this, R.string.unex, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		// saveFeedData(saveParams);
		// 收集设备参数信息
		collectDeviceInfo(this);
		// 保存日志文件，创建文件
		String name = saveCrashInfo2File(ex);
		String sdcard = ComUtil.getFilePath();
		DapException dapException = new DapException();
		dapException.setEnable(1);
		dapException.setFileName(name);
		dapException.setFilePath(sdcard);
		dapException.setMacAddress(macAddress);
		dapException.setUserId("".equals(userId) ? "游客" : userId);
		if(!Util.isEmpty(name)){
			boolean addTab = dbService.addTab(dapException);
		}else{
			return true;
		}
		// if(addTab){
		// System.out.println("添加成功");
		// }else{
		// System.out.println("添加失败");
		// }
		// 假如有网,发邮件
		if (ComUtil.checkNet(this)) {
			// 发完了,在记录状态。
			threadSendEmail(dapException);
		}
		// 否则就不发
		else {

		}
		// if (log.exists()) {
		// new UploadInfor(sdcard, name, System.currentTimeMillis() + "");
		// }
		return true;
	}

	/**
	 * 6.theSendEmail 发送邮件
	 */
	private void threadSendEmail(final DapException dapException) {
		new Thread() {
			public void run() {
				try {
					sendMail("depaiceshi", "depaiceshi1", "bug@dapchina.cn", dapException);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 7.发送邮箱方法
	 */
	private void sendMail(String userName, String passWord, String email, DapException dapException) throws Exception {
		// System.out.println("邮件:" + email);
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
				return new PasswordAuthentication(name, pass);// @sohu.com
			}

		}
		Session session = Session.getInstance(props, new MyAuthenticator(userName, passWord));
		// Session session = Session.getInstance(props, new Authenticator() {
		//
		// @Override
		// protected PasswordAuthentication getPasswordAuthentication() {
		// return new PasswordAuthentication("dapserver",
		// "dapchina_2016");//@sohu.com
		// }
		//
		// });
		MimeMessage message = new MimeMessage(session);
		String emailFrom = userName + "@163.com";
		message.setFrom(InternetAddress.parse(MimeUtility.encodeText("SIM SYSTEM") + "<" + emailFrom + ">")[0]);
		message.setRecipient(RecipientType.TO, new InternetAddress(email));
		message.setSentDate(new Date());
		message.setSubject(this.getString(R.string.error_message, dapException.getFileName()));
		// message.setContent("Dear Participant,<br><br>This is a reminder to take medication according to your dosing schedule and complete the appropriate diary pages.Thanks.<br><br>CIMS System<br>",
		// "text/html");

		// 邮件正文
		MimeBodyPart text = new MimeBodyPart();

		text.setContent(this.getString(R.string.get_message, dapException.getUserId(), dapException.getMacAddress(), dapException.getFilePath()), "text/html;charset=gbk");

		// 描述数据关系
		MimeMultipart multipart = new MimeMultipart();
		// 附件
		File log = new File(dapException.getFilePath(), dapException.getFileName());
		/** 开始加的附件 */
		MimeBodyPart mbpFile = new MimeBodyPart();
		// 得到数据源
		FileDataSource fds = new FileDataSource(log);
		// 得到附件本身并至入BodyPart
		mbpFile.setDataHandler(new DataHandler(fds));
		// 得到文件名同样至入BodyPart
		mbpFile.setFileName(fds.getName());
		/** 结束加的附件 */
		multipart.addBodyPart(mbpFile);
		multipart.addBodyPart(text);
		multipart.setSubType("related");

		message.setContent(multipart);

		message.saveChanges();
		Transport.send(message);
		session.getTransport().close();
		// DataHandler dh = new DataHandler("","");
		// 回收垃圾内存
		dbService.updateTabToUnEnable(dapException.getFileName());
	}

	/**
	 * 4.收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				// infos.put("userName", DapData.config.user.userId);
				infos.put("userName", "".equals(userId) ? "游客" : userId);
				infos.put("dateTime", ComUtil.getTimeSec());
				infos.put("macAddress", macAddress);
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (Exception e) {
			// Log.e("jy", "an error occured when collect package info", e);
			e.printStackTrace();
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				// Log.d("jy", field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				// Log.e("jy   ", "an error occured when collect crash info",
				// e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 5.保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		// System.out.println("日志信息--->"+sb.toString());
		String fileName = ("".equals(userId) ? "游客" : userId) + "_" + ComUtil.getLocalMacAddress(this) + "_" + Util.getTime(System.currentTimeMillis(), -1) + ".log";
		try {
			// 判断SD卡是否存在，并且是否具有读写权限
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// String path = ComUtil.getFilePath();
				File dir = new File(ComUtil.getFilePath());
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(dir + File.separator + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
				return fileName;
			}else{
				Toasts.makeText(this, R.string.phone_no_permission, Toast.LENGTH_LONG).show();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
