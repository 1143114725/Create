package cn.dapchina.newsupper.activity;

import java.io.File;
import java.util.Arrays;

import java.util.List;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.adapter.AttachAdapter;
import cn.dapchina.newsupper.bean.Knowledge;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class KnowledgeDetailActivity extends Activity implements OnClickListener {

	private ImageView knowleage_left_iv;// 返回
	private Knowledge knowledge;
	private TextView tvContent, tvTitle, tvKind;
	private SlidingDrawer sdImages;
	private ListView lv;
	private AttachAdapter attachAdapter;
	private List<String> list;

	/**
	 * 设备屏幕的宽
	 */
	private int screenWidth;

	/**
	 * 设备屏幕的高
	 */
	private int screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.knowledgedetail);
		knowledge = (Knowledge) getIntent().getSerializableExtra("knowledge");
		knowleage_left_iv = (ImageView) findViewById(R.id.knowleage_left_iv);
		knowleage_left_iv.setOnClickListener(this);
		tvContent = (TextView) findViewById(R.id.tvContent);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvKind = (TextView) findViewById(R.id.tvKind);
		ImageGetter imgGetter = new Html.ImageGetter() {
			public Drawable getDrawable(String source) {
				Drawable drawable = null;
				String name = KnowledgeDetailActivity.this.getFilesDir() + File.separator + "survey" + File.separator + knowledge.getKind() + File.separator + source;
				// System.out.println("name:" + name);
				drawable = Drawable.createFromPath(name);
				Bitmap image = BitmapFactory.decodeFile(name);
				if (image != null) {
					int tWidth = image.getWidth();
					int tHeight = image.getHeight();
					if(tWidth>screenWidth){
						tWidth=screenWidth;
						tHeight=screenWidth/tWidth*tHeight;
					}
					drawable.setBounds(0, 0, tWidth, tHeight);
					return drawable;
				} else {
					return null;
				}
			}
		};
		tvContent.setText(Html.fromHtml(knowledge.getContent(), imgGetter, null));
		tvTitle.setText(knowledge.getTitle());
		tvKind.setText("★ " + knowledge.getKind());
		initView();
	}

	private void initView() {
		sdImages = (SlidingDrawer) this.findViewById(R.id.pic_sd);
		// 大树知识库
		if (knowledge.getFileName().contains(";")) {
			String[] split = knowledge.getFileName().split(";");
			list = Arrays.asList(split);
		}else {
			sdImages.setVisibility(View.GONE);
		}
		// 
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		LayoutParams layoutParams = new LayoutParams(screenWidth, screenHeight / 2);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		sdImages.setLayoutParams(layoutParams);
		/**
		 * 实例化抽屉控件装载图片的GridView，控制平板本地预览
		 */
		lv = (ListView) this.findViewById(R.id.listView1);
		sdImages.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 开抽屉
		{
			@Override
			public void onDrawerOpened() {
				attachAdapter = new AttachAdapter(list, KnowledgeDetailActivity.this);
				lv.setAdapter(attachAdapter);
			}
		});
		// 关闭
		sdImages.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			KnowledgeDetailActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.knowleage_left_iv:
			KnowledgeDetailActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		default:
			break;
		}

	}

}
