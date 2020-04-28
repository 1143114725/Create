package cn.dapchina.newsupper.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Xml;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.util.StreamTool;
import cn.dapchina.newsupper.util.Util;

public class WebFeedXML {
	ArrayList<Answer> hList = null;
	HashMap<Integer, Question> qMap = null;

	public WebFeedXML() {
		hList = new ArrayList<Answer>();
		qMap = new HashMap<Integer, Question>();
	}

	/**
	 * 命名规则中断 sid pid
	 * @param str
	 * @param hMap
	 * @param path
	 * @param filename
	 * @param records
	 * @return
	 */
	public boolean writeFeedXML(String str, HashMap<String, String> hMap, String path, String filename, ArrayList<UploadFeed> records,String sid,String pid) {
		return writeFeedXML(str, hMap, records, path, filename,sid,pid);
	}

	/**
	 * 命名规则中断 sid pid
	 * @param str
	 * @param hMap
	 * @param records
	 * @param path
	 * @param filename
	 * @return
	 */
	public boolean writeFeedXML(String str, HashMap<String, String> hMap, ArrayList<UploadFeed> records, String path, String filename,String sid,String pid) {
		boolean f = false;
		if (str != null) {
			String params[] = str.split("&");
			for (int i = 0; i < params.length; i++) {
				String param[] = params[i].split("=");
				if (param[0].indexOf("HID") == 0) {
					continue;
				}
				String name = URLDecoder.decode(param[0]);
				String value = null;
				if (param.length > 1) {
					value = URLDecoder.decode(param[1]);
				}
				if (name.indexOf("VIS") == 0) {
					int id = Integer.parseInt(name.split("_")[2]);
					addQuestion(id, name, value);
				} else if (!hMap.containsKey(name)) {
					addHidden(name, value);
				}
			}
			if (hMap != null) {
				Set<Entry<String, String>> set = hMap.entrySet();
				Iterator<Entry<String, String>> itor = set.iterator();
				while (itor.hasNext()) {
					Entry<String, String> entry = itor.next();
					addHidden(entry.getKey(), entry.getValue());
				}
			}
			//命名规则 中断
			f = xmlToFile(path, filename, records,sid,pid);
		}
		return f;
	}

	private void serialAnswer(XmlSerializer serializer, Answer a) throws Exception {
		serializer.startTag("", "answer");
		if (a.type != null) {
			serializer.attribute("", "type", a.type);
		}
		serializer.startTag("", "name");
		serializer.text(a.name);
		serializer.endTag("", "name");
		serializer.startTag("", "value");
		if (a.value != null) {
			serializer.text(a.value);
		}
		serializer.endTag("", "value");
		serializer.endTag("", "answer");
	}

	/**
	 * 命名规则中断
	 * @param path
	 * @param filename
	 * @param records
	 * @param sid
	 * @param pid
	 * @return
	 */
	private boolean xmlToFile(String path, String filename, ArrayList<UploadFeed> records,String sid,String pid) {
		boolean f = false;
		try {
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			/**
			 * 答卷的开始节点
			 */
			serializer.startTag("", "response");

			serializer.startTag("", "hidden");
			for (int i = 0; i < hList.size(); i++) {
				Answer a = hList.get(i);
				serialAnswer(serializer, a);
			}
			serializer.endTag("", "hidden");

			if (!Util.isEmpty(records)) {
				serializer.startTag("", "files");
				for (UploadFeed re : records) {
					if (Cnt.FILE_TYPE_PNG == re.getType()) {
						serializer.startTag("", "photo");
					} else {
						serializer.startTag("", "record");
					}
					serializer.attribute("", "startDate", Util.getTime(re.getStartTime(), 0));
					serializer.attribute("", "regDate", Util.getTime(re.getRegTime(), 0));
					serializer.attribute("", "size", String.valueOf(re.getSize()));
					//命名规则开始中断
					String[] fileStr = filename.split("_");
					String date=fileStr[2];
					//不能解析 原来的第三位是日期 ，判断最早的访问专家
					if(Util.getLongTime(date, 5)==0){
						String newDate=fileStr[3];
						//判断ipsos的
						if(Util.getLongTime(newDate, 5)==0){
							serializer.text(sid+File.separator+pid+File.separator+re.getName());
						}else{
							serializer.text(re.getName());
						}
					}else{
						serializer.text(re.getName());
					}
					//命名规则结束
					if (Cnt.FILE_TYPE_PNG == re.getType()) {
						serializer.endTag("", "photo");
					} else {
						serializer.endTag("", "record");
					}
				}
				serializer.endTag("", "files");
			}

			Set<Entry<Integer, Question>> set = qMap.entrySet();
			Iterator<Entry<Integer, Question>> itor = set.iterator();
			while (itor.hasNext()) {
				Entry<Integer, Question> entry = itor.next();
				Question q = entry.getValue();
				serializer.startTag("", "question");
				serializer.attribute("", "index", "" + entry.getKey());
				for (int i = 0; i < q.aList.size(); i++) {
					Answer a = q.aList.get(i);
					serialAnswer(serializer, a);
				}
				serializer.endTag("", "question");
			}
			serializer.endTag("", "response");
			/**
			 * 答卷的结尾节点
			 */
			serializer.endDocument();
			f = writeToFile(writer.toString(), path, filename);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("xml拼凑错误!");
		}
		return f;
	}

	public boolean writeToFile(String data, String path, String filename) {
		boolean f = false;
		OutputStream os = null;
		try {
			os = new FileOutputStream(path + File.separator + filename);
			// Base64OutputStream outputStream =
			OutputStreamWriter osw = new OutputStreamWriter(new Base64OutputStream(os, Base64.DEFAULT));
			// Base64.encoder(data, os);
			// byte[] tmp = Base64.encode(data.getBytes("UTF-8"),
			// Base64.DEFAULT);
			// osw.write(new String(tmp, "UTF-8"));
			osw.write(data);
			osw.close();
			// outputStream.close();
			os.close();
			// System.out.println("xml写成功!");
			f = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("写文件失败!");
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				System.out.println("写文件文件流关闭失败!");
			}
		}
		return f;
	}

	public String readFeedData(String path, String filename) {
		InputStream is = null;
		try {
			is = new FileInputStream(path + File.separator + filename);
			// byte[] bytes = StreamTool.readStream(is);
			// String data = Base64.encodeToString(bytes, Base64.DEFAULT);
			byte[] bytes = Base64.decode(StreamTool.read(is), Base64.DEFAULT);
			String data = new String(bytes, "UTF-8");
			is.close();
			// System.out.println("base64解密后的字符--->"+data);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addHidden(String name, String value) {
		hList.add(new Answer(null, name, value));
	}

	private void addQuestion(int id, String name, String value) {
		Question q = qMap.get(id);
		if (q == null) {
			q = new Question(id);
			q.addAnswer(new Answer("item", name, value));
			qMap.put(id, q);
		} else
			q.addAnswer(new Answer("item", name, value));
	}

	private class Question {
		ArrayList<Answer> aList = null;

		public Question(int id) {
			aList = new ArrayList<Answer>();
		}

		public void addAnswer(Answer a) {
			aList.add(a);
		}
	}

	private class Answer {
		String type;
		String name;
		String value;

		public Answer(String t, String n, String v) {
			type = t;
			name = n;
			value = v;
		}
	}

	/*************************************************************************************************/

	/**
	 * 命名规则 sid,pid write
	 * @param str
	 * @param hMap
	 * @param records
	 * @param path
	 * @param filename
	 * @param dateList
	 * @param pointList
	 * @param pleaceList
	 * @return
	 */
	public boolean writeFeedXML(String str, HashMap<String, String> hMap, ArrayList<UploadFeed> records, String path, String filename, String dateList, String pointList, String pleaceList,String sid,String pid) {
		boolean f = false;
		if (str != null) {
			String params[] = str.split("&");
			for (int i = 0; i < params.length; i++) {
				String param[] = params[i].split("=");
				if (param[0].indexOf("HID") == 0) {
					continue;
				}
				String name = URLDecoder.decode(param[0]);
				String value = null;
				if (param.length > 1) {
					value = URLDecoder.decode(param[1]);
				}
				if (name.indexOf("VIS") == 0) {
					int id = Integer.parseInt(name.split("_")[2]);
					addQuestion(id, name, value);
				} else if (!hMap.containsKey(name)) {
					addHidden(name, value);
				}
			}
			if (hMap != null) {
				Set<Entry<String, String>> set = hMap.entrySet();
				Iterator<Entry<String, String>> itor = set.iterator();
				while (itor.hasNext()) {
					Entry<String, String> entry = itor.next();
					addHidden(entry.getKey(), entry.getValue());
				}
			}
			//命名规则write
			f = xmlToFile(path, filename, records, dateList, pointList, pleaceList,sid,pid);
		}
		return f;
	}

	/**
	 * 命名规则write
	 * @param path
	 * @param filename
	 * @param records
	 * @param dateList
	 * @param pointList
	 * @param pleaceList
	 * @return
	 */
	private boolean xmlToFile(String path, String filename, ArrayList<UploadFeed> records, String dateList, String pointList, String pleaceList,String sid,String pid) {
		boolean f = false;
		try {
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			/**
			 * 答卷的开始节点
			 */
			serializer.startTag("", "response");

			serializer.startTag("", "hidden");
			for (int i = 0; i < hList.size(); i++) {
				Answer a = hList.get(i);
				serialAnswer(serializer, a);
			}
			serializer.endTag("", "hidden");

			if (records != null && records.size() > 0) {
				serializer.startTag("", "files");
				for (UploadFeed re : records) {
					if (Cnt.FILE_TYPE_PNG == re.getType()) {
						serializer.startTag("", "photo");
					} else {
						serializer.startTag("", "record");
					}
					if (!Util.isEmpty(re.getQuestionId())) {
						serializer.attribute("", "questionID", re.getQuestionId());
					} else {
						serializer.attribute("", "questionID", "");
					}
					serializer.attribute("", "startDate", Util.getTime(re.getStartTime(), 0));
					serializer.attribute("", "regDate", Util.getTime(re.getRegTime(), 0));
					serializer.attribute("", "size", String.valueOf(re.getSize()));
					//命名规则开始write
					String[] fileStr = filename.split("_");
					String date=fileStr[2];
					//不能解析 原来的第三位是日期 ，判断最早的访问专家
					if(Util.getLongTime(date, 5)==0){
						String newDate=fileStr[3];
						//判断ipsos的
						if(Util.getLongTime(newDate, 5)==0){
							serializer.text(sid+File.separator+pid+File.separator+re.getName());
						}else{
							serializer.text(re.getName());
						}
					}else{
						serializer.text(re.getName());
					}
					//命名规则结束
					
					if (Cnt.FILE_TYPE_PNG == re.getType()) {
						serializer.endTag("", "photo");
					} else {
						serializer.endTag("", "record");
					}
				}

				serializer.endTag("", "files");
			}

			Set<Entry<Integer, Question>> set = qMap.entrySet();
			Iterator<Entry<Integer, Question>> itor = set.iterator();
			while (itor.hasNext()) {
				Entry<Integer, Question> entry = itor.next();
				Question q = entry.getValue();
				serializer.startTag("", "question");
				serializer.attribute("", "index", "" + entry.getKey());
				for (int i = 0; i < q.aList.size(); i++) {
					Answer a = q.aList.get(i);
					serialAnswer(serializer, a);
				}
				serializer.endTag("", "question");
			}

			/**
			 * 答卷的结尾节点
			 */

			/**
			 * 时间列表
			 */
			if (!Util.isEmpty(dateList)) {
				/**
				 * 将最后一个分号去掉
				 */
				String list = dateList.substring(0, dateList.length() - 1);
				if (0 < list.length()) {
					/**
					 * 获取每一次答卷的开始时间和结束时间
					 */
					String[] dateTime = list.split(";");
					serializer.startTag("", "times");
					for (String day : dateTime) {
						String[] datetime = day.split(",");
						serializer.startTag("", "datetime");

						if (Util.isEmpty(datetime)) {
							serializer.attribute("", "startDate", "");
							serializer.attribute("", "regtDate", "");
						} else if (2 == datetime.length) {
							serializer.attribute("", "startDate", datetime[0]);
							serializer.attribute("", "regtDate", datetime[1]);
						} else {
							serializer.attribute("", "startDate", "");
							serializer.attribute("", "regtDate", "");
						}

						serializer.text("");
						serializer.endTag("", "datetime");
					}
					serializer.endTag("", "times");
				}
			}

			/**
			 * 经纬度列表
			 */
			if (!Util.isEmpty(pointList)) {
				/**
				 * 将最后一个分号去掉
				 */
				String list = pointList.substring(0, pointList.length() - 1);
				if (0 < list.length()) {
					/**
					 * 获取每一次答卷的开始时间和结束时间
					 */
					String[] points = list.split(";");
					serializer.startTag("", "points");
					for (String point : points) {
						String[] p = point.split(",");
						serializer.startTag("", "point");
						// if(!Util.isEmpty(p) && Util.isEmpty(p[0])){
						// serializer.attribute("", "lat", "");
						// }else{
						// serializer.attribute("", "lat", p[0]);
						// }
						// if((null!=p) && (2==p.length) && Util.isEmpty(p[1])){
						// serializer.attribute("", "lng", "");
						// }else{
						// serializer.attribute("", "lng", p[1]);
						// }
						if (Util.isEmpty(p)) {
							serializer.attribute("", "lat", "");
							serializer.attribute("", "lng", "");
						} else if (2 == p.length) {
							serializer.attribute("", "lat", p[0]);
							serializer.attribute("", "lng", p[1]);
						} else {
							serializer.attribute("", "lat", "");
							serializer.attribute("", "lng", "");
						}
						serializer.text("");
						serializer.endTag("", "point");
					}
					serializer.endTag("", "points");
				}
			}
			/**
			 * 地点列表
			 */
			if (!Util.isEmpty(pleaceList)) {
				/**
				 * 获取每一次答卷的开始时间和结束时间
				 */
				String[] places = pleaceList.split(",");
				serializer.startTag("", "places");
				for (String place : places) {
					serializer.startTag("", "place");
					serializer.text(place);
					serializer.endTag("", "place");
				}
				serializer.endTag("", "places");
			}
			serializer.endTag("", "response");
			serializer.endDocument();
			f = writeToFile(writer.toString(), path, filename);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("xml拼凑错误!");
		}
		return f;
	}

	/*************************************************************************************************/

	public boolean isXmlValidte(String file) {
		InputStream fis;
		try {
			fis = new FileInputStream(file);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream base = new Base64InputStream(fis, Base64.DEFAULT);
			Document document = builder.parse(base);
			Element element = document.getDocumentElement();
			NodeList noteList = element.getElementsByTagName("question");
//			for (int i = 0; i < noteList.getLength(); i++) {
//				Node valueNode = noteList.item(i).getLastChild().getLastChild().getLastChild();
//				if (null != valueNode) {
//					if (null != valueNode.getNodeName()) {
//						return true;
//					}
//				}
//			}
			System.out.println("长度:"+noteList.getLength());
			if(noteList.getLength()>0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("isXmlValidte异常");
			return false;
		}
		return false;
	}
}
