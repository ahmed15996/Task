package com.example.aninterface.task.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class Singleton {
    private static Singleton mInstance;
    private Context context;
    private ImageLoader loader;
    private RequestQueue requestQueue;

    private Singleton(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
        loader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
    }
    public static synchronized Singleton getInstance(Context context){
        return mInstance == null ? mInstance = new Singleton(context) : mInstance;
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
    public <T>void addToRequestQueue(Request<T> request, String tag){
        request.setTag(tag);
        getRequestQueue().add(request);
    }
    public ImageLoader getImageLoader(){
        return loader;
    }
    public void cancelRequest(String tag){
        if (requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }
}
