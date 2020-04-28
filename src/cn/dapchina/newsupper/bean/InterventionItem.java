package cn.dapchina.newsupper.bean;

import java.io.Serializable;
/**
 * 干预项
 */
public class InterventionItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7862683289580260428L;

	private String index;
	
	private int rows=-100;
	
	private String iText="";
	
	private String regex="";

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getiText() {
		return iText;
	}

	public void setiText(String iText) {
		this.iText = iText;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
