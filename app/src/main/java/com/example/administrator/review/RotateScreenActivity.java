package com.example.administrator.review;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class RotateScreenActivity extends AppCompatActivity {
    private RestoreDataFragment restoreDataFragment;
    private Bitmap mBitmap;
    private DialogFragment dialogFragment;
    private ImageView fragmentSaveDataIV;
    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_screen);
        FragmentManager fm = getFragmentManager();
        restoreDataFragment = (RestoreDataFragment) fm.findFragmentByTag("data");
        if(restoreDataFragment == null){
            restoreDataFragment = new RestoreDataFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(restoreDataFragment,"data");
            transaction.commit();
        }
        fragmentSaveDataIV = (ImageView)findViewById(R.id.fragmentSaveDataIV);
//        mBitmap = restoreDataFragment.getData();
//        if(mBitmap == null){
//            dialogFragment = new DialogFragment();
//            dialogFragment.show(fm,"dialog");
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            ImageRequest imageRequest = new ImageRequest("http://p2.so.qhmsg.com/t01fe53a734803b5887.jpg", new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap response) {
//                    fragmentSaveDataIV.setImageBitmap(response);
//                    restoreDataFragment.setData(response);
//                    dialogFragment.dismiss();
//                }
//            },0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dialogFragment.dismiss();
//                }
//            });
//            requestQueue.add(imageRequest);
//        }else{
//            fragmentSaveDataIV.setImageBitmap(mBitmap);
//        }

        myAsyncTask = restoreDataFragment.getMyAsyncTask();
        if(myAsyncTask == null){
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute("http://p2.so.qhmsg.com/t01fe53a734803b5887.jpg");
            restoreDataFragment.setMyAsyncTask(myAsyncTask);
        }else{
            myAsyncTask.setActivity(this);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        myAsyncTask.setActivity(null);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void noyifyImage(){
        if(myAsyncTask != null){
            fragmentSaveDataIV.setImageBitmap(myAsyncTask.getmBitmap());
        }
    }
}
