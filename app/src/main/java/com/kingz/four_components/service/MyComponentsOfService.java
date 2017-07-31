package com.kingz.four_components.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.utils.ZLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: KingZ
 * @Data: 2015年10月7日下午9:02:56
 * @Description:四大组件之一 ———— 服务
 * 生命周期：
 * 	A:	用.startService()方法启动
 * 		onCreate()[只执行一次] ——> onStartCommand()[可多次被调用] ——> onDestory()
 *  B:	用.bingService()方法启动
 *  	onCreate()[只执行一次] ——> onBind() ——> onUnbind() ——> onDestory()
 *  如果是先startService()方法启动服务，再用bindService()方法绑定服务，在调用unbindService()方法
 *  解除绑定，最后再用unbindService()绑定到服务。出发的生命周期为：
 *  onCreate()  ——> onStart() ——> onBind() ——> onUnbind()[重载后的方法需返回true]  ——> onRebind();
 */
public class MyComponentsOfService extends Service{

	/**
	 * 回调的Binder
	 */
	private IBinder echoServiceBinder = new LocalServiceBinder();

	//onBind()将返回给客户端一个IBind接口实例，IBind允许客户端回调服务的方法
	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(this, "本地服务 onBind", Toast.LENGTH_SHORT).show();
		ZLog.i("MyComponentsOfService","本地服务 onBind");
		return echoServiceBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "本地服务 onUnbind", Toast.LENGTH_SHORT).show();
		ZLog.i("MyComponentsOfService","本地服务 onUnbind");
		return super.onUnbind(intent);
	}

	private int i = 0;
	private Timer timer = null;
	private TimerTask task = null;

	public void startTimer(){
		ZLog.i("MyComponentsOfService","启动计时器");
		if(timer == null){
			timer = new Timer();
			task = new  TimerTask() {
				@Override
				public void run() {
					i++;
					ZLog.i("MyComponentsOfService","计时器中i为："+i);
				}
			};
			timer.schedule(task, 1000,1000);
		}
	}
	public void stopTimer(){
		if(timer != null){
			task.cancel();
			timer.cancel();
			task = null;
			timer = null;
		}
	}

	/**
	 * @author: KingZ
	 * @Data: 2015年10月7日下午11:17:20
	 * @Description: 一个服务的继承器
	 * 	 用来外部和Service通信的桥梁
	 */
	public class LocalServiceBinder extends Binder{
		//得到当前服务的实例
		public MyComponentsOfService getService(){
			return MyComponentsOfService.this;
		}

	}

	public int getCurrentNum(){
		return i;
	}

	@Override
	public void onCreate() {
		ZLog.i("MyComponentsOfService","onCreate");
		startTimer();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ZLog.i("MyComponentsOfService","onStartCommand.....");
		Toast.makeText(this,"Service onStartCommand() more than once",Toast.LENGTH_SHORT).show();
		i = 0;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		ZLog.i("MyComponentsOfService","onDestroy.....停止计时器");
		stopTimer();
		super.onDestroy();
	}
}
