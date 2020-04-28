package cn.dapchina.newsupper.util;

import java.io.BufferedReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RadioGroup.LayoutParams;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.activity.NativeModeActivity;
import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.AnswerMap;
import cn.dapchina.newsupper.bean.Call;
import cn.dapchina.newsupper.bean.CstmMatcher;
import cn.dapchina.newsupper.bean.Intervention;
import cn.dapchina.newsupper.bean.MatcherItem;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.QuestionItem;
import cn.dapchina.newsupper.bean.Restriction;
import cn.dapchina.newsupper.bean.RestrictionValue;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.view.DoubleDatePickerDialog;
import cn.dapchina.newsupper.view.NumericWheelAdapter;
import cn.dapchina.newsupper.view.OnWheelChangedListener;
import cn.dapchina.newsupper.view.WheelView;

/**
 * 通用工具类
 */
public class Util {

	// public final static String REX_RN = "\r\n";

	private static int START_YEAR = 1000, END_YEAR = 9999;

	/**
	 * 大于
	 */
	public final static String GREATER_THAN = ">";
	/**
	 * 大于等于
	 */
	public final static String GREATER_EQUAL = ">=";
	/**
	 * 小于
	 */
	public final static String LESS_THAN = "<";
	/**
	 * 小于等于
	 */
	public final static String LESS_EQUAL = "<=";

	private Util() {

	}

	/**
	 * 验证字符串的有效性
	 */
	public static boolean isEmpty(CharSequence s) {
		return (null == s || "".equals(s.toString().trim()));
	}

	public static boolean isEmpty(String[] s) {
		return (null == s || 0 == s.length);
	}

	public static boolean isNullStr(String str) {
		return "null".equals(str.trim().toLowerCase());
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return (null == map || map.isEmpty());
	}

	public static boolean isEmpty(Collection<?> coll) {
		return (null == coll || 0 == coll.size());
	}

	/** 获取res/string/目录下的文字资源方法 **/
	public static String getString(Context _context, int id) {
		return _context.getResources().getString(id);
	}

	/**
	 * 判断数组的有效性
	 */
	public static boolean isEmpty(Object[] col) {
		return null == col || 0 == col.length;
	}

	/**
	 * 将字符串转换成字符串组数,按照指定的标记进行转换
	 */
	public static String[] str2Arr(String str, String tag) {
		if (isEmpty(str)) {
			return str.split(tag);
		}
		return null;
	}

	/**
	 * 将数组转换成字符串,按照指定的tag进行拼接
	 */
	public static String arr2Str(Object[] arr, String tag) {
		String temp = "";
		if (!isEmpty(arr)) {
			for (int i = 0; i < arr.length; i++) {
				if (i == (arr.length - 1)) {
					temp = temp + arr[i];
				} else {
					temp = temp + arr[i] + tag;
				}
			}
		}
		return temp;
	}

	public static String getCurrentDate(int type) {
		return getTime(System.currentTimeMillis(), type);
	}

	/**
	 * 获取所有MP3文件
	 * 
	 * */
	public static Vector<String> GetVideoFileName(String fileAbsolutePath,UploadFeed feed) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
 
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(".mp3")) {
                	if(filename.trim().indexOf(feed.getSurveyId())!=-1 && filename.trim().indexOf(feed.getUuid())!=-1){
                		vecFile.add(filename);
                	}
                }
            }
        }
        return vecFile;
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
		case -1:
			p = "yyyyMMddHHmm";
			break;

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
		}
		return new SimpleDateFormat(p)//
				.format(new Date(lTime));
	}

	public static boolean isSuccess(String uri) {
		String tail = uri.substring(uri.lastIndexOf("/") + 1);
		// System.out.println("tail--->" + tail);
		if (isFormat(tail, -1)) {
			return true;
		}
		return false;
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

		/**
		 * 是否为数字
		 */
		case -1:
			p = "^\\d+$";
			break;

		/** 手机校验 **/
		case 1:
			p = "^1[3458]\\d{9}$";
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

		/**
		 * 是否为数字
		 */
		case 8:
			p = "^\\d+.\\d+$";
			break;
		/**
		 * 是否为数字和负数或者小数或者整数
		 */
		case 9:
			p = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
			break;
		/**
		 * 是否为正数或者负数
		 */
		case 10:
			p = "^(\\-|\\+)?\\d+$";
			break;
		/**
		 * 是否是英文或者数字
		 */
		case 11:
			p = "(^(\\-|\\+)?\\d+(\\.\\d+)?$)|(\\d+.\\d+|\\w+)";
			break;
		}

		return Pattern.compile(p)//
				.matcher(data).matches();
	}

	public static File getFilePath(Context _c) {
		return _c.getCacheDir();
	}

	public static String getFileNameAtUrl(String url) {
		if (null != url && 0 < url.length()) {
			return url.substring(url.lastIndexOf("/") + 1);
		}
		return null;
	}

	/**
	 * 
	 * @param q
	 *            问题实体
	 * @param item
	 *            选项实体
	 * @param row
	 *            第几行
	 * @param col
	 *            第几列
	 * @return
	 */
	public static String GetAnswerName(Question q, QuestionItem item, int row, int col, boolean isEdit) {

		// VIS_0_20_0_0_10_2_0_0_0
		StringBuilder sb = new StringBuilder("");
		switch (q.qType) {
		// case Cnt.TYPE_HEADER://标题
		// //"VIS_"+q.qType+"_"+q.qIndex
		// sb.append("VIS_");
		// sb.append(q.qType);
		// break;

		case Cnt.TYPE_RADIO_BUTTON:// 单选
			/**
			 * 1 2 3 4 5 6 7 8 9 10 radio VIS_0_3_0_0_01_5_0_0_0 edit
			 * VIS_0_3_3_2_01_5_0_0_0 其他项
			 */
			sb.append("VIS_");// 第一组
			sb.append(Cnt.TYPE_RADIO_BUTTON);// 第二组, 对象类型编号规则, 即题型
			sb.append("_");
			sb.append(q.qIndex);// 第三组, 问题ID规则,即index

			// 第4、5组
			if (isEdit) {// 是其他项的文本框
				sb.append("_").append(item.itemValue).append("_2_");
			} else {
				sb.append("_0_0_");
			}

			// 第6组 是两个数字 第一个是是否必填。第二个是是否有其他选项
			if (1 == q.qRequired) {// 必填
				sb.append("1");
			} else {// 非必填
				sb.append("0");
			}
			if (1 == q.haveOther) {// 有其他项
				sb.append("1_");
			} else {// 无其他项
				sb.append("0_");
			}
			/**
			 * 第7组, 选项的总个数
			 */
			sb.append(q.getRowItemArr().size());
			/**
			 * 第8、9、10组
			 */
			sb.append("_0_0_0");
			break;

		case Cnt.TYPE_CHECK_BOX:// 复选
			/**
			 * 1 2 3 4 5 6 7 8 9 10 CHECK_BOX VIS_1_3_0_1_01_5_3_2_0 edit
			 * VIS_1_3_3_2_01_5_3_2_0 其他项
			 */
			sb.append("VIS_");// 第一组
			sb.append(Cnt.TYPE_CHECK_BOX);// 第二组, 对象类型编号规则, 即题型
			sb.append("_");
			sb.append(q.qIndex);// 第三组, 问题ID规则,即index
			sb.append("_");
			// 第四组, 以实际选项数顺序编号
			sb.append(item.itemValue);

			// 第5组
			if (isEdit) {// 是文本框
				sb.append("_2_");//
			} else {
				sb.append("_1_");// 第五组, 复选、矩阵复选为1
			}
			// 第6组
			if (1 == q.qRequired) {// 必填
				sb.append("1");
			} else {// 非必填
				sb.append("0");
			}
			if (1 == q.haveOther) {// 有其他项
				sb.append("1_");
			} else {// 无其他项
				sb.append("0_");
			}
			/**
			 * 第7组, 选项的总个数
			 */
			sb.append(q.getRowItemArr().size());
			sb.append("_");
			/**
			 * 第8组 上限
			 */
			sb.append(q.upperBound);

			sb.append("_");

			/**
			 * 第9组 下限
			 */
			sb.append(q.lowerBound);

			/**
			 * 第10组
			 */
			sb.append("_0");
			break;

		case Cnt.TYPE_MATRIX_RADIO_BUTTON:// 单选矩阵
			/**
			 * 1 2 3 4 5 6 7 8 9 10 单选矩阵 VIS_6_3_0_0_00_5_0_0_0 edit
			 * VIS_6_3_3_0_00_5_0_0_0 其他项
			 */
			sb.append("VIS_");// 第一组
			sb.append(Cnt.TYPE_MATRIX_RADIO_BUTTON);// 第二组, 对象类型编号规则, 即题型
			sb.append("_");
			sb.append(q.qIndex);// 第三组, 问题ID规则,即index
			// 第四组, 一行作为一个选项,按行的顺序编号。
			sb.append("_");
			sb.append(row);
			sb.append("_0_");// 五组, 第四组单选统一为0, 第五组单选或矩阵单选为0

			// 第6组
			if (1 == q.qRequired) {// 必填
				sb.append("1");
			} else {// 非必填
				sb.append("0");
			}
			if (1 == q.haveOther) {// 有其他项
				sb.append("1_");
			} else {// 无其他项
				sb.append("0_");
			}
			/**
			 * 第7组, 选项的总个数
			 */
			sb.append(q.getColItemArr().size());

			/**
			 * 第8、9、10组
			 */
			sb.append("_0_0_0");
			break;

		case Cnt.TYPE_MATRIX_CHECK_BOX:// 复选矩阵
			/**
			 * 1 2 3 4 5 6 7 8 9 10 复选矩阵 VIS_7_3_4_1_00_6_3_1_0 edit
			 * VIS_7_3_4_1_00_6_3_1_0 其他项
			 */
			sb.append("VIS_");// 第一组
			sb.append(Cnt.TYPE_MATRIX_CHECK_BOX);// 第二组, 对象类型编号规则, 即题型
			sb.append("_");
			sb.append(q.qIndex);// 第三组, 问题ID规则,即index
			sb.append("_");
			// 第四、按从左到右，从上向下的顺序逐一编号
			sb.append(q.getColItemArr().size() * row + col);// 列数乘以第几行加上第几列
			sb.append("_1_");// 五组, 复选、矩阵复选为1
			// 第6组
			if (1 == q.qRequired) {// 必填
				sb.append("1");
			} else {// 非必填
				sb.append("0");
			}
			if (1 == q.haveOther) {// 有其他项
				sb.append("1_");
			} else {// 无其他项
				sb.append("0_");
			}
			/**
			 * 第7组, 选项的总个数
			 */
			sb.append(q.getRowItemArr().size() * q.getColItemArr().size());

			/**
			 * 第8组 上限
			 */
			sb.append("_").append(q.upperBound).append("_");

			/**
			 * 第9组 下限
			 */
			sb.append(q.lowerBound);

			/**
			 * 第10组
			 */
			sb.append("_0");
			break;

		case Cnt.TYPE_DROP_DOWN_LIST:// 下拉列表
			/**
			 * 1 2 3 4 5 6 7 8 9 10 下拉列表 VIS_5_3_0_4_00_6_0_0_0 edit
			 * VIS_5_3_0_4_00_6_0_0_0 其他项
			 */
			sb.append("VIS_");// 第一组
			sb.append(Cnt.TYPE_DROP_DOWN_LIST);// 第二组, 对象类型编号规则, 即题型
			sb.append("_");
			sb.append(q.qIndex);// 第三组, 问题ID规则,即index
			/**
			 * 第4、5位
			 */
			sb.append("_0_4_");//
			// 第6组
			if (1 == q.qRequired) {// 必填
				sb.append("1");
			} else {// 非必填
				sb.append("0");
			}
			if (1 == q.haveOther) {// 有其他项
				sb.append("1_");
			} else {// 无其他项
				sb.append("0_");
			}
			/**
			 * 第7组, 选项的总个数
			 */
			sb.append(q.getRowItemArr().size());

			/**
			 * 第8、9、10组
			 */
			sb.append("_0_0_0");
			break;

		case Cnt.TYPE_FREE_TEXT_BOX:// 单行文本框
			/**
			 * 1 2 3 4 5 6 7 8 9 10 单行文本 VIS_2_3_0_2_00_6_0_0_0 edit
			 * VIS_2_3_0_2_00_6_0_0_0 其他项
			 */
			sb.append("VIS_");// 第一组
			sb.append(Cnt.TYPE_FREE_TEXT_BOX);// 第二组, 对象类型编号规则, 即题型
			sb.append("_");
			sb.append(q.qIndex);// 第三组, 问题ID规则,即index
			// sb.append("_2_2_");//第四、五组, 单行文本框为2
			sb.append("_").append(item.itemValue);
			sb.append("_2_");
			// 第6组
			if (1 == q.qRequired) {// 必填
				sb.append("1");
			} else {// 非必填
				sb.append("0");
			}
			if (1 == q.haveOther) {// 有其他项
				sb.append("1_");
			} else {// 无其他项
				sb.append("0_");
			}
			/**
			 * 第7组, 选项的总个数 改动的
			 */
			sb.append(q.getColItemArr().size());
			/**
			 * 第8、9、10组
			 */
			sb.append("_0_0_0");
			break;

		case Cnt.TYPE_FREE_TEXT_AREA:// 文本域
			/**
			 * 1 2 3 4 5 6 7 8 9 10 文本域 VIS_3_3_0_3_00_6_0_0_0 edit
			 * VIS_3_3_0_3_00_6_0_0_0 其他项
			 */
			sb.append("VIS_");// 第一组
			sb.append(Cnt.TYPE_FREE_TEXT_AREA);// 第二组, 对象类型编号规则, 即题型
			sb.append("_");
			sb.append(q.qIndex);// 第三组, 问题ID规则,即index
			sb.append("_").append(item.itemValue);
			sb.append("_3_");// 第四、五组, 多行文本框为3
			// 第6组
			if (1 == q.qRequired) {// 必填
				sb.append("1");
			} else {// 非必填
				sb.append("0");
			}
			if (1 == q.haveOther) {// 有其他项
				sb.append("1_");
			} else {// 无其他项
				sb.append("0_");
			}
			/**
			 * 第7组, 选项的总个数
			 */
			sb.append(q.getRowItemArr().size());
			/**
			 * 第8、9、10组
			 */
			sb.append("_0_0_0");
			break;

		// case Cnt.TYPE_MEDIA://媒体题型
		// sb.append("VIS_");//第一组
		// sb.append(Cnt.TYPE_MEDIA);//第二组, 对象类型编号规则, 即题型
		// sb.append("_");
		// sb.append(q.qIndex);//第三组, 问题ID规则,即index
		// sb.append("_0_0_");//第四、五组, 第四组单选统一为0, 第五组单选或矩阵单选为0
		// //第6组
		// if(1 == q.qRequired){//必填
		// sb.append("1");
		// }else{//非必填
		// sb.append("0");
		// }
		// if(1 == q.haveOther){//有其他项
		// sb.append("1_");
		// }else{//无其他项
		// sb.append("0_");
		// }
		// /**
		// * 第7组, 选项的总个数
		// */
		// sb.append(q.getRowItemArr().size());
		// /**
		// * 第8组
		// */
		//
		// break;
		}
		return sb.toString();
	}

	public static void viewShake(Context _context, View v) {
		Animation shake = AnimationUtils.loadAnimation(_context, R.anim.shake);
		v.startAnimation(shake);
	}

	public static void showView(Context _context, View v, int howShow) {
		// howShow
		Animation anim = AnimationUtils.loadAnimation(_context, howShow);
		v.startAnimation(anim);
	}

	/**
	 * 获取指定字符串中所有符合regex条件的所有子字符串
	 * 
	 * @param regex
	 * @param orgStr
	 *            指定的字符串
	 * @return eg:
	 *         矩阵复::@@6::@@选标题::@@2::@@文字1::@@3::@@文字2::@@4::@@【该题最高得分值:40.00分】
	 *         (必填项目) 指定regex=::@@\\d+::@@
	 *         则能获取::@@6::@@、::@@2::@@、::@@3::@@、::@@4::@@
	 */
	public static ArrayList<String> findMatcherList(String regex, String orgStr) {
		ArrayList<String> list = new ArrayList<String>();
		Matcher matcher = Pattern.compile(regex).matcher(orgStr);
		while (true) {
			if (matcher.find()) {
				String matcherStr = matcher.group();
				list.add(matcherStr);
				orgStr.replace(matcherStr, "");
				continue;
			} else {
				break;
			}
		}
		return list;
	}

	/**
	 * 新方法
	 * 
	 * @param orgStr
	 * @param ma
	 * @param uuid
	 * @param question
	 * @return
	 */
	public static CstmMatcher findMatcherItemList(String orgStr, MyApp ma, String uuid,String sid) {
		CstmMatcher cm = new CstmMatcher();
		ArrayList<MatcherItem> mis = new ArrayList<MatcherItem>();
		System.out.println("orgStr:"+orgStr);
		Matcher matcher = Pattern.compile("::@@\\d+-\\d+::@@|::@@\\d+::@@").matcher(orgStr);
		while (true) {
			if (matcher.find()) {
				String matcherStr = matcher.group();
				MatcherItem mi = new MatcherItem();
				mi.start = orgStr.indexOf(matcherStr);
				Matcher m = Pattern.compile("\\d+-\\d+|\\d+").matcher(matcherStr);
				if (m.find()) {
					String quto = "";
					// m.group()里面的值
					String index = m.group();
					// 假如找到了-，说明是矩阵题目，可以引用某个题目某个选项值
					String realIndex = index;
					String realRow = "";
					if (-1 < index.indexOf("-")) {
						String[] split = index.split("-");
						if (split.length == 2) {
							realIndex = split[0];
							realRow = split[1];
						}
					}
					Answer an = ma.dbService.getAnswer(uuid, realIndex);
					if (!Util.isEmpty(realRow)) {
//						System.out.println("realRow:"+realRow);
						if (null != an) {
							ArrayList<AnswerMap> ams = an.getAnswerMapArr();
							if (!isEmpty(ams)) {
								AnswerMap am = ams.get(Integer.parseInt(realRow));
								String answerValue = am.getAnswerValue();
								/**
								 * 获取列标题
								 */
								Question question = ma.dbService.getQuestion(sid, realIndex);
								ArrayList<QuestionItem> rColmns = new ArrayList<QuestionItem>();
								rColmns.addAll(question.getColItemArr());
								if(!Util.isEmpty(rColmns)){
									QuestionItem questionItem = rColmns.get(Integer.parseInt(answerValue));
									quto = quto + "<font color='blue'>" + questionItem.getItemText() + "</font>";
								}
							}
						}
					} else {
						// 为空 全遍历
						if (null != an) {
							ArrayList<AnswerMap> ams = an.getAnswerMapArr();
							if (!isEmpty(ams)) {
								for (AnswerMap am : ams) {
									// 更改的样式
									quto = quto + "<font color='blue'>" + am.getAnswerText() + "</font>";
								}
							}
						}
					}

					mi.end = mi.start + quto.length();
					orgStr = orgStr.replace(matcherStr, quto);
					if (!isEmpty(quto)) {
						mis.add(mi);
					} else {
						MatcherItem mi2 = new MatcherItem();
						mi2.start = -1;
						mi2.end = -1;
						mis.add(mi2);
					}
					cm.setResultStr(orgStr);
				}
				continue;
			} else {
				break;
			}
		}
		cm.setMis(mis);
		return cm;
	}

	/**
	 * 
	 * @param regex
	 * @param orgStr
	 * @param ma
	 * @param surveyId
	 * @return
	 */
	// public static CstmMatcher findMatcherItemList(String orgStr, MyApp ma,
	// String uuid) {
	// CstmMatcher cm = new CstmMatcher();
	// ArrayList<MatcherItem> mis = new ArrayList<MatcherItem>();
	// Matcher matcher = Pattern.compile("::@@\\d+::@@").matcher(orgStr);
	// while (true) {
	// if (matcher.find()) {
	// String matcherStr = matcher.group();
	// MatcherItem mi = new MatcherItem();
	// mi.start = orgStr.indexOf(matcherStr);
	// Matcher m = Pattern.compile("\\d+").matcher(matcherStr);
	// if (m.find()) {
	// String quto = "";
	// String index = m.group();
	// // System.out.println("index="+index);
	// // if(!answerMap.isEmpty()){
	// Answer an = ma.dbService.getAnswer(uuid, index);
	// if (null != an) {
	// ArrayList<AnswerMap> ams = an.getAnswerMapArr();
	// if (!isEmpty(ams)) {
	// for (AnswerMap am : ams) {
	//
	// //更改的样式
	// quto = quto + "<font color='blue'>"+am.getAnswerText()+"</font>";
	// }
	// }
	// }
	//
	// // }
	//
	// // int order = ma.dbService.getQuestionOrder(surveyId,
	// // Integer.parseInt(index));
	// // String ord =
	// // ma.getResources().getString(R.string.quotation_order,
	// // order);
	// mi.end = mi.start + quto.length();
	// orgStr = orgStr.replace(matcherStr, quto);
	// if (!isEmpty(quto)) {
	// mis.add(mi);
	// } else {
	// MatcherItem mi2 = new MatcherItem();
	// mi2.start = -1;
	// mi2.end = -1;
	// mis.add(mi2);
	// }
	// cm.setResultStr(orgStr);
	// }
	// continue;
	// } else {
	// break;
	// }
	// }
	// cm.setMis(mis);
	// return cm;
	// }

	/**
	 * 获取指定字符串中符合regex条件的字符串(一个或多个)的开始和结束位置 eg 1.<b>中华人民共和国<b>是文化底蕴非常深厚的文化大国.
	 * 
	 * @param regex
	 *            正则 eg:<b>\\S+</b>则获取到“<b>\\S+</b>”在字符串中的开始位置和结束位置
	 * 
	 * @param orgStr
	 *            元字符串年
	 * @return 符合条件字符的开始及结束位置
	 */
	public static ArrayList<MatcherItem> findMatcherItemList(String regex, String orgStr) {
		ArrayList<MatcherItem> mis = new ArrayList<MatcherItem>();
		Matcher matcher = Pattern.compile(regex).matcher(orgStr);
		while (true) {
			if (matcher.find()) {
				String matcherStr = matcher.group();
				MatcherItem mi = new MatcherItem();
				mi.start = orgStr.indexOf(matcherStr);
				mi.end = mi.start + matcherStr.length();
				mis.add(mi);
				orgStr.replace(matcherStr, "");
				continue;
			} else {
				break;
			}
		}
		return mis;
	}

	/**
	 * 按regex替换指定的字符串,并返回替换后的字符串位置 eg: 将 1.<b>中华人民共和国</b>是文化底蕴非常深厚的文化大国. 替换为
	 * 1.中华人民共和国是文化底蕴非常深厚的文化大国. 并返回“中华人民共和国”在“1.中华人民共和国是文化底蕴非常深厚的文化大国.”中的开始结束位置
	 * 
	 * @param regex
	 *            传入正则
	 * @param orgStr
	 *            传入指定的字符串
	 * @param prxRegex
	 *            正则前缀的长度 eg:<b>--->prxRegex=3
	 * @param tailRegex
	 *            正则后缀的长度 eg:</b>--->tailRegex=4
	 * @param ma
	 *            传入应用的上下文
	 * @return
	 */
	// public static ArrayList<MatcherItem> findMatcherList(String orgStr, MyApp
	// ma){
	// ArrayList<MatcherItem> list = new ArrayList<MatcherItem>();
	// String regex =
	// "<b>\\S+</b>|<B>\\S+</B>|<b>\\s+\\S+\\s+</b>|<B>\\s+\\S+\\s+</B>|<b>\\s+\\S+</b>|<B>\\s+\\S+</B>|<b>\\S+\\s+</b>|<B>\\S+\\s+</B>";
	// Matcher matcher = Pattern.compile(regex).matcher(orgStr);
	// while(true){
	// if(matcher.find()){
	// String matcherStr = matcher.group();
	// MatcherItem i = new MatcherItem();
	// i.start = orgStr.indexOf(matcherStr);
	// i.end = i.start + matcherStr.length()-7;
	// list.add(i);
	// orgStr = orgStr.replace(matcherStr, matcherStr.subSequence(3,
	// matcherStr.length()-4));
	// ma.currTitle = orgStr;
	// //System.out.println(orgStr);
	// continue;
	// }else{
	// break;
	// }
	// }
	// return list;
	// }

	public static CstmMatcher findBoldMatcherList(String orgStr) {
		CstmMatcher cm = new CstmMatcher();
		ArrayList<MatcherItem> list = new ArrayList<MatcherItem>();
		if (Util.isEmpty(orgStr)) {
			return cm;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<b>\\S+</b>").append("|");
		sb.append("<b>[a-z|A-Z]+</b>").append("|");
		sb.append("<b>[\u4E00-\u9FA5]+</b>").append("|");
		sb.append("<B>\\S+</B>").append("|");
		sb.append("<b>\\s+\\S+\\s+</b>").append("|");
		sb.append("<B>\\s+\\S+\\s+</B>").append("|");
		sb.append("<b>\\s+\\S+</b>").append("|");
		sb.append("<B>\\s+\\S+</B>").append("|");
		sb.append("<b>\\S+\\s+</b>").append("|");
		sb.append("<b>.*?</b>").append("|");
		sb.append("<B>.*?</B>").append("|");
		sb.append("<B>\\S+\\s+</B>");

		Matcher matcher = Pattern.compile(sb.toString()).matcher(orgStr);
		while (true) {
			if (matcher.find()) {
				String matcherStr = matcher.group();
				MatcherItem i = new MatcherItem();
				i.start = orgStr.indexOf(matcherStr);
				i.end = i.start + matcherStr.length() - 7;
				list.add(i);
				orgStr = orgStr.replace(matcherStr, matcherStr.subSequence(3, matcherStr.length() - 4));
				cm.setResultStr(orgStr);
				// System.out.println(orgStr);
				continue;
			} else {
				break;
			}
		}
		cm.setMis(list);
		return cm;
	}

	/**
	 * 加下划线的
	 * 
	 * @param orgStr
	 * @return
	 */
	public static CstmMatcher findUnderlineMatcherList(String orgStr) {
		CstmMatcher cm = new CstmMatcher();
		ArrayList<MatcherItem> list = new ArrayList<MatcherItem>();
		if (Util.isEmpty(orgStr)) {
			return cm;
		}
		String regex = "<u>\\S+</u>|<U>\\S+</U>|<u>\\s+\\S+\\s+</u>|<U>\\s+\\S+\\s+</U>|<u>\\s+\\S+</u>|<U>\\s+\\S+</U>|<u>\\S+\\s+</u>|<U>\\S+\\s+</U>|<u>.*?</u>|<U>.*?</U>";
		Matcher matcher = Pattern.compile(regex).matcher(orgStr);
		while (true) {
			if (matcher.find()) {
				String matcherStr = matcher.group();
				MatcherItem i = new MatcherItem();
				i.start = orgStr.indexOf(matcherStr);
				i.end = i.start + matcherStr.length() - (7);
				list.add(i);
				orgStr = orgStr.replace(matcherStr, matcherStr.subSequence(3, matcherStr.length() - 4));
				cm.setResultStr(orgStr);
				// System.out.println(orgStr);
				continue;
			} else {
				break;
			}
		}
		cm.setMis(list);
		return cm;
	}

	// <img src=upload/attach/61_1E2BC2B01E29FB41.png>
	public static CstmMatcher findImageMatherList(String orgStr) {
		CstmMatcher cm = new CstmMatcher();
		if (isEmpty(orgStr)) {
			return cm;
		}
		// ArrayList<MatcherItem> list = new ArrayList<MatcherItem>();
		String regex = "<img src=\\S+.[a-z|A-Z]+[\\s]*>";
		Matcher matcher = Pattern.compile(regex).matcher(orgStr);
		while (true) {
			if (matcher.find()) {
				MatcherItem mi = new MatcherItem();
				String src = matcher.group();
				mi.start = orgStr.indexOf(src);
				orgStr = orgStr.replace(src, "");
				Matcher m = Pattern.compile("=\\S+.[a-z|A-Z]+[\\s]*>").matcher(src);
				if (m.find()) {
					String path = m.group();
					// mi.name = path.substring(1,
					// path.length()-1).substring(path.lastIndexOf("/"));
					mi.name = path.substring(1, path.length() - 1).trim();
				}
				// list.add(mi);
				cm.getMis().add(mi);
				cm.setResultStr(orgStr);
				continue;
			} else {
				break;
			}
		}
		return cm;
	}

	public static/**ArrayList<MatcherItem>**/CstmMatcher findFontMatcherList(String orgStr) {
		CstmMatcher cm = new CstmMatcher();
		// ArrayList<MatcherItem> list = new ArrayList<MatcherItem>();
		/**
		 * 假如传过来的串为空,直接返回
		 */
		if (Util.isEmpty(orgStr)) {
			return cm;
		}
		// 请您回忆一下,您在<font color=blue>过去7天中</font>有过下列哪些上网行为?
		String regex = "<font color=[‘|’|\"]*[a-z|A-Z]+[‘|’|\"]*>\\s*\\S+\\s*</font>|<font color=[a-z|A-Z]+>\\s*\\S+\\s*</font>|<font color=[‘|’|\"]*[a-z|A-Z]+[‘|’|\"]*>\\s*\\S+\\s*<font>|<font color=[‘|’|\"]*[a-z|A-Z]+[‘|’|\"]*>\\s*\\S+\\s*"; // \\S+
		Matcher matcher = Pattern.compile(regex).matcher(orgStr);
		// System.out.println(orgStr);
		while (true) {
			if (matcher.find()) {
				MatcherItem mi = new MatcherItem();
				String matcherStr = matcher.group();
				// System.out.println("findFontMatcherList--->"+matcherStr);
				// "
				// 匹配font标签中的颜色值如,blue
				Matcher m = Pattern.compile("color=[‘|’|\"]*[a-z|A-Z]+[‘|’|\"]*>|color=[a-z|A-Z]+>").matcher(matcherStr);
				if (m.find()) {
					String mc = m.group();
					String color = mc.substring(6, mc.length() - 1).trim();
					// System.out.println(color);
					if (color.contains(MatcherItem.COLOR_BLUE)) {
						mi.color = Color.BLUE;
					} else if (color.contains(MatcherItem.COLOR_RED)) {
						mi.color = Color.RED;
					} else if (color.contains(MatcherItem.COLOR_GREEN)) {
						mi.color = Color.GREEN;
					}
				}
				// <font>匹配之间的文字</font>
				m = Pattern.compile(">\\s*\\S+\\s*+<").matcher(matcherStr);// \\S+
				if (m.find()) {
					String mct = m.group();
					String content = mct.substring(1, mct.length() - 1);
					mi.start = orgStr.indexOf(matcherStr);
					mi.end = mi.start + content.length();
					orgStr = orgStr.replace(matcherStr, content);
					// if(!isEmpty(content)){
					// list.add(mi);
					cm.getMis().add(mi);
					// }
				} else {
					// System.out.println("matcherStr="+matcherStr);
					/**
					 * font标签之间没有值,则将其替换为空字符
					 */
					orgStr = orgStr.replaceFirst("<font color=[‘|’|\"]*[a-z|A-Z]+[‘|’|\"]*>\\s*</font>|<font color=[a-z|A-Z]+>\\s*</font>", "");
				}
				// ma.currTitle = orgStr;
				// System.out.println("最终orgStr--->"+orgStr);
				cm.setResultStr(orgStr);
				continue;
			} else {
				break;
			}
		}
		// cm.setMis(list);
		return cm;
	}

	/**
	 * 按regex替换指定的字符串,并返回替换后的字符串位置 eg: 将 1.<b>中华人民共和国<b>是文化底蕴非常深厚的文化大国. 替换为
	 * 1.中华人民共和国是文化底蕴非常深厚的文化大国. 并返回“中华人民共和国”在“1.中华人民共和国是文化底蕴非常深厚的文化大国.”中的开始结束位置
	 * 
	 * @param regex
	 *            传入正则
	 * @param orgStr
	 *            传入指定的字符串
	 * @param prxRegex
	 *            正则前缀的长度 eg:<b>--->prxRegex=3
	 * @param tailRegex
	 *            正则后缀的长度 eg:</b>--->tailRegex=4
	 * @param ma
	 *            传入应用的上下文
	 * @return
	 */
	public static String replaceMatcherList(String orgStr) {
		String regex = "<BR>|<BR/>|<Br>|<Br/>|<bR>|<bR/>|<br>|<br/>";
		Matcher matcher = Pattern.compile(regex).matcher(orgStr);
		boolean mtched = false;
		while (true) {
			if (matcher.find()) {
				String matcherStr = matcher.group();
				orgStr = orgStr.replace(matcherStr, "\n");
				// = orgStr;
				// System.out.println("br--->"+orgStr);
				/**
				 * 至少匹配过一次
				 */
				mtched = true;
				continue;
			} else {
				// orgStr = null;
				break;
			}
		}
		if (mtched) {
			return orgStr;
		} else {
			return null;
		}
	}

	// /**
	// * 按照regex规则截取指定字符串中的字串
	// *
	// * @param orgStr
	// * @param regex
	// * @return
	// */
	// public static ArrayList<String> obtainList(String orgStr) {
	// ArrayList<String> list = new ArrayList<String>();
	// int start = orgStr.indexOf("@@");
	// int end = -1;
	// if (-1 != start) {
	// end = orgStr.indexOf("%%");
	// if (-1 != end && (2 < end - start)) {
	// list.add(orgStr.substring(start + 2, end));
	// }
	// } else {
	// end = orgStr.indexOf("%%");
	// if (-1 != end && 2 < end) {
	// list.add(orgStr.substring(0, end));
	// list.add(orgStr.substring(2, end));
	// }
	// }
	// if (-1 != end) {
	// String[] vs = orgStr.substring(end).split("%%");
	// for (String str : vs) {
	// if (!isEmpty(str)) {
	// list.add(str);
	// }
	// }
	// }
	// return list;
	// }

	/**
	 * 按照regex规则截取指定字符串中的字串
	 * 
	 * @param orgStr
	 * @param regex
	 * @return
	 */
	public static ArrayList<String> obtainList(String orgStr) {
		ArrayList<String> list = new ArrayList<String>();
		// 获取%%位置 更改
		int indexOf = orgStr.indexOf("%%");
		if(indexOf==-1){
			return list;	
		}
		
		// 获取@@位置
		int start = orgStr.indexOf("@@");
		// 初始结束位置
		int end = -1;
		// 假如找到@@
		if (-1 != start) {
			end = orgStr.indexOf("@@") + 2;
		}
		// 假如没找到@@
		else {
			end = 0;
		}
		
		if (-1 != end) {
			String[] vs = orgStr.substring(end).split("%%");
			for (String str : vs) {
				list.add(str);
			}
		}
		return list;
	}

	public static String getLeftCap(String str) {
		
		int index = str.indexOf("@@");
		if (0 < index) {
			// System.out.println("@@_start--->"+str.substring(0, index));
			return str.substring(0, index);
		} else if (0 == index) {
			return "";
		} else {
			index = str.indexOf("%%");
			if (1 < index) {
				// System.out.println("%%_start--->"+str.substring(0, index));
				return str.substring(0, index);
			}
		}
		return "";
	}

	/**
	 * 判断是否符合逻辑
	 */
	public static boolean isMatcher(MyApp ma, Question q, String uuid) {
		if (1 == q.qMatchQuestion) {
			return isAndMatcher(ma, q, uuid);
		} else {
			return isOrMatcher(ma, q, uuid);
		}
	}

	// 配额逻辑算法
	public static boolean isMatcher(MyApp ma, ArrayList<Restriction> resAll, String uuid, int qMatchQuestion) {
		if (1 == qMatchQuestion) {
			Question q=new Question();
				q.setResctItemArr(resAll);
				return isAndMatcher(ma, q, uuid);
			} else {
				Question q=new Question();
				q.setResctItemArr(resAll);
				return isOrMatcher(ma, q, uuid);
			}
		}

	
	/**
	 * 判断是否符合或逻辑
	 * @param ma
	 * @param q
	 * @param uuid
	 * @return
	 */
	public static boolean isOrMatcher(MyApp ma, Question q, String uuid) {
		System.out.println("isOrMatcher");
		/**
		 * Question节点的逻辑标志
		 */
		boolean _isQuestionMatcher = false;
		if (null == ma || null == q || Util.isEmpty(uuid)) {
			return _isQuestionMatcher;
		}
		/********************************************************************************/
		/**
		 * Restriction节点的逻辑标志
		 */
		boolean isRestMatcher = false;
		for (Restriction rstr : q.getResctItemArr()) {// for (Restriction rstr :
														// q.getResctItemArr())
			if (null == rstr || null == rstr.getQuestionId() || -1 == rstr.getQuestionId()) {
				continue;
			}
			Answer asw = ma.dbService.getAnswerByOrder(uuid, rstr.getQuestionId());
			if (null == asw || isEmpty(asw.getAnswerMapArr())) {
				isRestMatcher = false;
					continue;
			}

			if (Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType) {
				HashMap<Integer, RestrictionValue> tmpMap = new HashMap<Integer, RestrictionValue>();
				for (RestrictionValue rv : rstr.getRvs()) {
					RestrictionValue value = tmpMap.get(rv.getRows());
					if (null == value) {
						tmpMap.put(rv.getRows(), rv);
					} else {
						value.setValue(value.getValue() + "_" + rv.getValue());
						tmpMap.put(rv.getRows(), value);
					}
				}

				rstr.getRvs().clear();
				Iterator<Entry<Integer, RestrictionValue>> it = tmpMap.entrySet().iterator();
				while (it.hasNext()) {
					rstr.getRvs().add(it.next().getValue());
				}
				tmpMap.clear();
				tmpMap = null;
			}

			boolean _isValueMatcher = false;

			HashMap<String, AnswerMap> vHM = new HashMap<String, AnswerMap>();
			/**
			 * 将rstr.getQuestionId()题目的答案保存到vHM中便于比较
			 */
			for (AnswerMap am : asw.getAnswerMapArr()) {
				if (Cnt.TYPE_FREE_TEXT_BOX == asw.answerType || Cnt.TYPE_FREE_TEXT_AREA == asw.answerType) {

				} else if (isEmpty(am.getAnswerValue())) {
					continue;
				}
				// int type =
				// Integer.parseInt(am.getAnswerName().split("_")[1]);
				switch (asw.answerType) {
				case Cnt.TYPE_RADIO_BUTTON:
				case Cnt.TYPE_DROP_DOWN_LIST:
					vHM.put(am.getAnswerValue(), am);
					break;

				case Cnt.TYPE_CHECK_BOX:
				case Cnt.TYPE_FREE_TEXT_AREA:
				case Cnt.TYPE_FREE_TEXT_BOX:
					vHM.put(am.getAnswerName().split("_")[3], am);
					break;

				case Cnt.TYPE_MATRIX_RADIO_BUTTON:
					// vHM.put(am.getAnswerName().split("_")[3]+""+am.getCol(),
					// am);
					vHM.put(am.getAnswerName().split("_")[3], am);
					break;

				case Cnt.TYPE_MATRIX_CHECK_BOX:
					vHM.put(am.getRow() + "" + am.getCol(), am);
					break;
				}

			}

			/**
			 * (Question或, Restriction且)Restriction内部value之间的关系
			 */
			if ("all".equals(rstr.getMatch())) {
				isRestMatcher = true;
				_isValueMatcher = true;
				/**
				 * ------------内部所有的value节点都必须成立
				 */
				// for
				// (RestrictionValue
				// rv :
				// rstr.getRvs())
				for (int i = 0; i < rstr.getRvs().size(); i++) {
					RestrictionValue rv = rstr.getRvs().get(i);
					/**
					 * 非矩阵
					 */
					if (-1 == rv.getRows()) {
						// vHM为答案, 逻辑提供的值rv.getValue()
						AnswerMap am = vHM.get(rv.getValue());
						//非逻辑假如是复选
						if(Cnt.TYPE_CHECK_BOX==asw.answerType&&rv.isExclusive()){
							if(null!=am){
								_isValueMatcher = false;
								break;
							}else{
								_isValueMatcher = true;
								continue;
							}
						}else{
						//非逻辑假如是复选结束
							if (null != am) {
								/**
								 * 假如没有值限定
								 */
								if (Util.isEmpty(rv.getEqualText1()) && Util.isEmpty(rv.getEqualText2()) //
										&& Cnt.TYPE_FREE_TEXT_BOX != asw.answerType //
										&& Cnt.TYPE_FREE_TEXT_AREA != asw.answerType) {//
									_isValueMatcher = true;
									continue;
								} else {
									/**
									 * 只有EqualText1有值
									 */
									if (Util.isEmpty(rv.getEqualText2())) {
										/**
										 * 是数字
										 */
										if (isFormat(rv.getEqualText1(), -1)) {
											// int realValue, String matchEqual, int
											// equalText1, int equalText2, String
											// equal1, String equal2
											// System.out.println("rv.getEqualText1():"+rv.getEqualText1()+"---- rv.getEqual1():"+
											// rv.getEqual1()+"---am.getAnswerValue():"+am.getAnswerValue());
											if (isFormat(am.getAnswerValue(), -1)
													&& restValueValidate(Integer.parseInt(am.getAnswerValue()), null, Integer.parseInt(rv.getEqualText1()), 0, rv.getEqual1(), null)) {
												_isValueMatcher = true;
												continue;
											} else {
												_isValueMatcher = false;
												break;
											}
										} else {
											/**
											 * 非数字
											 */
											if (!Util.isEmpty(rv.getEqualText1()) && !Util.isEmpty(rv.getEqualText2())) {
												/**
												 * 有两个值
												 */
												if (restValueValidate(am.getAnswerValue(), rv.getMatchEqual(), rv.getEqualText1(), rv.getEqualText2(), rv.getEqual1(), rv.getEqual2())) {
													_isValueMatcher = true;
													continue;
												} else {
													_isValueMatcher = false;
													break;
												}

											} else {
												// System.out.println("rv.getEqualText1():"+rv.getEqualText1()+"---- rv.getEqual1():"+
												// rv.getEqual1()+"---am.getAnswerValue():"+am.getAnswerValue());

												/**
												 * 只有一个值
												 */
												if (restValueValidate(am.getAnswerValue(), null, rv.getEqualText1(), null, rv.getEqual1(), null)) {
													_isValueMatcher = true;
													continue;
												} else {
													_isValueMatcher = false;
													break;
												}
											}
										}
									} else {
										/**
										 * 两个都有值
										 */
										/**
										 * 两个值是数字
										 */
										if (isFormat(rv.getEqualText1(), -1) && isFormat(rv.getEqualText2(), -1)) {
											if (isFormat(am.getAnswerValue(), -1)
													&& restValueValidate(Integer.parseInt(am.getAnswerValue()), rv.getMatchEqual(), Integer.parseInt(rv.getEqualText1()),
															Integer.parseInt(rv.getEqualText2()), rv.getEqual1(), rv.getEqual2())) {
												_isValueMatcher = true;
												continue;
											} else {
												_isValueMatcher = false;
												break;
											}
										} else {
											/**
											 * 非数字
											 */
											if (restValueValidate(am.getAnswerValue(), rv.getMatchEqual(), rv.getEqualText1(), rv.getEqualText2(), rv.getEqual1(), rv.getEqual2())) {
												_isValueMatcher = true;
												continue;
											} else {
												_isValueMatcher = false;
												break;
											}
										}
									}
								}
							} else {
								/**
								 * 不存在答案
								 */
								_isValueMatcher = false;
								break;
							}
						}
						
					} else {
						/**
						 * 矩阵, 需要比较行号相等且值相等
						 */
						boolean is = false;
						AnswerMap am = vHM.get((Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType) ? ("" + rv.getRows()) : (rv.getRows() + rv.getValue()));
						if (null != am && (Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType)) {
							/**
							 * 矩阵题一行中引用了几个答案, 一行引用的是或的关系
							 */
							// 单选矩阵问题
							if (isContains(rv.getValue(), String.valueOf(am.getCol()))) {
								is = true;
							} else {
								is = false;
							}
						} else if (null != am && rv.getValue().equals(String.valueOf(am.getCol()))) {
							is = true;
						}

						if (is) {
							/**
							 * 矩阵匹配上了
							 */
							_isValueMatcher = true;
							continue;
						} else {
							/**
							 * 矩阵没有匹配上
							 */
							_isValueMatcher = false;
							break;
						}
					}
				}// for (RestrictionValue rv : rstr.getRvs())
					// 或 且
				if (!_isValueMatcher) {// (Restriction rstr :
										// q.getResctItemArr())循环
					// Restriction有一个节点没满足
					isRestMatcher = false;
				} else {
					isRestMatcher = true;
					break;
				}
			} else {
				/**
				 * (Question或, Restriction或)--------内部所有的value节点满足一个即可
				 */
				isRestMatcher = false;
				_isValueMatcher = false;
				for (RestrictionValue rv : rstr.getRvs()) {// for
															// (RestrictionValue
															// rv :
															// rstr.getRvs())
					/**
					 * 非矩阵
					 */
					if (-1 == rv.getRows()) {
						// vHM为答案, 逻辑提供的值rv.getValue()
						AnswerMap am = vHM.get(rv.getValue());
						//非逻辑假如是复选
						if(Cnt.TYPE_CHECK_BOX==asw.answerType&&rv.isExclusive()){
							if(null!=am){
								_isValueMatcher = false;
								continue;
							}else{
								_isValueMatcher = true;
								break;
							}
						}else{
							//非逻辑假如是复选结束
							if (null != am) {
								/**
								 * 假如没有值限定
								 */
								if (Util.isEmpty(rv.getEqualText1()) && Util.isEmpty(rv.getEqualText2())//
										&& Cnt.TYPE_FREE_TEXT_BOX != asw.answerType //
										&& Cnt.TYPE_FREE_TEXT_AREA != asw.answerType) {
									_isValueMatcher = true;
									break;
								} else {
									/**
									 * 只有EqualText1有值
									 */
									if (Util.isEmpty(rv.getEqualText2())) {
										/**
										 * 是数字
										 */
										if (isFormat(rv.getEqualText1(), -1)) {
											// int realValue, String matchEqual, int
											// equalText1, int equalText2, String
											// equal1, String equal2
											// System.out.println(Integer.parseInt(am.getAnswerValue())
											// + ":" +
											// Integer.parseInt(rv.getEqualText1()));
											if (isFormat(am.getAnswerValue(), -1)
													&& restValueValidate(Integer.parseInt(am.getAnswerValue()), null, Integer.parseInt(rv.getEqualText1()), 0, rv.getEqual1(), null)) {
												_isValueMatcher = true;
												break;
											} else {
												_isValueMatcher = false;
												continue;
											}
										} else {
											/**
											 * 非数字
											 */
											if (!Util.isEmpty(rv.getEqualText1()) && !Util.isEmpty(rv.getEqualText2())) {
												/**
												 * 有两个值
												 */
												if (restValueValidate(am.getAnswerValue(), rv.getMatchEqual(), rv.getEqualText1(), rv.getEqualText2(), rv.getEqual1(), rv.getEqual2())) {
													_isValueMatcher = true;
													break;
												} else {
													_isValueMatcher = false;
													continue;
												}

											} else {
												/**
												 * 只有一个值
												 */
												if (restValueValidate(am.getAnswerValue(), null, rv.getEqualText1(), null, rv.getEqual1(), null)) {
													_isValueMatcher = true;
													break;
												} else {
													_isValueMatcher = false;
													continue;
												}
											}
										}
									} else {
										/**
										 * 两个都有值
										 */
										/**
										 * 两个值是数字
										 */
										if (isFormat(rv.getEqualText1(), -1) && isFormat(rv.getEqualText2(), -1)) {
											if (isFormat(am.getAnswerValue(), -1)
													&& restValueValidate(Integer.parseInt(am.getAnswerValue()), rv.getMatchEqual(), Integer.parseInt(rv.getEqualText1()),
															Integer.parseInt(rv.getEqualText2()), rv.getEqual1(), rv.getEqual2())) {
												_isValueMatcher = true;
												break;
											} else {
												_isValueMatcher = false;
												continue;
											}
										} else {
											/**
											 * 非数字
											 */
											if (restValueValidate(am.getAnswerValue(), rv.getMatchEqual(), rv.getEqualText1(), rv.getEqualText2(), rv.getEqual1(), rv.getEqual2())) {
												_isValueMatcher = true;
												break;
											} else {
												_isValueMatcher = false;
												continue;
											}
										}
									}
								}
							} else {
								_isValueMatcher = false;
								continue;
							}
						}
					} else {
						/**
						 * 矩阵, 需要比较行号相等且值相等
						 */
						boolean is = false;
						AnswerMap am = vHM.get((Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType) ? ("" + rv.getRows()) : (rv.getRows() + rv.getValue()));
						if (null != am && (Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType)) {
							// 单选矩阵问题
							if (isContains(rv.getValue(), String.valueOf(am.getCol()))) {
								is = true;
							} else {
								is = false;
							}
						} else if (null != am && rv.getValue().equals(String.valueOf(am.getCol()))) {
							is = true;
						}
						if (is) {
							/**
							 * 矩阵匹配上了
							 */
							_isValueMatcher = true;
							break;
						} else {
							/**
							 * 矩阵没有匹配上
							 */
							_isValueMatcher = false;
							continue;
						}
					}
				}// for (RestrictionValue rv : rstr.getRvs())
					// 或 或
				if (_isValueMatcher) {
					/**
					 * Restriction有一个节点满足了
					 */
					isRestMatcher = true;
					break;
				}
			}
		}// for (Restriction rstr : q.getResctItemArr())
		_isQuestionMatcher = _isQuestionMatcher || isRestMatcher;
		return _isQuestionMatcher;
	}
	/**
	 * 判断是否符合且逻辑
	 * @param ma
	 * @param q
	 * @param uuid
	 * @return
	 */
	public static boolean isAndMatcher(MyApp ma, Question q, String uuid) {
		/**
		 * Question节点的逻辑标志
		 */
		boolean _isResultMatcher = true;
		if (null == ma || null == q || Util.isEmpty(uuid)) {
			return false;
		}
		/**
		 * 所有的Restriction之间都为且的关系
		 */
		/**
		 * Restriction节点的逻辑标志
		 */
		boolean isRestMatcher = true;
		//大题循环
		for (Restriction rstr : q.getResctItemArr()) {// for (Restriction rstr :
														// q.getResctItemArr())
			if (null == rstr || null == rstr.getQuestionId() || -1 == rstr.getQuestionId()) {
				continue;
			}
			Answer asw = ma.dbService.getAnswerByOrder(uuid, rstr.getQuestionId());
			/**
			 * 题目没有答案
			 */
			if (null == asw || isEmpty(asw.getAnswerMapArr())) {
				isRestMatcher = false;
				break;
				// System.out.println("第一个:"+isRestMatcher);
				// if ("all".equals(rstr.getMatch())) {
				// System.out.println("all");
				// /**
				// * 整体匹配失败
				// */
				// break;
				//
				// } else {
				// System.out.println("one");
				// /**
				// * 整体匹配失败
				// */
				// break;
				// }
			}
			if (Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType) {
				HashMap<Integer, RestrictionValue> tmpMap = new HashMap<Integer, RestrictionValue>();
				for (RestrictionValue rv : rstr.getRvs()) {
					RestrictionValue value = tmpMap.get(rv.getRows());
					if (null == value) {
						tmpMap.put(rv.getRows(), rv);
						// System.out.println("rv:"+rv.getRows()+"\t"+rv.toString());
					} else {
						value.setValue(value.getValue() + "_" + rv.getValue());
						tmpMap.put(rv.getRows(), value);
						// System.out.println("value:"+rv.getRows()+"\t"+value);
					}
				}

				rstr.getRvs().clear();
				Iterator<Entry<Integer, RestrictionValue>> it = tmpMap.entrySet().iterator();
				while (it.hasNext()) {
					rstr.getRvs().add(it.next().getValue());
				}
				tmpMap.clear();
				tmpMap = null;
			}
			/**
			 * 记录某一个Restriction当中单个的value节点是否成立
			 */
			boolean _isValueMatcher = false;

			HashMap<String, AnswerMap> vHM = new HashMap<String, AnswerMap>();
			/**
			 * 将rstr.getQuestionId()题目的答案保存到vHM中便于比较
			 */
			for (AnswerMap am : asw.getAnswerMapArr()) {
				if (Cnt.TYPE_FREE_TEXT_BOX == asw.answerType || Cnt.TYPE_FREE_TEXT_AREA == asw.answerType) {

				} else if (isEmpty(am.getAnswerValue())) {
					continue;
				}
				// int type =
				// Integer.parseInt(am.getAnswerName().split("_")[1]);
				switch (asw.answerType) {
				case Cnt.TYPE_RADIO_BUTTON:
				case Cnt.TYPE_DROP_DOWN_LIST:
					vHM.put(am.getAnswerValue(), am);
					break;
				//复选
				case Cnt.TYPE_CHECK_BOX:
				case Cnt.TYPE_FREE_TEXT_AREA:
				case Cnt.TYPE_FREE_TEXT_BOX:
					//item.itemValue
					vHM.put(am.getAnswerName().split("_")[3], am);
					break;

				case Cnt.TYPE_MATRIX_RADIO_BUTTON:
					// vHM.put(am.getAnswerName().split("_")[3]+""+am.getCol(),
					// am);
					// System.out.println("am.getAnswerName()"+am.getAnswerName().split("_")[3]+"\t"+am.toString());
					vHM.put(am.getAnswerName().split("_")[3], am);
					break;

				case Cnt.TYPE_MATRIX_CHECK_BOX:
					vHM.put(am.getRow() + "" + am.getCol(), am);
					break;
				}
			}
			/**
			 * (Question且, Restriction且)Restriction内部value之间的关系
			 */
			if ("all".equals(rstr.getMatch())) {
				isRestMatcher = true;
				_isValueMatcher = true;
				/**
				 * value且------------内部所有的value节点都必须成立
				 */
				//遍历所有的子逻辑
				for (RestrictionValue rv : rstr.getRvs()) {// for
															// (RestrictionValue
															// rv :
															// rstr.getRvs())
					/**
					 * 非矩阵
					 */
					if (-1 == rv.getRows()) {
						// vHM为答案, 逻辑提供的值rv.getValue()   非矩阵 引用答案的每个值
						AnswerMap am = vHM.get(rv.getValue());
						//非逻辑假如是复选
						if(Cnt.TYPE_CHECK_BOX==asw.answerType&&rv.isExclusive()){
							if(null!=am){
								_isValueMatcher = false;
								break;
							}else{
								_isValueMatcher = true;
								continue;
							}
						}else{
						//非逻辑假如是复选结束
							if (null != am) {
								/**
								 * 假如没有值限定
								 */
								if (Util.isEmpty(rv.getEqualText1()) && Util.isEmpty(rv.getEqualText2()) //
										&& Cnt.TYPE_FREE_TEXT_BOX != asw.answerType //
										&& Cnt.TYPE_FREE_TEXT_AREA != asw.answerType) {
									_isValueMatcher = true;
									continue;
								} else {
									/**
									 * 只有EqualText1有值
									 */
									if (Util.isEmpty(rv.getEqualText2())) {
										/**
										 * 是数字
										 */
										if (isFormat(rv.getEqualText1(), -1)) {
											// int realValue, String matchEqual, int
											// equalText1, int equalText2, String
											// equal1, String equal2
											if (isFormat(am.getAnswerValue(), -1)
													&& restValueValidate(Integer.parseInt(am.getAnswerValue()), null, Integer.parseInt(rv.getEqualText1()), 0, rv.getEqual1(), null)) {
												_isValueMatcher = true;
												continue;
											} else {
												_isValueMatcher = false;
												break;
											}
										} else {
											/**
											 * 非数字
											 */
											if (!Util.isEmpty(rv.getEqualText1()) && !Util.isEmpty(rv.getEqualText2())) {
												/**
												 * 有两个值
												 */
												if (restValueValidate(am.getAnswerValue(), rv.getMatchEqual(), rv.getEqualText1(), rv.getEqualText2(), rv.getEqual1(), rv.getEqual2())) {
													_isValueMatcher = true;
													continue;
												} else {
													_isValueMatcher = false;
													break;
												}

											} else {
												/**
												 * 只有一个值
												 */
												if (restValueValidate(am.getAnswerValue(), null, rv.getEqualText1(), null, rv.getEqual1(), null)) {
													_isValueMatcher = true;
													continue;
												} else {
													_isValueMatcher = false;
													break;
												}
											}
										}
									} else {
										/**
										 * 两个都有值
										 */
										/**
										 * 两个值是数字
										 */
										if (isFormat(rv.getEqualText1(), -1) && isFormat(rv.getEqualText2(), -1)) {
											if (isFormat(am.getAnswerValue(), -1)
													&& restValueValidate(Integer.parseInt(am.getAnswerValue()), rv.getMatchEqual(), Integer.parseInt(rv.getEqualText1()),
															Integer.parseInt(rv.getEqualText2()), rv.getEqual1(), rv.getEqual2())) {
												_isValueMatcher = true;
												continue;
											} else {
												_isValueMatcher = false;
												break;
											}
										} else {
											/**
											 * 非数字
											 */
											if (restValueValidate(am.getAnswerValue(), rv.getMatchEqual(), rv.getEqualText1(), rv.getEqualText2(), rv.getEqual1(), rv.getEqual2())) {
												_isValueMatcher = true;
												continue;
											} else {
												_isValueMatcher = false;
												break;
											}
										}
									}
								}
							} else {
								/**
								 * 没有该答案
								 */
								_isValueMatcher = false;
								break;
							}
						}
					} else {
						/**
						 * 矩阵, 需要比较行号相等且值相等
						 */
						boolean is = false;
						AnswerMap am = vHM.get((Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType) ? ("" + rv.getRows()) : (rv.getRows() + rv.getValue()));
						if (null != am && (Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType)) {
							// 单选矩阵问题
							if (isContains(rv.getValue(), String.valueOf(am.getCol()))) {
								is = true;
							} else {
								is = false;
							}
						} else if (null != am && rv.getValue().equals(String.valueOf(am.getCol()))) {
							is = true;
						}
						if (is) {
							/**
							 * 矩阵匹配上了
							 */
							_isValueMatcher = true;
							continue;
						} else {
							/**
							 * 矩阵没有匹配上
							 */
							_isValueMatcher = false;
							break;
						}
					}
				}// for (RestrictionValue rv : rstr.getRvs())
				isRestMatcher = isRestMatcher && _isValueMatcher;
				// 且 且
				if (!isRestMatcher) {// 跳出for (Restriction rstr :
										// q.getResctItemArr())循环
					/**
					 * Restriction有一个节点没满足
					 */
					break;
				}
			} else {
				/**
				 * (Question且, Restriction或)value或--------内部所有的value节点满足一个即可
				 */
				_isValueMatcher = false;
				isRestMatcher = false;
				for (RestrictionValue rv : rstr.getRvs()) {// for
															// (RestrictionValue
															// rv :
															// rstr.getRvs())
					/**
					 * 非矩阵
					 */
					if (-1 == rv.getRows()) {
						// vHM为答案, 逻辑提供的值rv.getValue()
						AnswerMap am = vHM.get(rv.getValue());
						//非逻辑假如是复选
						if(Cnt.TYPE_CHECK_BOX==asw.answerType&&rv.isExclusive()){
							if(null!=am){
								_isValueMatcher = false;
								continue;
							}else{
								_isValueMatcher = true;
								break;
							}
						}else{
						//非逻辑假如是复选结束
							if (null != am) {
								/**
								 * 假如没有值限定
								 */
								if (Util.isEmpty(rv.getEqualText1()) && Util.isEmpty(rv.getEqualText2())//
										&& Cnt.TYPE_FREE_TEXT_BOX != asw.answerType //
										&& Cnt.TYPE_FREE_TEXT_AREA != asw.answerType) {
									_isValueMatcher = true;
									break;
								} else {
									/**
									 * 只有EqualText1有值
									 */
									if (Util.isEmpty(rv.getEqualText2())) {
										/**
										 * 是数字
										 */
										if (isFormat(rv.getEqualText1(), -1)) {
											// int realValue, String matchEqual, int
											// equalText1, int equalText2, String
											// equal1, String equal2
											if (isFormat(am.getAnswerValue(), -1)
													&& restValueValidate(Integer.parseInt(am.getAnswerValue()), null, Integer.parseInt(rv.getEqualText1()), 0, rv.getEqual1(), null)) {
												_isValueMatcher = true;
												break;
											} else {
												_isValueMatcher = false;
												continue;
											}
										} else {
											/**
											 * 非数字
											 */
											if (!Util.isEmpty(rv.getEqualText1()) && !Util.isEmpty(rv.getEqualText2())) {
												/**
												 * 有两个值
												 */
												if (restValueValidate(am.getAnswerValue(), rv.getMatchEqual(), rv.getEqualText1(), rv.getEqualText2(), rv.getEqual1(), rv.getEqual2())) {
													_isValueMatcher = true;
													break;
												} else {
													_isValueMatcher = false;
													continue;
												}

											} else {
												/**
												 * 只有一个值
												 */
												if (restValueValidate(am.getAnswerValue(), null, rv.getEqualText1(), null, rv.getEqual1(), null)) {
													_isValueMatcher = true;
													break;
												} else {
													_isValueMatcher = false;
													continue;
												}
											}
										}
									} else {
										/**
										 * 两个都有值
										 */
										/**
										 * 两个值是数字
										 */
										if (isFormat(rv.getEqualText1(), -1) && isFormat(rv.getEqualText2(), -1)) {
											if (isFormat(am.getAnswerValue(), -1)
													&& restValueValidate(Integer.parseInt(am.getAnswerValue()), rv.getMatchEqual(), Integer.parseInt(rv.getEqualText1()),
															Integer.parseInt(rv.getEqualText2()), rv.getEqual1(), rv.getEqual2())) {
												_isValueMatcher = true;
												break;
											} else {
												_isValueMatcher = false;
												continue;
											}
										} else {
											/**
											 * 非数字
											 */
											if (restValueValidate(am.getAnswerValue(), rv.getMatchEqual(), rv.getEqualText1(), rv.getEqualText2(), rv.getEqual1(), rv.getEqual2())) {
												_isValueMatcher = true;
												break;
											} else {
												_isValueMatcher = false;
												continue;
											}
										}
									}
								}
							} else {
								_isValueMatcher = false;
								continue;
							}
						}
					} else {
						/**
						 * 矩阵, 需要比较行号相等且值相等
						 */
						boolean is = false;
						AnswerMap am = vHM.get((Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType) ? ("" + rv.getRows()) : (rv.getRows() + rv.getValue()));
						if (null != am && (Cnt.TYPE_MATRIX_RADIO_BUTTON == asw.answerType)) {
							// 单选矩阵问题
							if (isContains(rv.getValue(), String.valueOf(am.getCol()))) {
								is = true;
							} else {
								is = false;
							}
						} else if (null != am && rv.getValue().equals(String.valueOf(am.getCol()))) {
							is = true;
						}
						if (is) {
							/**
							 * 矩阵匹配上了
							 */

							_isValueMatcher = true;
							break;
						} else {
							/**
							 * 矩阵没有匹配上
							 */
							_isValueMatcher = false;
							continue;
						}
					}
				}// for (RestrictionValue rv : rstr.getRvs())
					// 且 或
				isRestMatcher = isRestMatcher || _isValueMatcher;
				if (!isRestMatcher) {
					/**
					 * 有一个Restriction不满足
					 */
					break;
				}
			}// else
		}// for (Restriction rstr : q.getResctItemArr())
		_isResultMatcher = _isResultMatcher && isRestMatcher;
		return _isResultMatcher;
	}

	/**
	 * 单选矩阵逻辑问题
	 * 
	 * @param a
	 *            字符串 a
	 * @param b
	 *            字符串 b
	 */
	private static boolean isContains(String a, String b) {
		String[] split = a.split("_");
		if (split.length > 0) {
			for (String str : split) {
				if (str.equals(b)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void showDateTimePicker(Context _context, final EditText et, final int dateType, int width, int height) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int secord = calendar.get(Calendar.SECOND);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		final Dialog dialog = new Dialog(_context, R.style.timePicker);
		// dialog.
		// dialog.getWindow().
		// dialog.setTitle(R.string.select_time);
		// 找到dialog的布局文件
		// dialog.
		// dialog.
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.time_layout, null);
		// 年
		final WheelView wv_year = (WheelView) view.findViewById(R.id.year);

		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel(_context.getResources().getString(R.string.year));// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		final WheelView wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel(_context.getResources().getString(R.string.month));
		wv_month.setCurrentItem(month);

		// 日
		final WheelView wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel(_context.getResources().getString(R.string.day));
		wv_day.setCurrentItem(day - 1);

		// 时
		final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setLabel(_context.getResources().getString(R.string.hour));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(hour);

		// 分
		final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_mins.setLabel(_context.getResources().getString(R.string.minute));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(minute);

		/**
		 * 秒
		 */
		final WheelView wv_secs = (WheelView) view.findViewById(R.id.secords);
		wv_secs.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_secs.setLabel(_context.getResources().getString(R.string.secord));
		wv_secs.setCyclic(true);
		wv_secs.setCurrentItem(secord);

		System.out.println("日期类型--->" + dateType);

		switch (dateType) {
		case 0:
		case 1:
			// 年月日
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
			wv_secs.setVisibility(View.GONE);
			break;

		case 2:
			// 年月日时
			wv_mins.setVisibility(View.GONE);
			wv_secs.setVisibility(View.GONE);
			break;

		case 3:
			// 年月日时分
			wv_secs.setVisibility(View.GONE);
			break;

		case 4:
			break;
		}

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year.getCurrentItem() + START_YEAR) % 100 != 0) || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;

		textSize = 18;
		// 设置 大小
		textSize = (height / 100) * 3;
		wv_day.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

		// 日期控件样式更改
		TextView leftTv = (TextView) view.findViewById(R.id.time_left_tv);
		TextView rightTv = (TextView) view.findViewById(R.id.time_right_tv);
		// Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
		// Button btn_cancel = (Button)
		// view.findViewById(R.id.btn_datetime_cancel);
		// // 确定
		rightTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				int year = wv_year.getCurrentItem() + START_YEAR;
				int month = wv_month.getCurrentItem() + 1;
				int day = wv_day.getCurrentItem() + 1;
				int hour = wv_hours.getCurrentItem();
				int minute = wv_mins.getCurrentItem();
				int sec = wv_secs.getCurrentItem();

				String sb = year + "-";//
				sb += month < 10 ? ("0" + month) : month;//
				sb += "-";//
				sb += day < 10 ? ("0" + day) : day;//

				switch (dateType) {
				case 0:
				case 1:
					break;

				case 2:
					// 年月日时
					sb += " ";//
					sb += hour < 10 ? ("0" + hour) : hour;
					break;

				case 3:
					// 年月日时分
					sb += " ";//
					sb += hour < 10 ? ("0" + hour) : hour;
					sb += ":";
					sb += minute < 10 ? ("0" + minute) : minute;
					break;

				case 4:
					sb += " ";//
					sb += hour < 10 ? ("0" + hour) : hour;
					sb += ":";
					sb += minute < 10 ? ("0" + minute) : minute;
					sb += ":";
					sb += sec < 10 ? ("0" + sec) : sec;
					break;
				}

				System.out.println("日期格式:" + sb.toString());
				et.setText(sb);

				dialog.dismiss();

			}
		});
		// 取消
		leftTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				et.setText("");
				dialog.dismiss();
			}
		});
		// 设置屏幕适配 日期样式修改
		LogUtil.printfLog("width:" + width + "height:" + height);
		float proPortion = height / width;
		// 注释掉宽高设置
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// dialog.getWindow().getAttributes().width=(int) (width/1.5);
		// dialog.getWindow().getAttributes().height=(int)
		// (proPortion*(width/1.5));
		dialog.show();
	}

	public static Integer getTableRowId(String uri) {
		String id = uri.substring(uri.lastIndexOf("/") + 1);
		if (!isEmpty(id)) {
			return Integer.parseInt(id);
		} else {
			return 0;
		}
	}

	public static boolean isHaveKey(HashMap<String, AnswerMap> map, String value, String rows) {
		boolean isHave = false;
		Iterator<Entry<String, AnswerMap>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, AnswerMap> entry = iter.next();
			String key = entry.getKey();
			AnswerMap v = entry.getValue();
			System.out.println(key + "--->" + value);
			if (value.equals(v)) {
				// && rows.equals(key.split("_")[3])
				System.out.println("isHaveKey--->值相等");
				if (3 < key.length()) {
					System.out.println("isHaveKey_Key--->长度大于3");
					String row = key.split("_")[3];
					System.out.println("isHaveKey_name中的row=" + row);
					if (rows.equals(row)) {
						isHave = true;
						break;
					}
				}
			}
		}
		return isHave;
	}

	/**
	 * 解压zip文件
	 * 
	 * @param srcFile
	 *            eg: xxx:/src/yyy.zip
	 * @param destPath
	 *            eg: yyy:/dist/
	 */
	public static boolean decompress(String srcFile, String destPath, String surveyId, Call call) {
		try {
			File outFile = new File(destPath);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ZipFile zipFile = new ZipFile(srcFile);
			Enumeration<?> en = zipFile.getEntries();
			ZipArchiveEntry zipEntry = null;
			int current = 0;
			while (en.hasMoreElements()) {
				zipEntry = (ZipArchiveEntry) en.nextElement();
				if (zipEntry.isDirectory()) {
					String dirName = zipEntry.getName();
					dirName = dirName.substring(0, dirName.length() - 1);
					File f = new File(outFile.getPath() + "/" + dirName);
					f.mkdirs();
				} else {
					String path = "";
					if (zipEntry.getName().toLowerCase().endsWith(".mp3")//
							|| zipEntry.getName().toLowerCase().endsWith(".avi")//
							|| zipEntry.getName().toLowerCase().endsWith(".wmv")//
							|| zipEntry.getName().toLowerCase().endsWith(".mp4")//
							|| zipEntry.getName().toLowerCase().endsWith(".flv")//
							|| zipEntry.getName().toLowerCase().endsWith(".3gp")//
							|| zipEntry.getName().toLowerCase().endsWith(".mov")) {
						path = getAppExtr() + File.separator + surveyId;
					} else {
						path = outFile.getPath();
					}
					File f = new File(path + "/" + zipEntry.getName());
					if (!f.getParentFile().exists()) {
						f.getParentFile().mkdirs();
					}
					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					OutputStream out = new FileOutputStream(f);
					// current += IOUtils.copy(in, out);
					byte[] buff = new byte[1024];
					int len = 0;
					while (-1 != (len = in.read(buff))) {
						out.write(buff, 0, len);
						current += len;
						if (null != call) {
							call.updateProgress(current, (int) outFile.length());
						}
					}
					out.flush();
					out.close();
					in.close();
				}
				if (null != call) {
					call.updateProgress(current, (int) outFile.length());
				}
			}
			zipFile.close();
			if (null != call) {
				call.updateProgress(100, 100);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getSurveyFileAbsolutePath(Context _c, String surveyId) {
		return _c.getFilesDir() + File.separator + "survey" + File.separator + surveyId + ".zip";
	}

	public static String getSurveySaveFilePath(Context _c) {
		return _c.getFilesDir() + File.separator + "survey";
	}

	public static String getSurveyFilePath(Context _c, String sruveyId) {
		return _c.getFilesDir() + File.separator + "survey" + File.separator + sruveyId;
	}

	public static String getSurveyUrl(Context _c, String sruveyId) {
		return _c.getFilesDir() + File.separator + "survey" + File.separator + sruveyId + File.separator + sruveyId + ".htm";
	}

	public static String getSurveyXML(Context _c, String sruveyId) {
		return _c.getFilesDir() + File.separator + "survey" + File.separator + sruveyId + File.separator + sruveyId + ".xml";
	}

	public static String getImagePath(Context _c, String sruveyId, String name) {
		return _c.getFilesDir() + File.separator + "survey" + File.separator + sruveyId + File.separator + name;
	}

	public static String getMediaPath(Context _c, String sruveyId, String name) {
		return _c.getFilesDir() + File.separator + "survey" + File.separator + sruveyId + File.separator + "upload" + File.separator + name;
	}

	public static String getMediaPath(String sruveyId, String name) {
		return getAppExtr() + File.separator + sruveyId + File.separator + "upload" + File.separator + name;
	}

	public static String getAppExtr() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CAPI_DATA_EXPORT";
	}

	// 知识库附件路径
	public static String getKnowPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DapSurvey" + File.separator + "KnowLedge";
	}

	public static String getSurveyIntervention(Context _c, String sruveyId) {
		return _c.getFilesDir() + File.separator + "survey" + File.separator + sruveyId + File.separator + "intervention.xml";
	}

	// 存放监控用的地址
	public static String getMonitorPath(Context _c, String surveyId, String uuid, String qIndex, int optType) {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "RemoteMonitor" + File.separator + surveyId + File.separator + optType + "_" + uuid + "_" + qIndex
				+ ".xml";
	}

	// 自定义logo功能 存储地址
	public static String getLogoPath(Context _c) {
		return _c.getFilesDir() + File.separator + "logo";
	}

	/**
	 * 判断按钮是否被连续频繁的点击
	 */
	private static long lC;

	public static boolean validateSyncClick() {
		long current = System.currentTimeMillis();
		if (300 > (current - lC)) {
			return true;
		}
		lC = current;
		return false;
	}

	/**
	 * 判断按钮是否被连续频繁的点击
	 */
	private static long llC;

	public static boolean validateSyncLongClick() {
		long current = System.currentTimeMillis();
		if (2000 > (current - llC)) {
			return true;
		}
		llC = current;
		return false;
	}

	public static String getLocalMacAddress(Context context) {
		String mac = null;
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (null == wifi) {
			return mac;
		}
		/**
		 * wifi是可用的
		 */
		boolean b = wifi.isWifiEnabled();
		if (!b) {
			/**
			 * 不可用的,则打开一会儿
			 */
			wifi.setWifiEnabled(true);
		}
		WifiInfo info = wifi.getConnectionInfo();
		if (null == info) {
			return mac;
		}
		mac = info.getMacAddress();
		if (!b) {
			/**
			 * 假如是不可用的,则关闭其状态,保持和以前的状态一致
			 */
			wifi.setWifiEnabled(false);
		}
		return mac;
	}

	/**
	 * PNG或MP3的存放路径
	 * 
	 * @param surveyId
	 * @return
	 */
	public static String getRecordPath(String surveyId) {
		return Environment.getExternalStorageDirectory()//
				.getAbsolutePath() + File.separator + "DapSurvey" + File.separator + "record" + surveyId;
	}

	public static String getRecordInnerPath(Context _c, String surveyId) {
		return _c.getFilesDir().getPath() + File.separator + "record" + File.separator + surveyId;
	}

	public static String getXmlPath(Context c, String surveyId) {
		return c.getFilesDir() + File.separator + "survey" + File.separator + surveyId;
	}

	/**
	 * 设置名字 ,命名规则
	 * 
	 * @param userId
	 * @param surveyId
	 * @param uuid
	 * @param pid
	 * @return
	 */
	public static String getXmlName(String userId, String surveyId, String uuid, String pid, String content) {
		StringBuilder sb = new StringBuilder();
		sb.append(surveyId)// 哪个项目
				.append("_").append(pid)// 哪个pid
				.append("_").append(userId);// 哪一个调查
		// 命名规则判断
		if (!Util.isEmpty(content)) {
			sb.append("_").append(content);
		} else {
			sb.append("_").append("0");
		}
		// 命名规则判断
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String nameOne = dateFormat.format(new Date());
		sb.append("_").append(nameOne);// 什么时间做的
		sb.append("_").append(uuid);// 对应的UUID
		sb.append(".xml");
		return sb.toString();

	}

	// public static String getXmlName(String userId, String surveyId, String
	// uuid,String pid) {
	// StringBuilder sb = new StringBuilder();
	// sb.append(userId)// 哪个人
	// .append("_").append(surveyId);// 哪一个调查
	// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	// String nameOne = dateFormat.format(new Date());
	// sb.append("_").append(nameOne);// 什么时间做的
	// sb.append("_").append(uuid);// 对应的UUID
	// sb.append(".xml");
	// return sb.toString();
	// }

	/**
	 * 命名规则
	 * 
	 * @param userId
	 * @param surveyId
	 * @param fileType
	 * @param uuid
	 * @param number
	 * @return
	 */
	public static String getRecordName(String userId, String surveyId, int fileType, String uuid, String number, String pid, String content, String qOrder) {
		StringBuilder sb = new StringBuilder();
		sb.append(surveyId)// 哪个项目
				.append("_").append(pid)// 哪个pid
				.append("_").append(userId);// 哪一个调查
		// 命名规则判断
		if (!Util.isEmpty(content)) {
			sb.append("_").append(content);
		} else {
			sb.append("_").append("0");
		}
		// 命名规则判断
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String nameOne = dateFormat.format(new Date());
		sb.append("_").append(nameOne);// 什么时间做的
		if (!isEmpty(number)) {
			sb.append("_").append(number);
		}
		sb.append("_").append(uuid);// 对应的UUID
		// 命名规则
		sb.append("=").append(qOrder);
		if (Cnt.FILE_TYPE_PNG == fileType) {
			sb.append(".png");
		} else {
			sb.append(".mp3");
		}
		return sb.toString();

	}

	// public static String getRecordName(String userId, String surveyId, int
	// fileType, String uuid, String number) {
	// StringBuilder sb = new StringBuilder();
	// sb.append(userId)// 哪个人
	// .append("_").append(surveyId);// 哪一个调查
	// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	// String nameOne = dateFormat.format(new Date());
	// sb.append("_").append(nameOne);// 什么时间做的
	// if (!isEmpty(number)) {
	// sb.append("_").append(number);
	// }
	// sb.append("_").append(uuid);// 对应的UUID
	// if (Cnt.FILE_TYPE_PNG == fileType) {
	// sb.append(".png");
	// } else {
	// sb.append(".mp3");
	// }
	// return sb.toString();
	// }
	/**
	 * true安装了 false没安装
	 * 
	 * @param _c
	 * @param pacakge
	 * @return
	 */
	public static boolean apkIsInstalled(Context _c, String pacakge) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = _c.getPackageManager().getPackageInfo(pacakge, 0);
		} catch (NameNotFoundException e) {
			// e.printStackTrace();
			packageInfo = null;
		}
		/**
		 * packageInfo=null表示没安装, 否则安装了
		 */
		return null != packageInfo;
	}

	public static Location getLocation(Context _c, LocationListener locationListener) {
		// 获取位置管理服务
		LocationManager locationManager;
		locationManager = (LocationManager) _c.getSystemService(Context.LOCATION_SERVICE);
		// 查找到服务信息
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_HIGH); // 低功耗
		String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
		Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
		locationManager.requestLocationUpdates(provider, 5000, 100, locationListener);
		return location;
	}

	public static boolean isGPSOpen(Context _c) {
		LocationManager alm = (LocationManager) _c.getSystemService(Context.LOCATION_SERVICE);
		return alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
	}
	public static boolean isPlayInstall(Context _c) {
		return apkIsInstalled(_c, "com.google.android.gms");
	}

	public static boolean isPlayDownloaded() {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), //
				"google_play.apk");
		return file.exists() && 0 < file.length();
	}

	public static boolean isStoreInstall(Context _c) {
		return apkIsInstalled(_c, "com.android.vending");
	}

	public static boolean isStoreDownloaded() {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), //
				"google_play_store.apk");
		return file.exists() && 0 < file.length();
	}

	public static void Log(String msg) {
		android.util.Log.i("kjy", msg);
	}

	// eg:<value EqualText2="10" Equal2="<=" MatchEqual="and" EqualText1="5"
	// Equal1=">=">0</value>
	/**
	 * @param realValue
	 *            指定题目的真实数字
	 * @param matchEqual
	 *            “EqualText2="10" Equal2="<="”与“EqualText1="5" Equal1=">="”的关系
	 * @param equalText1
	 * @param equalText2
	 * @param equal1
	 *            EqualText1的取值范围
	 * @param equal2
	 *            EqualText2的取值范围
	 * @return true符合, false不符合 eg中验证一道题的第一个选项值value为5<=value且value<=10符合条件
	 */
	public static boolean restValueValidate(int realValue, String matchEqual, int equalText1, int equalText2, String equal1, String equal2) {
		boolean validate = false;
		if (isEmpty(matchEqual)) {
			if ("==".equals(equal1)) {
				validate = (realValue == equalText1);
			} else if ("<=".equals(equal1)) {
				validate = (realValue <= equalText1);
			} else if (">=".equals(equal1)) {
				validate = (realValue >= equalText1);
			} else if ("<".equals(equal1)) {
				validate = (realValue < equalText1);
			} else if (">".equals(equal1)) {
				validate = (realValue > equalText1);
			}
		} else {
			if ("and".equals(matchEqual.trim().toLowerCase())) {
				if ("==".equals(equal1)) {
					validate = (realValue == equalText1);
				} else if ("<=".equals(equal1)) {
					validate = (realValue <= equalText1);
				} else if (">=".equals(equal1)) {
					validate = (realValue >= equalText1);
				} else if ("<".equals(equal1)) {
					validate = (realValue < equalText1);
				} else if (">".equals(equal1)) {
					validate = (realValue > equalText1);
				}

				if ("==".equals(equal2)) {
					validate = ((realValue == equalText2) && validate);
				} else if ("<=".equals(equal2)) {
					validate = ((realValue <= equalText2) && validate);
				} else if (">=".equals(equal2)) {
					validate = ((realValue >= equalText2) && validate);
				} else if ("<".equals(equal2)) {
					validate = ((realValue < equalText2) && validate);
				} else if (">".equals(equal2)) {
					validate = ((realValue > equalText2) && validate);
				}

			} else if ("or".equals(matchEqual.trim().toLowerCase())) {
				if ("==".equals(equal1)) {
					validate = (realValue == equalText1);
				} else if ("<=".equals(equal1)) {
					validate = (realValue <= equalText1);
				} else if (">=".equals(equal1)) {
					validate = (realValue >= equalText1);
				} else if ("<".equals(equal1)) {
					validate = (realValue < equalText1);
				} else if (">".equals(equal1)) {
					validate = (realValue > equalText1);
				}

				if ("==".equals(equal2)) {
					validate = ((realValue == equalText2) || validate);
				} else if ("<=".equals(equal2)) {
					validate = ((realValue <= equalText2) || validate);
				} else if (">=".equals(equal2)) {
					validate = ((realValue >= equalText2) || validate);
				} else if ("<".equals(equal2)) {
					validate = ((realValue < equalText2) || validate);
				} else if (">".equals(equal2)) {
					validate = ((realValue > equalText2) || validate);
				}
			}
		}
		return validate;
	}

	public static boolean restValueValidate(String realValue, String matchEqual, String equalText1, String equalText2, String equal1, String equal2) {
		/**
		 * 用户填写的值为空, 则意味着
		 */
		if (isEmpty(realValue)) {
			return true;
		}
		boolean validate = false;
		if (isEmpty(matchEqual)) {
			if ("==".equals(equal1)) {
				validate = isEmpty(realValue) ? false : realValue.equals(equalText1);
			} else if ("!=".equals(equal1)) {
				validate = isEmpty(realValue) ? false : !realValue.equals(equalText1);
			}
		} else {
			if ("and".equals(matchEqual.trim().toLowerCase())) {
				if ("==".equals(equal1)) {
					validate = isEmpty(realValue) ? false : realValue.equals(equalText1);
				} else if ("!=".equals(equal1)) {
					validate = isEmpty(realValue) ? false : !realValue.equals(equalText1);
				}

				if ("==".equals(equal2)) {
					validate = validate && (isEmpty(realValue) ? false : realValue.equals(equalText2));
				} else if ("!=".equals(equal2)) {
					validate = validate && (isEmpty(realValue) ? false : !realValue.equals(equalText2));
				}

			} else if ("or".equals(matchEqual.trim().toLowerCase())) {
				if ("==".equals(equal1)) {
					validate = isEmpty(realValue) ? false : realValue.equals(equalText1);
				} else if ("!=".equals(equal1)) {
					validate = isEmpty(realValue) ? false : !realValue.equals(equalText1);
				}

				if ("==".equals(equal2)) {
					validate = validate || (isEmpty(realValue) ? false : realValue.equals(equalText2));
				} else if ("!=".equals(equal2)) {
					validate = validate || (isEmpty(realValue) ? false : !realValue.equals(equalText2));
				}
			}
		}
		return validate;
	}

	/**
	 * 获取SDCARD上的存储空间 total为SDCARD的总大小,单位是M avail为SDCARD剩余可用的大小,单位为M
	 * 
	 * @return
	 */
	public static double[] readSDCard() {
		String state = Environment.getExternalStorageState();
		double total = 0;
		double avail = 0;
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long blockCount = sf.getBlockCount();
			long availCount = sf.getAvailableBlocks();
			total = blockSize * blockCount / 1024d / 1024d;
			avail = availCount * blockSize / 1024d / 1024d;
			// 地址变更 单位 改为 G
			total = total / 1024d;
			avail = avail / 1024d;
			BigDecimal b = new BigDecimal(total);
			total = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			b = new BigDecimal(avail);
			avail = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return new double[] { total, avail };
	}

	/**
	 * 获取系统内部存储空间 total为系统内部存储空间的总大小,单位是M avail为系统内部存储空间剩余可用的大小,单位为M
	 * 
	 * @return
	 */
	public static double[] readSystem() {
		double total = 0;
		double avail = 0;
		File root = Environment.getRootDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		long availCount = sf.getAvailableBlocks();
		total = blockSize * blockCount / 1024d / 1024d;
		avail = availCount * blockSize / 1024d / 1024d;
		return new double[] { total, avail };
	}

	/**
	 * 手工干预是否通过
	 * 
	 * @param ma
	 * @param q
	 * @param uuid
	 *            true表示符合, false表示不符合
	 * 
	 *            目前只针对单选矩阵
	 */
	public static boolean isIntervention(MyApp ma, Question q, String uuid) {
		Intervention it = q.getIntervention();
		if (null == it || isEmpty(it.getRule())) {
			return false;
		}
		String[] rules = it.getRule().split(",");
		/**
		 * 获取前面一个单选矩阵的答案
		 */
		Answer asw1 = ma.dbService.getAnswerByIndex(uuid, rules[0]);
		/**
		 * 获取后面一个单选矩阵的答案
		 */
		Answer asw2 = ma.dbService.getAnswerByIndex(uuid, rules[1]);
		/**
		 * 假如有一个答案
		 */
		if (null == asw1 || null == asw2) {
			return false;
		}

		// 找出指定行选的是哪一列
		int col1 = -100;
		int col2 = -100;
		for (AnswerMap am : asw1.getAnswerMapArr()) {
			int row = it.getIisMap().get(rules[0]).getRows();// 获得指定的规则row
			if (!isEmpty(am.getAnswerValue()) && am.getRow() == row) {
				col1 = am.getCol();
				break;
			}
		}

		for (AnswerMap am : asw2.getAnswerMapArr()) {
			int row = it.getIisMap().get(rules[1]).getRows();
			if (!isEmpty(am.getAnswerValue()) && am.getRow() == row) {
				col2 = am.getCol();
				break;
			}
		}

		if (-100 == col1 || -100 == col2) {
			return false;
		}

		// 两道题都有答案
		if (GREATER_THAN.equals(it.getSymbol())) {// 大于
			if (col1 > col2) {
				return true;
			} else {
				return false;
			}
		} else if (GREATER_EQUAL.equals(it.getSymbol())) {// 大于等于
			if (col1 >= col2) {
				return true;
			} else {
				return false;
			}
		} else if (LESS_THAN.equals(it.getSymbol())) {// 小于
			if (col1 < col2) {
				return true;
			} else {
				return false;
			}
		} else if (LESS_EQUAL.equals(it.getSymbol())) {// 小于等于
			if (col1 <= col2) {
				return true;
			} else {
				return false;
			}
		} else {// 不合法
			return false;
		}
	}

	/**
	 * 
	 * @param dateValue1
	 *            比较的上面题目时间字符串
	 * @param dateValue2
	 *            比较的当前题目时间字符串
	 * @param symbol
	 *            比较符号
	 * @return
	 * @throws ParseException
	 */
	public static boolean getDateCompare(String dateValue1, String dateValue2, String symbol) throws ParseException {
		boolean isSuccess = false;
		if (Util.isEmpty(dateValue1)) {
			return isSuccess;
		}

		if (dateValue1.length() != 19) {
			if (dateValue1.length() == 10) {
				dateValue1 = dateValue1 + " 00:00:00";
			} else if (dateValue1.length() == 13) {
				dateValue1 = dateValue1 + ":00:00";
			} else if (dateValue1.length() == 16) {
				dateValue1 = dateValue1 + ":00";
			}
		}
		if (dateValue2.length() != 19) {
			if (dateValue2.length() == 10) {
				dateValue2 = dateValue2 + " 00:00:00";
			} else if (dateValue2.length() == 13) {
				dateValue2 = dateValue2 + ":00:00";
			} else if (dateValue2.length() == 16) {
				dateValue2 = dateValue2 + ":00";
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = dateFormat.parse(dateValue1);
		Date date2 = dateFormat.parse(dateValue2);
		long l1 = date1.getTime();
		long l2 = date2.getTime();
		if ("=".equals(symbol)) {
			if (l2 == l1) {
				isSuccess = true;
			}
		} else if ("!=".equals(symbol)) {
			if (l2 != l1) {
				isSuccess = true;
			}
		} else if (">".equals(symbol)) {
			if (l2 > l1) {
				isSuccess = true;
			}
		} else if (">=".equals(symbol)) {
			if (l2 >= l1) {
				isSuccess = true;
			}
		} else if ("<".equals(symbol)) {
			if (l2 < l1) {
				isSuccess = true;
			}
		} else if ("<=".equals(symbol)) {
			if (l2 <= l1) {
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	/**
	 * 
	 * @param dateValue1
	 *            比较的上面题目时间字符串
	 * @param dateValue2
	 *            比较的当前题目时间字符串
	 * @param symbol
	 *            比较符号
	 * @return
	 * @throws ParseException
	 */
	public static boolean getDateCompareByGeTime(String dateValue1, String dateValue2, String symbol) throws ParseException {
		boolean isSuccess = false;
		if (Util.isEmpty(dateValue1)) {
			return isSuccess;
		}

		if (dateValue1.length() != 19) {
			if (dateValue1.length() == 10) {
				dateValue1 = dateValue1 + " 00:00:00";
			} else if (dateValue1.length() == 13) {
				dateValue1 = dateValue1 + ":00:00";
			} else if (dateValue1.length() == 16) {
				dateValue1 = dateValue1 + ":00";
			}
		}
		if (dateValue2.length() != 19) {
			if (dateValue2.length() == 10) {
				dateValue2 = dateValue2 + " 00:00:00";
			} else if (dateValue2.length() == 13) {
				dateValue2 = dateValue2 + ":00:00";
			} else if (dateValue2.length() == 16) {
				dateValue2 = dateValue2 + ":00";
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date1 = dateFormat.parse(dateValue1);
		Date date2 = dateFormat.parse(dateValue2);
		long l1 = date1.getTime();
		long l2 = date2.getTime();
		if ("=".equals(symbol)) {
			if (l2 == l1) {
				isSuccess = true;
			}
		} else if ("!=".equals(symbol)) {
			if (l2 != l1) {
				isSuccess = true;
			}
		} else if (">".equals(symbol)) {
			if (l2 > l1) {
				isSuccess = true;
			}
		} else if (">=".equals(symbol)) {
			if (l2 >= l1) {
				isSuccess = true;
			}
		} else if ("<".equals(symbol)) {
			if (l2 < l1) {
				isSuccess = true;
			}
		} else if ("<=".equals(symbol)) {
			if (l2 <= l1) {
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	/**
	 * 验证整形数字比较
	 * 
	 * @param preValue
	 *            原值
	 * @param currentValue
	 *            现值
	 * @param symbol
	 *            符号
	 * @return true 成功 false 不成功
	 * @throws ParseException
	 */
	public static boolean getDoubleNumberCompare(String preValue, String currentValue, String symbol) throws ParseException {
		boolean isSuccess = false;
		if (Util.isEmpty(preValue)) {
			return isSuccess;
		}
		double preNum = Double.parseDouble(preValue);
		double currentNum = Double.parseDouble(currentValue);
		if ("=".equals(symbol)) {
			if (currentNum == preNum) {
				isSuccess = true;
			}
		} else if ("!=".equals(symbol)) {
			if (currentNum != preNum) {
				isSuccess = true;
			}
		} else if (">".equals(symbol)) {
			if (currentNum > preNum) {
				isSuccess = true;
			}
		} else if (">=".equals(symbol)) {
			if (currentNum >= preNum) {
				isSuccess = true;
			}
		} else if ("<".equals(symbol)) {
			if (currentNum < preNum) {
				isSuccess = true;
			}
		} else if ("<=".equals(symbol)) {
			if (currentNum <= preNum) {
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	/**
	 * 获取单题拍照的名字
	 * 
	 * @param userId
	 * @param surveyId
	 * @param qCameraName
	 * @param fileType
	 * @return
	 */

	// dap1_38_20130829190619_002.png
	// public static String getSingleName(String userId, String surveyId, String
	// qCameraName, int fileType) {
	// StringBuilder sb = new StringBuilder();
	// sb.append(userId)// 哪个人
	// .append("_").append(surveyId);// 哪一个调查
	// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	// String nameOne = dateFormat.format(new Date());
	// sb.append("_").append(nameOne);// 什么时间做的
	// sb.append("_").append(qCameraName);// 对应的单题拍照名字
	// if (Cnt.FILE_TYPE_PNG == fileType) {// 类型
	// sb.append(".png");
	// } else {
	// sb.append(".mp3");
	// }
	// return sb.toString();
	// }

	/**
	 * 通过传进去的宽度值，和最大宽度值 算android的宽度
	 * 
	 * @param webWidth
	 * @param maxWidth
	 * @return
	 */
	public static int getEditWidth(int webWidth, int maxWidth) {
		int editWidth = 0;
		switch (webWidth) {
		case 5:
			editWidth = maxWidth / 12;
			break;
		case 10:
			editWidth = maxWidth / 6;
			break;
		case 15:
			editWidth = maxWidth / 4;
			break;
		case 20:
			editWidth = maxWidth / 3;
			break;
		case 25:
			editWidth = (int) (maxWidth / 2.4);
			break;
		case 30:
			editWidth = maxWidth / 2;
			break;
		case 35:
			editWidth = (int) (maxWidth / 1.7);
			break;
		case 40:
			editWidth = (int) (maxWidth / 1.5);
			break;
		case 45:
			editWidth = (int) (maxWidth / 1.3);
			break;
		case 50:
			editWidth = (int) (maxWidth / 1.2);
			break;
		case 55:
			editWidth = (int) (maxWidth / 1.1);
			break;
		case 60:
			editWidth = maxWidth / 1;
			break;
		}
		return editWidth;
	}

	/**
	 * 获得版本号
	 */
	public static String getVersion(Application app) {
		String mVersion = "";
		try {
			PackageManager pm = app.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(app.getPackageName(), PackageManager.GET_ACTIVITIES);
			mVersion = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			mVersion = "";
		}
		return mVersion;
	}

	/**
	 * 
	 * @param diffList
	 *            小组之间的空隙
	 * @param qDiffList
	 *            大组之间的空隙
	 * @param orderMap
	 *            每个大组中的所有小组
	 * @param groupSize
	 *            大组个数
	 * @return 返回最终的排序
	 */
	public static ArrayList<String> getAllGroupOrder(ArrayList<String> diffList, ArrayList<String> qDiffList, HashMap<Integer, ArrayList<String>> orderMap, int groupSize) {
		ArrayList<String> myList = new ArrayList<String>();
		// 遍历大题组
		for (int i = 0; i < groupSize; i++) {
			// 大体组中最大值变量
			int bigMaxValue = 0;
			// 通过大体组唯一order值找到对应大体组的每一个小题组
			ArrayList<String> orderList = orderMap.get(i);
			// 大组里的遍历每一个小题组集合
			for (int j = 0; j < orderList.size(); j++) {
				// 小题组中最大值
				int maxValue = 0;
				// 每一个小题组中的数组集合
				String[] orderArr = orderList.get(j).split(",");
				// 分解成一小组中的每一个数字
				for (int k = 0; k < orderArr.length; k++) {
					int myValue = Integer.parseInt(orderArr[k]);
					// 假如这个值大于最大值。最大值改变
					if (myValue > maxValue) {
						maxValue = myValue;
					}
					if (myValue > bigMaxValue) {
						bigMaxValue = myValue;
					}
					// 假如集合
					myList.add(myValue + "");
				}
				// 小题组中最大值的下一个值
				String nextValue = (maxValue + 1) + "";
				// 遍历小题组间距
				for (int x = 0; x < diffList.size(); x++) {
					String myDiffStr = diffList.get(x);
					// 如果小题组间距包含最大值下一个值
					String[] diffArr = myDiffStr.split(",");
					if (diffArr[0].equals(nextValue)) {
						for (int z = 0; z < diffArr.length; z++) {
							// 就把这个间距的所有值加上
							myList.add(diffArr[z]);
						}
					}
				}
			}
			// 最大大题组中的最大值
			String nextBigValue = (bigMaxValue + 1) + "";
			// 遍历大题组间距
			for (int big = 0; big < qDiffList.size(); big++) {
				String myBigDiff = qDiffList.get(big);
				// 假如大题组间距包含下一个值
				String[] bigDiffArr = myBigDiff.split(",");
				if (bigDiffArr[0].equals(nextBigValue)) {
					for (int bigNum = 0; bigNum < bigDiffArr.length; bigNum++) {
						// 就把这个间距加上
						myList.add(bigDiffArr[bigNum]);
					}
				}

			}
		}
		// 返回集合
		return myList;
	}

	/**
	 * 回调函数
	 * 
	 * @param feed
	 * @param q
	 *            上次调用的问题
	 * @param ma
	 * @return
	 */
	public static ArrayList<Integer> getSiteOption(UploadFeed feed, Question q, MyApp ma, ArrayList<Integer> have) {
		// 假如是排斥
		// 根据问卷号和问题的顺序号获取指定的问题
		Question backQuestion = ma.dbService.getQuestion(feed.getSurveyId(), q.qSiteOption);
		if ("1".equals(backQuestion.qInclusion)) {
			Answer an = ma.dbService.getAnswer(feed.getUuid(), backQuestion.qSiteOption);
			if (null != an) {
				for (AnswerMap am : an.getAnswerMapArr()) {
					// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
					if (!Util.isEmpty(am.getAnswerValue())) {
						if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
							have.add(am.getRow());
						} else {
							have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
						}
					}
				}
				return getSiteOption(feed, backQuestion, ma, have);
			} else {
				return have;
			}
		} 
		//没选的加进去加到have里面
		else if("0".equals(backQuestion.qInclusion)){
			Answer an = ma.dbService.getAnswer(feed.getUuid(), backQuestion.qSiteOption);
			if(an!=null){
				ArrayList<Integer> all=new ArrayList<Integer>();
				ArrayList<Integer> tempHave=new ArrayList<Integer>();
				for (int i = 0; i < q.getRowItemArr().size(); i++) {
					QuestionItem qi = q.getRowItemArr().get(i);
					all.add(qi.itemValue);
				}
				
				for (AnswerMap am : an.getAnswerMapArr()) {
					// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
					if (!Util.isEmpty(am.getAnswerValue())) {
						if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
							tempHave.add(am.getRow());
						} else {
							tempHave.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
						}
					}
				}
				all.removeAll(tempHave);
				have.addAll(all);
				return have;
			}else{
				return have;
			}
		}else{
			return have;
		}
	}

	// 排序法
	public void sort(int[] targetArr) {
		// 大到小的排序
		int temp = 0;
		for (int i = 0; i < targetArr.length; i++) {
			for (int j = i; j < targetArr.length; j++) {
				if (targetArr[i] < targetArr[j]) {
					// 方法一：
					temp = targetArr[i];
					targetArr[i] = targetArr[j];
					targetArr[j] = temp;
				}
			}
		}
	}

	/**
	 * 排序法
	 * 
	 * @param ss
	 * @return 通过ss冒泡进行排序
	 */
	public static ArrayList<Survey> sort(ArrayList<Survey> ss) {
		// 大到小的排序
		for (int i = 1; i < ss.size(); i++) {
			// 每一个值都与他后面的值相比，因为在他前面的都是比他大的，第一个值前面没有值就暂时认为他是最大的
			for (int j = 0; j < ss.size() - i; j++) {
				Survey sj1 = ss.get(j);
				// 如果当前的值比它后面的值大就进行两个值的互换
				Survey sj2 = ss.get(j + 1);
				if ((Integer.parseInt(sj1.surveyId)) < (Integer.parseInt(sj2.surveyId))) {
					// 把当前值放入临时变量中
					ss.remove(j);
					ss.add(j, sj2);
					ss.remove(j + 1);
					ss.add(j + 1, sj1);
				}
			}
		}
		return ss;
	}

	/**
	 * 创建手写签名文件 单题签名
	 * 
	 * @return
	 */
	public static File createFile(Bitmap mSignBitmap, String path, String name) {
		ByteArrayOutputStream baos = null;
		File file = new File(path, name);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			// String sign_dir = Environment.getExternalStorageDirectory() +
			// File.separator;
			// _path = sign_dir + System.currentTimeMillis() + ".jpg";
			baos = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(file).write(photoBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 单题签名名字 命名规则
	 * 
	 * @param userId
	 * @param surveyId
	 * @param fileType
	 * @param uuid
	 * @param number
	 * @return
	 */
	public static String getSignName(String userId, String surveyId, int fileType, String uuid, String number, String pid, String content, String qOrder) {
		StringBuilder sb = new StringBuilder();
		sb.append(surveyId)// 哪个项目
				.append("_").append(pid)// 哪个pid
				.append("_").append(userId);// 哪一个调查
		// 命名规则判断
		if (!Util.isEmpty(content)) {
			sb.append("_").append(content);
		} else {
			sb.append("_").append("0");
		}
		// 命名规则判断
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String nameOne = dateFormat.format(new Date());
		sb.append("_").append(nameOne);// 什么时间做的
		if (!isEmpty(number)) {
			sb.append("_").append(number);
		}
		sb.append("_").append(uuid);// 对应的UUID
		// 命名规则
		sb.append("=").append(qOrder);
		if (Cnt.FILE_TYPE_PNG == fileType) {
			sb.append(".jpg");
		} else {
			sb.append(".mp3");
		}
		return sb.toString();
	}

	// public static String getSignName(String userId, String surveyId, int
	// fileType, String uuid, String number,String pid) {
	// StringBuilder sb = new StringBuilder();
	// sb.append(userId)// 哪个人
	// .append("_").append(surveyId);// 哪一个调查
	// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	// String nameOne = dateFormat.format(new Date());
	// sb.append("_").append(nameOne);// 什么时间做的
	// if (!isEmpty(number)) {
	// sb.append("_").append(number);
	// }
	// sb.append("_").append(uuid);// 对应的UUID
	// if (Cnt.FILE_TYPE_PNG == fileType) {
	// sb.append(".jpg");
	// } else {
	// sb.append(".mp3");
	// }
	// return sb.toString();
	// }

	/**
	 * 问卷提醒 命名规则
	 * 
	 * @param sTime
	 *            传入的时间
	 * @param fmt
	 *            true完整的 false年月日格式
	 * @return 年月日 时分秒 格式的字符串时间 或者是年月日字符串时间
	 */
	public static long getLongTime(String sTime, int type) {
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
		case 5:
			// 命名规则
			p = "yyyyMMddHHmmss";
			break;
		}
		try {
			DateFormat df = new SimpleDateFormat(p);
			Date d = df.parse(sTime);
			return d.getTime();
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * 哑题新加判断是否选项相同
	 * 
	 * @param strs
	 * @param s
	 * @return
	 */
	public static boolean isHave(String[] strs, String s) {
		/* 此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串 * */
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equals(s)) {
				// 循环查找字符串数组中的每个字符串中是否包含所有查找的内容
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;
	}

	/**
	 * 看看是否引用受访者参数
	 * 
	 * @param regex
	 * @param orgStr
	 * @param ma
	 * @param surveyId
	 * @return
	 */
	public static CstmMatcher findMatcherPropertyItemList(String orgStr, ArrayList<Parameter> parameters) {
		CstmMatcher cm = new CstmMatcher();
		ArrayList<MatcherItem> mis = new ArrayList<MatcherItem>();
		Matcher matcher = Pattern.compile("::@@[a-zA-Z]+::@@").matcher(orgStr);
		// System.out.println("orgStr:"+orgStr);
		while (true) {
			if (matcher.find()) {
				String matcherStr = matcher.group();
				MatcherItem mi = new MatcherItem();
				mi.start = orgStr.indexOf(matcherStr);
				Matcher m = Pattern.compile("[a-zA-Z]+").matcher(matcherStr);
				// System.out.println("find前");
				if (m.find()) {
					// System.out.println("find后");
					String quto = "";
					String strGroup = m.group();
					// System.out.println("strGroup:"+strGroup);
					String myReplace = "";
					if (null != parameters && parameters.size() > 0) {
						for (Parameter parameter : parameters) {
							if (parameter.getSid().equals(strGroup)) {
								// 更改的样式
								myReplace = "<font color='blue'>" + parameter.getContent() + "</font>";
								break;
							}
						}
					} else {
						myReplace = "";
					}
					quto = quto + myReplace;

					mi.end = mi.start + quto.length();
					orgStr = orgStr.replace(matcherStr, quto);
					if (!isEmpty(quto)) {
						mis.add(mi);
					} else {
						MatcherItem mi2 = new MatcherItem();
						mi2.start = -1;
						mi2.end = -1;
						mis.add(mi2);
					}
					cm.setResultStr(orgStr);
				}
				continue;
			} else {
				break;
			}
		}
		cm.setMis(mis);
		return cm;
	}

	/**
	 * 地图监控用的流转string 的方法
	 * 
	 * @param is
	 * @return
	 */
	public static String Stream2String(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 16 * 1024); // 强制缓存大小为16KB，一般Java类默认为8KB
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) { // 处理换行符
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	/**
	 * 解析state串
	 * 
	 * @param is
	 * @param feed
	 * @return
	 */
	public static String resolvData(InputStream is) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String state = "0";
		// System.out.println("resolvData");
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(is);
			Element element = document.getDocumentElement();
			state = element.getElementsByTagName("state").item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
			Log.e("DapDesk", "Message:" + e.getMessage());
		}
		return state;
	}

	/**
	 * 命名规则判断正则是否为中文的
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {

		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	// 知识库各种打开方式开始
	public static Intent openFile(String filePath) {

		File file = new File(filePath);
		if (!file.exists())
			return null;
		/* 取得扩展名 */
		String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			return getAudioFileIntent(filePath);
		} else if (end.equals("3gp") || end.equals("mp4")) {
			return getAudioFileIntent(filePath);
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
			return getImageFileIntent(filePath);
		} else if (end.equals("apk")) {
			return getApkFileIntent(filePath);
		} else if (end.equals("ppt")) {
			return getPptFileIntent(filePath);
		} else if (end.equals("xls")) {
			return getExcelFileIntent(filePath);
		} else if (end.equals("doc")) {
			return getWordFileIntent(filePath);
		} else if (end.equals("pdf")) {
			return getPdfFileIntent(filePath);
		} else if (end.equals("chm")) {
			return getChmFileIntent(filePath);
		} else if (end.equals("txt")) {
			return getTextFileIntent(filePath, false);
		} else {
			return getAllIntent(filePath);
		}
	}

	// Android获取一个用于打开APK文件的intent
	public static Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	// Android获取一个用于打开APK文件的intent
	public static Intent getApkFileIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// Android获取一个用于打开VIDEO文件的intent
	public static Intent getVideoFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// Android获取一个用于打开AUDIO文件的intent
	public static Intent getAudioFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// Android获取一个用于打开Html文件的intent
	public static Intent getHtmlFileIntent(String param) {

		Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// Android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// Android获取一个用于打开PPT文件的intent
	public static Intent getPptFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// Android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// Android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// Android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// Android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(String param, boolean paramBoolean) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// Android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

	// 知识库各种打开方式结束
	// 大树 全角半角 转换

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	// 替换、过滤特殊字符
	public static String StringFilter(String str) throws PatternSyntaxException {
		str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	// 大树 打开 本地本件的 通用方法

	public static void openFile(File file, Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType(file);
		// 设置intent的data和Type属性。
		intent.setDataAndType(Uri.fromFile(file), type);
		// 跳转
		context.startActivity(intent);
	}

	// 大树 获取文件类型

	private static String getMIMEType(File file) {

		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			// MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	// 大树 后缀名大全

	private static final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" }, { ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" }, { ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" }, { ".c", "text/plain" }, { ".class", "application/octet-stream" }, { ".conf", "text/plain" }, { ".cpp", "text/plain" }, { ".doc", "application/msword" },
			{ ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" }, { ".xls", "application/vnd.ms-excel" },
			{ ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" }, { ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" }, { ".jar", "application/java-archive" }, { ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" }, { ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" }, { ".log", "text/plain" }, { ".m3u", "audio/x-mpegurl" }, { ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" }, { ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" }, { ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" }, { ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" }, { ".mpga", "audio/mpeg" }, { ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" }, { ".pdf", "application/pdf" }, { ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" }, { ".ppt", "application/vnd.ms-powerpoint" }, { ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" }, { ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" }, { ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" }, { ".z", "application/x-compress" }, { ".zip", "application/x-zip-compressed" }, { "", "*/*" } };

	/**
	 * 新哑题 受访者是否匹配的上
	 * 
	 * @param content
	 *            这个受访者的值
	 * @param intSymbol
	 *            符号 1不符合 2符合 = !=
	 * @param compareContent
	 *            要比较的值
	 * @return
	 */
	public static boolean isRespondentsMatching(String content, String intSymbol, String compareContent) {
		boolean isOk = false;
		if (Util.isEmpty(content) || Util.isEmpty(intSymbol) || Util.isEmpty(compareContent)) {
			return isOk;
		}
		if ("1".equals(intSymbol)) {
			if (content.indexOf(compareContent) == -1) {
				isOk = true;
			} else {

			}
		} else if ("2".equals(intSymbol)) {
			if (content.indexOf(compareContent) != -1) {
				isOk = true;
			} else {

			}
		} else if ("=".equals(intSymbol)) {
			if (content.equals(compareContent)) {
				isOk = true;
			} else {

			}
		} else if ("!=".equals(intSymbol)) {
			if (!content.equals(compareContent)) {
				isOk = true;
			} else {

			}
		}
		return isOk;
	}

	/**
	 * 新哑题 单行文本题目是否能匹配上
	 * 
	 * @param answerText
	 *            原值
	 * @param symbol
	 *            符号和类型 >:1 0:// 普通类型 1:// 日期格式 2013-08-08 16:12:49 2:// 数字格式
	 *            3:// 英文/数字格式5:// 邮件 4.字典 6.维码
	 * @param compareContent
	 *            内容
	 * @return
	 */
	public static boolean isPass(String answerText, String symbol, String compareContent) {
		boolean isOk = false;
		if (Util.isEmpty(answerText) || Util.isEmpty(symbol) || Util.isEmpty(compareContent)) {
			return isOk;
		}
		String[] symbolArr = symbol.split(":");
		if (symbolArr.length != 2) {
			return isOk;
		}
		String strSymbol = symbolArr[0];// 符号
		int type = Integer.parseInt(symbolArr[1]);// 文本类型
		switch (type) {
		// 普通类型 只有= !=符号
		case 0:
		case 3:
		case 4:
		case 5:
		case 6:
			if ("=".equals(strSymbol)) {
				if (answerText.equals(compareContent)) {
					isOk = true;
				}
			} else if ("!=".equals(strSymbol)) {
				if (!answerText.equals(compareContent)) {
					isOk = true;
				}
			}
			// 1不包含 2包含
			else if ("1".equals(strSymbol)) {
				if (answerText.indexOf(compareContent) == -1) {
					isOk = true;
				}
			} else if ("2".equals(strSymbol)) {
				if (answerText.indexOf(compareContent) != -1) {
					isOk = true;
				}
			}
			break;
		// 日期格式
		case 1:
			try {
				boolean dateCompare = Util.getDateCompare(compareContent, answerText, strSymbol);
				if (dateCompare) {
					isOk = true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		// 数字格式
		case 2:
			double intNum = Double.parseDouble(answerText);
			double intCompare = Double.parseDouble(compareContent);
			if ("=".equals(strSymbol)) {
				if (intNum == intCompare) {
					isOk = true;
				}
			} else if ("!=".equals(strSymbol)) {
				if (intNum != intCompare) {
					isOk = true;
				}
			} else if ("<".equals(strSymbol)) {
				if (intNum < intCompare) {
					isOk = true;
				}
			} else if ("<=".equals(strSymbol)) {
				if (intNum <= intCompare) {
					isOk = true;
				}
			} else if (">".equals(strSymbol)) {
				if (intNum > intCompare) {
					isOk = true;
				}
			} else if (">=".equals(strSymbol)) {
				if (intNum >= intCompare) {
					isOk = true;
				}
			} else {

			}
			break;
		default:
			break;
		}
		return isOk;
	}

	/**
	 * 新哑题
	 * 
	 * @param answerText
	 *            原值
	 * @param strSymbol
	 *            符号
	 * @param compareContent
	 *            比较符
	 * @return
	 */
	public static boolean isMath(String answerText, String strSymbol, String compareContent) {
		boolean isOk = false;
		if (Util.isEmpty(answerText) || Util.isEmpty(strSymbol) || Util.isEmpty(compareContent)) {
			return isOk;
		}
		double intNum = Double.parseDouble(answerText);
		double intCompare = Double.parseDouble(compareContent);
		if ("=".equals(strSymbol)) {
			if (intNum == intCompare) {
				isOk = true;
			}
		} else if ("!=".equals(strSymbol)) {
			if (intNum != intCompare) {
				isOk = true;
			}
		} else if ("<".equals(strSymbol)) {
			if (intNum < intCompare) {
				isOk = true;
			}
		} else if ("<=".equals(strSymbol)) {
			if (intNum <= intCompare) {
				isOk = true;
			}
		} else if (">".equals(strSymbol)) {
			if (intNum > intCompare) {
				isOk = true;
			}
		} else if (">=".equals(strSymbol)) {
			if (intNum >= intCompare) {
				isOk = true;
			}
		}
		return isOk;
	}

	/**
	 * 日期控件样式更改
	 */
	public static void changeDateStyle(Context content, final EditText et, int type) {

		Calendar c = Calendar.getInstance();
		DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(content, 0, new DoubleDatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
				String textString = String.format("%d-%d-%d", startYear, startMonthOfYear + 1, startDayOfMonth);
				et.setText(textString);
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
		dialog.show();
	}

}
