package com.cs591.tianyang.worksheet4_a;


import android.content.Intent;
import android.widget.ImageView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    //adding this to see if my commit successed
    private GestureDetectorCompat GD;
    //register the image view
    ImageView imageView2;
    //register the rotate anim.
    Animation rotateanim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GD = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        GD.setOnDoubleTapListener(this);
        //binding the anim to the xml
        rotateanim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        //find the image:
        imageView2 = (ImageView) findViewById(R.id.imageView2);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.GD.onTouchEvent(event); //insert this line to consume the touch event locally by our GD,
        return super.onTouchEvent(event); //if we have a handler for the touch event we will handle before passing on.

    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(getApplicationContext(), "Double Tap",
                Toast.LENGTH_SHORT).show();
        imageView2.startAnimation(rotateanim);
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

        Intent i = new Intent(getApplicationContext(), Main2Activity.class );
        //               i.putExtra("secretVal", "42");  //basically just a hashmap, this bundle will be passed with intent.
        startActivity(i);
        return false;
    }
}
