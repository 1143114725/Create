package cn.dapchina.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OpenStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3297785636622767841L;

	private String parameter1 = "";
	private String parameter2 = "";
	private String parameter3 = "";
	private String parameter4 = "";

	private ArrayList<InnerPanel> ips = new ArrayList<InnerPanel>();
	// 保存引用受访者参数
	private ArrayList<ParameterInnerPanel> parameterIps = new ArrayList<ParameterInnerPanel>();

	private String parameterName = "";//命名规则参数

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public ArrayList<ParameterInnerPanel> getParameterIps() {
		return parameterIps;
	}

	public void setParameterIps(ArrayList<ParameterInnerPanel> parameterIps) {
		this.parameterIps = parameterIps;
	}

	public ArrayList<InnerPanel> getIps() {
		return ips;
	}

	public void setIps(ArrayList<InnerPanel> ips) {
		this.ips = ips;
	}

	public String getParameter1() {
		return parameter1;
	}

	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	public String getParameter2() {
		return parameter2;
	}

	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}

	public String getParameter3() {
		return parameter3;
	}

	public void setParameter3(String parameter3) {
		this.parameter3 = parameter3;
	}

	public String getParameter4() {
		return parameter4;
	}

	public void setParameter4(String parameter4) {
		this.parameter4 = parameter4;
	}

}
