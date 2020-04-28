package cn.dapchina.newsupper.mybean;

public class Person {

	private int number; // 编号  
	
	private String title; // 标题 
	
	private String content;// 内容  

	public Person() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Person(int number, String title, String content) {
		super();
		this.number = number;
		this.title = title;
		this.content = content;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
