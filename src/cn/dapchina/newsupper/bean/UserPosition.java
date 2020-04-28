package cn.dapchina.newsupper.bean;

public class UserPosition {

	private String userId;
	private double userLat; // 纬
	private double userLng; // 经
	private String time; // 时间
	private String addName; // 地点
	private int isUpload;// 是否上传了
	private String date;// 日期

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getUserLat() {
		return userLat;
	}

	public void setUserLat(double userLat) {
		this.userLat = userLat;
	}

	public double getUserLng() {
		return userLng;
	}

	public void setUserLng(double userLng) {
		this.userLng = userLng;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddName() {
		return addName;
	}

	public void setAddName(String addName) {
		this.addName = addName;
	}

	public int getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(int isUpload) {
		this.isUpload = isUpload;
	}

}
