package cn.dapchina.newsupper.bean;

import java.io.File;
import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/** 应用升级对象 **/
public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 版本号路径 **/
	private double version = 0;
	/** 路径 **/
	private String path = null;

	/** 更新内容 **/
	private String content = null;
	/** 提示消息 **/
	private String notice = null;

	public Application() {

	}

	public Application(double version, String path) {
		this.version = version;
		this.path = path;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/* 在手机上打开文件的method */
	public void openFile(Context _c, File f) {
		if (null == f || !f.exists()) {
			return;
		}
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		/* 设置intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), getMIMEType(f));
		_c.startActivity(intent);
	}

	/** 判断文件MimeType的method **/
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/** 取得扩展名 **/
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

		/** 依扩展名的类型决定MimeType **/
		if (end.equals("apk")) {
			/** android.permission.INSTALL_PACKAGES **/
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		/** 如果无法直接打开，就跳出软件列表给用户选择 **/
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}

	public PackageInfo getPackageInfo(Context _c) throws Exception {
		PackageManager pm = _c.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(_c.getPackageName(), PackageManager.GET_ACTIVITIES);
		return pi;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}
