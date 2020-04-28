package cn.dapchina.newsupper.util;

import android.util.Log;

public class LogUtil{

	/**
	 *   日志输出类   用于  平常经常 打印日志 方便  
	 */
	//  日志标志  
	private static final String TAG="zrl1";
	//  输出标志   name
	private static final String NAME="name:";
    //  输出标志   value
	private static final String VALUE="value:";
    //  输出标志   text 
	private static final String TEXT="text:";
	//  系统打印答案  输出格式  
	public static void printfLog(int printType,Object content){
		switch (printType) {
		case 0:
			Log.i(TAG, NAME+content);
			break;
		case 1:
			Log.i(TAG, VALUE+content);
			break; 
		case 2:
			Log.i(TAG, TEXT+content);
			break; 
		default:
			break;
		}
	}
	//   自定义 打印格式  
	public static void printfLog(String tag,Object content){
		Log.i(TAG, tag+content.toString());
	}
	public static void printfLog(Object content){
		
		Log.i(TAG, content.toString());
	}
	 
}
