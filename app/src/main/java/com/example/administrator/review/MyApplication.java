package com.example.administrator.review;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Administrator on 2018/6/28.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,"BW2P9wwURyYgBKKYGwjWGKJx-gzGzoHsz","7JNBRjRxdQTh8LuQtObmoCD2");
        AVOSCloud.setDebugLogEnabled(true);
    }
}
