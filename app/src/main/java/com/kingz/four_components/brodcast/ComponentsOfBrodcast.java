package com.kingz.four_components.brodcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/1/2 0:42
 * description: 广播分为两种：普通广播（Normal broadcasts）和有序广播（Ordered broadcasts）。
 * ------> 普通:
 * <p/>
 * 【发送】：Context.sendBroadcast()  所有订阅者都会收到并进行处理。
 * <p/>
 * ------> 有序：
 * #设置优先级：优先级别声明在 intent-filter 元素的 android:priority
 * 属性中，数越大优先级别越高,取值范围:-1000到1000，优先级别也可以调
 * 用IntentFilter对象的setPriority()进行设置 。
 * 【发送】：Context.sendOrderedBroadcast()  按优先级接收处理并处理。
 * 前面的接收者有权终止广播(BroadcastReceiver.abortBroadcast())，并可以
 * 将数据通过setResultExtras(Bundle)方法存放进结果对象传递给后者。
 */
public class ComponentsOfBrodcast extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SMS_RECEIVED.equals(action)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                for (Object pdu : pdus) {
                    /* 要特别注意，这里是android.telephony.SmsMessage 可不是  android.telephony.SmsManager  */
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = message.getOriginatingAddress();
                    String conetnt = message.getMessageBody();
                    Date date = new Date(message.getTimestampMillis());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = dateFormat.format(date);
                    //sendSMS(sender, conetnt, time);

                    /* 实现黑名单功能，下面这段代码将  5556 发送者的短信屏蔽
                     * 前提是，本接受者的优先权要高于 Android 内置的短信应用程序 */
                    if ("5556".equals(sender)) {
                        abortBroadcast();
                    }
                }
            }
        }
    }
}
