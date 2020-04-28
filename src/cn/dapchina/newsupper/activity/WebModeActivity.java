package cn.dapchina.newsupper.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.adapter.ImageAdapter;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.service.WebFeedXML;
import cn.dapchina.newsupper.util.ComUtil;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * @author kejunyao 网页模式访问
 */
public class WebModeActivity extends BaseActivity {

	private UploadFeed feed;

	private View vTopBar;

	private WebView wbVisit;
	private RelativeLayout re_btn;

	private MyApp ma;

	/**
	 * 用来保存设备信息和异常信息
	 */
	// private Map<String, String> infos = new HashMap<String, String>();

	/**
	 * 第一次加载网页
	 */
	private boolean isFirstLoad = true;
	private String actionLocation;
	private boolean actionPost = false;
	/**
	 * 保存标志
	 */
	private int saveAction = 0;

	/**
	 * 录音标志, 录音是否为开启状态
	 */
	private int isStart = -1;

	private String htmlUrl;

	private Button btnRecord, btnCamera;

	private volatile MediaRecorder mRecorder;
	/**
	 * 录音按钮是否点了
	 */
	private volatile boolean isClicked = false;

	private volatile File recordFile;

	/**
	 * 上一页
	 */
	private final int MSG_PRE = 1;

	/**
	 * 下一页
	 */
	private final int MSG_NEXT = 2;

	/**
	 * 预览
	 */
	private final int MSG_REVIEW = 3;

	/**
	 * 问卷重新加载
	 */
	private final int MSG_REDIRECT = 4;

	/**
	 * 录音
	 */
	private final int MSG_RECORD = 5;

	private final int MSG_SAVE = 6;

	private final int MSG_NEW_FEED = 8;

	/**
	 * 预览时或重新加载时需要的传给js的字符串
	 */
	private String saveData = "";

	/**
	 * 
	 */
	// private String saveParams = "";

	private WebFeedXML wfx = new WebFeedXML();

	// private View vBottomBar;

	private View vResult;

	private SlidingDrawer sdImages;
	// private ImageView ivDragIcon;
	private ImageSwitcher mSwitcher;
	private Gallery g;
	private ImageAdapter mImageAdapter;

	private TextView tvImageCount;
	private int screen;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		if(Util.isEmpty(ma.userId)){
			ma.userId=ma.cfg.getString("UserId",this.getString(R.string.user_name_test));
		}
		feed = (UploadFeed) getIntent().getExtras().get("feed");
		// if(Cnt.VISIT_STATE_COMPLETED==feed.getIsCompleted()){
		// /**
		// * 假如是完成的
		// */
		// screen = feed.getScreenOrientation();
		// }else{
		if (null == ma.cfg) {
			ma.cfg = new Config(getApplicationContext());
		}
		/**
		 * 0竖屏 1横屏
		 */
		screen = ma.cfg.getInt("ScreenOrientation", 0);
		// }
		int cur = getResources().getConfiguration().orientation;
		if (1 == screen && ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == cur) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == cur) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setContentView(R.layout.activity_web);
		overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		vTopBar = findViewById(R.id.wb_title_rl);
		re_btn=(RelativeLayout) findViewById(R.id.re_btn);
		vResult = findViewById(R.id.result_ll);
		/**
		 * 实例化抽屉控件
		 */
		sdImages = (SlidingDrawer) findViewById(R.id.pic_sd);
		// vBottomBar = findViewById(R.id.web_bottom_ll);

		wbVisit = (WebView) findViewById(R.id.visit_wb);
		btnRecord = (Button) findViewById(R.id.web_record_btn);
		btnCamera = (Button) findViewById(R.id.web_camera_btn);
		// /data/data/cn.dapchina.supper/files/survey/1504/1504.html
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		Button nq_btn = (Button)this.findViewById(R.id.web_next_btn);
		Button bq_btn = (Button) this.findViewById(R.id.web_pre_btn);
		RelativeLayout.LayoutParams bqParams = new RelativeLayout.LayoutParams((int) (screenWidth * 10 / 37), (int) (screenHeigh / 10 * 1 / 2));
		bqParams.leftMargin = (screenWidth * 3 / 37);
		bqParams.addRule(RelativeLayout.CENTER_VERTICAL);
		bq_btn.setLayoutParams(bqParams);
		RelativeLayout.LayoutParams nqParams = new RelativeLayout.LayoutParams((int) (screenWidth * 18 / 37), (int) (screenHeigh / 10 * 1 / 2));
		nqParams.addRule(RelativeLayout.CENTER_VERTICAL);
		nqParams.addRule(RelativeLayout.RIGHT_OF, R.id.web_pre_btn);
		nqParams.leftMargin = (screenWidth * 3 / 37);
		nq_btn.setLayoutParams(nqParams);

		if (null != feed) {
			/**
			 * html的绝对路径
			 */
			htmlUrl = Util.getSurveyUrl(WebModeActivity.this, feed.getSurveyId());
			File file = new File(htmlUrl);
			System.out.println(file.getAbsolutePath());
			if (file.exists()) {
				actionLocation = "file://" + getFilesDir().getPath() + File.separator + "dap_desk_action";
				htmlUrl = "file://" + htmlUrl;
			} else {
				/**
				 * 假如htm文件不存在,则弹出对话框
				 */
				showMessage(0);
				return;
			}
			long start = System.currentTimeMillis();
			if (Util.isEmpty(feed.getName())) {
				initBar();
				/**
				 * 新建 文件名称为空则说明是新建
				 */
				newFeed(start);
				initWebView();
				loadingWebView();
			} else {
				File f = new File(feed.getPath(), feed.getName());
				if (100 == feed.getIsReview()) {
					initBar();
					if (f.exists()) {
						showWebView();
						initWebView();
						loadingWebView();
						saveData = wfx.readFeedData(feed.getPath(), feed.getName());
						if (Util.isEmpty(saveData)) {
							showMessage(3);
						} else {
							/**
							 * 预览
							 */
							System.out.println("预览--->" + saveData);
							reviewMode();
							handler.sendEmptyMessageDelayed(MSG_REVIEW, 800);
						}
					} else {
						showMessage(3);
					}
				} else if (Cnt.VISIT_STATE_COMPLETED == feed.getIsCompleted() && Cnt.UPLOAD_STATE_UPLOADED <= feed.getIsUploaded()) {
					initBar();
					if (f.exists()) {
						showWebView();
						initWebView();
						loadingWebView();
						saveData = wfx.readFeedData(feed.getPath(), feed.getName());
						if (Util.isEmpty(saveData)) {
							showMessage(3);
						} else {
							/**
							 * 预览
							 */
							// System.out.println("预览--->" + saveData);
							reviewMode();
							handler.sendEmptyMessageDelayed(MSG_REVIEW, 800);
						}
					} else {
						showMessage(3);
					}
				} else {
					/** 继续做 **/
					initBar();
					/**
					 * 重新刷新其开始时间
					 */
					feed.setStartTime(start);
					/**
					 * 有文件名,则说明是继续访问的
					 */
					showWebView();
					initWebView();
					loadingWebView();
					if (f.exists()) {
						saveData = wfx.readFeedData(feed.getPath(), feed.getName());
					}
				}

			}

			/**
			 * 实例化抽屉控件的图标
			 */
			// ivDragIcon = (ImageView) findViewById(R.id.drag_icon_iv);

			mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
			mSwitcher.setFactory(new CustomViewFactor());
			mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
			mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

			g = (Gallery) findViewById(R.id.gallery);
			// g.setAdapter(new ImageAdapter(this));
			g.setOnItemSelectedListener(new CustomItemSelectListener());

			/**
			 * 实例化抽屉控件装载图片的GridView
			 */
			sdImages.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 开抽屉
			{
				@Override
				public void onDrawerOpened() {
					/**
					 * 顶部的上一题、下一题界面隐藏掉
					 */
//					vTopBar.setVisibility(View.GONE);
//					re_btn.setVisibility(View.GONE);
					/**
					 * 打开抽屉控件
					 */
					// ivDragIcon.setImageResource(R.drawable.arrow_down_float);
					ArrayList<UploadFeed> images = ma.dbService.getImages(feed.getUuid(), feed.getSurveyId());
					if (null == tvImageCount) {
						tvImageCount = (TextView) findViewById(R.id.img_count_tv);
					}
					if (0 < images.size()) {
						System.out.println("0<images.size()===" + images.size());
						if (null == mImageAdapter) {
							mImageAdapter = new ImageAdapter(WebModeActivity.this, images);
							g.setAdapter(mImageAdapter);
						} else {
							mImageAdapter.refresh(images);
						}
						tvImageCount.setText("(" + 1 + "/" + images.size() + ")");
					} else {
						tvImageCount.setText("No Pictures");
					}
				}
			});
			sdImages.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
				@Override
				public void onDrawerClosed() {
					/**
					 * 关闭抽屉控件
					 */
					// ivDragIcon.setImageResource(R.drawable.arrow_up_float);
					/**
					 * 显示顶部的上一题、下一题按钮
					 */
//					vTopBar.setVisibility(View.VISIBLE);
//					re_btn.setVisibility(View.VISIBLE);
				}
			});
		} else {
			// TODO
			/**
			 * 属于异常情况
			 */
			clearMemory();
		}
	}

	private void newFeed(long start) {
		feed.setFeedId(null);
		feed.setManyPlaces(null);
		feed.setManyTimes(null);
		feed.setSpent(0);
		feed.setLotsCoord(null);
		feed.setSize(0);
		feed.setPath(null);
		feed.setName(null);
		feed.setUuid(UUID.randomUUID().toString());
		//增加pid
		feed.setPid("0");
		//命名规则
		String name = Util.getXmlName(feed.getUserId(), feed.getSurveyId(), feed.getUuid(),feed.getPid(),feed.getParametersContent());
		feed.setName(name);// System.nanoTime()
		feed.setCreateTime(start);
		feed.setStartTime(start);
		feed.setReturnType("0");
		feed.setType(Cnt.FILE_TYPE_XML);
		String path = Util.getXmlPath(WebModeActivity.this, feed.getSurveyId());
		feed.setPath(path);
		ma.dbService.addUploadFeed(feed);
		showWebView();
	}

	public void btnClick(View v) {
		if (Util.validateSyncClick()) {
			Toasts.makeText(WebModeActivity.this, R.string.too_frequently, Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		/**
		 * 上一页
		 */
		case R.id.web_pre_btn:
			handler.sendEmptyMessage(MSG_PRE);
			break;

		/**
		 * 下一页
		 */
		case R.id.web_next_btn:
			handler.sendEmptyMessage(MSG_NEXT);
			break;

		/**
		 * 录音按钮
		 */
		case R.id.web_record_btn:
			new RecordTask(isClicked, null).execute();
			break;

		/**
		 * 拍照按钮
		 */
		case R.id.web_camera_btn:
			Intent intent = new Intent(WebModeActivity.this, PhotoActivity.class);
			Bundle bundle = new Bundle();
			UploadFeed photo = new UploadFeed();
			photo.setUserId(feed.getUserId());
			photo.setSurveyId(feed.getSurveyId());
			photo.setUuid(feed.getUuid());
			if(ma.cfg.getBoolean("save_inner", false)){
				/**
				 * 保存与内部
				 */
				photo.setPath(Util.getRecordInnerPath(WebModeActivity.this, feed.getSurveyId()));
				photo.setIsSaveInner(0);
			}else{
				/**
				 * 保存在sdcard上
				 */
				photo.setPath(Util.getRecordPath(feed.getSurveyId()));
				photo.setIsSaveInner(1);
			}
			//增加pid  命名规则
			photo.setName(Util.getRecordName(feed.getUserId(), feed.getSurveyId(), Cnt.FILE_TYPE_PNG, feed.getUuid(), null,feed.getPid(),feed.getParametersContent(),"0"));
			photo.setStartTime(System.currentTimeMillis());
			bundle.putSerializable("photo", photo);
			intent.putExtras(bundle);
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;
		}
	}

	/**
	 * 初始化并显示顶部、底部Bar的数据信息
	 */
	private void initBar() {
		if (!Util.isEmpty(feed.getSurveyTitle())) {
			((TextView) findViewById(R.id.web_title_tv)).setText(feed.getSurveyTitle());
		}
		// TextView tvAddr = ((TextView)findViewById(R.id.web_info_tv));
		// if(!Util.isEmpty(feed.getUserId())){
		// tvAddr.setText(feed.getUserId()+"(lat="+feed.getLat()+",lng="+feed.getLng()+")");
		// }else if(!Util.isEmpty(feed.getLat()) &&
		// !Util.isEmpty(feed.getLng())){
		// tvAddr.setText("(lat="+feed.getLat()+",lng="+feed.getLng()+")");
		// }

	}

	/**
	 * 加载页面
	 */
	private void loadingWebView() {
		actionPost = false;
		wbVisit.loadUrl(htmlUrl);
	}

	@SuppressLint("JavascriptInterface")
	private void initWebView() {
		wbVisit.setBackgroundColor(Color.parseColor("#00000000"));
		wbVisit.getSettings().setJavaScriptEnabled(true);
//		wbVisit.getSettings().setPluginsEnabled(true);
		wbVisit.getSettings().setSaveFormData(false);
		wbVisit.getSettings().setSavePassword(false);
		wbVisit.getSettings().setSupportZoom(true);
		wbVisit.getSettings().setBuiltInZoomControls(true);
		// 大树乱码
		wbVisit.getSettings().setDefaultTextEncodingName("UTF-8");
		wbVisit.requestFocusFromTouch();
		setZoomControlGone(wbVisit);
		wbVisit.addJavascriptInterface(new FeedJavaScript(), "feedData");
		wbVisit.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("shouldOverrideUrlLoading");
				view.loadUrl(url);
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				System.out.println("onPageStarted");
				if (isFirstLoad) {
					showWeb();
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				System.out.println("onPageFinished");
				if (isFirstLoad) {
					dismiss();
					isFirstLoad = false;
				}
				super.onPageFinished(view, url);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				System.out.println("onLoadResource");
				if (0 == url.indexOf(actionLocation)) {
					view.stopLoading();
					if (!actionPost) {
						actionPost = true;
						if (url.length() > actionLocation.length()) {
							String params = url.substring(actionLocation.length() + 1);
							wbVisit.setVisibility(View.GONE);
							if (1 == saveAction) {
								handler.sendEmptyMessage(7);
								/**
								 * 最终保存
								 */
								new XmlTask(params, 1).execute();
							} else if (0 == saveAction) {
								/**
								 * 中间保存
								 */
								new XmlTask(params, 0).execute();
							} else {
								System.out.println("保存操作不明确");
							}
						} else {
							System.out.println("onLoadResource--->url.length() > actionLocation.length()不成立");
						}
					} else {
						System.out.println("onLoadResource--->actionPost=true");
					}
				} else {
					System.out.println("onLoadResource--->0 != url.indexOf(actionLocation)");
				}
			}

			@Override
			public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
				super.onTooManyRedirects(view, cancelMsg, continueMsg);
				System.out.println("onTooManyRedirects");
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				System.out.println("onReceivedError");
				/**
				 * 中间保存出错,则重新定位
				 */
				// if (0 == saveAction) {
				// handler.sendEmptyMessageDelayed(MSG_REDIRECT, 100);
				// }
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend, Message resend) {
				super.onFormResubmission(view, dontResend, resend);
				System.out.println("onFormResubmission");
			}

			@Override
			public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
				super.doUpdateVisitedHistory(view, url, isReload);
				System.out.println("doUpdateVisitedHistory");
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);
				System.out.println("onReceivedSslError");
			}

			@Override
			public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
				super.onReceivedHttpAuthRequest(view, handler, host, realm);
				System.out.println("onReceivedHttpAuthRequest");
			}

			@Override
			public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
				System.out.println("shouldOverrideKeyEvent");
				return super.shouldOverrideKeyEvent(view, event);
			}

			@Override
			public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
				super.onUnhandledKeyEvent(view, event);
				System.out.println("onUnhandledKeyEvent");
			}

			@Override
			public void onScaleChanged(WebView view, float oldScale, float newScale) {
				super.onScaleChanged(view, oldScale, newScale);
				System.out.println("onScaleChanged");
			}

		});
		wbVisit.setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
				new AlertDialog.Builder(WebModeActivity.this).setTitle(R.string.notice).setMessage(message).setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				}).setCancelable(false).create().show();
				return true;
			}

		});
	}

	/**
	 * 显示访问结果界面
	 */
	private void showResultView() {
		/**
		 * 显示访问结果界面,则要隐藏WebView界面
		 */
		hideWebView();
		/**
		 * 隐藏底部的Bar
		 */
		// vBottomBar.setVisibility(View.GONE);
		/**
		 * 显示访问结果View
		 */
		vResult.setVisibility(View.VISIBLE);
		Util.showView(WebModeActivity.this, vResult, R.anim.zoom_in);

	}

	/**
	 * 隐藏访问结果界面
	 */
	private void hideResultView() {
		/**
		 * 隐藏结果View
		 */
		Util.showView(WebModeActivity.this, vResult, R.anim.zoom_out);
		vResult.setVisibility(View.GONE);
		/**
		 * 显示底部的Bar
		 */
		// vBottomBar.setVisibility(View.VISIBLE);
		/**
		 * 显示WebView
		 */
		wbVisit.setVisibility(View.VISIBLE);
		Util.showView(WebModeActivity.this, wbVisit, R.anim.zoom_in);
	}

	/**
	 * 显示WebView控件
	 */
	private void showWebView() {
		/**
		 * 显示WebView控件,则要隐藏访问结果界面
		 */
		hideResultView();
		vTopBar.setVisibility(View.VISIBLE);
		re_btn.setVisibility(View.VISIBLE);
		// wbVisit.setVisibility(View.VISIBLE);
		if (null != feed.getSurvey()) {
			if (1 == feed.getSurvey().isPhoto) {
				cameraView(View.VISIBLE);
			} else {
				cameraView(View.GONE);
				sdImages.setVisibility(View.GONE);
			}

			if (1 == feed.getSurvey().isRecord) {
				recordView(View.VISIBLE);
			} else {
				recordView(View.GONE);
			}

			if (1 == feed.getSurvey().globalRecord) {
				if (feed.getIsCompleted() == 1 && feed.getIsReview() == 100) {

				} else {
					System.out.println("隐藏录音按钮");
					recordView(View.GONE);
					new RecordTask(isClicked, null).execute();
				}
			}
		}
	}

	/**
	 * 隐藏WebView控件
	 */
	private void hideWebView() {
		if (null != vTopBar) {
			vTopBar.setVisibility(View.GONE);
		}
		if (null != wbVisit) {
			Util.showView(WebModeActivity.this, wbVisit, R.anim.zoom_out);
			wbVisit.setVisibility(View.GONE);
		}
		if(null!=re_btn){
			re_btn.setVisibility(View.GONE);
		}
	}

	private void reviewMode() {
		if (null != vTopBar) {
			vTopBar.setVisibility(View.GONE);
		}
		if(null!=re_btn){
			re_btn.setVisibility(View.GONE);
		}
		// if (null != vBottomBar) {
		// vBottomBar.setVisibility(View.GONE);
		// }

	}

	private void recordView(int visible) {
		/**
		 * 录音按钮是否显示
		 */
		btnRecord.setVisibility(visible);
		if (View.VISIBLE == visible) {
			btnRecord.setBackgroundResource(R.drawable.audio_online);
		}
	}

	private void cameraView(int visible) {
		/**
		 * 拍照按钮是否显示
		 */
		btnCamera.setVisibility(visible);
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... param) {

	}

	/**
	 * 使用反射： 移除WebView中放大缩小按钮, 不影响多点触屏放大缩小功能
	 * 
	 * @param view
	 *            传入WebView对象
	 */
	public void setZoomControlGone(View view) {
		Class<?> classType;
		Field field;
		try {
			classType = WebView.class;
			/**
			 * 获取私有属性mZoomButtonsController
			 */
			field = classType.getDeclaredField("mZoomButtonsController");
			field.setAccessible(true);
			ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
			mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
			field.set(view, mZoomButtonsController);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	final class FeedJavaScript {
		public void saveFeedXML(String str) {

		}
		public String getActionURL() {

			return actionLocation;
		}

		public void surveySave(int x) {
			saveAction = x;
			actionPost = false;
		}

		public String getSurveyData() {

			return saveData;
		}

		public void surveyCheck(String data) {
			feed.setIndexArr(data);
		}

		public String getCheck() {
			return feed.getIndexArr();
		}

		public int startRec(String name) {
			if (1 == isStart) {
				stopRec();
			}
			Message msg = Message.obtain();
			msg.what = MSG_RECORD;
			msg.obj = name;
			handler.sendMessageDelayed(msg, 500);
			isStart = 1;
			return isStart;
		}

		public int stopRec() {
			new RecordTask(isClicked, null);
			isStart = 0;
			return isStart;
		}
	}

	private final class RecordTask extends AsyncTask<Void, Integer, Boolean> {

		public boolean click;
		private String num;

		public RecordTask(boolean isClick, String number) {
			this.click = isClick;
			this.num = number;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			if (!this.click) {
				/**
				 * 录音
				 */
				String path = "";
				int inner = 0;
				if(ma.cfg.getBoolean("save_inner", false)){
					path = Util.getRecordInnerPath(WebModeActivity.this, feed.getSurveyId());
				}else{
					path = Util.getRecordPath(feed.getSurveyId());
					inner = 1;
				}
				//增加pid  命名规则
				recordFile = new File(path, // path
						Util.getRecordName(feed.getUserId(), feed.getSurveyId(), Cnt.FILE_TYPE_MP3, feed.getUuid(), null,feed.getPid(),feed.getParametersContent(),"0"));
				if (!recordFile.getParentFile().exists()) {
					recordFile.getParentFile().mkdirs();
				}

				try {
					mRecorder = new MediaRecorder();
					mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
					mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					mRecorder.setOutputFile(recordFile.getAbsolutePath());
					mRecorder.prepare();
					mRecorder.start();
					Log.i("kjy", "路径--->" + recordFile.getAbsolutePath());
					ma.dbService.addRecord(//
							feed.getUserId(), //
							feed.getSurveyId(), //
							feed.getUuid(), //
							recordFile.getParent(), //
							recordFile.getName(), //
							System.currentTimeMillis(), //
							Cnt.FILE_TYPE_MP3, //
							num,//
							inner,
							feed.getFeedId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				isClicked = true;
			} else {
				/**
				 * 停止录音
				 */
				if (null != recordFile && null != mRecorder) {
					mRecorder.stop();
					mRecorder.release();
					mRecorder = null;
				}
				isClicked = false;

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/**
			 * 开始录音,网数据库中插入数据
			 */
			if (!this.click) {
				btnRecord.setBackgroundResource(R.drawable.audio_busy);
				Toasts.makeText(WebModeActivity.this, R.string.record_open, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (this.click) {
				/**
				 * 关闭录音
				 */
				btnRecord.setBackgroundResource(R.drawable.audio_online);
				Toasts.makeText(WebModeActivity.this, R.string.record_close, Toast.LENGTH_SHORT).show();
				ma.dbService.updateRecord(recordFile.getName(), System.currentTimeMillis(), recordFile.length());
			}
		}

	};

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_PRE:
				/**
				 * 上一页
				 */
				wbVisit.loadUrl("javascript:andBackPage()");
				break;

			case MSG_NEXT:
				/**
				 * 下一页
				 */
				wbVisit.loadUrl("javascript:andNextPage()");
				break;

			case MSG_REVIEW:
				/**
				 * 预览
				 */
				wbVisit.loadUrl("javascript:androidCheck()");
				System.out.println("javascript:androidCheck()");
				break;

			case MSG_REDIRECT:
				Intent it = new Intent(WebModeActivity.this, WebModeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				startActivity(it);
				clearMemory();
//				System.out.println("网页重定位");
//				/**
//				 * 把答案界面隐藏掉 把上放的Bar 及WebView控件显示出来
//				 */
//				showWebView();
//				/**
//				 * 重定向
//				 */
//				File f = new File(feed.getPath(), feed.getName());
//				if (f.exists()) {
//					/**
//					 * 读取文件中的串
//					 */
//					saveData = wfx.readFeedData(feed.getPath(), feed.getName());
//
//					/**
//					 * 重新加载网页
//					 */
//					loadingWebView();
//				} else {
//					loadingWebView();
//				}
				break;

			case MSG_RECORD:
				/**
				 * 录音
				 */

				break;

			case MSG_SAVE:
				wbVisit.setVisibility(View.GONE);
				wbVisit.loadUrl("javascript:saveData()");
				break;

			case 7:
				wbVisit.setVisibility(View.GONE);
				break;

			case MSG_NEW_FEED:
				newFeed(System.currentTimeMillis());
				loadingWebView();
				break;
			}
		}
	};

	/**
	 * 最总写入
	 * @param str
	 * @return
	 */
	private Integer writeXML(String str) {
		// str = "";
		int result = 0;
		/**
		 * 0空串 1写入失败 2js有问题 3成功
		 */
		if (Util.isEmpty(str)) {
			/**
			 * 空串
			 */
			return 0;
		}
		feed.setRegTime(System.currentTimeMillis());
		if (Util.isEmpty(feed.getFeedId())) {
			feed.setFeedId("0");
		}
		String start = Util.getTime(feed.getStartTime(), 5);
		String end = Util.getTime(feed.getRegTime(), 5);
		if (!Util.isEmpty(feed.getManyTimes())) {
			if (!feed.getManyTimes().contains(start + "," + end)) {
				feed.setManyTimes(feed.getManyTimes() + start + "," + end + ";");
				feed.setSpent((feed.getSpent() + (feed.getRegTime() - feed.getStartTime())));
			}
		} else {
			feed.setManyTimes(start + "," + end + ";");
			feed.setSpent((feed.getSpent() + (feed.getRegTime() - feed.getStartTime())));
		}
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("startdate", start);
		hMap.put("RegDate", end);
		hMap.put("UserAgent", feed.getUuid());
		hMap.put("VisitorID", feed.getUserId());
		hMap.put("FeedID", feed.getFeedId());
		hMap.put("VisitTimes", start);
		hMap.put("VisitAddress", feed.getVisitAddress());
		hMap.put("VisitSate", Cnt.VISIT_STATE_COMPLETED + "");
		hMap.put("lat", String.valueOf(feed.getLat()));
		hMap.put("lng", String.valueOf(feed.getLng()));
		hMap.put("VersionCode", ma.versionCode);
		hMap.put("MacAddress", ma.macAddress);
		hMap.put("MapType", feed.getSurvey().mapType);
		hMap.put("VisitMode", String.valueOf(feed.getVisitMode()));

		/**
		 * 内部名单信息
		 */
		/**
		 * 内部名单信息
		 */
		if (1 == feed.getSurvey().openStatus) {
			HashMap<String, Parameter> hm = feed.getInnerPanel().getPsMap();
			Iterator<Entry<String, Parameter>> it = hm.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Parameter> entry = it.next();
				hMap.put(entry.getKey(), entry.getValue().getContent());
			}
		}

		/**
		 * 每一次的坐标点
		 */
		if (!Util.isEmpty(feed.getLotsCoord())) {
			feed.setLotsCoord(feed.getLotsCoord() + feed.getLat() + "," + feed.getLng() + ";");
		} else {
			feed.setLotsCoord(feed.getLat() + "," + feed.getLng() + ";");
		}

		/**
		 * 每一次的访问位置
		 */
		if (!Util.isEmpty(feed.getManyPlaces())) {
			feed.setManyPlaces(feed.getManyPlaces() + "," + feed.getVisitAddress());
		} else {
			feed.setManyPlaces(feed.getVisitAddress());
		}

		ArrayList<UploadFeed> tempfs = ma.dbService.getEmptyRecordList();
		for (int ii = 0; ii < tempfs.size(); ii++) {
			UploadFeed tempUf = tempfs.get(ii);
			String name = tempUf.getName();
			String path = tempUf.getPath();
			File tempFile = new File(path, name);
			long realLenth = tempFile.length();
			if (realLenth > 0) {
				// 更新已经有的录音文件
				ma.dbService.updateRecord(name, System.currentTimeMillis(), realLenth);
			} else {
				// 放弃该录音文件
				ma.dbService.updateRecordGiveUp(name);
			}
		}
		ArrayList<UploadFeed> fs = ma.dbService.getRecordList(feed.getUuid(), feed.getSurveyId());
		// feed.setName(name);
		//命名规则
		boolean b = wfx.writeFeedXML(str, hMap, fs, feed.getPath(), feed.getName(), feed.getManyTimes(), feed.getLotsCoord(), feed.getManyPlaces(),feed.getSurveyId(),feed.getPid());
		if (b) {
			System.out.println("文件写成功");
			b = wfx.isXmlValidte(feed.getPath() + File.separator + feed.getName());
		} else {
			System.out.println("文件写失败");
			result = 1;
		}
		if (b) {
			//中断断续1
			feed.setIsCompleted(Cnt.VISIT_STATE_COMPLETED);
			result = 3;
		} else {
			//中断断续0
			feed.setIsCompleted(Cnt.VISIT_STATE_INTERRUPT);
			result = 2;
		}
		File xml = new File(feed.getPath(), feed.getName());
		feed.setSize(xml.length());
		ma.dbService.updateUploadFeed(feed);
		return result;
	}

	/**
	 * 中间保存
	 * 
	 * @param str
	 * @return
	 */
	private Integer saveXML(String str) {
		// str = "";
		int result = 0;
		if (Util.isEmpty(str)) {
			return 0;
		}
		feed.setRegTime(System.currentTimeMillis());
		if (Util.isEmpty(feed.getFeedId())) {
			feed.setFeedId("0");
		}
		String start = Util.getTime(feed.getStartTime(), 5);
		String end = Util.getTime(feed.getRegTime(), 5);
		if (!Util.isEmpty(feed.getManyTimes())) {
			if (!feed.getManyTimes().contains(start + "," + end)) {
				feed.setManyTimes(feed.getManyTimes() + start + "," + end + ";");
				feed.setSpent((feed.getSpent() + (feed.getRegTime() - feed.getStartTime())));
			}
		} else {
			feed.setManyTimes(start + "," + end + ";");
			feed.setSpent((feed.getSpent() + (feed.getRegTime() - feed.getStartTime())));
		}
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("startdate", start);
		hMap.put("RegDate", end);
		hMap.put("UserAgent", feed.getUuid());
		hMap.put("VisitorID", feed.getUserId());
		hMap.put("FeedID", feed.getFeedId());
		hMap.put("VisitTimes", start);
		hMap.put("lat", String.valueOf(feed.getLat()));
		hMap.put("lng", String.valueOf(feed.getLng()));
		hMap.put("VisitAddress", feed.getVisitAddress());
		hMap.put("VisitSate", Cnt.VISIT_STATE_INTERRUPT + "");
		hMap.put("VersionCode", ma.versionCode);
		hMap.put("MacAddress", ma.macAddress);
		hMap.put("MapType", feed.getSurvey().mapType);
		hMap.put("VisitMode", String.valueOf(feed.getVisitMode()));

		/**
		 * 内部名单信息
		 */
		/**
		 * 内部名单信息
		 */
		if (1 == feed.getSurvey().openStatus) {
			HashMap<String, Parameter> hm = feed.getInnerPanel().getPsMap();
			Iterator<Entry<String, Parameter>> it = hm.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Parameter> entry = it.next();
				hMap.put(entry.getKey(), entry.getValue().getContent());
			}
		}

		/**
		 * 每一次的坐标点
		 */
		if (!Util.isEmpty(feed.getLotsCoord())) {
			feed.setLotsCoord(feed.getLotsCoord() + feed.getLat() + "," + feed.getLng() + ";");
		} else {
			feed.setLotsCoord(feed.getLat() + "," + feed.getLng() + ";");
		}

		/**
		 * 每一次的访问位置
		 */
		if (!Util.isEmpty(feed.getManyPlaces())) {
			feed.setManyPlaces(feed.getManyPlaces() + "," + feed.getVisitAddress());
		} else {
			feed.setManyPlaces(feed.getVisitAddress());
		}

		ArrayList<UploadFeed> tempfs = ma.dbService.getEmptyRecordList();
		for (int ii = 0; ii < tempfs.size(); ii++) {
			UploadFeed tempUf = tempfs.get(ii);
			String name = tempUf.getName();
			String path = tempUf.getPath();
			File tempFile = new File(path, name);
			long realLenth = tempFile.length();
			if (realLenth > 0) {
				// 更新已经有的录音文件
				ma.dbService.updateRecord(name, System.currentTimeMillis(), realLenth);
			} else {
				// 放弃该录音文件
				ma.dbService.updateRecordGiveUp(name);
			}
		}
		ArrayList<UploadFeed> fs = ma.dbService.getRecordList(feed.getUuid(), feed.getSurveyId());
		// feed.setName(name);
		// boolean b = wfx.writeFeedXML(str, hMap, fs, feed.getPath(),
		// feed.getName(), feed.getManyTimes(), feed.getLotsCoord(),
		// feed.getManyPlaces());
		//命名规则
		boolean b = wfx.writeFeedXML(str, hMap, feed.getPath(), feed.getName(), fs,feed.getSurveyId(),feed.getPid());
		if (b) {
			b = wfx.isXmlValidte(feed.getPath() + File.separator + feed.getName());
		} else {
			result = 1;
		}
		if (b) {
			System.out.println("状态:"+feed.getIsCompleted());
			if (Cnt.VISIT_STATE_COMPLETED == feed.getIsCompleted()) {
				
			} else {
				//中断断续
				feed.setIsCompleted(Cnt.VISIT_STATE_INTERRUPT);
			}
			result = 3;
		} else {
			//未访问
			feed.setIsCompleted(Cnt.VISIT_STATE_NOACCESS);
			result = 2;
		}
		File xml = new File(feed.getPath(), feed.getName());
		feed.setSize(xml.length());
		ma.dbService.updateUploadFeed(feed);
		return result;
	}

	private final class XmlTask extends AsyncTask<Void, Integer, Integer> {

		/**
		 * 要保存的字符串
		 */
		private String data;

		/**
		 * 是中间保存还是最终保存
		 */
		private int action;

		public XmlTask(String saveData, int saveAction) {
			this.data = saveData;
			this.action = saveAction;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// show();
			if (isClicked) {
				/**
				 * 关闭录音
				 */
				btnRecord.setBackgroundResource(R.drawable.audio_online);
				Toasts.makeText(WebModeActivity.this, R.string.record_close, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Integer doInBackground(Void... params) {
			if (null != recordFile && null != mRecorder) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
				isClicked = false;
				ma.dbService.updateRecord(recordFile.getName(), System.currentTimeMillis(), recordFile.length());
			}
			if (1 == action) {
				return writeXML(data);
			} else {
				return saveXML(data);
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			// if(result){
			// System.out.println("成功");
			// }else{
			// System.out.println("失败");
			// }
			// dismiss();
			showResultView();
			TextView tvVisitTime = (TextView) vResult.findViewById(R.id.visit_time_tv);
			tvVisitTime.setText(getResources().getString(R.string.visit_time, Util.getTime(feed.getStartTime(), 5)));
			TextView tvVisitor = (TextView) vResult.findViewById(R.id.visitor_tv);
			tvVisitor.setText(getResources().getString(R.string.visitor, feed.getUserId()));
			TextView tvAddr = (TextView) vResult.findViewById(R.id.visit_addr_tv);
			tvAddr.setText(Util.isEmpty(feed.getVisitAddress())//
			? //
			getResources().getString(R.string.null_addr)//
					: //
					getResources().getString(R.string.visit_addr, feed.getVisitAddress()));
			TextView tvVisitState = (TextView) vResult.findViewById(R.id.visitor_state_tv);
			Button btnLeft = (Button) vResult.findViewById(R.id.left_btn);
			Button btnRight = (Button) vResult.findViewById(R.id.right_btn);
			isFinish=false;
			switch (result) {
			case 0:
				// tvVisitState.setTextColor(Color.RED);
				String str0 = WebModeActivity.this.getResources().getString(R.string.null_str);
				SpannableString ss0 = new SpannableString(str0);
				ss0.setSpan(new ForegroundColorSpan(Color.RED), 6, str0.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvVisitState.setText(
				// visit_state
						ss0);
				btnLeft.setTextColor(Color.YELLOW);
				btnLeft.setText(WebModeActivity.this.getResources().getString(R.string.giveup));
				btnRight.setTextColor(Color.BLUE);
				btnRight.setText(WebModeActivity.this.getResources().getString(R.string.try_again));
				btnLeft.setOnClickListener(new ResultClickListener(0, action));
				btnRight.setOnClickListener(new ResultClickListener(0, action));
				System.out.println("空串");
				break;

			case 1:
				// tvVisitState.setTextColor(Color.RED);
				String str1 = WebModeActivity.this.getResources().getString(R.string.write_failure);
				SpannableString ss1 = new SpannableString(str1);
				ss1.setSpan(new ForegroundColorSpan(Color.RED), 6, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvVisitState.setText(ss1);
				btnLeft.setTextColor(Color.YELLOW);
				btnLeft.setText(WebModeActivity.this.getResources().getString(R.string.giveup));
				btnRight.setTextColor(Color.GREEN);
				btnRight.setText(WebModeActivity.this.getResources().getString(R.string.try_again));
				btnLeft.setOnClickListener(new ResultClickListener(1, action));
				btnRight.setOnClickListener(new ResultClickListener(1, action));
				System.out.println("写入失败");
				break;

			case 2:
				// tvVisitState.setTextColor(Color.YELLOW);
				String str2 = WebModeActivity.this.getResources().getString(R.string.null_data);
				SpannableString ss2 = new SpannableString(str2);
				ss2.setSpan(new ForegroundColorSpan(Color.YELLOW), 6, str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvVisitState.setText(ss2);
				btnLeft.setTextColor(Color.YELLOW);
				btnLeft.setText(WebModeActivity.this.getResources().getString(R.string.giveup));
				btnRight.setTextColor(Color.GREEN);
				btnRight.setText(WebModeActivity.this.getResources().getString(R.string.try_again));
				btnLeft.setOnClickListener(new ResultClickListener(2, action));
				btnRight.setOnClickListener(new ResultClickListener(2, action));
				System.out.println("没有question节点");
				// Toasts.makeText(WebModeActivity.this, R.string.null_data,
				// Toast.LENGTH_LONG).show();
				break;

			case 3:
				System.out.println("成功!");
				// clearMemory();
				// tvVisitState.setTextColor(Color.GREEN);
				String str3 = WebModeActivity.this.getResources().getString(R.string.visit_successfully);
				SpannableString ss3 = new SpannableString(str3);
				ss3.setSpan(new ForegroundColorSpan(Color.BLUE), 6, str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvVisitState.setText(ss3);
				btnLeft.setTextColor(Color.BLACK);
				/**
				 * 假如是最终保存,则将左边的那个按钮置为“新建”
				 */
				if (1 == action) {
					if(1 == feed.getSurvey().openStatus){
						btnLeft.setVisibility(View.GONE);
					}else{
						btnLeft.setText(WebModeActivity.this.getResources().getString(R.string.new_panel));
					}
					
				} else {
					/**
					 * 假如是中间退出,则将左边的那个按钮置为“继续”
					 */
					btnLeft.setText(WebModeActivity.this.getResources().getString(R.string._continue));
				}
				btnRight.setTextColor(Color.BLACK);
				btnRight.setText(WebModeActivity.this.getResources().getString(R.string.terminal));
				btnLeft.setOnClickListener(new ResultClickListener(3, action));
				btnRight.setOnClickListener(new ResultClickListener(3, action));
				break;
			}
		}
	}
	//判断是否 保存值完成
	boolean isFinish;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (View.VISIBLE == vResult.getVisibility() || sdImages.isOpened()) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(isFinish){
				Toasts.makeText(WebModeActivity.this, R.string.too_frequently, 1).show();
			}else{
				isFinish=true;
				if (Cnt.VISIT_STATE_COMPLETED == feed.getIsCompleted() && 100 == feed.getIsReview()) {
					clearMemory();
				} else {
					System.out.println("退出:"+feed.getIsCompleted());
					showMessage(1);
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void showMessage(int type) {
		super.showMessage(type);
		if (0 == type) {
			msgTitle.setText(getResources().getString(R.string.warn));
			msgContent.setVisibility(View.VISIBLE);
			msgContent.setText(getResources().getString(R.string.null_survey));
			btnOk.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.GONE);
			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					clearMemory();
				}
			});
		} else if (1 == type) {
			
			msgTitle.setText(getResources().getString(R.string.notice));
			msgContent.setVisibility(View.VISIBLE);
			msgContent.setText(getResources().getString(R.string.sure_exist));
			btnOk.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					handler.sendEmptyMessage(MSG_SAVE);
				}
			});
			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isFinish=false;
					dialog.dismiss();
				}
			});
		} else if (3 == type) {
			msgTitle.setText(getResources().getString(R.string.notice));
			msgContent.setVisibility(View.VISIBLE);
			msgContent.setText(getResources().getString(R.string.null_show));
			btnOk.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.GONE);
			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					clearMemory();
				}
			});
			// null_show
		}
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private final class ResultClickListener implements OnClickListener {

		private int s;
		private int a;

		public ResultClickListener(int state, int action) {
			this.s = state;
			this.a = action;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/**
			 * 左边的按钮,新建访问or放弃
			 */
			case R.id.left_btn:
				switch (this.s) {
				case 0:// 空串
				case 1:// 写入失败
				case 2:// 没有question节点
					ma.dbService.giveUpFeed(feed.getUuid(), feed.getSurveyId());
					clearMemory();
					break;

				case 3:
					// 成功
					if (1 == a) {
						/**
						 * 此处显示新建
						 */
						Survey survey = feed.getSurvey();
						final String spwd=survey.getPassword();
						//输入密码，假如正确新建，否则退出。
						if(!"".equals(survey.getPassword())){
							//弹出窗口，正确就能新建。
							final EditText et=new EditText(WebModeActivity.this);
							new AlertDialog.Builder(WebModeActivity.this)
							.setTitle(WebModeActivity.this.getString(R.string.input_pwd))
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(et)
							.setPositiveButton(WebModeActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									String pwd = et.getText().toString();
									if("".equals(pwd.trim())){
										Toasts.makeText(WebModeActivity.this, WebModeActivity.this.getString(R.string.null_reverse_input), Toast.LENGTH_SHORT).show();
										return;
									}
									if(pwd.equals(spwd)){
										Intent intent=new Intent(WebModeActivity.this,WebModeActivity.class);
										UploadFeed feed1 = new UploadFeed();
										feed1.setSurvey(feed.getSurvey());
										feed1.setVisitMode(feed.getVisitMode());
										feed1.setSurveyId(feed.getSurveyId());
										feed1.setSurveyTitle(feed.getSurveyTitle());
										feed1.setUserId(ma.userId);
										feed1.setLng(feed.getLng());
										feed1.setLat(feed.getLat());
										Bundle bundle = new Bundle();
										bundle.putSerializable("feed", feed1);
										intent.putExtras(bundle);
										startActivity(intent);
										clearMemory();
									}else{
										Toasts.makeText(WebModeActivity.this, WebModeActivity.this.getString(R.string.pwd_no), Toast.LENGTH_SHORT).show();
										dialog.dismiss();
									}
								}
							})
							.setNegativeButton(WebModeActivity.this.getString(R.string.cancle), null)
							.show();
						}else{
							Intent intent=new Intent(WebModeActivity.this,WebModeActivity.class);
							UploadFeed feed1 = new UploadFeed();
							feed1.setSurvey(feed.getSurvey());
							feed1.setVisitMode(feed.getVisitMode());
							feed1.setSurveyId(feed.getSurveyId());
							feed1.setSurveyTitle(feed.getSurveyTitle());
							feed1.setUserId(ma.userId);
							feed1.setLng(feed.getLng());
							feed1.setLat(feed.getLat());
							Bundle bundle = new Bundle();
							bundle.putSerializable("feed", feed1);
							intent.putExtras(bundle);
							startActivity(intent);
							clearMemory();
						}
					}  else {
						/**
						 * 此处显示继续
						 */
						handler.sendEmptyMessage(MSG_REDIRECT);
					}
					break;
				}
				break;

			/**
			 * 右边的按钮,重试or终止
			 */
			case R.id.right_btn:
				switch (this.s) {
				case 0:// 空串
				case 1:// 写入失败
				case 2:// 没有question节点
					handler.sendEmptyMessage(MSG_REDIRECT);
					break;

				case 3:
					Survey survey = feed.getSurvey();
					final String spwd=survey.getPassword();
					//输入密码，假如正确新建，否则退出。
					if(!"".equals(survey.getPassword())){
						//弹出窗口，正确就能新建。
						final EditText et=new EditText(WebModeActivity.this);
						new AlertDialog.Builder(WebModeActivity.this)
						.setTitle(WebModeActivity.this.getString(R.string.input_pwd))
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(et)
						.setPositiveButton(WebModeActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String pwd = et.getText().toString();
								if("".equals(pwd.trim())){
									Toasts.makeText(WebModeActivity.this, WebModeActivity.this.getString(R.string.null_reverse_input), Toast.LENGTH_SHORT).show();
									return;
								}
								if(pwd.equals(spwd)){
									clearMemory();
								}else{
									Toasts.makeText(WebModeActivity.this, WebModeActivity.this.getString(R.string.pwd_no), Toast.LENGTH_SHORT).show();
									dialog.dismiss();
								}
							}
						})
						.setNegativeButton(WebModeActivity.this.getString(R.string.cancle), null)
						.show();
					}else{
						// 成功
						clearMemory();
					}
					break;
				}
				break;
			}
		}

	};

	private final class CustomViewFactor implements ViewFactory {

		@Override
		public View makeView() {
			ImageView i = new ImageView(WebModeActivity.this);
			// i.setBackgroundColor(0xFF000000);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return i;
		}

	}

	/**
	 * 用于记录上一次点过的图片
	 */
	private View preView;
	private BitmapDrawable bd = null;

	private final class CustomItemSelectListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			UploadFeed image = (UploadFeed) parent.getItemAtPosition(position);
			if (null != image) {
				if (null != preView) {
					/**
					 * 假如上一次点过图片的花 则将其北京置为白色,即表示不是当前显示的图片
					 */
					preView.setBackgroundColor(Color.WHITE);
				}
				/**
				 * 将现当前显示的图片置为上一次显示的图片
				 */
				preView = view;

				/**
				 * 显示当前图片
				 */
				// mSwitcher.setImageURI(Uri.parse(image.getPath() +
				// File.separator + image.getName()));

				if (null != bd) {
					bd.getBitmap().recycle();
					bd = null;
					System.gc();
				}
				mSwitcher.reset();
				try {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					File file = new File(image.getPath() + File.separator + image.getName());
					// 数字越大读出的图片占用的heap越小 不然总是溢出
					long len = file.length();
					if (1048576 > len) { //小于1024k
						opts.inSampleSize = 3;
					} else{
						opts.inSampleSize = 6;
					}
					//resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
					//Bitmap resizeBmp = ;
					
					bd = new BitmapDrawable(BitmapFactory.decodeStream(new FileInputStream(file), null, opts));
					mSwitcher.setImageDrawable(bd);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return;
				}

				/**
				 * 显示当前显示的是哪一个位置的图片,例如(1/6)一共有6张图片当前显示是第1张
				 */
				tvImageCount.setText("(" + (1 + position) + "/" + parent.getCount() + ")");
				/**
				 * 将当前显示的图片背景色置为深灰,即和其他的图片背景色不一样(其他的为白色的)
				 */
				view.setBackgroundColor(Color.BLUE);

			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}
	
	/**
	 * 车展退出问卷清理内存方法
	 * */
	public void clearMemory(){
		ma.cfg.putBoolean("clearMemory", true);
		System.exit(0);
	}
	
	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}
}
