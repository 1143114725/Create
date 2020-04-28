package cn.dapchina.newsupper.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.bean.Question;
import cn.dapchina.newsupper.bean.UploadFeed;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.newphoto.photo.Item;
import cn.dapchina.newsupper.newphoto.photo.PhotoAlbumActivity;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.util.Util;
import cn.dapchina.newsupper.view.Toasts;

public class PhotoActivity extends Activity {

	// private ImageView img_EditAvatorImage;
	/**
	 * 拍照
	 */
	public static final int mPHOTOHRAPH = 1;
	/**
	 * 缩放
	 */
	public static final int mPHOTOZOOM = 2; 
	/**
	 * 结果
	 */
	public static final int mPHOTORESOULT = 3;
	public static final String mIMAGE_UNSPECIFIED = "image/*";
	/**
	 * 照相bitmap
	 */
	private Bitmap photoBitmap;
	/**
	 * 应用程序
	 */
	private MyApp ma;
	/**
	 * feed
	 */
	private UploadFeed feed;
	// public static String tempName = null;

	/**
	 * 临时文件
	 */
	public volatile File tempFile = null;

	/**
	 * 确认按钮，取消按钮。
	 */
	private Button btnOk, btnCancel;

	/**
	 * 刷新
	 */
	private ImageView ivChange;

	/**
	 * 显示预览照片
	 */
	private ImageView ivShow;

	// AlertDialog.Builder builder;

	/**
	 * 选择方式dialog
	 */
	private Dialog dialog;

	/**
	 * 底部菜单
	 */
	private View vYesNo;

	/**
	 * 图像矩阵
	 */
	private Matrix mMatrix;

	/**
	 * 旋转次数
	 */
	private int change;

	/**
	 * 等待View
	 */
	private View vWait;

	/**
	 * 返回的状态码 确定拍照 还是摄像
	 */
	private int SAVE_TYPE = 0;
	/**
	 * 工具类
	 */
	private Config cfg;
	/**
	 * 问题类
	 */
	private Question question;

	/**
	 *  摄像还是拍照参数
	 */
	private String opt = "";
	/**
	 * 横竖屏问题
	 */
	private int screen;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		/** 将继基类所有的Activity横屏放置 **/
		setContentView(R.layout.activity_photo);
		overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		/**
		 * 0竖屏 1横屏
		 */
		screen = ma.cfg.getInt("ScreenOrientation", 0);
		/**
		 * 初始化问卷字号动态设置完毕
		 */
		if (1 == screen) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		ivShow = (ImageView) findViewById(R.id.show_tv);
		vYesNo = findViewById(R.id.yes_no_ll);
		vWait = findViewById(R.id.wait_ll);
		onEditAvator();
		btnOk = (Button) findViewById(R.id.photo_ok_btn);
		ivChange = (ImageView) findViewById(R.id.photo_change_iv);
		btnCancel = (Button) findViewById(R.id.photo_giveup_btn);
		feed = (UploadFeed) getIntent().getExtras().get("photo");
		question = (Question) getIntent().getExtras().get("question");
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//没有选择照片
				if (0 == SAVE_TYPE) {
					Toasts.makeText(PhotoActivity.this, R.string.image_null, Toast.LENGTH_SHORT).show();
					PhotoActivity.this.finish();
				} else {
					//100或者200，证明获得图片
					if (100 == SAVE_TYPE || 200 == SAVE_TYPE) {
						new SavePhotoTask(SAVE_TYPE).execute();
					}
					// 判断确认是摄像
					else if (300 == SAVE_TYPE) {
//						Log.i("zrl1", "zou zhe li");
						Toasts.makeText(PhotoActivity.this, R.string.video_obtain, Toast.LENGTH_SHORT).show();
						new SaveVideoTask(SAVE_TYPE).execute();
					}
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toasts.makeText(PhotoActivity.this, R.string.image_cancle, Toast.LENGTH_SHORT).show();
				if (200 == SAVE_TYPE && null != tempFile && tempFile.exists()) {
					if (tempFile.delete()) {
						// System.out.println("btnCancel--->删除成功");
					}
				}
				PhotoActivity.this.finish();
			}
		});

		ivChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null == mMatrix) {
					mMatrix = new Matrix();
				}
				mMatrix.reset();
				change++;
				change = (4 == change ? change = 1 : change);
				mMatrix.postRotate(change * 90);
				Bitmap bit = Bitmap.createBitmap(//
						photoBitmap, 0, 0, //
						photoBitmap.getWidth(), //
						photoBitmap.getHeight(), //
						mMatrix, true);//
				BitmapDrawable bd = new BitmapDrawable(bit);
				ivShow.setImageDrawable(bd);

			}
		});
		
	}

	/** 编辑头像 **/
	private void onEditAvator() {
		// 获取摄像还是拍照参数
		opt = getIntent().getExtras().getString("opt");
		vYesNo.setVisibility(View.GONE);
		dialog = new Dialog(PhotoActivity.this, R.style.question_ds);
		dialog.setContentView(R.layout.photo_dialog);
		dialog.setCanceledOnTouchOutside(true);
		//选择照片方式
		TextView tv = (TextView) dialog.findViewById(R.id.msg_tv);
		tv.setText(getResources().getString(R.string.image_style));
		//照相
		Button mAvatarEditCamera = (Button) dialog.findViewById(R.id.avatar_edit_camera);
		//相册
		Button mAvatarEditAlbum = (Button) dialog.findViewById(R.id.avatar_edit_album);
		// 摄像按钮
		Button mAvatarEditeRecording = (Button) dialog.findViewById(R.id.avatar_edit_recording);
		// 摄像按钮点击
		mAvatarEditeRecording.setOnClickListener(onButtonClick);
		
		mAvatarEditAlbum.setOnClickListener(onButtonClick);
		mAvatarEditCamera.setOnClickListener(onButtonClick);
		// 判断摄像还是拍照隐藏
		if ("0".equals(opt)) {
			mAvatarEditeRecording.setVisibility(View.GONE);
		} else if ("1".equals(opt)) {
			mAvatarEditCamera.setVisibility(View.GONE);
			mAvatarEditAlbum.setVisibility(View.GONE);
		} else if (Util.isEmpty(opt)) {
			mAvatarEditeRecording.setVisibility(View.GONE);
		}
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					finish();
				}
				return true;
			}
		});
		try {
			if (!PhotoActivity.this.isFinishing())
				dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 多选图片
	/* 用来标识请求gallery的activity */
	private final int PHOTO_PICKED_WITH_DATA = 3021;
	// 所选图的信息(主要是路径)
	private ArrayList<Item> photoList = new ArrayList<Item>();

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			final ProgressDialog dialog;
			dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置为圆形
			dialog.setMessage("数据加载中...");
			dialog.setIndeterminate(false);//
			// dialog.setCancelable(true); //按回退键取消
			dialog.show();
			Window window = dialog.getWindow();
			View view = window.getDecorView();
			// Tools.setViewFontSize(view,21);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					// 初始化提示框
					dialog.dismiss();
				}

			}, 1000);
			// final Intent intent = new
			// Intent(MainActivity.this,GetAllImgFolderActivity.class);
			final Intent intent = new Intent(PhotoActivity.this, PhotoAlbumActivity.class);
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "图库中找不到照片", Toast.LENGTH_LONG).show();
		}
	}
	
	/** 选择编辑头像的方式 **/
	private OnClickListener onButtonClick = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.avatar_edit_album:
				// 可做兼容
				// Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				// getAlbum.setType(mIMAGE_UNSPECIFIED);
				// startActivityForResult(getAlbum, 100);
				// 多选图片
				// 打开选择图片界面
				doPickPhotoFromGallery();
				break;

			case R.id.avatar_edit_camera:
				String name = System.currentTimeMillis() + ".png";
				if (null == cfg) {
					cfg = new Config(PhotoActivity.this);
				}
				//保存image_path的名字
				cfg.putString("image_path", name);
				tempFile = new File(Environment.getExternalStorageDirectory(), name);
				Toasts.makeText(PhotoActivity.this, R.string.camera_select, Toast.LENGTH_SHORT).show();
				//捕获
				Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intents.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				startActivityForResult(intents, 200);
				break;
			// 摄像按钮事件
			case R.id.avatar_edit_recording:
				// String audioName=System.currentTimeMillis()+".mp4";
				// if (null==cfg) {
				// cfg=new Config(PhotoActivity.this);
				// }
				// cfg.putString("audio_path", audioName);
				// tempFile=new
				// File(Environment.getExternalStorageDirectory(),audioName);
				// 进行录制，可以该表一下地址。
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(intent, 300);
				break;
			default:
				break;
			}

			try {
				if (!PhotoActivity.this.isFinishing())
					dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/** 选择相片放回处理 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		System.out.println("requestCode:"+requestCode+"\tresultCode:"+resultCode);
		if (resultCode == 0) {
			//多选图片
			if(requestCode==PHOTO_PICKED_WITH_DATA){
				//多选图片就直接关闭了
				finish();
			}
			if (data == null || data.getData() == null) {
				dialog.show();
			}
			return;
		}

		/**
		 * 摄像 获取 系统 视频
		 */
		if (requestCode == 300) {
			SAVE_TYPE = 300;
			vYesNo.setVisibility(View.VISIBLE);
			// 隐藏选装
			ivChange.setVisibility(View.GONE);
			Log.i("zrl1", data + "");
			Log.i("zrl1", data.getData().getPath());
			Uri uri = data.getData();
			ContentResolver cr1 = getContentResolver();
			Cursor cursor = managedQuery(uri, new String[] { MediaStore.Video.Media.DATA }, null, null, null);
			if (cursor.moveToFirst()) {
				String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
				Log.i("zrl1", "视频的路径！" + path);
				if (!Util.isEmpty(path)) {
					tempFile = new File(path);
					/**
					 * 摄像 ： 获取 视频流的 缩率图 显示在ivShow上头 ！
					 */
					if (!tempFile.exists()) {
						try {
							tempFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, Thumbnails.MINI_KIND);
					ivShow.setImageBitmap(bitmap);
				}

			}
		}

		/**
		 * 用相机拍出来的照片
		 */
		if (requestCode == 200) {
			SAVE_TYPE = 200;
			vYesNo.setVisibility(View.VISIBLE);
			// photo =
			// BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+
			// File.separator + tempName);
			try {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				if (null == tempFile) {
					if (null == cfg) {
						cfg = new Config(this);
					}
					String name = cfg.getString("image_path", "");
					if (Util.isEmpty(name)) {
						tempFile = null;
					} else {
						tempFile = new File(Environment.getExternalStorageDirectory(), name);
						cfg.putString("image_path", "");
					}

				}
				if (null != tempFile) {
					// 数字越大读出的图片占用的heap越小 不然总是溢出
					long len = tempFile.length();
					if (1048576 > len) { // 小于1024k, 即1M的
						opts.inSampleSize = 3;
					} else {
						/**
						 * 大于1M的
						 */
						opts.inSampleSize = 6;
					}
					photoBitmap = BitmapFactory.decodeStream(new FileInputStream(tempFile), null, opts);
					ivShow.setImageBitmap(photoBitmap);
					if (null != dialog && dialog.isShowing()) {
						if (!PhotoActivity.this.isFinishing())
							dialog.dismiss();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/**
		 * 做兼容 从相册中选择图片
		 */
		// if (requestCode == 100) {
		// SAVE_TYPE = 100;
		// vYesNo.setVisibility(View.VISIBLE);
		// try {
		// ContentResolver resolver = getContentResolver();
		// Uri originalUri = data.getData(); // 获得图片的uri
		// // photoBitmap = MediaStore.Images.Media.getBitmap(resolver,
		// originalUri); // 显得到bitmap图片
		// //System.out.println("originalUri--->"+originalUri);
		// InputStream input = resolver.openInputStream(originalUri);
		// BitmapFactory.Options bmpFactoryOptions = new
		// BitmapFactory.Options();
		// bmpFactoryOptions.inSampleSize = 2;
		// photoBitmap = BitmapFactory.decodeStream(input, null,
		// bmpFactoryOptions);
		// input.close();
		// ivShow.setImageBitmap(photoBitmap);
		// Cursor cursor = managedQuery(originalUri, new
		// String[]{MediaStore.Images.Media.DATA}, null, null, null);
		// if(cursor.moveToFirst()){
		// String path =
		// cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		// // System.out.println("相册中图片的路径="+path);
		// if(!Util.isEmpty(path)){
		// tempFile = new File(path);
		// }
		// }
		// } catch (IOException e) {
		// Log.e("MM", e.toString());
		// }
		// }

		// 多选图片
		if (requestCode == PHOTO_PICKED_WITH_DATA) {
			System.out.println("PHOTO_PICKED_WITH_DATA");
			SAVE_TYPE = 100;
			if (data == null){
				return;
			}
			photoList = data.getParcelableArrayListExtra("fileNames");
			Log.e("test", "被选中的照片" + photoList.toString());

			if (photoList == null) {
				Toasts.makeText(PhotoActivity.this, "没有选中照片，请重新选择!", Toast.LENGTH_LONG).show();
			} else {
				Toasts.makeText(PhotoActivity.this, R.string.image_obtain, Toast.LENGTH_SHORT).show();
				new SaveMuchPhotoTask(SAVE_TYPE).execute();
			}
			return;

		}

		// 拍照
		if (requestCode == mPHOTOHRAPH) {
			System.out.println("拍照");
			// 设置文件保存路径这里放在跟目录下
			// File picture = new File(Environment.getExternalStorageDirectory()
			// + "/" + tempName);
			startPhotoZoom(Uri.fromFile(tempFile));
		}

		if (data == null){
//			System.out.println("data为空");
		
			return;
		}
			
		// 读取相册缩放图片
		if (requestCode == mPHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == mPHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				photoBitmap = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photoBitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0
																				// //
				// -100)压缩文件
				ivShow.setImageBitmap(photoBitmap);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	/** 缩放到指定大小的图片 **/
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, mIMAGE_UNSPECIFIED);

		intent.putExtra("crop", "true");
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, mPHOTORESOULT);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ma.remove(this);
		if (null != photoBitmap) {
			photoBitmap.recycle();
			photoBitmap = null;
		}
		if (null != tempFile) {
			tempFile = null;
		}
		if (null != ivShow) {
			ivShow = null;
		}
		if (null != dialog) {
			try {
				dialog.dismiss();
				dialog = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 摄像重命名方法
	private static String getVideoName(String str) {
		return str.substring(0, str.length() - 4) + ".mp4";
	}

	/**
	 * 摄像 ： 保存 数据 并写入 数据库 写入文件 并删除 临时文件 ！
	 * 
	 * @author Administrator
	 * 
	 */
	private final class SaveVideoTask extends AsyncTask<Void, Integer, Void> {

		private int save_Type = 0;

		public SaveVideoTask(int save_Type) {
			super();
			this.save_Type = save_Type;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			vWait.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			/**
			 * 摄像 ： 三个任务： 搞清楚 ： 1 写入 视频 文件 并 2 删除 临时文件 3 存库 !
			 */
			// 给原名重命名
			String videoName = getVideoName(feed.getName());
			feed.setName(videoName);
			File f = new File(feed.getPath(), feed.getName());
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			if (null != tempFile && tempFile.exists()) {
				FileInputStream fis = null;
				FileOutputStream fos = null;
				try {
					fis = new FileInputStream(tempFile);
					fos = new FileOutputStream(f);
					byte[] buff = new byte[1024];
					int len;
					while (-1 != (len = fis.read(buff))) {
						fos.write(buff, 0, len);
					}
					fos.flush();
					feed.setRegTime(System.currentTimeMillis());
					feed.setSize(f.length());

					/**
					 * 摄像 添加 字段 到数据库 中 这样 才是解问题的关键 ！
					 * 
					 * 给 feed里头添加一个字段 用来判断是否是录像还是 拍照 ！ --已完成
					 * 
					 * 
					 * 下周安排 ： 工作 如下：
					 * 
					 * 在DbService里头添加一个方法 addVideo --未完成 在数据库DBOpenHelper 里头
					 * 要添加字段 这是核心关键 ！ --未完成 !
					 * 还有放弃视频，旋转等操作，需要对缩率图进行缩小以及处理以下，这样才能才算大功告成！
					 */
					ma.dbService.addVideo(feed, false, "");

					// if(question!=null){
					// ma.dbService.addPhoto(feed,(question.qCamera==1)?true:false,question.qIndex+"");
					// }else{
					// ma.dbService.addPhoto(feed,false,"");
					// }
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fis) {
							fis.close();
							fis = null;
						}
						if (null != fos) {
							fos.close();
							fos = null;
						}
						if (300 == save_Type) {
							if (tempFile.delete()) {
								tempFile = null;
								// System.out.println("文件删除成功");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			vWait.setVisibility(View.GONE);
			PhotoActivity.this.finish();
		}

	}

	private final class SavePhotoTask extends AsyncTask<Void, Integer, Integer> {
		private int saveType;
		private int isSuccess;//0是不成功 1是成功

		public SavePhotoTask(int _saveType) {
			this.saveType = _saveType;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			vWait.setVisibility(View.VISIBLE);
		}
		@Override
		protected Integer doInBackground(Void... params) {
			File f = new File(feed.getPath(), feed.getName());
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			if (null != tempFile && tempFile.exists()) {
				FileInputStream fis = null;
				FileOutputStream fos = null;
				try {
					fis = new FileInputStream(tempFile);
					fos = new FileOutputStream(f);
					// long start_1 = System.currentTimeMillis();
					// mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
					// System.out.println("用的时间============"+((System.currentTimeMillis()-start_1)/1000)+"秒");

					byte[] buff = new byte[1024];

					int len;

					while (-1 != (len = fis.read(buff))) {
						fos.write(buff, 0, len);
					}
					fos.flush();
					feed.setRegTime(System.currentTimeMillis());
//					System.out.println("第一个长度:"+f.length());
					feed.setSize(f.length());
					if(f.length()>0){
						//缩略图
						BitmapFactory.Options opts = new BitmapFactory.Options();
						// 数字越大读出的图片占用的heap越小 不然总是溢出
						long lens = f.length();
						if (1048576*3 > lens) { // 显示图片时的压缩 小于1024k, 即1M的
							opts.inSampleSize = 2;
						} else if(1048576*8>lens&&lens>=1048576*3){
							/**
							 * 大于1M的
							 */
							opts.inSampleSize = 6;
						}
						else {
							opts.inSampleSize = 10;
						}
						Bitmap oldBitmap= BitmapFactory.decodeStream(new FileInputStream(f),null,opts);
						
						float width = oldBitmap.getWidth();
						float height = oldBitmap.getHeight(); 
						
						Bitmap newBitmap=null;
						if(width > 90 || height > 90) {
							float scaleRate = width > height ? 90 / width : 90 / height;
							Matrix matrix = new Matrix();
							matrix.postScale(scaleRate, scaleRate);
							// 得到新的图片          
							newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, (int)width, (int)height, matrix, true);   
						}else{
							Matrix matrix = new Matrix();
							matrix.postScale(1, 1);
							newBitmap =  Bitmap.createBitmap(oldBitmap, 0, 0, (int)width, (int)height, matrix, true);
						}
						
						
						UploadFeed uf = (UploadFeed) feed.clone();
						//uf为空
						String name = uf.getName();
						String[] split = name.split("[.]");
						String name_q=split[0];
						name_q+="_thumbnail";
						uf.setName(name_q+"."+split[1]);
						File signFile = Util.createFile(newBitmap, uf.getPath(), uf.getName());
						uf.setSize(signFile.length());
						
						if (question != null) {
							ma.dbService.addPhoto(feed, (question.qCamera == 1) ? true : false, question.qIndex + "");
							//缩略图
							ma.dbService.addPhoto(uf, (question.qCamera == 1) ? true : false, question.qIndex + "");
						} else {
							ma.dbService.addPhoto(feed, false, "");
							//缩略图
							ma.dbService.addPhoto(uf, false, "");
						}
						isSuccess=1;
					}else{
						
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fis) {
							fis.close();
							fis = null;
						}
						if (null != fos) {
							fos.close();
							fos = null;
						}
						if (200 == saveType) {
							if (tempFile.delete()) {
								tempFile = null;
								// System.out.println("文件删除成功");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if (null != photoBitmap) {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(f);
					photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
					fos.flush();
					feed.setRegTime(System.currentTimeMillis());
//					System.out.println("第二个长度:"+f.length());
					feed.setSize(f.length());
					if(f.length()>0){
						//缩略图
						float width = photoBitmap.getWidth();
						float height = photoBitmap.getHeight(); 
						
						Bitmap newBitmap=null;
						if(width > 90 || height > 90) {
							float scaleRate = width > height ? 90 / width : 90 / height;
							Matrix matrix = new Matrix();
							matrix.postScale(scaleRate, scaleRate);
							// 得到新的图片          
							newBitmap = Bitmap.createBitmap(photoBitmap, 0, 0, (int)width, (int)height, matrix, true);   
						}else{
							Matrix matrix = new Matrix();
							matrix.postScale(1, 1);
							newBitmap =  Bitmap.createBitmap(photoBitmap, 0, 0, (int)width, (int)height, matrix, true);
						}
						
						
						UploadFeed uf = (UploadFeed) feed.clone();
						//uf为空
						String name = uf.getName();
						String[] split = name.split("[.]");
						String name_q=split[0];
						name_q+="_thumbnail";
						uf.setName(name_q+"."+split[1]);
						File signFile = Util.createFile(newBitmap, uf.getPath(), uf.getName());
						uf.setSize(signFile.length());
						
						if (question != null) {
							ma.dbService.addPhoto(feed, (question.qCamera == 1) ? true : false, question.qIndex + "");
							//缩略图
							ma.dbService.addPhoto(uf, (question.qCamera == 1) ? true : false, question.qIndex + "");
						} else {
							ma.dbService.addPhoto(feed, false, "");
							//缩略图
							ma.dbService.addPhoto(uf, false, "");
						}
						//获取照片成功
						isSuccess=1;
					}else{
						
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fos) {
							fos.close();
							fos = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				
			}
			return isSuccess;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(1==isSuccess){
				//获取照片成功
				Toasts.makeText(PhotoActivity.this, R.string.image_obtain, Toast.LENGTH_SHORT).show();
			}else{
				//获取照片失败
				Toasts.makeText(PhotoActivity.this, R.string.image_obtain_fail, Toast.LENGTH_SHORT).show();
			}
			vWait.setVisibility(View.GONE);
			finish();
		}

	}

	// 多选图片
	private final class SaveMuchPhotoTask extends AsyncTask<Void, Integer, Integer> {
		private int saveType;

		public SaveMuchPhotoTask(int _saveType) {
			this.saveType = _saveType;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			vWait.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String realName = feed.getName();
			String[] split = realName.split("\\.");
			//缩略图判断是否有多图
			int count=0;
			for (int i = 0; i < photoList.size(); i++) {
				String one = split[0] + "(" + (i + 1) + ")";
				String newName = one + "." + split[1];
				feed.setName(newName);
				File f = new File(feed.getPath(), feed.getName());
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				Item item = photoList.get(i);
				File muchTempFile = new File(item.getPhotoPath());
				// tempFile
				if (null != muchTempFile && muchTempFile.exists()) {
					FileInputStream fis = null;
					FileOutputStream fos = null;
					try {
						fis = new FileInputStream(muchTempFile);
						fos = new FileOutputStream(f);

						byte[] buff = new byte[1024];

						int len;

						while (-1 != (len = fis.read(buff))) {
							fos.write(buff, 0, len);
						}
						fos.flush();
						feed.setRegTime(System.currentTimeMillis());
						feed.setSize(f.length());
						
						if(f.length()>0){
							//缩略图
							BitmapFactory.Options opts = new BitmapFactory.Options();
							// 数字越大读出的图片占用的heap越小 不然总是溢出
							long lens = f.length();
							if (1048576*3 > lens) { // 显示图片时的压缩 小于1024k, 即1M的
								opts.inSampleSize = 2;
							} else if(1048576*8>lens&&lens>=1048576*3){
								/**
								 * 大于1M的
								 */
								opts.inSampleSize = 6;
							}
							else {
								opts.inSampleSize = 10;
							}
							Bitmap oldBitmap= BitmapFactory.decodeStream(new FileInputStream(f),null,opts);
							
							float width = oldBitmap.getWidth();
							float height = oldBitmap.getHeight(); 
							
							Bitmap newBitmap=null;
							if(width > 90 || height > 90) {
								float scaleRate = width > height ? 90 / width : 90 / height;
								Matrix matrix = new Matrix();
								matrix.postScale(scaleRate, scaleRate);
								// 得到新的图片          
								newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, (int)width, (int)height, matrix, true);   
							}else{
								Matrix matrix = new Matrix();
								matrix.postScale(1, 1);
								newBitmap =  Bitmap.createBitmap(oldBitmap, 0, 0, (int)width, (int)height, matrix, true);
							}
							
							
							UploadFeed uf = (UploadFeed) feed.clone();
							//uf为空
							String name = uf.getName();
							String[] split1 = name.split("[.]");
							String name_q=split1[0];
							name_q+="_thumbnail";
							uf.setName(name_q+"."+split1[1]);
							File signFile = Util.createFile(newBitmap, uf.getPath(), uf.getName());
							uf.setSize(signFile.length());
							
							if (question != null) {
								ma.dbService.addPhoto(feed, (question.qCamera == 1) ? true : false, question.qIndex + "");
								//缩略图
								ma.dbService.addPhoto(uf, (question.qCamera == 1) ? true : false, question.qIndex + "");
							} else {
								ma.dbService.addPhoto(feed, false, "");
								//缩略图
								ma.dbService.addPhoto(uf, false, "");
							}
							
						}

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (null != fis) {
								fis.close();
								fis = null;
							}
							if (null != fos) {
								fos.close();
								fos = null;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else{
					//缩略图否则没有
					count++;
				}
			}
			return count;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			//缩略图
			if(result==0){
				vWait.setVisibility(View.GONE);
				finish();
			}else{
				Toasts.makeText(PhotoActivity.this, R.string.image_obtain_fail, Toast.LENGTH_LONG).show();
				vWait.setVisibility(View.GONE);
				finish();
			}
			
		}

	}

}