package com.kingz.view.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * author: King.Z
 * date:  2016/9/28 21:52
 * description:
 */
public class ZWebChromClient extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}
