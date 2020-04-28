package cn.dapchina.newsupper.intervention;


import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * 随机数工具
 *
 * @author EraJieZhang
 * @data 2019/12/7
 */
public class RandomUtil {
	
	public static Random r;
	
	static ArrayList<String> collist = new ArrayList<String>();
	
	static ArrayList<String> biglist = new ArrayList<String>();
	static ArrayList<String> smalllist = new ArrayList<String>();
	
	static ArrayList<String> reslist = new ArrayList<String>();
	
//	public static void main(String[] args) {
//		
//		TotalBean totalBean = new TotalBean(150,200,300,400,5,6,7,8,9,10,11,2,13,5,4,2,4,5,2,1,5,2);
//		String json = GsonUtil.BeanToJson(totalBean);
//		System.out.println("json:: = " + json);
//		
//		Map<String, Double> map = GsonUtil.GsonToMaps(json);
//		
//		for (String key : map.keySet()) {
//			System.out.println("Key = " + key + "value = " + map.get(key));
//			if(map.get(key) >= 150.0){
//				biglist.add(key.split("_")[1]);
//			}
//		}
//		System.out.println("json:: = " + json);
//		
////		for (int i = 0, size = 3; i < size; i++) {
////			collist.add("col:" + i);
////		}
////		if ( collist.size() >4 ){
////			//如果选中的超过四个 进入概率抽取
////			//没超过150份的概率80%
////			for (int i = 0, size = 10; i < size; i++) {
////				biglist.add("big:" + i);
////			}
////			//超过150份的概率20%
////			for (int i = 0, size = 10; i < size; i++) {
////				smalllist.add("small:" + i);
////			}
////			Collections.shuffle(biglist);
////			Collections.shuffle(smalllist);
////			getrun();
////		}else{
////			//选中的小于等于四个，全部显示
////			reslist.addAll(collist);
////		}
////		System.out.println("reslist:: = " + reslist.toString());
//		
//
//	}

	
	public static void getrun() {
		
		for (int i = 0, size = 4; i < size; i++) {
			//0-7 是80%   89 是20%
			int probability = getRandom(0,10);
			System.out.println("probability:: = " + probability);
			
			
			if (probability < 8 ){
				reslist.add(biglist.get(0));
				biglist.remove(0);
			}else{
				reslist.add(smalllist.get(0));
				smalllist.remove(0);
			}
			
		}
	}
	
	
	/**
	 * 返回一个随机数
	 * @param seed  随机种子可添 0 ：当前时间戳
	 * @param max   最大值 - 可不填 添负数的时候会取绝对值
	 * @return
	 */
	public static int getRandom(int seed, int max) {
		int retRandom = 0 ;
		if ( seed == 0 ) {
			r = new Random();
		} else {
			r = new Random(seed);
		}
		if(max == 0 ){
			retRandom = r.nextInt();
		}else{
			retRandom = r.nextInt(Math.abs(max));
		}
		r = null;
		return retRandom;
	}
	
}
