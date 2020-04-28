package cn.dapchina.newsupper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 此工具类用于存放
 * 数据库是否创建及是否写入了数据
 * 保存桌面快捷图标是否创建
 * 保存通知图标是否创建
 * 保存上一次的城市ID号
 */
public class Config {
	private SharedPreferences sp = null;
	private Editor et = null;
	public Config(Context context){
		/**此对象只需实例化一次即可**/
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		et = sp.edit();
	}
	
	/**
	 * 获取Boolean值
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key){
		return sp.getBoolean(key, false);
	}
	
	/**
	 * 获取Boolean值
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key, boolean value){
		return sp.getBoolean(key, false);
	}
	
	/**
	 * 获取到了则返回响应的值
	 * 没有则返回null
	 * @param key
	 * @return
	 */
	public String getString(String key, String value){
		return sp.getString(key, value);
	}
	
	/**
	 * 向xml中写入字符串
	 * @param key
	 */
	public void putString(String key, String value){
		et.putString(key, value);
		et.commit();
	}
	
	/**
	 * 向xml中写入boolean
	 * @param key
	 * @param value
	 */
	public void putBoolean(String key, boolean value){
		et.putBoolean(key, value);
		et.commit();
	}
	
	/**
	 * 向xml中写入int
	 * @param key
	 * @param value
	 */
	public void putInt(String key, int value){
		et.putInt(key, value);
		et.commit();
	}
	
	public int getInt(String key, int defValue){
		return sp.getInt(key, defValue);
	}
	/**
	 * 提交Editor
	 */
	public void commit(){
		et.commit();
	}
}
