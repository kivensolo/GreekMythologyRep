package com.kingz.library.player.exo

import android.content.Context
import android.graphics.SurfaceTexture
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.RepeatMode
import com.google.android.exoplayer2.drm.*
import com.google.android.exoplayer2.drm.DrmSession.STATE_RELEASED
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId
import com.google.android.exoplayer2.source.MediaSourceEventListener.LoadEventInfo
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.kingz.library.player.BasePlayer
import com.kingz.library.player.IPlayer
import com.kingz.library.player.IPlayer.Companion.MEDIA_INFO_BUFFERING_END
import com.kingz.library.player.IPlayer.Companion.MEDIA_INFO_BUFFERING_START
import com.kingz.library.player.TrackInfo
import com.kingz.library.player.helper.TrackSelectionHelper
import java.io.IOException
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.*

/**
 * author：KingZ
 * date：2019/7/30
 * description：EXO
 */
class ExoPlayer(context: Context) : BasePlayer() {

    companion object {
        private val TAG = ExoPlayer::class.java.simpleName
        private val DEFAULT_COOKIE_MANAGER = CookieManager()

        fun logD(log: String?) {
            Log.d(TAG, log)
        }

        init {
            DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
        }
    }

    private val mDefaultBandwidthMeter = DefaultBandwidthMeter.Builder(null).build()
    private var player: SimpleExoPlayer? = null
    private val eventListener: ExoPlayerEvents
    private var eventLogger: EventLogger? = null
    private val mediaDataSourceFactory: DataSource.Factory
    private lateinit var trackSelector: DefaultTrackSelector
    private lateinit var trackSelectionHelper: TrackSelectionHelper
    private var uris: Array<Uri?> = arrayOf()
    private var extensions: Array<String?> = arrayOf()

    // Drm的后续抽出来
    private val drmSchemeUuid: UUID? = null
    private val drmLicenseUrl: String? = null

    init {
        mContext = context
        mediaDataSourceFactory = buildDataSourceFactory(mDefaultBandwidthMeter)
        eventListener = ExoPlayerEvents()
        init()
    }

    override fun seekTo(msec: Long) {
        val duration = player?.duration
        var seekTo: Long = if (msec < 0) 0 else msec
        if (msec == duration) {
            seekTo -= 10000
        }
        player?.seekTo(seekTo)
    }

    override fun setSoundTrack(soundTrack: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 设置播放数据源
     * raw文件为：Uri.parse("rawresource:///" + R.raw.xxx)
     */
    override fun setDataSource(uri: Uri?) {
        uris = arrayOf(uri)
        extensions = arrayOfNulls(uris.size)
        prepare()
    }

    override fun setDataSource(url: String) {
    }

    override fun getCurrentLoadSpeed(): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBufferTimeOutThreshold(threshold: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectAudioTrack(audioTrackIndex: Int) {
        setSelectedTrack(RendererType.AUDIO, audioTrackIndex, 0)
    }

    override fun setDisplayHolder(holder: SurfaceHolder?) {
        if(holder != null){
            player?.setVideoSurfaceHolder(holder)
        }
    }

    override fun setAudioTrack(stcTrackInfo: TrackInfo?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 初始化播放器
     */
    private fun init() {
        if (player == null) {
            // 渲染工厂
            val renderersFactory = DefaultRenderersFactory(mContext)
            // 默认渲染器优先
            renderersFactory.setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
            // TrackSelector
            createTrackSelector()
            val drmSessionManager = createDrmSessionManager()
            player = ExoPlayerFactory.newSimpleInstance(
                mContext,
                renderersFactory,
                trackSelector,
                DefaultLoadControl(),   // 默认的加载控制器
                drmSessionManager
            ).apply {
                addListener(eventListener)
                eventLogger = EventLogger(trackSelector)
                addMetadataOutput { metadata -> Log.d(TAG, "onMetadata()  metaData=$metadata") }
                // addAudioDebugListener\addVideoDebugListener ... is deprecated.
                  // Use AnalyticsListener to get more detailed debug information
                addAnalyticsListener(eventLogger!!)
                playWhenReady = false
            }
            setSurface(null)
        }
    }

    private fun prepare() {
        if (uris.isEmpty()) {
            eventListener.onPlayerError(ExoPlaybackException.createForSource(IOException("no uris")))
            return
        }

        if (hasAnyState(STATE_PREPARED or STATE_PREPARING or STATE_ERROR)) {
            player?.stop()
        }
        if (hasAnyState(STATE_REBUILD or STATE_RELEASED)) {
            changeState(
                STATE_REBUILD or
                        STATE_RELEASED or
                        STATE_PREPARED or
                        STATE_PREPARING or
                        STATE_STOPPED or
                        STATE_ERROR, 0
            )
//            createPlayer(mContext) //TODO 进行播放器重建
        }
        if(player == null){
            return
        }

        val mediaSources = arrayOfNulls<MediaSource>(uris.size)
        for (i in uris.indices) {
            mediaSources[i] = createMediaSource(uris[i]!!, extensions[i])
            mediaSources[i]!!.addEventListener(playerHandler, CustomMediaSourceEventListener())
        }
        val mediaSource = if (mediaSources.size == 1) {
            mediaSources[0]
        } else {
            ConcatenatingMediaSource(*mediaSources)
        }
        player?.prepare(mediaSource!!)

        changeState(STATE_PREPARING or STATE_PREPARED,
            STATE_PREPARING)
    }

    private fun addListenerWithPlayer() {
        eventLogger = EventLogger(trackSelector)
        player?.apply {
            addListener(eventListener)
            // addAudioDebugListener\addVideoDebugListener ... is deprecated.
            // Use AnalyticsListener to get more detailed debug information
            addAnalyticsListener(eventLogger!!)
            addMetadataOutput {
                metadata -> Log.d(TAG, "onMetadata()  metaData=$metadata")
            }
        }
    }

    private fun createTrackSelector() {
        // 自定义的bandwidth meter需要在ExoPlayerFactory.()的时候传递进去
        //TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        val adaptiveTrackSelectionFactory: TrackSelection.Factory =
            AdaptiveTrackSelection.Factory() //TrackSelection.Factory
        trackSelector = DefaultTrackSelector()
        trackSelectionHelper = TrackSelectionHelper(trackSelector, adaptiveTrackSelectionFactory)
    }

    /**
     * 创建DRM会话管理者
     *
     * @return DefaultRenderersFactory
     */
    private fun createDrmSessionManager(): DrmSessionManager<FrameworkMediaCrypto>? {
        if (drmSchemeUuid != null) {
            return try {
                buildDrmSessionManager(drmSchemeUuid, drmLicenseUrl!!)
            } catch (e: UnsupportedDrmException) {
                e.printStackTrace()
                eventListener.onPlayerError(ExoPlaybackException.createForSource(IOException("drmSessionManager == null")))
                return null
            }
        } else {
            return null
        }
    }

    /**
     * Create [MediaSource] by uri：
     * DASH (DashMediaSource),
     * SmoothStreaming (SsMediaSource),
     * HLS (HlsMediaSource)，
     * Other formats(eg:.mp4) use(ExtractorMediaSource(旧)  ProgressiveMediaSource(新)).
     * [EXOPLAYER Support Formats](https://exoplayer.dev/supported-formats.html)
     * @param uri               playUrl
     * @param overrideExtension param of Extension
     * @return A MediaSource
     */
    private fun createMediaSource(uri: Uri, overrideExtension: String?): MediaSource {
        @C.ContentType
        val type: Int = if (TextUtils.isEmpty(overrideExtension)) {
            Util.inferContentType(uri)
        } else {
            Util.inferContentType(".$overrideExtension")
        }
        return when (type) {
            C.TYPE_DASH -> DashMediaSource.Factory(
                DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                buildDataSourceFactory(null)
            )
                .createMediaSource(uri)
//                    DashMediaSource.Factory(mediaDataSourceFactory)
//                        .createMediaSource(uri)
            C.TYPE_SS -> SsMediaSource.Factory(
                DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                buildDataSourceFactory(null)
            )
                .createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(uri)
            C.TYPE_OTHER ->  // Old
                // ExtractorMediaSource extractorMediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                //        .createMediaSource(uri);
                // New
                ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(uri)
            else -> {
                throw IllegalArgumentException("Unsupported type: $type")
            }
        }
    }

    @Throws(UnsupportedDrmException::class)
    private fun buildDrmSessionManager(
        uuid: UUID,
        licenseUrl: String
    ): DrmSessionManager<FrameworkMediaCrypto> { //if (Util.SDK_INT < 18) {
        val drmCallback = HttpMediaDrmCallback(
            licenseUrl,
            false,
            buildHttpDataSourceFactory(if (false) mDefaultBandwidthMeter else null)
        )
        return DefaultDrmSessionManager(
            uuid,
            FrameworkMediaDrm.newInstance(uuid),
            drmCallback,
            null,
            false,
            3
        )
    }

    override fun setSpeed(speed: Float) {
        player?.setPlaybackParameters(PlaybackParameters(speed, 1.0f))
    }

    override fun setPlayerOptions(
        playerOptionCategory: Int,
        optionName: String,
        optionValue: String
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPlayerOptions(
        playerOptionCategory: Int,
        optionName: String,
        optionValue: Long
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMirrorPlay() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setRepeatMode(@RepeatMode repeatMode: Int) {
        player?.repeatMode = repeatMode
    }

    override fun setBufferSize(bufferSize: Int) {}

    override fun setSurface(surface: Surface?) {
        player?.apply {
//            addTextOutput(null)
            setVideoSurface(surface)
            if (playView is TextureView) {
                setVideoTextureView(playView as TextureView)
            } else if (playView is SurfaceView) {
                setVideoSurfaceView(playView as SurfaceView)
            }
        }
    }

    override fun stop() {
        super.stop()
        player ?: return
        if (hasAnyState(STATE_PREPARED or STATE_PREPARING or STATE_ERROR)) {
            player?.stop()
        }
    }

    override fun attachListener() {}

    override fun detachListener() {
        player?.removeListener(eventListener)
    }

    private fun buildDataSourceFactory(bandwidthMeter: TransferListener?): DataSource.Factory {
        return DefaultDataSourceFactory(
            mContext, bandwidthMeter,
            buildHttpDataSourceFactory(bandwidthMeter)
        )
        //DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
    }

    private fun buildHttpDataSourceFactory(bandwidthMeter: TransferListener?): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory("kingz_exo_media_player", bandwidthMeter)
    }

    private fun destoryPlayer() {
        player?.release()
        player = null
        eventLogger = null
    }

    override val currentURI: Uri
        get() = if (uris.isEmpty()) Uri.parse("") else uris[0]!!

    override fun destory() {
        super.destory()
        reset()
        destoryPlayer()
    }

    override fun play() {
        super.play()
        player?.playWhenReady = true
    }

    override fun pause() {
        super.pause()
        player?.playWhenReady = false
    }

    override fun isPlaying(): Boolean{
        return player != null && hasState(STATE_PLAYING or STATE_PREPARED)
                && !hasAnyState(
                    STATE_STOPPED
                    or STATE_UNINITIALIZED
                    or STATE_RELEASED)
    }

    override val duration: Long
        get() = player?.duration ?: 0L

    override val currentPosition: Long
        get() = player?.currentPosition ?: 0L

    val currentWindowIndex: Int
        get() = player?.currentWindowIndex ?: 0

    /**
     * 缓冲的位置（具体进度）
     */
    override val bufferedPosition: Long
        get() = getIPlayer().bufferedPosition

    fun onCompletion() {
        destory()
        playerListener?.onCompletion(this)
    }

    fun onInfo(what: Int) {
        var s = "Buffing..."
        if (what == Player.STATE_READY) {
            s = "Buff End."
        }
        logD("onInfo():$s")
        when (what) {
            MEDIA_INFO_BUFFERING_START -> {
                if (!hasState(STATE_BUFFERING)) {
                    changeState(STATE_BUFFERING)
                }
                playerListener?.onBuffering(this,true,100f)
            }
            MEDIA_INFO_BUFFERING_END -> {
                if (hasState(STATE_BUFFERING)) {
                    changeState(STATE_BUFFERING, 0)
                }
                playerListener?.onBuffering(this,false,0f)
            }
            else -> playerListener?.onInfo(this, what, -1)

        }
    }

    val bit: List<ExoBit>
        get() = trackSelectionHelper[trackSelector.currentMappedTrackInfo, 0]

    fun setBit(exobit: ExoBit?) {
        trackSelectionHelper.set(exobit)
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    class ExoBit(var bit: String, var groupIndex: Int, var trackIndex: Int) {
        override fun toString(): String {
            return "bit:$bit groupIndex:$groupIndex trackIndex:$trackIndex"
        }

    }

    private fun getExoMediaRendererType(exoPlayerTrackType: Int): RendererType? {
        return when (exoPlayerTrackType) {
            C.TRACK_TYPE_AUDIO -> RendererType.AUDIO
            C.TRACK_TYPE_VIDEO -> RendererType.VIDEO
            C.TRACK_TYPE_TEXT -> RendererType.CLOSED_CAPTION
            C.TRACK_TYPE_METADATA -> RendererType.METADATA
            else -> null
        }
    }

    override fun getAudioTrack(): IntArray {
        val mappedTrackInfo = trackSelector.currentMappedTrackInfo
        if (mappedTrackInfo != null) {
            val rendererTracksInfo = getExoPlayerTracksInfo(
                RendererType.AUDIO,
                0, mappedTrackInfo
            )
            for (trackIndex in rendererTracksInfo.rendererTrackIndexes) {
                val trackGroupArray = mappedTrackInfo.getTrackGroups(trackIndex)
                trackGroupArray?.apply {
                    val audioTrackArray = IntArray(trackGroupArray.length)
                    for (i in 0 until trackGroupArray.length) {
                        audioTrackArray[i] = i
                    }
                    return audioTrackArray
                }
            }
        }
        return IntArray(0)
    }


    private fun setSelectedTrack(
        type: RendererType,
        groupIndex: Int,
        trackIndex: Int
    ) { // Retrieves the available tracks
        val mappedTrackInfo = trackSelector.currentMappedTrackInfo ?: return
        val tracksInfo = getExoPlayerTracksInfo(type, groupIndex, mappedTrackInfo)
        val trackGroupArray = if (tracksInfo.rendererTrackIndex == C.INDEX_UNSET) {
            null
        } else {
            mappedTrackInfo.getTrackGroups(tracksInfo.rendererTrackIndex)
        }
        if (trackGroupArray == null || trackGroupArray.length == 0
            || trackGroupArray.length <= tracksInfo.rendererTrackGroupIndex
        ) {
            return
        }

        // Finds the requested group
        val group = trackGroupArray.get(tracksInfo.rendererTrackGroupIndex)
        if (group == null || group.length <= trackIndex) {
            return
        }

        val parametersBuilder = trackSelector.buildUponParameters()
        for (rendererTrackIndex in tracksInfo.rendererTrackIndexes) {
            parametersBuilder.clearSelectionOverrides(rendererTrackIndex)
            if (tracksInfo.rendererTrackIndex == rendererTrackIndex) {
                // Specifies the correct track to use
                parametersBuilder.setSelectionOverride(
                    rendererTrackIndex, trackGroupArray,
                    DefaultTrackSelector.SelectionOverride(
                        tracksInfo.rendererTrackGroupIndex,
                        trackIndex
                    )
                )
                // make sure renderer is enabled
                parametersBuilder.setRendererDisabled(rendererTrackIndex, false)
            } else {
                // disable other renderers of the same type to avoid playback errors
                parametersBuilder.setRendererDisabled(rendererTrackIndex, true)
            }
        }
        trackSelector.setParameters(parametersBuilder)
    }


    private fun getExoPlayerTracksInfo(
        type: RendererType, groupIndex: Int,
        mappedTrackInfo: MappingTrackSelector.MappedTrackInfo?
    ): ExoPlayerRendererTracksInfo {
        val rendererTrackList = ArrayList<Int>()
        var rendererTrackIndex = C.INDEX_UNSET
        var rendererTrackGroupIndex = C.INDEX_UNSET
        var skippedRenderersGroupsCount = 0
        if (mappedTrackInfo != null) {
            for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
                val exoPlayerRendererType = mappedTrackInfo.getRendererType(rendererIndex)
                if (type === getExoMediaRendererType(exoPlayerRendererType)) {
                    rendererTrackList.add(rendererIndex)
                    val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
                    if (skippedRenderersGroupsCount + trackGroups.length > groupIndex) {
                        if (rendererTrackIndex == C.INDEX_UNSET) {
                            // if the groupIndex belongs to the current exo player renderer
                            rendererTrackIndex = rendererIndex
                            rendererTrackGroupIndex =
                                groupIndex - skippedRenderersGroupsCount
                        }
                    } else {
                        skippedRenderersGroupsCount += trackGroups.length
                    }
                }
            }
        }
        return ExoPlayerRendererTracksInfo(
            rendererTrackList,
            rendererTrackIndex,
            rendererTrackGroupIndex
        )
    }

    private inner class CustomMediaSourceEventListener : DefaultMediaSourceEventListener() {
        override fun onMediaPeriodCreated(windowIndex: Int, mediaPeriodId: MediaPeriodId) {
            super.onMediaPeriodCreated(windowIndex, mediaPeriodId)
        }

        override fun onMediaPeriodReleased(windowIndex: Int, mediaPeriodId: MediaPeriodId) {
            super.onMediaPeriodReleased(windowIndex, mediaPeriodId)
        }

        override fun onLoadStarted(
            windowIndex: Int,
            mediaPeriodId: MediaPeriodId?,
            loadEventInfo: LoadEventInfo,
            mediaLoadData: MediaLoadData
        ) {
            super.onLoadStarted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData)
            logD("onLoadStarted")
        }

        override fun onLoadCompleted(
            windowIndex: Int,
            mediaPeriodId: MediaPeriodId?,
            loadEventInfo: LoadEventInfo,
            mediaLoadData: MediaLoadData
        ) {
            super.onLoadCompleted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData)
            //移除监听器，避免重复回调
            //mediaSource.removeEventListener(this);
            //long duration = player.getDuration();
            logD("onLoadCompleted")
        }

        override fun onLoadCanceled(
            windowIndex: Int,
            mediaPeriodId: MediaPeriodId?,
            loadEventInfo: LoadEventInfo,
            mediaLoadData: MediaLoadData
        ) {
            super.onLoadCanceled(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData)
        }

        override fun onLoadError(
            windowIndex: Int,
            mediaPeriodId: MediaPeriodId?,
            loadEventInfo: LoadEventInfo,
            mediaLoadData: MediaLoadData,
            error: IOException,
            wasCanceled: Boolean
        ) {
            super.onLoadError(
                windowIndex,
                mediaPeriodId,
                loadEventInfo,
                mediaLoadData,
                error,
                wasCanceled
            )
        }

        override fun onReadingStarted(windowIndex: Int, mediaPeriodId: MediaPeriodId) {
            super.onReadingStarted(windowIndex, mediaPeriodId)
            logD("onReadingStarted")
        }

        override fun onUpstreamDiscarded(
            windowIndex: Int,
            mediaPeriodId: MediaPeriodId?,
            mediaLoadData: MediaLoadData){
            super.onUpstreamDiscarded(windowIndex, mediaPeriodId, mediaLoadData)
            logD("onUpstreamDiscarded")
        }

        override fun onDownstreamFormatChanged(
            windowIndex: Int,
            mediaPeriodId: MediaPeriodId?,
            mediaLoadData: MediaLoadData){
            super.onDownstreamFormatChanged(windowIndex, mediaPeriodId, mediaLoadData)
            logD("onDownstreamFormatChanged")
        }
    }


    /**
     * 关于Player.EventListene接口：
     * 两个最重要的是[ExoPlayerEvents.onPlayerStateChanged]和[ExoPlayerEvents.onPlayerError]
     * 具体的参见<a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.EventListener.html">JavaDoc</a>>
     */
    private inner class ExoPlayerEvents : Player.EventListener {
        override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
            logD("PlayerEvent:: onTimelineChanged() $timeline")
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
            logD("PlayerEvent:: onTracksChanged() $trackGroups $trackSelections")
        }

        /**
         * Called when the player starts or stops loading the source.
         */
        override fun onLoadingChanged(isLoading: Boolean) {
            logD("PlayerEvent:: onLoadingChanged $isLoading")
        }

        /**
         * 当 <a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.html#getPlayWhenReady--">Player.getPlayWhenReady()</a>>
         * 或者<a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.html#getPlaybackState--">Player.getPlaybackState()</a>>
         * 的返回值改变的时候被调用.
         *
         *
         * playbackState一共有四个状态:
         * Player.STATE_IDLE : 初始化状态，当播放器stopped或者playback失败的时候，
         * Player.STATE_BUFFERING: 缓冲状态
         * Player.STATE_READY: 播放器能够立即从当前位置开始播放（开始或者缓冲结束）
         * Player.STATE_ENDED：播放器播完了所有媒体资源
         *
         * playWhenReady的flag是为了表示用户的播放意图，
         * 播放器只有在playWhenReady=true和状态为Player.STATE_READY时才能播放。
         */
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            val stateString: String
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    stateString = "ExoPlayer.STATE_BUFFERING -"
                    onInfo(MEDIA_INFO_BUFFERING_START)
                }
                Player.STATE_ENDED -> {
                    stateString = "ExoPlayer.STATE_ENDED     -"
                    onCompletion()
                }
                Player.STATE_IDLE -> stateString = "ExoPlayer.STATE_IDLE      -"
                Player.STATE_READY -> {
                    //STATE_READY在buffer之后也会回调,所以做状态判断
                    stateString = "ExoPlayer.STATE_READY     -"
                    if (!isReady()) {
                        onPrepared()
                        playerListener?.onVideoFirstFrameShow(this@ExoPlayer)
                    }else if (isBuffering()) {
                        onInfo(MEDIA_INFO_BUFFERING_END)
                    }
                }
                else -> stateString = "UNKNOWN_STATE             -"
            }
            Log.d(TAG, "changed state to $stateString playWhenReady: $playWhenReady")
        }

        override fun onRepeatModeChanged(repeatMode: Int) {}
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
        override fun onPlayerError(error: ExoPlaybackException) {
            val rendererIndex = error.rendererIndex //错误标识
//            error.type  //错误详细信息
//            error.printStackTrace()
            logD(">>>>>>>>>>>>>>>PlayerEvent:: onPlayerError() " +
                    "code=$rendererIndex, msg=${error.message}")
            //remove buffering
            changeState(STATE_BUFFERING, 0)
            changeState(0, STATE_ERROR)
            if (rendererIndex == MEDIA_ERROR_SERVER_DIED) {
                changeState(0, STATE_REBUILD)
            }
            playerListener?.onError(this@ExoPlayer, IPlayer.MEDIA_ERROR_CUSTOM_ERROR, 0)
        }

        override fun onPositionDiscontinuity(reason: Int) {
            logD("PlayerEvent:: onPositionDiscontinuity()$reason")
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
        override fun onSeekProcessed() {}
    }

    private fun onPrepared() {
        changeState(STATE_PREPARING, STATE_PREPARED)
        if (hasState(STATE_EXO_SEEKING)) {
//            seekToTarget(seekTarget)
        } else if (hasState(STATE_PLAYING)) {
            player?.playWhenReady = true
        }

        playerListener?.onPrepared(this)
    }

    private fun isReady():Boolean{
        return player != null && hasAnyState(STATE_PREPARED)
    }
    private fun isBuffering():Boolean{
        return player != null && hasAnyState(STATE_BUFFERING)
    }


}