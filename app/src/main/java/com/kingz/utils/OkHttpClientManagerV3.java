package com.kingz.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import com.App;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.MultipartBuilder;
//import com.squareup.okhttp.RequestBody;

/**
 * author: King.Z <br>
 * date:  2019/4/16 23:49 <br>
 * description: OkHttp工具类
 * 基于OKHttp 3.x <br>
 *     TODO 完善 优化
 */
public class OkHttpClientManagerV3 {
    private static final String TAG = "OkHttpClientManagerV3";

    private static OkHttpClientManagerV3 mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private OkHttpClientManagerV3() {
        _initDefaultOkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    private OkHttpClient _initDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

    public static OkHttpClientManagerV3 getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManagerV3.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManagerV3();
                }
            }
        }
        return mInstance;
    }

    private void _initWithTimeOut(int connectTimeOut, int readTimeOut, int writeTimeOut) {
        // 3.x设置超时和2.x有区别，不能再通过OkHttpClient对象设置，
        // 而是通过OkHttpClient.Builder来设置，通过builder配置好OkHttpClient后
        // 用builder.build()来返回OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }

    /**************************
     * 同步的Get请求  start  用Call.execute()
     **************************/
    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    private String _getAsString(String url) throws IOException {
        Response execute = _getAsyn(url);
        return execute.body().string();
    }

    /**************************
     * 同步的Post请求  start
     **************************/
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**************************
     * 异步的Get请求  start   用Call.enqueue()
     **************************/
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }

    /**************************
     * 异步的Post请求  start
     **************************/
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 加载图片
     *
     * @param view
     * @param url
     * @throws IOException
     */
    private void _displayImage(final ImageView view, final String url, final int errorResId) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //TODO
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

            public void onFailure(Request request, IOException e) {
                setErrorResId(view, errorResId);
            }

            public void onResponse(Response response) {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    //ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                    //ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                    //int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                    try {
                        is.reset();
                    } catch (IOException e) {
                        response = _getAsyn(url);
                        is = response.body().byteStream();
                    }
                    //BitmapFactory.Options ops = new BitmapFactory.Options();
                    //ops.inJustDecodeBounds = false;
                    //ops.inSampleSize = inSampleSize;
                    //final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                    final Bitmap bm = BitMapUtils.decodeStreamCustomOpts(is);

                    FileUtils.saveBitmapWithPath(new File(new File(App.getAppInstance().getAppContext().getCacheDir().getPath(), "FilmPageDir"), EncryptTools.MD5(url)),
                            bm,
                            Bitmap.CompressFormat.PNG,
                            90);
//                    FileUtils.dealPathFilesWithOldDate(new File(App.getAppContext().getCacheDir().getPath(),"FilmPageDir").toString(),System.currentTimeMillis() - 3 * 24 * 3600 * 1000);

                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageBitmap(bm);
                        }
                    });
                } catch (Exception e) {
                    setErrorResId(view, errorResId);
                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }
    //*************对外公布的方法************

    public static void initWithTimeOut(int connectTimeOut, int readTimeOut, int writeTimeOut) {
        getInstance()._initWithTimeOut(connectTimeOut, readTimeOut, writeTimeOut);
    }

    public static Response getAsyn(String url) throws IOException {
        return getInstance()._getAsyn(url);
    }

    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    public static void getAsyn(String url, ResultCallback callback) {
        getInstance()._getAsyn(url, callback);
    }

    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    public static void postAsyn(String url, final ResultCallback callback, Param... params) {
        getInstance()._postAsyn(url, callback, params);
    }


    public static void postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        getInstance()._postAsyn(url, callback, params);
    }


    //public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
    //    return getInstance()._post(url, files, fileKeys, params);
    //}
    //
    //public static Response post(String url, File file, String fileKey) throws IOException {
    //    return getInstance()._post(url, file, fileKey);
    //}
    //
    //public static Response post(String url, File file, String fileKey, Param... params) throws IOException {
    //    return getInstance()._post(url, file, fileKey, params);
    //}
    //
    //public static void postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
    //    getInstance()._postAsyn(url, callback, files, fileKeys, params);
    //}
    //
    //
    //public static void postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
    //    getInstance()._postAsyn(url, callback, file, fileKey);
    //}


    //public static void postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
    //    getInstance()._postAsyn(url, callback, file, fileKey, params);
    //}

    public static void displayImage(final ImageView view, String url, int errorResId) throws IOException {
        getInstance()._displayImage(view, url, errorResId);
    }


    public static void displayImage(final ImageView view, String url) {
        getInstance()._displayImage(view, url, -1);
    }

    //public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
    //    getInstance()._downloadAsyn(url, destDir, callback);
    //}

    //****************************


    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params) {
        //TODO  buildMultipartFormRequest
        return null;
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();

    //异步request请求
    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            //TODO 分发请求
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();//获得返回的字符串
                    //final byte[] mBytes= response.body().bytes();//通过二进制字节数组,可以转换为BItmap图片资源
                    //获得返回的inputStream,则调用response.body().byteStream() ;这里支持大文件下载,有inputStream可以通过IO的方式写文件。
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    private Request buildPostRequest(String url, Param[] params) {
        MediaType mediaType = MediaType.parse("text/html; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "I'm Test content");
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }


    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

}
