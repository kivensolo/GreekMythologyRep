package com.mplayer;

import android.media.MediaPlayer;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/9/8 16:38 <br>
 * description: 播放器监听接口 <br>
 */
public abstract class MediaPlayerListeners implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnInfoListener,MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener{

}
