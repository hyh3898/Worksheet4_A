package com.cs591.tianyang.worksheet4_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {
    private
    ImageView ImageMove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //register ImageView
        ImageMove = (ImageView) findViewById(R.id.ImageMove);

    }
}
