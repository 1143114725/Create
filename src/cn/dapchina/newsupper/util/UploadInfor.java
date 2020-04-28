package cn.dapchina.newsupper.util;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import android.os.AsyncTask;
import cn.dapchina.newsupper.global.Cnt;


public class UploadInfor {

	public UploadInfor(){
		
	}
	
	public UploadInfor(String path,String name, String time){
		new UploadLogTask(path, name, time).execute();
	}
	
	
	private final class UploadLogTask extends AsyncTask<Void, Void, Void>{

		private String p;
		private String n;
		private String u;
		public UploadLogTask(String path,String name, String time){
			this.p = path;
			this.n = name;
			this.u = time;
		}
		@Override
		protected Void doInBackground(Void... params) {
			String id = uploadRecordAndPhoto(p, n, u);
				if(null != id & 0 != id.trim().length()){
					File file = new File(p, n);
//					if(file.exists()){
//						file.delete();
//					}
				}
			return null;
		}
		
		
	}
	
	
	/**上传录音和图片**/
	public String uploadRecordAndPhoto(String path, String uploadFile, String time) {
		try {
			System.out.println("************start**************");
			File f = new File(path + File.separator + uploadFile);
			// 构造拼接协议
			String head = "Content-Length=" + f.length() + ";filename=" + uploadFile + ";sourceid=" + (time == null ? "" : time) + "\n";
			// 通过Socket取得输出流
			//TODO
			System.out.println("到了");
			Socket socket = new Socket(Cnt.RECORD_PHOTO_URL, Cnt.PORT);
			//			Socket socket = new Socket("192.168.1.7", 7878);
			OutputStream outStream = socket.getOutputStream();
			outStream.write(head.getBytes());

			PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
			// 获取到字符流的id与位置
			String response = StreamTool.readLine(inStream);
//			Log.e("MM", "response====" + response);
			String[] items = response.split(";");
			String responseid = items[0].substring(items[0].indexOf("=") + 1);
			String position = items[1].substring(items[1].indexOf("=") + 1);
			RandomAccessFile fileOutStream = new RandomAccessFile(f, "r");
			fileOutStream.seek(Integer.valueOf(position));
			byte[] buffer = new byte[1024];
			int len = -1;
			System.out.println("完毕");
			int length = Integer.valueOf(position);
			while ((len = fileOutStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
				length += len;
				System.out.println("已经上传:"+length/1024+"k");
			}
			fileOutStream.close();
			outStream.close();
			inStream.close();
			socket.close();
			return responseid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
