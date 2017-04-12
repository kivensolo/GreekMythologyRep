package com.kingz.four_components.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2017/1/17 22:18
 * description:其他应用安装被覆盖安装后的广播接受器
 */

public class OtherAppReplacedReceiver extends BroadcastReceiver {
        private static final String TAG = "OtherAppReplacedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //doOtherThing

    }
}
