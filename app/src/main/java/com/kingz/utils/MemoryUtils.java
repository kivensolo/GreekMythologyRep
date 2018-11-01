package com.kingz.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * author: King.Z <br>
 * date:  2018/1/22 15:12 <br>
 * description: 设备内存工具类 <br>
 */
public class MemoryUtils {
    public  static  long getCpuFreeMemory() {
        String file = "/proc/meminfo";// 系统内存信息文件
        String freeMemStr;
        String[] arrayOfString;
        long freeMem = 0;
        try {
            FileReader localFileReader = new FileReader(file);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            String totalMemStr = localBufferedReader.readLine();
            freeMemStr = localBufferedReader.readLine();
            arrayOfString = freeMemStr.split("\\s+");
            freeMem = Integer.valueOf(arrayOfString[1]);
            localBufferedReader.close();
            ZLog.i("[MEM_INFO] 总共内存====" + totalMemStr);
            ZLog.i("[MEM_INFO] 剩余内存====" + freeMemStr);
        } catch (IOException ignored) {
        }
        return freeMem;
    }
}
