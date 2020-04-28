package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.HashMap;

public class InnerPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -924646523019289548L;
	
	/**
	 * 答卷的id
	 */
	private String feedId="";
	
	private String panelID = "";
	
	private HashMap<String, Parameter> psMap = new HashMap<String, Parameter>();
	
	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public HashMap<String, Parameter> getPsMap() {
		return psMap;
	}

	public void setPsMap(HashMap<String, Parameter> psMap) {
		this.psMap = psMap;
	}
	
	public String getPanelID() {
		return panelID;
	}

	public void setPanelID(String panelID) {
		this.panelID = panelID;
	}
	
}
