package cn.dapchina.newsupper.bean;

import java.util.ArrayList;

public class CstmMatcher {

	private String resultStr;
	
	private ArrayList<MatcherItem> mis = new ArrayList<MatcherItem>();

	public String getResultStr() {
		return resultStr;
	}

	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}

	public ArrayList<MatcherItem> getMis() {
		return mis;
	}

	public void setMis(ArrayList<MatcherItem> mis) {
		this.mis = mis;
	}
	
}
