package cn.dapchina.newsupper.global;

public class TaskType {

	/**
	 * 授权
	 */
	public static final int TS_AUTHOR = 1;
	/**
	 * 在线登录
	 */
	public static final int TS_ONLINE_LOGIN = 2;
	
	/**
	 * 获取应用升级的信息
	 */
	public static final int TS_GET_APP = 3;
	
	public static final int TS_LOGIN_GET_APP = 4;
	
	/**
	 * 获取注册免费帐号常量
	 */
	public static final int TS_REGIST = 5;
	
	/**
	 * 自定义logo功能 常量
	 */
	public static final int TS_GET_LOGO = 6;
	/**
	 * 自定义下载知识库功能
	 */
	public static final int TS_GET_KNOW = 7;
	
	public static final int TS_NOTICE_SURVEY=14;
	public static final int TS_NOTICE_INNER=15;
	public static final int TS_NOTICE_QUOTA=17;
	
	
	

	/**
	 *   大树   注册 付费账号  常量  
	 */
	public static final int TS_PAY_REGIST=8;
	
	/**
	 *   大树  登陆  免费账号   常量   
	 */
	public static final int TS_FREE_LOGIN=9;
	
	/**
	 *   大树   知识库下载  附件失败    常量
	 */
	public static final int TS_GET_KNOWFILE_FAIL=10;
	
	/**
	 *   大树    重置功能   
	 */
	public static final int TS_RESET = 11;
	/**
	 *   大树    重置功能  从服务器读取返回码 验证  是否重置成功  
	 */
	public static final int TS_REDEAL= 12;
	/**
	 *   大树   录音功能    全局录音  	
	 */
	public static final int TS_RECORED=13;
	/**
	 * 配额
	 */
	public static final int TS_GET_QUOTA=16;
}
