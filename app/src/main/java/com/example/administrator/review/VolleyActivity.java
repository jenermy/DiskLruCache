package com.example.administrator.review;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import org.json.JSONObject;

public class VolleyActivity extends AppCompatActivity {
    private ImageView sgImage,imageLoaderIV;
    private NetworkImageView networkIV;
    private RequestQueue requestQueue;
    String imageUrl = "https://p.nanrenwo.net/uploads/allimg/180620/8488-1P620103541.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        sgImage = (ImageView)findViewById(R.id.sgImage);
        imageLoaderIV = (ImageView)findViewById(R.id.imageLoaderIV);
        networkIV = (NetworkImageView)findViewById(R.id.networkIV);
        requestQueue = Volley.newRequestQueue(VolleyActivity.this);
        StringRequest stringRequest = new StringRequest("https://www.baidu.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("wanlijun",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("wanlijun",error.getMessage(),error);
            }
        });
        requestQueue.add(stringRequest);
        // 测试 SDK 是否正常工作的代码

//        AVObject songObject = new AVObject("Song");
//        songObject.put("name","讲真的");
//        songObject.put("lyric","明明对你念念不忘，思前想后愈发紧张，无法深藏，爱美爱过想听你讲");
//        songObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    Toast.makeText(VolleyActivity.this,"sucess",Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(VolleyActivity.this,"failure",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        songObject = new AVObject("Song");
//        songObject.put("name","全是爱");
//        songObject.put("lyric","此情不是罪过，忘情不是洒脱，最后为你撕心裂肺有什么结果");
//        songObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    Toast.makeText(VolleyActivity.this,"sucess",Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(VolleyActivity.this,"failure",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://api.k780.com:88/?app=phone.get", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("wanlijun",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("wanlijun",error.getMessage(),error);
            }
        });
        GsonRequest gsonRequest = new GsonRequest("http://api.k780.com:88/?app=phone.get", ResultBean.class, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                ResultBean bean = (ResultBean) response;
                Log.i("wanlijun",bean.getMsg());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("wanlijun",error.getMessage(),error);
            }
        });
        requestQueue.add(jsonObjectRequest);
        requestQueue.add(gsonRequest);
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                sgImage.setImageBitmap(response);
            }
        }, 0, 200, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(imageRequest);
        ImageLoader imageLoader =  new ImageLoader(requestQueue, new BitmapCache());
        boolean iscached = imageLoader.isCached(imageUrl,imageLoaderIV.getMaxWidth(),imageLoaderIV.getMaxHeight());
        Log.i("wanlijun",iscached+"=iscached");
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageLoaderIV,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background);
        imageLoader.get(imageUrl,imageListener,imageLoaderIV.getMaxWidth(),imageLoaderIV.getMaxHeight());
        networkIV.setDefaultImageResId(R.drawable.ic_launcher_background);
        networkIV.setErrorImageResId(R.drawable.ic_launcher_background);
        networkIV.setImageUrl(imageUrl,imageLoader);

    }

    public class BitmapCache implements ImageLoader.ImageCache{
        private LruCache<String,Bitmap> mLruCache;
        public BitmapCache(){
            int maxSize = 10 * 1024 * 1024;
            mLruCache = new LruCache<String, Bitmap>(maxSize){
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }
        @Override
        public Bitmap getBitmap(String url) {
            return mLruCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mLruCache.put(url,bitmap);
        }
    }
}
