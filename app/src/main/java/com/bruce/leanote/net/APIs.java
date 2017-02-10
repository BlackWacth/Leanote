package com.bruce.leanote.net;

import com.bruce.leanote.model.Login;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Bruce on 2017/2/10.
 */
public interface APIs {

    @GET("auth/login")
    Observable<Login> login(@Query("email") String email, @Query("pwd") String pwd);

}
