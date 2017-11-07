package com.zkyr.customviewdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zkyr.customviewdemo.R;

import static android.R.attr.bitmap;

/**
 * Created by xiaohua on 2017/11/7.
 */

public class CustomImageView extends View {

    private Paint mPaint;
    private Rect mBound;//测量text文本边界
    private Rect rect;//矩形框

    private Bitmap mImage;
    private String textStr;
    private int textColor;
    private int textSize;
    private int mImageScale;

    private static final int IMAGE_SCALE_FITXY = 0;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImageView_src://图片资源
                    mImage = BitmapFactory.decodeResource(getResources(), ta.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType://缩放类型
                    mImageScale = ta.getInt(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleText://文本
                    textStr = ta.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor://文本颜色
                    textColor = ta.getColor(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleTextSize://文本字体大小
                    textSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                            , 16, getResources().getDisplayMetrics()));
                    break;
            }
        }

        ta.recycle();
        mPaint = new Paint();
        mBound = new Rect();
        rect = new Rect();

        mPaint.setTextSize(textSize);
        mPaint.getTextBounds(textStr, 0, textStr.length(), mBound);
    }

    private int width;
    private int height;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (widthMode == MeasureSpec.EXACTLY) {//match_parent  具体数值
            width = widthSize;
        } else {
            int desiredImage = getPaddingLeft() + mImage.getWidth() + getPaddingRight();
            int desireText = getPaddingLeft() + getPaddingRight() + mBound.width();

            if (widthMode == MeasureSpec.AT_MOST) {//wrap_content
                int desire = Math.max(desiredImage, desireText);
                width = Math.min(widthSize, desire);//防止文本内容过多 desire超过手机屏幕
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = getPaddingBottom() + getPaddingTop() + mImage.getHeight() + mBound.height();
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //画边框
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

        //画内容*******************************

        //画文本
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);


        if (mBound.width() > (getWidth() - getPaddingLeft() - getPaddingRight())) {//当文本内容大于容器内容时,使用XXX...表示
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(textStr, paint, getWidth() - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), getHeight() - getPaddingBottom(), mPaint);
        } else {
            canvas.drawText(textStr, getWidth() / 2 - mBound.width() / 2, getHeight() - getPaddingBottom(), mPaint);
        }

        //画图片
        rect.left = getPaddingLeft();
        rect.top = getPaddingTop();
        rect.right = getWidth() - getPaddingRight();
        rect.bottom = getHeight() - getPaddingBottom() - mBound.height();

        if (mImageScale == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mImage, null, rect, mPaint);
        } else {
            rect.left = getWidth() / 2 - mImage.getWidth() / 2;
            rect.right = getWidth() / 2 + mImage.getWidth() / 2;
            rect.top = (getHeight() - mBound.height()) / 2 - mImage.getHeight() / 2;
            rect.bottom = (getHeight() - mBound.height()) / 2 + mImage.getHeight() / 2;

            canvas.drawBitmap(mImage, null, rect, mPaint);
        }

    }
}
