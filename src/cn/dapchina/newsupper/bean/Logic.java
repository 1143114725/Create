package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;

import com.alibaba.fastjson.JSON;


public class Logic implements Serializable {

	/**
	 * 逻辑次数跳转的主实体
	 */
	private static final long serialVersionUID = 1642267206920505612L;

	private int jumpIndex;// 要跳到哪个题目
	private int countJump;// 几次跳
	private ArrayList<LogicItem> logicItem = new ArrayList<LogicItem>();
	private String logicItemsStr;

	public int getJumpIndex() {
		return jumpIndex;
	}

	public void setJumpIndex(int jumpIndex) {
		this.jumpIndex = jumpIndex;
	}

	public int getCountJump() {
		return countJump;
	}

	public void setCountJump(int countJump) {
		this.countJump = countJump;
	}

	public ArrayList<LogicItem> getLogicItem() {
		return logicItem;
	}

	public void setLogicItem(ArrayList<LogicItem> mlogicItem) {
		this.logicItem = mlogicItem;
		if (!Util.isEmpty(mlogicItem)) {
			this.logicItemsStr = JSON.toJSONString(mlogicItem);
		}
	}

	public String getLogicItemsStr() {
		return logicItemsStr;
	}

	public void setLogicItemsStr(String mlogicItemsStr) {
		this.logicItemsStr = mlogicItemsStr;
		if (!Util.isEmpty(mlogicItemsStr)) {
			this.logicItem.clear();
			ArrayList<LogicItem> lis = (ArrayList<LogicItem>) JSON.parseArray(mlogicItemsStr, LogicItem.class);
			if (!Util.isEmpty(lis)) {
				this.logicItem.addAll(lis);
			}
		}
	}
	
	

}
