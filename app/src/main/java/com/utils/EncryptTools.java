package com.utils;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

public class EncryptTools {

	static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	static final String AES_TRANSFORMATION = "AES/ECB/NoPadding"; //"AES/CBC/NoPadding";

	private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    // 32 bytes from sha-256 -> 64 hex chars.
    private static final char[] SHA_256_CHARS = new char[64];
    // 20 bytes from sha-1 -> 40 chars.
    private static final char[] SHA_1_CHARS = new char[40];

	private static final String TAG = "EncryptLogic";

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

	public static byte[] sha1WithRsa(byte[] data, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
		}
		return null;
	}

	public static PublicKey getPublicKey(String publicKeyString) {
		byte[] keyBytes;
		try {
			keyBytes = Base64.decode(publicKeyString, Base64.DEFAULT);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
		}
		return null;
	}

	public static PrivateKey getPrivateKey(String privateKeyString) {
		byte[] keyBytes;
		try {
			keyBytes = Base64.decode(privateKeyString, Base64.DEFAULT);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			return keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getRandomString(int length) { //length表示生成字符串的长度
		String base = "abcdef0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static int encryptByAes(byte[] dataBytes, String pass, String init, OutputStream outputStream) throws Exception {
		SecretKeySpec keyspec = new SecretKeySpec(hexString2Bytes(pass), "AES");
		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, keyspec);
		int blockSize = cipher.getBlockSize();
		int dataLength = 0;

		for (int i = 0; i < dataBytes.length; i += blockSize) {
			byte[] data;
			if (i + blockSize >= dataBytes.length) {
				byte[] padData = new byte[blockSize];
				Arrays.fill(padData, (byte) 0);

				for (int offset = i; offset < dataBytes.length; ++offset) {
					padData[offset - i] = dataBytes[offset];
				}
				data = cipher.doFinal(padData);
			} else {
				data = cipher.update(dataBytes, i, blockSize);
			}

			outputStream.write(data);
			dataLength += data.length;
		}
		return dataLength;
	}


	public static int decryptByAes(InputStream dataStream, String pass, String init, OutputStream outputStream) throws Exception {
		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		SecretKeySpec keyspec = new SecretKeySpec(hexString2Bytes(pass), "AES");
		cipher.init(Cipher.DECRYPT_MODE, keyspec);

		final int blockSize = cipher.getBlockSize();
		final byte[] buf = new byte[blockSize];
		int dataLength = 0;
		while (true) {
			int len = 0;
			while (len != blockSize) {
				int readBytes = dataStream.read(buf, len, buf.length - len);
				if (readBytes <= 0) {
					break;
				}
				len += readBytes;
			}


			if (len <= 0) {
				final byte[] buffer = cipher.doFinal();
				if (buffer != null) {
					outputStream.write(buffer);
					dataLength += buffer.length;
				}
				break;
			} else {
				final byte[] buffer = cipher.update(buf);
				outputStream.write(buffer);
				dataLength += buffer.length;
			}
		}
		return dataLength;
	}

	public static byte[] encryptByRsa(String data, PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encryptData = cipher.doFinal(data.getBytes());
			return encryptData;
		} catch (Exception e) {
			return new byte[0];
		}
	}

	public static String decryptByRsa(byte[] data, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			String ret = new String(cipher.doFinal(data));
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	/**
     * Returns the hex string of the given byte array representing a SHA256 hash.
     */
    public static String sha256BytesToHex(byte[] bytes) {
        return bytesToHex(bytes, SHA_256_CHARS);
    }

    /**
     * Returns the hex string of the given byte array representing a SHA1 hash.
     */
    public static String sha1BytesToHex(byte[] bytes) {
        return bytesToHex(bytes, SHA_1_CHARS);
    }

    // Taken from:
    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java/9655275#9655275
    private static String bytesToHex(byte[] bytes, char[] hexChars) {
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHAR_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHAR_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
