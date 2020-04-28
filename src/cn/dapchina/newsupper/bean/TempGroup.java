package cn.dapchina.newsupper.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class TempGroup {

	private String bigGroupName;// 大组名
	private HashMap<Integer, String> smallGroupMap;
	private int bigOrder;// 大组号
	private ArrayList<Integer> smallOrder;// 小组号
	
	public TempGroup(){
		
	}

	public TempGroup(String bigGroupName, HashMap<Integer, String> smallGroupMap, int bigOrder, ArrayList<Integer> smallOrder) {
		super();
		this.bigGroupName = bigGroupName;
		this.smallGroupMap = smallGroupMap;
		this.bigOrder = bigOrder;
		this.smallOrder = smallOrder;
	}

	public String getBigGroupName() {
		return bigGroupName;
	}

	public void setBigGroupName(String bigGroupName) {
		this.bigGroupName = bigGroupName;
	}

	public HashMap<Integer, String> getSmallGroupMap() {
		return smallGroupMap;
	}

	public void setSmallGroupMap(HashMap<Integer, String> smallGroupMap) {
		this.smallGroupMap = smallGroupMap;
	}

	public int getBigOrder() {
		return bigOrder;
	}

	public void setBigOrder(int bigOrder) {
		this.bigOrder = bigOrder;
	}

	public ArrayList<Integer> getSmallOrder() {
		return smallOrder;
	}

	public void setSmallOrder(ArrayList<Integer> smallOrder) {
		this.smallOrder = smallOrder;
	}

	@Override
	public String toString() {
		return "TempGroup [bigGroupName=" + bigGroupName + ", smallGroupMap=" + smallGroupMap + ", bigOrder=" + bigOrder + ", smallOrder=" + smallOrder + "]";
	}

	

}
