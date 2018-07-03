package com.example.administrator.review;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/7/3.
 */

public class MyListView extends ListView implements View.OnTouchListener,GestureDetector.OnGestureListener{
    private GestureDetector gestureDetector;
    private OnDeleteListener onDeleteListener;
    private View deleteBtn;
    private ViewGroup itemLayout;
    private int selectedItem;
    private boolean isDeleteShown;
    public  MyListView(Context context){
        this(context,null);
    }
    public  MyListView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public  MyListView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        gestureDetector = new GestureDetector(getContext(),this);
        setOnTouchListener(this);
    }

    public void setOnDeleteListener(OnDeleteListener listener){
        this.onDeleteListener = listener;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("wanlijun","onFling");
        if(!isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)){
            deleteBtn = LayoutInflater.from(getContext()).inflate(R.layout.add_btn_layout,null);
            deleteBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(deleteBtn);
                    deleteBtn = null;
                    isDeleteShown = false;
                    onDeleteListener.onDelete(selectedItem);
                }
            });
            itemLayout = (ViewGroup) getChildAt(selectedItem - getFirstVisiblePosition());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            itemLayout.addView(deleteBtn,params);
            isDeleteShown = true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("wanlijun","onDown");
        if(!isDeleteShown){
            selectedItem = pointToPosition((int) e.getX(),(int) e.getY());
        }
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(isDeleteShown){
            itemLayout.removeView(deleteBtn);
            deleteBtn = null;
            isDeleteShown = false;
            return false;
        }else{
            return gestureDetector.onTouchEvent(event);
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    public interface OnDeleteListener{
        void onDelete(int index);
    }
}
