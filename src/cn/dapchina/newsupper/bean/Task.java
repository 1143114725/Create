package cn.dapchina.newsupper.bean;

import java.util.HashMap;

public class Task {

	private int taskId;
	private HashMap<String, Object> params;
	
	public Task(int taskId, HashMap<String,Object> params) {
		this.taskId = taskId;
		this.params = params;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public HashMap<String,Object> getParams() {
		return params;
	}
	public void setParams(HashMap<String,Object> params) {
		this.params = params;
	}
}
