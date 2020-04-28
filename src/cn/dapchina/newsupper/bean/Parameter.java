package cn.dapchina.newsupper.bean;

import java.io.Serializable;

public class Parameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2176444494246433380L;

	/**
	 * 节点的名称
	 */
	private String nodeName="";
	
	/**
	 * 要现实的列标题
	 */
	//private String note="";
	
	/**
	 * 回传的字段
	 */
	private String sid="";
	
	/**
	 * 回传值
	 */
	private String content="";

	public Parameter(){
		
	}
	
	/**
	 * 引用受访者参数
	 * @param sid
	 * @param content
	 */
	public Parameter(String sid, String content) {
		this.sid = sid;
		this.content = content;
	}
	
	public Parameter(String nodeName, String sid, String content) {
		this.nodeName = nodeName;
		//this.note = node;
		this.sid = sid;
		this.content = content;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
