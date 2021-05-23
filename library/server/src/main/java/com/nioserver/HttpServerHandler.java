package com.nioserver;

import android.text.TextUtils;

import com.nioserver.utils.MemoryOutputStream;
import com.nioserver.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler of server.
 * deal below work :
 * [read/decode/compute/encode/send]
 */
public class HttpServerHandler implements ISelectableChannelHandler{
    private final NIOHttpServer _server;
    private final SocketChannel _socketChannel;
    private HttpRequestBuilder mRequestBuilder;
    private ByteBuffer mRequestBuffer = ByteBuffer.allocate(2048);
    private ByteBuffer mResponseBuffer;
    private HttpServerResponse _response;
    private boolean _sendChunkedData = false;

    public HttpServerHandler(NIOHttpServer server, SocketChannel socketChannel) {
        _server = server;
        _socketChannel = socketChannel;
    }

    SocketChannel getSocketChannel() {
        return _socketChannel;
    }

    /**
     * 通过socketChannel对象发送Response数据给客户端;
     * 若无Content-Type的头信息,则添加默认值： text/html
     * @param serverResponse HttpResponse.
     */
    void send(HttpServerResponse serverResponse) {
        _response = serverResponse;
        serverResponse.addHeaderIfNotExists("Content-Type", "text/html")
                .addHeaderIfNotExists("Connection", "close");

        final String transferEncoding = _response.headers.get("Transfer-Encoding");
        _sendChunkedData = "chunked".equals(transferEncoding);
        serverResponse.prepareResponseData();

        mResponseBuffer = ByteBuffer.wrap(serverResponse.getData(), 0, serverResponse.getDataSize());
        try {
            Selector selector = _server.getSelector();
            _socketChannel.register(selector, SelectionKey.OP_WRITE, this);
            selector.wakeup();
        } catch (ClosedChannelException e) {
            clear();
            e.printStackTrace();
        }
    }

    @NotNull
    private Buffer resetRequestBuffer() {
        return mRequestBuffer.rewind();
    }

    private void _internalHandleHttpRequest(final HttpServerRequest request) throws IOException {
        //将channel注册到选择器上
        _socketChannel.register(_server.getSelector(), 0, this);
        _server.dispatchRequest(this, request);
    }

    /**
     * 可重写此方法, 实现自定义解析Request的逻辑;
     * @param request HttpRequest
     */
    protected void handleHttpRequest(HttpServerRequest request) throws IOException {
        getResponse(request)
                .setStatus(404)
                .setMessage("Page Not Found")
                .send();
    }

    /**
     * 调用此方法,获取最基本的Response;
     * @param httpRequest HttpRequest
     * @return HttpResponse
     */
    public HttpServerResponse getResponse(HttpServerRequest httpRequest) {
        HttpServerResponse serverResponse = new HttpServerResponse(this);
        serverResponse.protocolVer = httpRequest.protocolVer;
        serverResponse.setStatus(200)
                .addHeader("Host", httpRequest.getHostString());
        return serverResponse;
    }


    /**
     * Close socket channel and destory HttpServerResponse
     */
    @Override
    public void terminate() {
        SocketChannel socketChannel = _socketChannel;
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        clear();
    }

    /**
     * 内部通知有数据可写出，执行以下流程：
     * encode ---> send
     */
    @Override
    public void notifyWritable(long clock) {
        if (mResponseBuffer == null) {
            return;
        }
        try {
            //send data to client with socketChannel.
            final SocketChannel socketChannel = _socketChannel;
            socketChannel.write(mResponseBuffer);
            if (!mResponseBuffer.hasRemaining()) {
                //没有剩余的buffer容量了
                if (_response.hasUserBodyStream()) {
                    // Client has send body data.
                    final Selector selector = _server.getSelector();
                    final HttpServerHandler attachment = this;
                    socketChannel.register(selector, 0, attachment);
                    // Causes the first blocking selection operation return immediately.
                    selector.wakeup();
                    _server.getReactorPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 分块时以32个字节为起点
                                int beginOffset = _sendChunkedData ? 32 : 0;
                                // 分块时话以2字节为结束点
                                int endOffset = _sendChunkedData ? 2 : 0;
                                // 分块的话,一个块8KB?
                                int sizeLimit = _sendChunkedData ? 8192 : -1;
                                if (_response == null || !_response.prepareUserBodyData(beginOffset, endOffset, sizeLimit)) {
                                    terminate();
                                    return;
                                }
                                int dataSize = _response.getDataSize();
                                //if response output data <= 0.
                                if (dataSize <= 0) {
                                    if (_sendChunkedData) {
                                        _response.writeStream(null);
                                        mResponseBuffer = ByteBuffer.wrap("0\r\n\r\n".getBytes());
                                    } else {
                                        terminate();
                                        return;
                                    }
                                } else {
                                    // get output data.
                                    byte[] data = _response.getData();
                                    // TODO 看不懂分块的逻辑
                                    if (_sendChunkedData) {
                                        String dataLength = String.format("%X\r\n", dataSize);
                                        final byte[] dataLengthBytes = dataLength.getBytes();
                                        beginOffset -= dataLengthBytes.length;
                                        System.arraycopy(dataLengthBytes, 0, data, beginOffset, dataLengthBytes.length);
                                        dataSize += dataLengthBytes.length;
                                        data[beginOffset + dataSize++] = '\r';
                                        data[beginOffset + dataSize++] = '\n';
                                    }
                                    //wrap byte[] to buffer
                                    mResponseBuffer = ByteBuffer.wrap(data, beginOffset, dataSize);
                                }
                                socketChannel.register(selector, SelectionKey.OP_WRITE, attachment);
                                selector.wakeup();
                            } catch (ClosedChannelException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    socketChannel.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 内部通知有数据可读取，执行以下流程：
     * read ---> decode ---> compute
     */
    @Override
    public void notifyReadable(long clock) {
        resetRequestBuffer();
        try {

            //read bytes data from soketChannel.
            int readBytes = 0;
            readBytes = _socketChannel.read(mRequestBuffer);

            if (readBytes < 0) { // end is '-1'
                if (mRequestBuilder == null) {
                    _socketChannel.close(); // close the channel
                    return;
                }
                if (mRequestBuilder.isFinished()) {
                    // wait for handling request
                    return;
                }
                // if request has no Content-Length, try to finish the request building
                resetRequestBuffer();
                HttpServerRequest xulHttpRequest = mRequestBuilder.buildRequest(mRequestBuffer, readBytes);
                if (xulHttpRequest == null) {
                    // build request failed
                    _socketChannel.close();
                } else {
                    _internalHandleHttpRequest(xulHttpRequest);
                }
                return;
            }

            if (mRequestBuilder == null) {
                mRequestBuilder = new HttpRequestBuilder();
            }
            resetRequestBuffer();
            HttpServerRequest xulHttpRequest = mRequestBuilder.buildRequest(mRequestBuffer, readBytes);
            if (xulHttpRequest != null) {
                _internalHandleHttpRequest(xulHttpRequest);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        HttpRequestBuilder requestBuilder = mRequestBuilder;
        mRequestBuilder = null;
        if (requestBuilder != null) {
            requestBuilder.destroy();
        }

        HttpServerResponse response = _response;
        _response = null;
        if (response != null) {
            response.destroy();
        }
    }

    public class HttpRequestBuilder {
        private HttpServerRequest mRequest;
        private MemoryOutputStream mReadBuffer;
        // 0 for REQUEST line, 1 for HEADER, 2 for BODY
        private int parseState = 0;
        // content-length (byte)
        private int mRequestBodySize = 0;
        // body size limit: 1MB
        private int mBodySizeLimit = 1024 * 1024;
        private int _readPos = 0;
        private int _scanPos = 0;
        private boolean _finished = false;
        //default read buffer size: 4KB
        private int defaultCapacity = 4 * 1024;

        public HttpRequestBuilder() {
            mReadBuffer = new MemoryOutputStream(defaultCapacity);
        }

        public boolean isFinished() {
            return _finished;
        }

        public void destroy() {
            if (mReadBuffer != null) {
                mReadBuffer.onClose();
            }
        }

        private String readLine() {
            int dataSize = mReadBuffer.getDataSize();
            byte[] dataBuffer = mReadBuffer.getDataBuffer();
            for (int i = _scanPos; i < dataSize; i++) {
                byte ch = dataBuffer[i];
                if (ch != '\r' && ch != '\n') {
                    _scanPos = i;
                } else if (i + 1 < dataSize &&
                        ch == '\r' &&
                        dataBuffer[i + 1] == '\n'
                ) {
                    int lineHead = _readPos;
                    int lineEnd = i;
                    _readPos = _scanPos = i + 2;
                    return new String(dataBuffer, lineHead, lineEnd - lineHead);
                }
            }
            return null;
        }

        /**
         * Build request data object.
         *
         * @param netBuffer buffer of request
         * @param readBytes
         * @return HttpRequest
         */
        public HttpServerRequest buildRequest(ByteBuffer netBuffer, int readBytes) {
            int dataSize = mReadBuffer.getDataSize();
            if (readBytes <= 0) {
                readBytes = 0;
                if (parseState == 2) {
                    mRequestBodySize = dataSize - _readPos;
                }
            }
            int newSize = dataSize + readBytes;
            mReadBuffer.expand(newSize);
            // Http请求的字节数据
            byte[] dataBuffer = mReadBuffer.getDataBuffer();
            netBuffer.get(dataBuffer, dataSize, readBytes);
            mReadBuffer.setDataSize(dataSize + readBytes);

            String line;

            while (parseState != 2 && (line = readLine()) != null) {
                if (parseState == 0) {
                    parseRequestLine(line);
                } else if (parseState == 1) {
                    if (line.isEmpty()) {
                        if (parseHeadersError()) {
                            return null;
                        }
                        break;
                    }
                    parseHeadersParams(line);
                } else {
                    // error
                }
            }

            // parse ---> end
            if (parseState == 2) {
                // TODO 达到啥条件?
                if (mRequestBodySize <= newSize - _readPos) {
                    // body ready
                    if (mRequestBodySize > 0) {
                        mRequest.body = Arrays.copyOfRange(dataBuffer, _readPos, _readPos + mRequestBodySize);
                    }
                    return mRequest;
                }
            }
            return null;
        }

        /**
         * Parse Header line is empty.
         *
         * @return True: failed
         * False: try pull body data.
         */
        boolean parseHeadersError() {
            parseState = 2;
            if (mRequestBodySize == 0 && !"get".equals(mRequest.method)) {
                String connection = mRequest.getHeader("connection");
                if (connection != null && connection.toLowerCase().contains("close")) {
                    mRequestBodySize = Integer.MAX_VALUE;
                }
            }

            // Check client has '100-continue' request at post protcal.
            String expect = mRequest.getHeader("expect");
            if (expect != null && expect.contains("100-continue")) {
                if (mRequestBodySize > mBodySizeLimit) {
                    // don't receive file larger then 1MB
                    try {
                        _socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                try {
                    String resp100Continue = mRequest.protocolVer.toUpperCase() + " 100 Continue\r\n\r\n";
                    // FIXME android-28 no write method. android-26 still exist.
                    _socketChannel.write(ByteBuffer.wrap(resp100Continue.getBytes("utf-8")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        /**
         * Parse request line data. Parse the parameter part in the path.
         *
         * @param line one line data of request line.
         *             以HTTP/1.1为例:
         *             {method} {path}{version}
         *             GET /index.html HTTP/1.1
         */
        void parseRequestLine(String line) {
            String[] requestLine = line.split(" ");

            String method = requestLine[0];
            String path = requestLine[1];
            String httpVer = requestLine.length > 2 ? requestLine[2] : "HTTP/1.1";

            mRequest = new HttpServerRequest();
            mRequest.method = method.toLowerCase();
            mRequest.protocolVer = httpVer.toLowerCase();
            try {
                mRequest.schema = httpVer.split("/")[0].toLowerCase();
            } catch (Exception e) {
                mRequest.schema = "http";
            }

            int queryStart = path.indexOf("?");
            int fragmentStart = path.indexOf("#");

            if (fragmentStart > 0) {
                mRequest.fragment = path.substring(fragmentStart + 1);
                path = path.substring(0, fragmentStart);
            }

            if (queryStart < 0) {
                // no '?', so no query params
                mRequest.path = path;
            } else {
                // has '?', get query param and save.
                mRequest.path = path.substring(0, queryStart);
                String queryString = path.substring(queryStart + 1);
                Pattern queryParamPattern = Pattern.compile("([^&=]+)(?:=([^&]*))?");
                Matcher matcher = queryParamPattern.matcher(queryString);
                while (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    try {
                        key = URLDecoder.decode(key, "utf-8");
                        if (!TextUtils.isEmpty(value)) {
                            value = URLDecoder.decode(value, "utf-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mRequest.addQueryString(key, value);
                }
            }
            //change to next state
            parseState = 1;
        }

        /**
         * Parse request headers data.
         * Confirm body size and save headers data.
         *
         * @param line one line data of headers.
         */
        void parseHeadersParams(String line) {
            int split = line.indexOf(':');
            String headerKey = line.substring(0, split).trim();
            String headerValue = line.substring(split + 1).trim();
            mRequest.addHeader(headerKey.toLowerCase(), headerValue);

            // handle content-length
            if (headerKey.equalsIgnoreCase("content-length")) {
                mRequestBodySize = Utils.tryParseInt(headerValue);
            } else if (headerKey.equalsIgnoreCase("host")) {
                int hostPortPos = headerValue.indexOf(":");
                if (hostPortPos > 0) {
                    mRequest.port = Utils.tryParseInt(headerValue.substring(hostPortPos + 1));
                    mRequest.host = headerValue.substring(0, hostPortPos);
                } else {
                    mRequest.host = headerValue;
                }
            }
        }
    }

}