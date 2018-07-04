package com.example.administrator.review;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

public class FragmentDemoActivity extends AppCompatActivity implements TwoFragment.FTwoBtnClickListener{
    private FrameLayout containerFragment;
    private OneFragment oneFragment;
    private TwoFragment twoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_demo);
        containerFragment = (FrameLayout)findViewById(R.id.containerFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        if(dm.widthPixels > dm.heightPixels){
//            oneFragment = new OneFragment();
//            // transaction.add(R.id.containerFragment,oneFragment,"ONE");
//            //用add在屏幕旋转的时候会有fragment重叠问题，改成replace后没有重叠问题了
//            transaction.replace(R.id.containerFragment,oneFragment,"ONE");
//        }else{
//            twoFragment = new TwoFragment();
//            transaction.replace(R.id.containerFragment,twoFragment,"TWO");
//        }
        //第二种解决屏幕旋转fragment重叠问题：
        if(savedInstanceState == null) {
            if (dm.widthPixels > dm.heightPixels) {//横屏显示的Fragment
                oneFragment = new OneFragment();
                transaction.add(R.id.containerFragment, oneFragment, "ONE");
            } else { //竖屏显示的Fragment
                twoFragment = new TwoFragment();
                transaction.add(R.id.containerFragment, twoFragment, "TWO");
            }
            transaction.commit();
        }

        MyDialogFragment myDialogFragment = new MyDialogFragment();

        //一个对话框在大屏幕上以对话框的形式展示，而小屏幕上则直接嵌入当前的Actvity中
        //DialogFragment做屏幕适配时只能重写onCreateView，重写onCreateDialog时对话框显示不出来，不做屏幕适配的话重写onCreateView和onCreateDialog其中一个就可以
        boolean isLargeLayout = getResources().getBoolean(R.bool.large_layout);
        Log.i("wanlijun","isLargeLayout="+isLargeLayout);
        if(isLargeLayout){
            myDialogFragment.show(getSupportFragmentManager(),"DialogFragment");
        }else{
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.containerFragment,myDialogFragment);
            transaction.commit();
        }

    }

    @Override
    public void onFTwoBtnClick() {
        if(oneFragment == null){
            oneFragment = new OneFragment();
            oneFragment.setFOneBtnClickListener(new OneFragment.FOneBtnClickListener() {
                @Override
                public void onFOneBtnClick() {
                    if(twoFragment == null){
                        twoFragment = new TwoFragment();
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.hide(oneFragment);
//                    transaction.add(R.id.containerFragment,twoFragment,"TWO");
                    transaction.replace(R.id.containerFragment,twoFragment,"TWO");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerFragment,oneFragment,"ONE");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
