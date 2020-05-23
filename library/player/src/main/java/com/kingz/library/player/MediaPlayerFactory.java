package com.kingz.library.player;

import android.content.Context;

import com.kingz.library.player.exo.ExoPlayer;
import com.kingz.library.player.ijk.IJKPlayer;
import com.kingz.library.player.internal.AndroidPlayer;

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

    public static IPlayer newInstance(Context context) {
        return createIJK(context);
    }

    public static IPlayer newInstance(Context context, int flag, Object args) {
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

    private static IPlayer createExo(Context context, Object config) {
        return new ExoPlayer(context);
    }

    private static IPlayer createSys(Context context) {
        return new AndroidPlayer(context);
    }

    private static IPlayer createIJK(Context context) {
        return new IJKPlayer(context);
    }
}
