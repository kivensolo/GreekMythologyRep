package com.kingz.module.common.utils

object HexUtil {

    private val DIGITS_LOWER = charArrayOf(
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f'
    )

    private val DIGITS_UPPER = charArrayOf(
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'A', 'B',
        'C', 'D', 'E', 'F'
    )

    /**
     * Encode byteArray to CharArray.
     * @param data: bytes array
     */
    fun encodeHex(data: ByteArray?, toLowerCase: Boolean = true): CharArray? {
        return encodeHex(data, if (toLowerCase) DIGITS_LOWER else DIGITS_UPPER)
    }

    private fun encodeHex(data: ByteArray?, toDigits: CharArray): CharArray? {
        if (data == null) return null
        val l = data.size
        val out = CharArray(l shl 1)
        var i = 0
        var j = 0
        while (i < l) {
            out[j++] = toDigits[0xF0.and(data[i].toInt()) ushr 4]
            out[j++] = toDigits[0x0F.and(data[i].toInt())]
            i++
        }
        return out
    }

    /**
     * Hex bytes array to String
     * @param bytes byte data array
     * @param addSpace Enable Space
     * @return Bytes value in String.
     */
    fun formatHexString(bytes: ByteArray?, addSpace: Boolean = false): String {
        if (bytes == null || bytes.isEmpty()) return "null"
        val sb = StringBuilder()
        for (i in bytes.indices) {
            var hex = Integer.toHexString(bytes[i].toInt() and 0xFF)
            if (hex.length == 1) {
                hex = "0$hex"
            }
            sb.append(hex)
            if (addSpace) sb.append(" ")
        }
        return sb.toString().trim { it <= ' ' }
    }

    /**
     * Hex string to bytes array.
     * @param hexString String of hex
     * @return Bytes value in String.
     */
    fun hexStringToBytes(string: String?): ByteArray? {
        var hexString = string
        if (hexString == null || hexString == "") {
            return null
        }
        hexString = hexString.trim { it <= ' ' }
        hexString = hexString.toUpperCase()
        val length = hexString.length / 2
        val hexChars = hexString.toCharArray()
        val array = ByteArray(length)
        for (i in 0 until length) {
            val pos = i * 2
            val hight = hexChars[pos].toInt() shl 4
            val low = hexChars[pos + 1].toInt()
            array[i] = (hight or low).toByte()
        }
        return array
    }



    /**
     * Conver hex char to Byte
     * @param c : Unicode(2byte) hex char
     * @return Byte of given hex char.
     */
    fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }

    /**
     * Hex to Int
     * @param byte byte value
     */
    fun byteToInt(byte: Byte): Int {
        return byte.toInt() and 0xFF
    }

    fun toDigit(ch: Char, index: Int): Int {
        val digit = Character.digit(ch, 16)
        if (digit == -1) {
            throw RuntimeException( "Illegal hexadecimal character $ch at index $index" )
        }
        return digit
    }
}