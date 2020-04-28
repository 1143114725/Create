package cn.dapchina.newsupper.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class OutFile {
	public static void outToFile(InputStream is,String fileName) {
		try {
			OutputStream outputStream = new FileOutputStream(
					Environment.getExternalStorageDirectory() + File.separator + fileName+".xml");
			int bytesWritten = 0;
			int byteCount = 0;

			byte[] bytes = new byte[1024];

			while ((byteCount = is.read(bytes)) != -1) {
				outputStream.write(bytes, bytesWritten, byteCount);
				bytesWritten += byteCount;
			}
			outputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
