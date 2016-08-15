package com.nativeWidgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kingz.customdemo.R;

/**
 * Created by KingZ on 2016/1/16.
 * Discription:
 */
public class WebViewUseJs extends Activity{

    public static final String TAG = "WebViewUseJs";
    private Context context;
    private WebView mWebView;
    private WebAPPInterface webAPPInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webAPPInterface = new WebAPPInterface(this);

        intView();
    }

    private void intView() {
        mWebView = new WebView(this);
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);              //Enabling JavaScript
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setDefaultFontSize(30);
        webSetting.setLoadWithOverviewMode(true);

        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);

        mWebView.addJavascriptInterface(webAPPInterface,"KingZWebAPP");  //Binding JavaScript code to Android code
        mWebView.setBackgroundColor(getResources().getColor(R.color.chartreuse));
        //影响浏览器的用户界面发生变化时
        final Activity activity = this;
        mWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress) {
                 // Activities and WebViews measure progress with different scales.
                 // The progress meter will automatically disappear when we reach 100%
                 activity.setProgress(progress * 1000);
               }

        });
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted url = " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished url = " + url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.d(TAG, "onLoadResource url = " + url);
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d(TAG, "onReceivedError error = " + errorCode + ";description = " + description);
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        mWebView.loadUrl("file:///android_asset/webAppTest.html");
//        mWebView.loadUrl("http://cbc.starcor1.net:57880/nn_cms/api/xjiptv/kukan_pay/index.html");
        setContentView(mWebView);
    }

    public class WebAPPInterface{
        private Context context;

        public WebAPPInterface(Context context) {
            this.context = context;
        }
        /** Show a toast from the web page */
        // If set targetSdkVersion to 17 or higher,
        // Must add the @JavascriptInterface annotation.
        @JavascriptInterface
        public void showToast(String msg){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
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
