package cn.dapchina.newsupper.activity;

import java.util.ArrayList;


import java.util.HashMap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.adapter.VisitAdapter;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.map.ScaleView;
import cn.dapchina.newsupper.map.ZoomControlView;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.HotalkMenuView;
import cn.dapchina.newsupper.view.Toasts;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VisitActivity extends BaseActivity implements OnClickListener {

	private ListView lvVisit;
	private ImageView visit_left_iv, new_panel, more_opt;
	private VisitAdapter visitAdapter;
	private LinearLayout globle_search;// 全局搜索条
	private LinearLayout inner_count;//名单数
	private TextView tv_spinner,download_sum_count,completed_num_count,uploaded_num_count,unUploaded_num_count;// 下拉选项
	// 嵌入
	private Survey survey;
	private MyApp ma;
	private ArrayList<UploadFeed> fs;
	private TextView tvSurveyTile;
	/** , tvSurveyTile **/
	private TextView tvNoList;
	private AutoCompleteTextView actvKeyWords;
	private int position;// 下拉位置

	// 定位嵌入
	private BMapManager bm;
	private LocationClient mLocationClient;
	private static MapLocationListener mMapListener;
	private volatile boolean mIsStart;// 不用每次都配置


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.visit_activity);
		// 大树动画 弹出底部动画
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		boolean isClearMemory = ma.cfg.getBoolean("clearMemory");
		boolean isLoginMode = ma.cfg.getBoolean(Cnt.LOGIN_MODE);
		// 是否清理内存并且不是离线登录状态才 重新读取
		if (isClearMemory) {
			if (!isLoginMode) {
				ma.userId = ma.cfg.getString("userId", this.getString(R.string.user_name_test));
				ma.userPwd = ma.cfg.getString("memoryPP", null);
				System.out.println("ma.userPwd:" + ma.userPwd);
			} else {
				ma.userId = ma.cfg.getString("userId", this.getString(R.string.user_name_test));
			}
			ma.cfg.putBoolean("clearMemory", false);
		}
		download_sum_count=(TextView) findViewById(R.id.download_sum_count);
		completed_num_count=(TextView) findViewById(R.id.completed_num_count);
		uploaded_num_count=(TextView) findViewById(R.id.uploaded_num_count);
		unUploaded_num_count=(TextView) findViewById(R.id.unUploaded_num_count);
		inner_count = (LinearLayout) findViewById(R.id.inner_count);
		new_panel = (ImageView) findViewById(R.id.new_panel);
		globle_search = (LinearLayout) findViewById(R.id.globle_search);
		more_opt = (ImageView) findViewById(R.id.more_opt);
		more_opt.setOnClickListener(this);
		visit_left_iv = (ImageView) findViewById(R.id.visit_left_iv);
		lvVisit = (ListView) findViewById(R.id.visit_list);
		visit_left_iv.setOnClickListener(this);
		actvKeyWords = (AutoCompleteTextView) findViewById(R.id.keyword_actv);
		actvKeyWords.setInputType(InputType.TYPE_NULL);
		actvKeyWords.setOnTouchListener(new FocusListener());
		tvSurveyTile = (TextView) findViewById(R.id.visit_survey_name_tv);
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int)(width/16*9),RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		tvSurveyTile.setMaxLines(3);
		tvSurveyTile.setLayoutParams(params);
		tvNoList = (TextView) findViewById(R.id.no_visit_list_tv);
		tv_spinner = (TextView) findViewById(R.id.spinner);
		/**
		 * 接收LocalActivity列表中传递过来的Survey
		 */
		survey = (Survey) getIntent().getExtras().get("s");
		if (!Util.isEmpty(survey.surveyTitle)) {
			tvSurveyTile.setText(survey.surveyTitle);
		}
		// 新建
		if (survey.openStatus == 1) {
			new_panel.setVisibility(View.INVISIBLE);
		} else {
			globle_search.setVisibility(View.GONE);
		}
		// 地图定位
		if (bm == null) {
			bm = new BMapManager(getApplication());
			bm.init("01DA4281D2CF3AB79EB06975453EF58FB37B274C", new MyGeneralListener());
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
	}

	// 定位
	private class MapLocationListener implements BDLocationListener {

		private BDLocation dLocation;

		public MapLocationListener() {
		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			// System.out.println("onReceiveLocation--->running...");
			if (null != location) {
				System.out.println("onReceiveLocation"+"Lat:"+location.getLatitude()+"Lng:"+location.getLongitude()+"Type:"+location.getLocType());
				dLocation = location;
			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {
			// System.out.println("onReceivePoi--->running...");
			if (null != location) {
				// System.out.println("onReceivePoi");
				dLocation = location;
			}
		}

		public BDLocation getBDLocation() {
			// System.out.println("getBDLocation");
			return dLocation;
		}
	};

	public static BDLocation getBDLocation() {
		return mMapListener.getBDLocation();
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
		// option.setServiceName("com.baidu.location.service_v2.9");
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
			// System.out.println("没网络");
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// System.out.println("key错误");
			}
		}
	}

	@Override
	protected void onResume() {
		
		// 新建限制
		if (1 == survey.oneVisit) {
			boolean isNewPanel = ma.dbService.IsExistSurvey(survey.surveyId);
			if (isNewPanel) {
				new_panel.setVisibility(View.INVISIBLE);
			}
		}
		if (Util.isEmpty(ma.userId)) {
			ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(this)) : (ma.cfg)).getString("UserId", "");
		}
		System.out.println("ma.userId:" + ma.userId);
		fs = ma.dbService.getAllXmlUploadFeedList(survey.surveyId, ma.userId);
		if (Util.isEmpty(fs)) {
			lvVisit.setVisibility(View.GONE);
			tvNoList.setVisibility(View.VISIBLE);
		} else {
			tvNoList.setVisibility(View.GONE);
			lvVisit.setVisibility(View.VISIBLE);
			// 地图位置传入更改
			BDLocation loc = mMapListener.getBDLocation();
			if (null == visitAdapter) {
				if (null != loc) {
					visitAdapter = new VisitAdapter(VisitActivity.this, fs, loc.getLatitude(), loc.getLongitude(),
							loc.getAddrStr(), survey);
				} else {
					visitAdapter = new VisitAdapter(VisitActivity.this, fs, null, null, null, survey);
				}

				lvVisit.setAdapter(visitAdapter);
			} else {
				if (null != loc) {
					visitAdapter.refresh(fs, loc.getLatitude(), loc.getLongitude(), loc.getAddrStr());
				} else {
					visitAdapter.refresh(fs, null, null, null);
				}
			}
			// lvVisit.setOnItemClickListener(this);
			download_sum_count.setText(visitAdapter.getCount() + "");
			completed_num_count.setText(visitAdapter.getCompletedCount() + "");
			uploaded_num_count.setText(visitAdapter.getUploadedCount() + "");
			unUploaded_num_count.setText(visitAdapter.getUnUploadedCount() + "");
		}

		super.onResume();
	}

	private String spinnerTv;

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.ll_spinner:
			AlertDialog.Builder builder = new AlertDialog.Builder(VisitActivity.this);
			builder.setIcon(R.drawable.ic_menu_archive);
			builder.setTitle(R.string.input_category);
			spinnerTv = getString(R.string.more_thing);
			// 指定下拉列表的显示数据
			final String[] cities = { getString(R.string.input_category), survey.getParameter1(), survey.getParameter2(), survey.getParameter3(), survey.getParameter4() };
			// 设置一个下拉的列表选择项
			builder.setItems(cities, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toasts.makeText(VisitActivity.this, VisitActivity.this.getString(R.string.choice_mode) + cities[which], Toast.LENGTH_SHORT).show();
					position = which;
					System.out.println("position:" + position);
					if (which != 0) {
						spinnerTv = cities[which];
						tv_spinner.setText(spinnerTv);
					} else {
						tv_spinner.setText(spinnerTv);
					}
				}
			});
			builder.show();
			break;
		case R.id.search_btn:
			if (0 == position) {
				Util.viewShake(VisitActivity.this, globle_search);
				Toasts.makeText(VisitActivity.this, R.string.input_category_please, Toast.LENGTH_LONG).show();
				return;
			}
			String words = actvKeyWords.getText().toString().trim();
			if (Util.isEmpty(words)) {
				// Util.viewShake(VisitActivity.this, actvKeyWords);
				// Toasts.makeText(VisitActivity.this, R.string.input_keyword,
				// Toast.LENGTH_LONG).show();
				// return;
				fs = ma.dbService.getAllXmlUploadFeedList(survey.surveyId, ma.userId);
			} else {
				fs = ma.dbService.searchXmlUploadFeedList(survey.surveyId, ma.userId, position, words);
				if (Util.isEmpty(fs)) {
					Toasts.makeText(VisitActivity.this, VisitActivity.this.getString(R.string.no_find), Toast.LENGTH_SHORT).show();
				}
			}
			// 地图位置传入更改
			BDLocation _loc = mMapListener.getBDLocation();
			// BDLocation _loc = null;
			if (null == visitAdapter) {
				if (null != _loc) {
					visitAdapter = new VisitAdapter(VisitActivity.this, fs, _loc.getLatitude(), _loc.getLongitude(), _loc.getAddrStr(), survey);
				} else {
					visitAdapter = new VisitAdapter(VisitActivity.this, fs, null, null, null, survey);
				}
				lvVisit.setAdapter(visitAdapter);
			} else {
				if (null != _loc) {
					visitAdapter.refresh(fs, _loc.getLatitude(), _loc.getLongitude(), _loc.getAddrStr());
				} else {
					visitAdapter.refresh(fs, null, null, null);
				}
			}
			break;
		case R.id.new_panel:
			// 证明有这个密码,弹出窗口
			if (!"".equals(survey.getPassword())) {
				final EditText et = new EditText(this);
				new AlertDialog.Builder(this).setTitle(VisitActivity.this.getString(R.string.input_pwd)).setIcon(android.R.drawable.ic_dialog_info).setView(et)
						.setPositiveButton(VisitActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String pwd = et.getText().toString();
								if ("".equals(pwd.trim())) {
									Toasts.makeText(VisitActivity.this, VisitActivity.this.getString(R.string.null_reverse_input), Toast.LENGTH_SHORT).show();
									return;
								}
								if (pwd.equals(survey.getPassword())) {
									// 跳转
									UploadFeed feed = new UploadFeed();
									feed.setSurvey(survey);
									feed.setVisitMode(survey.visitMode);
									feed.setSurveyId(survey.surveyId);
									feed.setSurveyTitle(survey.surveyTitle);
									feed.setUserId(ma.userId);
									feed.setFeedId("0");
									// 地图位置传入更改
									BDLocation loc = mMapListener.getBDLocation();
									if (null != loc) {
										if (BDLocation.TypeNetWorkLocation == loc.getLocType()) {
											feed.setVisitAddress(loc.getAddrStr());
										}
										String lat = String.valueOf(loc.getLatitude());
										String lng = String.valueOf(loc.getLongitude());
										if (-1 < lat.indexOf("E") || -1 < lat.indexOf("e")) {
											feed.setLat("");
										} else {
											feed.setLat(lat);
										}

										if (-1 < lng.indexOf("E") || -1 < lng.indexOf("e")) {
											feed.setLng("");
										} else {
											feed.setLng(lng);
										}
									} else {
										feed.setLat("");
										feed.setLng("");
									}
									// feed.setVisitMode(ma.u)
									if (1 == feed.getVisitMode()) {
										Intent it = new Intent(VisitActivity.this, NativeModeActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("feed", feed);
										it.putExtras(bundle);
										startActivity(it);
										overridePendingTransition(R.anim.zzright, R.anim.zzleft);
									} else {
										// WEB模式
										Intent it = new Intent(VisitActivity.this, WebModeActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("feed", feed);
										it.putExtras(bundle);
										startActivity(it);
										overridePendingTransition(R.anim.zzright, R.anim.zzleft);
									}
									// 跳转结束
									dialog.dismiss();
								} else {
									Toasts.makeText(VisitActivity.this, VisitActivity.this.getString(R.string.pwd_no), Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								}
							}
						}).setNegativeButton(VisitActivity.this.getString(R.string.cancle), null).show();

			} else {
				UploadFeed feed = new UploadFeed();
				feed.setSurvey(survey);
				feed.setVisitMode(survey.visitMode);
				feed.setSurveyId(survey.surveyId);
				feed.setSurveyTitle(survey.surveyTitle);
				feed.setUserId(ma.userId);
				feed.setFeedId("0");
				// 地图位置传入更改
				BDLocation loc = mMapListener.getBDLocation();
				if (null != loc) {
					if (BDLocation.TypeNetWorkLocation == loc.getLocType()) {
						feed.setVisitAddress(loc.getAddrStr());
					}
					String lat = String.valueOf(loc.getLatitude());
					String lng = String.valueOf(loc.getLongitude());
					if (-1 < lat.indexOf("E") || -1 < lat.indexOf("e")) {
						feed.setLat("");
					} else {
						feed.setLat(lat);
					}

					if (-1 < lng.indexOf("E") || -1 < lng.indexOf("e")) {
						feed.setLng("");
					} else {
						feed.setLng(lng);
					}
				} else {
					feed.setLat("");
					feed.setLng("");
				}

				// feed.setVisitMode(ma.u)
				if (1 == feed.getVisitMode()) {
					Intent it = new Intent(VisitActivity.this, NativeModeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("feed", feed);
					it.putExtras(bundle);
					startActivity(it);
					overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				} else {
					Intent it = new Intent(VisitActivity.this, WebModeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("feed", feed);
					it.putExtras(bundle);
					startActivity(it);
					overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				}
			}

			break;
		}
	}

	private final class FocusListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				((EditText) v).setInputType(InputType.TYPE_CLASS_TEXT);
			}
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			VisitActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.visit_left_iv:
			VisitActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.more_opt:
			super.openOptionsMenu();
			break;
		default:
			break;
		}
	}

	// 菜单
	public HotalkMenuView menuListView = null;

	/**
	 * 系统菜单初始化 void 大树动画 2 查看地图 访前说明 重置  
	 */
	private void initSysMenu() {
		if (menuListView == null) {
			menuListView = new HotalkMenuView(this);
		}
		menuListView.listview.setOnItemClickListener(listClickListener);
		menuListView.clear();
		// 添加按位置添加      ic_menu_mapmode_2   ic_menu_paste_holo_light_2   ic_menu_refresh_2   ic_menu_archive  
		menuListView.add(HotalkMenuView.MENU_SEND_MSG_FORMULAR, VisitActivity.this.getString(R.string.read_quota), R.drawable.test_zsj33);
		menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT, VisitActivity.this.getString(R.string.read_map), R.drawable.test_zsj22);
		menuListView.add(HotalkMenuView.MENU_ADD_CONTACT, VisitActivity.this.getString(R.string.read_ins), R.drawable.test_zsj33);
		// 版本兼容 只有IPSOS才能添加
		if (2 == Cnt.appVersion) {
			menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, VisitActivity.this.getString(R.string.reset), R.drawable.test_zsj44);
		}
		//删除附件
		menuListView.add(HotalkMenuView.MENU_DELETE_MULTI_MSG, VisitActivity.this.getString(R.string.read_attach), 	R.drawable.test_zsj11);
	}

	// 大树动画
	protected void switchSysMenuShow() {
		// 初始化系统菜单
		initSysMenu();
		if (!menuListView.getIsShow()) {
			menuListView.show();
		} else {
			menuListView.close();
		}
	}

	/**
	 * 创建菜单,只在创建时调用一次. 大树动画
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		// 注意返回值.
		return super.onCreateOptionsMenu(menu);
	}

	// 大树动画
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		switchSysMenuShow();
		return false;// 返回为true 则显示系统menu
	}

	/**
	 * 菜单点击事件处理 大树动画 1 跳转在这里
	 */
	OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
			// 获取key唯一标识
			int key = Integer.parseInt(view.getTag().toString());
			// 跳转
			switch (key) {
			case HotalkMenuView.MENU_SEND_MSG_FORMULAR:
				view.setBackgroundColor(Color.YELLOW);
				Intent itquota = new Intent(VisitActivity.this, MyQuotaActivity.class);
				itquota.putExtra("s", survey);
				startActivity(itquota);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				break;
			case HotalkMenuView.MENU_VIEW_CONTACT:
//				System.out.println("查看地图");
				view.setBackgroundColor(Color.YELLOW);
				Intent it = new Intent(VisitActivity.this, MapActivity.class);
				startActivity(it);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				break;
			case HotalkMenuView.MENU_ADD_CONTACT:
//				System.out.println("访前说明");
				view.setBackgroundColor(Color.YELLOW);
				Intent itRed = new Intent(VisitActivity.this, MyWordActivity.class);
				itRed.putExtra("survey", survey);
				startActivity(itRed);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				break;
			case HotalkMenuView.MENU_ADD_TO_FAVORITES:
//				System.out.println("重置");
				view.setBackgroundColor(Color.YELLOW);
				if (NetUtil.checkNet(VisitActivity.this)) {
					show();
					Log.i("zrl1", "重置走这里");
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put("surveyID", survey.surveyId);
					MainService.newTask(new Task(TaskType.TS_RESET, hm));
				} else {
					Toasts.makeText(VisitActivity.this, R.string.net_exception, Toast.LENGTH_LONG).show();
				}
				break;
			case HotalkMenuView.MENU_DELETE_MULTI_MSG:
				//删除附件
//				System.out.println("查看删除附件");
				view.setBackgroundColor(Color.YELLOW);
				Intent intent = new Intent(VisitActivity.this, AttachActivity.class);
				intent.putExtra("sid", survey.surveyId);
				startActivity(intent);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				break;
			default:
				break;
			}
			// 关闭
			menuListView.close();
		}

	};

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.map:
	// Intent it = new Intent(VisitActivity.this, MapActivity.class);
	// startActivity(it);
	// overridePendingTransition(R.anim.zzright, R.anim.zzleft);
	// break;
	// // 大树 访前说明 在这里 体现
	// case R.id.red_ins:
	// Intent itRed = new Intent(VisitActivity.this, MyWordActivity.class);
	// itRed.putExtra("survey", survey);
	// startActivity(itRed);
	// overridePendingTransition(R.anim.zzright, R.anim.zzleft);
	// break;
	// // 大树 重置 功能 在这里体现 1
	// // case R.id.reset:
	// // if (NetUtil.checkNet(VisitActivity.this)) {
	// // show();
	// // Log.i("zrl1", "重置走这里");
	// // HashMap<String, Object> hm = new HashMap<String, Object>();
	// // hm.put("surveyID", survey.surveyId);
	// // MainService.newTask(new Task(TaskType.TS_RESET, hm));
	// // } else {
	// // Toasts.makeText(this, R.string.net_exception,
	// // Toast.LENGTH_LONG).show();
	// // }
	// // break;
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	/*@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}*/

	@Override
	protected void onDestroy() {
		if (bm != null) {
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
	public void init() {
		// TODO Auto-generated method stub

	}

	// 大树 重置 功能 2
	ArrayList<UploadFeed> feeds = new ArrayList<UploadFeed>();
	private String arr = "";

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch ((Integer) param[0]) {
		case TaskType.TS_REDEAL:
			dismiss();
			if (null != param[1]) {
				int state = (Integer) param[1];
				if (state == 100) {
					Toasts.makeText(this, R.string.reset_success, Toast.LENGTH_LONG).show();
				} else {
					Toasts.makeText(this, R.string.reset_fail, Toast.LENGTH_LONG).show();
				}
			}
			break;
		case TaskType.TS_RESET:
			dismiss();
			if (null != param[1]) {
				feeds = (ArrayList<UploadFeed>) param[1];
				if (feeds.size() > 0) {
					setPromot(VisitActivity.this.getString(R.string.less_compare) + feeds.size());
					UploadFeed resetFeed = (UploadFeed) feeds.get(0);
					new RecoverTask(resetFeed, 1).execute();
					return;
				} else {
					Toasts.makeText(this, R.string.no_need_reset, Toast.LENGTH_LONG).show();
				}
			} else {
				Toasts.makeText(this, R.string.reset_fail, Toast.LENGTH_LONG).show();
			}
			break;
		}

	}

	// 大树 重置 3 重置 任务 的实现
	// 重置功能线程
	class RecoverTask extends AsyncTask<Void, Integer, Boolean> {
		private UploadFeed u;
		private int isAll;// 0单个, 1所有

		public RecoverTask(UploadFeed upload, int _isAll) {
			this.u = upload;
			this.isAll = _isAll;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean isSuceess = Boolean.FALSE;
			if (null != u) {
				try {
					// 通过feedId比对工作
					String feedId = u.getFeedId();
					UploadFeed resetFeed = ma.dbService.getResetFeed(survey.surveyId, feedId);
					// 存在改状态
					if (null != resetFeed && !Util.isEmpty(resetFeed.getUuid())) {
						// 1.清空答案表
						int updateResetFeed = ma.dbService.updateResetFeed(resetFeed.getUuid());
						// 2.清空upload文件位置 数据库状态。
						int deleteResetFeedAttach = ma.dbService.deleteResetFeedAttach(resetFeed.getUuid());
						// 3.删除upload相关文件的数据库
						int deleteResetAnswer = ma.dbService.deleteResetAnswer(survey.surveyId, resetFeed.getUuid());
						// 证明成功
						if (updateResetFeed > 0 && deleteResetAnswer > 0) {
							arr += u.getFeedId() + ",";
						}
					}
					isSuceess = Boolean.TRUE;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return isSuceess;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				// 成功
				sendSyncMessage(1006, isAll, u);
			} else {
				// 失败
				sendSyncMessage(1006, isAll, u);
			}
			super.onPostExecute(result);
		}

		private void sendSyncMessage(int what, int arg2, Object u) {
			Message msg = Message.obtain();
			msg.what = what;
			msg.arg2 = arg2;
			msg.obj = u;
			handler.sendMessage(msg);
		}
	}

	// 大树 重置 开发 4

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1006:
				feeds.remove(0);
				if (feeds.size() > 0) {
					UploadFeed uploadFile = feeds.get(0);
					setPromot(VisitActivity.this.getString(R.string.less_compare) + feeds.size());
					new RecoverTask(uploadFile, 1).execute();
				} else {
					// 传串
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put("surveyID", survey.surveyId);
					hm.put("feedList", arr);
					MainService.newTask(new Task(TaskType.TS_REDEAL, hm));
				}
				break;

			default:
				break;
			}
		};
	};

	// 大树 重置 功能 5 滚动条的生成
	public volatile ProgressDialog mPd;

	public void show() {
		if (null == mPd) {
			mPd = new ProgressDialog(VisitActivity.this);
			/** 生成一个进度条 **/
			// ProgressDialog.STYLE_SPINNER
			mPd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mPd.setMessage(VisitActivity.this.getResources().getString(R.string.dialog_content));
			mPd.setOnKeyListener(new MyOnKeyListener());
			mPd.setCanceledOnTouchOutside(false);
		}
		mPd.show();
	}

	public void dismiss() {
		mPd.dismiss();
	}

	public void setPromot(String msg) {
		if (null != mPd) {
			mPd.setMessage(getResources().getString(R.string.wait_pro, msg));
		}
	}
	// 滚动条以上 部分

}
