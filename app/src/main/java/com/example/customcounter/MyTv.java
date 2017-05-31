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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Ubrainians</u>
 */

public class MyTv extends View implements ViewTreeObserver.OnGlobalLayoutListener {
    Paint paint;
    Paint bgPaint;
    Paint secondTextPaint;

    private String textCurrent = "0000";
    private String textNext = "1111";
    private String textAfterNext = "0001";
    private int fontSize;
    private float xCurrent = 0f;
    private float yCurrent = 0f;
    private Rect textBounds;
    Shader textShader;
    private boolean isStarted = false;

    public MyTv(Context context) {
        super(context);
    }

    public MyTv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        fontSize = convertSpToPixels(70, context);
        Log.d("LOG_TAG", "fontSize : " + fontSize);

        initPaints(context);

        textBounds = new Rect();
        paint.getTextBounds(textCurrent, 0, textCurrent.length(), textBounds);

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void initPaints(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));

        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(ContextCompat.getColor(context, R.color.counter_outside_center_element));

        secondTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        secondTextPaint.setTextSize(fontSize);
        secondTextPaint.setStyle(Paint.Style.STROKE);
        secondTextPaint.setColor(Color.WHITE);
        secondTextPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
    }

    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams((int) (textBounds.width() +
                        convertDpToPixel(24, getContext())),
                        (int) (textBounds.height() * 2 + convertDpToPixel(32, getContext())));
        setLayoutParams(params);

        xCurrent = getLeft() + convertDpToPixel(8, getContext());
        yCurrent = getTop() + textBounds.height() + convertDpToPixel(8, getContext());

        float shaderY1 = yCurrent + textBounds.height() + convertDpToPixel(16, getContext());
        float shaderY2 = yCurrent - (textBounds.height());

        textShader = new LinearGradient((int) (xCurrent + textBounds.width() / 2),
                shaderY1,
                (int) (xCurrent + textBounds.width() / 2),
                shaderY2,
                new int[]{Color.TRANSPARENT, Color.WHITE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        secondTextPaint.setShader(textShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.counter_outside_center_element));
        canvas.drawRect(getLeft(), getTop(), getRight(), yCurrent + convertDpToPixel(8, getContext()), bgPaint);
        canvas.drawText(textCurrent, xCurrent, yCurrent, paint);

        canvas.drawText(textNext, xCurrent,
                yCurrent + textBounds.height() + convertDpToPixel(16, getContext()),
                secondTextPaint);
        canvas.drawText(textAfterNext, xCurrent,
                yCurrent + 2 * (textBounds.height() + convertDpToPixel(16, getContext())),
                secondTextPaint);
    }

    public void animateChange() {
        if (!isStarted) {
            isStarted = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    yCurrent -= 6;
                    if (yCurrent >= -convertDpToPixel(8, getContext())) {
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
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
