package com.example.customcounter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Ubrainians</u>
 */

public class MyTv extends View {
    Paint paint;
    Paint bgPaint;

    private String textCurrent = "0000";
    private String textNext = "0001";
    private String textAfterNext = "0001";
    private int fontSize;
    private float xCurrent = 0f;
    private float yCurrent = 0f;
    private float shaderY1;
    private float shaderY2;

    Shader textShader;
    private boolean isStarted = false;

    public MyTv(Context context) {
        super(context);
    }

    public MyTv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        fontSize = convertSpToPixels(70, context);
        Log.d("LOG_TAG", "fontSize : " + fontSize);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);

        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(ContextCompat.getColor(context,R.color.counter_outside_center_element));

        final Rect textBounds = new Rect();
        paint.getTextBounds(textCurrent,0,textCurrent.length(),textBounds);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                FrameLayout.LayoutParams params =
                        new FrameLayout.LayoutParams((int)(paint.measureText(textCurrent) +
                                convertDpToPixel(32,getContext())),
                                (int) textBounds.height()*3);
                params.gravity = Gravity.CENTER_VERTICAL|Gravity.START;

                setLayoutParams(params);

                xCurrent = getLeft() + convertDpToPixel(8,getContext());
                yCurrent = getTop() + textBounds.height() + convertDpToPixel(8,getContext());//+ convertDpToPixel(70,getContext());

                shaderY1 = yCurrent + textBounds.height() + convertDpToPixel(16,getContext());
                shaderY2 = yCurrent - (textBounds.height());

                textShader = new LinearGradient(x + (paint.measureText(textCurrent) / 2f),
                        shaderY1,
                        x + (paint.measureText(textCurrent) / 2f),
                        shaderY2,
                        new int[]{Color.TRANSPARENT, Color.WHITE},
                        new float[]{0, 1}, Shader.TileMode.CLAMP);
            }
        });
    }

   /* @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        *//*x = Math.abs((getLeft() - getRight()) / 2f) - (paint.measureText(text) / 2f);
        y = Math.abs((getTop() + fontSize));*//*

        shaderY1 = y + fontSize + 25;
        shaderY2 = y - (fontSize);
        textShader = new LinearGradient(x + (paint.measureText(text) / 2f), shaderY1, x + (paint.measureText(text) / 2f), shaderY2,
                new int[]{Color.TRANSPARENT, Color.WHITE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
    }*/

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.d("LOG_TAG", "widthMeasureSpecc " + parentWidth);
        Log.d("LOG_TAG", "heightMeasureSpec= " + parentHeight);
        this.setMeasuredDimension(parentWidth/2, parentHeight);
        this.setLayoutParams(new RelativeLayout.LayoutParams((int)(parentWidth/2),parentHeight / 3));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }*/


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.YELLOW);
        canvas.drawRect(getLeft(),getTop(),getRight(),yCurrent + convertDpToPixel(8,getContext()),bgPaint);

        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        canvas.drawText(textCurrent, xCurrent, yCurrent, paint);

        paint.setShader(textShader);
        canvas.drawText(textNext, x,
                y + fontSize + 25, paint);
        canvas.drawText(textAfterNext, x, y + 2 * fontSize + 50, paint);
    }

    public void animateChange() {
        if (!isStarted) {
            isStarted = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    yCurrent -= 6;
                    if (y >= -fontSize) {
                        invalidate();
                        postDelayed(this, 1);
                    } else {
                        isStarted = false;
                    }
                }
            }, 100);
        }
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}
