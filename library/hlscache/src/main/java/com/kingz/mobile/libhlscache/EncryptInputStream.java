package com.kingz.mobile.libhlscache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 间隔取反加密 InputStream，加密解密同一逻辑。
 */
public class EncryptInputStream extends FilterInputStream {
    private static final int INVERSE_INTERVAL = 4096;
    private int count = 0;

    public EncryptInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public int read() throws IOException {
        int data = super.read();
        count++;
        if (data % INVERSE_INTERVAL == 0) {
            data = ~data;
        }
        return data;
    }

    public int read(byte[] var1) throws IOException {
        return read(var1, 0, var1.length);
    }

    public int read(byte[] var1, int var2, int var3) throws IOException {
        /**
         * 对每个累加序号满足 INVERSE_INTERVAL 的数据进行取反。具体方法如下：
         * 1. 求出每个数组中第一个取反位
         * 2. 以间隔递增取反。
         *
         * 每个数组第一个取反位 index 计算方式：
         * 本数组中第几个为取反位 = 本数组中下一个需要取反的字节为第几个（相对于所有累计） - 之前累计读取的字节总量
         *                     = ( 之前累计读取的字节总量 / 间隔 + 1 ) * 间隔        - 之前累计读取的字节总量
         * 组数index = 第几个取反位 - 1
         */
        int beforeCount = count;
        int read = super.read(var1, var2, var3);
        count += read;
        int startPoint = (beforeCount / INVERSE_INTERVAL + 1) * INVERSE_INTERVAL - beforeCount - 1;

        for (int i = startPoint; i < (var3 - var2); i += INVERSE_INTERVAL) {
            var1[i + var2] = (byte) ~var1[i + var2];
        }

        return read;
    }

    @Override
    public boolean markSupported() {
        return false;
    }
}
