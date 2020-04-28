package cn.dapchina.newsupper.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Parameter;
import cn.dapchina.newsupper.bean.Survey;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.util.Util;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;

public class ChoiceModeActivity extends Activity implements OnClickListener {
	private LinearLayout choicell;
	private UploadFeed feed;
	private TextView tvfw, tvyl;// 访问，预览
	private Survey survey;// 问卷
	private Double lat, lng;

	private String addr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_mode_activity);
//		this.setFinishOnTouchOutside(false);
		choicell = (LinearLayout) findViewById(R.id.ll);
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (screenWidth / 2), (int) (screenHeigh / 4));
		choicell.setLayoutParams(params);
		feed = (UploadFeed) getIntent().getSerializableExtra("uf");
		survey = (Survey) getIntent().getSerializableExtra("survey");
		tvfw = (TextView) findViewById(R.id.tvfw);
		tvyl = (TextView) findViewById(R.id.tvyl);
		tvfw.setOnClickListener(this);
		tvyl.setOnClickListener(this);

		//  大树拒访    如果访问状态  0 表示正常  2代表拒访  
		if (feed.getReturnTypeId()==0||feed.getReturnTypeId()==-2) {
			switch (feed.getIsCompleted()) {
			// 只是开始访问
			case Cnt.VISIT_STATE_NOACCESS:
				// 预览隐藏
				tvyl.setVisibility(View.GONE);
				break;
			// 中断断续
			case Cnt.VISIT_STATE_INTERRUPT:
				if (feed.getVisitMode() == 1) {

				} else {
					// WEB模式隐藏
					tvyl.setVisibility(View.GONE);
				}
				break;
			// 完成
			case Cnt.VISIT_STATE_COMPLETED:
				// 已上传不能访问了
				if (Cnt.UPLOAD_STATE_UPLOADED <= feed.getIsUploaded()) {
					tvfw.setVisibility(View.GONE);
				} else {

				}
				break;
			}
			//  大树拒访    隐藏预览  上传显示  
		}else {
			if (feed.getIsUploaded()<1) {
				tvfw.setVisibility(View.VISIBLE);
				tvyl.setVisibility(View.GONE);
			}else {
				tvfw.setVisibility(View.GONE);
				tvyl.setVisibility(View.VISIBLE);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvfw:
			feed.setSurvey(survey);

			if (null == lat || null == lng || Util.isEmpty(addr)) {
				BDLocation loc = VisitActivity.getBDLocation();// 地图位置传入更改this.getBDLocation()
				if (null != loc) {
					lat = loc.getLatitude();
					lng = loc.getLongitude();
					addr = loc.getAddrStr();
				} else {
					lat = null;
					lng = null;
					addr = "";
				}
			}

			/**
			 * 假如经纬度中存在E或e 则将其置为空串
			 */
			if (null != lat) {
				String latStr = String.valueOf(lat);
				if (-1 < latStr.indexOf("E") || -1 < latStr.indexOf("e")) {
					feed.setLat("");
				} else {
					feed.setLat(latStr);
				}
			} else {
				feed.setLat("");
			}

			if (null != lng) {
				String lngStr = String.valueOf(lng);
				if (-1 < lngStr.indexOf("E") || -1 < lngStr.indexOf("e")) {
					feed.setLng("");
				} else {
					feed.setLng(lngStr);
				}
			} else {
				feed.setLng("");
			}

			// 命名规则开始
			String content = "";
			String parameterName = survey.getParameterName();
			ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
			String parametersStr = feed.getParametersStr();
			if (!Util.isEmpty(parametersStr)) {
				parameterList.clear();
				ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr, Parameter.class);
				if (!Util.isEmpty(tParameters)) {
					parameterList.addAll(tParameters);
				}
			}
			if (!Util.isEmpty(parameterName) && !Util.isEmpty(parameterList)) {
				for (Parameter parameter : parameterList) {
					if (parameter.getSid().equals(parameterName)) {
						content = parameter.getContent();
						// 是中文的就置为空
						if (Util.isContainChinese(content)) {
							content = "";
						}
						break;
					}
				}
			}
			// 命名规则结束

			if (1 == feed.getVisitMode()) {
				Intent it=null;
				if (1 == survey.openStatus) {
					it = new Intent(this, CheckAddrActivity.class);
				} else {
					it = new Intent(this, NativeModeActivity.class);
				}
				Bundle bundle = new Bundle();
				feed.setVisitAddress(Util.isEmpty(addr) ? "" : addr);
				feed.setSurveyTitle(survey.surveyTitle);
				// 命名规则设置参数
				feed.setParametersContent(content);
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				this.startActivity(it);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
			} else {
				// web模式
				Intent it=null;
				if (1 == survey.openStatus) {
					it = new Intent(this, CheckAddrActivity.class);
				} else {
					it = new Intent(this, WebModeActivity.class);
				}
				Bundle bundle = new Bundle();
				feed.setSurveyTitle(survey.surveyTitle);
				feed.setVisitAddress(Util.isEmpty(addr) ? "" : addr);
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				this.startActivity(it);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
			}
			break;
		case R.id.tvyl:
			feed.setSurvey(survey);
			if (1 == feed.getVisitMode()) {
				Intent it = new Intent(this, NativeReviewActivity.class);
				Bundle bundle = new Bundle();
				feed.setSurveyTitle(survey.surveyTitle);
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				this.startActivity(it);
				// 大树 关闭界面
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
			} else {
				Intent it = new Intent(this, WebModeActivity.class);
				Bundle bundle = new Bundle();
				// f.setVisitAddress(Util.isEmpty(addr)?"":addr);
				feed.setIsReview(100);
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				this.startActivity(it);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
			}
			break;

		default:
			break;
		}
	}

}
