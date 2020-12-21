package com.kingz.mobile.libhlscache.utils;

import com.kingz.mobile.libhlscache.http.AbsHttpRequester;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created 2017/9/20.
 */
public class IOUtils {
    public static boolean save2FileWithTmpFile(InputStream in, File file, AbsHttpRequester.ProgressCallback progressCallback) throws IOException {
        String before = file.getAbsolutePath() + "tmp";
        save2File(in, new File(before), progressCallback);
        return rename(before, file.getAbsolutePath());
    }

    public static void save2File(InputStream in, File file, AbsHttpRequester.ProgressCallback progressCallback) throws IOException {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            cpStream(in, bos, progressCallback);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void cpStream(InputStream in, OutputStream out, AbsHttpRequester.ProgressCallback progressCallback) throws IOException {
        byte[] buf = new byte[1024];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
            if (progressCallback != null) {
                progressCallback.onRead(n);
            }
        }
    }

    public static boolean rename(String before, String after) {
        File srcFile = new File(before);
        File dstFile = new File(after);
        return srcFile.renameTo(dstFile);
    }

    public static boolean exists(String path) {
        return new File(path).exists();
    }


    public static boolean rmFile(String path) {
        File file = new File(path);
        return file.delete();
    }

}
