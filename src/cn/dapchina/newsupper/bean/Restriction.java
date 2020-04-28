package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;


/**
 *	逻辑
 */
public class Restriction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4087103758053265496L;

	private String match;
	
	private Integer questionId = -1;
	
	//private String valueArr;

	private String valueArrStr;
	
	private ArrayList<RestrictionValue> rvs = new ArrayList<RestrictionValue>();
	
	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getValueArrStr() {
		return valueArrStr;
	}

	public void setValueArrStr(String valueArrStr) {
		this.valueArrStr = valueArrStr;
		if(!Util.isEmpty(valueArrStr)){
			ArrayList<RestrictionValue> _rvs = XmlUtil.parserJson2RestValueArr(valueArrStr);
			if(!Util.isEmpty(_rvs)){
				this.rvs = _rvs;
			}
		}
	}

	public ArrayList<RestrictionValue> getRvs() {
		return rvs;
	}

	public void setRvs(ArrayList<RestrictionValue> rvs) {
		this.rvs = rvs;
		if(!Util.isEmpty(rvs)){
			this.valueArrStr = XmlUtil.parserRestValueArr2Json(rvs);
		}
	}
	
	
	
}
