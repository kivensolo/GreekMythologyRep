package com.kingz.four_components.brodcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.kingz.utils.ZLog;

/**
 * author: King.Z <br>
 * date:  2017/9/1 14:44 <br>
 * description: <br>
 */
public class PaySucessBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int code = intent.getIntExtra("code",1);
        String import_id = intent.getStringExtra("import_id");
        String reason = intent.getStringExtra("reason");
        String pay_channel = intent.getStringExtra("pay_channel");
        ZLog.d("PaySucessBroadcast", "code = " + code + ";import_id = " + import_id + ";reason = " + reason + ";pay_channel=" + pay_channel);
    }
}
