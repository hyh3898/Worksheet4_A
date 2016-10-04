package com.cs591.tianyang.worksheet4_a;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;



public class Main2Activity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{
    private
    ImageView ImageMove;
    Animation moveRightAnim,rotateAnim,moveLeftAnim,moveUpAnim,moveDownAnim,shakingAnim,rotateCW5Anim,rotateCCW5Anim,rotateCW10Anim,rotateCCW10Anim;
    private String TAG = "BOSTON";
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private float lastX, lastY, lastZ;  //remeber the starting position

    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private SensorManager sensorManager = null;

    // determine if the user is shaking the phone reeeeeeally hard.
    private static int SIGNIFICANT_SHAKE = 1000;

    private GestureDetectorCompat GD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // initialize acceleration values
        acceleration = 0.00f;                                         //Initializing Acceleration data.
        currentAcceleration = SensorManager.GRAVITY_EARTH;            //We live on Earth.
        lastAcceleration = SensorManager.GRAVITY_EARTH;               //Ctrl-Click to see where else we could use our phone.
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        GD = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        GD.setOnDoubleTapListener(this);

        //register ImageView
        ImageMove = (ImageView)findViewById(R.id.ImageMove);
        //register anim
        moveRightAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_right);
        moveLeftAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_left);
        moveUpAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_up);
        moveDownAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_down);
        rotateAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        shakingAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shaking);
        rotateCW5Anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_cw_5);
        rotateCCW5Anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_ccw_5);
        rotateCCW10Anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_ccw_10);
        rotateCW10Anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_cw_10);


    }
    //here i twisted the code from professor, i went through the entire code.
    //this includes enableAccelerometerListening, disableAccelerometerListing and how we determine how the user is changing the position.
    //I twisted the algorithm inside onSensorChanged().
    @Override
    protected void onStart() {
        setContentView(R.layout.activity_main2);
        super.onStart();
        //register ImageView
        ImageMove = (ImageView)findViewById(R.id.ImageMove);

        Log.i(TAG, "onStart Triggered.");
        enableAccelerometerListening();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop Triggered.");
        disableAccelerometerListening();
        super.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.GD.onTouchEvent(event); //insert this line to consume the touch event locally by our GD,
        return super.onTouchEvent(event); //if we have a handler for the touch event we will handle before passing on.

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
            if (acceleration>10) {
                Log.e(TAG, "we are shaking!");
                ImageMove.startAnimation(shakingAnim);
            }
            else if (acceleration >2 && acceleration<10) {
                float DeltaX = Math.abs(x-lastX);
                float DeltaY = Math.abs(y-lastY);

                if (DeltaX<DeltaY){
                    if (x-lastX >1){
                        Log.e(TAG, "moving to left!");
                        ImageMove.startAnimation(moveLeftAnim);

                    }
                    else if (x-lastX<-1){
                        Log.e(TAG, "moving to right!");
                        ImageMove.startAnimation(moveRightAnim);

                    }
                }else {
                    if (y-lastY >1) {
                        Log.e(TAG, "moving up!");
                        ImageMove.startAnimation(moveUpAnim);

                    }
                    else if (y-lastY <-1) {
                        Log.e(TAG, "moving down!");
                        ImageMove.startAnimation(moveDownAnim);

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


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    Log.e(TAG, "swip right!");
                    if (velocityX>500){
                        //we should turn 10 times
                        ImageMove.startAnimation(rotateCW10Anim);
                    }else {
                        ImageMove.startAnimation(rotateCW5Anim);
                    }
                } else {
                    Log.e(TAG, "swip left!");
                    if (velocityX<-500){
                        //we should turn 10 times
                        ImageMove.startAnimation(rotateCCW10Anim);
                    }else {
                        ImageMove.startAnimation(rotateCCW5Anim);
                    }

                }
            }
        }
        return false;
    }
}
