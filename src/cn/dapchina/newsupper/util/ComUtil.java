package cn.dapchina.newsupper.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Spinner;
import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.AnswerMap;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.QuestionItem;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;

public class ComUtil {
	/**
	 * 判断集合是否为空或者没有元素
	 * 
	 * @param coll
	 * @return
	 */
	public static boolean isEmpty(Collection<?> coll) {
		return (null == coll || coll.isEmpty());
	}

	/**
	 * 判断字符串是否为空或为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (null == str || 0 == str.trim().length());
	}

	/**
	 * 判断文件是否存在或是否可用
	 * 
	 * @param file
	 * @return false表存在且可用,true表不存在或不可用
	 */
	public static boolean isEffective(String path, String name) {
		File file = new File(Uri.parse(path + File.separator + name).getPath());
		if (file.exists() && 0 < file.length()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断文件是否存在或是否可用
	 * 
	 * @param file
	 * @return false表存在且可用,true表不存在或不可用
	 */
	public static boolean isEffective(String path) {
		File file = new File(path);
		if (file.exists() && 0 < file.length()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param sTime
	 *            传入的时间
	 * @param fmt
	 *            true完整的 false年月日格式
	 * @return 年月日 时分秒 格式的字符串时间 或者是年月日字符串时间
	 */
	public static long getTime(String sTime, int type) {
		String p = null;
		switch (type) {
		case 0:
			p = "yyyy-MM-dd";
			break;

		case 1:
			p = "yyyy-MM-dd HH:mm";
			break;

		case 2:
			p = "yyyy-MM-dd HH:mm:ss";
			break;

		case 3:
			p = "yyyy/MM/dd HH:mm:ss";
			break;

		case 4:
			p = "yyyy_MM_dd_HH_mm_ss";
			break;
		}
		try {
			DateFormat df = new SimpleDateFormat(p);
			Date d = df.parse(sTime);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 
	 * @param lTime
	 *            传过来的long型日期
	 * @param type
	 *            ={ 0.yyyy-MM-dd HH:mm:ss; 1.yyyy-MM-dd, 2.HH:mm:ss, 3.年月日
	 *            4.时分秒 }
	 * @return 返回字符串
	 */
	public static String getTime(long lTime, int type) {
		String p = "";
		switch (type) {
		case 0:
			p = "yyyy-MM-dd HH:mm:ss";
			break;
		case 1:
			p = "yyyy-MM-dd";
			break;
		case 2:
			p = "HH:mm:ss";
			break;
		case 3:
			p = "MM月dd日";
			break;
		case 4:
			p = "HH:mm";
			break;
		case 5:
			p = "yyyy/MM/dd HH:mm:ss";
			break;
		case 6:
			p = "yyyyMMddHHmmss";
			break;
		case 7:
			p = "yyyy-MM-dd HH:mm";
			break;
		case 8:
			p = "yyyy 年  MM 月 dd 日";
			break;
		case 9:
			p = "yyyy年MM月dd日   HH时mm分";
			break;
		case 10:
			p = "yyyy_MM_dd_HH_mm_ss";
			break;
		}
		return new SimpleDateFormat(p)//
				.format(new Date(lTime));
	}

	/**
	 * 判断按钮是否被连续频繁的点击
	 */
	private static long lastClick;

	public static boolean validateClick() {
		long current = System.currentTimeMillis();
		if (500 > (current - lastClick)) {
			return true;
		}
		lastClick = current;
		return false;
	}

	public static String isToString(InputStream inStream) throws Exception {
		return new String(read(inStream), "UTF-8");
	}

	/**
	 * 读取输入流数据
	 * 
	 * @param inStream
	 * @return
	 */
	public static byte[] read(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.flush();
		inStream.close();
		outStream.close();
		return outStream.toByteArray();
	}

	public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (null != info && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 加压zip文件
	 * 
	 * @param srcFile
	 *            eg: xxx:/src/yyy.zip
	 * @param destPath
	 *            eg: yyy:/dist/
	 */
	public static void decompress(String srcFile, String destPath) {
		try {
			File outFile = new File(destPath);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ZipFile zipFile = new ZipFile(srcFile);
			Enumeration<?> en = zipFile.getEntries();
			ZipArchiveEntry zipEntry = null;
			while (en.hasMoreElements()) {
				zipEntry = (ZipArchiveEntry) en.nextElement();
				if (zipEntry.isDirectory()) {
					String dirName = zipEntry.getName();
					dirName = dirName.substring(0, dirName.length() - 1);
					File f = new File(outFile.getPath() + "/" + dirName);
					f.mkdirs();
				} else {
					File f = new File(outFile.getPath() + "/" + zipEntry.getName());
					if (!f.getParentFile().exists()) {
						f.getParentFile().mkdirs();
					}
					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					OutputStream out = new FileOutputStream(f);
					IOUtils.copy(in, out);
					out.close();
					in.close();
				}
			}
			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getFilePath() {
		return Environment.getExternalStorageDirectory()//
				.getAbsolutePath() + File.separator + "DapSurvey";
	}

	/**
	 * 
	 * @param tel
	 * @return
	 */
	public static boolean isFormat(String data, int type) {
		String p = "";
		switch (type) {
		case -2:
			/**
			 * 冰存管校验规则,以10开头的8位数字
			 */
			p = "^10\\d{6}$";
			break;

		case -1:
			p = "^\\d+$";
			break;

		/** 手机校验 **/
		case 1:
			p = "^1[358]\\d{9}$";
			break;

		/** 15位身份证校验 **/
		case 0:
			p = "^[1-8]\\d{7}((0[1-9])|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
			break;

		/** 18位身份证校验 **/
		case 2:
			p = "^\\d{6}(19\\d{2}|20\\d{2})(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])(\\d{3})?[\\da-zA-Z]$";
			break;

		case 3:
			p = "\\d{4}$";
			break;

		/** 邮箱校验 **/
		case 4:
			p = "[a-zA-Z0-9_-][\\.a-zA-Z0-9_-]*@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+";
			break;

		case 5:
			p = "^-?[0-9]\\d*$";
			break;

		/** 电话话校验 **/
		case 6:
			p = "^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";
			break;

		/** 带分机号校验 **/
		case 7:
			p = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
			break;
		}

		return Pattern.compile(p)//
				.matcher(data).matches();
	}

	public static boolean isSuccess(String uri) {
		String tail = uri.substring(uri.lastIndexOf("/") + 1);
		System.out.println("tail--->" + tail);
		if (isFormat(tail, -1)) {
			return true;
		}
		return false;
	}

	public static String getTimeSec() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		String time = formatter.format(curDate);
		return time;
	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 设置 题外关联之和 的 判断 如果符合条件那么
	 */
	public static boolean questionOutyingRelevance(Question q, ArrayList<AnswerMap> answerMapArr, MyApp ma, UploadFeed feed) {

		if (!q.qParentAssociatedCheck.equals("") && q.qType == Cnt.TYPE_FREE_TEXT_BOX && null == q.freeSymbol) {
			String value = q.qParentAssociatedCheck;
			String[] ss = value.split(",");
			double sum = 0.0;
			double sum1 = 0.0;
			if (ss[0].equals("2")) {
				Answer anPre = ma.dbService.getAnswer(feed.getUuid(), ss[1]);
				if (anPre != null && anPre.getAnswerMapArr() != null) {
					for (AnswerMap map : anPre.getAnswerMapArr()) {
						if (map.getAnswerName().split("_")[3].equals(ss[2])) {
							if (!Util.isEmpty(map.getAnswerText())) {
							sum = Double.valueOf(map.getAnswerText().trim());
						}}
					}
				}
			} else {
				return false;
			}
			/**
			 * 获取 答案的值 各个文本框的数字 !
			 */
			if (answerMapArr != null) {
				for (AnswerMap map : answerMapArr) {
					String value1 = map.getAnswerText().trim();
					if (Util.isFormat(value1, 9)) {
						sum1 += Double.valueOf(value1);
					}
				}
			}
			if (sum != sum1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 题外关联 之 选项置顶 判断 是否是 大树 预览 添加 1
	 */

	public static boolean[] isItemStick(ArrayList<QuestionItem> tempRows) {
		boolean[] isItems = { false, false };
		for (QuestionItem item : tempRows) {

			if (item.padding != null && item.padding == 1) {
				isItems[0] = true;
			} else if (item.padding != null && item.padding == 2) {
				isItems[1] = true;
			}
		}

		return isItems;
	}

	/**
	 * 题外关联 之 选项置顶 获取所有 选项置顶的 选项 大树 预览 添加 2
	 */
	public static int[] getItemStick(ArrayList<QuestionItem> tempRows) {
		int[] list = { 100, 100 };
		for (QuestionItem item : tempRows) {
			if (item.padding != null && item.padding == 1) {
				list[0] = item.itemValue;
			} else if (item.padding != null && item.padding == 2) {
				list[1] = item.itemValue;
			}

		}
		return list;
	}

	/**
	 * 题外关联 获取 之和 获取 关联题目的 答案 大树 预览 添加 3
	 * 
	 * @param q
	 * @param ma
	 * @param feed
	 * @return
	 */
	public static String getRelevanceAnswer(Question q, MyApp ma, UploadFeed feed) {

		String answer = "";
		if (!q.qParentAssociatedCheck.equals("") && q.qType == Cnt.TYPE_FREE_TEXT_BOX && null == q.freeSymbol) {
			String value = q.qParentAssociatedCheck;
			String[] ss = value.split(",");
			if (ss[0].equals("2")) {
				Answer anPre = ma.dbService.getAnswer(feed.getUuid(), ss[1]);
				if (anPre != null && anPre.getAnswerMapArr() != null) {
					for (AnswerMap map : anPre.getAnswerMapArr()) {
						if (map.getAnswerName().split("_")[3].equals(ss[2])) {
							answer = map.getAnswerText().trim();
						}
					}
				}
			}
		}

		return answer;
	}

	/**
	 * 题外关联 之 显示 的处理 获取题外关联显示的选项 大树 预览 添加 4
	 */

	public static ArrayList<QuestionItem> getOutLyingRelevanceDisplayItems(Question q, UploadFeed feed, MyApp ma, ArrayList<QuestionItem> tbColumns) {

		ArrayList<QuestionItem> tempColomns = new ArrayList<QuestionItem>();
		boolean display = false;
		double num = 0;
		if (q.qParentAssociatedCheck != null && !q.qParentAssociatedCheck.equals("")) {
			Log.i("zrl1", q.qParentAssociatedCheck);
			String[] ss = q.qParentAssociatedCheck.split(",");
			if (ss[0].equals("1")) {
				Answer answer = ma.dbService.getAnswer(feed.getUuid(), ss[1]);
				if (answer != null && answer.getAnswerMapArr() != null) {
					for (AnswerMap map : answer.getAnswerMapArr()) {
						if (map.getAnswerName().split("_")[3].equals(ss[2])) {
							display = true;
							if (!Util.isEmpty(map.getAnswerText())) {
								num = Double.valueOf(map.getAnswerText());
							}
						}
					}
					num = num * q.freeTextColumn;
				}
			}
		}

		for (int i = 0; i < tbColumns.size(); i++) {
			tbColumns.get(i).isHide = false;
		}

		/**
		 * 设置 显示和隐藏 选项 是最好的 解决办法
		 */
		if (display) {

			if (num < tbColumns.size()) {
				// 1 2 3
				for (int i = 1; i < tbColumns.size() + 1; i++) {

					if (i <= num) {
						tbColumns.get(i - 1).isHide = false;
					} else {
						tbColumns.get(i - 1).isHide = true;
					}

				}

			}
			tempColomns = tbColumns;
		} else {
			tempColomns = q.getColItemArr();
		}

		/**
		 * 设置当页面 加载 的时候 从 答案中 获取 要显示的 选项个数 ！　
		 */
		// if (display) {
		// ArrayList<QuestionItem> tempQis=new ArrayList<QuestionItem>();
		//
		// if (num<=tbColumns.size()) {
		// for (int i = 0; i <num; i++) {
		// tempQis.add(tbColumns.get(i));
		// }
		// }else {
		// tempQis=tbColumns;
		// }
		// tempColomns=tempQis;
		// }else {
		// tempColomns=q.getColItemArr();
		// }

		return tempColomns;

	}

	/**
	 * 大树排序 判断 是否 有重复 选项 前提是 必须是排序 q.isorder==true ;
	 */

	public static String getRepeatValue(ArrayList<Integer> orderList, Spinner sp) {
		
		if (orderList.size() == 0) {
			orderList.add(sp.getSelectedItemPosition());
		} else {
			if (orderList.indexOf(sp.getSelectedItemPosition()) != -1) {
				return sp.getSelectedItem().toString();
			} else {
				orderList.add(sp.getSelectedItemPosition());
			}
		}
		return "";
	}

}
