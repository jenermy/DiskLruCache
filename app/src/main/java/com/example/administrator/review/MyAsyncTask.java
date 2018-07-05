package com.example.administrator.review;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/7/5.
 */

public class MyAsyncTask extends AsyncTask<String,Integer,Bitmap> {
    private RotateScreenActivity activity;
    private DialogFragment dialogFragment;
    private Bitmap mBitmap;
    private boolean isComplete = false;
    public MyAsyncTask(RotateScreenActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        dialogFragment = new DialogFragment();
        dialogFragment.show(activity.getFragmentManager(),"MyAsyncTask");
    }


    public Bitmap getmBitmap() {
        return mBitmap;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String imageUrl = strings[0];
        HttpURLConnection httpURLConnection = null;
        try {
            Thread.sleep(10000);
            URL url = new URL(imageUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(5000);
            mBitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }
        return mBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        isComplete = true;
        if(activity != null){
            activity.noyifyImage();
        }
        if(dialogFragment != null){
            dialogFragment.dismiss();
        }
    }

    public void setActivity(RotateScreenActivity activity) {
        //销毁上一个activity的加载框
        if(this.activity == null && dialogFragment != null){
            dialogFragment.dismiss();
        }
        //当前activity被销毁，需要同时销毁加载框
        if(activity == null && dialogFragment != null){
            dialogFragment.dismiss();
        }
        this.activity = activity;
        if(activity != null){
            if(isComplete){
                activity.noyifyImage();
            }else{
                dialogFragment = new DialogFragment();
                dialogFragment.show(activity.getFragmentManager(),"MyAsyncTask");
            }
        }

    }
}
