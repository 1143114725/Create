package cn.dapchina.newsupper.bean;

import java.util.ArrayList;

public class TempLogic {

//	private ArrayList<Integer> list;// 存放键值的 值是order
	private int LastOrder = 0;// 最后一次跳转的order
	private boolean IsForwardComplete = false;// 是否可以跳转
	private boolean IsReturnComplete = false;// 是否可以返回跳转
	
	private int tempLastOrder=0;//暂存的最后order,用于清空答案
	
	public int getTempLastOrder() {
		return tempLastOrder;
	}

	public void setTempLastOrder(int tempLastOrder) {
		this.tempLastOrder = tempLastOrder;
	}

	public boolean isIsReturnComplete() {
		return IsReturnComplete;
	}

	public void setIsReturnComplete(boolean isReturnComplete) {
		IsReturnComplete = isReturnComplete;
	}

	public boolean isIsForwardComplete() {
		return IsForwardComplete;
	}

	public void setIsForwardComplete(boolean isForwardComplete) {
		IsForwardComplete = isForwardComplete;
	}

//	public ArrayList<Integer> getList() {
//		return list;
//	}
//
//	public void setList(ArrayList<Integer> list) {
//		this.list = list;
//	}

	public int getLastOrder() {
		return LastOrder;
	}

	public void setLastOrder(int lastOrder) {
		LastOrder = lastOrder;
	}

}
