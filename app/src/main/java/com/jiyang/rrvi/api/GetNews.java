package com.jiyang.rrvi.api;

import com.jiyang.rrvi.bean.Result;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by jy on 2016/11/24.
 * REST API (RxJava form)
 */

public interface GetNews {
    @GET("latest")
    Observable<Result> getNews();
}
