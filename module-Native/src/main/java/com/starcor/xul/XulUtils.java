package com.starcor.xul;

import java.nio.Buffer;

/**
 * Copyright (c) 2023, 北京视达科科技有限责任公司 All rights reserved.
 * author：ZekeWang
 * date：2023/7/19
 * description：
 */
public class XulUtils {
    public static boolean doBlurOnBuffer(Buffer buf, int width, int height, int pass) {
        return _native_do_fast_blur(buf, width, height, pass * 2);
    }

    private static native boolean _native_do_fast_blur(Buffer var0, int var1, int var2, int var3);

}
