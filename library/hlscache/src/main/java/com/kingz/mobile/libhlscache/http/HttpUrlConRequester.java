package com.kingz.mobile.libhlscache.http;

import com.kingz.mobile.libhlscache.utils.ActionCallBack;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created 2017/11/8.
 */
public class HttpUrlConRequester extends AbsHttpRequester {

    @Override
    public void enqueue(final String url, final ActionCallBack<IResponse> parser, final ActionCallBack<IOException> failCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IResponse response = execute(url);
                    if (parser != null) {
                        parser.call(response);
                    } else {
                        try {
                            response.close();
                        } catch (IOException ignore) {
                        }
                    }
                } catch (IOException e) {
                    if (failCallback != null) {
                        failCallback.call(e);
                    }
                }
            }
        }).start();
    }

    @Override
    public IResponse execute(String url) throws IOException {
        URL url1 = new URL(url);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url1.openConnection();
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            if (connection.getResponseCode() != 200) {
                throw new HttpResponseNot200Exception(connection.getResponseCode() + " with " + url);
            }

            return new Response(connection, connection.getInputStream());
        } catch (IOException e) {
            if (connection != null) {
                connection.disconnect();
            }
            throw e;
        }
    }

    public static class Response implements IResponse {
        private InputStream mInputStream;
        private HttpURLConnection mConnection;

        public Response(HttpURLConnection connection, InputStream inputStream) {
            mConnection = connection;
            mInputStream = inputStream;
        }

        @Override
        public InputStream getInputStream() {
            return mInputStream;
        }

        @Override
        public void close() throws IOException {
            mInputStream.close();
            mConnection.disconnect();
        }
    }

}
