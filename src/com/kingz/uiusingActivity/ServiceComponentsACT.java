package com.kingz.uiusingActivity;

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
import com.kingz.four_components.ComponentsOfService;
import com.kingz.uiusingListViews.R;

/**
 * @author: KingZ
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_service_test);

		/**
		 * ��Ҫһ������Service��Intent
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
			startService(seviceIntent); //��������
			break;
		case R.id.stop_server:
			stopService(seviceIntent);	//ֹͣ����
			break;

		/**
		 * �ͷ���ͨ�š���������bindService�ķ�ʽ
		 * �ͷ�����а󶨺���ܺͷ������ͨ��
		 */
		case R.id.bind_service:
			/**
			 *  �󶨷���
			 *  // if this Context is an Activity that is stopped, the service will
			 *  not be required to continue running until the Activity is resumed.
			 */
			bindService(seviceIntent, this, Context.BIND_AUTO_CREATE);
			break;
		case R.id.unbind_service:
			unbindService(this);	//������
			break;
		case R.id.btn_getCurrentNum:
			if(echoService != null){
				System.out.println("��ǰ����Ϊ��" + echoService.getCurrentNum());
			}
			break;
		default:
			break;
		}

	}

	//Ŀ�����
	private ComponentsOfService echoService = null;


	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		// ****************��Ҫ������********************
		//���IBinder���͵Ĳ������Ƿ���İ���ComponentsOfServiceBinder
		echoService = ((ComponentsOfService.ComponentsOfServiceBinder)binder).getService();//ȡ��ʵ��

		System.out.println("onServiceConnected");
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		System.out.println("onServiceDisconnected");
	}
}
