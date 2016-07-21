package com.utils;

import android.os.Debug;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/4/12 15:36 <br>
 * description:  <br>
 */
public class DevicesInfoUtils {

	private DevicesInfoUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static long getFreeMemory() {
		long l1 = Runtime.getRuntime().maxMemory();
		long l2 = Debug.getNativeHeapAllocatedSize();
		return l1 - l2;
	}

	/**
	 * 获得剩余内存X_M
	 * @return
     */
	public static float getFreeMemoryM() {
		int free = (int) (getFreeMemory() >> 10);
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


    /**
	 * 判断是否为4.0以上版本
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
