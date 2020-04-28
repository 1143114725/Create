package cn.dapchina.newsupper.newphoto.photo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.newphoto.util.PictureManageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AlbumAdapter extends BaseAdapter {
	private List<Album> albumList;
	private Context context;
	private ViewHolder holder;
	private Map<String, Bitmap> cacheBms = new HashMap<String, Bitmap>();

	public AlbumAdapter(List<Album> list, Context context) {
		this.albumList = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return albumList.size();
	}

	@Override
	public Object getItem(int position) {
		return albumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_album_item, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.photoalbum_item_image);
			holder.tv = (TextView) convertView.findViewById(R.id.photoalbum_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (cacheBms.get(position + "") == null) {
			// 通过ID 获取缩略图
			Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), albumList.get(position).getBitmap(), Thumbnails.MICRO_KIND, null);
			int rotate = PictureManageUtil.getCameraPhotoOrientation(albumList.get(position).getBitList().get(0).getPhotoPath());
			bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
			holder.iv.setImageBitmap(bitmap);
			cacheBms.put(position + "", bitmap);
		} else {
			holder.iv.setImageBitmap(cacheBms.get(position + ""));
		}
		holder.tv.setText(albumList.get(position).getName() + " ( " + albumList.get(position).getCount() + " )");
		return convertView;
	}

	static class ViewHolder {
		ImageView iv;
		TextView tv;
	}

}
