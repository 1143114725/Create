package cn.dapchina.newsupper.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.util.Xml;
import cn.dapchina.newsupper.bean.MyRecoder;
//记录丢失文件表
public class RecodeXml {

	public static boolean writeXML(ArrayList<MyRecoder> recodes){
		boolean flag = false;
		String str="";
		try {
			str = writeToStr(recodes);
		}catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		flag = writeToXml(str);
		return flag;
	}

	// android XmlSerializer写xml示例代码
	/****************************************************/
	public static String writeToStr(ArrayList<MyRecoder> recodes) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "Info");

		for (MyRecoder recode : recodes) {
			serializer.startTag(null, "files");
			serializer.startTag(null, "SurveyID");
			serializer.text(recode.getSurveyId());
			serializer.endTag(null, "SurveyID");
			serializer.startTag(null, "FeedID");
			serializer.text(recode.getFeedId());
			serializer.endTag(null, "FeedID");
			serializer.startTag(null, "FilesName");
			serializer.text(recode.getName());
			serializer.endTag(null, "FilesName");
			serializer.endTag(null, "files");
		}

		serializer.endTag(null, "Info");
		serializer.endDocument();
		writer.flush();
		writer.close();
		return writer.toString();
	}

	/****************************************************/

	/**
	 * 将xml字符串写入xml文件
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public static boolean writeToXml(String str) {
		try {
			OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "accessPanelXML.xml");
			OutputStreamWriter outw = new OutputStreamWriter(out);
			try {
				outw.write(str);
				outw.close();
				out.close();
				return true;
			} catch (IOException e) {
				return false;
			}
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
}
