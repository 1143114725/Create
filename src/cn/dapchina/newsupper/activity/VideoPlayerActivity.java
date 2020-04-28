package cn.dapchina.newsupper.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.dapchina.newsupper.R;
import cn.dapchina.newsupper.global.MyApp;
import cn.dapchina.newsupper.util.Config;
import cn.dapchina.newsupper.view.SoundView;
import cn.dapchina.newsupper.view.SoundView.OnVolumeChangedListener;
import cn.dapchina.newsupper.view.VideoView;
import cn.dapchina.newsupper.view.VideoView.MySizeChangeLinstener;


/**
 * 
 *  播放器
 */
public class VideoPlayerActivity extends Activity{
	/**
	 * 打出日志
	 */
	private final static String TAG = "VideoPlayerActivity";

	/**
	 * 播放了多少时间
	 */
	private int playedTime;

	/**
	 * 视频显示控件-这个类相当重要
	 */
	private VideoView mVideoView = null;
	/**
	 * 播放进度条
	 */
	private SeekBar seekBar = null;

	/**
	 * 显示视频时间长度
	 */
	private TextView durationTextView = null;
	/**
	 * 用于显示视频播放了多少时间
	 */
	private TextView playedTextView = null;
	/**
	 * 手势识别器
	 */
	private GestureDetector mGestureDetector = null;
	/**
	 * 声音管理
	 */
	private AudioManager mAudioManager = null;

	/**
	 * 得到当前媒体音量
	 */
	private int currentVolume = 0;

	private ImageButton mPlay = null;
	/**
	 * 声音控制按钮
	 */
	private Button mSoundControl = null;

	/**
	 * 控制视频界面
	 */
	private View mControlView = null;
	/**
	 * 控制视频界面的具体按钮
	 */
	private PopupWindow mControlerPopupWindow = null;

	/**
	 * 
	 * 调节声音大小
	 * 
	 */

	private SoundView mSoundView = null;
	/**
	 * 音量控制窗口
	 */
	private PopupWindow mSoundWindow = null;

	/**
	 * 手机或模拟器的屏幕宽
	 */
	private static int screenWidth = 0;
	/**
	 * 手机或模拟器的屏幕高
	 */
	private static int screenHeight = 0;
	/**
	 * 控制面板屏幕的高
	 */
	private static int controlViewHeight = 0;
	/**
	 * 多常时间隐藏控制面板
	 */
	private final static int TIME = 6868;
	/**
	 * 是否显示控制面板
	 */
	private boolean isControllerShow = true;
	/**
	 * 是否暂停
	 */
	private boolean isPaused = false;
	/**
	 * 是否满屏
	 */
	private boolean isFullScreen = false;
	/**
	 * 是否是静音
	 */
	private boolean isSilent = false;

	/**
	 * 是否显示调音控件
	 */
	private boolean isSoundShow = false;
	/**
	 * 是否在线播放或第三方应用发起播放
	 */
	private boolean isOnline = false;

	/**
	 * 充满屏幕
	 */
	private final static int SCREEN_FULL = 0;
	/**
	 * 默认屏幕
	 */
	private final static int SCREEN_DEFAULT = 1;

	private boolean isChangedVideo = false;

	/**
	 * 隐藏控制面板消息
	 */
	private final static int HIDE_CONTROLER = 1;

	/**
	 * 在播放列表是发的消息
	 */
	private final static int PAUSE = 3;
	/**
	 * 刷新播放时间和拖动条位置消息
	 */
	private final static int PROGRESS_CHANGED = 0;

	/**
	 * 意图，从播放列表携带的视频信息
	 */
	//private Intent mIntentContent;
	/**
	 * 具体的数据的路径或网络地址
	 */
	private Uri uri;

	private Dialog erroVideoDialog;
	private MyApp ma;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 保持背光常亮的设置
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// 保持背光常亮的设置(例外一种)
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.video_view);
		ma=(MyApp) getApplication();
		ma.addActivity(this);
		Log.v(TAG, "onCreate()");

		Log.d("OnCreate", getIntent().toString());
		// 如果是从
		// uri = getIntent().getData();
		Log.d(TAG, "The main thread id = " + Thread.currentThread().getId()
				+ "\n");

//		mIntentContent = getIntent();
//		if (mIntentContent != null) {
//			uri = mIntentContent.getData();
//		}
//		position = mIntentContent.getIntExtra("CurrentPosInMediaIdList", 0);
//		playList = mIntentContent.getStringArrayListExtra("MediaIdList");
		
		String path = getIntent().getStringExtra("path");
		//String path = "/mnt/sdcard/iphone/aa.avi";
		uri = Uri.parse(path);
		// 加载控制面板
		Looper.myQueue().addIdleHandler(new IdleHandler() {

			@Override
			public boolean queueIdle() {

				// TODO Auto-generated method stub
				if (mControlerPopupWindow != null && mVideoView.isShown()) {
					mControlerPopupWindow.showAtLocation(mVideoView,
							Gravity.BOTTOM, 0, 0);

					mControlerPopupWindow.update(0, 0, screenWidth,
							controlViewHeight);

				}

				return false;
			}
		});

		// 加载控制视频界面。
		mControlView = getLayoutInflater().inflate(R.layout.mediacontroller,
				null);

		// PopupWindow 作为一种用户提醒 而且其开销也比Activity要小
		mControlerPopupWindow = new PopupWindow(mControlView);

		// 显示视频总长度时间
		durationTextView = (TextView) mControlView.findViewById(R.id.duration);
		// 显示视频播放了多少时间
		playedTextView = (TextView) mControlView.findViewById(R.id.has_played);


		// 播放视频
		mPlay = (ImageButton) mControlView.findViewById(R.id.control_play_state);

		// 控制声音
		mSoundControl = (Button) mControlView.findViewById(R.id.control_sound_control);

		// 视频显示
		mVideoView = (VideoView) findViewById(R.id.vv);

		mPlay.setOnClickListener(mPlayListener);
		mSoundControl.setOnClickListener(mSoundControlListener);

		getScreenSize();

		/**
		 * 监听播放出错时的处理
		 */
		mVideoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				if (mVideoView != null) {
					mVideoView.stopPlayback();
				}

				errorsInformationDialog();
				return false;

			}

		});

		mVideoView.setMySizeChangeLinstener(new MySizeChangeLinstener() {

			@Override
			public void doMyThings() {
				// TODO Auto-generated method stub
				setVideoScale(SCREEN_DEFAULT);
			}

		});

		/**
		 * 调音量类
		 * 
		 */
		mSoundView = new SoundView(this);
		mSoundView.setOnVolumeChangeListener(new OnVolumeChangedListener() {

			@Override
			public void setYourVolume(int index) {

				cancelDelayHide();
				updateVolume(index);
				hideControllerDelay();
			}
		});

		mSoundWindow = new PopupWindow(mSoundView);

		// 音频管理服务
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// 得到视频当前音量
		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		// 长按钮效果
		mSoundControl.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				long time = System.currentTimeMillis();
				if (time - lastTimeonLongClick < CLICK_INTERVAL) {
					return true;
				}
				lastTimeonLongClick = time;

				if (isSilent) {
					mSoundControl.setText(R.string.control_soundControl);
				} else {
					mSoundControl.setText(R.string.control_soundControl_mute);
				}
				isSilent = !isSilent;
				updateVolume(currentVolume);
				cancelDelayHide();
				hideControllerDelay();
				return true;
			}

		});

		// 播放进度条,可以拖动
		seekBar = (SeekBar) mControlView.findViewById(R.id.seekbar);
		seekBar.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				// long time = System.currentTimeMillis();
				// if (time - lastTimeonProgressChanged < CLICK_INTERVAL) {
				// return ;
				// }
				// lastTimeonProgressChanged = time;
				if (fromUser) {
					mVideoView.seekTo(progress);

				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				mHandler.removeMessages(HIDE_CONTROLER);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
			}
		});

		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				long time = System.currentTimeMillis();
				if (time - lastTimeonDoubleTap < CLICK_INTERVAL) {
					return true;
				}
				lastTimeonDoubleTap = time;

				if (isFullScreen) {
					setVideoScale(SCREEN_DEFAULT);
				} else {
					setVideoScale(SCREEN_FULL);
				}
				isFullScreen = !isFullScreen;

				if (isControllerShow) {
					showController();
				}

				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				long time = System.currentTimeMillis();
				if (time - lastTimeonSingleTapConfirmed < CLICK_INTERVAL) {
					return true;
				}
				lastTimeonSingleTapConfirmed = time;

				if (!isControllerShow) {
					showController();
					hideControllerDelay();
				} else {
					cancelDelayHide();
					hideController();
				}
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				long time = System.currentTimeMillis();
				if (time - lastTimeonLongPress < CLICK_INTERVAL) {
					return;
				}
				lastTimeonLongPress = time;
				if (isPaused) {
					mVideoView.start();
					mPlay.setBackgroundResource(R.drawable.btn_pause_normal);
					cancelDelayHide();
					hideControllerDelay();
				} else {
					mVideoView.pause();
					mPlay.setBackgroundResource(R.drawable.btn_play_normal);
					cancelDelayHide();
					showController();
				}
				isPaused = !isPaused;

			}
		});

		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {

				setVideoScale(SCREEN_DEFAULT);
				isFullScreen = false;
				if (isControllerShow) {
					showController();
				}

				int i = mVideoView.getDuration();
				Log.d("onCompletion", "" + i);
				seekBar.setMax(i);
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				if (hour > 0) {
					durationTextView.setText(String.format("%02d:%02d:%02d",
							hour, minute, second));
				} else {
					durationTextView.setText(String.format("%02d:%02d", minute,
							second));
				}
				mVideoView.start();

				mPlay.setBackgroundResource(R.drawable.btn_pause_normal);
				hideControllerDelay();
				mHandler.sendEmptyMessage(PROGRESS_CHANGED);
			}
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {

				if (uri != null) {
					if (isOnline) {
						if (mVideoView != null) {
							finish();
							mVideoView.stopPlayback();
						}

						isOnline = false;
					} else {
						if (mVideoView != null) {
							finish();
							mVideoView.stopPlayback();
						}

						isOnline = false;
					}

				} else {

					mVideoView.setVideoURI(uri);

				}

			}
		});

		// position = -1;
		// 得到URL

		startPlay();

	}

	private static long CLICK_INTERVAL = 800;
	private long lastTimeonDoubleTap;
	private long lastTimeonSingleTapConfirmed;
	private long lastTimeonLongPress;
	private long lastTimeonLongClick;
	private long lastTimemPlayListener;
	private long lastTimemSoundControlListener;

	private View.OnClickListener mPlayListener = new View.OnClickListener() {
		public void onClick(View v) {
			long time = System.currentTimeMillis();
			if (time - lastTimemPlayListener < CLICK_INTERVAL) {
				return;
			}
			lastTimemPlayListener = time;
			cancelDelayHide();

			if (isPaused) {
				mVideoView.start();
				mPlay.setBackgroundResource(R.drawable.btn_pause_normal);

				hideControllerDelay();
			} else {
				mVideoView.pause();
				mPlay.setBackgroundResource(R.drawable.btn_play_normal);

			}
			isPaused = !isPaused;

		}
	};

	private View.OnClickListener mSoundControlListener = new View.OnClickListener() {
		public void onClick(View v) {
			long time = System.currentTimeMillis();
			if (time - lastTimemSoundControlListener < CLICK_INTERVAL) {
				return;
			}
			lastTimemSoundControlListener = time;
			cancelDelayHide();
			if (isSoundShow) {
				mSoundWindow.dismiss();
			} else {
				if (mSoundWindow.isShowing()) {
					mSoundWindow.update(15, 0, SoundView.MY_WIDTH,
							SoundView.MY_HEIGHT);
				} else {
					mSoundWindow.showAtLocation(mVideoView, Gravity.RIGHT
							| Gravity.CENTER_VERTICAL, 15, 0);
					mSoundWindow.update(15, 0, SoundView.MY_WIDTH,
							SoundView.MY_HEIGHT);
				}
			}
			isSoundShow = !isSoundShow;
			hideControllerDelay();
		}
	};

	private View.OnClickListener mErrorLearnListener = new View.OnClickListener() {
		public void onClick(View v) {
			//Toasts.makeText(VideoPlayerActivity.this, "不好意思还没有实现", 1).show();
			//com.storm.smart
			Intent it = new Intent(Intent.ACTION_VIEW);  
			it.setDataAndType(uri , "*/*");  
			startActivity(it);
			finish();
		}
	};
	private View.OnClickListener mErrorExitListener = new View.OnClickListener() {
		public void onClick(View v) {
			erroVideoDialog.dismiss();
			finish();
		}
	};

	private void startPlay() {
		if (uri != null && mVideoView != null) {
			mVideoView.stopPlayback();
			mVideoView.setVideoURI(uri);
			Log.i("afu", uri.toString());
			isOnline = true;

			mPlay.setBackgroundResource(R.drawable.btn_pause_normal);
		} else {
			if (mVideoView != null) {
					isOnline = false;
					isChangedVideo = true;

					mVideoView.setVideoURI(uri);


					mPlay.setBackgroundResource(R.drawable.btn_play_normal);
			}

		}
	}


	/**
	 * 设置视频播放屏幕大小的模式
	 * 
	 * @param flag
	 */
	private void setVideoScale(int flag) {

		switch (flag) {
		case SCREEN_FULL:

			Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: "
					+ screenHeight);
			mVideoView.setVideoScale(screenWidth, screenHeight);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			break;

		case SCREEN_DEFAULT:

			int videoWidth = mVideoView.getVideoWidth();
			int videoHeight = mVideoView.getVideoHeight();
			int mWidth = screenWidth;
			int mHeight = screenHeight - 25;

			if (videoWidth > 0 && videoHeight > 0) {
				if (videoWidth * mHeight > mWidth * videoHeight) {

					mHeight = mWidth * videoHeight / videoWidth;
				} else if (videoWidth * mHeight < mWidth * videoHeight) {

					mWidth = mHeight * videoWidth / videoHeight;
				} else {

				}
			}

			mVideoView.setVideoScale(mWidth, mHeight);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			break;
		}
	}

	/**
	 * 发送信息，告诉控制面板在多少秒后隐藏。
	 */
	private void hideControllerDelay() {
		mHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	/**
	 * 隐藏控制面板
	 */
	private void hideController() {

		if (mControlerPopupWindow.isShowing()) {
			mControlerPopupWindow.update(0, 0, 0, 0);
			isControllerShow = false;
		}

		if (mSoundWindow.isShowing()) {
			mSoundWindow.dismiss();
			isSoundShow = false;
		}
	}

	/**
	 * 删除隐藏面板消息
	 */
	private void cancelDelayHide() {
		mHandler.removeMessages(HIDE_CONTROLER);
	}

	/**
	 * 显示控制面板
	 */
	private void showController() {
		mControlerPopupWindow.update(0, 0, screenWidth, controlViewHeight);
		isControllerShow = true;
	}

	/**
	 * 用于处理拖动调进度更新、播放时间进度更新、隐藏控制面板消息
	 */
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {

			case PROGRESS_CHANGED:
				// Log.d(TAG, "The handler thread id = "
				// + Thread.currentThread().getId() + "\n");
				int i = mVideoView.getCurrentPosition();
				seekBar.setProgress(i);
				if (isOnline) {
					int j = mVideoView.getBufferPercentage();
					seekBar.setSecondaryProgress(j * seekBar.getMax() / 100);
				} else {
					// 这里我们不需要第2进度，所以为0
					seekBar.setSecondaryProgress(0);
				}

				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				if (hour > 0) {
					playedTextView.setText(String.format("%02d:%02d:%02d",
							hour, minute, second));
				} else {
					playedTextView.setText(String.format("%02d:%02d", minute,
							second));
				}

				sendEmptyMessageDelayed(PROGRESS_CHANGED, 100);
				break;

			case HIDE_CONTROLER:
				hideController();
				Log.d(TAG, "The handler thread id = "
						+ Thread.currentThread().getId() + "\n");
				break;
			case PAUSE:
				if (mVideoView != null) {
					mVideoView.pause();
					mPlay.setBackgroundResource(R.drawable.btn_play_normal);
				}

				break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.v(TAG, " onTouchEvent(MotionEvent event)");

		boolean result = mGestureDetector.onTouchEvent(event);

		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

				/*
				 * if(!isControllerShow){ showController();
				 * hideControllerDelay(); }else { cancelDelayHide();
				 * hideController(); }
				 */
			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.v(TAG, " onConfigurationChanged()");

		getScreenSize();
		if (isControllerShow) {

			cancelDelayHide();
			hideController();
			showController();
			hideControllerDelay();
		}

		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 设置音量大小
	 * 
	 * @param index
	 */
	private void updateVolume(int index) {
		if (mAudioManager != null) {
			if (isSilent) {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			} else {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
						0);
			}
			currentVolume = index;
		}
	}

	/**
	 * 获得手机或模拟器屏蔽高和宽，并计算好控制面板的高度
	 */
	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		controlViewHeight = screenHeight / 4;

	}

	/**
	 * 
	 * 
	 * 播放时出错信息提示对话框.
	 */
	private void errorsInformationDialog() {

		erroVideoDialog = new Dialog(this, R.style.question_ds);
		erroVideoDialog.setContentView(R.layout.error_video_dialog);
		erroVideoDialog.show();
		Button mErrorLearn = (Button) erroVideoDialog
				.findViewById(R.id.error_video_learn);
		mErrorLearn.setOnClickListener(mErrorLearnListener);
		Button mErrorExit = (Button) erroVideoDialog
				.findViewById(R.id.error_video_sure);
		mErrorExit.setOnClickListener(mErrorExitListener);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, " onActivityResult()");

		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			int result = data.getIntExtra("CHOOSE", -1);
			Log.d("RESULT", "" + result);
			if (result != -1) {
				isOnline = false;
				isChangedVideo = true;
				mVideoView.setVideoURI(uri);

			} else {
				String url = data.getStringExtra("CHOOSE_URL");
				if (url != null) {
					mVideoView.setVideoPath(url);
					isOnline = true;
					isChangedVideo = true;

				}
			}

			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		Log.v(TAG, " onPause()");
		playedTime = mVideoView.getCurrentPosition();
		mVideoView.pause();
		mPlay.setBackgroundResource(R.drawable.btn_play_normal);
		mHandler.sendEmptyMessage(PAUSE);
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.v(TAG, "onResume()");
		if (!isChangedVideo) {
			mVideoView.seekTo(playedTime);
			mVideoView.start();
		} else {
			isChangedVideo = false;
		}
		if (mVideoView.isPlaying()) {
			mPlay.setBackgroundResource(R.drawable.btn_pause_normal);
			hideControllerDelay();
		}

		/**
		 * 设置为横屏
		 */
//		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
		/**
		 * 0竖屏 1横屏
		 */
		int screen = new Config(this).getInt("ScreenOrientation", 0);

		if (1 == screen) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		ma.remove(this);
		Log.v(TAG, " onDestroy()");
		if (mControlerPopupWindow.isShowing()) {
			mControlerPopupWindow.dismiss();
		}
		if (mSoundWindow.isShowing()) {
			mSoundWindow.dismiss();
		}

		mHandler.removeMessages(PROGRESS_CHANGED);
		mHandler.removeMessages(HIDE_CONTROLER);
		if (mVideoView.isPlaying()) {
			mVideoView.stopPlayback();
		}
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v(TAG, " onRestart()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.v(TAG, "onSaveInstanceState()");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v(TAG, "onStart())");
	}

	@Override
	protected void onStop() {
		mHandler.sendEmptyMessage(PAUSE);
		super.onStop();
		Log.v(TAG, "onStop()");
	}

	
}