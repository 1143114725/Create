package cn.dapchina.newsupper.bean;

import java.io.Serializable;

public class Data implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2406023166104917688L;

	private String classId;// 数据字典唯一id
	private String className;//数据字典分类组
	private String datas;// 该classId下所有的数据
	private String localDatas;// 本地新增数据

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getDatas() {
		return datas;
	}

	public void setDatas(String datas) {
		this.datas = datas;
	}

	public String getLocalDatas() {
		return localDatas;
	}

	public void setLocalDatas(String localDatas) {
		this.localDatas = localDatas;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	
	
}
