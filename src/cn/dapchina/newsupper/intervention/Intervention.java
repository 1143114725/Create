package cn.dapchina.newsupper.intervention;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.AnswerMap;
import cn.dapchina.newsupper.bean.QuestionItem;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.BaseLog;
import cn.dapchina.newsupper.util.GsonUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.xhttp.Xutils;
import cn.dapchina.newsupper.xhttp.Xutils.XCallBack2;

/**
 * 干预
 * 
 * @author Administrator
 * 
 */
public class Intervention {
	private int surveyId;
	private MyApp ma;
	private String uuid;

	private String INTERVENTION_S23 = "147";

	public Intervention(int surveyId, MyApp ma, String uuid) {
		super();
		this.surveyId = surveyId;
		this.ma = ma;
		this.uuid = uuid;
	}

	/**
	 * 获取答案
	 * 
	 * @param index
	 * @return
	 */
	public Answer geianswer(String index) {
		Answer p4aans = ma.dbService.getAnswer(uuid, index);
		if (p4aans != null && p4aans.getAnswerMapArr() != null) {
			return p4aans;
		}
		return null;

	}

	/**
	 * 请求s11的答案
	 */
	public ArrayList<String> sendS11(Answer ans, String json) {

		collist.clear();
		biglist.clear();
		smalllist.clear();
		reslist.clear();

		// 如果S10 有答案
		// if (ans10 != null) {
		// // 循环判断有没有 烹饪用具，妇女用品
		// for (int i = 0; i < ans10.getAnswerMapArr().size(); i++) {
		// BaseLog.v("s10的答案："
		// + ans10.getAnswerMapArr().get(i).getAnswerText()
		// + "--编号："
		// + ans10.getAnswerMapArr().get(i).getAnswerValue());
		// // 如果有加入collist 对应的value
		// // S11 烹饪用具 = 14，妇女用品 = 21
		// // S10 烹饪用具 = 17，妇女用品 = 25
		// if (ans10.getAnswerMapArr().get(i).getAnswerValue()
		// .equals("17")) {
		// // 烹饪用具
		// collist.add(14 + "");
		// }
		// if (ans10.getAnswerMapArr().get(i).getAnswerValue()
		// .equals("25")) {
		// // 烹饪用具
		// collist.add(21 + "");
		// }
		// }
		// }

		BaseLog.v("s10的添加后：" + collist.toString());
		BaseLog.v("---------：" + ans.getAnswerMapStr());
		boolean isadd = false;

		for (int i = 0; i < ans.getAnswerMapArr().size(); i++) {
			isadd = false;
			int value = Integer.parseInt(ans.getAnswerMapArr().get(i)
					.getAnswerValue());
			int col = ans.getAnswerMapArr().get(i).getCol();
			int row = ans.getAnswerMapArr().get(i).getRow();

			if (value == 0) {
				isadd = true;
			}

			if (row == 17) {
				if (value <= 1) {
					isadd = true;
				}
			}
			if (row == 25) {
				if (value <= 1) {
					isadd = true;
				}
			}
			if (row >= 19) {
				row--;
			}
			if (row >= 17) {
				row--;
			}
			if (row >= 12) {
				row--;
			}
			if (row >= 7) {
				row--;
			}

			if (row == 8 || row == 18 || row == 13 || row == 20 || row > 27) {
				isadd = false;
			}

			if (isadd) {
				BaseLog.v("value = " + value + "--row = " + row + "--col = "
						+ col);
				collist.add(row + "");
			}

		}

		BaseLog.v("s8的添加后：" + collist.toString());

		if (collist.size() <= 4) {
			return collist;
		}

		Map<String, Double> map = GsonUtil.GsonToMaps(json);

		for (String key : map.keySet()) {

			BaseLog.v("Key = " + key + "value = " + map.get(key));
			int item = Integer.parseInt(key.split("_")[1]);
			if (item < 22) {
				if (-1 != collist.indexOf(item + "")) {
					if (map.get(key) >= 150.0) {
						biglist.add(key.split("_")[1]);
					} else {
						smalllist.add(key.split("_")[1]);
					}
				}

			}
		}

		Collections.shuffle(biglist);
		Collections.shuffle(smalllist);

		BaseLog.v("biglist = " + biglist.size());
		BaseLog.v("smalllist = " + smalllist.size());

		for (int i = 0, size = 4; i < size; i++) {

			int probability = getRandom(0, 10);
			if (biglist.size() != 0) {
				if (probability > 9) {

					reslist.add(biglist.get(0));
					biglist.remove(0);
				} else {
					if (smalllist.size() != 0) {
						reslist.add(smalllist.get(0));
						smalllist.remove(0);
					}
				}
			} else {
				reslist.add(smalllist.get(0));
				smalllist.remove(0);
			}

		}

		BaseLog.v("reslist = " + reslist.size() + "--and reslist = "
				+ reslist.toString());

		return reslist;
	}

	private static final int CALCULAYE_S8 = 9;
	// private static final int CALCULAYE_S10 = 186;
	private static final int CALCULAYE_A1 = 259;

	public Random r;
	ArrayList<String> collist = new ArrayList<String>();
	ArrayList<String> biglist = new ArrayList<String>();
	ArrayList<String> smalllist = new ArrayList<String>();

	ArrayList<String> reslist = new ArrayList<String>();

	/**
	 * 返回一个随机数
	 * 
	 * @param seed
	 *            随机种子可添 0 ：当前时间戳
	 * @param max
	 *            最大值 - 可不填 添负数的时候会取绝对值
	 * @return
	 */
	public int getRandom(int seed, int max) {
		int retRandom = 0;
		if (seed == 0) {
			r = new Random();
		} else {
			r = new Random(seed);
		}
		if (max == 0) {
			retRandom = r.nextInt();
		} else {
			retRandom = r.nextInt(Math.abs(max));
		}
		r = null;
		return retRandom;
	}

	public void InterventionS11(final LinearLayout bodyView, String json) {

		Answer ans = geianswer(CALCULAYE_S8 + "");
		// Answer anss10 = geianswer(CALCULAYE_S10 + "");

		ArrayList<String> sList = sendS11(ans, json);

		for (int k = 0; k < bodyView.getChildCount(); k++) {
			if (bodyView.getChildAt(k) instanceof CheckBox) {
				CheckBox chb = (CheckBox) bodyView.getChildAt(k);
				QuestionItem item = (QuestionItem) chb.getTag();
				String itemvalue = item.getItemValue() + "";

				if (sList.indexOf(itemvalue) != -1) {
					chb.setChecked(true);

				} else {
					chb.setChecked(false);
					chb.setVisibility(View.GONE);
				}
				chb.setClickable(false);

			}
		}
	}

	public boolean isA1() {
		Answer ans = geianswer(CALCULAYE_A1 + "");
		int am = 0;
		int pm = 0;
		if (ans != null) {
			BaseLog.v("A1答案：" + ans.getAnswerMapStr());
			for (int i = 0; i < ans.getAnswerMapArr().size(); i++) {
				AnswerMap ansmap = ans.getAnswerMapArr().get(i);
				if (!Util.isEmpty(ansmap.getAnswerValue())) {
					// collist.add(ansmap.getCol());
					if (ansmap.getCol() == 0) {
						am++;
					}
					if (ansmap.getCol() == 1) {
						pm++;
					}
				}
			}

			if (am == 1 && pm == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	public boolean showS14(String index) {
		Answer ans = geianswer(index);
		if (ans != null) {
			return true;
		} else {
			return false;
		}
	}
}
