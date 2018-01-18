package com.dempseywood.operatordatacollector.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jason.Liu on 18/01/2018.
 */

public class RequestService {
    private static RequestService mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private RequestService(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static RequestService getInstance(Context context) {
        if(mInstance == null) {
            synchronized(RequestService.class) {
                if(mInstance == null) {
                    mInstance = new RequestService(context);
                }
            }
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }




}
