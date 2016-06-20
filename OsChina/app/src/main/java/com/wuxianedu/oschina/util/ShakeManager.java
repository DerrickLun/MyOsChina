package com.wuxianedu.oschina.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 晃动监听器
 * Created by lungank on 2016/5/11.
 */
public class ShakeManager implements SensorEventListener {

    private static final long UPDATE_INTERVAL_TIME = 50;
    private static final double SPEED_SHRESHOLD = 4000; //速度阈值
    private Context context;
    private SensorManager sensorManager;
    private Sensor sensor;
    private OnShakeListener shakeListener;
    private long lastUpdateTime;
    private float lastX;
    private float lastY;
    private float lastZ;

    public ShakeManager(Context context){
        this.context = context;
        start();
    }

    /**
     * 开启监听
     */
    public void start() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if(sensor != null){
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    /**
     * 关闭监听
     */
    public void stop(){
        sensorManager.unregisterListener(this);
    }

    public void setShakeListener(OnShakeListener shakeListener){
        this.shakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPDATE_INTERVAL_TIME){
            return;
        }
        lastUpdateTime = currentUpdateTime;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ)
                / timeInterval * 10000;
        if (speed >= SPEED_SHRESHOLD) {
            shakeListener.onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnShakeListener{

        public void onShake();

    }


}
