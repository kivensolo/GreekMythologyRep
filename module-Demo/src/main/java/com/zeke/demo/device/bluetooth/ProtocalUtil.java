package com.zeke.demo.device.bluetooth;

import android.text.TextUtils;

import com.zeke.kangaroo.zlog.ZLog;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtocalUtil {

    private static final String TAG = ProtocalUtil.class.getSimpleName();

    public static Map<String, String> getData(List<byte[]> byteLists) {
        Map<String, String> data = new HashMap<>();
        int size = 0;
        for (byte[] bytes : byteLists) {
            size += bytes.length;
        }
        size -= byteLists.size();
        byte[] bytes = new byte[size];
        int i = 0;
        for (byte[] bTmp : byteLists) {
            System.arraycopy(bTmp, 1, bytes, i, bTmp.length - 1);
            i += (bTmp.length - 1);
        }
        ZLog.d("sync combine data:");
        ZLog.d(String.valueOf(bytes));

        i = 10;
        while (i < bytes.length) {
            int count = bytes[i + 1];
            if (bytes[i] == 0x12) {
                String strTime = getIntTime(bytes, i + 2, bytes[i + 1]);
                long time = Long.parseLong(strTime) * 1000;
                data.put("lastUpdateTimeMs", String.valueOf(time));
            } else if (bytes[i] == 0x22) {
                float weight = Float.parseFloat(getIntTime(bytes, i + 2, bytes[i + 1])) / 100;
                data.put("weight", String.valueOf(weight));
            } else if (bytes[i] == 0x1f) {
                data.put("impedanceOfLLRL", getIntTime(bytes, i + 2, bytes[i + 1]));
            }
            i += count + 3;
        }
        return data;
    }

    public static List<byte[]> getBindRequest(int deviceType, long time, String tag) {
        int length = tag.length() + 10;
        byte[] bytes = new byte[Math.min(length, 20)];
        bytes[0] = 0x00;
        bytes[1] = (byte) (length / 21 + 1); //bug
        bytes[2] = (byte) 0xaa;
        bytes[3] = (byte) 0x01;
        bytes[4] = 0x00;
        byte[] byteTime;
        //乐心血压是需要UTC时间的
        if (deviceType == 501952) {
            byteTime = getBytesTimeForSeconds(time);
        } else {
            byteTime = getBytesTimeForMillis(System.currentTimeMillis());
        }
        bytes[5] = byteTime[0];
        bytes[6] = byteTime[1];
        bytes[7] = byteTime[2];
        bytes[8] = byteTime[3];
        bytes[9] = (byte) tag.length();
        byte[] tagBytes = tag.getBytes();
        int j = 0;
        for (int i = 10; i < Math.min(length, 20); i++) {
            bytes[i] = tagBytes[j++];
        }

        List<byte[]> bytes1 = new ArrayList<>();
        bytes1.add(bytes);
        if (length > 20) {
            bytes = new byte[length - 19];
            bytes[0] = 0x01;
            for (int i = 1; i < bytes.length; i++) {
                bytes[i] = tagBytes[j++];
            }
            bytes1.add(bytes);
        }

        for (byte[] bytes2 : bytes1) {
            String res = "";
            for (byte b : bytes2) {
                res = res + Integer.toHexString(b) + ",";
            }
//            logger.info("bindDataz getBindRequest : " + res);
        }
        return bytes1;
    }

    public static List<byte[]> getSuperBindRequest(int deviceType, long time, String tag) {
        int length = tag.length() + 10;
        byte[] bytes = new byte[Math.min(length, 20)];
        bytes[0] = 0x00;
        bytes[1] = (byte) (length / 21 + 1); //bug
        bytes[2] = (byte) 0xaa;
        bytes[3] = (byte) 0x03;
        bytes[4] = 0x00;
        byte[] byteTime;
        //乐心血压是需要UTC时间的
        if (deviceType == 501952) {
            byteTime = getBytesTimeForSeconds(time);
        } else {
            byteTime = getBytesTimeForMillis(System.currentTimeMillis());
        }
        bytes[5] = byteTime[0];
        bytes[6] = byteTime[1];
        bytes[7] = byteTime[2];
        bytes[8] = byteTime[3];
        bytes[9] = (byte) tag.length();
        byte[] tagBytes = tag.getBytes();
        int j = 0;
        for (int i = 10; i < Math.min(length, 20); i++) {
            bytes[i] = tagBytes[j++];
        }

        List<byte[]> bytes1 = new ArrayList<>();
        bytes1.add(bytes);
        if (length > 20) {
            bytes = new byte[length - 19];
            bytes[0] = 0x01;
            for (int i = 1; i < bytes.length; i++) {
                bytes[i] = tagBytes[j++];
            }
            bytes1.add(bytes);
        }
        for (byte[] bytes2 : bytes1) {
            String res = "";
            for (byte b : bytes2) {
                res = res + Integer.toHexString(b) + ",";
            }
//            logger.info("bindDataz getSuperBindRequest : " + res);
        }
        return bytes1;
    }

    public static List<byte[]> getLoginRequest(int deviceType, long time, String userTag, String loginTag) {
        int length = loginTag.length() + 11 + userTag.length();
        byte[] bytes = new byte[length];
        bytes[0] = 0x00;
        bytes[1] = (byte) (length / 21 + 1); //bug
        bytes[2] = (byte) 0xaa;
        bytes[3] = (byte) 0x02;
        bytes[4] = 0x00;
        byte[] byteTime;
        //乐心血压是需要UTC时间的
        if (deviceType == 501952) {
            byteTime = getBytesTimeForSeconds(time);
        } else {
            byteTime = getBytesTimeForMillis(System.currentTimeMillis());
        }
        bytes[5] = byteTime[0];
        bytes[6] = byteTime[1];
        bytes[7] = byteTime[2];
        bytes[8] = byteTime[3];

        //usertag
        int curIndex = 9;
        bytes[curIndex++] = (byte) userTag.length();
        byte[] userTagBytes = userTag.getBytes();
        System.arraycopy(userTagBytes, 0, bytes, curIndex, userTagBytes.length);
        curIndex += userTagBytes.length;

        //loginTag
        bytes[curIndex++] = (byte) loginTag.length();
        byte[] loginTagBytes = loginTag.getBytes();
        System.arraycopy(loginTagBytes, 0, bytes, curIndex, loginTagBytes.length);

        List<byte[]> bytes1 = new ArrayList<>();
        int maxPageCount = 20;
        for (int i = 0; i < bytes[1]; i++) {
            if (i == 0) {
                byte[] tmpBytes = new byte[Math.min(bytes.length - i * maxPageCount, maxPageCount)];
                System.arraycopy(bytes, 0, tmpBytes, 0, tmpBytes.length);
                bytes1.add(tmpBytes);
            } else {
                byte[] tmpBytes = new byte[Math.min(bytes.length - maxPageCount - (i - 1) * (maxPageCount - 1) + 1, maxPageCount)];
                tmpBytes[0] = (byte) i;
                System.arraycopy(bytes, maxPageCount + (i - 1) * (maxPageCount - 1), tmpBytes, 1, tmpBytes.length - 1);
                bytes1.add(tmpBytes);
            }
        }

        for (byte[] bytes2 : bytes1) {
            String res = "";
            for (byte b : bytes2) {
                res = res + Integer.toHexString(b) + ",";
            }
//            logger.info("bindDataz getLoginRequest : " + res);
        }
        return bytes1;
    }


    public static byte[] getTimeStampRequest(long time) {
        byte[] bytes = new byte[20];
        bytes[0] = 0x00;
        bytes[1] = 0x02;
        bytes[2] = (byte) 0xc1;
        bytes[3] = 0x02;
        bytes[4] = 0x00;

        byte[] byteTime = getBytesTimeForSeconds(time);
        bytes[5] = byteTime[0];
        bytes[6] = byteTime[1];
        bytes[7] = byteTime[2];
        bytes[8] = byteTime[3];

        bytes[9] = 0x00;
        bytes[10] = 0x12;
        bytes[11] = 0x04;
        bytes[12] = byteTime[0];
        bytes[13] = byteTime[1];
        bytes[14] = byteTime[2];
        bytes[15] = byteTime[3];
        bytes[16] = 0x00;
        bytes[17] = 0x15;
        bytes[18] = 0x01;
        bytes[19] = 0x08;
        return bytes;
    }

    public static byte[] getSyncRequest(long time) {
        int length = 11;
        byte[] bytes = new byte[length];
        bytes[0] = 0x00;
        bytes[1] = (byte) (0x01); //bug
        bytes[2] = (byte) 0xb3;
        bytes[3] = (byte) 0x01;
        bytes[4] = 0x00;
        byte[] byteTime = getBytesTimeForSeconds(time);
        bytes[5] = byteTime[0];
        bytes[6] = byteTime[1];
        bytes[7] = byteTime[2];
        bytes[8] = byteTime[3];
        bytes[9] = 0x00;
        bytes[10] = 0x01;

        return bytes;
    }

    public static byte[] getBytesTimeForSeconds(long time) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        byteBuffer.putLong(time / 1000);
        byte[] result = new byte[4];
        System.arraycopy(byteBuffer.array(), 4, result, 0, 4);
        return result;
    }

    public static byte[] getBytesTimeForMillis(long time) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        byteBuffer.putLong(time);
        byte[] result = new byte[4];
        System.arraycopy(byteBuffer.array(), 4, result, 0, 4);
        return result;
    }

    public static String getIntTime(byte[] byteSource, int index, int size) {
        byte[] bytes = new byte[size];
        System.arraycopy(byteSource, index, bytes, 0, size);
        return new BigInteger(1, bytes).toString(10);
    }

    public static String changeMAC(String mac) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length(); i++) {
            if (i % 2 == 0 && i != 0) {
                sb.append(':');
            }
            sb.append(mac.charAt(i));
        }

        return sb.toString();
    }


    public static String deviceIdToMAC(String deviceId) {
        if (TextUtils.isEmpty(deviceId)) {
            return null;
        }
        String[] values = deviceId.split("-");
        if (values.length != 3) {
            return null;
        }
        return changeMAC(values[2]);
    }
}
