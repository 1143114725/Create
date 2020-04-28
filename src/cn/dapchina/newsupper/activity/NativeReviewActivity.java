package cn.dapchina.newsupper.activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.alibaba.fastjson.JSON;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.adapter.ImageAdapter;
import cn.dapchina.newsupper.bean.Answer;
import cn.dapchina.newsupper.bean.AnswerMap;
import cn.dapchina.newsupper.bean.CstmMatcher;
import cn.dapchina.newsupper.bean.MatcherItem;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.QuestionItem;
import cn.dapchina.newsupper.bean.Task;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.global.TaskType;
import cn.dapchina.newsupper.main.MainService;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.NetUtil;
import cn.dapchina.newsupper.util.ThreeLeverUtil;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.util.XmlUtil;
import cn.dapchina.newsupper.view.HotalkMenuView;
import cn.dapchina.newsupper.view.Toasts;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class NativeReviewActivity extends BaseActivity implements OnClickListener{

	MyApp ma;
	UploadFeed feed;
	LinearLayout ll;

	private Display dis;
	private LayoutInflater inflater;
	private ArrayList<Question> qs;

	private final LayoutParams FILL_WRAP = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	private final LayoutParams WRAP_WRAP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	private SlidingDrawer sdImages;
	// private ImageView ivDragIcon;
	private ImageSwitcher mSwitcher;
	private Gallery g;
	private ImageAdapter mImageAdapter;
	private TextView tvImageCount;
	private ImageView native_left_iv, native_opt;
	
	/**
	 * 设备屏幕的宽
	 */
	private int screenWidth;
	/**
	 * 设备屏幕的高
	 */
	private int screenHeight;
	HashMap<Integer, Answer> anMap;
	private boolean isNew = true;
	// 屏幕的宽度
	private int maxCWidth;
	/**
	 * 判断是否是单题拍照
	 */
	private boolean isHaveSingle;
	// 问卷字号动态设置
	private int surveySize;// 最小的字体
	private int lowSurveySize;// 说明字体大小
	private int middleSueveySize;// 中等字体
	private int bigSurveySize;// 大字体
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

	int provincePos = 0;// 一级联动 点击的 位置

	int cityPos = 0;
	int areaPos = 0;
	int itemLL_Index = 0;

	private Spinner provinceSpinner = null;
	private Spinner citySpinner = null;
	private Spinner countrySpinner = null;
	private ArrayAdapter<String> provinceAdapter = null;
	private ArrayAdapter<String> countryAdapter = null;
	private ArrayAdapter<String> cityAdapter = null;

	// 三级联动 结束

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		feed = (UploadFeed) getIntent().getExtras().get("feed");
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		if (null == ma.cfg) {
			ma.cfg = new Config(getApplicationContext());
		}
		if (1 == ma.cfg.getInt("ScreenOrientation", 0)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
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
		setContentView(R.layout.activity_native_review);
		overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		/**
		 * 获取显示
		 */
		dis = getWindowManager().getDefaultDisplay();
		maxCWidth = (int) (dis.getWidth() * 0.9);
		// ListView lvReview = (ListView) findViewById(R.id.review_list);
		//
		ll = (LinearLayout) findViewById(R.id.review_ll);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sdImages = (SlidingDrawer) findViewById(R.id.pic_sd);
		if (null != feed) {
			show();
			new ShowTask().execute();
		} else {
			finish();
		}
		/**
		 * 预览音视频，录音按钮
		 * */
		native_opt = (ImageView) findViewById(R.id.native_opt);
		native_opt.setOnClickListener(this);
		native_left_iv = (ImageView) findViewById(R.id.native_left_iv);
		native_left_iv.setOnClickListener(this);
		
		/**
		 * 实例化抽屉控件
		 */
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
				/**
				 * 打开抽屉控件
				 */
				// ivDragIcon.setImageResource(R.drawable.arrow_down_float);
				ArrayList<UploadFeed> images = ma.dbService.getImages(feed.getUuid(), feed.getSurveyId());
				//去除缩略图显示
				Iterator<UploadFeed> iter = images.iterator();
			        while(iter.hasNext()){
			        	UploadFeed image = iter.next();
			        	if("thumbnail".equals(image.getName().substring(image.getName().lastIndexOf("_")+1, image.getName().lastIndexOf(".")))){
							iter.remove();
						}else {
						}
			        }
				
				if (null == tvImageCount) {
					tvImageCount = (TextView) findViewById(R.id.img_count_tv);
				}
				if (0 < images.size()) {
					if (null == mImageAdapter) {
						mImageAdapter = new ImageAdapter(NativeReviewActivity.this, images);
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
			}
		});
	}

	private final class ShowTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			qs = ma.dbService.getQuestionList(feed.getSurveyId());
			if (Cnt.VISIT_STATE_COMPLETED == feed.getIsCompleted()//
					&& Cnt.UPLOAD_STATE_UPLOADED <= feed.getIsUploaded()) {
				anMap = XmlUtil.getFeedAnswer(new File(feed.getPath(), feed.getName()));
			}
			int headNum = 0;
			for (int i = 0; i < qs.size(); i++) {
				Question question = qs.get(i);
				if (1 == question.qCamera) {
					isHaveSingle = true;
				}
				if (Cnt.TYPE_HEADER == question.qType) {
					headNum += 1;
				}
				if (headNum > 1) {
					if (Util.isEmpty(question.qid)) {
						question.qid = "Q" + (question.qOrder - headNum + 1);
					}
				}
			}
			return 0 != qs.size();
		}

		@Override
		protected void onPreExecute() {
			((TextView) findViewById(R.id.review_survey_title_tv)).setText(feed.getSurveyTitle());
			refreshView();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (1 != feed.getSurvey().isPhoto && !isHaveSingle) {
				sdImages.setVisibility(View.GONE);
			}
			for (int i = 0; i < qs.size(); i++) {
				handler.sendEmptyMessageDelayed(i, 6 * i);
			}
			super.onPostExecute(result);
		}

	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			createQuestionBodyView(qs.get(msg.what));
			if (msg.what == (qs.size() - 1)) {
				dismiss();
			}
		}
	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refreshView();
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
		// int maxWidth = (int) (screenWidth * 0.9);
		ll.setPadding(10, //
				screenHeight / 20, //
				screenWidth / 20, //
				10);
	}

	public void createQuestionBodyView(Question q) {// 动态生成题干的方法
		// System.out.println("q.qTitle"+q.qTitle);
		Answer tempAnswer = null;
		/**
		 * 完成且上传过
		 */
		if (Cnt.VISIT_STATE_COMPLETED == feed.getIsCompleted()//
				&& Cnt.UPLOAD_STATE_UPLOADED <= feed.getIsUploaded()) {
			tempAnswer = anMap.get(q.qIndex);
		} else {
			tempAnswer = ma.dbService.getAnswer(feed.getUuid(), q.qIndex + "");
		}

		ArrayList<AnswerMap> amList = new ArrayList<AnswerMap>();
		if (Cnt.TYPE_MEDIA == q.qType) {

		} else if (null != tempAnswer) {
			amList = tempAnswer.getAnswerMapArr();
		} else {
			return;
		}
		RelativeLayout questionView = (RelativeLayout) inflater.inflate(R.layout.review_list_item, null);

		TextView tvTitle = (TextView) questionView.findViewById(R.id.review_title_tv);
		tvTitle.setMaxWidth((int) (dis.getWidth() * 0.9));
		TextView tvCaption = (TextView) questionView.findViewById(R.id.review_caption_tv);
		tvCaption.setMaxWidth((int) (dis.getWidth() * 0.9));
		tvCaption.setTextSize(lowSurveySize);
		LinearLayout llCaption = (LinearLayout) questionView.findViewById(R.id.review_caption_ll);
		TextView tvComment = (TextView) questionView.findViewById(R.id.review_comment_tv);
		tvComment.setMaxWidth((int) (dis.getWidth() * 0.9));
		tvComment.setTextSize(lowSurveySize);
		LinearLayout bodyView = (LinearLayout) questionView.findViewById(R.id.review_body_ll);

		LinearLayout llComment = (LinearLayout) questionView.findViewById(R.id.review_comment_ll);

		// if (null != tempAnswer) {
		// System.out.println("答案UUID--->" + tempAnswer.uuid);
		// } else {
		// System.out.println("Question--->" + q.qIndex);
		// }

		/**
		 * 获取引用其他问题的集合
		 */
		// ArrayList<Restriction> rs = q.getResctItemArr();

		// System.out.println("当前question_index--->" + q.qIndex);

		/**
		 * 设置标题的位置
		 */
		if (Cnt.POS_LEFT.equals(q.qTitlePosition)) {
			tvTitle.setGravity(Gravity.LEFT);
		} else if (Cnt.POS_CENTER.equals(q.qTitlePosition)) {
			tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
		} else if (Cnt.POS_RIGHT.equals(q.qTitlePosition)) {
			tvTitle.setGravity(Gravity.RIGHT);
		} else {
			tvTitle.setGravity(Gravity.LEFT);
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
				tvTitle.setText(q.qTitle);
			}
			bodyView.setVisibility(View.GONE);
		} else {
			//更改的样式
			if (0 == q.qTitleDisable) {
				tvTitle.setClickable(false);
			}
			tvTitle.setTextColor(Color.BLACK);
			tvTitle.setTextSize(middleSueveySize);

			// StringBuilder sbTitle = new StringBuilder("");
			String strTilte = "";
			if (!Util.isEmpty(q.qid)) {
				strTilte = q.qid + ". " + q.qTitle;
			} else {
				if (!Util.isEmpty(q.qTitle)) {
					strTilte = NativeReviewActivity.this.getResources().getString(R.string.question_order, q.qOrder) + q.qTitle;
				} else {
					strTilte = NativeReviewActivity.this.getResources().getString(R.string.question_order, q.qOrder);
				}

			}
			
			/**
			 * 标题逻辑引用
			 */
			CstmMatcher qutoMatcherList = Util.findMatcherItemList(strTilte, ma, feed.getUuid(),q.surveyId);
			boolean qutoHave = Util.isEmpty(qutoMatcherList.getResultStr());
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
			/**
			 * 引用受访者参数结束
			 */

			/**
			 * 必填
			 */
			if (1 == q.qRequired) {
				strTilte = strTilte + "<font color='red'>"+getResources().getString(R.string.question_must)+"</font>";
			}
			//更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = NativeReviewActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator+source;
					System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if(image!=null){
						int tWidth = image.getWidth();
						int tHeight = image.getHeight();
						drawable.setBounds(0, 0, tWidth,tHeight);
						return drawable;
					}else{
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(strTilte, imgGetter, null);
			tvTitle.setText(fromHtml);
			//更改的样式

			if (1 == q.qRadomed) {
				String radomed = getResources().getString(R.string.question_random);
				SpannableString ss = new SpannableString(radomed);
				ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(), Spanned.SPAN_COMPOSING);
				tvTitle.append(ss);
			}

			if (0 < q.lowerBound || 0 < q.upperBound) {
				String bound = getResources().getString(R.string.question_bound, q.upperBound, q.lowerBound);
				SpannableString ss = new SpannableString(bound);
				ss.setSpan(new ForegroundColorSpan(Color.RED), 0, bound.length(), Spanned.SPAN_COMPOSING);
				tvTitle.append(ss);
			}

		}

		// tvTitle.setTextSize(30);

		/**
		 * 假如题目有上方追加说明
		 */
		if (!Util.isEmpty(q.qCaption)) {
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
			}

			//更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = NativeReviewActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator+source;
					System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if(image!=null){
						int tWidth = image.getWidth();
						int tHeight = image.getHeight();
						drawable.setBounds(0, 0, tWidth,tHeight);
						return drawable;
					}else{
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(q.qCaption, imgGetter, null);
			tvCaption.setText(fromHtml);
			//更改的样式
		}

		/**
		 * 假如题目有下方追加说明
		 */
		if (!Util.isEmpty(q.qComment)) {
			/**
			 * 设置上方追加说明的位置
			 */
			if (Cnt.POS_CENTER.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.CENTER_HORIZONTAL);
			} else if (Cnt.POS_RIGHT.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.RIGHT);
			}

			//更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = NativeReviewActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator+source;
					System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if(image!=null){
						int tWidth = image.getWidth();
						int tHeight = image.getHeight();
						drawable.setBounds(0, 0, tWidth,tHeight);
						return drawable;
					}else{
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(q.qComment, imgGetter, null);
			tvComment.setText(fromHtml);
			//更改的样式
		}

		// Answer an = tmpAnswerList.get(q.qIndex);
		// Answer an = ma.dbService.getAnswer(qAnswer.uuid, q.qIndex)

		switch (q.qType) {
		case Cnt.TYPE_HEADER:// 标题
			// tvTitle.setMaxWidth(Integer.MAX_VALUE);
			break;

		case Cnt.TYPE_RADIO_BUTTON:// 单选按钮
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放 追加说明新布局&& 0 == q.isHaveItemCap
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else if (Cnt.ORIENT_VERTICAL.equals(q.deployment)) {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			} else {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}

			/**
			 * 行
			 */
			ArrayList<QuestionItem> radioRows = q.getRowItemArr();
			if (!Util.isEmpty(radioRows)) {
				// Answer an = tmpAnswerList.get(q.qIndex);
				// ArrayList<AnswerMap> amList = null;
				// if(null != an){
				// amList = an.getAnswerMapArr();
				// }
				Answer an = null;
				ArrayList<AnswerMap> aswMapList = new ArrayList<AnswerMap>();
				if (!Util.isEmpty(q.qSiteOption)) {
					an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (null != an) {
						aswMapList = an.getAnswerMapArr();
					}
				}
				//  大树  双引用   如果是双引用   那么 置为空  1  
				if (!Util.isEmpty(q.qSiteOption2)) {
					aswMapList=null; 
				}	
				ArrayList<LinearLayout> arrLayout = new ArrayList<LinearLayout>();
				/**
				 * 需要分组 追加说明新布局&& 0 == q.isHaveItemCap
				 */
				if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
					for (int i = 0; i < q.rowsNum; i++) {
						LinearLayout ll = new LinearLayout(this);
						ll.setOrientation(LinearLayout.VERTICAL);
						// 设置颜色
						if (0 == i % 2) {
							ll.setBackgroundColor(Color.parseColor("#10000050"));
						}
						ll.setPadding(0, 0, 20, 0);
						// FILL_WRAP (screenWidth-20)/q.rowsNum,
						// LayoutParams.WRAP_CONTENT
						ll.setLayoutParams(WRAP_WRAP);

						arrLayout.add(ll);
						bodyView.addView(ll, bodyView.getChildCount());
					}
				}

				for (int i = 0; i < radioRows.size(); i++) {
					QuestionItem item = radioRows.get(i);

					RadioButton rb = new RadioButton(NativeReviewActivity.this);
					rb.setClickable(false);
					if (1 == item.isOther) {// 其他项
						if (-1 != item.itemValue) {
							LinearLayout otherLayout = new LinearLayout(NativeReviewActivity.this);
							otherLayout.setLayoutParams(WRAP_WRAP);
							rb.setLayoutParams(WRAP_WRAP);
							rb.setFocusable(false);
							rb.setButtonDrawable(R.drawable.small_radiobutton);
							rb.setBackgroundResource(R.drawable.small_radiobutton_background);
							rb.setTextColor(Color.BLACK);
							rb.setTextSize(lowSurveySize);
							//更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeReviewActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator+source;
									System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory.decodeFile(name);
									if(image!=null){
										int tWidth = image.getWidth();
										int tHeight = image.getHeight();
										drawable.setBounds(0, 0, tWidth,tHeight);
										return drawable;
									}else{
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(item.getItemText(), imgGetter, null);
							rb.setText(fromHtml);
							//更改的样式

							String idStr = "ohter_" + q.qIndex + "_" + i;
							rb.setId(idStr.hashCode());

							EditText et = new EditText(NativeReviewActivity.this);
							et.setFocusable(false);
							et.setOnTouchListener(null);
							et.setClickable(false);
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
										// System.out.println("etName=" + etName
										// + ", etValue=" +
										// am.getAnswerValue());
										et.setText(am.getAnswerValue());
										et.setTag(item);
									}
								}
							}
							if (Util.isEmpty(q.qSiteOption)) {
								otherLayout.addView(rb, otherLayout.getChildCount());
								otherLayout.addView(et, otherLayout.getChildCount());
								if (0 < arrLayout.size()) {
									LinearLayout ll = arrLayout.get((i % q.rowsNum));
									ll.addView(otherLayout, ll.getChildCount());
								} else {
									bodyView.addView(otherLayout, bodyView.getChildCount());
								}
								// bodyView.addView(otherLayout,
								// bodyView.getChildCount());
								// 将文本框假如结合中
							} else {
								/**
								 * 数据库中没有答案
								 */
								if (null == an || Util.isEmpty(an.getAnswerMapArr())) {
									otherLayout.addView(rb, otherLayout.getChildCount());
									otherLayout.addView(et, otherLayout.getChildCount());
									if (0 < arrLayout.size()) {
										LinearLayout ll = arrLayout.get((i % q.rowsNum));
										ll.addView(otherLayout, ll.getChildCount());
									} else {
										bodyView.addView(otherLayout, bodyView.getChildCount());
									}
									// bodyView.addView(otherLayout,
									// bodyView.getChildCount());
									// 将文本框假如结合中
								} else {
									/**
									 * 数据库中有答案
									 */
									//  大树  双引用 修改在这里 体现    多 加一重 判断  在这里  2
									
									if (aswMapList==null) {
										otherLayout.addView(rb, otherLayout.getChildCount());								
										if (0 < arrLayout.size()) {
											LinearLayout ll = arrLayout.get((i % q.rowsNum));
											ll.addView(otherLayout, ll.getChildCount());
										} else {
											bodyView.addView(otherLayout, bodyView.getChildCount());
										}
									}else {
										for (AnswerMap am : aswMapList) {
											String value = am.getAnswerValue();
											if (String.valueOf(item.itemValue).equals(value)) {
												otherLayout.addView(rb, otherLayout.getChildCount());
												// otherLayout.addView(et,
												// otherLayout.getChildCount());
												if (0 < arrLayout.size()) {
													LinearLayout ll = arrLayout.get((i % q.rowsNum));
													ll.addView(otherLayout, ll.getChildCount());
												} else {
													bodyView.addView(otherLayout, bodyView.getChildCount());
												}
												// bodyView.addView(otherLayout,
												// bodyView.getChildCount());
												// 将文本框假如结合中
												// vs.add(et);
												// break;
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
					} else {// 单选项
						TextView tvItemCap = new TextView(NativeReviewActivity.this);
						/**
						 * 选项的追加说明
						 */
						if (!Util.isEmpty(item.caption)) {
							// 追加说明新布局 FILL_WRAP
							tvItemCap.setLayoutParams(WRAP_WRAP);
							tvItemCap.setTextColor(Color.GRAY);// 统计局专有页面
							tvItemCap.setTextSize(lowSurveySize);
							//更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeReviewActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator+source;
									System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory.decodeFile(name);
									if(image!=null){
										int tWidth = image.getWidth();
										int tHeight = image.getHeight();
										drawable.setBounds(0, 0, tWidth,tHeight);
										return drawable;
									}else{
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(item.caption, imgGetter, null);
							tvItemCap.setText(fromHtml);
							//更改的样式
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
						rb.setButtonDrawable(R.drawable.small_radiobutton);
						rb.setBackgroundResource(R.drawable.small_radiobutton_background);
						rb.setTextColor(Color.BLACK);
						rb.setTextSize(lowSurveySize);
						// rb.setText(item.getItemText());

						//更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = NativeReviewActivity.this.getFilesDir() + File.separator + "survey" + File.separator + feed.getSurveyId() + File.separator+source;
								System.out.println("name:" + name);
								drawable = Drawable.createFromPath(name);
								Bitmap image = BitmapFactory.decodeFile(name);
								if(image!=null){
									int tWidth = image.getWidth();
									int tHeight = image.getHeight();
									drawable.setBounds(0, 0, tWidth,tHeight);
									return drawable;
								}else{
									return null;
								}
							}
						};
						Spanned fromHtml = Html.fromHtml(item.getItemText(), imgGetter, null);
						rb.setText(fromHtml);

						String name = Util.GetAnswerName(q, item, 0, 0, false);
						if (1 == q.qPreSelect) {
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
						if (Util.isEmpty(q.qSiteOption)) {
							if (!Util.isEmpty(item.caption)) {
								if (0 < arrLayout.size()) {
									LinearLayout ll = arrLayout.get((i % q.rowsNum));
									ll.addView(tvItemCap, ll.getChildCount());
								} else {
									bodyView.addView(tvItemCap, bodyView.getChildCount());
								}
								// bodyView.addView(tvItemCap,
								// bodyView.getChildCount());
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
						} else {
							//  大树  双引用  在这里 体现  修改  判断  如果是  否  如果不是双引用  那么 走   3 
							if (aswMapList==null) {
								if (!Util.isEmpty(item.caption)) {
									bodyView.addView(tvItemCap, bodyView.getChildCount());
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
							}else {
								for (AnswerMap am : aswMapList) {
									String value = am.getAnswerValue();
									if ("1".equals(q.qInclusion)) {
										if (!String.valueOf(item.itemValue).equals(value)) {
											if (!Util.isEmpty(item.caption)) {
												bodyView.addView(tvItemCap, bodyView.getChildCount());
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
												// bodyView.addView(rb,
												// bodyView.getChildCount());
											// bodyView.addView(rb,
											// bodyView.getChildCount());
											// 将没有其他项的单选按钮加入集合中
											break;
										}
									} else {
										if (String.valueOf(item.itemValue).equals(value)) {
											if (!Util.isEmpty(item.caption)) {
												bodyView.addView(tvItemCap, bodyView.getChildCount());
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
												// bodyView.addView(rb,
												// bodyView.getChildCount());
											// bodyView.addView(rb,
											// bodyView.getChildCount());
											// 将没有其他项的单选按钮加入集合中
											break;
										}
									}
								}
							}
						}
					}
				}
			}
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
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else if (Cnt.ORIENT_VERTICAL.equals(q.deployment)) {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			} else {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}

			ArrayList<QuestionItem> checkRows = q.getRowItemArr();
			if (!Util.isEmpty(checkRows)) {
				ArrayList<LinearLayout> arrLayout = new ArrayList<LinearLayout>();
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

				for (int i = 0; i < checkRows.size(); i++) {
					QuestionItem item = checkRows.get(i);

					CheckBox cb = new CheckBox(NativeReviewActivity.this);
					//隐藏选项
					if(1==item.hide){
						cb.setVisibility(View.GONE);
					}
					cb.setClickable(false);
					cb.setTag(item);

					if (1 == item.isOther) {
						if (-1 != item.itemValue) {// 并且不是<freeInput/>这种空标签
							LinearLayout otherLayout = new LinearLayout(NativeReviewActivity.this);
							otherLayout.setLayoutParams(WRAP_WRAP);
							cb.setLayoutParams(WRAP_WRAP);
							String idStr = "ohter_" + q.qIndex + "_" + i;
							cb.setId(idStr.hashCode());
							cb.setBackgroundResource(R.drawable.small_checkbox_background);
							cb.setButtonDrawable(R.drawable.small_checkbox);
							cb.setTextSize(lowSurveySize);
							cb.setTextColor(Color.BLACK);
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
								cb.setText(item.getItemText());
							}

							otherLayout.addView(cb, otherLayout.getChildCount());

							EditText et = new EditText(NativeReviewActivity.this);
							et.setFocusable(false);
							et.setOnTouchListener(null);
							et.setClickable(false);
							et.setTag(item);
							et.setLayoutParams(WRAP_WRAP);
							et.setMinimumWidth(150);
							if (!Util.isEmpty(amList)) {
								// System.out.println("复选题以前的答案集合不为空");
								String chName = Util.GetAnswerName(q, item, 0, 0, false);
								// System.out.println("chName--->"+chName+", value="+item.itemValue);
								String etName = Util.GetAnswerName(q, item, 0, 0, true);
								for (AnswerMap am : amList) {
									// System.out.println("name="+am.getAnswerName()+", value="+am.getAnswerValue());
									if (chName.equals(am.getAnswerName()) && am.getAnswerValue().equals(String.valueOf(item.itemValue))) {
										cb.setChecked(true);
										cb.setTag(item);
										// System.out.println("复选题以前的答案配对");
									}
									// System.out.println("复选框其他项etName=" +
									// etName + ", name=" + am.getAnswerName() +
									// ", value=" + am.getAnswerValue());
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
										// et.setTag(item);
									}
								}
							}
							et.setId((idStr + "_et").hashCode());
							/**
							 * 假如当前的CheckBox被选中了则让文本框可以编辑
							 * 假如没有被选中则让文本框中的内容为空并且是不可以编辑的
							 */
							otherLayout.addView(et, otherLayout.getChildCount());
							if (0 < arrLayout.size()) {
								LinearLayout ll = arrLayout.get((i % q.rowsNum));
								ll.addView(otherLayout, ll.getChildCount());
							} else {
								bodyView.addView(otherLayout, bodyView.getChildCount());
							}
						}
					} else {
						// 复选追加说明方法
						TextView tvItemCap = new TextView(NativeReviewActivity.this);
						if (!Util.isEmpty(item.caption)) {
							// 追加说明新布局&& 原FILL_WRAP
							tvItemCap.setLayoutParams(WRAP_WRAP);
							tvItemCap.setTextColor(Color.GRAY);// 统计局专有页面
							tvItemCap.setTextSize(lowSurveySize);
							CstmMatcher cm = Util.findFontMatcherList(item.caption);
							boolean font = Util.isEmpty(cm.getMis());
							if (!font) {
								item.caption = cm.getResultStr();
								SpannableString ss = new SpannableString(item.caption);
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= item.caption.length()) {
										ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
								tvItemCap.setText(ss);
							} else {
								tvItemCap.setText(item.caption);
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
						cb.setBackgroundResource(R.drawable.small_checkbox_background);
						cb.setButtonDrawable(R.drawable.small_checkbox);
						cb.setTextSize(lowSurveySize);
						cb.setTextColor(Color.BLACK);
						// cb.setText(item.getItemText());
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
							cb.setText(item.getItemText());
						}

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
							if (0 < arrLayout.size()) {
								LinearLayout ll = arrLayout.get((i % q.rowsNum));
								ll.addView(tvItemCap, ll.getChildCount());
							} else {
								bodyView.addView(tvItemCap, bodyView.getChildCount());
							}
						}

						CstmMatcher imgCm = Util.findImageMatherList(item.itemText);

						/**
						 * 单选题目选项有图片
						 */
						if (0 < imgCm.getMis().size()) {
							LinearLayout imgLL = new LinearLayout(this);
							imgLL.setPadding(0, 5, 0, 5);
							imgLL.setLayoutParams(FILL_WRAP);
							imgLL.setOrientation(LinearLayout.VERTICAL);
							imgLL.setGravity(Gravity.CENTER_VERTICAL);
							cb.setText(Util.isEmpty(imgCm.getResultStr()) ? "" : imgCm.getResultStr());
							imgLL.addView(cb, imgLL.getChildCount());
							for (MatcherItem mi : imgCm.getMis()) {
								ImageView iv = new ImageView(this);
								String path = Util.getImagePath(this, feed.getSurveyId(), mi.name);
								// 图片百分比开始
								// iv.setLayoutParams(WRAP_WRAP);
								Bitmap image = BitmapFactory.decodeFile(path);
								int tWidth = image.getWidth();
								int tHeight = image.getHeight();
								iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
								// 图片百分比结束
								iv.setImageURI(Uri.parse(Util.getImagePath(NativeReviewActivity.this, feed.getSurveyId(), mi.name)));
								imgLL.addView(iv, imgLL.getChildCount());
							}
							if (0 < arrLayout.size()) {
								LinearLayout ll = arrLayout.get((i % q.rowsNum));
								ll.addView(imgLL, ll.getChildCount());
							} else {
								bodyView.addView(imgLL, bodyView.getChildCount());
							}
						} else {
							if (0 < arrLayout.size()) {
								LinearLayout ll = arrLayout.get((i % q.rowsNum));
								ll.addView(cb, ll.getChildCount());
							} else {
								bodyView.addView(cb, bodyView.getChildCount());
							}
							// bodyView.addView(cb, bodyView.getChildCount());
						}
						// bodyView.addView(cb, bodyView.getChildCount());
					}
				}
			}
			break;

		case Cnt.TYPE_MATRIX_RADIO_BUTTON:// 矩阵单选
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			// if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
			// System.out.println("横向");
			// bodyView.setOrientation(LinearLayout.HORIZONTAL);
			// } else {
			// System.out.println("纵向");
			bodyView.setOrientation(LinearLayout.VERTICAL);
			// }

			/**
			 * 获取行标题
			 */
			ArrayList<QuestionItem> rRows = q.getRowItemArr();
			/**
			 * 获取列标题
			 */
			ArrayList<QuestionItem> rColmns = q.getColItemArr();

			/**
			 * 假如题干的宽度大于或等于屏幕宽度的3/4
			 */
			boolean isBeyond = (screenWidth * 2 / 3 - 20) <= rColmns.size() * 100;

			// 处理非随机矩阵
			/**
			 * 遍历每一行
			 */
			for (int r = 0; r < rRows.size() + 1; r++) {
				QuestionItem rowItem = null;
				if (0 != r) {
					// System.out.println("row=" + rRows.get(r - 1).itemText);
					rowItem = rRows.get(r - 1);
				}

				/**
				 * 遍历每一列
				 */
				RadioGroup ll = new RadioGroup(this);
				ll.setGravity(Gravity.CENTER_VERTICAL);
				for (int c = 0; c < rColmns.size() + 1; c++) {
					QuestionItem colItem = null;

					if (0 != c) {
						colItem = rColmns.get(c - 1);
					}

					ll.setOrientation(LinearLayout.HORIZONTAL);
					ll.setLayoutParams(FILL_WRAP);

					TextView tvTb = new TextView(this);
					tvTb.setLayoutParams(WRAP_WRAP);
					tvTb.setGravity(Gravity.FILL);
					tvTb.setTextSize(lowSurveySize);
					tvTb.setWidth(100);
					tvTb.setPadding(2, 2, 2, 2);
					if (0 == r) {// 如过是第一行, 则打印出每一列的值
						// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
						ll.setBackgroundColor(Color.LTGRAY);
						if (0 == c) {// 打印表头
							tvTb.setText(" ");
							tvTb.setWidth(dis.getWidth() / 3);
						} else {// c !=0 打印每列的,即列标题
							// System.out.println("c_colmns_item_value=" +
							// rColmns.get(c - 1).itemValue + ", colmns=" + (c -
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

						CstmMatcher cm = Util.findImageMatherList(tvTb.getText().toString());
						if (0 < cm.getMis().size()) {
							LinearLayout _im = new LinearLayout(NativeReviewActivity.this);
							_im.setOrientation(LinearLayout.VERTICAL);
							_im.setGravity(Gravity.FILL);
							_im.setLayoutParams(new LayoutParams(isBeyond ? 100 : (screenWidth * 2 / 3 - 20) / rColmns.size(), LayoutParams.WRAP_CONTENT));
							tvTb.setText(cm.getResultStr());
							_im.addView(tvTb, _im.getChildCount());
							for (int i = 0; i < cm.getMis().size(); i++) {
								MatcherItem mi = cm.getMis().get(i);
								ImageView iv = new ImageView(NativeReviewActivity.this);
								String path = Util.getImagePath(this, feed.getSurveyId(), mi.name);
								// 图片百分比开始
								// iv.setLayoutParams(WRAP_WRAP);
								Bitmap image = BitmapFactory.decodeFile(path);
								int tWidth = image.getWidth();
								int tHeight = image.getHeight();
								iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
								// 图片百分比结束
								iv.setPadding(2, 2, 2, 2);
								iv.setImageURI(Uri.parse(Util.getImagePath(NativeReviewActivity.this, feed.getSurveyId(), mi.name)));
								_im.addView(iv, _im.getChildCount());
							}
							ll.addView(_im, ll.getChildCount());
						} else {
							ll.addView(tvTb, ll.getChildCount());
						}
					} else {// r != 0
						if (r % 2 == 0)
							ll.setBackgroundColor(Color.parseColor("#10000050"));
						else
							ll.setBackgroundColor(Color.TRANSPARENT);
						//隐藏选项
						if(1==rowItem.hide){
							ll.setVisibility(View.GONE);
						}
						if (0 == c) {
							tvTb.setTextColor(Color.BLACK);
							tvTb.setWidth(dis.getWidth() / 3);
							tvTb.setBackgroundColor(Color.TRANSPARENT);
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
								// 单选矩阵图片解析开始
								CstmMatcher cImage = Util.findImageMatherList(t);
								if (0 < cImage.getMis().size()) {
									LinearLayout _im = new LinearLayout(this);
									_im.setOrientation(LinearLayout.VERTICAL);
									_im.setGravity(Gravity.FILL);
									_im.setLayoutParams(new LayoutParams(screenWidth / 3, LayoutParams.WRAP_CONTENT));
									tvTb.setText(cImage.getResultStr());
									_im.addView(tvTb, _im.getChildCount());
									for (int i = 0; i < cImage.getMis().size(); i++) {
										MatcherItem mi = cImage.getMis().get(i);
										ImageView iv = new ImageView(this);
										iv.setPadding(2, 2, 2, 2);
										String path = Util.getImagePath(this, feed.getSurveyId(), mi.name);
										// 图片百分比开始
										// iv.setLayoutParams(WRAP_WRAP);
										Bitmap image = BitmapFactory.decodeFile(path);
										int tWidth = image.getWidth();
										int tHeight = image.getHeight();
										iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth, tHeight));
										// 图片百分比结束
										iv.setImageURI(Uri.parse(path));
										_im.addView(iv, _im.getChildCount());
										ll.addView(_im, ll.getChildCount());
									}
								} else {
									ll.addView(tvTb, ll.getChildCount());
								}
								// //单选矩阵图片解析结束
							} else {
								ll.addView(tvTb, ll.getChildCount());
							}
							// ***********************************样式处理**************************//

							// tvTb.setText(rowItem.itemText);
						} else {// 打印单选按钮
							// QuestionItem item = rRows.get(r - 1);
							if (null == rowItem || (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
								// 空其他项,即只有一个<freeInput/>标签
								continue;
							}
							// System.out.println("r_row_item_value=" +
							// rowItem.itemValue + ", r=" + (r - 1));
							RadioButton radio = new RadioButton(this);
							radio.setLayoutParams(WRAP_WRAP);
							radio.setGravity(Gravity.FILL | Gravity.CENTER_VERTICAL);
							radio.setClickable(false);
							// radio.setGravity(Gravity.CENTER);

							AnswerMap am = new AnswerMap();
							String name = Util.GetAnswerName(q, rowItem, rowItem.itemValue, 0, false);
							am.setAnswerName(name);
							// System.out.println("矩阵测试--->(c-1)=" + (c - 1) +
							// ", value=" + rColmns.get(c - 1).itemValue);
							// am.setAnswerValue(String.valueOf((c - 1)));
							am.setAnswerValue(String.valueOf(colItem.itemValue));
							radio.setTag(am);
							ll.addView(radio, ll.getChildCount());
							if (!Util.isEmpty(amList)) {
								for (AnswerMap tam : amList) {
									if (name.equals(tam.getAnswerName()) && am.getAnswerValue().equals(tam.getAnswerValue())) {
										radio.setChecked(true);
									}
								}
							}
							radio.setPadding(2, 2, 2, 2);
							radio.setTextSize(lowSurveySize);
							radio.setTextColor(Color.BLACK);
							radio.setButtonDrawable(R.drawable.small_radiobutton);
							radio.setBackgroundResource(R.drawable.small_radiobutton_background);
							radio.setWidth(100);
							if (isBeyond) {
								/**
								 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
								 */
								// 矩阵百分比
								radio.setLayoutParams(new LinearLayout.LayoutParams((screenWidth * 2 / 3 - 20) / rColmns.size(), LinearLayout.LayoutParams.WRAP_CONTENT));
								radio.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							} else {
								radio.setWidth((screenWidth * 2 / 3 - 20) / rColmns.size());
							}
						}
					}

				}
				bodyView.addView(ll, bodyView.getChildCount());
			}
			break;

		case Cnt.TYPE_MATRIX_CHECK_BOX:// 矩阵复选
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
			/**
			 * 获取行标题
			 */
			ArrayList<QuestionItem> cRows = q.getRowItemArr();
			/**
			 * 获取列标题
			 */
			ArrayList<QuestionItem> cColmns = q.getColItemArr();

			/**
			 * 假如题干的宽度大于或等于屏幕宽度的3/4
			 */
			boolean _isBeyond = ((screenWidth * 2 / 3 - 20)) <= cColmns.size() * 100;

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
				RadioGroup ll = new RadioGroup(this);
				ll.setGravity(Gravity.CENTER_VERTICAL);
				for (int c = 0; c < cColmns.size() + 1; c++) {
					QuestionItem colItem = null;

					if (0 != c) {
						colItem = cColmns.get(c - 1);
					}

					ll.setOrientation(LinearLayout.HORIZONTAL);
					ll.setLayoutParams(FILL_WRAP);
					TextView tvTb = new TextView(this);
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
							tvTb.setWidth(dis.getWidth() / 3);
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
							tvTb.setWidth(dis.getWidth() / 3);
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

							CheckBox check = new CheckBox(this);
							check.setClickable(false);
							// System.out.println("(r - 1)=" + (r - 1) +
							// ", (c - 1)=" + (c - 1));
							System.out.println("rowItem.itemValue=" + rowItem.itemValue + ", colItem.itemValue=" + colItem.itemValue);
							String name = Util.GetAnswerName(q, null, rowItem.itemValue, colItem.itemValue, false);
							int value = q.getColItemArr().size() * rowItem.itemValue + colItem.itemValue;
							AnswerMap am = new AnswerMap();
							am.setAnswerName(name);
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
						}
					}

				}
				bodyView.addView(ll, bodyView.getChildCount());
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

			Spinner sp = new Spinner(NativeReviewActivity.this);
			sp.setClickable(false);
			// LayoutParams dropLp = new LayoutParams(-1, -2);
			// dropLp.addRule(RelativeLayout.BELOW, q.qTitle.hashCode());
			sp.setLayoutParams(FILL_WRAP);
			ArrayList<QuestionItem> columns = q.getColItemArr();
			HashMap<String, Integer> tvMap = new HashMap<String, Integer>();
			HashMap<Integer, Integer> ivMap = new HashMap<Integer, Integer>();
			if (!Util.isEmpty(columns)) {
				ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeReviewActivity.this, R.layout.simple_spinner_adapter);
				aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
				for (int i = 0; i < columns.size(); i++) {
					QuestionItem item = columns.get(i);
					aa.add(item.itemText);
					tvMap.put(i + "_" + item.itemText, item.itemValue);
					ivMap.put(item.itemValue, i);
				}
				sp.setAdapter(aa);
				// Answer an = tmpAnswerList.get(q.qIndex);
				// ArrayList<AnswerMap> amList = an.getAnswerMapArr();
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
			tvMap.clear();
			tvMap = null;
			ivMap.clear();
			ivMap = null;
			break;

		case Cnt.TYPE_FREE_TEXT_BOX:// 单行文本框
			isNew = true;
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
						TextView tvSyb = new TextView(NativeReviewActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(surveySize);
						tvSyb.setText(getResources().getString(R.string.question_num_sum_no_repeat, q.freeSymbol + q.freeSumNumber));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					} else if (1 == q.freeNoRepeat) {
						TextView tvSyb = new TextView(NativeReviewActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(surveySize);
						tvSyb.setText(getResources().getString(R.string.question_no_repeat));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					} else if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber)) {
						TextView tvSyb = new TextView(NativeReviewActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(surveySize);
						tvSyb.setText(getResources().getString(R.string.question_num_sum, q.freeSymbol + q.freeSumNumber));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					}
				}
				int tempQWidth = (dis.getWidth()) / q.freeTextColumn;
				// 每行多个文本题 ，弄两个垂直布局
				if (1 < q.freeTextColumn) {
					LinearLayout lvTitle = new LinearLayout(NativeReviewActivity.this);
					lvTitle.setOrientation(LinearLayout.HORIZONTAL);
					lvTitle.setLayoutParams(FILL_WRAP);
					if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber) && 1 == q.freeNoRepeat) {
						TextView tvSyb = new TextView(NativeReviewActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(surveySize);
						tvSyb.setText(getResources().getString(R.string.question_num_sum_no_repeat, q.freeSymbol + q.freeSumNumber));
						lvTitle.addView(tvSyb, lvTitle.getChildCount());
					} else if (1 == q.freeNoRepeat) {
						TextView tvSyb = new TextView(NativeReviewActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(surveySize);
						tvSyb.setText(getResources().getString(R.string.question_no_repeat));
						lvTitle.addView(tvSyb, lvTitle.getChildCount());
					} else if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber)) {
						TextView tvSyb = new TextView(NativeReviewActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(surveySize);
						tvSyb.setText(getResources().getString(R.string.question_num_sum, q.freeSymbol + q.freeSumNumber));
						lvTitle.addView(tvSyb, lvTitle.getChildCount());
					}
					bodyView.addView(lvTitle, bodyView.getChildCount());
					// 设置外层布局
					LinearLayout lvBodyView = new LinearLayout(NativeReviewActivity.this);
					lvBodyView.setOrientation(LinearLayout.HORIZONTAL);
					lvBodyView.setLayoutParams(new LayoutParams(dis.getWidth(), LayoutParams.WRAP_CONTENT));
					// lvBodyView.setBackgroundColor(Color.RED);
					// 追加说明%%处理
					if (!Util.isEmpty(q.qCaption)) {
						if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
							LinearLayout captionLL = new LinearLayout(NativeReviewActivity.this);
							captionLL.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
							captionLL.setOrientation(LinearLayout.HORIZONTAL);
							// 判断出生成几个小%%%%追加说明
							String[] tvCount = q.qCaption.split("%%%%");
							int everyLength = dis.getWidth() / tvCount.length;
							for (int tv = 0; tv < tvCount.length; tv++) {
								TextView tvSmallCaption = new TextView(this);
								String strTvCaption = tvCount[tv];
								tvSmallCaption.setTextColor(Color.BLACK);
								tvSmallCaption.setTextSize(lowSurveySize);
								if (strTvCaption.contains("%%")) {
									strTvCaption = strTvCaption.replaceAll("%%", "");
									tvSmallCaption.setText(strTvCaption);
									// LinearLayout.LayoutParams layoutParams =
									// new
									// LinearLayout.LayoutParams(everyLength/2,LinearLayout.LayoutParams.WRAP_CONTENT);
									// layoutParams.leftMargin=everyLength/2;
									// tvSmallCaption.setLayoutParams(layoutParams);
								} else {

								}
								tvSmallCaption.setLayoutParams(new LinearLayout.LayoutParams(everyLength, LinearLayout.LayoutParams.WRAP_CONTENT));
								tvSmallCaption.setText(strTvCaption);
								captionLL.addView(tvSmallCaption);
							}
							bodyView.addView(captionLL);
						}
					}
					// 遍历每行的列数。
					for (int i = 0; i < q.freeTextColumn; i++) {
						LinearLayout ll = new LinearLayout(NativeReviewActivity.this);
						ll.setOrientation(LinearLayout.VERTICAL);
						ll.setPadding(0, 0, 0, 0);
						// 都给1/3
						// ll.setLayoutParams(new LayoutParams(WRAP_WRAP));
						ll.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
						// ll.setBackgroundColor(Color.BLUE);
						colsLL.add(ll);
						// 给横屏的bodyView加上
						lvBodyView.addView(ll, lvBodyView.getChildCount());
					}
					bodyView.addView(lvBodyView, bodyView.getChildCount());
				}
				tempQWidth -= 20;
				// 1.2一般的方法
				// 遍历每一个项的值。
				for (int i = 0; i < tbColumns.size(); i++) {// for_1
					// 得到每一个项
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;
					// 初始化每一项的布局
					LinearLayout itemLL = new LinearLayout(NativeReviewActivity.this);
					itemLL.setOrientation(LinearLayout.HORIZONTAL);
					// LayoutParams lp = new LayoutParams(100,
					// LayoutParams.WRAP_CONTENT);
					itemLL.setLayoutParams(new LayoutParams(tempQWidth + 20, LayoutParams.WRAP_CONTENT));
					// itemLL.setBackgroundColor(Color.BLUE);
					// itemLL.setPadding(5, 5, 20, 5);
					if (item.required) {
						TextView tvRequired = new TextView(NativeReviewActivity.this);
						RelativeLayout.LayoutParams myParams = WRAP_WRAP;
						// myParams.addRule(RelativeLayout.CENTER_VERTICAL,
						// itemLL.getId());
						// myParams.setMargins(0, 0, 6, 0);
						tvRequired.setLayoutParams(myParams);
						tvRequired.setText(getResources().getString(R.string.notice_required));
						tvRequired.setTextColor(Color.RED);
						tempQWidth -= tvRequired.getWidth();
						// tvRequired.setTextSize(15);
						itemLL.addView(tvRequired, itemLL.getChildCount());
					}
					// 初始化每一项的edittext
					EditText et = new EditText(NativeReviewActivity.this);
					et.setFocusable(false);
					et.setOnTouchListener(null);
					et.setClickable(false);
					if (item.itemSize != 0) {
						int editWidth = Util.getEditWidth(item.itemSize, maxCWidth);
						LayoutParams ITSELF = new LayoutParams(editWidth, LayoutParams.WRAP_CONTENT);
						et.setLayoutParams(ITSELF);
					} else {
						int editWidth = Util.getEditWidth(20, maxCWidth);
						LayoutParams ITSELF = new LayoutParams(editWidth, LayoutParams.WRAP_CONTENT);
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
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length() && mi.end <= strTilte.length()) {
										ssLeft.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new UnderlineSpan(), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length() && mi.end <= strTilte.length()) {
										ssRight.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new UnderlineSpan(), mi.start, mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
					case 0:// 典型的文本
							// 三级联动判断
						if (1 == q.qLinkage) {
							/**
							 * 需要加一个字段 来 做 判断 是否是三级联动 三级联动 的话 必须type 是none 现在测试
							 */
							if (!Util.isEmpty(item.leftsideWord)) {
								// Log.i("zrl1", item.leftsideWord);
								if (i == 0) {
									s1 = item.leftsideWord;
									provinceSpinner = new Spinner(NativeReviewActivity.this);
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
										provinceSpinner.setSelection(cityPos, true);
									} else {
										provinceSpinner.setSelection(0, true);
									}
									provinceSpinner.setClickable(false);
									provinceSpinner.setOnItemSelectedListener(null);
									provinceSpinner.setTag(item);
									itemLL.addView(provinceSpinner, itemLL.getChildCount());
								} else if (i == 1) {
									s2 = item.leftsideWord;
									citySpinner = new Spinner(this);
									area = ThreeLeverUtil.getSecondList(s2);
									areaListTemp = ThreeLeverUtil.getCityPosList(area, city, cityPos);
									if (areaListTemp.size() == 0) {
										areaListTemp.add(NativeReviewActivity.this.getString(R.string.empty));
									}
									cityAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter, areaListTemp);// list是一个
									cityAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									citySpinner.setAdapter(cityAdapter);
									citySpinner.setClickable(false);
									citySpinner.setOnItemSelectedListener(null);
									/**
									 * 获取原有答案
									 */
									if (!Util.isEmpty(amList)) {

										AnswerMap am1 = amList.get(1);
										areaPos = cityAdapter.getPosition(am1.getAnswerValue());
										citySpinner.setSelection(areaPos, true);
									} else {
										citySpinner.setSelection(0, true);
									}
									citySpinner.setTag(item);
									itemLL.addView(citySpinner, itemLL.getChildCount());

								} else if (i == 2) {
									s3 = item.leftsideWord;
									countrySpinner = new Spinner(this);
									way = ThreeLeverUtil.getThridList(s3);
									wayListTemp = ThreeLeverUtil.getAreaPosList(areaListTemp, way, areaPos);
									if (wayListTemp.size() == 0) {
										wayListTemp.add(NativeReviewActivity.this.getString(R.string.empty));
									}

									countryAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter, wayListTemp);// list是一个
									countryAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									countrySpinner.setAdapter(countryAdapter);
									countrySpinner.setClickable(false);
									/**
									 * 获取 原有 答案
									 */
									if (!Util.isEmpty(amList)) {

										AnswerMap am1 = amList.get(2);
										int pos = countryAdapter.getPosition(am1.getAnswerValue());
										countrySpinner.setSelection(pos, true);
									} else {
										countrySpinner.setSelection(0, true);
									}
									itemLL.addView(countrySpinner, itemLL.getChildCount());
									countrySpinner.setTag(item);
								}
							}

						}
						// 三级联动结束
						else {
							// 左右有文本值
							if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
								// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
								ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
								// 左边没有下拉
								if (Util.isEmpty(leftList)) {
									TextView tvLeft = new TextView(NativeReviewActivity.this);
									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(ssLeft);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
									if (lenLeft > tempQWidth / 3) {
										lenLeft = tempQWidth / 3;
										tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
									}
									TextView tvRight = new TextView(NativeReviewActivity.this);
									tvRight.setTextSize(lowSurveySize);
									tvRight.setTextColor(Color.BLACK);
									tvRight.setText(item.rightsideWord);
									tvRight.setPadding(0, 0, 0, 8);
									TextPaint paintRight = tvRight.getPaint();
									int lenRight = (int) paintRight.measureText(item.rightsideWord);
									if (lenRight > tempQWidth / 3) {
										lenRight = tempQWidth / 3;
										tvRight.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
									}
									itemLL.addView(tvLeft, itemLL.getChildCount());
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
										et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
									}
									itemLL.addView(et, itemLL.getChildCount());
									itemLL.addView(tvRight, itemLL.getChildCount());
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
										TextView tvLeft = new TextView(NativeReviewActivity.this);
										tvLeft.setTextSize(lowSurveySize);
										tvLeft.setTextColor(Color.BLACK);
										tvLeft.setText(iCap);
										tvLeft.setPadding(0, 0, 0, 8);
										TextPaint paintLeft = tvLeft.getPaint();
										lenLeft = (int) paintLeft.measureText(iCap);
										if (lenLeft > tempQWidth / 3) {
											lenLeft = tempQWidth / 3;
											tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
										}
										itemLL.addView(tvLeft, itemLL.getChildCount());
									}

									Spinner spLeft = new Spinner(NativeReviewActivity.this);
									spLeft.setClickable(false);
									spLeft.setTag(item);
									spLeft.setLayoutParams(WRAP_WRAP);
									ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeReviewActivity.this, R.layout.simple_spinner_adapter);
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

									TextView tvRight = new TextView(NativeReviewActivity.this);
									tvRight.setTextSize(lowSurveySize);
									tvRight.setTextColor(Color.BLACK);
									tvRight.setText(item.rightsideWord);
									tvRight.setPadding(0, 0, 0, 8);
									TextPaint paintRight = tvRight.getPaint();
									int lenRight = (int) paintRight.measureText(item.rightsideWord);
									// 左边说明不为空，右边长度小于最大长度
									if (!Util.isEmpty(iCap)) {
										if (lenRight > tempQWidth / 3) {
											lenRight = tempQWidth / 3;
											tvRight.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
										}
									} else {
										if (lenRight > tempQWidth / 2) {
											lenRight = tempQWidth / 2;
											tvRight.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
										}
									}

									int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									LayoutParams SPSELF = new LayoutParams(spWidth, LayoutParams.WRAP_CONTENT);
									// 判断有没有前面的左描述
									if (!Util.isEmpty(iCap)) {
										// 余下的长度
										int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenLeft) + (tempQWidth / 3 - lenRight);
										if (spWidth > remainWidth) {
											spLeft.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									} else {
										int remainWidthT = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
										if (spWidth > remainWidthT) {
											spLeft.setLayoutParams(new LayoutParams(remainWidthT, LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									}
									itemLL.addView(spLeft, itemLL.getChildCount());
									itemLL.addView(tvRight, itemLL.getChildCount());
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
									TextView tvLeft = new TextView(NativeReviewActivity.this);
									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(iCap);
									if (lenLeft > tempQWidth / 2) {
										lenLeft = tempQWidth / 2;
										tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
									}
									itemLL.addView(tvLeft, itemLL.getChildCount());
								} else if (Util.isEmpty(leftList)) {
									// 左边不是说明文字 。没有下拉列表框
									TextView tvLeft = new TextView(NativeReviewActivity.this);
									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(ssLeft);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(item.leftsideWord);
									if (lenLeft > tempQWidth / 2) {
										lenLeft = tempQWidth / 2;
										tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
									}
									itemLL.addView(tvLeft, itemLL.getChildCount());
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
										et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
									}
									itemLL.addView(et, itemLL.getChildCount());
								} else {
									// 是下拉框。给下拉框赋值
									Spinner spLeft = new Spinner(NativeReviewActivity.this);
									spLeft.setClickable(false);
									spLeft.setTag(item);
									spLeft.setLayoutParams(WRAP_WRAP);
									ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeReviewActivity.this, R.layout.simple_spinner_adapter);
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
									LayoutParams SPSELF = new LayoutParams(spWidth, LayoutParams.WRAP_CONTENT);
									if (!Util.isEmpty(iCap)) {
										int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
										if (spWidth > remainWidth) {
											spLeft.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									} else {
										if (spWidth > tempQWidth) {
											spLeft.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									}

									itemLL.addView(spLeft, itemLL.getChildCount());
								}
							} else if (!Util.isEmpty(item.rightsideWord)) {
								// 只有右边有。左边没有
								/**
								 * 左边是文本框 右边是说明
								 */
								TextView tvRight = new TextView(NativeReviewActivity.this);
								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(item.rightsideWord);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();
								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								if (lenRight > tempQWidth / 2) {
									lenRight = tempQWidth / 2;
									tvRight.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
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
									et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(et, itemLL.getChildCount());
								itemLL.addView(tvRight, itemLL.getChildCount());
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
									et.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(et, itemLL.getChildCount());
							}
						}

						break;

					case 1:// 日期格式
					case 2:// 数字格式
					case 3:// 英文/数字格式
					case 5:// 邮件格式
						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							// 左右有文字说明
							TextView tvLeft = new TextView(NativeReviewActivity.this);
							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(ssLeft);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
							if (lenLeft > tempQWidth / 3) {
								lenLeft = tempQWidth / 3;
								tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
							}
							if (1 == item.type) {
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_TEXT);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
							TextView tvRight = new TextView(NativeReviewActivity.this);
							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(item.rightsideWord);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							if (lenRight > tempQWidth / 3) {
								lenRight = tempQWidth / 3;
								tvRight.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
							}
							itemLL.addView(tvLeft, itemLL.getChildCount());
							// 假如是滑动条,且是数字题目。显示滑动条。
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(NativeReviewActivity.this);
								sb.setClickable(false);
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
								TextView tvSeekTop = new TextView(NativeReviewActivity.this);
								tvSeekTop.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								// tvSeekTop.setLayoutParams(FILL_WRAP);

								tvSeekTop.setTextSize(lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

								LinearLayout rightLL = new LinearLayout(NativeReviewActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								itemLL.addView(rightLL, itemLL.getChildCount());
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
								int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenRight) + (tempQWidth / 3 - lenLeft);
								if (et.getLayoutParams().width > remainWidth) {
									et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(et, itemLL.getChildCount());
							}
							itemLL.addView(tvRight, itemLL.getChildCount());
						} else if (!Util.isEmpty(item.leftsideWord)) {
							// 只左有
							TextView tvLeft = new TextView(NativeReviewActivity.this);
							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(ssLeft);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
							if (lenLeft > tempQWidth / 2) {
								lenLeft = tempQWidth / 2;
								tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
							}
							if (1 == item.type) {
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_TEXT);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
							itemLL.addView(tvLeft, itemLL.getChildCount());
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(NativeReviewActivity.this);
								sb.setClickable(false);
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
								TextView tvSeekTop = new TextView(NativeReviewActivity.this);
								// tvSeekTop.setLayoutParams(FILL_WRAP);
								tvSeekTop.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								tvSeekTop.setTextSize(lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

								LinearLayout rightLL = new LinearLayout(NativeReviewActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								itemLL.addView(rightLL, itemLL.getChildCount());
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
									et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(et, itemLL.getChildCount());
							}
						} else if (!Util.isEmpty(item.rightsideWord)) {
							if (1 == item.type) {// 日期
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_TEXT);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
							TextView tvRight = new TextView(NativeReviewActivity.this);
							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(item.rightsideWord);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							if (lenRight > tempQWidth / 2) {
								lenRight = tempQWidth / 2;
								tvRight.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
							}
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(NativeReviewActivity.this);
								sb.setClickable(false);
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
								TextView tvSeekTop = new TextView(NativeReviewActivity.this);
								// tvSeekTop.setLayoutParams(FILL_WRAP);
								tvSeekTop.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								tvSeekTop.setTextSize(lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

								LinearLayout rightLL = new LinearLayout(NativeReviewActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								itemLL.addView(rightLL, itemLL.getChildCount());
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
									et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(et, itemLL.getChildCount());
							}

							itemLL.addView(tvRight, itemLL.getChildCount());

						} else {
							// 左右无
							if (1 == item.type) {// 日期
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_TEXT);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(NativeReviewActivity.this);
								sb.setClickable(false);
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
								TextView tvSeekTop = new TextView(NativeReviewActivity.this);
								tvSeekTop.setLayoutParams(FILL_WRAP);
								tvSeekTop.setTextSize(lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								tvSeekTop.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

								LinearLayout rightLL = new LinearLayout(NativeReviewActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								itemLL.addView(rightLL, itemLL.getChildCount());
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
									et.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(et, itemLL.getChildCount());
								// System.out.println("tempQWidth:"+tempQWidth+"--item"+itemLL.getLayoutParams().width);
							}
						}
						break;

					case 4:// 字典格式
						// 字典格式
						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							// 左边没有下拉
							if (Util.isEmpty(leftList)) {
								TextView tvLeft = new TextView(NativeReviewActivity.this);

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(ssLeft);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
								if (lenLeft > tempQWidth / 3) {
									lenLeft = tempQWidth / 3;
									tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
								}
								TextView tvRight = new TextView(NativeReviewActivity.this);

								

								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();
								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								if (lenRight > tempQWidth / 3) {
									lenRight = tempQWidth / 3;
									tvRight.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(tvLeft, itemLL.getChildCount());
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
									et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(et, itemLL.getChildCount());
								itemLL.addView(tvRight, itemLL.getChildCount());
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
									TextView tvLeft = new TextView(NativeReviewActivity.this);


									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(iCap);
									if (lenLeft > tempQWidth / 3) {
										lenLeft = tempQWidth / 3;
										tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
									}
									itemLL.addView(tvLeft, itemLL.getChildCount());
								}

								Spinner spLeft = new Spinner(NativeReviewActivity.this);


								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeReviewActivity.this, R.layout.simple_spinner_adapter);
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

								TextView tvRight = new TextView(NativeReviewActivity.this);


								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();
								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								// 左边说明不为空，右边长度小于最大长度
								if (!Util.isEmpty(iCap)) {
									if (lenRight > tempQWidth / 3) {
										lenRight = tempQWidth / 3;
										tvRight.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
									}
								} else {
									if (lenRight > tempQWidth / 2) {
										lenRight = tempQWidth / 2;
										tvRight.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
									}
								}

								int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
								LayoutParams SPSELF = new LayoutParams(spWidth, LayoutParams.WRAP_CONTENT);
								// 判断有没有前面的左描述
								if (!Util.isEmpty(iCap)) {
									// 余下的长度
									int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenLeft) + (tempQWidth / 3 - lenRight);
									if (spWidth > remainWidth) {
										spLeft.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
									} else {
										spLeft.setLayoutParams(SPSELF);
									}
								} else {
									int remainWidthT = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
									if (spWidth > remainWidthT) {
										spLeft.setLayoutParams(new LayoutParams(remainWidthT, LayoutParams.WRAP_CONTENT));
									} else {
										spLeft.setLayoutParams(SPSELF);
									}
								}
								itemLL.addView(spLeft, itemLL.getChildCount());
								itemLL.addView(tvRight, itemLL.getChildCount());
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
								TextView tvLeft = new TextView(NativeReviewActivity.this);

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(iCap);
								if (lenLeft > tempQWidth / 2) {
									lenLeft = tempQWidth / 2;
									tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(tvLeft, itemLL.getChildCount());
							} else if (Util.isEmpty(leftList)) {
								// 左边不是说明文字 。没有下拉列表框
								TextView tvLeft = new TextView(NativeReviewActivity.this);

								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(ssLeft);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(item.leftsideWord);
								if (lenLeft > tempQWidth / 2) {
									lenLeft = tempQWidth / 2;
									tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(tvLeft, itemLL.getChildCount());
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
									et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
								}
								itemLL.addView(et, itemLL.getChildCount());
							} else {
								// 是下拉框。给下拉框赋值
								/**
								 * 大树文本框修改
								 */
								if (Util.isEmpty(iCap)) {
									Log.i("zrl1", item.leftsideWord + "左边文字");
									// 1
									TextView tvLeft = new TextView(NativeReviewActivity.this);
									tvLeft.setTextSize(20);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(item.leftsideWord);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(item.leftsideWord);
									if (lenLeft > tempQWidth / 3) {
										lenLeft = tempQWidth / 3;
										tvLeft.setLayoutParams(new LayoutParams(tempQWidth / 3, LayoutParams.WRAP_CONTENT));
									}
									itemLL.addView(tvLeft, itemLL.getChildCount());
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
										et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
									}
									itemLL.addView(et, itemLL.getChildCount());

								} else {
									// 大树文本框修改
									Spinner spLeft = new Spinner(NativeReviewActivity.this);
									spLeft.setTag(item);
									spLeft.setLayoutParams(WRAP_WRAP);
									// simple_spinner_item
									// R.layout.simple_spinner_dropdown_item
									ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeReviewActivity.this, R.layout.simple_spinner_adapter);
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
									LayoutParams SPSELF = new LayoutParams(spWidth, LayoutParams.WRAP_CONTENT);
									if (!Util.isEmpty(iCap)) {
										int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
										if (spWidth > remainWidth) {
											spLeft.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									} else {
										if (spWidth > tempQWidth) {
											spLeft.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
										} else {
											spLeft.setLayoutParams(SPSELF);
										}
									}

									itemLL.addView(spLeft, itemLL.getChildCount());
								}

							}
						} else if (!Util.isEmpty(item.rightsideWord)) {
							// 只有右边有。左边没有
							/**
							 * 左边是文本框 右边是说明
							 */
							TextView tvRight = new TextView(NativeReviewActivity.this);

							tvRight.setTextSize(lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();

							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							if (lenRight > tempQWidth / 2) {
								lenRight = tempQWidth / 2;
								tvRight.setLayoutParams(new LayoutParams(tempQWidth / 2, LayoutParams.WRAP_CONTENT));
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
								et.setLayoutParams(new LayoutParams(remainWidth, LayoutParams.WRAP_CONTENT));
							}
							itemLL.addView(et, itemLL.getChildCount());
							itemLL.addView(tvRight, itemLL.getChildCount());
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
								et.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
							}
							itemLL.addView(et, itemLL.getChildCount());
						}
						//数据字典
						//为输入添加TextWatcher监听文字的变化
						et.setOnTouchListener(null);
						et.setKeyListener(null);
						break;
					}// switch
						// 每行多列
					if (1 < q.freeTextColumn) {
						LinearLayout ll = colsLL.get(i % q.freeTextColumn);
						ll.addView(itemLL, ll.getChildCount());
					} else {
						// 直接加上 一行一列文本题目
						bodyView.addView(itemLL, bodyView.getChildCount());
					}
				}// for_1
					// 追加说明%%处理
				if (!Util.isEmpty(q.qCaption)) {
					if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
						tvCaption.setText("");
					}
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
					bodyView.setOrientation(LinearLayout.VERTICAL);
				} else if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
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
						TextView tvSyb = new TextView(NativeReviewActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(surveySize);
						tvSyb.setText(getResources().getString(R.string.question_syb, Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,//
								Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber, q.freeSymbol + q.freeSumNumber + (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					} else {
						/**
						 * 数字类型的最大值不为空
						 */
						TextView tvSyb = new TextView(NativeReviewActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(surveySize);
						tvSyb.setText(getResources().getString(R.string.question_max_min, Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,
								Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber + (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					}

					for (int i = 0; i < tbColumns.size(); i++) {
						QuestionItem item = tbColumns.get(i);
						item.itemValue = i;
						SeekBar sb = new SeekBar(NativeReviewActivity.this);
						sb.setClickable(false);
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

						TextView tvRight = new TextView(NativeReviewActivity.this);
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
							LinearLayout ll = new LinearLayout(NativeReviewActivity.this);
							ll.setOrientation(LinearLayout.HORIZONTAL);
							ll.setLayoutParams(FILL_WRAP);
							ll.setPadding(5, 5, 20, 5);

							TextView tvLeft = new TextView(NativeReviewActivity.this);
							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(item.leftsideWord);

							/**
							 * 显示当前的刻度
							 */
							tvRight.setText(item.rightsideWord + "(" + sb.getProgress() + "/" + sb.getMax() + ")");

							LinearLayout rightLL = new LinearLayout(NativeReviewActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvRight, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							bodyView.addView(tvLeft, bodyView.getChildCount());
							bodyView.addView(rightLL, bodyView.getChildCount());
						} else if (!Util.isEmpty(item.leftsideWord)) {
							/**
							 * 左边有文字, 效果:左边文字,右边滚动条
							 */
							/**
							 * 次布局存放左边文字、滚动条、右边文字
							 */
							LinearLayout ll = new LinearLayout(NativeReviewActivity.this);
							ll.setOrientation(LinearLayout.HORIZONTAL);
							ll.setLayoutParams(FILL_WRAP);
							ll.setPadding(5, 5, 20, 5);

							TextView tvLeft = new TextView(NativeReviewActivity.this);
							tvLeft.setTextSize(lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(item.leftsideWord);

							/**
							 * 显示当前的刻度
							 */
							tvRight.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

							LinearLayout rightLL = new LinearLayout(NativeReviewActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvRight, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
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

							LinearLayout rightLL = new LinearLayout(NativeReviewActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvRight, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							bodyView.addView(rightLL, bodyView.getChildCount());
						} else {
							/**
							 * 只有滚动条
							 */
							tvRight.setText("(" + sb.getProgress() + "/" + sb.getMax() + ")");

							LinearLayout rightLL = new LinearLayout(NativeReviewActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvRight, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							bodyView.addView(rightLL, bodyView.getChildCount());
						}
					}
					// 一下代码不要执行
					break;
				}

				// 需要求EditText之中的条件是否符合大于、大于等于、小于、小于等于的条件
				boolean isSyb = (1 != q.qDragChecked && "figure".equals(q.freeTextSort) && //
				!Util.isEmpty(q.freeSymbol));
				if (isSyb) {
					TextView tvSyb = new TextView(NativeReviewActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_syb, Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,//
							Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber, q.freeSymbol + q.freeSumNumber + (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				} else if ("figure".equals(q.freeTextSort) && !Util.isEmpty(q.freeMaxNumber)) {
					/**
					 * 数字类型的最大值不为空
					 */
					TextView tvSyb = new TextView(NativeReviewActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(surveySize);
					tvSyb.setText(getResources().getString(R.string.question_max_min, Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,
							q.freeMaxNumber + (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				}
				if (!Util.isEmpty(tbColumns)) {// 非滚动条
					for (int i = 0; i < tbColumns.size(); i++) {
						QuestionItem item = tbColumns.get(i);
						item.itemValue = i;
						LinearLayout ll = new LinearLayout(NativeReviewActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(WRAP_WRAP);
						ll.setPadding(5, 5, 20, 5);
						EditText et = new EditText(NativeReviewActivity.this);
						et.setFocusable(false);
						et.setOnTouchListener(null);
						et.setClickable(false);
						// et.set
						if (1 != q.qDragChecked && "figure".equals(q.freeTextSort)) {
							et.setInputType(InputType.TYPE_CLASS_NUMBER);// setInputType(InputType.TYPE_CLASS_NUMBER);
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
								TextView tvLeft = new TextView(NativeReviewActivity.this);
								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(item.leftsideWord);
								TextView tvRight = new TextView(NativeReviewActivity.this);
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
							} else {
								/**
								 * 左边有文字
								 */
								String iCap = Util.getLeftCap(item.leftsideWord);
								if (!Util.isEmpty(iCap)) {
									/**
									 * @@::前面有文字
									 */
									TextView tvLeft = new TextView(NativeReviewActivity.this);
									tvLeft.setTextSize(lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									ll.addView(tvLeft, ll.getChildCount());
								}
								Spinner spLeft = new Spinner(NativeReviewActivity.this);
								spLeft.setClickable(false);
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeReviewActivity.this, R.layout.simple_spinner_adapter);
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

								TextView tvRight = new TextView(NativeReviewActivity.this);
								tvRight.setTextSize(lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(item.rightsideWord);
								ll.addView(tvRight, ll.getChildCount());
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
								TextView tvLeft = new TextView(NativeReviewActivity.this);
								tvLeft.setTextSize(lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								ll.addView(tvLeft, ll.getChildCount());
							} else if (Util.isEmpty(leftList)) {
								TextView tvLeft = new TextView(NativeReviewActivity.this);
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
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								ll.addView(et, ll.getChildCount());
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
							}
							if (!Util.isEmpty(leftList)) {
								// System.out.println("!Util.isEmpty(leftList)--->"
								// + leftList);
								Spinner spLeft = new Spinner(NativeReviewActivity.this);
								spLeft.setClickable(false);
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								ArrayAdapter<String> aa = new ArrayAdapter<String>(NativeReviewActivity.this, R.layout.simple_spinner_adapter);
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
							}
							bodyView.addView(ll, bodyView.getChildCount());
						} else if (Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {// 右边有文字
							// System.out.println("右边文字");
							/**
							 * 左边是文本框 右边是说明
							 */
							TextView tvRight = new TextView(NativeReviewActivity.this);
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
						} else {// 两边都没有文字
							// System.out.println("两边都没文字");
							if (1 == item.dateCheck) {
								et.setMinWidth(240);
								// System.out.println("1 == item.dateCheck");
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
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
						}
					}
				}// 非滚动条
			}
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
			if (1 != q.qDragChecked && //
					"figure".equals(q.freeTextSort) && //
					!Util.isEmpty(q.freeSymbol)) {
				TextView tvSyb = new TextView(this);
				tvSyb.setLayoutParams(WRAP_WRAP);
				tvSyb.setTextColor(Color.RED);
				tvSyb.setTextSize(surveySize);
				tvSyb.setText(getResources().getString(R.string.question_syb, Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber, Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber,
						q.freeSymbol + q.freeSumNumber + (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
				bodyView.addView(tvSyb, bodyView.getChildCount());
			}
			ArrayList<QuestionItem> tbRows = q.getRowItemArr();
			int textAreaRows = q.textAreaRows;// 一共几行
			if (!Util.isEmpty(tbRows)) {
				for (int i = 0; i < tbRows.size(); i++) {
					QuestionItem item = tbRows.get(i);
					item.itemValue = i;
					EditText et = new EditText(NativeReviewActivity.this);
					et.setFocusable(false);
					et.setOnTouchListener(null);
					et.setClickable(false);
					et.setTag(item);
					et.setLayoutParams(FILL_WRAP);
					et.setWidth((int) (dis.getWidth() / 1.5));
					et.setHeight(textAreaRows * dis.getHeight() / 20);
					et.setGravity(Gravity.TOP);
					// 问卷字号动态设置
					et.setTextSize(lowSurveySize);
					// System.out.println("item.itemText=" + item.itemText);
					if (!Util.isEmpty(item.itemText)) {
						TextView tvCap = new TextView(NativeReviewActivity.this);
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
				}
			}
			break;

		case Cnt.TYPE_MEDIA:// 预留多为体题型
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
					ImageView iv = new ImageView(NativeReviewActivity.this);
					iv.setLayoutParams(WRAP_WRAP);
					Uri uri = Uri.parse(Util.getMediaPath(NativeReviewActivity.this, q.surveyId, q.qMediaSrc));
					iv.setImageURI(uri);
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
					ImageView iv = new ImageView(NativeReviewActivity.this);
					iv.setPadding(60, 60, 60, 60);
					iv.setBackgroundResource(R.drawable.shape_bg);
					iv.setLayoutParams(WRAP_WRAP);
					iv.setImageResource(R.drawable.play);
					final Question _q = q;
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(NativeReviewActivity.this, VideoPlayerActivity.class);
							it.putExtra("path", Util.getMediaPath(_q.surveyId, _q.qMediaSrc));
							startActivity(it);
						}
					});
					bodyView.addView(iv, bodyView.getChildCount());
				}

			}
			break;
		}
		ll.addView(questionView, ll.getChildCount());
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... param) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private final class CustomViewFactor implements ViewFactory {

		@Override
		public View makeView() {
			ImageView i = new ImageView(NativeReviewActivity.this);
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
						if (1048576 > len) { // 小于1024k
							opts.inSampleSize = 3;
						} else {
							opts.inSampleSize = 6;
						}
						// resizeBmp = BitmapFactory.decodeFile(file.getPath(),
						// opts);
						// Bitmap resizeBmp = ;
						
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

	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
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
			long mp3coint=ma.dbService.getAllmp3Count(feed.getUuid(), feed.getUserId());
			long mp4coint=ma.dbService.getAllmp4Count(feed.getUuid(), feed.getUserId());
			// 添加按位置添加      ic_menu_mapmode_2   ic_menu_paste_holo_light_2   ic_menu_refresh_2   ic_menu_archive  
			menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT, NativeReviewActivity.this.getString(R.string.read_voice,mp3coint+""), R.drawable.test_zsj33);
			menuListView.add(HotalkMenuView.MENU_ADD_CONTACT, NativeReviewActivity.this.getString(R.string.read_vedio,mp4coint+""), R.drawable.test_zsj33);
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
				
				case HotalkMenuView.MENU_VIEW_CONTACT:
//					System.out.println("查看地图");
					view.setBackgroundColor(Color.YELLOW);
					Intent it = new Intent(NativeReviewActivity.this, RecordActivity.class);
					it.putExtra("feed", feed);
					it.putExtra("checkrecord", "3");
					startActivity(it);
					overridePendingTransition(R.anim.zzright, R.anim.zzleft);
					break;
				case HotalkMenuView.MENU_ADD_CONTACT:
//					System.out.println("访前说明");
					view.setBackgroundColor(Color.YELLOW);
					Intent itmp4 = new Intent(NativeReviewActivity.this, RecordActivity.class);
					itmp4.putExtra("feed", feed);
					itmp4.putExtra("checkrecord", "4");
					startActivity(itmp4);
					overridePendingTransition(R.anim.zzright, R.anim.zzleft);
					break;
				default:
					break;
				}
				// 关闭
				menuListView.close();
			}

		};
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.native_left_iv:
			NativeReviewActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.native_opt:
			super.openOptionsMenu();
			break;
		default:
			break;
		}
	}
}
