package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Intervention implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7613583674475958875L;

	/**
	 * <intervention qIndex="1" symbol=">=" gotoIndex="3,5" single="false">
			<question qIndex="1" rows="3" iText="" regex=""></question>
			<question qIndex="2" rows="4" iText="" regex=""></question>
	 *	</intervention>
	 */
	
	/**
	 * 哪一道题目的index,
	 * 即主动干预者的index
	 */
	private String index;
	
	/**
	 * 比较符号
	 */
	private String symbol;
	
	private boolean single;

	private String rule;
	
	private HashMap<String, InterventionItem> iisMap = new HashMap<String, InterventionItem>();

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public HashMap<String, InterventionItem> getIisMap() {
		return iisMap;
	}

	public void setIisMap(HashMap<String, InterventionItem> iisMap) {
		this.iisMap = iisMap;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
