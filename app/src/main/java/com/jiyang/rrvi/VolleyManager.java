package com.jiyang.rrvi;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jy on 2016/11/24.
 * Volley Single
 */
public class VolleyManager {
    private RequestQueue requestQueue;
    private static VolleyManager ourInstance = new VolleyManager();

    public static VolleyManager getInstance() {
        return ourInstance;
    }

    private VolleyManager() {
    }

    /**
     * get volley requestQueue
     *
     * @param context activity
     * @return volley requestQueue
     */
    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
}
