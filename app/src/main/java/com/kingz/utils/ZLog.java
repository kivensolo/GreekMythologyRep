package com.kingz.utils;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.MissingFormatArgumentException;

//Logcat统一管理类
public class ZLog {
    private static final String TAG = "Zlog";
    private ZLog() {
        throw new Error("Do not need instantiate!");
    }

    // 允许输出日志
    public static boolean isDebug = true;       // 是否需要打印bug，可以在application的onCreate函数里面初始化

    // Log过滤级别
    private static LogType _logLevel = LogType.DEBUG;

    public enum LogType {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        logString(LogType.INFO, TAG, msg);
    }
    public static void i(String tag, String msg) {
        logString(LogType.INFO, tag, msg);
    }

    public static void d(String msg) {
        logString(LogType.DEBUG, TAG, msg);
    }
    public static void d(String tag, String msg) {
        logString(LogType.DEBUG, tag, msg);
    }

    public static void e(String msg) {
        logString(LogType.ERROR, TAG, msg);
    }
    public static void e(String tag, String msg) {
        logString(LogType.ERROR, tag, msg);
    }


    public static void v(String msg) {
        logString(LogType.VERBOSE, TAG, msg);
    }
    public static void v(String tag, String msg) {
        logString(LogType.VERBOSE, tag, msg);
    }

    public static void w(String msg) {
        logString(LogType.WARN, TAG, msg);
    }
    public static void w(String tag, String msg) {
        logString(LogType.WARN, tag, msg);
    }

    public static void i(String tag, String msg,Throwable tr) {
        if (isDebug) Log.i(tag, msg,tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isDebug) Log.d(tag, msg, tr);
    }

    public static void e(String tag, String msg,Throwable tr) {
        if (isDebug) Log.e(tag, msg,tr);
    }

    public static void v(String tag, String msg,Throwable tr) {
        if (isDebug) Log.v(tag, msg,tr);
    }
    public static void w(String tag, String msg,Throwable tr) {
        if (isDebug) Log.w(tag, msg,tr);
    }

    /**
     * 根据当前的参数log等级判断是否需要打印log
     */
    public static boolean needPrintLog(LogType type) {
        return isDebug && (type.ordinal() >= _logLevel.ordinal());
    }

    /**
     * 打印字符串
     */
    private static void logString(LogType type, String tag, String msg, Object... args) {
        if (!needPrintLog(type)) {
            return;
        }

        if (args.length > 0) {
            msg = getFormatMsg(msg, args);
        }

        switch (type) {
            case VERBOSE:
                Log.v(tag, msg);
                break;
            case DEBUG:
                Log.d(tag, msg);
                break;
            case INFO:
                Log.i(tag, msg);
                break;
            case WARN:
                Log.w(tag, msg);
                break;
            case ERROR:
                Log.e(tag, msg);
                break;
            case ASSERT:
                Log.wtf(tag, msg);
                break;
            default:
                break;
        }
    }

    private static String getFormatMsg(String msg, Object[] args) {
        String result = "";

        if (msg == null) {
            msg = "<null>";
        } else {
            try {
                result = String.format(msg, args);
            } catch (MissingFormatArgumentException e) {
            }
        }

        // 简单判断是否格式化正确
        if (TextUtils.isEmpty(result.trim()) || !result
                .contains(objectToString(args[args.length - 1]))) {
            StringBuilder builder = new StringBuilder(msg);
            for (Object arg : args) {
                builder.append(" ").append(objectToString(arg));
            }
            result = builder.toString();
        }

        return result;
    }

    // 基本数据类型
    private final static String[] types = {"int", "java.lang.String", "boolean", "char",
            "float", "double", "long", "short", "byte"};

    public static <T> String objectToString(T object) {
        if (object == null) {
            return "<null>";
        }
        if (object.toString().startsWith(object.getClass().getName() + "@")) {
            StringBuilder builder = new StringBuilder(object.getClass().getSimpleName() + "{");
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                boolean flag = false;
                for (String type : types) {
                    if (field.getType().getName().equalsIgnoreCase(type)) {
                        flag = true;
                        Object value = null;
                        try {
                            value = field.get(object);
                        } catch (IllegalAccessException e) {
                            value = e;
                        } finally {
                            builder.append(String.format("%s=%s, ", field.getName(),
                                    value == null ? "null"
                                            : value.toString()));
                            break;
                        }
                    }
                }
                if (!flag) {
                    builder.append(String.format("%s=%s, ", field.getName(), "Object"));
                }
            }
            return builder.replace(builder.length() - 2, builder.length() - 1, "}").toString();
        } else {
            return object.toString();
        }
    }
}