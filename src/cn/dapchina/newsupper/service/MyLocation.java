package cn.dapchina.newsupper.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import cn.dapchina.newsupper.bean.UserPosition;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.ComUtil;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.Util;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
/**
 * 内嵌程序里的定位功能
 * @author Administrator
 *
 */
public class MyLocation extends Service {

	private BMapManager bm;
	private LocationClient mLocationClient;
	private MapLocationListener mMapListener;
	private MyApp ma;
	private Config cfg; //  大树地图      全局变量   
	private static final String SERVERIP="http://www.baidu.com/";
	private volatile boolean mIsStart;//不用每次都配置
	
	
	@Override
	public void onCreate() {
//		System.out.println("监听位置的oncreate");
		ma = (MyApp) getApplication();
		// Log.i(Cnt.TAG, "DiaryService--->onCreate()");
	    //  大树地图      全局变量   
		cfg=(ma.cfg==null ? new Config(getApplicationContext()) : ma.cfg);
		ma.userId=cfg.getString(Cnt.USER_ID, "");
//		ma.userPwd=cfg.getString(Cnt.USER_PWD, "");
		 //  大树地图      全局变量   
		super.onCreate();
	}
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		System.out.println("开始进行判断");
		//有网络定位
		if(ComUtil.checkNet(this)&&!Util.isEmpty(ma.userId)&&!Util.isEmpty(ma.userPwd)){
			System.out.println("有网也有用户名");
			if (bm == null) {
				bm = new BMapManager(getApplication());
				bm.init("01DA4281D2CF3AB79EB06975453EF58FB37B274C",
						new MyGeneralListener());
			}
			mLocationClient = new LocationClient(ma);
			mMapListener = new MapLocationListener();
			mLocationClient.registerLocationListener(mMapListener);
			if (!mIsStart) {
				setLocationOption();
				mLocationClient.start();
				mIsStart = true;
			}
			bm.start();
		}else{
			MyLocation.this.stopSelf();
		}
		super.onStart(intent, startId);
	}
	
	
	private  class MapLocationListener implements BDLocationListener {

		private BDLocation dLocation;

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			if (null != location) {
				dLocation = location;
				new SaveAndUploadAsyncTask(dLocation).execute();
			}
		}
		
		//异步线程
		public class SaveAndUploadAsyncTask extends AsyncTask<Void, Integer, Boolean> {
			private BDLocation dLocation;
			
			public SaveAndUploadAsyncTask(BDLocation Location){
				this.dLocation=Location;
			}
			
			@Override
			protected Boolean doInBackground(Void... params) {
				String userId=ma.userId;
				long ldate=getServerTime();
				//  大树    添加  1
				if (Util.isEmpty(dLocation.getAddrStr()) || ldate==0) {
					return false;
				}
			    //  大树    添加  1
				String time=ComUtil.getTime(ldate, 0);
				String date=ComUtil.getTime(ldate, 1);
				UserPosition up=new UserPosition();
				up.setTime(time);
				up.setDate(date);
				up.setAddName(dLocation.getAddrStr());
				up.setUserLng(dLocation.getLongitude()* 1e6);
				up.setUserLat(dLocation.getLatitude()* 1e6);
				up.setUserId(userId);
				ma.dbService.addPosition(up);
				//暂时取消
				HashMap<String, Object> hm=new HashMap<String, Object>();
				hm.put(Cnt.USER_ID, up.getUserId());
				hm.put(Cnt.USER_PWD, ma.userPwd);
				hm.put("lon",dLocation.getLongitude());
				hm.put("lat",dLocation.getLatitude());
				hm.put("locationName", up.getAddName());
				hm.put("positionDate", up.getTime());
				try {
					InputStream inStream = NetService.openUrl(Cnt.POSITION_URL, hm, "GET");
					if(null==inStream){
//						System.out.println("返回流为空");
						return false;
					}else{
						//获取服务器返回回来的状态
						String state = Util.resolvData(inStream);
						if("100".equals(state)){
//							System.out.println("更改状态:"+state);
							ma.dbService.updatePositionState(1);
							return true;
						}else{
							return false;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				MyLocation.this.stopSelf();
				super.onPostExecute(result);
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}  
			
			
			
		}

		@Override
		public void onReceivePoi(BDLocation location) {
			// System.out.println("onReceivePoi--->running...");
			if (null != location) {
				dLocation = location;
			}
		}

		public BDLocation getBDLocation() {
			// System.out.println("getBDLocation");
			return dLocation;
		}
	};
	
	/**
	 * 获取百度服务器上的时间
	 * @return
	 */
	private long getServerTime() {
		long date=0;
		try {
			URL url = new URL(SERVERIP);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.connect();
			date = conn.getDate();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	// 设置相关参数 百度最新定位key
		private void setLocationOption() {

			LocationClientOption option = new LocationClientOption();
			/**
			 * 假如没有网络就打开gps
			 */
			// boolean isNet = NetUtil.checkNet(this);
			// option.setOpenGps((!isNet)); // 打开gps
			option.setOpenGps(true);
			option.setCoorType("bd09ll"); // 设置坐标类型
			/**
			 * sdk版本号
			 */
//			option.setServiceName("com.baidu.location.service_v2.9");
			option.setPoiExtraInfo(true);
			/**
			 * 获取全地址
			 */
			option.setAddrType("all");// 返回的定位结果包含地址信息

			/**
			 * 10秒钟一次
			 */
			option.setScanSpan(10000);
			// option.setScanSpan(0);

			// 是否需要设备商
			option.setNeedDeviceDirect(true);
			// 需要地址
			option.setIsNeedAddress(true);
			option.disableCache(true);// 禁止启用缓存定位
			option.setPoiDistance(500); // poi查询距离
			/**
			 * 高精度定位方式
			 */
			option.setLocationMode(LocationMode.Hight_Accuracy);
			option.setPoiNumber(5);
			option.disableCache(true);
			mLocationClient.setLocOption(option);
		}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
//			System.out.println("没网络");
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
//				System.out.println("key错误");
			}
		}
	}
	
	@Override
	public void onDestroy() {
		System.out.println("销毁");
		if(bm!=null){
			bm.stop();
			bm = null;
		}
		if (mIsStart) {
			mLocationClient.stop();
			mIsStart = false;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
}
