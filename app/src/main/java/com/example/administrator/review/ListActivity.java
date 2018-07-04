package com.example.administrator.review;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListActivity extends AppCompatActivity {
    private ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        myListView = (ListView)findViewById(R.id.myListView);
//        ImageAdapter adapter = new ImageAdapter(ListActivity.this,0,Images.imageUrls);
        Image2Adapter adapter = new Image2Adapter(ListActivity.this,0,Images.imageUrls);
        myListView.setAdapter(adapter);
    }


    public class ImageAdapter extends ArrayAdapter<String> {
        private Context mContext;
        private LruCache<String, BitmapDrawable> mMemoryCache;
        private ListView mListView;

        public ImageAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            mContext = context;
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
                @Override
                protected int sizeOf(String key, BitmapDrawable value) {
                    return value.getBitmap().getByteCount();
                }
            };
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(mListView == null)mListView = (ListView) parent;
            String url = getItem(position);
            View view = null;
            if (convertView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.image_item, null);
            } else {
                view = convertView;
            }
            ImageView picIv = (ImageView) view.findViewById(R.id.picIv);
            picIv.setTag(url);
            BitmapDrawable drawable = getBitmapFromMemoryCache(url);
            if (drawable == null) {
                BitmapTask task = new BitmapTask();
                task.execute(url);
            } else {
                picIv.setImageDrawable(drawable);
            }
            return view;

        }

        public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
            if (getBitmapFromMemoryCache(key) == null) {
                mMemoryCache.put(key, drawable);
            }
        }

        public BitmapDrawable getBitmapFromMemoryCache(String key) {
            return mMemoryCache.get(key);
        }


        public class BitmapTask extends AsyncTask<String, Integer, BitmapDrawable> {
//            private ImageView imageView;
//
//            public BitmapTask(ImageView imageView) {
//                this.imageView = imageView;
//            }

            private String imageUrl;

            @Override
            protected BitmapDrawable doInBackground(String... strings) {
                imageUrl = strings[0];
                Bitmap bitmap = null;
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(imageUrl);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(5000);
                    bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                addBitmapToMemoryCache(imageUrl,bitmapDrawable);
                return bitmapDrawable;
            }

            @Override
            protected void onPostExecute(BitmapDrawable drawable) {
                ImageView imageView = mListView.findViewWithTag(imageUrl);
                if (imageView != null && drawable != null) {
                    imageView.setImageDrawable(drawable);
                }
            }
        }
    }


    public class Image2Adapter extends ArrayAdapter<String>{
        private Context mContext;
        private LruCache<String,BitmapDrawable> mLruCache;
        private ListView mListView;
        private Bitmap tempBitmap;
        public Image2Adapter(Context context,int resource,String[] objects){
            super(context,resource,objects);
            mContext = context;
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 8;
            mLruCache = new LruCache<String,BitmapDrawable>(cacheSize){
                @Override
                protected int sizeOf(String key, BitmapDrawable value) {
                    return value.getBitmap().getByteCount();
                }
            };
            tempBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String imageUrl = getItem(position);
            if(mListView == null) mListView = (ListView)parent;
            View view = null;
            if(convertView == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.image_item,null);
            }else{
                view = convertView;
            }
            ImageView picIv = (ImageView) view.findViewById(R.id.picIv);
            BitmapDrawable drawable = getBitmapFromMemoryCache(imageUrl);
            if(drawable != null){
                picIv.setImageDrawable(drawable);
            }else{
                if(cancelPotentialWork(imageUrl,picIv)){
                    BitmapTask bitmapTask = new BitmapTask(picIv);
                    AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(),tempBitmap,bitmapTask);
                    picIv.setImageDrawable(asyncDrawable);
                    bitmapTask.execute(imageUrl);
                }
            }
            return view;
        }

        public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
            if (getBitmapFromMemoryCache(key) == null) {
                mLruCache.put(key, drawable);
            }
        }

        public BitmapDrawable getBitmapFromMemoryCache(String key) {
            return mLruCache.get(key);
        }

        class AsyncDrawable extends BitmapDrawable{
            private WeakReference<BitmapTask> bitmapTaskPreference;
            public AsyncDrawable(Resources resources, Bitmap bitmap, BitmapTask bitmapTask){
                super(resources,bitmap);
                bitmapTaskPreference = new WeakReference(bitmapTask);
            }

            public BitmapTask getBitmapTask() {
                return bitmapTaskPreference.get();
            }
        }

        class BitmapTask extends AsyncTask<String,Integer,BitmapDrawable>{
            private String imageUrl;
            private WeakReference<ImageView> imageViewReference;
            public BitmapTask(ImageView imageView){
                this.imageViewReference = new WeakReference<ImageView>(imageView);
            }
            @Override
            protected BitmapDrawable doInBackground(String... strings) {
                imageUrl = strings[0];
                HttpURLConnection httpURLConnection = null;
                Bitmap bitmap = null;
                try {
                    URL url = new URL(imageUrl);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(10000);
                    bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(httpURLConnection != null){
                        httpURLConnection.disconnect();
                    }
                }
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
                addBitmapToMemoryCache(imageUrl,bitmapDrawable);
                return bitmapDrawable;
            }

            @Override
            protected void onPostExecute(BitmapDrawable drawable) {
                ImageView imageView = getAttachedImageView();
               if(imageView != null && drawable != null){
                   imageView.setImageDrawable(drawable);
               }
            }
            private ImageView getAttachedImageView(){
                ImageView imageView = imageViewReference.get();
                BitmapTask bitmapTask = getBitmapTask(imageView);
                if(bitmapTask == this){
                    return imageView;
                }
                return null;
            }

        }

        private BitmapTask getBitmapTask(ImageView imageView){
            if(imageView != null){
                Drawable drawable = imageView.getDrawable();
                if(drawable instanceof AsyncDrawable){
                    AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                    return asyncDrawable.getBitmapTask();
                }
            }
            return null;
        }

        private boolean cancelPotentialWork(String url,ImageView imageView){
            BitmapTask bitmapTask = getBitmapTask(imageView);
            if(bitmapTask != null){
                String imageUrl = bitmapTask.imageUrl;
                if(imageUrl == null || !imageUrl.equals(url)){
                    bitmapTask.cancel(true);
                }else {
                    return false;
                }
            }
            return true;
        }
    }


    public class Image3Adapter extends ArrayAdapter<String>{
        ImageLoader imageLoader;
        LruCache<String,Bitmap> mLruCache;
        public Image3Adapter(Context context,int resource,String[] objects){
            super(context,resource,objects);
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory /8;
            mLruCache = new LruCache<String, Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
                @Override
                public Bitmap getBitmap(String url) {
                    return mLruCache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    mLruCache.put(url,bitmap);
                }
            });
        }
    }


}
