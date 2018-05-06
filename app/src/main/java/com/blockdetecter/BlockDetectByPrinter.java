package com.blockdetecter;

import android.os.Looper;
import android.util.Printer;

/**
 * Copyright(C) 2018, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2018/2/4 13:00 <br>
 * description: 卡顿检测打印 <br>
 */
public class BlockDetectByPrinter {
    public static void start() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
           private static final String START = ">>>>> Dispatching";
           private static final String END = "<<<<< Finished";
            @Override
            public void println(String x) {
              if (x.startsWith(START)) {
                    LogMonitor.getInstance().startMonitor();
                }
              if (x.startsWith(END)) {
                    LogMonitor.getInstance().removeMonitor();
                }
            }
        });
    }
}
