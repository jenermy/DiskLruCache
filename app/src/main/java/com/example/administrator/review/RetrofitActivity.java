package com.example.administrator.review;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.baidu.com/").build();
        MyApi service = retrofit.create(MyApi.class);

    }
}
