package com.starcor.mobile.libhlscache;

import java.io.File;

/**
 * Created  2017/11/10.
 */
public class Tools {

    public static File getResourceFile(ClassLoader classLoader, String filePath) {
        return new File(classLoader.getResource(filePath).getFile());
    }

}
