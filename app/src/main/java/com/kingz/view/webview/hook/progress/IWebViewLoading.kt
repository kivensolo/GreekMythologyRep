package com.kingz.view.webview.hook.progress

import android.content.Context
interface IWebViewLoading {
   fun onProgress(context: Context, newProgress:Int)

}