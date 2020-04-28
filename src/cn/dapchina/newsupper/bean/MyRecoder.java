package cn.dapchina.newsupper.bean;
//记录丢失文件表
public class MyRecoder {

	private String feedId;// feedId
	private String name;// 附件名字
	private String uuid;// xml和附件的名字一样。通过这个查到feedId
	private int enable;// 0没提交，1已经提交

	private String surveyId;//问卷id
	
	private int count;//这个uuid的数量
	
	

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
