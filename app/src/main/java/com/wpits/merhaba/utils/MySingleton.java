package com.wpits.merhaba.utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingleton(Context context){
        mCtx=context;
        requestQueue=getRequestQue();

    }

    public RequestQueue getRequestQue(){
        if (requestQueue == null){
            requestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context){

        if(mInstance == null){
            mInstance=new MySingleton(context);
        }
        return mInstance;

    }

    public <T> void addToRequest(Request<T> request){

        requestQueue.add(request);
    }


}
