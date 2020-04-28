package cn.dapchina.newsupper.bean;

import java.io.Serializable;
/**
 * <answer type="item">
 * <name>VIS_2_1_0_2_10_0_0_0_0</name> 
 * <value>1</value> 
 * </answer>
 */
public class AnswerMap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1373054313087411095L;
	
	/**
	 *   题外关联 之  外部关联   设置  数字格式 的标示    大树  外部关联 
	 */
	public boolean isFigure;	
	/**
	 *    题外关联之  内部关联   设置 SUM标示 选项    大树  内部关联 
	 */
	public boolean isSUM;
	/**
	 * 题目标节点的名称
	 * <name>VIS_2_1_0_2_10_0_0_0_0</name> 
	 */
	public String answerName;
	
	/**
	 * 每个题目对应的值
	 * <value>1</value> 
	 */
	public String answerValue;

	/**
	 * 辅助引用
	 */
	public String answerText = "";
	
	/**
	 * 辅助矩阵
	 */
	private int row;
	
	/**
	 * 辅助
	 */
	private int col;
	
	public AnswerMap(){
		
	}
	
	public AnswerMap(String answerName, String answerValue, String answerText) {
		this.answerName = answerName;
		this.answerValue = answerValue;
		this.answerText = answerText;
	}

	public String getAnswerName() {
		return answerName;
	}

	public void setAnswerName(String answerName) {
		this.answerName = answerName;
	}

	public String getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(String answerValue) {
		this.answerValue = answerValue;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public String toString() {
		return "AnswerMap [isFigure=" + isFigure + ", isSUM=" + isSUM + ", answerName=" + answerName + ", answerValue=" + answerValue + ", answerText=" + answerText + ", row=" + row + ", col=" + col
				+ "]";
	}
	
	
	
}
