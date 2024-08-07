package com.kingz.view.webview.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.kingz.customdemo.R;
import com.kingz.utils.ToastTools;
import com.kingz.view.webview.js.CommonAndroidObject;
import com.kingz.view.webview.js.XJsBridgeImpl;
import com.kingz.view.webview.js.XJsInterfaceUniteWrapper;
import com.module.tools.ScreenTools;
import com.mplayer.ExtMplayer;
import com.zeke.kangaroo.zlog.ZLog;

import org.json.JSONObject;

import java.util.Locale;

/**
 * author: King.Z
 * date:  2016/9/28 22:20
 * description:
 */
public abstract class XSystemWebView extends WebView implements IWebView {

    private Context mContext;
    private String mNameOfJsCall = "JsExtApollo";
    private XJsInterfaceUniteWrapper jsInterfaceWrapper = new XJsInterfaceUniteWrapper();

    private Handler viewHandler = new Handler();
    private ExtMplayer extMplayer;

    public XSystemWebView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public XSystemWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public XSystemWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        setScaleX(1.0f);
        setScaleY(1.0f);
        getSettings().setJavaScriptEnabled(true);              //Enabling JavaScript
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setDefaultFontSize(30);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(false);
        //设置缩放
        inittialScale();

        setWebViewClient(new XWebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
                if (request.getUrl().getScheme().equals("zekefish")) {
                    // 处理自定义scheme的请求，比如启动对应Activity
                    Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    getViewContext().startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        XJsBridgeImpl defaultJsBridge = new XJsBridgeImpl(this);
        defaultJsBridge.bindInterfaceObject(new JsPlayerInterfaceObject());
        jsInterfaceWrapper.wrap(defaultJsBridge);

        addJavascriptInterface(jsInterfaceWrapper, mNameOfJsCall);  //Binding JavaScript code to Android code
        setBackgroundColor(getResources().getColor(R.color.lightslategray));
    }

    /**
     * 这里需要根据不同的分辨率设置不同的比例,比如
     * 5寸手机设置190  屏幕宽度 > 650   180
     * 4.5寸手机设置170  屏幕宽度>  500 小于 650  160
     * 4寸手机设置150  屏幕宽度>  450 小于 550  150
     * 3           屏幕宽度>  300 小于 450  120
     * 小于    300  100
     * 320×480  480×800 540×960 720×1280
     */
    private void inittialScale() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if (width > 650) {
            this.setInitialScale(190);
        } else if (width > 520) {
            this.setInitialScale(160);
        } else if (width > 450) {
            this.setInitialScale(140);
        } else if (width > 300) {
            this.setInitialScale(120);
        } else {
            this.setInitialScale(100);
        }
    }


    public Context getViewContext() {
        return mContext;
    }

    @Override
    public String _waitForJsReturnValue(int index, int i) {
        return null;
    }

    private class JsPlayerInterfaceObject extends CommonAndroidObject {

        public JsPlayerInterfaceObject() {
        }

        @JavascriptInterface
        public void showToastByJs(final String msg) {
            Log.d(getTAG(), "showToastByJs() msg=" + msg);
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastTools.i().showToast(getViewContext(), msg);
                }
            });
        }

        @JavascriptInterface
        public void requestFocus() {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    setFocusable(true);
                    requestFocus();
                }
            });
        }

        // direction: 将焦点释放给当前webview在某个方向上的相邻元素，如无相邻元素，则焦点不变化
        public void releaseFocus(String direction) {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    clearFocus();
                }
            });
        }

        /**
         * 发送JSONObject类型的信息
         */
        public void sendMessage(final String msg, final JSONObject extInfo) {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    _internalOnReceiveMessage(msg, extInfo);
                }
            });
        }

        /**
         * 发送String类型的信息
         */
        public void sendMessage(final String msg, final String extInfo) {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    _internalOnReceiveMessage(msg, extInfo);
                }
            });
        }

        public void closeBrowser(final String reason) {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    onCloseBrowser(reason);
                }
            });
        }

        // 调整浏览器尺寸，调整后，浏览器保持在屏幕上居中位置
        public void resizeBrowser(final double width, final double height) {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    onResizeBrowser(width, height);
                }
            });
        }

        public void moveBrowser(final double x, final double y) {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    onMoveBrowser(x, y);
                }
            });
        }

        public void moveBrowserEx(final double x, final double y, final double width, final double height) {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    onMoveBrowser(x, y, width, height);
                }
            });
        }

        public String getBrowserPosition() {
            Rect rectInScreen = new Rect();
            getGlobalVisibleRect(rectInScreen);
            return String.format(Locale.getDefault(),"%d,%d,%d,%d", rectInScreen.left, rectInScreen.top, rectInScreen.width(), rectInScreen.height());
        }

        @JavascriptInterface
        public String createPlayer(final String x, final String y,final String width, final String height){
            ZLog.i(getTAG(), "createPlayer() createPlayer  width = " + width);
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    extMplayer = new ExtMplayer(mContext);
                    extMplayer.createPlayer(x,y,width,height);
                    ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ScreenTools.OperationWidth(Integer.valueOf(width)),
                            ScreenTools.OperationWidth(Integer.valueOf(height)));
                    extMplayer.setX(ScreenTools.OperationWidth(Integer.valueOf(x)));
                    extMplayer.setY(ScreenTools.OperationWidth(Integer.valueOf(y)));
                    addView(extMplayer,lps);
                }
            });
            if(extMplayer != null){
                return String.valueOf(extMplayer.getLatestPlayerId());
            }
            return "-1";
        }


        @JavascriptInterface
        public void playVideoByUrl(final String playerId, final String playUrl,final String position){
            ZLog.i(getTAG(), "playVideoByUrl() playerId = " + playerId + ";playUrl = " + playUrl + ";position=" + position);
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(extMplayer != null){
                        extMplayer.playVideoByUrl(playerId,playUrl,position);
                    }
                }
            });
        }

        @JavascriptInterface
        public void playVideoByUrl(final String playerId,final String position){
            ZLog.i(getTAG(), "playVideoByUrl() playerId = " + ";position=" + position);
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(extMplayer != null){
                        //extMplayer.playVideoByUrl(playerId,"rtsp://222.83.47.212:554/vod/00000050280004173152.mpg?userid=91494124504&stbip=182.138.101.47&clienttype=1&ifcharge=1&time=20170717181940+08&life=172800&vcdnid=001&boid=001&srcboid=001&backupagent=222.83.47.212:554&ctype=50&playtype=0&Drm=0&programid=00000050280004173152&contname=&fathercont=&authid=0&tscnt=0&tstm=0&tsflow=0&ifpricereqsnd=1&stbid=3C-DA-2A-B1-49-B7&nodelevel=3&terminalflag=1&distype=0",position);
                        extMplayer.playVideoByUrl(playerId,"http://video.chinanews.com/tvmining/News/MP4ZXW/DongNanTV/2016/04/29/DongNanTV_1500000_20160429_18866283_0_40.mp4",position);
                    }
                }
            });
        }
        @JavascriptInterface
        public void play(final String playerId){
            ZLog.i(getTAG(), "playVideoByUrl() playerId = " + playerId);
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (extMplayer != null) {
                        extMplayer.play(playerId);
                    }
                }
            });
        }
        @JavascriptInterface
        public void pause(final String playerId){
            ZLog.i(getTAG(), "playVideoByUrl() playerId = " + playerId );
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (extMplayer != null) {
                        extMplayer.pause(playerId);
                    }
                }
            });
        }
    }

    private void _internalOnReceiveMessage(String msg, Object info) {
        this.onReceiveMessage(msg, info);
    }

    //    public abstract void onSetHandler(String handlerName, JSCallback callback);
    public abstract void onReceiveMessage(String msg, Object info);
    public abstract void onCloseBrowser(String reason);
    public abstract void onShowBrowser(double width, double height);
    public abstract void onResizeBrowser(double width, double height);
    public abstract void onMoveBrowser(double x, double y);
    public abstract void onMoveBrowser(double x, double y, double width, double height);
}