package cn.dapchina.newsupper.activity;

import java.io.File;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.AnswerMap;
import cn.dapchina.newsupper.bean.CstmMatcher;
import cn.dapchina.newsupper.bean.Group;
import cn.dapchina.newsupper.bean.Logic;
import cn.dapchina.newsupper.bean.LogicItem;
import cn.dapchina.newsupper.bean.LogicList;
import cn.dapchina.newsupper.bean.MatcherItem;
import cn.dapchina.newsupper.bean.MyRecoder;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.QGroup;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.QuestionItem;
import cn.dapchina.newsupper.bean.Restriction;
import cn.dapchina.newsupper.bean.ReturnType;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.bean.TempGroup;
import cn.dapchina.newsupper.bean.TempLogic;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.pageview.MenuHorizontalScrollView;
import cn.dapchina.newsupper.pageview.MenuListAdapter;
import cn.dapchina.newsupper.pageview.SizeCallBackForMenu;
import cn.dapchina.newsupper.service.FeedXml;
import cn.dapchina.newsupper.service.FileUpLoad;
import cn.dapchina.newsupper.util.ComUtil;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.DialogListener;
import cn.dapchina.newsupper.util.ImsIntervetion;
import cn.dapchina.newsupper.util.LogUtil;
import cn.dapchina.newsupper.util.MD5;
import cn.dapchina.newsupper.util.NetService;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.ThreeLeverUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.WritePadDialog;
import cn.dapchina.newsupper.util.XmlUtil;
import cn.dapchina.newsupper.view.DialogBulder;
import cn.dapchina.newsupper.view.DoubleDatePickerDialog;
import cn.dapchina.newsupper.view.EditTextListView;
import cn.dapchina.newsupper.view.HotalkMenuView;
import cn.dapchina.newsupper.view.PopMenu;
import cn.dapchina.newsupper.view.Toasts;

import com.alibaba.fastjson.JSON;

import cn.dapchina.newsupper.adapter.ImageAdapter;
import cn.dapchina.newsupper.activity.NativeModeActivity;

/**
 * @author kejunyao 原生模式访问
 */
public class NativeModeActivity extends BaseActivity implements cn.dapchina.newsupper.view.PopMenu.OnItemClickListener {
	GestureDetector mGesture = null;
	/**
	 * 答卷信息实体
	 */
	private UploadFeed feed;

	/**
	 *   IMS 皮炎湿疹   
	 */
	ImsIntervetion imsIntervetion;  
	/**
	 *    皮炎湿疹     提示    逻辑错误  
	 */
	private final int LUO_JI_WRONG=85;  
	/**
	 * 某一张问卷的所有问题
	 */
	ArrayList<Question> qs;

	/**
	 * 
	 */
	private MyApp ma;

	/**
	 * 题干界面
	 */
	LinearLayout bodyView;

	/**
	 * 当前题目在ArrayList<Question> qs中的下标
	 */
	private int index;

	/**
	 * 当前问题实体
	 */
	private Question q;
	/**
	 * 答卷界面顶部的说明信息
	 */
	TextView tvBar;
	/**
	 * 保存问题中所有有效的控件
	 */
	ArrayList<View> vs = new ArrayList<View>();

	/**
	 * 题目的标题
	 */
	private TextView tvTitle;

	/**
	 * 引用其它问题的答案
	 */
	/**
	 * 上方追加说明
	 */
	private TextView tvCaption;
	/**
	 * 底部追加说明
	 */
	private TextView tvComment;
	/**
	 * 问题横向、纵向包裹界面 用于界面震动
	 */
	private RelativeLayout rlQuestion;
	/**
	 * 设备屏幕的宽
	 */
	private int screenWidth;

	/**
	 * 设备屏幕的高
	 */
	private int screenHeight;

	/**
	 * 显示器设备
	 */
	private Display dis;
	/**
	 * 当前问题的答案
	 */
	private Answer qAnswer = new Answer();

	/**
	 * 暂存答案的变量
	 */
	private Answer tempAnswer;

	/**
	 * 录音
	 */
	private volatile MediaRecorder mRecorder;
	/**
	 * 录音按钮是否点了
	 */
	private volatile boolean isClicked = false;

	/**
	 * 当前录音文件
	 */
	private volatile File recordFile;

	/**
	 * 顶部
	 */
	private View vTopBar;

	/**
	 * 底部经纬度及录音按钮、拍照按钮
	 */

	/**
	 * 问卷访问结果显示视图
	 */
	private View vResult;

	/**
	 * 访问界面包含题干等界面在内
	 */
	private View vVisit;

	/**
	 * 上一页
	 */
	private final int MSG_PRE = 1;

	/**
	 * 下一页
	 */
	private final int MSG_NEXT = 2;

	/**
	 * 录音
	 */
	private final int MSG_RECORD = 3;

	/**
	 * 中间保存答卷的xml
	 */
	private final int MSG_SAVE = 4;

	/**
	 * 最终保存答卷的xml
	 */
	private final int MSG_WRITE = 5;

	/**
	 * 新建当前问卷的访问
	 */
	private final int MSG_NEW_FEED = 6;

	/**
	 * 重现,即继续完成没有完成的答卷
	 */
	private final int MSG_REDIRECT = 7;

	/**
	 * 题目选项答的个数小于预定最小值,非矩阵题
	 */
	private final int STATE_BOUND_LOWER = 8;

	/**
	 * 题目选项答的个数超过预定最大值,非矩阵题
	 */
	private final int STATE_BOUND_UPPER = 9;

	/**
	 * 选项选中的个数小于给定的最小值,矩阵题
	 */
	private final int STATE_BOUND_MATRIX_LOWER = 10;
	/**
	 * 选项选中的个数大于给定的最大值,矩阵题
	 */
	private final int STATE_BOUND_MATRIX_UPPER = 11;

	/**
	 * 同一列中选项连续选择的个数不在预先给定的范围内
	 */
	private final int STATE_CONTINUOUS = 12;

	/**
	 * 小于文本给定的最小值
	 */
	private final int STATE_SYB_MIN = 13;

	/**
	 * 大于文本框给定的最大值
	 */
	private final int STATE_SYB_MAX = 14;

	/**
	 * 文本框要求填写数字,而填写的不是数字
	 */
	private final int FORMAT_NO_NUMBER = 15;

	/**
	 * 每一行选择的个数小于给定的个数
	 */
	private final int STATE_ROW_LESS = 16;

	/**
	 * 什么都没做,用于必答题什么都没答的情况
	 */
	private final int STATE_NOTHING = 17;

	/**
	 * 关闭界面
	 */
	private final int MSG_FINISH = 18;

	private final int STATE_SYB_REPEAT = 19;

	/**
	 * 和上面比较等于日期不正确。
	 */
	private final int STATE_SYB_DATE_EQUAL = 20;
	/**
	 * 和上面比较大于等于日期不正确。
	 */
	private final int STATE_SYB_DATE_UPPER_EQUAL = 21;
	/**
	 * 和上面比较不等于日期不正确。
	 */
	private final int STATE_SYB_DATE_NO_EQUAL = 22;

	/**
	 * 和上面比较大于日期不正确。
	 */
	private final int STATE_SYB_DATE_UPPER = 23;
	/**
	 * 和上面比较小于日期不正确。
	 */
	private final int STATE_SYB_DATE_LOW_EQUAL = 24;
	/**
	 * 和上面比较小于等于日期不正确。
	 */
	private final int STATE_SYB_DATE_LOW = 25;
	/**
	 * 不是整数或者小数
	 */
	private final int FORMAT_NO_FLOAT_NUMBER = 26;
	/**
	 * 总和是不是满足条件
	 */
	private final int STATE_SYB_SUM_VALUE = 27;
	/**
	 * 不是数字或者英文
	 */
	private final int FORMAT_NO_NUMBER_AND_WORD = 28;
	/**
	 * 不是标准邮箱
	 */
	private final int FORMAT_NO_EMAIL = 29;
	/**
	 * 是否是单题拍照的验证码
	 */
	private final int FORMAT_NO_CAMERA = 30;
	/**
	 * 是否单题签名验证码
	 */
	private final int FORMAT_NO_SIGN = 33;
	/**
	 * 旧版本的数字之和
	 */
	private final int TOTAL_SUM_TOAST = 31;
	/**
	 * 单项是否满足
	 */
	private final int ITEM_NO_REQUIRED = 32;

	/**
	 * 题外关联 之和 的 判断 ！ 如果不想等 则给出提示 大树 外部关联 1
	 */
	private final int QUESTION_OUT_YING_RELEVANCE = 50;

	/**
	 * 题外关联之 内部关联 判断 提示 大树 内部关联 1
	 */
	private final int QUESTION_INTERIOR_RELEVANCE = 51;
	/**
	 * IMS临时干预价格 小数点后必须保留两位有效数字
	 */
	private final int DECIMAL_PLACES_NO_MEET = 52;
	/**
	 * 大树排序 判断是否重复 如果选项重复提示
	 */
	private final int QUESTION_ITEM_ORDER_REPEAT = 53;
	/**
	 * 大树排序 重复选项值
	 */
	private ArrayList<Integer> orderRepeatList;
	private HashMap<Integer, String> orderRepeatMap;
	/**
	 * 文本框最大值、最小值的中间保存变量
	 */
	private int syb;

	/**
	 * 写XML文件用到的类
	 */
	private FeedXml fm = new FeedXml();

	/**
	 * 提示性对话框
	 */
	private Dialog mDialog;

	/**
	 * 控制结果界面是否显示
	 */
	private boolean isShow = true;

	/**
	 * 上方追加说明图片显示布局
	 */
	private LinearLayout llCaption;

	/**
	 * 下方追缴说明图片显示布局
	 */
	private LinearLayout llComment;

	private final LayoutParams FILL_WRAP = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	private final LayoutParams WRAP_WRAP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	/**
	 * 手指摁下屏幕的开始位置
	 */

	private SlidingDrawer sdImages;
	private ImageSwitcher mSwitcher;
	private Gallery g;
	private ImageAdapter mImageAdapter;

	private TextView tvImageCount;

	private int screen;

	private boolean mapMonitor;
	
	ArrayList<QuestionItem> mTempRows = new ArrayList<QuestionItem>();
	// 存放下拉的键值
	private HashMap<String, Integer> tvMap = null;
	/** 之前某某题目的项 **/
	int preIndex = 0;
	/** 之前某某题目的A1号 **/
	String preTitle = "";
	/** 本题目第几项 **/
	int currIndex = 0;
	/**
	 ** 总数值为0
	 **/
	double sum = 0;

	private boolean isNew = true;

	// 屏幕的宽度
	private int maxCWidth;
	/** 测试不能点击下一页 */
	private Button nq_btn, bq_btn;
	/** 全局进度条 **/
	private ProgressBar globleProgress;

	/**
	 * 辅助题组随机 根据问题的index值找到该题目在ArrayList<Question> qs中的位置
	 */
	HashMap<Integer, Integer> iiMap = new HashMap<Integer, Integer>();

	/**
	 * 用于记录一个题组随机是否完成
	 */
	private QGroup qGroup = new QGroup();

	private boolean isGroup;

	private ArrayList<Integer> tempIndexArr = new ArrayList<Integer>();

	/**
	 * 判断是否是单题拍照
	 */
	private boolean isHaveSingle;
	/*** ZZ题组随机 */
	List<String> arrList = new ArrayList<String>();
	// 手动循环题组随机的顺序
	private String groupSequence;

	/*** ZZ题组随机 */

	// 单题签名
	private ImageView ivSign;
	// 问卷字号动态设置
	private int surveySize;// 最小 的字体
	private int lowSurveySize;// 说明字体大小
	private int middleSueveySize;// 中等字体
	private int bigSurveySize;// 大字体

	/**
	 * 逻辑次数跳转
	 */
	private LogicList logicList;

	// 目录开始
	private MenuHorizontalScrollView scrollView; // 侧滑
	private Button my_btn; // 侧滑效果
	private ListView menuList; // 显示的目录list
	private MenuListAdapter menuListAdapter;// 目录 adapter
	private View[] children; // 左右两边
	private LayoutInflater inflater;// 条件器
	private View acbuwaPage;// 真正做题的页面
	private ArrayList<Integer> dtOrderList = new ArrayList<Integer>();// 存取目录显示值
	private boolean isQgroupOrLogic = false;// 判断是否是题组随机题目 或者是逻辑题

	// get set方法
	public MenuHorizontalScrollView getScrollView() {
		return scrollView;
	}

	public void setScrollView(MenuHorizontalScrollView scrollView) {
		this.scrollView = scrollView;
	}

	// 目录结束

	// 单复选矩阵固定开始
	private TextView tvTitle_new;
	private TextView tvCaption_new;
	private RelativeLayout rlQuestion_new;
	private LinearLayout llCaption_new;
	LinearLayout bodyView_new;
	private LinearLayout ltitle_tv_new;
	private ImageView ivSign_new;
	private LinearLayout ltitle_tv;
	// 单复选矩阵固定结束

	// 三级联动 开始
	/**
	 * 一级 城市 集合
	 */
	private ArrayList<String> city = null;

	/**
	 * 二级 区域 集合 键值对
	 */
	private static HashMap<String, ArrayList<String>> area = null;

	/**
	 * 三维 街道 集合 兼职对
	 */
	private static HashMap<String, ArrayList<String>> way = null;

	/**
	 * 二级 区部 集合 临时 变量
	 */
	private ArrayList<String> areaListTemp = null;
	/**
	 * 三级 街道 集合 临时变量
	 */
	private ArrayList<String> wayListTemp = null;

	private String s1 = "";
	private String s2 = "";
	private String s3 = "";

	int cityPos = 0;
	int areaPos = 0;

	private Spinner provinceSpinner = null;
	private Spinner citySpinner = null;
	private Spinner countrySpinner = null;
	private ArrayAdapter<String> provinceAdapter = null;
	private ArrayAdapter<String> countryAdapter = null;
	private ArrayAdapter<String> cityAdapter = null;
	private RelativeLayout re_btn;

	/**
	 * 大树拒访 定义数据类型
	 */
	private Button btn_title_choice; // 大树拒访
	private PopMenu popMenu; // 大树拒访
	private ArrayList<ReturnType> rtList; // 拒绝访问 返回类型 集合
	private int returnTypeId = -2; // 初始为-2证明没选。 访问状态
	private String returnName="";//访问状态名称

	// 三级联动 结束
	TextView twoSiteNoticeTv; // 双引用 提示语

	private static final String TAG="zrl1";  //  本地测试  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		/**
		 * 应用的上下文
		 */
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		if (null == ma.cfg) {
			ma.cfg = new Config(NativeModeActivity.this);
		}

		/**
		 * 0竖屏 1横屏    默认横屏 显示      
		 */
		screen = ma.cfg.getInt("ScreenOrientation", 0);
		
		/**
		 * 0竖屏 1横屏    默认横屏 显示      
		 */
		mapMonitor = ma.cfg.getBoolean("mapMonitor",false);
		
		/**
		 * 初始化问卷字号动态设置
		 */
		String size = ma.cfg.getString("surveySize", "");
		if (Util.isEmpty(size)) {
			surveySize = 16;
		} else {
			try {
				surveySize = Integer.parseInt(size);
			} catch (Exception e) {
				surveySize = 16;
			}
		}
		lowSurveySize = (int) (surveySize * 1.25);
		middleSueveySize = (int) (surveySize * 1.75);
		bigSurveySize = (int) (surveySize * 2);
		
		/**
		 * 初始化问卷字号动态设置完毕
		 */
		if (1 == screen) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// 目录开始 所有的acbuwaPage
		setContentView(R.layout.menu_scroll_view);
		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuList = (ListView) findViewById(R.id.menuList);
		inflater = LayoutInflater.from(this);
		this.acbuwaPage = inflater.inflate(R.layout.activity_native, null);
		my_btn = (Button) acbuwaPage.findViewById(R.id.my_btn);
		//mGesture = new GestureDetector(this, new GestureListener());
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		LayoutParams params = new LayoutParams(screenWidth / 12, screenHeight / 12);
		// 目录 滑动横竖屏bug。
		if (1 == screen) {
			// 1280*752
			// 假如宽小于高，没有真正横屏
			if (screenWidth < screenHeight) {
				this.scrollView.setLayoutParams(new FrameLayout.LayoutParams(screenHeight, screenWidth));
			}
		}
		// 目录 滑动横竖屏bug结束

		// my_btn.setLayoutParams(params);
		my_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scrollView.clickMenuBtn();
			}
		});
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		// leftView.setBackgroundResource(R.drawable.menu_bg);
		children = new View[] { leftView, acbuwaPage };
		this.scrollView.initViews(children, new SizeCallBackForMenu(this.my_btn), this.menuList);
		this.scrollView.setMenuBtn(this.my_btn);
		// 目录结束
		/**
		 * 获取屏幕设备
		 */
		dis = getWindowManager().getDefaultDisplay();
		maxCWidth = (int) (dis.getWidth() * 0.95);
		/**
		 * 问题限制提示对话框
		 */
		mDialog = new Dialog(NativeModeActivity.this, R.style.question_ds);
		/**
		 * 设置其界面
		 */
		mDialog.setContentView(R.layout.question_promot_dialog);

		/**
		 * 按键禁用,比如点返回按钮时,对话框不消失
		 */
		mDialog.setOnKeyListener(new MyOnKeyListener());

		/**
		 * 获取上一个界面传过来的对象
		 */
		feed = (UploadFeed) getIntent().getExtras().get("feed");
		/**
		 * 答题的界面
		 */
		vVisit = acbuwaPage.findViewById(R.id.visit_sv);
		rlQuestion = (RelativeLayout) acbuwaPage.findViewById(R.id.quesiont_rl);
		nq_btn = (Button) acbuwaPage.findViewById(R.id.nq_btn);
		bq_btn = (Button) acbuwaPage.findViewById(R.id.bq_btn);
		RelativeLayout.LayoutParams bqParams = new RelativeLayout.LayoutParams((int) (screenWidth * 10 / 37), (int) (screenHeight / 8 * 1 / 2));
		// bqParams.topMargin = screenHeigh / 8 * 1 / 5;
		bqParams.leftMargin = (screenWidth * 3 / 37);
		// bqParams.addRule(RelativeLayout.CENTER_VERTICAL);
		bqParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		bq_btn.setLayoutParams(bqParams);
		RelativeLayout.LayoutParams nqParams = new RelativeLayout.LayoutParams((int) (screenWidth * 18 / 37), (int) (screenHeight / 8 * 1 / 2));
		nqParams.addRule(RelativeLayout.RIGHT_OF, R.id.bq_btn);
		nqParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		// nqParams.addRule(RelativeLayout.CENTER_VERTICAL);
		// nqParams.topMargin = screenHeigh / 8 * 1 / 5;

		nqParams.leftMargin = (screenWidth * 3 / 37);
		nq_btn.setLayoutParams(nqParams);
		nq_btn.setClickable(false);

		/**
		 * 进度条 控制
		 */
		globleProgress = (ProgressBar) acbuwaPage.findViewById(R.id.visit_progressBar);
		re_btn = (RelativeLayout) findViewById(R.id.re_btn);
		/**
		 * 显示标题
		 */
		tvTitle = (TextView) acbuwaPage.findViewById(R.id.qtitle_tv);
		/**
		 * 显示单题签名
		 */
		ivSign = (ImageView) acbuwaPage.findViewById(R.id.ivSign);
		/**
		 * 顶部上一页、下一页、标题等界面
		 */
		vTopBar = acbuwaPage.findViewById(R.id.title_rl);

		/**
		 * 顶部拍照、录音、经纬度等界面
		 */

		/**
		 * 访问完成后的结果显示界面
		 */
		vResult = acbuwaPage.findViewById(R.id.result_ll);

		/**
		 * 粗体
		 */
		/**
		 * 上方追缴说明
		 */
		tvCaption = (TextView) acbuwaPage.findViewById(R.id.caption_tv);
		tvCaption.setTextSize(lowSurveySize);

		/**
		 * 上方追加说明中包含的图片显示布局
		 */
		llCaption = (LinearLayout) acbuwaPage.findViewById(R.id.caption_ll);

		/**
		 * 下方追加说明
		 */
		tvComment = (TextView) acbuwaPage.findViewById(R.id.comment_tv);
		tvComment.setTextSize(lowSurveySize);

		/**
		 * 下方追加说明中包含的图片显示布局
		 */
		llComment = (LinearLayout) acbuwaPage.findViewById(R.id.comment_ll);

		/**
		 * 题干界面
		 */
		bodyView = (LinearLayout) acbuwaPage.findViewById(R.id.body_ll);
		// 单复选矩阵固定
		rlQuestion_new = (RelativeLayout) acbuwaPage.findViewById(R.id.quesiont_rl_new);
		/**
		 * 显示标题
		 */
		tvTitle_new = (TextView) acbuwaPage.findViewById(R.id.qtitle_tv_new);
		/**
		 * 上方追缴说明
		 */
		tvCaption_new = (TextView) acbuwaPage.findViewById(R.id.caption_tv_new);
		tvCaption_new.setTextSize(20);
		/**
		 * 上方追加说明中包含的图片显示布局
		 */
		llCaption_new = (LinearLayout) acbuwaPage.findViewById(R.id.caption_ll_new);
		bodyView_new = (LinearLayout) acbuwaPage.findViewById(R.id.body_ll_new);
		ltitle_tv_new = (LinearLayout) acbuwaPage.findViewById(R.id.ltitle_tv_new);
		ltitle_tv = (LinearLayout) acbuwaPage.findViewById(R.id.ltitle_tv);
		ivSign_new = (ImageView) acbuwaPage.findViewById(R.id.ivSign_new);
		// 单复选矩阵固定结束

		/**
		 * 大树拒访  访问状态
		 */
		btn_title_choice = (Button) acbuwaPage.findViewById(R.id.btn_title_choice);
		btn_title_choice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 选择状态
				popMenu.showAsDropDown(btn_title_choice);
			}
		});
		popMenu = new PopMenu(this);
		rtList = feed.getSurvey().getRlist();
		//有返回接口并且有内部名单 
		if(!Util.isEmpty(rtList)&&feed.getSurvey().openStatus==1){
			String[] arr = new String[rtList.size()];
			for(int i=0;i<rtList.size();i++){
				arr[i]=rtList.get(i).getReturnName();
			}
			popMenu.addItems(arr);
			popMenu.setOnItemClickListener(this);
		}else{
			btn_title_choice.setVisibility(View.GONE);
		}
		// 以上 大树拒访  访问状态
		twoSiteNoticeTv = (TextView) findViewById(R.id.notice_tv);
		/**
		 * //逻辑次数跳转JSON串
		 */
		String strLogicList = feed.getSurvey().strLogicList;
		if (!Util.isEmpty(strLogicList)) {
			logicList = XmlUtil.parserJsonToLogicList(strLogicList);
			/** 逻辑次数跳转开始 **/
			if (logicList != null) {
				isQgroupOrLogic = true;
				ArrayList<Logic> logics = logicList.getLogics();
				if (!Util.isEmpty(logics)) {
					for (Logic logic : logics) {
						// System.out.println("累加几次后跳:"+logic.getCountJump()+"/t跳到哪个index中:"+logic.getJumpIndex());
						int countJump = logic.getCountJump();
						int jumpIndex = logic.getJumpIndex();
						if (countJump == 0 || jumpIndex == 0) {
							continue;
						} else {
							indexList.add(jumpIndex);
						}
					}
				}
			}
		}
		/**
		 * //逻辑次数跳转JSON串结束
		 */
		if (null == feed) {
			finish();
		} else {
			groupSequence = feed.getGroupSequence();
		}
		tvBar = (TextView) acbuwaPage.findViewById(R.id.survey_title_tv);
		LinearLayout.LayoutParams barparams = new LinearLayout.LayoutParams((int) (screenWidth / 4 * 3), LinearLayout.LayoutParams.WRAP_CONTENT);
		tvBar.setMaxLines(2);
		tvBar.setLayoutParams(barparams);
		/**
		 * 实例化抽屉控件 <!-- 目录开始 -->
		 */
		sdImages = (SlidingDrawer) acbuwaPage.findViewById(R.id.pic_sd);

		LayoutParams layoutParams = new LayoutParams(screenWidth, screenHeight);
		sdImages.setLayoutParams(layoutParams);
		/**
		 * 实例化抽屉控件的图标 <!-- 目录结束 -->
		 */
		// ivDragIcon = (ImageView) findViewById(R.id.drag_icon_iv);

		mSwitcher = (ImageSwitcher) acbuwaPage.findViewById(R.id.switcher);
		mSwitcher.setFactory(new CustomViewFactor());
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		g = (Gallery) acbuwaPage.findViewById(R.id.gallery);
		// g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(new CustomItemSelectListener());

		/**
		 * 实例化抽屉控件装载图片的GridView，控制平板本地预览
		 */
		sdImages.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 开抽屉
		{
			@Override
			public void onDrawerOpened() {
				// System.out.println("onDrawerOpened()");
				/**
				 * 顶部的上一题、下一题界面隐藏掉
				 */
				vTopBar.setVisibility(View.GONE);
				/**
				 * 打开抽屉控件
				 */
				ArrayList<UploadFeed> images = ma.dbService.getImages(feed.getUuid(), feed.getSurveyId());
				if (null == tvImageCount) {
					tvImageCount = (TextView) acbuwaPage.findViewById(R.id.img_count_tv);
				}
				if (0 < images.size()) {
					// System.out.println("0<images.size()===" + images.size());
					if (null == mImageAdapter) {
						mImageAdapter = new ImageAdapter(NativeModeActivity.this, images);
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
				vTopBar.setVisibility(View.VISIBLE);
			}
		});
		// 目录判断结束
		new OrderTask().execute(); 
	}

	// 小组差
	private ArrayList<String> diffList = new ArrayList<String>();
	// 大组差
	private ArrayList<String> qDiffList = new ArrayList<String>();
	// 每组的集合顺序
	private static HashMap<Integer, ArrayList<String>> orderMap = new HashMap<Integer, ArrayList<String>>();
	// 手动随机
	private static HashMap<Integer, TempGroup> dialogMap = new HashMap<Integer, TempGroup>();
	// 题组随机标识符
	int tempGroupRow = 0;
	// 记录有几个大组
	int groupSize = 0;
	// 为手动循环暂存的每组的集合顺序
	private static HashMap<Integer, ArrayList<String>> tempOrderMap = new HashMap<Integer, ArrayList<String>>();
	// 选择了几次
	private int choiceNum = -1;
	// 第一次的排序
	private ArrayList<String> allGroupOrder;
	// 第一次的排序位置
	private int firstGroupOrder = 0;
	// 最后一组的位置
	private int lastGroupOrder = 0;
	// 手动已经循环了几次
	private int handGroupNum = 0;
	// 记录每一次手动随机的位置。就是dialogMap key值
	private ArrayList<Integer> handList = new ArrayList<Integer>();
	// 每一次选择前的临时记录。
	private static HashMap<Integer, TempGroup> preDialogMap = new HashMap<Integer, TempGroup>();
	// 有几个手动循环组
	private int handGroupSum = 0;
	// 是否循环结束
	private boolean isFinishProgress;

	private final class OrderTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			/**
			 * 隐藏题目显示界面
			 */
			vVisit.setVisibility(View.GONE);
			globleProgress.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			/**
			 * 获取本答卷中所有的问题
			 */
			qs = ma.dbService.getQuestionList(feed.getSurveyId());
			Log.i("zrl1", qs + "");
			
			// List<QGroup> qlist=new ArrayList<QGroup>();
			// 记录上一组的最后一个位置的数字
			int lastTemp = -1;
			// 记录上一大组最后一个位置的数字
			int bigLastTemp = -1;
			// 记录大组走的轨迹
			int x = 0;
			for (Question question : qs) {
				QGroup qGroup = question.getGroup();
				// 大组不为空
				if (null != qGroup) {
					//isQgroupOrLogic = true;
					if (x == 0) {
						firstGroupOrder = Integer.parseInt(qGroup.getIndex());
					}
					x += 1;
					groupSize += 1;
					if (x > 1) {
						// 得到触发大组的第一个位置
						String firstOrder = qGroup.getIndex();
						// 每个大组之间的间距
						int bigDiff = Integer.parseInt(firstOrder) - bigLastTemp;
						// 每个大组间距的值,每个值用','隔开
						String bigDiffContinue = "";
						for (int y = 1; y < bigDiff; y++) {
							int bigDiffSum = bigLastTemp + y;
							if (y == bigDiff - 1) {
								bigDiffContinue += bigDiffSum;
							} else {
								bigDiffContinue += bigDiffSum + ",";
							}
						}
						// 加入大组间距集合中
						if (!"".equals(bigDiffContinue)) {
							// 赋给全局变量
							qDiffList.add(bigDiffContinue);
						}
					}
					ArrayList<Group> groups = qGroup.getGroups();
					for (int j = 0; j < groups.size(); j++) {
						Group tempGroup = groups.get(j);
						// 得到每个小组的顺序号
						ArrayList<Integer> tempOrder = tempGroup.getIndexArr();
						// 小组第一个位置
						int first = tempOrder.get(0);
						// 小组最后一个位置
						int last = tempOrder.get(tempOrder.size() - 1);
						// 大于1才能进行比较
						if (j > 0) {
							String diffContinue = "";
							// 相邻小组的差
							int diff = first - lastTemp;
							// 遍历 得到小组中间差值
							for (int k = 1; k < diff; k++) {
								int diffSum = lastTemp + k;
								if (k == diff - 1) {
									diffContinue += diffSum;
								} else {
									diffContinue += diffSum + ",";
								}
							}
							// 加入小组间距集合中
							if (!"".equals(diffContinue)) {
								// 赋给全局变量
								diffList.add(diffContinue);
							}
						}
						lastTemp = last;
						if (j == groups.size() - 1) {
							// 得出最后的值
							bigLastTemp = last;
						}
					}
				}
			}
			// 得到最后位置的序号
			lastGroupOrder = bigLastTemp;

			for (Question tpQuestion : qs) {
				QGroup tempQGroup = tpQuestion.getGroup();
				if (null != tempQGroup) {
					// 每一个大组中的所有小组list
					ArrayList<String> orderList = new ArrayList<String>();
					ArrayList<String> tempOrderList = new ArrayList<String>();
					ArrayList<Group> tempGroups = tempQGroup.getGroups();
					// 假如大体组随机，进行随机操作
					if (QGroup.GROUP_TYPE_AUTO == tempQGroup.getGroupType()) {
						Collections.shuffle(tempGroups);
						// 手动循环情况下
					} else if (QGroup.GROUP_TYPE_HAND == tempQGroup.getGroupType()) {
						handGroupSum++;
						// 获得大题组名字
						String bigGroupName = tempQGroup.getGroupName();
						// 获得大题组顺序号
						int bigOrder = tempQGroup.getOrder();
						// 获得小题组的顺序号
						ArrayList<Integer> smallOrder = new ArrayList<Integer>();
						// 获得对应该小题组号的小题名字
						HashMap<Integer, String> smallGroupMap = new HashMap<Integer, String>();
						// 循环大题组
						for (int zz = 0; zz < tempGroups.size(); zz++) {
							// 获得小题组
							Group zzGroup = tempGroups.get(zz);
							// 获得小题组号
							Integer zzOrder = zzGroup.getOrder();
							// 获得小题组名字
							String zzOrderName = zzGroup.getGroupName();
							smallOrder.add(zzOrder);
							smallGroupMap.put(zzOrder, zzOrderName);
						}
						// 初始化暂存随机题组
						TempGroup tg = new TempGroup(bigGroupName, smallGroupMap, bigOrder, smallOrder);
						// 加入大集合
						dialogMap.put(Integer.parseInt(tempQGroup.getIndex()), tg);
						handList.add(Integer.parseInt(tempQGroup.getIndex()) - 1);
					}
					// 遍历小题组
					for (int i = 0; i < tempGroups.size(); i++) {
						// 获得组内序号
						Group group = tempGroups.get(i);
						// 小组循环的话，进行循环
						if (group.isRandom()) {
							Collections.shuffle(group.getIndexArr());
							group.setIndexArr(group.getIndexArr());
						}
						orderList.add(group.getIndexStr());
						tempOrderList.add(group.getIndexStr());
					}
					// 通过每个大组唯一的order号给hashMap赋值
					orderMap.put(tempQGroup.getOrder(), orderList);
					// 给暂存组赋值
					tempOrderMap.put(tempQGroup.getOrder(), tempOrderList);
				}
			}

			System.out.println("大体组个数:" + groupSize);
			allGroupOrder = Util.getAllGroupOrder(diffList, qDiffList, orderMap, groupSize);
			System.out.println("最后的排序:" + allGroupOrder);
			// 标识符，作用只添加一次集合
			boolean isOnlyOne = true;
			// System.out.println("firstGroupOrder:" + firstGroupOrder +
			// "lastGroupOrder:" + lastGroupOrder);
			for (int i = 0; i < qs.size(); i++) {
				// 得到每个题目的order顺序号
				String orderStr = i + "";
				// 假如在上一个排序组中找到顺序号了，就按照排序组的顺序号走
				if (firstGroupOrder <= i && lastGroupOrder >= i) {
					if (isOnlyOne) {
						arrList.addAll(allGroupOrder);
						isOnlyOne = false;
					}
				} else {
					// 否则就按照已有的顺序走
					arrList.add(orderStr);
				}
			}
			System.out.println("整体最后的排序:" + arrList);
			// 题组随机结束

			// 目录处理判断有没有题组随机
			// 没有题组随机才能显示
			if (!isQgroupOrLogic) {
				if (!Util.isEmpty(feed.getDirectory())) {
					String[] arrDirectory = feed.getDirectory().split(",");
					for (String strDirectory : arrDirectory) {
						dtOrderList.add(Integer.parseInt(strDirectory));
					}
					recodeOrderList.add(0);
				} else {
					dtOrderList.add(0);
					recodeOrderList.add(0);
				}
			}
			return 0 < qs.size();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			new QuestionTask().execute();
			// 目录添加布局结束
			super.onPostExecute(result);
		}
	}

	/**
	 * 考虑到问题过多会影响界面的流畅 比如有的问卷有1000多道题,此时从数据库中将这些记录查出时耗时较多, 故在子线程中完成,然后在主线程中刷新界面
	 */
	private final class QuestionTask extends AsyncTask<Void, Integer, Boolean> {
		private ProgressBar vProgress;

		@Override
		protected void onPreExecute() {
			/**
			 * 隐藏题目显示界面
			 */
			// vVisit.setVisibility(View.GONE);
			// vProgress = (ProgressBar) findViewById(R.id.visit_progressBar);
			/**
			 * 显示等待进度条
			 */
			// vProgress.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			int headNum = 0;
			Log.i("zrl1", qs + "");
			for (int i = 0; i < qs.size(); i++) {
				Question question = qs.get(i);
				if (1 == question.qCamera) {
					isHaveSingle = true;
				}
				if (Cnt.TYPE_HEADER == question.qType) {
					headNum += 1;
				}
				if (headNum > 1) {
					// 目录右侧新增
					if (Util.isEmpty(question.qid) && Cnt.TYPE_HEADER != question.qType) {
						question.qid = "Q" + (question.qOrder - headNum + 1);
					}
				} else {
					// 目录右侧新增
					if (Util.isEmpty(question.qid) && Cnt.TYPE_HEADER != question.qType) {
						question.qid = "Q" + (question.qOrder);
					}
				}
				iiMap.put(question.qIndex, i);

				// System.out.println(question.qCamera + question.qCameraName);
			}
			return 0 < qs.size();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			System.out.println("添加目录");
			// 目录添加布局
			if (!isQgroupOrLogic) {
				// System.out.println("赋值");
				// 更改的样式
				NativeModeActivity.this.menuListAdapter = new MenuListAdapter(NativeModeActivity.this, 0, qs, dtOrderList, ma, feed);
				NativeModeActivity.this.menuList.setAdapter(menuListAdapter);
			}
			/**
			 * 问卷中所有的问题查询完成后, 隐藏进度条
			 */
			// vProgress.setVisibility(View.GONE);
			globleProgress.setVisibility(View.GONE);
			isFinishProgress = true;
			nq_btn.setClickable(true);
			if (result) {
				/**
				 * 刷新界面出现答题
				 */
				initData();
				 //  IMS   皮炎湿疹   临时干预  
				imsIntervetion=new ImsIntervetion(Integer.valueOf(feed.getSurveyId()), ma, feed.getUuid()); 
			} else {
				/**
				 * 没有Android模式下的问卷, 可能问卷的zip包中没有xml形式的问卷
				 */
				Message msg = Message.obtain();
				msg.what = MSG_FINISH;
				msg.obj = getResources().getString(R.string.null_questions);
				handler.sendMessage(msg);
			}
			super.onPostExecute(result);
		}
	}

	private void initData() {
		if (null != feed) {

			/**
			 * 显示答题界面
			 */
			showVisitView();

			/**
			 * 初始化顶部控件的信息
			 */
			initBar();
			/**
			 * 新建的
			 */
			if (Util.isEmpty(feed.getName())) {
				/**
				 * 数据库中新增一条答卷信息
				 */
				newFeed(System.currentTimeMillis());
				/**
				 * recordView、cameraView 根据问卷的要求设置其显示或隐藏
				 */
				if (null != feed.getSurvey()) {
					if (1 == feed.getSurvey().globalRecord) {
						// 注释录音  
//						new RecordTask(isClicked, null).execute();
						HashMap<String, Object> params=new HashMap<String, Object>();
						params.put("feed", feed);
						params.put("content", NativeModeActivity.this);
						params.put("isClicked", isClicked);
						params.put("q", qs.get(1));
						MainService.newTask(new Task(TaskType.TS_RECORED, params));
					}
				}
				// System.out.println("新建访问FileName=" + feed.getName());
				qAnswer.uuid = feed.getUuid();
				if (!Util.isEmpty(qs)) {
					/**
					 * 问题的集合不为空, 显示第一题
					 */
					createQuestionBodyView(MSG_NEXT);
				}
			} else {
				/**
				 * 继续访问
				 */
				// System.out.println("继续访问FileName=" + feed.getName());
				qAnswer.uuid = feed.getUuid();
				feed.setStartTime(System.currentTimeMillis());
				/**
				 * recordView、cameraView 根据问卷的要求设置其显示或隐藏
				 */
				if (null != feed.getSurvey()) {
					if (1 == feed.getSurvey().globalRecord) {
						// 注释录音  
//						new RecordTask(isClicked, null).execute();
						HashMap<String, Object> params=new HashMap<String, Object>();
						params.put("feed", feed);
						params.put("content", NativeModeActivity.this);
						params.put("isClicked", isClicked);
						params.put("q", qs.get(1));
						MainService.newTask(new Task(TaskType.TS_RECORED, params));
					}
				}
				if (!Util.isEmpty(qs)) {
					createQuestionBodyView(MSG_NEXT);
				}
			}

		} else {
			// 异常情况
			return;
		}
	}

	private void showVisitView() {
		isShow = true;
		/**
		 * 显示顶部上一下、下一页、标题界面
		 */
		vTopBar.setVisibility(View.VISIBLE);

		/**
		 * 结果界面缩小隐藏
		 */
		Util.showView(NativeModeActivity.this, vResult, R.anim.zoom_out);
		vResult.setVisibility(View.GONE);
		/**
		 * 显示答题界面
		 */
		vVisit.setVisibility(View.VISIBLE);
		re_btn.setVisibility(View.VISIBLE);
		/**
		 * 答题界面放大显示
		 */
		Util.showView(NativeModeActivity.this, vVisit, R.anim.zoom_in);
		/**
		 * 显示底部拍照、录音、经纬度
		 */
		// vBottomBar.setVisibility(View.VISIBLE);
		// z1加的
		sdImages.setVisibility(View.GONE);
		// 目录控制按钮
		scrollView.isScorll = 0;
	}

	private void showResultView() {
		re_btn.setVisibility(View.GONE);
		vTopBar.setVisibility(View.GONE);
		Util.showView(NativeModeActivity.this, vVisit, R.anim.zoom_out);
		vVisit.setVisibility(View.GONE);
		vResult.setVisibility(View.VISIBLE);
		Util.showView(NativeModeActivity.this, vResult, R.anim.zoom_in);
		// vBottomBar.setVisibility(View.GONE);
		// z1加的
		sdImages.setVisibility(View.GONE);
		if (null != miPhoto) {
			miPhoto.setTitle(this.getString(R.string.open_photo));
		}
		// 单复选矩阵固定
		rlQuestion_new.setVisibility(View.GONE);
		// 目录控制按钮
		scrollView.isScorll = 1;
	}

	/**
	 * 初始化并显示顶部、底部Bar的数据信息
	 */
	private void initBar() {
		if (!Util.isEmpty(feed.getSurveyTitle())) {
			// 大树 修改
			tvBar.setText(feed.getSurveyTitle() + "(1/" + qs.size() + ")");
		}
	}

	private void newFeed(long start) {
		feed.setIsCompleted(0);
		feed.setFeedId("0");
		feed.setManyPlaces(null);
		feed.setManyTimes(null);
		feed.setSpent(0);
		feed.setLotsCoord(null);
		feed.setSize(0);
		feed.setPath(null);
		feed.setName(null);
		String uuid = UUID.randomUUID().toString();
		feed.setUuid(uuid);
		// 增加pid
		feed.setPid("0");
		// 命名规则
		String name = Util.getXmlName(feed.getUserId(), feed.getSurveyId(), feed.getUuid(), feed.getPid(), feed.getParametersContent());
		feed.setName(name);// System.nanoTime()
		feed.setCreateTime(start);
		feed.setStartTime(start);
		feed.setReturnType("0");
		feed.setType(Cnt.FILE_TYPE_XML);
		String path = Util.getXmlPath(NativeModeActivity.this, feed.getSurveyId());
		feed.setPath(path);
		if(Util.isEmpty(feed.getUserId())){
			feed.setUserId(ma.cfg.getString(Cnt.USER_ID, ""));
		}
		ma.dbService.addUploadFeed(feed);
	}

	@Override
	protected void onStart() {
		refreshView();
		super.onStart();
	}

	public void btnClick(View v) {
		if (Util.validateSyncClick()) {
			Toasts.makeText(NativeModeActivity.this, R.string.too_frequently, Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		case R.id.my_setting:
			super.openOptionsMenu();
			break;
		case R.id.right_btn:
			NativeModeActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.left_btn:
			showVisitView();
			break;
		// 上一页
		case R.id.bq_btn:
			backPage();
			break;
		// 下一页
		case R.id.nq_btn:
			// 哑题封装
			nextPage(false);
			break;
		}
	}

	/**
	 * 哑题返回上一页
	 */
	private void backPage() {
	
		if (0 == index) {
			return;
		}
		globleProgress.setVisibility(View.VISIBLE);
		setTopClick(false);
		index--;
		Collections.sort(handList);
		// 包含这个index.说明返回了一层，这种触发重新分配手动循环位置事件
		if (handList.contains(index)) {
			// 先得到原先位置的实体
			choiceNum -= 1;
			handGroupNum -= 1;
			// 通过预选项得到原先这个位置的 暂存组
			TempGroup tempGroup = preDialogMap.get(index + 1);
			// 如果已经有的事件小于总手动随机数目,并且现在choiceNum没值
			if (dialogMap.size() < handGroupSum && -2 == choiceNum) {
				// choiceNum等于 改组集合顺序的个数-2。就为上一组题组随机触发事件的最后位置。
				choiceNum = orderMap.get(tempGroup.getBigOrder()).size() - 2;
				// System.out.println("choiceNum:"+choiceNum);
			} else {
				// 首先得到储存 手动集合位置
				int tempIndexOf = handList.indexOf(index);
				// 移除掉上一组触发事件的 map
				dialogMap.remove(handList.get(tempIndexOf + 1) + 1);
				// 移除上一次点下一题组的位置
				handList.remove(tempIndexOf + 1);
			}
			// 移除这一组保存的数据
			preDialogMap.remove(index + 1);
			// 给这一组赋值
			dialogMap.put(index + 1, tempGroup);
			// System.out.println("dialogMap:"+dialogMap.size());
			// System.out.println(dialogMap.get(index+1).toString());
			System.out.println("外部choiceNum:" + choiceNum);
			// System.out.println("handList:"+handList);
			// System.out.println("preDialogMap:"+preDialogMap.size());

			/**
			 * 重新分配位置 0@3|0@4
			 */
		}
		// 上一页题组随机
		bodyView.removeAllViews();
		// 单复选矩阵固定
		bodyView_new.removeAllViews();
		vs.clear();
		createQuestionBodyView(MSG_PRE);
	}
	/**
	 * 单选哑题
	 * @param item
	 * @param rb
	 * @param isDumbOk
	 * @param operType
	 */
	private HashMap<String, Object> setDumbRb(QuestionItem item, RadioButton rb,boolean dumbOk, int operType){
		QuestionItem iTem=item;
		boolean isDumbOk=dumbOk;
		HashMap<String, Object> map=new HashMap<String,Object>();
			// 假如是哑题就清空
			if (!isDumbOk) {
				String dumbList = iTem.dumbList;
				// 5,2:3:4 第一种题型
				// Region,1,北京 受访者属性题型 不包含为 1 包含为2
				// 1:0,=,北京 单行文本题型 符号 >、>= 、<、=、!=
				// 哑题的List串不为空
				if (!Util.isEmpty(dumbList)) {
					if (dumbList.indexOf(",") != -1) {
						if (dumbList.split(",").length > 2) {
							// 每项有两个逗号
							if (dumbList.indexOf("|") != -1) {
								// 文本题或的关系
								// 1:0,=,北京|1:0,=,天津 单行文本题型 符号
								// >、>=
								// 、<、=、!=
								// 受访者属性关系
								// Company,2,北京|Company,=,北京
								String[] twoCondition = dumbList.split("\\|");
								String firstCondition = twoCondition[0];
								String secondCondition = twoCondition[1];
								String[] dumbFirstArr = firstCondition.split(",");
								String[] dumbSecondArr = secondCondition.split(",");
								if (dumbFirstArr.length == 3) {
									boolean isOneOk = false;
									boolean isTwoOk = false;
									String[] dumbOneFirstArr = dumbFirstArr[0].split(":");
									String[] dumbOneSecondArr = dumbSecondArr[0].split(":");
									if (dumbOneFirstArr.length == 2 && dumbOneSecondArr.length == 2) {
										// 第一个条件
										String textIndex = dumbOneFirstArr[0];// 引用的题目index
										String textItemOrder = dumbOneFirstArr[1];// 引用的位置
										if (!Util.isEmpty(textIndex) && !Util.isEmpty(textItemOrder)) {
											Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid,
													textIndex);
											// 存取值的list
											ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
											if (null != textAnswer) {
												amTextList = textAnswer.getAnswerMapArr();
											}
											// 假如存取值的List不为空
											if (!Util.isEmpty(amTextList)) {
												AnswerMap answerMap = amTextList
														.get(Integer.parseInt(textItemOrder));
												String answerText = answerMap.getAnswerText();
												// 哑题改动
												Question question = ma.dbService
														.getQuestion(feed.getSurveyId(), textIndex);
												int type = -1;
												if (null != question) {
													ArrayList<QuestionItem> colItemArr = question
															.getColItemArr();
													if (!Util.isEmpty(colItemArr)) {
														QuestionItem questionItem = colItemArr
																.get(Integer.parseInt(textItemOrder));
														if (null != questionItem) {
															type = questionItem.getType();
														}
													}
												}
												if (-1 != type) {
													dumbFirstArr[1] = dumbFirstArr[1] + ":" + type;
												}
												// 哑题改动匹配成功
												if (Util.isPass(answerText, dumbFirstArr[1],
														dumbFirstArr[2])) {
													isOneOk = true;
												} else {
													// 不满足什么都不做
												}
											} else {
												// 答案为空什么也不做
											}
										} else {
											// 服务器传的字符串为空什么也不做
										}

										// 第二个条件
										String textTwoIndex = dumbOneSecondArr[0];// 引用的index
										String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
										if (!Util.isEmpty(textTwoIndex)
												&& !Util.isEmpty(textTwoItemOrder)) {
											Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid,
													textTwoIndex);
											// 存取值的list
											ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
											if (null != textAnswer) {
												amTextList = textAnswer.getAnswerMapArr();
											}
											// 假如存取值的List不为空
											if (!Util.isEmpty(amTextList)) {
												AnswerMap answerMap = amTextList
														.get(Integer.parseInt(textTwoItemOrder));
												String answerText = answerMap.getAnswerText();
												// 哑题改动
												Question question = ma.dbService
														.getQuestion(feed.getSurveyId(), textIndex);
												int type = -1;
												if (null != question) {
													ArrayList<QuestionItem> colItemArr = question
															.getColItemArr();
													if (!Util.isEmpty(colItemArr)) {
														QuestionItem questionItem = colItemArr
																.get(Integer.parseInt(textItemOrder));
														if (null != questionItem) {
															type = questionItem.getType();
														}
													}
												}
												if (-1 != type) {
													dumbSecondArr[1] = dumbSecondArr[1] + ":" + type;
												}
												// 哑题改动
												if (Util.isPass(answerText, dumbSecondArr[1],
														dumbSecondArr[2])) {
													isTwoOk = true;
												} else {
													// 不满足什么都不做
												}
											} else {
												// 答案为空什么也不做
											}
										} else {
											// 服务器传的字符串为空什么也不做
										}
										// 两者 满足一个
										if (isOneOk || isTwoOk) {
											// 给清空答案
											// System.out.println("清空答案1");
											// 只有下一页才清空
											if (MSG_NEXT == operType) {
												ma.dbService.updateAnswer2Null(tempAnswer);
											}
											iTem.isDumbOk = true;
											// 给rb预设选中
											rb.setChecked(true);
											rb.setTag(iTem);
											// 设为OK证明此哑题
											isDumbOk = true;
										}
									} else if (dumbOneFirstArr.length == 1
											&& dumbOneSecondArr.length == 1) {
										/**
										 * 引用受访者参数
										 */
										String parametersStr = feed.getParametersStr();
										ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
										if (!Util.isEmpty(parametersStr)) {
											parameterList.clear();
											ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
													.parseArray(parametersStr, Parameter.class);
											if (!Util.isEmpty(tParameters)) {
												parameterList.addAll(tParameters);
											}
										}
										String content = "";
										if (null != parameterList && parameterList.size() > 0) {
											for (Parameter parameter : parameterList) {
												if (parameter.getSid().equals(dumbFirstArr[0])) {
													System.out.println("content:" + content);
													content = parameter.getContent();
													break;
												}
											}
										} else {
											content = "";
										}
										// 假如查到content了，在判断是什么符号
										if (!Util.isEmpty(content)) {
											// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
											// 假如是数字
											if (Util.isFormat(dumbFirstArr[2], 9)
													&& Util.isFormat(content, 9)
													&& !Util.isFormat(dumbFirstArr[1], 9)) {
												if (Util.isMath(content, dumbFirstArr[1],
														dumbFirstArr[2])) {
													isOneOk = true;
												}
											}
											// 日期
											else if (dumbFirstArr[1].indexOf("-") != -1
													&& dumbFirstArr[1].length() > 5) {
												try {
													if (Util.getDateCompare(dumbFirstArr[2], content,
															dumbFirstArr[1])) {
														isOneOk = true;
													}
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
											// 文本格式
											else {
												if (Util.isRespondentsMatching(content, dumbFirstArr[1],
														dumbFirstArr[2])) {
													isOneOk = true;
												}
											}
										}

										if (null != parameterList && parameterList.size() > 0) {
											for (Parameter parameter : parameterList) {
												if (parameter.getSid().equals(dumbSecondArr[0])) {
													content = parameter.getContent();
													break;
												}
											}
										} else {
											content = "";
										}

										// 假如查到content了，在判断是什么符号
										if (!Util.isEmpty(content)) {
											// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
											// 假如是数字
											if (Util.isFormat(dumbSecondArr[2], 9)
													&& Util.isFormat(content, 9)
													&& !Util.isFormat(dumbSecondArr[1], 9)) {
												if (Util.isMath(content, dumbSecondArr[1],
														dumbSecondArr[2])) {
													isTwoOk = true;
												}
											}
											// 日期
											else if (dumbSecondArr[1].indexOf("-") != -1
													&& dumbSecondArr[1].length() > 5) {
												try {
													if (Util.getDateCompare(dumbSecondArr[2], content,
															dumbSecondArr[1])) {
														isTwoOk = true;
													}
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
											// 文本格式
											else {
												if (Util.isRespondentsMatching(content,
														dumbSecondArr[1], dumbSecondArr[2])) {
													isTwoOk = true;
												}
											}
										}

										// 两者 满足一个
										if (isOneOk || isTwoOk) {
											// 给清空答案
											// System.out.println("清空答案1");
											// 只有下一页才清空
											if (MSG_NEXT == operType) {
												ma.dbService.updateAnswer2Null(tempAnswer);
											}
											iTem.isDumbOk = true;
											// 给rb预设选中
											rb.setChecked(true);
											rb.setTag(iTem);
											// 设为OK证明此哑题
											isDumbOk = true;
										}
									}
								}
							} else if (dumbList.indexOf("&") != -1) {
								// 文本题与的关系
								// 1:0,=,北京&1:0,=,天津 单行文本题型 符号
								// >、>=
								// 、<、=、!=
								String[] twoCondition = dumbList.split("&");
								String firstCondition = twoCondition[0];
								String secondCondition = twoCondition[1];
								String[] dumbFirstArr = firstCondition.split(",");
								String[] dumbSecondArr = secondCondition.split(",");
								boolean isOneOk = false;
								boolean isTwoOk = false;
								String[] dumbOneFirstArr = dumbFirstArr[0].split(":");// 题号和答案下标
								String[] dumbOneSecondArr = dumbSecondArr[0].split(":");// 题号和答案下标
								if (dumbOneFirstArr.length == 2 && dumbOneSecondArr.length == 2) {
									// 第一个条件
									String textIndex = dumbOneFirstArr[0];// 引用的index
									String textItemOrder = dumbOneFirstArr[1];// 引用的位置
									if (!Util.isEmpty(textIndex) && !Util.isEmpty(textItemOrder)) {
										Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid,
												textIndex);
										// 存取值的list
										ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
										if (null != textAnswer) {
											amTextList = textAnswer.getAnswerMapArr();
										}
										// 假如存取值的List不为空
										if (!Util.isEmpty(amTextList)) {
											AnswerMap answerMap = amTextList
													.get(Integer.parseInt(textItemOrder));
											String answerText = answerMap.getAnswerText();
											// 哑题改动
											Question question = ma.dbService
													.getQuestion(feed.getSurveyId(), textIndex);
											int type = -1;
											if (null != question) {
												ArrayList<QuestionItem> colItemArr = question
														.getColItemArr();
												if (!Util.isEmpty(colItemArr)) {
													QuestionItem questionItem = colItemArr
															.get(Integer.parseInt(textItemOrder));
													if (null != questionItem) {
														type = questionItem.getType();
													}
												}
											}
											if (-1 != type) {
												dumbFirstArr[1] = dumbFirstArr[1] + ":" + type;
											}
											// 哑题改动
											if (Util.isPass(answerText, dumbFirstArr[1],
													dumbFirstArr[2])) {
												isOneOk = true;
											} else {
												// 不满足什么都不做
											}
										} else {
											// 答案为空什么也不做
										}
									} else {
										// 服务器传的字符串为空什么也不做
									}

									// 第二个条件
									String textTwoIndex = dumbOneSecondArr[0];// 引用的index
									String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
									if (!Util.isEmpty(textTwoIndex)
											&& !Util.isEmpty(textTwoItemOrder)) {
										Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid,
												textTwoIndex);
										// 存取值的list
										ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
										if (null != textAnswer) {
											amTextList = textAnswer.getAnswerMapArr();
										}
										// 假如存取值的List不为空
										if (!Util.isEmpty(amTextList)) {
											AnswerMap answerMap = amTextList
													.get(Integer.parseInt(textTwoItemOrder));
											String answerText = answerMap.getAnswerText();
											// 哑题改动
											Question question = ma.dbService
													.getQuestion(feed.getSurveyId(), textIndex);
											int type = -1;
											if (null != question) {
												ArrayList<QuestionItem> colItemArr = question
														.getColItemArr();
												if (!Util.isEmpty(colItemArr)) {
													QuestionItem questionItem = colItemArr
															.get(Integer.parseInt(textItemOrder));
													if (null != questionItem) {
														type = questionItem.getType();
													}
												}
											}
											if (-1 != type) {
												dumbSecondArr[1] = dumbSecondArr[1] + ":" + type;
											}
											// 哑题改动
											if (Util.isPass(answerText, dumbSecondArr[1],
													dumbSecondArr[2])) {
												isTwoOk = true;
											} else {
												// 不满足什么都不做
											}
										} else {
											// 答案为空什么也不做
										}
									} else {
										// 服务器传的字符串为空什么也不做
									}
									// 两者 满足一个
									if (isOneOk && isTwoOk) {
										// 给清空答案
										System.out.println("清空答案1");
										// 只有下一页才清空
										if (MSG_NEXT == operType) {
											ma.dbService.updateAnswer2Null(tempAnswer);
										}
										iTem.isDumbOk = true;
										// 给rb预设选中
										rb.setChecked(true);
										rb.setTag(iTem);
										// 设为OK证明此哑题
										isDumbOk = true;
									}
								} else if (dumbOneFirstArr.length == 1
										&& dumbOneSecondArr.length == 1) {
									// 受访者属性题目

									/**
									 * 引用受访者参数
									 */
									String parametersStr = feed.getParametersStr();
									ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
									if (!Util.isEmpty(parametersStr)) {
										parameterList.clear();
										ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
												.parseArray(parametersStr, Parameter.class);
										if (!Util.isEmpty(tParameters)) {
											parameterList.addAll(tParameters);
										}
									}
									String content = "";
									if (null != parameterList && parameterList.size() > 0) {
										for (Parameter parameter : parameterList) {
											if (parameter.getSid().equals(dumbFirstArr[0])) {
												content = parameter.getContent();
												break;
											}
										}
									} else {
										content = "";
									}
									// 假如查到content了，在判断是什么符号
									if (!Util.isEmpty(content)) {
										// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
										// 假如是数字
										if (Util.isFormat(dumbFirstArr[2], 9)
												&& Util.isFormat(content, 9)
												&& !Util.isFormat(dumbFirstArr[1], 9)) {
											if (Util.isMath(content, dumbFirstArr[1],
													dumbFirstArr[2])) {
												isOneOk = true;
											}
										}
										// 日期
										else if (dumbFirstArr[1].indexOf("-") != -1
												&& dumbFirstArr[1].length() > 5) {
											try {
												if (Util.getDateCompare(dumbFirstArr[2], content,
														dumbFirstArr[1])) {
													isOneOk = true;
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}
										}
										// 文本格式
										else {
											if (Util.isRespondentsMatching(content, dumbFirstArr[1],
													dumbFirstArr[2])) {
												isOneOk = true;
											}
										}
									}

									if (null != parameterList && parameterList.size() > 0) {
										for (Parameter parameter : parameterList) {
											if (parameter.getSid().equals(dumbSecondArr[0])) {
												content = parameter.getContent();
												break;
											}
										}
									} else {
										content = "";
									}

									// 假如查到content了，在判断是什么符号
									if (!Util.isEmpty(content)) {
										// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
										// 假如是数字
										if (Util.isFormat(dumbSecondArr[2], 9)
												&& Util.isFormat(content, 9)
												&& !Util.isFormat(dumbSecondArr[1], 9)) {
											if (Util.isMath(content, dumbSecondArr[1],
													dumbSecondArr[2])) {
												isTwoOk = true;
											}
										}
										// 日期
										else if (dumbSecondArr[1].indexOf("-") != -1
												&& dumbSecondArr[1].length() > 5) {
											try {
												if (Util.getDateCompare(dumbSecondArr[2], content,
														dumbSecondArr[1])) {
													isTwoOk = true;
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}
										}
										// 文本格式
										else {
											if (Util.isRespondentsMatching(content, dumbSecondArr[1],
													dumbSecondArr[2])) {
												isTwoOk = true;
											}
										}
									}

									// 两者 都满足
									if (isOneOk && isTwoOk) {
										// 给清空答案
										// System.out.println("清空答案1");
										// 只有下一页才清空
										if (MSG_NEXT == operType) {
											ma.dbService.updateAnswer2Null(tempAnswer);
										}
										iTem.isDumbOk = true;
										// 给rb预设选中
										rb.setChecked(true);
										rb.setTag(iTem);
										// 设为OK证明此哑题
										isDumbOk = true;
									}

								}
							} else {
								String[] dumbArr = dumbList.split(",");
								// Company,2,北京 受访者属性题型 不包含为 1
								// 包含为2
								// 1:0,=,北京 单行文本题型
								String dumbOneStr = dumbArr[0];
								String[] dumbOneArr = dumbOneStr.split(":");
								// 1为受访者属性题型
								if (dumbOneArr.length == 1) {
									/**
									 * 引用受访者参数
									 */
									String parametersStr = feed.getParametersStr();
									ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
									if (!Util.isEmpty(parametersStr)) {
										parameterList.clear();
										ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
												.parseArray(parametersStr, Parameter.class);
										if (!Util.isEmpty(tParameters)) {
											parameterList.addAll(tParameters);
										}
									}
									String content = "";
									if (null != parameterList && parameterList.size() > 0) {
										for (Parameter parameter : parameterList) {
											if (parameter.getSid().equals(dumbArr[0])) {
												content = parameter.getContent();
												break;
											}
										}
									} else {
										content = "";
									}
									// System.out.println("0:"+content+"1:"+dumbArr[1]+"2:"+dumbArr[2]);
									// 假如查到content了，在判断是什么符号
									if (!Util.isEmpty(content)) {
										// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
										// 假如是数字
										if (Util.isFormat(dumbArr[2], 9) && Util.isFormat(content, 9)
												&& !Util.isFormat(dumbArr[1], 9)) {
											if (Util.isMath(content, dumbArr[1], dumbArr[2])) {
												// 给清空答案
												System.out.println("清空答案2");
												// 只有下一页才清空
												if (MSG_NEXT == operType) {
													ma.dbService.updateAnswer2Null(tempAnswer);
												}
												iTem.isDumbOk = true;
												// 给rb预设选中
												rb.setChecked(true);
												rb.setTag(iTem);
												// 设为OK证明此哑题
												isDumbOk = true;
											}
										}
										// 日期
										else if (dumbArr[1].indexOf("-") != -1
												&& dumbArr[1].length() > 5) {
											try {
												if (Util.getDateCompare(dumbArr[2], content,
														dumbArr[1])) {
													// 给清空答案
													System.out.println("清空答案2");
													// 只有下一页才清空
													if (MSG_NEXT == operType) {
														ma.dbService.updateAnswer2Null(tempAnswer);
													}
													iTem.isDumbOk = true;
													// 给rb预设选中
													rb.setChecked(true);
													rb.setTag(iTem);
													// 设为OK证明此哑题
													isDumbOk = true;
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}
										}
										// 文本格式
										else {
											if (Util.isRespondentsMatching(content, dumbArr[1],
													dumbArr[2])) {
												// 给清空答案
												System.out.println("清空答案2");
												// 只有下一页才清空
												if (MSG_NEXT == operType) {
													ma.dbService.updateAnswer2Null(tempAnswer);
												}
												iTem.isDumbOk = true;
												// 给rb预设选中
												rb.setChecked(true);
												rb.setTag(iTem);
												// 设为OK证明此哑题
												isDumbOk = true;
											}
										}
									}
								}
								// 2为单行文本题型 ,单选,复选,单选矩阵
								// 格式一样(1:0,=,北京)
								else if (dumbOneArr.length == 2) {
									// 1:0,=,北京 单行文本题型 符号 >、>=
									// 、<、=、!=
									String textIndex = dumbOneArr[0];// 引用的index
									String textItemOrder = dumbOneArr[1];// 引用的位置
									if (!Util.isEmpty(textIndex) && !Util.isEmpty(textItemOrder)) {
										Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid,
												textIndex);
										// 存取值的list
										ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
										if (null != textAnswer) {
											amTextList = textAnswer.getAnswerMapArr();
										}
										// 假如存取值的List不为空
										if (!Util.isEmpty(amTextList)) {
											AnswerMap answerMap = amTextList
													.get(Integer.parseInt(textItemOrder));
											String answerText = answerMap.getAnswerText();
											// 哑题改动
											Question question = ma.dbService
													.getQuestion(feed.getSurveyId(), textIndex);
											int type = -1;
											if (null != question) {
												ArrayList<QuestionItem> colItemArr = question
														.getColItemArr();
												if (!Util.isEmpty(colItemArr)) {
													QuestionItem questionItem = colItemArr
															.get(Integer.parseInt(textItemOrder));
													if (null != questionItem) {
														// 单行文本题的数据类型
														type = questionItem.getType();
													}
												}
											}
											if (-1 != type) {
												dumbArr[1] = dumbArr[1] + ":" + type;
											}
											// 哑题改动 该选项是否满足条件
											if (Util.isPass(answerText, dumbArr[1], dumbArr[2])) {
												// 给清空答案
												System.out.println("清空答案3");
												// 只有下一页才清空
												if (MSG_NEXT == operType) {
													ma.dbService.updateAnswer2Null(tempAnswer);
												}
												iTem.isDumbOk = true;
												// 给rb预设选中
												rb.setChecked(true);
												rb.setTag(iTem);
												// 设为OK证明此哑题
												isDumbOk = true;
											} else {
												// 不满足什么都不做
											}
										} else {
											// 答案为空什么也不做
										}
									} else {
										// 服务器传的字符串为空什么也不做
									}
								}
							}
						} else {
							// 只有一个逗号
							// 单选、复选： dumbList只有一个逗号
							// >>规则：（index,itemvalue:itemvalue）
							String[] dumbArr = dumbList.split(",");
							// 哑题有引用的index和value值。
							// 5,2:3:4 第一种题型 (满足2:3:4中的一项即可)
							if (dumbArr.length == 2) {
								String dumbIndex = dumbArr[0];// index
								String dumbValueStr = dumbArr[1];// value
								// 引用的那道哑题的答案
								Answer dumbAnswer = ma.dbService.getAnswer(qAnswer.uuid, dumbIndex);
								// 存取值的list
								ArrayList<AnswerMap> dumbAmList = new ArrayList<AnswerMap>();
								if (null != dumbAnswer) {
									dumbAmList = dumbAnswer.getAnswerMapArr();
								}
								// 假如存取值的List不为空
								if (!Util.isEmpty(dumbAmList)) {
									String[] dumbValueArr = dumbValueStr.split(":");// 数组
									// 循环储存的值
									for (AnswerMap dumbAm : dumbAmList) {
										String dumbValue = dumbAm.getAnswerValue();// 原有的值
										System.out.println("上一道的值:" + dumbValue);
										// 假如答案和引用的value值相同
										for (String str : dumbValueArr) {
											System.out.println("本道逻辑可跳的值:" + str);
										}
										if (Util.isHave(dumbValueArr, dumbValue)) {
											// 给清空答案
											System.out.println("清空答案1");
											// 只有下一页才清空
											if (MSG_NEXT == operType) {
												ma.dbService.updateAnswer2Null(tempAnswer);
											}
											iTem.isDumbOk = true;
											// 给rb预设选中
											rb.setChecked(true);
											rb.setTag(iTem);
											// 设为OK证明此哑题
											isDumbOk = true;
											break;
										} else {

										}
									}
								} else {
									// 为空,什么都不做。
								}
							}
						}
					} else {
						// 单复选矩阵题
						// index:rowvalue:cloumnvalue:cloumnvalue|rowvalue:cloumnvalue:cloumnvalue|..
						// 第一个:所在位置
						int firstStr = dumbList.indexOf(":");
						String dumbIndex = dumbList.substring(0, firstStr);// 所引用题的index
						String dumbValueArr = dumbList.substring(firstStr + 1);// 引用的条件
						// 引用的那道哑题的答案
						Answer dumbAnswer = ma.dbService.getAnswer(qAnswer.uuid, dumbIndex);
						// 存取值的list
						ArrayList<AnswerMap> dumbAmList = new ArrayList<AnswerMap>();
						if (null != dumbAnswer) {
							dumbAmList = dumbAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(dumbAmList)) {
							if (dumbValueArr.indexOf("|") != -1) {
								String[] dumbValueStr = dumbValueArr.split("\\|");// 矩阵每行的条件
								// 循环储存的值
								for (int aw = 0; aw < dumbAmList.size(); aw++) {
									int firstValueStr = dumbValueStr[aw].indexOf(":");
									String rowvalue = dumbValueStr[aw].substring(0, firstValueStr);// 所引用题的行
									String cloumnvalue = dumbValueStr[aw].substring(firstValueStr + 1);// 引用的条件
									String[]dumbValueAry =cloumnvalue.split(":");
									AnswerMap dumbAm = dumbAmList.get(Integer.parseInt(rowvalue));
									String dumbValue = dumbAm.getAnswerValue();// 原有的值
									System.out.println("上一道的值:" + dumbValue);
									// 假如答案和引用的value值相同
									for (String str : dumbValueAry) {
										System.out.println("本道逻辑可跳的值:" + str);
									}
									if (Util.isHave(dumbValueAry, dumbValue)) {
										// 给清空答案
										System.out.println("清空答案1");
										// 只有下一页才清空
										if (MSG_NEXT == operType) {
											ma.dbService.updateAnswer2Null(tempAnswer);
										}
										iTem.isDumbOk = true;
										// 给rb预设选中
										rb.setChecked(true);
										rb.setTag(iTem);
										// 设为OK证明此哑题
										isDumbOk = true;
										break;
									} else {

									}

								}
							} else {
									int firstValueStr = dumbValueArr.indexOf(":");
									String rowvalue = dumbValueArr.substring(0, firstValueStr);// 所引用题的行
									String cloumnvalue = dumbValueArr.substring(firstValueStr + 1);// 引用的条件
									String[]dumbValueAry =cloumnvalue.split(":");
									AnswerMap dumbAm = dumbAmList.get(Integer.parseInt(rowvalue));
									String dumbValue = dumbAm.getAnswerValue();// 原有的值
									System.out.println("上一道的值:" + dumbValue);
									
									// 假如答案和引用的value值相同
									for (String str : dumbValueAry) {
										System.out.println("本道逻辑可跳的值:" + str);
									}
									if (Util.isHave(dumbValueAry, dumbValue)) {
										// 给清空答案
										System.out.println("清空答案1");
										// 只有下一页才清空
										if (MSG_NEXT == operType) {
											ma.dbService.updateAnswer2Null(tempAnswer);
										}
										iTem.isDumbOk = true;
										// 给rb预设选中
										rb.setChecked(true);
										rb.setTag(iTem);
										// 设为OK证明此哑题
										isDumbOk = true;
									} else {

									}
								// 为空,什么都不做。
							}
						}

				}
			}

		}
		map.put("item", iTem);
		map.put("isDumbOk", isDumbOk);
		return map;
	}

	/**
	 * 隐藏选项
	 * 
	 * @param item
	 * @param rb
	 */
	private void setHideRb(QuestionItem item, View rb){
		if (!Util.isEmpty(item.hideList)) {
			String hideList = item.hideList;
			System.out.println("hideList:" + hideList);
			// 5,2:3:4 第一种题型
			// Region,1,北京 受访者属性题型 不包含为 1 包含为2
			// 1:0,=,北京 单行文本题型 符号 >、>= 、<、=、!=
			// 哑题的List串不为空
			if (!Util.isEmpty(hideList)) {
				if (hideList.indexOf("|") != -1) {
					// 文本题或的关系
					// 1:0,=,北京|1:0,=,天津 单行文本题型 符号 >、>=
					// 、<、=、!=
					// 受访者属性关系
					// Company,2,北京|Company,=,北京
					String[] twoCondition = hideList.split("\\|");
					String firstCondition = twoCondition[0];
					String secondCondition = twoCondition[1];
					String[] dumbFirstArr = firstCondition.split(",");
					String[] dumbSecondArr = secondCondition.split(",");

					boolean isOneOk = false;
					boolean isTwoOk = false;
					String[] dumbOneFirstArr = dumbFirstArr[0].split(":");
					String[] dumbOneSecondArr = dumbSecondArr[0].split(":");
					if (dumbOneFirstArr.length == 2 && dumbOneSecondArr.length == 2) {
						// 第一个条件
						String textIndex = dumbOneFirstArr[0];// 引用的index
						String textItemOrder = dumbOneFirstArr[1];// 引用的位置
						if (!Util.isEmpty(textIndex) && !Util.isEmpty(textItemOrder)) {
							Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid, textIndex);
							// 存取值的list
							ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
							if (null != textAnswer) {
								amTextList = textAnswer.getAnswerMapArr();
							}
							// 假如存取值的List不为空
							if (!Util.isEmpty(amTextList)) {
								AnswerMap answerMap = amTextList.get(Integer.parseInt(textItemOrder));
								String answerText = answerMap.getAnswerText();
								// 哑题改动
								Question question = ma.dbService.getQuestion(feed.getSurveyId(),
										textIndex);
								int type = -1;
								if (null != question) {
									ArrayList<QuestionItem> colItemArr = question.getColItemArr();
									if (!Util.isEmpty(colItemArr)) {
										QuestionItem questionItem = colItemArr
												.get(Integer.parseInt(textItemOrder));
										if (null != questionItem) {
											type = questionItem.getType();
										}
									}
								}
								if (-1 != type) {
									dumbFirstArr[1] = dumbFirstArr[1] + ":" + type;
								}
								// 哑题改动

								if (Util.isPass(answerText, dumbFirstArr[1], dumbFirstArr[2])) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								// 答案为空什么也不做
							}
						} else {
							// 服务器传的字符串为空什么也不做
						}

						// 第二个条件
						String textTwoIndex = dumbOneSecondArr[0];// 引用的index
						String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
						if (!Util.isEmpty(textTwoIndex) && !Util.isEmpty(textTwoItemOrder)) {
							Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid, textTwoIndex);
							// 存取值的list
							ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
							if (null != textAnswer) {
								amTextList = textAnswer.getAnswerMapArr();
							}
							// 假如存取值的List不为空
							if (!Util.isEmpty(amTextList)) {
								AnswerMap answerMap = amTextList
										.get(Integer.parseInt(textTwoItemOrder));
								String answerText = answerMap.getAnswerText();
								// 哑题改动
								Question question = ma.dbService.getQuestion(feed.getSurveyId(),
										textIndex);
								int type = -1;
								if (null != question) {
									ArrayList<QuestionItem> colItemArr = question.getColItemArr();
									if (!Util.isEmpty(colItemArr)) {
										QuestionItem questionItem = colItemArr
												.get(Integer.parseInt(textItemOrder));
										if (null != questionItem) {
											type = questionItem.getType();
										}
									}
								}
								if (-1 != type) {
									dumbSecondArr[1] = dumbSecondArr[1] + ":" + type;
								}
								// 哑题改动
								if (Util.isPass(answerText, dumbSecondArr[1], dumbSecondArr[2])) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								// 答案为空什么也不做
							}
						} else {
							// 服务器传的字符串为空什么也不做
						}
						// 两者 满足一个
						if (isOneOk || isTwoOk) {
							hideCount++;
							rb.setVisibility(View.GONE);
							realRows--;
						}
					} else if (dumbOneFirstArr.length == 1 && dumbOneSecondArr.length == 1) {
						/**
						 * 引用受访者参数
						 */
						String parametersStr = feed.getParametersStr();
						ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
						if (!Util.isEmpty(parametersStr)) {
							parameterList.clear();
							ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
									.parseArray(parametersStr, Parameter.class);
							if (!Util.isEmpty(tParameters)) {
								parameterList.addAll(tParameters);
							}
						}
						String content = "";
						if (null != parameterList && parameterList.size() > 0) {
							for (Parameter parameter : parameterList) {
								if (parameter.getSid().equals(dumbFirstArr[0])) {
									System.out.println("content:" + content);
									content = parameter.getContent();
									break;
								}
							}
						} else {
							content = "";
						}
						// 假如查到content了，在判断是什么符号
						if (!Util.isEmpty(content)) {
							// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
							// 假如是数字
							if (Util.isFormat(dumbFirstArr[2], 9) && Util.isFormat(content, 9)
									&& !Util.isFormat(dumbFirstArr[1], 9)) {
								if (Util.isMath(content, dumbFirstArr[1], dumbFirstArr[2])) {
									isOneOk = true;
								}
							}
							// 日期
							else if (dumbFirstArr[1].indexOf("-") != -1
									&& dumbFirstArr[1].length() > 5) {
								try {
									if (Util.getDateCompare(dumbFirstArr[2], content,
											dumbFirstArr[1])) {
										isOneOk = true;
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							// 文本格式
							else {
								if (Util.isRespondentsMatching(content, dumbFirstArr[1],
										dumbFirstArr[2])) {
									isOneOk = true;
								}
							}
						}

						if (null != parameterList && parameterList.size() > 0) {
							for (Parameter parameter : parameterList) {
								if (parameter.getSid().equals(dumbSecondArr[0])) {
									content = parameter.getContent();
									break;
								}
							}
						} else {
							content = "";
						}

						// 假如查到content了，在判断是什么符号
						if (!Util.isEmpty(content)) {
							// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
							// 假如是数字
							if (Util.isFormat(dumbSecondArr[2], 9) && Util.isFormat(content, 9)
									&& !Util.isFormat(dumbSecondArr[1], 9)) {
								if (Util.isMath(content, dumbSecondArr[1], dumbSecondArr[2])) {
									isTwoOk = true;
								}
							}
							// 日期
							else if (dumbSecondArr[1].indexOf("-") != -1
									&& dumbSecondArr[1].length() > 5) {
								try {
									if (Util.getDateCompare(dumbSecondArr[2], content,
											dumbSecondArr[1])) {
										isTwoOk = true;
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							// 文本格式
							else {
								if (Util.isRespondentsMatching(content, dumbSecondArr[1],
										dumbSecondArr[2])) {
									isTwoOk = true;
								}
							}
						}

						// 两者 满足一个
						if (isOneOk || isTwoOk) {
							hideCount++;
							realRows--;
							rb.setVisibility(View.GONE);
						}
					}
				} else if (hideList.indexOf("&") != -1) {
					// 文本题与的关系
					// 文本题或的关系
					// 1:0,=,北京&1:0,=,天津 单行文本题型 符号 >、>=
					// 、<、=、!=
					String[] twoCondition = hideList.split("&");
					String firstCondition = twoCondition[0];
					String secondCondition = twoCondition[1];
					String[] dumbFirstArr = firstCondition.split(",");
					String[] dumbSecondArr = secondCondition.split(",");
					boolean isOneOk = false;
					boolean isTwoOk = false;
					String[] dumbOneFirstArr = dumbFirstArr[0].split(":");
					String[] dumbOneSecondArr = dumbSecondArr[0].split(":");
					if (dumbOneFirstArr.length == 2 && dumbOneSecondArr.length == 2) {
						// 第一个条件
						String textIndex = dumbOneFirstArr[0];// 引用的index
						String textItemOrder = dumbOneFirstArr[1];// 引用的位置
						if (!Util.isEmpty(textIndex) && !Util.isEmpty(textItemOrder)) {
							Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid, textIndex);
							// 存取值的list
							ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
							if (null != textAnswer) {
								amTextList = textAnswer.getAnswerMapArr();
							}
							// 假如存取值的List不为空
							if (!Util.isEmpty(amTextList)) {
								AnswerMap answerMap = amTextList.get(Integer.parseInt(textItemOrder));
								String answerText = answerMap.getAnswerText();
								// 哑题改动
								Question question = ma.dbService.getQuestion(feed.getSurveyId(),
										textIndex);
								int type = -1;
								if (null != question) {
									ArrayList<QuestionItem> colItemArr = question.getColItemArr();
									if (!Util.isEmpty(colItemArr)) {
										QuestionItem questionItem = colItemArr
												.get(Integer.parseInt(textItemOrder));
										if (null != questionItem) {
											type = questionItem.getType();
										}
									}
								}
								if (-1 != type) {
									dumbFirstArr[1] = dumbFirstArr[1] + ":" + type;
								}
								// 哑题改动
								if (Util.isPass(answerText, dumbFirstArr[1], dumbFirstArr[2])) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								// 答案为空什么也不做
							}
						} else {
							// 服务器传的字符串为空什么也不做
						}

						// 第二个条件
						String textTwoIndex = dumbOneSecondArr[0];// 引用的index
						String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
						if (!Util.isEmpty(textTwoIndex) && !Util.isEmpty(textTwoItemOrder)) {
							Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid, textTwoIndex);
							// 存取值的list
							ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
							if (null != textAnswer) {
								amTextList = textAnswer.getAnswerMapArr();
							}
							// 假如存取值的List不为空
							if (!Util.isEmpty(amTextList)) {
								AnswerMap answerMap = amTextList
										.get(Integer.parseInt(textTwoItemOrder));
								String answerText = answerMap.getAnswerText();
								// 哑题改动
								Question question = ma.dbService.getQuestion(feed.getSurveyId(),
										textIndex);
								int type = -1;
								if (null != question) {
									ArrayList<QuestionItem> colItemArr = question.getColItemArr();
									if (!Util.isEmpty(colItemArr)) {
										QuestionItem questionItem = colItemArr
												.get(Integer.parseInt(textItemOrder));
										if (null != questionItem) {
											type = questionItem.getType();
										}
									}
								}
								if (-1 != type) {
									dumbSecondArr[1] = dumbSecondArr[1] + ":" + type;
								}
								// 哑题改动
								if (Util.isPass(answerText, dumbSecondArr[1], dumbSecondArr[2])) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								// 答案为空什么也不做
							}
						} else {
							// 服务器传的字符串为空什么也不做
						}
						// 两者 满足一个
						if (isOneOk && isTwoOk) {
							hideCount++;
							realRows--;
							rb.setVisibility(View.GONE);
						}
					} else if (dumbOneFirstArr.length == 1 && dumbOneSecondArr.length == 1) {
						// 受访者属性题目

						/**
						 * 引用受访者参数
						 */
						String parametersStr = feed.getParametersStr();
						ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
						if (!Util.isEmpty(parametersStr)) {
							parameterList.clear();
							ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
									.parseArray(parametersStr, Parameter.class);
							if (!Util.isEmpty(tParameters)) {
								parameterList.addAll(tParameters);
							}
						}
						String content = "";
						if (null != parameterList && parameterList.size() > 0) {
							for (Parameter parameter : parameterList) {
								if (parameter.getSid().equals(dumbFirstArr[0])) {
									content = parameter.getContent();
									break;
								}
							}
						} else {
							content = "";
						}
						// 假如查到content了，在判断是什么符号
						if (!Util.isEmpty(content)) {
							// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
							// 假如是数字
							if (Util.isFormat(dumbFirstArr[2], 9) && Util.isFormat(content, 9)
									&& !Util.isFormat(dumbFirstArr[1], 9)) {
								if (Util.isMath(content, dumbFirstArr[1], dumbFirstArr[2])) {
									isOneOk = true;
								}
							}
							// 日期
							else if (dumbFirstArr[1].indexOf("-") != -1
									&& dumbFirstArr[1].length() > 5) {
								try {
									if (Util.getDateCompare(dumbFirstArr[2], content,
											dumbFirstArr[1])) {
										isOneOk = true;
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							// 文本格式
							else {
								if (Util.isRespondentsMatching(content, dumbFirstArr[1],
										dumbFirstArr[2])) {
									isOneOk = true;
								}
							}
						}

						if (null != parameterList && parameterList.size() > 0) {
							for (Parameter parameter : parameterList) {
								if (parameter.getSid().equals(dumbSecondArr[0])) {
									content = parameter.getContent();
									break;
								}
							}
						} else {
							content = "";
						}

						// 假如查到content了，在判断是什么符号
						if (!Util.isEmpty(content)) {
							// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
							// 假如是数字
							if (Util.isFormat(dumbSecondArr[2], 9) && Util.isFormat(content, 9)
									&& !Util.isFormat(dumbSecondArr[1], 9)) {
								if (Util.isMath(content, dumbSecondArr[1], dumbSecondArr[2])) {
									isTwoOk = true;
								}
							}
							// 日期
							else if (dumbSecondArr[1].indexOf("-") != -1
									&& dumbSecondArr[1].length() > 5) {
								try {
									if (Util.getDateCompare(dumbSecondArr[2], content,
											dumbSecondArr[1])) {
										isTwoOk = true;
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							// 文本格式
							else {
								if (Util.isRespondentsMatching(content, dumbSecondArr[1],
										dumbSecondArr[2])) {
									isTwoOk = true;
								}
							}
						}

						// 两者 满足一个
						if (isOneOk && isTwoOk) {
							hideCount++;
							realRows--;
							rb.setVisibility(View.GONE);
						}

					}
				} else {
					String[] dumbArr = hideList.split(",");
					// 哑题有引用的index和value值。
					// 5,2:3:4 第一种题型
					//单复选
					if (dumbArr.length == 2) {
						String dumbIndex = dumbArr[0];// index
						String dumbValueStr = dumbArr[1];// value
						// 引用的那道哑题的答案
						Answer dumbAnswer = ma.dbService.getAnswer(qAnswer.uuid, dumbIndex);
						// 存取值的list
						ArrayList<AnswerMap> dumbAmList = new ArrayList<AnswerMap>();
						if (null != dumbAnswer) {
							dumbAmList = dumbAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(dumbAmList)) {
							String[] dumbValueArr = dumbValueStr.split(":");// 数组
							// 循环储存的值
							for (AnswerMap dumbAm : dumbAmList) {
								String dumbValue = dumbAm.getAnswerValue();// 原有的值
								System.out.println("上一道的值:" + dumbValue);
								// 假如答案和引用的value值相同
								for (String str : dumbValueArr) {
									System.out.println("本道逻辑可跳的值:" + str);
								}
								if (Util.isHave(dumbValueArr, dumbValue)) {
									hideCount++;
									realRows--;
									rb.setVisibility(View.GONE);
								} else {

								}
							}
						} else {
							// 为空,什么都不做。
						}
					}
					// 二三种题型
					else if (dumbArr.length == 3) {
						// Company,2,北京 受访者属性题型 不包含为 1 包含为2
						// 1:0,=,北京 单行文本题型
						String dumbOneStr = dumbArr[0];
						String[] dumbOneArr = dumbOneStr.split(":");
						// 1为受访者属性题型
						if (dumbOneArr.length == 1) {
							/**
							 * 引用受访者参数
							 */
							String parametersStr = feed.getParametersStr();
							ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
							if (!Util.isEmpty(parametersStr)) {
								parameterList.clear();
								ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
										.parseArray(parametersStr, Parameter.class);
								if (!Util.isEmpty(tParameters)) {
									parameterList.addAll(tParameters);
								}
							}
							String content = "";
							if (null != parameterList && parameterList.size() > 0) {
								for (Parameter parameter : parameterList) {
									if (parameter.getSid().equals(dumbArr[0])) {
										content = parameter.getContent();
										break;
									}
								}
							} else {
								content = "";
							}
							// System.out.println("0:"+content+"1:"+dumbArr[1]+"2:"+dumbArr[2]);
							// 假如查到content了，在判断是什么符号
							if (!Util.isEmpty(content)) {
								// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
								// 假如是数字
								if (Util.isFormat(dumbArr[2], 9) && Util.isFormat(content, 9)
										&& !Util.isFormat(dumbArr[1], 9)) {
									if (Util.isMath(content, dumbArr[1], dumbArr[2])) {
										hideCount++;
										realRows--;
										rb.setVisibility(View.GONE);
									}
								}
								// 日期
								else if (dumbArr[1].indexOf("-") != -1 && dumbArr[1].length() > 5) {
									try {
										if (Util.getDateCompare(dumbArr[2], content, dumbArr[1])) {
											hideCount++;
											realRows--;
											rb.setVisibility(View.GONE);
										}
									} catch (ParseException e) {
										e.printStackTrace();
									}
								}
								// 文本格式
								else {
									if (Util.isRespondentsMatching(content, dumbArr[1], dumbArr[2])) {
										hideCount++;
										realRows--;
										rb.setVisibility(View.GONE);
									}
								}
							}
						}
						// 2为单行文本题型
						else if (dumbOneArr.length == 2) {
							// 1:0,=,北京 单行文本题型 符号 >、>=
							// 、<、=、!=
							String textIndex = dumbOneArr[0];// 引用的index
							String textItemOrder = dumbOneArr[1];// 引用的位置
							if (!Util.isEmpty(textIndex) && !Util.isEmpty(textItemOrder)) {
								Answer textAnswer = ma.dbService.getAnswer(qAnswer.uuid, textIndex);
								// 存取值的list
								ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
								if (null != textAnswer) {
									amTextList = textAnswer.getAnswerMapArr();
								}
								// 假如存取值的List不为空
								if (!Util.isEmpty(amTextList)) {
									AnswerMap answerMap = amTextList
											.get(Integer.parseInt(textItemOrder));
									String answerText = answerMap.getAnswerText();
									// 哑题改动
									Question question = ma.dbService.getQuestion(feed.getSurveyId(),
											textIndex);
									int type = -1;
									if (null != question) {
										ArrayList<QuestionItem> colItemArr = question.getColItemArr();
										if (!Util.isEmpty(colItemArr)) {
											QuestionItem questionItem = colItemArr
													.get(Integer.parseInt(textItemOrder));
											if (null != questionItem) {
												type = questionItem.getType();
											}
										}
									}
									if (-1 != type) {
										dumbArr[1] = dumbArr[1] + ":" + type;
									}
									// 哑题改动
									if (Util.isPass(answerText, dumbArr[1], dumbArr[2])) {
										rb.setVisibility(View.GONE);
										hideCount++;
										realRows--;
									} else {
										// 不满足什么都不做
									}
								} else {
									// 答案为空什么也不做
								}
							} else {
								// 服务器传的字符串为空什么也不做
							}
						}
					}
				}
			}
		}
		//隐藏选项
		if (1 == item.hide) {
			rb.setVisibility(View.GONE);
			realRows--;
		}
		//条件隐藏选项
	}

	/**
	 * 哑题无验证的方法
	 */
	private void nextPage(boolean isNoValidate) {
		int state = getQuestionAnswer(MSG_NEXT, isNoValidate);
		sum = 0;
		// System.out.println("state--------->" + state);
		// 为false就验证 不让过。
		if (!isNoValidate) {
			if (Cnt.STATE_SUCCESS != state) {
				switch (state) {
				case STATE_NOTHING:
					Message msg = Message.obtain();
					msg.what = STATE_NOTHING;
					msg.obj = getResources().getString(R.string.msg_must);
					handler.sendMessage(msg);
					break;

				case STATE_SYB_MIN:// 文本框中填写的值小于给定的最小值
				case STATE_SYB_MAX:// 文本框中的值大于给定的最大值
				case Cnt.STATE_FAIL:// 选了其他项, 但是没有填写文本框里的内容
				case STATE_BOUND_LOWER:// 复选题目选项选择的个数小于给定的最小个数
				case STATE_BOUND_UPPER:// 复选题目选项选择的个数大于给定的最大个数
				case STATE_BOUND_MATRIX_LOWER:// 复选矩阵一行选择的选项小于给定的下限
				case STATE_BOUND_MATRIX_UPPER:// 复选矩阵一行选择的选项大于给定的上限
				case STATE_CONTINUOUS:// 单选矩阵同一列中连续选择选项的个数大于给定的个数
				case STATE_ROW_LESS:// 必答题没有答完,例如本来有3行,只答1行或2行
					break;
				}
				return;
			}
		}
		if (qs.size() == index) {
			// getQuestionAnswer(MSG_NEXT);
			handler.sendEmptyMessage(MSG_WRITE);
			// 写文件
			return;
		}
		globleProgress.setVisibility(View.VISIBLE);
		setTopClick(false);
		index++;
		/*** 题组随机开始 */
		// 看看能不能得到组
		final TempGroup tg = dialogMap.get(index);
		// 假如得到了，就是要进行选择和排序了
		if (tg != null) {
			tempGroupRow = 0;
			// 获得小题组集合号
			final ArrayList<Integer> smallOrder = tg.getSmallOrder();
			// 得到存小题组名字的集合
			final HashMap<Integer, String> smallGroupMap = tg.getSmallGroupMap();
			// 把集合小题组名字变成数组

			String[] myChoice = new String[smallOrder.size()];
			for (int i = 0; i < smallOrder.size(); i++) {
				String smallChoice = smallGroupMap.get(smallOrder.get(i));
				myChoice[i] = smallChoice;
			}
			// 弹出对话框选择
			// System.out.println("弹窗:"+myChoice.toString()+"个数"+myChoice.length);
			// for(String jj:myChoice){
			// System.out.println("循环");
			// System.out.println(jj);
			// }
			new AlertDialog.Builder(this).setTitle(tg.getBigGroupName()).setCancelable(false).setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(myChoice, 0, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.out.println("which:" + which);
							// 得到你选择的位置
							tempGroupRow = which;
						}
					}).setPositiveButton(NativeModeActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 得到每一次选择之前的状态小题组集合
							HashMap<Integer, String> reseverSmallGroup = tg.getSmallGroupMap();
							// 得到每一次选择之前的状态小题组集合模版
							HashMap<Integer, String> tempSmallHashMap = new HashMap<Integer, String>();
							// 给模版赋值
							for (int k = 0; k < reseverSmallGroup.size(); k++) {
								Iterator iter = reseverSmallGroup.keySet().iterator();
								while (iter.hasNext()) {
									Integer key = (Integer) iter.next();
									// System.out.println("重新赋值:"+reseverSmallGroup.get(key));
									tempSmallHashMap.put(key, reseverSmallGroup.get(key));
									k++;
								}
							}
							// 得到每一次选择之前的状态小题组顺序号
							ArrayList<Integer> smallOrder2 = tg.getSmallOrder();
							// 得到每一次选择之前的状态小题组顺序号模版
							ArrayList<Integer> tempSmallOrder2 = new ArrayList<Integer>();
							// 循环赋值
							for (Integer tempStrOrder : smallOrder2) {
								tempSmallOrder2.add(tempStrOrder);
							}
							// 得到前一次的模版手动题组随机对象
							TempGroup preTg = new TempGroup(tg.getBigGroupName(), tempSmallHashMap, tg.getBigOrder(), tempSmallOrder2);
							// 存在指定的位置
							preDialogMap.put(index, preTg);
							/**
							 * 重新赋值分位置,取消一个值
							 */
							// 得到选择的小题组号
							Integer removeMapKey = smallOrder.get(tempGroupRow);
							// 移除这个小题组号
							smallOrder.remove(tempGroupRow);
							// 移除这个小题组号的集合
							smallGroupMap.remove(removeMapKey);
							// 重新赋值
							tg.setSmallOrder(smallOrder);
							tg.setSmallGroupMap(smallGroupMap);
							// 处理下一次触发选择的算法
							int nextKeyOrder = 0;
							// 通过大题组号得到 该大题组的所有小组
							ArrayList<String> arrayList = orderMap.get(tg.getBigOrder());
							System.out.println("存取组的集合:" + arrayList);
							// 通过小组号得到要移除的该小组
							String removeSmallGroupStr = arrayList.get(removeMapKey);
							String[] removeSmallGroupArr = removeSmallGroupStr.split(",");
							// 假如小题组后面还有小题组。用下一个小题组的最小值减去这个小题组的值。
							if (arrayList.size() - 1 > removeMapKey) {
								String nextSmallGroupStr = arrayList.get(removeMapKey + 1);
								String[] nextSmallGroupArr = nextSmallGroupStr.split(",");
								int minBackValue = 999999;
								for (int c = 0; c < removeSmallGroupArr.length; c++) {
									Integer smallGroupNum = Integer.parseInt(removeSmallGroupArr[c]);
									if (smallGroupNum < minBackValue) {
										minBackValue = smallGroupNum;
									}
								}
								int minNextValue = 999999;
								for (int d = 0; d < nextSmallGroupArr.length; d++) {
									Integer nextSmallGroupNum = Integer.parseInt(nextSmallGroupArr[d]);
									if (nextSmallGroupNum < minNextValue) {
										minNextValue = nextSmallGroupNum;
									}
								}
								// 这个就是触发下一次事件的位置
								nextKeyOrder = minNextValue - minBackValue + index;
							}
							// 否则直接加上这个小题组的长度
							else {
								nextKeyOrder = (removeSmallGroupArr.length) + index;
							}
							// 处理下一次触发选择的算法
							dialogMap.remove(index);
							// 选择次数+1
							if (smallOrder.size() != 0) {
								// 把每次手动选择位置记录下来
								handList.add(nextKeyOrder - 1);
								dialogMap.put(nextKeyOrder, tg);
							}
							handGroupNum++;
							choiceNum++;
							// Toasts.makeText(NativeModeActivity.this,
							// "选中了第" + tempGroupRow + "项", 2).show();
							/**
							 * 重新分配位置
							 */
							// 选择已经选中的 小题组字符串
							String choiceStr = arrayList.get(removeMapKey);
							// 在另一个能得到小题组的模版上取值
							ArrayList<String> tempArrayList = tempOrderMap.get(tg.getBigOrder());
							// 首先移除掉已经选择的这一组
							tempArrayList.remove(choiceStr);
							Log.i("@@@", "choiceNum="+choiceNum+"choiceStr="+choiceStr);
							// 然后在指定位置加上这一组
							tempArrayList.add(choiceNum, choiceStr);
							// System.out.println("选择的值"+arrayList.get(removeMapKey));
							// System.out.println("removeMapKey:"+removeMapKey);
							// System.out.println("choiceStr:"+choiceStr);
							// System.out.println("choiceNum:"+choiceNum);
							// System.out.println("临时集合"+tempArrayList);
							ArrayList<String> tempGroupOrder = Util.getAllGroupOrder(diffList, qDiffList, tempOrderMap, groupSize);
							// System.out.println("重新排的位置:"+tempGroupOrder);
							arrList.removeAll(allGroupOrder);
							arrList.addAll(firstGroupOrder, tempGroupOrder);
							dialog.dismiss();
							if (smallOrder.size() == 0) {
								choiceNum = -1;
							}
							// System.out.println("第二个实体:"+preTg);
							// 增加手动循环序列号
							if (null == groupSequence) {
								groupSequence = "";
							}
							String[] groupSequenceArr = groupSequence.split("\\|");
							String hgsBody = tg.getBigOrder() + "@" + removeMapKey;
							// 手动题组次数大于已经记录的数目或者题组随机的序列号为"" 直接加即可
							if (handGroupNum > groupSequenceArr.length || "".equals(groupSequence)) {
								groupSequence += hgsBody + "|";
							}
							// 否则改变已经有的序列号的位置
							else {
								// 重新循环
								groupSequence = "";
								String tempGroupSequenceStr = "";
								for (int recodeHandNum = 0; recodeHandNum < groupSequenceArr.length; recodeHandNum++) {
									// 是该选的位置时候
									if (recodeHandNum == (handGroupNum - 1)) {
										// 原先位置的序列
										tempGroupSequenceStr = groupSequenceArr[recodeHandNum];
										// 这组序列 就是该选上的
										groupSequenceArr[recodeHandNum] = hgsBody;
									}
									// 不是这个位置
									else {
										if (hgsBody.equals(groupSequenceArr[recodeHandNum])) {
											groupSequenceArr[recodeHandNum] = tempGroupSequenceStr;
										}
									}
									groupSequence += groupSequenceArr[recodeHandNum] + "|";
								}
							}

							/*** 题组随机结束 */
							bodyView.removeAllViews();
							// 单复选矩阵固定
							bodyView_new.removeAllViews();
							// Util.showView(SurveyActivity.this, allView,
							// R.anim.slide_left_out);
							vs.clear();

							createQuestionBodyView(MSG_NEXT);
						}

					}).show();
		} else {
			/*** 题组随机结束 */
			bodyView.removeAllViews();
			// 单复选矩阵固定
			bodyView_new.removeAllViews();
			// Util.showView(SurveyActivity.this, allView,
			// R.anim.slide_left_out);
			vs.clear();
			createQuestionBodyView(MSG_NEXT);
		}
	}

	// 单选追加说明方法
	public class MyRadioButtonClick implements OnCheckedChangeListener {
		// private int id;
		private String item;

		public MyRadioButtonClick(String _item) {
			// id=_id;
			item = _item;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isPrompt) {
				return;
			}
			if (isChecked) {
				Intent intent = new Intent(NativeModeActivity.this, MyDialogActivity.class);
				intent.putExtra("item", item);
				NativeModeActivity.this.startActivity(intent);
				// Toasts.makeText(NativeModeActivity.this,
				// item,Toast.LENGTH_LONG).show();
			}
		}

	}

	// 逻辑次数跳转 //给hashmap logic
	private HashMap<Integer, TempLogic> myLogic = new HashMap<Integer, TempLogic>();
	private ArrayList<Integer> indexList = new ArrayList<Integer>();// 保存序列号list
	private boolean isForwarding = false;// 是否正在跳转
	// 追加说明方法继续上一页下一页终止提示
	private boolean isPrompt = false;
	// 记录上一次的realIndex,监控用
	private int recodeTempIndex = -1;
	// 监控用
	private int realIndex = 0;
	// 目录记录上一个order集合
	private ArrayList<Integer> recodeOrderList = new ArrayList<Integer>();
	
	public void createQuestionBodyView(int operType) {// 动态生成题干的方法
		// 追加说明方法继续上一页下一页终止提示
		hideCount=0;
		realRows=0;
		isPrompt = true;
		mTempRows.clear();
		// if ((qs.size() - 1) == index) {
		// return;
		// }
		/**
		 * 假如题目集合中没有题目则停止执行一下代码
		 */
		if (Util.isEmpty(qs)) {
			return;
		}

		if (index == qs.size()) {
			if (globleProgress.VISIBLE == View.VISIBLE) {
				globleProgress.setVisibility(View.GONE);
			}
			// 确定取消 确认提交 监控用
			Message msg = handler.obtainMessage();
			msg.what = MSG_WRITE;
			msg.obj = "1";// 1代表最后一页提交
			handler.sendMessage(msg);
			return;
		}
		/*** zz题组随机开始 ****/
		realIndex = index;
		if (!Util.isEmpty(arrList)) {
			String strIndex = arrList.get(index);
			realIndex = Integer.parseInt(strIndex);
		}
		/*** zz题组随机结束 ****/

		/**
		 * 获取当前题目
		 */
		q = qs.get(realIndex);
		// 监控初始位置
		/**
		 * 若问题对象为空, 停止执行一下代码
		 */
		if (null == q) {
			return;
		}

		initView();

		/**
		 * 单题拍照 大树动画 7 设置单题拍照
		 */
		if (q.qCamera == 1) {
			isHaveSingle = true;
		} else {
			isHaveSingle = false;
		}

		// if (1 == feed.getSurvey().isPhoto) {z1删除
		// boolean isHaveCamera = (q.qCamera == 1 ? true : false);
		// // 有单题拍照
		// if (isHaveCamera) {
		// miCamera.setEnabled(false);
		// } else {
		// miCamera.setEnabled(true);
		// }
		// }z1删除

		/**
		 * 本道题的答案
		 */
		tempAnswer = ma.dbService.getAnswer(qAnswer.uuid, q.qIndex + "");

		/**
		 *  定制干预    IMS  皮炎湿疹  临时 跳转  V6  
		 */
		if (ImsIntervetion.isSkipV6(imsIntervetion, q, 48)) {
			if (MSG_NEXT == operType) {
				index = index + 1;
				/**
				 * 假入是下一页操作    
				 */
				handler.sendEmptyMessage(MSG_NEXT);
			} else {
				index = index - 1;
				/**
				 * 上一页操作
				 */
				handler.sendEmptyMessage(MSG_PRE);
			}
			return;
		}
		/**
		 * 订制干预,假如到S9 判断s6的答案 未选择1 就不跳转s9 ,选择1了 就到s10，翻一页
		 */
		// System.out.println("qOrder:"+q.qOrder+"qIndex:"+q.qIndex+",qTitle:"+q.qTitle+",q.qQid"+q.qid);
		/**
		 * 手动干预
		 */
		// 查看是否有干预
		if (!q.getIntervention().getIisMap().isEmpty()) {
			// 手动干预是否能通过
			boolean is = Util.isIntervention(ma, q, qAnswer.uuid);
			if (is) {
				/**
				 * 成立则显示本道题目
				 */
			} else {
				/**
				 * 假如跳到下一道题或上一题, 则将本道题的答案置为空
				 */
				ma.dbService.updateAnswer2Null(tempAnswer);
				if (MSG_NEXT == operType) {
					index = index + 1;
					/**
					 * 假入是下一页操作
					 */
					handler.sendEmptyMessage(MSG_NEXT);
				} else {
					index = index - 1;
					/**
					 * 上一页操作
					 */
					handler.sendEmptyMessage(MSG_PRE);
				}
				return;
			}
			System.out.println("-------->_index==_q.qIndex");
		}

		/** 逻辑次数跳转开始 **/
		// 假如数据库中筛选终止logicList不为空
		if (logicList != null) {
			// 假如没有正在跳转
			if (!isForwarding) {
				// 获得有几个筛选终止的list
				ArrayList<Logic> logics = logicList.getLogics();
				// 假如筛选终止list不为空
				if (!Util.isEmpty(logics)) {
					// 循环筛选终止
					for (Logic logic : logics) {
						// System.out.println("累加几次后跳:"+logic.getCountJump()+"/t跳到哪个index中:"+logic.getJumpIndex());
						// 获得累加几次跳转的数
						int countJump = logic.getCountJump();
						// 获得要跳转的index
						int jumpIndex = logic.getJumpIndex();
						// 假如0等于次数或者0等于要跳转的index就下一个循环
						if (0 == countJump || 0 == jumpIndex) {
							continue;
						}
						// 通过要跳转的index获得要跳转的order
						int jumpOrder = ma.dbService.getQuestionOrder(feed.getSurveyId(), jumpIndex);
						// 假如现在的order大于等于要跳的order,就下一个循环，取消这此循环。
						if (q.qOrder >= jumpOrder) {
							continue;
						}
						// 获取该筛选终止选项集合
						ArrayList<LogicItem> logicItems = logic.getLogicItem();
						// 假如选项集合不为空
						if (!Util.isEmpty(logicItems)) {
							// 记录累加了几次
							int sumCountIndex = 0;
							// 这个集合中记录最大的顺序号
							int maxOrder = 0;
							// 记录最小的order
							int minOrder = 0;
							// 循环以便获得最小的order
							for (LogicItem li : logicItems) {
								// 获得index
								int liIndex = li.getLogicIndex();
								// 获得这个index的order
								int liOrder = ma.dbService.getQuestionOrder(feed.getSurveyId(), liIndex);
								// order==0跳过
								if (liOrder == 0) {
									continue;
								}
								// 最小的order假如没记录过
								if (minOrder == 0) {
									// 最小的order
									minOrder = liOrder;
								}
								// 假如最小的order大于当前的order
								if (minOrder > liOrder) {
									// 最小的order就赋给新的
									minOrder = liOrder;
								}
							}
							// 假如当前的order小于最小的order 终止判断，不自动翻页
							if (q.qOrder < minOrder) {
								break;
							}
							// 循环选项
							for (LogicItem logicItem : logicItems) {
								// System.out.println("哪个题需要累加:"+logicItem.getLogicIndex()+"/t累加题需要选什么值才能累加:"+logicItem.getLogicValue());
								// 获得该选项的index
								int logicIndex = logicItem.getLogicIndex();
								// 获得该选项需要选得值有哪些
								String logicValue = logicItem.getLogicValue();
								// 存取的数组为空
								String[] arrLogicValue = null;
								// 假如选项能选择的值不为空跳过次循环
								if (Util.isEmpty(logicValue)) {
									continue;
								} else {
									// 否则获得该选项的数组值
									arrLogicValue = logicValue.split(",");
								}
								// 通过这个选项index的值获得answer实体
								Answer myAnswer = ma.dbService.getAnswer(qAnswer.uuid, logicIndex + "");
								// 实例化AnswerMap list集合
								ArrayList<AnswerMap> answerMapList = new ArrayList<AnswerMap>();
								// 假如答案不为空
								if (null != myAnswer) {
									// 获取答案存取的AnswerMap list集合
									answerMapList = myAnswer.getAnswerMapArr();
								} else {
									// 否则跳转
									continue;
								}
								// 获得该题的order号
								int qOrder = myAnswer.qOrder;
								// 假如答案存取的answerMapList list集合不为空
								if (!Util.isEmpty(answerMapList)) {
									// 循环AnswerMap实体
									for (AnswerMap am : answerMapList) {
										// 获得这个AnswerMap的value值
										String localAnswerValue = am.getAnswerValue();
										// 假如AnswerMap的value值不为空
										if (!Util.isEmpty(localAnswerValue)) {
											// 假如数据库存取的值在 应该跳转的数组中存在
											if (Util.isHave(arrLogicValue, localAnswerValue)) {
												// 找到了 累加一次
												sumCountIndex++;
												// 假如储存的最大值<现在比较成功的值
												if (maxOrder < qOrder) {
													maxOrder = qOrder;// 记录最大的order值
												}
												break;
											}
										} else {
											// 为空跳过本次循环
											continue;
										}
									}
								} else {
									// 为空跳过本次循环
									continue;
								}
							}
							// System.out.println("sumCountIndex:"+sumCountIndex);
							// 假如累加的值>=要跳的数目值
							if (sumCountIndex >= countJump) {
								// 通过要跳的key获取暂存的TempLogic实体，此实体用来控制跳转
								TempLogic tl = myLogic.get(jumpIndex);
								// 假如TempLogic实体为空
								if (null == tl) {
									// 实例化实体
									tl = new TempLogic();
								}
								// 假如是下一页
								if (MSG_NEXT == operType) {
									// TempLogic实体设置可以往后跳转
									tl.setIsForwardComplete(true);
									// TempLogic实体设置不可以往前跳转
									tl.setIsReturnComplete(false);
								}
								// 假如是上一页
								else if (MSG_PRE == operType) {
									// TempLogic实体设置不可以往后跳转
									tl.setIsForwardComplete(false);
									// TempLogic实体设置可以往前跳转
									tl.setIsReturnComplete(true);
								}
								// 假如现在的order小于等于最大的order
								if (q.qOrder <= maxOrder) {
									// 设置最后一个跳转order为0
									tl.setLastOrder(0);
								} else {
									// 设置最后一个跳转order为最大的order
									tl.setLastOrder(maxOrder);
								}
								// System.out.println("maxOrder:"+maxOrder);
								// 设置最后一个跳转order为最大的order作为临时order
								tl.setTempLastOrder(maxOrder);
								// 存取hashmap值
								myLogic.put(jumpIndex, tl);
								break;
							} else {
								// 假如累加的值小于要跳转的数目
								TempLogic tl = myLogic.get(jumpIndex);
								// 获得实体
								if (null == tl) {
									tl = new TempLogic();
								}
								// 设置既不能往前也不能往后跳
								tl.setIsForwardComplete(false);
								tl.setIsReturnComplete(false);
								// 储存最后的值为0
								tl.setLastOrder(0);
								// 存取值
								myLogic.put(jumpIndex, tl);
							}
						}
					}
				}
			}
		}
		/** 逻辑次数跳转结束 **/
		/** 逻辑次数跳转 **/
		for (int n = 0; n < indexList.size(); n++) {
			int indexKey = indexList.get(n);
			// 最小能跳的验证
			int indexKeyOrder = 0;
			if (n > 0) {
				int lastIndexKey = indexList.get(n - 1);
				// 得到上一个要跳转的order号
				indexKeyOrder = ma.dbService.getQuestionOrder(feed.getSurveyId(), lastIndexKey);
			}
			if (q.qOrder > indexKeyOrder) {
				// 得到hashmap 9这个键值对应的logic实体
				TempLogic logic = myLogic.get(indexKey);
				// 假如logic实体不为空
				if (logic != null) {
					// 假如是下一页
					if (MSG_NEXT == operType) {
						// 可以跳转
						if (logic.isIsForwardComplete()) {
							// 没完成的话qIndex是否为9
							if (q.qIndex != indexKey) {
								// 不是一直翻页
								index = index + 1;
								isForwarding = true;
								int nowOrder = ma.dbService.getQuestionOrder(feed.getSurveyId(), q.qIndex);// 现在的order
								int tempLastOrder = logic.getTempLastOrder();// 最后记录的order
								// System.out.println("nowOrder:"+nowOrder);
								// System.out.println("tempLastOrder:"+tempLastOrder);
								// 现在的order>最后的order
								if (nowOrder > tempLastOrder) {
									if (null != tempAnswer) {
										/**
										 * 置为无效的答案
										 */
										// 最后一个选项以后才能置为空
										ma.dbService.updateAnswerUnEnable(tempAnswer);
									}
								}
								handler.sendEmptyMessage(MSG_NEXT);
								return;
							} else {
								isForwarding = false;
								// 跳到9了，设置跳转完成
								logic.setIsForwardComplete(false);
								myLogic.put(indexKey, logic);
							}
						}
					} else if (MSG_PRE == operType) {
						// 假如是上一页
						// 能否返回,假如能返回
						if (logic.isIsReturnComplete()) {
							// 假如有记录最后一个顺序号
							if (0 != logic.getLastOrder()) {
								// 假如这个最后一个order没到
								if (q.qOrder != logic.getLastOrder()) {
									// 就一直上一页翻
									index = index - 1;
									isForwarding = true;
									int lastOrder = logic.getLastOrder();// 获取最后一个order号
									if (null != tempAnswer) {
										/**
										 * 置为无效的答案
										 */
										ma.dbService.updateAnswerUnEnable(tempAnswer);
									}
									handler.sendEmptyMessage(MSG_PRE);
									return;
								} else {
									isForwarding = false;
									// 到了上一页记录号的题目
									// 设置最后一个号为0
									logic.setLastOrder(0);
									// 设置返回状态为不能返回
									logic.setIsForwardComplete(true);
									// 给hashmap logic赋值
									myLogic.put(indexKey, logic);
								}
							}
						}

					}
					// 假如为第9题
					if (indexKey == q.qIndex) {
						// 记录能返回
						logic.setIsReturnComplete(true);
						myLogic.put(indexKey, logic);
					} else {
						// 不为第9记录不能返回
						logic.setIsReturnComplete(false);
						myLogic.put(indexKey, logic);
					}
				}
			}
		}
		/** 逻辑次数跳转结束 */

		/**
		 * 获取引用其他问题的集合
		 */
		ArrayList<Restriction> rs = new ArrayList<Restriction>();
		rs.addAll(q.getResctItemArr());

		// System.out.println("当前question_index--->" + q.qIndex);

		/**
		 * 本道引用了上一道的选项答案, 如果上面的某道题符合条件 则显示这道题,否则显示下一道题
		 */
		if (!Util.isEmpty(rs)) {
			for (Restriction ri : rs) {
				if (null == ri) {
					rs.remove(ri);
				}
			}
		}
		if (!Util.isEmpty(rs)) {// 假如引用其它问题的答案不为空
			// System.out.println("逻辑限制非空--->"+q.qIndex);
			// Answer anw = tmpAnswerList.get(q.qIndex);
			// if(null == anw){
			// System.out.println("没答案");
			// index = index + 1;
			// createQuestionBodyView();
			// return;
			// }
			boolean isMatcher = Util.isMatcher(ma, q, qAnswer.uuid);
			System.out.println("isMatcher:" + isMatcher);
			// 假如不成功 不符合逻辑
			if (!isMatcher) {
				if (null != tempAnswer) {
					/**
					 * 置为无效的答案
					 */
					ma.dbService.updateAnswerUnEnable(tempAnswer);
				}
				// Runtime.getRuntime().freeMemory();
				if (MSG_NEXT == operType) {
					index = index + 1;
					// createQuestionBodyView(OPERATION_NEXT);
					handler.sendEmptyMessage(MSG_NEXT);
				} else if (MSG_PRE == operType) {
					index = index - 1;
					// createQuestionBodyView(OPERATION_PREV);
					handler.sendEmptyMessage(MSG_PRE);
				}
				return;
			}
			// ArrayList<Restriction> restrList
		}
		if (globleProgress.VISIBLE == View.VISIBLE) {
			globleProgress.setVisibility(View.GONE);
			setTopClick(true);
		}
		// 初始一个目录，只有添加操作
		if (!isQgroupOrLogic) {
			// 判断目录是否在题组外
			if (realIndex < firstGroupOrder || realIndex > lastGroupOrder) {
				if (realIndex != 0) {
					if (MSG_NEXT == operType) {
						// 不包含才加
						if (!recodeOrderList.contains(realIndex)) {
							recodeOrderList.add(realIndex);
						}
						// System.out.println(recodeOrderList.toString());
						// System.out.println(dtOrderList.toString());
						// 正常情况下
						if (recodeOrderList.size() - dtOrderList.size() == 1) {
							dtOrderList.add(realIndex);
						} else {
							// dtOrderList.containsAll(recodeOrderList)
							if (Collections.indexOfSubList(dtOrderList, recodeOrderList) != -1) {

							} else {
								dtOrderList.clear();
								for (int recodeOrder : recodeOrderList) {
									dtOrderList.add(recodeOrder);
								}
							}
						}
					} else {
					recodeOrderList.remove(recodeOrderList.size() - 1);
				}
			} else {
				if (MSG_NEXT == operType) {

					} else {
						recodeOrderList.clear();
						recodeOrderList.add(0);
					}
				}
			}
			this.menuListAdapter.refresh(dtOrderList);
			this.menuListAdapter.refreshIndex(index);
		}
		// 初始目录结束
		/** 监控用开始 */
		// 获取上一个index的值,监控用 建设一个异步线程
		// 有网传 大树注销监控 1
		if (NetUtil.checkNet(this)&&mapMonitor) {
		new MonitorTask(operType, realIndex).execute();
		 }
		/** 监控用结束 */
		Util.showView(NativeModeActivity.this, rlQuestion, R.anim.slide_left_in);
		// tvBar.setText(getResources().getString(R.string.survey_title, (index
		// + 1), qs.size()));
		// 大树 添加 标题
		tvBar.setText(feed.getSurveyTitle() + "(" + (index + 1) + "/" + qs.size() + ")");
		// 大树
		System.err.println("----------------------------------" + "qIndex=" + q.qIndex + "---------------------------------");
		// 单复选矩阵固定
		boolean isFixed = (1 == q.isFixed);// true固定的,false不固定的

		if (isFixed) {
			rlQuestion_new.setVisibility(View.VISIBLE);
			ltitle_tv.setVisibility(View.GONE);
			tvCaption.setVisibility(View.GONE);
			llCaption.setVisibility(View.GONE);
			// 单复选矩阵固定
			if (Cnt.POS_LEFT.equals(q.qTitlePosition)) {
				// 单复选矩阵固定
				tvTitle_new.setGravity(Gravity.LEFT);
			} else if (Cnt.POS_CENTER.equals(q.qTitlePosition)) {
				// 单复选矩阵固定
				tvTitle_new.setGravity(Gravity.LEFT);
			} else if (Cnt.POS_RIGHT.equals(q.qTitlePosition)) {
				// 单复选矩阵固定
				tvTitle_new.setGravity(Gravity.LEFT);
			} else {
				// 单复选矩阵固定
				tvTitle_new.setGravity(Gravity.LEFT);
			}
		} else {
			// 单复选矩阵固定
			rlQuestion_new.setVisibility(View.GONE);
			// System.out.println("隐藏rlQuestion_new");
			ltitle_tv.setVisibility(View.VISIBLE);
			if (Cnt.POS_LEFT.equals(q.qTitlePosition)) {
				tvTitle.setGravity(Gravity.LEFT);
			} else if (Cnt.POS_CENTER.equals(q.qTitlePosition)) {
				tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
			} else if (Cnt.POS_RIGHT.equals(q.qTitlePosition)) {
				tvTitle.setGravity(Gravity.RIGHT);
			} else {
				tvTitle.setGravity(Gravity.LEFT);
			}
		}

		if (Cnt.TYPE_HEADER == q.qType) {// 假如是标题
			/**
			 * 设置标题的颜色
			 */
			tvTitle.setTextColor(Color.BLUE);
			tvTitle.setTextSize(bigSurveySize);
			/**
			 * 显示标题文本
			 */
			if (!Util.isEmpty(q.qTitle)) {
				// 更改的样式
				String strTilte = q.qTitle;
				/**
				 * 标题逻辑引用
				 */
				CstmMatcher qutoMatcherList = Util.findMatcherItemList(strTilte, ma, qAnswer.uuid,q.surveyId);
				boolean qutoHave = Util.isEmpty(qutoMatcherList.getMis());
				if (!qutoHave) {
					strTilte = qutoMatcherList.getResultStr();
				}
				/**
				 * 引用受访者参数
				 */
				String parametersStr = feed.getParametersStr();
				ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
				if (!Util.isEmpty(parametersStr)) {
					parameterList.clear();
					ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
					if (!Util.isEmpty(tParameters)) {
						parameterList.addAll(tParameters);
					}
				}
				CstmMatcher parameterMatcherList = Util.findMatcherPropertyItemList(strTilte, parameterList);
				boolean parameterHave = Util.isEmpty(parameterMatcherList.getMis());
				if (!parameterHave) {
					strTilte = parameterMatcherList.getResultStr();
				}
				// 更改的样式
				ImageGetter imgGetter = new Html.ImageGetter() {
					public Drawable getDrawable(String source) {
						Drawable drawable = null;
						String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
						// System.out.println("name:" + name);
						drawable = Drawable.createFromPath(name);
						Bitmap image = BitmapFactory.decodeFile(name);
						if (image != null) {
							int tWidth = image.getWidth();
							int tHeight = image.getHeight();
							if(tWidth>maxCWidth){
								tWidth=maxCWidth;
								tHeight=maxCWidth/tWidth*tHeight;
							}
							drawable.setBounds(0, 0, tWidth, tHeight);
							return drawable;
						} else {
							return null;
						}
					}
				};
				Spanned fromHtml = Html.fromHtml(strTilte, imgGetter, null);
				tvTitle.setText(fromHtml);
				// 更改的样式
			}

			/**
			 * 增加单题拍照按钮 在最后面
			 */
			if (q.qCamera == 1 && !Util.isEmpty(feed.getUuid())) {
				// System.out.println("单题拍照");
				// System.out.println("有单题拍照");
				Drawable drawable = this.getResources().getDrawable(R.drawable.new_camera);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				tvTitle.setCompoundDrawables(null, null, drawable, null);
				tvTitle.setOnClickListener(new CstOnClickListener(q));
			} else {
				tvTitle.setCompoundDrawables(null, null, null, null);
				// System.out.println("无单题拍照");
			}
			/**
			 * 增加单题签名
			 */
			if (q.qSign == 1 && !Util.isEmpty(feed.getUuid())) {
				ivSign.setVisibility(View.VISIBLE);
				ivSign.setOnClickListener(new CstOnClickListener(q));
			}
			bodyView.setVisibility(View.GONE);
		} else {
			// 单复选矩阵固定
			if (isFixed) {
				// 更改的样式
				if (0 == q.qTitleDisable) {
					tvTitle_new.setClickable(false);
				}
				tvTitle_new.setTextColor(Color.BLACK);
				tvTitle_new.setTextSize(middleSueveySize);

				// StringBuilder sbTitle = new StringBuilder("");
				String strTilte = "";
				if (Util.isEmpty(q.qTitle)) {
					q.qTitle = NativeModeActivity.this.getString(R.string.no_title);
				}
				if (!Util.isEmpty(q.qid)) {
					strTilte = q.qid + ". " + q.qTitle;
				} else {
					if (!Util.isEmpty(q.qTitle)) {
						strTilte = getResources().getString(R.string.question_order, q.qOrder) + q.qTitle;
					} else {
						strTilte = getResources().getString(R.string.question_order, q.qOrder);
					}

				}

				/**
				 * 标题逻辑引用
				 */
				
				CstmMatcher qutoMatcherList = Util.findMatcherItemList(strTilte, ma, qAnswer.uuid,q.surveyId);
				boolean qutoHave = Util.isEmpty(qutoMatcherList.getMis());
				if (!qutoHave) {
					strTilte = qutoMatcherList.getResultStr();
				}

				/**
				 * 引用受访者参数
				 */
				String parametersStr = feed.getParametersStr();
				ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
				if (!Util.isEmpty(parametersStr)) {
					ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
					if (!Util.isEmpty(tParameters)) {
						parameterList.addAll(tParameters);
					}
				}
				// System.out.println("parameterList.size():"+parameterList.size());
				CstmMatcher parameterMatcherList = Util.findMatcherPropertyItemList(strTilte, parameterList);
				boolean parameterHave = Util.isEmpty(parameterMatcherList.getMis());
				if (!parameterHave) {
					strTilte = parameterMatcherList.getResultStr();
				}
				String must = "<font color='red'>" + getResources().getString(R.string.question_must) + "</font>";

				/**
				 * 必填
				 */
				if (1 == q.qRequired) {
					strTilte = strTilte + must;
				}
				// 更改的样式
				ImageGetter imgGetter = new Html.ImageGetter() {
					public Drawable getDrawable(String source) {
						Drawable drawable = null;
						String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
						// System.out.println("name:" + name);
						drawable = Drawable.createFromPath(name);
						Bitmap image = BitmapFactory.decodeFile(name);
						if (image != null) {
							int tWidth = image.getWidth();
							int tHeight = image.getHeight();
							if(tWidth>maxCWidth){
								tWidth=maxCWidth;
								tHeight=maxCWidth/tWidth*tHeight;
							}
							drawable.setBounds(0, 0, tWidth, tHeight);
							return drawable;
						} else {
							return null;
						}
					}
				};
				Spanned fromHtml = Html.fromHtml(strTilte, imgGetter, null);
				tvTitle_new.setText(fromHtml);
				// 更改的样式
				// 引用受访者参数在这运算

				if (1 == q.qRadomed) {
					String radomed = getResources().getString(R.string.question_random);
					SpannableString ss = new SpannableString(radomed);
					ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(), Spanned.SPAN_COMPOSING);
					tvTitle_new.append(ss);
				}
				// 提示
				if (0 < q.lowerBound || 0 < q.upperBound) {
					String bound = "";
					if (0 == q.upperBound) {
						bound = getResources().getString(R.string.question_bound_min, q.lowerBound);
					} else {
						bound = getResources().getString(R.string.question_bound, q.upperBound, q.lowerBound);
					}
					SpannableString ss = new SpannableString(bound);
					ss.setSpan(new ForegroundColorSpan(Color.RED), 0, bound.length(), Spanned.SPAN_COMPOSING);
					tvTitle_new.append(ss);
				}
				// 更改的样式
				/**
				 * 增加单题拍照按钮 在最后面
				 */
				if (q.qCamera == 1 && !Util.isEmpty(feed.getUuid())) {
					System.out.println("单题拍照");
					// System.out.println("有单题拍照");
					Drawable drawable = this.getResources().getDrawable(R.drawable.new_camera);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					tvTitle_new.setCompoundDrawables(null, null, drawable, null);
					tvTitle_new.setOnClickListener(new CstOnClickListener(q));
				} else {
					tvTitle_new.setCompoundDrawables(null, null, null, null);
					// System.out.println("无单题拍照");
				}
				/**
				 * 增加单题签名
				 */
				if (q.qSign == 1 && !Util.isEmpty(feed.getUuid())) {
					ivSign_new.setVisibility(View.VISIBLE);
					ivSign_new.setOnClickListener(new CstOnClickListener(q));
				}
			} else {
				if (0 == q.qTitleDisable) {
					tvTitle.setClickable(false);
				}
				tvTitle.setTextColor(Color.BLACK);
				tvTitle.setTextSize(middleSueveySize);

				// StringBuilder sbTitle = new StringBuilder("");
				String strTilte = "";
				if (Util.isEmpty(q.qTitle)) {
					q.qTitle = NativeModeActivity.this.getString(R.string.no_title);
				}
				if (!Util.isEmpty(q.qid)) {
					strTilte = q.qid + ". " + q.qTitle;
				} else {
					if (!Util.isEmpty(q.qTitle)) {
						strTilte = getResources().getString(R.string.question_order, q.qOrder) + q.qTitle;
					} else {
						strTilte = getResources().getString(R.string.question_order, q.qOrder);
					}

				}

				/**
				 * 标题逻辑引用
				 */
				CstmMatcher qutoMatcherList = Util.findMatcherItemList(strTilte, ma, qAnswer.uuid,q.surveyId);
				boolean qutoHave = Util.isEmpty(qutoMatcherList.getMis());
				if (!qutoHave) {
					strTilte = qutoMatcherList.getResultStr();
				}

				/**
				 * 引用受访者参数
				 */
				String parametersStr = feed.getParametersStr();
				ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
				if (!Util.isEmpty(parametersStr)) {
					ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
					if (!Util.isEmpty(tParameters)) {
						parameterList.addAll(tParameters);
					}
				}
				// System.out.println("parameterList.size():"+parameterList.size());
				CstmMatcher parameterMatcherList = Util.findMatcherPropertyItemList(strTilte, parameterList);
				boolean parameterHave = Util.isEmpty(parameterMatcherList.getMis());
				if (!parameterHave) {
					strTilte = parameterMatcherList.getResultStr();
				}

				String must = "<font color='red'>" + getResources().getString(R.string.question_must) + "</font>";

				/**
				 * 必填
				 */
				if (1 == q.qRequired) {
					strTilte = strTilte + must;
				}

				// 更改的样式
				ImageGetter imgGetter = new Html.ImageGetter() {
					public Drawable getDrawable(String source) {
						Drawable drawable = null;
						String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
						// System.out.println("name:" + name);
						drawable = Drawable.createFromPath(name);
						Bitmap image = BitmapFactory.decodeFile(name);
						if (image != null) {
							int tWidth = image.getWidth();
							int tHeight = image.getHeight();
							if(tWidth>maxCWidth){
								tWidth=maxCWidth;
								tHeight=maxCWidth/tWidth*tHeight;
							}
							drawable.setBounds(0, 0, tWidth, tHeight);
							return drawable;
						} else {
							return null;
						}
					}
				};
				Spanned fromHtml = Html.fromHtml(strTilte, imgGetter, null);
				tvTitle.setText(fromHtml);
				// 更改的样式
				if (1 == q.qRadomed) {
					String radomed = getResources().getString(R.string.question_random);
					SpannableString ss = new SpannableString(radomed);
					ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(), Spanned.SPAN_COMPOSING);
					tvTitle.append(ss);
				}
				// 提示
				if (0 < q.lowerBound || 0 < q.upperBound) {
					String bound = "";
					if (0 == q.upperBound) {
						bound = getResources().getString(R.string.question_bound_min, q.lowerBound);
					} else {
						bound = getResources().getString(R.string.question_bound, q.upperBound, q.lowerBound);
					}
					SpannableString ss = new SpannableString(bound);
					ss.setSpan(new ForegroundColorSpan(Color.RED), 0, bound.length(), Spanned.SPAN_COMPOSING);
					tvTitle.append(ss);
				}

				/**
				 * 增加单题拍照按钮 在最后面
				 */
				if (q.qCamera == 1 && !Util.isEmpty(feed.getUuid())) {
					System.out.println("单题拍照");
					// System.out.println("有单题拍照");
					Drawable drawable = this.getResources().getDrawable(R.drawable.new_camera);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					tvTitle.setCompoundDrawables(null, null, drawable, null);
					tvTitle.setOnClickListener(new CstOnClickListener(q));
				} else {
					tvTitle.setCompoundDrawables(null, null, null, null);
					// System.out.println("无单题拍照");
				}
				/**
				 * 增加单题签名
				 */
				if (q.qSign == 1 && !Util.isEmpty(feed.getUuid())) {
					ivSign.setVisibility(View.VISIBLE);
					ivSign.setOnClickListener(new CstOnClickListener(q));
				}
			}
			bodyView.setVisibility(View.VISIBLE);
		}

		// 单复选矩阵固定
		if (isFixed) {
			/**
			 * 不直接使用q.qCaption而使用中间变量qCaption, 因为直接使用q.qCaption会将其值改变,
			 * 在上一下、下一页多次操作后, 导致界面显示不正确
			 */
			String qCaption = q.qCaption;

			/**
			 * 假如题目有上方追加说明
			 */
			if (!Util.isEmpty(qCaption)) {
				/**
				 * 显示上方追加说明的控件
				 */
				tvCaption_new.setVisibility(View.VISIBLE);
				/**
				 * 设置上方追加说明的位置
				 */
				if (Cnt.POS_CENTER.equals(q.qCommentPosition)) {
					tvCaption_new.setGravity(Gravity.CENTER_HORIZONTAL);
				} else if (Cnt.POS_RIGHT.equals(q.qCommentPosition)) {
					tvCaption_new.setGravity(Gravity.RIGHT);
				} else if (Cnt.POS_LEFT.equals(q.qCommentPosition)) {
					tvCaption_new.setGravity(Gravity.LEFT);
				}

				// 更改的样式
				ImageGetter imgGetter = new Html.ImageGetter() {
					public Drawable getDrawable(String source) {
						Drawable drawable = null;
						String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
						// System.out.println("name:" + name);
						drawable = Drawable.createFromPath(name);
						Bitmap image = BitmapFactory.decodeFile(name);
						if (image != null) {
							int tWidth = image.getWidth();
							int tHeight = image.getHeight();
							if(tWidth>maxCWidth){
								tWidth=maxCWidth;
								tHeight=maxCWidth/tWidth*tHeight;
							}
							drawable.setBounds(0, 0, tWidth, tHeight);
							return drawable;
						} else {
							return null;
						}
					}
				};
				Spanned fromHtml = Html.fromHtml(qCaption, imgGetter, null);
				tvCaption_new.setText(fromHtml);
				// 更改的样式
			}
		} else {
			/**
			 * 不直接使用q.qCaption而使用中间变量qCaption, 因为直接使用q.qCaption会将其值改变,
			 * 在上一下、下一页多次操作后, 导致界面显示不正确
			 */
			String qCaption = q.qCaption;

			/**
			 * 假如题目有上方追加说明
			 */
			if (!Util.isEmpty(qCaption)) {
				/**
				 * 显示上方追加说明的控件
				 */
				tvCaption.setVisibility(View.VISIBLE);
				/**
				 * 设置上方追加说明的位置
				 */
				if (Cnt.POS_CENTER.equals(q.qCommentPosition)) {
					tvCaption.setGravity(Gravity.CENTER_HORIZONTAL);
				} else if (Cnt.POS_RIGHT.equals(q.qCommentPosition)) {
					tvCaption.setGravity(Gravity.RIGHT);
				} else if (Cnt.POS_LEFT.equals(q.qCommentPosition)) {
					tvCaption.setGravity(Gravity.LEFT);
				}

				// 更改的样式
				ImageGetter imgGetter = new Html.ImageGetter() {
					public Drawable getDrawable(String source) {
						Drawable drawable = null;
						String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
						// System.out.println("name:" + name);
						drawable = Drawable.createFromPath(name);
						Bitmap image = BitmapFactory.decodeFile(name);
						if (image != null) {
							int tWidth = image.getWidth();
							int tHeight = image.getHeight();
							if(tWidth>maxCWidth){
								tWidth=maxCWidth;
								tHeight=maxCWidth/tWidth*tHeight;
							}
							drawable.setBounds(0, 0, tWidth, tHeight);
							return drawable;
						} else {
							return null;
						}
					}
				};
				Spanned fromHtml = Html.fromHtml(qCaption, imgGetter, null);
				tvCaption.setText(fromHtml);
				// 更改的样式
			}
		}

		/**
		 * 不直接使用q.qComment而使用中间变量qComment, 因为直接使用q.qComment会将其值改变,
		 * 在上一下、下一页多次操作后, 导致界面显示不正确
		 */
		String qComment = q.qComment;
		/**
		 * 假如题目有下方追加说明
		 */
		if (!Util.isEmpty(qComment)) {
			/**
			 * 显示上方追加说明的控件
			 */
			tvComment.setVisibility(View.VISIBLE);
			System.out.println("q.qCommentPosition:" + q.qCommentPosition);
			/**
			 * 设置上方追加说明的位置
			 */
			if (Cnt.POS_CENTER.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.CENTER_HORIZONTAL);
			} else if (Cnt.POS_RIGHT.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.RIGHT);
			} else if (Cnt.POS_LEFT.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.LEFT);
			}

			// 更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
					// System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if (image != null) {
						int tWidth = image.getWidth();
						int tHeight = image.getHeight();
						if(tWidth>maxCWidth){
							tWidth=maxCWidth;
							tHeight=maxCWidth/tWidth*tHeight;
						}
						drawable.setBounds(0, 0, tWidth, tHeight);
						return drawable;
					} else {
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(q.qComment, imgGetter, null);
			tvComment.setText(fromHtml);
			// 更改的样式

		}

		// Answer an = tmpAnswerList.get(q.qIndex);
		// Answer an = ma.dbService.getAnswer(qAnswer.uuid, q.qIndex)
		ArrayList<AnswerMap> amList = new ArrayList<AnswerMap>();
		if (null != tempAnswer) {
			amList = tempAnswer.getAnswerMapArr();
		}
		// 大树 添加 双引用在里实现 双引用
		ArrayList<String> twoQsiteOptions = new ArrayList<String>(); // 双引用 题目
		if (!Util.isEmpty(q.qSiteOption2)) {
			// 单复选矩阵固定
			if (isFixed) {
				twoSiteNoticeTv.setVisibility(View.GONE);
			} else {
				twoSiteNoticeTv.setVisibility(View.VISIBLE);
			}
			if (q.qSiteOption2.indexOf(",") == -1) {
				twoQsiteOptions.add(q.qSiteOption2.toString().trim());
			} else {
				for (int i = 0; i < q.qSiteOption2.split(",").length; i++) {
					twoQsiteOptions.add(q.qSiteOption2.split(",")[i].toString().trim());
				}
			}
		} else {
			twoSiteNoticeTv.setVisibility(View.GONE);
		}
		// 大树 以上
		switch (q.qType) {
		case Cnt.TYPE_HEADER:// 标题
			// tvTitle.setMaxWidth(Integer.MAX_VALUE);
			break;

		case Cnt.TYPE_RADIO_BUTTON:// 单选按钮

			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);
			boolean isDumbOk = false;// 哑题 这一项是否成功
			/**
			 * 题型的横向、纵向摆放 追加说明新布局 && 0 == q.isHaveItemCap
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else if (Cnt.ORIENT_VERTICAL.equals(q.deployment)) {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			} else {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}

			/**
			 * 行
			 */
			ArrayList<QuestionItem> radioRows = new ArrayList<QuestionItem>();
			radioRows.addAll(q.getRowItemArr());
			if (!Util.isEmpty(radioRows)) {
				Answer an = null;
				// boolean isInclusion = false;
				/**
				 * 排斥
				 */
				if ("1".equals(q.qInclusion)) {
					an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (null != an) {
						ArrayList<Integer> have = new ArrayList<Integer>();
						for (AnswerMap am : an.getAnswerMapArr()) {
							if (!Util.isEmpty(am.getAnswerValue())) {
								System.out.println("am.getAnswerValue()):"+am.getAnswerValue());
//								 if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
//								 have.add(am.getRow());
//								 } else {
//								 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
//								 }
							    //   双引用but修改  
								if (Util.isFormat(am.getAnswerValue(), 9)) {
									have.add(Integer.valueOf(am.getAnswerValue()));
								}else {
									 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								}
							}
						}
						// 大树 一下 双引用
						if (!Util.isEmpty(q.qSiteOption2)) {
							// 大树 输出 把所有要排斥的选项添加到集合hava中
							if (twoQsiteOptions.size() > 0) {
								for (int i = 0; i < twoQsiteOptions.size(); i++) {
									Answer anQsite = ma.dbService.getAnswer(feed.getUuid(), twoQsiteOptions.get(i));
									if (anQsite != null && anQsite.getAnswerMapArr().size() > 0) {
										for (AnswerMap am : anQsite.getAnswerMapArr()) {
											/**
											 * 假如有值 自动查重 去除重复
											 */
											if (!Util.isEmpty(am.getAnswerValue())) {
//												 if (Cnt.TYPE_RADIO_BUTTON ==
//												 an.answerType) {
//												 have.add(am.getRow());
//												 } else {
//												 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
//												 }
												  //   双引用but修改  
												if (Util.isFormat(am.getAnswerValue(), 9)) {
													have.add(Integer.valueOf(am.getAnswerValue()));
												}else {
													 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
												}
											}
										}
									}
								}
							}
						}
						twoQsiteOptions.add(0, q.qSiteOption.trim());
						// 大树 以上部分
						have = Util.getSiteOption(feed, q, ma, have);
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							// have里面找到这个项
							if (-1 != have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
								/**
								 * 选择了这些选项
								 */
								radioRows.remove(qi);
							}
						}
						have.clear();
						have = null;
						mTempRows.addAll(radioRows);
					} else {
						mTempRows.addAll(radioRows);
					}
				} else if ("0".equals(q.qInclusion)) {
					/**
					 * 引用
					 */

					an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (an != null) {
						ArrayList<Integer> have = new ArrayList<Integer>();
						for (AnswerMap am : an.getAnswerMapArr()) {
							/**
							 * 假如有值
							 */
							if (!Util.isEmpty(am.getAnswerValue())) {
//								 if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
//								 have.add(am.getRow());
//								 } else {
//								 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
//								 }
								  //   双引用but修改  
								if (Util.isFormat(am.getAnswerValue(), 9)) {
									have.add(Integer.valueOf(am.getAnswerValue()));
								}else {
									 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								}
							}
						}
						// 大树 添加 双引用在里实现 以下： 双引用
						if (!Util.isEmpty(q.qSiteOption2)) {
							// 大树 输出 自动查重 的功能
							if (twoQsiteOptions.size() > 0) {
								for (int i = 0; i < twoQsiteOptions.size(); i++) {

									Answer anQsite = ma.dbService.getAnswer(feed.getUuid(), twoQsiteOptions.get(i));
									if (anQsite != null && anQsite.getAnswerMapArr().size() > 0) {
										for (AnswerMap am : anQsite.getAnswerMapArr()) {
											/**
											 * 假如有值 自动查重 去除重复
											 */
											if (!Util.isEmpty(am.getAnswerValue())) {
//												 if (Cnt.TYPE_RADIO_BUTTON ==
//												 an.answerType) {
//												 have.add(am.getRow());
//												 } else {
//												 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
//												 }
												  //   双引用but修改  
												if (Util.isFormat(am.getAnswerValue(), 9)) {
													have.add(Integer.valueOf(am.getAnswerValue()));
												}else {
													 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
												}
											}
										}
									}
								}
							}

						}
						twoQsiteOptions.add(0, q.qSiteOption.trim());
						//引用没有用
						have = Util.getSiteOption(feed, q, ma, have);
						
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (-1 == have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
								/**
								 * 没有选择的, 则移走
								 */
								radioRows.remove(qi);
							}
						}
						have.clear();
						have = null;
						mTempRows.addAll(radioRows);
					} else {
						radioRows.removeAll(radioRows);
						mTempRows.addAll(radioRows);
					}
				}

				/**
				 * 暂存单选题的所有选项
				 */
				ArrayList<QuestionItem> tempRows = new ArrayList<QuestionItem>();
				// 加radioRows
				tempRows.addAll(radioRows);

				/**
				 * 暂存单选题的其他项,因为其他项不需要随机,所以单独处理
				 */
				ArrayList<QuestionItem> otherItems = new ArrayList<QuestionItem>();

				Random rand = new Random();

				ArrayList<LinearLayout> arrLayout = new ArrayList<LinearLayout>();
				/**
				 * 需要分组 每行显示几个。没有说明,水平布局 追加说明新布局 && 0 == q.isHaveItemCap
				 */
				// 大树 添加 垂直结构的 实现 提示语 水平结构 提示语 都在这里 引用排斥 双引用
				// 双引用 修改部分
				if (!Util.isEmpty(q.qSiteOption2)) {
					twoSiteNoticeTv.setTextColor(Color.RED);
					twoSiteNoticeTv.setTextSize(lowSurveySize);
					StringBuilder sb1 = new StringBuilder();
					if (twoQsiteOptions.size() > 0) {
						for (int i = 0; i < twoQsiteOptions.size(); i++) {
							Question q = ma.dbService.getQuestion(feed.getSurveyId(), twoQsiteOptions.get(i));
							if (null != q) {
								if (!Util.isEmpty(q.qOrder + "")) {
									if (!Util.isEmpty(q.qid)) {
										sb1.append(q.qid + ",");
									} else {
										sb1.append("Q" + q.qOrder + ",");
									}
								} else
									Log.i("zrl1", "qid为空");
							}
						}
					}
					if (q.qInclusion.equals("0") && -1 != sb1.toString().indexOf(",")) {
						twoSiteNoticeTv.setText(NativeModeActivity.this.getString(R.string.each_answer_associated) + sb1.substring(0, sb1.toString().lastIndexOf(",")).toString()
								+ NativeModeActivity.this.getString(R.string.answer_value_reference));
					} else if (q.qInclusion.equals("1") && -1 != sb1.toString().indexOf(",")) {
						twoSiteNoticeTv.setText(NativeModeActivity.this.getString(R.string.each_answer_associated) + sb1.substring(0, sb1.toString().lastIndexOf(",")).toString()
								+ NativeModeActivity.this.getString(R.string.answer_value_rejection));
					}
				}
				// 大树 以上部分

				if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {

					for (int i = 0; i < q.rowsNum; i++) {
						LinearLayout ll = new LinearLayout(this);
						ll.setOrientation(LinearLayout.VERTICAL);
						if (0 == i % 2) {
							ll.setBackgroundColor(Color.parseColor("#10000050"));
						}
						ll.setPadding(0, 0, 20, 0);
						ll.setLayoutParams(WRAP_WRAP);
						arrLayout.add(ll);
						bodyView.addView(ll, bodyView.getChildCount());
					}
				}

				/**
				 * 题外关联 之 选项置顶 设置 标示 置顶 选项 置底 isItemBottom
				 * 
				 * 获取 所有选项置顶的 选项 集合
				 */
				boolean isItemStick = ComUtil.isItemStick(tempRows)[0];
				int[] itemList = ComUtil.getItemStick(tempRows);// 置顶标示位置 数组
				int counter = 0; // 计数器
				QuestionItem botItem = null;
				/**
				 * 题外关联 之选项置顶 给其他项 itemvalue 赋值 并且删除掉 置底
				 */
				if (isItemStick) {
					for (int i = 0; i < tempRows.size(); i++) {
						QuestionItem item = tempRows.get(i);
						if (item.isOther == 1) {
							item.itemValue = i;
						}
					}
				}

				/**
				 * 处理随机项
				 */
				for (int i = 0; i < radioRows.size(); i++) {
					QuestionItem item;

					// 题外关联 之 选项置顶 文本 获取

					/**
					 * 假如题目要求选项随机
					 */
					if (1 == q.qRadomed) {

						/**
						 * 随机产生数组的下标值
						 */
						int index = rand.nextInt(tempRows.size());
						/**
						 * 取得随机产生的选项对象item
						 * 
						 */
						item = tempRows.get(index);

						/**
						 * 题外关联 之选项 置顶 选项的 操作 进行交换 得到 padding==1 等于置顶
						 * 
						 * 题外关联 之选项置底 选项的操作 进行交换 得到 padding==2 等于 置底
						 */

						if (1 != item.isOther) {

							if (null != item.padding && counter == 0 && item.padding != 1) {
								if (isItemStick) {
									index = itemList[0];
									item = tempRows.get(index);
								}
							}
							counter++;
							// 删除掉 置底 的选项
							if (null != item.padding && item.padding == 2) {
								botItem = item;
								tempRows.remove(index);
								continue;
							}
						}

						/**
						 * 在暂存数组中移除随机产生的选项对象item
						 */
						tempRows.remove(index);
					} else {
						/**
						 * 假如题目的选项没有要求随机
						 */
						item = tempRows.get(i);
					}

					/**
					 * 生成一个单选按钮
					 */
					RadioButton rb = new RadioButton(NativeModeActivity.this);
					if (1 == item.isOther) {// 其他项
//						System.out.println("其他项");
						/**
						 * 将其他项暂存在otherItems集合中,此处不显示,留在最后显示
						 */
						otherItems.add(item);
					} else {// 单选项
						TextView tvItemCap = new TextView(NativeModeActivity.this);
						/**
						 * 
						 * 选项的追加说明 ,有追加说明的情况
						 */
						if (!Util.isEmpty(item.caption)) {
							// 单选追加说明方法
							if (1 == item.caption_check) {
								rb.setOnCheckedChangeListener(new MyRadioButtonClick(item.caption));
							} else {
								// 追加说明新布局 原先是FILL
								tvItemCap.setLayoutParams(WRAP_WRAP);
								tvItemCap.setTextColor(Color.GRAY);// 统计局专有页面
								tvItemCap.setTextSize(lowSurveySize);

								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable.createFromPath(name);
										Bitmap image = BitmapFactory.decodeFile(name);
										if (image != null) {
											int tWidth = image.getWidth();
											int tHeight = image.getHeight();
											if(tWidth>maxCWidth){
												tWidth=maxCWidth;
												tHeight=maxCWidth/tWidth*tHeight;
											}
											drawable.setBounds(0, 0, tWidth, tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(item.caption, imgGetter, null);
								tvItemCap.setText(fromHtml);
								// 更改的样式
							}
						}

						// 单选百分比开始 原单选 现单选
						// rb.setLayoutParams(WRAP_WRAP);
						if (1 < q.rowsNum && 0 == q.isHaveItemCap && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
							rb.setLayoutParams(new LayoutParams(screenWidth / q.rowsNum - 20, LayoutParams.WRAP_CONTENT));
						} else {
							rb.setLayoutParams(new LayoutParams(screenWidth, LayoutParams.WRAP_CONTENT));
						}
						// 百分比结束
						String idStr = q.qIndex + "_" + i;
						rb.setId(idStr.hashCode());
						// rb.setButtonDrawable(R.drawable.small_radiobutton_temp);
						rb.setButtonDrawable(R.drawable.small_radiobutton);
						rb.setBackgroundResource(R.drawable.small_radiobutton_background);
						rb.setTextColor(Color.BLACK);
						rb.setTextSize(lowSurveySize);

						// 更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
								// System.out.println("name:" + name);
								drawable = Drawable.createFromPath(name);
								Bitmap image = BitmapFactory.decodeFile(name);
								if (image != null) {
									int tWidth = image.getWidth();
									int tHeight = image.getHeight();
									if(tWidth>maxCWidth){
										tWidth=maxCWidth;
										tHeight=maxCWidth/tWidth*tHeight;
									}
									drawable.setBounds(0, 0, tWidth, tHeight);
									return drawable;
								} else {
									return null;
								}
							}
						};
						Spanned fromHtml = Html.fromHtml(item.getItemText(), imgGetter, null);
						rb.setText(fromHtml);
						if (null != item.padding && item.padding == 1) {
							String radomed = getResources().getString(R.string.option_top);
							SpannableString ss = new SpannableString(radomed);
							ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(), Spanned.SPAN_COMPOSING);
							rb.append(ss);
						}
						// 更改的样式
						//条件隐藏选项
						setHideRb(item, rb);
						/**
						 * 判断是否是哑题,然后逻辑判断。 新哑题
						 */
						// TODO
						// 假如是哑题 开始判断
						if (1 == q.qDumbFlag) {
							item.isDumbOk = false;
							HashMap<String, Object> map= setDumbRb(item, rb, isDumbOk, operType);
							isDumbOk=(Boolean) map.get("isDumbOk");
							item=(QuestionItem) map.get("item");
						}
						/**
						 * 新哑题 判断哑题结束
						 */
						String name = Util.GetAnswerName(q, item, 0, 0, false);
						if (1 == q.qPreSelect) {
							// 假如没有哑题，就预设，
							if (1 == item.deft) {
								if (Util.isEmpty(amList)) {
									rb.setChecked(true);
									rb.setTag(item);
								} else {
									for (AnswerMap am : amList) {
										if (name.equals(am.getAnswerName()) && String.valueOf(item.itemValue).equals(am.getAnswerValue())) {
											// System.out.println("ddddddddd");
											rb.setChecked(true);
											rb.setTag(item);
											break;
										}
									}
								}
							} else {
								// System.out.println("不是预选项");
								if (!Util.isEmpty(amList)) {// 题目有答案
									// System.out.println("题目有答案");
									for (AnswerMap am : amList) {
										if (name.equals(am.getAnswerName()) && String.valueOf(item.itemValue).equals(am.getAnswerValue())) {
											// System.out.println("ddddddddd");
											rb.setChecked(true);
											rb.setTag(item);
											break;
										}
									}
								}
							}
						} else {
							// System.out.println("不是预选项");
							if (!Util.isEmpty(amList)) {// 题目有答案
								// System.out.println("题目有答案");
								for (AnswerMap am : amList) {
									if (name.equals(am.getAnswerName()) && String.valueOf(item.itemValue).equals(am.getAnswerValue())) {
										// System.out.println("ddddddddd");
										rb.setChecked(true);
										rb.setTag(item);
										break;
									}
								}
							}
						}
						// 单选自动下一页
						rb.setOnClickListener(new RadioListenner(idStr.hashCode(), 0, 0, item, 1));
						// if (Util.isEmpty(q.qSiteOption)) {
						/**
						 * 假如选项说明不为空
						 */
						if (!Util.isEmpty(item.caption)) {
							// 追加说明方法
							if (1 == item.caption_check) {

							} else {
								if (0 < arrLayout.size()) {
									LinearLayout ll = arrLayout.get((i % q.rowsNum));
									ll.addView(tvItemCap, ll.getChildCount());
								} else {
									bodyView.addView(tvItemCap, bodyView.getChildCount());
								}
							}
						}

						/**
						 * 单选题目选项有图片
						 */
						if (0 < arrLayout.size()) {
							LinearLayout ll = arrLayout.get((i % q.rowsNum));
							ll.addView(rb, ll.getChildCount());
						} else {
							bodyView.addView(rb, bodyView.getChildCount());

						}
						// 将没有其他项的单选按钮加入集合中
						vs.add(rb);
					}

				}

				/**
				 * 题外关联 之 选项置顶 其他项的的处理 把置底的选项 扔到 其他项里头 是最好的选择
				 */
				if (botItem != null) {
					otherItems.add(botItem);
				}

				// Collections.sort()
				// 处理其他项, 因为其他项不需要随机
				for (int i = 0; i < otherItems.size(); i++) {
					// 获取其他项item
					QuestionItem item = otherItems.get(i);

					RadioButton rb = new RadioButton(NativeModeActivity.this);
					if (-1 != item.itemValue) {
						LinearLayout otherLayout = new LinearLayout(NativeModeActivity.this);
						otherLayout.setOrientation(LinearLayout.HORIZONTAL);
						otherLayout.setLayoutParams(WRAP_WRAP);
						rb.setLayoutParams(WRAP_WRAP);
						rb.setFocusable(false);
						// rb.setButtonDrawable(R.drawable.small_radiobutton_temp);
						rb.setButtonDrawable(R.drawable.small_radiobutton);
						rb.setBackgroundResource(R.drawable.small_radiobutton_background);
						rb.setTextColor(Color.BLACK);
						rb.setTextSize(lowSurveySize);
						// rb.setText(item.getItemText());
						// 更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
								// System.out.println("name:" + name);
								drawable = Drawable.createFromPath(name);
								Bitmap image = BitmapFactory.decodeFile(name);
								if (image != null) {
									int tWidth = image.getWidth();
									int tHeight = image.getHeight();
									if(tWidth>maxCWidth){
										tWidth=maxCWidth;
										tHeight=maxCWidth/tWidth*tHeight;
									}
									drawable.setBounds(0, 0, tWidth, tHeight);
									return drawable;
								} else {
									return null;
								}
							}
						};
						Spanned fromHtml = Html.fromHtml(item.getItemText(), imgGetter, null);
						rb.setText(fromHtml);
						if (null != item.padding && item.padding == 1 && 1 != item.isOther) {
							String radomed = getResources().getString(R.string.option_bottom);
							SpannableString ss = new SpannableString(radomed);
							ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(), Spanned.SPAN_COMPOSING);
							rb.append(ss);
						}
						// 更改的样式

						String idStr = "ohter_" + q.qIndex + "_" + i;
						rb.setId(idStr.hashCode());
						// System.out.println("radio_id="+idStr.hashCode());
						rb.setOnClickListener(new RadioListenner(idStr.hashCode(), 1, (idStr + "_et").hashCode(), item));

						EditText et = new EditText(NativeModeActivity.this);
						et.setLayoutParams(WRAP_WRAP);
						et.setMinimumWidth(150);
						// et.setId(free.itemValue);
						et.setId((idStr + "_et").hashCode());
						if (!Util.isEmpty(amList)) {
							// System.out.println("amList != null");
							String name = Util.GetAnswerName(q, item, 0, 0, false);
							String etName = Util.GetAnswerName(q, item, 0, 0, true);
							// System.out.println("etName--->"+etName);
							for (AnswerMap am : amList) {
								// System.out.println("am="+am.getAnswerName());
								if (name.equals(am.getAnswerName()) && String.valueOf(item.itemValue).equals(am.getAnswerValue())) {
									// System.out.println("ddddddddd");
									rb.setChecked(true);
									rb.setTag(item);
								}
								if (etName.equals(am.getAnswerName())) {
									// System.out.println("etName=" + etName +
									// ", etValue=" + am.getAnswerValue());
									et.setText(am.getAnswerValue());
									et.setTag(item);
								}
							}
						}
						/**
						 * 没有预选项
						 */
						if (Util.isEmpty(q.qSiteOption)) {
							otherLayout.addView(rb, otherLayout.getChildCount());
							vs.add(rb);
							/**
							 * 题外关联 之 选项置顶 去掉文本框 如果是置底
							 */
							if (1 == item.isOther) {
								otherLayout.addView(et, otherLayout.getChildCount());
							}

							if (0 < arrLayout.size()) {
								LinearLayout ll = arrLayout.get((radioRows.size() - otherItems.size() + i) % q.rowsNum);
								ll.addView(otherLayout, ll.getChildCount());
							} else {
								bodyView.addView(otherLayout, bodyView.getChildCount());
							}
							// 将文本框加入集合中
							vs.add(et);
						} else {
							// System.out.println("2");
							/**
							 * 数据库中没有答案
							 */
							if ((null == an || Util.isEmpty(an.getAnswerMapArr())||("1".equals(q.qInclusion)))) {
								otherLayout.addView(rb, otherLayout.getChildCount());
								vs.add(rb);
								/**
								 * 题外关联 之 选项置顶 去掉文本框 如果是置底
								 */
								if (1 == item.isOther) {
									otherLayout.addView(et, otherLayout.getChildCount());
								}
								// otherLayout.addView(et,
								// otherLayout.getChildCount());
								if (0 < arrLayout.size()) {
									LinearLayout ll = arrLayout.get((radioRows.size() - otherItems.size() + i) % q.rowsNum);
									ll.addView(otherLayout, ll.getChildCount());
								} else {
									bodyView.addView(otherLayout, bodyView.getChildCount());
								}
								// 将文本框假如结合中
								vs.add(et);
							} else {
								/**
								 * 数据库中有答案
								 */
								for (AnswerMap am : an.getAnswerMapArr()) {
									String value = am.getAnswerValue();
									if (String.valueOf(item.itemValue).equals(value)) {
										if (-1 == otherLayout.indexOfChild(rb)) {
											otherLayout.addView(rb, otherLayout.getChildCount());
										}
										if (-1 == vs.indexOf(rb)) {
											vs.add(rb);
										}
										if (0 < arrLayout.size()) {
											LinearLayout ll = arrLayout.get((radioRows.size() - otherItems.size() + i) % q.rowsNum);
											if (-1 == ll.indexOfChild(otherLayout) && -1 == bodyView.indexOfChild(otherLayout)) {
												ll.addView(otherLayout, ll.getChildCount());
											}
										} else {
											if (-1 == bodyView.indexOfChild(otherLayout)) {
												bodyView.addView(otherLayout, bodyView.getChildCount());
											}
										}
									} else {
//										int idx = bodyView.indexOfChild(otherLayout);
//										if (-1 == idx) {
//											otherLayout.addView(rb, otherLayout.getChildCount());
//											vs.add(rb);
//											bodyView.addView(otherLayout, bodyView.getChildCount());
//										}
									}
									String name = am.getAnswerName();
									if (!Util.isEmpty(name)) {
										String[] d = name.split("_");
										if (String.valueOf(item.itemValue).equals(d[3]) && "2".equals(d[4])) {
											rb.append(" " + am.getAnswerValue());
										}
									}
								}
							}
						}
					}
				}

			}
			// 哑题赋完值，自动翻页。
			if (isDumbOk) {
				// int templ=-1;
				for (int l = 0; l < vs.size(); l++) {
					View tempV = vs.get(l);
					QuestionItem tag = (QuestionItem) tempV.getTag();
					if (tag != null) {
						// System.out.println("tag:"+tag.itemText);
						// System.out.println("tag.isDumbOk:"+tag.isDumbOk);
						if (!tag.isDumbOk) {
							tempV.setTag(null);
						}
					}
					// if(templ!=-1){
					// vs.remove(templ);
					// }
				}
				// 判断是上一页，还是下一页，是翻页还是回页
				if (MSG_NEXT == operType) {
					// 是下一页，自动翻页 //哑题中没有验证的翻页
					nextPage(true);
				} else {
					// 是上一页，自动返回上一页
					backPage();
				}
			} else {
				if (1 == q.qDumbFlag) {
					// 哑题没有对应选项清空
					if (MSG_NEXT == operType) {

					}
				}
			}
			// TODO
			break;
		case Cnt.TYPE_CHECK_BOX:// 复选框
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);
			// System.out.println("复选题");
			/**
			 * 题型的横向、纵向摆放 追加说明新布局&& 0 == q.isHaveItemCap
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else if (Cnt.ORIENT_VERTICAL.equals(q.deployment)) {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			} else {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}

			ArrayList<QuestionItem> checkRows = new ArrayList<QuestionItem>();
			checkRows.addAll(q.getRowItemArr());
			Random rand = new Random();
			if (!Util.isEmpty(checkRows)) {
				// Answer an = tmpAnswerList.get(q.qIndex);
				// ArrayList<AnswerMap> amList = null;
				// if(null != an){
				// amList = an.getAnswerMapArr();
				// }

				Answer an = null;
				// boolean isInclusion = false;

				/**
				 * 排斥
				 */
				if ("1".equals(q.qInclusion)) {
					// isInclusion = true;
					// Question _q =
					// ma.dbService.getQuestion(feed.getSurveyId(),
					// q.qSiteOption);
					an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (null != an) {
						ArrayList<Integer> have = new ArrayList<Integer>();
						for (AnswerMap am : an.getAnswerMapArr()) {
							/**
							 * 假如有值
							 */
							if (!Util.isEmpty(am.getAnswerValue())) {
//								 if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
//								 // 单选加法
//								 have.add(am.getRow());
//								 } else {
//								 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
//								 }
								  //   双引用but修改  
								if (Util.isFormat(am.getAnswerValue(), 9)) {
									have.add(Integer.valueOf(am.getAnswerValue()));
								}else {
									 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								}
							}
						}
						// 大树 一下 双引用
						if (!Util.isEmpty(q.qSiteOption2)) {
							// 大树 输出 把所有要排斥的选项添加到集合hava中
							if (twoQsiteOptions.size() > 0) {
								for (int i = 0; i < twoQsiteOptions.size(); i++) {
									Answer anQsite = ma.dbService.getAnswer(feed.getUuid(), twoQsiteOptions.get(i));
									if (anQsite != null && anQsite.getAnswerMapArr().size() > 0) {
										for (AnswerMap am : anQsite.getAnswerMapArr()) {
											/**
											 * 假如有值 自动查重 去除重复
											 */
											if (!Util.isEmpty(am.getAnswerValue())) {
//												 if (Cnt.TYPE_RADIO_BUTTON ==
//												 an.answerType) {
//												 have.add(am.getRow());
//												 } else {
//												 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
//												 }
												  //   双引用but修改  
												if (Util.isFormat(am.getAnswerValue(), 9)) {
													have.add(Integer.valueOf(am.getAnswerValue()));
												}else {
													 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
												}
											}
										}
									}
								}
							}
						}
						twoQsiteOptions.add(0, q.qSiteOption.trim());
						// 大树 以上部分
						have = Util.getSiteOption(feed, q, ma, have);
						for (int z = 0; z < have.size(); z++) {
							System.out.println(z + ":" + have.get(z));
						}
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (-1 != have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
								/**
								 * 选择了这些选项
								 */
								checkRows.remove(qi);
							}
						}
						have.clear();
						have = null;
						mTempRows.addAll(checkRows);
					} else {
						mTempRows.addAll(checkRows);
					}
				} else if ("0".equals(q.qInclusion)) {
					/**
					 * 引用
					 */
					// isInclusion = true;
					// Question _q =
					// ma.dbService.getQuestion(feed.getSurveyId(),
					// q.qSiteOption);
					an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (an != null) {
						ArrayList<Integer> have = new ArrayList<Integer>();
						for (AnswerMap am : an.getAnswerMapArr()) {
							/**
							 * 假如有值
							 */
							if (!Util.isEmpty(am.getAnswerValue())) {
//								 if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
//								 have.add(am.getRow());
//								 } else {
//								 String answerValue = am.getAnswerValue();
//								 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
//								 }
								  //   双引用but修改  
								if (Util.isFormat(am.getAnswerValue(), 9)) {
									have.add(Integer.valueOf(am.getAnswerValue()));
								}else {
									 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								}
							}
						}
						// 大树 添加 双引用在里实现 以下： 双引用
						if (!Util.isEmpty(q.qSiteOption2)) {
							// 大树 输出 自动查重 的功能
							if (twoQsiteOptions.size() > 0) {
								for (int i = 0; i < twoQsiteOptions.size(); i++) {

									Answer anQsite = ma.dbService.getAnswer(feed.getUuid(), twoQsiteOptions.get(i));
									if (anQsite != null && anQsite.getAnswerMapArr().size() > 0) {
										for (AnswerMap am : anQsite.getAnswerMapArr()) {
											/**
											 * 假如有值 自动查重 去除重复
											 */
											if (!Util.isEmpty(am.getAnswerValue())) {
//												 if (Cnt.TYPE_RADIO_BUTTON ==
//												 an.answerType) {
//												 have.add(am.getRow());
//												 } else {
//												 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
//												 }
												  //   双引用but修改  
												if (Util.isFormat(am.getAnswerValue(), 9)) {
													have.add(Integer.valueOf(am.getAnswerValue()));
												}else {
													 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
												}
											}
										}
									}
								}
							}

						}
						twoQsiteOptions.add(0, q.qSiteOption.trim());
						// 大树 以上
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (-1 == have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
								/**
								 * 没有选择的, 则移走
								 */
								checkRows.remove(qi);
							}
						}
						have.clear();
						have = null;
						mTempRows.addAll(checkRows);
					} else {
						checkRows.removeAll(checkRows);
						mTempRows.addAll(checkRows);
					}
				}

				ArrayList<QuestionItem> tempRows = new ArrayList<QuestionItem>();
				tempRows.addAll(checkRows);

				ArrayList<QuestionItem> otherRows = new ArrayList<QuestionItem>();

				ArrayList<LinearLayout> arrLayout = new ArrayList<LinearLayout>();
				// 大树 添加 垂直结构的 实现 提示语 水平结构 提示语 都在这里 引用排斥 双引用
				if (!Util.isEmpty(q.qSiteOption2)) {
					twoSiteNoticeTv.setTextColor(Color.RED);
					twoSiteNoticeTv.setTextSize(lowSurveySize);
					StringBuilder sb1 = new StringBuilder();
					if (twoQsiteOptions.size() > 0) {
						for (int i = 0; i < twoQsiteOptions.size(); i++) {
							Question q = ma.dbService.getQuestion(feed.getSurveyId(), twoQsiteOptions.get(i));
							if (null != q) {
								if (!Util.isEmpty(q.qOrder + "")) {
									if (!Util.isEmpty(q.qid)) {
										sb1.append(q.qid + ",");
									} else {
										sb1.append("Q" + q.qOrder + ",");
									}
								} else
									Log.i("zrl1", "qid为空");
							}
						}
					}
					if (q.qInclusion.equals("0") && -1 != sb1.toString().indexOf(",")) {
						twoSiteNoticeTv.setText(NativeModeActivity.this.getString(R.string.each_answer_associated) + sb1.substring(0, sb1.toString().lastIndexOf(",")).toString()
								+ NativeModeActivity.this.getString(R.string.answer_value_reference));
					} else if (q.qInclusion.equals("1") && -1 != sb1.toString().indexOf(",")) {
						twoSiteNoticeTv.setText(NativeModeActivity.this.getString(R.string.each_answer_associated) + sb1.substring(0, sb1.toString().lastIndexOf(",")).toString()
								+ NativeModeActivity.this.getString(R.string.answer_value_rejection));
					}
				}
				// 大树 以上部分
				/**
				 * 需要分组 追加说明新布局&& 0 == q.isHaveItemCap
				 */
				if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
					for (int i = 0; i < q.rowsNum; i++) {
						LinearLayout ll = new LinearLayout(this);
						ll.setOrientation(LinearLayout.VERTICAL);
						if (0 == i % 2) {
							ll.setBackgroundColor(Color.parseColor("#10000050"));
						}
						ll.setPadding(0, 0, 20, 0);
						// (screenWidth-20)/q.rowsNum, LayoutParams.WRAP_CONTENT
						ll.setLayoutParams(WRAP_WRAP);
						arrLayout.add(ll);
						bodyView.addView(ll, bodyView.getChildCount());
					}
				}
				/**
				 * 题外关联 之 选项置顶 设置 标示 置顶 选项 置底 isItemBottom
				 * 
				 * 获取 所有选项置顶的 选项 集合
				 */
				boolean isItemStick = ComUtil.isItemStick(tempRows)[0];
				int[] itemList = ComUtil.getItemStick(tempRows);// 置顶标示位置 数组
				int counter = 0; // 计数器
				QuestionItem botItem = null;
				for (int i = 0; i < checkRows.size(); i++) {
					QuestionItem item;

					if (1 == q.qRadomed) {
						/**
						 * 随机产生数组的下标值
						 */
						int index = rand.nextInt(tempRows.size());
						/**
						 * 取得随机产生的选项对象item
						 */

						item = tempRows.get(index);

						/**
						 * 题外关联 之选项 置顶 选项的 操作 进行交换 得到 padding==1 等于置顶
						 * 
						 * 题外关联 之选项置底 选项的操作 进行交换 得到 padding==2 等于 置底
						 */

						if (1 != item.isOther) {

							if (null != item.padding && counter == 0 && item.padding != 1) {
								if (isItemStick) {
									index = itemList[0];
									item = tempRows.get(index);
								}
							}
							counter++;
							// 删除掉 置底 的选项
							if (null != item.padding && item.padding == 2) {
								botItem = item;
								tempRows.remove(index);
								continue;
							}
						}

						/**
						 * 在暂存数组中移除随机产生的选项对象item
						 */
						tempRows.remove(index);
					} else {
						item = checkRows.get(i);
					}

					if (1 == item.isOther) {
						otherRows.add(item);
					} else {
						CheckBox cb = new CheckBox(NativeModeActivity.this);
						// 隐藏选项
						//条件隐藏选项
						setHideRb(item, cb);
						//条件隐藏选项
						cb.setTag(item);
						// 复选追加说明方法
						TextView tvItemCap = new TextView(NativeModeActivity.this);
						if (!Util.isEmpty(item.caption)) {
							if (1 == item.caption_check) {
								cb.setOnCheckedChangeListener(new MyRadioButtonClick(item.caption));
							} else {
								// 追加说明新布局原FILL_WRAP
								tvItemCap.setLayoutParams(WRAP_WRAP);
								tvItemCap.setTextColor(Color.GRAY);// 统计局专有页面
								tvItemCap.setTextSize(lowSurveySize);
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable.createFromPath(name);
										Bitmap image = BitmapFactory.decodeFile(name);
										if (image != null) {
											int tWidth = image.getWidth();
											int tHeight = image.getHeight();
											if(tWidth>maxCWidth){
												tWidth=maxCWidth;
												tHeight=maxCWidth/tWidth*tHeight;
											}
											drawable.setBounds(0, 0, tWidth, tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(item.caption, imgGetter, null);
								tvItemCap.setText(fromHtml);
								// 更改的样式
							}
						}
						// 复选追加说明方法
						// CheckBox cb = new CheckBox(this);
						String idStr = q.qIndex + "_" + i;
						cb.setId(idStr.hashCode());
						// 复选百分比 原复选，现在复选
						// cb.setLayoutParams(WRAP_WRAP);
						if (1 < q.rowsNum && 0 == q.isHaveItemCap && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
							cb.setLayoutParams(new LayoutParams(screenWidth / q.rowsNum - 20, LayoutParams.WRAP_CONTENT));
						} else {
							cb.setLayoutParams(new LayoutParams(screenWidth, LayoutParams.WRAP_CONTENT));
						}
						// 复选百分比结束
						cb.setBackgroundResource(R.drawable.small_checkbox_background);
						cb.setButtonDrawable(R.drawable.small_checkbox);
						cb.setTextSize(lowSurveySize);
						cb.setTextColor(Color.BLACK);
						// cb.setText(item.getItemText());

						// 更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = NativeModeActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator + source;
								// System.out.println("name:" + name);
								drawable = Drawable.createFromPath(name);
								Bitmap image = BitmapFactory.decodeFile(name);
								if (image != null) {
									int tWidth = image.getWidth();
									int tHeight = image.getHeight();
									if(tWidth>maxCWidth){
										tWidth=maxCWidth;
										tHeight=maxCWidth/tWidth*tHeight;
									}
									drawable.setBounds(0, 0, tWidth, tHeight);
									return drawable;
								} else {
									return null;
								}
							}
						};
						Spanned fromHtml = Html.fromHtml(item.getItemText(), imgGetter, null);
						cb.setText(fromHtml);
						if (null != item.padding && item.padding == 1) {
							String radomed = getResources().getString(R.string.option_top);
							SpannableString ss = new SpannableString(radomed);
							ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(), Spanned.SPAN_COMPOSING);
							cb.append(ss);
						}
						// 更改的样式
						cb.setOnClickListener(new CheckListener(cb, null, item));
						if (!Util.isEmpty(amList)) {
							// System.out.println("复选题以前的答案集合不为空");
							String chName = Util.GetAnswerName(q, item, 0, 0, false);
							// System.out.println("chName--->"+chName+", value="+item.itemValue);
							for (AnswerMap am : amList) {
								// System.out.println("name="+am.getAnswerName()+", value="+am.getAnswerValue());
								if (chName.equals(am.getAnswerName()) && am.getAnswerValue().equals(String.valueOf(item.itemValue))) {
									cb.setChecked(true);
									// cb.setTag(item);
									// System.out.println("复选题以前的答案配对");
								}
							}
						}
						// 追加说明方法复选
						if (!Util.isEmpty(item.caption)) {
							if (1 == item.caption_check) {

							} else {
								if (0 < arrLayout.size()) {
									LinearLayout ll = arrLayout.get((i % q.rowsNum));
									ll.addView(tvItemCap, ll.getChildCount());
								} else {
									bodyView.addView(tvItemCap, bodyView.getChildCount());
								}
							}
						}

						/**
						 * 单选题目选项有图片
						 */
						if (0 < arrLayout.size()) {
							LinearLayout ll = arrLayout.get((i % q.rowsNum));
							ll.addView(cb, ll.getChildCount());
						} else {
							bodyView.addView(cb, bodyView.getChildCount());
						}
						vs.add(cb);
					}
				}

				/**
				 * 题外关联 之 选项置顶 其他项的的处理 把置底的选项 扔到 其他项里头 是最好的选择
				 */
				if (botItem != null) {
					otherRows.add(botItem);
				}
				for (int i = 0; i < otherRows.size(); i++) {
					QuestionItem item = otherRows.get(i);
					CheckBox cb = new CheckBox(NativeModeActivity.this);
					cb.setTag(item);
					if (-1 != item.itemValue) {// 并且不是<freeInput/>这种空标签
						LinearLayout otherLayout = new LinearLayout(NativeModeActivity.this);
						otherLayout.setLayoutParams(WRAP_WRAP);
						cb.setLayoutParams(WRAP_WRAP);
						String idStr = "ohter_" + q.qIndex + "_" + i;
						cb.setId(idStr.hashCode());
						cb.setBackgroundResource(R.drawable.small_checkbox_background);
						cb.setButtonDrawable(R.drawable.small_checkbox);
						cb.setTextSize(lowSurveySize);
						cb.setTextColor(Color.BLACK);
						/**
						 * 不为空
						 */
						CstmMatcher cm = Util.findFontMatcherList(item.getItemText());
						if (!Util.isEmpty(cm.getMis())) {
							SpannableString ss = new SpannableString(cm.getResultStr());
							for (MatcherItem mi : cm.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= item.getItemText().length())
									ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
							cb.setText(ss);
							//
						} else {

							/**
							 * 题外关联 之 选项置顶 提示语 的显示 用于 置底
							 */
							SpannableString ss1 = new SpannableString(item.getItemText() + "  " + this.getString(R.string.option_bottom));
							if (1 != item.isOther) {
								ss1.setSpan(new ForegroundColorSpan(Color.RED), item.getItemText().length(), item.getItemText().length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								cb.setText(ss1);
							} else {
								cb.setText(item.getItemText());
							}
							// cb.setText(item.getItemText());
						}

						otherLayout.addView(cb, otherLayout.getChildCount());
						vs.add(cb);

						EditText et = new EditText(NativeModeActivity.this);
						et.setTag(item);
						et.setLayoutParams(WRAP_WRAP);
						et.setMinimumWidth(150);
						if (!Util.isEmpty(amList)) {
							// System.out.println("复选题以前的答案集合不为空");
							String chName = Util.GetAnswerName(q, item, 0, 0, false);
							// System.out.println("chName--->"+chName+", value="+item.itemValue);
							String etName = Util.GetAnswerName(q, item, 0, 0, true);
							// System.out.println("etName:"+etName);
							for (AnswerMap am : amList) {
								// System.out.println("name="+am.getAnswerName()+", value="+am.getAnswerValue());
								if (chName.equals(am.getAnswerName()) && am.getAnswerValue().equals(String.valueOf(item.itemValue))) {
									cb.setChecked(true);
									cb.setTag(item);
									// System.out.println("复选题以前的答案配对");
								}
								// System.out.println("复选框其他项etName=" + etName +
								// ", name=" + am.getAnswerName() + ", value=" +
								// am.getAnswerValue());
								// System.out.println("am.getAnswerName():"+am.getAnswerName());
								if (etName.equals(am.getAnswerName())) {
									item.isCheck = true;
									// System.out.println("复选am.getAnswerValue():"+am.getAnswerValue());
									et.setText(am.getAnswerValue());
									// et.setTag(item);
								}
							}
						}
						// et.setId(free.itemValue);
						et.setId((idStr + "_et").hashCode());
						// otherLayout.setOnClickListener(new
						// RadioListenner(idStr.hashCode(), 1,
						// (idStr+"_et").hashCode()));
						/**
						 * 假如当前的CheckBox被选中了则让文本框可以编辑
						 * 假如没有被选中则让文本框中的内容为空并且是不可以编辑的
						 */

						cb.setOnClickListener(new CheckListener(cb, et, item));
						/**
						 * 题外关联 之 选项置顶 添加其他项 的操作 选项置底 隐藏 文本框
						 */
						if (1 == item.isOther) {
							otherLayout.addView(et, otherLayout.getChildCount());
						}

						if (0 < arrLayout.size()) {
							LinearLayout ll = arrLayout.get(((checkRows.size() - otherRows.size() + i) % q.rowsNum));
							ll.addView(otherLayout, ll.getChildCount());
						} else {
							bodyView.addView(otherLayout, bodyView.getChildCount());
						}
						// 将文本框假如结合中
						vs.add(et);
					}
				}

			}
			break;
		case Cnt.TYPE_MATRIX_RADIO_BUTTON:// 矩阵单选
			matrixRadioBt(isFixed,twoQsiteOptions, amList);
			break;

		case Cnt.TYPE_MATRIX_CHECK_BOX:// 矩阵复选
			// 单复选矩阵固定
			if (isFixed) {
				/**
				 * 标题最大宽度
				 */
				// tvTitle.setMaxWidth(800);

				/**
				 * 题型的横向、纵向摆放。
				 */
				if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
					// System.out.println("横向");
					bodyView.setOrientation(LinearLayout.HORIZONTAL);
				} else {
					// System.out.println("纵向");
					bodyView.setOrientation(LinearLayout.VERTICAL);
				}
				/**
				 * 获取行标题
				 */
				ArrayList<QuestionItem> cRows = new ArrayList<QuestionItem>();
				cRows.addAll(q.getRowItemArr());
				/**
				 * 获取列标题
				 */
				ArrayList<QuestionItem> cColmns = new ArrayList<QuestionItem>();
				cColmns.addAll(q.getColItemArr());

				boolean _isInclusion = false;

				/**
				 * 排斥
				 */
				if ("1".equals(q.qInclusion)) {
					_isInclusion = true;
					// Question _q =
					// ma.dbService.getQuestion(feed.getSurveyId(),
					// q.qSiteOption);
					Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					ArrayList<AnswerMap> aml = an.getAnswerMapArr();
					ArrayList<Integer> have = new ArrayList<Integer>();
					if (null != aml) {
						for (AnswerMap am : aml) {
							/**
							 * 假如有值
							 */
							if (!Util.isEmpty(am.getAnswerValue())) {
								if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
									have.add(am.getRow());
								} else {
									have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								}
							}
						}
						aml.clear();
						aml = null;
					}
					for (int i = 0; i < q.getRowItemArr().size(); i++) {
						QuestionItem qi = q.getRowItemArr().get(i);
						if (-1 != have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
							/**
							 * 选择了这些选项
							 */
							cRows.remove(qi);
						}
					}
					have.clear();
					have = null;
					mTempRows.addAll(cRows);
				} else if ("0".equals(q.qInclusion)) {
					/**
					 * 引用
					 */
					_isInclusion = true;
					// Question _q =
					// ma.dbService.getQuestion(feed.getSurveyId(),
					// q.qSiteOption);
					Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (an != null) {
						ArrayList<AnswerMap> aml = (null == an) ? null : an.getAnswerMapArr();
						ArrayList<Integer> have = new ArrayList<Integer>();
						if (null != aml) {
							for (AnswerMap am : aml) {
								/**
								 * 假如有值
								 */
								if (!Util.isEmpty(am.getAnswerValue())) {
									if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
										have.add(am.getRow());
									} else {
										have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
									}
								}
							}
							aml.clear();
							aml = null;
						}
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (-1 == have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
								/**
								 * 没有选择的, 则移走
								 */
								cRows.remove(qi);
							}
						}
						have.clear();
						have = null;
						mTempRows.addAll(cRows);
					} else {
						cRows.retainAll(cRows);
						mTempRows.addAll(cRows);
					}
				}

				/**
				 * 假如是随即题目 则将其行和列都随机一遍
				 */
				if (1 == q.qRadomed) {
					/**
					 * 行随机暂存行中的数据
					 */
					ArrayList<QuestionItem> tmpRows = new ArrayList<QuestionItem>();
					Random rad = new Random();
					int size = 0;
					if (_isInclusion) {
						size = cRows.size();
					} else {
						size = q.getRowItemArr().size();
					}
					for (int i = 0; i < size; i++) {
						/**
						 * 随机产生数组的下标值
						 */
						int index = rad.nextInt(cRows.size());
						/**
						 * 取得随机产生的选项对象item
						 */
						tmpRows.add(cRows.get(index));
						/**
						 * 在暂存数组中移除随机产生的选项对象item
						 */
						cRows.remove(index);
					}
					cRows.addAll(tmpRows);
					tmpRows.clear();
				}

				/**
				 * 假如题干的宽度大于或等于屏幕宽度的3/4
				 */
				boolean _isBeyond = (screenWidth * 2 / 3 - 20) <= cColmns.size() * 100;

				/**
				 * 遍历每一行
				 */
				for (int r = 0; r < cRows.size() + 1; r++) {
					QuestionItem rowItem = null;
					if (0 != r) {
						rowItem = cRows.get(r - 1);
					}

					/**
					 * 遍历每一列
					 */
					RadioGroup ll = new RadioGroup(NativeModeActivity.this);
					ll.setGravity(Gravity.CENTER_VERTICAL);
					// 单复选矩阵固定
					RadioGroup ll_new = new RadioGroup(NativeModeActivity.this);
					ll_new.setGravity(Gravity.CENTER_VERTICAL);
					for (int c = 0; c < cColmns.size() + 1; c++) {
						QuestionItem colItem = null;

						if (0 != c) {
							colItem = cColmns.get(c - 1);
						}

						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(FILL_WRAP);
						// 单复选矩阵固定
						ll_new.setOrientation(LinearLayout.HORIZONTAL);
						ll_new.setLayoutParams(FILL_WRAP);
						TextView tvTb = new TextView(NativeModeActivity.this);
						tvTb.setLayoutParams(WRAP_WRAP);
						tvTb.setGravity(Gravity.FILL);
						tvTb.setTextSize(lowSurveySize);
						tvTb.setWidth(100);
						tvTb.setPadding(2, 2, 2, 2);
						// tvTb.setHeight(100);
						// tvTb.setBackgroundColor(Color.LTGRAY);

						if (0 == r) {// 如过是第一行, 则打印出每一列的值
							// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
							// 单复选矩阵固定
							ll_new.setBackgroundColor(Color.LTGRAY);
							if (0 == c) {// 打印表头
								tvTb.setText(" ");
								tvTb.setWidth(screenWidth / 3);
							} else {// 打印每列的
								tvTb.setTextColor(Color.BLACK);
								if (_isBeyond) {
									/**
									 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
									 */
								} else {
									tvTb.setWidth((screenWidth * 2 / 3 - 20) / cColmns.size());
								}
								String t = colItem.itemText;

								// ***********************************样式处理**************************//
								CstmMatcher cm = Util.findFontMatcherList(t);
								if (!Util.isEmpty(cm.getMis())) {
									t = cm.getResultStr();
									SpannableString ss = new SpannableString(t);
									for (MatcherItem mi : cm.getMis()) {
										if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length())
											ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
									CstmMatcher bCm = Util.findBoldMatcherList(t);
									if (!Util.isEmpty(bCm.getMis())) {
										for (MatcherItem mi : bCm.getMis()) {
											if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
												ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
												ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											}
										}
									}
									tvTb.setText(ss);
									//
								} else {
									CstmMatcher bCm = Util.findBoldMatcherList(t);
									if (!Util.isEmpty(bCm.getMis())) {
										t = bCm.getResultStr();
										SpannableString ss = new SpannableString(t);
										for (MatcherItem mi : bCm.getMis()) {
											if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
												ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
												ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											}
										}
										tvTb.setText(ss);
									} else {
										tvTb.setText(t);
									}

								}
							}
							// ***********************************样式处理**************************//
							// 单复选矩阵固定
							ll_new.addView(tvTb, ll_new.getChildCount());
						} else {
							if (r % 2 == 0)
								ll.setBackgroundColor(Color.parseColor("#10000050"));
							else
								ll.setBackgroundColor(Color.TRANSPARENT);
							if (0 == c) {
								tvTb.setTextColor(Color.BLACK);
								tvTb.setWidth(screenWidth / 3);
								tvTb.setBackgroundColor(Color.TRANSPARENT);
								// tvTb.setText(cRows.get(r - 1).itemText);
								// tvTb.setText(rowItem.itemText);
								String t = rowItem.itemText;
								// ***********************************样式处理**************************//

								if (!Util.isEmpty(t)) {
									CstmMatcher cm = Util.findFontMatcherList(t);
									if (!Util.isEmpty(cm.getMis())) {
										t = cm.getResultStr();
										if (Util.isEmpty(t)) {
											SpannableString ss = new SpannableString(t);
											for (MatcherItem mi : cm.getMis()) {
												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length())
													ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											}
											CstmMatcher bCm = Util.findBoldMatcherList(t);
											if (!Util.isEmpty(bCm.getMis())) {
												for (MatcherItem mi : bCm.getMis()) {
													if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
														ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
														ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
													}
												}
											}
											tvTb.setText(ss);
										} else {
											tvTb.setText(rowItem.itemText);
										}
										//
									} else {
										CstmMatcher bCm = Util.findBoldMatcherList(t);
										if (!Util.isEmpty(bCm.getMis())) {
											t = bCm.getResultStr();
											if (!Util.isEmpty(t)) {
												SpannableString ss = new SpannableString(t);
												for (MatcherItem mi : bCm.getMis()) {
													if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
														ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
														ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
													}
												}
												tvTb.setText(ss);
											} else {
												tvTb.setText(rowItem.itemText);
											}
										} else {
											tvTb.setText(t);
										}

									}
								}
								// ***********************************样式处理**************************//
								ll.addView(tvTb, ll.getChildCount());
							} else {// 打印单选按钮
								// QuestionItem rowItem = cRows.get(r - 1);
								if (null == rowItem || (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
									// 空其他项不打印CheckBox
									continue;
								}

								// QuestionItem colItem = cColmns.get(c - 1);

								CheckBox check = new CheckBox(NativeModeActivity.this);
								// System.out.println("(r - 1)=" + (r - 1) +
								// ", (c - 1)=" + (c - 1));
								// System.out.println("rowItem.itemValue=" +
								// rowItem.itemValue + ", colItem.itemValue=" +
								// colItem.itemValue);
								String name = Util.GetAnswerName(q, null, rowItem.itemValue, colItem.itemValue, false);
								int value = q.getColItemArr().size() * rowItem.itemValue + colItem.itemValue;
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setRow(rowItem.itemValue);
								am.setCol(colItem.itemValue);
								am.setAnswerValue(String.valueOf(value));
								check.setTag(am);
								ll.addView(check, ll.getChildCount());
								if (!Util.isEmpty(amList)) {
									for (AnswerMap tam : amList) {
										if (name.endsWith(tam.getAnswerName()) && am.getAnswerValue().equals(tam.getAnswerValue())) {
											check.setChecked(true);
										}
									}
								}
								check.setPadding(2, 2, 2, 2);
								check.setGravity(Gravity.FILL);
								check.setBackgroundResource(R.drawable.small_checkbox_background);
								check.setButtonDrawable(R.drawable.small_checkbox);
								check.setWidth(100);
								if (_isBeyond) {
									/**
									 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
									 */
								} else {
									check.setWidth((screenWidth * 2 / 3 - 20) / cColmns.size());
								}
								/**
								 * 复选矩阵排他项
								 * */
								check.setOnClickListener(new CheckListenerMatrix(check, rowItem,ll));
								
								// check.setHeight(100);
								vs.add(check);
							}
						}

					}
					// 单复选矩阵固定
					bodyView_new.addView(ll_new, bodyView_new.getChildCount());
					bodyView.addView(ll, bodyView.getChildCount());
				}
			} else {
				/**
				 * 标题最大宽度
				 */
				// tvTitle.setMaxWidth(800);

				/**
				 * 题型的横向、纵向摆放。
				 */
				if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
					// System.out.println("横向");
					bodyView.setOrientation(LinearLayout.HORIZONTAL);
				} else {
					// System.out.println("纵向");
					bodyView.setOrientation(LinearLayout.VERTICAL);
				}
				/**
				 * 获取行标题
				 */
				ArrayList<QuestionItem> cRows = new ArrayList<QuestionItem>();
				cRows.addAll(q.getRowItemArr());
				/**
				 * 获取列标题
				 */
				ArrayList<QuestionItem> cColmns = new ArrayList<QuestionItem>();
				cColmns.addAll(q.getColItemArr());

				boolean _isInclusion = false;

				/**
				 * 排斥
				 */
				if ("1".equals(q.qInclusion)) {
					_isInclusion = true;
					// Question _q =
					// ma.dbService.getQuestion(feed.getSurveyId(),
					// q.qSiteOption);
					Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					ArrayList<AnswerMap> aml = an.getAnswerMapArr();
					ArrayList<Integer> have = new ArrayList<Integer>();
					if (null != aml) {
						for (AnswerMap am : aml) {
							/**
							 * 假如有值
							 */
							if (!Util.isEmpty(am.getAnswerValue())) {
								if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
									have.add(am.getRow());
								} else {
									have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								}
							}
						}
						aml.clear();
						aml = null;
					}
					for (int i = 0; i < q.getRowItemArr().size(); i++) {
						QuestionItem qi = q.getRowItemArr().get(i);
						if (-1 != have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
							/**
							 * 选择了这些选项
							 */
							cRows.remove(qi);
						}
					}
					have.clear();
					have = null;
					mTempRows.addAll(cRows);
				} else if ("0".equals(q.qInclusion)) {
					/**
					 * 引用
					 */
					_isInclusion = true;
					// Question _q =
					// ma.dbService.getQuestion(feed.getSurveyId(),
					// q.qSiteOption);
					Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (an != null) {
						ArrayList<AnswerMap> aml = (null == an) ? null : an.getAnswerMapArr();
						ArrayList<Integer> have = new ArrayList<Integer>();
						if (null != aml) {
							for (AnswerMap am : aml) {
								/**
								 * 假如有值
								 */
								if (!Util.isEmpty(am.getAnswerValue())) {
									if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
										have.add(am.getRow());
									} else {
										have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
									}
								}
							}
							aml.clear();
							aml = null;
						}
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (-1 == have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
								/**
								 * 没有选择的, 则移走
								 */
								cRows.remove(qi);
							}
						}
						have.clear();
						have = null;
						mTempRows.addAll(cRows);
					} else {
						cRows.retainAll(cRows);
						mTempRows.addAll(cRows);
					}
				}

				/**
				 * 假如是随即题目 则将其行和列都随机一遍
				 */
				if (1 == q.qRadomed) {
					/**
					 * 行随机暂存行中的数据
					 */
					ArrayList<QuestionItem> tmpRows = new ArrayList<QuestionItem>();
					Random rad = new Random();
					int size = 0;
					if (_isInclusion) {
						size = cRows.size();
					} else {
						size = q.getRowItemArr().size();
					}
					for (int i = 0; i < size; i++) {
						/**
						 * 随机产生数组的下标值
						 */
						int index = rad.nextInt(cRows.size());
						/**
						 * 取得随机产生的选项对象item
						 */
						tmpRows.add(cRows.get(index));
						/**
						 * 在暂存数组中移除随机产生的选项对象item
						 */
						cRows.remove(index);
					}
					cRows.addAll(tmpRows);
					tmpRows.clear();
				}

				/**
				 * 假如题干的宽度大于或等于屏幕宽度的3/4
				 */
				boolean _isBeyond = (screenWidth * 2 / 3 - 20) <= cColmns.size() * 100;

				/**
				 * 遍历每一行
				 */
				for (int r = 0; r < cRows.size() + 1; r++) {
					QuestionItem rowItem = null;
					if (0 != r) {
						rowItem = cRows.get(r - 1);
					}

					/**
					 * 遍历每一列
					 */
					RadioGroup ll = new RadioGroup(NativeModeActivity.this);
					ll.setGravity(Gravity.CENTER_VERTICAL);
					for (int c = 0; c < cColmns.size() + 1; c++) {
						QuestionItem colItem = null;

						if (0 != c) {
							colItem = cColmns.get(c - 1);
						}

						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(FILL_WRAP);
						TextView tvTb = new TextView(NativeModeActivity.this);
						tvTb.setLayoutParams(WRAP_WRAP);
						tvTb.setGravity(Gravity.FILL);
						tvTb.setTextSize(lowSurveySize);
						tvTb.setWidth(100);
						tvTb.setPadding(2, 2, 2, 2);
						// tvTb.setHeight(100);
						// tvTb.setBackgroundColor(Color.LTGRAY);

						if (0 == r) {// 如过是第一行, 则打印出每一列的值
							// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
							ll.setBackgroundColor(Color.LTGRAY);
							if (0 == c) {// 打印表头
								tvTb.setText(" ");
								tvTb.setWidth(screenWidth / 3);
							} else {// 打印每列的
								tvTb.setTextColor(Color.BLACK);
								if (_isBeyond) {
									/**
									 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
									 */
								} else {
									tvTb.setWidth((screenWidth * 2 / 3 - 20) / cColmns.size());
								}
								String t = colItem.itemText;

								// ***********************************样式处理**************************//
								CstmMatcher cm = Util.findFontMatcherList(t);
								if (!Util.isEmpty(cm.getMis())) {
									t = cm.getResultStr();
									SpannableString ss = new SpannableString(t);
									for (MatcherItem mi : cm.getMis()) {
										if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length())
											ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
									CstmMatcher bCm = Util.findBoldMatcherList(t);
									if (!Util.isEmpty(bCm.getMis())) {
										for (MatcherItem mi : bCm.getMis()) {
											if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
												ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
												ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											}
										}
									}
									tvTb.setText(ss);
									//
								} else {
									CstmMatcher bCm = Util.findBoldMatcherList(t);
									if (!Util.isEmpty(bCm.getMis())) {
										t = bCm.getResultStr();
										SpannableString ss = new SpannableString(t);
										for (MatcherItem mi : bCm.getMis()) {
											if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
												ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
												ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											}
										}
										tvTb.setText(ss);
									} else {
										tvTb.setText(t);
									}

								}
							}
							// ***********************************样式处理**************************//
							ll.addView(tvTb, ll.getChildCount());
						} else {
							if (r % 2 == 0)
								ll.setBackgroundColor(Color.parseColor("#10000050"));
							else
								ll.setBackgroundColor(Color.TRANSPARENT);
							if (0 == c) {
								tvTb.setTextColor(Color.BLACK);
								tvTb.setWidth(screenWidth / 3);
								tvTb.setBackgroundColor(Color.TRANSPARENT);
								// tvTb.setText(cRows.get(r - 1).itemText);
								// tvTb.setText(rowItem.itemText);
								String t = rowItem.itemText;
								// ***********************************样式处理**************************//

								if (!Util.isEmpty(t)) {
									CstmMatcher cm = Util.findFontMatcherList(t);
									if (!Util.isEmpty(cm.getMis())) {
										t = cm.getResultStr();
										if (Util.isEmpty(t)) {
											SpannableString ss = new SpannableString(t);
											for (MatcherItem mi : cm.getMis()) {
												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length())
													ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											}
											CstmMatcher bCm = Util.findBoldMatcherList(t);
											if (!Util.isEmpty(bCm.getMis())) {
												for (MatcherItem mi : bCm.getMis()) {
													if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
														ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
														ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
													}
												}
											}
											tvTb.setText(ss);
										} else {
											tvTb.setText(rowItem.itemText);
										}
										//
									} else {
										CstmMatcher bCm = Util.findBoldMatcherList(t);
										if (!Util.isEmpty(bCm.getMis())) {
											t = bCm.getResultStr();
											if (!Util.isEmpty(t)) {
												SpannableString ss = new SpannableString(t);
												for (MatcherItem mi : bCm.getMis()) {
													if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
														ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
														ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
													}
												}
												tvTb.setText(ss);
											} else {
												tvTb.setText(rowItem.itemText);
											}
										} else {
											tvTb.setText(t);
										}

									}
								}
								// ***********************************样式处理**************************//
								ll.addView(tvTb, ll.getChildCount());
							} else {// 打印单选按钮
								// QuestionItem rowItem = cRows.get(r - 1);
								if (null == rowItem || (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
									// 空其他项不打印CheckBox
									continue;
								}

								// QuestionItem colItem = cColmns.get(c - 1);

								CheckBox check = new CheckBox(NativeModeActivity.this);
								// System.out.println("(r - 1)=" + (r - 1) +
								// ", (c - 1)=" + (c - 1));
								// System.out.println("rowItem.itemValue=" +
								// rowItem.itemValue + ", colItem.itemValue=" +
								// colItem.itemValue);
								String name = Util.GetAnswerName(q, null, rowItem.itemValue, colItem.itemValue, false);
								System.out.println("q.getColItemArr().size():"+q.getColItemArr().size());
								System.out.println("rowItem.itemValue:"+rowItem.itemValue);
								System.out.println("colItem.itemValue:"+colItem.itemValue);
								int value = q.getColItemArr().size() * rowItem.itemValue + colItem.itemValue;
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setRow(rowItem.itemValue);
								am.setCol(colItem.itemValue);
								am.setAnswerValue(String.valueOf(value));
								check.setTag(am);
								ll.addView(check, ll.getChildCount());
								if (!Util.isEmpty(amList)) {
									for (AnswerMap tam : amList) {
										if (name.endsWith(tam.getAnswerName()) && am.getAnswerValue().equals(tam.getAnswerValue())) {
											check.setChecked(true);
										}
									}
								}
								check.setPadding(2, 2, 2, 2);
								check.setGravity(Gravity.FILL);
								check.setBackgroundResource(R.drawable.small_checkbox_background);
								check.setButtonDrawable(R.drawable.small_checkbox);
								check.setWidth(100);
								if (_isBeyond) {
									/**
									 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
									 */
								} else {
									check.setWidth((screenWidth * 2 / 3 - 20) / cColmns.size());
								}
								// check.setHeight(100);
								
								/**
								 * 复选矩阵排他项
								 * */
								check.setOnClickListener(new CheckListenerMatrix(check, rowItem,ll));
								
								vs.add(check);
							}
						}

					}
					bodyView.addView(ll, bodyView.getChildCount());
				}
			}

			break;

		case Cnt.TYPE_DROP_DOWN_LIST:// 下来列表
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}

			Spinner sp = new Spinner(NativeModeActivity.this);
			sp.setLayoutParams(FILL_WRAP);

			ArrayList<QuestionItem> columns = q.getColItemArr();
			tvMap = new HashMap<String, Integer>();
			HashMap<Integer, Integer> ivMap = new HashMap<Integer, Integer>();
			if (!Util.isEmpty(columns)) {
				ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this, R.layout.simple_spinner_adapter);
				aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
				int deftPosition = 0;
				for (int i = 0; i < columns.size(); i++) {
					QuestionItem item = columns.get(i);
					if (1 == item.deft) {
						deftPosition = i;
					}
					aa.add(item.itemText);
					tvMap.put(i + "_" + item.itemText, item.itemValue);
					ivMap.put(item.itemValue, i);
				}
				sp.setAdapter(aa);
				sp.setSelection(deftPosition);
				if (!Util.isEmpty(amList)) {
					String name = Util.GetAnswerName(q, null, 0, 0, false);
					for (AnswerMap am : amList) {
						if (name.equals(am.getAnswerName())) {
							int value = Integer.parseInt(am.getAnswerValue());
							int pos = ivMap.get(value);
							sp.setSelection(pos);
						}
					}
				}
			}
			bodyView.addView(sp, bodyView.getChildCount());
			vs.add(sp);
			ivMap.clear();
			ivMap = null;
			break;

		case Cnt.TYPE_FREE_TEXT_BOX:// 单行文本框

			// 开始
			// System.out.println("q.qLinkage1:" + q.qLinkage);
			// 区分新旧文本题目
			freeTextBox(amList);
			/**
			 * 添加 三级联动事件 点击
			 */

			break;

		case Cnt.TYPE_FREE_TEXT_AREA:// 文本域
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);
			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}
			ArrayList<QuestionItem> tbRows = q.getRowItemArr();
			int textAreaRows = q.textAreaRows;// 一共几行
			if (!Util.isEmpty(tbRows)) {
				for (int i = 0; i < tbRows.size(); i++) {
					QuestionItem item = tbRows.get(i);
					item.itemValue = i;
					EditText et = new EditText(NativeModeActivity.this);
					et.setTag(item);
					et.setLayoutParams(FILL_WRAP);
					et.setWidth((int) (dis.getWidth() / 1.5));
					et.setHeight(textAreaRows * dis.getHeight() / 20);
					// 问卷字号动态设置
					et.setTextSize(lowSurveySize);
					et.setGravity(Gravity.TOP);
					// System.out.println("item.itemText=" + item.itemText);
					if (!Util.isEmpty(item.itemText)) {
						TextView tvCap = new TextView(NativeModeActivity.this);
						tvCap.setLayoutParams(FILL_WRAP);
						tvCap.setTextColor(Color.BLACK);
						tvCap.setTextSize(lowSurveySize);
						tvCap.setText(item.itemText);
						bodyView.addView(tvCap, bodyView.getChildCount());
					}
					if (!Util.isEmpty(amList)) {
						String etName = Util.GetAnswerName(q, item, 0, 0, true);
						for (AnswerMap am : amList) {
							if (etName.equals(am.getAnswerName())) {
								et.setText(am.getAnswerValue());
							}
						}
					}
					bodyView.addView(et, bodyView.getChildCount());
					vs.add(et);
				}
			}

			break;

		case Cnt.TYPE_MEDIA:
			// 预留多为体题型
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}
			if (!Util.isEmpty(q.qMediaSrc)) {
				if (q.qMediaSrc.toLowerCase().endsWith(".png")//
						|| q.qMediaSrc.toLowerCase().endsWith(".jpg") //
						|| q.qMediaSrc.toLowerCase().endsWith(".bmp")//
						|| q.qMediaSrc.toLowerCase().endsWith(".gif")) {//
					ImageView iv = new ImageView(NativeModeActivity.this);
					iv.setLayoutParams(WRAP_WRAP);
					String filename = Util.getMediaPath(NativeModeActivity.this, q.surveyId, q.qMediaSrc);
					try {
						BitmapFactory.Options opts = new BitmapFactory.Options();
						File file = new File(filename);
						// 数字越大读出的图片占用的heap越小 不然总是溢出
						long len = file.length();
						if (1048576*3 > len) { // 显示图片时的压缩 小于1024k, 即1M的
							opts.inSampleSize = 2;
						} else if(1048576*8>len&&len>=1048576*3){
							/**
							 * 大于1M的
							 */
							opts.inSampleSize = 6;
						}
						else {
							opts.inSampleSize = 10;
						}
						Bitmap oldBitmap= BitmapFactory.decodeStream(new FileInputStream(file),null,opts);
						
						float width = oldBitmap.getWidth();
						float height = oldBitmap.getHeight(); 
						
						Bitmap newBitmap=null;
						if(width > screenWidth || height > screenHeight) {
							float scaleRate = width > height ? screenWidth / width : screenHeight / height;
							Matrix matrix = new Matrix();
							matrix.postScale(scaleRate, scaleRate);
							// 得到新的图片          
							newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, (int)width, (int)height, matrix, true);   
						}else{
							float scaleRate = width > height ? screenWidth / width : screenHeight / height;
							Matrix matrix = new Matrix();
							matrix.postScale(scaleRate, scaleRate);
							newBitmap =  Bitmap.createBitmap(oldBitmap, 0, 0, (int)width, (int)height, matrix, true);
						}
						// if (1048576 > len) { // 小于1024k
						// opts.inSampleSize = 3;
						// } else {
						// opts.inSampleSize = 6;
						// }

						bd = new BitmapDrawable(newBitmap);
						iv.setImageDrawable(bd);
						iv.setOnLongClickListener(new ImageLongClickListener(filename));
					} catch (Exception e) {
						e.printStackTrace();
					}
					bodyView.addView(iv, bodyView.getChildCount());
				} else if (q.qMediaSrc.toLowerCase().endsWith(".mp3")//
						|| q.qMediaSrc.toLowerCase().endsWith(".avi")//
						|| q.qMediaSrc.toLowerCase().endsWith(".wmv")//
						|| q.qMediaSrc.toLowerCase().endsWith(".mp4")//
						|| q.qMediaSrc.toLowerCase().endsWith(".flv")//
						|| q.qMediaSrc.toLowerCase().endsWith(".wma")//
						|| q.qMediaSrc.toLowerCase().endsWith(".swf")//
						|| q.qMediaSrc.toLowerCase().endsWith(".3gp")//
						|| q.qMediaSrc.toLowerCase().endsWith(".mov")) {
					ImageView iv = new ImageView(NativeModeActivity.this);
					iv.setPadding(60, 60, 60, 60);
					iv.setBackgroundResource(R.drawable.shape_bg);
					iv.setLayoutParams(WRAP_WRAP);
					iv.setImageResource(R.drawable.play);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(NativeModeActivity.this, VideoPlayerActivity.class);
							it.putExtra("path", Util.getMediaPath(q.surveyId, q.qMediaSrc));
							startActivity(it);
						}
					});
					bodyView.addView(iv, bodyView.getChildCount());
				}
				
			}
			break;
		}
		// 追加说明方法继续上一页下一页终止提示
		isPrompt = false;
	}

	/**
	 * 设置 二级联动 城市 适配器
	 * 
	 * @param pos
	 */
	private void setCityAdapter(int pos, QuestionItem item) {

		areaListTemp = new ArrayList<String>();
		areaListTemp = ThreeLeverUtil.getCityPosList(area, city, pos);
		if (areaListTemp.size() == 0) {
			areaListTemp.add("空");
		}

		cityAdapter = new ArrayAdapter<String>(NativeModeActivity.this, R.layout.simple_spinner_adapter, areaListTemp);
		cityAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
		citySpinner.setAdapter(cityAdapter);
		citySpinner.setTag(item);
	}

	/**
	 * 设置 三级 联动 的 关联 情况
	 */

	private void setCountryAdapter(int pos, QuestionItem item) {

		wayListTemp = new ArrayList<String>();
		wayListTemp = ThreeLeverUtil.getAreaPosList(areaListTemp, way, pos);
		if (wayListTemp.size() == 0) {
			wayListTemp.add("空");
		}

		countryAdapter = new ArrayAdapter<String>(NativeModeActivity.this, R.layout.simple_spinner_adapter, wayListTemp);
		countryAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
		countrySpinner.setAdapter(countryAdapter);
		countrySpinner.setTag(item);
	}

	/**
	 * 维码扫描用,给指定的值赋值
	 */
	private EditText etSave;
	private int hideCount=0;
	private int realRows=0;
	class ScanningListener implements OnClickListener {

		private EditText innerEt;

		public ScanningListener(EditText editText) {
			innerEt = editText;
		}

		@Override
		public void onClick(View v) {
			etSave = innerEt;
			Intent openCameraIntent = new Intent(NativeModeActivity.this, CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
		}

	}

	/**
	 * 维码扫描用
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etSave.setText(scanResult);
		}
		//数据字典
		else if(resultCode == 2){
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			etData.setText(result);
			etData.setFocusable(true);
			etData.setFocusableInTouchMode(true);
			etData.requestFocus();
		}
	}

	/**
	 * 监控用的写异步线程
	 */
	private final class MonitorTask extends AsyncTask<Void, Integer, InputStream> {

		private int optType;// 是上一页还是下一页 next为2 opt为1
		private int realIndex;

		public MonitorTask(int opt, int realIndex) {
			this.optType = opt;
			this.realIndex = realIndex;
		}

		@Override
		protected InputStream doInBackground(Void... params) {
			int recodeQIndex = -1;
			int recodeTempRealIndex = -1;
			ArrayList<AnswerMap> recodeAmList = new ArrayList<AnswerMap>();
			if (recodeTempIndex != -1) {
				System.out.println("recodeTempIndex:" + recodeTempIndex);
				Question recodeQuestion = qs.get(recodeTempIndex);
				recodeQIndex = recodeQuestion.qIndex;
				Answer recodeAnswer = ma.dbService.getAnswer(qAnswer.uuid, recodeQIndex + "");
				if (null != recodeAnswer) {
					recodeAmList = recodeAnswer.getAnswerMapArr();
				}
				// 更改的
				recodeTempRealIndex = ma.dbService.getQuestionIndex(feed.getSurveyId(), recodeTempIndex);
			}
			String monitorPath = writeMonitorXml(recodeTempRealIndex, recodeAmList, optType);
			// 上传 地址不为空就上传
			InputStream is = null;
			if (!Util.isEmpty(monitorPath)) {
				String name = monitorPath.substring(monitorPath.lastIndexOf(File.separator) + 1, monitorPath.length());
				String path = monitorPath.substring(0, monitorPath.lastIndexOf(File.separator));
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("surveyId", feed.getSurveyId());
				paramsMap.put(Cnt.USER_ID, ma.userId);
				paramsMap.put("uuid", feed.getUuid());
				paramsMap.put("index", recodeTempRealIndex + "");
				paramsMap.put(Cnt.USER_PWD, ma.userPwd);
				FileUpLoad fupLoad = new FileUpLoad();
				is = fupLoad.upLoad(Cnt.MONITOR_URL, path, name, paramsMap);
			}
			if (is == null) {
				// System.out.println("is空");
				return null;
			} else {
				// System.out.println("is不为空");
				return is;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(InputStream is) {
			String state = "0";
			recodeTempIndex = realIndex;
			if (is != null) {
				state = Util.resolvData(is);
			}
			if ("100".equals(state)) {
				// System.out.println("成功");
			}
			super.onPostExecute(is);
		}

	}

	/**
	 * 监控用的写xml
	 * 
	 * @param recodeQIndex
	 * @param recodeAmList
	 */
	private String writeMonitorXml(int recodeTempRealIndex, ArrayList<AnswerMap> recodeAmList, int optType) {
		String path = "";
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "monitor");
			serializer.attribute(null, "jumpIndex", q.qIndex + "");
			serializer.attribute(null, "index", recodeTempRealIndex + "");
			serializer.attribute(null, "uuid", feed.getUuid());
			if (MSG_NEXT == optType) {
				serializer.attribute(null, "opt", "2");
			} else if (3 == optType) {
				serializer.attribute(null, "opt", "3");
			} else if (4 == optType) {
				serializer.attribute(null, "opt", "4");
			} else {
				serializer.attribute(null, "opt", "1");
			}
			for (AnswerMap recodeAnswerMap : recodeAmList) {
				serializer.startTag(null, "answer");
				String recodeAnswerValue = recodeAnswerMap.getAnswerValue();
				String recodeAnswerName = recodeAnswerMap.getAnswerName();
				if (Util.isEmpty(recodeAnswerValue)) {
					recodeAnswerValue = "";
				}
				serializer.startTag(null, "name");
				serializer.text(recodeAnswerName);
				serializer.endTag(null, "name");
				serializer.startTag(null, "value");
				serializer.text(recodeAnswerValue);
				serializer.endTag(null, "value");
				serializer.endTag(null, "answer");
			}
			serializer.endTag(null, "monitor");
			serializer.endDocument();
			writer.flush();
			writer.close();
			path = Util.getMonitorPath(ma, feed.getSurveyId(), feed.getUuid(), q.qIndex + "", optType);
			File file = new File(path);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			OutputStream out = new FileOutputStream(path);
			OutputStreamWriter outw = new OutputStreamWriter(out);
			outw.write(writer.toString());
			outw.close();
			out.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	class CstOnClickListener implements OnClickListener {

		private Question question;

		public CstOnClickListener(Question q) {
			this.question = q;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 单题签名
			// 单复选矩阵固定
			case R.id.ivSign_new:
			case R.id.ivSign:
				WritePadDialog writeTabletDialog = new WritePadDialog(NativeModeActivity.this, new DialogListener() {
					@Override
					public void refreshActivity(Object object) {
						// System.out.println("刷新界面");
						Bitmap mSignBitmap = (Bitmap) object;
						UploadFeed photo = new UploadFeed();
						photo.setUserId(feed.getUserId());
						photo.setSurveyId(feed.getSurveyId());
						photo.setUuid(feed.getUuid());
						if (ma.cfg.getBoolean("save_inner", false)) {
							/**
							 * 存放在内部
							 */
							photo.setPath(Util.getRecordInnerPath(NativeModeActivity.this, feed.getSurveyId()));
							photo.setIsSaveInner(0);
						} else {
							/**
							 * 存放在外部
							 */
							photo.setPath(Util.getRecordPath(feed.getSurveyId()));
							photo.setIsSaveInner(1);
						}
						// photo.setPath(Util.getRecordInnerPath(NativeModeActivity.this,
						// feed.getSurveyId()));
						// 增加pid 命名规则
						photo.setName(Util.getSignName(feed.getUserId(), feed.getSurveyId(), Cnt.FILE_TYPE_PNG, feed.getUuid(), question.qIndex + "", feed.getPid(), feed.getParametersContent(),
								(question.qOrder + 1) + ""));
						File signFile = Util.createFile(mSignBitmap, photo.getPath(), photo.getName());
						photo.setStartTime(System.currentTimeMillis());
						photo.setRegTime(System.currentTimeMillis());
						photo.setSize(signFile.length());
						//缩略图
						if(signFile.length()>0){
							//缩略图
							float width = mSignBitmap.getWidth();
							float height = mSignBitmap.getHeight(); 
							Bitmap newBitmap=null;
							if(width > 90 || height > 90) {
								float scaleRate = width > height ? 90 / width : 90 / height;
								Matrix matrix = new Matrix();
								matrix.postScale(scaleRate, scaleRate);
								// 得到新的图片          
								newBitmap = Bitmap.createBitmap(mSignBitmap, 0, 0, (int)width, (int)height, matrix, true);   
							}else{
								Matrix matrix = new Matrix();
								matrix.postScale(1, 1);
								newBitmap =  Bitmap.createBitmap(mSignBitmap, 0, 0, (int)width, (int)height, matrix, true);
							}
							
							UploadFeed uf = (UploadFeed) photo.clone();
							//uf为空
							String name = uf.getName();
							String[] split1 = name.split("[.]");
							String name_q=split1[0];
							name_q+="_thumbnail";
							uf.setName(name_q+"."+split1[1]);
							File newsignFile = Util.createFile(newBitmap, uf.getPath(), uf.getName());
							uf.setSize(newsignFile.length());
							
							ma.dbService.addPhoto(photo, true, "");
							ma.dbService.addPhoto(uf, true, "");
						}
						
						

						/*
						 * BitmapFactory.Options options = new
						 * BitmapFactory.Options(); options.inSampleSize = 15;
						 * options.inTempStorage = new byte[5 * 1024]; Bitmap
						 * zoombm = BitmapFactory.decodeFile(signPath, options);
						 */

					}
				});
				writeTabletDialog.show();
				break;
			// 单复选矩阵固定
			case R.id.qtitle_tv_new:
			case R.id.qtitle_tv:
				Intent intent = new Intent(NativeModeActivity.this, PhotoActivity.class);
				Bundle bundle = new Bundle();
				UploadFeed photo = new UploadFeed();
				photo.setUserId(feed.getUserId());
				photo.setSurveyId(feed.getSurveyId());
				photo.setUuid(feed.getUuid());
				if (ma.cfg.getBoolean("save_inner", false)) {
					/**
					 * 存放在内部
					 */
					photo.setPath(Util.getRecordInnerPath(NativeModeActivity.this, feed.getSurveyId()));
					photo.setIsSaveInner(0);
				} else {
					/**
					 * 存放在外部
					 */
					photo.setPath(Util.getRecordPath(feed.getSurveyId()));
					photo.setIsSaveInner(1);
				}
				// photo.setPath(Util.getRecordInnerPath(NativeModeActivity.this,
				// feed.getSurveyId()));
				// 增加pid 命名规则
				photo.setName(Util.getRecordName(feed.getUserId(), feed.getSurveyId(), Cnt.FILE_TYPE_PNG, feed.getUuid(), question.qIndex + "", feed.getPid(), feed.getParametersContent(),
						(question.qOrder + 1) + ""));
				photo.setStartTime(System.currentTimeMillis());
				bundle.putSerializable("photo", photo);
				bundle.putSerializable("question", question);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;

			default:
				break;
			}
		}

	}

	private void initView() {
		tvTitle.setText("");
		tvCaption.setTextColor(Color.GRAY);// 统计局专有页面
		tvCaption.setText("");
		if (View.VISIBLE == llCaption.getVisibility()) {
			llCaption.removeAllViews();
			llCaption.setVisibility(View.GONE);
		}

		tvComment.setTextColor(Color.GRAY);// 统计局专有页面
		tvComment.setText("");

		if (View.VISIBLE == llComment.getVisibility()) {
			llComment.removeAllViews();
			llComment.setVisibility(View.GONE);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refreshView();
	}

	private final class RadioListenner implements OnClickListener {
		private int rId;// i=1表示是带有其他项的, i=0表示单纯的RadioButton
		private int o;
		private int eId;// 其他项的id号
		// private Question q;
		private QuestionItem item;
		// 单选自动下一页
		private int flag;

		public RadioListenner(int radioId, int isOther, int etId, QuestionItem i, int flag) {
			this.rId = radioId;
			this.o = isOther;
			this.eId = etId;
			this.item = i;
			this.flag = flag;
		}

		public RadioListenner(int radioId, int isOther, int etId, QuestionItem i) {
			this.rId = radioId;
			this.o = isOther;
			this.eId = etId;
			// this.q = qustion;
			this.item = i;
			flag = 0;
		}

		@Override
		public void onClick(View view) {
			// qAnswer.qIndex = q.qIndex;
			// qAnswer.qOrder = q.qOrder;
			// TODOqAnswer.userId
			// System.out.println("v_size=" + vs.size());
			// qAnswer.getAnswerMapArr().clear();
			for (View v : vs) {
				if (v instanceof RadioButton) {
					RadioButton rb = (RadioButton) v;
					if (rId != rb.getId()) {// 别人
						rb.setChecked(false);
						rb.setTag(null);
						// System.out.println("rd_id_别人=" + rb.getId());
					} else {
						// System.out.println("rd_id_自己=" + rb.getId());
						rb.setChecked(true);
						// qAnswer.getAnswerMapArr()
						// AnswerMap am = new AnswerMap();
						// String an = ComUtil.GetAnswerName(q, item, 0, 0,
						// false);
						// am.setAnswerName(an);
						// am.setAnswerValue(String.valueOf(item.itemValue));
						// qAnswer.getAnswerMapArr().add(am);
						rb.setTag(item);
					}
				} else if (v instanceof EditText) {
					EditText et = (EditText) v;
					if (1 == o) {// 如果他自己是其他项
						if (eId == et.getId()) {// 自己的文本框
							et.setFocusable(true);
							et.setFocusableInTouchMode(true);
							// System.out.println("自己的文本框");
							et.setTag(item);
						} else {// 别人的文本框
							et.setTag(null);
							et.setText("");
							et.setFocusableInTouchMode(false);
							et.setFocusable(false);
							// System.out.println("别人的文本框");
						}
					} else {// 别人的其他项
						et.setTag(null);
						/**
						 * 置为不可用
						 */
						et.setFocusable(false);
						et.setFocusableInTouchMode(false);
						/**
						 * 清空里面的值
						 */
						et.setText("");
						// System.out.println("别人的文本框2");
					}
				}
			}
			// 单选自动下一页
//			if (1 == flag) {
//				nextPage(false);
//			}
		}

	}

	/**
	 * 对话框图片控件点击监听器, 继承了对话框的点击时间,也就意味着有对话框点击事件类的所有功能
	 * 但在执行父类onClick方法之前,判断复选框的选中状态及重置为当前状态的反向状态
	 */
	private final class CheckImageClickListener extends CheckListener {

		public CheckImageClickListener(CheckBox check, EditText edit, QuestionItem item) {
			super(check, edit, item);
		}

		@Override
		public void onClick(View v) {
			/**
			 * 取当前状态的反向状态
			 */
			cb.setChecked(cb.isChecked() ? false : true);
			super.onClick(v);
		}

	}

	class CheckListener implements OnClickListener {

		CheckBox cb;
		private EditText et;
		private QuestionItem item;

		// private ArrayList<QuestionItem> items;

		public CheckListener(CheckBox check, EditText edit, QuestionItem item) {
			this.cb = check;
			this.et = edit;
			this.item = item;
			// this.items = rowItems;
		}

		@Override
		public void onClick(View v) {
			if ("true".equals(item.getExclude())) {// 有排斥的
				// System.out.println("排斥");
				for (View view : vs) {// for
					if (view instanceof CheckBox) {// check
						CheckBox checkBox = (CheckBox) view;
						if (cb.getId() == checkBox.getId()) {// 是自己
							checkBox.setChecked(true);
							item.isCheck = true;
							checkBox.setTag(item);
						} else {
							checkBox.setChecked(false);
							item.isCheck = false;
							checkBox.setTag(item);
						}
					} else if (view instanceof EditText) {
						EditText vet = (EditText) view;
						if (null != et && vet.getId() == et.getId()) {// 是自己的EditText
							/**
							 * 焦点有效
							 */
							vet.setFocusable(true);
							/**
							 * 触碰焦点有效
							 */
							vet.setFocusableInTouchMode(true);
							item.isCheck = true;
							vet.setTag(item);
						} else {// 别人的EidtText
							vet.setText("");
							vet.setFocusable(false);
							vet.setFocusableInTouchMode(false);
							item.isCheck = false;
							vet.setTag(item);
						}
					}
				}// for
			} else {
				if (null != et) {// 其他项
					if (cb.isChecked()) {
						/**
						 * CheckBox被选中了, 则CheckBox后面的EditText可是输入
						 */
						et.setFocusable(true);
						et.setFocusableInTouchMode(true);
						item.isCheck = true;
						cb.setTag(item);
						et.setTag(item);
						for (View vv : vs) {
							if (vv instanceof CheckBox) {
								CheckBox cb1 = ((CheckBox) vv);
								QuestionItem qi = (QuestionItem) cb1.getTag();
								if (cb.getId() != vv.getId() && "true".equals(qi.getExclude())) {
									cb1.setChecked(false);
									qi.isCheck = false;
									cb1.setTag(qi);
								}
							} else if (vv instanceof EditText) {

								QuestionItem qi = (QuestionItem) vv.getTag();
								if ("true".equals(qi.getExclude())) {
									((EditText) vv).setText("");
									((EditText) vv).setFocusable(false);
									((EditText) vv).setFocusableInTouchMode(false);
								}
							}
						}
					} else {
						/**
						 * 假如CheckBox为非选中状态, 则其后面的EditText中的文本为空, 并且是不可以输入的
						 */
						et.setText("");
						et.setFocusable(false);
						et.setFocusableInTouchMode(false);
						item.isCheck = false;
						cb.setTag(item);
						et.setTag(item);
						// System.out.println("其他项取消选中");
					}
				} else {// 非其他项

					if (cb.isChecked()) {
						cb.setChecked(true);
						item.isCheck = true;
						cb.setTag(item);
						// System.out.println("非其他项选中");
						// for (QuestionItem qi:items) {
						for (View vv : vs) {
							if (vv instanceof CheckBox) {
								CheckBox cb1 = ((CheckBox) vv);
								QuestionItem qi = (QuestionItem) cb1.getTag();
								if (cb.getId() != vv.getId() && "true".equals(qi.getExclude())) {
									cb1.setChecked(false);
									qi.isCheck = false;
									cb1.setTag(qi);
								}
							} else if (vv instanceof EditText) {
								// 提示
								// ((EditText) vv).setText("");
								((EditText) vv).setFocusable(false);
								((EditText) vv).setFocusableInTouchMode(false);
							}
						}
					}
				}

			}
		}

	}

	/**
	 * 复选矩阵排他项点击事件
	 * */
	class CheckListenerMatrix implements OnClickListener {
		CheckBox cb;
		private QuestionItem item;
		private RadioGroup ll;

		public CheckListenerMatrix(CheckBox check,QuestionItem item,RadioGroup ll ) {
			this.cb = check;
			this.item = item;
			this.ll=ll;
		}

		@Override
		public void onClick(View v) {
		String excludeValue=item.getExclude();
		if(!Util.isEmpty(excludeValue)){
			int exclude=Integer.parseInt(excludeValue);
			if(ll.getChildAt(exclude+1) instanceof CheckBox){
				CheckBox checkBox =(CheckBox) ll.getChildAt(exclude+1);
				for(int i = 0; i < ll.getChildCount(); i++) {
					if(ll.getChildAt(i) instanceof CheckBox){
						if (cb == checkBox) {// 点击的是排他项
							((CheckBox) ll.getChildAt(i)).setChecked(false);
							cb.setChecked(true);
							ll.setTag(item);
						} else {
							checkBox.setChecked(false);
							ll.setTag(item);
						}
					}
				}
			}
		}
				
	}
	
	}
	private void refreshView() {
		/**
		 * 获取屏幕的宽度
		 */
		screenWidth = dis.getWidth();
		/**
		 * 获取屏幕的高度
		 */
		screenHeight = dis.getHeight();

		/**
		 * 获取题干View的宽度
		 */
		// int qWidth = rlQuestion.getWidth();
		/**
		 * 获取题干View的高度
		 */
		// int qHeight = rlQuestion.getHeight();
		/**
		 * 标题、上方说明、底部追加说明的最大宽度只能是屏幕的
		 */
		int maxWidth = (int) (screenWidth * 0.9);
		tvTitle.setMaxWidth(maxWidth);
		tvCaption.setMaxWidth(maxWidth);
		tvComment.setMaxWidth(maxWidth);
		rlQuestion.setPadding(10, //
				screenHeight / 20, //
				10, //
				screenHeight / 20);
	}

	/**
	 * 哑题验证
	 * 
	 * @param operType
	 * @return
	 */
	public int getQuestionAnswer(int operType, boolean isNoValidate) {
		if (null == q) {
			return Cnt.STATE_SUCCESS;
		}
		qAnswer.getAnswerMapArr().clear();
		ArrayList<AnswerMap> answerMapArr = new ArrayList<AnswerMap>();
		/**
		 * 初始状态
		 */
		qAnswer.returnType = STATE_NOTHING;

		switch (q.qType) {
		case Cnt.TYPE_HEADER:
			/**
			 * 标题直接算获取答案成功
			 */
			qAnswer.returnType = Cnt.STATE_SUCCESS;
			break;

		case Cnt.TYPE_RADIO_BUTTON:
			for (View view : vs) {
				QuestionItem item = (QuestionItem) view.getTag();
				if (null != item) {
					if (1 == item.isOther) {// 带其他项
						if (view instanceof RadioButton) {
							// RadioButton rb = (RadioButton)view;
							// if(rb.isChecked()){
							AnswerMap am = new AnswerMap();
							String radioName = Util.GetAnswerName(q, item, 0, 0, false);
							am.setAnswerName(radioName);
							am.setAnswerValue(String.valueOf(item.itemValue));
							am.setRow(item.itemValue);
							// System.out.println("其他项单选按钮_name=" + radioName +
							// ", 其他项单选按钮_value=" +
							// String.valueOf(item.itemValue));
							am.setAnswerText(item.itemText);
							// qAnswer.getAnswerMapArr().add(am);
							answerMapArr.add(am);
							// }
						} else if (view instanceof EditText) {
							EditText et = (EditText) view;
							AnswerMap am = new AnswerMap();
							String etName = Util.GetAnswerName(q, item, 0, 0, true);
							am.setAnswerName(etName);
							String text = et.getText().toString();
							// System.out.println("其他项_文本框_name=" + etName +
							// ", 其他项_文本框_value=" + text);
							am.setAnswerValue(text);
							am.setRow(item.itemValue);
							am.setAnswerText(Util.isEmpty(item.itemText) ? text : (item.itemText + ":" + text));
							if (Util.isEmpty(text)) {
								// et.setBackgroundColor(Color.YELLOW);
								Util.viewShake(NativeModeActivity.this, et);
								qAnswer.returnType = Cnt.STATE_FAIL;
							} else {
								// qAnswer.getAnswerMapArr().add(am);
								answerMapArr.add(am);
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							}
						}
					} else {// 不带其他项的
						if (view instanceof RadioButton) {
							// RadioButton rb = (RadioButton)view;
							// if(rb.isChecked()){
							AnswerMap am = new AnswerMap();
							// System.out.println("单选按钮_name=" +
							// Util.GetAnswerName(q, item, 0, 0, false));
							am.setAnswerName(Util.GetAnswerName(q, item, 0, 0, false));
							// System.out.println("单选按钮_value=" +
							// String.valueOf(item.itemValue));
							am.setAnswerValue(String.valueOf(item.itemValue));
							am.setRow(item.itemValue);
							am.setAnswerText(item.itemText);
							// qAnswer.getAnswerMapArr().add(am);
							answerMapArr.add(am);
							qAnswer.returnType = Cnt.STATE_SUCCESS;
							// }
						}
					}
				}
			}
			break;
		/**
		 * 复选题目
		 */
		case Cnt.TYPE_CHECK_BOX:
			int bound = 0;
			for (View view : vs) {
				QuestionItem item = (QuestionItem) view.getTag();
				// System.out.println("ddddddddddddddd");
				if (null != item) {
					// System.out.println("item");
					if (1 == item.isOther) {// 带其他项的

						System.out.println("item.isCheck:" + item.isCheck);
						if (view instanceof CheckBox) {
							CheckBox cb = (CheckBox) view;
							if (cb.isChecked()) {
								bound += 1;
								AnswerMap am = new AnswerMap();
								// System.out.println("其他项复选_name=" +
								// Util.GetAnswerName(q, item, 0, 0, false));
								am.setAnswerName(Util.GetAnswerName(q, item, 0, 0, false));
								// System.out.println("其他项复选_value=" +
								// String.valueOf(item.itemValue));
								am.setAnswerValue(String.valueOf(item.itemValue));
								am.setAnswerText(item.itemText);
								// qAnswer.getAnswerMapArr().add(am);
								answerMapArr.add(am);
							} else if (1 != q.qRequired) {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(Util.GetAnswerName(q, item, 0, 0, false));
								am.setAnswerValue("");
								answerMapArr.add(am);
							}
						} else if (view instanceof EditText && item.isCheck) {
							EditText et = (EditText) view;
							AnswerMap am = new AnswerMap();
							// System.out.println("复选其他项_文本框_name=" +
							// Util.GetAnswerName(q, item, 0, 0, true));
							System.out.println(Util.GetAnswerName(q, item, 0, 0, true) + "编号");
							am.setAnswerName(Util.GetAnswerName(q, item, 0, 0, true));
							String text = et.getText().toString();
							am.setAnswerValue(text);
							am.setAnswerText(Util.isEmpty(item.itemText) ? text : (item.itemText + ":" + text));
							// System.out.println("复选其他项_文本框_value=" + text);
							if (Util.isEmpty(text)) {
								// et.setBackgroundColor(Color.YELLOW);
								Util.viewShake(NativeModeActivity.this, et);
								qAnswer.returnType = Cnt.STATE_FAIL;
							} else {
								// qAnswer.getAnswerMapArr().add(am);
								answerMapArr.add(am);
								/**
								 * 防止选了两个其他项,但是只填了其中一个其他项,另一个没填
								 */
								if (Cnt.STATE_FAIL != qAnswer.returnType) {
									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
							}
						}
					} else {// 不带其他项
						if (view instanceof CheckBox) {
							CheckBox cb = (CheckBox) view;
							if (cb.isChecked()) {
								bound += 1;
								AnswerMap am = new AnswerMap();
								// System.out.println("复选_name=" +
								// Util.GetAnswerName(q, item, 0, 0, false));
								am.setAnswerName(Util.GetAnswerName(q, item, 0, 0, false));
								// System.out.println("复选_value=" +
								// String.valueOf(item.itemValue));
								am.setAnswerValue(String.valueOf(item.itemValue));
								am.setAnswerText(item.itemText);
								// qAnswer.getAnswerMapArr().add(am);
								answerMapArr.add(am);
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							} else if (1 != q.qRequired) {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(Util.GetAnswerName(q, item, 0, 0, false));
								am.setAnswerValue("");
								answerMapArr.add(am);
							}
						}
					}
				}
			}
			/**
			 * 逻辑判断
			 */
			if (0 != bound && (0 < q.lowerBound || 0 < q.upperBound)) {
				if (bound < q.lowerBound) {
					/**
					 * 下限
					 */
					qAnswer.returnType = STATE_BOUND_LOWER;
				} else if (bound > q.upperBound && q.upperBound != 0) {
					/**
					 * 上限
					 */
					qAnswer.returnType = STATE_BOUND_UPPER;

				}
			}
			break;

		case Cnt.TYPE_MATRIX_RADIO_BUTTON:
			int tempRow = 0;
			HashMap<Integer, Integer> radioMap = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> rowMap = new HashMap<Integer, Integer>();
			for (View view : vs) {
				AnswerMap am = (AnswerMap) view.getTag();
				if (null != am) {
					if (view instanceof RadioButton) {
						RadioButton rb = (RadioButton) view;
						if (rb.isChecked()) {
							/**
							 * 统计回答了多少行
							 */
							rowMap.put(am.getRow(), am.getRow());

							Integer col = radioMap.get(am.getCol());
							if (null == col) {
								radioMap.put(am.getCol(), 1);
								tempRow = am.getRow();
							} else {
								if (1 == (am.getRow() - tempRow)) {
									radioMap.put(am.getCol(), col + 1);
								}
								tempRow = am.getRow();
							}
							// System.out.println("矩阵单选的AnswerName=" +
							// am.getAnswerName());
							// System.out.println("矩阵单选的AnswerValue=" +
							// am.getAnswerValue());
							// qAnswer.getAnswerMapArr().add(am);
							answerMapArr.add(am);
						}
					}
				}
			}
			if (1 == q.qRequired) {
				if (0 == rowMap.size()) {
					/**
					 * 假如单选矩阵一个选项都没选
					 */
					qAnswer.returnType = STATE_NOTHING;
					break;
				} else {
					// 隐藏选项
					int hideSize = 0;
					ArrayList<QuestionItem> rowItemArr = q.getRowItemArr();
					for (QuestionItem qi : rowItemArr) {
						if (1 == qi.hide) {
							// 得到有多少隐藏的
							hideSize++;
						}
					}

					/**
					 * 排斥或引用
					 */
					if ("1".equals(q.qInclusion) || "0".equals(q.qInclusion)) {
						// 隐藏选项
						if (rowMap.size() < (mTempRows.size() - hideSize-hideCount)) {
							qAnswer.returnType = STATE_ROW_LESS;
						}
					}
					// 隐藏选项
					else if (rowMap.size() < (q.getRowItemArr().size() - hideSize-hideCount)) {
						qAnswer.returnType = STATE_ROW_LESS;
						break;
					}
				}
			} else {
				/**
				 * 非必填
				 */
				qAnswer.returnType = Cnt.STATE_SUCCESS;
			}
			rowMap.clear();
			rowMap = null;
			/**
			 * 说明这道题设置了同一列连续作答的限制
			 */
			if (0 < q.qContinuous) {
				Iterator<Entry<Integer, Integer>> iter = radioMap.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<Integer, Integer> entry = iter.next();
					Integer v = entry.getValue();
					if (v > q.qContinuous) {
						qAnswer.returnType = STATE_CONTINUOUS;
						break;
					}
				}
			}
			radioMap.clear();
			radioMap = null;
			break;
		case Cnt.TYPE_MATRIX_CHECK_BOX:
			/**
			 * 保存每一行选了多少个
			 */
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (View view : vs) {
				if (view instanceof CheckBox) {
					CheckBox cb = (CheckBox) view;
					if (cb.isChecked()) {
						AnswerMap am = (AnswerMap) cb.getTag();
						// System.out.println("矩阵复选_name=" + am.getAnswerName()
						// + ", value=" + am.getAnswerValue());
						// qAnswer.getAnswerMapArr().add(am);
						answerMapArr.add(am);
						/**
						 * 如果am.getRow()那一行没有选过,则将其值置为1;否则,在其原有的值上加1
						 */
						map.put(am.getRow(), null == map.get(am.getRow()) ? 1 : (map.get(am.getRow()) + 1));
					} else if (1 != q.qRequired) {
						AnswerMap am = (AnswerMap) cb.getTag();
						am.setAnswerValue("");
						answerMapArr.add(am);
						if (cb.isChecked()) {
							map.put(am.getRow(), null == map.get(am.getRow()) ? 1 : (map.get(am.getRow()) + 1));
						}
					}
				}
			}

			if (1 == q.qRequired) {
				if (0 == map.size()) {
					/**
					 * 假如单选矩阵一个选项都没选
					 */
					qAnswer.returnType = STATE_NOTHING;
					break;
				} else {
					if ("1".equals(q.qInclusion) || "0".equals(q.qInclusion)) {
						if (map.size() != mTempRows.size()) {
							qAnswer.returnType = STATE_ROW_LESS;
							break;
						}
					} else if (map.size() != q.getRowItemArr().size()) {
						qAnswer.returnType = STATE_ROW_LESS;
						break;
					}
				}
			} else {
				qAnswer.returnType = Cnt.STATE_SUCCESS;
			}
			/**
			 * 说明这道题设置了上下限 或者的关系
			 */
			// 矩阵复选问题记录
			if (0 < q.lowerBound || 0 < q.upperBound) {
				Iterator<Entry<Integer, Integer>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<Integer, Integer> entry = it.next();
					Integer v = entry.getValue();
					System.out.println("v:" + v);
					if ((null != v) && (v < q.lowerBound)) {
						qAnswer.returnType = STATE_BOUND_MATRIX_LOWER;
						break;
					} else if ((null != v) && (v > q.upperBound && q.upperBound != 0)) {
						qAnswer.returnType = STATE_BOUND_MATRIX_UPPER;
						break;
					}
				}
			}
			/**
			 * 清空map中的数据
			 */
			map.clear();
			/**
			 * 断开内存地址映射
			 */
			map = null;
			break;

		case Cnt.TYPE_DROP_DOWN_LIST:// 下来列表
			for (View view : vs) {
				if (view instanceof Spinner) {
					Spinner sp = (Spinner) view;
					String name = Util.GetAnswerName(q, null, 0, 0, false);
					int value = sp.getSelectedItemPosition();
					// QuestionItem qi;
					// 1为忽略第一项，选第一项也算没选
					if (1 == q.ignoreFirstItem) {
						if (0 == value) {
							qAnswer.returnType = STATE_NOTHING;
							break;
						} else {
							String key = value + "_" + sp.getAdapter().getItem(value);
							int realValue = tvMap.get(key);
							AnswerMap am = new AnswerMap();
							am.setAnswerName(name);
							am.setAnswerValue(String.valueOf(realValue));
							am.setAnswerText(sp.getSelectedItem().toString());
							// qAnswer.getAnswerMapArr().add(am);
							answerMapArr.add(am);
							tvMap.clear();
							tvMap = null;
						}
					} else {
						String key = value + "_" + sp.getAdapter().getItem(value);
						int realValue = tvMap.get(key);
						AnswerMap am = new AnswerMap();
						am.setAnswerName(name);
						am.setAnswerValue(String.valueOf(realValue));
						am.setAnswerText(sp.getSelectedItem().toString());
						// qAnswer.getAnswerMapArr().add(am);
						answerMapArr.add(am);
						tvMap.clear();
						tvMap = null;
					}
				}
			}
			break;

		case Cnt.TYPE_FREE_TEXT_BOX:// 单行文本框

			if (isNew) {
				/** 数字类型的存储状态 **/
				syb = 0;
				/** 是否需要验证 **/
				boolean is = false;
				/** 临时记录item各个项 **/
				int tempIndex = 0;
				/** 是否判断有没有浮点数还是整数 **/
				int format = 10;
				/** 判断是不是数字题目并且最大值不等于空为true否则为false **/
				// boolean _syb = ("figure".equals(q.freeTextSort) &&
				// !Util.isEmpty(q.freeMaxNumber));// 1
				/** 不能重复时候把所有值放到集合中去判断 **/
				ArrayList<Double> repeat_value = new ArrayList<Double>();
				// 大树排序
				Log.i("zrl1", q.qSortChecked + "排序：");
				if (q.qSortChecked == 1) {
					orderRepeatList = new ArrayList<Integer>();
					orderRepeatMap = new HashMap<Integer, String>();
				}
				for (View view : vs) {
					tempIndex++;

					if (view instanceof EditText) {
						/**
						 * 文本、数字、邮件、日期
						 */
						QuestionItem item = (QuestionItem) view.getTag();
						if (null != item) {
							EditText et = (EditText) view;
							String value = et.getText().toString().trim();
							String name = Util.GetAnswerName(q, item, 0, 0, true);
							switch (item.type) {
							case 0:// 普通类型
									// 维码扫描
							case 4://数据字典
							case 6:
								if (1 == q.qRequired) {
									if (!Util.isEmpty(value)) {
										AnswerMap am = new AnswerMap();
										am.setAnswerName(name);
										am.setAnswerValue(value);
										am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
										answerMapArr.add(am);
										if (Cnt.STATE_FAIL != qAnswer.returnType) {
											/**
											 * 防止前面选项的题目没有填写
											 */
											qAnswer.returnType = Cnt.STATE_SUCCESS;
											is = true;
										}
									} else {
										// qAnswer.returnType = Cnt.STATE_FAIL;
										/**
										 * 题外关联 之 显示关联 第一次加载的时候 限制干预 大树 显示关联 22
										 */
										if (item.isHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											qAnswer.returnType = Cnt.STATE_FAIL;
										}
									}
								} else {
									// 看看是不是单项必填的
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
									if (!Util.isEmpty(value)) {
										is = true;
									} else {
										// 假如是必选的
										if (item.required) {
											currIndex = tempIndex;
											syb = ITEM_NO_REQUIRED;
											qAnswer.returnType = ITEM_NO_REQUIRED;
											break;
										}
									}
									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
								if (!is) {
									/**
									 * 一个答案都没填
									 */
									if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
										syb = STATE_NOTHING;
										qAnswer.returnType = STATE_NOTHING;
									}
								}
								break;
							case 1:// 日期格式 2013-08-08 16:12:49
								if (1 == q.qRequired) {
									// System.out.println("value:" + value);
									if (!Util.isEmpty(value)) {
										AnswerMap am = new AnswerMap();
										am.setAnswerName(name);
										am.setAnswerValue(value);
										am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
										answerMapArr.add(am);
										if (Cnt.STATE_FAIL != qAnswer.returnType) {
											/**
											 * 防止前面选项的题目没有填写
											 */
											qAnswer.returnType = Cnt.STATE_SUCCESS;
											is = true;
										}
									} else {
										/**
										 * 题外关联 之 显示关联 第一次加载的时候 限制干预 大树 显示关联 23
										 */
										if (item.isHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											qAnswer.returnType = Cnt.STATE_FAIL;
										}
									}
								} else {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
									if (!Util.isEmpty(value)) {
										is = true;
									} else {
										// 假如是必选的
										if (item.required) {
											currIndex = tempIndex;
											syb = ITEM_NO_REQUIRED;
											qAnswer.returnType = ITEM_NO_REQUIRED;
											break;
										}
									}

									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
								if (is) {
									/**
									 * 一个答案都没填
									 */
									// 获取引用的题号和顺序
									String titlefrom = item.titlefrom;
									String symbol = item.symbol;
									// 引用和逻辑不为空
									if (!Util.isEmpty(titlefrom) && !Util.isEmpty(symbol)) {
										String qIndex = titlefrom.split(",")[0];
										Answer answer = ma.dbService.getAnswer(feed.getUuid(), qIndex);
										Question preQues = ma.dbService.getQuestion(q.surveyId, qIndex);
										// 查到了
										if (answer != null && preQues != null) {
											ArrayList<AnswerMap> answerMapArr2 = answer.getAnswerMapArr();
											preIndex = Integer.parseInt(titlefrom.split(",")[1]);
											if (!Util.isEmpty(preQues.qid)) {
												preTitle = preQues.qid;
											} else {
												preTitle = getResources().getString(R.string.question_order, preQues.qOrder);
											}
											AnswerMap answerMap = answerMapArr2.get(preIndex);
											String dateValue1 = answerMap.getAnswerValue();
											try {
												boolean dateCompare = Util.getDateCompare(dateValue1, value, symbol);
												// System.out.println("比较结果:" +
												// dateCompare);
												if (dateCompare) {
													/**** TODO **/
													// 验证成功
												} else {
													currIndex = tempIndex;
													// System.out.println("验证不成功的:"
													// + dateCompare);
													if ("=".equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_EQUAL;
													} else if ("!=".equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_NO_EQUAL;
													} else if (">".equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_UPPER;
													} else if (">=".equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_UPPER_EQUAL;
													} else if ("<".equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_LOW;
													} else if ("<=".equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_LOW_EQUAL;
													}
													break;
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}
										}
										// 没查到
										else {
											/**** TODO **/
											// 服务器配置xml错误
										}
									}
								} else {
									/**
									 * 一个答案都没填
									 */
									if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
										syb = STATE_NOTHING;
										qAnswer.returnType = STATE_NOTHING;
									}
								}
								break;

							case 2:// 数字格式
								if (1 == q.qRequired) {
									if (!Util.isEmpty(value)) {
										AnswerMap am = new AnswerMap();

										/**
										 * 题外关联 之 外部关联 设置 数字标示 大树 外部关联 2
										 */
										am.isFigure = true;

										/**
										 * 题外关联 之 内部关联 设置 SUM标示 大树 内部关联 12
										 */

										if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
											am.isSUM = true;
										}

										am.setAnswerName(name);
										am.setAnswerValue(value);
										am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
										answerMapArr.add(am);
										if (Cnt.STATE_FAIL != qAnswer.returnType) {
											/**
											 * 防止前面选项的题目没有填写
											 */
											qAnswer.returnType = Cnt.STATE_SUCCESS;
											is = true;
										}
									} else {

										/**
										 * 题外关联 之 显示关联 第一次加载的时候 限制干预 大树 显示关联 24
										 */
										if (item.isHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											qAnswer.returnType = Cnt.STATE_FAIL;
										}

									}
								} else {
									AnswerMap am = new AnswerMap();
									/**
									 * 题外关联 之 外部关联 设置 数字标示 大树 外部关联 3
									 */
									am.isFigure = true;

									/**
									 * 题外关联 之 内部关联 设置 SUM标示 大树 内部关联 13
									 */

									if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
										am.isSUM = true;
									}
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
									if (!Util.isEmpty(value)) {
										is = true;
									} else {
										// 假如是必选的
										if (item.required) {
											currIndex = tempIndex;
											syb = ITEM_NO_REQUIRED;
											qAnswer.returnType = ITEM_NO_REQUIRED;
											break;
										}
									}

									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}

								if (is) {
									q.special=item.specialNumber;
									if (item.isFloat) {
										format = 9;// 浮点 整数
									} else {
										format = 10;// 整数
									}
									if (Util.isFormat(value, format)) {
										// 需要验证。是否是数字并且是否为数字
										/**
										 * IMS临时干预价格 4月份执行final 判断 浮点数位数 是否2位
										 */
										Log.i("zrl1", "走这里");
										boolean isIms = false; // 判断是不是
																// IMS的项目以及干预题目
										if (Cnt.appVersion == 3 && "132".equals(feed.getSurveyId())) {

											if (q.qIndex == 299 || q.qIndex == 302 || q.qIndex == 306 || q.qIndex == 309 || q.qIndex == 312 || q.qIndex == 315 || q.qIndex == 318 || q.qIndex == 321
													|| q.qIndex == 324 || q.qIndex == 327 || q.qIndex == 350 || q.qIndex == 330 || q.qIndex == 333 || q.qIndex == 336 || q.qIndex == 341) {
												isIms = true;
												if (value.contains(".")) {
													String[] ss = value.split("\\.");
													if (ss[1].length() != 2) {
														Log.i("zrl1", "不合条件");
														qAnswer.returnType = DECIMAL_PLACES_NO_MEET;
													}
												}
											}
										} else if (Cnt.appVersion == 3 && "134".equals(feed.getSurveyId())) {
											if (q.qIndex == 299 || q.qIndex == 302 || q.qIndex == 420 || q.qIndex == 306 || q.qIndex == 309 || q.qIndex == 312 || q.qIndex == 315 || q.qIndex == 318
													|| q.qIndex == 321 || q.qIndex == 324 || q.qIndex == 429 || q.qIndex == 438 || q.qIndex == 327 || q.qIndex == 350 || q.qIndex == 330
													|| q.qIndex == 333 || q.qIndex == 336 || q.qIndex == 341) {

												isIms = true;
												if (value.contains(".")) {
													String[] ss = value.split("\\.");
													if (ss[1].length() != 2) {
														Log.i("zrl1", "不合条件");
														qAnswer.returnType = DECIMAL_PLACES_NO_MEET;
													}
												}
											}
										} else if (Cnt.appVersion == 3 && "138".equals(feed.getSurveyId())) {
											if (q.qIndex == 299 || q.qIndex == 302 || q.qIndex == 420 || q.qIndex == 306 || q.qIndex == 309 || q.qIndex == 312 || q.qIndex == 315 || q.qIndex == 318
													|| q.qIndex == 321 || q.qIndex == 324 || q.qIndex == 429 || q.qIndex == 438 || q.qIndex == 327 || q.qIndex == 350 || q.qIndex == 330
													|| q.qIndex == 333 || q.qIndex == 336 || q.qIndex == 341) {

												isIms = true;
												if (value.contains(".")) {
													String[] ss = value.split("\\.");
													if (ss[1].length() != 2) {
														Log.i("zrl1", "不合条件");
														qAnswer.returnType = DECIMAL_PLACES_NO_MEET;
													}
												}
											}
										}
										//  138  6月份 final  临时干预  
										else if (Cnt.appVersion==3 && "145".equals(feed.getSurveyId())) {
											if (q.qIndex == 299 || q.qIndex == 302 || q.qIndex == 420 || q.qIndex == 306 || q.qIndex == 309 || q.qIndex == 312 || q.qIndex == 315 || q.qIndex == 318
													|| q.qIndex == 321 || q.qIndex == 324 || q.qIndex == 429 || q.qIndex == 438 || q.qIndex == 327 || q.qIndex == 350 || q.qIndex == 330
													|| q.qIndex == 333 || q.qIndex == 336 || q.qIndex == 341
													|| q.qIndex == 447 || q.qIndex == 456) {

												isIms = true;
												if (value.contains(".")) {
													String[] ss = value.split("\\.");
													if (ss[1].length() != 2) {
														Log.i("zrl1", "不合条件");
														qAnswer.returnType = DECIMAL_PLACES_NO_MEET;
													}
												}
											}
										}
										//特殊值处理
										/*else if(!Util.isEmpty(item.specialNumber)){
											String itemsp=item.specialNumber;
											String[] speciallist=itemsp.split(",");
											boolean issp=false;
											for (String special : speciallist) {
												double v = Double.parseDouble(value);
												double spe=Double.parseDouble(special);
												if ( v == spe) {
													issp=true;
													break;
												}
											}
											if(issp){
												qAnswer.returnType = Cnt.STATE_SUCCESS;
											}else {
												currIndex = tempIndex;
												syb = STATE_SYB_MIN;
												q.freeMinNumber = item.minNumber;
												qAnswer.returnType = STATE_SYB_MIN;
											}
											break;
										}*/
									    //  145   7月份 final  临时干预  
										double v = Double.parseDouble(value);
										if (!Util.isEmpty(item.minNumber)) {
											// 假如v< 最小的值 不合法 跳出本次循环
											if (v < (Util.isEmpty(item.minNumber) ? 0 : Double.parseDouble(item.minNumber))) {
												// IMS临时干预价格 如果是0 过 如果 不是两位 小数
												// 也过
												Log.i("zrl1", "v==" + v);
												if (isIms && v == 0.0) {
													qAnswer.returnType = Cnt.STATE_SUCCESS;
												} else {
													currIndex = tempIndex;
													syb = STATE_SYB_MIN;
													q.freeMinNumber = item.minNumber;
													qAnswer.returnType = STATE_SYB_MIN;
												}
												break;
												// return Cnt.STATE_SYB;
												// 假如v> 最大的值 不合法 跳出本次循环
											}
										}
										
										if (!Util.isEmpty(item.maxNumber)) {
											if (v > (Util.isEmpty(item.maxNumber) ? 0 : Double.parseDouble(item.maxNumber))) {
												currIndex = tempIndex;
												q.freeMaxNumber = item.maxNumber;
												syb = STATE_SYB_MAX;
												qAnswer.returnType = STATE_SYB_MAX;
												break;
											}
										}

										// 假如不能重复并且 重复集合中找到这个值 不合法 退出
										if (1 == q.freeNoRepeat && -1 != repeat_value.indexOf(Double.parseDouble(value))) {
											syb = STATE_SYB_REPEAT;
											qAnswer.returnType = STATE_SYB_REPEAT;
											break;
										}
										repeat_value.add(Double.parseDouble(value));

										/** 比较和原先题目 **/
										/**
										 * 一个答案都没填
										 */
										// 获取引用的题号和顺序
										String titlefrom = item.titlefrom;
										String symbol = item.symbol;
										// 引用和逻辑不为空
										if (!Util.isEmpty(titlefrom) && !Util.isEmpty(symbol)) {
											String qIndex = titlefrom.split(",")[0];
											Answer answer = ma.dbService.getAnswer(feed.getUuid(), qIndex);
											Question preQues = ma.dbService.getQuestion(q.surveyId, qIndex);
											// 查到了
											if (answer != null && preQues != null) {
												ArrayList<AnswerMap> answerMapArr2 = answer.getAnswerMapArr();
												preIndex = Integer.parseInt(titlefrom.split(",")[1]);
												if (!Util.isEmpty(preQues.qid)) {
													preTitle = preQues.qid;
												} else {
													preTitle = getResources().getString(R.string.question_order, preQues.qOrder);
												}
												AnswerMap answerMap = answerMapArr2.get(preIndex);
												String preValue = answerMap.getAnswerValue();
												try {
													boolean dateCompare = Util.getDoubleNumberCompare(preValue, value, symbol);
													// System.out.println("比较结果:"
													// + dateCompare + "   现在的值"
													// + value + "  原有的值:" +
													// preValue);
													if (dateCompare) {
														/**** TODO **/
														// 验证成功
													} else {
														currIndex = tempIndex;
														// System.out.println("验证不成功的:"
														// + dateCompare);
														if ("=".equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_EQUAL;
														} else if ("!=".equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_NO_EQUAL;
														} else if (">".equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_UPPER;
														} else if (">=".equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_UPPER_EQUAL;
														} else if ("<".equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_LOW;
														} else if ("<=".equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_LOW_EQUAL;
														}
														break;
													}
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
											// 没查到
											else {
												/**** TODO **/
												// 服务器配置xml错误
											}
										}
										sum += v;
									} else {

										currIndex = tempIndex;
										if (format == 9) {
											// 浮点

											syb = FORMAT_NO_NUMBER;
											qAnswer.returnType = FORMAT_NO_NUMBER;
										} else if (format == 10) {
											// 数字
											syb = FORMAT_NO_FLOAT_NUMBER;
											qAnswer.returnType = FORMAT_NO_FLOAT_NUMBER;
										}
									}
								} else {
									/**
									 * 一个答案都没填
									 */
									if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
										syb = STATE_NOTHING;
										qAnswer.returnType = STATE_NOTHING;
									}
								}

								break;

							case 3:// 英文/数字格式
							case 5:// 邮件
								if (1 == q.qRequired) {
									if (!Util.isEmpty(value)) {
										AnswerMap am = new AnswerMap();
										am.setAnswerName(name);
										am.setAnswerValue(value);
										am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
										answerMapArr.add(am);
										if (Cnt.STATE_FAIL != qAnswer.returnType) {
											/**
											 * 防止前面选项的题目没有填写
											 */
											qAnswer.returnType = Cnt.STATE_SUCCESS;
											is = true;
										}
									} else {

										/**
										 * 题外关联 之 显示关联 第一次加载的时候 限制干预 大树 显示关联 25
										 */
										if (item.isHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											qAnswer.returnType = Cnt.STATE_FAIL;
										}
									}
								} else {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
									if (!Util.isEmpty(value)) {
										is = true;
									} else {
										// 假如是必选的
										if (item.required) {
											currIndex = tempIndex;
											syb = ITEM_NO_REQUIRED;
											qAnswer.returnType = ITEM_NO_REQUIRED;
											break;
										}
									}

									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
								if (is) {
									if (3 == item.type) {
										// 判断是不是数字\英文
										if (Util.isFormat(value, 11) && (value.getBytes().length == value.length())) {
											// 是数字或者英文
										} else {
											// 不是
											currIndex = tempIndex;
											syb = FORMAT_NO_NUMBER_AND_WORD;
											qAnswer.returnType = FORMAT_NO_NUMBER_AND_WORD;
										}
									} else if (5 == item.type) {
										// 判断是不是邮件格式
										if (Util.isFormat(value, 4)) {
											// 是正确的邮箱地址
										} else {
											// 不是
											currIndex = tempIndex;
											syb = FORMAT_NO_EMAIL;
											qAnswer.returnType = FORMAT_NO_EMAIL;
										}
									}
								} else {
									/**
									 * 一个答案都没填
									 */
									if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
										syb = STATE_NOTHING;
										qAnswer.returnType = STATE_NOTHING;
									}
								}
								break;
							}
						}

					} else if (view instanceof Spinner) {
						// item.type=0
						/**
						 * 下拉,含1%%2%%3 三级联动关联 问题的解决 @ 存值问题的解决！
						 */
						QuestionItem item = (QuestionItem) view.getTag();
						if (null != item) {
							Spinner sp = (Spinner) view;
							// System.out.println("类型:" + sp.toString());
							String name = Util.GetAnswerName(q, item, 0, 0, true);
							String value = sp.getSelectedItem().toString();
							// 大树排序
							if (orderRepeatList != null) {
								orderRepeatList.add(sp.getSelectedItemPosition());
								orderRepeatMap.put(sp.getSelectedItemPosition(), value);
							}
							/**
							 * 三级联动 存值 设置
							 */
							// System.out.println("下拉框value:" + value);
							if (1 == q.qRequired) {
								// if(Cnt.STATE_FAIL == qAnswer.answerType){
								// qAnswer.answerType = Cnt.STATE_FAIL;
								// }
								if (!Util.isEmpty(value)) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
									if (Cnt.STATE_FAIL != qAnswer.returnType) {
										qAnswer.returnType = Cnt.STATE_SUCCESS;
									}
								} else {
									syb = STATE_NOTHING;
									qAnswer.returnType = STATE_NOTHING;
								}
							} else {
								if (!Util.isEmpty(value)) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
								} else {
									if (item.required) {
										currIndex = tempIndex;
										syb = ITEM_NO_REQUIRED;
										qAnswer.returnType = ITEM_NO_REQUIRED;
										break;
									}
								}
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							}
						}

					} else if (view instanceof SeekBar) {
						// item.type=2
						/**
						 * 拖动题
						 */
						QuestionItem item = (QuestionItem) view.getTag();
						if (null != item) {
							SeekBar sb = (SeekBar) view;
							String name = Util.GetAnswerName(q, item, 0, 0, true);
							int value = sb.getProgress();
							if (1 == q.qRequired) {
								if (!Util.isEmpty(value + "")) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value + "");
									answerMapArr.add(am);
									if (Cnt.STATE_FAIL != qAnswer.returnType) {
										/**
										 * 防止前面选项的题目没有填写
										 */
										qAnswer.returnType = Cnt.STATE_SUCCESS;
										is = true;
									}
								} else {
									qAnswer.returnType = Cnt.STATE_FAIL;
								}
							} else {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setAnswerValue(value + "");
								answerMapArr.add(am);
								is = true;
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							}

							if (is) {
								// 需要验证。是否是数字并且是否为数字

								if (!Util.isEmpty(item.minNumber)) {
									// 假如v< 最小的值 不合法 跳出本次循环
									if (value < (Util.isEmpty(item.minNumber) ? 0 : Integer.parseInt(item.minNumber))) {
										currIndex = tempIndex;
										syb = STATE_SYB_MIN;
										q.freeMinNumber = item.minNumber;
										qAnswer.returnType = STATE_SYB_MIN;
										break;
									}
									// return Cnt.STATE_SYB;

									// 假如v> 最大的值 不合法 跳出本次循环
								}
								if (!Util.isEmpty(item.maxNumber)) {
									if (value > (Util.isEmpty(item.maxNumber) ? 0 : Integer.parseInt(item.maxNumber))) {
										currIndex = tempIndex;
										q.freeMaxNumber = item.maxNumber;
										syb = STATE_SYB_MAX;
										qAnswer.returnType = STATE_SYB_MAX;
										break;
									}
								}
								/** 比较和原先题目 **/
								/**
								 * 一个答案都没填
								 */
								// 获取引用的题号和顺序
								String titlefrom = item.titlefrom;
								String symbol = item.symbol;
								// 引用和逻辑不为空
								if (!Util.isEmpty(titlefrom) && !Util.isEmpty(symbol)) {
									String qIndex = titlefrom.split(",")[0];
									Answer answer = ma.dbService.getAnswer(feed.getUuid(), qIndex);
									Question preQues = ma.dbService.getQuestion(q.surveyId, qIndex);
									// 查到了
									if (answer != null && preQues != null) {
										ArrayList<AnswerMap> answerMapArr2 = answer.getAnswerMapArr();
										preIndex = Integer.parseInt(titlefrom.split(",")[1]);
										if (!Util.isEmpty(preQues.qid)) {
											preTitle = preQues.qid;
										} else {
											preTitle = getResources().getString(R.string.question_order, preQues.qOrder);
										}
										AnswerMap answerMap = answerMapArr2.get(preIndex);
										String preValue = answerMap.getAnswerValue();
										try {
											boolean dateCompare = Util.getDoubleNumberCompare(preValue, value + "", symbol);
											// System.out.println("比较结果:" +
											// dateCompare);
											if (dateCompare) {
												/**** TODO **/
												// 验证成功
											} else {
												currIndex = tempIndex;
												// System.out.println("验证不成功的:"
												// + dateCompare);
												if ("=".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_EQUAL;
												} else if ("!=".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_NO_EQUAL;
												} else if (">".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_UPPER;
												} else if (">=".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_UPPER_EQUAL;
												} else if ("<".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_LOW;
												} else if ("<=".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_LOW_EQUAL;
												}
											}
										} catch (ParseException e) {
											e.printStackTrace();
										}
									}
									// 没查到
									else {
										/**** TODO **/
										// 服务器配置xml错误
									}
								}
							} else {
								/**
								 * 一个答案都没填
								 */
								if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
									syb = STATE_NOTHING;
									qAnswer.returnType = STATE_NOTHING;
								}
							}

						}

					}

					// else if (view instanceof AutoCompleteTextView) {
					// /**
					// * 字典 item.type=4
					// */
					//
					// }

					is = false;
					if (syb == STATE_NOTHING || syb == STATE_SYB_DATE_EQUAL || syb == STATE_SYB_DATE_UPPER_EQUAL || syb == STATE_SYB_DATE_NO_EQUAL || syb == STATE_SYB_DATE_UPPER
							|| syb == STATE_SYB_DATE_LOW_EQUAL || syb == STATE_SYB_DATE_LOW || syb == STATE_SYB_MIN || syb == STATE_SYB_MAX || syb == STATE_SYB_REPEAT || syb == FORMAT_NO_FLOAT_NUMBER
							|| syb == FORMAT_NO_NUMBER || syb == STATE_SYB_SUM_VALUE || syb == FORMAT_NO_NUMBER_AND_WORD || syb == FORMAT_NO_EMAIL || syb == ITEM_NO_REQUIRED) {
						sum = 0;
						break;
					}

				}

			} else {
				// System.out.println("老版本2");
				syb = 0;
				boolean _syb = ("figure".equals(q.freeTextSort) && !Util.isEmpty(q.freeMaxNumber));// 1
				// !=
				// q.qDragChecked
				// &&
				int sum1 = 0;
				boolean is = false;
				ArrayList<String> repeat_value = new ArrayList<String>();
				for (View view : vs) {
					if (view instanceof EditText) {
						QuestionItem item = (QuestionItem) view.getTag();
						if (null != item) {
							EditText et = (EditText) view;
							// et.set
							String value = et.getText().toString().trim();
							String name = Util.GetAnswerName(q, item, 0, 0, true);
							// System.out.println("文本_name=" + name + ", value="
							// +
							// value);
							if (1 == q.qRequired) {
								if (!Util.isEmpty(value)) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
									System.out.println("最后一次:" + qAnswer.returnType);
									if (Cnt.STATE_FAIL != qAnswer.returnType) {
										/**
										 * 防止前面选项的题目没有填写
										 */
										qAnswer.returnType = Cnt.STATE_SUCCESS;
										is = true;
									}
								} else {
									System.out.println("失败");
									qAnswer.returnType = Cnt.STATE_FAIL;
								}
							} else {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setAnswerValue(value);
								am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
								answerMapArr.add(am);
								if (!Util.isEmpty(value)) {
									is = true;
								}

								qAnswer.returnType = Cnt.STATE_SUCCESS;
							}

							/**
							 * 判断文本框中填写的数字是否在合理的范围内, 并且各个数字之和是否符合标准
							 */
							if (is) {
								if (_syb && Util.isFormat(value, -1)) {
									int v = Integer.parseInt(value);
									if (v < (Util.isEmpty(q.freeMinNumber) ? 0 : Integer.parseInt(q.freeMinNumber))) {
										syb = STATE_SYB_MIN;
										qAnswer.returnType = STATE_SYB_MIN;
										break;
										// return Cnt.STATE_SYB;
									} else if (v > (Util.isEmpty(q.freeMaxNumber) ? 0 : Integer.parseInt(q.freeMaxNumber))) {
										syb = STATE_SYB_MAX;
										qAnswer.returnType = STATE_SYB_MAX;
										break;
									} else if (1 == q.freeNoRepeat && -1 != repeat_value.indexOf(value)) {
										syb = STATE_SYB_REPEAT;
										qAnswer.returnType = STATE_SYB_REPEAT;
										break;
									}
									sum1 += v;
								} else if ("figure".equals(q.freeTextSort) && !Util.isEmpty(value) && !Util.isFormat(value, -1)) {
									qAnswer.returnType = FORMAT_NO_NUMBER;
								}// /
								repeat_value.add(value);
							} else {
								/**
								 * 一个答案都没填
								 */
								if (Cnt.STATE_SUCCESS != qAnswer.returnType) {

									qAnswer.returnType = STATE_NOTHING;

								}

							}

							// qAnswer.getAnswerMapArr().add(am);
						}
					} else if (view instanceof Spinner) {
						QuestionItem item = (QuestionItem) view.getTag();
						if (null != item) {
							Spinner sp = (Spinner) view;
							String name = Util.GetAnswerName(q, item, 0, 0, true);
							String value = sp.getSelectedItem().toString();
							if (1 == q.qRequired) {
								// if(Cnt.STATE_FAIL == qAnswer.answerType){
								// qAnswer.answerType = Cnt.STATE_FAIL;
								// }
								if (!Util.isEmpty(value)) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
									if (Cnt.STATE_FAIL != qAnswer.returnType) {
										qAnswer.returnType = Cnt.STATE_SUCCESS;
									}
								} else {
									qAnswer.returnType = Cnt.STATE_FAIL;
								}
							} else {
								if (!Util.isEmpty(value)) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
									answerMapArr.add(am);
								}
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							}
						}
					} else if (view instanceof SeekBar) {
						QuestionItem item = (QuestionItem) view.getTag();
						if (null != item) {
							SeekBar sb = (SeekBar) view;
							String name = Util.GetAnswerName(q, item, 0, 0, true);
							int value = sb.getProgress();
							if (1 == q.qRequired) {// TODO此处可能要判断是否大于最小值且小于最大值
								if (0 < value) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value + "");
									// am.setAnswerText(item.caption + ":" +
									// value);
									answerMapArr.add(am);
									if (Cnt.STATE_FAIL != qAnswer.returnType) {
										qAnswer.returnType = Cnt.STATE_SUCCESS;
										is = true;
									}
								} else {
									qAnswer.returnType = Cnt.STATE_FAIL;
								}
							} else {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setAnswerValue(value + "");
								answerMapArr.add(am);
								is = true;
								/**
								 * 滚动题是非必填的,则让它过
								 */
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							}

							/**
							 * 判断文本框中填写的数字是否在合理的范围内, 并且各个数字之和是否符合标准
							 */
							if (is) {
								if (_syb) {
									if (value < (Util.isEmpty(q.freeMinNumber) ? 0 : Integer.parseInt(q.freeMinNumber))) {
										syb = STATE_SYB_MIN;
										qAnswer.returnType = STATE_SYB_MIN;
										break;
										// return Cnt.STATE_SYB;
									} else if (value > (Util.isEmpty(q.freeMaxNumber) ? 0 : Integer.parseInt(q.freeMaxNumber))) {
										syb = STATE_SYB_MAX;
										qAnswer.returnType = STATE_SYB_MAX;
										break;
									} else if (1 == q.freeNoRepeat && -1 != repeat_value.indexOf(value + "")) {
										// System.out.println("数字不能重复");
										syb = STATE_SYB_REPEAT;
										qAnswer.returnType = STATE_SYB_REPEAT;
										break;
									}
									sum1 += value;
									repeat_value.add(value + "");
								}
							}

						} else {
							/**
							 * 一个答案都没填
							 */
							if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
								qAnswer.returnType = STATE_NOTHING;
							}
						}
					}
				}

				if ("<".equals(q.freeSymbol)) {
					if (!(sum1 < Integer.parseInt(q.freeSumNumber)) && syb != qAnswer.returnType) {
						Toasts.makeText(NativeModeActivity.this, getResources().getString(R.string.question_sum, q.freeSymbol + q.freeSumNumber), Toast.LENGTH_LONG).show();
						return TOTAL_SUM_TOAST;
					}
				} else if ("<=".equals(q.freeSymbol)) {
					if (!(sum1 <= Integer.parseInt(q.freeSumNumber)) && syb != qAnswer.returnType) {
						Toasts.makeText(NativeModeActivity.this, getResources().getString(R.string.question_sum, q.freeSymbol + q.freeSumNumber), Toast.LENGTH_LONG).show();
						return TOTAL_SUM_TOAST;
					}
				} else if (">".equals(q.freeSymbol)) {
					if (!(sum1 > Integer.parseInt(q.freeSumNumber)) && syb != qAnswer.returnType) {
						Toasts.makeText(NativeModeActivity.this, getResources().getString(R.string.question_sum, q.freeSymbol + q.freeSumNumber), Toast.LENGTH_LONG).show();
						return TOTAL_SUM_TOAST;
					}
				} else if (">=".equals(q.freeSymbol)) {
					if (!(sum1 >= Integer.parseInt(q.freeSumNumber)) && syb != qAnswer.returnType) {
						Toasts.makeText(NativeModeActivity.this, getResources().getString(R.string.question_sum, q.freeSymbol + q.freeSumNumber), Toast.LENGTH_LONG).show();
						return TOTAL_SUM_TOAST;
					}
				} else if ("=".equals(q.freeSymbol)) {
					if (!(sum1 == Integer.parseInt(q.freeSumNumber)) && syb != qAnswer.returnType) {
						Toasts.makeText(NativeModeActivity.this, getResources().getString(R.string.question_sum, q.freeSymbol + q.freeSumNumber), Toast.LENGTH_LONG).show();
						return TOTAL_SUM_TOAST;
					}
				}
			}

			break;

		case Cnt.TYPE_FREE_TEXT_AREA:// 文本域
			for (View view : vs) {
				if (view instanceof EditText) {
					QuestionItem item = (QuestionItem) view.getTag();
					if (null != item) {
						EditText et = (EditText) view;
						// et.set
						String value = et.getText().toString().trim();
						String name = Util.GetAnswerName(q, item, 0, 0, true);
						// System.out.println("文本_name=" + name + ", value=" +
						// value);
						if (1 == q.qRequired) {
							if (!Util.isEmpty(value)) {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setAnswerValue(value);
								am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
								answerMapArr.add(am);
								if (Cnt.STATE_FAIL != qAnswer.returnType) {
									/**
									 * 防止前面选项的题目没有填写
									 */
									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
							} else {
								qAnswer.returnType =STATE_NOTHING;
							}
						} else {
							AnswerMap am = new AnswerMap();
							am.setAnswerName(name);
							am.setAnswerValue(value);
							am.setAnswerText(Util.isEmpty(item.caption) ? value : (item.caption + ":" + value));
							answerMapArr.add(am);
							qAnswer.returnType = Cnt.STATE_SUCCESS;
						}
					}
				}
			}
			break;
		}
		/**
		 *  IMS 临时干预    皮炎湿疹     单选自动下一页   注释掉    单选自动下一页
		 */
		if (!Util.isEmpty(answerMapArr)) {
			Log.i("zrl1", "不为空"); 
		}else {
			Log.i("zrl1", "为空");
		}
		if (imsIntervetion.isSkipNext(imsIntervetion, q, 89,answerMapArr)) {
			qAnswer.returnType=LUO_JI_WRONG;  
			//  
		}
		/**
		 * 题外关联 干预 在这里 大树 外部关联 3
		 */
		String relevanceIndex = "0";// 关联的 上一题的 index
		int relevanceOptionIndex = 0;// 关联的上一题的 哪一个选项
		if (ComUtil.questionOutyingRelevance(q, answerMapArr, ma, feed)) {
			Log.i("zrl1", "走这里1" + "关联类型" + q.qParentAssociatedCheck);
			qAnswer.returnType = QUESTION_OUT_YING_RELEVANCE;
			String[] ss = q.qParentAssociatedCheck.split(",");
			relevanceIndex = ss[1];
			relevanceOptionIndex = Integer.valueOf(ss[2]) + 1;
		}  
		/**
		 * 题外关联 之和 干预 以上
		 */

		/**
		 * 题外关联 之 内部关联 以下 求和标志选项 这里 需要修改一下 有一个 小 bug 大树 内部关联 14
		 */

		String sumSign = "";
		if (qAnswer.returnType != STATE_NOTHING && q.qParentAssociatedCheck != null) {
			double valueSum = 0;
			double itemSUm = 0;
			BigDecimal big = new BigDecimal("0");
			boolean isInterRe = false;
			if (answerMapArr != null && answerMapArr.size() > 0) {
				for (AnswerMap map : answerMapArr) {
					String value = map.getAnswerText().trim();
					if (map.isSUM && !value.equals("")) {
						isInterRe = true;
						if (Util.isFormat(value, 9)) {
							sumSign = String.valueOf((Integer.valueOf(map.getAnswerName().split("_")[3]) + 1));
							valueSum = Double.valueOf(value);
						}
					} else if (map.isFigure && !value.equals("")) {
						if (Util.isFormat(value, 9)) {
							itemSUm += Double.valueOf(value);
							big = big.add(new BigDecimal(value));

						}
					}
				}
			}
			if (isInterRe && valueSum != big.doubleValue()) {
				qAnswer.returnType = QUESTION_INTERIOR_RELEVANCE;
			}
		}
		/**
		 * 题外关联 之 内部关联 以上   
		 */
		
		/**
		 * 大树排序 如果 重复选项不为空 那么给出提示
		 */
		// Log.i("zrl1", orderRepeatList.size()+"");// 测试录音异常
		if (!Util.isEmpty(orderRepeatList)) {
			Log.i("zrl1", "orderList:==" + orderRepeatList.size());
			if (orderRepeatList.size() > orderRepeatMap.size()) {
				qAnswer.returnType = QUESTION_ITEM_ORDER_REPEAT;
			}
			orderRepeatList.clear();
			orderRepeatList = null;
			orderRepeatMap.clear();
			orderRepeatMap = null;
		}
		if (!isNoValidate) {
			String strTilte = "";
			if (!Util.isEmpty(q.qid)) {
				strTilte = q.qid;
			} else {
				strTilte = getResources().getString(R.string.question_order, q.qOrder);
			}
			if (STATE_NOTHING == qAnswer.returnType) {
				Util.viewShake(NativeModeActivity.this, bodyView);
			} else if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
				// System.out.println(" qAnswer.returnType:" +
				// qAnswer.returnType);
				Message msg = Message.obtain();
				msg.what = qAnswer.returnType;

				switch (qAnswer.returnType) {
				case STATE_SYB_MAX:
					if (isNew) {
						msg.obj = getResources().getString(R.string.max_value, currIndex, q.freeMaxNumber);
					} else {
						msg.obj = getResources().getString(R.string.old_max_value, q.freeMaxNumber);
					}
					break;

				case STATE_SYB_MIN:
					if (isNew) {
						msg.obj = getResources().getString(R.string.min_value, currIndex, q.freeMinNumber);
					} else {
						msg.obj = getResources().getString(R.string.old_min_value, q.freeMinNumber);
					}
					break;

				case FORMAT_NO_NUMBER:
					if (isNew) {
						msg.obj = getResources().getString(R.string.format_no_number, currIndex + "");
					} else {
						msg.obj = getResources().getString(R.string.old_format_no_number);
					}
					break;

				case STATE_BOUND_LOWER:
					msg.obj = getResources().getString(R.string.bound_min, q.lowerBound);
					break;

				case STATE_BOUND_UPPER:
					msg.obj = getResources().getString(R.string.bound_max, q.upperBound);
					break;

				case STATE_CONTINUOUS:
					msg.obj = getResources().getString(R.string.question_coutinuous, q.qContinuous);
					break;

				case STATE_BOUND_MATRIX_LOWER:
					msg.obj = getResources().getString(R.string.bound_matrix_min, q.lowerBound);
					break;

				case STATE_BOUND_MATRIX_UPPER:
					msg.obj = getResources().getString(R.string.bound_matrix_max, q.upperBound);
					break;

				case STATE_ROW_LESS:
					msg.obj = getResources().getString(R.string.row_less);
					break;

				case STATE_SYB_REPEAT:
					msg.obj = getResources().getString(R.string.value_repeat);
					break;
				case STATE_SYB_DATE_EQUAL:
					msg.obj = getResources().getString(R.string.date_equal_error, strTilte, currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_UPPER_EQUAL:
					msg.obj = getResources().getString(R.string.date_upper_equal_error, strTilte, currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_NO_EQUAL:
					msg.obj = getResources().getString(R.string.date_no_equal_error, strTilte, currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_UPPER:
					msg.obj = getResources().getString(R.string.date_upper_error, strTilte, currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_LOW_EQUAL:
					msg.obj = getResources().getString(R.string.date_low_equal_error, strTilte, currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_LOW:
					msg.obj = getResources().getString(R.string.date_low_error, strTilte, currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case FORMAT_NO_FLOAT_NUMBER:
					msg.obj = getResources().getString(R.string.format_no_float_number, currIndex + "");
					break;
				case STATE_SYB_SUM_VALUE:
					msg.obj = getResources().getString(R.string.question_sum, q.freeSymbol + q.freeSumNumber);
					break;
				case FORMAT_NO_NUMBER_AND_WORD:
					msg.obj = getResources().getString(R.string.format_no_word_and_number, currIndex + "");
					break;
				case FORMAT_NO_EMAIL:
					msg.obj = getResources().getString(R.string.format_no_email, currIndex + "");
					break;
				case ITEM_NO_REQUIRED:
					msg.obj = getResources().getString(R.string.item_no_required, currIndex + "");
					break;
				/**
				 * 题外关联之和 提示语 的展现 ！ 大树 外部关联 4
				 */
				case QUESTION_OUT_YING_RELEVANCE:
					msg.obj = getString(R.string.question_outying_error, relevanceIndex, relevanceOptionIndex + "");
					break;
				/**
				 * 题外关联 之和 内部关联 大树 内部关联 15
				 */
				case QUESTION_INTERIOR_RELEVANCE:
					msg.obj = getString(R.string.question_innerying_error, sumSign);
					break;
				/**
				 * IMS临时干预价格 小数点位数为 2
				 */
				case DECIMAL_PLACES_NO_MEET:
					msg.obj = getString(R.string.decimel_places_no_meet);
					break;
				/**
				 * 大树排序 选项值不能重复
				 */
				case QUESTION_ITEM_ORDER_REPEAT:
					msg.obj = getString(R.string.sequence_repeat);
					break;
				case LUO_JI_WRONG:  //  IMS  皮炎湿疹  
					msg.obj=getString(R.string.luoji_wrong);  
					break; 
				}
				currIndex = 0;
				handler.sendMessage(msg);
				return qAnswer.returnType;
			}
			if (isNew) {
				if (1 == q.qRequired && syb == STATE_NOTHING && q.qType == Cnt.TYPE_FREE_TEXT_BOX) {
					return qAnswer.returnType;
				}
				/**** 数字之和开始 **/
				// 看看数值题目的文本题是否是小于号
				if ("=".equals(q.freeSymbol)) {
					// 假如总值不小于最大值并且没有其他逻辑错误 提示值必须大于
					if (!(sum == Double.parseDouble(q.freeSumNumber))) {
						syb = STATE_SYB_SUM_VALUE;
						qAnswer.returnType = STATE_SYB_SUM_VALUE;
					}
				} else if ("<=".equals(q.freeSymbol)) {
					// 假如总值不小于等于最大值并且没有其他逻辑错误
					if (!(sum <= Double.parseDouble(q.freeSumNumber))) {
						syb = STATE_SYB_SUM_VALUE;
						qAnswer.returnType = STATE_SYB_SUM_VALUE;
					}
				} else if (">=".equals(q.freeSymbol)) {
					// 假如总值不大于等于最大值并且没有其他逻辑错误
					if (!(sum >= Double.parseDouble(q.freeSumNumber))) {
						syb = STATE_SYB_SUM_VALUE;
						qAnswer.returnType = STATE_SYB_SUM_VALUE;
					}
				}
				if (qAnswer.returnType == STATE_SYB_SUM_VALUE) {
					Message msg = Message.obtain();
					msg.what = qAnswer.returnType;
					msg.obj = getResources().getString(R.string.question_sum, q.freeSymbol + q.freeSumNumber);
					currIndex = 0;
					sum = 0;
					handler.sendMessage(msg);
					return qAnswer.returnType;
				}
			}
			/**** 数字之和结束 **/

			/** 单题拍照验证开始 */
			if (1 == q.qCamera) {
				// System.out.println("验证拍照");
				boolean isSingleCamera = ma.dbService.isSingleCamera(feed.getSurveyId(), feed.getUserId(), feed.getUuid(), q.qIndex + "");
				if (!isSingleCamera) {
					// System.out.println("没有拍照");
					Message msg = Message.obtain();
					qAnswer.returnType = FORMAT_NO_CAMERA;
					msg.what = qAnswer.returnType;
					msg.obj = getResources().getString(R.string.no_picture);
					currIndex = 0;
					sum = 0;
					handler.sendMessage(msg);
					return qAnswer.returnType;
				} else {
					// System.out.println("有拍照");
				}
			}
			/** 单题拍照验证结束 */
			/** 单题签名验证开始 */
			if (1 == q.qSign) {
				// System.out.println("验证拍照");
				boolean isSingleSign = ma.dbService.isSingleSign(feed.getSurveyId(), feed.getUserId(), feed.getUuid(), q.qIndex + "");
				if (!isSingleSign) {
					// System.out.println("没有拍照");
					Message msg = Message.obtain();
					qAnswer.returnType = FORMAT_NO_SIGN;
					msg.what = qAnswer.returnType;
					msg.obj = getResources().getString(R.string.no_sign);
					currIndex = 0;
					sum = 0;
					handler.sendMessage(msg);
					return qAnswer.returnType;
				} else {
					// System.out.println("有拍照");
				}
			}  
		} else {
			qAnswer.returnType = Cnt.STATE_SUCCESS;
		}
		if ((!Util.isEmpty(answerMapArr) || 1 != q.qRequired) && Cnt.STATE_FAIL != qAnswer.returnType) {
			qAnswer.returnType = Cnt.STATE_SUCCESS;
		}

		if (Cnt.STATE_SUCCESS == qAnswer.returnType && !Util.isEmpty(answerMapArr)) {
			qAnswer.setAnswerMapArr(answerMapArr);
			qAnswer.surveyId = feed.getSurveyId();
			qAnswer.userId = feed.getUserId();
			qAnswer.enable = 1;
			qAnswer.answerType = q.qType;
			qAnswer.qIndex = q.qIndex;
			qAnswer.qOrder = q.qOrder;
			// int i = 0;
			if (null != tempAnswer) {
				ma.dbService.updateAnswer(qAnswer);
				// System.out.println("更新答案");
			} else {
				// System.out.println("插入答案");
				ma.dbService.addAnswer(qAnswer);
			}
			// if (0 != i) {
			// Toasts.makeText(getApplicationContext(), "写入成功!",
			// Toast.LENGTH_LONG).show();
			// } else {
			// Toasts.makeText(getApplicationContext(), "写入失败!",
			// Toast.LENGTH_LONG).show();
			// }
		}
		// initView();
		return qAnswer.returnType;
	}

	//数据字典触摸
	private EditText etData;
	private final class DataTouchListener implements OnTouchListener {
		
		private String classId;
		private EditText et;
		
		public DataTouchListener(String classId,EditText et){
			this.classId=classId;
			this.et=et;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				if(!Util.isEmpty(classId)){
					etData=this.et;
					Intent intent=new Intent(NativeModeActivity.this,EditTextListView.class);
					intent.putExtra("classId", classId);
					startActivityForResult(intent, 1);
				}
			}
			return false;
		}
		
	}
	
	private final class OutDayTouchListener implements OnTouchListener {

		private Context c;
		private EditText et;

		private int type;

		public OutDayTouchListener(Context c, EditText et, int dateType) {
			this.c = c;
			this.et = et;
			this.type = dateType;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				// 设置出院日期
				
				// showDateTimePicker(1); 日期样式修改
				Util.showDateTimePicker(c, et, type, screenWidth, screenHeight);
				//  日期控件样式更改  
//				Util.changeDateStyle(c, et, type);
			}
			return false;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isFinishProgress) {
			if (KeyEvent.KEYCODE_BACK == keyCode) {
				if (sdImages.isOpened()) {
					return true;
				}
				// 目录更改
				if (MenuHorizontalScrollView.menuOut == true) {
					this.scrollView.clickMenuBtn();
				} else {
					if (View.VISIBLE != vResult.getVisibility()) {
						if (1 == feed.getIsCompleted()) {
							getQuestionAnswer(MSG_NEXT, false);
							handler.sendEmptyMessage(MSG_WRITE);
						} else {
							isShow = false;
							getQuestionAnswer(MSG_NEXT, false);
							handler.sendEmptyMessage(MSG_SAVE);
						}
					}
				}
				return true;
			}
		} else {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
  
	private final class RecordTask extends AsyncTask<Void, Integer, Boolean> {

		public boolean click;
		private String num;

		public RecordTask(boolean isClick, String number) {
			this.click = isClick;
			this.num = number;
		}

		// 大树动画 添加构造方法 用于切换图片 8
		private ImageView recordIv;

		public RecordTask(boolean click, String num, ImageView recordIv) {
			super();
			this.click = click;
			this.num = num;
			this.recordIv = recordIv;
		}

		// 大树动画 添加构造
		@Override
		protected Boolean doInBackground(Void... params) {
			if (!this.click) {
				/**
				 * 录音
				 */
				String path = "";
				int inner = 0;
				//  注释部分  
//				if (ma.cfg.getBoolean("save_inner", false)) {
//					path = Util.getRecordInnerPath(NativeModeActivity.this, feed.getSurveyId());
//				} else {
//					path = Util.getRecordPath(feed.getSurveyId());
//					inner = 1;
//				}
				/**
				 *   录音地址变更    如果存在 SDCARD 并且  大小有100M 的空间  
				 */
				if (Util.readSDCard()[1]>=0.1) {
					//  存到本地 SDcard 中   
					Log.i(TAG, "剩余空间大小：G"+Util.readSDCard()[1]);
					path=Util.getRecordPath(feed.getSurveyId());
					inner=1; 
				}else {  
					//  存到 系统内置卡  里头  
					path = Util.getRecordInnerPath(NativeModeActivity.this, feed.getSurveyId());
				}
				
				// 增加pid 命名规则
				recordFile = new File(path, // path
						Util.getRecordName(feed.getUserId(), feed.getSurveyId(), Cnt.FILE_TYPE_MP3, feed.getUuid(), null, feed.getPid(), feed.getParametersContent(), (q.qOrder + 1) + ""));
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
					/**
					 * 大树录音 捕获异常 处理
					 */
					ma.uncaughtException(new Thread(Thread.currentThread().getName()), e);
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
				// if (miRecode != null) {
				// miRecode.setIcon(R.drawable.audio_busy);
				// }
				// 大树动画 录音图片切换 9
				if (recordIv != null) {
					recordIv.setImageResource(R.drawable.audio_busy_2);
				}
				Toasts.makeText(NativeModeActivity.this, R.string.record_open, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (this.click) {
				/**
				 * 关闭录音
				 */
				// if (miRecode != null) {
				// miRecode.setIcon(android.R.drawable.ic_btn_speak_now);
				// }
				// 大树动画 录音图片切换 10
				if (recordIv != null) {
					recordIv.setImageResource(R.drawable.ic_btn_speak_now_2);
				}
				Toasts.makeText(NativeModeActivity.this, R.string.record_close, Toast.LENGTH_SHORT).show();
				ma.dbService.updateRecord(recordFile.getName(), System.currentTimeMillis(), recordFile.length());
			}
		}

	};

	/**
	 * 最总写入
	 * 
	 * @param str
	 * @return
	 */
	private Integer writeXML() {
		// str = "";
		int result = 0;
		/**
		 * 0空串 1写入失败 2js有问题 3成功
		 */
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
		hMap.put("lat", feed.getLat());
		hMap.put("lng", feed.getLng());
		hMap.put("VisitAddress", feed.getVisitAddress());
		// 大树拒访 设置状态在这里体现 访问状态
		if (returnTypeId != -2) {
			// 2代表拒绝访问 大树拒访
			feed.setReturnTypeId(returnTypeId);
			hMap.put("VisitSate", returnTypeId + "");
		} else {
			feed.setReturnTypeId(returnTypeId);
			hMap.put("VisitSate", Cnt.VISIT_STATE_COMPLETED + "");
		}
		returnTypeId = -2;
		// 以上部分拒访
		hMap.put("VersionCode", ma.versionCode);
		hMap.put("MacAddress", ma.macAddress);
		hMap.put("MapType", feed.getSurvey().mapType);
		hMap.put("VisitMode", String.valueOf(feed.getVisitMode()));
		hMap.put("RandomOption", groupSequence);

		if (-1 == feed.getSurvey().eligible) {
			// Log.i("_test", "-1 == feed.getSurvey().eligible");
			hMap.put("CheckFormName", "");
		} else {
			Question q = ma.dbService.getQuestion(feed.getSurveyId(), String.valueOf(feed.getSurvey().eligible));
			if (null == q) {
				// Log.i("_test", "null==q");
				hMap.put("CheckFormName", "");
			} else {
				if (Cnt.TYPE_RADIO_BUTTON == q.qType) {
					// Log.i("_test", "Cnt.TYPE_RADIO_BUTTON == q.qType");
					hMap.put("CheckFormName", Util.GetAnswerName(q, null, 0, 0, false));
				} else {
					if (Util.isEmpty(q.getRowItemArr())) {
						// Log.i("_test", "Util.isEmpty(q.getRowItemArr())");
						hMap.put("CheckFormName", "");
					} else {
						// Log.i("_test", "!Util.isEmpty(q.getRowItemArr())");
						StringBuilder sb = new StringBuilder("");
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (i == (q.getRowItemArr().size() - 1)) {
								sb.append(Util.GetAnswerName(q, qi, 0, 0, false));
							} else {
								sb.append(Util.GetAnswerName(q, qi, 0, 0, false)).append(",");
							}
						}
						hMap.put("CheckFormName", sb.toString());
					}
				}
			}
		}

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
		// todo
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
		// 获取所有音视频文件
		ArrayList<UploadFeed> fs = ma.dbService.getRecordList(feed.getUuid(), feed.getSurveyId());
		// feed.setName(name);
		ArrayList<Answer> aswList = ma.dbService.getUserAllAnswer(qAnswer.uuid);
		/** 青岛逻辑次数跳转验证 */
		boolean isLogicList = false;
		if (logicList != null) {
			isLogicList = true;
		}
		hMap.put("logicJumpsFlag", isLogicList + "");
		// 命名规则存附件地址
		boolean b = fm.write2Xml(feed.getPath(), feed.getName(), hMap, aswList, fs, feed.getManyTimes(), feed.getLotsCoord(), feed.getManyPlaces(), true, feed.getSurveyId(), feed.getPid());
		if (b) {
			feed.setIsCompleted(Cnt.VISIT_STATE_COMPLETED);
			result = 2;
		} else {
			feed.setIsCompleted(Cnt.VISIT_STATE_INTERRUPT);
			result = 1;
		}
		File xml = new File(feed.getPath(), feed.getName());
		feed.setSize(xml.length());
		if (Util.isEmpty(aswList)) {
			feed.setIsCompleted(Cnt.VISIT_STATE_NOACCESS);
			result = 2;
		}
		String strDirectory = "";
		for (int directory : dtOrderList) {
			strDirectory += directory + ",";
		}
		// 设置目录
		feed.setDirectory(strDirectory);
		// 大树拒访 设置完成状态 如果拒访 那么 完成 状态 访问状态
		if (feed.getReturnTypeId() != -2) {
			feed.setIsCompleted(Cnt.VISIT_STATE_COMPLETED);
		}
		ma.dbService.updateUploadFeed(feed);
		return result;
	}

	/**
	 * 中间保存
	 * 
	 * @param str
	 * @return
	 */
	private Integer saveXML() {
		// str = "";
		int result = 0;
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
		hMap.put("lat", feed.getLat());
		hMap.put("lng", feed.getLng());
		hMap.put("VisitAddress", feed.getVisitAddress());
		// 大树拒访
		if (returnTypeId !=- 2) {
			// 2代表拒绝访问 大树拒访
			feed.setReturnTypeId(returnTypeId);
			hMap.put("VisitSate", returnTypeId + "");
		} else {
			feed.setReturnTypeId(returnTypeId);
			hMap.put("VisitSate", Cnt.VISIT_STATE_INTERRUPT + "");
		}
		returnTypeId = -2;
		// 大树拒访
		// hMap.put("VisitSate", Cnt.VISIT_STATE_INTERRUPT + "");
		hMap.put("VersionCode", ma.versionCode);
		hMap.put("MacAddress", ma.macAddress);
		hMap.put("MapType", feed.getSurvey().mapType);
		hMap.put("VisitMode", String.valueOf(feed.getVisitMode()));
		hMap.put("RandomOption", groupSequence);

		if (-1 == feed.getSurvey().eligible) {
			// Log.i("_test", "-1 == feed.getSurvey().eligible");
			hMap.put("CheckFormName", "");
		} else {
			Question q = ma.dbService.getQuestion(feed.getSurveyId(), String.valueOf(feed.getSurvey().eligible));
			if (null == q) {
				// Log.i("_test", "null==q");
				hMap.put("CheckFormName", "");
			} else {
				if (Cnt.TYPE_RADIO_BUTTON == q.qType) {
					// Log.i("_test", "Cnt.TYPE_RADIO_BUTTON == q.qType");
					hMap.put("CheckFormName", Util.GetAnswerName(q, null, 0, 0, false));
				} else {
					if (Util.isEmpty(q.getRowItemArr())) {
						// Log.i("_test", "Util.isEmpty(q.getRowItemArr())");
						hMap.put("CheckFormName", "");
					} else {
						// Log.i("_test", "!Util.isEmpty(q.getRowItemArr())");
						StringBuilder sb = new StringBuilder("");
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (i == (q.getRowItemArr().size() - 1)) {
								sb.append(Util.GetAnswerName(q, qi, 0, 0, false));
							} else {
								sb.append(Util.GetAnswerName(q, qi, 0, 0, false)).append(",");
							}
						}
						hMap.put("CheckFormName", sb.toString());
					}
				}
			}
		}

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
		ArrayList<Answer> aswList = ma.dbService.getUserAllAnswer(qAnswer.uuid);
		// 命名规则存附件地址
		boolean b = fm.write2Xml(feed.getPath(), feed.getName(), hMap, aswList, fs, feed.getManyTimes(), feed.getLotsCoord(), feed.getManyPlaces(), true, feed.getSurveyId(), feed.getPid());
		if (b) {
			feed.setIsCompleted(Cnt.VISIT_STATE_INTERRUPT);
			result = 2;
		} else {
			feed.setIsCompleted(Cnt.VISIT_STATE_NOACCESS);
			result = 1;
		}
		File xml = new File(feed.getPath(), feed.getName());
		feed.setSize(xml.length());
		if (Util.isEmpty(aswList)) {
			feed.setIsCompleted(Cnt.VISIT_STATE_NOACCESS);
			result = 2;
		}
		String strDirectory = "";
		for (int directory : dtOrderList) {
			strDirectory += directory + ",";
		}
		// 设置目录
		feed.setDirectory(strDirectory);
		// 大树拒访 设置 拒访 如果拒访 ，那么完成 状态
		if (feed.getReturnTypeId()!=-2) {
			feed.setIsCompleted(Cnt.VISIT_STATE_COMPLETED);
		}
		ma.dbService.updateUploadFeed(feed);
		return result;
	}

	private final class XmlTask extends AsyncTask<Void, Integer, Integer> {
		/**
		 * 是中间保存还是最终保存
		 */
		private int action;
		private String submitCode = "";

		public XmlTask(int saveAction) {
			this.action = saveAction;
		}

		/**
		 * 监控用，重新的构造方法
		 * 
		 * @param saveAction
		 */
		public XmlTask(int saveAction, String submitCode) {
			this.action = saveAction;
			this.submitCode = submitCode;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// show();
			if (isClicked) {
				/**
				 * 关闭录音
				 */
				if (miRecode != null) {
					miRecode.setIcon(android.R.drawable.ic_btn_speak_now);
				}
				Toasts.makeText(NativeModeActivity.this, R.string.record_close, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Integer doInBackground(Void... params) {
			if (null != recordFile && null != mRecorder) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
				isClicked = false;
				// 没更新上
				ma.dbService.updateRecord(recordFile.getName(), System.currentTimeMillis(), recordFile.length());
			}
			/**
			 *   关闭录音 走这里  在这里体现   全局录音   
			 */
			if (null!=MainService.recordFile &&null !=MainService.mRecorder) {
				MainService.mRecorder.stop();
				MainService.mRecorder.release();
				MainService.mRecorder = null;
				isClicked=false;
				ma.dbService.updateRecord(MainService.recordFile.getName(), System.currentTimeMillis(), MainService.recordFile.length());
			}
			//  关闭 录音  
			// 设置组内序列号
			if (!Util.isEmpty(groupSequence)) {
				System.out.println("手动题组序列号:" + groupSequence);
				feed.setGroupSequence(groupSequence);
			}

			// 监控用 有网的情况。告知服务器退出 写xml文件 大树注销监控 2
			 if (NetUtil.checkNet(NativeModeActivity.this)&& mapMonitor) {
			 if ("1".equals(submitCode)) {
			 // 提交退出
			 new MonitorTask(4, index).execute();
			 } else {
			 // 中断退出
			 new MonitorTask(3, realIndex).execute();
			 }
			 }
			// 监控用结束
			if (MSG_WRITE == action) {
				return writeXML();
			} else {
				return saveXML();
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
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
			switch (result) {
			case 0:
			case 1:
				// tvVisitState.setTextColor(Color.RED);
				String str1 = NativeModeActivity.this.getResources().getString(R.string.write_failure);
				SpannableString ss1 = new SpannableString(str1);
				ss1.setSpan(new ForegroundColorSpan(Color.RED), 6, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvVisitState.setText(ss1);
				// 大树拒访 拒绝访问
				if (feed.getReturnTypeId() != -2) {
					tvVisitState.setText(NativeModeActivity.this.getResources().getString(R.string.no_fang_mao,returnName));
				} else {
					tvVisitState.setText(ss1);
				}
				// 大树拒访
				btnLeft.setTextColor(Color.YELLOW);
				btnLeft.setText(NativeModeActivity.this.getResources().getString(R.string.giveup));
				btnRight.setTextColor(Color.GREEN);
				btnRight.setText(NativeModeActivity.this.getResources().getString(R.string.try_again));
				btnLeft.setOnClickListener(new ResultClickListener(1, action));
				btnRight.setOnClickListener(new ResultClickListener(1, action));
				// System.out.println("写入失败");
				break;

			case 2:
				// System.out.println("成功!");
				// finish();
				// tvVisitState.setTextColor(Color.GREEN);

				String str3 = NativeModeActivity.this.getResources().getString(R.string.visited_interrupt);
				/**
				 * 假如是最终保存,则将左边的那个按钮置为“新建”
				 */
				if (MSG_WRITE == action) {
					str3 = NativeModeActivity.this.getResources().getString(R.string.visit_successfully);
					if (1 == feed.getSurvey().openStatus) {
						btnLeft.setVisibility(View.GONE);
					} else {
						btnLeft.setText(NativeModeActivity.this.getResources().getString(R.string.new_panel));
						// 新建限制
						if (1 == feed.getSurvey().oneVisit) {
							btnLeft.setVisibility(View.GONE);
						}
					}
				} else {
					/**
					 * 假如是中间退出,则将左边的那个按钮置为“继续”
					 */
					btnLeft.setText(NativeModeActivity.this.getResources().getString(R.string._continue));
				}

				SpannableString ss3 = new SpannableString(str3);
				ss3.setSpan(new ForegroundColorSpan(Color.BLUE), 6, str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvVisitState.setText(ss3);
				// 大树拒访 12
				if (feed.getReturnTypeId() != -2) {
					tvVisitState.setText(NativeModeActivity.this.getResources().getString(R.string.no_fang_mao,returnName));
				} else {
					tvVisitState.setText(ss3);
				}
				// 大树拒访
				btnLeft.setTextColor(Color.WHITE);
				btnRight.setTextColor(Color.BLACK);
				btnRight.setText(NativeModeActivity.this.getResources().getString(R.string.terminal));
				btnLeft.setOnClickListener(new ResultClickListener(2, action));
				btnRight.setOnClickListener(new ResultClickListener(2, action));
				break;
			}
		}
	}

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_PRE:
				/**
				 * 上一页
				 */
			case MSG_NEXT:
				/**
				 * 下一页
				 */
				createQuestionBodyView(msg.what);
				break;

			case MSG_RECORD:
				/**
				 * 单题录录音
				 */
				new RecordTask(isClicked, String.valueOf(msg.obj)).execute();
				break;
			// 目录自动翻下一页
			case NEXT_READ_TITLE:
				/**
				 * 自动翻页
				 */
				sum = 0;
				setTopClick(false);
				index++;
				int forwardOrder = msg.arg1;
				if (forwardOrder > index) {
					Message myMsg = handler.obtainMessage();
					myMsg.what = NEXT_READ_TITLE;
					myMsg.arg1 = forwardOrder;
					handler.sendMessage(myMsg);
					return;
				} else if (forwardOrder == index) {
					// 下一页重新排序
					recodeOrderList.clear();
					for (int list : dtOrderList) {
						if (forwardOrder > list) {
							recodeOrderList.add(list);
						}
					}
					/*** 题组随机结束 */
					bodyView.removeAllViews();
					// 单复选矩阵固定
					bodyView_new.removeAllViews();
					vs.clear();
					createQuestionBodyView(MSG_NEXT);
				}
				// if(0==totalNum){
				// globleProgress.setVisibility(View.GONE);
				// }
				break;
			// 目录自动翻上一页
			case BACK_READ_TITLE:
				/**
				 * 自动翻页
				 */
				sum = 0;
				setTopClick(false);
				// recodeOrderList.remove(recodeOrderList.size()-1);
				index--;
				int fOrder = msg.arg1;
				if(firstGroupOrder>fOrder){
					clearGroupHand(fOrder);
				}
				if (fOrder < index) {
					Message myMsg = handler.obtainMessage();
					myMsg.what = BACK_READ_TITLE;
					myMsg.arg1 = fOrder;
					handler.sendMessage(myMsg);
					return;
				} else if (fOrder == index) {
					// 上一页重新排序
					recodeOrderList.clear();
					for (int list : dtOrderList) {
						if (fOrder >= list) {
							recodeOrderList.add(list);
						}
					}
					recodeOrderList.add(9999);// 随便加一个，最后也会被去除
					/*** 题组随机结束 */
					bodyView.removeAllViews();
					// 单复选矩阵固定
					bodyView_new.removeAllViews();
					vs.clear();
					createQuestionBodyView(MSG_PRE);
				}
				break;
			case MSG_SAVE:
			case MSG_WRITE:
				// 监控用得到最后确认提交码
				String submitCode = (String) msg.obj;
				if (Util.isEmpty(submitCode)) {
					new XmlTask(msg.what).execute();
				} else {
					new XmlTask(msg.what, submitCode).execute();
				}
				break;
			// 新建受访者
			case MSG_NEW_FEED:
				//是否是题组改动
				boolean isQuestionGroup=false;
//				System.out.println("新建");
				Survey survey = feed.getSurvey();
				final String spwd = survey.getPassword();
				// 输入密码，假如正确新建，否则退出。
				if (!"".equals(survey.getPassword())) {
					// 弹出窗口，正确就能新建。
					final EditText et = new EditText(NativeModeActivity.this);
					new AlertDialog.Builder(NativeModeActivity.this).setTitle(NativeModeActivity.this.getString(R.string.input_pwd)).setIcon(android.R.drawable.ic_dialog_info).setView(et)
							.setPositiveButton(NativeModeActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//改动
									boolean isQuestion=false;
									String pwd = et.getText().toString();
									if ("".equals(pwd.trim())) {
										Toasts.makeText(NativeModeActivity.this, NativeModeActivity.this.getString(R.string.null_reverse_input), Toast.LENGTH_SHORT).show();
										return;
									}
									if (pwd.equals(spwd)) {
										// 目录新建重新赋值开始
										if (!isQgroupOrLogic) {
											recodeOrderList.clear();
											recodeOrderList.add(0);
											dtOrderList.clear();
											dtOrderList.add(0);
										}
										// 目录新建重新赋值结束
										// 有题组随机时候才重新初始化
										if (groupSize > 0) {
											//改动
											isQuestion=true;
											initGroupHand();
										}
										initView();
										index = 0;
										// 监控用(西城BUG)
										recodeTempIndex = -1;
										showVisitView();
										newFeed(System.currentTimeMillis());
										// if (1 == feed.getSurvey().isPhoto)
										// {z1删
										// cameraView(View.VISIBLE);
										// } else {
										// cameraView(View.GONE);
										// if (isHaveSingle) {
										//
										// } else {
										// sdImages.setVisibility(View.INVISIBLE);
										// Log.i("kjy",
										// "sdImages.setVisibility(View.GONE);");
										// }
										// }z1删
										qAnswer.uuid = feed.getUuid();
										tvTitle.setCompoundDrawables(null, null, null, null);
										// System.out.println("qs.size()=" +
										// qs.size());
										//改动
										if (!Util.isEmpty(qs)&&!isQuestion) {
											createQuestionBodyView(MSG_NEXT);
										}
										if (1 == feed.getSurvey().globalRecord) {
											// System.out.println("隐藏录音按钮");
											// recordView(View.GONE);
											new RecordTask(isClicked, null).execute();
										}
										dialog.dismiss();
									} else {
										Toasts.makeText(NativeModeActivity.this, NativeModeActivity.this.getString(R.string.pwd_no), Toast.LENGTH_SHORT).show();
										dialog.dismiss();
									}
								}
							}).setNegativeButton(NativeModeActivity.this.getString(R.string.cancle), null).show();
				} else {
					// 目录新建重新赋值开始
					if (!isQgroupOrLogic) {
						recodeOrderList.clear();
						recodeOrderList.add(0);
						dtOrderList.clear();
						dtOrderList.add(0);
					}
					// 目录新建重新赋值结束
					// 有题组随机时候才重新初始化
					if (groupSize > 0) {
						//改动
						isQuestionGroup=true;
						initGroupHand();
					}
					initView();
					index = 0;
					// 监控用(西城BUG)
					recodeTempIndex = -1;
					showVisitView();
					newFeed(System.currentTimeMillis());
					// if (1 == feed.getSurvey().isPhoto) {z1删除
					// cameraView(View.VISIBLE);
					// } else {
					// cameraView(View.GONE);
					// if (isHaveSingle) {
					//
					// } else {
					// sdImages.setVisibility(View.INVISIBLE);
					// Log.i("kjy", "sdImages.setVisibility(View.GONE);");
					// }
					// }z1删除
					qAnswer.uuid = feed.getUuid();
					tvTitle.setCompoundDrawables(null, null, null, null);
					// System.out.println("qs.size()=" + qs.size());
					//更改
					if (!Util.isEmpty(qs)&&!isQuestionGroup) {
						createQuestionBodyView(MSG_NEXT);
					}
					if (1 == feed.getSurvey().globalRecord) {
						// System.out.println("隐藏录音按钮");
						// recordView(View.GONE);
						new RecordTask(isClicked, null).execute();
					}
				}
				break;

			case MSG_REDIRECT:
				bodyView.removeAllViews();
				// 单复选矩阵固定
				bodyView_new.removeAllViews();
				vs.clear();
				createQuestionBodyView(MSG_NEXT);
				showVisitView();
				// if (1 == feed.getSurvey().isPhoto) {z1删除
				// cameraView(View.VISIBLE);
				// } else {
				// cameraView(View.GONE);
				// if (isHaveSingle) {
				//
				// } else {
				// sdImages.setVisibility(View.INVISIBLE);
				// Log.i("kjy", "sdImages.setVisibility(View.GONE);");
				// }
				// }z1删除
				if (1 == feed.getSurvey().globalRecord) {
					// System.out.println("隐藏录音按钮");
					// recordView(View.GONE);
					new RecordTask(isClicked, null).execute();
				}
				break;

			case STATE_NOTHING:
			case STATE_BOUND_LOWER:
			case STATE_BOUND_UPPER:
			case STATE_BOUND_MATRIX_UPPER:
			case STATE_BOUND_MATRIX_LOWER:
			case STATE_CONTINUOUS:
			case STATE_ROW_LESS:
			case STATE_SYB_MIN:
			case STATE_SYB_MAX:
			case FORMAT_NO_NUMBER:
			case STATE_SYB_REPEAT:
			case STATE_SYB_DATE_EQUAL:
			case STATE_SYB_DATE_UPPER_EQUAL:
			case STATE_SYB_DATE_NO_EQUAL:
			case STATE_SYB_DATE_UPPER:
			case STATE_SYB_DATE_LOW_EQUAL:
			case STATE_SYB_DATE_LOW:
			case FORMAT_NO_FLOAT_NUMBER:
			case STATE_SYB_SUM_VALUE:
			case FORMAT_NO_NUMBER_AND_WORD:
			case FORMAT_NO_EMAIL:
			case FORMAT_NO_CAMERA:// 单题签名
			case ITEM_NO_REQUIRED:
			case FORMAT_NO_SIGN:
			case QUESTION_OUT_YING_RELEVANCE:// 题外关联 之和 字段在这里 大树 外部关联 5
			case QUESTION_INTERIOR_RELEVANCE:// 题外关联 之和 字段 内部关联 大树 内部关联 16
			case DECIMAL_PLACES_NO_MEET: // IMS临时干预价格 小数点位数 为 2
			case QUESTION_ITEM_ORDER_REPEAT: // 大树排序 选项值不能重复
			case LUO_JI_WRONG:               // IMS 逻辑错误   
				final String noMsg = String.valueOf(msg.obj);
				// if (isShow && !Util.isEmpty(noMsg)) {
				// mDialog.show();
				// ((TextView)
				// mDialog.findViewById(R.id.msg_tv)).setText(noMsg);
				// mDialog.findViewById(R.id.ok_btn).setOnClickListener(new
				// OnDialogMissListener());
				// }
				// 干预提示框 样式修改
				if (isShow && !Util.isEmpty(noMsg)) {
					DialogBulder builder = new DialogBulder(NativeModeActivity.this, screenWidth, screenHeight);
					builder.setTitle(NativeModeActivity.this.getString(R.string.intevention_notice));
					builder.setMessage(noMsg);
					builder.setButtons(NativeModeActivity.this.getString(R.string.ok), NativeModeActivity.this.getString(R.string.cancel), new DialogBulder.OnDialogButtonClickListener() {
						public void onDialogButtonClick(Context context, DialogBulder builder, Dialog dialog, int dialogId, int which) {
							if (which == BUTTON_LEFT) {
								Toast.makeText(NativeModeActivity.this, noMsg, Toast.LENGTH_SHORT).show();// 吐司提示
							}
						}
					}, false);
					Dialog dialog = builder.create();
					dialog.show();
				}
				break;

			case MSG_FINISH:
				String _msg = String.valueOf(msg.obj);
				if (isShow && !Util.isEmpty(_msg)) {
					mDialog.show();
					((TextView) mDialog.findViewById(R.id.msg_tv)).setText(_msg);
					mDialog.findViewById(R.id.ok_btn).setOnClickListener(new OnDialogMissListener(MSG_FINISH));
				}
				break;
			}
		}

	};

	/** 新建时候重新初始化题组随机 */
	private void initGroupHand() {
		diffList.clear();
		qDiffList.clear();
		orderMap.clear();
		dialogMap.clear();
		groupSize = 0;
		tempOrderMap.clear();
		choiceNum = -1;
		allGroupOrder.clear();
		handGroupNum = 0;
		handList.clear();
		preDialogMap.clear();
		handGroupSum = 0;
		new OrderTask().execute();
	}
	private void initGroupHands() {
		diffList.clear();
		qDiffList.clear();
		orderMap.clear();
		dialogMap.clear();
		groupSize = 0;
		tempOrderMap.clear();
		choiceNum = -1;
		allGroupOrder.clear();
		handGroupNum = 0;
		handList.clear();
		preDialogMap.clear();
		handGroupSum = 0;
	}
	/**
	 * 题组时点击跳转
	 * @param fOrder
	 */
	private void clearGroupHand(int fOrder) {
		Collections.sort(handList);
		// 包含这个index.说明返回了一层，这种触发重新分配手动循环位置事件
		if (handList.contains(index)) {
			// 先得到原先位置的实体
			choiceNum -= 1;
			handGroupNum -= 1;
			// 通过预选项得到原先这个位置的 暂存组
			TempGroup tempGroup = preDialogMap.get(index + 1);
			// 如果已经有的事件小于总手动随机数目,并且现在choiceNum没值
			if (dialogMap.size() < handGroupSum && -2 == choiceNum) {
				// choiceNum等于 改组集合顺序的个数-2。就为上一组题组随机触发事件的最后位置。
				choiceNum = orderMap.get(tempGroup.getBigOrder()).size() - 2;
				// System.out.println("choiceNum:"+choiceNum);
			} else {
				// 首先得到储存 手动集合位置
				int tempIndexOf = handList.indexOf(index);
				// 移除掉上一组触发事件的 map
				dialogMap.remove(handList.get(tempIndexOf + 1) + 1);
				// 移除上一次点下一题组的位置
				handList.remove(tempIndexOf + 1);
			}
			// 移除这一组保存的数据
			preDialogMap.remove(index + 1);
			// 给这一组赋值
			dialogMap.put(index + 1, tempGroup);
			// System.out.println("dialogMap:"+dialogMap.size());
			// System.out.println(dialogMap.get(index+1).toString());
			System.out.println("外部choiceNum:" + choiceNum);
			// System.out.println("handList:"+handList);
			// System.out.println("preDialogMap:"+preDialogMap.size());

			/**
			 * 重新分配位置 0@3|0@4
			 */
		}
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
					ma.dbService.giveUpFeed(feed.getUuid(), feed.getSurveyId());
					finish();
					break;

				case 2:
					// 成功
					if (MSG_WRITE == a) {
						/**
						 * 此处显示新建
						 */
						handler.sendEmptyMessage(MSG_NEW_FEED);
					} else {
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
					handler.sendEmptyMessage(MSG_REDIRECT);
					break;

				case 2:
					// 成功
					Survey survey = feed.getSurvey();
					final String spwd = survey.getPassword();
					// 输入密码，假如正确新建，否则退出。
					if (!"".equals(survey.getPassword())) {
						// 弹出窗口，正确就能新建。
						final EditText et = new EditText(NativeModeActivity.this);
						new AlertDialog.Builder(NativeModeActivity.this).setTitle(NativeModeActivity.this.getString(R.string.input_pwd)).setIcon(android.R.drawable.ic_dialog_info).setView(et)
								.setPositiveButton(NativeModeActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										String pwd = et.getText().toString();
										if ("".equals(pwd.trim())) {
											Toasts.makeText(NativeModeActivity.this, NativeModeActivity.this.getString(R.string.null_reverse_input), Toast.LENGTH_SHORT).show();
											return;
										}
										if (pwd.equals(spwd)) {
											initGroupHands();
											if(1==feed.getIsCompleted()){
												//加入判断
												new NetTask().execute();
												dialog.dismiss();
											}else{
												dialog.dismiss();
												finish();
											}
											
										} else {
											Toasts.makeText(NativeModeActivity.this, NativeModeActivity.this.getString(R.string.pwd_no), Toast.LENGTH_SHORT).show();
											dialog.dismiss();
										}
									}
								}).setNegativeButton(NativeModeActivity.this.getString(R.string.cancle), null).show();
						
					} else {
						initGroupHands();
						if(1==feed.getIsCompleted()){
							//加入判断
							new NetTask().execute();
						}else{
							finish();
						}
					}
					break;
				}
				break;
			}
		}
	};
	//逻辑处理，判断是否联网，是否有附件，是否登录
	public class NetTask extends AsyncTask<Void, Void, Boolean> {

		
		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean checknet=true;
//			try {
//				checknet = NetService.checknet("http://"+Cnt.RECORD_PHOTO_URL+"/", null, "GET");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			return checknet;
		}
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				try {
					if(!Util.isEmpty(ma.userPwd)&&ma.dbService.checkUploadfeed(feed.getUuid(), feed.getSurveyId())){
						new UpLoadFileTask(feed).execute(ma.userId, MD5.Md5Pwd(ma.userPwd), feed.getSurveyId(), feed.getPath(), feed.getName(), Cnt.UPLOAD_URL);
					}else{
						finish();
					}
				} catch (Exception e) {
					finish();
					e.printStackTrace();
				}
			}else{
				finish();
			}
			super.onPostExecute(result);
		}
		
		
	}
	//自动上传
	public class UpLoadFileTask extends AsyncTask<String, String, HashMap<String, String>> {
		private UploadFeed feed;

		public UpLoadFileTask(UploadFeed f) {
			feed = f;
		}

		protected HashMap<String, String> doInBackground(String... params) {// userId
																			// userPsd
																			// surveyId
																			// path
																			// filename
																			// URL
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("surveyId", params[2]);
			paramsMap.put(Cnt.USER_ID, params[0]);
			paramsMap.put(Cnt.USER_PWD, params[1]);
			FileUpLoad fupLoad = new FileUpLoad();
			InputStream is = fupLoad.upLoadBase64(params[5], params[3], params[4], paramsMap);
			if (is == null) {
				Log.e("kjy", "UpLoadErrorX:" + feed.getName());
				return null;
			} else {
				return resolvData(is, feed);
			}
		}

		private HashMap<String, String> resolvData(InputStream is, UploadFeed feed) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			HashMap<String, String> rMap = new HashMap<String, String>();
			String state = "0";
			String fid = null;
			String rtp = null;
			// System.out.println("resolvData");
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(is);
				Element element = document.getDocumentElement();
				state = element.getElementsByTagName("S").item(0).getFirstChild().getNodeValue();
				rMap.put("S", state);
				// Log.e("DapDesk", "State:" + state);
				System.out.println("解析之后的状态--->" + state);
				if ("100".equals(state)) {
					fid = element.getElementsByTagName("FID").item(0).getFirstChild().getNodeValue();
					rtp = element.getElementsByTagName("RTP").item(0).getFirstChild().getNodeValue();
					// logic.updateFeedStatus(String.valueOf(feed.id), proId);
					// System.out.println("resolvData--->feed.uuid--->"+feed.getUuid());
					/**
					 * XML报完之后将uploaded=1--->uploaded=1,说明XML报过音频和图片没有报
					 */
					// ma.dbService.updateFeedStatusIpsos(feed.getUuid(), 9);
					// xml上传完成 等候上传照片录音
					Log.e("DapDesk", "FID:" + fid + " RTP:" + rtp);
				} else {
					is.close();
					return rMap;
				}
			} catch (Exception e) {
				Log.e("DapDesk", "Message:" + e.getMessage());
			}
			if (fid != null)
				rMap.put("FID", fid);
			if (rtp != null)
				rMap.put("RTP", rtp);
			return rMap;
		}

		protected void onPostExecute(HashMap<String, String> rMap) {
			if (rMap == null) {
				upLoadError(feed.getId(), 0, feed);
				return;
			}
			String stStr = rMap.get("S");
			int state = 0;
			if (stStr != null)
				state = Integer.parseInt(stStr);
			 System.out.println("XML上传后服务器返回的状态码--->"+state);
			if (state == 100) {
				String fid = rMap.get("FID");
				String rtp = rMap.get("RTP");
				upLoadSuccess(feed, fid, rtp);
			} else {
				upLoadError(feed.getId(), state, feed);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		public void upLoadError(long id, int state, UploadFeed feed) {
			if (0 == state) {
			} else if (95 == state) {
				ma.dbService.giveUpFeed(feed.getUuid(), feed.getSurveyId());
			}
			finish();
			// boolean b = ma.dbService.feedAnswerIsHave(feed.getUuid());
			// File file = new File(feed.getPath(), feed.getName());
			/**
			 * 假如是空问卷
			 */
			// if ( !file.exists()) {
			// ma.dbService.updateUploadFeedNoAccessState(feed.getId());
			// }
		}
		
		private void upLoadSuccess(UploadFeed feed, String fid, String rtp) {
			if (null != feed) {
				feed.setFeedId(fid);
				feed.setReturnType(rtp);
				/**
				 * 上传状态设置为9,
				 */
				feed.setIsUploaded(9);
				ma.dbService.updateUploadFeedStatus(feed);
				Toasts.makeText(NativeModeActivity.this, getResources().getString(R.string.finish_surveys), Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	}
	
	
	private final class SeekBarChangeListener implements OnSeekBarChangeListener {
		private TextView tv;
		private String w;

		public SeekBarChangeListener(TextView tvProgress, String word) {
			this.tv = tvProgress;
			this.w = word;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

			if (Util.isEmpty(this.w)) {
				/**
				 * 假如单行文本题，在这里是滚动条体型, 假如它右边没有文字,则直接在其上方显示当前拖动进度
				 */
				this.tv.setText("(" + progress + "/" + seekBar.getMax() + ")");
			} else {
				/**
				 * 假如右边有文字，则其文字后边加上一个括号，然后里面再标出其当前的进度
				 */
				this.tv.setText(this.w + "(" + progress + "/" + seekBar.getMax() + ")");
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

	};

	protected class MyOnKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (KeyEvent.KEYCODE_SEARCH == keyCode || KeyEvent.KEYCODE_BACK == keyCode) {
				return true;
			}
			return false;
		}
	}

	private final class OnDialogMissListener implements OnClickListener {
		private int msg;

		public OnDialogMissListener() {

		}

		public OnDialogMissListener(int _msg) {
			msg = _msg;
		}

		@Override
		public void onClick(View v) {
			mDialog.dismiss();
			if (MSG_FINISH == msg) {
				finish();
			}
		}

	}

	private final class CustomViewFactor implements ViewFactory {

		@Override
		public View makeView() {
			ImageView i = new ImageView(NativeModeActivity.this);
			// i.setBackgroundColor(0xFF000000);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return i;
		}

	}

	private View preView;
	private BitmapDrawable bd = null;

	private final class CustomItemSelectListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			UploadFeed image = (UploadFeed) parent.getItemAtPosition(position);
			if (null != image) {
				if (null != preView) {
					/**
					 * 假如上一次点过图片的花 则将其背景置为白色,即表示不是当前显示的图片
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
					if (1048576*3 > len) { // 显示图片时的压缩 小于1024k, 即1M的
						opts.inSampleSize = 2;
					} else if(1048576*8>len&&len>=1048576*3){
						/**
						 * 大于1M的
						 */
						opts.inSampleSize = 6;
					}
					else {
						opts.inSampleSize = 10;
					}
					Bitmap oldBitmap= BitmapFactory.decodeStream(new FileInputStream(file),null,opts);
					// if (1048576 > len) { // 小于1024k
					// opts.inSampleSize = 3;
					// } else {
					// opts.inSampleSize = 6;
					// }
					// resizeBmp = BitmapFactory.decodeFile(file.getPath(),
					// opts);
					// Bitmap resizeBmp = ;

					bd = new BitmapDrawable(oldBitmap);
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
	private void matrixRadioBt(Boolean isFixed,  ArrayList<String> twoQsiteOptions, ArrayList<AnswerMap> amList ){
		if (isFixed) {

			/**
			 * 标题最大宽度
			 */

			/**
			 * 题型的横向、纵向摆放
			 */
			bodyView.setOrientation(LinearLayout.VERTICAL);
			// }

			/**
			 * 获取行标题, 局部变量移除无碍, 不可直接对q.getRowItemArr()做remove或clear操作,
			 * 所以申请rRows空间暂存q.getRowItemArr()中的值, 其他的做法与此类似
			 */
			ArrayList<QuestionItem> rRows = new ArrayList<QuestionItem>();
			rRows.addAll(q.getRowItemArr());

			/**
			 * 获取列标题
			 */
			ArrayList<QuestionItem> rColmns = new ArrayList<QuestionItem>();
			rColmns.addAll(q.getColItemArr());

			boolean isInclusion = false;

			/**
			 * 排斥
			 */
			if ("1".equals(q.qInclusion)) {
				isInclusion = true;
				// Question _q =
				// ma.dbService.getQuestion(feed.getSurveyId(),
				// q.qSiteOption);
				Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
				ArrayList<AnswerMap> aml = an.getAnswerMapArr();
				ArrayList<Integer> have = new ArrayList<Integer>();
				for (AnswerMap am : aml) {
					if (!Util.isEmpty(am.getAnswerValue())) {
						/**
						 * 假如有值
						 */
						 if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
						 have.add(am.getRow());
						 } else {
						 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
						 }
					}
				}
				aml.clear();
				aml = null;
				// 大树 一下 双引用 单选矩阵
				if (!Util.isEmpty(q.qSiteOption2)) {
					// 大树 输出 把所有要排斥的选项添加到集合hava中
					if (twoQsiteOptions.size() > 0) {
						for (int i = 0; i < twoQsiteOptions.size(); i++) {
							Answer anQsite = ma.dbService.getAnswer(feed.getUuid(), twoQsiteOptions.get(i));
							if (anQsite != null && anQsite.getAnswerMapArr().size() > 0) {
								for (AnswerMap am : anQsite.getAnswerMapArr()) {
									/**
									 * 假如有值 自动查重 去除重复
									 */
									if (!Util.isEmpty(am.getAnswerValue())) {
										if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
											have.add(am.getRow());
										} else {
											have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
										}
									}
								}
							}
						}
					}
				}
				twoQsiteOptions.add(0, q.qSiteOption.trim());
				// 大树 以上部分 单选矩阵
				for (int i = 0; i < q.getRowItemArr().size(); i++) {
					QuestionItem qi = q.getRowItemArr().get(i);
					if (-1 != have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
						/**
						 * 选择了这些选项
						 */
						rRows.remove(qi);
					}
				}
				have.clear();
				have = null;
				mTempRows.addAll(rRows);
			} else if ("0".equals(q.qInclusion)) {
				/**
				 * 引用
				 */
				isInclusion = true;
				// Question _q =
				// ma.dbService.getQuestion(feed.getSurveyId(),
				// q.qSiteOption);
				Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
				if (an != null) {
					ArrayList<AnswerMap> aml = an.getAnswerMapArr();
					ArrayList<Integer> have = new ArrayList<Integer>();
					for (AnswerMap am : aml) {
						/**
						 * 假如有值
						 */
						if (!Util.isEmpty(am.getAnswerValue())) {
							if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
								have.add(am.getRow());
							} else {
								have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
							}
						}
					}
					aml.clear();
					aml = null;

					// 大树 添加 双引用在里实现 以下： 双引用 单选矩阵
					if (!Util.isEmpty(q.qSiteOption2)) {
						// 大树 输出 自动查重 的功能
						if (twoQsiteOptions.size() > 0) {
							for (int i = 0; i < twoQsiteOptions.size(); i++) {

								Answer anQsite = ma.dbService.getAnswer(feed.getUuid(), twoQsiteOptions.get(i));
								if (anQsite != null && anQsite.getAnswerMapArr().size() > 0) {
									for (AnswerMap am : anQsite.getAnswerMapArr()) {
										/**
										 * 假如有值 自动查重 去除重复
										 */
										if (!Util.isEmpty(am.getAnswerValue())) {
											 if (Cnt.TYPE_RADIO_BUTTON ==
											 an.answerType) {
											 have.add(am.getRow());
											 } else {
											 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
											 }
										}
									}
								}
							}
						}

					}
					twoQsiteOptions.add(0, q.qSiteOption.trim());
					// 单选矩阵 大树 双引用 的显示
					for (int i = 0; i < q.getRowItemArr().size(); i++) {
						QuestionItem qi = q.getRowItemArr().get(i);
						if (-1 == have.indexOf(qi.itemValue)&& 1 != qi.itemShow) {
							/**
							 * 没有选择的, 则移走
							 */
							rRows.remove(qi);
						}
					}
					have.clear();
					have = null;
					mTempRows.addAll(rRows);
				} else {
					rRows.retainAll(rRows);
					mTempRows.addAll(rRows);
				}
			}

			// 大树 添加 垂直结构的 实现 提示语 水平结构 提示语 都在这里 引用排斥 双引用 单选矩阵 的处理 修改一下
			if (!Util.isEmpty(q.qSiteOption2) || !Util.isEmpty(q.qSiteOption)) {
				twoSiteNoticeTv.setTextColor(Color.RED);
				twoSiteNoticeTv.setTextSize(lowSurveySize);
				StringBuilder sb1 = new StringBuilder();
				if (twoQsiteOptions.size() > 0) {
					for (int i = 0; i < twoQsiteOptions.size(); i++) {
						Log.i("zrl1", twoQsiteOptions.get(i) + "双引用题目编号：");
						Question q = ma.dbService.getQuestion(feed.getSurveyId(), twoQsiteOptions.get(i));
						if (null != q) {
							if (!Util.isEmpty(q.qOrder + "")) {
								if (!Util.isEmpty(q.qid)) {
									sb1.append(q.qid + ",");
								} else {
									sb1.append("Q" + q.qOrder + ",");
								}
							} else
								Log.i("zrl1", "qid为空");
						}
					}
				}
				if (q.qInclusion.equals("0") && -1 != sb1.toString().indexOf(",")) {
					twoSiteNoticeTv.setText(NativeModeActivity.this.getString(R.string.each_answer_associated) + sb1.substring(0, sb1.toString().lastIndexOf(",")).toString()
							+ NativeModeActivity.this.getString(R.string.answer_value_reference));
				} else if (q.qInclusion.equals("1") && -1 != sb1.toString().indexOf(",")) {
					twoSiteNoticeTv.setText(NativeModeActivity.this.getString(R.string.each_answer_associated) + sb1.substring(0, sb1.toString().lastIndexOf(",")).toString()
							+ NativeModeActivity.this.getString(R.string.answer_value_rejection));
				}
			}

			// 大树 以上部分
			/**
			 * 题外关联 之 选项置顶 设置 标示 置顶 选项 置底 isItemBottom
			 * 
			 * 获取 所有选项置顶的 选项 集合 还有别忘啦 提示语的 显示 出来
			 */
			boolean isItemStick = ComUtil.isItemStick(rRows)[0];
			int[] itemList = ComUtil.getItemStick(rRows);// 置顶标示位置 数组
			int counter = 0; // 计数器
			int botValue = 0;// 置底 临时 位置 标示 底部
			int itemCount = rRows.size();// 所有的 选项 数量

			/**
			 * 假如是随即题目 则将其行和列都随机一遍
			 */
			if (1 == q.qRadomed) {
				/**
				 * 行随机暂存行中的数据
				 */
				ArrayList<QuestionItem> _tmpRows = new ArrayList<QuestionItem>();
				Random rd = new Random();
				int size = 0;
				if (isInclusion) {
					size = rRows.size();
				} else {
					size = q.getRowItemArr().size();
				}
				for (int i = 0; i < size; i++) {
					/**
					 * 随机产生数组的下标值
					 */
					int index = rd.nextInt(rRows.size());

					/**
					 * 题外关联 之 选项置顶 单选矩阵 的实现 选项置底 等操作的实现 过程
					 */
					QuestionItem item = rRows.get(index);
					if (null != item.padding && counter == 0 && item.padding != 1) {
						if (isItemStick) {
							index = itemList[0];
							item = rRows.get(index);
						}
					} else if (null != item.padding && counter != itemCount - 1 && item.padding == 2) {
						botValue = index;
						while (true) {
							index = rd.nextInt(rRows.size());
							if (index != botValue) {
								item = rRows.get(index);
								break;
							}
						}
					}
					counter++;

					_tmpRows.add(item);
					/**
					 * 取得随机产生的选项对象item
					 */

					// _tmpRows.add(rRows.get(index)); 注释 语句 这条
					/**
					 * 在暂存数组中移除随机产生的选项对象item
					 */
					rRows.remove(index);
				}

				rRows.addAll(_tmpRows);
				_tmpRows.clear();

				/**
				 * 列随机暂存列中的数据
				 */
				// ArrayList<QuestionItem> tmpCols = new
				// ArrayList<QuestionItem>();
				// for (int i = 0; i < q.getColItemArr().size(); i++) {
				// /**
				// * 随机产生数组的下标值
				// */
				// int index = rd.nextInt(rColmns.size());
				// /**
				// * 取得随机产生的选项对象item
				// */
				// tmpCols.add(rColmns.get(index));
				// /**
				// * 在暂存数组中移除随机产生的选项对象item
				// */
				// rColmns.remove(index);
				// }
				// rColmns.addAll(tmpCols);
				// tmpCols.clear();
			}

			/**
			 * 假如题干的宽度大于或等于屏幕宽度的3/4
			 */
			boolean isBeyond = ((screenWidth * 2 / 3 - 20)) <= rColmns.size() * 100;

			// 处理非随机矩阵
			/**
			 * 遍历每一行
			 */
			for (int r = 0; r < rRows.size() + 1; r++) {
				final ArrayList<View> rowViews=new ArrayList<View>();
				QuestionItem rowItem = null;
				if (0 != r) {
					// System.out.println("row=" + rRows.get(r -
					// 1).itemText);
					rowItem = rRows.get(r - 1);
					/**
					 * 题外关联 之 选项置顶 单选矩阵 把 itemValue 给注释掉 即可
					 */
					// rowItem.itemValue = r - 1;
				}

				/**
				 * 遍历每一列
				 */
				RadioGroup rg = new RadioGroup(NativeModeActivity.this);// 每一列的
				rg.setVerticalGravity(Gravity.CENTER_VERTICAL);
				rg.setBackgroundColor(Color.TRANSPARENT);
				if (q.qStarCheck == 1) {
					rg.setBackgroundResource(R.drawable.piont_lv);
				}
				RadioGroup.LayoutParams rgPar = new RadioGroup.LayoutParams((screenWidth * 2 / 3 - 20),
						LayoutParams.WRAP_CONTENT);
				rgPar.setMargins(0, 0, 0, 0);
				rg.setLayoutParams(rgPar);
				rg.setPadding(0, 0, 0, 0);
				rg.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout ll = new LinearLayout(NativeModeActivity.this);
				ll.setVerticalGravity(Gravity.CENTER_VERTICAL);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setLayoutParams(FILL_WRAP);

				// 单复选矩阵固定
				RadioGroup ll_new = new RadioGroup(NativeModeActivity.this);
				ll_new.setGravity(Gravity.CENTER_VERTICAL);

				//新加的
				int rightNum=(q.isRight==1)?1:0;
				
				for (int c = 0; c < rColmns.size() + 1+rightNum; c++) {
					QuestionItem colItem = null;
					//新加的
					if (0 != c) {
						if(rightNum==1&&rColmns.size()+rightNum==c){
							
						}else{
							colItem = rColmns.get(c - 1);
						}
					}

					ll.setOrientation(LinearLayout.HORIZONTAL);
					ll.setLayoutParams(FILL_WRAP);

					// 单复选矩阵固定
					ll_new.setOrientation(LinearLayout.HORIZONTAL);
					ll_new.setLayoutParams(FILL_WRAP);

					TextView tvTb = new TextView(NativeModeActivity.this);
					tvTb.setLayoutParams(WRAP_WRAP);
					tvTb.setGravity(Gravity.FILL);
					tvTb.setTextSize(lowSurveySize);
					tvTb.setWidth(100);
					tvTb.setPadding(2, 2, 2, 2);
					if (0 == r) {// 如过是第一行, 则打印出每一列的值
						// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
						// 单复选矩阵固定
						ll_new.setBackgroundColor(Color.LTGRAY);// Color.LTGRAY
						if (0 == c) {// 打印表头
							tvTb.setText(" ");
							//矩阵右侧
							if(q.isRight==1){
								tvTb.setWidth(screenWidth / 6);
							}else{
								tvTb.setWidth(screenWidth / 3);
							}
						} //矩阵右侧 新加的
						else if(rColmns.size()+rightNum==c&&q.isRight==1){
							tvTb.setText(" ");
							tvTb.setWidth(screenWidth / 6);
						}
						else {// c !=0 打印每列的,即列标题
							
							// System.out.println("c_colmns_item_value=" +
							// rColmns.get(c - 1).itemValue + ", colmns=" +
							// (c -
							// 1));
							tvTb.setTextColor(Color.BLACK);
							if (isBeyond) {
								/**
								 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
								 */
								// 矩阵百分比
								tvTb.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							} else {
								tvTb.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							}
							String t = colItem.itemText;
							// ***********************************样式处理**************************//
							CstmMatcher cm = Util.findFontMatcherList(t);
							if (!Util.isEmpty(cm.getMis())) {
								t = cm.getResultStr();
								SpannableString ss = new SpannableString(t);
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length())
										ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
								CstmMatcher bCm = Util.findBoldMatcherList(t);
								if (!Util.isEmpty(bCm.getMis())) {
									for (MatcherItem mi : bCm.getMis()) {
										if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
											ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										}
									}
								}
								tvTb.setText(ss);
							} else {
								CstmMatcher bCm = Util.findBoldMatcherList(t);
								if (!Util.isEmpty(bCm.getMis())) {
									t = bCm.getResultStr();
									SpannableString ss = new SpannableString(t);
									for (MatcherItem mi : bCm.getMis()) {
										if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
											ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
													Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end,
													Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										}
									}
									tvTb.setText(ss);
								} else {
									tvTb.setText(t);
								}

							}
							if (q.qStarCheck != 0 && c != 1 && c != rColmns.size()) {
								tvTb.setText("");
							}
						}
						// ***********************************样式处理**************************//
						CstmMatcher cm = Util.findImageMatherList(tvTb.getText().toString());
						if (0 < cm.getMis().size()) {
							LinearLayout _im = new LinearLayout(NativeModeActivity.this);
							_im.setOrientation(LinearLayout.VERTICAL);
							_im.setGravity(Gravity.FILL);
							_im.setLayoutParams(
									new LayoutParams(isBeyond ? 100 : (screenWidth * 2 / 3 - 20) / rColmns.size(),
											LayoutParams.WRAP_CONTENT));
							tvTb.setText(cm.getResultStr());
							_im.addView(tvTb, _im.getChildCount());
							for (int i = 0; i < cm.getMis().size(); i++) {
								MatcherItem mi = cm.getMis().get(i);
								ImageView iv = new ImageView(NativeModeActivity.this);
								iv.setPadding(2, 2, 2, 2);
								String path = Util.getImagePath(NativeModeActivity.this, feed.getSurveyId(), mi.name);
								// 图片百分比开始
								// iv.setLayoutParams(WRAP_WRAP);
								Bitmap image = BitmapFactory.decodeFile(path);
								int tWidth = image.getWidth();
								int tHeight = image.getHeight();
								iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
								// 图片百分比结束
								iv.setImageURI(Uri.parse(path));
								_im.addView(iv, _im.getChildCount());
								iv.setOnLongClickListener(new ImageLongClickListener(path));
							}
							// 单复选矩阵固定
							ll_new.addView(_im, ll_new.getChildCount());
						} else {
							// 单复选矩阵固定
							ll_new.addView(tvTb, ll_new.getChildCount());
						}

					} else {// r != 0
						// ll.setBackgroundColor(Color.YELLOW);
					
						if (realRows % 2 == 0) {
							ll.setBackgroundColor(Color.parseColor("#F0F0F0"));
						} else {
							ll.setBackgroundColor(Color.TRANSPARENT);
						}
						if (0 == c) {
							/**
							 * 打印行标题
							 */
							tvTb.setBackgroundResource(R.drawable.small_text_background);
							tvTb.setTextColor(Color.BLACK);
							// 矩阵右侧
							if (1 == q.isRight) {
								tvTb.setWidth(screenWidth / 6);
							} else {
								tvTb.setWidth(screenWidth / 3);
							}
							//tvTb.setBackgroundColor(Color.TRANSPARENT);
							String t = rowItem.itemText;

							/**
							 * 题外关联 之 选项置顶 单选矩阵 的提示语的 显示 出来
							 */
							if (null != rowItem.padding) {
								if (rowItem.padding == 1) {
									t = rowItem.itemText + "<font color=red>  " + this.getString(R.string.option_top) + "</font>";
								} else if (rowItem.padding == 2) {
									t = rowItem.itemText + "<font color=red>  " + this.getString(R.string.option_bottom) + "</font>";
								}
							}
							// ***********************************样式处理**************************//
                            
							if (!Util.isEmpty(t)) {
								CstmMatcher cm = Util.findFontMatcherList(t);
								if (!Util.isEmpty(cm.getMis())) {
									t = cm.getResultStr();
									if (!Util.isEmpty(t)) {
										SpannableString ss = new SpannableString(t);
										for (MatcherItem mi : cm.getMis()) {
											ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										}
										CstmMatcher bCm = Util.findBoldMatcherList(t);
										if (!Util.isEmpty(bCm.getMis())) {
											for (MatcherItem mi : bCm.getMis()) {
												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
												}
											}
										}
										tvTb.setText(ss);
									} else {
										tvTb.setText(rowItem.itemText);
									}
								} else {
									CstmMatcher bCm = Util.findBoldMatcherList(t);
									if (!Util.isEmpty(bCm.getMis())) {
										t = bCm.getResultStr();
										if (!Util.isEmpty(t)) {
											SpannableString ss = new SpannableString(t);
											for (MatcherItem mi : bCm.getMis()) {
												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
												}
											}
											tvTb.setText(ss);
										} else {
											tvTb.setText(rowItem.itemText);
										}
									} else {
										tvTb.setText(t);
									}

								}

							}
							// ******** 图片 加载 ******************
							CstmMatcher cm = Util.findImageMatherList(tvTb.getText().toString());
							if (0 < cm.getMis().size()) {
								LinearLayout _im = new LinearLayout(NativeModeActivity.this);
								_im.setOrientation(LinearLayout.VERTICAL);
								_im.setGravity(Gravity.FILL);
								_im.setLayoutParams(new LayoutParams(screenWidth / 3, LayoutParams.WRAP_CONTENT));
								// _im.setLayoutParams(new
								// LayoutParams(isBeyond
								// ? 100 : (screenWidth * 2 / 3 - 20) /
								// rColmns.size(),
								// LayoutParams.WRAP_CONTENT));
								tvTb.setText(cm.getResultStr());
								_im.addView(tvTb, _im.getChildCount());
								for (int i = 0; i < cm.getMis().size(); i++) {
									MatcherItem mi = cm.getMis().get(i);
									ImageView iv = new ImageView(NativeModeActivity.this);
									iv.setPadding(2, 2, 2, 2);
									String path = Util.getImagePath(NativeModeActivity.this, feed.getSurveyId(), mi.name);
									// 图片百分比开始
									// iv.setLayoutParams(WRAP_WRAP);
									Bitmap image = BitmapFactory.decodeFile(path);
									int tWidth = image.getWidth();
									int tHeight = image.getHeight();
									iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
									// 图片百分比结束
									iv.setImageURI(Uri.parse(path));
									_im.addView(iv, _im.getChildCount());
									iv.setOnLongClickListener(new ImageLongClickListener(path));
								}
								ll.addView(_im, ll.getChildCount());
							} else {
								ll.addView(tvTb, ll.getChildCount());
							}

							// ***********************************样式处理**************************//

						} //矩阵右侧 新加的
						else if(rColmns.size()+rightNum==c&&q.isRight==1){
							/**
							 * 打印行标题
							 */
							tvTb.setBackgroundResource(R.drawable.small_text_background);
							tvTb.setTextColor(Color.BLACK);
							//矩阵右侧
							tvTb.setWidth(screenWidth / 6);
							
							//tvTb.setBackgroundColor(Color.TRANSPARENT);
							String t = rowItem.itemTextRight;
							// ***********************************样式处理**************************//

//							if (!Util.isEmpty(t)) {
//								CstmMatcher cm = Util.findFontMatcherList(t);
//								if (!Util.isEmpty(cm.getMis())) {
//									t = cm.getResultStr();
//									if (!Util.isEmpty(t)) {
//										SpannableString ss = new SpannableString(t);
//										for (MatcherItem mi : cm.getMis()) {
//											ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//										}
//										CstmMatcher bCm = Util.findBoldMatcherList(t);
//										if (!Util.isEmpty(bCm.getMis())) {
//											for (MatcherItem mi : bCm.getMis()) {
//												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
//													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//												}
//											}
//										}
//										tvTb.setText(ss);
//									} else {
//										tvTb.setText(rowItem.itemText);
//									}
//									//
//								} else {
//									CstmMatcher bCm = Util.findBoldMatcherList(t);
//									if (!Util.isEmpty(bCm.getMis())) {
//										t = bCm.getResultStr();
//										if (!Util.isEmpty(t)) {
//											SpannableString ss = new SpannableString(t);
//											for (MatcherItem mi : bCm.getMis()) {
//												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
//													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//												}
//											}
//											tvTb.setText(ss);
//										} else {
//											tvTb.setText(rowItem.itemText);
//										}
//									} else {
//										tvTb.setText(t);
//									}
//
//								}
//
//							}
							//  样式修改      以上注释  
							if (!Util.isEmpty(t)) {
//								t=t.replace("color=Khaki", "color=#FDF8BF"); 
								tvTb.setText(Html.fromHtml(t));  
							}
							// ******** 图片 加载 ******************
							CstmMatcher cm = Util.findImageMatherList(tvTb.getText().toString());
							if (0 < cm.getMis().size()) {
								LinearLayout _im = new LinearLayout(NativeModeActivity.this);
								_im.setOrientation(LinearLayout.VERTICAL);
								_im.setGravity(Gravity.FILL);
								_im.setLayoutParams(new LayoutParams(screenWidth / 3, LayoutParams.WRAP_CONTENT));
								// _im.setLayoutParams(new
								// LayoutParams(isBeyond
								// ? 100 : (screenWidth * 2 / 3 - 20) /
								// rColmns.size(),
								// LayoutParams.WRAP_CONTENT));
								tvTb.setText(cm.getResultStr());
								_im.addView(tvTb, _im.getChildCount());
								for (int i = 0; i < cm.getMis().size(); i++) {
									MatcherItem mi = cm.getMis().get(i);
									ImageView iv = new ImageView(NativeModeActivity.this);
									iv.setPadding(2, 2, 2, 2);
									String path = Util.getImagePath(NativeModeActivity.this, feed.getSurveyId(), mi.name);
									// 图片百分比开始
									// iv.setLayoutParams(WRAP_WRAP);
									Bitmap image = BitmapFactory.decodeFile(path);
									int tWidth = image.getWidth();
									int tHeight = image.getHeight();
									iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
									// 图片百分比结束
									iv.setImageURI(Uri.parse(path));
									_im.addView(iv, _im.getChildCount());
									iv.setOnLongClickListener(new ImageLongClickListener(path));
								}
								ll.addView(_im, ll.getChildCount());
							} else {
								ll.addView(tvTb, ll.getChildCount());
							}
						}
						else {// 打印单选按钮
							if(1==c){
								ll.addView(rg);
							}
							// QuestionItem item = rRows.get(r - 1);
							if (null == rowItem || (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
								// 空其他项,即只有一个<freeInput/>标签
								continue;
							}
							// System.out.println("r_row_item_value=" +
							// rowItem.itemValue + ", r=" + (r - 1));
							RadioButton radio = new RadioButton(NativeModeActivity.this);
							radio.setLayoutParams(WRAP_WRAP);
							radio.setGravity(Gravity.FILL | Gravity.CENTER_VERTICAL);
							// radio.setGravity(Gravity.CENTER);

							AnswerMap am = new AnswerMap();
							String name = Util.GetAnswerName(q, rowItem, rowItem.itemValue, 0, false);
							am.setAnswerName(name);
							/**
							 * 相对的
							 */
							am.setRow(rowItem.itemValue);
							/**
							 * 绝对的
							 */
							am.setCol(colItem.itemValue);
							// radio.setOnClickListener(new On(rowItem,
							// colItem));
							// System.out.println("单选矩阵随机--->Key(" +
							// rowItem.itemText + "," + colItem.itemText +
							// ")====>Value(" + rowItem.itemValue + ", " +
							// colItem.itemValue + ")");
							am.setAnswerValue(String.valueOf(colItem.itemValue));
							radio.setTag(am);
							// radio.setTag(key, tag)
							rg.addView(radio, rg.getChildCount());
							if (!Util.isEmpty(amList)) {
								for (AnswerMap tam : amList) {
									if (name.equals(tam.getAnswerName()) && am.getAnswerValue().equals(tam.getAnswerValue())) {
										// System.out.println("匹配--->name="+name+", value="+am.getAnswerValue());
										radio.setChecked(true);
									}
								}
							}
							radio.setPadding(2, 2, 2, 2);
							radio.setTextSize(lowSurveySize);
							radio.setTextColor(Color.BLACK);
							// radio.setButtonDrawable(R.drawable.small_radiobutton_temp);
							radio.setButtonDrawable(R.drawable.small_radiobutton);
							radio.setBackgroundResource(R.drawable.small_radiobutton_background);
							if (q.qStarCheck != 0) {
								int drawable = R.drawable.small_radiobutton;
								radio.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(drawable),
										null, null, null);
								radio.setButtonDrawable(android.R.color.transparent);
								radio.setBackgroundResource(R.drawable.small_text_background);
								if (realRows % 2 == 0) {
									radio.setBackgroundColor(Color.WHITE);
								} else {
									radio.setBackgroundColor(Color.parseColor("#F0F0F0"));
							}
								switch (q.qStarCheck) {
								case 1:
									drawable = android.R.color.transparent;
									TextView tvLine = new TextView(NativeModeActivity.this);
									if (realRows % 2 == 0) {
										tvLine.setBackgroundColor(Color.parseColor("#F0F0F0"));
										radio.setBackgroundColor(Color.WHITE);
									} else {
										tvLine.setBackgroundColor(Color.WHITE);
										radio.setBackgroundColor(Color.parseColor("#F0F0F0"));
									}
									tvLine.setLayoutParams(new LayoutParams(3, LayoutParams.MATCH_PARENT));
									rg.addView(tvLine);
									break;
								case 2:
									drawable=R.drawable.star_24_off;
									break;
								case 3:
									drawable=R.drawable.hand_24_off;
									break;
								case 4:
									drawable=R.drawable.heart_24_off;
									break;
								}
								radio.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(drawable), null, null, null);
								rowViews.add(radio);
							}
							radio.setWidth(100);
							if (isBeyond) {
								/**
								 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
								 */
								// 矩阵百分比
								radio.setLayoutParams(new LinearLayout.LayoutParams((screenWidth * 2 / 3 - 20) / rColmns.size(), LinearLayout.LayoutParams.WRAP_CONTENT));
								radio.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							} else {
								// System.out.println("设置了Radio的宽度");
								radio.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							}
							// radio.setHeight(100);
							vs.add(radio);
							final String t = colItem.itemText;
						}

					}

				}
				
				if (0 != q.qStarCheck) {
					for (int i = 0; i < rowViews.size(); i++) {
						RadioButton rb = (RadioButton) rowViews.get(i);
						if (rb.isChecked()) {
							for (int j = 0; j < rowViews.size(); j++) {
								int drawable = R.drawable.small_radiobutton;
								switch (q.qStarCheck) {
								case 1:
									drawable = android.R.color.transparent;
									TextView tvLine = new TextView(NativeModeActivity.this);
									if (realRows % 2 == 0) {
										tvLine.setBackgroundColor(Color.parseColor("#F0F0F0"));
										((RadioButton) rowViews.get(j)).setBackgroundColor(Color.WHITE);
									} else {
										tvLine.setBackgroundColor(Color.WHITE);
										((RadioButton) rowViews.get(j)).setBackgroundColor(Color.parseColor("#F0F0F0"));
									}
									tvLine.setLayoutParams(new LayoutParams(3, LayoutParams.MATCH_PARENT));
									rg.addView(tvLine);
									break;
								case 2:
									drawable = R.drawable.star_24_off;
									break;
								case 3:
									drawable = R.drawable.hand_24_off;
									break;
								case 4:
									drawable = R.drawable.heart_24_off;
									break;
								}
								((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
										getResources().getDrawable(drawable), null, null, null);
							}
							for (int j = 0; j < i + 1; j++) {
								int drawable = R.drawable.small_radiobutton;
								switch (q.qStarCheck) {
								case 1:
									((RadioButton) rowViews.get(j)).setBackgroundResource(R.color.transparent);
									drawable = android.R.color.transparent;
									break;
								case 2:
									drawable = R.drawable.star_24;
									break;
								case 3:
									drawable = R.drawable.hand_24;
									break;
								case 4:
									drawable = R.drawable.heart_24;
									break;
								}
								((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
										getResources().getDrawable(drawable), null, null, null);
							}
						}
						if (realRows % 2 == 0) {
							rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									if (isChecked) {
										for (int j = 0; j < rowViews.size(); j++) {
											int drawable = R.drawable.small_radiobutton;
											switch (q.qStarCheck) {
											case 1:
												drawable = android.R.color.transparent;
												((RadioButton) rowViews.get(j)).setBackgroundColor(Color.WHITE);
												break;
											case 2:
												drawable = R.drawable.star_24_off;
												break;
											case 3:
												drawable = R.drawable.hand_24_off;
												break;
											case 4:
												drawable = R.drawable.heart_24_off;
												break;
											}
											((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
													getResources().getDrawable(drawable), null, null, null);
										}
										for (int j = 0; j < rowViews.indexOf(buttonView) + 1; j++) {
											int drawable = R.drawable.small_radiobutton;
											switch (q.qStarCheck) {
											case 1:
												((RadioButton) rowViews.get(j))
														.setBackgroundResource(R.color.transparent);
												drawable = android.R.color.transparent;
												break;
											case 2:
												drawable = R.drawable.star_24;
												break;
											case 3:
												drawable = R.drawable.hand_24;
												break;
											case 4:
												drawable = R.drawable.heart_24;
												break;
											}
											((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
													getResources().getDrawable(drawable), null, null, null);
										}
									}

								}
							});
						} else {
							rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									if (isChecked) {
										for (int j = 0; j < rowViews.size(); j++) {
											int drawable = R.drawable.small_radiobutton;
											switch (q.qStarCheck) {
											case 1:
												drawable = android.R.color.transparent;
												((RadioButton) rowViews.get(j))
														.setBackgroundColor(Color.parseColor("#F0F0F0"));
												break;
											case 2:
												drawable = R.drawable.star_24_off;
												break;
											case 3:
												drawable = R.drawable.hand_24_off;
												break;
											case 4:
												drawable = R.drawable.heart_24_off;
												break;
											}
											((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
													getResources().getDrawable(drawable), null, null, null);
										}
										for (int j = 0; j < rowViews.indexOf(buttonView) + 1; j++) {
											int drawable = R.drawable.small_radiobutton;
											switch (q.qStarCheck) {
											case 1:
												((RadioButton) rowViews.get(j))
														.setBackgroundResource(R.color.transparent);
												drawable = android.R.color.transparent;
												break;
											case 2:
												drawable = R.drawable.star_24;
												break;
											case 3:
												drawable = R.drawable.hand_24;
												break;
											case 4:
												drawable = R.drawable.heart_24;
												break;
											}
											((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
													getResources().getDrawable(drawable), null, null, null);
										}
									}

								}
							});
						}
					}
				}
				Log.i("@@@", "realRows="+realRows);
				realRows++;
				if (null != rowItem) {// 隐藏选项
					// 条件隐藏选项
					setHideRb(rowItem, ll);
					if (1 == rowItem.hide) {
						ll.setVisibility(View.GONE);
					}
				}
				bodyView.addView(ll, bodyView.getChildCount());
				bodyView_new.addView(ll_new, bodyView_new.getChildCount());
			}
		} else {

			/**
			 * 标题最大宽度
			 */

			/**
			 * 题型的横向、纵向摆放
			 */
			bodyView.setOrientation(LinearLayout.VERTICAL);
			// }

			/**
			 * 获取行标题, 局部变量移除无碍, 不可直接对q.getRowItemArr()做remove或clear操作,
			 * 所以申请rRows空间暂存q.getRowItemArr()中的值, 其他的做法与此类似
			 */
			ArrayList<QuestionItem> rRows = new ArrayList<QuestionItem>();
			rRows.addAll(q.getRowItemArr());

			/**
			 * 获取列标题
			 */
			ArrayList<QuestionItem> rColmns = new ArrayList<QuestionItem>();
			rColmns.addAll(q.getColItemArr());

			boolean isInclusion = false;

			/**
			 * 排斥
			 */
			if ("1".equals(q.qInclusion)) {
				isInclusion = true;
				// Question _q =
				// ma.dbService.getQuestion(feed.getSurveyId(),
				// q.qSiteOption);
				Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
				ArrayList<AnswerMap> aml = an.getAnswerMapArr();
				ArrayList<Integer> have = new ArrayList<Integer>();
				for (AnswerMap am : aml) {
					if (!Util.isEmpty(am.getAnswerValue())) {
						/**
						 * 假如有值
						 */
						if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
							have.add(am.getRow());
						} else {
							have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
						}
					}
				}
				aml.clear();
				aml = null;
				// 大树 一下 双引用 单选矩阵
				if (!Util.isEmpty(q.qSiteOption2)) {
					// 大树 输出 把所有要排斥的选项添加到集合hava中
					if (twoQsiteOptions.size() > 0) {
						for (int i = 0; i < twoQsiteOptions.size(); i++) {
							Answer anQsite = ma.dbService.getAnswer(feed.getUuid(), twoQsiteOptions.get(i));
							if (anQsite != null && anQsite.getAnswerMapArr().size() > 0) {
								for (AnswerMap am : anQsite.getAnswerMapArr()) {
									/**
									 * 假如有值 自动查重 去除重复
									 */
									if (!Util.isEmpty(am.getAnswerValue())) {
										 if (Cnt.TYPE_RADIO_BUTTON ==
										 an.answerType) {
										 have.add(am.getRow());
										 } else {
										 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
										 }
									}
								}
							}
						}
					}
				}
				twoQsiteOptions.add(0, q.qSiteOption.trim());
				// 大树 以上部分 单选矩阵
				for (int i = 0; i < q.getRowItemArr().size(); i++) {
					QuestionItem qi = q.getRowItemArr().get(i);
					if (-1 != have.indexOf(qi.itemValue) && 1 != qi.itemShow) {
						/**
						 * 选择了这些选项
						 */
						rRows.remove(qi);
					}
				}
				have.clear();
				have = null;
				mTempRows.addAll(rRows);
			} else if ("0".equals(q.qInclusion)) {
				/**
				 * 引用
				 */
				isInclusion = true;
				// Question _q =
				// ma.dbService.getQuestion(feed.getSurveyId(),
				// q.qSiteOption);
				Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
				if (an != null) {
					ArrayList<AnswerMap> aml = an.getAnswerMapArr();
					ArrayList<Integer> have = new ArrayList<Integer>();
					for (AnswerMap am : aml) {
						/**
						 * 假如有值
						 */
						if (!Util.isEmpty(am.getAnswerValue())) {
							 if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
							 have.add(am.getRow());
							 } else {
							 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
							 }
						}
					}
					aml.clear();
					aml = null;

					// 大树 添加 双引用在里实现 以下： 双引用 单选矩阵
					if (!Util.isEmpty(q.qSiteOption2)) {
						// 大树 输出 自动查重 的功能
						if (twoQsiteOptions.size() > 0) {
							for (int i = 0; i < twoQsiteOptions.size(); i++) {

								Answer anQsite = ma.dbService.getAnswer(feed.getUuid(), twoQsiteOptions.get(i));
								if (anQsite != null && anQsite.getAnswerMapArr().size() > 0) {
									for (AnswerMap am : anQsite.getAnswerMapArr()) {
										/**
										 * 假如有值 自动查重 去除重复
										 */
										if (!Util.isEmpty(am.getAnswerValue())) {
											 if (Cnt.TYPE_RADIO_BUTTON ==
											 an.answerType) {
											 have.add(am.getRow());
											 } else {
											 have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
											 }
										}
									}
								}
							}
						}

					}
					twoQsiteOptions.add(0, q.qSiteOption.trim());
					// 单选矩阵 大树 双引用 的显示
					for (int i = 0; i < q.getRowItemArr().size(); i++) {
						QuestionItem qi = q.getRowItemArr().get(i);
						if (-1 == have.indexOf(qi.itemValue) && 1 != qi.itemShow) {

							/**
							 * 没有选择的, 则移走
							 */
							rRows.remove(qi);
						}
					}
					have.clear();
					have = null;
					mTempRows.addAll(rRows);
				} else {
					rRows.retainAll(rRows);
					mTempRows.addAll(rRows);
				}
			}

			// 大树 添加 垂直结构的 实现 提示语 水平结构 提示语 都在这里 引用排斥 双引用 单选矩阵 的处理 修改一下
			if (!Util.isEmpty(q.qSiteOption2) || !Util.isEmpty(q.qSiteOption)) {
				twoSiteNoticeTv.setTextColor(Color.RED);
				twoSiteNoticeTv.setTextSize(lowSurveySize);
				StringBuilder sb1 = new StringBuilder();
				if (twoQsiteOptions.size() > 0) {
					for (int i = 0; i < twoQsiteOptions.size(); i++) {
						Log.i("zrl1", twoQsiteOptions.get(i) + "双引用题目编号：");
						Question q = ma.dbService.getQuestion(feed.getSurveyId(), twoQsiteOptions.get(i));
						if (null != q) {
							if (!Util.isEmpty(q.qOrder + "")) {
								if (!Util.isEmpty(q.qid)) {
									sb1.append(q.qid + ",");
								} else {
									sb1.append("Q" + q.qOrder + ",");
								}
							} else
								Log.i("zrl1", "qid为空");
						}
					}
				}
				if (q.qInclusion.equals("0") && -1 != sb1.toString().indexOf(",")) {
					twoSiteNoticeTv.setText(NativeModeActivity.this.getString(R.string.each_answer_associated) + sb1.substring(0, sb1.toString().lastIndexOf(",")).toString()
							+ NativeModeActivity.this.getString(R.string.answer_value_reference));
				} else if (q.qInclusion.equals("1") && -1 != sb1.toString().indexOf(",")) {
					twoSiteNoticeTv.setText(NativeModeActivity.this.getString(R.string.each_answer_associated) + sb1.substring(0, sb1.toString().lastIndexOf(",")).toString()
							+ NativeModeActivity.this.getString(R.string.answer_value_rejection));
				}
			}

			// 大树 以上部分
			/**
			 * 题外关联 之 选项置顶 设置 标示 置顶 选项 置底 isItemBottom
			 * 
			 * 获取 所有选项置顶的 选项 集合 还有别忘啦 提示语的 显示 出来
			 */
			boolean isItemStick = ComUtil.isItemStick(rRows)[0];
			int[] itemList = ComUtil.getItemStick(rRows);// 置顶标示位置 数组
			int counter = 0; // 计数器
			int botValue = 0;// 置底 临时 位置 标示 底部
			int itemCount = rRows.size();// 所有的 选项 数量

			/**
			 * 假如是随机题目 则将其行和列都随机一遍
			 */
			if (1 == q.qRadomed) {
				/**
				 * 行随机暂存行中的数据
				 */
				ArrayList<QuestionItem> _tmpRows = new ArrayList<QuestionItem>();
				Random rd = new Random();
				int size = 0;
				if (isInclusion) {
					size = rRows.size();
				} else {
					size = q.getRowItemArr().size();
				}
				for (int i = 0; i < size; i++) {
					/**
					 * 随机产生数组的下标值
					 */
					int index = rd.nextInt(rRows.size());

					/**
					 * 题外关联 之 选项置顶 单选矩阵 的实现 选项置底 等操作的实现 过程
					 */
					QuestionItem item = rRows.get(index);
					if (null != item.padding && counter == 0 && item.padding != 1) {
						if (isItemStick) {
							index = itemList[0];
							item = rRows.get(index);
						}
					} else if (null != item.padding && counter != itemCount - 1 && item.padding == 2) {
						botValue = index;
						while (true) {
							index = rd.nextInt(rRows.size());
							if (index != botValue) {
								item = rRows.get(index);
								break;
							}
						}
					}
					counter++;

					_tmpRows.add(item);
					/**
					 * 取得随机产生的选项对象item
					 */

					// _tmpRows.add(rRows.get(index)); 注释 语句 这条
					/**
					 * 在暂存数组中移除随机产生的选项对象item
					 */
					rRows.remove(index);
				}

				rRows.addAll(_tmpRows);
				_tmpRows.clear();

				/**
				 * 列随机暂存列中的数据
				 */
				// ArrayList<QuestionItem> tmpCols = new
				// ArrayList<QuestionItem>();
				// for (int i = 0; i < q.getColItemArr().size(); i++) {
				// /**
				// * 随机产生数组的下标值
				// */
				// int index = rd.nextInt(rColmns.size());
				// /**
				// * 取得随机产生的选项对象item
				// */
				// tmpCols.add(rColmns.get(index));
				// /**
				// * 在暂存数组中移除随机产生的选项对象item
				// */
				// rColmns.remove(index);
				// }
				// rColmns.addAll(tmpCols);
				// tmpCols.clear();
			}

			/**
			 * 假如题干的宽度大于或等于屏幕宽度的3/4
			 */
			boolean isBeyond = ((screenWidth * 2 / 3 - 20)) <= rColmns.size() * 100;

			// 处理非随机矩阵
			/**
			 * 遍历每一行
			 */
			//新加的
			int rightNum=(q.isRight==1)?1:0;
			for (int r = 0; r < rRows.size() + 1; r++) {
				final ArrayList<View> rowViews=new ArrayList<View>();
				QuestionItem rowItem = null;
				if (0 != r) {
					// System.out.println("row=" + rRows.get(r -
					// 1).itemText);
					rowItem = rRows.get(r - 1);
					/**
					 * 题外关联 之 选项置顶 单选矩阵 把 itemValue 给注释掉 即可
					 */
					 //rowItem.itemValue = r - 1;
				}

				/**
				 * 遍历每一列
				 */
				RadioGroup rg = new RadioGroup(NativeModeActivity.this);// 每一列的
				rg.setVerticalGravity(Gravity.CENTER_VERTICAL);
				rg.setBackgroundColor(Color.TRANSPARENT);
				if (q.qStarCheck == 1) {
					rg.setBackgroundResource(R.drawable.piont_lv);
				}
				RadioGroup.LayoutParams rgPar = new RadioGroup.LayoutParams((screenWidth * 2 / 3 - 20),
						LayoutParams.WRAP_CONTENT);
				rgPar.setMargins(0, 0, 0, 0);
				rg.setLayoutParams(rgPar);
				rg.setPadding(0, 0, 0, 0);
				rg.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout ll = new LinearLayout(NativeModeActivity.this);
				ll.setVerticalGravity(Gravity.CENTER_VERTICAL);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setLayoutParams(FILL_WRAP);
				//新加的
				for (int c = 0; c < rColmns.size() + 1+rightNum; c++) {
					QuestionItem colItem = null;
					//新加的
					if (0 != c) {
						if(rightNum==1&&rColmns.size()+rightNum==c){
							
						}else{
							colItem = rColmns.get(c - 1);
						}
					}

					TextView tvTb = new TextView(NativeModeActivity.this);
					tvTb.setLayoutParams(WRAP_WRAP);
					tvTb.setGravity(Gravity.FILL);
					tvTb.setTextSize(lowSurveySize);
					tvTb.setWidth(100);
					tvTb.setPadding(2, 2, 2, 2);
					if (0 == r) {// 如过是第一行, 则打印出每一列的值
						// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
						ll.setBackgroundColor(Color.LTGRAY);// Color.LTGRAY
						if (0 == c) {// 打印表头
							tvTb.setText("");
							//矩阵右侧
							if(q.isRight==1){
								tvTb.setWidth(screenWidth / 6);
							}else{
								tvTb.setWidth(screenWidth / 3);
							}
							
						}
						//矩阵右侧 新加的
						else if(rColmns.size()+rightNum==c&&q.isRight==1){
							tvTb.setText("");
							tvTb.setWidth(screenWidth / 6);
						}else {// c !=0 打印每列的,即列标题
							// System.out.println("c_colmns_item_value=" +
							// rColmns.get(c - 1).itemValue + ", colmns=" +
							// (c -
							// 1));
							tvTb.setTextColor(Color.BLACK);
							if (isBeyond) {
								/**
								 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
								 */
								// 矩阵百分比
								tvTb.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							} else {
								tvTb.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							}
							System.out.println("c:"+c);
							System.out.println("colItem.itemText:"+colItem.itemText);
							String t = colItem.itemText;
							
							// ***********************************样式处理**************************//
							CstmMatcher cm = Util.findFontMatcherList(t);
							if (!Util.isEmpty(cm.getMis())) {
								t = cm.getResultStr();
								SpannableString ss = new SpannableString(t);
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length())
										ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
								CstmMatcher bCm = Util.findBoldMatcherList(t);
								if (!Util.isEmpty(bCm.getMis())) {
									for (MatcherItem mi : bCm.getMis()) {
										if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
											ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										}
									}
								}
								tvTb.setText(ss);
								//
							} else {
								CstmMatcher bCm = Util.findBoldMatcherList(t);
								if (!Util.isEmpty(bCm.getMis())) {
									t = bCm.getResultStr();
									SpannableString ss = new SpannableString(t);
									for (MatcherItem mi : bCm.getMis()) {
										if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
											ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										}
									}
									tvTb.setText(ss);
								} else {
									tvTb.setText(t);
								}

							}
							if (q.qStarCheck != 0 && c != 1 && c != rColmns.size()) {
								tvTb.setText("");
							}
						}
						// ***********************************样式处理**************************//
						CstmMatcher cm = Util.findImageMatherList(tvTb.getText().toString());
						if (0 < cm.getMis().size()) {
							LinearLayout _im = new LinearLayout(NativeModeActivity.this);
							_im.setOrientation(LinearLayout.VERTICAL);
							_im.setGravity(Gravity.FILL);
							_im.setLayoutParams(new LayoutParams(isBeyond ? 100 : (screenWidth * 2 / 3 - 20) / rColmns.size(), LayoutParams.WRAP_CONTENT));
							tvTb.setText(cm.getResultStr());
							_im.addView(tvTb, _im.getChildCount());
							for (int i = 0; i < cm.getMis().size(); i++) {
								MatcherItem mi = cm.getMis().get(i);
								ImageView iv = new ImageView(NativeModeActivity.this);
								iv.setPadding(2, 2, 2, 2);
								String path = Util.getImagePath(NativeModeActivity.this, feed.getSurveyId(), mi.name);
								// 图片百分比开始
								// iv.setLayoutParams(WRAP_WRAP);
								Bitmap image = BitmapFactory.decodeFile(path);
								int tWidth = image.getWidth();
								int tHeight = image.getHeight();
								iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
								// 图片百分比结束
								iv.setImageURI(Uri.parse(path));
								_im.addView(iv, _im.getChildCount());
								iv.setOnLongClickListener(new ImageLongClickListener(path));
							}
							ll.addView(_im, ll.getChildCount());
						} else {
							ll.addView(tvTb, ll.getChildCount());
						}

					} else {
						if (realRows % 2 == 0) {
							ll.setBackgroundColor(Color.parseColor("#F0F0F0"));
						} else {
							ll.setBackgroundColor(Color.TRANSPARENT);
						}
						if (0 == c) {
							/**
							 * 打印行标题
							 */
							tvTb.setBackgroundResource(R.drawable.small_text_background);
							tvTb.setTextColor(Color.BLACK);
							//矩阵右侧
							if(1==q.isRight){
								tvTb.setWidth(screenWidth / 6);
							}else{
								tvTb.setWidth(screenWidth / 3);
							}
							
							//tvTb.setBackgroundColor(Color.TRANSPARENT);
							String t = rowItem.itemText;

							/**
							 * 题外关联 之 选项置顶 单选矩阵 的提示语的 显示 出来
							 */
							if (null != rowItem.padding) {
								if (rowItem.padding == 1) {
									t = rowItem.itemText + "<font color=red>  " + this.getString(R.string.option_top) + "</font>";
								} else if (rowItem.padding == 2) {
									t = rowItem.itemText + "<font color=red>  " + this.getString(R.string.option_bottom) + "</font>";
								}
							}
							// ***********************************样式处理**************************//

//							if (!Util.isEmpty(t)) {
//								CstmMatcher cm = Util.findFontMatcherList(t);
//								if (!Util.isEmpty(cm.getMis())) {
//									t = cm.getResultStr();
//									if (!Util.isEmpty(t)) {
//										SpannableString ss = new SpannableString(t);
//										for (MatcherItem mi : cm.getMis()) {
//											ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//										}
//										CstmMatcher bCm = Util.findBoldMatcherList(t);
//										if (!Util.isEmpty(bCm.getMis())) {
//											for (MatcherItem mi : bCm.getMis()) {
//												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
//													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//												}
//											}
//										}
//										tvTb.setText(ss);
//									} else {
//										tvTb.setText(rowItem.itemText);
//									}
//									//
//								} else {
//									CstmMatcher bCm = Util.findBoldMatcherList(t);
//									if (!Util.isEmpty(bCm.getMis())) {
//										t = bCm.getResultStr();
//										if (!Util.isEmpty(t)) {
//											SpannableString ss = new SpannableString(t);
//											for (MatcherItem mi : bCm.getMis()) {
//												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
//													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//												}
//											}
//											tvTb.setText(ss);
//										} else {
//											tvTb.setText(rowItem.itemText);
//										}
//									} else {
//										tvTb.setText(t);
//									}
//
//								}
//
//							}
							//  样式修改      以上注释  
							if (!Util.isEmpty(t)) {
//								t=t.replace("color=Khaki", "color=#FDF8BF"); 
								tvTb.setText(Html.fromHtml(t));  
							}
							// ******** 图片 加载 ******************
							CstmMatcher cm = Util.findImageMatherList(tvTb.getText().toString());
							if (0 < cm.getMis().size()) {
								LinearLayout _im = new LinearLayout(NativeModeActivity.this);
								_im.setOrientation(LinearLayout.VERTICAL);
								_im.setGravity(Gravity.FILL);
								_im.setLayoutParams(new LayoutParams(screenWidth / 3, LayoutParams.WRAP_CONTENT));
								// _im.setLayoutParams(new
								// LayoutParams(isBeyond
								// ? 100 : (screenWidth * 2 / 3 - 20) /
								// rColmns.size(),
								// LayoutParams.WRAP_CONTENT));
								tvTb.setText(cm.getResultStr());
								_im.addView(tvTb, _im.getChildCount());
								for (int i = 0; i < cm.getMis().size(); i++) {
									MatcherItem mi = cm.getMis().get(i);
									ImageView iv = new ImageView(NativeModeActivity.this);
									iv.setPadding(2, 2, 2, 2);
									String path = Util.getImagePath(NativeModeActivity.this, feed.getSurveyId(), mi.name);
									// 图片百分比开始
									// iv.setLayoutParams(WRAP_WRAP);
									Bitmap image = BitmapFactory.decodeFile(path);
									int tWidth = image.getWidth();
									int tHeight = image.getHeight();
									iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
									// 图片百分比结束
									iv.setImageURI(Uri.parse(path));
									_im.addView(iv, _im.getChildCount());
									iv.setOnLongClickListener(new ImageLongClickListener(path));
								}
								ll.addView(_im, ll.getChildCount());
							} else {
								ll.addView(tvTb, ll.getChildCount());
							}

							// ***********************************样式处理**************************//
//							if (1 == q.qStarCheck) {
//								TextView tvLine = new TextView(NativeModeActivity.this);
//								tvLine.setBackgroundColor(Color.LTGRAY);
//								tvLine.setLayoutParams(new LayoutParams(3, LayoutParams.MATCH_PARENT));
//								ll.addView(tvLine);
//							}
						}
						// 矩阵右侧 新加的
						else if (rColmns.size() + rightNum == c && q.isRight == 1) {
							/**
							 * 打印行标题
							 */
							tvTb.setBackgroundResource(R.drawable.small_text_background);
							tvTb.setTextColor(Color.BLACK);
							// 矩阵右侧
							tvTb.setWidth(screenWidth / 6);

							//tvTb.setBackgroundColor(Color.TRANSPARENT);
							String t = rowItem.itemTextRight;
							// ***********************************样式处理**************************//

//							if (!Util.isEmpty(t)) {
//								CstmMatcher cm = Util.findFontMatcherList(t);
//								if (!Util.isEmpty(cm.getMis())) {
//									t = cm.getResultStr();
//									if (!Util.isEmpty(t)) {
//										SpannableString ss = new SpannableString(t);
//										for (MatcherItem mi : cm.getMis()) {
//											ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//										}
//										CstmMatcher bCm = Util.findBoldMatcherList(t);
//										if (!Util.isEmpty(bCm.getMis())) {
//											for (MatcherItem mi : bCm.getMis()) {
//												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
//													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//												}
//											}
//										}
//										tvTb.setText(ss);
//									} else {
//										tvTb.setText(rowItem.itemText);
//									}
//									//
//								} else {
//									CstmMatcher bCm = Util.findBoldMatcherList(t);
//									if (!Util.isEmpty(bCm.getMis())) {
//										t = bCm.getResultStr();
//										if (!Util.isEmpty(t)) {
//											SpannableString ss = new SpannableString(t);
//											for (MatcherItem mi : bCm.getMis()) {
//												if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
//													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//												}
//											}
//											tvTb.setText(ss);
//										} else {
//											tvTb.setText(rowItem.itemText);
//										}
//									} else {
//										tvTb.setText(t);
//									}
//
//								}
//
//							}
							//  样式修改      以上注释  
							if (!Util.isEmpty(t)) {
//								t=t.replace("color=Khaki", "color=#FDF8BF"); 
								tvTb.setText(Html.fromHtml(t));  
							}
							// ******** 图片 加载 ******************
							CstmMatcher cm = Util.findImageMatherList(tvTb.getText().toString());
							if (0 < cm.getMis().size()) {
								LinearLayout _im = new LinearLayout(NativeModeActivity.this);
								_im.setOrientation(LinearLayout.VERTICAL);
								_im.setGravity(Gravity.FILL);
								_im.setLayoutParams(new LayoutParams(screenWidth / 3, LayoutParams.WRAP_CONTENT));
								// _im.setLayoutParams(new
								// LayoutParams(isBeyond
								// ? 100 : (screenWidth * 2 / 3 - 20) /
								// rColmns.size(),
								// LayoutParams.WRAP_CONTENT));
								tvTb.setText(cm.getResultStr());
								_im.addView(tvTb, _im.getChildCount());
								for (int i = 0; i < cm.getMis().size(); i++) {
									MatcherItem mi = cm.getMis().get(i);
									ImageView iv = new ImageView(NativeModeActivity.this);
									iv.setPadding(2, 2, 2, 2);
									String path = Util.getImagePath(NativeModeActivity.this, feed.getSurveyId(), mi.name);
									// 图片百分比开始
									// iv.setLayoutParams(WRAP_WRAP);
									Bitmap image = BitmapFactory.decodeFile(path);
									int tWidth = image.getWidth();
									int tHeight = image.getHeight();
									iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
									// 图片百分比结束
									iv.setImageURI(Uri.parse(path));
									_im.addView(iv, _im.getChildCount());
									iv.setOnLongClickListener(new ImageLongClickListener(path));
								}
								ll.addView(_im, ll.getChildCount());
							} else {
								ll.addView(tvTb, ll.getChildCount());
							}
						}
						else {// 打印单选按钮
							if(1==c){
								ll.addView(rg);
							}
							// QuestionItem item = rRows.get(r - 1);
							if (null == rowItem || (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
								// 空其他项,即只有一个<freeInput/>标签
								continue;
							}
							// System.out.println("r_row_item_value=" +
							// rowItem.itemValue + ", r=" + (r - 1));
							RadioButton radio = new RadioButton(NativeModeActivity.this);
							radio.setLayoutParams(WRAP_WRAP);
							radio.setGravity(Gravity.FILL | Gravity.CENTER_VERTICAL);
							// radio.setGravity(Gravity.CENTER);

							AnswerMap am = new AnswerMap();
							String name = Util.GetAnswerName(q, rowItem, rowItem.itemValue, 0, false);
							am.setAnswerName(name);
							/**
							 * 相对的
							 */
							am.setRow(rowItem.itemValue);
							/**
							 * 绝对的
							 */
							am.setCol(colItem.itemValue);
							// radio.setOnClickListener(new On(rowItem,
							// colItem));
							// System.out.println("单选矩阵随机--->Key(" +
							// rowItem.itemText + "," + colItem.itemText +
							// ")====>Value(" + rowItem.itemValue + ", " +
							// colItem.itemValue + ")");
							am.setAnswerValue(String.valueOf(colItem.itemValue));
							radio.setTag(am);
							// radio.setTag(key, tag)
							rg.addView(radio, rg.getChildCount());
							if (!Util.isEmpty(amList)) {
								for (AnswerMap tam : amList) {
									if (name.equals(tam.getAnswerName()) && am.getAnswerValue().equals(tam.getAnswerValue())) {
										// System.out.println("匹配--->name="+name+", value="+am.getAnswerValue());
										radio.setChecked(true);
									}
								}
							}
							radio.setPadding(2, 2, 2, 2);
							radio.setTextSize(lowSurveySize);
							radio.setTextColor(Color.BLACK);
							// radio.setButtonDrawable(R.drawable.small_radiobutton_temp);
							radio.setButtonDrawable(R.drawable.small_radiobutton);
							radio.setBackgroundResource(R.drawable.small_radiobutton_background);
							if (q.qStarCheck != 0) {
								int drawable = R.drawable.small_radiobutton;
								radio.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(drawable),
										null, null, null);
								radio.setButtonDrawable(android.R.color.transparent);
								radio.setBackgroundResource(R.drawable.small_text_background);
								if (realRows % 2 == 0) {
									radio.setBackgroundColor(Color.WHITE);
								} else {
									radio.setBackgroundColor(Color.parseColor("#F0F0F0"));
								}
								switch (q.qStarCheck) {
								case 1:
									drawable = android.R.color.transparent;
									TextView tvLine = new TextView(NativeModeActivity.this);
									if (realRows % 2 == 0) {
										tvLine.setBackgroundColor(Color.parseColor("#F0F0F0"));
										radio.setBackgroundColor(Color.WHITE);
									} else {
										tvLine.setBackgroundColor(Color.WHITE);
										radio.setBackgroundColor(Color.parseColor("#F0F0F0"));
									}
									tvLine.setLayoutParams(new LayoutParams(3, LayoutParams.MATCH_PARENT));
									rg.addView(tvLine);
									break;
								case 2:
									drawable=R.drawable.star_24_off;
									break;
								case 3:
									drawable=R.drawable.hand_24_off;
									break;
								case 4:
									drawable=R.drawable.heart_24_off;
									break;
								}
								radio.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(drawable), null, null, null);
								rowViews.add(radio);
							}
							radio.setWidth(100);
							if (isBeyond) {
								/**
								 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
								 */
								// 矩阵百分比
								radio.setLayoutParams(new LinearLayout.LayoutParams((screenWidth * 2 / 3 - 20) / rColmns.size(), LinearLayout.LayoutParams.WRAP_CONTENT));
								radio.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							} else {
								// System.out.println("设置了Radio的宽度");
								radio.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							}
							// radio.setHeight(100);
							vs.add(radio);
//							radio.setOnClickListener(new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									Toasts.makeText(ma, "您选择了:"+t, Toast.LENGTH_SHORT).show();
//								}
//							});
						}

					}
				}
				
				if (0 != q.qStarCheck) {
					for (int i = 0; i < rowViews.size(); i++) {
						RadioButton rb = (RadioButton) rowViews.get(i);
						if (rb.isChecked()) {
							for (int j = 0; j < rowViews.size(); j++) {
								int drawable = R.drawable.small_radiobutton;
								switch (q.qStarCheck) {
								case 1:
									drawable = android.R.color.transparent;
									TextView tvLine = new TextView(NativeModeActivity.this);
									if (realRows % 2 == 0) {
										tvLine.setBackgroundColor(Color.parseColor("#F0F0F0"));
										((RadioButton) rowViews.get(j)).setBackgroundColor(Color.WHITE);
									} else {
										tvLine.setBackgroundColor(Color.WHITE);
										((RadioButton) rowViews.get(j)).setBackgroundColor(Color.parseColor("#F0F0F0"));
									}
									tvLine.setLayoutParams(new LayoutParams(3, LayoutParams.MATCH_PARENT));
									rg.addView(tvLine);
									break;
								case 2:
									drawable = R.drawable.star_24_off;
									break;
								case 3:
									drawable = R.drawable.hand_24_off;
									break;
								case 4:
									drawable = R.drawable.heart_24_off;
									break;
								}
								((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
										getResources().getDrawable(drawable), null, null, null);
							}
							for (int j = 0; j < i + 1; j++) {
								int drawable = R.drawable.small_radiobutton;
								switch (q.qStarCheck) {
								case 1:
									((RadioButton) rowViews.get(j)).setBackgroundResource(R.color.transparent);
									drawable = android.R.color.transparent;
									break;
								case 2:
									drawable = R.drawable.star_24;
									break;
								case 3:
									drawable = R.drawable.hand_24;
									break;
								case 4:
									drawable = R.drawable.heart_24;
									break;
								}
								((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
										getResources().getDrawable(drawable), null, null, null);
							}
						}
						if (realRows % 2 == 0) {
							rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									if (isChecked) {
										for (int j = 0; j < rowViews.size(); j++) {
											int drawable = R.drawable.small_radiobutton;
											switch (q.qStarCheck) {
											case 1:
												drawable = android.R.color.transparent;
												((RadioButton) rowViews.get(j)).setBackgroundColor(Color.WHITE);
												break;
											case 2:
												drawable = R.drawable.star_24_off;
												break;
											case 3:
												drawable = R.drawable.hand_24_off;
												break;
											case 4:
												drawable = R.drawable.heart_24_off;
												break;
											}
											((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
													getResources().getDrawable(drawable), null, null, null);
										}
										for (int j = 0; j < rowViews.indexOf(buttonView) + 1; j++) {
											int drawable = R.drawable.small_radiobutton;
											switch (q.qStarCheck) {
											case 1:
												((RadioButton) rowViews.get(j))
														.setBackgroundResource(R.color.transparent);
												drawable = android.R.color.transparent;
												break;
											case 2:
												drawable = R.drawable.star_24;
												break;
											case 3:
												drawable = R.drawable.hand_24;
												break;
											case 4:
												drawable = R.drawable.heart_24;
												break;
											}
											((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
													getResources().getDrawable(drawable), null, null, null);
										}
									}

								}
							});
						} else {
							rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									if (isChecked) {
										for (int j = 0; j < rowViews.size(); j++) {
											int drawable = R.drawable.small_radiobutton;
											switch (q.qStarCheck) {
											case 1:
												drawable = android.R.color.transparent;
												((RadioButton) rowViews.get(j))
														.setBackgroundColor(Color.parseColor("#F0F0F0"));
												break;
											case 2:
												drawable = R.drawable.star_24_off;
												break;
											case 3:
												drawable = R.drawable.hand_24_off;
												break;
											case 4:
												drawable = R.drawable.heart_24_off;
												break;
											}
											((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
													getResources().getDrawable(drawable), null, null, null);
										}
										for (int j = 0; j < rowViews.indexOf(buttonView) + 1; j++) {
											int drawable = R.drawable.small_radiobutton;
											switch (q.qStarCheck) {
											case 1:
												((RadioButton) rowViews.get(j))
														.setBackgroundResource(R.color.transparent);
												drawable = android.R.color.transparent;
												break;
											case 2:
												drawable = R.drawable.star_24;
												break;
											case 3:
												drawable = R.drawable.hand_24;
												break;
											case 4:
												drawable = R.drawable.heart_24;
												break;
											}
											((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
													getResources().getDrawable(drawable), null, null, null);
										}
									}

								}
							});
						}
					}
				}
				Log.i("@@@", "realRows="+realRows);
				realRows++;
				if (null != rowItem) {
					setHideRb(rowItem, ll);
					// 隐藏选项
					if (1 == rowItem.hide) {
						ll.setVisibility(View.GONE);
					}
				}
				bodyView.addView(ll, bodyView.getChildCount());
			}
		}
	}
	private void freeTextBox(ArrayList<AnswerMap> amList) {

		isNew = true;
		/**
		 * 题外关联 之 内部关联 字段 判断 大树 内部关联 2
		 */
		boolean isInner = false;
		ArrayList<QuestionItem> tbColumns = q.getColItemArr();
		for (int i = 0; i < tbColumns.size(); i++) {
			QuestionItem questionItem = tbColumns.get(i);
			if (questionItem.type == -1) {
				isNew = false;
				break;
			}
		}

		if (isNew) {
			bodyView.setOrientation(LinearLayout.VERTICAL);
			// 每个item值
			ArrayList<LinearLayout> colsLL = new ArrayList<LinearLayout>();
			// 单行文本框
			if (1 == q.freeTextColumn) {
				if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber) && 1 == q.freeNoRepeat) {
					TextView tvSyb = new TextView(NativeModeActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_num_sum_no_repeat,
							q.freeSymbol + q.freeSumNumber));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				} else if (1 == q.freeNoRepeat) {
					TextView tvSyb = new TextView(NativeModeActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_no_repeat));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				} else if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber)) {
					TextView tvSyb = new TextView(NativeModeActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_num_sum, q.freeSymbol + q.freeSumNumber));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				} else if (!Util.isEmpty(q.qParentAssociatedCheck)) {
					String value = q.qParentAssociatedCheck;
					String[] ss = value.split(",");
					if (ss[0].equals("2")) {

						TextView tvSyb = new TextView(NativeModeActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(16);
						/**
						 * 在这里 加一个 题外关联 字段的 判断 ： 然后 给用户提示 给出提示语句 ！
						 */
						String relevanceAnswer = ComUtil.getRelevanceAnswer(q, ma, feed);
						tvSyb.setText(getResources().getString(R.string.question_outying_right, relevanceAnswer));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					}
					if (ss[0].equals("1")) {
						TextView tvSyb = new TextView(NativeModeActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(16);

						/**
						 * 在这里加一个提示 题外关联 显示的 提示 语 ！ 大树 显示关联 26
						 */
						String parentQid = ma.dbService.getQuestion(feed.getSurveyId(), ss[1]).qid;
						tvSyb.setText(getResources().getString(R.string.question_outying_display_right, parentQid + "",
								(Integer.valueOf(ss[2]) + 1)) + "");
						bodyView.addView(tvSyb, bodyView.getChildCount());
					}
				} else {
					/**
					 * 题外关联 之 内部关联 提示语 大树 内部关联 3
					 */
					for (QuestionItem item : q.getColItemArr()) {
						if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
							TextView tvSyb = new TextView(NativeModeActivity.this);
							tvSyb.setLayoutParams(WRAP_WRAP);
							tvSyb.setTextColor(Color.RED);
							tvSyb.setTextSize(16);

							/**
							 * 在这里加一个提示 题外关联 显示的 提示 语 ！
							 */
							tvSyb.setText(this.getString(R.string.inner_refrense));
							bodyView.addView(tvSyb, bodyView.getChildCount());
							break;
						}
					}
				}
			}
			int tempQWidth = (dis.getWidth()) / q.freeTextColumn;
			// 每行多个文本题 ，弄两个垂直布局
			if (1 < q.freeTextColumn) {

				LinearLayout lvTitle = new LinearLayout(NativeModeActivity.this);
				lvTitle.setOrientation(LinearLayout.HORIZONTAL);
				lvTitle.setLayoutParams(FILL_WRAP);
				if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber) && 1 == q.freeNoRepeat) {
					TextView tvSyb = new TextView(NativeModeActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_num_sum_no_repeat,
							q.freeSymbol + q.freeSumNumber));
					lvTitle.addView(tvSyb, lvTitle.getChildCount());
				} else if (1 == q.freeNoRepeat) {
					TextView tvSyb = new TextView(NativeModeActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_no_repeat));
					lvTitle.addView(tvSyb, lvTitle.getChildCount());
				} else if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber)) {
					TextView tvSyb = new TextView(NativeModeActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_num_sum, q.freeSymbol + q.freeSumNumber));
					lvTitle.addView(tvSyb, lvTitle.getChildCount());
				} else if (!Util.isEmpty(q.qParentAssociatedCheck)) {
					String value = q.qParentAssociatedCheck;
					String[] ss = value.split(",");
					if (ss[0].equals("2")) {

						TextView tvSyb = new TextView(NativeModeActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(16);
						/**
						 * 在这里 加一个 题外关联 字段的 判断 ： 然后 给用户提示 给出提示语句 ！
						 */
						String relevanceAnswer = ComUtil.getRelevanceAnswer(q, ma, feed);
						tvSyb.setText(getResources().getString(R.string.question_outying_right, relevanceAnswer));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					}
					if (ss[0].equals("1")) {
						TextView tvSyb = new TextView(NativeModeActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(16);

						/**
						 * 在这里加一个提示 题外关联 显示的 提示 语 ！ 大树 显示关联 27
						 */
						String parentQid = ma.dbService.getQuestion(feed.getSurveyId(), ss[1]).qid;
						tvSyb.setText(getResources().getString(R.string.question_outying_display_right, parentQid + "",
								(Integer.valueOf(ss[2]) + 1)) + "");
						bodyView.addView(tvSyb, bodyView.getChildCount());
					}
				} else {
					/**
					 * 题外关联 之 内部关联 提示语 大树 内部关联 4
					 */
					for (QuestionItem item : q.getColItemArr()) {
						if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {

							isInner = true;
							tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1);
							TextView tvSyb = new TextView(NativeModeActivity.this);
							tvSyb.setLayoutParams(WRAP_WRAP);
							tvSyb.setTextColor(Color.RED);
							tvSyb.setTextSize(16);

							/**
							 * 在这里加一个提示 题外关联 显示的 提示 语 ！
							 */
							tvSyb.setText(this.getString(R.string.inner_refrense));
							bodyView.addView(tvSyb, bodyView.getChildCount());
							break;
						}
					}
				}
				bodyView.addView(lvTitle, bodyView.getChildCount());
				// 设置外层布局
				LinearLayout lvBodyView = new LinearLayout(NativeModeActivity.this);
				lvBodyView.setOrientation(LinearLayout.HORIZONTAL);
				lvBodyView.setLayoutParams(new LayoutParams(dis.getWidth(), LayoutParams.WRAP_CONTENT));
				// lvBodyView.setBackgroundColor(Color.RED);
				// 遍历每行的列数。
				for (int i = 0; i < q.freeTextColumn; i++) {
					LinearLayout ll = new LinearLayout(NativeModeActivity.this);
					ll.setOrientation(LinearLayout.VERTICAL);
					ll.setPadding(0, 0, 0, 0);
					// 都给1/3
					// ll.setLayoutParams(new LayoutParams(WRAP_WRAP));

					/**
					 * 题外关联 之内部关联 之 判断 字段 宽度进行 设置 对于 成 列的状态 大树 内部关联 5
					 */
					if (isInner) {
						ll.setLayoutParams(new LayoutParams(tempQWidth + tempQWidth / (q.freeTextColumn + 1),
								LayoutParams.WRAP_CONTENT));
					} else {
						QuestionItem itemNull = tbColumns.get(0);
						String leftsideWord = itemNull.getLeftsideWord();
						if (!Util.isEmpty(leftsideWord)) {
							tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1);
							if (i == 0) {
								ll.setLayoutParams(new LayoutParams(tempQWidth * 2, LayoutParams.WRAP_CONTENT));
							} else {
								ll.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
							}
						} else {
							ll.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
						}
					}

					// ll.setBackgroundColor(Color.BLUE);

					colsLL.add(ll);
					// 给横屏的bodyView加上
					lvBodyView.addView(ll, lvBodyView.getChildCount());
				}
				bodyView.addView(lvBodyView, bodyView.getChildCount());
			}
			/**
			 * 题外关联 之 显示关联 的处理 在这里做一下处理 ： 判断 --获取答案---比对---显示 大树 显示关联 1
			 */
			tbColumns = ComUtil.getOutLyingRelevanceDisplayItems(q, feed, ma, tbColumns);

			// 表格布局
			// 总布局宽度
			ArrayList<ArrayList<LinearLayout>> colsLLy = new ArrayList<ArrayList<LinearLayout>>();
			LinearLayout tl = new LinearLayout(NativeModeActivity.this);
			tl.setLayoutParams(WRAP_WRAP);
			bodyView.addView(tl, bodyView.getChildCount());
			tl.setOrientation(LinearLayout.VERTICAL);
			int tbRows = tbColumns.size() / q.freeTextColumn;
			if (0 != tbColumns.size() % q.freeTextColumn) {
				tbRows += 1;
			}
			for (int i = 0; i < tbRows; i++) {
				LinearLayout tbr = new LinearLayout(NativeModeActivity.this);// 每行的TableRow对象
				tbr.setOrientation(LinearLayout.HORIZONTAL);
				tbr.setGravity(Gravity.CENTER_VERTICAL);
				for (int col = 0; col < q.freeTextColumn; col++) {
					ArrayList<LinearLayout> itemLary = new ArrayList<LinearLayout>();// 每一个选项的整体
					LinearLayout itemll = new LinearLayout(NativeModeActivity.this);// 每一列的整体布局
					LinearLayout lLeft = new LinearLayout(NativeModeActivity.this);
					LinearLayout lRight = new LinearLayout(NativeModeActivity.this);
					itemll.setOrientation(LinearLayout.HORIZONTAL);
					itemll.setGravity(Gravity.CENTER_VERTICAL);
					itemll.setLayoutParams(
							new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
					itemll.setBackgroundColor(getResources().getColor(R.color.gray_bold));
					lLeft.setOrientation(LinearLayout.HORIZONTAL);
					lLeft.setGravity(Gravity.CENTER_VERTICAL);
					lLeft.setPadding(1, 1, 1, 1);
					lRight.setOrientation(LinearLayout.HORIZONTAL);
					lRight.setGravity(Gravity.CENTER_VERTICAL);
					lRight.setPadding(1, 1, 1, 1);
					itemLary.add(lLeft);
					itemLary.add(lRight);
					colsLLy.add(itemLary);
					itemll.addView(lLeft, itemll.getChildCount());
					itemll.addView(lRight, itemll.getChildCount());
					tbr.addView(itemll, tbr.getChildCount());
				}
				tl.addView(tbr, tl.getChildCount());
			}
			tempQWidth -= 20;
			// 1.2一般的方法
			// 遍历每一个项的值。
			// for_1
			ArrayList<Integer> maxSumWidArr = new ArrayList<Integer>();
			double fx = 1;
			// 遍历第一行
			for (int c = 1; c <= (tbColumns.size() / (q.freeTextColumn)); c++) {
				int sumWidth = 0;
				for (int h = 1; h <= q.freeTextColumn; h++) {
					int i = h * c - 1;
					if (1 < q.freeTextColumn) {
						QuestionItem itemNull = tbColumns.get(0);
						String leftsideWord = itemNull.getLeftsideWord();
						if (!Util.isEmpty(leftsideWord)) {
							if (0 == i % q.freeTextColumn) {
								tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1) * 2;
							} else {
								tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1);
							}
						}
					}
					ArrayList<LinearLayout> itemLary = colsLLy.get(i);
					LinearLayout lLeft = itemLary.get(0);
					LinearLayout lRight = itemLary.get(1);
					// 得到每一个项
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;

					// LayoutParams lp = new LayoutParams(100,
					// LayoutParams.WRAP_CONTENT);
					/**
					 * 题外关联 之 内部关联 判断 字段 并进行 界面 设定 宽度 大树 内部关联 6
					 */
					// itemLL.setBackgroundColor(Color.BLUE);
					// itemLL.setPadding(5, 5, 20, 5);
					if (item.required) {
						TextView tvRequired = new TextView(NativeModeActivity.this);

						/**
						 * 题外关联 之 显示 隐藏 选项 显示 必填 隐藏起来 大树 显示关联 2
						 */

						if (item.isHide) {
							tvRequired.setVisibility(View.GONE);
						}

						RelativeLayout.LayoutParams myParams = new LayoutParams(5, 5);
						// myParams.addRule(RelativeLayout.CENTER_VERTICAL,
						// itemLL.getId());
						// myParams.setMargins(0, 0, 6, 0);
						tvRequired.setLayoutParams(myParams);
						tvRequired.setText(getResources().getString(R.string.notice_required));
						tvRequired.setTextColor(Color.RED);
						int tvRequiredWid = (int) tvRequired.getPaint()
								.measureText(getResources().getString(R.string.notice_required));
						tempQWidth -= tvRequiredWid;
						// tvRequired.setTextSize(15);
						lLeft.addView(tvRequired, lLeft.getChildCount());
						sumWidth += tvRequiredWid;

					}
					// 初始化每一项的edittext
					EditText et = new EditText(NativeModeActivity.this);

					/**
					 * 题外关联 显示 的 设计 在这 显示几个选项
					 */

					if (item.isHide) {
						et.setVisibility(View.GONE);
					}

					if (item.itemSize != 0) {
						int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
						LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx), LayoutParams.WRAP_CONTENT);
						et.setLayoutParams(ITSELF);
					} else {
						int editWidth = Util.getEditWidth(20, maxCWidth);
						LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx), LayoutParams.WRAP_CONTENT);
						et.setLayoutParams(ITSELF);
					}
					// et.setMinimumWidth(150);
					et.setTextSize(lowSurveySize);
					et.setTag(item);
					// 左边样式
					SpannableString ssLeft = null;
					if (!Util.isEmpty(item.leftsideWord)) {
						String strTilte = item.leftsideWord;

						/**
						 * 换行
						 */
						String newTitle = Util.replaceMatcherList(strTilte);
						if (!Util.isEmpty(newTitle)) {
							strTilte = newTitle;
						}

						/**
						 * 粗体
						 */
						CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
						boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
						if (!boldHave) {
							strTilte = boldMatcherList.getResultStr();
						}

						/**
						 * 加下划线
						 */
						CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							strTilte = cmUnderLine.getResultStr();
						}

						/**
						 * font标签
						 */
						CstmMatcher cm = Util.findFontMatcherList(strTilte);
						boolean noFont = Util.isEmpty(cm.getResultStr());
						if (!noFont) {
							strTilte = cm.getResultStr();
							// System.out.println("Font之后--->"+strTilte);
						}
						int len = strTilte.length();
						if (0 < len) {

							ssLeft = new SpannableString(strTilte);
							if (!noFont) {
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
											&& mi.end <= strTilte.length()) {
										ssLeft.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new UnderlineSpan(), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

						}
					}
					// 右边样式
					SpannableString ssRight = null;
					if (!Util.isEmpty(item.rightsideWord)) {
						String strTilte = item.rightsideWord;
						/**
						 * 换行
						 */
						String newTitle = Util.replaceMatcherList(strTilte);
						if (!Util.isEmpty(newTitle)) {
							strTilte = newTitle;
						}

						/**
						 * 粗体
						 */
						CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
						boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
						if (!boldHave) {
							strTilte = boldMatcherList.getResultStr();
						}

						/**
						 * 加下划线
						 */
						CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							strTilte = cmUnderLine.getResultStr();
						}

						/**
						 * font标签
						 */
						CstmMatcher cm = Util.findFontMatcherList(strTilte);
						boolean noFont = Util.isEmpty(cm.getResultStr());
						if (!noFont) {
							strTilte = cm.getResultStr();
							// System.out.println("Font之后--->"+strTilte);
						}
						int len = strTilte.length();
						if (0 < len) {
							ssRight = new SpannableString(strTilte);

							if (!noFont) {
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
											&& mi.end <= strTilte.length()) {
										ssRight.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new UnderlineSpan(), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

						}
					}
					/**
					 * 2.其次问是什么类型的题目
					 */

					switch (item.type) {// switch
					/**
					 * 3.第三要询问左右两边是否都有字符串, 一共有3中情况:左右有、只左有、只右有、左右无
					 */
					case 0:
						// 维码扫描
					case 6:
						System.out.println("q.qLinkage:" + q.qLinkage);
						// 三级联动判断
						if (1 == q.qLinkage) {
							/**
							 * 需要加一个字段 来 做 判断 是否是三级联动 三级联动 的话 必须type 是none 现在测试
							 */
							if (!Util.isEmpty(item.leftsideWord)) {
								// Log.i("zrl1", item.leftsideWord);
								if (i == 0) {
									cityPos = 0;// 清0
									s1 = item.leftsideWord;
									provinceSpinner = new Spinner(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 3
									 */
									if (item.isHide) {
										provinceSpinner.setVisibility(View.GONE);
									}
									
									city = ThreeLeverUtil.getFirstList(s1);
									provinceAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter,
											city);// list是一个
									provinceAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									provinceSpinner.setAdapter(provinceAdapter);
									/**
									 * 获取 默认的答案
									 */
									if (!Util.isEmpty(amList)) {
										AnswerMap am1 = amList.get(0);
										cityPos = provinceAdapter.getPosition(am1.getAnswerValue());
										// 三级联动更改 处理更新问卷的操作,原因是找不到值
										if (-1 == cityPos) {
											cityPos = 0;
										}
										provinceSpinner.setSelection(cityPos, true);
									} else {
										provinceSpinner.setSelection(0, true);
									}
									provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(AdapterView<?> parent, View view, int position,
												long id) {
											// TODO Auto-generated method stub
											if (vs.size() >= 2) {
												Spinner spinner = (Spinner) vs.get(1);
												QuestionItem item = (QuestionItem) spinner.getTag();
												setCityAdapter(position, item);
											}

										}

										@Override
										public void onNothingSelected(AdapterView<?> parent) {
											// TODO Auto-generated method stub

										}
									});
									provinceSpinner.setTag(item);
									TextView tvLeft = new TextView(NativeModeActivity.this);
									String iCap = Util.getLeftCap(item.leftsideWord);
									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									int lenLeft = (int) paintLeft.measureText(iCap);
									if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
										lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
									}
									tvLeft.setLayoutParams(
											new LayoutParams(lenLeft,
													LayoutParams.WRAP_CONTENT));
									lLeft.addView(tvLeft, lLeft.getChildCount());
									lRight.addView(provinceSpinner, lRight.getChildCount());
									sumWidth += provinceSpinner.getLayoutParams().width+lenLeft;
								} else if (i == 1) {
									areaPos = 0;// 清0
									s2 = item.leftsideWord;
									citySpinner = new Spinner(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 大树 显示关联 4
									 */
									if (item.isHide) {
										citySpinner.setVisibility(View.GONE);
									}

									area = ThreeLeverUtil.getSecondList(s2);
									System.out.println("cityPos:" + cityPos);
									areaListTemp = ThreeLeverUtil.getCityPosList(area, city, cityPos);
									if (areaListTemp.size() == 0) {
										areaListTemp.add("空");
									}
									cityAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter,
											areaListTemp);// list是一个
									cityAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									citySpinner.setAdapter(cityAdapter);
									citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(AdapterView<?> parent, View view, int position,
												long id) {
											// TODO Auto-generated method stub
											if (vs.size() >= 3) {
												Spinner spinner = (Spinner) vs.get(2);
												QuestionItem item = (QuestionItem) spinner.getTag();
												setCountryAdapter(position, item);
											}

										}

										@Override
										public void onNothingSelected(AdapterView<?> parent) {
											// TODO Auto-generated method stub

										}
									});
									/**
									 * 获取原有答案
									 */
									if (!Util.isEmpty(amList)) {

										AnswerMap am1 = amList.get(1);
										areaPos = cityAdapter.getPosition(am1.getAnswerValue());
										// 三级联动更改 处理更新问卷的操作,原因是找不到值
										if (-1 == areaPos) {
											areaPos = 0;
										}
										citySpinner.setSelection(areaPos, true);
									} else {
										citySpinner.setSelection(0, true);
									}
									citySpinner.setTag(item);
									TextView tvLeft = new TextView(NativeModeActivity.this);
									String iCap = Util.getLeftCap(item.leftsideWord);
									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									int lenLeft = (int) paintLeft.measureText(iCap);
									if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
										lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
									}
									tvLeft.setLayoutParams(
											new LayoutParams(lenLeft,
													LayoutParams.WRAP_CONTENT));
									lLeft.addView(tvLeft, lLeft.getChildCount());
									lRight.addView(citySpinner, lRight.getChildCount());
									sumWidth += citySpinner.getLayoutParams().width+lenLeft;

								} else if (i == 2) {
									s3 = item.leftsideWord;
									countrySpinner = new Spinner(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 大树 显示关联 5
									 */
									if (item.isHide) {
										countrySpinner.setVisibility(View.GONE);
									}
									way = ThreeLeverUtil.getThridList(s3);
									wayListTemp = ThreeLeverUtil.getAreaPosList(areaListTemp, way, areaPos);
									if (wayListTemp.size() == 0) {
										wayListTemp.add("空");
									}

									countryAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter,
											wayListTemp);// list是一个
									countryAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									countrySpinner.setAdapter(countryAdapter);
									/**
									 * 获取 原有 答案
									 */
									if (!Util.isEmpty(amList)) {

										AnswerMap am1 = amList.get(2);
										int pos = countryAdapter.getPosition(am1.getAnswerValue());
										// 三级联动更改 处理更新问卷的操作,原因是找不到值
										if (-1 == pos) {
											pos = 0;
										}
										countrySpinner.setSelection(pos, true);
									} else {
										countrySpinner.setSelection(0, true);
									}
									TextView tvLeft = new TextView(NativeModeActivity.this);
									String iCap = Util.getLeftCap(item.leftsideWord);
									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									int lenLeft = (int) paintLeft.measureText(iCap);
									if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
										lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
									}
									tvLeft.setLayoutParams(
											new LayoutParams(lenLeft,
													LayoutParams.WRAP_CONTENT));
									lLeft.addView(tvLeft, lLeft.getChildCount());
									lRight.addView(countrySpinner, lRight.getChildCount());
									sumWidth += countrySpinner.getLayoutParams().width+lenLeft;
									countrySpinner.setTag(item);
								}
							}

						} else {
							// 非三级联动
							// 典型的文本
							// 左右有文本值
							// 假如左右不为空，给设置颜色
							ImageView iv = new ImageView(NativeModeActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 6
							 */
							if (item.isHide) {
								iv.setVisibility(View.GONE);
							}
							// 维码扫描 item.scanning
							if (6 == item.type) {
								iv = new ImageView(NativeModeActivity.this);
								RelativeLayout.LayoutParams myParams = WRAP_WRAP;
								String idStr = q.qIndex + "_" + i;
								iv.setId(idStr.hashCode());
								iv.setLayoutParams(myParams);
								iv.setBackgroundResource(R.drawable.icon_scanning);
								iv.setOnClickListener(new ScanningListener(et));
							}
							// 维码扫描

							if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
								// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
								ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
								// 左边没有下拉
								if (Util.isEmpty(leftList)) {
									TextView tvLeft = new TextView(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(ssLeft);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
									if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
										lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
									}
									tvLeft.setLayoutParams(
											new LayoutParams(lenLeft,
													LayoutParams.WRAP_CONTENT));
									TextView tvRight = new TextView(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 8
									 */
									if (item.isHide) {
										tvRight.setVisibility(View.GONE);
									}

									tvRight.setTextSize(lowSurveySize);
									tvRight.setTextColor(Color.BLACK);
									tvRight.setText(ssRight);
									tvRight.setPadding(0, 0, 0, 8);
									TextPaint paintRight = tvRight.getPaint();
									int lenRight = (int) paintRight.measureText(item.rightsideWord);
									if(lenRight>(int) (screenWidth / (q.freeTextColumn + 3) * fx)){
										lenRight=(int) (screenWidth / (q.freeTextColumn + 3) * fx);
									}
									tvRight.setLayoutParams(
											new LayoutParams(lenLeft,
													LayoutParams.WRAP_CONTENT));
									lLeft.addView(tvLeft, lLeft.getChildCount());
									sumWidth += tvLeft.getLayoutParams().width;
									// 有答案取答案 赋值
									if (!Util.isEmpty(amList)) {
										String etName = Util.GetAnswerName(q, item, 0, 0, true);
										for (AnswerMap am : amList) {
											if (etName.equals(am.getAnswerName())) {
												et.setText(am.getAnswerValue());
											}
										}
									}
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
									lRight.addView(et, lRight.getChildCount());
									sumWidth += et.getLayoutParams().width;
									lRight.addView(tvRight, lRight.getChildCount());
									sumWidth += tvRight.getLayoutParams().width;
								} else {
									/**
									 * 左边有下拉框
									 */
									String iCap = Util.getLeftCap(item.leftsideWord);
									int lenLeft = 0;
									if (!Util.isEmpty(iCap)) {
										/**
										 * @@::前面有文字
										 */
										TextView tvLeft = new TextView(NativeModeActivity.this);

										/**
										 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 9
										 */
										if (item.isHide) {
											tvLeft.setVisibility(View.GONE);
										}

										tvLeft.setTextSize(lowSurveySize);
										tvLeft.setTextColor(Color.BLACK);
										tvLeft.setText(iCap);
										tvLeft.setPadding(0, 0, 0, 8);
										TextPaint paintLeft = tvLeft.getPaint();
										lenLeft = (int) paintLeft.measureText(iCap);
										int maxLeft = (int) (screenWidth / (q.freeTextColumn + 2) * fx) ;
										if (lenLeft > maxLeft) {
											lenLeft = maxLeft;
										}
										tvLeft.setLayoutParams(
												new LayoutParams(lenLeft,
														LayoutParams.WRAP_CONTENT));
										lLeft.addView(tvLeft, lLeft.getChildCount());
										sumWidth += lenLeft;
									}

									Spinner spLeft = new Spinner(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 大树 显示关联 10
									 */
									if (item.isHide) {
										spLeft.setVisibility(View.GONE);
									}

									spLeft.setTag(item);
									spLeft.setLayoutParams(WRAP_WRAP);
									// simple_spinner_item
									// R.layout.simple_spinner_dropdown_item
									ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
											R.layout.simple_spinner_adapter);
									aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									for (String str : leftList) {
										aa.add(str);
									}
									spLeft.setAdapter(aa);

									if (!Util.isEmpty(amList)) {
										for (int j = 0; j < amList.size(); j++) {
											// 通过存的value得到位置
											if (j == i) {
												AnswerMap am = amList.get(i);
												int pos = aa.getPosition(am.getAnswerValue());
												if (-1 != pos) {
													// 选上位置

													spLeft.setSelection(pos);
													break;
												}
											}
										}
									}

									TextView tvRight = new TextView(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 11
									 */
									if (item.isHide) {
										tvRight.setVisibility(View.GONE);
									}

									tvRight.setTextSize(lowSurveySize);
									tvRight.setTextColor(Color.BLACK);
									tvRight.setText(ssRight);
									tvRight.setPadding(0, 0, 0, 8);
									TextPaint paintRight = tvRight.getPaint();
									int lenRight = (int) paintRight.measureText(item.rightsideWord);
									if(lenRight>(int) (screenWidth / (q.freeTextColumn + 3) * fx)){
										lenRight=(int) (screenWidth / (q.freeTextColumn + 3) * fx);
									}
									tvRight.setLayoutParams(
											new LayoutParams(lenRight,
													LayoutParams.WRAP_CONTENT));
									int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams SPSELF = new LayoutParams(spWidth, LayoutParams.WRAP_CONTENT);
									// 判断有没有前面的左描述
									if (!Util.isEmpty(iCap)) {
										// 余下的长度
										int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenLeft)
												+ (tempQWidth / 3 - lenRight);
										if (spWidth > remainWidth) {
											spLeft.setLayoutParams(new LayoutParams((int) (remainWidth * fx),
													LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									} else {
										int remainWidthT = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
										if (spWidth > remainWidthT) {
											spLeft.setLayoutParams(new LayoutParams((int) (remainWidthT * fx),
													LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									}
									lRight.addView(spLeft, lRight.getChildCount());
									sumWidth += spLeft.getLayoutParams().width;
									lRight.addView(tvRight, lRight.getChildCount());
									sumWidth += tvRight.getLayoutParams().width;
								}
							} else if (!Util.isEmpty(item.leftsideWord)) {
								// 只有左边有文字。右边没文字
								ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
								/**
								 * 获取左边的说明文字
								 */
								String iCap = Util.getLeftCap(item.leftsideWord);
								int lenLeft = 0;
								if (!Util.isEmpty(iCap)) {
									/**
									 * 左边是说明文字 右边是下拉列表框
									 */
									TextView tvLeft = new TextView(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 大树 显示关联 12
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(iCap);
									if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
										lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
									}
									tvLeft.setLayoutParams(
											new LayoutParams(lenLeft,
													LayoutParams.WRAP_CONTENT));
									lLeft.addView(tvLeft, lLeft.getChildCount());
									sumWidth += tvLeft.getLayoutParams().width;
								} else if (Util.isEmpty(leftList)) {
									// 左边不是说明文字 。没有下拉列表框
									TextView tvLeft = new TextView(NativeModeActivity.this);
									/**
									 * 题外关联--- 显示关联 大树 显示关联 13
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(ssLeft);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(item.leftsideWord);
									if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
										lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
									}
									tvLeft.setLayoutParams(
											new LayoutParams(lenLeft,
													LayoutParams.WRAP_CONTENT));
									lLeft.addView(tvLeft, lLeft.getChildCount());
									sumWidth += tvLeft.getLayoutParams().width;
								}
								/**
								 *** 假如左边不是下拉框 直接给文本框赋值。
								 */
								if (Util.isEmpty(leftList)) {
									if (!Util.isEmpty(amList)) {
										String etName = Util.GetAnswerName(q, item, 0, 0, true);
										for (AnswerMap am : amList) {
											if (etName.equals(am.getAnswerName())) {
												et.setText(am.getAnswerValue());
											}
										}
									}
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
									lRight.addView(et, lRight.getChildCount());
									sumWidth += et.getLayoutParams().width;
								} else {
									Spinner spLeft = new Spinner(NativeModeActivity.this);
									/**
									 * 题外关联--- 显示关联 大树 显示关联 14
									 */
									if (item.isHide) {
										spLeft.setVisibility(View.GONE);
									}
									spLeft.setTag(item);
									spLeft.setLayoutParams(WRAP_WRAP);
									// simple_spinner_item
									// R.layout.simple_spinner_dropdown_item
									ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
											R.layout.simple_spinner_adapter);
									aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									for (String str : leftList) {
										aa.add(str);
									}
									spLeft.setAdapter(aa);
									if (!Util.isEmpty(amList)) {
										for (int j = 0; j < amList.size(); j++) {
											// 通过存的value得到位置
											if (j == i) {
												AnswerMap am = amList.get(i);
												int pos = aa.getPosition(am.getAnswerValue());
												if (-1 != pos) {
													// 选上位置
													spLeft.setSelection(pos);
													break;
												}
											}
										}
									}
									int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams SPSELF = new LayoutParams((int) (spWidth * fx), LayoutParams.WRAP_CONTENT);
									spLeft.setLayoutParams(SPSELF);

									lRight.addView(spLeft, lRight.getChildCount());
									sumWidth += spLeft.getLayoutParams().width;

								}
							} else if (!Util.isEmpty(item.rightsideWord)) {
								// 只有右边有。左边没有
								/**
								 * 左边是文本框 右边是说明
								 */
								TextView tvRight = new TextView(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 15
								 */
								if (item.isHide) {
									tvRight.setVisibility(View.GONE);
								}
								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();

								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								if(lenRight>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
									lenRight=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
								}
								tvRight.setLayoutParams(
										new LayoutParams(lenRight,
												LayoutParams.WRAP_CONTENT));
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
								if (et.getLayoutParams().width > remainWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								sumWidth += et.getLayoutParams().width;
								lRight.addView(tvRight, lRight.getChildCount());
								sumWidth += tvRight.getLayoutParams().width;
							} else {
								// 左右无,只有文本题目
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								if (et.getLayoutParams().width > (tempQWidth)) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								sumWidth += et.getLayoutParams().width;
							}
							// 维码
							if (6 == item.type) {
								lRight.addView(iv, lRight.getChildCount());
								sumWidth += iv.getLayoutParams().width;
							}
						}
						break;

					case 1:// 日期格式
					case 2:// 数字格式
					case 3:// 英文/数字格式
					case 5:// 邮件格式
						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							// 左右有文字说明
							TextView tvLeft = new TextView(NativeModeActivity.this);

							/**
							 * 题外关联 之显示 隐藏 左边文字
							 */

							if (item.isHide) {
								tvLeft.setVisibility(View.GONE);
							}

							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(ssLeft);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							int lenLeft = (int) paintLeft.measureText(item.leftsideWord);

							tvLeft.setLayoutParams(new LayoutParams((int) (screenWidth / (q.freeTextColumn + 2) * fx),
									LayoutParams.WRAP_CONTENT));

							if (1 == item.type) {
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
								et.setOnTouchListener(
										new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								}
								if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
									et.setHint(
											this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
								} else if (!Util.isEmpty(item.minNumber)) {
									et.setHint(this.getString(R.string.edit_min, item.minNumber));
								} else if (!Util.isEmpty(item.maxNumber)) {
									et.setHint(this.getString(R.string.edit_max, item.maxNumber));
								}
							} else if (3 == item.type) {// 英文/数字
								et.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if (5 == item.type) {// 邮件
								et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								et.setHint(this.getString(R.string.please_input_email));
							}
							TextView tvRight = new TextView(NativeModeActivity.this);

							/**
							 * 题外关联 之 显示 隐藏 选项
							 */
							if (item.isHide) {
								tvRight.setVisibility(View.GONE);
							}

							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							tvRight.setLayoutParams(new LayoutParams((int) (screenWidth / (q.freeTextColumn + 3) * fx),
									LayoutParams.WRAP_CONTENT));
							lLeft.addView(tvLeft, lLeft.getChildCount());
							sumWidth += tvLeft.getLayoutParams().width;
							// 假如是滑动条,且是数字题目。显示滑动条。
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(NativeModeActivity.this);
								sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
								sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
								sb.setTag(item);
								// sb.setLayoutParams(new LayoutParams(3 *
								// dis.getWidth() / 4, //
								// LayoutParams.WRAP_CONTENT));
								// sb.setPadding(5, 0, 5, 15);
								// sb.setMinimumWidth(400);
								int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenRight)
										+ (tempQWidth / 3 - lenLeft);
								sb.setLayoutParams(WRAP_WRAP);
								sb.setMax(Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber));
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											if (!Util.isEmpty(am.getAnswerValue())) {
												sb.setProgress(Integer.parseInt(am.getAnswerValue()));
											}
										}
									}
								}
								sb.setThumbOffset(0);
								TextView tvSeekTop = new TextView(NativeModeActivity.this);
								tvSeekTop.setLayoutParams(WRAP_WRAP);
								// tvSeekTop.setLayoutParams(FILL_WRAP);

								tvSeekTop.setTextSize(lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

								LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvSeekTop, null));
								lRight.addView(rightLL, lRight.getChildCount());
								sumWidth += tvSeekTop.getLayoutParams().width + sb.getLayoutParams().width;
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
							} else {
								// 不是情况直接显示文本题目

								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenRight)
										+ (tempQWidth / 3 - lenLeft);
								if (et.getLayoutParams().width > remainWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								sumWidth += et.getLayoutParams().width;
							}
							lRight.addView(tvRight, lRight.getChildCount());
							sumWidth += tvRight.getLayoutParams().width;

							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 7
							 */
							if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {

								TextView tvSyb = new TextView(NativeModeActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								// tvSyb.setLayoutParams(new
								// LayoutParams(100,LayoutParams.WRAP_CONTENT));
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(16);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("(" + this.getString(R.string.sum) + ")");
								TextPaint paintSyb = tvSyb.getPaint();
								int lenSyb = (int) paintSyb.measureText(item.rightsideWord);
								tvSyb.setLayoutParams(new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
								lRight.addView(tvSyb);
								sumWidth += tvSyb.getLayoutParams().width;
							}

						} else if (!Util.isEmpty(item.leftsideWord)) {
							// 只左有
							TextView tvLeft = new TextView(NativeModeActivity.this);
							/**
							 * 题外关联 之显示 隐藏 选项 左边文字
							 */
							if (item.isHide) {
								tvLeft.setVisibility(View.GONE);

							}
							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(ssLeft);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							int lenLeft = (int) paintLeft.measureText(item.leftsideWord);

							tvLeft.setLayoutParams(new LayoutParams((int) (screenWidth / (q.freeTextColumn + 2) * fx),
									LayoutParams.WRAP_CONTENT));

							if (1 == item.type) {
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
								et.setOnTouchListener(
										new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								}
								if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
									et.setHint(
											this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
								} else if (!Util.isEmpty(item.minNumber)) {
									et.setHint(this.getString(R.string.edit_min, item.minNumber));
								} else if (!Util.isEmpty(item.maxNumber)) {
									et.setHint(this.getString(R.string.edit_max, item.maxNumber));
								}
							} else if (3 == item.type) {// 英文/数字
								et.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if (5 == item.type) {// 邮件
								et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								et.setHint(this.getString(R.string.please_input_email));
							}

							// 假如是滑动条,且是数字题目。显示滑动条。
							lLeft.addView(tvLeft, lLeft.getChildCount());
							sumWidth += tvLeft.getLayoutParams().width;
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 16
								 */
								if (item.isHide) {
									sb.setVisibility(View.GONE);
								}

								sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
								sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
								sb.setTag(item);
								// sb.setLayoutParams(new LayoutParams(3 *
								// dis.getWidth() / 4, //
								// LayoutParams.WRAP_CONTENT));
								// sb.setPadding(5, 0, 5, 15);
								// sb.setMinimumWidth(400);
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
								sb.setLayoutParams(WRAP_WRAP);
								// sb.set
								sb.setMax(Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber));
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											if (!Util.isEmpty(am.getAnswerValue())) {
												sb.setProgress(Integer.parseInt(am.getAnswerValue()));
											}
										}
									}
								}
								sb.setThumbOffset(0);

								TextView tvSeekTop = new TextView(NativeModeActivity.this);
								// tvSeekTop.setLayoutParams(FILL_WRAP);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 17
								 */
								if (item.isHide) {
									tvSeekTop.setVisibility(View.GONE);
								}
								tvSeekTop.setLayoutParams(WRAP_WRAP);
								tvSeekTop.setTextSize(lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

								LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvSeekTop, null));
								lRight.addView(rightLL, lRight.getChildCount());
								sumWidth += tvSeekTop.getLayoutParams().width + sb.getLayoutParams().width;
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
							} else {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
								if (et.getLayoutParams().width > remainWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								sumWidth += et.getLayoutParams().width;

								/**
								 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树
								 * 内部关联 8
								 */
								if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
									TextView tvSyb = new TextView(NativeModeActivity.this);
									tvSyb.setLayoutParams(WRAP_WRAP);
									// tvSyb.setLayoutParams(new
									// LayoutParams(150,
									// LayoutParams.WRAP_CONTENT));
									tvSyb.setTextColor(Color.RED);
									tvSyb.setTextSize(16);
									/**
									 * 在这里加一个提示 题外关联 显示的 提示 语 ！
									 */
									tvSyb.setText("(" + this.getString(R.string.sum) + ")");
									TextPaint paintSyb = tvSyb.getPaint();
									int lenSyb = (int) paintSyb.measureText(item.rightsideWord);
									tvSyb.setLayoutParams(
											new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
									lRight.addView(tvSyb, lRight.getChildCount());
									sumWidth += tvSyb.getLayoutParams().width;
								}

							}
						} else if (!Util.isEmpty(item.rightsideWord)) {
							if (1 == item.type) {// 日期
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
								et.setOnTouchListener(
										new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								}
								if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
									et.setHint(
											this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
								} else if (!Util.isEmpty(item.minNumber)) {
									et.setHint(this.getString(R.string.edit_min, item.minNumber));
								} else if (!Util.isEmpty(item.maxNumber)) {
									et.setHint(this.getString(R.string.edit_max, item.maxNumber));
								}
							} else if (3 == item.type) {// 英文/数字
								et.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if (5 == item.type) {// 邮件
								et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								et.setHint(this.getString(R.string.please_input_email));
							}
							TextView tvRight = new TextView(NativeModeActivity.this);

							/**
							 * 题外关联 之显示 隐藏 选项 右边文字
							 */
							if (item.isHide) {
								tvRight.setVisibility(View.GONE);
							}
							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							tvRight.setLayoutParams(new LayoutParams((int) (screenWidth / (q.freeTextColumn + 3) * fx),
									LayoutParams.WRAP_CONTENT));
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 18
								 */
								if (item.isHide) {
									sb.setVisibility(View.GONE);
								}

								sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
								sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
								sb.setTag(item);
								// sb.setLayoutParams(new LayoutParams(3 *
								// dis.getWidth() / 4, //
								// LayoutParams.WRAP_CONTENT));
								// sb.setPadding(5, 0, 5, 15);
								// sb.setMinimumWidth(400);
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
								sb.setLayoutParams(WRAP_WRAP);
								// sb.set
								sb.setMax(Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber));
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											if (!Util.isEmpty(am.getAnswerValue())) {
												sb.setProgress(Integer.parseInt(am.getAnswerValue()));
											}
										}
									}
								}
								sb.setThumbOffset(0);
								TextView tvSeekTop = new TextView(NativeModeActivity.this);
								// tvSeekTop.setLayoutParams(FILL_WRAP);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 19
								 */
								if (item.isHide) {
									tvSeekTop.setVisibility(View.GONE);
								}

								tvSeekTop.setLayoutParams(WRAP_WRAP);
								tvSeekTop.setTextSize(lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

								LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvSeekTop, null));
								lRight.addView(rightLL, lRight.getChildCount());
								sumWidth += tvSeekTop.getLayoutParams().width + sb.getLayoutParams().width;

								/**
								 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树
								 * 内部关联 9
								 */
								if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
									TextView tvSyb = new TextView(NativeModeActivity.this);
									tvSyb.setLayoutParams(WRAP_WRAP);
									tvSyb.setTextColor(Color.RED);
									tvSyb.setTextSize(16);
									/**
									 * 在这里加一个提示 题外关联 显示的 提示 语 ！
									 */
									tvSyb.setText("(" + this.getString(R.string.sum) + ")");
									TextPaint paintSyb = tvSyb.getPaint();
									int lenSyb = (int) paintSyb.measureText(item.rightsideWord);
									tvSyb.setLayoutParams(
											new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
									lRight.addView(tvSyb);
									sumWidth += tvSyb.getLayoutParams().width;
								}

								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
							} else {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
								if (et.getLayoutParams().width > remainWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								sumWidth += et.getLayoutParams().width;
								/**
								 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树
								 * 内部关联 10
								 */
								if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
									TextView tvSyb = new TextView(NativeModeActivity.this);
									tvSyb.setTextColor(Color.RED);
									tvSyb.setTextSize(16);
									tvSyb.setText("(" + this.getString(R.string.sum) + ")");
									TextPaint paintSyb = tvSyb.getPaint();
									int lenSyb = (int) paintSyb.measureText(item.rightsideWord);
									tvSyb.setLayoutParams(
											new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
									/**
									 * 在这里加一个提示 题外关联 显示的 提示 语 ！
									 */
									lRight.addView(tvSyb);
									sumWidth += tvSyb.getLayoutParams().width;
								}

							}

							lRight.addView(tvRight, lRight.getChildCount());
							sumWidth += tvRight.getLayoutParams().width;

						} else {
							// 左右无
							if (1 == item.type) {// 日期
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
								et.setOnTouchListener(
										new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								}
								if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
									et.setHint(
											this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
								} else if (!Util.isEmpty(item.minNumber)) {
									et.setHint(this.getString(R.string.edit_min, item.minNumber));
								} else if (!Util.isEmpty(item.maxNumber)) {
									et.setHint(this.getString(R.string.edit_max, item.maxNumber));
								}
							} else if (3 == item.type) {// 英文/数字
								et.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if (5 == item.type) {// 邮件
								et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								et.setHint(this.getString(R.string.please_input_email));
							}

							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 20
								 */
								if (item.isHide) {
									sb.setVisibility(View.GONE);
								}

								sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
								sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
								sb.setTag(item);
								sb.setLayoutParams(WRAP_WRAP);
								sb.setPadding(5, 0, 5, 15);
								// sb.setMinimumWidth(400);
								// sb.set
								sb.setMax(Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber));
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											if (!Util.isEmpty(am.getAnswerValue())) {
												sb.setProgress(Integer.parseInt(am.getAnswerValue()));
											}
										}
									}
								}
								sb.setThumbOffset(0);
								TextView tvSeekTop = new TextView(NativeModeActivity.this);
								/**
								 * 题外关联--- 显示关联 大树 显示关联 21
								 */
								if (item.isHide) {
									tvSeekTop.setVisibility(View.GONE);
								}

								tvSeekTop.setLayoutParams(FILL_WRAP);
								tvSeekTop.setTextSize(lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

								LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvSeekTop, null));
								lRight.addView(rightLL, lRight.getChildCount());
								sumWidth += tvSeekTop.getLayoutParams().width + sb.getLayoutParams().width;
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
							} else {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								if (et.getLayoutParams().width > tempQWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}

								lRight.addView(et, lRight.getChildCount());
								sumWidth += et.getLayoutParams().width;

								/**
								 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树
								 * 内部关联 11
								 */
								if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
									TextView tvSyb = new TextView(NativeModeActivity.this);
									tvSyb.setTextColor(Color.RED);
									tvSyb.setTextSize(16);
									/**
									 * 在这里加一个提示 题外关联 显示的 提示 语 ！
									 */
									tvSyb.setText("(" + this.getString(R.string.sum) + ")");
									TextPaint paintSyb = tvSyb.getPaint();
									int lenSyb = (int) paintSyb.measureText(item.leftsideWord);
									tvSyb.setLayoutParams(
											new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
									lRight.addView(tvSyb);
									sumWidth += tvSyb.getLayoutParams().width;
								}

								// System.out.println("tempQWidth:"+tempQWidth+"--item"+itemLL.getLayoutParams().width);
							}
						}
						break;

					case 4:// 字典格式
							// 假如左右不为空，给设置颜色

						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							// 左边没有下拉
							if (Util.isEmpty(leftList)) {
								TextView tvLeft = new TextView(NativeModeActivity.this);

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(ssLeft);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								int lenLeft = (int) paintLeft.measureText(item.leftsideWord);

								tvLeft.setLayoutParams(new LayoutParams(
										(int) (screenWidth / (q.freeTextColumn + 2) * fx), LayoutParams.WRAP_CONTENT));
								TextView tvRight = new TextView(NativeModeActivity.this);

								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();
								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								tvRight.setLayoutParams(new LayoutParams(
										(int) (screenWidth / (q.freeTextColumn + 3) * fx), LayoutParams.WRAP_CONTENT));
								lLeft.addView(tvLeft, lLeft.getChildCount());
								sumWidth += tvLeft.getLayoutParams().width;
								// 有答案取答案 赋值
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								// 余下的长度
								int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenLeft)
										+ (tempQWidth / 3 - lenRight);
								if (et.getLayoutParams().width > remainWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								sumWidth += et.getLayoutParams().width;
								lRight.addView(tvRight, lRight.getChildCount());
								sumWidth += tvRight.getLayoutParams().width;
							} else {
								/**
								 * 左边有下拉框
								 */
								String iCap = Util.getLeftCap(item.leftsideWord);
								int lenLeft = 0;
								if (!Util.isEmpty(iCap)) {
									/**
									 * @@::前面有文字
									 */
									TextView tvLeft = new TextView(NativeModeActivity.this);

									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(iCap);
									int maxLeft = (int) (screenWidth / (q.freeTextColumn + 2) * fx) ;
									if (lenLeft > maxLeft) {
										lenLeft = maxLeft;
									}
									tvLeft.setLayoutParams(
											new LayoutParams(lenLeft,
													LayoutParams.WRAP_CONTENT));

									lLeft.addView(tvLeft, lLeft.getChildCount());
									sumWidth += lenLeft;
								}

								Spinner spLeft = new Spinner(NativeModeActivity.this);

								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);

								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										if (j == i) {
											AnswerMap am = amList.get(i);
											int pos = aa.getPosition(am.getAnswerValue());
											if (-1 != pos) {
												// 选上位置

												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}

								TextView tvRight = new TextView(NativeModeActivity.this);

								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();
								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								// 左边说明不为空，右边长度小于最大长度
								tvRight.setLayoutParams(new LayoutParams(
										(int) (screenWidth / (q.freeTextColumn + 3) * fx), LayoutParams.WRAP_CONTENT));

								int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams SPSELF = new LayoutParams(spWidth, LayoutParams.WRAP_CONTENT);
								// 判断有没有前面的左描述
								if (!Util.isEmpty(iCap)) {
									// 余下的长度
									int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenLeft)
											+ (tempQWidth / 3 - lenRight);
									if (spWidth > remainWidth) {
										spLeft.setLayoutParams(
												new LayoutParams((int) (remainWidth * fx), LayoutParams.WRAP_CONTENT));
									} else {
										spLeft.setLayoutParams(SPSELF);
									}
								} else {
									int remainWidthT = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
									if (spWidth > remainWidthT) {
										spLeft.setLayoutParams(
												new LayoutParams((int) (remainWidthT * fx), LayoutParams.WRAP_CONTENT));
									} else {
										spLeft.setLayoutParams(SPSELF);
									}
								}
								lRight.addView(spLeft, lRight.getChildCount());
								sumWidth += spLeft.getLayoutParams().width;
								lRight.addView(tvRight, lRight.getChildCount());
								sumWidth += tvRight.getLayoutParams().width;
							}
						} else if (!Util.isEmpty(item.leftsideWord)) {
							// 只有左边有文字。右边没文字
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							/**
							 * 获取左边的说明文字
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							int lenLeft = 0;
							if (!Util.isEmpty(iCap)) {
								/**
								 * 左边是说明文字 右边是下拉列表框
								 */
								TextView tvLeft = new TextView(NativeModeActivity.this);

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(iCap);
								tvLeft.setLayoutParams(new LayoutParams(
										(int) (screenWidth / (q.freeTextColumn + 2) * fx), LayoutParams.WRAP_CONTENT));
								lLeft.addView(tvLeft, lLeft.getChildCount());
								sumWidth += tvLeft.getLayoutParams().width;
							} else if (Util.isEmpty(leftList)) {
								// 左边不是说明文字 。没有下拉列表框
								TextView tvLeft = new TextView(NativeModeActivity.this);

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(ssLeft);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(item.leftsideWord);
								tvLeft.setLayoutParams(new LayoutParams(
										(int) (screenWidth / (q.freeTextColumn + 2) * fx), LayoutParams.WRAP_CONTENT));
								lLeft.addView(tvLeft, lLeft.getChildCount());
								sumWidth += tvLeft.getLayoutParams().width;
							}
							/**
							 *** 假如左边不是下拉框 直接给文本框赋值。
							 */
							if (Util.isEmpty(leftList)) {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
								if (et.getLayoutParams().width > remainWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								sumWidth += et.getLayoutParams().width;
							} else {
								// 是下拉框。给下拉框赋值
								/**
								 * 大树文本框修改
								 */
								if (Util.isEmpty(iCap)) {
									Log.i("zrl1", item.leftsideWord + "左边文字");
									// 1
									TextView tvLeft = new TextView(NativeModeActivity.this);
									tvLeft.setTextSize(20);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(item.leftsideWord);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(item.leftsideWord);
									tvLeft.setLayoutParams(
											new LayoutParams((int) (screenWidth / (q.freeTextColumn + 2) * fx),
													LayoutParams.WRAP_CONTENT));
									lLeft.addView(tvLeft, lLeft.getChildCount());
									sumWidth += tvLeft.getLayoutParams().width;
									// 2
									if (!Util.isEmpty(amList)) {
										String etName = Util.GetAnswerName(q, item, 0, 0, true);
										for (AnswerMap am : amList) {
											if (etName.equals(am.getAnswerName())) {
												et.setText(am.getAnswerValue());
											}
										}
									}
									int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
									if (et.getLayoutParams().width > remainWidth) {
										int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
										LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
												LayoutParams.WRAP_CONTENT);
										et.setLayoutParams(ITSELF);
									}
									lRight.addView(et, lRight.getChildCount());
									sumWidth += et.getLayoutParams().width;

								} else {
									// 大树文本框修改
									Spinner spLeft = new Spinner(NativeModeActivity.this);
									spLeft.setTag(item);
									spLeft.setLayoutParams(WRAP_WRAP);
									// simple_spinner_item
									// R.layout.simple_spinner_dropdown_item
									ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
											R.layout.simple_spinner_adapter);
									aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									for (String str : leftList) {
										aa.add(str);
									}
									spLeft.setAdapter(aa);
									if (!Util.isEmpty(amList)) {
										for (int j = 0; j < amList.size(); j++) {
											// 通过存的value得到位置
											if (j == i) {
												AnswerMap am = amList.get(i);
												int pos = aa.getPosition(am.getAnswerValue());
												if (-1 != pos) {
													// 选上位置
													spLeft.setSelection(pos);
													break;
												}
											}
										}
									}
									int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams SPSELF = new LayoutParams((int) (spWidth * fx),
											LayoutParams.WRAP_CONTENT);
									if (!Util.isEmpty(iCap)) {
										int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
										if (spWidth > remainWidth) {
											spLeft.setLayoutParams(new LayoutParams((int) (remainWidth * fx),
													LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									} else {
										if (spWidth > tempQWidth) {
											spLeft.setLayoutParams(new LayoutParams((int) (tempQWidth * fx),
													LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									}

									lRight.addView(spLeft, lRight.getChildCount());
									sumWidth += spLeft.getLayoutParams().width;
								}

							}
						} else if (!Util.isEmpty(item.rightsideWord)) {
							// 只有右边有。左边没有
							/**
							 * 左边是文本框 右边是说明
							 */
							TextView tvRight = new TextView(NativeModeActivity.this);

							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 0);
							TextPaint paintRight = tvRight.getPaint();

							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							tvRight.setLayoutParams(new LayoutParams((int) (screenWidth / (q.freeTextColumn + 4) * fx),
									LayoutParams.WRAP_CONTENT));
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
							if (et.getLayoutParams().width > remainWidth) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());
							sumWidth += et.getLayoutParams().width;
							lRight.addView(tvRight, lRight.getChildCount());
							sumWidth += tvRight.getLayoutParams().width;
						} else {
							// 左右无,只有文本题目
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							if (et.getLayoutParams().width > (tempQWidth)) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());
							sumWidth += et.getLayoutParams().width;
						}
						// 数据字典
						// 为输入添加TextWatcher监听文字的变化
						et.setOnTouchListener(new DataTouchListener(item.classid, et));
						et.setKeyListener(null);
						break;
					}
					// switch
					// 每行多列
					// if (1 < q.freeTextColumn) {
					// LinearLayout ll = colsLL.get(i % q.freeTextColumn);
					// ll.addView(itemLL, ll.getChildCount());
					// } else {
					// // 直接加上 一行一列文本题目
					// bodyView.addView(itemLL, bodyView.getChildCount());
					// }

				}
				maxSumWidArr.add(sumWidth);
			}
			int maxSumWid = 0;
			for (int i = 0; i < maxSumWidArr.size(); i++) {
				if (maxSumWid < maxSumWidArr.get(i)) {
					maxSumWid = maxSumWidArr.get(i);
				}
			}
			if (maxCWidth < maxSumWid) {
				fx = (double)maxCWidth / (double) maxSumWid;
			}
			/**
			 * 循环取左右侧说明框的宽度
			 */
			ArrayList<Integer> leftWidAry = new ArrayList<Integer>();
			ArrayList<Integer> leftRequiredWidAry = new ArrayList<Integer>();
			ArrayList<Integer> editWidAry = new ArrayList<Integer>();
			ArrayList<Integer> rightWidAry = new ArrayList<Integer>();
			for (int i = 0; i < tbColumns.size(); i++) {
				QuestionItem item = tbColumns.get(i);
				item.itemValue = i;
				SpannableString ssLeft = null;
				if (!Util.isEmpty(item.leftsideWord)) {
					String strTilte = item.leftsideWord;

					/**
					 * 换行
					 */
					String newTitle = Util.replaceMatcherList(strTilte);
					if (!Util.isEmpty(newTitle)) {
						strTilte = newTitle;
					}

					/**
					 * 粗体
					 */
					CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
					boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
					if (!boldHave) {
						strTilte = boldMatcherList.getResultStr();
					}

					/**
					 * 加下划线
					 */
					CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

					if (!Util.isEmpty(cmUnderLine.getMis())) {
						strTilte = cmUnderLine.getResultStr();
					}

					/**
					 * font标签
					 */
					CstmMatcher cm = Util.findFontMatcherList(strTilte);
					boolean noFont = Util.isEmpty(cm.getResultStr());
					if (!noFont) {
						strTilte = cm.getResultStr();
						// System.out.println("Font之后--->"+strTilte);
					}
					int len = strTilte.length();
					if (0 < len) {

						ssLeft = new SpannableString(strTilte);
						if (!noFont) {
							for (MatcherItem mi : cm.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
										&& mi.end <= strTilte.length()) {
									ssLeft.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}
						}

						/**
						 * 加粗引用
						 */
						if (!boldHave) {
							for (MatcherItem mi : boldMatcherList.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
									ssLeft.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								// ss.setSpan(new RelativeSizeSpan(1.3f),
								// mi.start,
								// mi.end,
								// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							for (MatcherItem mi : cmUnderLine.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
									ssLeft.setSpan(new UnderlineSpan(), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

					}
				}
				SpannableString ssRight = null;
				if (!Util.isEmpty(item.rightsideWord)) {
					String strTilte = item.rightsideWord;
					/**
					 * 换行
					 */
					String newTitle = Util.replaceMatcherList(strTilte);
					if (!Util.isEmpty(newTitle)) {
						strTilte = newTitle;
					}

					/**
					 * 粗体
					 */
					CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
					boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
					if (!boldHave) {
						strTilte = boldMatcherList.getResultStr();
					}

					/**
					 * 加下划线
					 */
					CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

					if (!Util.isEmpty(cmUnderLine.getMis())) {
						strTilte = cmUnderLine.getResultStr();
					}

					/**
					 * font标签
					 */
					CstmMatcher cm = Util.findFontMatcherList(strTilte);
					boolean noFont = Util.isEmpty(cm.getResultStr());
					if (!noFont) {
						strTilte = cm.getResultStr();
						// System.out.println("Font之后--->"+strTilte);
					}
					int len = strTilte.length();
					if (0 < len) {
						ssRight = new SpannableString(strTilte);

						if (!noFont) {
							for (MatcherItem mi : cm.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
										&& mi.end <= strTilte.length()) {
									ssRight.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}
						}

						/**
						 * 加粗引用
						 */
						if (!boldHave) {
							for (MatcherItem mi : boldMatcherList.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
									ssRight.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								// ss.setSpan(new RelativeSizeSpan(1.3f),
								// mi.start,
								// mi.end,
								// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							for (MatcherItem mi : cmUnderLine.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
									ssRight.setSpan(new UnderlineSpan(), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

					}
				}
				int lenRight=0;
				if (!Util.isEmpty(item.rightsideWord)) {
					TextView tvRight = new TextView(NativeModeActivity.this);

					/**
					 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
					 */
					if (item.isHide) {
						tvRight.setVisibility(View.GONE);
					}
					tvRight.setTextSize(lowSurveySize);
					tvRight.setTextColor(Color.BLACK);
					tvRight.setText(ssRight);
					tvRight.setPadding(0, 0, 0, 0);
					TextPaint paintRight = tvRight.getPaint();
					lenRight = (int) paintRight.measureText(item.rightsideWord);
					int maxRight = (int) (screenWidth / (q.freeTextColumn + 4) * fx);
					if (lenRight > maxRight) {
						lenRight = maxRight;
					}
				}
				rightWidAry.add(lenRight);
				int leftText=0;
				if (!Util.isEmpty(item.leftsideWord)&&1!=q.qLinkage) {
					TextView tvLeft = new TextView(NativeModeActivity.this);

					/**
					 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
					 */
					if (item.isHide) {
						tvLeft.setVisibility(View.GONE);
					}
					tvLeft.setTextSize(lowSurveySize);
					tvLeft.setTextColor(Color.BLACK);
					tvLeft.setText(ssLeft);
					tvLeft.setPadding(0, 0, 0, 0);
					TextPaint paintLeft = tvLeft.getPaint();
					String textLeft = item.leftsideWord;
					if (-1 != item.leftsideWord.indexOf("@@")) {
						textLeft = item.leftsideWord.substring(0, item.leftsideWord.indexOf("@@"));
					}
					leftText = (int) paintLeft.measureText(textLeft);
					int maxLeft = (int) (screenWidth / (q.freeTextColumn + 2) * fx);
					if (leftText > maxLeft) {
						leftText = maxLeft;
					}
				}
				leftWidAry.add(leftText);
				int tvRequiredWid=0;
				if (item.required) {
					TextView tvRequired = new TextView(NativeModeActivity.this);
					tvRequired.setText(getResources().getString(R.string.notice_required));
					tvRequired.setTextColor(Color.RED);
					tvRequiredWid = (int) tvRequired.getPaint()
							.measureText(getResources().getString(R.string.notice_required));
				}
				leftRequiredWidAry.add(tvRequiredWid); 

				// 初始化每一项的edittext

				if (item.itemSize != 0) {
					int editWidth = (int) (Util.getEditWidth(item.itemSize, maxCWidth) * fx);
					editWidAry.add(editWidth);
				} else {
					int editWidth = Util.getEditWidth(40, maxCWidth);
					editWidAry.add(editWidth);
				}

			}
			// 每一列edit的最大值
			ArrayList<Integer> editWidCol = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				editWidCol.add(0);
			}
			for (int i = 0; i < editWidAry.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (editWidCol.get(j) < editWidAry.get(i)) {
							editWidCol.set(j, editWidAry.get(i));
						}
					}
				}
			}
			// 每一列左边的最大宽度
			ArrayList<Integer> leftWidCol = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftWidCol.add(0);
			}
			for (int i = 0; i < leftWidAry.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (leftWidCol.get(j) < leftWidAry.get(i)) {
							leftWidCol.set(j, leftWidAry.get(i));
						}
					}
				}
			}
			// 每一列左边必答标记最大宽度
			ArrayList<Integer> leftRequiredWidCol = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftRequiredWidCol.add(0);
			}
			for (int i = 0; i < leftWidAry.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (leftRequiredWidCol.get(j) < leftRequiredWidAry.get(i)) {
							leftRequiredWidCol.set(j, leftRequiredWidAry.get(i));
						}
					}
				}
			}
			// 每一列右边的最大宽度
			ArrayList<Integer> rightWidCol = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				rightWidCol.add(0);
			}
			for (int i = 0; i < rightWidAry.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (rightWidCol.get(j) < rightWidAry.get(i)) {
							rightWidCol.set(j, rightWidAry.get(i));
						}
					}
				}
			}
			// 每一列左侧最大总宽度
			ArrayList<Integer> leftAllWid = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftAllWid.add(leftWidCol.get(j)+leftRequiredWidCol.get(j));
			}
			// 每一列右侧最大总宽度
			ArrayList<Integer> rightAllWid = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				rightAllWid.add(rightWidCol.get(j) + editWidCol.get(j));
			}
			// 追加说明%%处理
			// 追加说明%%处理
			// 改的地方
			if (!Util.isEmpty(q.qCaption)) {
				// tvRequired.setTextSize(15);
				if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
					// 得到每一个项
					QuestionItem item = tbColumns.get(0);
					// 判断出生成几个小%%%%追加说明
					String[] tvCount = q.qCaption.split("%%");
					LinearLayout tbr = new LinearLayout(NativeModeActivity.this);
					tbr.setOrientation(LinearLayout.HORIZONTAL);
					tbr.setGravity(Gravity.CENTER_VERTICAL);
					ArrayList<LinearLayout> CapLary = new ArrayList<LinearLayout>();// 每行的TableRow对象
					for (int col = 0; col < tvCount.length; col++) {
						LinearLayout capItem = new LinearLayout(NativeModeActivity.this);
						capItem.setOrientation(LinearLayout.VERTICAL);
						capItem.setGravity(Gravity.CENTER_HORIZONTAL);
						if (0 == col % 2) {// 左侧表头
							if (col / 2 < q.freeTextColumn) {
								LinearLayout.LayoutParams capItemPar = new LinearLayout.LayoutParams(
										leftAllWid.get(col / 2) + 2, LayoutParams.WRAP_CONTENT);
								capItemPar.setMargins(1, 1, 1, 1);
								capItem.setLayoutParams(capItemPar);
							}
						} else {
							if (col / 2 < q.freeTextColumn) {
								LinearLayout.LayoutParams capItemPar = new LinearLayout.LayoutParams(
										rightAllWid.get(col / 2) + 2, LayoutParams.WRAP_CONTENT);
								capItemPar.setMargins(1, 1, 1, 1);
								capItem.setLayoutParams(capItemPar);
							}
						}
						CapLary.add(capItem);
						tbr.addView(capItem, tbr.getChildCount());
					}
					tl.addView(tbr, 0);
					for (int tv = 0; tv < tvCount.length; tv++) {
						LinearLayout itemLary = CapLary.get(tv);
						TextView tvSmallCaption = new TextView(this);
						String strTvCaption = tvCount[tv];
						tvSmallCaption.setTextColor(Color.BLACK);
						tvSmallCaption.setTextSize(lowSurveySize);
						TextPaint paint = tvSmallCaption.getPaint();
						paint.setFakeBoldText(true);
						tvSmallCaption.setLayoutParams(WRAP_WRAP);
						tvSmallCaption.setText(strTvCaption);
						itemLary.addView(tvSmallCaption);
					}
				}
			}

			// 结束 事件
			if (!Util.isEmpty(q.qCaption)) {
				if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
					tvCaption.setText("");
				}
			}
			for (int i = 0; i < tbColumns.size(); i++) {
				if (1 < q.freeTextColumn) {
					QuestionItem itemNull = tbColumns.get(0);
					String leftsideWord = itemNull.getLeftsideWord();
					if (!Util.isEmpty(leftsideWord)) {
						if (0 == i % q.freeTextColumn) {
							tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1) * 2;
						} else {
							tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1);
						}
					}
				}
				// 得到每一个项
				QuestionItem item = tbColumns.get(i);
				item.itemValue = i;
				ArrayList<LinearLayout> itemLary = colsLLy.get(i);
				LinearLayout lLeft = itemLary.get(0);
				// 设置每项的左侧布局
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						LinearLayout.LayoutParams lLeftPar = new LinearLayout.LayoutParams(leftAllWid.get(j),
								LayoutParams.MATCH_PARENT);
						if (1 < q.freeTextColumn) {
							lLeftPar.setMargins(1, 1, 1, 1);
						}
						lLeft.setLayoutParams(lLeftPar);
					}
				}
				lLeft.setBackgroundColor(getResources().getColor(R.color.white));
				lLeft.removeAllViews();
				LinearLayout lRight = itemLary.get(1);
				// 设置每项的右侧布局
				if(item.dragChecked){
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							LinearLayout.LayoutParams lRightPar =new LinearLayout.LayoutParams(
									screenWidth/q.freeTextColumn-leftAllWid.get(j), LayoutParams.MATCH_PARENT);
							if (1 < q.freeTextColumn) {
								lRightPar.setMargins(1, 1, 1, 1);
							}
							lRight.setLayoutParams(lRightPar);
						}
					}
				}else{
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							LinearLayout.LayoutParams lRightPar = new LinearLayout.LayoutParams(
									editWidCol.get(j) + rightWidCol.get(j), LayoutParams.MATCH_PARENT);
							if (1 < q.freeTextColumn) {
								lRightPar.setMargins(1, 1, 1, 1);
							}
							lRight.setLayoutParams(lRightPar);
						}
					}
				}
				lRight.setBackgroundColor(getResources().getColor(R.color.white));
				lRight.removeAllViews();
				
				// LayoutParams lp = new LayoutParams(100,
				// LayoutParams.WRAP_CONTENT);
				/**
				 * 题外关联 之 内部关联 判断 字段 并进行 界面 设定 宽度 大树 内部关联 6
				 */
				// itemLL.setBackgroundColor(Color.BLUE);
				// itemLL.setPadding(5, 5, 20, 5);
				if (item.required) {
					TextView tvRequired = new TextView(NativeModeActivity.this);

					/**
					 * 题外关联 之 显示 隐藏 选项 显示 必填 隐藏起来 大树 显示关联 2
					 */

					if (item.isHide) {
						tvRequired.setVisibility(View.GONE);
					}

					LayoutParams myParams = new LayoutParams(WRAP_WRAP);
					// myParams.addRule(RelativeLayout.CENTER_VERTICAL,
					// itemLL.getId());
					// myParams.setMargins(0, 0, 6, 0);
					tvRequired.setLayoutParams(myParams);
					tvRequired.setText(getResources().getString(R.string.notice_required));
					tvRequired.setTextColor(Color.RED);
					int tvRequiredWid = (int) tvRequired.getPaint()
							.measureText(getResources().getString(R.string.notice_required));
					tempQWidth -= tvRequiredWid;
					// tvRequired.setTextSize(15);
					lLeft.addView(tvRequired, lLeft.getChildCount());
				}
				// 初始化每一项的edittext
				EditText et = new EditText(NativeModeActivity.this);

				/**
				 * 题外关联 显示 的 设计 在这 显示几个选项
				 */

				if (item.isHide) {
					et.setVisibility(View.GONE);
				}

				if (item.itemSize != 0) {
					int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
					LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx), LayoutParams.WRAP_CONTENT);
					et.setLayoutParams(ITSELF);
				} else {
					int editWidth = Util.getEditWidth(20, maxCWidth);
					LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx), LayoutParams.WRAP_CONTENT);
					et.setLayoutParams(ITSELF);
				}
				// et.setMinimumWidth(150);
				et.setTextSize(lowSurveySize);
				et.setTag(item);
				// 左边样式
				SpannableString ssLeft = null;
				if (!Util.isEmpty(item.leftsideWord)) {
					String strTilte = item.leftsideWord;

					/**
					 * 换行
					 */
					String newTitle = Util.replaceMatcherList(strTilte);
					if (!Util.isEmpty(newTitle)) {
						strTilte = newTitle;
					}

					/**
					 * 粗体
					 */
					CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
					boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
					if (!boldHave) {
						strTilte = boldMatcherList.getResultStr();
					}

					/**
					 * 加下划线
					 */
					CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

					if (!Util.isEmpty(cmUnderLine.getMis())) {
						strTilte = cmUnderLine.getResultStr();
					}

					/**
					 * font标签
					 */
					CstmMatcher cm = Util.findFontMatcherList(strTilte);
					boolean noFont = Util.isEmpty(cm.getResultStr());
					if (!noFont) {
						strTilte = cm.getResultStr();
						// System.out.println("Font之后--->"+strTilte);
					}
					int len = strTilte.length();
					if (0 < len) {

						ssLeft = new SpannableString(strTilte);
						if (!noFont) {
							for (MatcherItem mi : cm.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
										&& mi.end <= strTilte.length()) {
									ssLeft.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}
						}

						/**
						 * 加粗引用
						 */
						if (!boldHave) {
							for (MatcherItem mi : boldMatcherList.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
									ssLeft.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								// ss.setSpan(new RelativeSizeSpan(1.3f),
								// mi.start,
								// mi.end,
								// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							for (MatcherItem mi : cmUnderLine.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
									ssLeft.setSpan(new UnderlineSpan(), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

					}
				}
				// 右边样式
				SpannableString ssRight = null;
				if (!Util.isEmpty(item.rightsideWord)) {
					String strTilte = item.rightsideWord;
					/**
					 * 换行
					 */
					String newTitle = Util.replaceMatcherList(strTilte);
					if (!Util.isEmpty(newTitle)) {
						strTilte = newTitle;
					}

					/**
					 * 粗体
					 */
					CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
					boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
					if (!boldHave) {
						strTilte = boldMatcherList.getResultStr();
					}

					/**
					 * 加下划线
					 */
					CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

					if (!Util.isEmpty(cmUnderLine.getMis())) {
						strTilte = cmUnderLine.getResultStr();
					}

					/**
					 * font标签
					 */
					CstmMatcher cm = Util.findFontMatcherList(strTilte);
					boolean noFont = Util.isEmpty(cm.getResultStr());
					if (!noFont) {
						strTilte = cm.getResultStr();
						// System.out.println("Font之后--->"+strTilte);
					}
					int len = strTilte.length();
					if (0 < len) {
						ssRight = new SpannableString(strTilte);

						if (!noFont) {
							for (MatcherItem mi : cm.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
										&& mi.end <= strTilte.length()) {
									ssRight.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}
						}

						/**
						 * 加粗引用
						 */
						if (!boldHave) {
							for (MatcherItem mi : boldMatcherList.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
									ssRight.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								// ss.setSpan(new RelativeSizeSpan(1.3f),
								// mi.start,
								// mi.end,
								// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							for (MatcherItem mi : cmUnderLine.getMis()) {
								if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
									ssRight.setSpan(new UnderlineSpan(), mi.start, mi.end,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

					}
				}
				/**
				 * 2.其次问是什么类型的题目
				 */

				switch (item.type) {// switch
				/**
				 * 3.第三要询问左右两边是否都有字符串, 一共有3中情况:左右有、只左有、只右有、左右无
				 */
				case 0:
					// 维码扫描
				case 6:
					System.out.println("q.qLinkage:" + q.qLinkage);
					// 三级联动判断
					if (1 == q.qLinkage) {
						/**
						 * 需要加一个字段 来 做 判断 是否是三级联动 三级联动 的话 必须type 是none 现在测试
						 */
						if (!Util.isEmpty(item.leftsideWord)) {
							// Log.i("zrl1", item.leftsideWord);
							if (i == 0) {
								cityPos = 0;// 清0
								s1 = item.leftsideWord;
								provinceSpinner = new Spinner(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 3
								 */
								if (item.isHide) {
									provinceSpinner.setVisibility(View.GONE);
								}

								city = ThreeLeverUtil.getFirstList(s1);
								provinceAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter, city);// list是一个
								provinceAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								provinceSpinner.setAdapter(provinceAdapter);
								/**
								 * 获取 默认的答案
								 */
								if (!Util.isEmpty(amList)) {
									AnswerMap am1 = amList.get(0);
									cityPos = provinceAdapter.getPosition(am1.getAnswerValue());
									// 三级联动更改 处理更新问卷的操作,原因是找不到值
									if (-1 == cityPos) {
										cityPos = 0;
									}
									provinceSpinner.setSelection(cityPos, true);
								} else {
									provinceSpinner.setSelection(0, true);
								}
								provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(AdapterView<?> parent, View view, int position,
											long id) {
										// TODO Auto-generated method stub
										if (vs.size() >= 2) {
											Spinner spinner = (Spinner) vs.get(1);
											QuestionItem item = (QuestionItem) spinner.getTag();
											setCityAdapter(position, item);
										}

									}

									@Override
									public void onNothingSelected(AdapterView<?> parent) {
										// TODO Auto-generated method stub

									}
								});
								provinceSpinner.setTag(item);
								TextView tvLeft = new TextView(NativeModeActivity.this);
								String iCap = Util.getLeftCap(item.leftsideWord);
								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								int lenLeft = (int) paintLeft.measureText(iCap);
								if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
									lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
								}
								tvLeft.setLayoutParams(
										new LayoutParams(lenLeft,
												LayoutParams.WRAP_CONTENT));
								lLeft.addView(tvLeft, lLeft.getChildCount());
								lRight.addView(provinceSpinner, lRight.getChildCount());
								vs.add(provinceSpinner);
							} else if (i == 1) {
								areaPos = 0;// 清0
								s2 = item.leftsideWord;
								citySpinner = new Spinner(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 4
								 */
								if (item.isHide) {
									citySpinner.setVisibility(View.GONE);
								}

								area = ThreeLeverUtil.getSecondList(s2);
								System.out.println("cityPos:" + cityPos);
								areaListTemp = ThreeLeverUtil.getCityPosList(area, city, cityPos);
								if (areaListTemp.size() == 0) {
									areaListTemp.add("空");
								}
								cityAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter,
										areaListTemp);// list是一个
								cityAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								citySpinner.setAdapter(cityAdapter);
								citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(AdapterView<?> parent, View view, int position,
											long id) {
										// TODO Auto-generated method stub
										if (vs.size() >= 3) {
											Spinner spinner = (Spinner) vs.get(2);
											QuestionItem item = (QuestionItem) spinner.getTag();
											setCountryAdapter(position, item);
										}

									}

									@Override
									public void onNothingSelected(AdapterView<?> parent) {
										// TODO Auto-generated method stub

									}
								});
								/**
								 * 获取原有答案
								 */
								if (!Util.isEmpty(amList)) {

									AnswerMap am1 = amList.get(1);
									areaPos = cityAdapter.getPosition(am1.getAnswerValue());
									// 三级联动更改 处理更新问卷的操作,原因是找不到值
									if (-1 == areaPos) {
										areaPos = 0;
									}
									citySpinner.setSelection(areaPos, true);
								} else {
									citySpinner.setSelection(0, true);
								}
								citySpinner.setTag(item);
								TextView tvLeft = new TextView(NativeModeActivity.this);
								String iCap = Util.getLeftCap(item.leftsideWord);
								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								int lenLeft = (int) paintLeft.measureText(iCap);
								if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
									lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
								}
								tvLeft.setLayoutParams(
										new LayoutParams(lenLeft,
												LayoutParams.WRAP_CONTENT));
								lLeft.addView(tvLeft, lLeft.getChildCount());
								lRight.addView(citySpinner, lRight.getChildCount());
								vs.add(citySpinner);

							} else if (i == 2) {
								s3 = item.leftsideWord;
								countrySpinner = new Spinner(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 5
								 */
								if (item.isHide) {
									countrySpinner.setVisibility(View.GONE);
								}
								way = ThreeLeverUtil.getThridList(s3);
								wayListTemp = ThreeLeverUtil.getAreaPosList(areaListTemp, way, areaPos);
								if (wayListTemp.size() == 0) {
									wayListTemp.add("空");
								}

								countryAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter,
										wayListTemp);// list是一个
								countryAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								countrySpinner.setAdapter(countryAdapter);
								/**
								 * 获取 原有 答案
								 */
								if (!Util.isEmpty(amList)) {

									AnswerMap am1 = amList.get(2);
									int pos = countryAdapter.getPosition(am1.getAnswerValue());
									// 三级联动更改 处理更新问卷的操作,原因是找不到值
									if (-1 == pos) {
										pos = 0;
									}
									countrySpinner.setSelection(pos, true);
								} else {
									countrySpinner.setSelection(0, true);
								}
								TextView tvLeft = new TextView(NativeModeActivity.this);
								String iCap = Util.getLeftCap(item.leftsideWord);
								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								int lenLeft = (int) paintLeft.measureText(iCap);
								if(lenLeft>(int) (screenWidth / (q.freeTextColumn + 2) * fx)){
									lenLeft=(int) (screenWidth / (q.freeTextColumn + 2) * fx);
								}
								tvLeft.setLayoutParams(
										new LayoutParams(lenLeft,
												LayoutParams.WRAP_CONTENT));
								lLeft.addView(tvLeft, lLeft.getChildCount());
								lRight.addView(countrySpinner, lRight.getChildCount());
								countrySpinner.setTag(item);
								vs.add(countrySpinner);
							}
						}

					} else {
						// 非三级联动
						// 典型的文本
						// 左右有文本值
						// 假如左右不为空，给设置颜色
						ImageView iv = new ImageView(NativeModeActivity.this);

						/**
						 * 题外关联--- 显示关联 大树 显示关联 6
						 */
						if (item.isHide) {
							iv.setVisibility(View.GONE);
						}
						// 维码扫描 item.scanning
						if (6 == item.type) {
							iv = new ImageView(NativeModeActivity.this);
							RelativeLayout.LayoutParams myParams = WRAP_WRAP;
							String idStr = q.qIndex + "_" + i;
							iv.setId(idStr.hashCode());
							iv.setLayoutParams(myParams);
							iv.setBackgroundResource(R.drawable.icon_scanning);
							iv.setOnClickListener(new ScanningListener(et));
						}
						// 维码扫描

						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							// 左边没有下拉
							if (Util.isEmpty(leftList)) {
								TextView tvLeft = new TextView(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(ssLeft);
								tvLeft.setPadding(0, 0, 0, 8);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvLeft.setLayoutParams(
												new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								TextView tvRight = new TextView(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 8
								 */
								if (item.isHide) {
									tvRight.setVisibility(View.GONE);
								}

								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvRight.setLayoutParams(
												new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
								// 有答案取答案 赋值
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
								lRight.addView(et, lRight.getChildCount());
								lRight.addView(tvRight, lRight.getChildCount());
								vs.add(et);
							} else {
								/**
								 * 左边有下拉框
								 */
								String iCap = Util.getLeftCap(item.leftsideWord);
								int lenLeft = 0;
								if (!Util.isEmpty(iCap)) {
									/**
									 * @@::前面有文字
									 */
									TextView tvLeft = new TextView(NativeModeActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 9
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(iCap);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
								}

								Spinner spLeft = new Spinner(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 10
								 */
								if (item.isHide) {
									spLeft.setVisibility(View.GONE);
								}

								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);

								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										if (j == i) {
											AnswerMap am = amList.get(i);
											int pos = aa.getPosition(am.getAnswerValue());
											if (-1 != pos) {
												// 选上位置

												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}

								TextView tvRight = new TextView(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 11
								 */
								if (item.isHide) {
									tvRight.setVisibility(View.GONE);
								}

								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();
								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								// 左边说明不为空，右边长度小于最大长度
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvRight.setLayoutParams(
												new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams SPSELF = new LayoutParams((int) (spWidth * fx), LayoutParams.WRAP_CONTENT);
								// 判断有没有前面的左描述
								spLeft.setLayoutParams(SPSELF);
								lRight.addView(spLeft, lRight.getChildCount());
								lRight.addView(tvRight, lRight.getChildCount());
								vs.add(spLeft);
							}
						} else if (!Util.isEmpty(item.leftsideWord)) {
							// 只有左边有文字。右边没文字
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							/**
							 * 获取左边的说明文字
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							int lenLeft = 0;
							if (!Util.isEmpty(iCap)) {
								/**
								 * 左边是说明文字 右边是下拉列表框
								 */
								TextView tvLeft = new TextView(NativeModeActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 12
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(iCap);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvLeft.setLayoutParams(
												new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
							} else if (Util.isEmpty(leftList)) {
								// 左边不是说明文字 。没有下拉列表框
								TextView tvLeft = new TextView(NativeModeActivity.this);
								/**
								 * 题外关联--- 显示关联 大树 显示关联 13
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(ssLeft);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(item.leftsideWord);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvLeft.setLayoutParams(
												new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
							}
							/**
							 *** 假如左边不是下拉框 直接给文本框赋值。
							 */
							if (Util.isEmpty(leftList)) {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
								if (et.getLayoutParams().width > remainWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								vs.add(et);
							} else {
								Spinner spLeft = new Spinner(NativeModeActivity.this);
								/**
								 * 题外关联--- 显示关联 大树 显示关联 14
								 */
								if (item.isHide) {
									spLeft.setVisibility(View.GONE);
								}
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);
								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										if (j == i) {
											AnswerMap am = amList.get(i);
											int pos = aa.getPosition(am.getAnswerValue());
											if (-1 != pos) {
												// 选上位置
												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}
								int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams SPSELF = new LayoutParams((int) (spWidth * fx), LayoutParams.WRAP_CONTENT);

								spLeft.setLayoutParams(SPSELF);

								lRight.addView(spLeft, lRight.getChildCount());
								vs.add(spLeft);

							}
						} else if (!Util.isEmpty(item.rightsideWord)) {
							// 只有右边有。左边没有
							/**
							 * 左边是文本框 右边是说明
							 */
							TextView tvRight = new TextView(NativeModeActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 15
							 */
							if (item.isHide) {
								tvRight.setVisibility(View.GONE);
							}
							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();

							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(
											new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
							if (et.getLayoutParams().width > remainWidth) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());
							lRight.addView(tvRight, lRight.getChildCount());
							vs.add(et);
						} else {
							// 左右无,只有文本题目
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							if (et.getLayoutParams().width > (tempQWidth)) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());
							vs.add(et);
						}
						// 维码
						if (6 == item.type) {
							lRight.addView(iv, lRight.getChildCount());
						}
					}
					break;

				case 1:// 日期格式
				case 2:// 数字格式
				case 3:// 英文/数字格式
				case 5:// 邮件格式
					if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
						// 左右有文字说明
						TextView tvLeft = new TextView(NativeModeActivity.this);

						/**
						 * 题外关联 之显示 隐藏 左边文字
						 */

						if (item.isHide) {
							tvLeft.setVisibility(View.GONE);
						}

						tvLeft.setTextSize(lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						tvLeft.setText(ssLeft);
						tvLeft.setPadding(0, 0, 0, 8);
						TextPaint paintLeft = tvLeft.getPaint();
						int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvLeft.setLayoutParams(new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
							}
						}
						if (1 == item.type) {
							// et.setMinWidth(240);
							Drawable d = getResources().getDrawable(R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
									d, //
									null);
							et.setOnTouchListener(
									new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
						} else if (2 == item.type) {// 数字
							if (item.isFloat) {
								et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							} else {
								et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							}
							if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
							} else if (!Util.isEmpty(item.minNumber)) {
								et.setHint(this.getString(R.string.edit_min, item.minNumber));
							} else if (!Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_max, item.maxNumber));
							}
						} else if (3 == item.type) {// 英文/数字
							et.setInputType(InputType.TYPE_CLASS_TEXT);
						} else if (5 == item.type) {// 邮件
							et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							et.setHint(this.getString(R.string.please_input_email));
						}
						TextView tvRight = new TextView(NativeModeActivity.this);

						/**
						 * 题外关联 之 显示 隐藏 选项
						 */
						if (item.isHide) {
							tvRight.setVisibility(View.GONE);
						}

						tvRight.setTextSize(lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(ssRight);
						tvRight.setPadding(0, 0, 0, 8);
						TextPaint paintRight = tvRight.getPaint();
						int lenRight = (int) paintRight.measureText(item.rightsideWord);
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvRight.setLayoutParams(
										new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
							}
						}
						lLeft.addView(tvLeft, lLeft.getChildCount());
						// 假如是滑动条,且是数字题目。显示滑动条。
						if (item.dragChecked && 2 == item.type) {
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
							SeekBar sb = new SeekBar(NativeModeActivity.this);
							sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
							sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
							sb.setTag(item);
							// sb.setLayoutParams(new LayoutParams(3 *
							// dis.getWidth() / 4, //
							// LayoutParams.WRAP_CONTENT));
							// sb.setPadding(5, 0, 5, 15);
							// sb.setMinimumWidth(400);
							int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenRight) + (tempQWidth / 3 - lenLeft);
							sb.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
							sb.setMax(Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber));
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										if (!Util.isEmpty(am.getAnswerValue())) {
											sb.setProgress(Integer.parseInt(am.getAnswerValue()));
										}
									}
								}
							}
							sb.setThumbOffset(0);
							TextView tvSeekTop = new TextView(NativeModeActivity.this);
							tvSeekTop.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
							// tvSeekTop.setLayoutParams(FILL_WRAP);

							tvSeekTop.setTextSize(lowSurveySize);
							tvSeekTop.setGravity(Gravity.CENTER);
							tvSeekTop.setTextColor(Color.BLUE);

							/**
							 * 只有滚动条
							 */
							tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");
							LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							// rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvSeekTop, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvSeekTop, null));
							lRight.addView(rightLL, lRight.getChildCount());
							vs.add(sb);
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
						} else {
							// 不是情况直接显示文本题目

							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenRight) + (tempQWidth / 3 - lenLeft);
							if (et.getLayoutParams().width > remainWidth) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());
							vs.add(et);
						}
						lRight.addView(tvRight, lRight.getChildCount());

						/**
						 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 7
						 */
						if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {

							TextView tvSyb = new TextView(NativeModeActivity.this);
							tvSyb.setLayoutParams(WRAP_WRAP);
							// tvSyb.setLayoutParams(new
							// LayoutParams(100,LayoutParams.WRAP_CONTENT));
							tvSyb.setTextColor(Color.RED);
							tvSyb.setTextSize(16);

							/**
							 * 在这里加一个提示 题外关联 显示的 提示 语 ！
							 */
							tvSyb.setText("(" + this.getString(R.string.sum) + ")");
							TextPaint paintSyb = tvSyb.getPaint();
							int lenSyb = (int) paintSyb.measureText(item.leftsideWord);
							tvSyb.setLayoutParams(new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
							lRight.addView(tvSyb);
						}

					} else if (!Util.isEmpty(item.leftsideWord)) {
						// 只左有
						TextView tvLeft = new TextView(NativeModeActivity.this);
						/**
						 * 题外关联 之显示 隐藏 选项 左边文字
						 */
						if (item.isHide) {
							tvLeft.setVisibility(View.GONE);

						}
						tvLeft.setTextSize(lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						tvLeft.setText(ssLeft);
						tvLeft.setPadding(0, 0, 0, 8);
						TextPaint paintLeft = tvLeft.getPaint();
						int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvLeft.setLayoutParams(new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
							}
						}
						if (1 == item.type) {
							// et.setMinWidth(240);
							Drawable d = getResources().getDrawable(R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
									d, //
									null);
							et.setOnTouchListener(
									new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
						} else if (2 == item.type) {// 数字
							if (item.isFloat) {
								et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							} else {
								et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							}
							if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
							} else if (!Util.isEmpty(item.minNumber)) {
								et.setHint(this.getString(R.string.edit_min, item.minNumber));
							} else if (!Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_max, item.maxNumber));
							}
						} else if (3 == item.type) {// 英文/数字
							et.setInputType(InputType.TYPE_CLASS_TEXT);
						} else if (5 == item.type) {// 邮件
							et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							et.setHint(this.getString(R.string.please_input_email));
						}

						// 假如是滑动条,且是数字题目。显示滑动条。
						lLeft.addView(tvLeft, lLeft.getChildCount());
						if (item.dragChecked && 2 == item.type) {
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
							SeekBar sb = new SeekBar(NativeModeActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 16
							 */
							if (item.isHide) {
								sb.setVisibility(View.GONE);
							}

							sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
							sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
							sb.setTag(item);
							// sb.setLayoutParams(new LayoutParams(3 *
							// dis.getWidth() / 4, //
							// LayoutParams.WRAP_CONTENT));
							// sb.setPadding(5, 0, 5, 15);
							// sb.setMinimumWidth(400);
							int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
							sb.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
							// sb.set
							sb.setMax(Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber));
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										if (!Util.isEmpty(am.getAnswerValue())) {
											sb.setProgress(Integer.parseInt(am.getAnswerValue()));
										}
									}
								}
							}
							sb.setThumbOffset(0);

							TextView tvSeekTop = new TextView(NativeModeActivity.this);
							// tvSeekTop.setLayoutParams(FILL_WRAP);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 17
							 */
							if (item.isHide) {
								tvSeekTop.setVisibility(View.GONE);
							}
							tvSeekTop.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
							tvSeekTop.setTextSize(lowSurveySize);
							tvSeekTop.setGravity(Gravity.CENTER);
							tvSeekTop.setTextColor(Color.BLUE);

							/**
							 * 只有滚动条
							 */
							tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

							LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							// rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvSeekTop, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvSeekTop, null));
							lRight.addView(rightLL, lRight.getChildCount());
							vs.add(sb);
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
						} else {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
							if (et.getLayoutParams().width > remainWidth) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());

							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 8
							 */
							if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
								TextView tvSyb = new TextView(NativeModeActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								// tvSyb.setLayoutParams(new
								// LayoutParams(150,
								// LayoutParams.WRAP_CONTENT));
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(16);
								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("(" + this.getString(R.string.sum) + ")");
								TextPaint paintSyb = tvSyb.getPaint();
								int lenSyb = (int) paintSyb.measureText(item.leftsideWord);
								tvSyb.setLayoutParams(new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
								lRight.addView(tvSyb, lRight.getChildCount());
							}

							vs.add(et);
						}
					} else if (!Util.isEmpty(item.rightsideWord)) {
						if (1 == item.type) {// 日期
							// et.setMinWidth(240);
							Drawable d = getResources().getDrawable(R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
									d, //
									null);
							et.setOnTouchListener(
									new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
						} else if (2 == item.type) {// 数字
							if (item.isFloat) {
								et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							} else {
								et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							}
							if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
							} else if (!Util.isEmpty(item.minNumber)) {
								et.setHint(this.getString(R.string.edit_min, item.minNumber));
							} else if (!Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_max, item.maxNumber));
							}
						} else if (3 == item.type) {// 英文/数字
							et.setInputType(InputType.TYPE_CLASS_TEXT);
						} else if (5 == item.type) {// 邮件
							et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							et.setHint(this.getString(R.string.please_input_email));
						}
						TextView tvRight = new TextView(NativeModeActivity.this);

						/**
						 * 题外关联 之显示 隐藏 选项 右边文字
						 */
						if (item.isHide) {
							tvRight.setVisibility(View.GONE);
						}
						tvRight.setTextSize(lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(ssRight);
						tvRight.setPadding(0, 0, 0, 8);
						TextPaint paintRight = tvRight.getPaint();
						int lenRight = (int) paintRight.measureText(item.rightsideWord);
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvRight.setLayoutParams(
										new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
							}
						}
						if (item.dragChecked && 2 == item.type) {
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
							SeekBar sb = new SeekBar(NativeModeActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 18
							 */
							if (item.isHide) {
								sb.setVisibility(View.GONE);
							}

							sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
							sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
							sb.setTag(item);
							// sb.setLayoutParams(new LayoutParams(3 *
							// dis.getWidth() / 4, //
							// LayoutParams.WRAP_CONTENT));
							// sb.setPadding(5, 0, 5, 15);
							// sb.setMinimumWidth(400);
							int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
							sb.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
							// sb.set
							sb.setMax(Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber));
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										if (!Util.isEmpty(am.getAnswerValue())) {
											sb.setProgress(Integer.parseInt(am.getAnswerValue()));
										}
									}
								}
							}
							sb.setThumbOffset(0);
							TextView tvSeekTop = new TextView(NativeModeActivity.this);
							// tvSeekTop.setLayoutParams(FILL_WRAP);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 19
							 */
							if (item.isHide) {
								tvSeekTop.setVisibility(View.GONE);
							}

							tvSeekTop.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
							tvSeekTop.setTextSize(lowSurveySize);
							tvSeekTop.setGravity(Gravity.CENTER);
							tvSeekTop.setTextColor(Color.BLUE);

							/**
							 * 只有滚动条
							 */
							tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

							LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							// rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvSeekTop, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvSeekTop, null));
							lRight.addView(rightLL, lRight.getChildCount());

							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 9
							 */
							if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
								TextView tvSyb = new TextView(NativeModeActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(16);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("(" + this.getString(R.string.sum) + ")");
								TextPaint paintLeft = tvSyb.getPaint();
								int lenSyb = (int) paintLeft.measureText(item.leftsideWord);
								tvSyb.setLayoutParams(new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
								lRight.addView(tvSyb);
							}

							vs.add(sb);
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
						} else {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
							if (et.getLayoutParams().width > remainWidth) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());
							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 10
							 */
							if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
								TextView tvSyb = new TextView(NativeModeActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(16);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("(" + this.getString(R.string.sum) + ")");
								lRight.addView(tvSyb);
							}

							vs.add(et);
						}

						lRight.addView(tvRight, lRight.getChildCount());

					} else {
						// 左右无
						if (1 == item.type) {// 日期
							// et.setMinWidth(240);
							Drawable d = getResources().getDrawable(R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
									d, //
									null);
							et.setOnTouchListener(
									new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
						} else if (2 == item.type) {// 数字
							if (item.isFloat) {
								et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							} else {
								et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							}
							if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
							} else if (!Util.isEmpty(item.minNumber)) {
								et.setHint(this.getString(R.string.edit_min, item.minNumber));
							} else if (!Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_max, item.maxNumber));
							}
						} else if (3 == item.type) {// 英文/数字
							et.setInputType(InputType.TYPE_CLASS_TEXT);
						} else if (5 == item.type) {// 邮件
							et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							et.setHint(this.getString(R.string.please_input_email));
						}

						if (item.dragChecked && 2 == item.type) {
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
							SeekBar sb = new SeekBar(NativeModeActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 20
							 */
							if (item.isHide) {
								sb.setVisibility(View.GONE);
							}

							sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
							sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
							sb.setTag(item);
							sb.setLayoutParams(new LayoutParams(9 * tempQWidth / 10, //
									LayoutParams.WRAP_CONTENT));
							sb.setPadding(5, 0, 5, 15);
							// sb.setMinimumWidth(400);
							// sb.set
							sb.setMax(Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber));
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										if (!Util.isEmpty(am.getAnswerValue())) {
											sb.setProgress(Integer.parseInt(am.getAnswerValue()));
										}
									}
								}
							}
							sb.setThumbOffset(0);
							TextView tvSeekTop = new TextView(NativeModeActivity.this);
							/**
							 * 题外关联--- 显示关联 大树 显示关联 21
							 */
							if (item.isHide) {
								tvSeekTop.setVisibility(View.GONE);
							}

							tvSeekTop.setLayoutParams(FILL_WRAP);
							tvSeekTop.setTextSize(lowSurveySize);
							tvSeekTop.setGravity(Gravity.CENTER);
							tvSeekTop.setTextColor(Color.BLUE);

							/**
							 * 只有滚动条
							 */
							tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

							LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvSeekTop, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvSeekTop, null));
							lRight.addView(rightLL, lRight.getChildCount());
							vs.add(sb);
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
						} else {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							if (et.getLayoutParams().width > tempQWidth) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}

							lRight.addView(et, lRight.getChildCount());

							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 11
							 */
							if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
								TextView tvSyb = new TextView(NativeModeActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(16);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("(" + this.getString(R.string.sum) + ")");
								TextPaint paintLeft = tvSyb.getPaint();
								int lenRight = (int) paintLeft.measureText(item.leftsideWord);
								tvSyb.setLayoutParams(
										new LayoutParams((int) (lenRight * fx), LayoutParams.WRAP_CONTENT));

								lRight.addView(tvSyb);
							}

							// System.out.println("tempQWidth:"+tempQWidth+"--item"+itemLL.getLayoutParams().width);
							vs.add(et);
						}
					}
					break;

				case 4:// 字典格式
						// 假如左右不为空，给设置颜色

					if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
						// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
						ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
						// 左边没有下拉
						if (Util.isEmpty(leftList)) {
							TextView tvLeft = new TextView(NativeModeActivity.this);

							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(ssLeft);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvLeft.setLayoutParams(
											new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							TextView tvRight = new TextView(NativeModeActivity.this);

							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(
											new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							lLeft.addView(tvLeft, lLeft.getChildCount());
							// 有答案取答案 赋值
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							// 余下的长度
							int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenLeft) + (tempQWidth / 3 - lenRight);
							if (et.getLayoutParams().width > remainWidth) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());
							lRight.addView(tvRight, lRight.getChildCount());
							vs.add(et);
						} else {
							/**
							 * 左边有下拉框
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							int lenLeft = 0;
							if (!Util.isEmpty(iCap)) {
								/**
								 * @@::前面有文字
								 */
								TextView tvLeft = new TextView(NativeModeActivity.this);

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(iCap);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvLeft.setLayoutParams(
												new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
							}

							Spinner spLeft = new Spinner(NativeModeActivity.this);

							spLeft.setTag(item);
							spLeft.setLayoutParams(WRAP_WRAP);
							// simple_spinner_item
							// R.layout.simple_spinner_dropdown_item
							ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
									R.layout.simple_spinner_adapter);
							aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
							for (String str : leftList) {
								aa.add(str);
							}
							spLeft.setAdapter(aa);

							if (!Util.isEmpty(amList)) {
								for (int j = 0; j < amList.size(); j++) {
									// 通过存的value得到位置
									if (j == i) {
										AnswerMap am = amList.get(i);
										int pos = aa.getPosition(am.getAnswerValue());
										if (-1 != pos) {
											// 选上位置

											spLeft.setSelection(pos);
											break;
										}
									}
								}
							}

							TextView tvRight = new TextView(NativeModeActivity.this);

							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							// 左边说明不为空，右边长度小于最大长度
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(
											new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
							LayoutParams SPSELF = new LayoutParams((int) (spWidth * fx), LayoutParams.WRAP_CONTENT);
							// 判断有没有前面的左描述

							spLeft.setLayoutParams(SPSELF);
							lRight.addView(spLeft, lRight.getChildCount());
							lRight.addView(tvRight, lRight.getChildCount());
							vs.add(spLeft);
						}
					} else if (!Util.isEmpty(item.leftsideWord)) {
						// 只有左边有文字。右边没文字
						ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
						/**
						 * 获取左边的说明文字
						 */
						String iCap = Util.getLeftCap(item.leftsideWord);
						int lenLeft = 0;
						if (!Util.isEmpty(iCap)) {
							/**
							 * 左边是说明文字 右边是下拉列表框
							 */
							TextView tvLeft = new TextView(NativeModeActivity.this);

							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(iCap);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							lenLeft = (int) paintLeft.measureText(iCap);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvLeft.setLayoutParams(
											new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							lLeft.addView(tvLeft, lLeft.getChildCount());
						} else if (Util.isEmpty(leftList)) {
							// 左边不是说明文字 。没有下拉列表框
							TextView tvLeft = new TextView(NativeModeActivity.this);

							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(ssLeft);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							lenLeft = (int) paintLeft.measureText(item.leftsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvLeft.setLayoutParams(
											new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							lLeft.addView(tvLeft, lLeft.getChildCount());
						}
						/**
						 *** 假如左边不是下拉框 直接给文本框赋值。
						 */
						if (Util.isEmpty(leftList)) {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
							if (et.getLayoutParams().width > remainWidth) {
								int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
										LayoutParams.WRAP_CONTENT);
								et.setLayoutParams(ITSELF);
							}
							lRight.addView(et, lRight.getChildCount());
							vs.add(et);
						} else {
							// 是下拉框。给下拉框赋值
							/**
							 * 大树文本框修改
							 */
							if (Util.isEmpty(iCap)) {
								Log.i("zrl1", item.leftsideWord + "左边文字");
								// 1
								TextView tvLeft = new TextView(NativeModeActivity.this);
								tvLeft.setTextSize(20);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(item.leftsideWord);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(item.leftsideWord);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvLeft.setLayoutParams(
												new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
								// 2
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
								if (et.getLayoutParams().width > remainWidth) {
									int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx),
											LayoutParams.WRAP_CONTENT);
									et.setLayoutParams(ITSELF);
								}
								lRight.addView(et, lRight.getChildCount());
								vs.add(et);

							} else {
								// 大树文本框修改
								Spinner spLeft = new Spinner(NativeModeActivity.this);
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);
								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										if (j == i) {
											AnswerMap am = amList.get(i);
											int pos = aa.getPosition(am.getAnswerValue());
											if (-1 != pos) {
												// 选上位置
												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}
								int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams SPSELF = new LayoutParams((int) (spWidth * fx), LayoutParams.WRAP_CONTENT);
								spLeft.setLayoutParams(SPSELF);
								lRight.addView(spLeft, lRight.getChildCount());
								vs.add(spLeft);
							}

						}
					} else if (!Util.isEmpty(item.rightsideWord)) {
						// 只有右边有。左边没有
						/**
						 * 左边是文本框 右边是说明
						 */
						TextView tvRight = new TextView(NativeModeActivity.this);

						tvRight.setTextSize(lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(ssRight);
						tvRight.setPadding(0, 0, 0, 8);
						TextPaint paintRight = tvRight.getPaint();

						int lenRight = (int) paintRight.measureText(item.rightsideWord);
						if (lenRight > (q.freeTextColumn + 4) * fx) {
							lenRight = (int) ((q.freeTextColumn + 4) * fx);
						}
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvRight.setLayoutParams(
										new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
							}
						}
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0, true);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
						if (et.getLayoutParams().width > remainWidth) {
							int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
							LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx), LayoutParams.WRAP_CONTENT);
							et.setLayoutParams(ITSELF);
						}
						lRight.addView(et, lRight.getChildCount());
						lRight.addView(tvRight, lRight.getChildCount());
						vs.add(et);
					} else {
						// 左右无,只有文本题目
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0, true);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						if (et.getLayoutParams().width > (tempQWidth)) {
							int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
							LayoutParams ITSELF = new LayoutParams((int) (editWidth * fx), LayoutParams.WRAP_CONTENT);
							et.setLayoutParams(ITSELF);
						}
						lRight.addView(et, lRight.getChildCount());
						vs.add(et);
					}
					// 数据字典
					// 为输入添加TextWatcher监听文字的变化
					et.setOnTouchListener(new DataTouchListener(item.classid, et));
					et.setKeyListener(null);
					break;
				}

				// switch
				// 每行多列
				// if (1 < q.freeTextColumn) {
				// LinearLayout ll = colsLL.get(i % q.freeTextColumn);
				// ll.addView(itemLL, ll.getChildCount());
				// } else {
				// // 直接加上 一行一列文本题目
				// bodyView.addView(itemLL, bodyView.getChildCount());
				// }
			}

		} else {
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_VERTICAL.equals(q.deployment)) {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			} else if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else if (1 == q.freeTextColumn) {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			} else {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}

			/**
			 * 拖动条风格
			 */
			if (1 == q.qDragChecked && "figure".equals(q.freeTextSort)) {

				if (!Util.isEmpty(q.freeSymbol)) {
					TextView tvSyb = new TextView(NativeModeActivity.this);

					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_syb,
							Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber, //
							Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber, q.freeSymbol + q.freeSumNumber
									+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				} else {
					/**
					 * 数字类型的最大值不为空
					 */
					TextView tvSyb = new TextView(NativeModeActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_max_min,
							Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,
							Util.isEmpty(q.freeMaxNumber) ? "0"
									: q.freeMaxNumber
											+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				}
				for (int i = 0; i < tbColumns.size(); i++) {
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;
					SeekBar sb = new SeekBar(NativeModeActivity.this);
					sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
					sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
					sb.setTag(item);
					sb.setLayoutParams(new LayoutParams(3 * dis.getWidth() / 4, //
							LayoutParams.WRAP_CONTENT));
					sb.setPadding(5, 0, 5, 15);
					sb.setMinimumWidth(400);
					// sb.set
					sb.setMax(Integer.parseInt(Util.isEmpty(q.freeMaxNumber) ? "100" : q.freeMaxNumber));
					if (!Util.isEmpty(amList)) {
						String etName = Util.GetAnswerName(q, item, 0, 0, true);
						for (AnswerMap am : amList) {
							if (etName.equals(am.getAnswerName())) {
								if (!Util.isEmpty(am.getAnswerValue())) {
									sb.setProgress(Integer.parseInt(am.getAnswerValue()));
								}
							}
						}
					}
					// sb.setContentDescription("50");

					// sb.setO
					vs.add(sb);

					TextView tvRight = new TextView(NativeModeActivity.this);
					tvRight.setLayoutParams(FILL_WRAP);
					tvRight.setTextSize(lowSurveySize);
					tvRight.setGravity(Gravity.CENTER);
					tvRight.setTextColor(Color.BLUE);

					/**
					 * 
					 * 滚动条左右两边都有文字, 效果:左边的文字在滚动条的左边|右边文字和滚动条,其中文字在滚动条的上边
					 */
					if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
						/**
						 * 次布局存放左边文字、滚动条、右边文字
						 */
						LinearLayout ll = new LinearLayout(NativeModeActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(FILL_WRAP);
						ll.setPadding(5, 5, 20, 5);

						TextView tvLeft = new TextView(NativeModeActivity.this);
						tvLeft.setTextSize(lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						tvLeft.setText(item.leftsideWord);

						/**
						 * 显示当前的刻度
						 */
						tvRight.setText(item.rightsideWord + "(" + sb.getProgress() + "/" + sb.getMax() + ")");

						LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
						rightLL.setOrientation(LinearLayout.VERTICAL);
						rightLL.setLayoutParams(WRAP_WRAP);
						rightLL.addView(tvRight, rightLL.getChildCount());
						rightLL.addView(sb, rightLL.getChildCount());
						sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvRight, item.rightsideWord));
						bodyView.addView(tvLeft, bodyView.getChildCount());
						bodyView.addView(rightLL, bodyView.getChildCount());
					} else if (!Util.isEmpty(item.leftsideWord)) {
						/**
						 * 左边有文字, 效果:左边文字,右边滚动条
						 */
						/**
						 * 次布局存放左边文字、滚动条、右边文字
						 */
						LinearLayout ll = new LinearLayout(NativeModeActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(FILL_WRAP);
						ll.setPadding(5, 5, 20, 5);

						TextView tvLeft = new TextView(NativeModeActivity.this);
						tvLeft.setTextSize(lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						tvLeft.setText(item.leftsideWord);

						/**
						 * 显示当前的刻度
						 */
						tvRight.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

						LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
						rightLL.setOrientation(LinearLayout.VERTICAL);
						rightLL.setLayoutParams(WRAP_WRAP);
						rightLL.addView(tvRight, rightLL.getChildCount());
						rightLL.addView(sb, rightLL.getChildCount());
						sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvRight, null));
						bodyView.addView(tvLeft, bodyView.getChildCount());
						bodyView.addView(rightLL, bodyView.getChildCount());

					} else if (!Util.isEmpty(item.rightsideWord)) {
						/**
						 * 右边文字, 效果:上方文字,下边滚动条
						 */
						/**
						 * 次布局存放左边文字、滚动条、右边文字
						 */

						/**
						 * 显示当前的刻度
						 */
						tvRight.setText(item.rightsideWord + "(" + sb.getProgress() + "/" + sb.getMax() + ")");

						LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
						rightLL.setOrientation(LinearLayout.VERTICAL);
						rightLL.setLayoutParams(WRAP_WRAP);
						rightLL.addView(tvRight, rightLL.getChildCount());
						rightLL.addView(sb, rightLL.getChildCount());
						sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvRight, item.rightsideWord));
						bodyView.addView(rightLL, bodyView.getChildCount());
					} else {
						/**
						 * 只有滚动条
						 */
						tvRight.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

						LinearLayout rightLL = new LinearLayout(NativeModeActivity.this);
						rightLL.setOrientation(LinearLayout.VERTICAL);
						rightLL.setLayoutParams(WRAP_WRAP);
						rightLL.addView(tvRight, rightLL.getChildCount());
						rightLL.addView(sb, rightLL.getChildCount());
						sb.setOnSeekBarChangeListener(new SeekBarChangeListener(tvRight, null));
						bodyView.addView(rightLL, bodyView.getChildCount());
					}
				}
				// 一下代码不要执行
				return;
			}

			// 需要求EditText之中的条件是否符合大于、大于等于、小于、小于等于的条件
			boolean isSyb = (1 != q.qDragChecked && "figure".equals(q.freeTextSort) && //
					!Util.isEmpty(q.freeSymbol));
			if (isSyb) {
				TextView tvSyb = new TextView(NativeModeActivity.this);
				tvSyb.setLayoutParams(WRAP_WRAP);
				tvSyb.setTextColor(Color.RED);
				tvSyb.setTextSize(surveySize);
				tvSyb.setText(getResources().getString(R.string.question_syb,
						Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber, //
						Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber, q.freeSymbol + q.freeSumNumber
								+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
				bodyView.addView(tvSyb, bodyView.getChildCount());
			} else if ("figure".equals(q.freeTextSort) && !Util.isEmpty(q.freeMaxNumber)) {
				/**
				 * 数字类型的最大值不为空
				 */
				TextView tvSyb = new TextView(NativeModeActivity.this);
				tvSyb.setLayoutParams(WRAP_WRAP);
				tvSyb.setTextColor(Color.RED);
				tvSyb.setTextSize(surveySize);
				tvSyb.setText(getResources().getString(R.string.question_max_min,
						Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,
						q.freeMaxNumber + (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
				bodyView.addView(tvSyb, bodyView.getChildCount());
			}
			if (!Util.isEmpty(tbColumns)) {// 非滚动条
				for (int i = 0; i < tbColumns.size(); i++) {
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;
					LinearLayout ll = new LinearLayout(NativeModeActivity.this);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					ll.setLayoutParams(WRAP_WRAP);
					ll.setPadding(5, 5, 20, 5);
					EditText et = new EditText(NativeModeActivity.this);
					// et.set
					if (1 != q.qDragChecked && "figure".equals(q.freeTextSort)) {
						et.setInputType(InputType.TYPE_CLASS_NUMBER);// setInputType(InputType.TYPE_CLASS_NUMBER);旧文本框
					}
					et.setLayoutParams(WRAP_WRAP);
					et.setMinimumWidth(150);
					et.setTextSize(lowSurveySize);
					// et.setId(i);
					et.setTag(item);

					/**
					 * 假如文本框左右两边都有说明文字的话
					 */
					if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
						// System.out.println("两边都有");
						// 含有%%1%%2%%此类信息
						ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
						// ArrayList<String> rightList =
						// Util.obtainList(item.rightsideWord);
						if (Util.isEmpty(leftList)) {
							TextView tvLeft = new TextView(NativeModeActivity.this);
							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(item.leftsideWord);
							TextView tvRight = new TextView(NativeModeActivity.this);
							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(item.rightsideWord);
							ll.addView(tvLeft, ll.getChildCount());

							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							ll.addView(et, ll.getChildCount());
							ll.addView(tvRight, ll.getChildCount());
							vs.add(et);
						} else {
							/**
							 * 左边有文字
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							if (!Util.isEmpty(iCap)) {
								/**
								 * @@::前面有文字
								 */
								TextView tvLeft = new TextView(NativeModeActivity.this);
								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								ll.addView(tvLeft, ll.getChildCount());
							}

							Spinner spLeft = new Spinner(NativeModeActivity.this);
							spLeft.setTag(item);
							spLeft.setLayoutParams(WRAP_WRAP);
							// simple_spinner_item
							// R.layout.simple_spinner_dropdown_item
							ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
									R.layout.simple_spinner_adapter);
							aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
							for (String str : leftList) {
								aa.add(str);
							}
							spLeft.setAdapter(aa);
							if (!Util.isEmpty(amList)) {
								for (int j = 0; j < amList.size(); j++) {
									// 通过存的value得到位置
									if (j == i) {
										AnswerMap am = amList.get(i);
										int pos = aa.getPosition(am.getAnswerValue());
										if (-1 != pos) {
											// 选上位置

											spLeft.setSelection(pos);
											break;
										}
									}
								}
							}
							ll.addView(spLeft, ll.getChildCount());

							TextView tvRight = new TextView(NativeModeActivity.this);
							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(item.rightsideWord);
							ll.addView(tvRight, ll.getChildCount());
							vs.add(spLeft);
						}
						bodyView.addView(ll, bodyView.getChildCount());

					} else if (!Util.isEmpty(item.leftsideWord) && Util.isEmpty(item.rightsideWord)) {// 只有左边有文字
						// System.out.println("左边文字");
						ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
						/**
						 * 获取左边的说明文字
						 */
						String iCap = Util.getLeftCap(item.leftsideWord);
						if (!Util.isEmpty(iCap)) {
							// System.out.println("iCap=" + iCap);
							/**
							 * 左边是说明文字 右边是下拉列表框
							 */
							TextView tvLeft = new TextView(NativeModeActivity.this);
							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(iCap);
							ll.addView(tvLeft, ll.getChildCount());
						} else if (Util.isEmpty(leftList)) {
							TextView tvLeft = new TextView(NativeModeActivity.this);
							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(item.leftsideWord);
							ll.addView(tvLeft, ll.getChildCount());
						}

						if (1 == item.dateCheck) {
							et.setMinWidth(240);
							// System.out.println("1 == item.dateCheck");
							Drawable d = getResources().getDrawable(R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
									d, //
									null);
							et.setOnTouchListener(
									new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							ll.addView(et, ll.getChildCount());
							vs.add(et);
						} else if (Util.isEmpty(leftList)) {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							ll.addView(et, ll.getChildCount());
							vs.add(et);
						}
						if (!Util.isEmpty(leftList)) {
							// System.out.println("!Util.isEmpty(leftList)--->"
							// + leftList);
							Spinner spLeft = new Spinner(NativeModeActivity.this);
							spLeft.setTag(item);
							spLeft.setLayoutParams(WRAP_WRAP);
							// simple_spinner_item
							// R.layout.simple_spinner_dropdown_item
							ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeModeActivity.this,
									R.layout.simple_spinner_adapter);
							aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
							for (String str : leftList) {
								aa.add(str);
							}
							spLeft.setAdapter(aa);

							if (!Util.isEmpty(amList)) {
								for (int j = 0; j < amList.size(); j++) {
									// 通过存的value得到位置
									if (j == i) {
										AnswerMap am = amList.get(i);
										int pos = aa.getPosition(am.getAnswerValue());
										if (-1 != pos) {
											// 选上位置
											spLeft.setSelection(pos);
											break;
										}
									}
								}
							}
							ll.addView(spLeft, ll.getChildCount());
							vs.add(spLeft);
						}
						bodyView.addView(ll, bodyView.getChildCount());
					} else if (Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {// 右边有文字
						// System.out.println("右边文字");
						/**
						 * 左边是文本框 右边是说明
						 */
						TextView tvRight = new TextView(NativeModeActivity.this);
						tvRight.setTextSize(lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(item.rightsideWord);
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0, true);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						ll.addView(et, ll.getChildCount());
						ll.addView(tvRight, ll.getChildCount());
						bodyView.addView(ll, bodyView.getChildCount());
						vs.add(et);
					} else {// 两边都没有文字
						// System.out.println("两边都没文字");
						if (1 == item.dateCheck) {
							et.setMinWidth(240);
							// System.out.println("1 == item.dateCheck");
							Drawable d = getResources().getDrawable(R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
									d, //
									null);
							et.setOnTouchListener(
									new OutDayTouchListener(NativeModeActivity.this, et, item.dateSelect));
						}
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0, true);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						bodyView.addView(et, bodyView.getChildCount());
						vs.add(et);
					}
				}
			} // 非滚动条
		}

		// 结束
	}
	public int getRandomQuestionIndex(Group group) {
		if (group.getIndexArr().isEmpty()) {
			return -1;
		}
		// group.getIndexArr();
		if (group.isRandom()) {
			/**
			 * 随机group子节点index节点
			 */
			Collections.shuffle(group.getIndexArr());
		}
		/**
		 * 获取随机后的group子节点的第一个节点
		 */
		Integer index = group.getIndexArr().get(0);
		/**
		 * 移除已获取的index节点
		 */
		group.getIndexArr().remove(index);
		/**
		 * 获取question_index=index节点在qs集合中的位置
		 */
		return null == iiMap.get(index) ? -1 : iiMap.get(index);
	}

	public int gotoQuestion(QGroup qGroup) {
		int _index = -1;
		if (qGroup.getGroups().isEmpty()) {
			return _index;
		}
		switch (qGroup.getGroupType()) {
		case QGroup.GROUP_TYPE_NONE:
		case QGroup.GROUP_TYPE_AUTO:
			if (QGroup.GROUP_TYPE_AUTO == qGroup.getGroupType() && !qGroup.isAlreadyRandom()) {
				Collections.shuffle(qGroup.getGroups());
				/**
				 * 已经随机过
				 */
				qGroup.setAlreadyRandom(true);
			}
			Group group = qGroup.getGroups().get(0);
			/**
			 * 小题组遍历完成
			 */
			if (group.getIndexArr().isEmpty()) {
				if (2 <= qGroup.getGroups().size()) {
					/**
					 * 假如有3个group 分别为group_order=0, group_order=1, group_order=2
					 * 
					 * 假如当前group_order=0, 则nextGroup_order=1 假如当前group_order=1,
					 * 则nextGroup_order=2 假如当前group_order=2,
					 * 则nextGroup_order=3错误.
					 */
					Group nextGroup = qGroup.getGsMap().get(group.getOrder() + 1);
					if (null != nextGroup) {
						Group currGroup = qGroup.getGsMap().get(group.getOrder());
						/**
						 * 获取group与group中间间隔的index对应的order集合
						 */
						tempIndexArr.addAll(subIndexArr(currGroup.getIndexArr(), nextGroup.getIndexArr()));
						qGroup.getGroups().remove(group);
						_index = tempIndexArr.get(0);
						tempIndexArr.remove(0);
						/**
						 * 告诉createView方法需要显示这一道题
						 */
						isGroup = true;
						break;
					}
				}
				qGroup.getGroups().remove(group);
			}

			/**
			 * 所有的Group遍历完成
			 */
			if (qGroup.getGroups().isEmpty()) {
				isGroup = false;
				break;
			}

			Group _group = qGroup.getGroups().get(0);
			_index = getRandomQuestionIndex(_group);
			if (-1 == _index) {
				isGroup = false;
			} else {
				isGroup = true;
			}
			break;

		case QGroup.GROUP_TYPE_HAND:

			break;
		}
		return _index;
	}

	/**
	 * @param list1
	 *            index1的集合
	 * @param list2
	 *            index2的集合
	 * @return
	 */
	public ArrayList<Integer> subIndexArr(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		/**
		 * 第一个list取最大值
		 */
		int min = 0;
		int max = 0;

		/**
		 * index转换成order
		 */
		for (Integer i : list1) {
			// 取第一个list的最大值
			int temp = (null == iiMap.get(i) ? -1 : iiMap.get(i));
			min = temp > min ? temp : min;

		}

		/**
		 * index转换成order
		 */
		for (Integer i : list2) {
			// 取第二个list的最小值
			int temp = (null == iiMap.get(i) ? -1 : iiMap.get(i));
			max = temp < max ? temp : max;
		}

		/**
		 * 假如{min~max}={1,2,3,4,5,6}, 那么result={2,3,4,5}
		 */
		for (int i = (min - 1); i < max; i++) {
			result.add(i);
		}
		return result;
	}

	public int toNextIndex(ArrayList<Integer> list) {
		return Collections.max(list) + 1;
	}

	/**
	 * 设置上面是否能点击,isClickable是否是可以点击的.
	 */
	private void setTopClick(boolean isClickable) {
		nq_btn.setClickable(isClickable);
		bq_btn.setClickable(isClickable);
	}

	private final class ImageLongClickListener implements OnLongClickListener {
		private String path;

		public ImageLongClickListener(String _path) {
			this.path = _path;
		}

		@Override
		public boolean onLongClick(View v) {
			Intent it = new Intent(NativeModeActivity.this, ShowImageActivity.class);
			it.putExtra("image_path", path);
			NativeModeActivity.this.startActivity(it);
			return false;
		}

	}

	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
		//  关闭服务  
		if (MainService.isRun) {
			Intent i = new Intent(NativeModeActivity.this, MainService.class);
			NativeModeActivity.this.stopService(i);
		}
	}

	/**
	 * 目录自动翻页的方法
	 * 
	 * @param forwardOrder
	 */
	private final int NEXT_READ_TITLE = 999;// 自动翻下一页常量
	private final int BACK_READ_TITLE = 998;// 自动翻上一页常量

	public void automaticPage(int forwardOrder) {
		globleProgress.setVisibility(View.VISIBLE);
		if (realIndex != forwardOrder) {
			if (realIndex < forwardOrder) {
				Message msg = handler.obtainMessage();
				msg.what = NEXT_READ_TITLE;
				msg.arg1 = forwardOrder;
				handler.sendMessage(msg);
			} else if (realIndex > forwardOrder) {
				Message msg = handler.obtainMessage();
				msg.what = BACK_READ_TITLE;
				msg.arg1 = forwardOrder;
				handler.sendMessage(msg);
			}
		} else {
			globleProgress.setVisibility(View.GONE);
		}
	}

	private final int RECODE = 0x000001;
	private final int CAMERA = 0x000002;
	private final int VIDEO = 0x000003;
	private final int KNOWLEDGE = 0x000004;
	private final int PHOTO = 0x000005;

	private MenuItem miRecode = null;
	private MenuItem miCamera = null;
	private MenuItem miVideo = null;
	private MenuItem miKnowledge = null;
	private MenuItem miPhoto = null;

	// 菜单 大树动画 1 添加 菜单列表
	public HotalkMenuView menuListView = null;

	/**
	 * 系统菜单初始化 void 大树动画 2
	 */
	private void initSysMenu() {
		if (menuListView == null) {
			menuListView = new HotalkMenuView(this);
		}
		menuListView.listview.setOnItemClickListener(listClickListener);
		menuListView.clear();
		// 添加按位置添加 录音 拍照 知识库 打开图库 android.R.drawable.ic_menu_camera
		if (1 == feed.getSurvey().globalRecord) {
		} else if (1 == feed.getSurvey().isRecord) {
			if (isClicked) {
				menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT, "关闭录音", R.drawable.audio_busy_2);
			} else {
				menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT, "开启录音", R.drawable.ic_btn_speak_now_2);
			}
		}
		if (1 == feed.getSurvey().isPhoto) {
			menuListView.add(HotalkMenuView.MENU_ADD_CONTACT, "拍照", R.drawable.ic_menu_camera_2);
			if (sdImages.getVisibility() == View.GONE) {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "打开图库", R.drawable.ic_menu_crop_2);
			} else {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "关闭图库", R.drawable.ic_menu_crop_2);
			}
		} else if (isHaveSingle) {
			if (sdImages.getVisibility() == View.GONE) {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "打开图库", R.drawable.ic_menu_crop_2);
			} else {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "关闭图库", R.drawable.ic_menu_crop_2);
			}
		}
		if (1 == feed.getSurvey().isVideo) {
			// R.drawable.presence_video_online_close
			menuListView.add(HotalkMenuView.MENU_DELETE_MULTI_MSG, "录像", R.drawable.presence_video_online_2);
		}
		menuListView.add(HotalkMenuView.MENU_MEMBER_MANAGER, "知识库", R.drawable.ic_menu_archive_2);
		menuListView.add(HotalkMenuView.MENU_SEND_MSG_FORMULAR, "退出访问", android.R.drawable.ic_menu_revert);
	}

	// 大树动画 3 添加
	protected void switchSysMenuShow() {
		// 初始化系统菜单
		initSysMenu();
		if (!menuListView.getIsShow()) {
			menuListView.show();
		} else {
			menuListView.close();
		}
	}

	// 大树动画 5 添加 菜单打开
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		switchSysMenuShow();
		return false;
	}

	/**
	 * 菜单点击事件处理 大树动画 6 跳转在这里 打开 录音 摄像 图片 等在这里
	 */
	OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
			// 获取key唯一标识
			int key = Integer.parseInt(view.getTag().toString());
			ImageView selectIv = (ImageView) view.findViewById(R.id.menu_item_view_iv);
			// 跳转
			switch (key) {
			case HotalkMenuView.MENU_SEND_MSG_FORMULAR:
				System.out.println("退出访问");
				if (View.VISIBLE != vResult.getVisibility()) {
					if (1 == feed.getIsCompleted()) {
						getQuestionAnswer(MSG_NEXT, false);
						handler.sendEmptyMessage(MSG_WRITE);
					} else {
						isShow = false;
						getQuestionAnswer(MSG_NEXT, false);
						handler.sendEmptyMessage(MSG_SAVE);
					}
				}
				break;
			case HotalkMenuView.MENU_VIEW_CONTACT:
				view.setBackgroundColor(Color.YELLOW);
				System.out.println("录音");
				// new RecordTask(isClicked, null).execute();
				new RecordTask(isClicked, null, selectIv).execute();
				break;
			case HotalkMenuView.MENU_ADD_CONTACT:
			case HotalkMenuView.MENU_DELETE_MULTI_MSG:
				System.out.println("拍照+录像");
				view.setBackgroundColor(Color.YELLOW);
				Intent intent = new Intent(NativeModeActivity.this, PhotoActivity.class);
				Bundle bundle = new Bundle();
				UploadFeed photo = new UploadFeed();
				photo.setUserId(feed.getUserId());
				photo.setSurveyId(feed.getSurveyId());
				photo.setUuid(feed.getUuid());
				if (ma.cfg.getBoolean("save_inner", false)) {
					/**
					 * 存放在内部
					 */
					photo.setPath(Util.getRecordInnerPath(NativeModeActivity.this, feed.getSurveyId()));
					photo.setIsSaveInner(0);
				} else {
					/**
					 * 存放在外部
					 */
					photo.setPath(Util.getRecordPath(feed.getSurveyId()));
					photo.setIsSaveInner(1);
				}
				// 增加pid 命名规则
				photo.setName(Util.getRecordName(feed.getUserId(), feed.getSurveyId(), Cnt.FILE_TYPE_PNG, feed.getUuid(), null, feed.getPid(), feed.getParametersContent(), (q.qOrder + 1) + ""));
				photo.setStartTime(System.currentTimeMillis());
				// 摄像传参判断是摄像还是拍照结束
				if (key == HotalkMenuView.MENU_ADD_CONTACT) {
					bundle.putString("opt", "0"); // 0代表拍照
				} else if (key == HotalkMenuView.MENU_DELETE_MULTI_MSG) {
					bundle.putString("opt", "1"); // 1代表摄像
				}
				// 摄像传参判断是摄像还是拍照结束
				bundle.putSerializable("photo", photo);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;
			case HotalkMenuView.MENU_MEMBER_MANAGER:
				System.out.println("知识库");
				view.setBackgroundColor(Color.YELLOW);
				Intent knowIntent = new Intent();
				knowIntent.setClass(NativeModeActivity.this, KnowleageActivity.class);
				startActivity(knowIntent);
				overridePendingTransition(R.anim.right, R.anim.left);
				break;

			case HotalkMenuView.MENU_ADD_TO_FAVORITES:
				System.out.println("打开图库");
				view.setBackgroundColor(Color.YELLOW);
				if (sdImages.getVisibility() == View.VISIBLE) {
					sdImages.setVisibility(View.GONE);
				} else {
					sdImages.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
			// 关闭
			menuListView.close();
		}
	};

	/**
	 * 创建菜单,只在创建时调用一次. 菜单操作 大树动画 4 注释掉
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
		// 拍照和预览初始化
		// if (1 == feed.getSurvey().isPhoto) {
		// miCamera = menu.add(Menu.NONE, CAMERA, 1,
		// R.string.camera).setIcon(android.R.drawable.ic_menu_camera);
		// miPhoto = menu.add(Menu.NONE, PHOTO, 4,
		// R.string.open_photo).setIcon(android.R.drawable.ic_menu_crop);
		// } else {
		// // 有单题拍照
		// if (isHaveSingle) {
		// miPhoto = menu.add(Menu.NONE, PHOTO, 4,
		// R.string.open_photo).setIcon(android.R.drawable.ic_menu_crop);
		// } else {
		//
		// }
		// }
		// // 摄像初始化
		// if (1 == feed.getSurvey().isVideo) {
		// miVideo = menu.add(Menu.NONE, VIDEO, 2,
		// R.string.video).setIcon(R.drawable.presence_video_online);
		// } else {
		//
		// }
		// // 全局录音 不加了
		// if (1 == feed.getSurvey().globalRecord) {
		//
		// } else {
		// // 不是全局录音有录音加上
		// if (1 == feed.getSurvey().isRecord) {
		// miRecode = menu.add(Menu.NONE, RECODE, 0,
		// R.string.recode).setIcon(android.R.drawable.ic_btn_speak_now);
		// } else {
		//
		// }
		// }
		// miKnowledge = menu.add(Menu.NONE, KNOWLEDGE, 3,
		// R.string.knowleage).setIcon(R.drawable.ic_menu_archive);

		// return true;
	}

	// 大树拒访 在这里 访问状态
	@Override
	public void onItemClick(int index) {
		// TODO Auto-generated method stub
		ReturnType rt = rtList.get(index);
		returnTypeId = rt.getReturnId();
		returnName=rt.getReturnName();
		if (1 == feed.getIsCompleted()) {
			getQuestionAnswer(MSG_NEXT, false);
			handler.sendEmptyMessage(MSG_WRITE);
		} else {
			isShow = false;
			getQuestionAnswer(MSG_NEXT, false);
			handler.sendEmptyMessage(MSG_SAVE);
		}
		Toasts.makeText(this, "选中了" + rt.getReturnName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	//  录音 的各种状态  开启   关闭   点击真否   
	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch ((Integer) param[0]) {
		case TaskType.TS_RECORED:
			if (null != param[1]) {
				int state = (Integer) param[1];
				if (state == 30) {
					Toasts.makeText(NativeModeActivity.this, R.string.record_open, Toast.LENGTH_SHORT).show();
				} else if (state==40) {
					Toasts.makeText(NativeModeActivity.this, R.string.record_close, Toast.LENGTH_SHORT).show();
				} else if (state==20) {
					isClicked = true;
				} else if (state==10) {
					isClicked = false;
				}
			}
			break;
		}
	}

	/**
	 * 每次运行都调用,可以运行时修改操作 菜单操作
	 */
	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// return super.onPrepareOptionsMenu(menu);
	// }

	/**
	 * 菜单操作
	 */
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// // 录音
	// case RECODE:
	// new RecordTask(isClicked, null).execute();
	// break;
	// // 拍照
	// // 摄像
	// case CAMERA:
	// case VIDEO:
	// Intent intent = new Intent(NativeModeActivity.this, PhotoActivity.class);
	// Bundle bundle = new Bundle();
	// UploadFeed photo = new UploadFeed();
	// photo.setUserId(feed.getUserId());
	// photo.setSurveyId(feed.getSurveyId());
	// photo.setUuid(feed.getUuid());
	// if (ma.cfg.getBoolean("save_inner", false)) {
	// /**
	// * 存放在内部
	// */
	// photo.setPath(Util.getRecordInnerPath(NativeModeActivity.this,
	// feed.getSurveyId()));
	// photo.setIsSaveInner(0);
	// } else {
	// /**
	// * 存放在外部
	// */
	// photo.setPath(Util.getRecordPath(feed.getSurveyId()));
	// photo.setIsSaveInner(1);
	// }
	// // 增加pid 命名规则
	// photo.setName(Util.getRecordName(feed.getUserId(), feed.getSurveyId(),
	// Cnt.FILE_TYPE_PNG, feed.getUuid(), null, feed.getPid(),
	// feed.getParametersContent(), (q.qOrder + 1) + ""));
	// photo.setStartTime(System.currentTimeMillis());
	// // 摄像传参判断是摄像还是拍照结束
	// if (item.getItemId() == CAMERA) {
	// bundle.putString("opt", "0"); // 0代表拍照
	// } else if (item.getItemId() == VIDEO) {
	// bundle.putString("opt", "1"); // 1代表摄像
	// }
	// // 摄像传参判断是摄像还是拍照结束
	// bundle.putSerializable("photo", photo);
	// intent.putExtras(bundle);
	// startActivity(intent);
	// overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	// break;
	// // 知识库
	// case KNOWLEDGE:
	// Intent knowIntent = new Intent();
	// knowIntent.setClass(NativeModeActivity.this, KnowleageActivity.class);
	// startActivity(knowIntent);
	// overridePendingTransition(R.anim.right, R.anim.left);
	// break;
	// case PHOTO:
	// sdImages.setVisibility(View.VISIBLE);
	// if (miPhoto.getTitle().equals(this.getString(R.string.open_photo))) {
	// miPhoto.setTitle(this.getString(R.string.close_photo));
	// sdImages.setVisibility(View.VISIBLE);
	// } else {
	// miPhoto.setTitle(this.getString(R.string.open_photo));
	// sdImages.setVisibility(View.GONE);
	// }
	// break;
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }
	
	
	

}
