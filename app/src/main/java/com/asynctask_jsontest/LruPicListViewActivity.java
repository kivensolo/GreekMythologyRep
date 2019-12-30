package com.asynctask_jsontest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.kingz.customdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * 这个Demo用于测试ListView的数据加载以及数据优化和图片的LRU缓存方法
 * Description：
 *      该类主要是AsyncTask的使用和对Json数据的解析，字节流转字符流
 */
public class LruPicListViewActivity extends Activity {

    private static final String TAG = LruPicListViewActivity.class.getSimpleName();
    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
    private GetInfoTask getInfoTask;        //获取数据的异步Task
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynctask_lru_main);

        mListView = (ListView) findViewById(R.id.lv_main);
        getInfoTask = new GetInfoTask();
        getInfoTask.execute(URL);
    }


    /**
     * 实现网络异步访问
     */
    class GetInfoTask extends AsyncTask<String,Void,List<NewsBean>> {
        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        /**
         * @param newsBeans
         */
        @Override
        protected void onPostExecute(List<NewsBean> newsBeans) {
            super.onPostExecute(newsBeans);
            NewsDataAdapter newsAdapter = new NewsDataAdapter(LruPicListViewActivity.this,newsBeans,mListView);
            mListView.setAdapter(newsAdapter);
        }
    }

    private List<NewsBean> getJsonData(String url) {
        JSONObject newsJsonObject;
        NewsBean newsBean;

        List<NewsBean> newsList = new ArrayList<NewsBean>();
        try {
            String jsonString = readStream(new URL(url).openStream()); //词句功能与url.openConnection().getInpuStream()相同 可根据Url直接联网获取网络数据，简单粗暴！
            //返回值为InputStream.

        //开始解析Json数据
            //1.建立JsonObject对象
            newsJsonObject = new JSONObject(jsonString);
            //2.通过JsonObject的对象的getJSONArray方法得到JsonArray
            JSONArray mJsonArray = newsJsonObject.getJSONArray("data");
            //3.得到JsonArray后，就遍历里面的节点，取出数据
            for (int i = 0; i < mJsonArray.length(); i++) {
                //4.JSONArray中的每一个元素都是个JSONObject
                newsJsonObject = mJsonArray.getJSONObject(i);
                newsBean = new NewsBean();
                newsBean.newspictureUrl = newsJsonObject.getString("picSmall");
                newsBean.newsTitle = newsJsonObject.getString("name");
                newsBean.newsContent= newsJsonObject.getString("description");
                //5.每找完一组数据，就添加到List中
                newsList.add(newsBean);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    /**
     * 通过InputStream解析网页返回的数据
     * @param ins  字节输入流
     * @return
     */
    private String readStream(InputStream ins){
        InputStreamReader isr;
        String result = " ";
        try {
            String line;
            isr = new InputStreamReader(ins,"utf-8");           //将字节流ins转为字符流
            BufferedReader buffer  = new BufferedReader(isr);   //把字符流读取到buffer里面
            while((line = buffer.readLine()) != null){
                result = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
