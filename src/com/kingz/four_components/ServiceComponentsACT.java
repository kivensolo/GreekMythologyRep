package com.kingz.four_components;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
public class ServiceComponentsACT extends Activity implements OnClickListener, ServiceConnection{

	private Button bt_start;
	private Button bt_stop;
	private Button bt_bind;
	private Button bt_unbind;
	private Button bt_getNum;
	private Intent seviceIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_service_test);

		/**
		 * 需要一个连接Service的Intent
		 */
		seviceIntent = new Intent(this, ComponentsOfService.class);

		initViews();
	}

	private void initViews() {
		bt_start = (Button) findViewById(R.id.start_server);
		bt_stop = (Button) findViewById(R.id.stop_server);
		bt_bind = (Button) findViewById(R.id.bind_service);
		bt_unbind = (Button) findViewById(R.id.unbind_service);
		bt_getNum = (Button) findViewById(R.id.btn_getCurrentNum);

		bt_start.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		bt_bind.setOnClickListener(this);
		bt_unbind.setOnClickListener(this);
		bt_getNum.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.start_server:
			startService(seviceIntent); //启动服务
			Toast.makeText(this,"startService(Intent intent) 启动服务",Toast.LENGTH_SHORT).show();
			break;
		case R.id.stop_server:
			stopService(seviceIntent);	//停止服务
			Toast.makeText(this,"stopService(Intent intent) 停止服务",Toast.LENGTH_SHORT).show();
			break;

		/**
		 * 和服务通信————用bindService的方式
		 * 和服务进行绑定后才能和服务进行通行
		 */
		case R.id.bind_service:
			/**
			 *  绑定服务
			 *  // if this Context is an Activity that is stopped, the service will
			 *  not be required to continue running until the Activity is resumed.
			 */
			bindService(seviceIntent, this, Context.BIND_AUTO_CREATE);
			break;
		case R.id.unbind_service:
			unbindService(this);	//解绑服务
			break;
		case R.id.btn_getCurrentNum:
			if(echoService != null){
				System.out.println("当前数字为：" + echoService.getCurrentNum());
			}
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
