package com.kingz.module.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zeke.kangaroo.utils.NetUtils;
import com.zeke.kangaroo.zlog.ZLog;

/**
 * 网络变化的广播接收器
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkChangeReceiver.class.getSimpleName();

    private static final String CONNECT_STATE_TRUE = "connect";
    private static final String CONNECT_STATE_FALSE = "disconnect";

    private static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    private static String lastConnectState = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.equals(action, ACTION_NETWORK_CHANGE)) {
            return;
        }
        String connectState = NetUtils.isConnect(context) ? CONNECT_STATE_TRUE : CONNECT_STATE_FALSE;
        if (TextUtils.equals(lastConnectState, connectState)) {
            return;
        }
        lastConnectState = connectState;
        boolean isConnect = TextUtils.equals(lastConnectState, CONNECT_STATE_TRUE);
        ZLog.d("Send message tag=CommonMessage.EVENT_NETWORK_CHANGE.isConnect="+isConnect);
//        XulMessageCenter.buildMessage().setTag(CommonMessage.EVENT_NETWORK_CHANGE).setData(isConnect).post();
        //TODO 进行组件事件分发
    }
}
