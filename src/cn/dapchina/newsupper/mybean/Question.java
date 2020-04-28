package cn.dapchina.newsupper.mybean;

import java.io.Serializable;

import java.util.ArrayList;


public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2965102114024717789L;
	
	public Integer id;
	
	
	
	/**
	 * 调查的问卷号
	 */
	public String surveyId;
	
	/**
	 * 下标号
	 */
	public int qIndex;
	
	/**
	 * 顺序号
	 */
	public int qOrder;
	
	/**
	 * 题型int标识
	 */
	public int qType;
	
	/**
	 * 题型字符说明
	 */
	public String qTypeStr;
	
	/**
	 * 题目标题
	 */
	public String qTitle;
	
	/**
	 * 题目的控件是否可用
	 */
	public int qTitleDisable;
	
	/**
	 * 题目文本的位置
	 */
	public String qTitlePosition;
	
	/**
	 * 上方追加说明
	 */
	public String qComment;
	
	/**
	 * 题目表述性文字的说明的位置
	 */
	public String qCommentPosition;
	
	/**
	 * 启用评分
	 */
	public int qScoreChecked; 
	
	/**
	 * 启用权重
	 */
	public int qWeightChecked;
	
	/**
	 * 必答题
	 */
	public int qRequired;
	
	/**
	 * 问题选项的位置是随机的
	 */
	public int qRadomed;
	
	public int qDragChecked;
	
	/**
	 * 引用和排斥
	 */
	public String qInclusion;
	
	/**
	 * 排他
	 */
	public int qExclude; 
	
	/**
	 * 引用指定的题目, qSiteOption即等于qIndex的值
	 */
	public String qSiteOption;
	
	/**
	 * 预选项, 也就是问题加载的时候, 题目中的选项已经被选上了
	 */
	public int qPreSelect;
	/**
	 * 哑题 是否是哑题
	 */
	public int qDumbFlag;//1代表哑题   0代表不是哑题。
	
	/**
	 * 允许上传附件
	 */
	public int qAttach;
	
	/**
	 * 控件的宽度
	 */
	public int sizeWidth;
	
	/**
	 * 题目控件横向、纵向摆放
	 */
	public String deployment;
	
	/**
	 * 矩阵题目控件横向、纵向摆放
	 */
	public String qColumnsDirection;/** horizontal vertal **/
	
	public int ignoreFirstItem;
	
	/**
	 * 问题的上方追加说明
	 */
	public String qCaption;
	
	/**
	 * 问题前方的Qid
	 */
	public String qid;
	
	
	
	
	//<sort sumNumber="50" Symbol=">=" maxNumber="30" minNumber="10">figure</sort>
	public int freeTextColumn;
	
	public String freeSumNumber;//==>sumNumber="50"
	
	public String freeSymbol;//==>Symbol=">="
	
	public String freeTextSort;//==>figure
	
	public String freeMaxNumber;//==>maxNumber="30"
	
	public String freeMinNumber;//==>minNumber="10"
	
	public Integer qMatchQuestion;//最外层。1就是并且。其他就是或者
	
	/**
	 * 文本框或下拉列表或进度条的值不能重复
	 */
	public int freeNoRepeat;
	/**
	 * 连续性选择判断，只适用于单选矩阵、复选矩阵
	 */
	public int qContinuous;
	
	/**
	 * 文本域的行数
	 */
	public int textAreaRows;
	
	/**
	 * 
	 */
	public int rowsNum;
	
	/**
	 * 媒体文件的位置
	 */
	public String qMediaPosition;
	
	/**
	 * 媒体文件源
	 */
	public String qMediaSrc;
	
	/**
	 * 媒体文件宽度
	 */
	public int qMediaWidth;
	
	/**
	 * 媒体文件高度
	 */
	public int qMediaHeight;
	
	/**
	 * 分数
	 */
	public String qScore;
	
	/**
	 * 有没有其他项 1有, 0无
	 */
	public int haveOther;
	
	/**
	 * 最少下限
	 */
	public int lowerBound;
	
	/**
	 * 最大上限
	 */
	public int upperBound;
	
	/**
	 * 引用其它问题的答案
	 */
	private String resctItemStr;
	
	/**
	 * 引用某些题目的标题
	 * eg: titleFrom="1,3,5,7,9"
	 */
	private String titleFrom;
	
	/**
	 * 单题拍照是否本题有拍照，以及拍照的名字,
	 */
	public int qCamera;//是否是单题拍照,0不支持，1支持。
	public String qCameraName;//单题拍照的名字
	/**
	 * 单题签名是否有
	 */
	public int qSign;//是否是单题签名,0不支持，1支持。
	
	/**
	 * 当前题目的选项中有没有追加说明
	 */
	public int isHaveItemCap;
	
	
	/**
	 *   当前题目题外之和关联的 标志性说明  2,2,2  三个数字代表什么意思  ？？
	 *   题外关联    字段    
	 */
	public String qParentAssociatedCheck;
	
	
	private ArrayList<Integer> titleFromArr = new ArrayList<Integer>();
	
//	private ArrayList<Restriction> resctItemArr = new ArrayList<Restriction>();z1
	
	private String rowItemStr;
//	private ArrayList<QuestionItem> rowItemArr = new ArrayList<QuestionItem>();z1
	
	private String colItemStr;
//	private ArrayList<QuestionItem> colItemArr = new ArrayList<QuestionItem>();z1
	
	public int qLinkage;//三级联动字段 1代表是，0代表不是
	
	/**
	 * 干预
	 */
//	private Intervention intervention = new Intervention();z1
	
	private String interventionStr;
	
//	private QGroup group;z1
	
	private String groupStr;
	
	public String getResctItemStr() {
		return resctItemStr;
	}
	
	
	/**
	 * 设置逻辑集合
	 * @param resctItemStr
	 */
//	public void setResctItemStr(String resctItemStr) {z11
//		this.resctItemStr = resctItemStr;
//		if(!Util.isEmpty(resctItemStr)){
//			/**
//			 * 从xml文件解析出逻辑的集合
//			 * 然后转成json字符串
//			 * 目的存在数据库中备以后拿出来用
//			 */
//			ArrayList<Restriction> rs = XmlUtil.restrictionStr2JsonArr(resctItemStr);
//			if(!Util.isEmpty(rs)){
//				this.resctItemArr = rs;
//			}
//		}
//	}
//	
//	public ArrayList<Restriction> getResctItemArr() {
//		return resctItemArr;
//	}
//	
//	public void setResctItemArr(ArrayList<Restriction> resctItemArr) {
//		this.resctItemArr = resctItemArr;
//		if(!Util.isEmpty(resctItemArr)){
//			this.resctItemStr = XmlUtil.jsonArr2RestrictionStr(resctItemArr);
//		}
//	}z12
	
	public String getRowItemStr() {
		return rowItemStr;
	}
	
//	public void setRowItemStr(String rowItemStr) {z11
//		this.rowItemStr = rowItemStr;
//		if(!Util.isEmpty(rowItemStr)){
//			/**
//			 * 从数据库中取出json数组然后转成ArrayList集合在答题时使用
//			 */
//			ArrayList<QuestionItem> items = XmlUtil.str2JsonArr(rowItemStr);
//			if(!Util.isEmpty(items)){
//				this.rowItemArr = items;
//			}
//		}
//	}
//	
//	public ArrayList<QuestionItem> getRowItemArr() {
//		return rowItemArr;
//	}
//	
//	public void setRowItemArr(ArrayList<QuestionItem> rowItemArr) {
//		this.rowItemArr = rowItemArr;
//		if(!Util.isEmpty(rowItemArr)){
//			/**
//			 * 将XML中解析出来的题目的行集合转换成json字符串保存在本地数据中
//			 */
//			this.rowItemStr = XmlUtil.jsonArr2Str(rowItemArr);
//		}
//	}
//	
//	public void setColItemStr(String colItemStr) {
//		this.colItemStr = colItemStr;
//		if(!Util.isEmpty(colItemStr)){
//			/**
//			 * 将列的json数据转换成列集合
//			 * 通常作用于从数据库中取出列字符串然后转成ArrayList集合
//			 * 在答题的时候使用
//			 */
//			ArrayList<QuestionItem> items = XmlUtil.str2JsonArr(colItemStr);
//			if(!Util.isEmpty(items)){
//				this.colItemArr = items;
//			}
//		}
//	}
//	
//	public ArrayList<QuestionItem> getColItemArr() {
//		return colItemArr;
//	}
//	
//	/**
//	 * 列的集合
//	 * @param colItemArr
//	 */
//	public void setColItemArr(ArrayList<QuestionItem> colItemArr) {
//		this.colItemArr = colItemArr;
//		if(!Util.isEmpty(colItemArr)){
//			/**
//			 * 将列的集合转成JSON数组保存在colItemStr变量中
//			 * 通常用于从XML中解析然后插入/更新到数据库中
//			 */
//			this.colItemStr = XmlUtil.jsonArr2Str(colItemArr);
//		}
//	}z12
	
	public String getColItemStr() {
		return colItemStr;
	}
	
	public ArrayList<Integer> getTitleFromArr() {
		return titleFromArr;
	}
	
	public String getTitleFrom() {
		return titleFrom;
	}
	
//	public void setTitleFrom(String titleFrom) {z11
//		this.titleFrom = titleFrom;
//		titleFromArr.clear();
//		/**
//		 * 假如引用的标题index数组不为空,
//		 * 则将其按照","拆分再装入ArrayList集合
//		 */
//		if(!Util.isEmpty(titleFrom)){
//			String[] arr = titleFrom.split(",");
//			for (int i = 0; i < arr.length; i++) {
//				//引用受访者参数只保存数字    循环不是数字跳过
//				if(!Util.isFormat(arr[i], -1)){
//					continue;
//				}
//				if(!Util.isEmpty(arr[i])){
//					titleFromArr.add(Integer.parseInt(arr[i]));
//				}
//			}
//		}
//	}
//
//	public Intervention getIntervention() {
//		return intervention;
//	}
//
//	/**
//	 * 设置的同时将Intervention对象转换成Intervention的JSON字符串
//	 * @param intervention
//	 */
//	public void setIntervention(Intervention intervention) {
//		this.intervention = intervention;
//		if(null != intervention){
//			String iiStr = XmlUtil.parserIntervention2Json(intervention);
//			if(!Util.isEmpty(iiStr)){
//				this.interventionStr = iiStr;
//			}
//		}
//	}
//
//	public String getInterventionStr() {
//		return interventionStr;
//	}
//
//	/*
//	 * 将Intervention的JSON字符串转换成Intervention对象
//	 */
//	public void setInterventionStr(String interventionStr) {
//		this.interventionStr = interventionStr;
//		if(!Util.isEmpty(interventionStr)){
//			Intervention ii = XmlUtil.parserJson2Intervention(interventionStr);
//			if(null != ii){
//				this.intervention = ii;
//			}
//		}
//	}
//
//	public QGroup getGroup() {
//		return group;
//	}
//
//	public void setGroup(QGroup group) {
//		this.group = group;
//		groupStr=null;
//		if(null != group){
//			groupStr = XmlUtil.parserQGroup2Json(group);
//		}
//	}
//
//	public String getGroupStr() {
//		return groupStr;
//	}
//
//	public void setGroupStr(String groupStr) {
//		this.groupStr = groupStr;
//		group = null;
//		if(!Util.isEmpty(groupStr)){
//			group = XmlUtil.parserJson2QGroup(groupStr);
//		}
//	}z12

}
