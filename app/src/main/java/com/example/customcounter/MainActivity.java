package com.example.customcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    MyTv tv1;
    TextView tv2;
    FrameLayout container;
    private float lastY = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (FrameLayout) findViewById(R.id.container);
        tv1 = (MyTv) findViewById(R.id.tvCurCounter);

        container.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("LOG_TAG", "touch");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("LOG_TAG", "down");
                float curY;
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curY = event.getY();

                if (curY < lastY){
                    tv1.animateChange();
                }

                lastY = curY;
                return true;
        }
        return true;
    }
}
