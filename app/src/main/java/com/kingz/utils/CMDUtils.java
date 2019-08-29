package com.kingz.utils;

import java.io.IOException;

/**
 * author：KingZ
 * date：2019/8/29
 * description：命令行工具类
 */
public class CMDUtils {
    public static final String TAG = CMDUtils.class.getSimpleName();

    public static int execCommand(String command) {
        ZLog.d(TAG, "exec:" + command);
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(command);
            exec.waitFor();
            int result = exec.exitValue();
            ZLog.d(TAG, "result = " + result);
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (exec != null) {
                try {
                    exec.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}
