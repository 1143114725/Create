package cn.dapchina.newsupper.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.util.Xml;
import cn.dapchina.newsupper.bean.Application;
import cn.dapchina.newsupper.bean.Knowledge;
import cn.dapchina.newsupper.bean.Logo;
import cn.dapchina.newsupper.bean.Notice;
import cn.dapchina.newsupper.bean.Quota;
import cn.dapchina.newsupper.bean.Restriction;
import cn.dapchina.newsupper.bean.RestrictionValue;
import cn.dapchina.newsupper.bean.ReturnType;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.bean.option;
import cn.dapchina.newsupper.util.OutFile;
import cn.dapchina.newsupper.util.Util;


/**
 * 负责Xml解析的
 */
public class XmlService {
	
	//  大树   重置   1   
	public List<UploadFeed> getAll(InputStream inStream) throws Exception {
		List<UploadFeed> feeds = new ArrayList<UploadFeed>();
		UploadFeed feed = null;
		XmlPullParser parser = Xml.newPullParser();
		/**为Pull解析器设置要解析的XML数据**/
		parser.setInput(inStream, "UTF-8");
		int event = parser.getEventType();
		while(event != XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
				
			case XmlPullParser.START_TAG:
				if("feed".equals(parser.getName())){
					String feedId = parser.getAttributeValue(0);
					feed = new UploadFeed();
					feed.setFeedId(feedId);
				}
				break;
				
			case XmlPullParser.END_TAG:
				if("feed".equals(parser.getName())){
					feeds.add(feed);
					feed = null;
				}
				break;
			}
			event = parser.next();
		}
		return feeds;
	}
	//  以上  
	public ArrayList<Notice> getNotice(InputStream inStream){
		ArrayList<Notice> notices = new ArrayList<Notice>();
		if(null == inStream){
			return notices;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList noticeList = element.getElementsByTagName("notice");
			if(null != noticeList){
				for (int i = 0; i < noticeList.getLength(); i++) {
					Node noticeNode = noticeList.item(i);
					if(null != noticeNode){
						NamedNodeMap noticeMap = noticeNode.getAttributes();
						if(null != noticeMap){
							Notice notice = new Notice();
							Node titleNode = noticeMap.getNamedItem("title");
							if(null != titleNode){
								/**
								 * 名称
								 */
								notice.setTitle(titleNode.getNodeValue().trim());
							}
							
							Node contentNode = noticeMap.getNamedItem("content");
							if(null != contentNode){
								/**
								 * 内容
								 */
								notice.setContent(contentNode.getNodeValue().trim());
							}
							
							Node timeNode = noticeMap.getNamedItem("time");
							if(null != timeNode){
								/**
								 * 时间
								 */
								notice.setTime(timeNode.getNodeValue().trim());
							}
							
							Node idNode = noticeMap.getNamedItem("id");
							if(null != idNode){
								/**
								 * 时间
								 */
								notice.setId(idNode.getNodeValue().trim());
							}
							notices.add(notice);
						}
					}
				}
			}else{
				return notices;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return notices;
		}
		return notices;
	}
	
	
	//解析知识库
	public ArrayList<Knowledge> getAllKnow(InputStream inStream){
		ArrayList<Knowledge> klist = new ArrayList<Knowledge>();
		if(null == inStream){
			return klist;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList knowledgeList = element.getElementsByTagName("knowledge");
			if(null != knowledgeList){
				for (int i = 0; i < knowledgeList.getLength(); i++) {
					Node knowNode = knowledgeList.item(i);
					if(null != knowNode){
						NamedNodeMap knowMap = knowNode.getAttributes();
						if(null != knowMap){
							Knowledge knowledge=new Knowledge();
							Node titleNode = knowMap.getNamedItem("knowledgeTitle");
							if(null != titleNode){
								/**
								 * 知识库名称
								 */
								knowledge.setTitle(titleNode.getNodeValue().trim());
							}
							Node kidNode = knowMap.getNamedItem("knowledgeId");
							if(null != kidNode){
								/**
								 * 知识库id
								 */
								knowledge.setId(kidNode.getNodeValue().trim());
							}
							
							Node pathNode = knowMap.getNamedItem("knowledgeSurvey");
							if(null != pathNode){
								//知识库分类
								knowledge.setKind( pathNode.getNodeValue().trim());
							}
							Node knowledgeContentNode = knowMap.getNamedItem("knowledgeContent");
							if(null != knowledgeContentNode){
								knowledge.setContent(knowledgeContentNode.getNodeValue().trim());
							}
							Node knowledgePathNode = knowMap.getNamedItem("knowledgePath");
							if(null != knowledgePathNode){
								String paths=knowledgePathNode.getNodeValue().trim();
								knowledge.setAttach(paths);
								if(Util.isEmpty(paths)){
									//下载路径为空,那名字也就是空
									knowledge.setFileName("");
								}else{
									String[] pathSplit = paths.split(";");
									String names="";
									for(String path:pathSplit){
										String name = path.substring(path.lastIndexOf("/")+1, path.length());
										names+=name+";";
									}
									knowledge.setFileName(names);
								}
								
							}
							knowledge.setEnable("0");
							klist.add(knowledge);
						}
					}
				}
			}else{
				return klist;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return klist;
		}
		return klist;
	}
	

	public ArrayList<Survey> getAllSurvey(InputStream inStream){
		ArrayList<Survey> surveys = new ArrayList<Survey>();
		if(null == inStream){
			return surveys;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList surveyList = element.getElementsByTagName("project");
			if(null != surveyList){
				for (int i = 0; i < surveyList.getLength(); i++) {
					Node proNode = surveyList.item(i);
					if(null != proNode){
						NamedNodeMap proMap = proNode.getAttributes();
						Survey survey = new Survey();
						if(null != proMap){
//							Survey survey = new Survey();
							Node titleNode = proMap.getNamedItem("title");
							if(null != titleNode){
								/**
								 * 问卷名称
								 */
								survey.surveyTitle = titleNode.getNodeValue().trim();
							}
							Node pwdNode = proMap.getNamedItem("AndroidNewPSEnableChar");
							if(null != pwdNode){
								/**
								 * 问卷名称
								 */
//								System.out.println("设置密码");
								survey.setPassword(pwdNode.getNodeValue().trim());
							}
							
							Node pathNode = proMap.getNamedItem("path");
							if(null != pathNode){
								survey.downloadUrl = pathNode.getNodeValue().trim();
							}
							Node uploadNode = proMap.getNamedItem("uploadfile");
							if(null != uploadNode){
								String upload = uploadNode.getNodeValue().trim();
								if("true".equals(upload)){
									survey.upload = 1;
								}else if("false".equals(upload)){
									survey.upload = 0;
								}
								//System.out.println("getAllProject--->upload="+upload);
							}
							Node uptimeNode = proMap.getNamedItem("uptime");
							if(null != uptimeNode){
								survey.publishTime = uptimeNode.getNodeValue().trim();
							}
							//问卷提醒解析
							Node generatedTimeNode = proMap.getNamedItem("generatedTime");
							if(null != generatedTimeNode){
								survey.setGeneratedTime(generatedTimeNode.getNodeValue().trim());
							}
							
							Node surveyIdNode = proMap.getNamedItem("SurveyID");
							if(null != surveyIdNode){
								survey.surveyId = surveyIdNode.getNodeValue().trim();
							}
							
							Node versionNode = proMap.getNamedItem("version");
							if(null != versionNode){
								String v = versionNode.getNodeValue().trim();
								if(!Util.isEmpty(v) && !Util.isNullStr(v)){
									survey.version = Float.parseFloat(v);
								}
							}
							
							Node camerNode = proMap.getNamedItem("cameraFlag");
							if(null != camerNode){
								String camer = camerNode.getNodeValue().trim();
								if(!Util.isEmpty(camer) && !Util.isNullStr(camer)){
									survey.isPhoto = Integer.parseInt(camer);
								}
							}
							
							Node recordNode = proMap.getNamedItem("recordFlag");
							if(null != recordNode){
								String record = recordNode.getNodeValue().trim();
								if(!Util.isEmpty(record) && !Util.isNullStr(record)){
									survey.isRecord = Integer.parseInt(record);
								}
							}
							//摄像解析节点
							Node vidoeNode = proMap.getNamedItem("videoFlag");
							if(null != vidoeNode){
								String video = vidoeNode.getNodeValue().trim();
								if(!Util.isEmpty(video) && !Util.isNullStr(video)){
									survey.isVideo= Integer.parseInt(video);
								}
							}
//							survey.isVideo=1;
							
							//新建限制
							Node oneVisitNode = proMap.getNamedItem("oneVisit");
							if(null != oneVisitNode){
								String oneVisit = oneVisitNode.getNodeValue().trim();
								if(!Util.isEmpty(oneVisit) && !Util.isNullStr(oneVisit)){
									survey.oneVisit= Integer.parseInt(oneVisit);
								}
							}
							
							Node androidNode = proMap.getNamedItem("AndroidFlag");
							if(null != androidNode){
								String android = androidNode.getNodeValue().trim();
								if(!Util.isEmpty(android) && !Util.isNullStr(android)){
									survey.visitMode = Integer.parseInt(android);
								}else{
									survey.visitMode = 0;
								}
							}else{
								survey.visitMode = 0;
							}
							
							Node openNode = proMap.getNamedItem("openStatus");
							if(null != openNode){
								String openStatus = openNode.getNodeValue().trim();
								if(!Util.isEmpty(openStatus) && !Util.isNullStr(openStatus)){
									survey.openStatus = Integer.parseInt(openStatus);
								}
							}
							
							/**
							 * 是否为全局录音
							 */
							Node globalNode = proMap.getNamedItem("RecordedFlag");
							if(null != globalNode){
								String global = globalNode.getNodeValue().trim();
								if(!Util.isEmpty(global) && !Util.isNullStr(global)){
									survey.globalRecord = Integer.parseInt(global);
								}
							}
//							surveys.add(survey);
						}
						//访问状态
						NodeList elementReturn = proNode.getChildNodes();
						if(null != elementReturn){
							for (int z = 0;z < elementReturn.getLength(); z++) {
								//return节点
								Node item = elementReturn.item(z);
								if(null!=item){
									NodeList elementSid = item.getChildNodes();
									if(null != elementSid){
										ArrayList<ReturnType> rList=new ArrayList<ReturnType>();
										for (int y = 0;y < elementSid.getLength(); y++) {
											Node itemSid = elementSid.item(y);
											NamedNodeMap attributeSid = itemSid.getAttributes();
											//属性
											if(null != attributeSid){
												ReturnType rt=new ReturnType();
												Node namedItemEnable = attributeSid.getNamedItem("enable");
												if(namedItemEnable!=null){
													if(0==Integer.parseInt(namedItemEnable.getNodeValue().trim())){
														continue;
													}else{
														rt.setEnable(Integer.parseInt(namedItemEnable.getNodeValue().trim()));
													}
												}
												Node namedItemSid = attributeSid.getNamedItem("sid");
												if(namedItemSid!=null){
//													System.out.println("sid:"+namedItemSid.getNodeValue().trim());
													if(1==Integer.parseInt(namedItemSid.getNodeValue().trim())||100==Integer.parseInt(namedItemSid.getNodeValue().trim())){
														continue;
													}
													rt.setReturnId(Integer.parseInt(namedItemSid.getNodeValue().trim()));
												}
												Node namedItemNote = attributeSid.getNamedItem("note");
												if(namedItemNote!=null){
													rt.setReturnName(namedItemNote.getNodeValue().trim());
												}
												if(Util.isEmpty(rt.getReturnName())){
													rt.setReturnName(itemSid.getTextContent().trim());
												}
												rList.add(rt);
											}
										}
										//存取成json
										survey.setRlist(rList);
									}
								}
							}
						}
						surveys.add(survey);
						//访问状态结束
					}
				}
			}else{
				return surveys;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return surveys;
		}
		return surveys;
	}
	
	public Survey getSurvey(InputStream inStream){
		Survey survey = new Survey();
		if(null == inStream){
			return null;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList surveyList = element.getElementsByTagName("project");
			if(null != surveyList){
				for (int i = 0; i < surveyList.getLength(); i++) {
					Node proNode = surveyList.item(i);
					if(null != proNode){
						NamedNodeMap proMap = proNode.getAttributes();
						if(null != proMap){
							survey = new Survey();
							Node titleNode = proMap.getNamedItem("title");
							if(null != titleNode){
								/**
								 * 问卷名称
								 */
								survey.surveyTitle = titleNode.getNodeValue().trim();
							}
							Node pwdNode = proMap.getNamedItem("AndroidNewPSEnableChar");
							if(null != pwdNode){
								/**
								 * 问卷名称
								 */
//								System.out.println("设置密码");
								survey.setPassword(pwdNode.getNodeValue().trim());
							}
							
							Node pathNode = proMap.getNamedItem("path");
							if(null != pathNode){
								survey.downloadUrl = pathNode.getNodeValue().trim();
							}
							Node uploadNode = proMap.getNamedItem("uploadfile");
							if(null != uploadNode){
								String upload = uploadNode.getNodeValue().trim();
								if("true".equals(upload)){
									survey.upload = 1;
								}else if("false".equals(upload)){
									survey.upload = 0;
								}
								//System.out.println("getAllProject--->upload="+upload);
							}
							Node uptimeNode = proMap.getNamedItem("uptime");
							if(null != uptimeNode){
								survey.publishTime = uptimeNode.getNodeValue().trim();
							}
							//问卷提醒解析
							Node generatedTimeNode = proMap.getNamedItem("generatedTime");
							if(null != generatedTimeNode){
								survey.setGeneratedTime(generatedTimeNode.getNodeValue().trim());
							}
							
							Node surveyIdNode = proMap.getNamedItem("SurveyID");
							if(null != surveyIdNode){
								survey.surveyId = surveyIdNode.getNodeValue().trim();
							}
							
							Node versionNode = proMap.getNamedItem("version");
							if(null != versionNode){
								String v = versionNode.getNodeValue().trim();
								if(!Util.isEmpty(v) && !Util.isNullStr(v)){
									survey.version = Float.parseFloat(v);
								}
							}
							
							Node camerNode = proMap.getNamedItem("cameraFlag");
							if(null != camerNode){
								String camer = camerNode.getNodeValue().trim();
								if(!Util.isEmpty(camer) && !Util.isNullStr(camer)){
									survey.isPhoto = Integer.parseInt(camer);
								}
							}
							
							Node recordNode = proMap.getNamedItem("recordFlag");
							if(null != recordNode){
								String record = recordNode.getNodeValue().trim();
								if(!Util.isEmpty(record) && !Util.isNullStr(record)){
									survey.isRecord = Integer.parseInt(record);
								}
							}
							//摄像解析节点
							Node vidoeNode = proMap.getNamedItem("videoFlag");
							if(null != vidoeNode){
								String video = vidoeNode.getNodeValue().trim();
								if(!Util.isEmpty(video) && !Util.isNullStr(video)){
									survey.isVideo= Integer.parseInt(video);
								}
							}
//							survey.isVideo=1;
							
							//新建限制
							Node oneVisitNode = proMap.getNamedItem("oneVisit");
							if(null != oneVisitNode){
								String oneVisit = oneVisitNode.getNodeValue().trim();
								if(!Util.isEmpty(oneVisit) && !Util.isNullStr(oneVisit)){
									survey.oneVisit= Integer.parseInt(oneVisit);
								}
							}
							
							Node androidNode = proMap.getNamedItem("AndroidFlag");
							if(null != androidNode){
								String android = androidNode.getNodeValue().trim();
								if(!Util.isEmpty(android) && !Util.isNullStr(android)){
									survey.visitMode = Integer.parseInt(android);
								}else{
									survey.visitMode = 0;
								}
							}else{
								survey.visitMode = 0;
							}
							
							Node openNode = proMap.getNamedItem("openStatus");
							if(null != openNode){
								String openStatus = openNode.getNodeValue().trim();
								if(!Util.isEmpty(openStatus) && !Util.isNullStr(openStatus)){
									survey.openStatus = Integer.parseInt(openStatus);
								}
							}
							
							/**
							 * 是否为全局录音
							 */
							Node globalNode = proMap.getNamedItem("RecordedFlag");
							if(null != globalNode){
								String global = globalNode.getNodeValue().trim();
								if(!Util.isEmpty(global) && !Util.isNullStr(global)){
									survey.globalRecord = Integer.parseInt(global);
								}
							}
							
						}
					}
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return survey;
	}
	
	public HashMap<String,String> parseLoginXml(InputStream is){
		HashMap<String,String> rMap=new HashMap<String,String>();
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder builder=factory.newDocumentBuilder();
		    Document document=builder.parse(is);
		    Element element=document.getDocumentElement();
		    NodeList ndL=element.getChildNodes();
		    for(int i=0;i<ndL.getLength();i++){
		    	Node nd=ndL.item(i);
		    	if(null != nd){
		    		if(nd.getNodeType()==Node.ELEMENT_NODE){
				    	if(null != nd.getFirstChild()){
				    		rMap.put(nd.getNodeName(), nd.getFirstChild().getNodeValue());
				    	}
			    	}
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return rMap;
	}
	
	/**问卷的解析**/
	public Application getApplication(InputStream inStream) throws Exception {
		Application app = null;
		XmlPullParser parser = Xml.newPullParser();
		/**为Pull解析器设置要解析的XML数据**/
		parser.setInput(inStream, "UTF-8");
		int event = parser.getEventType();
		while(event != XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				app = new Application();
				break;
				
			case XmlPullParser.START_TAG:
				if("application".equals(parser.getName())){
					/**此处要注意,服务器端提供的版本号必须为double格式字符串
					 * 否则将会出异常
					 * **/
					String v = parser.getAttributeValue(0);//parser.getAttributeValue(1);
					Log.i("a", "v"+v);
					app.setVersion(Double.parseDouble(v));
				}
				if("path".equals(parser.getName())){
					String path = parser.nextText();
					app.setPath(path);
				}
				
				if("content".equals(parser.getName())){
					String content = parser.nextText();
					app.setContent(content);
				}
				
				if("notice".equals(parser.getName())){
					String notice = parser.nextText();
					app.setNotice(notice);
				}
				break;
				
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		return app;
	}
	
	/**
	 * 注册解析获取是否注册成功
	 * 
	 * <?xml version="1.0" encoding="UTF-8" ?> 
- <returnanswer>
  <state>98</state> 
  </returnanswer>
	 */
	public int getRegistResponse(InputStream inStream,String charset) throws Exception{
		int state=0;
		XmlPullParser parser = XmlPullParserFactory.newInstance()
				.newPullParser();
		parser.setInput(inStream, charset);
		int eventType = parser.getEventType();
		while (XmlPullParser.END_DOCUMENT != eventType) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				String nodeName = parser.getName();
				if ("state".equals(nodeName)) {
					state=Integer.parseInt(parser.nextText());
				} 
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		
		return state;
	}
	
	/**
	 *  自定义logo功能  解析
	 */
	public Logo getLogo(InputStream inStream) throws Exception {
		Logo logo=null;
		XmlPullParser parser = Xml.newPullParser();
		/**为Pull解析器设置要解析的XML数据**/
		parser.setInput(inStream, "UTF-8");
		int event = parser.getEventType();
		while(event != XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				logo=new Logo();
				break;
				
			case XmlPullParser.START_TAG:
				if("name".equals(parser.getName())){
					/**此处要注意,服务器端提供的版本号必须为double格式字符串
					 * 否则将会出异常
					 * **/
					String v = parser.nextText();//parser.getAttributeValue(1);
					Log.i("a", "v"+v);
					logo.setName(v);
				}
				if("state".equals(parser.getName())){
					String state = parser.nextText();
					logo.setState(state);
					
				}				
				break;				
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		return logo;
	}
	
	/**
	 * 配额是否需要更新
	 * 
	 * @param quotaStream
	 * @return
	 */
	public String getListQuotaTime(InputStream inStream) {
		 
	    XmlPullParser parser = Xml.newPullParser();  
	    try {  
	        parser.setInput(inStream, "UTF-8");// 设置数据源编码  
	        int eventType = parser.getEventType();// 获取事件类型  
	        String time="";
	        while (eventType != XmlPullParser.END_DOCUMENT) {  
	            switch (eventType) {  
	            case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理  
	                break;  
	            case XmlPullParser.START_TAG://开始读取某个标签  
	                //通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值  
	                String name = parser.getName(); 
	                if("RegDate".equals(name)){
	                	time = parser.nextText();
					}
	                
	                break;  
	            case XmlPullParser.END_TAG:// 结束元素事件  
	                break;  
	            }  
	            eventType = parser.next();  
	        }  
	        inStream.close();  
	        return time;
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return null;  
	    
		
	}
	
	
	/**
	 * 配额解析
	 * 
	 * @param quotaStream
	 * @return
	 */
	public ArrayList<Quota> getListQuota(InputStream inStream) {
		 
	    XmlPullParser parser = Xml.newPullParser();  
	    try {  
	        parser.setInput(inStream, "UTF-8");// 设置数据源编码  
	        int eventType = parser.getEventType();// 获取事件类型  
	        Quota quota = null;  
	        ArrayList<Quota> quotalist = null;  
	        option opt = null;  
	        ArrayList<option> optlist = null;  
	        
	        while (eventType != XmlPullParser.END_DOCUMENT) {  
	            switch (eventType) {  
	            case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理  
	            	quotalist = new ArrayList<Quota>(); // 实例化集合类  
	            	optlist=new ArrayList<option>();
	            	opt=new option();
	            	quota=new Quota();
	                break;  
	            case XmlPullParser.START_TAG://开始读取某个标签  
	                //通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值  
	                String name = parser.getName(); 
	               /* if (name.equalsIgnoreCase("panels")) {  
	                	
	                }*/
	                if (name.equalsIgnoreCase("panel")) {
	                	quota = new Quota();
	                	quota.setQuotaId(parser.getAttributeValue(0));
					} 
	                if("RuleName".equals(name)){
	                	quota.setQuotaName(parser.nextText()); 
					}
	                if("RegDate".equals(name)){
	                	quota.setQuotaTime(parser.nextText());
					}
	                if("Option".equals(name)){
	                	opt.setCondition(parser.getAttributeValue(4));
	                	opt.setType(parser.getAttributeValue(1));
	                	opt.setMatch(parser.getAttributeValue(2));
	                	opt.setSymbol(parser.getAttributeValue(3));
	                	opt.setQuestionindex(parser.getAttributeValue(0));
	                	
					}
	                if("ContentStr".equals(name)){
	                	quota.setQuotaIns(parser.nextText()); 
					}	
	                
	                break;  
	            case XmlPullParser.END_TAG:// 结束元素事件  
	            	if (parser.getName().equalsIgnoreCase("Option")&& opt != null) {  
	                		optlist.add(opt);
	                		quota.setOptionlist(optlist);
	                }  
	                if (parser.getName().equalsIgnoreCase("panel")&& quota != null) {  
	                	quotalist.add(quota);
	                	optlist.clear();
	                	quota = null;  
	                }  
	                break;  
	            }  
	            eventType = parser.next();  
	        }  
	        inStream.close();  
	        return quotalist;  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return null;  
	    
		
	}
	
	public static List<HashMap<String,String>> parserXml(InputStream inputStream,String encoding,String startNode,String... nodes){  
	      
	    List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();  
	    HashMap<String, String> map = null;  
	      
	    try {  
	        XmlPullParser  parser = XmlPullParserFactory.newInstance().newPullParser();  
	          
	        parser.setInput(inputStream,encoding);  
	          
	        int evenType = parser.getEventType();  
	          
	        while (evenType!=XmlPullParser.END_DOCUMENT) {  
	              
	            String nodeName = parser.getName();  
	              
	            switch (evenType) {  
	            case XmlPullParser.START_TAG:  
	                if(startNode.equals(nodeName)){  
	                    map = new HashMap<String, String>();  
	                }  
	                for(int i=0;i<nodes.length;i++){  
	                    if(nodes[i].equalsIgnoreCase(nodeName)){  
	                        String temp = parser.nextText();  
	                        map.put(nodes[i],temp);  
	                    }  
	                }  
	                break;  
	  
	            case XmlPullParser.END_TAG:  
	                if(startNode.equals(nodeName)&&map!=null){  
	                    list.add(map);  
	                }  
	                break;  
	            }  
	              
	            evenType = parser.next();  
	              
	        }  
	          
	    } catch (XmlPullParserException e) {  
	    } catch (IOException e) { 
	    }  
	      
	    return list;  
	}  
	
	
}
