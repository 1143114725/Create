package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dapchina.newsupper.util.Util;

/**
 * 小题组
 */
public class Group implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7626768620977508015L;
	
	/**
	 * 小题组顺序号
	 */
	private Integer order = -1;
	
	/**
	 * 小题组名称
	 */
	private String groupName = "";
	
	/**
	 * 小题组是否随机
	 */
	private boolean random;
	
	/**
	 * 
	 */
	private String globalGroup;
	
	/**
	 * 随机题目index集合
	 */
	private ArrayList<Integer> indexArr = new ArrayList<Integer>();
	
	/**
	 * index字符串
	 */
	private String indexStr;

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	public String getGlobalGroup() {
		return globalGroup;
	}

	public void setGlobalGroup(String globalGroup) {
		this.globalGroup = globalGroup;
	}

	public ArrayList<Integer> getIndexArr() {
		return indexArr;
	}

	public void setIndexArr(ArrayList<Integer> mIndex) {
		this.indexArr = mIndex;
		if(!Util.isEmpty(mIndex)){
			this.indexStr = null;
			for (int i = 0; i < mIndex.size(); i++) {
				if(!Util.isEmpty(String.valueOf(mIndex.get(i)))){
					if(Util.isEmpty(this.indexStr)){
						this.indexStr = String.valueOf(mIndex.get(i));
					}else{
						this.indexStr += (","+mIndex.get(i));
					}
				}
			}
//			System.out.println("setIndexArr--->"+this.indexStr);
		}
	}

	public String getIndexStr() {
		return indexStr;
	}

	public void setIndexStr(String indexStr) {
		this.indexStr = indexStr;
		if(!Util.isEmpty(indexStr)){
			this.indexArr.clear();
			String[] indexs = indexStr.split(",");
			for (int i = 0; i < indexs.length; i++) {
				if(!Util.isEmpty(indexs[i])){
					this.indexArr.add(Integer.parseInt(indexs[i]));
				}
			}
//			System.out.print("setIndexStr--->");
//			System.out.println(this.indexArr);
		}
	}
	
}
