package com.nioserver;

import com.nioserver.http.HttpRequest;

import java.util.HashMap;

/**
 * @author: King.Z
 * @since: 2020/11/26 21:22 <br>
 * @desc: Http Request.
 */
public class HttpServerRequest extends HttpRequest {
    // protocol version
    public String protocolVer;
    // request's body bytes
    public byte[] body;
    // request's headers
    public HashMap<String, String> headers = new HashMap<String, String>();

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
