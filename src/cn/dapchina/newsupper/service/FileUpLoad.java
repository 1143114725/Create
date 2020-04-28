package cn.dapchina.newsupper.service;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import cn.dapchina.newsupper.bean.Call;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.OutFile;
import cn.dapchina.newsupper.util.StreamTool;
import cn.dapchina.newsupper.util.Util;

public class FileUpLoad{
	MyApp ma;
	public FileUpLoad(){
		
	}

	public FileUpLoad(MyApp myApp){
		ma = myApp;
	}
	public InputStream upLoadXml(String urlstr, byte[] data, HashMap<String, String> paramsMap) {
		try {
			// params
			String params = null;
			Set<Entry<String, String>> set = paramsMap.entrySet();
			Iterator<Entry<String, String>> itor = set.iterator();
			while (itor.hasNext()) {
				Entry<String, String> entry = itor.next();
				if (params == null)
					params = entry.getKey() + "=" + entry.getValue();
				else
					params += "&" + entry.getKey() + "=" + entry.getValue();
			}
			// params
			URL url = new URL(urlstr + "?" + params);
			if(Cnt.D)
			Util.Log(url.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");

			String BOUNDARY = java.util.UUID.randomUUID().toString();
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			// file
			dos.writeBytes("--" + BOUNDARY + "\r\n");
			dos.writeBytes("Content-Disposition: form-data; name=\"filename\"; filename=\"" + "author.xml" + "\"\r\n");
			dos.writeBytes("Content-Type: application/octet-stream\r\n");
			dos.writeBytes("\r\n");
			dos.write(data);
			dos.writeBytes("\r\n");
			dos.writeBytes("--" + BOUNDARY + "\r\n");
			// file
			dos.close();

			// InputStream is=conn.getInputStream();
			int code = conn.getResponseCode();
			Log.e("MM", "Code:" + code);
			if (code != 200){
				return null;
			}
			
			//InputStream inStream = conn.getInputStream();
			//Log.i("a", "ios----"+inStream.toString());
			
			return conn.getInputStream();
			
//			InputStream inStream = conn.getInputStream();
//			
//			if (null != inStream) {
//				byte [] bytes = StreamTool.read(inStream);
//				return new String(bytes, "UTF-8");
//			}
//			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public InputStream upLoad(String urlstr, String path, String filename, HashMap<String, String> paramsMap) {
		Log.w("junyao", "上传文件upLoad----->"+filename);
		try {
			// params
			String params = null;
			Set<Entry<String, String>> set = paramsMap.entrySet();
			Iterator<Entry<String, String>> itor = set.iterator();
			while (itor.hasNext()) {
				Entry<String, String> entry = itor.next();
				if (params == null)
					params = entry.getKey() + "=" + entry.getValue();
				else
					params += "&" + entry.getKey() + "=" + entry.getValue();
			}
			// params
			//Log.i("junyao", "upLoadUrl---"+urlstr + "?" + params);
//			URL url = new URL(DapData.config.UploadURL + "?" + params);
			// check url
			URL url = new URL(urlstr + "?" + params);
			if(Cnt.D)
			Util.Log(url.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			Log.e("DapDesk", "HttpURLConnection");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			Log.e("DapDesk", "DataOutputStream");
			
			// file
			dos.writeBytes("--" + BOUNDARY + "\r\n");
			dos.writeBytes("Content-Disposition: form-data; name=\"filename\"; filename=\"" + filename + "\"\r\n");
			dos.writeBytes("Content-Type: application/octet-stream\r\n");
			dos.writeBytes("\r\n");
			dos.write(getBytes(path, filename));
			dos.writeBytes("\r\n");
			dos.writeBytes("--" + BOUNDARY + "\r\n");
			StringBufferInputStream inputStream=new StringBufferInputStream(dos.toString());
			OutFile.outToFile(inputStream, "dos");
			// file
			Log.e("DapDesk", "InputStream");
			
			dos.close();
			
			// InputStream is=conn.getInputStream();
			int code = conn.getResponseCode();
			Log.e("DapDesk", "Code:" + code);
			
			if (code != 200){
				return null;
			}
			
			//InputStream inStream = conn.getInputStream();
			//Log.i("a", "io----"+inStream.toString());
			return conn.getInputStream();
		} catch (Exception e) {
			Log.e("DapDesk", "Error:" + filename + ", " + urlstr + ", " + e.getMessage());
			return null;
		}
	}
	
	public InputStream upLoadBase64(String urlstr, String path, String filename, HashMap<String, String> paramsMap) {
		Log.w("junyao", "上传文件upLoad----->"+filename);
		try {
			// params
			String params = null;
			Set<Entry<String, String>> set = paramsMap.entrySet();
			Iterator<Entry<String, String>> itor = set.iterator();
			while (itor.hasNext()) {
				Entry<String, String> entry = itor.next();
				if (params == null)
					params = entry.getKey() + "=" + entry.getValue();
				else
					params += "&" + entry.getKey() + "=" + entry.getValue();
			}
			// params
			//URL url = new URL(DapData.config.UploadURL + "?" + params);
			//check url
			URL url = new URL(urlstr + "?" + params);
			if(Cnt.D)
			Util.Log(url.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			Log.e("DapDesk", "HttpURLConnection");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			Log.e("DapDesk", "DataOutputStream");
			
			// file
			dos.writeBytes("--" + BOUNDARY + "\r\n");
			dos.writeBytes("Content-Disposition: form-data; name=\"filename\"; filename=\"" + filename + "\"\r\n");
			dos.writeBytes("Content-Type: application/octet-stream\r\n");
			dos.writeBytes("\r\n");
			dos.write(get64Bytes(path, filename));
			dos.writeBytes("\r\n");
			dos.writeBytes("--" + BOUNDARY + "\r\n");
			// file
			Log.e("DapDesk", "InputStream");
			dos.close();
			String strInput="--" + BOUNDARY + "\r\n"+"Content-Disposition: form-data; name=\"filename\"; filename=\"" + filename + "\"\r\n"+"Content-Type: application/octet-stream\r\n"+"\r\n";
			byte[] buffer = new byte[1024];
			buffer= strInput.getBytes();
			FileOutputStream fos=new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator +"dos.xml");
			fos.write(buffer);
			fos.write(get64Bytes(path, filename));
			strInput="\r\n"+"--" + BOUNDARY + "\r\n";
			buffer= strInput.getBytes();
			fos.write(buffer);
			
			// InputStream is=conn.getInputStream();
			int code = conn.getResponseCode();
			Log.e("DapDesk", "Code:" + code);
			if (code != 200){
				return null;
			}
			
			//InputStream inStream = conn.getInputStream();
			//Log.i("a", "io----"+inStream.toString());
			return conn.getInputStream();
		} catch (Exception e) {
			Log.e("DapDesk", "Error:" + filename + ", " + urlstr + ", " + e.getMessage());
			return null;
		}
	}
	
	public InputStream upLoadPidList(String urlstr, String path, String filename, HashMap<String, Object> paramsMap) {
		Log.w("junyao", "上传文件upLoad----->"+filename);
		try {
			// params
			String params = null;
			Set<Entry<String, Object>> set = paramsMap.entrySet();
			Iterator<Entry<String, Object>> itor = set.iterator();
			while (itor.hasNext()) {
				Entry<String, Object> entry = itor.next();
				if (params == null)
					params = entry.getKey() + "=" + entry.getValue().toString();
				else
					params += "&" + entry.getKey() + "=" + entry.getValue().toString();
			}
			// params
			//Log.i("kjy", "000000000000---"+urlstr + "?" + params);
//			URL url = new URL(DapData.config.UploadURL + "?" + params);
			//check url
			URL url = new URL(urlstr + "?" + params);
			if(Cnt.D)
			Util.Log(url.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			Log.e("DapDesk", "HttpURLConnection");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			Log.e("DapDesk", "DataOutputStream");
			
			// file
			dos.writeBytes("--" + BOUNDARY + "\r\n");
			dos.writeBytes("Content-Disposition: form-data; name=\"filename\"; filename=\"" + filename + "\"\r\n");
			dos.writeBytes("Content-Type: application/octet-stream\r\n");
			dos.writeBytes("\r\n");
			dos.write(get64Bytes(path, filename));
			dos.writeBytes("\r\n");
			dos.writeBytes("--" + BOUNDARY + "\r\n");
			// file
			Log.e("DapDesk", "InputStream");
			dos.close();
			
			// InputStream is=conn.getInputStream();
			int code = conn.getResponseCode();
			Log.e("DapDesk", "Code:" + code);
			
			if (code != 200){
				return null;
			}
			
			//InputStream inStream = conn.getInputStream();
			//Log.i("a", "io----"+inStream.toString());
			return conn.getInputStream();
		} catch (Exception e) {
			Log.e("DapDesk", "Error:" + filename + ", " + urlstr + ", " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 解析xml是否上报成功
	 * @param is
	 * @return
	 */
	public static boolean resolvData(InputStream is) {
		boolean success = false;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String state = "0";
		try {

			String xml = new String(StreamTool.read(is), "utf-8");
//			Log.e("MM", "xml==" + xml);

			StringReader sr = new StringReader(xml);
			InputSource iis = new InputSource(sr);

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(iis);
			Element element = document.getDocumentElement();
			state = element.getElementsByTagName("S").item(0).getFirstChild().getNodeValue();
			if (state.equals("100")) {
				success = true;
			}else if("95".equals(state)){
				success = true;
			}
			sr.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	//base 64
	private byte[] getBytes(String path, String filename) throws Exception {
		InputStream ins = new FileInputStream(new File(path + File.separator + filename));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = ins.read(b)) != -1) {
			out.write(b, 0, n);
		}
		ins.close();
		return out.toByteArray();
	}
	
	private byte[] get64Bytes(String path,String filename) throws Exception{
		InputStream ins=new FileInputStream(new File(path+File.separator+filename));
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		byte[] b=new byte[1024];
		int n;
		while((n=ins.read(b))!=-1){
			out.write(b,0,n);
		}
		byte[] bs = Base64.decode(out.toByteArray(), Base64.DEFAULT);
		out.flush();
		out.close();
		ins.close();
		return bs;
	}
	
	public void uploadModify(String path, String name, HashMap<String, String> hMap){
		new UploadModify(path, name, hMap).execute();
	}
	
	private final class UploadModify extends AsyncTask<Void, Void, Boolean>{
		private String path;
		private String name;
		private HashMap<String, String> map;
		
		public UploadModify(String path, String name, HashMap<String, String> hMap){
			this.path = path;
			this.name = name;
			this.map = hMap;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			InputStream iS = upLoad(Cnt.UPLOAD_URL, this.path, this.name, this.map);
			Boolean is = resolvData(iS);
			return is;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				System.out.println("受访者信息上传成功--->"+this.name);
				//ma.provider.delModify(this.name);
			}
			super.onPostExecute(result);
		}
	}
	
	/**上传意见反馈**/
	public String uploadFeedBack(String name, long time, String msg) {
		try {
			System.out.println("************start**************");
			// 构造拼接协议
			String head = "Content-Length=" + msg.length() + ";filename=" + name + ";sourceid=" + (time == 0 ? "" : time) + "\n";
			// 通过Socket取得输出流
			//TODO
			Socket socket = new Socket(Cnt.RECORD_PHOTO_URL, Cnt.PORT);
			socket.setSoTimeout(5000);
			OutputStream outStream = socket.getOutputStream();
			outStream.write(head.getBytes());

			PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
			// 获取到字符流的id与位置
			String response = StreamTool.readLine(inStream);
//			Log.e("MM", "response====" + response);
			String[] items = response.split(";");
			String responseid = items[0].substring(items[0].indexOf("=") + 1);
			outStream.write(msg.getBytes());
			outStream.close();
			inStream.close();
			socket.close();
			return responseid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void uploadFile(File f, Call call, String dir) {// file.getFilePath(),file.getFileName(),
		if(null==f || !f.exists()){
			return;
		}
		// file.get_id()
		try {// final String path, final String uploadFile, int _id
			Log.e("MM", "************start**************");
			// 构造拼接协议
			String head = "Content-Length=" + f.length() + ";filename=" + f.getName() + ";sourceid="+System.nanoTime()+";dir="+dir+"\r\n";
			// 通过Socket取得输出流
			Socket socket = new Socket(Cnt.CLIENT_SERVER_IP, Cnt.PORT);
			OutputStream outStream = socket.getOutputStream();
			outStream.write(head.getBytes());
			PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
			// 获取到字符流的id与位置
			String response = StreamTool.readLine(inStream);
			Log.e("kjy", "response====" + response);
			//String[] items = response.split(";");
			//String responseid = items[0].substring(items[0].indexOf("=") + 1);
			//String position = items[1].substring(items[1].indexOf("=") + 1);
			RandomAccessFile fileOutStream = new RandomAccessFile(f, "r");
			//fileOutStream.seek(0);
			byte[] buffer = new byte[1024];
			int len = -1;
			int length = 0;
			boolean isCall = (null != call);
			int total = (int) f.length();
			while ((len = fileOutStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
				length += len;
				if(isCall){
					call.updateProgress(length, total);
				}
			}
			fileOutStream.close();
			outStream.close();
			inStream.close();
			socket.close();
		} catch (Exception e) {

		}
	}
}
