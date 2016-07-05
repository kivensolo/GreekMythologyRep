package com.utils.media;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * 媒体播放音量的控制
 */
public class VolumeTools
{
	private final static String TAG = "VolumeTools";

	/**
	 * 进入APK前，系统的音量，保存起来，后续可以恢复或是使用
	 */
	private static int systemVolumePercentNotThisApp = 40;
	/**
	 * 进入播放器时的音量
	 */
	private static int appPlayerVolumePercent = 40;

	/**
	 * 设置进入播放器后，设置音量的初始值
	 * 如果这个值已经有保存，则使用保存值，
	 * 如果没有保存，则使用默认值（分不同的平台，支持不同的值）
//	 */
//	public static void setVolumeDefaultInPlayer()
//	{
//		//当前音量
//		systemVolumePercentNotThisApp = getStreamMusicVolumePercent();
//		Log.i(TAG, "setVolumeDefaultInPlayer systemVolumePercentNotThisApp:"+systemVolumePercentNotThisApp);
//
//		if( !GlobalEnv.getInstance().isVolumeConfiged() )
//		{
//			appPlayerVolumePercent = systemVolumePercentNotThisApp;
////			if( DeviceInfo.getFactory() == Factory.VERION_TV_TCL_MTK55 )
////			{
////				appPlayerVolumePercent = Math.max(40, systemVolumePercentNotThisApp);
////			}
//			GlobalEnv.getInstance().setVolumePercent(appPlayerVolumePercent);
//			Log.i(TAG, "setVolumeDefaultInPlayer firstDefault percent:"+appPlayerVolumePercent);
//		}
//		else
//		{
//			appPlayerVolumePercent = GlobalEnv.getInstance().getVolumePercent();
//			Log.i(TAG, "setVolumeDefaultInPlayer readConfig percent:"+appPlayerVolumePercent);
//		}
//
//		setStreamMusicVolumePercent(appPlayerVolumePercent);
//
//	}


	/**
	 * 恢复APP默认音
	 */
	//public static void restoreVolumeExitPlayer()
	//{
	//	int curVolumePercent = getStreamMusicVolumePercent();
	//	if( curVolumePercent != appPlayerVolumePercent )
	//	{
	//		GlobalEnv.getInstance().setVolumePercent(curVolumePercent);
	//	}
    //
	//	setStreamMusicVolumePercent(systemVolumePercentNotThisApp);
	//	Log.i(TAG, "restoreVolumeNotThisApp systemVolumePercentNotThisApp:"+systemVolumePercentNotThisApp);
	//}

	/**
	 * 初始化
	 */




	/**
	 * 读取当前系统的音量, 0-100
	 * @return
	 */
	public static int getStreamMusicVolumePercent(Context context)
	{
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		if( audioManager == null )
		{
			Log.e(TAG, "getStreamMusicVolumePercent getSystemService(Context.AUDIO_SERVICE)" );
			return 40;
		}
		//最大音量
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		if( maxVolume <= 0 )
		{
			Log.e(TAG, "getStreamMusicVolumePercent getStreamVolume cur:"+curVolume+", max:"+maxVolume );
			return 40;
		}

		int percent = 100*curVolume/maxVolume;
		Log.i(TAG, "getStreamMusicVolumePercent getStreamVolume cur:"+curVolume+", max:"+maxVolume +", percent:"+percent);
		return percent;
	}

	/**
	 *
	 * @param percent 0-100
	 * @return
	 */
	public static boolean setStreamMusicVolumePercent(Context context,int percent)
	{
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		if( audioManager == null )
		{
			Log.e(TAG, "setStreamMusicVolumePercent getSystemService(Context.AUDIO_SERVICE)" );
			return false;
		}
		//最大音量
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		if( maxVolume <= 0 )
		{
			Log.e(TAG, "setStreamMusicVolumePercent getStreamVolume cur:"+curVolume+", max:"+maxVolume );
			return false;
		}

		int newVolume = percent*maxVolume/100;
		if( newVolume != curVolume )
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);

		Log.i(TAG, "setStreamMusicVolumePercent percent:"+percent+", newVolume:"+newVolume );
		return true;
	}
}
