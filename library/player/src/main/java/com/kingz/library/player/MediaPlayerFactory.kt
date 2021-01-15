package com.kingz.library.player

import android.content.Context
import com.kingz.library.player.exo.ExoPlayer
import com.kingz.library.player.ijk.IJKPlayer
import com.kingz.library.player.internal.AndroidPlayer

/**
 * author：KingZ
 * date：2019/7/30
 * description：媒体播放器简单工厂类
 */
object MediaPlayerFactory {
    const val FLAG_SYS = 0x00
    const val FLAG_EXO = 0x01
    const val FLAG_DRM = 0x02
    const val FLAG_IJK = 0x03
    fun newInstance(context: Context): IPlayer {
        return createIJK(context)
    }

    @JvmStatic
    fun newInstance(context: Context, flag: Int, args: Any?): IPlayer {
        return when (flag) {
            FLAG_SYS -> createSys(context)
            FLAG_EXO -> createExo(context, args)
            FLAG_DRM, FLAG_IJK -> createIJK(context)
            else -> createIJK(context)
        }
    }

    private fun createExo(context: Context, @Suppress("UNUSED_PARAMETER") config: Any?): IPlayer {
        return ExoPlayer(context)
    }

    private fun createSys(context: Context): IPlayer {
        return AndroidPlayer(context)
    }

    private fun createIJK(context: Context): IPlayer {
        return IJKPlayer(context)
    }
}