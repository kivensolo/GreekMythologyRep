package com.kingz.module.common;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zeke.wang
 * @date 2020/7/2
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
public class ZLogUtils {
    private static boolean IS_SHOW_LOG = true;
    private static final String DEFAULT_MESSAGE = "execute";
    private static final String LINE_SEPARATOR;
    private static final int JSON_INDENT = 4;
    private static final int V = 1;
    private static final int D = 2;
    private static final int I = 3;
    private static final int W = 4;
    private static final int E = 5;
    private static final int A = 6;
    private static final int JSON = 7;

    public static void init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    public static void v() {
        printLog(1, (String)null, "execute");
    }

    public static void v(Object msg) {
        printLog(1, (String)null, msg);
    }

    public static void v(String tag, String msg) {
        printLog(1, tag, msg);
    }

    public static void d() {
        printLog(2, (String)null, "execute");
    }

    public static void d(Object msg) {
        printLog(2, (String)null, msg);
    }

    public static void d(String tag, Object msg) {
        printLog(2, tag, msg);
    }

    public static void i() {
        printLog(3, (String)null, "execute");
    }

    public static void i(Object msg) {
        printLog(3, (String)null, msg);
    }

    public static void i(String tag, Object msg) {
        printLog(3, tag, msg);
    }

    public static void w() {
        printLog(4, (String)null, "execute");
    }

    public static void w(Object msg) {
        printLog(4, (String)null, msg);
    }

    public static void w(String tag, Object msg) {
        printLog(4, tag, msg);
    }

    public static void e() {
        printLog(5, (String)null, "execute");
    }

    public static void e(Object msg) {
        printLog(5, (String)null, msg);
    }

    public static void e(String tag, Object msg) {
        printLog(5, tag, msg);
    }

    public static void a() {
        printLog(6, (String)null, "execute");
    }

    public static void a(Object msg) {
        printLog(6, (String)null, msg);
    }

    public static void a(String tag, Object msg) {
        printLog(6, tag, msg);
    }

    public static void json(String jsonFormat) {
        printLog(7, (String)null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(7, tag, jsonFormat);
    }

    private static void printLog(int type, String tagStr, Object objectMsg) {
        if (IS_SHOW_LOG) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int index = 4;
            String className = stackTrace[index].getFileName();
            String methodName = stackTrace[index].getMethodName();
            int lineNumber = stackTrace[index].getLineNumber();
            String tag = tagStr == null ? className : tagStr;
            methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");
            String msg;
            if (objectMsg == null) {
                msg = "Log with null Object";
            } else {
                msg = objectMsg.toString();
            }

            if (msg != null && type != 7) {
                stringBuilder.append(msg);
            }

            String logStr = stringBuilder.toString();
            switch(type) {
            case 1:
                Log.v(tag, logStr);
                break;
            case 2:
                Log.d(tag, logStr);
                break;
            case 3:
                Log.i(tag, logStr);
                break;
            case 4:
                Log.w(tag, logStr);
                break;
            case 5:
                Log.e(tag, logStr);
                break;
            case 6:
                Log.wtf(tag, logStr);
                break;
            case 7:
                if (TextUtils.isEmpty(msg)) {
                    Log.d(tag, "Empty or Null json content");
                    return;
                }

                String message = null;

                try {
                    if (msg.startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(msg);
                        message = jsonObject.toString(4);
                    } else if (msg.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(msg);
                        message = jsonArray.toString(4);
                    }
                } catch (JSONException var19) {
                    e(tag, var19.getCause().getMessage() + "\n" + msg);
                    return;
                }

                printLine(tag, true);
                message = logStr + LINE_SEPARATOR + message;
                String[] lines = message.split(LINE_SEPARATOR);
                StringBuilder jsonContent = new StringBuilder();
                String[] var15 = lines;
                int var16 = lines.length;

                for(int var17 = 0; var17 < var16; ++var17) {
                    String line = var15[var17];
                    jsonContent.append("║ ").append(line).append(LINE_SEPARATOR);
                }

                Log.e(tag, jsonContent.toString());
                printLine(tag, false);
            }

        }
    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }

    }

    static {
        IS_SHOW_LOG = true;
        LINE_SEPARATOR = System.getProperty("line.separator");
    }
}
