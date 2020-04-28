package cn.dapchina.newsupper.db;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.DapException;
import cn.dapchina.newsupper.bean.Data;
import cn.dapchina.newsupper.bean.Knowledge;
import cn.dapchina.newsupper.bean.MyRecoder;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.Quota;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.bean.User;
import cn.dapchina.newsupper.bean.UserPosition;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.util.Util;

public class DBService {

	// private Context mContext;
	private ContentResolver resolver;

	public DBService(Context context) {
		// this.mContext = context;
		resolver = context.getContentResolver();
	}
	
	/**
	 * 查询所有已完成且需要上传的UploadFeed
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getAllQuotaUploadFeed(String surveyId, String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID",  //
				"_SurveyId", "_AddTo07" }, //
				"_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=?", //
				new String[] { surveyId, "1", "1", "0", userId }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUuid(c.getString(1));
			feed.setSurveyId(c.getString(2));
			feed.setParametersStr(c.getString(3));
			fs.add(feed);
		}
		c.close();
		return fs;
	}
	
	
	/**
	 * 查询所有符合配额的答案
	 * 
	 * */
	public ArrayList<Answer> getUserAllquotaAnswer(String SurveyId,String QuestionIndex) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		ArrayList<Answer> ans = new ArrayList<Answer>();
		Cursor c = resolver.query(uri, new String[] { "_id", "_UserId", 
				"_QuestionOrder", //
				"_AnswerType", "_AnswerMap", //
				"_MediaPath", "_MediaName", "_UUID" }, //
				"_SurveyId=? AND _QuestionIndex=? AND _Enable <> 0", //
				new String[] { SurveyId, QuestionIndex }, null);
		while (c.moveToNext()) {
			Answer an = new Answer();
			an.id = c.getInt(0);
			an.userId = c.getString(1);
			an.surveyId = SurveyId;
			an.qIndex = Integer.parseInt(QuestionIndex);
			an.qOrder = c.getInt(2);
			an.answerType = c.getInt(3);
			an.setAnswerMapStr(c.getString(4));
			an.mediaPath = c.getString(5);
			an.mediaName = c.getString(6);
			an.uuid = c.getString(7);
			// return an;
			ans.add(an);
		}
		c.close();
		return ans;
	}
	
	
	
	/** 增加配额 **/
	public boolean addQuota(Quota quota,String surveyId,String userId) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB6);
		ContentValues values = new ContentValues();
		values.put("_AddTo01",quota.getQuotaId());
		values.put("_AddTo02",quota.getQuotaName() );
		values.put("_AddTo04",quota.getQuotaIns());
		values.put("_AddTo06",quota.getQuotaTime());
		values.put("_AddTo05",surveyId);
		values.put("_AddTo07",quota.getOptionstr());
		values.put("_AddTo08",userId);
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}
	
	/** 更新配额 **/
	public boolean updateQuota(Quota quota,String surveyId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB6);
		ContentValues values = new ContentValues();
		values.put("_AddTo02",quota.getQuotaName());
		values.put("_AddTo06",quota.getQuotaTime());
		values.put("_AddTo04",quota.getQuotaIns());
		values.put("_AddTo07",quota.getOptionstr());
		int i = resolver.update(uri, values, "_AddTo01=? and _AddTo05=?", new String[] { quota.getQuotaId()+"",surveyId });
		return 0 != i;
	}
	
	/**
	 * 获取存在的配额
	 * 
	 * @param surveyId
	 * @return
	 */
	public Quota getQuotaExsit(String quotaId,String surveyId) {
		Quota quota = null;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB6);
		Cursor c = resolver.query(uri, new String[] {
				"_AddTo02",// 0
				"_AddTo03",// 1
				"_AddTo04",// 2
				"_AddTo06",// 3
				"_AddTo07",// 4
				}, "_AddTo01=? and _AddTo05=?", new String[] {quotaId,surveyId}, null);
		if (c.moveToNext()) {
			quota=new Quota();
			quota.setQuotaId(quotaId);
			quota.setQuotaName(c.getString(0));
			quota.setQuotaIns(c.getString(2));
			quota.setOptionstr(c.getString(4));
		}
		c.close();
		return quota;
	}
	
	/**
	 * 获取该surevyId的配额
	 * 
	 * @return
	 */
	public ArrayList<Quota> getSurveyQuotaList(String surveyId , String userId) {
		ArrayList<Quota> qList = new ArrayList<Quota>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB6);
		Cursor c = resolver.query(uri, new String[] {//
						"_id", // 0
						"_AddTo01", // 1
						"_AddTo02", // 2
						"_AddTo03", // 3
						"_AddTo04", // 4
						"_AddTo06", // 5
						"_AddTo07", // 6
						}, "_AddTo05=? and _AddTo08=?", //
				new String[] { surveyId , userId }, null);
		while (c.moveToNext()) {
			Quota quota = new Quota();
			quota.setQuotaId(c.getString(1));
			quota.setQuotaName(c.getString(2));
			quota.setQuotaTime(c.getString(5));
			quota.setQuotaIns(c.getString(4));
			quota.setOptionstr(c.getString(6));
			qList.add(quota);
		}
		c.close();
		return qList;
	}
	
	/**
	 * 获取该surevyId的配额
	 * 
	 * @return
	 */
	public ArrayList<Quota> getSurveyQuotaList(String userId ) {
		ArrayList<Quota> qList = new ArrayList<Quota>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB6);
		Cursor c = resolver.query(uri, new String[] {//
						"_id", // 0
						"_AddTo01", // 1
						"_AddTo02", // 2
						"_AddTo04", // 3
						"_AddTo05",	// 4
						"_AddTo06", // 5
						"_AddTo07", // 6
						}, "_AddTo08=?", //
				new String[] {  userId }, null);
		while (c.moveToNext()) {
			Quota quota = new Quota();
			quota.setQuotaTime(c.getString(5));
			quota.setQuota_Userid(userId);
			quota.setQuota_Surveyid(c.getString(4));
			qList.add(quota);
		}
		c.close();
		return qList;
	}
	
	/**
	 * 得到录音或者视频
	 * */
	public ArrayList<UploadFeed> getRecordorvideo(String uuid, String surveyId,String type) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_FileType", //
				"_FileName", "_StartTime", "_RegTime",//
				"_FileSize", "_QuestionId","_FilePath"}, //
				"_UUID=? AND _SurveyId=? AND _FileType IN(?) AND _GiveUp=? AND _FileSize>0", // IN(?,
																									// ?,?)摄像
				new String[] { uuid, surveyId, type, "0" }, // 摄像格式String.valueOf(Cnt.FILE_TYPE_MP4)
				null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setType(c.getInt(1));
			feed.setName(c.getString(2));
			feed.setStartTime(c.getLong(3));
			feed.setRegTime(c.getLong(4));
			feed.setSize(c.getLong(5));
			feed.setQuestionId(c.getString(6));
			feed.setPath(c.getString(7));
			fs.add(feed);
		}
		c.close();
		return fs;
	}
	/**
	 * 兼顾离线 得到用户
	 * 
	 * @param userId
	 * @param userPass
	 * @return
	 */
	public User getUser(String userId, String userPass) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_USER);
		Cursor c = resolver.query(uri, new String[] { "_UserId",// 0
				"_UserName", // 1
				"_UserPass", // 2
				"_UserType", // 3
				"_IsEnable", // 4
				"_CreateTime"// 5
		}, // 17eligible
				"_UserId=? and _UserPass=? and _IsEnable=0", new String[] { userId, userPass }, null);
		User u = null;
		if (c.moveToFirst()) {
			u = new User();
			u.userId = c.getString(0);
			u.userName = c.getString(1);
			u.userPass = c.getString(2);
			u.userType = c.getInt(3);
			u.isEnable = c.getInt(4);
			u.createTime = c.getString(5);
		}
		c.close();
		return u;
	}

	/**
	 * 自定义logo功能得到名字
	 * 
	 * @param userId
	 * @param userPass
	 * @return
	 */
	public String getLogoName(String userId) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_USER);
		Cursor c = resolver.query(uri, new String[] { "_AddTo01" }, // 17eligible
				"_UserId=?", new String[] { userId }, null);
		String name = "";
		if (c.moveToFirst()) {
			name = c.getString(0);
		}
		c.close();
		return name;
	}

	/**
	 * 自定义logo功能添加用户
	 * **/
	public boolean addUserLogo(String userId, String name) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_USER);
		ContentValues values = new ContentValues();
		values.put("_UserId", userId);
		values.put("_AddTo01", name);
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/** 自定义logo功能更新用户 **/
	public boolean updateUserLogo(String userId, String name) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_USER);
		ContentValues values = new ContentValues();
		values.put("_AddTo01", name);
		int i = resolver.update(uri, values, "_UserId=?", new String[] { userId });
		return 0 != i;
	}

	/**
	 * 添加用户
	 * **/
	public boolean addUser(User user) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_USER);
		ContentValues values = new ContentValues();
		values.put("_UserId", user.userId);
		values.put("_UserName", user.userName);
		values.put("_UserPass", user.userPass);
		values.put("_UserType", user.userType);
		values.put("_IsEnable", user.isEnable);
		values.put("_CreateTime", user.createTime);
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/** 更新用户 **/
	public boolean updateUser(User user) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_USER);
		ContentValues values = new ContentValues();
		// values.put("UserId", user.userId);
		values.put("_UserName", user.userName);
		values.put("_UserPass", user.userPass);
		values.put("_UserType", user.userType);
		values.put("_IsEnable", user.isEnable);
		values.put("_CreateTime", user.createTime);
		int i = resolver.update(uri, values, "_UserId=?", new String[] { user.userId });
		return 0 != i;
	}

	/** 添加问卷 **/
	public boolean addSurvey(Survey survey) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		ContentValues values = new ContentValues();
		values.put("_SurveyId", survey.surveyId);
		values.put("_SurveyTitle", survey.surveyTitle);
		values.put("_IsPhoto", survey.isPhoto);
		// 摄像添加
		values.put("_AddTo16", survey.isVideo);
		values.put("_IsRecord", survey.isRecord);
		values.put("_IsExpand", survey.isExpand);
		values.put("_IsDowned", 0);
		// 新建限制添加
		values.put("_AddTo17", survey.oneVisit);
		values.put("_SurveyEnable", survey.surveyEnable);
		values.put("_DownloadUrl", survey.downloadUrl);
		values.put("_PublishTime", survey.publishTime);
		values.put("_VisitMode", survey.visitMode);
		if (!Util.isEmpty(survey.userIdList)) {
			values.put("_UserIdList", survey.userIdList);
		}
		/**
		 * 是否为测试卷问
		 */
		values.put("_AddTo01", survey.isTest);
		/**
		 * 是否有内部名单
		 */
		values.put("_AddTo02", survey.openStatus);
		/**
		 * 是否需要全局自动开启录音
		 */
		values.put("_AddTo04", survey.globalRecord);
		values.put("_AddTo10", survey.getPassword());
		/**
		 * 问卷提醒字段
		 */
		values.put("_AddTo12", 1);// 1为新增
		values.put("_AddTo13", survey.getGeneratedTime());// 记录第一次更新时间
		// 访问状态
		values.put("_AddTo18", survey.getReturnStr());
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/** 添加问卷 **/
	public boolean updateSurvey(Survey survey, int notice) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		ContentValues values = new ContentValues();
		// values.put("SurveyId", survey.surveyId);
		values.put("_SurveyTitle", survey.surveyTitle);
		values.put("_IsPhoto", survey.isPhoto);
		// 摄像添加
		values.put("_AddTo16", survey.isVideo);
		values.put("_IsRecord", survey.isRecord);
		values.put("_IsExpand", survey.isExpand);
		// 新建限制添加
		values.put("_AddTo17", survey.oneVisit);
		// values.put("IsDowned", 0);
		values.put("_SurveyEnable", survey.surveyEnable);
		values.put("_DownloadUrl", survey.downloadUrl);
		values.put("_PublishTime", survey.publishTime);
		values.put("_VisitMode", survey.visitMode);
		if (!Util.isEmpty(survey.userIdList)) {
			values.put("_UserIdList", survey.userIdList);
		}
		/**
		 * 是否为测试问卷
		 */
		values.put("_AddTo01", survey.isTest);
		/**
		 * 是否为有内部名单
		 */
		values.put("_AddTo02", survey.openStatus);
		/**
		 * 是否需要开启全局录音
		 */
		values.put("_AddTo04", survey.globalRecord);
		values.put("_AddTo10", survey.getPassword());
		/**
		 * 是否更改问卷提醒属性
		 */
		if (1 == notice) {
			values.put("_AddTo12", 2);// 2代表更新提醒
			values.put("_AddTo13", survey.getGeneratedTime());// 记录更新时间
		}
		if (0 == survey.isDowned) {
			values.put("_AddTo13", survey.getGeneratedTime());// 更新提醒生成时间
		}
		// 访问状态
		values.put("_AddTo18", survey.getReturnStr());
		int i = resolver.update(uri, values, "_SurveyId=?", new String[] { survey.surveyId });
		return 0 != i;
	}

	/** 逻辑次数跳转更新问卷 **/
	public boolean updateLogicListBySurvey(String sid, String logicListStr) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		ContentValues values = new ContentValues();
		// values.put("SurveyId", survey.surveyId);
		values.put("_AddTo14", logicListStr);
		int i = resolver.update(uri, values, "_SurveyId=?", new String[] { sid });
		return 0 != i;
	}

	// 命名规则更改
	public void updateSurveyOpenStatus(String surveyId, String p1, String p2, String p3, String p4, String parameterName) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		ContentValues values = new ContentValues();
		values.put("_AddTo06", p1);
		values.put("_AddTo07", p2);
		values.put("_AddTo08", p3);
		values.put("_AddTo09", p4);
		// 命名规则更改
		values.put("_AddTo15", parameterName);
		resolver.update(uri, values, "_SurveyId=?", new String[] { surveyId });
	}

	/** 添加问卷 **/
	public boolean surveyDownloaded(String surveyId, int eligible, String word) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		ContentValues values = new ContentValues();
		values.put("_IsDowned", 1);
		values.put("_AddTo05", eligible);
		values.put("_AddTo11", word);
		/**
		 * 问卷提醒状态
		 */
		values.put("_AddTo12", 0);
		/**
		 * 问卷目录
		 */
		int i = resolver.update(uri, values, "_SurveyId=?", new String[] { surveyId });
		return 0 != i;
	}

	/**
	 * 目录置为空
	 * 
	 * @param surveyId
	 * @return
	 */
	public void updateListUploadFeed(String surveyId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_AddTo08", "");
		resolver.update(uri, values, "_SurveyId=?", new String[] { surveyId });
	}

	public Survey getSurvey(String surveyId) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId",// 0
				"_SurveyTitle", // 1
				"_IsPhoto", // 2
				"_IsRecord", // 3
				"_IsExpand", // 4
				"_IsDowned", // 5
				"_SurveyEnable", // 6
				"_DownloadUrl", // 7
				"_PublishTime", // 8
				"_SurveyContent",// 9
				"_ID",// 10
				"_VisitMode",// 11
				"_UserIdList",// 12
				"_AddTo01", // 13是否为测试问卷
				"_AddTo02",// 14是否有内部名单
				"_AddTo03",// 15内部名单是否下过
				"_AddTo04",// 16是否全局录音
				"_AddTo05",// 17eligible
				"_AddTo16",// 18是否摄像
				"_AddTo11", // 19是说明
				"_AddTo17" },// 20是否新建限制
				"_SurveyId=?", new String[] { surveyId }, null);
		Survey s = null;
		if (c.moveToFirst()) {
			s = new Survey();
			s.surveyId = c.getString(0);
			s.surveyTitle = c.getString(1);
			s.isPhoto = c.getInt(2);
			s.isRecord = c.getInt(3);
			s.isExpand = c.getInt(4);
			s.isDowned = c.getInt(5);
			s.surveyEnable = c.getInt(6);
			s.downloadUrl = c.getString(7);
			s.publishTime = c.getString(8);
			s.surveyContent = c.getString(9);
			s.visitMode = c.getInt(11);
			s.userIdList = c.getString(12);
			s.isTest = c.isNull(13) ? 0 : c.getInt(13);
			s.openStatus = c.isNull(14) ? 0 : c.getInt(14);
			s.innerDone = c.isNull(15) ? 0 : c.getInt(15);
			s.globalRecord = c.isNull(16) ? 0 : c.getInt(16);
			s.eligible = c.isNull(17) ? -1 : c.getInt(17);
			// 摄像赋值
			s.isVideo = c.isNull(18) ? 0 : c.getInt(18);
			s.setWord(c.getString(19));
			s.id = c.getInt(10);
			// 新建限制赋值
			s.oneVisit = c.isNull(20) ? 0 : c.getInt(20);
		}
		c.close();
		return s;
	}

	public ArrayList<Survey> getAllSurvey(String userId) {
		ArrayList<Survey> ss = new ArrayList<Survey>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId",// 0
				"_SurveyTitle", // 1
				"_IsPhoto", // 2
				"_IsRecord", // 3
				"_IsExpand", // 4
				"_IsDowned", // 5
				"_SurveyEnable", // 6
				"_DownloadUrl", // 7
				"_PublishTime", // 8
				"_SurveyContent",// 9
				"_id",// 10
				"_VisitMode",// 11
				"_AddTo01",// 12
				"_AddTo02",// 13是否有内部受邀名单
				"_AddTo03",// 14内部受邀名单是否已下载过
				"_AddTo04",// 15
				"_AddTo05", // 16
				"_AddTo12",// 17
				"_AddTo16",// 18是否摄像
				"_AddTo13",// 19是生成时间
				"_AddTo11",// 20访前说明
				"_AddTo17", },// 21是否新建限制
				"_SurveyEnable=? AND _UserIdList LIKE '%" + userId + "%'", // "SurveyEnable=?"
				new String[] { "0" }, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			Survey s = new Survey();
			s.surveyId = c.getString(0);
			// System.out.println("顺序号:" + s.surveyId);
			s.surveyTitle = c.getString(1);
			s.isPhoto = c.getInt(2);
			s.isRecord = c.getInt(3);
			s.isExpand = c.getInt(4);
			s.isDowned = c.getInt(5);
			s.surveyEnable = c.getInt(6);
			s.downloadUrl = c.getString(7);
			s.publishTime = c.getString(8);
			s.surveyContent = c.getString(9);
			s.id = c.getInt(10);
			s.visitMode = c.getInt(11);
			s.isTest = c.isNull(12) ? 0 : c.getInt(12);
			s.openStatus = c.isNull(13) ? 0 : c.getInt(13);
			s.innerDone = c.isNull(14) ? 0 : c.getInt(14);
			s.globalRecord = c.isNull(15) ? 0 : c.getInt(15);
			s.eligible = c.isNull(16) ? -1 : c.getInt(16);
			// 问卷提醒获得提醒
			s.setNoticeNew(c.isNull(17) ? 0 : c.getInt(17));
			// 摄像赋值
			s.isVideo = c.isNull(18) ? 0 : c.getInt(18);
			// 生成时间
			s.setGeneratedTime(c.getString(19));
			s.setWord(c.getString(20));
			// 新建限制赋值
			s.oneVisit = c.isNull(21) ? 0 : c.getInt(21);
			ss.add(s);
		}
		if (ss.size() > 1) {
			ss = Util.sort(ss);
		}
		c.close();
		return ss;
	}

	/**
	 * 新建限制 是否已经新建过
	 * 
	 * @param surveyId
	 * @return
	 */
	public boolean IsExistSurvey(String surveyId) {
		boolean isExist = false;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, //
				"_SurveyId=? AND _FileType=? AND _GiveUp=?", //
				new String[] { surveyId, "1", "0" }, null);
		if (c.moveToFirst()) {
			isExist = (0 < c.getLong(0));
		}
		c.close();
		return isExist;
	}

	public ArrayList<Survey> getAllDownloadedSurvey(String userId) {
		ArrayList<Survey> ss = new ArrayList<Survey>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId",// 0
				"_SurveyTitle", // 1
				"_IsPhoto", // 2
				"_IsRecord", // 3
				"_IsExpand", // 4
				"_IsDowned", // 5
				"_SurveyEnable", // 6
				"_DownloadUrl", // 7
				"_PublishTime", // 8
				"_SurveyContent",// 9
				"_id",// 10
				"_VisitMode",// 11
				"_AddTo01",// 12
				"_AddTo02",// 13
				"_AddTo03",// 14
				"_AddTo04",// 15
				"_AddTo05",// 16
				"_AddTo06",// 17
				"_AddTo07",// 18
				"_AddTo08",// 19
				"_AddTo09",// 20
				"_AddTo10",// 21
				"_AddTo11",// 22
				"_AddTo14",// 23逻辑次数跳转
				"_AddTo15", // 24命名规则
				"_AddTo16",// 25是否摄像
				"_AddTo12",// 26为问卷提醒问卷状态
				"_AddTo13",// 27为问卷提醒问卷生成日期
				"_AddTo17",// 28是否新建限制
				"_AddTo18"	//29访问状态
		}, "_SurveyEnable=? AND _IsDowned=? AND _UserIdList LIKE '%" + userId + "%'", // "SurveyEnable=?"
				new String[] { "0", "1" }, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			Survey s = new Survey();
			s.surveyId = c.getString(0);
			s.surveyTitle = c.getString(1);
			s.isPhoto = c.getInt(2);
			s.isRecord = c.getInt(3);
			s.isExpand = c.getInt(4);
			s.isDowned = c.getInt(5);
			s.surveyEnable = c.getInt(6);
			s.downloadUrl = c.getString(7);
			s.publishTime = c.getString(8);
			s.surveyContent = c.getString(9);
			s.id = c.getInt(10);
			s.visitMode = c.getInt(11);
			s.isTest = c.isNull(12) ? 0 : c.getInt(12);
			s.openStatus = c.isNull(13) ? 0 : c.getInt(13);
			s.innerDone = c.isNull(14) ? 0 : c.getInt(14);
			s.globalRecord = c.isNull(15) ? 0 : c.getInt(15);
			s.eligible = c.isNull(16) ? -1 : c.getInt(16);
			s.setParameter1(c.isNull(17) ? "" : c.getString(17));
			s.setParameter2(c.isNull(18) ? "" : c.getString(18));
			s.setParameter3(c.isNull(19) ? "" : c.getString(19));
			s.setParameter4(c.isNull(20) ? "" : c.getString(20));
			s.setPassword(c.getString(21));
			s.setWord(c.getString(22));
			s.strLogicList = c.isNull(23) ? "" : c.getString(23);// 逻辑次数跳转
			s.setParameterName(c.isNull(24) ? "" : c.getString(24));// 命名规则
			// 摄像赋值
			s.isVideo = c.isNull(25) ? 0 : c.getInt(25);
			// 问卷提醒获得提醒
			s.setNoticeNew(c.isNull(26) ? 0 : c.getInt(26));
			// 生成时间
			s.setGeneratedTime(c.getString(27));
			// 新建限制赋值
			s.oneVisit = c.isNull(28) ? 0 : c.getInt(28);
			//访问状态
			s.setReturnStr(c.getString(29));
			ss.add(s);
		}
		if (ss.size() > 1) {
			ss = Util.sort(ss);
		}
		c.close();
		return ss;
	}

	public ArrayList<Survey> getAllDownloadedSurveyHaveInner(String userId) {
		ArrayList<Survey> ss = new ArrayList<Survey>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId",// 0
				"_SurveyTitle", // 1
				"_IsPhoto", // 2
				"_IsRecord", // 3
				"_IsExpand", // 4
				"_IsDowned", // 5
				"_SurveyEnable", // 6
				"_DownloadUrl", // 7
				"_PublishTime", // 8
				"_SurveyContent",// 9
				"_id",// 10
				"_VisitMode",// 11
				"_AddTo01",// 12
				"_AddTo02",// 13
				"_AddTo03",// 14
				"_AddTo04",// 15
				"_AddTo05",// 16
				"_AddTo06",// 17
				"_AddTo07",// 18
				"_AddTo08",// 19
				"_AddTo09",// 20
				"_AddTo10",// 21
				"_AddTo11",// 22
				"_AddTo14",// 23逻辑次数跳转
				"_AddTo15", // 24命名规则
				"_AddTo16",// 25是否摄像
				"_AddTo12",// 26为问卷提醒问卷状态
				"_AddTo13",// 27为问卷提醒问卷生成日期
				"_AddTo17"// 28是否新建限制
		}, "_SurveyEnable=? AND _IsDowned=? AND _AddTo02=1 AND _UserIdList LIKE '%" + userId + "%'", // "SurveyEnable=?"
				new String[] { "0", "1" }, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			Survey s = new Survey();
			s.surveyId = c.getString(0);
			s.surveyTitle = c.getString(1);
			s.isPhoto = c.getInt(2);
			s.isRecord = c.getInt(3);
			s.isExpand = c.getInt(4);
			s.isDowned = c.getInt(5);
			s.surveyEnable = c.getInt(6);
			s.downloadUrl = c.getString(7);
			s.publishTime = c.getString(8);
			s.surveyContent = c.getString(9);
			s.id = c.getInt(10);
			s.visitMode = c.getInt(11);
			s.isTest = c.isNull(12) ? 0 : c.getInt(12);
			s.openStatus = c.isNull(13) ? 0 : c.getInt(13);
			s.innerDone = c.isNull(14) ? 0 : c.getInt(14);
			s.globalRecord = c.isNull(15) ? 0 : c.getInt(15);
			s.eligible = c.isNull(16) ? -1 : c.getInt(16);
			s.setParameter1(c.isNull(17) ? "" : c.getString(17));
			s.setParameter2(c.isNull(18) ? "" : c.getString(18));
			s.setParameter3(c.isNull(19) ? "" : c.getString(19));
			s.setParameter4(c.isNull(20) ? "" : c.getString(20));
			s.setPassword(c.getString(21));
			s.setWord(c.getString(22));
			s.strLogicList = c.isNull(23) ? "" : c.getString(23);// 逻辑次数跳转
			s.setParameterName(c.isNull(24) ? "" : c.getString(24));// 命名规则
			// 摄像赋值
			s.isVideo = c.isNull(25) ? 0 : c.getInt(25);
			// 问卷提醒获得提醒
			s.setNoticeNew(c.isNull(26) ? 0 : c.getInt(26));
			// 生成时间
			s.setGeneratedTime(c.getString(27));
			// 新建限制赋值
			s.oneVisit = c.isNull(28) ? 0 : c.getInt(28);
			ss.add(s);
		}
		if (ss.size() > 1) {
			ss = Util.sort(ss);
		}
		c.close();
		return ss;
	}

	public ArrayList<Survey> getAllUploadSurvey(String userId) {
		ArrayList<Survey> ss = new ArrayList<Survey>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId",// 0
				"_SurveyTitle", // 1
				"_id", // 2
				"_AddTo11" }, // 大树 添加一个字段 访问前说明
				"_IsDowned=? AND _SurveyEnable=?  AND _UserIdList LIKE '%" + userId + "%'", // 3
				new String[] { "1", "0" }, //
				null);//
		while (c.moveToNext()) {
			Survey s = new Survey();
			s.surveyId = c.getString(0);
			s.surveyTitle = c.getString(1);
			s.id = c.getInt(2);
			// 大树 访问前说明 添加
			s.setWord(c.getString(3));
			ss.add(s);
		}
		if (ss.size() > 1) {
			ss = Util.sort(ss);
		}
		c.close();
		return ss;
	}

	public String surveyExsit(String surveyId) {
		String exist = null;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_UserIdList" }, "_SurveyId=?", new String[] { surveyId }, null);
		if (c.moveToFirst()) {
			exist = c.getString(0);
		}
		c.close();
		return exist;
	}

	/**
	 * 获取存在的问卷
	 * 
	 * @param surveyId
	 * @return
	 */
	public Survey getSurveyExsit(String surveyId) {
		Survey s = null;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId",// 0
				"_SurveyTitle", // 1
				"_IsPhoto", // 2
				"_IsRecord", // 3
				"_IsExpand", // 4
				"_IsDowned", // 5
				"_SurveyEnable", // 6
				"_DownloadUrl", // 7
				"_PublishTime", // 8
				"_SurveyContent",// 9
				"_id",// 10
				"_VisitMode",// 11
				"_AddTo01",// 12
				"_AddTo02",// 13
				"_AddTo03",// 14
				"_AddTo04",// 15
				"_AddTo05",// 16
				"_AddTo06",// 17
				"_AddTo07",// 18
				"_AddTo08",// 19
				"_AddTo09",// 20
				"_AddTo10",// 21
				"_AddTo11", "_UserIdList", "_AddTo13" }, "_SurveyId=?", new String[] { surveyId }, null);
		if (c.moveToNext()) {
			s = new Survey();
			s.surveyId = c.getString(0);
			s.surveyTitle = c.getString(1);
			s.isPhoto = c.getInt(2);
			s.isRecord = c.getInt(3);
			s.isExpand = c.getInt(4);
			s.isDowned = c.getInt(5);
			s.surveyEnable = c.getInt(6);
			s.downloadUrl = c.getString(7);
			s.publishTime = c.getString(8);
			s.surveyContent = c.getString(9);
			s.id = c.getInt(10);
			s.visitMode = c.getInt(11);
			s.isTest = c.isNull(12) ? 0 : c.getInt(12);
			s.openStatus = c.isNull(13) ? 0 : c.getInt(13);
			s.innerDone = c.isNull(14) ? 0 : c.getInt(14);
			s.globalRecord = c.isNull(15) ? 0 : c.getInt(15);
			s.eligible = c.isNull(16) ? -1 : c.getInt(16);
			s.setParameter1(c.isNull(17) ? "" : c.getString(17));
			s.setParameter2(c.isNull(18) ? "" : c.getString(18));
			s.setParameter3(c.isNull(19) ? "" : c.getString(19));
			s.setParameter4(c.isNull(20) ? "" : c.getString(20));
			s.setPassword(c.getString(21));
			s.setWord(c.getString(22));
			s.userIdList = c.getString(23);
			s.setGeneratedTime(c.getString(24));
		}
		c.close();
		return s;
	}

	// /**
	// * 获取指定用户的问卷
	// *
	// * @param userId
	// * @return
	// */
	// public ArrayList<Survey> getSurveyList(String userId) {
	// ArrayList<Survey> surveys = new ArrayList<Survey>();
	// // ContentResolver resolver = mContext.getContentResolver();
	// Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" +
	// DBProvider.TAB_USER_SURVEY);
	// Cursor c = resolver.query(uri, new String[] { "_SurveyId" }, "_UserId=?",
	// new String[] { userId }, null);
	// while (c.moveToNext()) {
	// String sId = c.getString(0);
	// if (!Util.isEmpty(sId)) {
	// Survey s = getSurvey(sId);
	// if (null != s) {
	// surveys.add(s);
	// }
	// }
	// }
	// c.close();
	// return surveys;
	// }

	/**
	 * 添加问题 题外关联 之和 判断 !
	 * 
	 * 大树 添加 选项置顶 双引用 字段在这里 AddTo11 AddTo12
	 * 
	 * @param q
	 * @return
	 */
	public boolean addQuestion(Question q) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		ContentValues values = new ContentValues();
		values.put("_SurveyId", q.surveyId);// 1
		values.put("_QIndex", q.qIndex);// 2
		values.put("_QOrder", q.qOrder);// 3
		values.put("_QType", q.qType);// 4
		values.put("_QTypeStr", q.qTypeStr);// 5
		values.put("_QTitle", q.qTitle);// 6
		if (!Util.isEmpty(q.getRowItemStr())) {
			values.put("_RowsItem", q.getRowItemStr());// 7
		}
		if (!Util.isEmpty(q.getColItemStr())) {
			values.put("_ColumnsItem", q.getColItemStr());// 8
		}
		if (!Util.isEmpty(q.getResctItemStr())) {
			values.put("_RestrictionsItem", q.getResctItemStr());
		}
		values.put("_QTitleDisable", q.qTitleDisable);// 9
		values.put("_QTitlePos", q.qTitlePosition);// 10
		values.put("_QComment", q.qComment);// 11
		values.put("_QCommentPos", q.qCommentPosition);// 12
		values.put("_QScoreChecked", q.qScoreChecked);// 13
		values.put("_QWeightChecked", q.qWeightChecked);// 14
		values.put("_QDragChecked", q.qDragChecked);// 15
		values.put("_QRequired", q.qRequired);// 16
		values.put("_QRadomed", q.qRadomed);// 17
		values.put("_QInclusion", q.qInclusion);// 18
		values.put("_QExclude", q.qExclude);// 19
		values.put("_QSiteOption", q.qSiteOption);// 20
		values.put("_QPreSelect", q.qPreSelect);// 21
		values.put("_QAttach", q.qAttach);// 22
		values.put("_SizeWidth", q.sizeWidth);// 23
		values.put("_Deployment", q.deployment);// 24
		values.put("_QColumnsDirection", q.qColumnsDirection);// 25
		values.put("_IgnoreFirstItem", q.ignoreFirstItem);// 26
		values.put("_Caption", q.qCaption);// 27
		values.put("_QId", q.qid);// 28
		values.put("_FreeTextSort", q.freeTextSort);// 29
		values.put("_FreeSumNumber", q.freeSumNumber);// 29
		values.put("_FreeSymbol", q.freeSymbol);// 29
		values.put("_FreeMaxNumber", q.freeMaxNumber);// 30
		values.put("_FreeMinNumber", q.freeMinNumber);// 31
		values.put("_FreeTextColumn", q.freeTextColumn);// 35
		values.put("_RowsNum", q.rowsNum);// 36
		values.put("_QMatchQuestion", q.qMatchQuestion);// 37
		values.put("_QContinuous", q.qContinuous);// 38
		values.put("_TextAreaRows", q.textAreaRows);

		values.put("_MediaPosition", q.qMediaPosition);
		values.put("_MediaSrc", q.qMediaSrc);
		values.put("_MediaWidth", q.qMediaWidth);
		values.put("_MediaHeight", q.qMediaHeight);
		values.put("_QScore", q.qScore);

		values.put("_HaveOther", q.haveOther);
		values.put("_LowerBound", q.lowerBound);
		values.put("_UpperBound", q.upperBound);
		if (!Util.isEmpty(q.getTitleFrom())) {
			values.put("_TitleFrom", q.getTitleFrom());
		}

		values.put("_AddTo01", q.isHaveItemCap);
		values.put("_AddTo02", q.freeNoRepeat);
		// 单题拍照开始
		values.put("_AddTo03", q.qCamera);
		// values.put("_AddTo04", q.qCameraName);
		// 单题拍照结束
		// 单题签名
		values.put("_AddTo07", q.qSign);
		// 哑题是否是
		values.put("_AddTo08", q.qDumbFlag);
		// 三级联动是否是
		values.put("_AddTo09", q.qLinkage);
		// 题外关联 之和 判断是否
		values.put("_AddTo10", q.qParentAssociatedCheck);
		// 题外关联 之 选项置顶 判断 是否是 大树 选项置顶 1
		values.put("_AddTo11", q.isItemStick);
		// 大树 双引用 字段在这里
		values.put("_AddTo12", q.qSiteOption2);
		// 单复选矩阵固定
		values.put("_AddTo13", q.isFixed);
		// 大树排序 1
		values.put("_AddTo14", q.qSortChecked);

		values.put("_AddTo14", q.qSortChecked);
		// 矩阵右侧
		values.put("_AddTo16", q.isRight);
		//单选矩阵星评显示
		values.put("_AddTo15", q.qStarCheck);

		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	public boolean updateQuestion(Question q) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		ContentValues values = new ContentValues();
		// values.put("SurveyId", q.surveyId);//1
		// values.put("QIndex", q.qIndex);//2
		values.put("_QOrder", q.qOrder);// 3
		values.put("_QType", q.qType);// 4
		values.put("_QTypeStr", q.qTypeStr);// 5
		values.put("_QTitle", q.qTitle);// 6
		if (!Util.isEmpty(q.getRowItemStr())) {
			values.put("_RowsItem", q.getRowItemStr());// 7
		} else {
			values.put("_RowsItem", "");// 7
		}
		if (!Util.isEmpty(q.getColItemStr())) {
			values.put("_ColumnsItem", q.getColItemStr());// 8
		} else {
			values.put("_ColumnsItem", "");
		}
		if (!Util.isEmpty(q.getResctItemStr())) {
			values.put("_RestrictionsItem", q.getResctItemStr());
		} else {
			values.put("_RestrictionsItem", "");
		}
		values.put("_QTitleDisable", q.qTitleDisable);// 9
		values.put("_QTitlePos", q.qTitlePosition);// 10
		values.put("_QComment", q.qComment);// 11
		values.put("_QCommentPos", q.qCommentPosition);// 12
		values.put("_QScoreChecked", q.qScoreChecked);// 13
		values.put("_QWeightChecked", q.qWeightChecked);// 14
		values.put("_QDragChecked", q.qDragChecked);// 15
		values.put("_QRequired", q.qRequired);// 16
		values.put("_QRadomed", q.qRadomed);// 17
		values.put("_QInclusion", q.qInclusion);// 18
		values.put("_QExclude", q.qExclude);// 19
		values.put("_QSiteOption", q.qSiteOption);// 20
		values.put("_QPreSelect", q.qPreSelect);// 21
		values.put("_QAttach", q.qAttach);// 22
		values.put("_SizeWidth", q.sizeWidth);// 23
		values.put("_Deployment", q.deployment);// 24
		values.put("_QColumnsDirection", q.qColumnsDirection);// 25
		values.put("_IgnoreFirstItem", q.ignoreFirstItem);// 26
		values.put("_Caption", q.qCaption);// 27
		values.put("_QId", q.qid);// 28
		values.put("_FreeTextSort", q.freeTextSort);// 29
		values.put("_FreeSumNumber", q.freeSumNumber);// 29
		values.put("_FreeSymbol", q.freeSymbol);// 29
		values.put("_FreeMaxNumber", q.freeMaxNumber);// 30
		values.put("_FreeMinNumber", q.freeMinNumber);// 31
		values.put("_FreeTextColumn", q.freeTextColumn);// 35
		values.put("_RowsNum", q.rowsNum);// 36
		values.put("_QMatchQuestion", q.qMatchQuestion);// 37
		values.put("_QContinuous", q.qContinuous);// 38
		values.put("_TextAreaRows", q.textAreaRows);
		values.put("_MediaPosition", q.qMediaPosition);
		values.put("_MediaSrc", q.qMediaSrc);
		values.put("_MediaWidth", q.qMediaWidth);
		values.put("_MediaHeight", q.qMediaHeight);
		values.put("_QScore", q.qScore);
		values.put("_HaveOther", q.haveOther);
		values.put("_LowerBound", q.lowerBound);
		values.put("_UpperBound", q.upperBound);
		// 单题拍照开始
		values.put("_AddTo03", q.qCamera);
		// values.put("_AddTo04", q.qCameraName);
		// 单题拍照结束
		// 单题签名
		values.put("_AddTo07", q.qSign);
		if (!Util.isEmpty(q.getTitleFrom())) {
			values.put("_TitleFrom", q.getTitleFrom());
		} else {
			values.put("_TitleFrom", "");
		}
		values.put("_AddTo01", q.isHaveItemCap);
		values.put("_AddTo02", q.freeNoRepeat);
		// 哑题是否是
		values.put("_AddTo08", q.qDumbFlag);
		// 三级联动是否是
		values.put("_AddTo09", q.qLinkage);
		// 题外关联 之和 是否
		values.put("_AddTo10", q.qParentAssociatedCheck);
		// 题外关联 之 选项置顶 判断 是否是 大树 选项置顶 1
		values.put("_AddTo11", q.isItemStick);
		// 大树 双引用 字段在这里
		values.put("_AddTo12", q.qSiteOption2);
		// 单复选矩阵固定
		values.put("_AddTo13", q.isFixed);
		// 大树排序 2
		values.put("_AddTo14", q.qSortChecked);
		// 矩阵右侧
		values.put("_AddTo16", q.isRight);
		//单选矩阵星评显示
		values.put("_AddTo15", q.qStarCheck);
		int i = resolver.update(uri, values, "_SurveyId=? AND _QIndex=?", new String[] { q.surveyId, String.valueOf(q.qIndex) });
		return 0 != i;
	}

	public void deleteQuestion(String surveyId, String index) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		resolver.delete(uri, "_SurveyId=? AND _QIndex=?", new String[] { surveyId, index });
	}

	/**
	 * 获取指问卷的所有问题
	 * 
	 * @return
	 */
	public ArrayList<Question> getQuestionList(String surveyId) {
		ArrayList<Question> qs = new ArrayList<Question>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		Cursor c = resolver.query(uri, new String[] { "_id", // 0
				"_SurveyId",// 1
				"_QIndex", // 2
				"_QOrder",// 3
				"_QType", // 4
				"_QTypeStr", // 5
				"_QTitle", // 6
				"_RowsItem", // 7
				"_ColumnsItem",// 8
				"_QTitleDisable",// 9
				"_QTitlePos",// 10
				"_QComment",// 11
				"_QCommentPos",// 12
				"_QScoreChecked",// 13
				"_QWeightChecked",// 14
				"_QDragChecked",// 15
				"_QRequired",// 16
				"_QRadomed",// 17
				"_QInclusion",// 18
				"_QExclude",// 19
				"_QSiteOption",// 20
				"_QPreSelect",// 21
				"_QAttach",// 22
				"_SizeWidth",// 23
				"_Deployment",// 24
				"_QColumnsDirection",// 25
				"_IgnoreFirstItem",// 26
				"_Caption",// 27
				"_QId",// 28
				"_FreeTextSort",// 29
				"_FreeMaxNumber",// 30
				"_FreeMinNumber",// 31
				// "FreeInputExclude",//32
				// "FreeInputValue",//33
				// "FreeInputText",//34
				// "FreeInputItems",//32
				"_FreeTextColumn",// 32
				"_RowsNum",// 33
				"_QMatchQuestion",// 34
				"_QContinuous",// 35
				"_TextAreaRows",/* 36 */
				"_RestrictionsItem",// 37
				"_MediaPosition",// 38
				"_MediaSrc",// 39
				"_MediaWidth",// 40
				"_MediaHeight",// 41
				"_QScore",// 42
				"_HaveOther",// 43
				"_LowerBound",// 44
				"_UpperBound",// 45
				"_FreeSumNumber",// 46
				"_FreeSymbol",// 47
				"_TitleFrom", // 48
				"_AddTo01",// 49
				"_AddTo02",// 50
				"_AddTo03",// 51是否可以单题拍照
				"_AddTo04",// 52单题拍照的名字
				"_AddTo05",// 53干预
				"_AddTo06",// 54题组随机
				"_AddTo07",// 55单题签名
				"_AddTo08",// 56哑题是否是
				"_AddTo09", // 57三级联动是否是
				"_AddTo10", // 58题外关联 之和 是否是
				"_AddTo11", // 59题外关联 之 选项置顶 大树 添加
				"_AddTo12", // 60双引用 大树 添加
				"_AddTo13", // 61单复选矩阵固定
				"_AddTo14", // 62选项排序 大树排序 3
				"_AddTo16",// 63矩阵右侧
				"_AddTo15" //64单选矩阵星评显示
		}, "_SurveyId=? AND _QOrder<>-1", new String[] { surveyId }, "_QOrder ASC");
		while (c.moveToNext()) {
			Question q = new Question();
			q.id = c.getInt(0);
			q.surveyId = c.getString(1);
			q.qIndex = c.getInt(2);
			q.qOrder = c.getInt(3);
			q.qType = c.getInt(4);
			q.qTypeStr = c.getString(5);
			q.qTitle = c.getString(6);
			q.setRowItemStr(c.getString(7));
			q.setColItemStr(c.getString(8));
			q.qTitleDisable = c.getInt(9);
			q.qTitlePosition = c.getString(10);
			q.qComment = c.getString(11);
			q.qCommentPosition = c.getString(12);
			q.qScoreChecked = c.getInt(13);
			q.qWeightChecked = c.getInt(14);
			q.qDragChecked = c.getInt(15);
			q.qRequired = c.getInt(16);
			q.qRadomed = c.getInt(17);
			q.qInclusion = c.getString(18);
			q.qExclude = c.getInt(19);
			q.qSiteOption = c.getString(20);
			q.qPreSelect = c.getInt(21);
			q.qAttach = c.getInt(22);
			q.sizeWidth = c.getInt(23);
			q.deployment = c.getString(24);
			q.qColumnsDirection = c.getString(25);
			q.ignoreFirstItem = c.getInt(26);
			q.qCaption = c.getString(27);
			q.qid = c.getString(28);
			q.freeTextSort = c.getString(29);
			q.freeMaxNumber = c.getString(30);
			q.freeMinNumber = c.getString(31);
			// q.setFreeItemStr(c.getString(32));
			// q.freeInputExclude = c.getString(32);
			// q.freeInputValue = c.getInt(33);
			// q.freeInputText = c.getString(34);
			q.freeTextColumn = c.getInt(32);
			q.rowsNum = c.getInt(33);
			q.qMatchQuestion = c.getInt(34);
			q.qContinuous = c.getInt(35);
			q.textAreaRows = c.getInt(36);
			q.setResctItemStr(c.getString(37));
			q.qMediaPosition = c.getString(38);
			q.qMediaSrc = c.getString(39);
			q.qMediaWidth = c.getInt(40);
			q.qMediaHeight = c.getInt(41);
			q.qScore = c.getString(42);
			q.haveOther = c.getInt(43);
			q.lowerBound = c.getInt(44);
			q.upperBound = c.getInt(45);
			q.freeSumNumber = c.getString(46);
			q.freeSymbol = c.getString(47);
			q.setTitleFrom(c.getString(48));
			q.isHaveItemCap = c.isNull(49) ? 0 : c.getInt(49);
			q.freeNoRepeat = c.isNull(50) ? 0 : c.getInt(50);// 答案是否允许重复
			q.qCamera = c.isNull(51) ? 0 : c.getInt(51);// 是否单题拍照
			// q.qCameraName=c.getString(52);//单题拍照文件的名称
			q.setInterventionStr(c.getString(53));// 干预
			q.setGroupStr(c.getString(54));// 题组随机
			// 单题签名
			q.qSign = c.isNull(55) ? 0 : c.getInt(55);// 是否单题
			// 哑题是否是
			q.qDumbFlag = c.isNull(56) ? 0 : c.getInt(56);
			// 三级联动是否是
			q.qLinkage = c.isNull(57) ? 0 : c.getInt(57);
			// 题外关联 之和是否是
			q.qParentAssociatedCheck = c.isNull(58) ? "" : c.getString(58);
			// 选项置顶 4 大树 添加
			q.isItemStick = c.isNull(59) ? "" : c.getString(59);
			// 双引用 大树 添加
			q.qSiteOption2 = c.isNull(60) ? "" : c.getString(60);
			q.isFixed = c.getInt(61);// 单复选矩阵固定
			q.qSortChecked = c.getInt(62);// 选项排序 大树排序 4
			// 矩阵右侧
			q.isRight = c.isNull(63) ? 0 : c.getInt(63);
			q.qStarCheck = c.isNull(64) ? 0 : c.getInt(64);
			qs.add(q);
		}
		c.close();
		return qs;
	}

	/**
	 * 根据问卷号和问题的顺序号获取指定的问题
	 * 
	 * @param surveyId
	 * @param order
	 * @return
	 */
	public Question getQuestion(String surveyId, String index) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		Cursor c = resolver.query(uri, new String[] { "_id", // 0
				"_SurveyId",// 1
				"_QIndex", // 2
				"_QOrder",// 3
				"_QType", // 4
				"_QTypeStr", // 5
				"_QTitle", // 6
				"_RowsItem", // 7
				"_ColumnsItem",// 8
				"_QTitleDisable",// 9
				"_QTitlePos",// 10
				"_QComment",// 11
				"_QCommentPos",// 12
				"_QScoreChecked",// 13
				"_QWeightChecked",// 14
				"_QDragChecked",// 15
				"_QRequired",// 16
				"_QRadomed",// 17
				"_QInclusion",// 18
				"_QExclude",// 19
				"_QSiteOption",// 20
				"_QPreSelect",// 21
				"_QAttach",// 22
				"_SizeWidth",// 23
				"_Deployment",// 24
				"_QColumnsDirection",// 25
				"_IgnoreFirstItem",// 26
				"_Caption",// 27
				"_QId",// 28
				"_FreeTextSort",// 29
				"_FreeMaxNumber",// 30
				"_FreeMinNumber",// 31
				"_FreeTextColumn",// 32
				"_RowsNum",// 33
				"_QMatchQuestion",// 34
				"_QContinuous",// 35
				"_TextAreaRows",// 36
				"_RestrictionsItem",// 37
				"_MediaPosition",// 38
				"_MediaSrc",// 39
				"_MediaWidth",// 40
				"_MediaHeight",// 41
				"_QScore",/* 42 */
				"_HaveOther",// 43
				"_LowerBound",// 44
				"_UpperBound",// 45
				"_FreeSumNumber",// 46
				"_FreeSymbol",// 47
				"_TitleFrom",// 48
				"_AddTo01", // 49
				"_AddTo02",// 50
				"_AddTo03",// 51
				"_AddTo04",// 52
				"_AddTo05",// 53
				"_AddTo06",// 54题组随机
				"_AddTo07",// 55单题签名
				"_AddTo08",// 56哑题是否是
				"_AddTo09",// 57三级联动是否是
				"_AddTo10",// 58题外关联之和 是否是
				"_AddTo11",// 59题外关联 之选项置顶 大树 添加
				"_AddTo12", // 60双引用 大树 添加
				"_AddTo14", // 61选项排序 大树排序 5
				"_Addto16", // 63矩阵右侧
				"_AddTo15" //64单选矩阵星评显示
		}, "_SurveyId=? AND _QIndex=?", new String[] { surveyId, index }, null);
		Question q = null;
		if (c.moveToFirst()) {
			q = new Question();
			q.id = c.getInt(0);
			q.surveyId = c.getString(1);
			q.qIndex = c.getInt(2);
			q.qOrder = c.getInt(3);
			q.qType = c.getInt(4);
			q.qTypeStr = c.getString(5);
			q.qTitle = c.getString(6);
			q.setRowItemStr(c.getString(7));
			q.setColItemStr(c.getString(8));
			q.qTitleDisable = c.getInt(9);
			q.qTitlePosition = c.getString(10);
			q.qComment = c.getString(11);
			q.qCommentPosition = c.getString(12);
			q.qScoreChecked = c.getInt(13);
			q.qWeightChecked = c.getInt(14);
			q.qDragChecked = c.getInt(15);
			q.qRequired = c.getInt(16);
			q.qRadomed = c.getInt(17);
			q.qInclusion = c.getString(18);
			q.qExclude = c.getInt(19);
			q.qSiteOption = c.getString(20);
			q.qPreSelect = c.getInt(21);
			q.qAttach = c.getInt(22);
			q.sizeWidth = c.getInt(23);
			q.deployment = c.getString(24);
			q.qColumnsDirection = c.getString(25);
			q.ignoreFirstItem = c.getInt(26);
			q.qCaption = c.getString(27);
			q.qid = c.getString(28);
			q.freeTextSort = c.getString(29);
			q.freeMaxNumber = c.getString(30);
			q.freeMinNumber = c.getString(31);
			q.freeTextColumn = c.getInt(32);
			q.rowsNum = c.getInt(33);
			q.qMatchQuestion = c.getInt(34);
			q.qContinuous = c.getInt(35);
			q.textAreaRows = c.getInt(36);
			q.setResctItemStr(c.getString(37));
			q.qMediaPosition = c.getString(38);
			q.qMediaSrc = c.getString(39);
			q.qMediaWidth = c.getInt(40);
			q.qMediaHeight = c.getInt(41);
			q.qScore = c.getString(42);
			q.haveOther = c.getInt(43);
			q.lowerBound = c.getInt(44);
			q.upperBound = c.getInt(45);
			q.freeSumNumber = c.getString(46);
			q.freeSymbol = c.getString(47);
			q.setTitleFrom(c.getString(48));
			q.isHaveItemCap = c.isNull(49) ? 0 : c.getInt(49);
			q.freeNoRepeat = c.isNull(50) ? 0 : c.getInt(50);
			q.qCamera = c.isNull(51) ? 0 : c.getInt(51);
			// q.qCameraName=c.getString(52);
			q.setInterventionStr(c.getString(53));
			q.setGroupStr(c.getString(54));
			// 单题签名
			q.qSign = c.isNull(55) ? 0 : c.getInt(55);// 是否单题
			// 哑题是否是
			q.qDumbFlag = c.isNull(56) ? 0 : c.getInt(56);
			// 三级联动是否是
			q.qLinkage = c.isNull(57) ? 0 : c.getInt(57);
			// 题外关联之和 是否是
			q.qParentAssociatedCheck = c.isNull(58) ? "" : c.getString(58);
			// 选项置顶 4 大树 添加
			q.isItemStick = c.isNull(59) ? "" : c.getString(59);
			// 双引用 大树 添加
			q.qSiteOption2 = c.isNull(60) ? "" : c.getString(60);
			// 大树排序 6
			q.qSortChecked = c.isNull(62) ? 0 : c.getInt(62);
			// 矩阵右侧
			q.isRight = c.isNull(63) ? 0 : c.getInt(63);
			q.qStarCheck = c.isNull(64) ? 0 : c.getInt(64);
		}
		c.close();
		return q;
	}

	public boolean isQuestionExist(String surveyId, int index) {
		boolean is = false;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, "_SurveyId=? AND _QIndex=?", new String[] { surveyId, String.valueOf(index) }, null);
		if (c.moveToFirst()) {
			is = (0 < c.getLong(0));
		}
		c.close();
		return is;
	}

	/**
	 * 根据问卷号和问题的顺序号获取指定的问order
	 * 
	 * @param surveyId
	 * @param order
	 * @return
	 */
	public int getQuestionOrder(String surveyId, int index) {
		int o = -1;
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		Cursor c = resolver.query(uri, //
				new String[] { "_QOrder" }, //
				"_SurveyId=? AND _QIndex=?", //
				new String[] { surveyId, String.valueOf(index) }, null);
		if (c.moveToFirst()) {
			o = c.getInt(0);
		}
		c.close();
		return o;
	}

	/**
	 * 监控用 根据问卷号和问题的顺序号获取指定的问order
	 * 
	 * @param surveyId
	 * @param order
	 * @return
	 */
	public int getQuestionIndex(String surveyId, int order) {
		int o = -1;
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		Cursor c = resolver.query(uri, //
				new String[] { "_QIndex" }, //
				"_SurveyId=? AND _QOrder=?", //
				new String[] { surveyId, String.valueOf(order) }, null);
		if (c.moveToFirst()) {
			o = c.getInt(0);
		}
		c.close();
		return o;
	}

	public Answer getAnswer(String uuid, String qIndex) {
		if (Util.isEmpty(uuid) || null == qIndex) {
			return null;
		}
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		Answer an = null;
		Cursor c = resolver.query(uri, new String[] { "_id", // 0
				"_UserId", // 1
				"_SurveyId", // 2
				"_QuestionIndex", // 3
				"_QuestionOrder", // 4
				"_AnswerType", // 5
				"_AnswerMap", // 6
				"_MediaPath",// 7
				"_MediaName",// 8
				"_UUID",// 9
				"_Enable" }, // 10
				"_UUID=? AND _QuestionIndex=? AND _Enable=?", //
				new String[] { uuid, qIndex, String.valueOf(1) }, null);
		if (c.moveToFirst()) {
			an = new Answer();
			an.id = c.getInt(0);
			an.userId = c.getString(1);
			an.surveyId = c.getString(2);
			an.qIndex = c.getInt(3);
			an.qOrder = c.getInt(4);
			an.answerType = c.getInt(5);
			an.setAnswerMapStr(c.getString(6));
			an.mediaPath = c.getString(7);
			an.mediaName = c.getString(8);
			an.uuid = c.getString(9);
			an.enable = c.getInt(10);
		}
		c.close();
		return an;
	}

	public Answer getAnswerByOrder(String uuid, Integer qIndex) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		Answer an = null;
		Cursor c = resolver.query(uri, new String[] { "_id", // 0
				"_UserId", // 1
				"_SurveyId", // 2
				"_QuestionIndex", // 3
				"_QuestionOrder", // 4
				"_AnswerType", // 5
				"_AnswerMap", // 6
				"_MediaPath",// 7
				"_MediaName",// 8
				"_UUID",// 9
				"_Enable" }, // 10
				"_UUID=? AND _QuestionOrder=? AND _Enable=?", //
				new String[] { uuid, String.valueOf(qIndex), String.valueOf(1) }, null);
		if (c.moveToFirst()) {
			an = new Answer();
			an.id = c.getInt(0);
			an.userId = c.getString(1);
			an.surveyId = c.getString(2);
			an.qIndex = c.getInt(3);
			an.qOrder = c.getInt(4);
			an.answerType = c.getInt(5);
			an.setAnswerMapStr(c.getString(6));
			an.mediaPath = c.getString(7);
			an.mediaName = c.getString(8);
			an.uuid = c.getString(9);
			an.enable = c.getInt(10);
		}
		c.close();
		return an;
	}

	/**
	 * 用于一般查询
	 * 
	 * @param uuid
	 * @param qIndex
	 * @return
	 */
	public Answer getAnswerByIndex(String uuid, String qIndex) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		Answer an = null;
		Cursor c = resolver.query(uri, new String[] { "_id", // 0
				"_UserId", // 1
				"_SurveyId", // 2
				"_QuestionIndex", // 3
				"_QuestionOrder", // 4
				"_AnswerType", // 5
				"_AnswerMap", // 6
				"_MediaPath",// 7
				"_MediaName",// 8
				"_UUID",// 9
				"_Enable" }, // 10
				"_UUID=? AND _QuestionIndex=? AND _Enable=?", //
				new String[] { uuid, qIndex, String.valueOf(1) }, null);
		if (c.moveToFirst()) {
			an = new Answer();
			an.id = c.getInt(0);
			an.userId = c.getString(1);
			an.surveyId = c.getString(2);
			an.qIndex = c.getInt(3);
			an.qOrder = c.getInt(4);
			an.answerType = c.getInt(5);
			an.setAnswerMapStr(c.getString(6));
			an.mediaPath = c.getString(7);
			an.mediaName = c.getString(8);
			an.uuid = c.getString(9);
			an.enable = c.getInt(10);
		}
		c.close();
		return an;
	}

	public ArrayList<Answer> getUserAllAnswer(String uuid) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		ArrayList<Answer> ans = new ArrayList<Answer>();
		Cursor c = resolver.query(uri, new String[] { "_id", "_UserId", "_SurveyId", //
				"_QuestionIndex", "_QuestionOrder", //
				"_AnswerType", "_AnswerMap", //
				"_MediaPath", "_MediaName", "_UUID", "_Enable" }, //
				"_UUID=? AND _Enable=?", //
				new String[] { uuid, String.valueOf(1) }, " _QuestionIndex ASC");
		while (c.moveToNext()) {
			Answer an = new Answer();
			an.id = c.getInt(0);
			an.userId = c.getString(1);
			an.surveyId = c.getString(2);
			an.qIndex = c.getInt(3);
			an.qOrder = c.getInt(4);
			an.answerType = c.getInt(5);
			an.setAnswerMapStr(c.getString(6));
			an.mediaPath = c.getString(7);
			an.mediaName = c.getString(8);
			an.uuid = c.getString(9);
			an.enable = c.getInt(10);
			// return an;
			ans.add(an);
		}
		c.close();
		return ans;
	}

	public Integer updateAnswer(Answer anw) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		ContentValues values = new ContentValues();
		values.put("_QuestionOrder", anw.qOrder);
		values.put("_AnswerMap", anw.getAnswerMapStr());
		values.put("_MediaPath", anw.mediaPath);
		values.put("_MediaName", anw.mediaName);
		values.put("_Enable", anw.enable);
		int i = resolver.update(uri, values, "_UUID=? AND _QuestionIndex=?", new String[] { anw.uuid, String.valueOf(anw.qIndex) });
		return i;
	}

	/**
	 * 将答案值置为空
	 * 
	 * @param uuid
	 * @param index
	 * @param order
	 * @return
	 */
	public Integer updateAnswer2Null(String uuid, String index, int order) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		ContentValues values = new ContentValues();
		values.put("_QuestionOrder", order);
		values.put("_AnswerMap", "");
		int i = resolver.update(uri, values, "_UUID=? AND _QuestionIndex=?", new String[] { uuid, index });
		return i;
	}

	/**
	 * 问卷题目的order发生变化时， 本地保存的答案的order也须与题目的order保持一致
	 * 
	 * @param q
	 */
	public void updateAnswerOrder(Question q) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		ContentValues values = new ContentValues();
		values.put("_QuestionOrder", q.qOrder);
		values.put("_AnswerType", q.qType);
		resolver.update(uri, values, "_SurveyId=? AND _QuestionIndex=?", new String[] { q.surveyId, String.valueOf(q.qIndex) });
	}

	/**
	 * 删除order=-1问题的答案
	 * 
	 * @param surveyId
	 * @param index
	 */
	public void deleteAnswer(String surveyId, String index) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		resolver.delete(uri, "_SurveyId=? AND _QuestionIndex=?", new String[] { surveyId, index });
	}

	public Integer updateAnswerUnEnable(Answer anw) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		ContentValues values = new ContentValues();
		values.put("_Enable", 0);
		int i = resolver.update(uri, values, "_UUID=? AND _QuestionIndex=?", new String[] { anw.uuid, String.valueOf(anw.qIndex) });
		return i;
	}

	public Integer addAnswer(Answer anw) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		ContentValues values = new ContentValues();
		values.put("_UserId", anw.userId);
		values.put("_SurveyId", anw.surveyId);
		values.put("_QuestionIndex", anw.qIndex);
		values.put("_QuestionOrder", anw.qOrder);
		values.put("_AnswerType", anw.answerType);
		values.put("_AnswerMap", anw.getAnswerMapStr());
		values.put("_MediaPath", anw.mediaPath);
		values.put("_MediaName", anw.mediaName);
		values.put("_Enable", anw.enable);
		values.put("_UUID", anw.uuid);
		Uri u = resolver.insert(uri, values);
		return Util.getTableRowId(u.toString());
	}

	/**
	 * 统计已完成的
	 * 
	 * @param surveyId
	 * @return
	 */
	public long feedCompletedCount(String surveyId, String userId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, "_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=?", new String[] { surveyId, "1", "1", "0", userId },
				null);
		if (c.moveToFirst()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}

	/**
	 * 查询所有已完成且需要上传的UploadFeed
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getAllCompletedUploadFeed(String surveyId, String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", //
				"_FilePath", "_FileName", "_FileSize", "_FileType", "_IsUploaded" }, //
				"_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=?", //
				new String[] { surveyId, "1", "1", "0", userId }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUuid(c.getString(1));
			feed.setSurveyId(c.getString(2));
			feed.setPath(c.getString(3));
			feed.setName(c.getString(4));
			feed.setSize(c.getLong(5));
			feed.setType(c.getInt(6));
			feed.setIsUploaded(c.getInt(7));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 查询所有已完成且需要上传的UploadFeed
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getAllInCompletedUploadFeed(String surveyId, String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);//
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", "_FilePath",//
				"_FileName", "_FileSize", "_FileType", "_IsUploaded" }, //
				"_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=?", //
				new String[] { surveyId, "0", "1", "0", userId }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUuid(c.getString(1));
			feed.setSurveyId(c.getString(2));
			feed.setPath(c.getString(3));
			feed.setName(c.getString(4));
			feed.setSize(c.getLong(5));
			feed.setType(c.getInt(6));
			feed.setIsUploaded(c.getInt(7));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 统计已完成的且未上传的
	 * 
	 * @param surveyId
	 * @return
	 */
	public long feedUnUploadCount(String surveyId, String userId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, "_SurveyId=? AND _IsCompleted=? AND _IsUploaded=? AND _FileType=? AND _GiveUp=? AND _UserId=?", new String[] { surveyId, "1",
				"0", "1", "0", userId }, null);
		if (c.moveToFirst()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}

	/**
	 * 未完成且未上报
	 * 
	 * @param surveyId
	 * @return
	 */
	public long feedIncompletedUnUploadCount(String surveyId, String userId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, //
				new String[] { "COUNT(_id)" }, "_SurveyId=? AND _IsCompleted=? AND _IsUploaded=? AND _FileType=? AND _GiveUp=? AND _UserId=?", //
				new String[] { surveyId, "0", "0", "1", "0", userId }, null);//
		if (c.moveToFirst()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}

	public void addUploadFeed(UploadFeed feed) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_UserId", feed.getUserId());
		values.put("_SurveyId", feed.getSurveyId());
		values.put("_UUID", feed.getUuid());
		if (!Util.isEmpty(feed.getVisitAddress())) {
			values.put("_VisitAddress", feed.getVisitAddress());
		}
		values.put("_CreateTime", feed.getCreateTime());
		values.put("_FilePath", feed.getPath());
		values.put("_FileName", feed.getName());
		values.put("_StartTime", feed.getStartTime());
		values.put("_IsCompleted", Cnt.VISIT_STATE_INTERRUPT);
		values.put("_FileType", feed.getType());
		values.put("_Lat", feed.getLat());
		values.put("_Lng", feed.getLat());
		values.put("_VisitMode", feed.getVisitMode());
		values.put("_FeedId", feed.getFeedId());
		resolver.insert(uri, values);
	}

	// 大树拒访 1
	public boolean updateUploadFeed(UploadFeed feed) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		// values.put("_UserId", feed.getUserId());
		values.put("_IsCompleted", feed.getIsCompleted());
		values.put("_IsUploaded", Cnt.UPLOAD_STATE_UNUPLOAD);
		values.put("_StartTime", feed.getStartTime());
		values.put("_RegTime", feed.getRegTime());
		values.put("_Spent", feed.getSpent());
		values.put("_FileSize", feed.getSize());
		values.put("_IndexArr", feed.getIndexArr());

		if (!Util.isEmpty(feed.getLotsCoord())) {
			/**
			 * 更新每一次访问坐标点集合
			 */
			values.put("_LotsCoord", feed.getLotsCoord());
		}
		if (!Util.isEmpty(feed.getManyPlaces())) {
			/**
			 * 更新每一次访问地点集合
			 */
			values.put("_ManyPlaces", feed.getManyPlaces());
		}
		if (!Util.isEmpty(feed.getManyTimes())) {
			/**
			 * 更新每一次访问时间集合
			 */
			values.put("_ManyTimes", feed.getManyTimes());
		}
		values.put("_Lat", feed.getLat());
		values.put("_Lng", feed.getLng());
		if (!Util.isEmpty(feed.getVisitAddress())) {
			/**
			 * 更新访问的地点
			 */
			values.put("_VisitAddress", feed.getVisitAddress());
		}

		if (!Util.isEmpty(feed.getGroupSequence())) {
			// 加入题组随机顺序号
			values.put("_AddTo06", feed.getGroupSequence());
		}
		// 目录
		if (!Util.isEmpty(feed.getDirectory())) {
			values.put("_AddTo08", feed.getDirectory());
		}
		// values.put("_AddTo02", feed.getScreenOrientation());
		// 拒访
		values.put("_AddTo09", feed.getReturnTypeId());
		return 0 != resolver.update(uri, values, "_FileName=?", new String[] { feed.getName() });
	}

	public boolean updateUploadFeed(String feedName, int isUploaded) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_IsUploaded", isUploaded);
		return 0 != resolver.update(uri, values, "_FileName=?", new String[] { feedName });
	}

	public int updateFeedStatus(String uuid, int isUploaded) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_IsUploaded", isUploaded);
		return resolver.update(uri, values, "_UUID=? AND _IsUploaded<>9", new String[] { uuid });
	}

	public int updateFeedStatusByName(String fileName, int isUploaded) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_IsUploaded", isUploaded);
		return resolver.update(uri, values, "_FileName=? AND _FileType=1", new String[] { fileName });
	}

	public boolean updateUploadFeedStatus(UploadFeed feed) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_IsUploaded", feed.getIsUploaded());
		values.put("_ReturnType", feed.getReturnType());
		values.put("_FeedId", feed.getFeedId());
		return 0 != resolver.update(uri, values, "_FileName=?", new String[] { feed.getName() });
	}

	/**
	 * 获取该SID下的个数
	 * 
	 * @return
	 */
	public long getAllXmlCount(String surveyId, String userId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"count(_id)" }, "_SurveyId=? AND _FileType=? AND _GiveUp<>? AND _UserId=?", //
				new String[] { surveyId, String.valueOf(Cnt.FILE_TYPE_XML), "1", userId }, null);
		if (c.moveToNext()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}
	
	/**
	 * 获取mp3的个数
	 * 
	 * @return
	 */
	public long getAllmp3Count(String uuid, String userId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"count(_id)" }, "_Uuid=? AND _FileType=? AND _GiveUp<>? AND _UserId=? AND _FileSize>0", //
				new String[] { uuid, String.valueOf(Cnt.FILE_TYPE_MP3), "1", userId }, null);
		if (c.moveToNext()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}
	/**
	 * 获取mp3的个数
	 * 
	 * @return
	 */
	public long getAllmp4Count(String uuid, String userId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"count(_id)" }, "_Uuid=? AND _FileType=? AND _GiveUp<>? AND _UserId=? AND _FileSize>0", //
				new String[] { uuid, String.valueOf(Cnt.FILE_TYPE_MP4), "1", userId }, null);
		if (c.moveToNext()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}
	
	
	
	/**
	 * 获取所有的XML文件 大树拒访 2
	 * 
	 * @return
	 */
	public ArrayList<UploadFeed> getAllXmlUploadFeedList(String surveyId, String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"_id", // 0
						"_UserId", // 1
						"_FeedId", // 2
						"_SurveyId", // 3
						"_UUID", // 4
						"_FilePath", // 5
						"_VisitAddress", // 6
						"_FileName", // 7
						"_CreateTime", // 8
						"_StartTime", // 9
						"_RegTime", // 10
						"_FileSize", // 11
						"_IsUploaded", // 12
						"_IsCompleted", // 13
						"_isSync", // 14
						"_FileType", // 15
						"_Spent", // 16
						"_ManyTimes", // 17
						"_Lat", // 18
						"_Lng", // 19
						"_VisitMode",// 20
						"_ReturnType",// 21
						"_IndexArr",// 22
						"_LotsCoord",// 23
						"_ManyPlaces",// 24
						// "_AddTo02",//已废除, 25纵向或横向, 0纵向, 1横向
						"_AddTo03",// 25内部名单json串
						"_AddTo06",// 26手动题组随机的序列号
						"_AddTo04",// 27pid
						"_AddTo07",// 28引用受访者参数
						"_AddTo08",// 29目录导航
						"_AddTo09" // 30拒访字段
				}, "_SurveyId=? AND _FileType=? AND _GiveUp<>? AND _UserId=?", //
				new String[] { surveyId, String.valueOf(Cnt.FILE_TYPE_XML), "1", userId }, "_RegTime DESC");
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUserId(c.getString(1));
			feed.setFeedId(c.getString(2));
			feed.setSurveyId(c.getString(3));
			feed.setUuid(c.getString(4));
			feed.setPath(c.getString(5));
			feed.setVisitAddress(c.getString(6));
			feed.setName(c.getString(7));
			feed.setCreateTime(c.getLong(8));
			feed.setStartTime(c.getLong(9));
			feed.setRegTime(c.getLong(10));
			feed.setSize(c.getLong(11));
			feed.setIsUploaded(c.getInt(12));
			feed.setIsCompleted(c.getInt(13));
			feed.setIsSync(c.getInt(14));
			feed.setType(c.getInt(15));
			feed.setSpent(c.getLong(16));
			feed.setManyTimes(c.getString(17));
			feed.setLat(c.getString(18));
			feed.setLng(c.getString(19));
			feed.setVisitMode(c.getInt(20));
			feed.setReturnType(c.getString(21));
			feed.setIndexArr(c.getString(22));
			feed.setLotsCoord(c.getString(23));
			feed.setManyPlaces(c.getString(24));
			// feed.setScreenOrientation(c.isNull(25)?0:c.getInt(25));
			feed.setInnerPanelStr(c.isNull(25) ? null : c.getString(25));
			feed.setGroupSequence(c.isNull(26) ? null : c.getString(26));
			feed.setPid(c.isNull(27) ? "0" : c.getString(27));
			// 引用受访者参数
			feed.setParametersStr(c.isNull(28) ? null : c.getString(28));
			// 目录记录查询
			feed.setDirectory(c.isNull(29) ? null : c.getString(29));
			// 拒访
			feed.setReturnTypeId(c.getInt(30));
			fs.add(feed);
		}
		c.close();
		return fs;
	}
	/**
	 * 获取所有有效配额
	 * 
	 * @return
	 */
	public ArrayList<UploadFeed> getAllXmlUploadFeedList(String surveyId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"_id", // 0
						"_UserId", // 1
						"_FeedId", // 2
						"_SurveyId", // 3
						"_UUID" // 4
						}, "_SurveyId=? AND _FileType=? AND _GiveUp<>?", //
				new String[] { surveyId, String.valueOf(Cnt.FILE_TYPE_XML), "1",}, "_RegTime DESC");
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUserId(c.getString(1));
			feed.setFeedId(c.getString(2));
			feed.setSurveyId(c.getString(3));
			feed.setUuid(c.getString(4));
			fs.add(feed);
		}
		c.close();
		return fs;
	}
	/**
	 * 获取所有的XML文件 增加pid
	 * 
	 * @return
	 */
	public ArrayList<UploadFeed> searchXmlUploadFeedList(String surveyId, String userId, int px, String content) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append("Parameter" + px);
		sb.append("\":{\"content\":\"");
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"_id", // 0
						"_UserId", // 1
						"_FeedId", // 2
						"_SurveyId", // 3
						"_UUID", // 4
						"_FilePath", // 5
						"_VisitAddress", // 6
						"_FileName", // 7
						"_CreateTime", // 8
						"_StartTime", // 9
						"_RegTime", // 10
						"_FileSize", // 11
						"_IsUploaded", // 12
						"_IsCompleted", // 13
						"_isSync", // 14
						"_FileType", // 15
						"_Spent", // 16
						"_ManyTimes", // 17
						"_Lat", // 18
						"_Lng", // 19
						"_VisitMode",// 20
						"_ReturnType",// 21
						"_IndexArr",// 22
						"_LotsCoord",// 23
						"_ManyPlaces",// 24
						// "_AddTo02",//已废除, 25纵向或横向, 0纵向, 1横向
						"_AddTo03",// 25内部名单json串
						"_AddTo06",// 26手动题组随机的序列号
						"_AddTo04",// 27pid
						"_AddTo07",// 28引用受访者参数
						"_AddTo08"// 29目录
				}, "_SurveyId=? AND _FileType=? AND _GiveUp<>? AND _UserId=? AND _AddTo03 LIKE '%" + sb.toString() + "%'", //
				new String[] { surveyId, String.valueOf(Cnt.FILE_TYPE_XML), "1", userId }, "_RegTime DESC");
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUserId(c.getString(1));
			feed.setFeedId(c.getString(2));
			feed.setSurveyId(c.getString(3));
			feed.setUuid(c.getString(4));
			feed.setPath(c.getString(5));
			feed.setVisitAddress(c.getString(6));
			feed.setName(c.getString(7));
			feed.setCreateTime(c.getLong(8));
			feed.setStartTime(c.getLong(9));
			feed.setRegTime(c.getLong(10));
			feed.setSize(c.getLong(11));
			feed.setIsUploaded(c.getInt(12));
			feed.setIsCompleted(c.getInt(13));
			feed.setIsSync(c.getInt(14));
			feed.setType(c.getInt(15));
			feed.setSpent(c.getLong(16));
			feed.setManyTimes(c.getString(17));
			feed.setLat(c.getString(18));
			feed.setLng(c.getString(19));
			feed.setVisitMode(c.getInt(20));
			feed.setReturnType(c.getString(21));
			feed.setIndexArr(c.getString(22));
			feed.setLotsCoord(c.getString(23));
			feed.setManyPlaces(c.getString(24));
			// feed.setScreenOrientation(c.isNull(25)?0:c.getInt(25));
			feed.setInnerPanelStr(c.isNull(25) ? null : c.getString(25));
			feed.setGroupSequence(c.isNull(26) ? null : c.getString(26));
			feed.setPid(c.isNull(27) ? "0" : c.getString(27));
			// 引用受访者参数
			feed.setParametersStr(c.isNull(28) ? null : c.getString(28));
			// 查询目录
			feed.setDirectory(c.isNull(29) ? null : c.getString(29));
			String jsonStr = feed.getInnerPanelStr();
			// 得到指定json key对象的value对象
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(jsonStr);
				JSONObject jsonPsMap = jsonObj.getJSONObject("psMap");
				JSONObject jsonPara = jsonPsMap.getJSONObject("Parameter" + px);
				String conte = jsonPara.getString("content");
				if (conte.indexOf(content) != -1) {
					fs.add(feed);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		c.close();
		return fs;
	}

	/**
	 * 获取所有的XML文件
	 * 
	 * @return
	 */
	public ArrayList<UploadFeed> getAllXmlUploadFeedList(String surveyId, int offset, int maxResult) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"_id", // 0
						"_UserId", // 1
						"_FeedId", // 2
						"_SurveyId", // 3
						"_UUID", // 4
						"_FilePath", // 5
						"_VisitAddress", // 6
						"_FileName", // 7
						"_CreateTime", // 8
						"_StartTime", // 9
						"_RegTime", // 10
						"_FileSize", // 11
						"_IsUploaded", // 12
						"_IsCompleted", // 13
						"_isSync", // 14
						"_FileType", // 15
						"_Spent", // 16
						"_ManyTimes", // 17
						"_Lat", // 18
						"_Lng", // 19
						"_VisitMode",// 20
						"_ReturnType",// 21
						"_IndexArr",// 22
						"_LotsCoord",// 23
						"_ManyPlaces"// 24
				}, "_SurveyId=? AND _FileType=? AND _GiveUp=?", //
				new String[] { surveyId, String.valueOf(Cnt.FILE_TYPE_XML), "0" }, "_RegTime ASC LIMIT " + offset + "," + maxResult);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUserId(c.getString(1));
			feed.setFeedId(c.getString(2));
			feed.setSurveyId(c.getString(3));
			feed.setUuid(c.getString(4));
			feed.setPath(c.getString(5));
			feed.setVisitAddress(c.getString(6));
			feed.setName(c.getString(7));
			feed.setCreateTime(c.getLong(8));
			feed.setStartTime(c.getLong(9));
			feed.setRegTime(c.getLong(10));
			feed.setSize(c.getLong(11));
			feed.setIsUploaded(c.getInt(12));
			feed.setIsCompleted(c.getInt(13));
			feed.setIsSync(c.getInt(14));
			feed.setType(c.getInt(15));
			feed.setSpent(c.getLong(16));
			feed.setManyTimes(c.getString(17));
			feed.setLat(c.getString(18));
			feed.setLng(c.getString(19));
			feed.setVisitMode(c.getInt(20));
			feed.setReturnType(c.getString(21));
			feed.setIndexArr(c.getString(22));
			feed.setLotsCoord(c.getString(23));
			feed.setManyPlaces(c.getString(24));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 获取所有的XML文件
	 * 
	 * @return
	 */
	public Integer xmlUploadCount(String surveyId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, "_SurveyId=? AND _FileType=? AND _GiveUp=?", //
				new String[] { surveyId, String.valueOf(Cnt.FILE_TYPE_XML), "0" }, null);
		Integer count = 0;
		if (c.moveToFirst()) {
			count = c.getInt(0);
		}
		c.close();
		return count;
	}

	public void addRecord(String userId, String surveyId, String uuid, String filePath, String fileName, long startTime, int fileType, String questionId, int innerSave,String feedId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_UserId", userId);
		values.put("_SurveyId", surveyId);
		values.put("_FilePath", filePath);
		values.put("_FileName", fileName);
		values.put("_StartTime", startTime);
		values.put("_UUID", uuid);
		values.put("_FeedId", feedId);
		values.put("_IsCompleted", Cnt.VISIT_STATE_INTERRUPT);
		if (!Util.isEmpty(questionId)) {
			values.put("_QuestionId", questionId);
		}
		values.put("_FileType", fileType);
		values.put("_AddTo05", innerSave);
		resolver.insert(uri, values);

	}

	public boolean updateRecord(String fileName, long regTime, long size) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_IsCompleted", Cnt.VISIT_STATE_COMPLETED);
		values.put("_IsUploaded", Cnt.UPLOAD_STATE_UNUPLOAD);
		values.put("_RegTime", regTime);
		// values.put("_Spent", spent);
		values.put("_FileSize", size);
		return 0 != resolver.update(uri, values, "_FileName=?", new String[] { fileName });
	}

	public boolean upDateFeedId(UploadFeed feed) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		String feedId = feed.getFeedId();
		values.put("_FeedId", feedId);
		return 0 != resolver.update(uri, values, "_UUID=?", new String[] { feed.getUuid() });
	}

	public void addPhoto(UploadFeed photo, boolean isCamera, String indexId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_UUID", photo.getUuid());
		values.put("_UserId", photo.getUserId());
		values.put("_SurveyId", photo.getSurveyId());
		values.put("_FilePath", photo.getPath());
		values.put("_FileName", photo.getName());
		values.put("_StartTime", photo.getStartTime());
		values.put("_IsCompleted", Cnt.VISIT_STATE_COMPLETED);
		values.put("_FileType", Cnt.FILE_TYPE_PNG);
		values.put("_IsUploaded", Cnt.UPLOAD_STATE_UNUPLOAD);
		values.put("_RegTime", photo.getRegTime());
		values.put("_FileSize", photo.getSize());
		values.put("_AddTo05", photo.getIsSaveInner());
		if (isCamera) {
			values.put("_QuestionId", indexId);
		}
		resolver.insert(uri, values);

	}

	/**
	 * 添加摄像方法
	 * 
	 * @param photo
	 * @param isCamera
	 * @param indexId
	 */
	public void addVideo(UploadFeed photo, boolean isCamera, String indexId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_UUID", photo.getUuid());
		values.put("_UserId", photo.getUserId());
		values.put("_SurveyId", photo.getSurveyId());
		values.put("_FilePath", photo.getPath());
		values.put("_FileName", photo.getName());
		values.put("_StartTime", photo.getStartTime());
		values.put("_IsCompleted", Cnt.VISIT_STATE_COMPLETED);
		values.put("_FileType", Cnt.FILE_TYPE_MP4);
		values.put("_IsUploaded", Cnt.UPLOAD_STATE_UNUPLOAD);
		values.put("_RegTime", photo.getRegTime());
		values.put("_FileSize", photo.getSize());
		values.put("_AddTo05", photo.getIsSaveInner());
		if (isCamera) {
			values.put("_QuestionId", indexId);
		}
		resolver.insert(uri, values);

	}
	/**
	 * 获取图片或音频文件
	 * 
	 * @param uuid
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getRecordList(String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_FileType", //
				"_FileName", "_StartTime", "_RegTime",//
				"_FileSize", "_QuestionId", "_FilePath", "_SurveyId", "_UUID" ,"_FeedId"}, //
				"_IsUploaded=? AND _FileSize>? AND _GiveUp=? AND _UserId=?", // AND _FileType
																// IN(?, ?)
				new String[] { //
				String.valueOf(1),//
						String.valueOf(0),//
						// String.valueOf(Cnt.FILE_TYPE_PNG), //
						// String.valueOf(Cnt.FILE_TYPE_MP3),//
						"0" ,userId}, //
				"_id ASC");//
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setType(c.getInt(1));
			feed.setName(c.getString(2));
			feed.setStartTime(c.getLong(3));
			feed.setRegTime(c.getLong(4));
			feed.setSize(c.getLong(5));
			feed.setQuestionId(c.getString(6));
			feed.setPath(c.getString(7));
			feed.setSurveyId(c.getString(8));
			feed.setUuid(c.getString(9));
			feed.setFeedId(c.getString(10));
			fs.add(feed);
		}
		c.close();
		return fs;
	}
	/**
	 * 获取图片或音频文件 在加上摄像视频的文件
	 * 
	 * @param uuid
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getRecordList(String uuid, String surveyId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_FileType", //
				"_FileName", "_StartTime", "_RegTime",//
				"_FileSize", "_QuestionId","_FilePath","_FeedId"}, //
				"_UUID=? AND _SurveyId=? AND _FileType IN(?, ?,?) AND _GiveUp=? AND _FileSize>0", // IN(?,
																									// ?,?)摄像
				new String[] { uuid, surveyId, String.valueOf(Cnt.FILE_TYPE_PNG), String.valueOf(Cnt.FILE_TYPE_MP3), String.valueOf(Cnt.FILE_TYPE_MP4), "0" }, // 摄像格式String.valueOf(Cnt.FILE_TYPE_MP4)
				null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setType(c.getInt(1));
			feed.setName(c.getString(2));
			feed.setStartTime(c.getLong(3));
			feed.setRegTime(c.getLong(4));
			feed.setSize(c.getLong(5));
			feed.setQuestionId(c.getString(6));
			feed.setPath(c.getString(7));
			feed.setFeedId(c.getString(8));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 获取图片或音频文件
	 * 
	 * @param uuid
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getRecordList() {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_FileType", //
				"_FileName", "_StartTime", "_RegTime",//
				"_FileSize", "_QuestionId", "_FilePath", "_SurveyId", "_UUID" ,"_FeedId"}, //
				"_IsUploaded=? AND _FileSize>? AND _GiveUp=?", // AND _FileType
																// IN(?, ?)
				new String[] { //
				String.valueOf(1),//
						String.valueOf(0),//
						// String.valueOf(Cnt.FILE_TYPE_PNG), //
						// String.valueOf(Cnt.FILE_TYPE_MP3),//
						"0" }, //
				"_id ASC");//
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setType(c.getInt(1));
			feed.setName(c.getString(2));
			feed.setStartTime(c.getLong(3));
			feed.setRegTime(c.getLong(4));
			feed.setSize(c.getLong(5));
			feed.setQuestionId(c.getString(6));
			feed.setPath(c.getString(7));
			feed.setSurveyId(c.getString(8));
			feed.setUuid(c.getString(9));
			feed.setFeedId(c.getString(10));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * checkUploadfeed检测是否存在附件
	 * 
	 * */
	public Boolean checkUploadfeed(String uuid, String surveyId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { //
				"_FileName", "_FilePath" }, //
				"_FileType<>? AND _UUID=? AND _SurveyId=?", //
				new String[] { 
						"1", uuid, surveyId //
				}, //
				"_id ASC");//
		if(c.moveToFirst()){
			c.close();
			return false;
		}
		c.close();
		return true;
	}
	
	
	public ArrayList<UploadFeed> getImages(String uuid, String surveyId) {
		System.out.println("getImages");
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { //
				"_FileName", "_FilePath" }, //
				"_FileSize>? AND _FileType=? AND _UUID=? AND _SurveyId=?", //
				new String[] { String.valueOf(0),//
						String.valueOf(Cnt.FILE_TYPE_PNG), uuid, surveyId //
				}, //
				"_id ASC");//
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setName(c.getString(0));
			feed.setPath(c.getString(1));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 查出PNG和MP3文件的总大小
	 * 
	 * @return
	 */
	public long getSum() {
		long sum = 0L;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "SUM(_FileSize)" }, "_IsUploaded=1 AND _FileType<>1 AND _GiveUp=0", null, null);//
		if (c.moveToFirst()) {
			sum = c.getLong(0);
		}
		c.close();
		return sum;
	}
	/**
	 * 查出PNG和MP3文件的总大小
	 * 
	 * @return
	 */
	public long getSum(String userId) {
		long sum = 0L;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "SUM(_FileSize)" }, "_IsUploaded=1 AND _FileType<>1 AND _GiveUp=0 AND _UserId=?", new String[] {userId}, null);//
		if (c.moveToFirst()) {
			sum = c.getLong(0);
		}
		c.close();
		return sum;
	}
	public String getBindId(String fileName) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_LOG);
		Cursor c = resolver.query(uri, new String[] { "_SourceId" }, "_FileName=?", new String[] { fileName }, null);
		String result = null;
		if (c.moveToFirst()) {
			result = c.getString(0);
		}
		c.close();
		return result;
	}

	public void save(String sourceId, String fileName) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_LOG);
		ContentValues values = new ContentValues();
		values.put("_SourceId", sourceId);
		values.put("_FileName", fileName);
		resolver.insert(uri, values);
	}

	public void delete(String fileName) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_LOG);
		resolver.delete(uri, "_FileName=?", new String[] { fileName });
	}

	public int uploadMp3AndPng(String fileName) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_IsUploaded", 9);
		return resolver.update(uri, values, "_FileName=?", new String[] { fileName });
	}

	/**
	 * 记录被放弃
	 * 
	 * @param uuid
	 * @param surveyId
	 * @return
	 */
	public boolean giveUpFeed(String uuid, String surveyId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_GiveUp", 1);
		return 0 != resolver.update(uri, values, "_UUID=? AND _SurveyId=?", new String[] { uuid, surveyId });
	}

	/**
	 * 是否有这个试用问卷
	 * 
	 * @param surveyId
	 * @return
	 */
	public String surveyFreeExsit(String surveyId) {
		String exist = null;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyTitle" }, "_SurveyId=?", new String[] { surveyId }, null);
		if (c.moveToFirst()) {
			exist = c.getString(0);
		}
		c.close();
		return exist;
	}

	/**
	 * 得到一个survey
	 */
	public Survey getTextSurvey() {
		Survey survey = new Survey();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId",// 0
				"_SurveyTitle", // 1
				"_IsPhoto", // 2
				"_IsRecord", // 3
				"_IsExpand", // 4
				"_IsDowned", // 5
				"_SurveyEnable", // 6
				"_DownloadUrl", // 7
				"_PublishTime", // 8
				"_SurveyContent",// 9
				"_id",// 10
				"_VisitMode",// 11
				"_AddTo11" }, // 12
				"_SurveyEnable=? AND _IsDowned=? AND _AddTo01=1", new String[] { "0", "1" }, // ,
																								// "1"
				"_SurveyId ASC");//
		if (c.moveToFirst()) {
			// System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwww");
			survey.surveyId = c.getString(0);
			survey.surveyTitle = c.getString(1);
			survey.isPhoto = c.getInt(2);
			survey.isRecord = c.getInt(3);
			survey.isExpand = c.getInt(4);
			survey.isDowned = c.getInt(5);
			survey.surveyEnable = c.getInt(6);
			survey.downloadUrl = c.getString(7);
			survey.publishTime = c.getString(8);
			survey.surveyContent = c.getString(9);
			survey.id = c.getInt(10);
			survey.visitMode = c.getInt(11);
			survey.setWord(c.getString(12));
		}
		c.close();
		return survey;
	}

	/**
	 * 获取所有需要导出的XML文件
	 * 
	 * @return
	 */
	public ArrayList<UploadFeed> getAllExportXml(String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"_FilePath", // 0
						"_FileName", // 1
						"_SurveyId" }, "_UserId=? AND _FileType=?", //
				new String[] { userId, String.valueOf(Cnt.FILE_TYPE_XML) }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setPath(c.getString(0));
			feed.setName(c.getString(1));
			feed.setSurveyId(c.getString(2));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 记录被放弃
	 * 
	 * @param uuid
	 * @param surveyId
	 * @return
	 */
	public boolean updateUploadFeedNoAccessState(Long id) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_IsCompleted", Cnt.VISIT_STATE_NOACCESS);
		return 0 != resolver.update(uri, values, "_id=?", new String[] { "" + id });
	}

	public boolean feedAnswerIsHave(String uuid) {
		boolean is = false;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		Cursor c = resolver.query(uri, new String[] { "_id" }, //
				"_UUID=? AND _Enable=1", //
				new String[] { uuid }, null);
		if (c.moveToFirst()) {
			is = true;
		}
		c.close();
		return is;
	}

	/** 添加问卷 **/
	public boolean updateSurveyInnerDone(String surveyId) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		ContentValues values = new ContentValues();
		values.put("_AddTo03", 1);
		int i = resolver.update(uri, values, "_SurveyId=?", new String[] { surveyId });
		return 0 != i;
	}

	public boolean isFeedExist(String surveyId, String feedId) {
		if (Util.isEmpty(feedId)) {
			return false;
		}
		boolean f = false;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id" }, "_SurveyId=? AND _FeedId=?", new String[] { surveyId, feedId }, null);
		if (c.moveToFirst()) {
			f = true;
		}
		c.close();
		return f;
	}

	// 引用受访者参数
	public void updateInnerUploadFeed(String surveyId, String panelId, String innerJsonStr, String feedId, String parametersStr) {
		if (Util.isEmpty(panelId)) {
			return;
		}
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_AddTo03", innerJsonStr);
		values.put("_GiveUp", 0);
		values.put("_FeedId", feedId);
		// 引用受访者参数
		values.put("_AddTo07", parametersStr);
		resolver.update(uri, values, "_SurveyId=? AND _AddTo04=?", new String[] { surveyId, panelId });
	}

	// 引用受访者参数
	public void addInnerUploadFeed(String feedId, String userId, String surveyId, String uuid, long time, String path, String name, int visitMode, String innerJsonStr, String panelId, String pStr) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_UserId", userId);
		values.put("_SurveyId", surveyId);
		values.put("_FeedId", feedId);
		values.put("_UUID", uuid);
		values.put("_CreateTime", time);
		values.put("_FilePath", path);
		values.put("_FileName", name);
		values.put("_StartTime", time);
		values.put("_RegTime", time);
		values.put("_IsCompleted", Cnt.VISIT_STATE_NOACCESS);
		values.put("_FileType", Cnt.FILE_TYPE_XML);
		values.put("_VisitMode", visitMode);
		values.put("_AddTo03", innerJsonStr);
		values.put("_AddTo04", panelId);
		// 引用受访者参数
		values.put("_AddTo07", pStr);
		resolver.insert(uri, values);
		// return Util.isSuccess(u.toString());
	}

	public void addInnerUploadFeed(String feedId, String userId, String surveyId, String uuid, long time, String path, String name, int visitMode, String innerJsonStr) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_UserId", userId);
		values.put("_SurveyId", surveyId);
		values.put("_FeedId", feedId);
		values.put("_UUID", uuid);
		values.put("_CreateTime", time);
		values.put("_FilePath", path);
		values.put("_FileName", name);
		values.put("_StartTime", time);
		values.put("_RegTime", time);
		values.put("_IsCompleted", Cnt.VISIT_STATE_NOACCESS);
		values.put("_FileType", Cnt.FILE_TYPE_XML);
		values.put("_VisitMode", visitMode);
		values.put("_AddTo03", innerJsonStr);
		resolver.insert(uri, values);
		// return Util.isSuccess(u.toString());
	}

	public void updateInnerUploadFeed(String surveyId, String feedId, String innerJsonStr) {
		if (Util.isEmpty(feedId)) {
			return;
		}
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_AddTo03", innerJsonStr);
		resolver.update(uri, values, "_SurveyId=? AND _FeedId=?", new String[] { surveyId, feedId });
	}

	public ArrayList<String> getUploadedFeed() {
		ArrayList<String> list = new ArrayList<String>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_UUID" }, "_IsCompleted=? AND _IsUploaded>=? AND _FileType=?", new String[] { "1", "1", "1" }, null);
		while (c.moveToNext()) {
			list.add(c.getString(0));
		}
		c.close();
		return list;
	}

	public void clearAnswers(String uuid) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		resolver.delete(uri, "_UUID=?", new String[] { uuid });
	}

	/**
	 * 通过surveyId获得 所有panelId号 +_FilePath+_FileName :::分隔符
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<String> getListBySurveyId(String surveyId, String userId) {
		ArrayList<String> dbList = new ArrayList<String>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id,_AddTo04", "_FilePath", "_FileName" }, "_SurveyId=? and _UserId=?", new String[] { surveyId, userId }, null);
		if (c != null && c.getCount() > 0) {
			while (c.moveToNext()) {
				String panelId = c.getString(c.getColumnIndex("_AddTo04"));
				String filePath = c.getString(c.getColumnIndex("_FilePath"));
				String fileName = c.getString(c.getColumnIndex("_FileName"));
				String file = filePath + "/" + fileName;
				dbList.add(panelId + ":::" + file);
			}
		}
		c.close();
		return dbList;
	}

	/**
	 * 更新upload数据库的giveup
	 * 
	 * @param panelId
	 * @param surveyId
	 * @param giveUp
	 */
	public boolean updateGiveUpByPanelId(String panelId, String surveyId, int giveUp) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_GiveUp", giveUp);
		return 0 != resolver.update(uri, values, "_SurveyId=? and _AddTo04=?", new String[] { surveyId, panelId });
	}

	/**
	 * 获取所有需要导出的XML文件
	 * 
	 * @return
	 */
	public ArrayList<UploadFeed> getAllInnerMp3OrPng() {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"_id",// 0
						"_FilePath", // 1
						"_FileName", // 2
						"_SurveyId"// 3
				}, "_FileType>? AND ifnull(_AddTo05, 0)=0", // _AddTo05
				new String[] { String.valueOf(Cnt.FILE_TYPE_XML) }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setPath(c.getString(1));
			feed.setName(c.getString(2));
			feed.setSurveyId(c.getString(3));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	public boolean updateMoveToSdcard(String _id, String newPath) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		/**
		 * 从内部移动到sdcard上, 已经移动过了
		 */
		values.put("_AddTo05", 1);
		values.put("_FilePath", newPath);
		return 0 != resolver.update(uri, values, "_id=?", new String[] { _id });
	}

	/**
	 * 查询所有已完成且需要上传的UploadFeed
	 * 
	 * @param surveyId
	 * @return
	 */
	public boolean isSingleCamera(String surveyId, String userId, String uuid, String questionIndex) {
		boolean isSingleCamera = false;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", //
				"_FilePath", "_FileName", "_FileSize" }, //
				"_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _UUID=?", //
				new String[] { surveyId, "1", "2", "0", userId, uuid }, null);
		while (c.moveToNext()) {
			String fileName = c.getString(4);
			String[] split = fileName.split("_");
			if (5 == split.length) {
				if (userId.equals(split[0]) && surveyId.equals(split[1]) && questionIndex.equals(split[3]) && (uuid + ".png").equals(split[4])) {
					isSingleCamera = true;
					break;
				}
			} else if (6 == split.length) {
				if (userId.equals(split[2]) && surveyId.equals(split[0]) && questionIndex.equals(split[4]) && (uuid + ".png").equals(split[5])) {
					isSingleCamera = true;
					break;
				}
			}
			// 命名规则兼容
			else if (7 == split.length) {
				String uuids = split[6];
				// 含等于
				if (uuids.contains("=")) {
					String[] split2 = uuids.split(".png");
					String[] split3 = split2[0].split("=");
					String realUuid = split3[0] + ".png";
					if (userId.equals(split[2]) && surveyId.equals(split[0]) && questionIndex.equals(split[5]) && (uuid + ".png").equals(realUuid)) {
						isSingleCamera = true;
						break;
					}
				}
				// 不含等于
				else {
					if (userId.equals(split[2]) && surveyId.equals(split[0]) && questionIndex.equals(split[5]) && (uuid + ".png").equals(split[6])) {
						isSingleCamera = true;
						break;
					}
				}

			}
			// 命名规则兼容
		}
		c.close();
		return isSingleCamera;
	}

	// public boolean isSingleCamera(String surveyId, String userId, String
	// uuid, String questionIndex) {
	// boolean isSingleCamera = false;
	// Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" +
	// DBProvider.TAB_UPLOAD_FEED);
	// Cursor c = resolver.query(uri, new String[] { "_id", "_UUID",
	// "_SurveyId", //
	// "_FilePath", "_FileName", "_FileSize" }, //
	// "_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _UUID=?",
	// //
	// new String[] { surveyId, "1", "2", "0", userId, uuid }, null);
	// while (c.moveToNext()) {
	// String fileName = c.getString(4);
	// String[] split = fileName.split("_");
	// if (5 == split.length) {
	// if (userId.equals(split[0]) && surveyId.equals(split[1]) &&
	// questionIndex.equals(split[3]) && (uuid + ".png").equals(split[4])) {
	// isSingleCamera = true;
	// break;
	// }
	// }
	// }
	// c.close();
	// return isSingleCamera;
	// }

	public void updateQuestionIntervention(String surveyId, String qIndex, String json) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		ContentValues values = new ContentValues();
		values.put("_AddTo05", json);
		resolver.update(uri, values, "_SurveyId=? AND _QIndex=?", new String[] { surveyId, qIndex });
	}

	public Integer updateAnswer2Null(Answer anw) {
		if (null == anw) {
			return -1;
		}
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		ContentValues values = new ContentValues();
		// System.out.println("清空:_UUID:"+
		// anw.uuid+"---QuestionIndex:"+anw.qIndex);
		values.put("_AnswerMap", "");
		int i = resolver.update(uri, values, "_UUID=? AND _QuestionIndex=?", new String[] { anw.uuid, String.valueOf(anw.qIndex) });
		return i;
	}

	/**
	 * 将有干预的问卷中 所有的干预标志置为空
	 * 
	 * @param surveyId
	 */
	public void updateAllIntervention2Null(String surveyId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		ContentValues values = new ContentValues();
		values.put("_AddTo05", "");
		resolver.update(uri, values, "_SurveyId=?", new String[] { surveyId });
	}

	/**
	 * 更新题组随机json串
	 * 
	 * @param surveyId
	 * @param qIndex
	 * @param json
	 * @return
	 */
	public boolean updateQuestionGroup(String surveyId, String qIndex, String json) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		ContentValues values = new ContentValues();
		values.put("_AddTo06", json);
		int i = resolver.update(uri, values, "_SurveyId=? AND _QIndex=?", new String[] { surveyId, qIndex });
		return 0 != i;
	}

	/**
	 * 更新题组随机json串
	 * 
	 * @param surveyId
	 * @param qIndex
	 * @param json
	 * @return
	 */
	public boolean updateQuestionGroup2Null(String surveyId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_QUESTION);
		ContentValues values = new ContentValues();
		values.put("_AddTo06", "");
		int i = resolver.update(uri, values, "_SurveyId=?", new String[] { surveyId });
		return 0 != i;
	}

	/**
	 * 获取图片或音频文件是否为空
	 * 
	 * @param uuid
	 * @param surveyId
	 * @return
	 */
	public boolean isEmptyRecordList() {
		boolean isEmpty = true;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, //
				"_IsUploaded=? AND _FileSize>? AND _GiveUp=?", // AND _FileType
																// IN(?, ?)
				new String[] { //
				String.valueOf(1),//
						String.valueOf(0),//
						"0" }, //
				null);//
		if (c.moveToFirst()) {
			isEmpty = false;
		}
		c.close();
		return isEmpty;
	}

	/**
	 * 是否size为0
	 * 
	 * @return
	 */
	public ArrayList<UploadFeed> getEmptyRecordList() {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id",//
				"_FileName", //
				"_FileSize", //
				"_FilePath" }, //
				"_IsUploaded=1 AND ((_FileSize is null) OR  _FileSize=0) AND _GiveUp=0", // AND
																							// _FileType

				null,// new String[] {String.valueOf(1), "0"}, //
				null);//
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setName(c.getString(1));
			feed.setSize(c.getLong(2));
			feed.setPath(c.getString(3));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 更改为放弃
	 * 
	 * @param fileName
	 * @param regTime
	 * @param size
	 * @return
	 */
	public boolean updateRecordGiveUp(String fileName) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_GiveUp", 1);
		// values.put("_Spent", spent);
		return 0 != resolver.update(uri, values, "_FileName=?", new String[] { fileName });
	}

	/**
	 * 添加错误记录号。可用的
	 */
	public boolean addTab(DapException dapException) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB1);
		ContentValues values = new ContentValues();
		values.put("_AddTo01", dapException.getFileName());
		values.put("_AddTo02", dapException.getFilePath());
		values.put("_AddTo03", dapException.getUserId());
		values.put("_AddTo04", dapException.getMacAddress());
		values.put("_AddTo05", dapException.getEnable());
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/**
	 * 把可用的变成不可用的 条件是
	 */
	public boolean updateTabToUnEnable(String fileName) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB1);
		ContentValues values = new ContentValues();
		values.put("_AddTo05", 0);// 设置为 不可用
		return 0 != resolver.update(uri, values, "_AddTo01=?", new String[] { fileName });
	}

	/**
	 * 得到没有传的问卷。_AddTo05 enable==1,查出可用的
	 * 
	 * /** 异常表 _AddTo01 文件名 _AddTo02 路径 _AddTo03 用户名字 _AddTo04 mac地址 _AddTo05
	 * 是否可用 0为不可用 1为可用
	 * 
	 * @return
	 */
	public ArrayList<DapException> getTab1ByEnable() {
		ArrayList<DapException> exList = new ArrayList<DapException>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB1);
		Cursor c = resolver.query(uri, new String[] { "_AddTo01",// 0
				"_AddTo02", // 1
				"_AddTo03", // 2
				"_AddTo04" }, // 3
				"_AddTo05=1", // "SurveyEnable=?"
				null, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			DapException dapException = new DapException();
			dapException.setFileName(c.getString(0));
			dapException.setFilePath(c.getString(1));
			dapException.setUserId(c.getString(2));
			dapException.setMacAddress(c.getString(3));
			exList.add(dapException);
		}
		c.close();
		return exList;
	}

	public ArrayList<UploadFeed> getUploadFeedByKey(String key) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		if (Util.isEmpty(key)) {
			return fs;
		}
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] {//
				"_FilePath", // 0
						"_FileName" }, "_UserId=? OR _id=?", //
				new String[] { key, key }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setPath(c.getString(0));
			feed.setName(c.getString(1));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	public ArrayList<String> getAllSurveyId(String userId) {
		ArrayList<String> surveyIdList = new ArrayList<String>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId" }, // 16
				"_SurveyEnable=? AND _UserIdList LIKE '%" + userId + "%'", // "SurveyEnable=?"
				new String[] { "0" }, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			String sId = c.getString(0);
			surveyIdList.add(sId);
		}
		c.close();
		return surveyIdList;
	}

	/** 添加问卷 **/
	public boolean enableSurvey(String stopSurveyId) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		ContentValues values = new ContentValues();
		// 置为不可用
		values.put("_SurveyEnable", 1);

		int i = resolver.update(uri, values, "_SurveyId=?", new String[] { stopSurveyId });
		return 0 != i;
	}

	/**
	 * 统计已完成的且未上传的，只统计问卷id
	 * 
	 * @param surveyId
	 * @return
	 */
	public long feedUnUploadCount(String surveyId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, "_SurveyId=? AND _IsCompleted=? AND _IsUploaded<>? AND _FileType=? AND _GiveUp=?", new String[] { surveyId, "1", "9", "1", "0" },
				null);
		if (c.moveToFirst()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}

	/**
	 * 单题签名
	 * 
	 * @param surveyId
	 * @return
	 */
	public boolean isSingleSign(String surveyId, String userId, String uuid, String questionIndex) {
		boolean isSingleCamera = false;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", //
				"_FilePath", "_FileName", "_FileSize" }, //
				"_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _UUID=?", //
				new String[] { surveyId, "1", "2", "0", userId, uuid }, null);
		while (c.moveToNext()) {
			String fileName = c.getString(4);
			String[] split = fileName.split("_");
			if (5 == split.length) {
				// jpg只有签名是这个格式的
				if (userId.equals(split[0]) && surveyId.equals(split[1]) && questionIndex.equals(split[3]) && (uuid + ".jpg").equals(split[4])) {
					isSingleCamera = true;
					break;
				}
			} else if (6 == split.length) {
				if (userId.equals(split[2]) && surveyId.equals(split[0]) && questionIndex.equals(split[4]) && (uuid + ".jpg").equals(split[5])) {
					isSingleCamera = true;
					break;
				}
			}
			// 命名规则兼容
			else if (7 == split.length) {
				String uuids = split[6];
				if (uuids.contains("=")) {
					String[] split2 = uuids.split(".jpg");
					String[] split3 = split2[0].split("=");
					String realUuid = split3[0] + ".jpg";
					if (userId.equals(split[2]) && surveyId.equals(split[0]) && questionIndex.equals(split[5]) && (uuid + ".jpg").equals(realUuid)) {
						isSingleCamera = true;
						break;
					}
				}
				// 不含等于
				else {
					if (userId.equals(split[2]) && surveyId.equals(split[0]) && questionIndex.equals(split[5]) && (uuid + ".jpg").equals(split[6])) {
						isSingleCamera = true;
						break;
					}
				}
			}
			// 命名规则兼容
		}
		c.close();
		return isSingleCamera;
	}

	/**
	 * 添加地图监控点 _AddTo01 userId; _AddTo02 userLat; // 纬 _AddTo03 userLng; // 经
	 * _AddTo04 time; // 时间 _AddTo05 addName; // 地点 _AddTo06 isUpload;// 是否上传了
	 * **/
	public boolean addPosition(UserPosition up) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB2);
		ContentValues values = new ContentValues();
		values.put("_AddTo01", up.getUserId());
		values.put("_AddTo02", up.getUserLat());
		values.put("_AddTo03", up.getUserLng());
		values.put("_AddTo04", up.getTime());
		values.put("_AddTo05", up.getAddName());
		values.put("_AddTo06", up.getIsUpload());
		values.put("_AddTo07", up.getDate());
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/**
	 * 更新地图监控状态
	 */
	public boolean updatePositionState(int isUpload) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB2);
		ContentValues values = new ContentValues();
		values.put("_AddTo06", isUpload);
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/**
	 * 查询地图监控该用户的所有记录点
	 */
	public ArrayList<UserPosition> getListPosition(String userId, String date) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB2);
		ArrayList<UserPosition> upList = new ArrayList<UserPosition>();
		Cursor c = resolver.query(uri, new String[] { "_AddTo02", "_AddTo03", "_AddTo04", "_AddTo05", "_AddTo01" }, //
				"_AddTo01=? and _AddTo07=?", //
				new String[] { userId, date }, "_id ASC");
		while (c.moveToNext()) {
			UserPosition up = new UserPosition();
			up.setUserLat(c.getDouble(0));
			up.setUserLng(c.getDouble(1));
			up.setTime(c.getString(2));
			up.setAddName(c.getString(3));
			up.setUserId(c.getString(4));
			upList.add(up);
		}
		c.close();
		return upList;
	}

	/** 增加知识库 **/
	public boolean addKnow(Knowledge knowledge) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB3);
		ContentValues values = new ContentValues();
		values.put("_AddTo01", knowledge.getId());
		values.put("_AddTo02", knowledge.getTitle());
		values.put("_AddTo03", knowledge.getKind());
		values.put("_AddTo04", knowledge.getContent());
		values.put("_AddTo05", knowledge.getAttach());
		values.put("_AddTo06", knowledge.getFileName());
		values.put("_AddTo07", knowledge.getEnable());
		values.put("_AddTo08", knowledge.getUserList());
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/** 更新知识库 **/
	public boolean updateKnow(Knowledge knowledge) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB3);
		ContentValues values = new ContentValues();
		values.put("_AddTo01", knowledge.getId());
		values.put("_AddTo02", knowledge.getTitle());
		values.put("_AddTo03", knowledge.getKind());
		values.put("_AddTo04", knowledge.getContent());
		values.put("_AddTo05", knowledge.getAttach());
		values.put("_AddTo06", knowledge.getFileName());
		values.put("_AddTo07", knowledge.getEnable());
		values.put("_AddTo08", knowledge.getUserList());
		int i = resolver.update(uri, values, "_AddTo01=?", new String[] { knowledge.getId() + "" });
		return 0 != i;
	}

	/**
	 * 查出所有知识库
	 */
	public ArrayList<Knowledge> getAllKnowledge(String userId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB3);
		ArrayList<Knowledge> kList = new ArrayList<Knowledge>();
		Cursor c = resolver.query(uri, new String[] { "_AddTo01", "_AddTo02", "_AddTo03", "_AddTo04", "_AddTo05", "_AddTo06", "_AddTo07", "_AddTo08" }, //
				"_AddTo07=? and _AddTo08 LIKE '%" + userId + "%'", //
				new String[] { "0" }, "_AddTo01 ASC");
		while (c.moveToNext()) {
			Knowledge knowLedge = new Knowledge();
			knowLedge.setId(c.getString(0));
			knowLedge.setTitle(c.getString(1));
			knowLedge.setKind(c.getString(2));
			knowLedge.setContent(c.getString(3));
			knowLedge.setAttach(c.getString(4));
			knowLedge.setFileName(c.getString(5));
			knowLedge.setEnable(c.getString(6));
			knowLedge.setUserList(c.getString(7));
			kList.add(knowLedge);
		}
		c.close();
		return kList;
	}

	/**
	 * 查出有附件的知识库
	 */
	public ArrayList<Knowledge> getAllHaveFileKnowledge(String userId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB3);
		ArrayList<Knowledge> kList = new ArrayList<Knowledge>();
		Cursor c = resolver.query(uri, new String[] { "_AddTo01", "_AddTo02", "_AddTo03", "_AddTo04", "_AddTo05", "_AddTo06", "_AddTo07", "_AddTo08" }, //
				"_AddTo07=? and _AddTo08 LIKE '%" + userId + "%' and _AddTo05 is not null", //
				new String[] { "0" }, "_AddTo01 ASC");
		while (c.moveToNext()) {
			Knowledge knowLedge = new Knowledge();
			knowLedge.setId(c.getString(0));
			knowLedge.setTitle(c.getString(1));
			knowLedge.setKind(c.getString(2));
			knowLedge.setContent(c.getString(3));
			knowLedge.setAttach(c.getString(4));
			knowLedge.setFileName(c.getString(5));
			knowLedge.setEnable(c.getString(6));
			knowLedge.setUserList(c.getString(7));
			kList.add(knowLedge);
		}
		c.close();
		return kList;
	}

	/**
	 * 获取存在的知识库
	 * 
	 * @param surveyId
	 * @return
	 */
	public Knowledge getKnowExsit(String id) {
		Knowledge k = null;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB3);
		Cursor c = resolver.query(uri, new String[] { "_AddTo01",// 0
				"_AddTo02",// 1
				"_AddTo03",// 2
				"_AddTo04",// 3
				"_AddTo05",// 4
				"_AddTo06",// 5
				"_AddTo07",// 6
				"_AddTo08" // 7
		}, "_AddTo01=?", new String[] { id }, null);
		if (c.moveToNext()) {
			k = new Knowledge();
			k.setId(c.getString(0));
			k.setTitle(c.getString(1));
			k.setKind(c.getString(2));
			k.setContent(c.getString(3));
			k.setAttach(c.getString(4));
			k.setFileName(c.getString(5));
			k.setEnable(c.getString(6));
			k.setUserList(c.getString(7));
		}
		c.close();
		return k;
	}

	/**
	 * 查询地图监控没上传成功的点
	 * 
	 */
	public ArrayList<UserPosition> getNoUploadListPosition() {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB2);
		ArrayList<UserPosition> upList = new ArrayList<UserPosition>();
		Cursor c = resolver.query(uri, new String[] { "_AddTo02", "_AddTo03", "_AddTo04", "_AddTo05", "_AddTo01" }, //
				"_AddTo06=0", //
				new String[] {}, null);
		while (c.moveToNext()) {
			UserPosition up = new UserPosition();
			up.setUserLat(c.getDouble(0));
			up.setUserLng(c.getDouble(1));
			up.setTime(c.getString(2));
			up.setAddName(c.getString(3));
			up.setUserId(c.getString(4));
			upList.add(up);
		}
		c.close();
		return upList;
	}

	/**
	 * 把未上传改为 又上传 条件是 记录丢失文件表
	 */
	public boolean updateRecodeEnable(String fileName) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB4);
		ContentValues values = new ContentValues();
		values.put("_AddTo03", 1);// 设置为 不可用
		return 0 != resolver.update(uri, values, "_AddTo02=?", new String[] { fileName });
	}

	/**
	 * 把未上传改为 又上传 条件是 记录丢失文件表
	 */
	public boolean updateRecodeEnableByUid(String uid) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB4);
		ContentValues values = new ContentValues();
		values.put("_AddTo03", 1);// 设置为 不可用
		return 0 != resolver.update(uri, values, "_AddTo01=?", new String[] { uid });
	}

	/**
	 * 
	 * 
	 * /** 被删除附件表 _AddTo01 文件名 _AddTo02 路径 _AddTo03 用户名字
	 * 
	 * @return
	 */
	/**
	 * 记录丢失文件表 添加删除记录号。可用的
	 */
	public boolean saveName(UploadFeed uploadFeed) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB4);
		ContentValues values = new ContentValues();
		values.put("_AddTo01", uploadFeed.getUuid());
		values.put("_AddTo02", uploadFeed.getName());
		values.put("_AddTo03", 0);// 0代表没有传的
		values.put("_AddTo04", uploadFeed.getSurveyId());
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/**
	 * 
	 * 
	 * /** 被删除附件表_AddTo01 uuid ID号 和附件id一样 然后查询feedid _AddTo02 feedName 附件名字
	 * _AddTo03 是否传完了 0没传 1传了 _AddTo04 问卷id
	 * 
	 * @return
	 */
	public ArrayList<MyRecoder> queryDeleteRecode() {
		ArrayList<MyRecoder> reList = new ArrayList<MyRecoder>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB4);
		Cursor c = resolver.query(uri, new String[] { "_AddTo01",// 0
				"_AddTo02", "_AddTo04" }, // 3
				"_AddTo03 = 0", // "SurveyEnable=?"//0代表没有上传的
				null, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			MyRecoder myRecoder = new MyRecoder();
			String myUID = c.getString(0);
			String feedId = queryFeedIdByUid(myUID);
			myRecoder.setUuid(c.getString(0));
			myRecoder.setName(c.getString(1));
			myRecoder.setFeedId(feedId);
			myRecoder.setSurveyId(c.getString(2));
			reList.add(myRecoder);
		}
		c.close();
		return reList;
	}

	/**
	 * 
	 * 
	 * /** 删除附件_AddTo01 uuid ID号 和附件id一样 然后查询feedid _AddTo02 feedName 附件名字
	 * _AddTo03 是否传完了 0没传 1传了 _AddTo04 问卷id groupby查询
	 * 
	 * @return
	 */
	public ArrayList<MyRecoder> queryDeleteRecodeGroupBy() {
		ArrayList<MyRecoder> reList = new ArrayList<MyRecoder>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB4);
		Cursor c = resolver.query(uri, new String[] { "_AddTo01",// 0
				"count(*)", "_AddTo04" }, // 3
				"_AddTo03 = 0 group by _AddTo01", // "SurveyEnable=?"//0代表没有上传的
				null, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			MyRecoder myRecoder = new MyRecoder();
			String myUID = c.getString(0);
			String feedId = queryFeedIdByUid(myUID);
			myRecoder.setUuid(c.getString(0));
			myRecoder.setCount(c.getInt(1));
			myRecoder.setFeedId(feedId);
			myRecoder.setSurveyId(c.getString(2));
			reList.add(myRecoder);
		}
		c.close();
		return reList;
	}

	/**
	 * 
	 * 
	 * /** 被删除附件表 _AddTo01 文件名 _AddTo02 路径 _AddTo03 用户名字 _AddTo04 问卷号 记录丢失文件表
	 * 删除附件
	 * 
	 * @return
	 */
	public ArrayList<MyRecoder> queryAllDeleteRecode(String sid) {
		ArrayList<MyRecoder> reList = new ArrayList<MyRecoder>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB4);
		Cursor c = resolver.query(uri, new String[] { "_AddTo01",// 0
				"_AddTo02", "_AddTo04" }, // 3
				"_AddTo04=?", // "SurveyEnable=?"//0代表没有上传的
				new String[] { sid }, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			MyRecoder myRecoder = new MyRecoder();
			myRecoder.setUuid(c.getString(0));
			myRecoder.setName(c.getString(1));
			myRecoder.setSurveyId(c.getString(2));
			reList.add(myRecoder);
		}
		c.close();
		return reList;
	}

	/**
	 * 记录丢失文件表
	 * 
	 * @return
	 */
	public String queryFeedIdByUid(String uuid) {
		String feedId = "0";
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_FeedId" }, //
				"_UUID=? AND _FileType=?", //
				new String[] { uuid, "1" }, null);
		if (c.moveToNext()) {
			int fid = c.getInt(0);
			feedId = fid + "";
		}
		c.close();
		return feedId;
	}

	// 上传开始
	/**
	 * 统计已完成的且未上传的
	 * 
	 * @param surveyId
	 * @return
	 */
	public long feedUnUploadCounts(String surveyId, String userId) {
		long count = 0;
		ArrayList<String> uuids=new ArrayList<String>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_UUID" },
				"_SurveyId=? AND _IsCompleted=? AND _IsUploaded<>? AND _GiveUp=? AND _UserId=?",
				new String[] { surveyId, "1", "9", "0", userId }, null);
		while (c.moveToNext()) {
			if (0 < uuids.size()) {
				if (!uuids.contains(c.getString(0))) {
					uuids.add(c.getString(0));
				}
			} else {
				uuids.add(c.getString(0));
			}
		}
		count = uuids.size();
		c.close();
		for (int i = 0; i < uuids.size(); i++) {
			Cursor d = resolver.query(uri, new String[] { "COUNT(_id)" },
					"_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _UUID=?",
					new String[] { surveyId, "1", "1", "0", userId, uuids.get(i) }, null);//查找包含已完成的xml
			if (d.moveToFirst()) {
				if (0 == d.getLong(0)) {//所查样本属于未完成的样本
					count--;
				}
			} else {//所查属于未完成的样本
				count--;
			}
			d.close();
		}
		return count;
	}

	/**
	 * 上传
	 * 
	 * @param userId
	 * @param surveyId
	 * @param isUploaded
	 * @return
	 */
	public int updateFeedStatusBySurveyId(String userId, String surveyId, int isUploaded) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_IsUploaded", isUploaded);
		return resolver.update(uri, values, "_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _IsUploaded=?", new String[] { surveyId, "1", "1", "0", userId, "1" });
	}

	/**
	 * 上传 查询所有已完成且需要上传的UploadFeed
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getAllCompletedUploadFeedIpsos(String surveyId, String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", //
				"_FilePath", "_FileName", "_FileSize", "_FileType", "_IsUploaded" }, //
				"_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _IsUploaded=?", //
				new String[] { surveyId, "1", "1", "0", userId, "2" }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUuid(c.getString(1));
			feed.setSurveyId(c.getString(2));
			feed.setPath(c.getString(3));
			feed.setName(c.getString(4));
			feed.setSize(c.getLong(5));
			feed.setType(c.getInt(6));
			feed.setIsUploaded(c.getInt(7));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 包括XML所有的大小 上传
	 * 
	 * @return
	 */
	public long getSumAllIpsos() {
		long sum = 0L;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "SUM(_FileSize)" }, "_IsUploaded=1 AND _GiveUp=0", null, null);//
		if (c.moveToFirst()) {
			sum = c.getLong(0);
		}
		c.close();
		return sum;
	}

	/**
	 * 上传
	 * 
	 * @param fileName
	 * @return
	 */
	public int uploadMp3AndPngIpsos(String fileName) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if ("xml".equals(type)) {
			// xml在传一次
			System.out.println("xml置为2");
			values.put("_IsUploaded", 2);
		} else {
			System.out.println("状态9");
			values.put("_IsUploaded", 9);
		}
		return resolver.update(uri, values, "_FileName=?", new String[] { fileName });
	}

	/**
	 * 4000该传xml消息时候,isUpload为2了 所以在加一个条件isUpload为0,代表新建的 上传
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getAllCompletedUploadFeedByNew(String surveyId, String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", //
				"_FilePath", "_FileName", "_FileSize", "_FileType", "_IsUploaded" }, //
				"_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _IsUploaded=?", //
				new String[] { surveyId, "1", "1", "0", userId, "0" }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUuid(c.getString(1));
			feed.setSurveyId(c.getString(2));
			feed.setPath(c.getString(3));
			feed.setName(c.getString(4));
			feed.setSize(c.getLong(5));
			feed.setType(c.getInt(6));
			feed.setIsUploaded(c.getInt(7));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 统计已完成的且未上传的 上传
	 * 
	 * @param surveyId
	 * @return
	 */
	public long feedUnUploadCountIpsos(String surveyId, String userId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, "_SurveyId=? AND _IsCompleted=? AND _IsUploaded=? AND _FileType=? AND _GiveUp=? AND _UserId=?", new String[] { surveyId, "1",
				"2", "1", "0", userId }, null);
		if (c.moveToFirst()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}

	public ArrayList<Survey> searchSurveyByWord(String userId, String words) {
		ArrayList<Survey> ss = new ArrayList<Survey>();
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_SURVEY);
		Cursor c = resolver.query(uri, new String[] { "_SurveyId",// 0
				"_SurveyTitle", // 1
				"_IsPhoto", // 2
				"_IsRecord", // 3
				"_IsExpand", // 4
				"_IsDowned", // 5
				"_SurveyEnable", // 6
				"_DownloadUrl", // 7
				"_PublishTime", // 8
				"_SurveyContent",// 9
				"_id",// 10
				"_VisitMode",// 11
				"_AddTo01",// 12
				"_AddTo02",// 13是否有内部受邀名单
				"_AddTo03",// 14内部受邀名单是否已下载过
				"_AddTo04",// 15
				"_AddTo05", // 16
				"_AddTo12",// 17
				"_AddTo16",// 18是否摄像
				"_AddTo13",// 19是生成时间
				"_AddTo11" },// 20是访前说明
				"_SurveyEnable=? AND _UserIdList LIKE '%" + userId + "%' AND _SurveyTitle LIKE  '%" + words + "%'", // "SurveyEnable=?"
				new String[] { "0" }, // new String[]{"1"}
				null);// "SurveyId ASC"
		while (c.moveToNext()) {
			Survey s = new Survey();
			s.surveyId = c.getString(0);
			// System.out.println("顺序号:" + s.surveyId);
			s.surveyTitle = c.getString(1);
			s.isPhoto = c.getInt(2);
			s.isRecord = c.getInt(3);
			s.isExpand = c.getInt(4);
			s.isDowned = c.getInt(5);
			s.surveyEnable = c.getInt(6);
			s.downloadUrl = c.getString(7);
			s.publishTime = c.getString(8);
			s.surveyContent = c.getString(9);
			s.id = c.getInt(10);
			s.visitMode = c.getInt(11);
			s.isTest = c.isNull(12) ? 0 : c.getInt(12);
			s.openStatus = c.isNull(13) ? 0 : c.getInt(13);
			s.innerDone = c.isNull(14) ? 0 : c.getInt(14);
			s.globalRecord = c.isNull(15) ? 0 : c.getInt(15);
			s.eligible = c.isNull(16) ? -1 : c.getInt(16);
			// 问卷提醒获得提醒
			s.setNoticeNew(c.isNull(17) ? 0 : c.getInt(17));
			// 摄像赋值
			s.isVideo = c.isNull(18) ? 0 : c.getInt(18);
			// 生成时间
			s.setGeneratedTime(c.getString(19));
			s.setWord(c.getString(20));
			ss.add(s);
		}
		if (ss.size() > 1) {
			ss = Util.sort(ss);
		}
		c.close();
		return ss;
	}

	// 新上传
	/**
	 * 查询所有已完成且需要上传的UploadFeed
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getAllSurveysCompletedUploadFeed(String userId, String mySid) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		String where = mySid + " AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=?";
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", //
				"_FilePath", "_FileName", "_FileSize", "_FileType", "_IsUploaded" }, //
				where, //
				new String[] { "1", "1", "0", userId }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUuid(c.getString(1));
			feed.setSurveyId(c.getString(2));
			feed.setPath(c.getString(3));
			feed.setName(c.getString(4));
			feed.setSize(c.getLong(5));
			feed.setType(c.getInt(6));
			feed.setIsUploaded(c.getInt(7));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 上传
	 * 
	 * @param userId
	 * @param surveyId
	 * @param isUploaded
	 * @return
	 */
	public int updateFeedStatusBySurveys(String userId, int isUploaded, String mySid) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		String where = mySid + " AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _IsUploaded=?";
		ContentValues values = new ContentValues();
		values.put("_IsUploaded", isUploaded);
		return resolver.update(uri, values, where, new String[] { "1", "1", "0", userId, "1" });
	}

	/**
	 * 上传 查询所有已完成且需要上传的UploadFeed
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getAllSurveysCompletedUploadFeedIpsos(String mySid, String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		String where = mySid + " AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _IsUploaded=?";
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", //
				"_FilePath", "_FileName", "_FileSize", "_FileType", "_IsUploaded" }, //
				where, //
				new String[] { "1", "1", "0", userId, "2" }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUuid(c.getString(1));
			feed.setSurveyId(c.getString(2));
			feed.setPath(c.getString(3));
			feed.setName(c.getString(4));
			feed.setSize(c.getLong(5));
			feed.setType(c.getInt(6));
			feed.setIsUploaded(c.getInt(7));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	/**
	 * 统计已完成的且未上传的 上传
	 * 
	 * @param surveyId
	 * @return
	 */
	public long feedUnUploadSurveysCountIpsos(String mySid, String userId) {
		long count = 0;
		String where = mySid + " AND _IsCompleted=? AND _IsUploaded=? AND _FileType=? AND _GiveUp=? AND _UserId=?";
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, where, new String[] { "1", "2", "1", "0", userId }, null);
		if (c.moveToFirst()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}

	/**
	 * 上传 查询所有已完成且需要上传的UploadFeed
	 * 
	 * @param surveyId
	 * @return
	 */
	public ArrayList<UploadFeed> getAllCompletedSurveysUploadFeedIpsos(String mySid, String userId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		String where = mySid + " AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _UserId=? AND _IsUploaded=?";
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_id", "_UUID", "_SurveyId", //
				"_FilePath", "_FileName", "_FileSize", "_FileType", "_IsUploaded" }, //
				where, //
				new String[] { "1", "1", "0", userId, "2" }, null);
		while (c.moveToNext()) {
			UploadFeed feed = new UploadFeed();
			feed.setId(c.getLong(0));
			feed.setUuid(c.getString(1));
			feed.setSurveyId(c.getString(2));
			feed.setPath(c.getString(3));
			feed.setName(c.getString(4));
			feed.setSize(c.getLong(5));
			feed.setType(c.getInt(6));
			feed.setIsUploaded(c.getInt(7));
			fs.add(feed);
		}
		c.close();
		return fs;
	}

	// 大树 重置 在这里 一下

	/**
	 * 重置功能 统计已经上传并且是该项目的该feedId sid
	 * 
	 * @param surveyId
	 * @return
	 */
	public UploadFeed getResetFeed(String surveyId, String feedId) {
		UploadFeed feed = null;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "_UUID", "_FilePath", "_FileName" }, "_SurveyId=? AND _IsCompleted=? AND _FileType=? AND _GiveUp=? AND _FeedId=? AND _IsUploaded=?",
				new String[] { surveyId, "1", "1", "0", feedId, "9" }, null);
		if (c.moveToFirst()) {
			feed = new UploadFeed();
			feed.setUuid(c.getString(0));
			feed.setPath(c.getString(1));
			feed.setName(c.getString(2));
		}
		c.close();
		return feed;
	}

	// 重置功能
	public int updateResetFeed(String uuid) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_RegTime", "");
		values.put("_FileSize", 0);
		values.put("_IsUploaded", 0);
		values.put("_IsCompleted", 0);
		values.put("_ReturnType", 0);
		return resolver.update(uri, values, "_UUID=? and _FileType=?", new String[] { uuid, "1" });
	}

	// 重置功能
	public int deleteResetFeedAttach(String uuid) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		return resolver.delete(uri, "_UUID=? and _FileType is not 1", new String[] { uuid });
	}

	/**
	 * 删除order=-1问题的答案
	 * 
	 * @param surveyId
	 * @param index
	 */
	public int deleteResetAnswer(String surveyId, String uuid) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_ANSWER);
		return resolver.delete(uri, "_SurveyId=? AND _UUID=?", new String[] { surveyId, uuid });
	}

	// 大树 重置 以上 部分

	// 大树查询 _GiveUp='2' 的受访者
	// select * from tb_UploadFeed
	// where _UserId='imsf961' and _GiveUp='3'
	// and _IsUploaded='0' and _IsCompleted='1'
	public long queryUploadGiveUP(String _SurveyId, String userId) {
		long count = 0;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, "_SurveyId=? AND _UserId=? AND _GiveUp=? AND _IsUploaded=? AND _IsCompleted=?",
				new String[] { _SurveyId, userId, "2", "0", "1", }, null);
		if (c.moveToFirst()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}

	// 大树查询 更新 _GiveUp='2' 的受访者 _GiveUp 置为 0
	// update tb_UploadFeed set _GiveUp='3'
	// where _UserId='imsf961' and _GiveUp='2' and _IsUploaded='0'
	// and _IsCompleted='1'
	public boolean updateUploadFeed(String _SurveyId, String userId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_GiveUp", 0);
		int i = resolver.update(uri, values, "_SurveyId=? AND _UserId=? AND _GiveUp=? AND _IsUploaded=? AND _IsCompleted=? ", new String[] { _SurveyId, userId, "2", "0", "1" });
		return 0 != i;
	}

	/**
	 * 数据字典
	 * 
	 * @param classId
	 * @return
	 */
	public boolean IsExistData(String classId) {
		boolean isExist = false;
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB5);
		Cursor c = resolver.query(uri, new String[] { "COUNT(_id)" }, //
				"_AddTo01=?", //
				new String[] { classId }, null);
		if (c.moveToFirst()) {
			isExist = (0 < c.getLong(0));
		}
		c.close();
		return isExist;
	}

	/**
	 * 数据字典
	 * 
	 * **/
	public boolean addData(Data data) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB5);
		ContentValues values = new ContentValues();
		values.put("_AddTo01", data.getClassId());
		values.put("_AddTo02", data.getClassName());
		values.put("_AddTo03", data.getDatas());
		Uri u = resolver.insert(uri, values);
		return Util.isSuccess(u.toString());
	}

	/** 数据字典 **/
	public boolean updateData(Data data) {
		// ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB5);
		ContentValues values = new ContentValues();
		values.put("_AddTo02", data.getClassName());
		values.put("_AddTo03", data.getDatas());
		int i = resolver.update(uri, values, "_AddTo01=?", new String[] { data.getClassId() });
		return 0 != i;
	}

	/** 数据字典 **/
	public boolean updateDataLocalDatas(String localDatas, String classId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB5);
		ContentValues values = new ContentValues();
		values.put("_AddTo04", localDatas);
		int i = resolver.update(uri, values, "_AddTo01=?", new String[] { classId });
		return 0 != i;
	}

	/**
	 * 数据字典
	 */
	public Data queryDataById(String classId) {
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_TAB5);
		Cursor c = resolver.query(uri, new String[] { "_AddTo03",// 0
				"_AddTo04" }, "_AddTo01=?", new String[] { classId }, null);
		Data data = null;
		if (c.moveToFirst()) {
			data = new Data();
			data.setDatas(c.getString(0));
			data.setLocalDatas(c.getString(1));
		}
		c.close();
		return data;
	}

	
	
	/**
	 * 删除匿名数据假删
	 */
	public boolean deleteFakeUploadFeed(ArrayList<Long> idList) {
		
		Uri uri = Uri.parse("content://" + DBProvider.AUTHOR + "/" + DBProvider.TAB_UPLOAD_FEED);
		ContentValues values = new ContentValues();
		values.put("_GiveUp", 1);
		String condition="";
		if(Util.isEmpty(idList)){
			return false;
		}
		String[] arr=new String[idList.size()];
		for(int i=0;i<idList.size();i++){
			arr[i]=idList.get(i)+"";
			if(i==0){
				condition+="_id=?";
			}else if(i>0){
				condition+=" or _id=?";
			}
		}
		return 0 != resolver.update(uri, values, condition, arr);
	}
	
}
