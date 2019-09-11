package com.kingz.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by KingZ on 2015/11/4.
 * Discription:文件操作的工具类
 */
public class FileUtils {

	private final static String TAG = "FileUtils";

    private FileUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final String[][] MIME_MAP = {
            // {后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    /**
     * 删除指定目录文件夹下面的文件，如果文件超过指定时间未修改
     * @param path  目录路径
     * @param limitTime 限制时间
     */
    public static boolean dealPathFilesWithOldDate(String path,long limitTime){
        if(TextUtils.isEmpty(path)){
            return false;
        }
        File file = new File(path);
        File[] fl = file.listFiles();
        if(fl ==null){
            return false;
        }
        ZLog.i(TAG, "deletePathFilesOldFile path:" + path + ", count:" + fl.length);
        for(File curFile : fl){
            if(curFile != null && (curFile.lastModified() < limitTime)){
                curFile.delete();
            }
        }
        return true;
    }

    /**
     * 删除指定文件夹下的全部文件，如果文件超过某个数量
     * @param path
     * @param count
     * @return
     */
    public static boolean dealPathFilesOverCount(String path,long count){
        if(TextUtils.isEmpty(path)){
            return false;
        }
        File file = new File(path);
        File[] fl = file.listFiles();
        if(fl ==null){
            return true;
        }
        if(fl.length < count){
            return false;
        }
        ZLog.i(TAG, "deletePathFilesOldFile path:" + path + ", count:" + fl.length);
        for(File curFile : fl){
            if(curFile != null){
                curFile.delete();
            }
        }
        return true;
    }

    /**
     * 储存bitMap的指定格式至指定目录
     * @param filePath 储存路径
     * @param bitmap   储存的bitmap
     * @param format   储存的格式
     * @param quality  储存的质量
     */
    public static void saveBitmapWithPath(File filePath, Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            if (filePath.canWrite()) {
                bitmap.compress(format, quality, out);
                out.flush();
                out.close();
            } else {
                throw new IOException("The filePath is can not Write!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 储存Object至指定目录
     */
    public static void saveObjectWithPath(Object obj, File filePath) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            if (filePath.canWrite()) {
                out.writeObject(obj);
                out.close();
            } else {
                throw new IOException("The filePath is can not Write!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取指定目录的Object
     */
    public static Object readObjectWithPath(File filePath) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath));
            if (filePath.canWrite()) {
                Object obj = inputStream.readObject();
                inputStream.close();
                return obj;
            } else {
                throw new IOException("The filePath is can not Write!");
            }
        } catch (IOException|ClassNotFoundException  e) {
            Log.e(TAG,"File not found :" + filePath.getAbsolutePath());
            return null;
        }
    }

    /**
     * 获取文件MIME类型
     * @param file 目标文件
     * @return     MIME类型值
     */
    public static String getMIMEType(File file) {
        String type = "";
        String fileName = file.getName();
        int dotIndex = fileName.indexOf('.');
        if (dotIndex < 0) {
            return type;
        }
        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
        if (TextUtils.equals("", end)) {
            return type;
        }
        for (String[] aMIME_MapTable : MIME_MAP) {
            if (TextUtils.equals(aMIME_MapTable[0], end)) {
                type = aMIME_MapTable[1];
            }
        }
        return type;
    }

}
