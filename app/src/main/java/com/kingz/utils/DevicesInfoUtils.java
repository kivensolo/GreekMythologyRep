package com.kingz.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import com.App;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

/**
 * author: King.Z <br>
 * date:  2016/4/12 15:36 <br>
 * description:  <br>
 */
public class DevicesInfoUtils {

    private static final String TAG = DevicesInfoUtils.class.getSimpleName();

    private static final String MEMINFO_PATH = "/proc/meminfo";
    private static final String CPUINFO_PATH = "/proc/cpuinfo";

    /**
     * Don't let anyone instantiate this class.
     */
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
     * @return Api level >= 15
     */
    public static boolean isVersion4() {
        int sdkVersion;
        try {
            sdkVersion = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion >= 15;
    }

    /**
     * 得到CPU核心数
     * @return CPU核心数
     */
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            });
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

     /**
     * whether this process is named with processName
     *
     * @param context     上下文
     * @param processName 进程名
     * @return
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     * <li>if one process of
     * {@link ActivityManager#getRunningAppProcesses()} is equal to
     * processName, return true,
     * otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null || TextUtils.isEmpty(processName)) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = manager
                .getRunningAppProcesses();
        if (processInfoList == null) {
            return true;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : manager
                .getRunningAppProcesses()) {
            if (processInfo.pid == pid
                    && processName.equalsIgnoreCase(processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取手机当前的Runtime
     *
     * @return 正常情况下可能取值Dalvik, ART, ART debug build;
     */
    public static String getCurrentRuntimeValue() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get",String.class, String.class);
                if (get == null) {
                    return "WTF?!";
                }
                try {
                    final String value = (String) get.invoke(
                            systemProperties,
                            "persist.sys.dalvik.vm.lib",
                            /* Assuming default is */"Dalvik");
                    if ("libdvm.so".equals(value)) {
                        return "Dalvik";
                    } else if ("libart.so".equals(value)) {
                        return "ART";
                    } else if ("libartd.so".equals(value)) {
                        return "ART debug build";
                    }
                    return value;
                } catch (IllegalAccessException e) {
                    return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "SystemProperties class is not found";
        }
    }


    /**
     * 获取设备的可用内存大小
     *
     * @param context 应用上下文对象context
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return (int) (mi.availMem / (1024 * 1024));        // 返回当前系统的可用内存
    }

    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static StatFs getSdCardStatFs(){
        File directory = Environment.getExternalStorageDirectory();
        return new  StatFs(directory.getAbsolutePath());
    }

    public static long getSdCardBlockSize(){
        return getSdCardStatFs().getBlockSizeLong();
    }

    public static long getSdCardBlockCount(){
        return getSdCardStatFs().getBlockCountLong();
    }

    public static long getSdCardAvailableBlocks(){
        return getSdCardStatFs().getAvailableBlocksLong();
    }

    public static String getExtStorageCapacity(){
        StatFs sf = getSdCardStatFs();
        long  blockSize = sf.getBlockSizeLong();
        long  blockCount = sf.getBlockCountLong();
        return Formatter.formatFileSize(App.getAppContext(), blockCount * blockSize);
    }

    public static String getExtStorageAvailCapacity(){
        StatFs sf = getSdCardStatFs();
        long  blockSize = sf.getBlockSizeLong();
        long  availCount = sf.getAvailableBlocks();
        return Formatter.formatFileSize(App.getAppContext(), availCount * blockSize);
    }

}
