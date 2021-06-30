package com.example.final_wallpaper.grid_img;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    Context context;

    private VolleySingleton(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    static VolleySingleton INSTANCE;

    public static synchronized VolleySingleton getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new VolleySingleton(context);
        }
        return INSTANCE;
    }

    RequestQueue requestQueue;

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
