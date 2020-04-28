package cn.dapchina.newsupper.util;
import java.util.ArrayList;

import android.util.Log;

import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.AnswerMap;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.global.MyApp;
public class ImsIntervetion {

	public static final int IMS_PI_YAN_SHI_ZHEN=139; 
	private int surveyId;
	private MyApp ma;
	private String uuid;
	public ImsIntervetion(int surveyId, MyApp ma, String uuid) {
		super();
		this.surveyId = surveyId;
		this.ma = ma;
		this.uuid = uuid;
	}
	public int getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	/**
	 * 判断是否是 干预    如果 V3  V4  V5 没有答案  ，那么  V6 出现  否则  隐藏  V6   
	 * @return
	 */
	public static boolean isSkipV6(ImsIntervetion ims,Question q,int qIndex){
		boolean b=false; 
		int surveyId=0;  
		if (isIMS(ims, q, qIndex)) {
			surveyId=Integer.valueOf(ims.getSurveyId());  
		}
		switch (surveyId) {
		case IMS_PI_YAN_SHI_ZHEN:
			switch (qIndex) {
			case 48:  //  V6 ==48   v3  ==45  V4== 46   V5== 47
				if (ims.getAnswerList("45", 0).size()==0 && 
				ims.getAnswerList("46", 0).size()==0 && 
				ims.getAnswerList("47", 0).size()==0) {
					
				}else {
					b=true;  
				}
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
		return b;  
	}
	/**
	 *   判断是否   IMS问卷  
	 */
	public static boolean isIMS(ImsIntervetion ims,Question q,int qIndex){
		boolean b=false; 
		if (ims!=null && q!=null) {
			
		}else {
			return b;  
		}
		Log.i("zrl1", ims.getSurveyId()+"id"); 
		if (ims.getSurveyId()==IMS_PI_YAN_SHI_ZHEN) {
			Log.i("zrl1", q.qIndex+"index:"); 
			if (q.qIndex==qIndex) {
				b =true;  
			}
		}
		return b; 
	}
	/**
	 *   获取  答案  
	 */
	public  ArrayList<String> getAnswerList(String qIndex, int type)
	{
		ArrayList<String> answerList = new ArrayList<String>();
		Answer an = ma.dbService.getAnswer(uuid, qIndex);
		if (an != null && an.getAnswerMapArr().size() > 0)
		{
			for (AnswerMap map : an.getAnswerMapArr())
			{
				switch (type)
				{
				case 0:
					// value
					answerList.add(map.getAnswerValue().trim());
					break;
				case 1:
					// text
					answerList.add(map.getAnswerText().trim());
					break;
				case 2:
					// name
					answerList.add(map.getAnswerName().trim());
					break;
				case 3:
					// 临时干预 三潭印月 展后 最后 部分 625 3-5 分的记录
					if (map.getCol() >= 2 && map.getCol() <= 4)
					{
						answerList.add(map.getRow() + "");
					}
					break;
				}
			}
		}
		return answerList;
	}
	 
	 /**
	  *   如果S4a选择了3或者4，V1.3不能选择1，要提示被访者逻辑错误，不能进入下一题。
	  *   
	  *   10    5     
	  */
	public  boolean isSkipNext(ImsIntervetion ims,Question q,
			int qIndex,ArrayList<AnswerMap> answerMaps){
		boolean b=false; 
		boolean c=false; 
//		StringBuilder sbBuilder=new StringBuilder();  
		if (isIMS(ims, q, qIndex)) {
			Log.i("zrl1", answerMaps.get(0).getAnswerValue()+"value:"); 
			if (ims.getAnswerList("10", 0).size()>0) {
				Answer an = ma.dbService.getAnswer(uuid,"10"); 
				if (an != null && an.getAnswerMapArr().size() > 0)
				{
					for (AnswerMap map : an.getAnswerMapArr())
					{
						Log.i("zrl1", "value1:"+map.getAnswerValue()); 
						if (map.getAnswerName().split("_")[3].equals("2") || map.getAnswerName().split("_")[3].equals("3")) {
							c=true;  
						}
//						sbBuilder.append(map.getAnswerValue()); 
					}
				}
			}
		}
		if (c) {
			if (!Util.isEmpty(answerMaps)) {
				for (AnswerMap map : answerMaps) {
					Log.i("zrl1", map.getAnswerValue()+"value:"); 
					if (map.getAnswerValue().equals("0")) {
						b=true; 
						break; 
					}
				}
			}
		}
//		if (!sbBuilder.toString().equals("")) {
//			Log.i("zrl1", sbBuilder.toString()); 
//			if ((sbBuilder.toString().contains("2")) || (sbBuilder.toString().contains("3"))) {
//				Log.i("zrl1", "走这里！"); 
//				if (!Util.isEmpty(answerMaps)) {
//					for (AnswerMap map : answerMaps) {
//						Log.i("zrl1", map.getAnswerValue()+"value:"); 
//						if (map.getAnswerValue().equals("0")) {
//							b=true; 
//							break; 
//						}
//					}
//				}
//			}
//		}
	
		return b;  
	}
	
	
	 
}
