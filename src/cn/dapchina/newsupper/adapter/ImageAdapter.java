package cn.dapchina.newsupper.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.util.Util;

public class ImageAdapter extends BaseAdapter {

	//private Context mContext;
	
	private ArrayList<UploadFeed> mImages;
	
	private LayoutInflater inflater;
	
	public ImageAdapter(Context _context, ArrayList<UploadFeed> _images){
		//this.mContext = _context;
		this.mImages = _images;
		inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		if(!Util.isEmpty(mImages)){
			return mImages.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(!Util.isEmpty(mImages)){
			return mImages.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void refresh(ArrayList<UploadFeed> _images){
		if(!Util.isEmpty(mImages)){
			mImages.clear();
			mImages.addAll(_images);
			notifyDataSetChanged();
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.image_item, null);
			vh = new ViewHolder();
			vh.iv = (ImageView) convertView.findViewById(R.id.show_iv);
			vh.iv.setPadding(3, 3, 3, 3);
			vh.iv.setBackgroundColor(Color.WHITE);
			vh.iv.setAdjustViewBounds(true);
			//vh.iv.setIm
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		UploadFeed image = mImages.get(position);
		if(null != image){
			//Log.i("img", image.getName());
			showImages(vh.iv, image.getPath()+File.separator+image.getName());
		}
		return convertView;
	}


	private static class ViewHolder{
		ImageView iv;
	}

	private void showImages(ImageView imageView, String path){
		LoadImageTask imageTask = new LoadImageTask(imageView, path);
		imageTask.execute();
	}
	
	private final class LoadImageTask extends AsyncTask<Void, Integer, Bitmap>{
		ImageView imageView = null;
		String imagePath = null;
		
		public LoadImageTask(ImageView _imageView, String _imagePath){
			this.imageView = _imageView;
			this.imagePath = _imagePath;
		}
		
		@Override
		protected Bitmap doInBackground(Void... params) {
			//imageView = (ImageView) params[0];
			//imagePath = (String) params[1];
			try {
				return fitSizePic(new File(this.imagePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
//			/**此处为解决2.1及其以下无法显示的问题**/
//			File file = null;
//			if(null != result)
//				file = new File(result.getPath());
//			
//			if(0<file.length()){
//				imageView.setImageBitmap(fitSizePic(file));	
//			}else{
//				//loading_fail
//			}
			//imageView.setImageURI(Uri.parse(file.toString()));
			
			if(null != result){
					imageView.setImageBitmap(result);
			}
			
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}
	
	
	public Bitmap fitSizePic(File file) throws FileNotFoundException {
		
		//File file = new File(path);
		// Bitmap bitMap = Util.fitSizePic(file);
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 数字越大读出的图片占用的heap越小 不然总是溢出
		long len = file.length();
		if (1048576 > len) { //小于1024k
			opts.inSampleSize = 8;
		} else{
			opts.inSampleSize = 10;
		}
		//resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		resizeBmp = BitmapFactory.decodeStream(new FileInputStream(file), null, opts);
		return resizeBmp;
	}
	
}
