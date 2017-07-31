package com.utils;

import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * author: King.Z <br>
 * date:  2017/7/31 16:35 <br>
 * description: 常用加密工具类 <br>
 */
public class EncryptUtils {

    public static final String TRANSFORMATION_DES = "DESede";

    /**
     * DES加密
     * @param message  加密信息
     * @param key      加密密钥
     * @return         加密后的返回数据
     */
    private static String encrypt_DES(String message, String key){
		try {
			Log.i("EncryptUtils","encrypt_DES message:" + message + ", key:" + key);
			byte[] arrayOfByte1 = HexString2Bytes(message);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION_DES);

			byte[] key1 = new byte[24];
			byte[] temp = key.getBytes("UTF-8");

			if (key1.length > temp.length) {
				System.arraycopy(temp, 0, key1, 0, temp.length);
			} else {
				System.arraycopy(temp, 0, key1, 0, key1.length);
			}

			SecretKeySpec desKeySpec = new SecretKeySpec(key1, TRANSFORMATION_DES);
			cipher.init(Cipher.DECRYPT_MODE, desKeySpec);
			byte[] result = cipher.doFinal(arrayOfByte1);
			return new String(result);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e) {
			e.printStackTrace();
		}
        return null;
	}


	private static byte[] HexString2Bytes(String paramString) {
		byte[] arrayOfByte1 = new byte[paramString.length() / 2];
		byte[] arrayOfByte2 = paramString.getBytes();
		for (int i = 0;; i++) {
			if (i >= arrayOfByte2.length / 2){
				return arrayOfByte1;
            }
			arrayOfByte1[i] = uniteBytes(arrayOfByte2[(i * 2)],arrayOfByte2[(1 + i * 2)]);
		}
	}

		private static byte uniteBytes(byte paramByte1, byte paramByte2) {
		return (byte) ((byte) (Byte.decode(
				"0x" + new String(new byte[] { paramByte1 })).byteValue() << 4) ^ Byte
				.decode("0x" + new String(new byte[] { paramByte2 }))
				.byteValue());
	}
}
