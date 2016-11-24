package com.jiyang.rrvi.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by jy on 2016/11/24.
 * get bitmap REST API (RxJava form)
 */

public interface GetBitmap {
    @GET
    Observable<ResponseBody> getPicFromNet(@Url String url);
}
