package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;

import com.alibaba.fastjson.JSON;


public class SurveyQuestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3660436003495135273L;

	private int eligible = -1;

	private String qgsStr;

	private ArrayList<QGroup> qgs = new ArrayList<QGroup>();

	private ArrayList<Question> questions = new ArrayList<Question>();
	// 访前说明
	private String word;
	// 逻辑次数跳转
	private LogicList logicList;
	
	private ArrayList<String> classId=new ArrayList<String>();//数据字典数组

	public ArrayList<String> getClassId() {
		return classId;
	}

	public void setClassId(ArrayList<String> classId) {
		this.classId = classId;
	}

	public LogicList getLogicList() {
		return logicList;
	}

	public void setLogicList(LogicList logicList) {
		this.logicList = logicList;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getEligible() {
		return eligible;
	}

	public void setEligible(int eligible) {
		this.eligible = eligible;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	public String getQgsStr() {
		return qgsStr;
	}

	public void setQgsStr(String qgsStr) {
		this.qgsStr = qgsStr;
		if (!Util.isEmpty(this.qgs)) {
			this.qgs.clear();
		}
		if (!Util.isEmpty(qgsStr)) {
			ArrayList<QGroup> _qgs = (ArrayList<QGroup>) JSON.parseArray(qgsStr, QGroup.class);
			if (!Util.isEmpty(_qgs)) {
				this.qgs.addAll(_qgs);
			}
		}
	}

	public ArrayList<QGroup> getQgs() {
		return qgs;
	}

	public void setQgs(ArrayList<QGroup> qgs) {
		this.qgs = qgs;
		this.qgsStr = null;
		if (!Util.isEmpty(qgs)) {
			this.qgsStr = JSON.toJSONString(qgs);
		}
	}

}
