package cn.dapchina.newsupper.bean;

import java.io.Serializable;


import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;



/**
 * 用于上传和XML的拼写
 */
public class UploadFeed implements Serializable,Cloneable {

	/**
	 * 缩略图
	 */
	public Object clone() {
		UploadFeed uf = null;
		try {
			uf = (UploadFeed) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return uf;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3529369505221638789L;

	/**
	 * mp3或png在数据库中的主键号
	 */
	private long id;
	
	/**
	 *   大树拒访  
	 */
	private int returnTypeId;//   问卷访问状态 返回码  -2 代表正常 ，其余   代表 拒访   
	public int getReturnTypeId() {
		return returnTypeId;
	}
	public void setReturnTypeId(int returnTypeId) {
		this.returnTypeId = returnTypeId;
	}

	/**
	 * 记录的全球唯一号
	 */
	private String uuid;

	/**
	 * 服务器端返回的ID
	 */
	private String feedId;
	/**
	 * 用户的id
	 */
	private String userId;
	/**
	 * 调查的id号
	 */
	private String surveyId;

	/**
	 * 1-->XML, 2-->PNG, 3-->MP3
	 */
	private int type;

	/**
	 * 文件存在那个目录下
	 */
	private String path;

	/**
	 * 文件的名称
	 */
	private String name;
	/**
	 * 记录的创建时间
	 */
	private long createTime;

	/**
	 * 文件的开始时间
	 */
	private long startTime;

	/**
	 * 文件的结束时间
	 */
	private long regTime;

	/**
	 * 文件的大小
	 */
	private long size;

	/**
	 * 是否完成
	 */
	private int isCompleted;
	/**
	 * 是否上传
	 */
	private int isUploaded;
	/**
	 * 纬度
	 */
	private String lat;
	/**
	 * 经度
	 */
	private String lng;
	/**
	 * 访问模式,web模式和原生模式
	 */
	private int visitMode;

	/**
	 * 记录每一次的开始时间和结束事件,以“开始时间,结束时间;”的方式记录
	 */
	private String manyTimes;
	/**
	 * 做访问时,当时的地点
	 */
	private String visitAddress;
	/**
	 * 是否为同步的
	 */
	private int isSync;

	/**
	 * 每次访问时间之和
	 */
	private long spent;

	/**
	 * 服务器返回的id号
	 */
	private String returnType;

	/**
	 * 调查的标题
	 */
	private String surveyTitle;

	/**
	 * 问题的id号
	 */
	private String questionId;

	/**
	 * 每一次坐标的经纬度
	 */
	private String LotsCoord;

	/**
	 * 问题的下标号
	 */
	private String indexArr;

	private String manyPlaces;

	private Survey survey;

	private String innerPanelStr;

	private InnerPanel innerPanel = new InnerPanel();

	/**
	 * 屏幕是横向还是纵向 0纵向 1横向
	 */
	// private int screenOrientation;

	/*
	 * web是否为预览
	 */
	private int isReview;

	/**
	 * 是不是保存在内部存储卡中
	 */
	private int isSaveInner;
	
	/**
	 * 手动题组随机的选择序列号
	 */
	private String groupSequence;
	
	private String pid;//增加pid
	//引用受访者参数
	private String parametersStr;
	
	private String parametersContent;//命名规则引用的属性
	
	private String directory;//目录节点
	

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getParametersContent() {
		return parametersContent;
	}

	public void setParametersContent(String parametersContent) {
		this.parametersContent = parametersContent;
	}

	public String getParametersStr() {
		return parametersStr;
	}

	public void setParametersStr(String parametersStr) {
		this.parametersStr = parametersStr;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public UploadFeed() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getRegTime() {
		return regTime;
	}

	public void setRegTime(long regTime) {
		this.regTime = regTime;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(int isCompleted) {
		this.isCompleted = isCompleted;
	}

	public int getIsUploaded() {
		return isUploaded;
	}

	public void setIsUploaded(int isUploaded) {
		this.isUploaded = isUploaded;
	}

	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public int getVisitMode() {
		return visitMode;
	}

	public void setVisitMode(int visitMode) {
		this.visitMode = visitMode;
	}

	public String getManyTimes() {
		return manyTimes;
	}

	public void setManyTimes(String manyTimes) {
		this.manyTimes = manyTimes;
	}

	public String getVisitAddress() {
		return visitAddress;
	}

	public void setVisitAddress(String visitAddress) {
		this.visitAddress = visitAddress;
	}

	public int getIsSync() {
		return isSync;
	}

	public void setIsSync(int isSync) {
		this.isSync = isSync;
	}

	public long getSpent() {
		return spent;
	}

	public void setSpent(long spent) {
		this.spent = spent;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getSurveyTitle() {
		return surveyTitle;
	}

	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getLotsCoord() {
		return LotsCoord;
	}

	public void setLotsCoord(String lotsCoord) {
		LotsCoord = lotsCoord;
	}

	public String getIndexArr() {
		return indexArr;
	}

	public void setIndexArr(String indexArr) {
		this.indexArr = indexArr;
	}

	public String getManyPlaces() {
		return manyPlaces;
	}

	public void setManyPlaces(String manyPlaces) {
		this.manyPlaces = manyPlaces;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public InnerPanel getInnerPanel() {
		return innerPanel;
	}

	public void setInnerPanel(InnerPanel innerPanel) {
		this.innerPanel = innerPanel;
	}

	public String getInnerPanelStr() {
		return innerPanelStr;
	}

	public void setInnerPanelStr(String innerPanelStr) {
		this.innerPanelStr = innerPanelStr;
		if (!Util.isEmpty(innerPanelStr)) {
			setInnerPanel(XmlUtil.parserJson2InnerPanel(innerPanelStr));
		}
	}

	public int getIsReview() {
		return isReview;
	}

	public void setIsReview(int isReview) {
		this.isReview = isReview;
	}

	public int getIsSaveInner() {
		return isSaveInner;
	}

	public void setIsSaveInner(int isSaveInner) {
		this.isSaveInner = isSaveInner;
	}

	public String getGroupSequence() {
		return groupSequence;
	}

	public void setGroupSequence(String groupSequence) {
		this.groupSequence = groupSequence;
	}
	
	

}
