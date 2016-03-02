package com.kingz.four_components;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.kingz.uiusingListViews.R;

/**
 * @author: KingZ
 * @Data: 2015年10月7日下午10:23:44
 * @Description:	服务测试activity
 */
public class ServiceComponentsACT extends Activity
						implements OnClickListener, ServiceConnection{
	public static final String TAG = "ServiceComponentsACT";
	private IAnimal animal;
	private Button bt_start,bt_stop,bt_bind,bt_unbind,bt_getNum,btn_startIntent,btn_ipc_method;
	private Intent seviceIntent;
	private Intent intent_2;
	private boolean mBound;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_service_test);

		/**
		 * 需要一个连接Service的Intent
		 */
		seviceIntent = new Intent(this, ComponentsOfService.class);
		intent_2 = new Intent(this, IntentServicDemo.class);
		intent_2.putExtra("info","我要下班回家");
		initViews();
	}

	private void initViews() {
		bt_start = (Button) findViewById(R.id.start_server);
		bt_stop = (Button) findViewById(R.id.stop_server);
		bt_bind = (Button) findViewById(R.id.bind_service);
		bt_unbind = (Button) findViewById(R.id.unbind_service);
		bt_getNum = (Button) findViewById(R.id.btn_getCurrentNum);
		bt_getNum = (Button) findViewById(R.id.btn_getCurrentNum);
		btn_startIntent = (Button) findViewById(R.id.btn_intentserver_id);
		btn_ipc_method = (Button) findViewById(R.id.btn_ipc_method);

		bt_start.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		bt_bind.setOnClickListener(this);
		bt_unbind.setOnClickListener(this);
		bt_getNum.setOnClickListener(this);
		btn_startIntent.setOnClickListener(this);
		btn_ipc_method.setOnClickListener(this);
	}

	/**
	 * 绑定服务的连接回调接口
	 */
	private ServiceConnection  conn = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "绑定回调onServiceConnected!!!");
			mBound = true;
			//绑定成功后的回调方法
			animal = IAnimal.Stub.asInterface(service);
			btn_ipc_method.setEnabled(true);
			Toast.makeText(ServiceComponentsACT.this, "绑定成功", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			//绑定失败的回调方法
			Log.d(TAG, "绑定回调onServiceDisconnected!!!");
			Toast.makeText(ServiceComponentsACT.this, "绑定失败", Toast.LENGTH_SHORT).show();
		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.start_server:
				startService(seviceIntent);
				Toast.makeText(this, "startService(Intent intent) 启动服务", Toast.LENGTH_SHORT).show();
				break;
			case R.id.stop_server:
				stopService(seviceIntent);
				Toast.makeText(this, "stopService(Intent intent) 停止服务", Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_getCurrentNum:
				if (echoService != null) {
					System.out.println("当前数字为：" + echoService.getCurrentNum());
				}
				break;

			/**
			 * 和服务通信————用bindService的方式
			 * 应用程序组件之间（客户端）通过bingServcice()方法绑定服务，android会调用onBind回调方法，
			 * 此方法会返回一个和服务端交互的IBinder对象。这个绑定是异步的，bindService()方法立即返回，并不会
			 * 给客户端返回IBinder对象，要接受IBinder对象,需要ServiceConnection类实例。
			 */
			case R.id.bind_service:
				Log.d(TAG, "绑定一个服务!");
				Intent boundServiceIntent = new Intent(this,BoundServiceDemo.class);
				bindService(boundServiceIntent,conn, Context.BIND_AUTO_CREATE);
				break;
			case R.id.unbind_service:
				if(mBound){
					Log.d(TAG, "解绑一个服务!！！");
					unbindService(conn);
					mBound = false;
				}
				break;
			case R.id.btn_ipc_method:
				if(animal == null){
					return;
				}else{
					try {
						animal.setName("KingZ");
						Toast.makeText(ServiceComponentsACT.this, ""+animal.getValue(), Toast.LENGTH_SHORT).show();
						Log.d(TAG, "获取到的信息:"+ animal.getValue());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.btn_intentserver_id:
				startService(intent_2); //启动服务
				Toast.makeText(this, "startService(Intent intent) 启动IntentService服务", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
		}

	}

	//目标服务
	private ComponentsOfService echoService = null;


	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		// ****************重要桥梁点********************
		//这个IBinder类型的参数就是服务的绑定器ComponentsOfServiceBinder
		echoService = ((ComponentsOfService.ComponentsOfServiceBinder)binder).getService();//取得实例

		System.out.println("onServiceConnected");
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		System.out.println("onServiceDisconnected");
	}
}
