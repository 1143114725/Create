package cn.dapchina.newsupper.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.view.Toasts;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;

public class ItemsOverlay extends ItemizedOverlay<OverlayItem> {
	/**点击mark时弹出的气泡View**/
	private View mPopView = null;	
	private Drawable marker;
	private Context mContext = null;
	private MapView mMapView = null;
	private List<OverlayItem> mOverlayItems = new ArrayList<OverlayItem>();
	
	private double mLat1 = 39.90923; // point1纬度
	private double mLon1 = 116.357428; // point1经度

	private double mLat2 = 39.90923;
	private double mLon2 = 116.397428;

	private double mLat3 = 39.90923;
	private double mLon3 = 116.437428;
	public ItemsOverlay(Drawable _marker,
			Context _context, MapView _mapView) {
		super(boundCenterBottom(_marker));
		this.mContext = _context;
		this.marker = _marker;
		this.mMapView = _mapView;
		/**创建点击mark时的弹出泡泡**/
		mPopView = LayoutInflater.from(_context).inflate(R.layout.popview, null);;
		/**用给定的经纬度构造GeoPoint，
		 * 单位是微度 (度 * 1E6)**/
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));

		/**构造OverlayItem的三个参数依次为：
		 * item的位置，标题文本，文字片段**/
		mOverlayItems.add(new OverlayItem(p1, "P1", "point1"));
		mOverlayItems.add(new OverlayItem(p2, "P2", "point2"));
		mOverlayItems.add(new OverlayItem(p3, "P3", "point3"));	
		 /**createItem(int)方法构造item。
		  * 一旦有了数据，
		  * 在调用其它方法前，
		  * 首先调用这个方法**/
		populate(); 
		
		mMapView.addView( mPopView,
                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		null, MapView.LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);
	}

	@Override
	protected OverlayItem createItem(int index) {
		return mOverlayItems.get(index);
	}

	@Override
	public int size() {
		return mOverlayItems.size();
	}

	@Override
	protected boolean onTap(int index) {
		setFocus(mOverlayItems.get(index));
		/**更新气泡位置,并使之显示**/
		GeoPoint pt = mOverlayItems.get(index).getPoint();
		mMapView.updateViewLayout( mPopView,
                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		pt, MapView.LayoutParams.BOTTOM_CENTER));
		mPopView.setVisibility(View.VISIBLE);
		Toasts.makeText(this.mContext, mOverlayItems.get(index).getSnippet(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		/**Projection接口用于
		 * 屏幕像素坐标和经纬度坐标之间的变换**/
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) { /** 遍历mOverlayItems**/
			OverlayItem overLayItem = getItem(index); 		/**得到给定索引的item**/
			String title = overLayItem.getTitle();
			/**把经纬度变换到相对于MapView左上角的屏幕像素坐标**/
			Point point = projection.toPixels(overLayItem.getPoint(), null); 
			/**可在此处添加您的绘制代码**/
			Paint paintText = new Paint();
			paintText.setColor(Color.BLUE);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x-30, point.y, paintText); /** 绘制文本 **/
		}
		super.draw(canvas, mapView, shadow);
		/**调整一个drawable边界，
		 * 使得(0，0)是这个drawable底部最后一行中心的一个像素**/
		boundCenterBottom(marker);
	}

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		/**消去弹出的气泡**/
		mPopView.setVisibility(View.GONE);
		return super.onTap(arg0, arg1);
	}
	
}
