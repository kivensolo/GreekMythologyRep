package com.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/3/3 14:48
 * description:
 */
public class NetTools {
	private static final String TAG = "NetTools";

	/**
	 * byte数组转为Mac
	 * @param mac
	 * @return
     */
	private static String MacString(byte[] mac) {
		StringBuilder sb = new StringBuilder();
		for (byte v : mac) {
			if (sb.length() > 0) {
				sb.append("-");
			}
			sb.append(String.format("%02X", v));
		}
		return sb.toString();
	}

	/**
	 * 获取第一个符合name_pattern的网卡的MAC地址 需要API Level 19, <uses-permission
	 * android:name="android.permission.INTERNET"/>
	 */
	private static String getMacLevel9(String name_pattern_rgx) {
		try {
			Method getHardwareAddress = NetworkInterface.class.getMethod("getHardwareAddress");
			Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			while (nis.hasMoreElements()) {
				NetworkInterface n = nis.nextElement();
				getHardwareAddress.invoke(n);
				byte[] hw_addr = (byte[]) getHardwareAddress.invoke(n);
				if (hw_addr != null) {
					String name = n.getName().toLowerCase(Locale.CHINA);
					String mac = MacString(hw_addr);
					Log.d("NetTools.getMacLevel9", name + " " + mac);
					if (name.matches(name_pattern_rgx)) {
						return mac;
					}
				}
			}
		} catch (SocketException e) {
			Log.d("NetTools.getMacLevel9", "Exception: SocketException.");
			e.printStackTrace();
		} catch (SecurityException e) {
			Log.d("NetTools.getMacLevel9", "Exception: SecurityException.");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			Log.d("NetTools.getMacLevel9", "Exception: NoSuchMethodException.");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.d("NetTools.getMacLevel9", "Exception: IllegalArgumentException.");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.d("NetTools.getMacLevel9", "Exception: IllegalAccessException.");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			Log.d("NetTools.getMacLevel9", "Exception: InvocationTargetException.");
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取第一个符合name_pattern的网卡的MAC地址 需要netcfg工具, <uses-permission
	 * android:name="android.permission.INTERNET"/>
	 */
	private static String getMacNetcfg(String name_pattern_rgx) {
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("netcfg");
			BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			proc.waitFor();
			Pattern result_pattern = Pattern.compile("^([a-z0-9]+)\\s+(UP|DOWN)\\s+([0-9./]+)\\s+.+\\s+([0-9a-f:]+)$", Pattern.CASE_INSENSITIVE);
			while (is.ready()) {
				String info = is.readLine();
				Matcher m = result_pattern.matcher(info);
				if (m.matches()) {
					String name = m.group(1).toLowerCase(Locale.CHINA);
					String status = m.group(2);
					String addr = m.group(3);
					String mac = m.group(4).toUpperCase(Locale.CHINA).replace(':', '-');
					Log.d("NetTools.getMacNetcfg", "match success:" + name + " " + status + " " + addr + " " + mac);
					if (name.matches(name_pattern_rgx)) {
						return mac;
					}
				}
			}
		} catch (IOException e) {
			Log.d("NetTools.getMacNetcfg", "Exception: IOException.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			Log.d("NetTools.getMacNetcfg", "Exception: InterruptedException.");
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 读取设备的有线网络的地址
	 *
	 * @return AA-BB-CC-DD-EE-FF 格式 如果不存在，返回""
	 */
	public static String getLANMac() {
		String mac = getMacLevel9("eth[0-9]+");
		if (mac.equals("")) {
			mac = getMacNetcfg("eth[0-9]+");
		}
		// MAC地址总是使用大写的。
		if (!TextUtils.isEmpty(mac)) {
			mac = mac.toUpperCase(Locale.CHINA);
		}
		if (mac.length() != 17) {
			Log.e(TAG, "getLANMac mac:" + mac);
		} else {
			Log.i(TAG, "getLANMac mac:" + mac);
		}
		return mac;
	}

    /**
     * 获取Wifi的mac
     * @return
     */
	public static String getWifiMac() {
		String mac = getMacLevel9("wlan[0-9]+");
		if (mac.equals("")) {
			mac = getMacNetcfg("wlan[0-9]+");
		}
		if (!TextUtils.isEmpty(mac)) {
			mac = mac.toUpperCase(Locale.CHINA);
		}
		if (mac.length() != 17) {
			Log.e(TAG, "getWLANMac mac:" + mac);
		} else {
			Log.i(TAG, "getWLANMac mac:" + mac);
		}
		return mac;
	}

	/**
	 * 获取连接的Wifi名称
	 * @param context
	 * @return
     */
	public static String getWifiName(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getSSID();
	}

	/**
	 * 读取设备的默认网关IP地址
	 * @return "000.000.000.000" 格式 如果不存在，返回""
	 */
	public static String getGatewayIp() {
		String ip = "";
		String info = "";
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("ip route");
			BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			proc.waitFor();
			Pattern result_pattern = Pattern.compile("default\\s+via\\s+(\\d+(?:\\.\\d+){3})\\s+dev.+", Pattern.CASE_INSENSITIVE);
			while (is.ready()) {
				info = is.readLine();
				Log.i(TAG, "getGatewayIp() ip route:" + info);

				Matcher m = result_pattern.matcher(info);
				if (m.matches()) {
					ip = m.group(1).toLowerCase(Locale.CHINA);
					Log.d(TAG, "getGatewayIp() m.group(1). ip:" + ip);
					return ip;
				}
			}
		} catch (IOException e) {
			Log.d(TAG, "getGatewayIp() Exception: IOException.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			Log.d(TAG, "getGatewayIp() Exception: InterruptedException.");
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 读取网口工作状态
	 * @return true: UP false:DOWN
	 */
	public static boolean getPortIsWork() {
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("netcfg");
			BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			proc.waitFor();
			Pattern result_pattern = Pattern.compile("^([a-z0-9]+)\\s+(UP|DOWN)\\s+([0-9./]+)\\s+.+\\s+([0-9a-f:]+)$", Pattern.CASE_INSENSITIVE);
			while (is.ready()) {
				String info = is.readLine();
				Matcher m = result_pattern.matcher(info);
				if (m.matches()) {
					String name = m.group(1).toLowerCase(Locale.CHINA);
					String status = m.group(2);
					String addr = m.group(3);
					String mac = m.group(4).toUpperCase(Locale.CHINA).replace(':', '-');
					Log.i(TAG, "getPortIsWork(), match success:" + name + " " + status + " " + addr + " " + mac);
					if (name.matches("eth[0-9]+") && status.matches("UP") && (!addr.matches("0.0.0.0")))

					{
						return true;
					}
					if (name.matches("wlan[0-9]+") && status.matches("UP") && (!addr.matches("0.0.0.0")))
					{
						return true;
					}
				}
			}
		} catch (IOException e) {
			Log.i(TAG, "getPortIsWork(), Exception: IOException.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			Log.i(TAG, "getPortIsWork(), Exception: InterruptedException.");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 网络连接状态判断
	 * @param context
	 * @return
     */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取无线网IP
	 * @param context
	 * @return
     */
	public static String getWlanIp(Context context) {
		WifiManager wifimanage = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(wifimanage.isWifiEnabled()){
			wifimanage.setWifiEnabled(true);
		}
		WifiInfo wifiinfo = wifimanage.getConnectionInfo();
		return intToIp(wifiinfo.getIpAddress());
	}

    /**
     * 获取本地Ip地址
     * @return
     */
    public static String getLocalIpAddress()
	{
		String ipaddress = "127.0.0.1";
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						ipaddress = inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex)
		{
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return ipaddress;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
	}

}
