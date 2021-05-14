package com.kingz.playerdemo

import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.kingz.playerdemo.decode.AudioSyncDecoder
import com.kingz.playerdemo.decode.VideoSyncDecoder
import com.kingz.playerdemo.sync.MediaSync
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * 使用MediaCodec播放本地视频
 * https://blog.csdn.net/u011418943/article/details/107561111
 */
class MainActivity : AppCompatActivity() {

    private val TAG: String = "PlayerDemoActivity"
    private var isStreamEnd = false

    private var mExecutorService: ExecutorService? = null
    private var mVideoSync: VideoSyncDecoder? = null
    private var mAudioDecodeSync: AudioSyncDecoder? = null

    private var callback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.i(TAG, "surfaceChanged")
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceDestroyed")
//            workerThread?.interrupt()
//            workerThread = null
//            if (audioMediaCodecWorker != null) {
//                audioMediaCodecWorker!!.interrupt()
//                audioMediaCodecWorker = null
//            }
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceCreated")
            // 音频解码
        }
    }
    var mediaSync:MediaSync? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        filePathUri = "android.resource://$packageName/${R.raw.welcome_video}"

        val mFile = File(externalCacheDir, "ChiLing.mp4")

       /* // 设置Surface不维护自己的缓冲区，等待屏幕的渲染引擎将内容推送到用户面前
        // 该api已经废弃，这个编辑会自动设置
        // surfaceView.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        val holder = surface_view.holder
        holder.addCallback(callback)

        mediaSync = MediaSync(
            holder.surface,
            mFile.absolutePath
        )
        play_player.setOnClickListener {
            mediaSync?.start()
        }
        pause_player.setOnClickListener {
            mediaSync?.pause()
        }*/

         mExecutorService = Executors.newFixedThreadPool(2)
        // 设置Surface不维护自己的缓冲区，等待屏幕的渲染引擎将内容推送到用户面前
        // 该api已经废弃，这个编辑会自动设置
        // surfaceView.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        val holder = surface_view.holder
        holder.addCallback(callback)
        play_player.setOnClickListener {
            // 音视频同步
            mVideoSync = VideoSyncDecoder (holder.surface, mFile.absolutePath)
            mAudioDecodeSync = AudioSyncDecoder(mFile.absolutePath)
            mExecutorService?.execute(mVideoSync)
            mExecutorService?.execute(mAudioDecodeSync)
        }
        pause_player.setOnClickListener {
            mVideoSync?.pauseMedia()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        isStreamEnd = true
    }
}
