package com.kingz.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import com.App;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.kingz.customdemo.R;

import okhttp3.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
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
 *
 *   异步发起的请求会被加入到 Dispatcher 中的 runningAsyncCalls 双端队列中通过线程池来执行。
 *   --- Post :
 *      post需要构造RequestBody对象，用它来携带我们要提交的数据。
 *      在构造 RequestBody 需要指定MediaType(https://tools.ietf.org/html/rfc2045)，
 *      用于描述请求/响应 body 的内容类型，
 */
public class OkHttpClientManager {
    private static final String TAG = "OkHttpClientManager";
    private static final long DEFAULT_CONNECT = 8L;
    private static final long DEFAULT_READ = 4L;
    private static final long DEFAULT_WRITE = DEFAULT_READ;

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private OkHttpClientManager() {
        _initDefaultOkHttpClient();
        _initWithTimeOut(DEFAULT_CONNECT, DEFAULT_READ,DEFAULT_WRITE);
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    private OkHttpClient _initDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    private void _initWithTimeOut(long connectTimeOut, long readTimeOut, long writeTimeOut) {
        // 3.x设置超时和2.x有区别，不能再通过OkHttpClient对象设置，
        // 而是通过OkHttpClient.Builder来设置，通过builder配置好OkHttpClient后
        // 用builder.build()来返回OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }


    /**
     * 构建Post的Request对象
     * @param url
     * @param params
     * @return
     * TODO 增加Post的RequestBody动态处理
     */
    private Request buildPostRequest(String url, Param... params) {
//        guessMimeType(url);
        MediaType mediaType = MediaType.parse("text/html; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "I'm Test content");
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }


    /** Get请求 [异步]  Call.enqueue() **/
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }
    /** Get请求 [同步] 使用execute后,要关闭ResponseBody**/
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

    /** Post请求 [同步] **/
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        return mOkHttpClient.newCall(request).execute();
    }
    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    /** Post请求 [异步] **/
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        //TODO
//        deliveryResult(callback, request);
    }

    /**
     * 异步任务分发，放入队列执行。
     */
    private void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure: " + e.getMessage());
                sendFailedStringCallback(request, e, callback);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.protocol() + " " +response.code() + " " + response.message());
                if(response.body() == null){
                    onFailure(call,new IOException("responseBody is empty."));
                    return;
                }
                sendSuccessResultCallback(response, callback);
            }
        });
    }
    // ----------------------- 异步  End


    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 异步加载图片
     *
     * @param view
     * @param url
     * @throws IOException
     */
    private void _displayImage(final ImageView view, final String url, final int errorResId) {
        ZLog.d(TAG,"_displayImage : url = " + url);
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ZLog.e(TAG,"_displayImage onFailure : url = " + url);
                setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ZLog.d(TAG,"_displayImage onResponse \n url = " + url);
                InputStream is = null;
                try {
                    // response.body()只能用一次
                    is = response.body().byteStream();
                    // 对InputStream先进行转换
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    byte[] bmp_buffer;
                    int len = 0;
                    while( (len = is.read(buffer)) != -1){
                        outStream.write(buffer, 0, len);
                    }
                    outStream.flush();
                    is.close();
                    bmp_buffer = outStream.toByteArray();
                    final Bitmap bm = BitmapFactory.decodeByteArray(bmp_buffer, 0, bmp_buffer.length);
//                    final Bitmap bm = BitMapUtils.decodeStreamCustomOpts(is);
                    if(bm == null){
                        setErrorResId(view, errorResId);
                        return;
                    }
                    File filePath = new File(App.getAppInstance().getAppContext().getCacheDir().getPath(),"FilmPageDir");
                    File file = new File(filePath, EncryptTools.MD5(url));
                    FileUtils.saveBitmapWithPath(file, bm, Bitmap.CompressFormat.PNG,90);
                  FileUtils.dealPathFilesWithOldDate(filePath.toString(),System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3));
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




    //*************对外公布的方法  Start************
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


    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance()._post(url, files, fileKeys, params);
    }
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
        url = "http://p0.meituan.net/165.220/movie/7f32684e28253f39fe2002868a1f3c95373851.jpg";
        getInstance()._displayImage(view, url, R.drawable.pic_default);
    }

    //public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
    //    getInstance()._downloadAsyn(url, destDir, callback);
    //}

    //*************对外公布的方法  End************
    /**
     * 创建表单请求Body
     * @param params
     * @return
     */
    private RequestBody createFormBody(Param[] params){
        params = validateParam(params);
        FormBody.Builder builder = new FormBody.Builder();
        for (Param p:params){
            builder.add(p.key,p.value);
        }
        return builder.build();
    }

    /**
     * 猜测路径对应的Mime类型
     * @param path
     * @return Mime类型     */
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

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Response response, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(response);
                }
            }
        });
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

        // 返回Response数据  具体的数据类型由业务代码处理response.body().XXXX()
        public abstract void onResponse(Response response);
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
