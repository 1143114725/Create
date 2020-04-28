package cn.dapchina.newsupper.bean;

import java.io.Serializable;

public class Knowledge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8405078543480390028L;

	private String id; // id
	private String title;// 名字
	private String content;// 内容
	private String kind;// 分类
	private String attach;// 附件 以;隔开
	private String fileName;// 附件名字 以;隔开
	private String userList;// 哪些用户共享知识库
	private String enable;// 是否可用 0可用 1不可用

	public String getUserList() {
		return userList;
	}

	public void setUserList(String userList) {
		this.userList = userList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

}
