package com.jiyang.rrvi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jy on 2016/11/24.
 * Retrofit Single
 */
public class RetrofitManager {
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/news/";
    private Retrofit retrofit;
    private static RetrofitManager ourInstance = new RetrofitManager();

    public static RetrofitManager getInstance() {
        return ourInstance;
    }

    private RetrofitManager() {
    }

    /**
     * get Retrofit single
     *
     * @return Retrofit single
     */
    public Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
