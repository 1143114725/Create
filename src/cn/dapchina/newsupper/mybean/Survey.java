package cn.dapchina.newsupper.mybean;

public class Survey {

	
	private String surveyName;
	
	private String surveyTime;
	
	private String surveyContent;

	public Survey() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Survey(String surveyName, String surveyTime, String surveyContent) {
		super();
		this.surveyName = surveyName;
		this.surveyTime = surveyTime;
		this.surveyContent = surveyContent;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getSurveyTime() {
		return surveyTime;
	}

	public void setSurveyTime(String surveyTime) {
		this.surveyTime = surveyTime;
	}

	public String getSurveyContent() {
		return surveyContent;
	}

	public void setSurveyContent(String surveyContent) {
		this.surveyContent = surveyContent;
	}
	
	
	
	
}
