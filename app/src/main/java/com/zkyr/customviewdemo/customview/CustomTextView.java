package com.zkyr.customviewdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zkyr.customviewdemo.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by xiaohua on 2017/11/6.
 */

public class CustomTextView extends View {


    private String textStr;
    private int textColor;
    private int textSize;

    private Paint mPaint;
    private Rect mBound;

    public CustomTextView(Context context) {
        this(context,null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        int indexCount=ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr=ta.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTextView_text://文字
                    textStr=ta.getString(attr);
                    break;
                case R.styleable.CustomTextView_textColor://颜色
                    textColor=ta.getColor(attr, Color.BLACK);//默认为黑色
                    break;
                case R.styleable.CustomTextView_textSize://字体大小
                    textSize=ta.getDimensionPixelSize(attr, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        ta.recycle();

        mPaint=new Paint();
        mPaint.setTextSize(textSize);

        mBound=new Rect();

        mPaint.getTextBounds(textStr,0,textStr.length(),mBound);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                textStr=getRondomText();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        mPaint.setColor(textColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(textStr,getWidth()/2,getHeight()/2+mPaint.getFontMetrics().bottom*3/2,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);

        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if(widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(textStr,0,textStr.length(),mBound);

            float textWidth=mBound.width();

            int disire=(int)(getPaddingLeft()+textWidth+getPaddingRight());//期望大小

            width=disire;
        }

        if(heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(textStr,0,textStr.length(),mBound);

            float textHeight=mBound.height();

            int disire=(int) (getPaddingTop()+textHeight+getPaddingBottom());

            height=disire;
        }

        setMeasuredDimension(width,height);
    }

    public String getRondomText(){
        Random random=new Random();
        Set<Integer> set=new HashSet<>();
        while (set.size()<4){
            int r=random.nextInt(10);
            set.add(r);
        }
        StringBuilder sb=new StringBuilder();
        for(Integer integer:set){
            sb.append(""+integer);
        }
        return sb.toString();
    }
}
