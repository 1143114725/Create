package cn.dapchina.newsupper.activity;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.global.MyApp;

public class ShowImageActivity extends Activity {

	private ImageView iv = null;
	private MyApp ma;
	//private RelativeLayout imageRe;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_show_image);
		ma=(MyApp) getApplication();
		ma.addActivity(this);
		//imageRe=(RelativeLayout) findViewById(R.id.imageRe);
		//WindowManager windowManager = getWindowManager();     
		//Display display = windowManager.getDefaultDisplay();    
		//int width=display.getWidth();
		//int height=display.getHeight();
		//LayoutParams params=new LayoutParams((int)(width/1.5),(int)(height/1.5));
		//imageRe.setLayoutParams(params);
		String path = getIntent().getExtras().getString("image_path");
		Bitmap bd = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			File file = new File(path);
			// 数字越大读出的图片占用的heap越小 不然总是溢出
//			long len = file.length();
//			if (1048576 > len) { // 小于1024k
//				opts.inSampleSize = 3;
//			} else {
//				opts.inSampleSize = 6;
//			}
			bd = BitmapFactory.decodeStream(new FileInputStream(file), null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 iv = (ImageView) findViewById(R.id.show_iv);
		 if(null!=bd){
			iv.setImageBitmap(bd) ;
		 }
	     iv.setOnTouchListener(new ImageTouchListener());
	}
	
	  private class ImageTouchListener implements OnTouchListener{
	    	/**控件开始点坐标**/
	    	private PointF startPoint;
	    	/**使用矩阵控制图片的移动**/
	    	private Matrix matrix = new Matrix();
	    	/**记录当前的位置**/
	    	private Matrix currentMatrix = new Matrix();
	    	/**刚开始的距离**/
	    	private float startDistance;
	    	/**拖拉**/
	    	private final static int DRAG = 1;
	    	/**缩放**/
	    	private final static int ZOOM = 2;
	    	/**手指的操作模式**/
	    	private int mode;
	    	/**两手指的中间点坐标**/
	    	private PointF midPoint;
	    	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				iv.setScaleType(ScaleType.MATRIX);
				switch (event.getAction()&MotionEvent.ACTION_MASK) {
				/**取得一个数值的低8位**/
				case MotionEvent.ACTION_DOWN:/**按下屏幕**/
					/**用户进行拖拉操作**/
					mode = DRAG;
					/**取得开始点坐标**/
					startPoint = new PointF(event.getX(), event.getY());
					/**将控件的当前位置保存在currentMatrix矩阵中**/
					currentMatrix.set(iv.getImageMatrix());
					break;

				case MotionEvent.ACTION_MOVE:/**按住屏幕进行拖动**/
					switch (mode) {
					case DRAG:	/**拖拽操作**/
						/**x轴移动的距离**/
						float dx = event.getX() - startPoint.x;
						/**y轴移动的距离**/
						float dy = event.getY() - startPoint.y;
						/**将控件的当前位置坐标赋给matrix**/
						matrix.set(currentMatrix);
						/**将控件移动dx、dy个距离**/
						matrix.postTranslate(dx, dy);
						break;

					case ZOOM:	/**放大操作**/
						/**获取缩放后两手指之间的距离**/
						float endDistance = getDistance(event);
						if(-1000==endDistance){
							break;
						}
						System.out.println("endDistance="+endDistance);
						/**计算得到放大或缩小倍数**/
						float scale = endDistance / startDistance;
						/**设置照片当前的放大倍数**/
						matrix.set(currentMatrix);
						/**通过矩阵控制控件的放大或缩小**/
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
						break;
					}
					
					break;
					
				case MotionEvent.ACTION_POINTER_DOWN:/**一个手指在屏幕上,另一只手指又按下时**/
					/**用户进行缩放操作**/
					mode = ZOOM;
					startDistance = getDistance(event);
					/**如果两点的距离大于10个像素**/
					if(startDistance>10){
						midPoint = getMidPoint(event);
						/**获取进行操作之前的照片操作缩放倍数,
						 * 在照片目前放大倍数的基础上进行放大
						 * **/
						currentMatrix.set(iv.getImageMatrix());
					}
					break;
				
				case MotionEvent.ACTION_POINTER_UP:/**一个手指离开屏幕，另一个手指还在**/
					
					break;
					
				case MotionEvent.ACTION_UP:/**最后一个手指离开时**/
					
					break;
				}
				iv.setImageMatrix(matrix);
				/**返回true代表销毁掉这个事件**/
				return true;
			}

	    }
	    
	    /**
	     * 获取两点之间的距离
	     * **/
	    public static float getDistance(MotionEvent event){
	    	/**x坐标的矢量**/
	    	float dx = 0;
	    	/**y坐标的矢量**/
	    	float dy = 0;
	    	boolean yes = false;
	    	try {
	    		dx = event.getX(1) - event.getX(0);
	    		dy = event.getY(1) - event.getY(0);
	    		yes = true;
			} catch (Exception e) {
				e.printStackTrace();
				yes = false;
			}
	    	if(yes){
	    		/**运用勾股定理计算两点的距离**/
		    	return FloatMath.sqrt(dx * dx + dy* dy);
	    	}else{
	    		return -1000;
	    	}
	    }
	    
	    /**获取手指中间点的坐标**/
	    public static PointF getMidPoint(MotionEvent event) {
			return new PointF(event.getX(1)/2 + event.getX(0)/2, event.getY(1)/2 + event.getY(0)/2);
			
		}
	    
	    @Override
		protected void onDestroy() {
			ma.remove(this);
			super.onDestroy();
		}
}

