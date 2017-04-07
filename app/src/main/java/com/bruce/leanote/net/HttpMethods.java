package com.bruce.leanote.net;

import com.bruce.leanote.entity.Account;
import com.bruce.leanote.entity.Login;
import com.bruce.leanote.entity.Result;
import com.bruce.leanote.entity.UserInfo;
import com.bruce.leanote.global.C;
import com.bruce.leanote.utils.L;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络工具类
 * Created by Bruce on 2017/2/10.
 */
public class HttpMethods {

    private static final String BASE_URL = "https://leanote.com/api/";
    private static final int DEFAULT_TIMEOUT = 10;

    private OkHttpClient mOkHttpClient;

    private HttpMethods() {
        mOkHttpClient = new OkHttpClient();
    }

    private final static class HttpMethodsHelper {
        static final HttpMethods HTTP_METHODS = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return HttpMethodsHelper.HTTP_METHODS;
    }

    private <T> void httpRequest(final Request request, Observer<T> observer, final Class<T> clazz) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                final Call call = mOkHttpClient.newCall(request);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        call.cancel();
                    }
                });
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful())
                        emitter.onNext(response.body().string());
                        emitter.onComplete();
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull final String s) throws Exception {
                        L.i("flatMap : " + s);
                        return Observable.create(new ObservableOnSubscribe<T>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {
                                Gson gson = new Gson();
                                try {
                                    boolean ok = new JSONObject(s).getBoolean("Ok");
                                    L.i("ok = " + ok);
                                    if(!ok) {
                                        Result result = gson.fromJson(s, Result.class);
                                        emitter.onError(new FailedResultException(result.getMsg()));
                                    } else {
                                        T t = gson.fromJson(s, clazz);
                                        L.i("t = " + t.toString());
                                        emitter.onNext(t);
                                        emitter.onComplete();
                                    }
                                } catch (JSONException e) {
                                    if("No value for Ok".equals(e.getMessage())) {
                                        T t = gson.fromJson(s, clazz);
                                        L.i("t = " + t.toString());
                                        emitter.onNext(t);
                                        emitter.onComplete();
                                    }
                                    L.i("JSONException = " + e.getMessage());
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                    L.i("JsonSyntaxException = " + e.getMessage());
                                }
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void login(String email, String pwd, Observer<Login> observer) {
        final String loginUrl = BASE_URL + "auth/login?email=" + email + "&pwd=" + pwd;
        Request request = new Request.Builder().url(loginUrl).get().build();
        httpRequest(request, observer, Login.class);
    }

    public void getUserInfo(String userId, String token, Observer<UserInfo> observer) {
        final String url = BASE_URL + "user/info?userId=" + userId + "&token=" + token;
        Request request = new Request.Builder().url(url).get().build();
        httpRequest(request, observer, UserInfo.class);
    }

    public void logout(String token, Observer<Result> observer) {
        final String url = BASE_URL + "auth/logout?token="  + token;
        Request request = new Request.Builder().url(url).get().build();
        httpRequest(request, observer, Result.class);
    }

    public void register(String email, String pwd, Observer<Result> observer) {
        final String loginUrl = BASE_URL + "auth/register";
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("email", email);
        builder.add("pwd", pwd);
        Request request = new Request.Builder().url(loginUrl).post(builder.build()).build();
        httpRequest(request, observer, Result.class);
    }

    /**
     * 获取泛型方法中泛型类型
     * @return 泛型类型
     */
    private Type getMethodGenericityType() {
        Method method = null;
        try {
            method =HttpMethods.class.getMethod("httpRequest", Request.class, Observer.class);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (method != null) {
            Type[] types = method.getGenericParameterTypes();
            ParameterizedType ParameterizedType = (ParameterizedType) types[1];
            return ParameterizedType.getActualTypeArguments()[0];
        } else {
            return null;
        }
    }
}
