package com.example.administrator.review;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/7/3.
 */

public class ScrollerLayout extends ViewGroup {
    private Scroller scroller;
    private int mTouchSlop;
    private float mXDown;
    private float mXMove;
    private float mXLastMove;
    private int leftBorder;
    private int rightBorder;
    public ScrollerLayout(Context context){
        this(context,null);
    }
    public ScrollerLayout(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public ScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        scroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            int childCount = getChildCount();
            for(int i=0;i<childCount;i++){
                View child = getChildAt(i);
                child.layout(i*child.getMeasuredWidth(),0,(i+1)*child.getMeasuredWidth(),child.getMeasuredHeight());
                leftBorder = getChildAt(0).getLeft();
                rightBorder = getChildAt(childCount-1).getRight();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                if(diff > mTouchSlop){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                int scrolledX = (int) (mXLastMove - mXMove);
                if(getScrollX() + scrolledX < leftBorder){
                    scrollTo(leftBorder,0);
                    return true;
                }else if(getScrollX() + getWidth() + scrolledX > rightBorder){
                    scrollTo(rightBorder - getWidth(),0);
                    return true;
                }
                scrollBy(scrolledX,0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                int target = (getScrollX() + getWidth()/2)/getWidth();
                int dx = target * getWidth() - getScrollX();
                scroller.startScroll(getScrollX(),0,dx,0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }
}
