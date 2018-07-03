package com.example.administrator.review;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/6/29.
 */

public class MyLayout extends ViewGroup {
    public MyLayout(Context context) {
        super(context,null);
    }
    public MyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }
    public MyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildCount() > 0){
            View child = getChildAt(0);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(getChildCount() > 0){
            View child = getChildAt(0);
            child.layout(0,0,child.getMeasuredWidth(),child.getMeasuredHeight());
        }
    }

}
