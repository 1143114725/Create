package cn.dapchina.newsupper.bean;

import java.io.Serializable;

/**
 *	匹配项实体
 */
public class MatcherItem implements Serializable {

	public static final String COLOR_RED = "red";
	public static final String COLOR_BLUE = "blue";
	public static final String COLOR_GREEN = "green";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1111663359077006528L;
	/**
	 * 特效的开始位置
	 */
	public int start = -1;
	
	/**
	 * 特效的结束位置
	 */
	public int end = -1;

	public int color;
	
	public String name;
}
