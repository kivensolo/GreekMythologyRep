package com.mplayer;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.module.tools.ScreenTools;
import com.zeke.kangaroo.utils.ZLog;

import java.util.HashMap;

//import com.module.tools.ScreenTools;

/**
 * author: King.Z <br>
 * date:  2017/7/17 10:31 <br>
 * description: 用于webview的原生播放器 <br>
 */
public class ExtMplayer extends RelativeLayout implements IExtMplayer {

    private static final String TAG = ExtMplayer.class.getSimpleName();

    /**播放器容器*/
    private HashMap<String, KingzPlayerView> mPlayerMap = new HashMap<>();

    /**异常反馈值*/
    public static final String ERROR = "-1";

    /**最新创建的播放器id*/
    private int mLatestPlayerid;

    private Context mContext;

    public ExtMplayer(Context context) {
        super(context);
        mContext = context;
    }

    public ExtMplayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ExtMplayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    public String createPlayer(String x, String y, String width, String height) {
        ZLog.d(TAG,"createPlayer().");
        if(TextUtils.equals(width,"0") || TextUtils.equals(height,"0")){
            ZLog.e(TAG,"createPlayer() Invalid parameter.");
            return ERROR;
        }
        initMplayerCore(mContext,x,y,width,height);
        return String.valueOf(mLatestPlayerid++);
    }

    void initMplayerCore(Context context,String x, String y, String width, String height) {
        final KingzPlayerView mpCore = new KingzPlayerView(context);
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ScreenTools.Operation(Integer.valueOf(width)),ScreenTools.Operation(Integer.valueOf(height)));
        mpCore.setLayoutParams(lps);
        addView(mpCore);
        mPlayerMap.put(String.valueOf(mLatestPlayerid),mpCore);
    }

    public int getLatestPlayerId(){
        return mLatestPlayerid;
    }

    @Override
    public String playVideo(String playerId, String videoId, String videoIndex, String position, String source) {
        //暂时无通过此方法调用的业务
        return ERROR;
    }

    @Override
    public String playVideoByUrl(String playerId, String playUrl, String position) {
        KingzPlayerView playerCore = getMplayerCore(playerId);
        if(playerCore != null){
            Uri uri = Uri.parse(playUrl);
            playerCore.setVideoURI(uri);
            return playerId;
        }
        return ERROR;
    }

    @Override
    public String pause(String playerId) {
        KingzPlayerView playerCore = getMplayerCore(playerId);
        if(playerCore != null){
            playerCore.pause();
            return playerId;
        }
        return ERROR;
    }

    @Override
    public String stop(String playerId) {
        KingzPlayerView playerCore = getMplayerCore(playerId);
        if (playerCore != null) {
            playerCore.stop();
            return playerId;
        }
        return ERROR;
    }

    @Override
    public String play(String playerId) {
        KingzPlayerView playerCore = getMplayerCore(playerId);
        if (playerCore != null) {
            playerCore.start();
            return playerId;
        }
        return ERROR;
    }

    @Override
    public String destory(String playerId) {
        KingzPlayerView core = mPlayerMap.remove(playerId);
        if(core != null){
            mLatestPlayerid--;
            return playerId;
        }
        return ERROR;
    }

    @Override
    public String getDuration(String playerId) {
        KingzPlayerView core = getMplayerCore(playerId);
        if(core != null){
            int duration = core.getDuration();
            ZLog.d(TAG,"getDuration = " + duration);
            return String.valueOf(duration);
        }
        return ERROR;
    }

    @Override
    public String getCurrentPosition(String playerId) {
        KingzPlayerView core = getMplayerCore(playerId);
        if(core != null){
            int currentPosition = core.getCurrentPosition();
            ZLog.d(TAG,"getCurrentPosition = " + currentPosition);
            return String.valueOf(currentPosition);
        }
        return ERROR;
    }

    @Override
    public String seekTo(String playerId, String pos) {
        KingzPlayerView core = getMplayerCore(playerId);
        if(core != null){
            core.seekTo(Integer.parseInt(pos));
            return playerId;
        }
        return ERROR;
    }

    private KingzPlayerView getMplayerCore(String playerId) {
        return mPlayerMap.get(playerId);
    }

    @Override
    public String getPlayerState(String playerId) {
        KingzPlayerView core = getMplayerCore(playerId);
        if (core != null) {
            //int playState = core.getPlayState();
            int playState = 0;
            String state = ERROR;
            if(playState == 0){
                state = "STATE_IDLE";
            }else if(playState == 1){
                state = "STATE_PREPARING";
            }else if(playState == 2){
                state = "STATE_PREPARED";
            }else if(playState == 3){
                state = "STATE_PLAYING";
            }else if(playState == 4){
                state = "STATE_PAUSED";
            }else if(playState == 5){
                state = "STATE_PLAYBACK_COMPLETED";
            }
            return state;
        }
        return ERROR;
    }

    @Override
    public String fullScreen(String playerId) {
        return setPlayerPosAndWH(playerId,"0","0","1","1");
    }



    @Override
    public String setPlayerPosAndWH(String playerId, String x, String y, String width, String height) {
        //TODO 全屏幕处理
        return ERROR;
    }


}
