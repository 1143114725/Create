package cn.dapchina.newsupper.global;

public class Cnt {

	private Cnt() {

	}

	public static final boolean D = true;
	
	public static int TITLE_HEIGHT = 0;// 标题高度
	public static int INS_HEIGHT = 0;//   内容高度
	public static int GRIVEW_COLUMN_NUMS = 2;// 列数

	/**
	 * 题型
	 */

	/**
	 * 单选
	 */
	public static final int TYPE_RADIO_BUTTON = 0;
	/**
	 * 复选
	 */
	public static final int TYPE_CHECK_BOX = 1;
	/**
	 * 下拉
	 */
	public static final int TYPE_DROP_DOWN_LIST = 5;
	/**
	 * 矩阵单选
	 */
	public static final int TYPE_MATRIX_RADIO_BUTTON = 6;
	/**
	 * 矩阵复选
	 */
	public static final int TYPE_MATRIX_CHECK_BOX = 7;
	/**
	 * 单行文本框
	 */
	public static final int TYPE_FREE_TEXT_BOX = 2;
	/**
	 * 多行文本框
	 */
	public static final int TYPE_FREE_TEXT_AREA = 3;
	/**
	 * 基本信息
	 */
	public static final int TYPE_HEADER = 4;

	/**
	 * 多媒体题型
	 */
	public static final int TYPE_MEDIA = 8;

	public static final String LOGIN_MODE = "offline";
	public static final String USER_ID = "userId";
	public static final String USER_PWD = "userPsd";
	public static final String USER_MAC = "mac";

	/**
	 * 
	 */
	public static final String HEADER = "Header";

	public static final String RADIO_BUTTON = "RadioButton";

	public static final String CHECK_BOX = "CheckBox";

	public static final String DROP_DOWN_LIST = "DropDownList";

	public static final String FREE_TEXT_BOX = "FreeTextBox";

	public static final String FREE_TEXT_AREA = "FreeTextArea";

	public static final String MATRIX_RADIO_BUTTON = "MatrixRadioButton";

	public static final String MATRIX_CHECK_BOX = "MatrixCheckBox";

	public static final String IMAGE = "Image";

	public static final String POS_CENTER = "center";
	public static final String POS_LEFT = "left";
	public static final String POS_RIGHT = "right";
	public static final String ORIENT_VERTICAL = "vertical";
	public static final String ORIENT_HORIZONTAL = "horizontal";

	/**
	 * 多媒体文件的下载路径
	 */
	public static final String URL_MEDIA_BASE = "";

	/**
	 * 选了文本框那一项,但是没填内容
	 */
	public static final int STATE_FAIL = -1;

	/**
	 * 答题成功
	 */
	public static final int STATE_SUCCESS = 100;

	public static final int STATE_ALL = 5;

	// public static final int STATE_SYB = 6;

	/**
	 * XML文件类型
	 */
	public static final int FILE_TYPE_XML = 1;

	/**
	 * PNG文件类型
	 */
	public static final int FILE_TYPE_PNG = 2;

	/**
	 * MP3文件类型
	 */
	public static final int FILE_TYPE_MP3 = 3;
	/**
	 * MP4文件类型 摄像
	 */

	public static final int FILE_TYPE_MP4 = 4;

	/**
	 * 问卷没有访问, 未访问
	 */
	public static final int VISIT_STATE_NOACCESS = -1;
	/**
	 * 中断待续
	 */
	public static final int VISIT_STATE_INTERRUPT = 0;
	/**
	 * 完成
	 */
	public static final int VISIT_STATE_COMPLETED = 1;

	/**
	 * 上传状态, 没动过
	 */
	public static final int UPLOAD_STATE_NOACCESS = -1;
	/**
	 * 没有上传过
	 */
	public static final int UPLOAD_STATE_UNUPLOAD = 0;

	/**
	 * 上传状态,已经传过
	 */
	public static final int UPLOAD_STATE_UPLOADED = 1;

	/**
	 * 1是访问专家 2是IPSOS 3是IMS 4是美国服务器 5是农业银行
	 */
	public static int appVersion = 1;

	// /**
	// * 免费地址
	// */
	public static String LOGIN_URL = "http://free.dapchina.cn/alisoft/OfflineLogin.asp";
	public static String SHOUQUAN_URL = "http://free.dapchina.cn/alisoft/OfflineEmpower.asp";
	public static String UPLOAD_URL = "http://free.dapchina.cn/alisoft/OfflineUpload.asp";
	public static String RECORD_PHOTO_URL = "223.112.24.66";
	// 下载试用问卷的地址
	public static String DOWN_FREE_SURVEY_URL = "http://free.dapchina.cn/alisoft/OfflineEmpower.asp";
	// 注册地址
	public static String REGIST_URL = "http://free.dapchina.cn/alisoft/OfflineRegister.asp";
	public static String INNER_URL = "http://free.dapchina.cn/alisoft/DownloadUser.asp";
	// 地图监控地址
	public static String POSITION_URL = "http://free.dapchina.cn/alisoft/OfflineAuthorMap.asp";
	// 监控用地址
	public static String MONITOR_URL = "http://free.dapchina.cn/alisoft/OfflineSurveyMonitor.asp";
	// 自定义logo功能地址
	public static String LOGO_URL = "http://free.dapchina.cn/alisoft/OfflineAppLogo.asp";
	// 自定义logo path功能
	public static String LOGO_URL_PATH = "http://free.dapchina.cn/";
	// 知识库地址
	public static String KNOWLEDGE_URL = "http://free.dapchina.cn/alisoft/OfflineKnowledge.asp";
	// 知识库前缀地址
	public static String KNOWLEDGE_ATTACH_URL = "http://free.dapchina.cn/upload/attach/";
	// 上传
	public static String DELETE_XML = "http://free.dapchina.cn/alisoft/OfflineMissing.asp";
	// 推送
	public static String SEND_URL = "http://free.dapchina.cn/alisoft/OfflineTuiSong.asp";
	// 重置 大树 1
	public static String RESET_URL = "http://free.dapchina.cn/alisoft/OfflineGetDelFeedList.asp";
	// 重置 大树 2
	public static String REDEAL_URL = "http://free.dapchina.cn/alisoft/OfflineReDelFeedList.asp";
	//请求是否更新项目
	public static String NOTICE_SURVEY = "http://free.dapchina.cn/alisoft/OfflineSingleEmpower.asp";
	//请求是否更新名单
	public static String NOTICE_INNER = "http://free.dapchina.cn/alisoft/DownLoadUserCountFlag.asp";
	//请求数据字典
	public static String DATA_URL = "http://free.dapchina.cn/alisoft/OfflineDictionaryList.asp";
	//配额
	public static String QUOTA_URL = "Http://IP.XX.XX.XX/alisoft/OfflineQuoteDown.asp";
	

	public static final String GOOGLE_PLAY_URL = "http://www.dapchina.cn/newsurvey/config/google_play.apk";
	public static final String GOOGLE_STORE_URL = "http://www.dapchina.cn/newsurvey/config/google_play_store.apk";
//	 public static String APP_URL="http://www.survey-expert.cn/newsurvey/config/appnew_dapsurvey.xml";
	 public static String APP_URL="http://www.survey-expert.cn/newsurvey/config/dapsurvey.xml";
	 
	 
	// IMSapp上线更新new
//	public static String APP_URL = "http://202.85.212.198/IMS/config/appnew_dapsurvey.xml";
	 //IPSOS 上线更新提示
//	public static String APP_URL = "http://www.ipsoschina.cn/config/appnew_dapsurvey.xml";
	//美国服务器 上线更新提示
//	public static String APP_URL = "https://www.ipsoschina.cn/config/appnew_dapsurvey.xml";
	public static int PORT = 7878;

	/**
	 * 美国改变域名的方法
	 * 
	 * @param isFree
	 *            是否是免费付费
	 * @param ip
	 *            域名
	 */
	// public static void changeNewURL(boolean isFree, String ip, String freeip,
	// String payip) {
	// String[] split = ip.split("/");
	// String path = split[0];
	// // 共用
	// LOGIN_URL = "https://" + ip + "/alisoft/OfflineLogin.asp";
	// SHOUQUAN_URL = "https://" + ip + "/alisoft/OfflineEmpower.asp";
	// UPLOAD_URL = "https://" + ip + "/alisoft/OfflineUpload.asp";
	// INNER_URL = "https://" + ip + "/alisoft/DownloadUser.asp";
	// //地图监控
	// POSITION_URL="https://" + ip + "/alisoft/OfflineAuthorMap.asp";
	// //监控用
	// MONITOR_URL="https://" + ip + "/alisoft/OfflineSurveyMonitor.asp";
	// RECORD_PHOTO_URL = path;
	// // 免费
	// REGIST_URL = "https://" + freeip + "/alisoft/OfflineRegister.asp";
	// DOWN_FREE_SURVEY_URL = "https://" + freeip +
	// "/alisoft/OfflineEmpower.asp";
	// // 付费
	// APP_URL = "https://" + payip + "/config/app_dapsurvey.xml";
	// //自定义logo
	// LOGO_URL="https://" + ip + "/alisoft/OfflineAppLogo.asp";
	// LOGO_URL_PATH="https://" + ip ;
	// String[] split2 = payip.split("/");
	// String payPath = split2[0];
	// CLIENT_SERVER_IP = payPath;
	// KNOWLEDGE_URL="https://" + ip + "/alisoft/OfflineKnowledge.asp";
	// //知识库前缀地址
	// KNOWLEDGE_ATTACH_URL="https://" + ip + "/upload/attach/";
	// //推送
	// SEND_URL = "https://" + ip + "/alisoft/OfflineTuiSong.asp";
	//
	//
	// if (isFree) {
	// // 下载试用问卷的地址
	// DOWN_FREE_SURVEY_URL = "https://" + ip + "/alisoft/OfflineEmpower.asp";
	// REGIST_URL = "https://" + ip + "/alisoft/OfflineRegister.asp";
	// // LOGIN_URL = "http://free.dapchina.cn/alisoft/OfflineLogin.asp";
	// // SHOUQUAN_URL =
	// // "http://free.dapchina.cn/alisoft/OfflineEmpower.asp";
	// // UPLOAD_URL = "http://free.dapchina.cn/alisoft/OfflineUpload.asp";
	// // RECORD_PHOTO_URL = "free.dapchina.cn";
	//
	// } else {
	// // LOGIN_URL =
	// // "http://www.dapchina.cn/newsurvey/alisoft/OfflineLogin.asp";
	// // SHOUQUAN_URL =
	// // "http://www.dapchina.cn/newsurvey/alisoft/OfflineEmpower.asp";
	// // UPLOAD_URL =
	// // "http://www.dapchina.cn/newsurvey/alisoft/OfflineUpload.asp";
	// // RECORD_PHOTO_URL = "www.dapchina.cn";
	// // INNER_URL = "http://" + ip + "/alisoft/DownloadUser1.asp";
	// // INNER_URL =
	// // "http://www.dapchina.cn/newsurvey/alisoft/DownloadUser1.asp";
	// }
	// }

	/**
	 * 改变域名的方法
	 * 
	 * @param isFree
	 *            是否是免费付费
	 * @param ip
	 *            域名
	 */
	public static void changeNewURL(boolean isFree, String ip, String freeip, String payip) {
		String[] split = ip.split("/");
		String path = split[0];
		if(-1!=split[0].indexOf(":")){
			path = (split[0].split(":"))[0];}
		String protocol="http://";
		//兼容美国服务器并且是付费，否则还是走http
		if(4==Cnt.appVersion){
			protocol="https://";
		}
		// 共用
		LOGIN_URL = protocol + ip + "/alisoft/OfflineLogin.asp";
		SHOUQUAN_URL = protocol + ip + "/alisoft/OfflineEmpower.asp";
		UPLOAD_URL = protocol + ip + "/alisoft/OfflineUpload.asp";//测试修改
		INNER_URL = protocol + ip + "/alisoft/DownloadUser.asp";
		// 地图监控
		POSITION_URL = protocol + ip + "/alisoft/OfflineAuthorMap.asp";
		// 监控用
		MONITOR_URL = protocol + ip + "/alisoft/OfflineSurveyMonitor.asp";
		//删除附件
		DELETE_XML=protocol + ip + "/alisoft/OfflineMissing.asp";
		RECORD_PHOTO_URL = path;
		// // 免费
		// REGIST_URL = protocol + freeip + "/alisoft/OfflineRegister.asp";
		DOWN_FREE_SURVEY_URL = protocol + freeip + "/alisoft/OfflineEmpower.asp";
		// 付费
		// app上线更新
		APP_URL = protocol + payip + "/config/dapsurvey.xml";
//		APP_URL = protocol + payip + "/config/appnew_dapsurvey.xml";
		
		
		
		
		
		// app上线更新结束
		// 自定义logo
		LOGO_URL = protocol + ip + "/alisoft/OfflineAppLogo.asp";
		LOGO_URL_PATH = protocol + ip;
		String[] split2 = payip.split("/");
		String payPath = split2[0];
		if(-1!=split2[0].indexOf(":")){
		 payPath = (split2[0].split(":"))[0];}
		// 监控走的都是付费版的
		CLIENT_SERVER_IP = payPath;
		KNOWLEDGE_URL = protocol + ip + "/alisoft/OfflineKnowledge.asp";
		// 知识库前缀地址
		KNOWLEDGE_ATTACH_URL = protocol + ip + "/upload/attach/";
		// 推送
		SEND_URL = protocol + ip + "/alisoft/OfflineTuiSong.asp";
		// 注册
		REGIST_URL = protocol + ip + "/alisoft/OfflineRegister.asp";
		// 重置 获取要重置
		RESET_URL = protocol + ip + "/alisoft/OfflineGetDelFeedList.asp";
		// 重置 后改变状态
		REDEAL_URL = protocol + ip + "/alisoft/OfflineReDelFeedList.asp";
		
		
		//请求是否更新项目
		NOTICE_SURVEY  =  protocol + ip + "/alisoft/OfflineSingleEmpower.asp";
		//请求是否更新名单
		NOTICE_INNER = protocol + ip + "/alisoft/DownLoadUserCountFlag.asp";
		//数据字典
		DATA_URL= protocol + ip + "/alisoft/OfflineDictionaryList.asp";
		//配额
		QUOTA_URL=protocol + ip + "/alisoft/OfflineQuoteDown.asp";
		if (isFree) {
			// 下载试用问卷的地址
			DOWN_FREE_SURVEY_URL = protocol + ip + "/alisoft/OfflineEmpower.asp";

		} else {

		}
	}

	public static String CLIENT_SERVER_IP = "";
	// public static String CLIENT_SERVER_IP = "www.dapchina.cn";
	public static final int CLIENT_SERVER_PORT = 6868;

	// ftp用户名 密码 和端口
	public static final String ftpName = "newupload";
	public static final String ftpPwd = "dapchina";
	public static final int ftpPort = 18;

}
