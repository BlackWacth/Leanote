package com.bruce.leanote.net;

import com.bruce.leanote.model.Login;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Bruce on 2017/2/10.
 */
public class HttpMethods {

    private static final String BASE_URL = "https://leanote.com/api/";
    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit mRetrofit;
    private APIs mAPIs;
    private HttpMethods() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mAPIs = mRetrofit.create(APIs.class);
    }

    private final static class HttpMethodsHelper {
        public static final HttpMethods HTTP_METHODS = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return HttpMethodsHelper.HTTP_METHODS;
    }

    public void login(String email, String pwd, Observer<Login> observer) {
        mAPIs.login(email, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
