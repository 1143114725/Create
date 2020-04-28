package cn.dapchina.newsupper.bean;

import java.io.Serializable;

public class ReturnType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -139856363252727105L;

	private int id;
	private String projectId;
	private int returnId=-1;  //sid
	private String returnName;  //选择的名字
	private int enable; //可用不可用
	private boolean isAndroid;
	
	

	
	@Override
	public String toString() {
		return "ReturnType [id=" + id + ", projectId=" + projectId + ", returnId=" + returnId + ", returnName=" + returnName + ", enable=" + enable + ", isAndroid=" + isAndroid + "]";
	}

	public boolean isAndroid() {
		return isAndroid;
	}

	public void setAndroid(boolean isAndroid) {
		this.isAndroid = isAndroid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public int getReturnId() {
		return returnId;
	}

	public void setReturnId(int returnId) {
		this.returnId = returnId;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

}
