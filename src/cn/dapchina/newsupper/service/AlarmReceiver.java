package cn.dapchina.newsupper.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {

	//MediaPlayer mp;
	//Uri uri;
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("arui.alarm.action".equals(intent.getAction())) {
			//没有运行这个线程的话
			if (!DiaryService.isRun) {
				Intent i = new Intent();
				i.setClass(context, DiaryService.class);
				// 启动service
				// 多次调用startService并不会启动多个service 而是会多次调用onStart
				context.startService(i);
			}
			
			
//			if(null == mp){
//				uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//				mp = new MediaPlayer();
//				try {
//					mp.setDataSource(context, uri);
//					AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//					if(0 != audioManager.getStreamVolume(AudioManager.STREAM_ALARM)){
//						mp.setAudioStreamType(AudioManager.STREAM_ALARM);
//						mp.setLooping(true);
//						mp.prepare();
//						mp.start();
//						
//					}
//				}catch(Exception e) {
//					e.printStackTrace();
//				}
//			}
			
		}
	}

}
