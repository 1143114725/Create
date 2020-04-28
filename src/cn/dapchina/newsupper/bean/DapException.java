package cn.dapchina.newsupper.bean;

public class DapException {

	// 文件名
	private String fileName;  //用户名_mac地址_时间
	// 路径
	private String filePath;
	// 用户名字
	private String userId;
	// mac地址
	private String macAddress;
	// 是否可用 0为不可用 1为可用
	private int enable;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

}
