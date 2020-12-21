package com.kingz.mobile.libhlscache.bean;

/**
 * 下载结果回调。
 * Created  2017/11/16.
 */
public interface OnResultListener {

    void onDownloadError(String id, Exception e);

    void onDownloadFinish(String id);

}
