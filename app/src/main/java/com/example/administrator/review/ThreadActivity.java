package com.example.administrator.review;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.http.Url;

public class ThreadActivity extends AppCompatActivity {

    //ThreadLocal作用域是线程，同个对象在不同线程持有不同的数据副本，在不同线程的数据存储和修改操作互不影响
    private ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();
    //HandlerThread创建子线程的时候顺便调用了Looper.prepare()和Looper.loop(),给子线程创建了Looper和MessageQueue对象
    private HandlerThread handlerThread = new HandlerThread("moezu");
    private boolean dura;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i("wanlijun","main:threadlocal="+threadLocal.get()+":dura="+dura);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        threadLocal.set(true);
        dura = true;
        Log.i("wanlijun","main:threadlocal="+threadLocal.get()+":dura="+dura);
        new Thread("thread1"){
            @Override
            public void run() {
//                threadLocal.set(false);
//                dura = false;
                Log.i("wanlijun","thread1:threadlocal="+threadLocal.get()+":dura="+dura);

            }
        }.start();
        new Thread("thread2"){
            @Override
            public void run() {
                threadLocal.set(false);
                dura = false;
                Log.i("wanlijun","thread2:threadlocal="+threadLocal.get()+":dura="+dura);
                handler.sendEmptyMessage(1);
            }
        }.start();
        new MyAsyncTask().execute("7219aeccdef281312939e613c0ac93e6");
    }


    //使用了异步消息处理机制
    public class MyAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("wanlijun","result="+s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ByteArrayOutputStream bos = null;
                URL url = new URL("http://v.juhe.cn/historyWeather/province?key=7219aeccdef281312939e613c0ac93e6");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.setRequestProperty("Content-type","application/x-java-serialized-object");
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                String param = new String();
//                param = "key=" + strings[0];
//                outputStream.write(param.getBytes());
//                outputStream.flush();
                if(httpURLConnection.getResponseCode() == 200){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1){
                        bos.write(buffer,0,len);
                        bos.flush();
                    }
                }
                return bos.toString("utf-8");

            }catch (Exception e){
                e.printStackTrace();
                Log.e("wanlijun",e.getMessage(),e);
            }
            //只有调用了publishProgress才会调用onProgressUpdate，并且publishProgress轻松实现子线程与UI线程的切换,本质也是用了handler发送消息到消息队列，以及处理Looper从消息队列中取出来的消息
            publishProgress(10);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

    }
}
