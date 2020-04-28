package cn.dapchina.newsupper.bean;

import java.io.Serializable;

public class LogicItem implements Serializable {

	/**
	 * 逻辑次数跳转验证选项
	 */
	private static final long serialVersionUID = 6354880049393591215L;

	private int logicIndex;// 需要验证的index
	private String logicValue;// 需要验证的值

	public int getLogicIndex() {
		return logicIndex;
	}

	public void setLogicIndex(int logicIndex) {
		this.logicIndex = logicIndex;
	}

	public String getLogicValue() {
		return logicValue;
	}

	public void setLogicValue(String logicValue) {
		this.logicValue = logicValue;
	}

}
