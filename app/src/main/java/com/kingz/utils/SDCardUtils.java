package com.kingz.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

//SD卡相关的辅助类
public class SDCardUtils {
    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否已挂在
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getRootDir(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取SD卡路径
     * @return
     */
    public static String getDir(Context context, String testRootDir) {
        String dirPath;
        if(isSDCardAvailable()){
            dirPath =  Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            dirPath = context.getCacheDir().getPath();
        }
        File rootDir = new File(dirPath, testRootDir);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootDir.getPath();
    }

    /**
     * @return SD卡的剩余容量 单位byte
     */
    public static long getFreeSize() {
        if (isSDCardAvailable()) {
            StatFs stat = new StatFs(getRootDir());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        if (filePath.startsWith(getRootDir())) {
            filePath = getRootDir();
        } else {
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


}
