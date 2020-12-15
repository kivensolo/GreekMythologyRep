package com.nioserver;

import android.text.TextUtils;

import com.nioserver.http.HttpResponse;
import com.nioserver.utils.MemoryOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: King.Z
 * @since: 2020/11/26 22:28 <br>
 * @desc:
 */
public class HttpServerResponse extends HttpResponse {
    public String protocolVer;
    public LinkedHashMap<String, String> headers = new LinkedHashMap<String, String>();
    private NIOHttpServer.HttpServerHandler _handler;
    private MemoryOutputStream _outputStream;
    private InputStream _bodyStream;
    private Runnable _cleanup;


    public HttpServerResponse(NIOHttpServer.HttpServerHandler handler) {
        _handler = handler;
        _outputStream = new MemoryOutputStream(2048);
        _outputStream.reset(2048);
    }


    static String httpMessageFromCode(int code) {
        switch (code) {
            case 200:
                return "OK";
            case 401:
                return "Unauthorized";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 301:
                return "Moved Permanently";
            case 302:
                return "Redirect";
            case 304:
                return "Not Modified";
            case 500:
                return "Internal Server Error";
            case 501:
                return "Not implemented";
            case 502:
                return "Proxy Error";
            case 100:
                return "Continue";
        }
        return null;
    }

    public void send() {
        _handler.send(this);
    }

    /**
     * build response data.
     * 1. State Line  eg: HTTP/1.1 200 OK
     * 2. Headers
     *
     * 3. Entity-body
     */
    void prepareResponseData() {
        StringBuilder responseHdr = new StringBuilder();
        // [ build response state line start
        responseHdr.append(protocolVer.toUpperCase());
        responseHdr.append(" ");
        responseHdr.append(code);
        if (TextUtils.isEmpty(message)) {
            message = httpMessageFromCode(code);
        }
        if (!TextUtils.isEmpty(message)) {
            responseHdr.append(" ");
            responseHdr.append(message);
        }
        // build response state line end ].
        responseHdr.append("\r\n");

        // [ build response headers start
        String transferEncoding = headers.get("Transfer-Encoding");
        boolean isChunked = "chunked".equals(transferEncoding);
        int contentLength = _outputStream.getDataSize();
        if (!hasUserBodyStream()) {
            if (!isChunked) {
                addHeader("Content-Length", String.valueOf(contentLength));
            }
        }

        for (Map.Entry<String, String> hdr : headers.entrySet()) {
            responseHdr.append(hdr.getKey());
            responseHdr.append(":");
            responseHdr.append(hdr.getValue());
            responseHdr.append("\r\n");
        }
        // build response headers end ].
        responseHdr.append("\r\n");

        byte[] responseHdrBytes = null;
        try {
            responseHdrBytes = responseHdr.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] dataBuffer = _outputStream.getDataBuffer();

        int headerSize = responseHdrBytes.length;
        int newDataSize = contentLength + headerSize;
        _outputStream.expand(newDataSize);
        byte[] newDataBuffer = _outputStream.getDataBuffer();
        System.arraycopy(dataBuffer, 0, newDataBuffer, headerSize, contentLength);
        System.arraycopy(responseHdrBytes, 0, newDataBuffer, 0, responseHdrBytes.length);
        _outputStream.setDataSize(newDataSize);
    }


    public HttpServerResponse addHeader(String key, String val) {
        headers.put(key, val);
        return this;
    }

    public HttpServerResponse setStatus(int code) {
        this.code = code;
        return this;
    }

    public HttpServerResponse addHeaderIfNotExists(String key, String val) {
        if (!headers.containsKey(key)) {
            headers.put(key, val);
        }
        return this;
    }

    public HttpServerResponse writeBody(String data) {
        try {
            _outputStream.write(data.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpServerResponse writeBody(byte[] data) {
        try {
            _outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpServerResponse writeBody(byte[] data, int offset, int length) {
        try {
            _outputStream.write(data, offset, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpServerResponse writeBody(InputStream inputStream) {
        while (true) {
            int dataSize = _outputStream.getDataSize();
            _outputStream.expand(dataSize + 1024);
            byte[] dataBuffer = _outputStream.getDataBuffer();
            int bufferAvailableLength = dataBuffer.length - dataSize;

            try {
                int readLen = inputStream.read(dataBuffer, dataSize, bufferAvailableLength);
                if (readLen > 0) {
                    _outputStream.setDataSize(dataSize + readLen);
                    continue;
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        return this;
    }

    public HttpServerResponse writeStream(InputStream inputStream) {
        _bodyStream = inputStream;
        return this;
    }

    public OutputStream getBodyStream(int size) {
        _outputStream.expand(size);
        return _outputStream;
    }

    public OutputStream getBodyStream() {
        return _outputStream;
    }

    public HttpServerResponse setMessage(String msg) {
        this.message = msg;
        return this;
    }

    public HttpServerResponse setCleanUp(Runnable runnable) {
        _cleanup = runnable;
        return this;
    }

    byte[] getData() {
        return _outputStream.getDataBuffer();
    }

    int getDataSize() {
        return _outputStream.getDataSize();
    }

    boolean hasUserBodyStream() {
        return _bodyStream != null;
    }

    public HttpResponse cleanBody() {
        _outputStream.setDataSize(0);
        return this;
    }

    public void destroy() {
        if (_outputStream != null) {
            _outputStream.onClose();
        }

        try {
            final InputStream bodyStream = _bodyStream;
            _bodyStream = null;
            if (bodyStream != null) {
                bodyStream.close();
            }
        } catch (Exception e) {
        }

        try {
            final Runnable cleanup = _cleanup;
            _cleanup = null;
            if (cleanup != null) {
                cleanup.run();
            }
        } catch (Exception e) {
        }
    }

    boolean prepareUserBodyData() {
        return prepareUserBodyData(0, 0, -1);
    }

    boolean prepareUserBodyData(int startOffset, int endOffset) {
        return prepareUserBodyData(startOffset, endOffset, -1);
    }

    boolean prepareUserBodyData(int startOffset, int endOffset, int sizeLimit) {
        if (sizeLimit < 0) {
            _outputStream.expand(startOffset + endOffset + 128);
        } else {
            _outputStream.expand(startOffset + endOffset + sizeLimit);
        }

        final byte[] dataBuffer = _outputStream.getDataBuffer();
        if (sizeLimit < 0 || sizeLimit > dataBuffer.length) {
            sizeLimit = dataBuffer.length;
        }
        try {
            int readLength = _bodyStream.read(dataBuffer, startOffset, sizeLimit - startOffset - endOffset);
            _outputStream.setDataSize(readLength);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
