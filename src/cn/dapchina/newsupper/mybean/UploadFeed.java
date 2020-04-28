package cn.dapchina.newsupper.mybean;

import java.io.Serializable;

public class UploadFeed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6081914491234073168L;
	private String id;
	private String time;
	private String state;
	private String add01;
	private String add02;
	private String add03;
	private String add04;
	private int isCheck; // 0是未选中 1为选中

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}

	public UploadFeed() {

	}

	public UploadFeed(String id, String time, String state, String add01, String add02, String add03, String add04) {
		super();
		this.id = id;
		this.time = time;
		this.state = state;
		this.add01 = add01;
		this.add02 = add02;
		this.add03 = add03;
		this.add04 = add04;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAdd01() {
		return add01;
	}

	public void setAdd01(String add01) {
		this.add01 = add01;
	}

	public String getAdd02() {
		return add02;
	}

	public void setAdd02(String add02) {
		this.add02 = add02;
	}

	public String getAdd03() {
		return add03;
	}

	public void setAdd03(String add03) {
		this.add03 = add03;
	}

	public String getAdd04() {
		return add04;
	}

	public void setAdd04(String add04) {
		this.add04 = add04;
	}

}
