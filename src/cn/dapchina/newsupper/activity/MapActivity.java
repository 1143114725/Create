package cn.dapchina.newsupper.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKMapViewListener;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.UserPosition;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.map.ScaleView;
import cn.dapchina.newsupper.map.ZoomControlView;
import cn.dapchina.newsupper.util.ComUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MapActivity extends com.baidu.mapapi.MapActivity implements OnClickListener {
	GestureDetector mGesture = null;
	// 手指向右滑动时的最小速度
	private static final int XSPEED_MIN = 1000;
	// 手指向右滑动时的最小距离
	private static final int XDISTANCE_MIN = 250;
	
	private TextView tvLocal, tv_time;
	/** 声明百度地图管理器 **/
	private BMapManager mBMapManager = null;

	/** 声明地图视图控件 **/
	private MapView mMapView = null;
	/** onResume时注册此listener，onPause时需要Remove **/
	private LocationListener mLocationListener = null;
	/** 定位图层 **/
	private MyLocationOverlay mLocationOverlay = null;

	/**
	 * 客户端定位对象
	 */
	private LocationClient mLocationClient = null;

	private MapLocationListener mMapListener;
	private volatile boolean mIsStart;

	private MKSearch mSearch;

	private CustomSearchListener mSearchListner;
	// 地图监控用的气泡
	static View mPopView = null;
	private MyApp ma;
	private ImageView map_left_iv;
	private Button btnDate;
	private final static int MAP = 1;
	private String preDate = "";// 记录上次日期
	private ArrayList<UserPosition> ufList = new ArrayList<UserPosition>(); // 大树
																			// 定义用户位置集合
	private MyOverlay myOverlay; // 大树 添加泡泡 代码
	private int count = 0; // 大树 定位计数器

	// 比例尺
	private ScaleView mScaleView;
	private ZoomControlView mZoomControlView;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MAP:
				// 初始化地图监控点
				String date = (String) msg.obj;
				if (date.equals(preDate)) {
					return;
				}
				preDate = date;
				initUserPosition(date);
				// 大树 地图监控轨迹 记录1 不明白
				if (ufList.size() == 0) {
					Toasts.makeText(MapActivity.this, R.string.map_no_recored, 5).show();
				} else {
					// 地图监控开始初始
					/** 添加ItemizedOverlay **/
					Drawable marker = getResources().getDrawable(R.drawable.mylocationmark);
					/** 得到需要标在地图上的资源 **/
					marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
					/** 为maker定义位置和边界 **/
					myOverlay = new MyOverlay(marker, MapActivity.this, ufList);
					mMapView.getOverlays().add(myOverlay); // 添加ItemizedOverlay实例到mMapView
					// 创建点击mark时的弹出泡泡
					mPopView = MapActivity.this.getLayoutInflater().inflate(R.layout.popview, null);
					mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
					mPopView.setVisibility(View.GONE);
					// 地图监控结束
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.map_activity);
		super.onCreate(savedInstanceState);
		tvLocal = (TextView) findViewById(R.id.local_tv);
		tv_time = (TextView) findViewById(R.id.tv_time);
		map_left_iv = (ImageView) findViewById(R.id.map_left_iv);
		map_left_iv.setOnClickListener(this);
		btnDate = (Button) findViewById(R.id.chooiceDate);
		btnDate.setOnClickListener(this);
		mGesture = new GestureDetector(this, new GestureListener()); 
		ma = (MyApp) getApplication();
		/** 实例化百度地图管理器 **/
		mBMapManager = new BMapManager(ma);
		// mSearch = new MKSearch();
		/** 实例化地图视图控件 **/
		mMapView = (MapView) this.findViewById(R.id.map_mv);
		/** 传入认证KEY实例化地图 **/
		/** 传入认证KEY实例化地图 **/
		try {
			mBMapManager.init( // 8CAEC6416168CBE4CCA49F2BA1089D9AE40E00CA
					"8CAEC6416168CBE4CCA49F2BA1089D9AE40E00CA", //
					new MyGeneralListener() //
					);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 大树
		mSearch = new MKSearch();
		// 大树
		mSearchListner = new CustomSearchListener();
		mSearch.init(mBMapManager, mSearchListner);
		/** 调用父类方法实例化当前Activity **/
		super.initMapActivity(mBMapManager);

		mLocationClient = new LocationClient(ma);
		mMapListener = new MapLocationListener(mMapView, tvLocal);
		mLocationClient.registerLocationListener(mMapListener);

		/** 设置启用内置的缩放控件 **/
		mMapView.setBuiltInZoomControls(false);
		mScaleView = (ScaleView) findViewById(R.id.scaleView);
		mScaleView.setMapView(mMapView);
		mZoomControlView = (ZoomControlView) findViewById(R.id.ZoomControlView);
		mZoomControlView.setMapView(mMapView);
		// 地图显示事件监听器。 该接口监听地图显示事件，用户需要实现该接口以处理相应事件。
		mMapView.regMapViewListener(mBMapManager, new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				refreshScaleAndZoomControl();
			}

		});

		/** 设置在缩放动画过程中也显示overlay,默认为不绘制 **/
		mMapView.setDrawOverlayWhenZooming(true);
		/** 获取MapView控制权,进行控制和驱动平移和缩放 **/
		MapController mMapController = mMapView.getController();

		GeoPoint gp = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
		/** 设置地图中心点 **/
		mMapController.setCenter(gp);
		/** 设置地图ZOOM级别 **/
		mMapController.setZoom(15);
		/** 添加定位图层 **/

		// 大树 地图监控画图轨迹 在这里 记录1 不明白
		long currentTimeMillis = System.currentTimeMillis();
		String date = ComUtil.getTime(currentTimeMillis, 1);
		initUserPosition(date);
		if (ufList.size() == 0) {
		} else {
			Log.i("zrl1", ufList.size() + "uflist.size()");
			Drawable marker = getResources().getDrawable(R.drawable.mylocationmark);
			/** 得到需要标在地图上的资源 **/
			marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
			myOverlay = new MyOverlay(marker, this, ufList);
			/** 为maker定义位置和边界 **/
			// 添加ItemizedOverlay实例到mMapView
			mMapView.getOverlays().add(myOverlay);
			// 创建点击mark时的弹出泡泡
			mPopView = super.getLayoutInflater().inflate(R.layout.popview, null);
			mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
			mPopView.setVisibility(View.GONE);
		}
		// 大树 以上部分 添加

		mLocationOverlay = new MyLocationOverlay(this, mMapView);
		mMapView.getOverlays().add(mLocationOverlay);
		// mMapView.getOverlays().add(new ItemsOverlay(marker, this, mMapView));
		/** 添加ItemizedOverlay实例到mMapView **/

		/** 注册定位事件 **/
		mLocationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {

				if (location != null) {
					GeoPoint pt = new GeoPoint((int) (location.getLatitude() * 1e6), (int) (location.getLongitude() * 1e6));
					mMapView.getController().animateTo(pt);
				}
			}
		};

	}

	// 大树 菜单选择 地图模式

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1,getString(R.string.traffic_mode));
		menu.add(0, 2, 2,getString(R.string.aerial_mode));
		return super.onCreateOptionsMenu(menu);
	}
    
	// 比例尺
	private void refreshScaleAndZoomControl() {
		// 更新缩放按钮的状态
		mZoomControlView.refreshZoomButtonStatus(Math.round(mMapView.getZoomLevel()));
		mScaleView.refreshScaleView(Math.round(mMapView.getZoomLevel()));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 1:// 交通模式
			mMapView.setTraffic(true);
			mMapView.setSatellite(false);

			break;
		case 2:// 卫星模式
			mMapView.setSatellite(true);
			mMapView.setTraffic(false);
			break;
		default:
			mMapView.setTraffic(true);
			mMapView.setSatellite(false);
			break;
		}
		return true;
	}

	// 大树 以上 地图模式选择

	// 大树 添加代码 重写定位图层 添加泡泡
	/** 地图监控画轨迹的覆盖层 */
	class MyOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
		private Drawable marker;
		private Context mContext;
		private ArrayList<UserPosition> uList = new ArrayList<UserPosition>();

		public MyOverlay(Drawable marker, Context context, ArrayList<UserPosition> uf) {
			super(boundCenterBottom(marker));
			// super(null);
			this.marker = marker;
			this.mContext = context;
			this.uList = uf;
			// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
			// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
			for (int i = 0; i < uList.size(); i++) {
				// System.out.println("遍历:" + i);
				UserPosition userp = uList.get(i);
				int lat = (int) userp.getUserLat();
				int lon = (int) userp.getUserLng();
				String resName = userp.getUserId();
				String date = userp.getTime();
				String addName = userp.getAddName();
				GeoPoint p = new GeoPoint(lat, lon);
				if (i == uList.size() - 1) {
					// mMapView.getController().setCenter(p);
					// mv.getController().animateTo(gp);
				}
				// 904对 设置一些信息
				mGeoList.add(new OverlayItem(p, (i + 1) + "." + resName, (i + 1) + "." + resName + "于" + date + "时间出现在" + addName));
				populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
			}

		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {

			// Projection接口用于屏幕像素坐标和经纬度坐标之间的变换
			Projection projection = mapView.getProjection();
			for (int index = size() - 1; index >= 0; index--) { // 遍历mGeoList
				OverlayItem overLayItem = getItem(index); // 得到给定索引的item
				String title = overLayItem.getTitle();
				// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
				Point point = projection.toPixels(overLayItem.getPoint(), null);
				// 可在此处添加您的绘制代码
				Paint paintText = new Paint();
				paintText.setColor(0xff6f7006);
				paintText.setStyle(Style.FILL);
				paintText.setTextSize(15);
				// 假如index的值大于9
				if (index + 1 > 9) {
					// 两位数把数字绘制到图上
					canvas.drawText((index + 1) + "", point.x - 8, point.y - 32, paintText); // 绘制文本
				} else {
					canvas.drawText((index + 1) + "", point.x - 4, point.y - 32, paintText); // 绘制文本
				}
				// 可在此处添加您的绘制代码
				Paint paintTextBlue = new Paint();
				paintTextBlue.setStyle(Style.FILL);
				paintTextBlue.setTextSize(15);

				if (index == size() - 1) {
					paintTextBlue.setColor(0xffa70d00);
					canvas.drawText(MapActivity.this.getString(R.string.end_point_hit), point.x - 16, point.y + 20, paintTextBlue); // 绘制文本
				} else if (index == 0) {
					paintTextBlue.setColor(0xff4b8707);
					canvas.drawText(MapActivity.this.getString(R.string.start_point_hit), point.x - 16, point.y + 20, paintTextBlue); // 绘制文本
				}
			}
			super.draw(canvas, mapView, shadow);
			// 调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
			// 先所有点的经度转像素
			ArrayList<Point> points = new ArrayList<Point>();
			for (int i = 0; i < this.size(); i++) {
				Point p = new Point();
				OverlayItem overLayItem = getItem(i);
				projection.toPixels(overLayItem.getPoint(), p);
				points.add(p);
			}
			// 第一个画笔 画圆
			Paint fillPaint = new Paint();
			fillPaint.setColor(0xff33b7fe);
			fillPaint.setAntiAlias(true);
			fillPaint.setStyle(Style.FILL);
			for (int j = 0; j < points.size(); j++) {
				// 将图画到上层
				canvas.drawCircle(points.get(j).x, points.get(j).y, 5.0f, fillPaint);
			}
			// 第二个画笔 画线
			Paint paint = new Paint();
			paint.setColor(0xff477df6);
			paint.setDither(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeWidth(1);
			// 连接 所有点
			Path path = new Path();
			path.moveTo(points.get(0).x, points.get(0).y);
			for (int i = 1; i < points.size(); i++) {
				path.lineTo(points.get(i).x, points.get(i).y);
			}
			// 画出路径
			canvas.drawPath(path, paint);
			boundCenterBottom(marker);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		@Override
		// 处理当点击事件
		protected boolean onTap(int i) {
			setFocus(mGeoList.get(i));
			// 更新气泡位置,并使之显示
			GeoPoint pt = mGeoList.get(i).getPoint();
			MapActivity.this.mMapView.updateViewLayout(MapActivity.this.mPopView,
					new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt, MapView.LayoutParams.BOTTOM_CENTER));
			MapActivity.mPopView.setVisibility(View.VISIBLE);
			Toasts.makeText(this.mContext, mGeoList.get(i).getSnippet(), Toast.LENGTH_LONG).show();
			return true;
		}

		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {
			// 消去弹出的气泡
			MapActivity.mPopView.setVisibility(View.GONE);
			return super.onTap(arg0, arg1);
		}

	}

	/** 地图监控画轨迹的覆盖层 */
	// 大树 添加代码 地图 以上 添加泡泡

	/** 地图监控画轨迹的覆盖层 */

	/**
	 * 初始化地图监控点
	 */
	private void initUserPosition(String date) {
		tv_time.setText(date);
		ufList = ma.dbService.getListPosition(ma.userId, date);
		ArrayList<UserPosition> tempList = new ArrayList<UserPosition>();
		// 算法，距离经纬小于400就不显示在地图上了，其余的显示在地图上
		for (int i = 0; i < ufList.size(); i++) {
			UserPosition userPo = ufList.get(i);
			if (i > 0) {
				double userLat = userPo.getUserLat();
				double userLng = userPo.getUserLng();
				UserPosition preUserPo = ufList.get(i - 1);
				double preUserLat = preUserPo.getUserLat();
				double preUserLng = preUserPo.getUserLng();
				if ((Math.abs(userLat - preUserLat) > 400) || (Math.abs(userLng - preUserLng) > 400)) {
					tempList.add(userPo);
				}
			} else {
				tempList.add(userPo);
			}
		}
		ufList.clear();
		ufList.addAll(tempList);
	}

	private class CustomSearchListener implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo addr, int error) {
			System.out.println(addr.strAddr);
		}

		/**
		 * 驾车
		 */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int error) {
			if (0 != error || null == result) {
				Toasts.makeText(MapActivity.this, R.string.sorry_no_result, Toast.LENGTH_SHORT).show();
				return;
			}
			int index = -1;
			int d = 0;
			for (int i = 0; i < result.getNumPlan(); i++) {
				int des = result.getPlan(i).getRoute(i).getDistance();
				if (des > d) {
					d = des;
					index = i;
				}
			}
			RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mMapView);
			// 此处仅展示一个方案作为示例
			if (-1 < index) {
				routeOverlay.setData(result.getPlan(index).getRoute(index));
			} else {
				routeOverlay.setData(result.getPlan(0).getRoute(0));
			}
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.invalidate();// 2.0.0及以上版本请使用mMapView.refresh();
			mMapView.getController().animateTo(result.getStart().pt);
		}

		/**
		 * 标志性建筑物
		 */
		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {

		}

		/**
		 * 公交、地铁
		 */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int error) {
			if (0 != error || null == result) {
				Toasts.makeText(MapActivity.this, R.string.sorry_no_result, Toast.LENGTH_SHORT).show();
				return;
			}
			int index = -1;
			int d = 0;
			for (int i = 0; i < result.getNumPlan(); i++) {
				int des = result.getPlan(i).getRoute(i).getDistance();
				if (des > d) {
					d = des;
					index = i;
				}
			}
			TransitOverlay tranOvers = new TransitOverlay(MapActivity.this, mMapView);
			if (-1 < index) {
				tranOvers.setData(result.getPlan(index));
			} else {
				tranOvers.setData(result.getPlan(0));
			}
			mMapView.getOverlays().add(tranOvers);
			mMapView.invalidate();
			mMapView.getController().animateTo(result.getStart().pt);
		}

		/**
		 * 步行
		 */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int error) {
			if (0 != error || null == result) {
				Toasts.makeText(MapActivity.this, R.string.sorry_no_result, Toast.LENGTH_SHORT).show();
				return;
			}
			int index = -1;
			int d = 0;
			for (int i = 0; i < result.getNumPlan(); i++) {
				int des = result.getPlan(i).getRoute(i).getDistance();
				if (des > d) {
					d = des;
					index = i;
				}
			}
			RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mMapView);
			// 此处仅展示一个方案作为示例
			if (-1 < index) {
				routeOverlay.setData(result.getPlan(index).getRoute(index));
			} else {
				routeOverlay.setData(result.getPlan(0).getRoute(0));
			}
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.invalidate();// 2.0.0及以上版本请使用mMapView.refresh();
			mMapView.getController().animateTo(result.getStart().pt);
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetRGCShareUrlResult(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mIsStart) {
			setLocationOption();
			mLocationClient.start();
			mIsStart = true;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		/** 若mBMapManager实例对象存在 **/
		if (mBMapManager != null) {
			/** 则销毁,并将其置为null **/
			mBMapManager.destroy();
			mBMapManager = null;
		}
		if (mIsStart) {
			mLocationClient.stop();
			mIsStart = false;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		/** 若mBMapManager实例对象存在 **/
		if (mBMapManager != null) {
			/** 移除更新当前位置监听器 **/
			mBMapManager.getLocationManager().removeUpdates(mLocationListener);
			/** 取消我的当前位置 **/
			mLocationOverlay.disableMyLocation();
			/** 关闭指南针 **/
			mLocationOverlay.disableCompass();
			/** 则停止地图管理者线程 **/
			mBMapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		/** 若mBMapManager实例对象存在 **/
		if (mBMapManager != null) {
			/** 请求更新当前位置监听器 **/
			Log.i("zrl1", "走这里回调事件");
			mBMapManager.getLocationManager().requestLocationUpdates(mLocationListener);
			/** 启用我的当前位置 **/
			mLocationOverlay.enableMyLocation();
			/** 打开指南针 **/
			mLocationOverlay.enableCompass();
			/** 则启动地图管理者线程 **/
			mBMapManager.start();
		}

		// 大树 重新绘制轨迹点

		long currentTimeMillis = System.currentTimeMillis();
		String date = ComUtil.getTime(currentTimeMillis, 1);
		initUserPosition(date);
		if (ufList.size() == 0) {

		} else {
			Drawable marker = getResources().getDrawable(R.drawable.mylocationmark);
			/** 得到需要标在地图上的资源 **/
			marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
			myOverlay = new MyOverlay(marker, this, ufList);
			/** 为maker定义位置和边界 **/
			// 添加ItemizedOverlay实例到mMapView
			mMapView.getOverlays().add(myOverlay);
			// 创建点击mark时的弹出泡泡
			mPopView = super.getLayoutInflater().inflate(R.layout.popview, null);
			mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
			mPopView.setVisibility(View.GONE);
		}

		super.onResume();
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {

		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {

			}
		}

	}

	private class MapLocationListener implements BDLocationListener {

		private BDLocation dLocation;

		private MapView mv;
		private TextView tv;

		public MapLocationListener(MapView mapView, TextView textView) {
			this.mv = mapView;
			this.tv = textView;
		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location) {
				dLocation = location;
				if (null != mv) {
					GeoPoint gp = new GeoPoint((int) (location.getLatitude() * 1e6), (int) (location.getLongitude() * 1e6));
					/** 设置地图中心点 **/
					mv.getController().setCenter(gp);
					mv.getController().animateTo(gp);
				}
				if (!Util.isEmpty(location.getAddrStr())) {
					Log.i("zrl1", location.getAddrStr() + "地址：");
					this.tv.setText(location.getAddrStr());
				}
				if (!Util.isEmpty(ma.userId) && count == 0 && !Util.isEmpty(dLocation.getAddrStr())) {
					long ldate = System.currentTimeMillis();
					String time = ComUtil.getTime(ldate, 0);
					String date = ComUtil.getTime(ldate, 1);
					UserPosition up = new UserPosition();
					up.setTime(time);
					up.setDate(date);
					up.setAddName(dLocation.getAddrStr());
					up.setUserLng(dLocation.getLongitude() * 1e6);
					up.setUserLat(dLocation.getLatitude() * 1e6);
					up.setUserId(ma.userId);
					ma.dbService.addPosition(up);
					count++;
					onResume();
				}

			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {
			if (null != location) {
				dLocation = location;
			}
		}

		public BDLocation getBDLocation() {
			// System.out.println("getBDLocation");
			return dLocation;
		}
	};

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
		option.setPoiExtraInfo(true);
		/**
		 * 获取全地址
		 */
		option.setAddrType("all");// 返回的定位结果包含地址信息

		/**
		 * 10秒钟一次
		 */
		option.setScanSpan(10000);
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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public BDLocation getBDLocation() {
		return mMapListener.getBDLocation();
	}

	private void setInnerView(int visibility, View... vs) {
		if (null == vs) {
			return;
		}
		for (int i = 0; i < vs.length; i++) {
			if (null != vs[i]) {
				vs[i].setVisibility(visibility);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.map_left_iv:
			MapActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.chooiceDate:
			DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
					String strMonth = (month + 1) + "";
					String strDay = dayOfMonth + "";
					if (strMonth.length() == 1) {
						strMonth = "0" + strMonth;
					}
					if (strDay.length() == 1) {
						strDay = "0" + strDay;
					}
					// 选择的日期
					String date = year + "-" + strMonth + "-" + strDay;
					if (!Util.isEmpty(date)) {
						Message msg = handler.obtainMessage();
						msg.what = MAP;
						msg.obj = date;
						handler.sendMessage(msg);
					}
				}
			};
			Calendar calendar = Calendar.getInstance();
			DatePickerDialog dialog = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		default:
			break;
		}

	}

	// 大树地图 搜索功能 开发 待开发 的搜索功能

	private void showSearchModeDialog() {
		// mSearch.TYPE_CITY_LIST;
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(null);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.map_search_dialog, null);

		/**
		 * 起点
		 */
		EditText etStartPoint = (EditText) view.findViewById(R.id.start_point_et);

		EditText etCityPoint = (EditText) view.findViewById(R.id.city_et);
		EditText etCityPoint2 = (EditText) view.findViewById(R.id.city2_et);
		/**
		 * 终点
		 */
		EditText etEndPoint = (EditText) view.findViewById(R.id.end_point_et);

		RadioGroup rgSearchMode = (RadioGroup) view.findViewById(R.id.travel_type_rg);
		rgSearchMode.setOnCheckedChangeListener(new StyleSelectListener(etCityPoint2));
		dialog.setPositiveButton(R.string.cancle, null);
		dialog.setNegativeButton(R.string.ok, new SearchClickListener(etCityPoint, etCityPoint2, etStartPoint, etEndPoint, rgSearchMode.getCheckedRadioButtonId()));
		dialog.setView(view);
		dialog.create().show();
	}

	// 大树地图 搜索 功能

	private class SearchClickListener implements DialogInterface.OnClickListener {

		private EditText etCity, et2City, etStart, etEnd;
		private int style;

		public SearchClickListener(EditText etCity, EditText et2City, EditText etStart, EditText etEnd, int style) {
			this.etCity = etCity;
			this.et2City = et2City;
			this.etStart = etStart;
			this.etEnd = etEnd;
			this.style = style;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String city = etCity.getText().toString();
			if (Util.isEmpty(city)) {
				etCity.setHintTextColor(Color.RED);
				etCity.setHint(R.string.null_city);
				return;
			}
			String startPleace = etStart.getText().toString();
			if (Util.isEmpty(startPleace)) {
				etStart.setHintTextColor(Color.RED);
				etStart.setHint(R.string.null_start);
				return;
			}
			String endPleace = etEnd.getText().toString();
			if (Util.isEmpty(endPleace)) {
				etEnd.setHintTextColor(Color.RED);
				etEnd.setHint(R.string.null_end);
				return;
			}
			String city2 = et2City.getText().toString();
			if (R.id.walking_rb == this.style || R.id.driving_rb == style) {
				if (Util.isEmpty(city2)) {
					et2City.setHintTextColor(Color.RED);
					et2City.setHint(R.string.null_city2);
					return;
				}
			}
			MKPlanNode startNode = new MKPlanNode();
			startNode.name = startPleace;
			MKPlanNode endNode = new MKPlanNode();
			endNode.name = endPleace;
			mSearch.transitSearch(city, startNode, endNode);
			switch (this.style) {
			case R.id.transit_rb:

				break;

			case R.id.driving_rb:
				mSearch.drivingSearch(city, startNode, city2, endNode);
				break;

			case R.id.walking_rb:
				mSearch.walkingSearch(city, startNode, city2, endNode);
				break;
			}

		}

	}

	// 大树地图 搜索功能
	private class StyleSelectListener implements RadioGroup.OnCheckedChangeListener {

		private EditText etCityPoint2;

		public StyleSelectListener(EditText etCityPoint2) {
			this.etCityPoint2 = etCityPoint2;
		}

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (R.id.driving_rb == checkedId || R.id.walking_rb == checkedId) {
				etCityPoint2.setVisibility(View.VISIBLE);
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mGesture.onTouchEvent(event);
	}
	
	
	class GestureListener extends SimpleOnGestureListener  
    {  
  
        @Override  
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
                float velocityY)  
        {  
        	if(e2.getRawX()-e1.getRawX()>XDISTANCE_MIN&&e2.getRawY()-e1.getRawY()<200&&Math.abs(velocityX)>XSPEED_MIN&&Math.abs(velocityY)<XSPEED_MIN){
        		finish();
    			overridePendingTransition(R.anim.right1, R.anim.left1);
    			return true;
    		}
    		return false;
        }  
  
          
    }  
	
}
