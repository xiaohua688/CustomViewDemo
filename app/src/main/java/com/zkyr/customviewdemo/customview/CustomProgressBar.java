package com.zkyr.customviewdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zkyr.customviewdemo.R;

import static android.R.attr.radius;

/**
 * Created by xiaohua on 2017/11/7.
 */

public class CustomProgressBar extends View {

    private Paint mPaint;
    private int firstColor;
    private int secondColor;
    private int speed;//圆环变化速度
    private int circleWidth;//圆环宽度
    private int progress;//进度

    private boolean isNext = false;//是否开启下一个

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgressBar_firstColor:
                    firstColor = typedArray.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    secondColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    circleWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20
                            , getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_speed:
                    speed = typedArray.getInteger(attr, 20);
                    break;
            }
        }

        typedArray.recycle();

        mPaint = new Paint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (progress == 360) {
                        progress = 0;
                        int tempColor = firstColor;
                        firstColor = secondColor;
                        secondColor = tempColor;
                    }
                    progress++;
                    postInvalidate();
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centre = getWidth() / 2;//圆心x坐标
        int radius = centre - circleWidth / 2;//半径

        mPaint.setStrokeWidth(circleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        RectF rect = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);//用于限定圆弧的大小界限


        mPaint.setColor(firstColor);
        canvas.drawCircle(centre, centre, radius, mPaint);
        mPaint.setColor(secondColor);
        canvas.drawArc(rect, -90, progress, false, mPaint);

    }
}
