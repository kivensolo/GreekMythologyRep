package com.kingz.net;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zeke.kangaroo.utils.NetUtils;
import com.zeke.ktx.App;

/**
 * author：KingZ
 * date：2019/5/29
 * description：网络状态控制类
 * 暂时无业务代码，只有初步的功能代码
 */
public class NetStateController {
    private final ConnectivityManager _connectivityManager;

    BroadcastReceiver _networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkConnectivityState();
        }
    };

    public NetStateController() {
        _connectivityManager = (ConnectivityManager) App.Companion.getInstance().getApplicationContext().getSystemService(Application.CONNECTIVITY_SERVICE);
        checkConnectivityState();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        App.Companion.getInstance().getApplicationContext().registerReceiver(_networkStateReceiver, intentFilter);
    }

    private void checkConnectivityState() {
        NetworkInfo mNetworkInfo = _connectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            //判断是有线连接还是Wifi
            if (NetUtils.isConnect(_connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET))) {
                //有线网络已连接
            } else if (NetUtils.isConnect(_connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI))) {
                //WIfi网络已连接
            } else {
                //其他网络已连接
            }
        } else {
            //网络断开
        }
    }
}
