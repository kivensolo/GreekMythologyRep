package com.kingz.mobile.libhlscache.utils;

import com.kingz.mobile.libhlscache.EncryptInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;

/**
 * Utils of fixed NanoHTTP Response.
 */
public class NanoHTTPResponseUtils {

    public static NanoHTTPD.Response newNotFoundResponse() {
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
    }

    public static NanoHTTPD.Response newBadRequestResponse() {
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "Error 400, bad request.");
    }

    public static NanoHTTPD.Response newFixedFileResponse(File file, String mime) throws FileNotFoundException {
        return newFixedFileResponse(file, mime, false);
    }

    public static NanoHTTPD.Response newFixedFileResponse(File file, String mime, boolean encrypt) throws FileNotFoundException {
        NanoHTTPD.Response res;
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        if (encrypt) {
            in = new EncryptInputStream(in);
        }
        res = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mime,
                in, (int) file.length());
        return res;
    }
}
