package cn.dapchina.newsupper.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ThreeLeverUtil {

	/**
	 * 工具类 专门用来 处理三级联动 的 显示 问题
	 */

	ArrayList<String> city = new ArrayList<String>();
	// 一级集合
	static ArrayList<String> areaList;
	static HashMap<String, ArrayList<String>> area = new HashMap<String, ArrayList<String>>();
	// 二级集合
	static HashMap<String, ArrayList<String>> way = new HashMap<String, ArrayList<String>>();
	static ArrayList<String> wayList;
	// 三级集合

	// 二级 集合 的临时选中@ 集合
	static ArrayList<String> areaListTemp = new ArrayList<String>();

	// 三级 联动 的临时 选中 集合

	static ArrayList<String> wayListTemp = new ArrayList<String>();

	/**
	 * 获取 一级 集合 city
	 */

	public static ArrayList<String> getFirstList(String s1) {
		ArrayList<String> city = new ArrayList<String>();
		// 三级联动
		// 三级联动更改
		if (s1.indexOf("@@") != -1) {
			String newS1 = s1.substring(s1.indexOf("@@") + "@@".length(), s1.length());

			String[] ss1 = newS1.split("\\|");
			for (int i = 0; i < ss1.length; i++) {
				city.add(ss1[i]);
				System.out.print(city.get(i));
			}
		}
		return city;
	}

	/**
	 * 获取 二级 集合 area
	 */

	public static HashMap<String, ArrayList<String>> getSecondList(String s2) {
		if (s2.indexOf("@@") != -1) {
			String newS2 = s2.substring(s2.indexOf("@@") + "@@".length(), s2.length());
			String[] ss2 = newS2.split("\\|");
			for (int i = 0; i < ss2.length; i++) {

				String[] sss2 = ss2[i].split(":");
				// System.out.println(sss2[0]);// 这个 用来保存 键值
				String[] valueS = { "" };
				areaList = new ArrayList<String>();
				if (1 < sss2.length) {
					valueS = sss2[1].split(",");
					for (int j = 0; j < valueS.length; j++) {
						areaList.add(valueS[j]);
					}
				}
				area.put(sss2[0], areaList);

			}
		}
		return area;
	}

	/**
	 * 获取 三级 联动集合
	 */

	public static HashMap<String, ArrayList<String>> getThridList(String s3) {
		if (s3.indexOf("@@") != -1) {
			String newS3 = s3.substring(s3.indexOf("@@") + "@@".length(), s3.length());
			String[] ss3 = newS3.split("\\|");
			for (int i = 0; i < ss3.length; i++) {
				String[] sss3 = ss3[i].split(":");
				String[] valueS = { "" };
				wayList = new ArrayList<String>();
				if (1 < sss3.length) {
					valueS = sss3[1].split(",");
					for (int j = 0; j < valueS.length; j++) {
						wayList.add(valueS[j]);
					}
				}
				way.put(sss3[0], wayList);
			}
		}
		return way;
	}

	/**
	 * 根据 一级 的位置 锁定 二级 的 集合 参数： 1 二级联动的键值对 集合， 2一级联动的 集合 3 一级位置
	 */

	public static ArrayList<String> getCityPosList(HashMap<String, ArrayList<String>> area, ArrayList<String> city,
			int cityPos) {
		ArrayList<String> areaListTemp = new ArrayList<String>();
		Set<String> set = area.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String test1 = it.next();
			if (city.size() > 0) {
				if (test1.equals(city.get(cityPos))) {
					areaListTemp = area.get(test1);
					break;
				}
			}
		}

		return areaListTemp;
	}

	/**
	 * 根据 二级的位置 锁定 三级的 集合 传递参数 ： 1 二级联动的临时选中 集合 2 三级联动的 键值对 3 二级 所在的位置
	 */

	public static ArrayList<String> getAreaPosList(ArrayList<String> areaListTemp,
			HashMap<String, ArrayList<String>> way, int areaPos) {

		ArrayList<String> wayListTemp = new ArrayList<String>();
		Set<String> set1 = way.keySet();
		Iterator<String> iterator = set1.iterator();
		while (iterator.hasNext()) {
			String ss = (String) iterator.next();
			if (areaListTemp.size() > 0) {
				if (areaPos < areaListTemp.size()) {
					if (ss.equals(areaListTemp.get(areaPos))) {
						wayListTemp = way.get(ss);
						break;
					}
				}
			}
		}

		return wayListTemp;
	}

}
