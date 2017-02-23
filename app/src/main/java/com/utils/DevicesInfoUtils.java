package com.utils;

import android.os.Debug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/4/12 15:36 <br>
 * description:  <br>
 */
public class DevicesInfoUtils {

    private static final String TAG = "DevicesInfoUtils";

    private static final String MEMINFO_PATH = "/proc/meminfo";
    private static final String CPUINFO_PATH = "/proc/cpuinfo";

    private DevicesInfoUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得空闲内存X_M
     * @return 空闲内存X_M
     */
    public static float getFreeMemoryM() {
        int free = (int) (getDeviceFreeMemoryBy_Runtime() >> 10);
        int m = free >> 10;
        float k = free % 1024 / 1000f;
        return m + k;
    }

    public static float getNativeUseredMemoryM() {
        return getMemoryM(Debug.getNativeHeapAllocatedSize());
    }

    public static float getMemoryM(long num) {
        num >>= 10;
        int m = (int) (num >> 10);
        float k = num % 1024 / 1000f;
        return m + k;
    }

    public static long getDeviceFreeMemoryBy_Runtime() {
        long l1 = Runtime.getRuntime().maxMemory();
        long l2 = Debug.getNativeHeapAllocatedSize();
        ZLog.d(TAG, "getDeviceFreeMemoryBy_Runtime() maxMemory=" + l1);
        return l1 - l2;
    }

    /**
     * 获取物理及虚拟机的内存大小
     * root@n200:/proc # cat meminfo
     * MemTotal:         781288 kB  所有可用RAM大小（即物理内存减去一些预留位和内核的二进制代码大小）
     * MemFree:           45488 kB  被系统留着未使用的内存
     * Buffers:            5108 kB  用来给文件做缓冲的大小
     * Cached:            88824 kB  被高速缓冲存储器（cache memory）用的内存的大小
     * SwapCached:        58388 kB  swap 缓存的大小，Android很少使用swap的，经常为0。
     *                                被高速缓冲存储器（cache memory）用来交换空间的大小,
     *                                用来在需要的时候很快的被替换而不需要再次打开I/O端口。
     * Active:           209912 kB  在活跃使用中的缓冲或高速缓冲存储器页面文件的大小，除非非常必要，否则不会被移作他用
     * Inactive:         281388 kB  在不经常使用中的缓冲或高速缓冲存储器页面文件的大小，可能被用于其他途径
     * Active(anon):     177088 kB
     * Inactive(anon):   226088 kB
     * Active(file):      32824 kB
     * Inactive(file):    55300 kB
     * Unevictable:        2092 kB
     * Mlocked:               0 kB
     * HighTotal:        108544 kB
     * HighFree:           1832 kB
     * LowTotal:         672744 kB
     * LowFree:           43656 kB
     * SwapTotal:        511996 kB  交换空间的总大小
     * SwapFree:         381312 kB  未被使用交换空间的大小
     * Dirty:                 8 kB  等待被写回到磁盘的内存大小。
     * Writeback:             0 kB  正在被写回到磁盘的内存大小
     * AnonPages:        369632 kB  未映射页的内存大小
     * Mapped:            36584 kB  设备和文件等映射的大小
     * Shmem:              3716 kB
     * Slab:              22068 kB  内核数据结构缓存的大小，可以减少申请和释放内存带来的消耗
     * SReclaimable:       6400 kB  可收回Slab的大小
     * SUnreclaim:        15668 kB  不可收回Slab的大小（SUnreclaim+SReclaimable＝Slab）
     * KernelStack:        7360 kB
     * PageTables:        10136 kB  管理内存分页页面的索引表的大小
     * NFS_Unstable:          0 kB  不稳定页表的大小
     * Bounce:                0 kB
     * WritebackTmp:          0 kB
     * CommitLimit:      902640 kB
     * Committed_AS:   25442448 kB
     * VmallocTotal:     245760 kB
     * VmallocUsed:       89632 kB
     * VmallocChunk:      74756 kB
     */
    public static long getDeviceFreeMemoryBy_Proc() {
        String freeMemStr;
        String[] arrayOfString;
        long freeMem = 0;
        try {
            FileReader localFileReader = new FileReader(MEMINFO_PATH);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            String totalMemStr = localBufferedReader.readLine();
            freeMemStr = localBufferedReader.readLine();
            arrayOfString = freeMemStr.split("\\s+");
            freeMem = Integer.valueOf(arrayOfString[1]);//单位  KB
            localBufferedReader.close();
            ZLog.i("==== Total Memory ====>" + totalMemStr);
            ZLog.i("==== Free Memory ====>" + freeMemStr);
        } catch (IOException ignored) {
        }
        return freeMem;
    }

    /**
     * 判断是否为4.0以上版本
     *
     * @return
     */
    public static boolean isVersion4() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        if (sdkVersion >= 15) {
            return true;
        } else
            return false;
    }


}
