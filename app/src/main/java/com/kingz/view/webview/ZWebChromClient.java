package com.kingz.view.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
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
