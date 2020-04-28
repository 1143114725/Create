package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;


/**
 * 存放答案的
 */
/**
 * @author Administrator
 *
 */
public class Answer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2489596352664041006L;
	
	/**
	 * 题目在数据库中的id号
	 */
	public Integer id;
	
	/**
	 * 问卷号
	 */
	public String surveyId;
	
	/**
	 * 受访者帐号
	 */
	public String userId;
	
	/**
	 * 下标号
	 */
	public Integer qIndex;
	
	/**
	 * 顺序号
	 */
	public Integer qOrder;
	
	/**
	 * 题目的类型
	 */
	public int answerType;
	
	/**
	 * 音视频或照片的存放路径
	 */
	public String mediaPath;
	
	/**
	 * 音视频文件的名称
	 */
	public String mediaName;
	
	/**
	 * 返回类型
	 */
	public int returnType;

	public int enable;
	
	/**
	 * 哪一个淡答卷的
	 * surveyId, userId, uuid
	 */
	public String uuid;
	
	private ArrayList<AnswerMap> answerMapArr = new ArrayList<AnswerMap>();
	
	private String answerMapStr;
	
	

	public ArrayList<AnswerMap> getAnswerMapArr() {
		return answerMapArr;
	}

	public void setAnswerMapArr(ArrayList<AnswerMap> answerMapArr) {
		this.answerMapArr = answerMapArr;
		if(!Util.isEmpty(answerMapArr)){
			String mapStr = XmlUtil.jsonArr2MapStr(answerMapArr);
			if(!Util.isEmpty(mapStr)){
				this.answerMapStr = mapStr;
			}
		}
		
	}

	public String getAnswerMapStr() {
		return answerMapStr;
	}

	public void setAnswerMapStr(String answerMapStr) {
		this.answerMapStr = answerMapStr;
		if(!Util.isEmpty(answerMapStr)){
			
			ArrayList<AnswerMap> maps = XmlUtil.mapStr2JsonArr(answerMapStr);
			if(!Util.isEmpty(maps)){
				this.answerMapArr = maps;
			}
		}
	}

	@Override
	public String toString() {
		return "Answer [id=" + id + ", surveyId=" + surveyId + ", userId=" + userId + ", qIndex=" + qIndex + ", qOrder=" + qOrder + ", answerType=" + answerType + ", mediaPath=" + mediaPath
				+ ", mediaName=" + mediaName + ", returnType=" + returnType + ", enable=" + enable + ", uuid=" + uuid + ", answerMapArr=" + answerMapArr + ", answerMapStr=" + answerMapStr + "]";
	}

	
	
}
