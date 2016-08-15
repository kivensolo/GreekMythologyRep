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

import com.kingz.customdemo.R;

/**
 * description：四大组件之Service的用法
 */
public class ServiceComponentsACT extends Activity
                            implements OnClickListener {

    private Intent seviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivty_service_test);
        seviceIntent = new Intent(this, MyComponentsOfService.class);
        initViews();
    }

    private void initViews() {
        Button bt_start = (Button) findViewById(R.id.start_server);
        Button bt_stop = (Button) findViewById(R.id.stop_server);
        Button bt_bind = (Button) findViewById(R.id.bind_service);
        Button bt_unbind = (Button) findViewById(R.id.unbind_service);
        Button bt_getNum = (Button) findViewById(R.id.btn_getCurrentNum);
        bt_start.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
        bt_bind.setOnClickListener(this);
        bt_unbind.setOnClickListener(this);
        bt_getNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_server:
                startService(seviceIntent);  //Service的onStartCommand会被调用
                break;
            case R.id.stop_server:
                stopService(seviceIntent);
                break;
            case R.id.bind_service:
                bindService(seviceIntent, conn, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(conn);
                break;
            case R.id.btn_getCurrentNum:
                if (echoService != null) {
                    System.out.println("getCurrentNum：" + echoService.getCurrentNum());
                }
                break;
            default:
                break;
        }

    }


    private MyComponentsOfService echoService = null;
    //当我们连接service成功或失败时，会主动触发其内部的onServiceConnected或onServiceDisconnected方法。
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            echoService = ((MyComponentsOfService.LocalServiceBinder)service).getService();
            System.out.println("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("onServiceDisconnected");
        }
    };
}
