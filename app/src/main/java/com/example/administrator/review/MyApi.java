package com.example.administrator.review;

import java.util.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2018/6/27.
 */

public interface MyApi {
    @FormUrlEncoded
    @POST("appconfig")
    Call<ResponseBody> getA(@Part("mobile") String mobile);
}
