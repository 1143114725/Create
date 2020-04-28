package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;


public class Quota implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -762459985077112187L;
	/**
	 * 
	 */

	private String quotaId;// 配额编号
	private String quotaName;// 配额名称
	private String quotaTime;// 创建时间
	private int quotaSuccess;// 成功量
	private String quotaIns;// 配额说明
	private ArrayList<option> optionlist  = new ArrayList<option>();//问题配额list
	private String optionstr ;//问题配额存数据字符串
	private String quota_Userid;// 配额用户编号
	private String quota_Surveyid;// 配额项目编号
	
	
	public String getQuota_Surveyid() {
		return quota_Surveyid;
	}


	public void setQuota_Surveyid(String quota_Surveyid) {
		this.quota_Surveyid = quota_Surveyid;
	}


	public String getQuota_Userid() {
		return quota_Userid;
	}


	public void setQuota_Userid(String quota_Userid) {
		this.quota_Userid = quota_Userid;
	}


	public ArrayList<option> getOptionlist() {
		return optionlist;
	}


	public String getOptionstr() {
		return optionstr;
	}


	public void setOptionstr(String optionstr) {
		this.optionstr = optionstr;
		if(!Util.isEmpty(optionstr)){
			
			ArrayList<option> optlist = XmlUtil.listStr2JsonArr(optionstr);
			if(!Util.isEmpty(optlist)){
				this.optionlist = optlist;
			}
		}
	}


	public void setOptionlist(ArrayList<option> optionlist) {
		this.optionlist = optionlist;
		if(!Util.isEmpty(optionlist)){
			String optStr = XmlUtil.jsonArr2optionStr(optionlist);
			if(!Util.isEmpty(optStr)){
				this.optionstr = optStr;
			}
		}
	}


	public String getQuotaId() {
		return quotaId;
	}


	public void setQuotaId(String quotaId) {
		this.quotaId = quotaId;
	}


	public String getQuotaName() {
		return quotaName;
	}

	public void setQuotaName(String quotaName) {
		this.quotaName = quotaName;
	}

	public String getQuotaTime() {
		return quotaTime;
	}

	public void setQuotaTime(String quotaTime) {
		this.quotaTime = quotaTime;
	}

	public int getQuotaSuccess() {
		return quotaSuccess;
	}

	public void setQuotaSuccess(int quotaSuccess) {
		this.quotaSuccess = quotaSuccess;
	}

	public String getQuotaIns() {
		return quotaIns;
	}

	public void setQuotaIns(String quotaIns) {
		this.quotaIns = quotaIns;
	}

}
