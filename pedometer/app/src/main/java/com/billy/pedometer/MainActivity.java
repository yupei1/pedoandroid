package com.billy.pedometer;

import android.print.PrinterId;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.PowerManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import java.util.*;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private final String TAG = "tiny";
    SensorManager mSensorManager;
    Sensor stepCounter;
    Sensor lightSensor;
    Sensor PressureSensor;

    private TextView tv_pre;
    private float pressure;

    private float mLux;
    private TextView mTextView;

    private float mSteps = 0;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);


        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.i(TAG,"Sensor size:"+sensorList.size());
        for (Sensor sensor : sensorList) {
            Log.i(TAG,"Supported Sensor: "+sensor.getName());
        }

        tv = (TextView)findViewById(R.id.tv_step);
        tv_pre = (TextView)findViewById(R.id.tv_pre);
        //mTextView = (TextView)findViewById(R.id.tv_light);

        lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            mSensorManager.registerListener(this,lightSensor,1000000);
        }
        else{
            Log.e(TAG,"no light sensor found");
        }

        PressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(PressureSensor != null){
            mSensorManager.registerListener(this,PressureSensor,1000000);
        }
        else{
            Log.e(TAG,"no pressure sensor found");
        }

        stepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCounter != null){
            mSensorManager.registerListener(this,stepCounter,1000000);
        }
        else{
            Log.e(TAG,"no step counter sensor found");
        }
    }


    public void onSensorChanged(SensorEvent event){


        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            mLux = event.values[0];
            //mTextView.setText("The light strength is : " + mLux);
            changeWindowBrightness((int) mLux);
        }

        if(event.sensor.getType() == Sensor.TYPE_PRESSURE){
            pressure = event.values[0];
            tv_pre.setText("The pressure here is : " + pressure + " hPa");
        }
        mSteps = event.values[0];
        Log.i(TAG,"Detected step changes:"+event.values[0]);
        tv.setText(String.valueOf((int)mSteps));
    }

    public void changeWindowBrightness(int Brightness){
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (Brightness == -1){
            layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        }else{
            layoutParams.screenBrightness = (Brightness <= 0 ? 1 : Brightness) / 255f;
        }
        window.setAttributes(layoutParams);
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Log.i(TAG,"onAccuracyChanged");
    }

    protected void onPause() {
        // if unregister this hardware will not detected the step changes
        // mSensorManager.unregisterListener(this);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();

    }
}
