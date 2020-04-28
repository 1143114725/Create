package cn.dapchina.newsupper.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DBProvider extends ContentProvider {
	
	private UriMatcher matcher = null;
	private DBOpenHelper openHelper = null;
	/**受访者信息表**/
	private final static int USER = 1;
	
	private final static int SURVEY = 2;
	
	private final static int QUESTION = 3;
	
	//private final static int USER_SURVEY = 4;
	/**
	 * 答案表
	 */
	private final static int ANSWER = 5;
	
	/**
	 * 大卷表
	 */
	private final static int UPLOAD_FEED = 6;
	
	/**
	 * 上传日志表
	 */
	private final static int UPLOAD_LOG = 7;
	//错误表
	private final static int TAB1 = 8;
	//地图监控序号
	private final static int TAB2 = 9;
	//知识库号
	private final static int TAB3 = 10;
	//记录丢失文件表
	private final static int TAB4 = 11;
	//数据字典
	private final static int TAB5 = 12;
	//配額
	private final static int TAB6 = 13;
		
	public final static String AUTHOR = "dapchina.newsupper.provider";
	
	public final static String TAB_USER = "tb_User";
	public final static String TAB_SURVEY = "tb_Survey";
	public final static String TAB_QUESTION = "tb_Question";
	//public final static String TAB_USER_SURVEY = "tb_UserSurvey";
	public final static String TAB_ANSWER = "tb_Answer";
	public final static String TAB_UPLOAD_FEED = "tb_UploadFeed";
	public final static String TAB_UPLOAD_LOG = "tb_UploadLog";
	
	public final static String TAB_TAB1 = "tb_Tab1";
	//地图监控表名
	public final static String TAB_TAB2 = "tb_Tab2";
	//知识库表名
	public final static String TAB_TAB3 = "tb_Tab3";
	//记录丢失文件表
	public final static String TAB_TAB4 = "tb_Tab4";
	//数据字典
	public final static String TAB_TAB5 = "tb_Tab5";
	//配額
	public final static String TAB_TAB6 = "tb_Tab6";
	
	
	@Override
	public boolean onCreate() {
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		openHelper = DBOpenHelper.getInstance(getContext());//dapchina.peace3vd.provider
		/**匹配受访者信息表**/
		matcher.addURI(AUTHOR, TAB_USER, USER);
		matcher.addURI(AUTHOR, TAB_SURVEY, SURVEY);
		matcher.addURI(AUTHOR, TAB_QUESTION, QUESTION);
		//matcher.addURI(AUTHOR, TAB_USER_SURVEY, USER_SURVEY);
		matcher.addURI(AUTHOR, TAB_ANSWER, ANSWER);
		matcher.addURI(AUTHOR, TAB_UPLOAD_FEED, UPLOAD_FEED);
		matcher.addURI(AUTHOR, TAB_UPLOAD_LOG, UPLOAD_LOG);
		matcher.addURI(AUTHOR, TAB_TAB1, TAB1);
		//加入地图监控URI
		matcher.addURI(AUTHOR, TAB_TAB2, TAB2);
		//加入知识库URI
		matcher.addURI(AUTHOR, TAB_TAB3, TAB3);
		//加入记录丢失文件表
		matcher.addURI(AUTHOR, TAB_TAB4, TAB4);
		//数据字典
		matcher.addURI(AUTHOR, TAB_TAB5, TAB5);
		//配额
		matcher.addURI(AUTHOR, TAB_TAB6, TAB6);
		return true;
	}

	/**
	 * 查询
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		switch (matcher.match(uri)) {
		case USER:
			return db.query(TAB_USER, projection, selection, selectionArgs, null, null, sortOrder);
			
		case SURVEY:
			return db.query(TAB_SURVEY, projection, selection, selectionArgs, null, null, sortOrder);
			
		case QUESTION:
			return db.query(TAB_QUESTION, projection, selection, selectionArgs, null, null, sortOrder);
			
//		case USER_SURVEY:
//			return db.query(TAB_USER_SURVEY, projection, selection, selectionArgs, null, null, sortOrder);
			
		case ANSWER:
			return db.query(TAB_ANSWER, projection, selection, selectionArgs, null, null, sortOrder);
			
		case UPLOAD_FEED:
			return db.query(TAB_UPLOAD_FEED, projection, selection, selectionArgs, null, null, sortOrder);
			
		case UPLOAD_LOG:
			return db.query(TAB_UPLOAD_LOG, projection, selection, selectionArgs, null, null, sortOrder);
		case TAB1:
			return db.query(TAB_TAB1, projection, selection, selectionArgs, null, null, sortOrder);
		//地图监控查询方法
		case TAB2:
			return db.query(TAB_TAB2, projection, selection, selectionArgs, null, null, sortOrder);
		//知识库查询方法
		case TAB3:
			return db.query(TAB_TAB3, projection, selection, selectionArgs, null, null, sortOrder);
		//加入记录丢失文件表
		case TAB4:
			return db.query(TAB_TAB4, projection, selection, selectionArgs, null, null, sortOrder);
		//加入记录丢失文件表
		case TAB5:
			return db.query(TAB_TAB5, projection, selection, selectionArgs, null, null, sortOrder);
		case TAB6:
			return db.query(TAB_TAB6, projection, selection, selectionArgs, null, null, sortOrder);
		default:
			throw new IllegalArgumentException("No match uri: " + uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		
		return null;
	}

	/**
	 * 增加
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		switch (matcher.match(uri)) {
		case USER:
			long uid = db.insert(TAB_USER, "_id", values);
			Uri uUri = ContentUris.withAppendedId(uri, uid);
			return uUri;
			
		case SURVEY:
			long sid = db.insert(TAB_SURVEY, "_id", values);
			Uri sUri = ContentUris.withAppendedId(uri, sid);
			return sUri;
			
		case QUESTION:
			long qid = db.insert(TAB_QUESTION, "_id", values);
			Uri qUri = ContentUris.withAppendedId(uri, qid);
			return qUri;
			
//		case USER_SURVEY:
//			long usid = db.insert(TAB_USER_SURVEY, "_id", values);
//			Uri usUri = ContentUris.withAppendedId(uri, usid);
//			return usUri;
			
		case ANSWER:
			long aid = db.insert(TAB_ANSWER, "_id", values);
			Uri aUri = ContentUris.withAppendedId(uri, aid);
			return aUri;
			
		case UPLOAD_FEED:
			long ufid = db.insert(TAB_UPLOAD_FEED, "_id", values);
			Uri ufUri = ContentUris.withAppendedId(uri, ufid);
			return ufUri;
			
		case UPLOAD_LOG:
			long ulid = db.insert(TAB_UPLOAD_LOG, "_id", values);
			Uri ulUri = ContentUris.withAppendedId(uri, ulid);
			return ulUri;
		case TAB1:
			long ttid = db.insert(TAB_TAB1, "_id", values);
			Uri ttUri = ContentUris.withAppendedId(uri, ttid);
			return ttUri;
		//地图监控增加方法
		case TAB2:
			long ttid2 = db.insert(TAB_TAB2, "_id", values);
			Uri ttUri2 = ContentUris.withAppendedId(uri, ttid2);
			return ttUri2;
		//知识库增加方法
		case TAB3:
			long ttid3 = db.insert(TAB_TAB3, "_id", values);
			Uri ttUri3 = ContentUris.withAppendedId(uri, ttid3);
			return ttUri3;
		//加入记录丢失文件表
		case TAB4:
			long ttid4 = db.insert(TAB_TAB4, "_id", values);
			Uri ttUri4 = ContentUris.withAppendedId(uri, ttid4);
			return ttUri4;
		//数据字典
		case TAB5:
			long ttid5 = db.insert(TAB_TAB5, "_id", values);
			Uri ttUri5 = ContentUris.withAppendedId(uri, ttid5);
			return ttUri5;
		case TAB6:
			long ttid6 = db.insert(TAB_TAB6, "_id", values);
			Uri ttUri6 = ContentUris.withAppendedId(uri, ttid6);
			return ttUri6;
		default:
			throw new IllegalArgumentException("No match uri: " + uri);
			
		}
	}

	/**
	 * 删除
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		switch (matcher.match(uri)) {
		case USER:
			int uC = db.delete(TAB_USER, selection, selectionArgs);
			return uC;
			
		case SURVEY:
			int sC = db.delete(TAB_SURVEY, selection, selectionArgs);
			return sC;
			
		case QUESTION:
			int qC = db.delete(TAB_QUESTION, selection, selectionArgs);
			return qC;
			
//		case USER_SURVEY:
//			int usC = db.delete(TAB_USER_SURVEY, selection, selectionArgs);
//			return usC;
			
		case ANSWER:
			int aC = db.delete(TAB_ANSWER, selection, selectionArgs);
			return aC;
			
		case UPLOAD_FEED:
			int ufC = db.delete(TAB_UPLOAD_FEED, selection, selectionArgs);
			return ufC;
			
		case UPLOAD_LOG:
			int ulC = db.delete(TAB_UPLOAD_LOG, selection, selectionArgs);
			return ulC;
		case TAB1:
			int ttC = db.delete(TAB_TAB1, selection, selectionArgs);
			return ttC;
		//地图监控删除方法
		case TAB2:
			int ttC2 = db.delete(TAB_TAB2, selection, selectionArgs);
			return ttC2;
		//知识库删除方法
		case TAB3:
			int ttC3 = db.delete(TAB_TAB3, selection, selectionArgs);
			return ttC3;
		//加入记录丢失文件表
		case TAB4:
			int ttC4 = db.delete(TAB_TAB4, selection, selectionArgs);
			return ttC4;
		//数据字典
		case TAB5:
			int ttC5 = db.delete(TAB_TAB5, selection, selectionArgs);
			return ttC5;
			//配额
		case TAB6:
			int ttC6 = db.delete(TAB_TAB6, selection, selectionArgs);
			return ttC6;
		default:
			throw new IllegalArgumentException("No match uri: " + uri);
		}
	}

	/**
	 * 更新
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		switch (matcher.match(uri)) {
		case USER:
			int uC = db.update(TAB_USER, values, selection, selectionArgs);
			return uC;
			
		case SURVEY:
			int sC = db.update(TAB_SURVEY, values, selection, selectionArgs);
			return sC;
		
		case QUESTION:
			int qC = db.update(TAB_QUESTION, values, selection, selectionArgs);
			return qC;
			
//		case USER_SURVEY:
//			int usC = db.update(TAB_USER_SURVEY, values, selection, selectionArgs);
//			return usC;
			
		case ANSWER:
			int aC = db.update(TAB_ANSWER, values, selection, selectionArgs);
			return aC;
			
		case UPLOAD_FEED:
			int ufC = db.update(TAB_UPLOAD_FEED, values, selection, selectionArgs);
			return ufC;
			
		case UPLOAD_LOG:
			int ulC = db.update(TAB_UPLOAD_LOG, values, selection, selectionArgs);
			return ulC;
		case TAB1:
			int ttC = db.update(TAB_TAB1, values, selection, selectionArgs);
			return ttC;
		//地图监控更新方法
		case TAB2:
			int ttC2 = db.update(TAB_TAB2, values, selection, selectionArgs);
			return ttC2;
		//知识库更新方法
		case TAB3:
			int ttC3 = db.update(TAB_TAB3, values, selection, selectionArgs);
			return ttC3;
		//加入记录丢失文件表
		case TAB4:
			int ttC4 = db.update(TAB_TAB4, values, selection, selectionArgs);
			return ttC4;
		//数据字典
		case TAB5:
			int ttC5 = db.update(TAB_TAB5, values, selection, selectionArgs);
			return ttC5;
		//配额
		case TAB6:
			int ttC6 = db.update(TAB_TAB6, values, selection, selectionArgs);
			return ttC6;
		default:
			throw new IllegalArgumentException("No match uri: " + uri);
		}
	}
}
