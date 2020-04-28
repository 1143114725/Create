/**
 *
 */
package cn.dapchina.newsupper.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.adapter.RecordAdapter;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.Cnt;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.Util;

/**
 * 张树锦与2016-1-27下午4:09:09TODO
 */
public class RecordActivity extends BaseActivity implements OnItemClickListener,OnClickListener{

	private UploadFeed feed;                   // 大树   传递过来的问卷
	private MyApp ma; 
	
	private ListView record_list;
	private TextView no_record;
	private ArrayList<UploadFeed> uf;
	private String checkrecord;
	private ImageView recod_back_iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
//		this.setFinishOnTouchOutside(false);
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		android.view.WindowManager.LayoutParams p = getWindow().getAttributes(); 
		p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.8
		p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.7
		getWindow().setAttributes(p);
		
		feed=(UploadFeed) getIntent().getSerializableExtra("feed");
		checkrecord=(String) getIntent().getSerializableExtra("checkrecord");
		
		init();
		recod_back_iv.setOnClickListener(this);
		if(feed!=null){
			uf=ma.dbService.getRecordorvideo(feed.getUuid(), feed.getSurveyId(),checkrecord);
			RecordAdapter readapter=new RecordAdapter(this,uf);
			record_list.setAdapter(readapter);
			record_list.setEmptyView(no_record);
			if(uf!=null){
				record_list.setOnItemClickListener(this);
			}
		}
		/*if (Util.readSDCard()[1]>=0.1) {
			//  存到本地 SDcard 中   
			path=Util.getRecordPath(feed.getSurveyId());
		}else {  
			//  存到 系统内置卡  里头  
			path = Util.getRecordInnerPath(RecordActivity.this, feed.getSurveyId());
		}*/
		setFinishOnTouchOutside(true);
	}

	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}

	@Override
	public void init() {
		record_list= (ListView) findViewById(R.id.record_list);
		no_record= (TextView) findViewById(R.id.no_record_list_tv);
		recod_back_iv= (ImageView) findViewById(R.id.recod_back_iv);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
	

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(checkrecord.equals("3")){
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.parse("file://" +uf.get(position).getPath() +"/"+uf.get(position).getName()), "audio/MP3");
			startActivity(it);     
		}else if (checkrecord.equals("4")) {
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.parse("file://" +uf.get(position).getPath() +"/"+uf.get(position).getName()), "video/mp4");
			startActivity(it);    
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recod_back_iv:
			RecordActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		default:
			break;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see cn.dapchina.newsupper.activity.BaseActivity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
        float y = event.getY();
        if(y < 0 || y>950){
        	this.finish();
        }
        if(x < 0 || x>900){
        	this.finish();
        }
        
        return super.onTouchEvent(event);
	}
	
	

}
