package com.kingz.communicat;

import android.app.Activity;
import android.os.*;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.utils.ZLog;

/**
 * author: King.Z <br>
 * date:  2017/7/12 21:14 <br>
 * description: Handler机制深入集合 <br>
 */
public class HandlerAllOverActuivity extends Activity {

    public static final String TAG = "HandlerAllOverActuivity";
    public static final int ADD = 1;
    public static final int DEC = 2;
    public static final int LOOP_DEC = 3;
    private Handler mHandler;
    private Message mMessage;
    private MessageQueue mMessageQueue;
    private Looper mLooper;
    private int i = 0;

    Button directSend;
    Button delaySend;
    Button directDdec;
    Button delayDdec;
    Button loopDec;
    Button stopLoop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handler_deep);
        final TextView showView = (TextView) findViewById(R.id.numshowview);
        showView.setText(String.valueOf(i));
        directSend = (Button) findViewById(R.id.directly_send);
        delaySend = (Button) findViewById(R.id.delay_send);
        directDdec = (Button) findViewById(R.id.directy_dec);
        delayDdec = (Button) findViewById(R.id.delay_dec);
        loopDec = (Button) findViewById(R.id.loop_dec);
        stopLoop = (Button) findViewById(R.id.loop_stop);

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ZLog.d(TAG,"handleMessage() what = " + msg.what);
                switch (msg.what) {
                    case ADD:
                        showView.setText(String.valueOf(++i));
                        return true;
                    case DEC:
                        showView.setText(String.valueOf(--i));
                        return true;
                    case LOOP_DEC:
                        showView.setText(String.valueOf(--i));
                        mHandler.sendEmptyMessageDelayed(LOOP_DEC,1000);
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });
        mLooper = Looper.myLooper();
        initListener();
    }

    private void initListener() {
        directSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZLog.d(TAG,"directSend onClick");
                Message msg = Message.obtain();
                msg.what = ADD;
                mHandler.sendMessageDelayed(msg, 0);
            }
        });

        delaySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZLog.d(TAG,"delaySend onClick");
                Message msg = Message.obtain();
                msg.what = ADD;
                mHandler.sendMessageDelayed(msg, 3000);
            }
        });

        directDdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZLog.d(TAG,"directDdec onClick");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(DEC);
                    }
                });
            }
        });
        delayDdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZLog.d(TAG,"delayDdec onClick");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(DEC);
                    }
                }, 3000);
            }
        });
        loopDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void loopDec(View v){
         mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessageDelayed(DEC,1000);
            }
        }, 3000);
    }
}
