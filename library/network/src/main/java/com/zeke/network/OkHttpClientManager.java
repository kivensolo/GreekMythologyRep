package com.zeke.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.ding.library.CaptureInfoInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.zeke.kangaroo.utils.ZLog;
import com.zeke.network.interceptor.LoggingInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author: King.Z <br>
 * date:  2019/4/16 23:49 <br>
 * description: OkHttp工具类
 * 基于OKHttp 3.x <br>
 * <p>
 * 异步发起的请求会被加入到 Dispatcher 中的 runningAsyncCalls 双端队列中通过线程池来执行。
 *
 * --- Post :
 * post需要构造RequestBody对象，携带要提交的数据。
 * 在构造 RequestBody 需要指定MediaType(https://tools.ietf.org/html/rfc2045)，
 * 用于描述请求/响应 body 的内容类型，
 */
@SuppressWarnings({"WeakerAccess"})
public class OkHttpClientManager {
    private static final String TAG = "OkHttpClientManager";
    private static final long DEFAULT_CONNECT = 8000L;
    private static final long DEFAULT_READ = 4000L;
    private static final long DEFAULT_WRITE = DEFAULT_READ;

    //reate a single OkHttpClient instanceC
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private OkHttpClientManager() {
//        _initDefaultOkHttpClient();
        _initWithTimeOut(DEFAULT_CONNECT, DEFAULT_READ, DEFAULT_WRITE);
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }

    @SuppressWarnings("unused")
    private OkHttpClient _initDefaultOkHttpClient() {
        return new OkHttpClient.Builder().build();
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

    /* -------------------------------------对外公布的方法  Start -------------------------------------*/
    public Request.Builder getRequestBuilder(){
        return new Request.Builder();
    }

    public static void initWithTimeOut(int connectTimeOut, int readTimeOut, int writeTimeOut) {
        getInstance()._initWithTimeOut(connectTimeOut, readTimeOut, writeTimeOut);
    }

    public static Response getAsyn(String url) throws IOException {
        return getInstance()._getAsyn(url);
    }

    public Response getAsyn(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
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

    public static Response post(String url, File file, String fileKey) throws IOException {
        return getInstance()._post(url, file, fileKey);
    }

    public static Response post(String url, File file, String fileKey,
                                Param... params) throws IOException {
        return getInstance()._post(url, file, fileKey, params);
    }

    public static void postAsyn(String url, ResultCallback callback,
                                File[] files, String[] fileKeys,
                                Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }


    public static void postAsyn(String url, ResultCallback callback,
                                File file, String fileKey) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey);
    }


    public static void postAsyn(String url, ResultCallback callback,
                                File file, String fileKey,
                                Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey, params);
    }

    public static void displayImage(final ImageView view, String url, int defaultResId) throws IOException {
        getInstance()._displayImage(view, url, defaultResId);
    }

    public static void displayImage(final ImageView view, String url) {
        getInstance()._displayImage(view, url, R.drawable.pic_default);
    }

    /**
     * 文件下载
     * @param url   文件url
     * @param destDir   目标dir
     * @param callback  callback
     */
    public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
        getInstance()._downloadAsyn(url, destDir, callback);
    }
    /* -------------------------------------对外公布的方法  End -------------------------------------*/

    private void _initWithTimeOut(long connectTimeOut, long readTimeOut, long writeTimeOut) {
        OkHttpClient.Builder builder = getOkHttpClientBuilder(connectTimeOut, readTimeOut, writeTimeOut);
        mOkHttpClient = builder.build();
    }

    @NotNull
    private OkHttpClient.Builder getOkHttpClientBuilder(long connectTimeOut,
                                                        long readTimeOut,
                                                        long writeTimeOut) {
        return new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .addInterceptor(new LoggingInterceptor())
                    .addInterceptor(new CaptureInfoInterceptor())
                    .addNetworkInterceptor(new StethoInterceptor());
    }

    /**
     * 设置缓存大小
     * @param cache new Cache(getExternalCacheDir().getAbsoluteFile(), cacheSize)
     */
    public void setCache(Cache cache){
        mOkHttpClient = mOkHttpClient.newBuilder().cache(cache).build();
    }

    /**
     * 构建Post的Request对象
     *
     * @param url 请求url
     * @param params post的参数数据
     * @return  post的Request
     */
    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param p:params){
           builder.add(p.key,p.value);
        }
        RequestBody body = builder.build();
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    /**
     * Get请求 [异步]  Call.enqueue()
     **/
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
//                .header("User-Agent","KingZ Example")
                .build();
        deliveryResult(callback, request);
    }

    /**
     * Get请求 [同步]
     * @return Response  需要执行response.body().close()
     **/
    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
//                .header("User-Agent","KingZ Example")
                .build();
        return mOkHttpClient.newCall(request).execute();
    }

    private String _getAsString(String url) throws IOException {
        Response execute = _getAsyn(url);
        ResponseBody body = execute.body();
        return body == null ? "" : body.string();
    }

    /** Post请求 [同步] **/
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        ResponseBody body = response.body();
        return body == null ? null : body.string();
    }

    /**
     * 基于同步post的文件上传
     */
    private Response _post(String url, File file, String fileKey) throws IOException {
        return _post(url, file, fileKey, (Param) null);
    }

    private Response _post(String url, File file, String fileKey, Param... params) throws IOException {
        return _post(url, new File[]{file}, new String[]{fileKey}, params);
    }

    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /*--------------------------- 异步post的文件上传  ----------------- Start ----------*/
    /**
     * 单文件上传,不带参数
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException{
        _postAsyn(url, callback, new File[]{file}, new String[]{fileKey}, (Param) null);
    }

    /**
     * 单文件上传,带参数
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey,
                           Param... params) throws IOException{
        _postAsyn(url, callback, new File[]{file}, new String[]{fileKey}, params);
    }

    /**
     * 多文件上传,带参数
     */
    private void _postAsyn(String url,ResultCallback callback,File[] files,
                           String[] fileKeys, Param... params) throws IOException{
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    /*--------------------------- 异步post的文件上传  ----------------- End ----------*/


    /*--------------------------- 异步 post 请求  ----------------- Start ----------*/
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }
    /*--------------------------- 异步 post 请求  ----------------- End ----------*/


    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 异步任务分发，放入队列执行。
     */
    private void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.protocol() + " " + response.code() + " " + response.message());
                ResponseBody body = response.body();
                if (body == null) {
                    onFailure(call, new IOException("responseBody is empty."));
                    return;
                }
                try{
                    final String string = body.string();
                    if (callback.mType == String.class){
                        sendSuccessResultCallback(string, callback);
                    } else{
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }
                }catch(IOException e){
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }
    // -------------------------------------------- 异步  End ------------------------------------


    // ------------------------------------------- 加载图片 ---------------------------------------

    /**
     * Load the image asynchronously and display it on the imageview
     * @param view Display imageview
     * @param url  image url
     * @param errorResId  resource id of default image.
     *
     */
    private void _displayImage(final ImageView view, final String url, final int errorResId) {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ZLog.e(TAG, "_displayImage onFailure : url = " + url);
                setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream is = null;
                try {
                    // response.body() Only be used once
                    ResponseBody body = response.body();
                    if(body == null) {
                        setErrorResId(view, errorResId);
                        return;
                    }
                    is =  body.byteStream();
                    // Convert the InputStream first
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    byte[] bmp_buffer;
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    outStream.flush();
                    is.close();
                    bmp_buffer = outStream.toByteArray();
                    final Bitmap bm = BitmapFactory.decodeByteArray(bmp_buffer, 0, bmp_buffer.length);
//                    final Bitmap bm = BitMapUtils.decodeStreamCustomOpts(is);
                    if (bm == null) {
                        setErrorResId(view, errorResId);
                        return;
                    }
//                    File filePath = new File(App.instance.getApplicationContext().getAppContext().getCacheDir().getPath(), "FilmPageDir");
//                    File file = new File(filePath, EncryptTools.MD5(url));
//                    FileUtils.saveBitmapWithPath(file, bm, Bitmap.CompressFormat.PNG, 90);
//                    FileUtils.dealPathFilesWithOldDate(filePath.toString(), System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3));
                    setImage(view,bm);
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
    private void setImage(final ImageView view, final Bitmap bm){
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bm);
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

    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private void _downloadAsyn(final String url, final String destDir,
                               final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    //noinspection ConstantConditions
                    is = response.body().byteStream();
                    File file = new File(destDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException ignored) {}
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException ignored) {}
                }

            }
        });
    }

    /**
     * 创建表单请求Body
     * @return 请求body
     */
    private RequestBody createFormBody(Param[] params) {
        params = validateParam(params);
        FormBody.Builder builder = new FormBody.Builder();
        for (Param p : params) {
            builder.add(p.key, p.value);
        }
        return builder.build();
    }

    /**
     * 猜测路径对应的Mime类型
     * @return Mime类型
     */
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

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object response, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @SuppressWarnings("unchecked")
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

        // 返回Response数据(主线程)  具体的数据类型由业务代码处理response.body().XXXX()
        public abstract void onResponse(T response);
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
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
