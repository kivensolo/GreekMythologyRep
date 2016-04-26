package com.utils;

import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * Created by KingZ on 2015/11/4.
 * Discription:文件操作的工具类，提供保存图片，获取图片，判断图片是否存在，删除图片的一些方法
 */
public class FileUtils {

    /**
	 * 转换首字母为大写
	 * @param str
	 * @return
	 */
	public final static String conversionFirstLetter(String str) {
		if (!TextUtils.isEmpty(str)) {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		return str;
	}

    	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public final synchronized static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();

			return bytes2Hex(md);

			// 把密文转换成十六进制的字符串形式
//			int j = md.length;
//			char str[] = new char[j * 2];
//			int k = 0;
//			for (int i = 0; i < j; i++) {
//				byte byte0 = md[i];
//				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//				str[k++] = hexDigits[byte0 & 0xf];
//			}
//			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

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


}
