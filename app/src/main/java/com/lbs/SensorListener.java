package com.lbs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * Created by KingZ on 2016/1/15.
 * Discription: 方向传感器类  实现SensorEventListener
 *  方向传感器有三个坐标轴
 */
public class SensorListener implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Context context;

    private float axis_X; //X轴
    private float axis_Y; //Y轴

    public SensorListener(Context context) {
        this.context = context;
    }

    /**
     * 开始监听
     */
    public void start(){
//        mSensorManager.registerListener()
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(null != mSensorManager){
//            mSensor = mSensorManager.getDefaultSensor(SensorManager.getOrientation());
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            if(null != mSensor){
                mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
            }else{
                Toast.makeText(context, "没有方向传感器", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 结束监听
     */
    public void stop(){

    }


    /**
     * 传感器被改变
     * @param event 改变事件源
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    /**
     * 进度改变回调函数
     * @param sensor    传感器实例
     * @param accuracy 精度
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
