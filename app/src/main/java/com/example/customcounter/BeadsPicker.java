package com.example.customcounter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Ubrainians</u>
 */
public class BeadsPicker extends NumberPicker implements View.OnClickListener,View.OnTouchListener {
    private Paint mPaint1;
    private Paint mPaint2;
    private float lastY = 0f;

    public BeadsPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint1 = new Paint();
        mPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint1.setStrokeWidth(1);
        mPaint1.setColor(ResourcesCompat.getColor(context.getResources(),
                R.color.counter_center_element, null));

        mPaint2 = new Paint();
        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint2.setStrokeWidth(1);
        mPaint2.setColor(ResourcesCompat.getColor(context.getResources(),
                R.color.counter_outside_center_element, null));
        setOnTouchListener(this);
        setDividerColor(ResourcesCompat.getColor(context.getResources(),
                R.color.transparent, null));
        setSelectionDividerDistance(60);
        setOnClickListener(this);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setFadingEdgeLength(260);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_SP,70);
            ((EditText) view).bringToFront();
            ((EditText) view).setTextColor(Color.WHITE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int topY = getTopDividerCoords();
        int bottomY = getBottomDividerCoords();
        canvas.drawRect(getLeft(), topY, getRight(), bottomY, mPaint1);
        super.onDraw(canvas);
        canvas.drawRect(getLeft(), getTop(), getRight(), topY, mPaint2);
    }

    public void setDividerColor(int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(this, colorDrawable);
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private int getTopDividerCoords() {
        Class c = getClass();
        try {
            Field field = NumberPicker.class.getDeclaredField("mTopSelectionDividerTop");
            field.setAccessible(true);
            return (int) field.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getBottomDividerCoords() {
        Class c = getClass();
        try {
            Field field = NumberPicker.class.getDeclaredField("mBottomSelectionDividerBottom");
            field.setAccessible(true);
            return (int) field.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float curY;
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curY = event.getY();
                if (curY > lastY) {
                    return true;
                }
                lastY = curY;
                return false;
        }
        return false;
    }

   /* private EditText getCenterEditText(){
        Class c = getClass();
        try {
            Field field = NumberPicker.class.getDeclaredField("mInputText");
            field.setAccessible(true);
            return (EditText) field.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    private void setSelectionDividerDistance(int distance){
        Class c = getClass();
        try {
            Field field = NumberPicker.class.getDeclaredField("mSelectionDividersDistance");
            field.setAccessible(true);
            field.set(this,(int)convertDpToPixel(distance,getContext()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public void onClick(View v) {
        Log.d("LOG_TAG", "OnClick");
    }
}
