package com.kingz.utils;

import android.content.Context;
import android.media.AudioManager;
/**
 * 音量控制工具类
 */
public class VolumeTools {
    private final static String TAG = "VolumeTools";
    private static final int DEFAULT_CURRENT_VALUE = 40;
    private static final int DEFAULT_MAX_VALUE = -1;

    /**
     * 读取当前系统的音量, 0-100
     * @return 当前音量
     */
    public static int getCurrentVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            return DEFAULT_CURRENT_VALUE;
        }
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static float getCurrentVolumePrecent(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            return 0f;
        }
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return (float) current / maxVolume;
    }

    /**
     * 获取最大音量
     * @param context
     * @return 最大音量
     */
    public static int getMaxVolume(Context context) {
        if(context == null){
            return DEFAULT_MAX_VALUE;
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            return DEFAULT_MAX_VALUE;
        }
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 设置音量
     * @param percent 0.0 ~ 1.0f 百分比
     * @return 是否设置成功
     */
    public static boolean setStreamMusicVolume(Context context, float percent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            return false;
        }
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (maxVolume <= 0) {
            return false;
        }
        int newVolume = (int) (percent * maxVolume);
        if (newVolume != curVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
        }
        return true;
    }
}
