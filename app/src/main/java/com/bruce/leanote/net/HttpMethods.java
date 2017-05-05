package com.bruce.leanote.net;

import com.bruce.leanote.entity.Login;
import com.bruce.leanote.entity.Notebook;
import com.bruce.leanote.entity.Result;
import com.bruce.leanote.entity.UserInfo;
import com.bruce.leanote.utils.L;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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

    private <T> void httpRequest(final Request request, Observer<List<T>> observer, final Type type) {
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
                .flatMap(new Function<String, ObservableSource<List<T>>>() {
                    @Override
                    public ObservableSource<List<T>> apply(@NonNull final String s) throws Exception {
                        L.i("flatMap : " + s);
                        return Observable.create(new ObservableOnSubscribe<List<T>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<T>> emitter) throws Exception {
                                Gson gson = new Gson();
                                try {
                                    boolean ok = new JSONObject(s).getBoolean("Ok");
                                    L.i("ok = " + ok);
                                    if(!ok) {
                                        Result result = gson.fromJson(s, Result.class);
                                        emitter.onError(new FailedResultException(result.getMsg()));
                                    } else {
                                        List<T> t = gson.fromJson(s, type);
                                        L.i("t = " + t.toString());
                                        emitter.onNext(t);
                                        emitter.onComplete();
                                    }
                                } catch (JSONException e) {
                                    if("No value for Ok".equals(e.getMessage())) {
                                        List<Notebook> t = gson.fromJson(s, new TypeToken<List<Notebook>>(){}.getType());
                                        L.i("result t = " + t.toString());
//                                        emitter.onNext(t);
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
     * 得到需要同步的笔记本
     * @param afterUsn 在此usn后的笔记本是需要同步的
     * @param maxEntry 最大要同步的量
     * @param token
     * @param observer
     */
    public void getSyncNotebooks(int afterUsn, int maxEntry, String token, Observer<List<Notebook>> observer) {
        final String url = BASE_URL + "notebook/getSyncNotebooks?afterUsn="  + afterUsn + "&maxEntry=" + maxEntry + "&token=" + token;
        get(url, observer);
    }

    /**
     * 得到所有笔记本
     * @param token
     */
    public void getNotebooks(String token, Observer<List<Notebook>> observer) {
        final String url = BASE_URL + "notebook/getNotebooks?token="  + token;
        Request request = new Request.Builder().url(url).get().build();
        httpRequest(request, observer, new TypeToken<List<Notebook>>(){}.getType());
    }

    /**
     * 添加笔记本
     * @param title 标题
     * @param parentNotebookId  父notebookId, 可空
     * @param seq 排列
     * @param token
     * @param observer
     */
    public void addNotebook(String title, String parentNotebookId, int seq, String token, Observer<Notebook> observer) {
        final String url = BASE_URL + "notebook/addNotebook";
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("title", title);
        builder.add("parentNotebookId", parentNotebookId);
        builder.add("seq", seq + "");
        builder.add("token", token);
        post(url, builder.build(), observer);
    }

    /**
     * 修改笔记本
     * 错误: {Ok: false, msg: ""} msg == "conflict" 表示冲突
     * @param notebookId 笔记本ID
     * @param title 标题
     * @param parentNotebookId 父notebookId
     * @param seq 排列
     * @param usn
     * @param token
     * @param observer
     */
    public void updateNotebook(String notebookId, String title, String parentNotebookId, int seq, int usn, String token, Observer<Notebook> observer) {
        final String url = BASE_URL + "notebook/updateNotebook";
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("notebookId", notebookId);
        builder.add("title", title);
        builder.add("parentNotebookId", parentNotebookId);
        builder.add("seq", seq + "");
        builder.add("usn", usn + "");
        builder.add("token", token);
        post(url, builder.build(), observer);
    }

    /**
     * 删除笔记本
     * 错误: {Ok: false, msg: ""} msg == "conflict" 表示冲突
     * 成功: {Ok: true}
     * @param notebookId 笔记本ID
     * @param usn
     * @param token
     * @param observer
     */
    public void deleteNotebook(String notebookId, int usn, String token, Observer<Result> observer) {
        final String url = BASE_URL + "notebook/deleteNotebook ";
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("notebookId", notebookId);
        builder.add("usn", usn + "");
        builder.add("token", token);
        post(url, builder.build(), observer);
    }


    private void get(String url, Observer observer) {
        Request request = new Request.Builder().url(url).get().build();
//        httpRequest(request, observer, Result.class);
    }

    private void post(String url, RequestBody requestBody, Observer observer) {
        Request request = new Request.Builder().url(url).post(requestBody).build();
//        httpRequest(request, observer, Result.class);
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
