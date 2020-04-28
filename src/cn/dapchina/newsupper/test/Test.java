package cn.dapchina.newsupper.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.net.Uri;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Base64;
import android.util.Log;
import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.AnswerMap;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.db.DBService;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.StreamTool;

public class Test extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		init();
	}

	String TAG = "_test";
	DBService db;

	/**
	 * 初始化数据操作实例
	 */
	public void init() {
		db = new DBService(getContext());
	}

	/**
	 * 获取某个帐号对应的某个调查的所有访问记录
	 */
	public void getAllVisitRecord() {
		init();
		ArrayList<UploadFeed> fs = db.getAllXmlUploadFeedList("3012", "test");
		for (UploadFeed feed : fs) {
			Log.i(TAG, "feed.uuid=" + feed.getUuid() + ", feed.path=" + feed.getPath() + ", feed.name=" + feed.getName());
		}
	}

	/**
	 * 获取某条访问记录所有的答案
	 */
	public void showAswer() {
		init();
		ArrayList<Answer> ans = db.getUserAllAnswer("2aee79db-1511-4e03-b7e0-da1aa940f451");
		for (Answer an : ans) {
			ArrayList<AnswerMap> ams = an.getAnswerMapArr();
			for (AnswerMap am : ams) {
				Log.i(TAG, am.answerName);
			}
		}
	}

	/**
	 * 打印某条访问记录xml中的内容
	 */
	public void getFeedFile() {
		// feed.uuid=2aee79db-1511-4e03-b7e0-da1aa940f451,
		// feed.path=/data/data/cn.dapchina.supper/files/survey/3012,
		// feed.name=test_3012_20130626104935_2aee79db-1511-4e03-b7e0-da1aa940f451.xml

		try {
			File file = new File("/data/data/cn.dapchina.newsupper/files/survey/3012", "test_3012_20130626104935_2aee79db-1511-4e03-b7e0-da1aa940f451.xml");
			if (!file.exists()) {
				Log.i(TAG, "不存在");
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] bs = Base64.decode(StreamTool.read(fis), Base64.DEFAULT);
			String str = new String(bs);
			Log.i(TAG, str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void feedAnswerIsHave() {
		init();
		// uploadRecordAndPhoto_new--->kjy_2697_20130705092009_79e1b362-ed8a-403b-a28d-a172a5621b39.xml
		boolean b = db.feedAnswerIsHave("79e1b362-ed8a-403b-a28d-a172a5621b39m");
		Assert.assertEquals(true, b);
	}

	public void testParserInnerPanelList() {
		String url = "http://www.dapchina.cn/newsurvey/alisoft/DownloadUser.asp?AuthorID=2473&SurveyID=3076";
		try {
			InputStream inStream = NetService.openUrl(url, null, "GET");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testLanguange() {
		
		String language = mContext.getResources().getConfiguration().locale.getLanguage();
		Log.i(TAG, "language=" + language);
	}

	//
	public void copyDataBase() {
		Uri uri = Uri.parse("/data/data/cn.dapchina.newsupper/databases/db_DapSupper.db");
		File file = new File(uri.getPath());
		if (file.exists()) {
			// TODO 将文件拷贝到指定的文件夹
			FileInputStream fis = null;
			String path = Environment.getExternalStorageDirectory()//
					.getAbsolutePath() + File.separator + "DataBases";
			try {
				fis = new FileInputStream(file);
				File f = new File(path, file.getName());
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(f);
				int len = 0;
				byte[] buff = new byte[1024];
				while (-1 != (len = fis.read(buff))) {
					fos.write(buff, 0, len);
				}
				fos.flush();
				fos.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("q", "fail");
			}
		}
	}
	
	public void getFileInfo(){
		//System.out.println("getFileInfo");
		//db.getRecordList();
		List<UploadFeed> fs = db.getEmptyRecordList();
		for (UploadFeed uf:fs) {
			System.out.println(uf.getName());
		}
	}
}
