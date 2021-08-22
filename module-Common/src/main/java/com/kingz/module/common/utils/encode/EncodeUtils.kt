package com.kingz.module.common.utils.encode

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * author：ZekeWang
 * date：2021/8/16
 * description：加解密工具类
 */
object EncodeUtils {

    private val DIGITS_LOWER = charArrayOf(
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f'
    )
    private val threadHashMap: WeakHashMap<Thread, MessageDigestCtx> = WeakHashMap<Thread, MessageDigestCtx>()

    /**
     * 对字节数组进行md5加密
     * @return md5的字符串
     */
    fun calMD5(data: ByteArray?): String{
        val md5: MessageDigestCtx? = getMD5()
        return String(md5?.digest(data)?: CharArray(0))
    }

    private fun getMD5(): MessageDigestCtx? {
        synchronized(threadHashMap) {
            val thread = Thread.currentThread()
            val messageDigest = threadHashMap[thread]
                ?: return try {
                    val md5 = MessageDigest.getInstance("md5")
                    val digestCtx = MessageDigestCtx(md5)
                    threadHashMap[thread] = digestCtx
                    digestCtx
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                    null
                }
            messageDigest.reset()
            return messageDigest
        }
    }

    /**
     * 使用MessageDigest进行加密
     *
     * 使用方式：
     * //MD5
     * MessageDigest md5 = MessageDigest.getInstance("md5");
	 * MessageDigestCtx digestCtx = new MessageDigestCtx(md5);
     *
     * //SHA-1
     * MessageDigest md_sha = MessageDigest.getInstance("SHA-1");
     * MessageDigestCtx digestCtx = new MessageDigestCtx(md_sha);
     *
     */
    private class MessageDigestCtx(private var digest: MessageDigest) {
        private var digestStr = CharArray(32)
        fun reset() { digest.reset() }

        /**
         * 加密算法, 把密文转成16进制的字符数组形式
         */
        fun digest(data: ByteArray?): CharArray {
            val digestVal = digest.digest(data)
            for (i in 0..15) {
                val b: Int = digestVal[i].toInt() and 0xFF
                digestStr[i * 2] = DIGITS_LOWER[b / 16]
                digestStr[i * 2 + 1] = DIGITS_LOWER[b % 16]
            }
            return digestStr
        }
    }
}