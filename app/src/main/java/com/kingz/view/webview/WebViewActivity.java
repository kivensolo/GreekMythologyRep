package com.kingz.view.webview;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * Created by KingZ on 2016/1/16.
 * Discription:
 * 1、可以是用loadData，这种方法需要先将html文件读取出来，以字符串传入loadData，可以展示页面，但是不会引用css、js等文件。

   2、使用loadUrl，不过需要注意，这里因为是使用本地数据，所以传入的url需要做些处理，例如：
     a、如果html文件存于assets：则加前缀：file:///android_asset/
     b、如果html文件存于sdcard：则加前缀：content://com.android.htmlfileprovider/sdcard/
     注意：content前缀可能导致异常，直接使用file:///sdcard/ or file:/sdcard也可以
 *
 */
public class WebViewActivity extends Activity{

    public static final String TAG = "WebViewActivity";
    private WebViewWithJs mWebView;
    public static final String testUrl = "file:///android_asset/webAppTest.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intView();
    }

    private void intView() {
        initWebView();
        setContentView(mWebView);
        mWebView.loadUrl(testUrl);
    }

    private void initWebView() {
        mWebView = new WebViewWithJs(this){

            @Override
            public void onReceiveMessage(String msg, Object info) {

            }

            @Override
            public void onCloseBrowser(String reason) {

            }

            @Override
            public void onShowBrowser(double width, double height) {

            }

            @Override
            public void onResizeBrowser(double width, double height) {

            }

            @Override
            public void onMoveBrowser(double x, double y) {

            }

            @Override
            public void onMoveBrowser(double x, double y, double width, double height) {

            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
