package com.kingz.playerdemo

import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.kingz.playerdemo.decode.AudioSyncDecoder
import com.kingz.playerdemo.decode.VideoSyncDecoder
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        filePathUri = "android.resource://$packageName/${R.raw.welcome_video}"

        val mFile = File(externalCacheDir, "ChiLing.mp4")

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

    /**
     * 音频解码工作线程
     *//*
    inner class AudioMediaCodecWorker(playUrl: String) : Thread() {

        var videoUrl: String? = playUrl

        //数据显示的时间戳
        var presentationTimeUs = 0L
        var inputErrorCount = 0
        var outputErrorCount = 0

        override fun run() {
            super.run()
            tryFindVideoTrack()
        }

        private fun tryFindVideoTrack(): Boolean {
            mediaExtractor = MediaExtractor()
            Log.i(TAG, "Try FindVideoTrack fileURl:$videoUrl")
            try{
                mediaExtractor.setDataSource(videoUrl)
            }catch (e:Exception){
                e.printStackTrace()
                return false
            }

            // 遍历数据视频轨道，创建指定格式的MediaCodec
            for (idx in 0 until mediaExtractor.trackCount) {
                val mediaFormat = mediaExtractor.getTrackFormat(idx)

                val mime = mediaFormat.getString(MediaFormat.KEY_MIME)
                // 找到视频轨道，并创建MediaCodec解码器
                if (mime.startsWith("video/")) {
//                    val width = mediaFormat.getInteger(MediaFormat.KEY_WIDTH)
//                    val height = mediaFormat.getInteger(MediaFormat.KEY_HEIGHT)
                    Log.w(TAG,"Find video track, MIME=$mime ; MediaFormat=$mediaFormat; ")

                    //选择此视频轨道
                    mediaExtractor.selectTrack(idx)
                    try {
                        //创建解码器  STATE --- Uninitialized
                        mMediaCodec = MediaCodec.createDecoderByType(mime).apply {
                            // STATE --- Configured
                            this.configure(mediaFormat, mSurface, null, 0)
                            // STATE --- Executing (Can deal with data) ---> Flushed
                            this.start()
                        }
                        val bufferInfo = MediaCodec.BufferInfo()
                        val inputbuffer = mMediaCodec?.inputBuffers
                        while (!isStreamEnd) {
                            inputData(mediaExtractor, inputbuffer)
                            outputData(bufferInfo)
                        }
                        return true
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return false
        }

        *//**
         * MediaCodec 输出数据至OutBuffer
         * @param outBuffer: 用于储存视频输出数据的buffer
         *//*
        private fun outputData(outBuffer: MediaCodec.BufferInfo) {
            val outBufferIndex = mMediaCodec?.dequeueOutputBuffer(outBuffer, 20 * 1000L)!!
            if (outBufferIndex >= 0) {
                Log.i(TAG, "=======>>>>> OutData, time(Us):$presentationTimeUs")
                //释放指定索引位置的buffer数据,并渲染到Surface上
                mMediaCodec?.releaseOutputBuffer(outBufferIndex, true)
                outputErrorCount = 0
                return
            }
            // outBufferIndex < 0 则认为查找失败
            if (outputErrorCount > 10) {
                outputErrorCount = 0
                Log.e(TAG, "输出超过错误上限")
                return
            }
            outputErrorCount++
            outputData(outBuffer)
        }

        *//**
         * 通过MediaExtractor从inputBuffer中读取数据
         * 并输入至MediaCodoc
         *//*
        private fun inputData(extractor: MediaExtractor, inputBuffer: Array<ByteBuffer>?) {
            //返回可以使用的输入buffer索引
            val bufferIndex: Int = mMediaCodec?.dequeueInputBuffer(10 * 1000L)!!

            // buffer有效
            if (bufferIndex >= 0) {
                presentationTimeUs++
//                var input: ByteBuffer? = null
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    input = mMediaCodec?.getInputBuffer(bufferIndex)
//                } else {
//                  input = inputBuffer?.get(bufferIndex)
//                }

                // 获取的有效ByteBuffer
                val tmpByteBuffer = inputBuffer?.get(bufferIndex)

                *//**
                 * 读取采样数据的buffer大小
                 * 通过MediaExtractor检索当前已编码的样例，
                 * 并将其存储在从给定偏移量开始的字节缓冲区中。
                 * 返回样本大小(如果*没有更多的样本，则返回-1)
                 *//*
                val size = extractor.readSampleData(tmpByteBuffer, 0)
                Log.i(TAG, "<<<<<<======= InputData time(us):$presentationTimeUs    size(byte):$size")
                var flag = 0
                if (size < 0) {
                    flag = MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    Log.e(TAG, "视频流结束")
                    isStreamEnd = true
                    mMediaCodec?.release()
                    return
                }
                inputErrorCount = 0
                // 设置指定索引位置的buffer数据
                mMediaCodec?.queueInputBuffer(bufferIndex, 0, size, presentationTimeUs, flag)
                // 提前到下一个样品。如果没有更多的示例数据，则返回false(流结束)
                extractor.advance()
                return
            }

            if (inputErrorCount > 10) {
                inputErrorCount = 0
                Log.e(TAG, "数据注入超过错误上限")
                return
            }
            outputErrorCount++
            inputData(extractor, inputBuffer)
        }
    }
*/
}
