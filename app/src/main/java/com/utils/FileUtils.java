package com.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by KingZ on 2015/11/4.
 * Discription:文件操作的工具类，提供保存图片，获取图片，判断图片是否存在，删除图片的一些方法
 */
public class FileUtils {

	private final static String TAG = "FileUtils";

    private FileUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 转换首字母为大写
     *
     * @param str
     * @return
     */
    public final static String conversionFirstLetter(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    }

    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    //Md5 加密法一
    public final synchronized static String MD5(String src) {
        try {
            byte[] btInput = src.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");  // 获得MD5摘要算法的 MessageDigest 对象
            mdInst.update(btInput);                                      // 使用指定的字节更新摘要
            byte[] md = mdInst.digest();                              // 获得密文
            return bytes2Hex(md);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Md5 加密法二
    private static void createMD5(String szSrc) throws NoSuchAlgorithmException {
        System.out.println("明文:" + szSrc);
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] md5 = md.digest(szSrc.getBytes());
        System.out.println("md5:" + byte2hex(md5));
    }

    //转换成十六进制字符串  法一
    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;

        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    //转换成十六进制字符串  法二
    public static String byte2hex(byte[] b) {
        String stmp = "";

        for (int n = 0; n < b.length; n++) {
            stmp += String.format("%02X", b[n]);
        }
        return stmp.toLowerCase();
    }


    /**
     * HexStr ————> Byte[]
     *
     * @param hexstr
     * @return
     */
    public static byte[] hexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }


    // 通过路径生成Base64文件
    public static String getBase64FromPath(String path) {
        String base64 = "";
        try {
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length() + 100];
            @SuppressWarnings("resource")
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    /**
     * 删除指定目录文件夹下面的文件，如果文件超过指定时间未修改
     * @param path
     * @param limitTime
     * @return
     */
    public static boolean dealPathFilesWithOldDate(String path,long limitTime){
        if(TextUtils.isEmpty(path)){
            return false;
        }
        File file = new File(path);
        File[] fl = file.listFiles();
        if(fl ==null){
            return true;
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
            return true;
        }
        ZLog.i(TAG, "deletePathFilesOldFile path:" + path + ", count:" + fl.length);
        for(File curFile : fl){
            if(curFile != null){
                curFile.delete();
            }
        }
        return true;
    }



}
