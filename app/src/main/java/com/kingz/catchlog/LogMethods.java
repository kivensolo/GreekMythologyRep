package com.kingz.catchlog;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: King.Z <br>
 * date:  2018/1/21 14:30 <br>
 * description: XXXXXXX <br>
 */
public class LogMethods {

    String logcat_v_time = "logcat -v time";

    public static void killOldAdbPs() {
        try {
            Pattern logcatPattern = Pattern.compile("^(.+?)\\s+(\\d+)\\s+1\\s+.+[A-Z]\\s+logcat$");
            Process ps = Runtime.getRuntime().exec("ps");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            while (true) {
                String s = reader.readLine();
                if (s == null) {
                    break;
                }
                Matcher matcher = logcatPattern.matcher(s);
                if (matcher.matches()) {
                    String pidStr = matcher.group(2);
                    int pid = tryParseInt(pidStr, -1);
                    try {
                        if (pid != -1) {
                            android.os.Process.killProcess(pid);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ps.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InputStream readSystemLogsStream() {
        try {
            Process process = Runtime.getRuntime().exec(logcat_v_time);
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeLog2SdCardFile(InputStream inputStream,String fileName) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        try {
            File dir = null;
            if (sdCardExist) {
                dir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + fileName + ".txt");
                if (!dir.exists()) {
                    dir.createNewFile();
                }
            }
            byte[] buffer = new byte[20 * 1024];
            try {
                if (dir == null) {
                    Log.e("LogCatcherReceiver", "file==null");
                    return;
                }
                try (FileOutputStream fos = new FileOutputStream(dir)) {
                    int read;
                    while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        fos.write(buffer, 0, read);
                    }
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static int tryParseInt(String val, int defVal) {
        if (TextUtils.isEmpty(val)) {
            return defVal;
        }
        try {
            return Integer.parseInt(val);
        } catch (Exception ignored) {
        }
        return defVal;
    }

}
