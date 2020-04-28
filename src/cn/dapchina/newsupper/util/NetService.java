package cn.dapchina.newsupper.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import android.util.Log;
import cn.dapchina.newsupper.bean.HttpBean;
import cn.dapchina.newsupper.global.Cnt;


/**
 * **************************************
 * 
 * @description * 网络数据流或字符串获取 *
 * @author kejunyao *
 * @creation 2012-5-9 * * *
 **************************************** 
 */
public class NetService {
	/** 以流的形式读取远程文件 **/
	public static InputStream openUrl(String path,
			HashMap<String, Object> params, String reqMethod) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(path);//
		if (null != params)
			sb.append("?") //
					.append(encodeUrl(params));
		if(Cnt.D)Log.i("kjy", sb.toString());
		HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString())
				.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod(reqMethod);// POST
		/** 允许对外输出 **/
		//conn.setDoOutput(true);
		/** 假如服务器对本次请求响应成功 **/
		if (200 == conn.getResponseCode()) {
			return conn.getInputStream();
		}
		conn.disconnect();
		return null;
	}
	/** 判断是否有网（内外网）**/
	public static Boolean checknet(String path,
			HashMap<String, Object> params, String reqMethod) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(path);//
		if (null != params)
			sb.append("?") //
					.append(encodeUrl(params));
		if(Cnt.D)Log.i("kjy", sb.toString());
		HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString())
				.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod(reqMethod);// POST
		/** 允许对外输出 **/
		//conn.setDoOutput(true);
		/** 假如服务器对本次请求响应成功 **/
		if (200 == conn.getResponseCode()) {
			return true;
		}
		conn.disconnect();
		return false;
	}
	
	/** 以流的形式读取远程文件 **/
	public static HttpBean obtainHttpBean(String path,
			HashMap<String, Object> params, String reqMethod) throws Exception {
		HttpBean hb = new HttpBean();
		StringBuilder sb = new StringBuilder();
		sb.append(path);//
		if (null != params)
			sb.append("?") //
			.append(encodeUrl(params));
		HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString())
		.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod(reqMethod);// POST
		/** 允许对外输出 **/
		//conn.setDoOutput(true);
		/** 假如服务器对本次请求响应成功 **/
		if (200 == conn.getResponseCode()) {
			hb.code = 200;
			hb.contentLength = conn.getContentLength();
			hb.inStream = conn.getInputStream();
			//return conn.getInputStream();
			return hb;
		}
		conn.disconnect();
		return null;
	}

	/** 将远程文件转换成字节流 **/
	public static String openUrlStr(String url, HashMap<String, Object> params,
			String reqMethod) throws Exception {
		InputStream inStream = openUrl(url, params, reqMethod);
		byte[] bytes = null;
		if (null != inStream) {
			bytes = StreamTool.read(inStream);
		}
		String data = null;
		if (null != bytes) {
			data = new String(bytes, "UTF-8");
		}
		return data;
	}

	public static String encodeUrl(HashMap<String, Object> parameters) {
		if (parameters == null)
			return "";
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet()) {
			if (first)
				first = false;
			else
				sb.append("&");
			sb.append(key + "=")//
					.append(URLEncoder.encode(parameters.get(key).toString()));
		}
		return sb.toString();
	}
}
