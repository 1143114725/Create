package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;

import com.alibaba.fastjson.JSON;

public class LogicList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2394320764496767750L;
	// 逻辑次数跳转
	private ArrayList<Logic> logics = new ArrayList<Logic>();
	private String logicsStr;

	public ArrayList<Logic> getLogics() {
		return logics;
	}

	public void setLogics(ArrayList<Logic> logics) {
		this.logics = logics;
		this.logicsStr = null;
		if (!Util.isEmpty(logics)) {
			this.logicsStr = JSON.toJSONString(logics);
		}
	}

	public String getLogicsStr() {
		return logicsStr;
	}

	public void setLogicsStr(String logicsStr) {
		this.logicsStr = logicsStr;
		if (!Util.isEmpty(this.logics)) {
			this.logics.clear();
		}
		if (!Util.isEmpty(logicsStr)) {
			ArrayList<Logic> _logics = (ArrayList<Logic>) JSON.parseArray(logicsStr, Logic.class);
			if (!Util.isEmpty(_logics)) {
				this.logics.addAll(_logics);
			}
		}
	}

}
