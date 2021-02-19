package com.zeke.assets;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.nioserver.HttpServerHandler;
import com.nioserver.HttpServerRequest;
import com.nioserver.HttpServerResponse;
import com.nioserver.NIOHttpServer;
import com.nioserver.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * author：ZekeWang
 * date：2021/2/1
 * description：基于Asset文件系统
 */
public class AssetServer extends NIOHttpServer {
    public static HttpServerResponse PENDING_RESPONSE = new HttpServerResponse(null);
    private static NIOHttpServer _debugServer;
    private Context context;

    public AssetServer(Context context, int port) {
        this(context, null, port);
    }

    public AssetServer(Context context, @Nullable String addr, int port) {
        super(addr, port);
        this.context = context;
    }

    @Override
    protected HttpServerHandler createHandler(NIOHttpServer server, SocketChannel socketChannel) {
        return new AssetRequestHandler(context, server, socketChannel);
    }

    class AssetRequestHandler extends HttpServerHandler {
        private final AssetManager mAssets;
		private volatile SimpleDateFormat _dateTimeFormat;


        public AssetRequestHandler(Context context, NIOHttpServer server, SocketChannel socketChannel) {
            super(server, socketChannel);
            mAssets = context.getResources().getAssets();
        }

        @Override
        protected void handleHttpRequest(HttpServerRequest request) throws IOException {
            String path = request.path;
            HttpServerResponse response;

            if(TextUtils.equals(path,"/")){
                response = getWebResource(request, "/index.html");
            } else if (path != null && path.startsWith("/api/")) {
                if ("/api/list-pages".equals(path)) {
                    response = listPages(request);
                }else{
                    response = getResponse(request)
                        .setStatus(501)
                        .setMessage("Debug API Not implemented");
                }
                if (response == PENDING_RESPONSE) {
                    return;
                }
            } else {
                response = getWebResource(request, path);
            }

            if (response != null) {
                response.send();
            }else{
                HttpServerResponse errorResponse = getResponse(request);
                errorResponse.setStatus(500)
                        .setMessage("HTTP/1.0 500 Internal Server Error")
                        .send();
            }
//			super.handleHttpRequest(request);
        }

        @Override
        public HttpServerResponse getResponse(HttpServerRequest httpRequest) {
            HttpServerResponse response = super.getResponse(httpRequest);
            synchronized (this) {
				if (_dateTimeFormat == null) {
					_dateTimeFormat = new SimpleDateFormat("ccc, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
					_dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				}
				response.addHeader("Date", _dateTimeFormat.format(new Date()))
					.addHeader("Server", "Assets Server/1.0");
			}
            return response;
        }

        /**
         * 获取Web资源
         */
        private HttpServerResponse getWebResource(HttpServerRequest request, String path) {
			HttpServerResponse response = getResponse(request);
            try {
                byte[] assetFileBytes = loadAssetFile(mAssets, path.substring(1));
                response.addHeader("Content-Type", Utils.getMIMEType(path.substring(1)));
                OutputStream bodyStream = response.getBodyStream();
                if(bodyStream != null){
                    //FIXME  java.lang.NullPointerException: Attempt to get length of null array
                    bodyStream.write(assetFileBytes);
                    bodyStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
			return response;
		}

        private HttpServerResponse listPages(HttpServerRequest request) {
			HttpServerResponse response = getResponse(request);
            response.setStatus(404)
                .cleanBody();
			return response;
		}
    }


    public static byte[] loadAssetFile(AssetManager assetManager, String fileName) throws IOException {
        InputStream input = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            input = assetManager.open(fileName);
            byte[] buffer = new byte[1024];
            int size;
            while (-1 != (size = input.read(buffer))) {
                output.write(buffer, 0, size);
            }
            output.flush();
            return output.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
