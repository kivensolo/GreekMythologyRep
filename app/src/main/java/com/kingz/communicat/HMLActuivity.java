package com.kingz.communicat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.zeke.kangaroo.utils.ZLog;

/**
 * author: King.Z <br>
 * date:  2017/7/12 21:14 <br>
 * description: Handler机制深入集合 <br>
 */
public class HMLActuivity extends Activity {

    public static final String TAG = "HMLActuivity";
    public static final int ADD = 0x001;
    public static final int DEC = 0x002;
    public static final int LOOP_DEC = 0x003;
    public static final int LOOP_ADD = 0x004;
    public static final String MESSAGE_DEC_TOKEN = "dec";
    private Handler mHandler;
    private Message mMessage;
    private MessageQueue mMessageQueue;
    private Looper mLooper;
    private int i = 0;

    TextView showView;
    Button directSend;
    Button delaySend;
    Button directDdec;
    Button loopDec;
    Button stopLoop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handler_deep);
        showView = (TextView) findViewById(R.id.numshowview);
        showView.setText(String.valueOf(i));
        directSend = (Button) findViewById(R.id.directly_send);
        delaySend = (Button) findViewById(R.id.delay_send);
        directDdec = (Button) findViewById(R.id.directy_dec);
        loopDec = (Button) findViewById(R.id.loop_dec);
        stopLoop = (Button) findViewById(R.id.loop_stop);

        initHandler(showView);

        mLooper = Looper.myLooper();
        initListener();
    }

    private void initHandler(final TextView showView) {
        Handler.Callback callback = new Handler.Callback() {
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
                        mMessage = Message.obtain();
                        mMessage.obj = MESSAGE_DEC_TOKEN;
                        mMessage.what = LOOP_DEC;
                        mHandler.sendMessageDelayed(mMessage,1000);
                        return true;
                    case LOOP_ADD:
                        showView.setText(String.valueOf(++i));
                        mHandler.sendEmptyMessageDelayed(LOOP_ADD,1000);
                        return true;

                    default:
                        break;
                }
                return false;
            }
        };
        mHandler = new Handler(callback);
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
    }

    public void delayDec(View v) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(DEC);
            }
        }, 3000);
    }

    public void loopDec(View v){
         mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(LOOP_DEC);
            }
        });
    }

    public void loopAdd(View v){
         mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(LOOP_ADD);
            }
        });
    }

    public void stopLoop(View v){
         mHandler.post(new Runnable() {
            @Override
            public void run() {
                //参数token 是message中的obj参数对象，相当于一个TAG吧，如果没有制定的TAG,则会清空所有pending状态的数据
                mHandler.removeCallbacksAndMessages(null);
            }
        });
    }
    public void stopDecLoop(View v){
         mHandler.post(new Runnable() {
            @Override
            public void run() {
                //参数token 是message中的obj参数对象，相当于一个TAG吧，如果没有制定的TAG,则会清空所有pending状态的数据
                mHandler.removeCallbacksAndMessages(MESSAGE_DEC_TOKEN);
            }
        });
    }
}
