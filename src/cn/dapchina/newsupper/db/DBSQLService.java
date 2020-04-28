package cn.dapchina.newsupper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBSQLService {

	private DBOpenHelper dbOpenHelper;
	
	public DBSQLService(Context _c){
		dbOpenHelper = DBOpenHelper.getInstance(_c);
	}
	
	public boolean updateSQL(String sql){
		boolean isSuccess = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.beginTransaction();
			try {
				db.execSQL(sql);
				db.setTransactionSuccessful();
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.endTransaction();
				db.close();
			}
		}
		return isSuccess;
	}
}
