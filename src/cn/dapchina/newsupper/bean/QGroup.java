package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import cn.dapchina.newsupper.util.Util;

import com.alibaba.fastjson.JSON;


/**
 * 大题组
 */
public class QGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7626768620977508015L;

	/**
	 * 大题组循环类型
	 */
	/**
	 * 不循环
	 */
	public static final int GROUP_TYPE_NONE = 0;
	/**
	 * 自动循环
	 */
	public static final int GROUP_TYPE_AUTO = 1;
	/**
	 * 手动循环
	 */
	public static final int GROUP_TYPE_HAND = 2;

	/**
	 * 题目的order
	 */
	private String index;
	/**
	 * 题目的index
	 */
	private String realIndex;

	/**
	 * 大题组顺序号
	 */
	private int order = -1;

	/**
	 * 大题组名称
	 */
	private String groupName = "";

	/**
	 * 大题组循环类型
	 */
	private int groupType;

	/**
	 * 大题组是否随机
	 */
	private boolean random;

	/**
	 * 小题组
	 */
	private ArrayList<Group> groups = new ArrayList<Group>();

	private HashMap<Integer, Group> gsMap = new HashMap<Integer, Group>();

	private String groupsStr;

	/**
	 * 已经随机过
	 */
	private boolean isAlreadyRandom;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> mGroups) {
		this.groups = mGroups;
		if (!Util.isEmpty(mGroups)) {
			this.groupsStr = JSON.toJSONString(mGroups);
		}
	}

	public String getGroupsStr() {
		return groupsStr;
	}

	public void setGroupsStr(String mGroupsStr) {
		this.groupsStr = mGroupsStr;
		if (!Util.isEmpty(mGroupsStr)) {
			this.groups.clear();
			ArrayList<Group> gs = (ArrayList<Group>) JSON.parseArray(mGroupsStr, Group.class);
			if (!Util.isEmpty(gs)) {
				this.groups.addAll(gs);
				for (Group g : gs) {
					this.gsMap.put(g.getOrder(), g);
				}
			}
		}
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public boolean isAlreadyRandom() {
		return isAlreadyRandom;
	}

	public void setAlreadyRandom(boolean isAlreadyRandom) {
		this.isAlreadyRandom = isAlreadyRandom;
	}

	public HashMap<Integer, Group> getGsMap() {
		return gsMap;
	}

	public void setGsMap(HashMap<Integer, Group> gsMap) {
		this.gsMap = gsMap;
	}

	public String getRealIndex() {
		return realIndex;
	}

	public void setRealIndex(String realIndex) {
		this.realIndex = realIndex;
	}

}
