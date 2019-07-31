package com.kingz.library.player.exo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.kingz.library.player.AbstractMediaPlayer;
import com.kingz.library.player.IMediaPlayer;
import com.kingz.library.player.helper.TrackSelectionHelper;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.UUID;

/**
 * author：KingZ
 * date：2019/7/30
 * description：EXO 媒体播放器
 * 使用方式：
 *
 * <pre> {@code
 * ExoMediaPlayer exoPlayer = new ExoMediaPlayer(context);
 * exoPlayer.setPlayerView(playerView);
 * exoPlayer.setPlayURI(playUri);
 * }</pre>
 *
 * <p>关于Player.EventListene接口：
 * 两个最重要的是{@link #onPlayerStateChanged}和{@link #onPlayerError}
 * 具体的参见<a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.EventListener.html">JavaDoc</>
 */

public class ExoMediaPlayer extends AbstractMediaPlayer implements Player.EventListener {
    private static final String TAG = ExoMediaPlayer.class.getName();
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER = new CookieManager();

    static {
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
    }

    private SimpleExoPlayer player;
    private EventLogger eventLogger;
    private DefaultTrackSelector trackSelector;
    private DataSource.Factory mediaDataSourceFactory;
    private TrackSelectionHelper trackSelectionHelper;
    private boolean playerNeedsSource;
    private boolean autoPlay;

    private UUID drmSchemeUuid = null;
    private String drmLicenseUrl = null;
    private Uri[] uris;
    private String[] extensions;

    public ExoMediaPlayer(Context context) {
        mContext = context;
        autoPlay = true;
        mediaDataSourceFactory = buildDataSourceFactory(BANDWIDTH_METER);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        logout("播放器回调 onTimelineChanged() " + timeline);
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        logout("播放器回调 onTracksChanged() " + trackGroups + " " + trackSelections);
    }

    /**
     * Called when the player starts or stops loading the source.
     */
    @Override
    public void onLoadingChanged(boolean isLoading) {
        logout("播放器回调 onLoadingChanged " + isLoading);
    }

    /**
     * 当 <a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.html#getPlayWhenReady--">Player.getPlayWhenReady()</>
     * 或者<a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.html#getPlaybackState--">Player.getPlaybackState()</>
     * 的返回值改变的时候被调用.
     * <p>
     * playbackState一共有四个状态:
     * Player.STATE_IDLE : 初始化状态，当播放器stopped或者playback失败的时候，
     * Player.STATE_BUFFERING: 缓冲状态
     * Player.STATE_READY: 播放器能够立即从当前位置开始播放
     * Player.STATE_ENDED：播放器播完了所有媒体资源
     * <p>
     * playWhenReady的flag是为了表示用户的播放意图，
     * 播放器只有在playWhenReady=true和状态为Player.STATE_READY时才能播放。
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String stateString;
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                stateString = "ExoPlayer.STATE_BUFFERING -";
                onInfo(AbstractMediaPlayer.MEDIA_INFO_BUFFERING_START);
                break;
            case Player.STATE_ENDED:
                stateString = "ExoPlayer.STATE_ENDED     -";
                onCompletion();
                break;
            case Player.STATE_IDLE:
                stateString = "ExoPlayer.STATE_IDLE      -";
                break;
            case Player.STATE_READY:
                stateString = "ExoPlayer.STATE_READY     -";
                if (!isPrepared()) {
                    isPrepared = true;
//                    onPrepared();
                }
                if (isBuffering()) {
                    onInfo(AbstractMediaPlayer.MEDIA_INFO_BUFFERING_END);
                }
                break;
            default:
                stateString = "UNKNOWN_STATE             -";
                break;
        }
        Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        error.printStackTrace();
        logout("播放器回调 onPlayerError()" + error.getMessage());
        isPrepared = false;
        isPaused = false;
        isBufferIng = false;
        if (playCallBack != null) {
            playCallBack.onError(this, IMediaPlayer.MEDIA_ERROR_CUSTOM_ERROR, 0);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        logout("播放器回调 onPositionDiscontinuity()" + reason);
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onSeekProcessed() {
    }

    @Override
    public void seekTo(long msec) {
        if (player == null) {
            return;
        }
        long duration = player.getDuration();
        msec = msec < 0 ? 0 : msec;
        if (msec == duration) {
            msec = msec - 10000;
        }
        player.seekTo(msec);
    }

    @Override
    public void setPlayURI(Uri uri) {
        uris = new Uri[]{uri};
        extensions = new String[uris.length];
        initializePlayer();
    }

    private void initializePlayer() {
        if (player == null) {
            DefaultRenderersFactory renderersFactory = createRenderersFactoryWithDRM();
            if (renderersFactory == null) {
                return;
            }

            createTrackSelector();
//            player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(mContext), //默认渲染工厂
                    new DefaultTrackSelector(), // 默认的轨道选择器（DefaultTrackSelector）
                    new DefaultLoadControl());  // 默认的加载

            player.addListener(this);
            addEventLoggerWithPlayer();

            setSurface(null);
            player.setPlayWhenReady(autoPlay);
            playerNeedsSource = true;
        }

        if (playerNeedsSource) {
            if (uris == null || uris.length <= 0) {
                onPlayerError(ExoPlaybackException.createForSource(new IOException("no uris")));
                return;
            }
            MediaSource[] mediaSources = new MediaSource[uris.length];
            for (int i = 0; i < uris.length; i++) {
                mediaSources[i] = buildMediaSource(uris[i], extensions[i], mainHandler, eventLogger);
            }
            MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                    : new ConcatenatingMediaSource(mediaSources);
            player.prepare(mediaSource);
            playerNeedsSource = false;
        }
    }

    private void addEventLoggerWithPlayer() {
        eventLogger = new EventLogger(trackSelector);
        player.addListener(eventLogger);
        player.addMetadataOutput(eventLogger);
        player.addAudioDebugListener(eventLogger);
        player.addVideoDebugListener(eventLogger);
    }

    /**
     * 创建轨道选择器
     */
    private void createTrackSelector() {
        TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        trackSelectionHelper = new TrackSelectionHelper(trackSelector, adaptiveTrackSelectionFactory);
    }

    /**
     * 创建带DRM的渲染工厂
     *
     * @return DefaultRenderersFactory
     */
    private DefaultRenderersFactory createRenderersFactoryWithDRM() {
        DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
        if (drmSchemeUuid != null) {
            try {
                drmSessionManager = buildDrmSessionManager(drmSchemeUuid, drmLicenseUrl);
            } catch (UnsupportedDrmException e) {
                e.printStackTrace();
                onPlayerError(ExoPlaybackException.createForSource(new IOException("drmSessionManager == null")));
                return null;
            }
        }
        int extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        return new DefaultRenderersFactory(this.mContext, drmSessionManager, extensionRendererMode);
    }


    /**
     * 创建{@link MediaSource}对象
     * DASH (DashMediaSource),
     * SmoothStreaming (SsMediaSource),
     * HLS (HlsMediaSource)，
     * 其他的格式使用 (ExtractorMediaSource).
     * <p>
     * <a href="https://exoplayer.dev/supported-formats.html">EXOPLAYER Support Formats</a>
     * <p>
     * MP4格式应该使用ExtractorMediaSource。
     *
     * @param uri               播放地址的uri
     * @param overrideExtension 扩展参数
     * @return 创建的MediaSource对象
     */
    private MediaSource buildMediaSource(Uri uri,
                                         String overrideExtension,
                                         @Nullable Handler handler,
                                         @Nullable MediaSourceEventListener listener) {
        @C.ContentType int type;
        if (TextUtils.isEmpty(overrideExtension)) {
            type = Util.inferContentType(uri);
        } else {
            type = Util.inferContentType("." + overrideExtension);
        }
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(null))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(null))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            default: {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid, String licenseUrl) throws UnsupportedDrmException {
        if (Util.SDK_INT < 18) {
            return null;
        }

        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, false,
                buildHttpDataSourceFactory(false ? BANDWIDTH_METER : null));
        return new DefaultDrmSessionManager<>(uuid,
                FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, eventLogger);
    }

    @Override
    public void setSpeed(float speed) {
        if (player != null) {
            player.setPlaybackParameters(new PlaybackParameters(speed, 1.0f));
        }
    }

    public void setRepeatMode(@Player.RepeatMode int repeatMode) {
        if (player != null) {
            player.setRepeatMode(repeatMode);
        }
    }

    @Override
    public void setBufferSize(int bufferSize) {

    }

    @Override
    protected void setSurface(Surface surface) {
        if (player != null) {
            this.player.addTextOutput(null);
            this.player.setVideoSurface(surface);
            if (playView instanceof TextureView) {
                player.setVideoTextureView((TextureView) playView);
            } else if (playView instanceof SurfaceView) {
                player.setVideoSurfaceView((SurfaceView) playView);
            }
        }
    }

    @Override
    protected void attachListener() {

    }

    @Override
    protected void detachListener() {
        if (player != null) {
            player.removeListener(this);
        }
    }

    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(mContext.getApplicationContext(), bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory("kingz_exo_media_player", bandwidthMeter);
    }

    private void releasePlayer() {
        if (player != null) {
            autoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
            eventLogger = null;
            trackSelectionHelper = null;
        }
    }

    @Override
    public Uri getCurrentURI() {
        return uris == null || uris.length <= 0 ? Uri.parse("") : uris[0];
    }

    @Override
    public void release() {
        super.release();
        reset();
        releasePlayer();
    }


    @Override
    public void play() {
        super.play();
        if (player == null) {
            return;
        }
        player.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        super.pause();
        if (player == null) {
            return;
        }
        player.setPlayWhenReady(false);
    }

    @Override
    public boolean isPlaying() {
        return player != null && player.getPlayWhenReady();
    }

    @Override
    public long getDuration() {
        if (player == null) { return 0L; }
        return player.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        if (player == null) { return 0L; }
        return player.getCurrentPosition();
    }

    public int getCurrentWindowIndex(){
        if (player == null) { return 0;  }
        return player.getCurrentWindowIndex();
    }

    /**
     * 缓冲的位置（具体进度）
     */
    @Override
    public long getBufferedPosition() {
        if (player == null) { return 0L; }
        return player.getBufferedPosition();
    }

    public void onCompletion() {
        release();
        if (playCallBack != null) {
            playCallBack.onCompletion(this);
        }
    }

    public void onInfo(int what) {
        String s = "正在缓冲";
        if (what == Player.STATE_BUFFERING) {

        } else if (what == Player.STATE_READY) {
            s = "缓冲结束";
        }
        logout("onInfo():" + s);
        switch (what) {
            case MEDIA_INFO_BUFFERING_START:
                isBufferIng = true;
                if (playCallBack != null) {
                    playCallBack.onBufferStart(this);
                }
                break;
            case MEDIA_INFO_BUFFERING_END:
                isBufferIng = false;
                if (playCallBack != null) {
                    playCallBack.onBufferEnd(this);
                }
                break;
            default:
                if (playCallBack != null) {
                    playCallBack.onInfo(this, what, -1);
                }
                break;
        }
    }

    static void logout(String log) {
        Log.d(TAG, log);
    }

    public List<ExoBit> getBit() {
        List<ExoBit> exobits = trackSelectionHelper.get(trackSelector.getCurrentMappedTrackInfo(), 0);
        return exobits;
    }

    public void setBit(ExoBit exobit) {
        trackSelectionHelper.set(exobit);
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public static class ExoBit {
        public ExoBit(String bit, int groupIndex, int trackIndex) {
            this.bit = bit;
            this.groupIndex = groupIndex;
            this.trackIndex = trackIndex;
        }

        public String bit;
        public int groupIndex;
        public int trackIndex;

        @Override
        public String toString() {
            return "bit:" + bit + " groupIndex:" + groupIndex + " trackIndex:" + trackIndex;
        }
    }

    @Override
    public IMediaPlayer getMediaPlayer() {
        return this;
    }
}
