package com.kushpreet.Step_Tracker.Services.StepCounter;
/* Class that just counts number of steps using accelerometer sensor */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepDetector implements SensorEventListener {
    private static final String TAG = "StepDetector";
    private final double threshHold = 2;

    private long previousTimeStamp;
    private double previousMagnitude;
    private int currSteps;
    private SensorManager sensorManager;
    private Sensor accelerometerStepCounter;
    private Context mContext;

    public StepDetector(Context context, int startingSteps){
        this.previousTimeStamp = System.currentTimeMillis();
        this.previousMagnitude = 0;
        this.currSteps = startingSteps;
        mContext = context;
    }

    public void startSensor() throws SensorFailureException {
        sensorManager = (SensorManager) this.mContext.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            this.accelerometerStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            this.sensorManager.registerListener(this,accelerometerStepCounter,SensorManager.SENSOR_DELAY_FASTEST);
        }else{
            throw new SensorFailureException("Failed to start accelerometer sensor");
        }
    }

    public void removeSensor(){
        sensorManager.unregisterListener(this, accelerometerStepCounter);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        long currTimeStamp = System.currentTimeMillis();
        if(currTimeStamp >= (previousTimeStamp+100)) { //if 1 milliseconds has passed
            checkForStep(sensorEvent.values);
            previousTimeStamp = currTimeStamp;
        }
    }

    private void checkForStep(float[] values){
        float accel_x = values[0];
        float accel_y = values[1];
        float accel_z = values[2];

        double magnitude = Math.sqrt(accel_x * accel_y * accel_z);
        double deltaMag = magnitude - previousMagnitude;
        previousMagnitude = magnitude;

        if (deltaMag > threshHold) {
            this.currSteps++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {    }

    public int getSteps(){
        return currSteps;
    }
}

