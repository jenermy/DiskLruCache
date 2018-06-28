package com.example.administrator.review;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2018/6/28.
 */

public class GsonRequest extends Request {
    private Response.Listener mListener;
    private Gson mGson;
    private Class mClass;
    public GsonRequest(int method, String url, Class clazz,Response.Listener listener, Response.ErrorListener errorListener){
        super(method,url,errorListener);
        mListener = listener;
        mClass = clazz;
        mGson = new Gson();
    }
    public GsonRequest(String url, Class clazz,Response.Listener listener, Response.ErrorListener errorListener){
        this(Method.GET,url,clazz,listener,errorListener);
    }

    @Override
    protected void deliverResponse(Object response) {
        if(mListener != null){
            mListener.onResponse(response);
        }
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String gsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(gsonStr,mClass),HttpHeaderParser.parseCacheHeaders(response));
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}
