package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;


public class option implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7808929358349741437L;
	/**
	 * 
	 */

	private String Questionindex;// 关联问题ID
	private String Type;// 问题类型
	private String Match;// 逻辑关系
	private String Symbol;// 比较符号
	private String Condition;// 对应问题的对应选项
	
	public String getQuestionindex() {
		return Questionindex;
	}
	public void setQuestionindex(String questionindex) {
		Questionindex = questionindex;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getMatch() {
		return Match;
	}
	public void setMatch(String match) {
		Match = match;
	}
	public String getSymbol() {
		return Symbol;
	}
	public void setSymbol(String symbol) {
		Symbol = symbol;
	}
	public String getCondition() {
		return Condition;
	}
	public void setCondition(String condition) {
		Condition = condition;
	}
	
	



}
