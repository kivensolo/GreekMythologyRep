package com.asuynctask_jsontest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.kingz.customdemo.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by KingZ on 2015/11/9.
 * Discription:
 * 使用多线程方式后台加载
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private ImageView imageView;
    public String mUrl;         //pic的url
    private LruCache<String,Bitmap> picCache;       //图片缓存管理变量
    public Set<GetNetImageTask> taskCollection;     //所有Task的集合
    private View view;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(imageView.getTag().equals(mUrl)){
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    public ImageLoader(View view) {
        this.view = view;
        taskCollection = new HashSet<GetNetImageTask>();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();  //获取程序最大可用内存
        Log.d(TAG,"得到可用的最大的内存为："+maxMemory);
        int cacheSize = maxMemory / 8;                           //设置图片缓存大小为程序最大可用内存的1/8
        picCache = new  LruCache<String,Bitmap>(cacheSize){
            //此方法用于获取每一个存进去的对象的大小，每次存入对象的时候调用
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value);
            }
        };
    }

    /**
     * 采用多线程的方式获取图片
     * @param imageView
     * @param mUrl
     */
    public void showImageByThread(final ImageView imageView, final String mUrl){
        this.imageView = imageView;
        this.mUrl = mUrl;
        /*********A: 用线程的方式执行 ****************/
        // 1。开启一个子线程(其实是多个)来获取bitmap数据
         new Thread(){
             @Override
             public void run() {
                 String threadName = this.getName();
                 Log.i(TAG,"通过子线程去获取图片数据的线程名字："+threadName);
                 try {
                     Bitmap bitmap = getbitmapByUrl(mUrl);
                     //不为空就添加图片到缓存
                     if(bitmap != null){
                         addBitmapToMemoryCache(mUrl,bitmap);
                     }
                     Message msg = Message.obtain();
                     msg.obj = bitmap;
                     msg.what = 110;
                     mHandler.sendMessage(msg);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }.start();
    }


    /**
     * 采用AsyncTask来后台获取图片
     */
    public void showImageByAsyncTask(ImageView imageView, String mUrl){
        judgeIsHasCachePic(mUrl, imageView);
    }
    /**
     * 从网络中华获取bitmap数据的方法
     * @param picUrl
     * @return
     * @throws IOException
     */
    private Bitmap getbitmapByUrl(String picUrl) throws IOException {
        Bitmap bitmap_net;
        InputStream ins = null;
        try {
            URL url = new URL(picUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);
            con.setDoInput(true);
            con.connect();
            //注意： 用BufferedinputStream 来读取所有的ins
            ins = new BufferedInputStream(con.getInputStream());
//            InputStream ins = new URL(picUrl).openStream();
            bitmap_net = BitmapFactory.decodeStream(ins);  //解析出图片
            //注意：释放~！
            con.disconnect();
            //模拟网络差的情况
//            Thread.sleep(1000);

            return bitmap_net;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        finally {
            if(ins != null){
                ins.close();//关闭流~！！
            }
        }
        return null;
    }


    /**
     * 异步加载图片Task
     */
    private class GetNetImageTask extends AsyncTask<String,Void,Bitmap> {

//        private ImageView imageView;
        private String url;
        private Bitmap resultmBitmap;

        public GetNetImageTask(String url) {
//            this.imageView = imageView;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                Log.i(TAG,"doInBackground().....后台通过网络获取图片ing....");
                resultmBitmap = getbitmapByUrl(params[0]);
                if(resultmBitmap != null){
                    //每次获取到图片后就添加到缓存队列中
                    Log.i(TAG,"doInBackground().....获取的图片不为空，添加到缓存");
                    addBitmapToMemoryCache(params[0],resultmBitmap);
                    return resultmBitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //当bitmap数据取回来后，在onPostExecute里面执行图片设置
            imageView = (ImageView) view.findViewWithTag(url);
            if(imageView != null && bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);    //执行成功后移除Task
        }
    }

     /**
     * 将图片存储到LruCache中。
     * @param key  LruCache的键，这里传入图片的URL地址。
     * @param bitmap LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        //先取，若为空 就添加
        if (getBitmapFromMemoryCache(key) == null) {
            Log.i(TAG,"addBitmapToMemoryCache()  缓存中没有该图片，添加缓存成功! key :"+key);
            picCache.put(key, bitmap);
        }
    }

    /**
     * 给ImageView设置图片。首先从LruCache中取出图片的缓存，设置到ImageView上。如果LruCache中没有该图片的缓存，
     * 就给ImageView设置一张默认图片。
     *
     * @param index   图片的URL地址，用于作为LruCache的键。
     * @param imageView   用于显示图片的控件。
     */
    public void judgeIsHasCachePic(String index, ImageView imageView) {
        Log.d(TAG,"judgeIsHasCachePic() 从缓存中查找图片");
        Bitmap bitmap = getBitmapFromMemoryCache(index);
        if (bitmap != null) {
             Log.d(TAG,"judgeIsHasCachePic() 有缓存图片，直接加载");
            imageView.setImageBitmap(bitmap);
        } else {
            Log.d(TAG,"judgeIsHasCachePic() 无缓存图片，显示默认图片");
            imageView.setImageResource(R.drawable.ic_launcher);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     * @param key  LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        Log.d(TAG,"getBitmapFromMemoryCache() 从缓存中根据key值获取图片 key："+key);
        return picCache.get(key);
    }

    /**
    * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
    * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
    *
    * @param firstVisibleItem  第一个可见的ImageView的下标
    * @param visibleItemCount  屏幕中总共可见的元素数
    */
    private String urlOfUrls = "";

    public void loadStart2EndBitmaps(int firstVisibleItem, int visibleItemCount) {
        try {
//          Log.d(TAG,"第一个可见的item为："+firstVisibleItem+";可见item的总数为："+visibleItemCount);
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                urlOfUrls = NewsDataAdapter.URLS[i];     //用url来当做indexKey
                Bitmap bitmap = getBitmapFromMemoryCache(urlOfUrls);
                if(bitmap == null){
                    Log.i(TAG,"从缓存中得到的数据为空，新建Task跑数据");
                    GetNetImageTask downloadImgTask = new GetNetImageTask(urlOfUrls);
                    taskCollection.add(downloadImgTask);    //添加下载任务
                    downloadImgTask.execute(urlOfUrls);
                    Log.d(TAG,"loadStart2EndBitmaps() 缓存中没有对应图片---->添加下载任务成功");
                }else{
                   //根据Tag获取到相应的imageVeiw
                    Log.i(TAG,"从缓存中得到了图片数据,直接设置");
                    imageView = (ImageView) view.findViewWithTag(urlOfUrls);
                    imageView.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * 滑动的时候，取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (GetNetImageTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }
}
