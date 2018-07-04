package com.example.administrator.review;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private DiskLruCache mDiskLruCache = null;
    private ImageView image;
    private OutputStream out;
    HttpURLConnection httpURLConnection;
    BufferedInputStream inputStream;
    BufferedOutputStream outputStream;
    String imageUrl = "https://p.nanrenwo.net/uploads/allimg/180620/8488-1P620103541.jpg";
    private Button threadBtn,viewBtn,view2Btn,fragmentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView)findViewById(R.id.image);
        threadBtn = (Button)findViewById(R.id.threadBtn);
        viewBtn = (Button)findViewById(R.id.viewBtn);
        view2Btn = (Button)findViewById(R.id.view2Btn);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VolleyActivity.class));
            }
        });
        threadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ThreadActivity.class));
            }
        });
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ViewActivity.class));
            }
        });
        view2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,View2Activity.class));
            }
        });
        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        });
        findViewById(R.id.fragmentBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FragmentDemoActivity.class));
            }
        });
        try {
            File cacheDir = Utils.getDiskCacheDir(getApplicationContext(),"bitmap");
            if(!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir,Utils.getAppVersion(getApplicationContext()),1,10*1024*1024);
            String key = Utils.hashkeyForDisk(imageUrl);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if(snapshot != null){
                InputStream inputStream1 = snapshot.getInputStream(0);
                Bitmap  bitmap = BitmapFactory.decodeStream(inputStream1);
                image.setImageBitmap(bitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String key = Utils.hashkeyForDisk(imageUrl);
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    out = editor.newOutputStream(0);
                   if(downloadPic(out)){
                       editor.commit();
                   }else{
                       editor.abort();
                   }
//                   mDiskLruCache.flush(); //每次调用会增加同步日志文件的时间
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean downloadPic(OutputStream out){
        try {
            URL url = new URL(imageUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream(),8*1024);
            outputStream = new BufferedOutputStream(out,8 * 1024);
            int b;
            while ((b = inputStream.read()) != -1){
                out.write(b);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mDiskLruCache.flush(); //将内存中的操作记录同步到日志文件（journal文件）
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mDiskLruCache.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
