package cn.dapchina.newsupper.bean;

import java.io.Serializable;

public class QuestionItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4181454795297171906L;
	
	/**
	 * 引用排斥，固定显示
	 */
	public int itemShow;
	
	/**
	 *   大树    预览  选项置顶 标示   
	 */
	public Integer padding;
	
	/**
	 *  大树     预览   是否隐藏   
	 */
	public boolean isHide;
	/**
	 * 是否为其他项
	 */
	public int isOther;// 1是其他项, 0普通项
	
	/**
	 * 哑题是否是该项选中
	 */
	public boolean isDumbOk=false;

	/**
	 * 选项的宽度
	 */
	public Integer itemSize;
	
	/**
	 * 隐藏选项
	 */
	public int hide=0;

	/**
	 * 是否启用日期控件
	 */
	public int dateCheck;// 1表示启用了日历控件, 0表示没有启用

	// <item value="2">33</item>
	/**
	 * 选项的编号 eg value=2
	 */
	public Integer itemValue = -100;

	/**
	 * 选项后置文子 eg 33
	 */
	public String itemText;
	
	/**
	 * 矩阵右侧
	 */
	public String itemTextRight;
	/**
	 * 
	 */
	public String leftsideWord;

	/**
	 * 
	 */
	public String rightsideWord;

	/**
	 * 选项排斥, 也就是选了它之后, 其他的都不能选
	 */
	public String exclude;

	/**
	 * 
	 */
	public int isFreeInput;

	/**
	 * 选项中的追加说明
	 */
	public String caption;

	public int caption_check;// 追加说明方法,是否是显示还是提示。0显示1提示 默认是0

	/**
	 * 是否被预选
	 */
	public int deft;// 1被预选, 0没有

	/**
	 * 启用权重
	 */
	public int weightValue = -1;

	/**
	 * dateSelect=1,表示时间格式是年月日; dateSelect=2,表示时间格式是年月日时
	 * dateSelect=3,表示时间格式是年月日时分 dateSelect=4,表示时间格式是年月日时分秒
	 */
	public int dateSelect;

	/**
	 * 启用积分
	 */
	public int scoreValue = -1;

	/**
	 * 自定义的并非服务器xml配置的, 标志选项被选中了,
	 */
	public boolean isCheck;

	/**
	 * 单行文本题的数据类型 即 默认-1 none=0, 无即文本类型 date=1 日期 ,figure=2 数字, alphabet=3 英文/数字, data=4 字典 ,email=5 电子邮件,
	 */
	public int type = -1;

	/**
	 * 是否允许小数点(针对数字题选项)
	 */
	public boolean isFloat;

	/**
	 * 最大值
	 */
	public String maxNumber;
	/**
	 * 特殊值
	 */
	public String specialNumber;
	/**
	 * 最大值
	 */
	public String minNumber;
	

	/**
	 * 字典的id(针对字典选项)
	 */
	public String classid;

	/**
	 * @index.@item引用了某一道题的某一个选项进行比较
	 */
	public String titlefrom;

	/**
	 * 比较符号
	 */
	public String symbol;

	/**
	 * 是否启用拖动条
	 */
	public boolean dragChecked;
	/**
	 * 是否是单项必填 true就是 false就不是
	 */
	public boolean required;

	/**
	 * 哑题的引用选项
	 */
	public String dumbList;// ""空代表此题没哑题。"1,0:1"
							// 逗号前面的1代表引用哪个index,逗号后面代表引用那个index的哪个值。
	
	/**
	 * 条件隐藏选项
	 */
	public String hideList;
	
	public String dataDictionary;//数据字典串 1,2,3,4,5,6

	

	

	@Override
	public String toString() {
		return "QuestionItem [padding=" + padding + ", isHide=" + isHide + ", isOther=" + isOther + ", isDumbOk=" + isDumbOk + ", itemSize=" + itemSize + ", hide=" + hide + ", dateCheck=" + dateCheck
				+ ", itemValue=" + itemValue + ", itemText=" + itemText + ", itemTextRight=" + itemTextRight + ", leftsideWord=" + leftsideWord + ", rightsideWord=" + rightsideWord + ", exclude="
				+ exclude + ", isFreeInput=" + isFreeInput + ", caption=" + caption + ", caption_check=" + caption_check + ", deft=" + deft + ", weightValue=" + weightValue + ", dateSelect="
				+ dateSelect + ", scoreValue=" + scoreValue + ", isCheck=" + isCheck + ", type=" + type + ", isFloat=" + isFloat + ", maxNumber=" + maxNumber +", specialNumber=" + specialNumber+ ", minNumber=" + minNumber
				+ ", classid=" + classid + ", titlefrom=" + titlefrom + ", symbol=" + symbol + ", dragChecked=" + dragChecked + ", required=" + required + ", dumbList=" + dumbList
				+ ", dataDictionary=" + dataDictionary + "]";
	}

	public String getDumbList() {
		return dumbList;
	}

	public void setDumbList(String dumbList) {
		this.dumbList = dumbList;
	}

	public Integer getItemSize() {
		return itemSize;
	}

	public void setItemSize(Integer itemSize) {
		this.itemSize = itemSize;
	}

	public Integer getItemValue() {
		return itemValue;
	}

	public void setItemValue(Integer itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemText() {
		return itemText;
	}

	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	public String getLeftsideWord() {
		return leftsideWord;
	}

	public void setLeftsideWord(String leftsideWord) {
		this.leftsideWord = leftsideWord;
	}

	public String getRightsideWord() {
		return rightsideWord;
	}

	public void setRightsideWord(String rightsideWord) {
		this.rightsideWord = rightsideWord;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public int getDateCheck() {
		return dateCheck;
	}

	public void setDateCheck(int dateCheck) {
		this.dateCheck = dateCheck;
	}

	public int getIsFreeInput() {
		return isFreeInput;
	}

	public void setIsFreeInput(int isFreeInput) {
		this.isFreeInput = isFreeInput;
	}

	public int getIsOther() {
		return isOther;
	}

	public void setIsOther(int isOther) {
		this.isOther = isOther;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getDeft() {
		return deft;
	}

	public void setDeft(int deft) {
		this.deft = deft;
	}

	public int getWeightValue() {
		return weightValue;
	}

	public void setWeightValue(int weightValue) {
		this.weightValue = weightValue;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public Integer getDateSelect() {
		return dateSelect;
	}

	public void setDateSelect(Integer dateSelect) {
		this.dateSelect = dateSelect;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public boolean isFloat() {
		return isFloat;
	}

	public void setFloat(boolean isFloat) {
		this.isFloat = isFloat;
	}

	public String getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(String maxNumber) {
		this.maxNumber = maxNumber;
	}

	public String getspecialNumber() {
		return specialNumber;
		
	}

	public void setspecialNumber(String specialNumber) {
		this.specialNumber = specialNumber;
	}
	public String getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(String minNumber) {
		this.minNumber = minNumber;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getTitlefrom() {
		return titlefrom;
	}

	public void setTitlefrom(String titlefrom) {
		this.titlefrom = titlefrom;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setDateSelect(int dateSelect) {
		this.dateSelect = dateSelect;
	}

	public boolean isDragChecked() {
		return dragChecked;
	}

	public void setDragChecked(boolean dragChecked) {
		this.dragChecked = dragChecked;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getCaption_check() {
		return caption_check;
	}

	public void setCaption_check(int caption_check) {
		this.caption_check = caption_check;
	}

	public String getItemTextRight() {
		return itemTextRight;
	}

	public void setItemTextRight(String itemTextRight) {
		this.itemTextRight = itemTextRight;
	}
	
	

}
