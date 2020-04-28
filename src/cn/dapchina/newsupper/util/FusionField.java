package cn.dapchina.newsupper.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;



import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.media.MediaPlayer;
import android.net.NetworkInfo;
import android.telephony.SignalStrength;
import android.view.KeyEvent;





public class FusionField
{
	/**
	 * 保存当前的Activity
	 */
	public static Activity currentActivity = null;
	
	public static Activity videoActivity = null;

	/**
	 * 保存当前的context
	 */
	public static Context currentContext = null;
	
	/**
	 * 保存activity的状态，用于捕捉HOME键能使得程序到后台在N分钟后会退出
	 */
	public static AbstractMap<Activity, String> activityStatus = new ConcurrentHashMap<Activity, String>();

	/**
	 * 后台退出时间间隔
	 */
	public static int iDelayShutdown = 300000;
	
	//add  by zhangkun 2011.9.20 begin
	/**
	 * launcher页面未读信息总数
	 */
	public static int launcherUnreadMsgNum= 0;
	//add  by zhangkun 2011.9.20 end
	
	/**
	 * 圈子未读消息总数
	 */
	public static int groupUnreadMsgNum = 0;
	
	/**
	 * 当前聊天对象（单聊、圈子、群发）的JID
	 */
	public static String currentChatWith = null;

	/**
	 * 当前是否存在聊天界面的Activity在栈中（由聊天界面进入其他页面时）：0不存在聊天界面，1存在单聊界面，2存在群聊界面
	 */
	public static int activityInStack = 0;
	
	/**
	 * 当前页面：0不在聊天界面，1在单聊界面，2在群聊界面
	 */
	public static int currentPage = 0;
	
	/**
	 * 保存群聊页面的对象（此变量其他人勿扰……切忌）
	 */
	public static Activity groupActivity = null;

	/**
	 * 保存当前所有活动中activity
	 */
	public static Vector<Activity> mActivityList = new Vector<Activity>();

	/**
	 * 广播信息
	 */
	public static final String Setting_Broadcast = "Setting_Broadcast";

	/**
	 * 屏幕分辨率
	 */
	public static float currentDensity;
	public static int currentDensityDpi;
	public static int currentWidthPixels;
	public static int currentHeightPixels;
	public static String currentscreenSize = null;

	/**
	 * 保存当前notification是那个单聊或者群聊的ID
	 */
	public static String notificationTag = null;
	/**
	 * 手机号
	 */
	public static String ACCOUNT = "";

	/**
	 * 验证码
	 */
	public static String PASSWORD = "";

	/**
	 * 用户名
	 */
	public static String NAME = "";

//	/**
//	 * 用于保存跳转记录的list
//	 */
//	public static ArrayList<Activity> activityList = new ArrayList<Activity>();

	/**
	 * 用来保存应用是否在后台运行
	 */
	public static boolean isBackgroundRun = false;
	
	
	/**
	 * 公开资设置电话是否公开: 1是公开;0是未公开
	 */
	public static String isOpenPhone = "-1";
	/**
	 * 公开资设置邮箱是否公开
	 */
	public static String isOpenEmail = "-1";
	/**
	 * 搜索设置是否启用
	 */
	public static String isOpenSearchable = "-1";

	/**
	 * 是否是更新名片
	 */
	public static boolean isUpdateCard=false;

	/**
	 * 用户的hotalk的sid
	 */
	public static String HOTALK_SID = null;
	/**
	 * 服务器下发的base64 编码过的 sid=dlfjslfjssdfsdf&uid=5120123
	 */
	public static String HOTALK_BASE64_SID_UID = null;
	
	/**
	 * 自建Hotalk多媒体服务器验证码
	 */
	public static String HOTALK_MCODE = null;
	
	//hkw 2011.10.26  统一账号支持 start
	public static String HOTALK_SERVICE_TOKEN = null;
	public static String HOTALK_BASE64_SERVICE_TOKEN = null;
	public static String accountName = null;
	public static String accountType = null;	
	//hkw 2011.10.26  统一账号支持 end

	/**
	 * 用户的hotalk的jid
	 */
	public static String HOTALK_JID = null;

	/**
	 * 用户的hotalk的id
	 */
	public static String HOTALK_ID = null;
	
	/**
	 * PUSH功能使用的Device Token
	 */
	public static String DEVICE_TOKEN = null;

	public static String HOTALK_CT = "0";

	public static String HOTALK_ROSTER_VER = "";
	
	/**
	 * 好友VCard版本号最大值
	 */
	public static String HOTALK_ROSTER_VCARD_MAX_VER = null;
	
	/**
	 * 获取服务的最大数目
	 */
	public static int SERVICE_AVAILABLE_MAXNUM = 200;

	/**
	 * 设置对话框的按键事件
	 */
	public static OnKeyListener onKeyListener = new OnKeyListener()
	{
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
		{
			if (keyCode == KeyEvent.KEYCODE_BACK
					|| keyCode == KeyEvent.KEYCODE_SEARCH)
			{
				return true;
			}
			return false;
		}
	};

	/**
	 * 短信签名的最大长度
	 */
	public static int SIGNATURE_MAX_LEN = 20;
	
	/**
	 * 下面定义是的程序中用到的dialog的id
	 */

	/**
	 * 登录时的等待框 
	 */
//	public final static int DIA_LOGIN_WAIT = 0;
//	public final static int DIA_REGISTER_WAIT = 1;
	public final static int DIA_LOADINDFRIEND_WAIT = 2;
	// add by xiao. 20110823. begin
	public final static int DIA_LOADINDCHATLOG_WAIT = 3;
	public final static int DIA_ADDFRIEND_WAIT = 4;
	public final static int DIA_INITGROUPMEMBERS_WAIT = 5;
	// add by xiao. 20110823. end
	public final static int DIA_SENDING_VERIFY_WAIT = 6;//kang.chen 发送验证短信的等待框
	
	public final static int DIA_REGISTER_AUTO_WAIT = 7;//gaogy 收到验证短信自动验证
	
	// 加载联系人提示
	public final static int DIA_LOADING_CONTACTS_WAIT = 8;
	
	// 创建群发提示
	public final static int DIA_CREATE_BROADCASTCHAT_WAIT = 9;
	
	public static String verifyCode;//gaogy 用于自动验证进度框提示
	
	/**
	 * 全局唯一定时器
	 */
	public static Timer timer = new Timer();
	
	/**
	 * 返回码
	 */
	public static String NSP_STATUS = "NSP_STATUS";

	/**
	 * 网络超时时间
	 */
	public final static int MAX_CONNECTION_TIMEOUT = 30000;

	/**
	 * 初始化客户端的Secret值
	 */
	//	public static String nspSecret = "d72893b0c15aa78bf5658d89b368452c";
	public static String nspSecret = "563175cbf74b2a1f8238740682998669";
	
	//gaogy 8-23 崩溃日志上传 start
	/**
	 * 用于崩溃日志上传的系统sid
	 */
	public static String sysSid = "YuT85NwuuNZzuuCG45ouTWgm-WTAwvQgSW0w9rR7QU-4G42Z";
	
	public static String SN = "android 2.1";

	/**
	 * 成功返回列表
	 */
	public static String SUCCESSLIST = "successList";

	/**
	 * 失败返回列表
	 */
	public static String FAILLIST = "failList";

	public static final String CONNECTION_AGREENMENT = "http://";

	/**
	 * 下载管理器中最大可同时下载的任务数
	 */
	public static int maxDownloadCount = 3;

	/**
	 * sd卡是否存在的标志
	 */
	public static boolean sdCardIsExist = true;

	/**
	 * 群消息提醒hashmap，保存每个群消息开关
	 * 默认情况下为open
	 */
	public static ArrayList<String> GroupChatNotificationList = new ArrayList<String>();

	public static Activity groupChat;

	public static final int FRIEND_TYPE_NONE = 1;
	public static final int FRIEND_TYPE_FROM = 2;
	public static final int FRIEND_TYPE_TO = 3;
	public static final int FRIEND_TYPE_BOTH = 4;
	public static final int FRIEND_TYPE_REMOVE = 5;

	public static final int MAPSIZE = 280;
	//获取缩略图使用，缩略图的宽度设置为280，高度=下面参数乘以280,此参数=默认图片高度/默认图片宽度
	public static final float MAP_HEIGHT_DIV_WIDTH = 0.5893f;
	public static final int MAPZOOM = 12;
	public static final int LATLONSIZE = 9;

	public static final String SYSTEMLANGUAGE = Locale.getDefault()
			.getLanguage();

	public static String VERSIONNAME = null;

	/**
	 * 升级包下载存储地址
	 */
	public static final String UPDATE_VERSION_SAVEPATH = "/sdcard/hotalk/update/";

	/**
	 * 邮箱匹配正则表达式
	 */
	public static final String EMAIL_MATCH = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*"
			+ "+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";

	public static String UPDATE_FILE_NAME = "Hotalk.apk";
	public static String UPDATE_PUSH_NAME = "Hotalk.apk";

	/**
	 * 默认屏幕密度
	 */
	public static final float DEFAULT_DENSITY = 1.0f;

	/**
	 * 高屏幕密度
	 */
	public static final float HIGH_DENSITY = 1.5f;
	
	/**
	 * 640 * 960分辨率
	 */
	public static final float HIGH_BIG_DENSITY = 2.0f;
	
	/**
	 * 低屏幕密度
	 */
	public static final float LOW_DENSITY = 0.75f;	
	public static int bitmapSize_Vcard_hbig = 164;
	public static int bitmapSize_FriendList_hbig = 96;
	public static int bitmapSize_Zoom_hbig = 320;
	public static int bitmapSize_Image_hbig = 960;	
	public static int bitmapSize_Member_hbig = 112;
	
	public static int chat_Image_mh_hbig = 276;
	public static int chat_Image_mw_hbig = 208;
	public static int chat_Image_min_h_hbig = 192;
	public static int chat_Image_min_w_hbig = 272;
	public static int chat_Image_forward_h_hbig = 54;
	public static int chat_Image_forward_w_hbig = 116;
	public static int chat_fomulat_alter_position_min_hbig = 82; 
	public static int chat_fomulat_alter_position_max_hbig = 154;
	public static int chat_Slide_subject_w_hbig = 12;
	public static int chat_Image_Bubble_h_hbig = 13;
	
	public static int bitmapSize_Vcard_h = 126;
	public static int bitmapSize_FriendList_h = 64;
	public static int bitmapSize_Zoom_h = 240;
	public static int bitmapSize_Image_h = 800;	
	public static int bitmapSize_Member_h = 84;
	
	public static int chat_Image_mh_h = 208;
	public static int chat_Image_mw_h = 157;
	public static int chat_Image_min_h_h = 145;
	public static int chat_Image_min_w_h = 205;
	public static int chat_Image_forward_h_h = 41;
	public static int chat_Image_forward_w_h = 87;
	public static int chat_fomulat_alter_position_min_h = 62; 
	public static int chat_fomulat_alter_position_max_h = 116;
	public static int chat_Slide_subject_w_h = 9;
	public static int chat_Image_Bubble_h_h = 10;
 
	public static int bitmapSize_Vcard_m = 82;
	public static int bitmapSize_FriendList_m = 48;
	public static int bitmapSize_Zoom_m = 160;
	public static int bitmapSize_Image_m = 480;	
	public static int bitmapSize_Member_m = 56;
	
	public static int chat_Image_mh_m = 138;
	public static int chat_Image_mw_m = 104;
	public static int chat_Image_min_h_m = 96;
	public static int chat_Image_min_w_m = 136;
	public static int chat_Image_forward_h_m = 27;
	public static int chat_Image_forward_w_m = 58;
	public static int chat_fomulat_alter_position_min_m = 43; 
	public static int chat_fomulat_alter_position_max_m = 77;
	public static int chat_Slide_subject_w_m = 6;
	public static int chat_Image_Bubble_h_m = 7;
	
	public static int bitmapSize_Vcard_l = 59;
	public static int bitmapSize_FriendList_l = 34;
	public static int bitmapSize_Zoom_l = 80;
	public static int bitmapSize_Image_l = 320;	
	
	public static int chat_Image_mh_l = 106;
	public static int chat_Image_mw_l = 80;
	public static int chat_Image_min_h_l = 74;
	public static int chat_Image_min_w_l = 105;
	public static int chat_Image_forward_h_l = 20;
	public static int chat_Image_forward_w_l = 43;
	public static int bitmapSize_Member_l = 42;
	public static int chat_fomulat_alter_position_min_l = 33; 
	public static int chat_fomulat_alter_position_max_l = 59;
	public static int chat_Slide_subject_w_l = 5;
	public static int chat_Image_Bubble_h_l = 4;

	public static int ImageSize_Vcard = 59;
	public static int ImageSize_Icon = 34;
	public static int ImageSize_Zoom = 80;
	public static int bitmapSize_Image = 320;
	public static int ImageSize_Member = 84;
	/**
	 * 会话介面的图片高度和宽度的设置
	 */
	public static int chat_Image_min_h = 145;
	public static int chat_Image_min_w = 205;
	public static int chat_Image_mh = 208;
	public static int chat_Image_mw = 157;
	/**
	 * 切换模式成功提示框位置
	 */
	public static int chat_fomulat_alter_position_min = 62;
	
	public static int chat_fomulat_alter_position_max = 116;
	/**
	 * 会话介面的图片底部显示设置转发条条
	 */
	public static int chat_Image_forward_h = 41;
	public static int chat_Image_forward_w = 87;
	
	/**
	 * 会话介面的图片转换成气泡样式设置
	 * h:三角形的高
	 */
	public static int chat_Image_Bubble_h = 10;
	
	/**
	 * 彩信主题图片宽度和低图相差的值
	 */
	public static int chat_Slide_subject_w = 9;
	
	public static String HOTALK_VERSION = null;
	
	public static String HOTALK_FILE_SIZE = null;

	public static String HOTALK_DESCRIPTION = null;
	
	/**
	 * 音频播放器
	 */
	public static MediaPlayer mp = new MediaPlayer();
	
	/**
	 * 处理多终端
	 */
	public static HashMap<String, String> jidMap = new HashMap<String, String>();
	
	public static boolean isConnectionOK = false;
	
	public static boolean MaybeFirstTime = true;
	
	//gaogy 8-17超时时间由10秒改为30秒 start
	public static int SYSTEM_TIME_OUT = 30000;
	//gaogy 8-17超时时间由10秒改为30秒 end
	
	public static String SESSIONID = null;
	
	public static String SEQUNCE = "0";
	
	/**
	 * 单聊离线消息是否通知
	 */
	public static boolean isSingleDelayNotification = true;
	/**
	 * 群聊离线消息是否通知
	 */
	public static boolean isGroupDelayNotification = true;

	/**
	 * 新建消息等页面跳转时携带的对象集合
	 * wei.han
	 */


	/**
     * sms的provider地址
     */
    public static String SMS_PROVIDER_URI = "content://sms/";
    
    public static String MMS_PROVIDER_URI = "content://mms/";
    
    /**
     * sms的的会话
     */
    public static String SMS_THREAD_URI = "content://sms/conversations";
    
    /**
     * mms-sms的会话
     */
    public static String MMS_SMS_THREAD_URI = "content://mms-sms/conversations?simple=true";
    
    /**
     * mms-sms会话对应的手机号码
     */
    public static String SMS_PHONE_URI = "content://mms-sms/canonical-addresses";

	// add by xiaojie.huang 2001.9.13
	public static boolean isPhoneInService = false;
	// end by xiaojie.huang 2001.9.13

	/**
	 * 最后一条短信的ID
	 */
	public static int smsLastId = 0;
	
	/**
	 * 最后一条彩信ID
	 */
	public static int mmsLastId = 0;
	
	/**
	 * 当前会话对应的手机号码
	 */
	public static String smsPhoneNumber = "";
	
	/**
	 * 标志网络是否连接上
	 */
	public static boolean isNetworkConntected = false;
	
	/**
	 * 保存多媒体上传与dbank鉴权的返回结果。
	 */
	public static String dbank_upload_host = null;
	public static String dbank_upload_secret = null;
	public static String dbank_upload_nsp_tstr = null;
	
	/**
	 * 用于判断手机是否支持google和高德地图，用于两种地图切换
	 */
	public static boolean isSupportAllMap = false;
	
	/**
	 * 记录下次点击地图按钮，使用哪种类型地图
	 * 0  第一次进入地图
	 * 1  使用google地图
	 * 2  使用高德地图
	 */
	public static int nextUseMap = 0;
	/**
	 * 是否是拍照的图片
	 */
	public static String isphotograph_key="imageisphotograph"; 

	public static  final String refreshQuickReplyBroadcast = "com.hotalk.ui.chat.singleChat.QuickReplyActivity";
	
	/**
	 * message接收Action
	 */
	public static String INTENT_ACTION_MESSAGE = "com.hotalk.server.receiver.MessageReceiver";
	
	public static boolean IsDeleteChat = false;

	/**
	 *  当token无效时，服务器返回的失败信息为
     *  <failure xmlns="urn:hotalk:params:xml:ns:xmpp-sasl"><invalid-servicetoken/></failure> 
	 */
	public static String AUTH_INVALID_SERVICETOKEN_TAG = "invalid-servicetoken";
	
	/**
	 * 当前彩信apn是否开启 wei.han
	 */
	public static NetworkInfo mmsInfo = null;
	
	/**
	 * 用来记录手机信号强度 wei.han
	 */
	public static SignalStrength mSignalStrength = null;
	
	/**
	 * 判断是否显示快速回复框
	 */
	public static boolean isReplyAppear = true;
	
	/**
	 * 启动时，进行自动校验登录最大次数控制在5次，防止不支持上行静默注册的用户一直不验证
	 * 而导致每次都要提示验证
	 */
	public static final int AUTO_REGIST_MAX_TIMES = 5;
	
	/**
     * 用户nickname没有自定义时，提示用户设置nickname，限制提示次数最多2次
     */
    public static final int NOTIFY_SET_NICKNAME_MAX_TIMES = 2;
    
    /**
     * 提示用户升级客户端，限制提示次数最多3次
     */
    public static final int NOTIFY_UPDATE_MAX_TIMES = 3;
    
    /**
     * 提示用户设置nickname界面展示
     */
    public static boolean NOTIFY_SET_NICKNAME_SHOW = false;
	
	public static boolean IMAGE_PREVIEW = true;
	
	/**
	 * 名字字体大小（用在回话列表、联系人等界面的好友姓名）
	 */
	public static float NAME_TEXT_SIZE = 18;
	
	/**
     * 名字下最后一条消息文本字体大小（用在会话列表最后一条消息展示和联系人界面电话号码文本字体大小）
     */
	public static float CHAT_TEXT_SIZE = 14.5f;
	
	/**
     * 会话列表中消息时间显示字体大小
     */
	public static float TIME_TEXT_SIZE = 11.5f;
	
	/**
     * 会话界面文本消息字体大小（用在单聊群聊会话文本消息字体大小）
     */
	public static float CHATBODY_TEXT_SIZE = 17;
	
	/**
     * 会话界面消息时间字体大小（用在单聊群聊会话单条消息时间显示字体大小）
     */
	public static float CHATTIME_TEXT_SIZE = 10;

//	public static float SET_NAME_TEXT_SIZE = 17.3f;
	
	/**
     * 设置界面各个选项字体大小,菜单字体大小
     */
	public static float SET_TYPE_TEXT_SIZE = 16;
	
	/**
     * 设置界面各个选项下简介说明字体大小
     */
	public static float SET_SMALL_TEXT_SIZE = 12;
	
	
	public static float FAVORITES_NAME_TEXT_SIZE = 14.5f;  
	
	/**
	 * SecretKey, 用于加密
	 */
	public static String SECRET_KEY = null;

	/**
	 * mCode， 用于与hotalk新多媒体文件服务器上传/下载文件认证
	 */
	public static String MCODE = null;
	
	/**
	 * 聊天记录时间显示：true 显示最后一条消息的时间  false 显示最后一天所有消息的时间
	 */
	public static boolean changeMessageTimeDisplayMode = false;
	
	/**
	 * 服务器上用于升级的push agent版本号
	 */
	public static String PUSH_VERSION = null;
	/**
	 * 升级Push Service的地址
	 */
	public static String PUSH_UPDATE_URL = null;
	
	/**
	 * 判断是否正在请求祝福短信
	 */
	public static boolean IS_REQUEST_BLESSSMS = false;
	
    // add by jiangzhihua 2012-03-26 解决群聊页面查看
    /**
     * 是否是群聊页面首次加载
     */
    public static boolean IS_FIRST_LOAD = true;

    /**
     * Http离线消息所携带的用于服务器的认证字符串
     */
    public static String HTTP_SECRET_KEY = null;
    
    /**
     * 下载升级包的handler处理
     */
	public static ArrayList<Object> updateHandler = new ArrayList<Object>();
    
    /**
     * default date format
     * 默认的时间格式
     * The setting is not set; use the default.
     * We use a resource string here instead of just DateFormat.SHORT
     * so that we get a four-digit year instead a two-digit year.
     */
    public static String numeric_date_format_default = "yyyy-MM-dd EE";
    
    /**
     * 幻灯片中缺省图片默认长度和调整值
     */
    public static final int SLIDE_DEFAULT_SIZE = 160;
    
    /**
     * 批量删除时分块删除，每块500条。
     */
    public static final int DELETE_BLOCK_COUNT = 500;
    
    /**
     * 单位:dp;其中58dp--Loading高度；日期分隔符为=16.67dp
     */
    public static final double SCROLL_LOADING_HEIGHT = 74.67;
}