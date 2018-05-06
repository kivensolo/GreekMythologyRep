package com.kingz.utils;

import com.App;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/21 12:57
 * description:
 */
public class HttpUtils {

    public static final String GET = "Get";
    public static final String POST = "Post";

    interface HttpCallBackListener{
        public void onFinish(String response);
        public void onError(Exception e);
    }

    /**
     * 通过HttpUrlConnection方式连接网络
     * @param address
     * @param listener
     */
    public static void sendHttpRequest(final String address, final HttpCallBackListener listener){
        if(!isNetWorkAvailable()){
            ToastTools.getInstance().showMgtvWaringToast(App.getAppInstance().getAppContext(),"net work is inavailable");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url =  new URL(address);
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod(GET);
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(10000);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        InputStream ins = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null){
                            response.append(line);
                        }
                        if(listener != null){
                            listener.onFinish(response.toString());
                        }

                    } catch (IOException e) {
                        if(listener != null){
                            listener.onError(e);
                        }
                        e.printStackTrace();
                    }finally {
                        if(connection != null){
                            connection.disconnect();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 通过HttpClient方式
     * @param address
     * @param listener
     */
    public static void sendHttpRequest2(final String address, final HttpCallBackListener listener){
        if(!isNetWorkAvailable()){
            ToastTools.getInstance().showMgtvWaringToast(App.getAppInstance().getAppContext(),"net work is inavailable");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpget);
                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        //服务器响应成功
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity);
                        listener.onFinish(response);
                    }
                } catch (IOException e) {
                    if(listener != null){
                        listener.onError(e);
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static boolean isNetWorkAvailable(){
        return true;
    }
}
