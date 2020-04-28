package cn.dapchina.newsupper.db;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	/**
	 * 数据库名称
	 */
	public static String DB_NAME = "db_DapSupper.db";
	/**
	 * 数据库版本号
	 */
	private static int version = 1;
	
	
	private static DBOpenHelper openHelper;
	
	/**
	 * 每张表的备用字段
	 */
	private final String END_TABLE = 
			"_AddTo01, _AddTo02, _AddTo03, _AddTo04, _AddTo05, _AddTo06, _AddTo07, _AddTo08, _AddTo09, _AddTo10, " +//
			"_AddTo11, _AddTo12, _AddTo13, _AddTo14, _AddTo15, _AddTo16, _AddTo17, _AddTo18, _AddTo19, _AddTo20, " +//
			"_AddTo21, _AddTo22, _AddTo23, _AddTo24, _AddTo25, _AddTo26, _AddTo27, _AddTo28, _AddTo29, _AddTo30, " +//
			"_AddTo31, _AddTo32, _AddTo33, _AddTo34, _AddTo35, _AddTo36, _AddTo37, _AddTo38, _AddTo39, _AddTo40, " +//
			"_AddTo50)";
	
	/**
	 * 问卷基本信息表
	 */
	private String tb_Survey = 						//
			/**
			 * _AddTo01字段已用于记录是否为测试用卷--->Survey.isTest
			 * _AddTo02字段已用于标志问卷是否有内部名单
			 * _AddTo03字段已用于标志改卷的内部名单是否下载过
			 * _AddTo04字段已用与标志问卷是否需要全局录音
			 * _AddTo05字段为问卷的eligible
			 * 
			 * {
			 *	如果问卷有内部受邀名单, 则_AddTo06、_AddTo07、_AddTo08、_AddTo09,
			 *	分别用于显示答卷列表上方的标题
			 * 	_AddTo06字段为第一个标题
			 * 	_AddTo07字段为第二个标题
			 * 	_AddTo08字段为第三个标题
			 * 	_AddTo09字段为第四个标题
			 * }
			 * _AddTo10为密码值
			 * _AddTo11为访前说明
			 * _AddTo12为问卷提醒问卷状态  --- 看是否是最新的问卷，以及可不可更新。   默认是0既不是新的也不可更新,新的存1,可更新存2
			 * _AddTo13为问卷提醒问卷生成日期
			 * _AddTo14字段为逻辑次数跳转json串
			 * _AddTo15存取的命名规则sid
			 * _AddTo16为是否摄像 0为没摄像 1为摄像
			 * _AddTo17是否新建限制 0不限制 1限制
			 * _AddTo18 访问状态json串
			 */
	"CREATE TABLE IF NOT EXISTS tb_Survey(" +			//
	"_id				INTEGER		PRIMARY KEY AUTOINCREMENT," +	//问卷表
	"_SurveyId			NVARCHAR		UNIQUE," +					//问卷号
	"_SurveyTitle		NVARCHAR," +					//问卷标题
	"_IsPhoto			INT 			DEFAULT'0', " +	//是否需要拍照
	"_IsRecord			INT 			DEFAULT'0'," +	//是否需要录音
	"_IsExpand			INT 			DEFAULT'0',"+//是否展开所有的问卷
	"_IsDowned			INT 			DEFAULT'0',"+//问卷是否已下载
	"_SurveyEnable		INT				DEFAULT'1'," +//问卷是否可用
	"_VisitMode			INT				DEFAULT'0'," +//访问模式,1为原生访问模式, 0为web访问模式
	"_DownloadUrl		NVARCHAR,		" +//问卷的网络地址
	"_PublishTime		NVARCHAR,		" +//问卷发布时间
	"_UserIdList		NVARCHAR		COLLATE NOCASE," +//假如用户有该问卷,则在次字段中引入,例如s001,dap1,ddww
	"_SurveyContent		TEXT," +//问卷内容
	END_TABLE;
	
	/**
	 * 原生问卷表
	 */
	private String tb_Question = 						//
	"CREATE TABLE IF NOT EXISTS tb_Question(" +			//
	/**
	 * _AddTo01字段已用于记录题目的选项有没有追加说明isHaveItemCap
	 * _AddTo02字段已用于记录文本框类型的题目答案值是否可以重复
	 * _AddTo03字段是否单题拍照
	 * _AddTo04字段单题拍照的名字
	 * _AddTo05字段已用于记干预的json字符串
	 * _AddTo06字段已用于记题组随机的json字符串
	 * _AddTo07  单题签名 //是否是单题签名,0不支持，1支持。
	 * _AddTo08  是否是哑题 //1代表是0代表不是
	 * _AddTo09  三级联动//1代表是0代表不是
	 * _AddTo10 题外关联  之和 //例如：（2,1,0）  2 标志位作为题外关联之和的标识  ,1  关联第1题  ,0  第1个选项  
	 * _AddTo11题外关联  之 选项置顶    判断 是否 是  选项置顶   1 代表  是   0  代表 不是     大树  添加 选项置顶
	 * _AddTo12字段是双引用 // 例如： （1,2 ） 表示引用这两道题的答案    大树 添加  双引用  
	 *  _AddTo13字段用于单复选矩阵固定功能
	 * _AddTo14 字段用于选项排序功能    大树排序  
	 * _AddTo15 用于字段是否隐藏选项属性 0不隐藏 1隐藏
	 * _AddTo16是否有矩阵右侧
	 */
	"_id					INTEGER		PRIMARY KEY AUTOINCREMENT," +	//
	"_SurveyId				NVARCHAR," +//问卷号
	"_QIndex				INT			," +//题目的index
	"_QOrder				INT			DEFAULT'-1'," +//题目的顺序号
	"_QType					INT			DEFAULT'-1'," +//题目的类型
	"_QTypeStr				NVARCHAR," +//题型说明
	"_QTitle				NVARCHAR," +//题目标题
	"_RowsItem				NVARCHAR," +//放置行的item
	"_ColumnsItem			NVARCHAR," +//放置列的item
	"_RestrictionsItem		NVARCHAR," +//引用其它问题的答案 json串
	"_QTitleDisable			INT			DEFAULT'0'," +//题目标题控件不可用
	"_QTitlePos				NVARCHAR," +//题目标题显示位置
	"_QComment				NVARCHAR," +//题目下方追加说明
	"_QCommentPos			NVARCHAR," +//题目下方追加说明的显示位置
	"_QScoreChecked			INT			DEFAULT'0'," +//是否启动评分的
	"_QWeightChecked		INT			DEFAULT'0'," +//启动权重
	"_QDragChecked			INT			DEFAULT'0'," +//是否启用图形
	"_QRequired				INT			DEFAULT'0'," +//必答的
	"_QRadomed				INT			DEFAULT'0'," +//可选的
	"_QInclusion			NVARCHAR," +//引用
	"_QExclude				INT			DEFAULT'0'," +//排他
	"_QSiteOption			NVARCHAR," +//
	"_QPreSelect			INT			DEFAULT'0'," +//预选
	"_QAttach				INT			DEFAULT'0'," +
	"_SizeWidth				INT			DEFAULT'-1'," + 
	"_Deployment			NVARCHAR," +//横纵向布局
	"_QColumnsDirection		NVARCHAR," +//
	"_IgnoreFirstItem		INT			DEFAULT'-1'," +
	"_Caption				NVARCHAR," +//追加说明有多个, 存成json字符串形式
	"_QId					NVARCHAR	," +//本问题在数据库中的存储编号
	"_FreeTextSort			NVARCHAR	," +
	"_FreeSumNumber			NVARCHAR	," +//填写的值加起来的和
	"_FreeSymbol			NVARCHAR	," +//单行文本中“>”、“<”、“=”、“>=”、“<=”等标志
	"_FreeMaxNumber			NVARCHAR	," +//单行文本最大值限制
	"_FreeMinNumber			NVARCHAR	," +//单行文本最小值限制
	"_FreeInputItems		NVARCHAR	," +//其他项
	"_FreeTextColumn		INT			DEFAULT'-1'," +
	"_RowsNum				INT			DEFAULT'-1'," +//
	"_QMatchQuestion		INT			DEFAULT'-1'," +
	"_QContinuous			INT			DEFAULT'-1'," +
	"_TextAreaRows			INT			DEFAULT'-1'," +
	"_MediaPosition			NVARCHAR," +//媒体文件在显示时,居左、居右还是居中
	"_MediaSrc				NVARCHAR	," +//媒体文件的路径
	"_MediaWidth			INT			DEFAULT'0'," +//媒体文件显示多宽
	"_MediaHeight			INT			DEFAULT'0'," +//媒体文件显示多高
	"_QScore				NVARCHAR," +//分数
	"_HaveOther				INT			DEFAULT'0'," +//有没有其他项
	"_LowerBound			INT			DEFAULT'0'," +//下限
	"_UpperBound			INT			DEFAULT'0'," +//上线
	"_TitleFrom				NVARCHAR	," +//引用某些题目的标题
	END_TABLE;
	
//	/**
//	 * 关联表, 用户关联的问卷有哪些
//	 */
//	private String tb_UserSurvey = 						//
//			"CREATE TABLE IF NOT EXISTS tb_UserSurvey(" +			//
//			"_id				INTEGER		PRIMARY KEY AUTOINCREMENT," +	//问卷表
//			"_UserId			NVARCHAR," +					//问卷号
//			"_SurveyId			NVARCHAR," +					//问卷标题
//			END_TABLE;
	
	/**
	 * 原生答案表
	 */
	private String tb_Answer = 						//
	"CREATE TABLE IF NOT EXISTS tb_Answer(" +			//
	"_id				INTEGER		PRIMARY KEY AUTOINCREMENT," +	//问卷表
	"_UserId			NVARCHAR 	NVARCHAR	COLLATE NOCASE," +					//用户帐号
	"_SurveyId			NVARCHAR," +					//问卷号
	"_QuestionIndex		INT			," +//
	"_QuestionOrder		INT			DEFAULT'-1'," +//题目的顺序号
	"_AnswerType		INT			DEFAULT'-1'," +//题目的类型
	"_AnswerMap			NVARCHAR," +//问题的答案, 以json串存储的
	"_MediaPath			NVARCHAR," +//媒体文件的本地存储目录
	"_MediaName			NVARCHAR," +//媒体文件的名称
	"_UUID				NVARCHAR," +//答案所属那个唯一标识
	"_Enable			INT			DEFAULT'0', " +//答案是否有效, 无效的不必查出
	END_TABLE;
	
	/**
	 * 用户信息表
	 * _AddTo01 代表自定义logo功能 文件名字
	 */
	private String tb_User = 						//
			"CREATE TABLE IF NOT EXISTS tb_User(" +			//
			"_id				INTEGER		PRIMARY KEY AUTOINCREMENT," +	//问卷表
			"_UserId			NVARCHAR	UNIQUE	 COLLATE NOCASE," +					//用户帐号
			"_UserName			NVARCHAR," +					//问卷号
			"_UserPass			NVARCHAR," +//
			"_UserType			INT			DEFAULT'-1'," +//题目的顺序号
			"_IsEnable			INT			DEFAULT'-1'," +//题目的类型
			"_ParentId			NVARCHAR," +//是按个部门的(备用)
			"_CreateTime		," +
			"_IsReset			INT			DEFAULT'-1'," +//是否重设
			"_VisitMode			INT			DEFAULT'-1'," +//访问方式, 0为网页访问方式, 1为原生访问方式
			END_TABLE;
	/**
	 * 异常表
	 * _AddTo01   文件名
	 * _AddTo02   路径
	 * _AddTo03   用户名字
	 * _AddTo04 mac地址
	 * _AddTo05   是否可用  0为不可用 1为可用
	 */
	private String tb_Tab1 = 						//
			"CREATE TABLE IF NOT EXISTS tb_Tab1(" +			//
			"_id	INTEGER		PRIMARY KEY AUTOINCREMENT," +	//
			END_TABLE;
	
	/**
	 * 地图监控表
	 * _AddTo01 userId;
	 * _AddTo02 userLat; // 纬
	 * _AddTo03 userLng; // 经
	 * _AddTo04 time; // 时间
	 * _AddTo05 addName; // 地点
	 * _AddTo06 isUpload;// 是否上传了
	 * _AddTo07 date;//当天时间
	 */
	private String tb_Tab2 = 						//
			"CREATE TABLE IF NOT EXISTS tb_Tab2(" +			//
			"_id	INTEGER		PRIMARY KEY AUTOINCREMENT," +	//
			END_TABLE;
	
	/**
	 * 知识库
	 * _AddTo01  服务器id
	 * _AddTo02  标题
	 * _AddTo03  分类项目名字
	 * _AddTo04  内容
	 * _AddTo05  附件下载路径
	 * _AddTo06  附件储存名字
	 * _AddTo07  是否可用          0代表可用  1代表不可用
	 * _AddTo08  是哪个用户的知识库
	 */
	private String tb_Tab3 = 						//
			"CREATE TABLE IF NOT EXISTS tb_Tab3(" +			//
			"_id	INTEGER		PRIMARY KEY AUTOINCREMENT," +	//
			END_TABLE;
	
	/**
	*  记录丢失文件表
	 *  _AddTo01   uuid  ID号 和附件id一样 然后查询feedid
	 *  _AddTo02   feedName 附件名字
	 *  _AddTo03         是否传完了 0没传 1传了
	 *  _AddTo04         问卷id
	 */
	private String tb_Tab4 = 						//
			"CREATE TABLE IF NOT EXISTS tb_Tab4(" +			//
			"_id	INTEGER		PRIMARY KEY AUTOINCREMENT," +	//
			END_TABLE;
	
	
	/**
	 * 备用表5
	 * 数据字典表
	 *  _AddTo01   classId 服务器唯一id
	 *  _AddTo02   className 对应id的名称
 	 *  _AddTo03   datas 数据字典字符串一逗号隔开
	 *  _AddTo04   localDatas 数据字典本地新增的
	 */
	private String tb_Tab5 = 						//
			"CREATE TABLE IF NOT EXISTS tb_Tab5(" +			//
			"_id	INTEGER		PRIMARY KEY AUTOINCREMENT," +	//
			END_TABLE;
	
	/**
	 * 备用表6
	 * 配额表
	 * 	_AddTo01	配额 唯一id
	 *  _AddTo02	配额名称
 	 *  _AddTo03	配额成功量
	 *  _AddTo04	配额说明
	 *  _AddTo05 	对应哪个问卷
	 *  _AddTo06 	创建时间
	 *  _AddTo07 	配额str
	 *  _AddTo08 	配额用户编号
	 *  
	 */
	private String tb_Tab6 = 						//
			"CREATE TABLE IF NOT EXISTS tb_Tab6(" +			//
			"_id	INTEGER		PRIMARY KEY AUTOINCREMENT," +	//
			END_TABLE;
	
	/**
	 * 上传日志表,用于记录断点续传
	 */
	private String tb_UploadLog = 						//
			"CREATE TABLE IF NOT EXISTS tb_UploadLog(" +			//
			"_id					INTEGER		PRIMARY KEY AUTOINCREMENT," +	//数据中的id号
			"_FileName				NVARCHAR," +					//文件名称
			"_SourceId				NVARCHAR," +					//资源号
			END_TABLE;
	
	/**
	 * 答卷信息记录表
	 */
	private String tb_UploadFeed = 	
			/**
			 * _AddTo01字段用于记录XML是否通过Socket上传过,也就是和PNG、MP3一起传过
			 * _AddTo01=1传过, _AddTo01=0或_AddTo01=null没传过
			 * 
			 * _AddTo02用于记录答卷用横屏还是用竖屏答的
			 * _AddTo02=0竖屏, _AddTo02=1横屏, _AddTo02=null还是竖屏
			 * 
			 * _AddTo03用于记录内部受邀名单对象InnerPanel的JSON字符串
			 * * _AddTo04用于记录唯一panelid号
			 * _AddTo05用于记录是否是存在内部控件的MP3或PNG
			 * _AddTo06用于记录保存手动题组随机的顺序号 0@3|0@2|0@1|0@0|1@3|1@2|1@1|1@0|2@3|2@2|2@1|2@0|
			 *  0@3| 说明：0 大题组号  3小题组号  @ |都是分隔符
			 *  _AddTo07  记录所有引用受访者参数
			 *  _AddTo08  记录目录标签的节点
			 *  _AddTo09 返回ID  大树拒访  
			 */
			"CREATE TABLE IF NOT EXISTS tb_UploadFeed(" +			//
			"_id				INTEGER		PRIMARY KEY AUTOINCREMENT," +	//问卷表
			"_UserId			NVARCHAR    COLLATE NOCASE," +					//谁的
			"_FeedId			INT		 	NOT NULL DEFAULT'0',"+//服务器端返回的ID
			"_SurveyId			NVARCHAR," +					//哪个问卷
			"_UUID				NVARCHAR," +					//唯一的表示
			"_FilePath			NVARCHAR," +					//文件路径
			"_VisitAddress		NVARCHAR," +					//访问的地址
			"_ManyPlaces		NVARCHAR," +					//每一次的访问地址(包含多个)
			"_FileName			NVARCHAR	UNIQUE," +					//文件名称
			"_CreateTime		BIGINT, " +					//问卷号
			"_StartTime			BIGINT, " +					//访问开始事件
			"_RegTime			BIGINT, " +					//访问结束事件
			"_FileSize			BIGINT," +					//文件的大小
			"_IsUploaded		INT 		NOT NULL DEFAULT'-1'," +//是否上传1传过, 0未传
			"_IsCompleted		INT 		NOT NULL DEFAULT'-1'," +//是否完成1完成, 0未完成
			"_isSync 			INT 		NOT NULL DEFAULT'-1'," +//是否同步过
			"_FileType			INT			NOT NULL DEFAULT'0'," +	//文件的类型XML=1, PNG=2, MP3=3, MP4=4代表摄像出来的mp4
			"_Spent				BIGINT		DEFAULT'0'," +	//总共花了多长时间
			"_ManyTimes			NVARCHAR," +	//每一次的答卷的开始时间和结束事件(包含多个)
			"_LotsCoord			NVARCHAR," +	//Coordinate每一次的坐标的经纬度(包含多个)
			"_IndexArr			NVARCHAR," +	//预览问卷答案字符串
			"_Lat				NVARCHAR,"+//纬度
			"_Lng				NVARCHAR,"+//经度
			"_ReturnType		NVARCHAR	NOT NULL DEFAULT'0',"+//上传答卷之后服务器端返回的状态值
			"_QuestionId		NVARCHAR	,"+//问题的ID号
			"_VisitMode			INT			DEFAULT'-1'," +//访问方式, 0为网页访问方式, 1为原生访问方式
			"_GiveUp			INT			DEFAULT'0'," +//1此份记录放弃了,0没有放弃  放弃了 但是还能做。 2不能上传能做
			END_TABLE;
	
	private DBOpenHelper(Context context) {
		super(context, DB_NAME, null, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		update(db, 0, 0);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		update(db, oldVersion, newVersion);
	}

	private void update(SQLiteDatabase db, int oV, int nV){
		
		db.execSQL(tb_Survey);//问卷
		db.execSQL(tb_Question);//问卷中的问题
		//db.execSQL(tb_UserSurvey);//用户的问卷表
		db.execSQL(tb_Answer);//问题的答案表
		db.execSQL(tb_User);//用户表
		db.execSQL(tb_Tab1);//备用表1
		db.execSQL(tb_Tab2);//备用表2
		db.execSQL(tb_Tab3);//备用表3
		db.execSQL(tb_Tab4);//备用表4
		db.execSQL(tb_Tab5);//备用表5
		db.execSQL(tb_Tab6);//备用表6
		db.execSQL(tb_UploadLog);//用于断电续传
		db.execSQL(tb_UploadFeed);//用于记录问卷的答卷情况
	}
	
	public static DBOpenHelper getInstance(Context _c){
		if(null==openHelper){
			openHelper = new DBOpenHelper(_c);
		}
		return openHelper;
	}
}
