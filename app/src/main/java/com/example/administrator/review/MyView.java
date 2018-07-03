package com.example.administrator.review;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/7/2.
 */

public class MyView extends View implements View.OnClickListener{
    private Paint mPaint;
    private int mCount;
    private Rect mBounds;
    public MyView(Context context){
        this(context,null);
    }
    public MyView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public MyView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(20);
        canvas.drawText("小生的花伞还落在你家",0,50,mPaint);
        String text = String.valueOf(mCount);
        mPaint.getTextBounds(text,0,text.length(),mBounds);
        int textWidth = mBounds.width();
        int textHeight = mBounds.height();
        canvas.drawText(text,getWidth()/2-textWidth/2,getHeight()/2-textHeight/2,mPaint);
    }

    @Override
    public void onClick(View v) {
        mCount ++;
        invalidate();
    }
}
