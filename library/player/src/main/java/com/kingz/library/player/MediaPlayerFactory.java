package com.kingz.library.player;

import android.content.Context;

import com.kingz.library.player.exo.ExoMediaPlayer;
import com.kingz.library.player.ijk.IJKMediaPlayer;
import com.kingz.library.player.internal.AndroidMediaPlayer;

/**
 * author：KingZ
 * date：2019/7/30
 * description：媒体播放器简单工厂类
 */
@SuppressWarnings("WeakerAccess")
public class MediaPlayerFactory {
    public static final int FLAG_SYS = 0x00;
    public static final int FLAG_EXO = 0x01;
    public static final int FLAG_DRM = 0x02;
    public static final int FLAG_IJK = 0x03;

    private MediaPlayerFactory() {
    }

    public static IMediaPlayer newInstance(Context context) {
        return createIJK(context);
    }

    public static IMediaPlayer newInstance(Context context, int flag, Object args) {
        switch (flag) {
            case FLAG_SYS:
                return createSys(context);
            case FLAG_EXO:
                return createExo(context, args);
            case FLAG_DRM:
            case FLAG_IJK:
            default:
                return createIJK(context);
        }
    }

    private static IMediaPlayer createExo(Context context, Object config) {
        return new ExoMediaPlayer(context);
    }

    private static IMediaPlayer createSys(Context context) {
        return new AndroidMediaPlayer(context);
    }

    private static IMediaPlayer createIJK(Context context) {
        return new IJKMediaPlayer(context);
    }
}
