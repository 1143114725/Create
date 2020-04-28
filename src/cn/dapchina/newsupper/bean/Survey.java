package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;

/**
 * @author Administrator
 *
 */
public class Survey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6185055396453310303L;

	/**
	 * 在android数据库中的顺序号
	 */
	public Integer id;
	
	/**
	 * 是否新建限制 0不限制 1限制
	 */
	public int oneVisit;

	/**
	 * 调查的问卷号
	 */
	public String surveyId;

	/**
	 * 是否拍照
	 */
	public int isPhoto;

	/**
	 * 是否录音
	 */
	public int isRecord;
	
	/**
	 * 是否摄像 0为没摄像 1为摄像
	 */
	public int isVideo;

	/**
	 * 是否整张问卷显示
	 */
	public int isExpand;

	/**
	 * 调查问卷的标题
	 */
	public String surveyTitle;

	// 行 row
	// 列 column
	/**
	 * 问卷的网络下载地址
	 */
	public String downloadUrl;

	/**
	 * 是否下载过
	 */
	public int isDowned;

	/**
	 * 问卷是否可用
	 */
	public int surveyEnable;

	/**
	 * 问卷的发布时间
	 */
	public String publishTime;

	/**
	 * 问卷的内容
	 */
	public String surveyContent;

	public int upload;

	public String upTime;

	public float version;

	public int openStatus;

	public int unUploadCount;
	/**
	 * 访问模式
	 */
	public int visitMode;

	public String userIdList;

	/**
	 * 是否为测试问卷
	 */
	public int isTest;

	/**
	 * 内部名单是否下载过
	 */
	public int innerDone;

	/**
	 * 是否为全局录音
	 */
	public int globalRecord;

	/**
	 * 地图类型,是谷歌还是百度
	 */
	public String mapType = "";

	public int eligible = -1;

	private String parameter1 = "";
	private String parameter2 = "";
	private String parameter3 = "";
	private String parameter4 = "";
	
	private String parameterName="";//命名规则属性
	
	// 逻辑次数跳转
	public String strLogicList="";

	/**
	 * 密码
	 */
	private String password = "";
	private String word = "";// 访前说明

	private int noticeNew = 0;// 问卷提醒字段
	private String generatedTime = "";// 问卷提醒生成问卷时间
	
	private int isCheck; // 0是未选中 1为选中
	//访问状态开始
	private ArrayList<ReturnType> rlist=new ArrayList<ReturnType>();
	private String returnStr;

	public ArrayList<ReturnType> getRlist() {
		return rlist;
	}

	public void setRlist(ArrayList<ReturnType> rlist) {
		this.rlist = rlist;
		if(!Util.isEmpty(rlist)){
			this.returnStr = XmlUtil.jsonArr2ReturnTypeStr(rlist);
		}
	}
	
	public String getReturnStr() {
		return returnStr;
	}

	public void setReturnStr(String returnStr) {
		this.returnStr = returnStr;
		if(!Util.isEmpty(returnStr)){
			/**
			 * 从xml文件解析出逻辑的集合
			 * 然后转成json字符串
			 * 目的存在数据库中备以后拿出来用
			 */
			ArrayList<ReturnType> rs = XmlUtil.returnTypeStr2JsonArr(returnStr);
			if(!Util.isEmpty(rs)){
				this.rlist = rs;
			}
		}
	}
	
	//访问状态结束

	

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getGeneratedTime() {
		return generatedTime;
	}

	public void setGeneratedTime(String generatedTime) {
		this.generatedTime = generatedTime;
	}

	public int getNoticeNew() {
		return noticeNew;
	}

	public void setNoticeNew(int noticeNew) {
		this.noticeNew = noticeNew;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Survey [id=" + id + ", surveyId=" + surveyId + ", isPhoto=" + isPhoto + ", isRecord=" + isRecord + ", isExpand=" + isExpand + ", surveyTitle=" + surveyTitle + ", downloadUrl="
				+ downloadUrl + ", isDowned=" + isDowned + ", surveyEnable=" + surveyEnable + ", publishTime=" + publishTime + ", surveyContent=" + surveyContent + ", upload=" + upload + ", upTime="
				+ upTime + ", version=" + version + ", openStatus=" + openStatus + "]";
	}

	public String getParameter1() {
		return parameter1;
	}

	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	public String getParameter2() {
		return parameter2;
	}

	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}

	public String getParameter3() {
		return parameter3;
	}

	public void setParameter3(String parameter3) {
		this.parameter3 = parameter3;
	}

	public String getParameter4() {
		return parameter4;
	}

	public void setParameter4(String parameter4) {
		this.parameter4 = parameter4;
	}

}
