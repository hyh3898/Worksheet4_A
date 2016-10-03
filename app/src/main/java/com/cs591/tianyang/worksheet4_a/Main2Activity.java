package com.cs591.tianyang.worksheet4_a;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;



public class Main2Activity extends AppCompatActivity {
    private
    ImageView ImageMove;
    private String TAG = "BOSTON";

    private float lastX, lastY, lastZ;  //remeber the starting position
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;

    // determine if the user is shaking the phone reeeeeeally hard.
    private static int SIGNIFICANT_SHAKE = 1000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //register ImageView
        ImageMove = (ImageView)findViewById(R.id.ImageMove);

        // initialize acceleration values
        acceleration = 0.00f;                                         //Initializing Acceleration data.
        currentAcceleration = SensorManager.GRAVITY_EARTH;            //We live on Earth.
        lastAcceleration = SensorManager.GRAVITY_EARTH;               //Ctrl-Click to see where else we could use our phone.
        setContentView(R.layout.activity_main);


    }
    //here i twisted the code from professor, i went through the entire code.
    //this includes enableAccelerometerListening, disableAccelerometerListing and how we determine how the user is changing the position.
    //I twisted the algorithm inside onSensorChanged().
    @Override
    protected void onStart() {
        setContentView(R.layout.activity_main2);
        super.onStart();
        Log.i(TAG, "onStart Triggered.");
        enableAccelerometerListening();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop Triggered.");
        disableAccelerometerListening();
        super.onStop();
    }

    // enable listening for accelerometer events
    private void enableAccelerometerListening() {
        // The Activity has a SensorManager Reference.
        // This is how we get the reference to the device's SensorManager.
        SensorManager sensorManager =
                (SensorManager) this.getSystemService(
                        Context.SENSOR_SERVICE);    //The last parm specifies the type of Sensor we want to monitor


        //Now that we have a Sensor Handle, let's start "listening" for movement (accelerometer).
        //3 parms, The Listener, Sensor Type (accelerometer), and Sampling Frequency.
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);   //don't set this too high, otw you will kill user's battery.
    }

    // disable listening for accelerometer events
    private void disableAccelerometerListening() {

        //Disabling Sensor Event Listener is two step process.
        //1. Retrieve SensorManager Reference from the activity.
        //2. call unregisterListener to stop listening for sensor events
        //THis will prevent interruptions of other Apps and save battery.

        // get the SensorManager
        SensorManager sensorManager =
                (SensorManager) this.getSystemService(
                        Context.SENSOR_SERVICE);

        // stop listening for accelerometer events
        sensorManager.unregisterListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }


    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // get x, y, and z values for the SensorEvent
            //each time the event fires, we have access to three dimensions.
            //compares these values to previous values to determine how "fast"
            // the device was shaken.
            //Ref: http://developer.android.com/reference/android/hardware/SensorEvent.html

            float x = event.values[0];   //always do this first
            float y = event.values[1];
            float z = event.values[2];

            // save previous acceleration value
            lastAcceleration = currentAcceleration;

            // calculate the current acceleration
            currentAcceleration = (float)Math.sqrt(x*x + y*y + z*z);   //This is a simplified calculation, to be real we would need time and a square root.

            // calculate the change in acceleration        //Also simplified, but good enough to determine random shaking.

            float delta = currentAcceleration - lastAcceleration;
            acceleration = acceleration*0.9f+delta;

            // if the acceleration is above a certain threshold
            // Make this higher or lower according to how much
            // motion you want to detect
            if (acceleration > 1) {
                float DeltaX = Math.abs(x-lastX);
                float DeltaY = Math.abs(y-lastY);

                if (DeltaX<DeltaY){
                    if (DeltaX <-5){
                        Log.e(TAG, "moving to right!");
                    }
                    if (DeltaX>5){
                        Log.e(TAG, "moving to left!");
                    }
                }else {
                    if (DeltaY < -5) {
                        Log.e(TAG, "moving up!");
                    }
                    if (DeltaY > 5) {
                        Log.e(TAG, "moving to down!");
                    }
                }


            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };








}
