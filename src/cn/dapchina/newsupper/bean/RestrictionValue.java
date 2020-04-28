package cn.dapchina.newsupper.bean;

import java.io.Serializable;

public class RestrictionValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8563393699230848699L;

	/**
	 * 第一个数值
	 */
	private String equalText1="";
	/**
	 * 第二个数值
	 */
	private String equalText2="";
	
	/**
	 * 第一个数值的比较关系
	 */
	private String equal1=""; 
	/**
	 * 第二个数值的比较关系
	 */
	private String equal2=""; 
	
	/**
	 * 两个是同时成立还是只要成立其中一个
	 */
	private String matchEqual="";
	
	private String value="";
	
	/**
	 * 非逻辑
	 */
	private boolean isExclusive;
	
	public boolean isExclusive() {
		return isExclusive;
	}

	public void setExclusive(boolean isExclusive) {
		this.isExclusive = isExclusive;
	}

	/**
	 * <restriction Match="one" questionID="165">
	 * 	<value rows="0">3</value>
	 * 	<value rows="0">4</value>
	 * </restriction>
	 * 表示矩阵题目的行下标
	 */
	private int rows = -1;
	
	public RestrictionValue(){
		
	}

	public String getEqual1() {
		return equal1;
	}

	public void setEqual1(String equal1) {
		this.equal1 = equal1;
	}

	public String getEqual2() {
		return equal2;
	}

	public void setEqual2(String equal2) {
		this.equal2 = equal2;
	}

	public String getMatchEqual() {
		return matchEqual;
	}

	public void setMatchEqual(String matchEqual) {
		this.matchEqual = matchEqual;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getEqualText1() {
		return equalText1;
	}

	public void setEqualText1(String equalText1) {
		this.equalText1 = equalText1;
	}

	public String getEqualText2() {
		return equalText2;
	}

	public void setEqualText2(String equalText2) {
		this.equalText2 = equalText2;
	}

	@Override
	public String toString() {
		return "RestrictionValue [equalText1=" + equalText1 + ", equalText2=" + equalText2 + ", equal1=" + equal1 + ", equal2=" + equal2 + ", matchEqual=" + matchEqual + ", value=" + value
				+ ", rows=" + rows + "]";
	}
	
	
}
