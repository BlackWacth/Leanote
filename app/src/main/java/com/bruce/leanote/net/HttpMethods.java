package com.bruce.leanote.net;

import android.text.TextUtils;

import com.bruce.leanote.entity.Login;
import com.bruce.leanote.entity.Notebook;
import com.bruce.leanote.entity.Result;
import com.bruce.leanote.entity.UserInfo;
import com.bruce.leanote.global.C;
import com.bruce.leanote.utils.L;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import okhttp3.Response;

/**
 * 网络工具类
 * Created by Bruce on 2017/2/10.
 */
public class HttpMethods {

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

    /**
     * 网络请求， 返回结果为具体对象
     * @param request 请求对象
     * @param observer Observer
     * @param <T> 泛型为对象
     */
    public <T> void httpRequest(final Request request, final Observer<T> observer) {
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
                                        T t = gson.fromJson(s, getParameterType(observer));
                                        L.i("t = " + t.toString());
                                        emitter.onNext(t);
                                        emitter.onComplete();
                                    }
                                } catch (JSONException e) {
                                    if("No value for Ok".equals(e.getMessage())) {
                                        T t = gson.fromJson(s, getParameterType(observer));
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

    /**
     * 网络请求， 返回结果为对象集合
     * @param request 请求对象
     * @param observer Observer
     * @param <T> 泛型为对象
     */
    private <T> void httpRequestList(final Request request, final Observer<List<T>> observer) {
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

                                JsonParser jsonParser = new JsonParser();
                                JsonElement jsonElement = jsonParser.parse(s);

                                Gson gson = new Gson();
                                if(jsonElement.isJsonArray()) {
                                    List<T> t = gson.fromJson(s, getParameterType(observer));
                                    L.i("result t = " + t.toString());
                                    emitter.onNext(t);
                                    emitter.onComplete();
                                } else if(jsonElement.isJsonObject()) {
                                    Result result = gson.fromJson(s, Result.class);
                                    emitter.onError(new FailedResultException(result.getMsg()));
                                }
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 登录
     * @param email 邮箱
     * @param pwd 密码
     * @param observer Observer
     */
    public void login(String email, String pwd, Observer<Login> observer) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("pwd", pwd);
        final String url = Url.splitJointUrl(Url.LOGIN, params);
        get(url, observer, false);
    }

    /**
     * 注销
     * @param observer Observer
     */
    public void logout(Observer<Result> observer) {
        get(Url.LOGOUT, observer, true);
    }

    /**
     * 注册
     * @param email 邮箱
     * @param pwd 密码
     * @param observer Observer
     */
    public void register(String email, String pwd, Observer<Result> observer) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("email", email);
        builder.add("pwd", pwd);
        post(Url.REGISTER, builder, observer, false);
    }

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @param observer Observer
     */
    public void getUserInfo(String userId, Observer<UserInfo> observer) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        final String url = Url.splitJointUrl(Url.USER_INFO, params);
        get(url, observer, true);
    }

    /**
     * 得到需要同步的笔记本
     * @param afterUsn 在此usn后的笔记本是需要同步的
     * @param maxEntry 最大要同步的量
     * @param observer Observer
     */
    public void getSyncNotebooks(int afterUsn, int maxEntry, Observer<List<Notebook>> observer) {
        Map<String, String> params = new HashMap<>();
        params.put("afterUsn", afterUsn+"");
        params.put("maxEntry", maxEntry+"");
        final String url = Url.splitJointUrl(Url.SYNC_NOTEBOOKS, params);
        getList(url, observer, true);
    }

    /**
     * 得到所有笔记本
     * @param observer Observer
     */
    public void getNotebooks(Observer<List<Notebook>> observer) {
        getList(Url.GET_NOTEBOOKS, observer, true);
    }

    /**
     * 添加笔记本
     * @param title 标题
     * @param parentNotebookId  父notebookId, 可空
     * @param seq 排列
     * @param observer Observer
     */
    public void addNotebook(String title, String parentNotebookId, int seq, Observer<Notebook> observer) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("title", title);
        builder.add("parentNotebookId", parentNotebookId);
        builder.add("seq", seq + "");
        post(Url.ADD_NOTEBOOK, builder, observer, true);
    }

    /**
     * 修改笔记本
     * 错误: {Ok: false, msg: ""} msg == "conflict" 表示冲突
     * @param notebookId 笔记本ID
     * @param title 标题
     * @param parentNotebookId 父notebookId
     * @param seq 排列
     * @param usn usn
     * @param observer Observer
     */
    public void updateNotebook(String notebookId, String title, String parentNotebookId, int seq, int usn, Observer<Notebook> observer) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("notebookId", notebookId);
        builder.add("title", title);
        builder.add("parentNotebookId", parentNotebookId);
        builder.add("seq", seq + "");
        builder.add("usn", usn + "");
        post(Url.UPDATE_NOTEBOOK, builder, observer, true);
    }

    /**
     * 删除笔记本
     * 错误: {Ok: false, msg: ""} msg == "conflict" 表示冲突
     * 成功: {Ok: true}
     * @param notebookId 笔记本ID
     * @param usn usn
     * @param observer Observer
     */
    public void deleteNotebook(String notebookId, int usn, Observer<Result> observer) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("notebookId", notebookId);
        builder.add("usn", usn + "");
        post(Url.DELETE_NOTEBOOK, builder, observer, true);
    }

    /**
     * GET请求， 请求为对象
     * @param url 地址
     * @param observer Observer
     * @param isAddToken 是否加token
     * @param <T> 泛型为对象
     */
    private <T> void get(String url, Observer<T> observer, boolean isAddToken) {
        if(isAddToken) {
            url = Url.splitJoinToken(url, C.TOKEN);
        }
        L.i("url = " + url);
        if(!TextUtils.isEmpty(url)) {
            Request request = new Request.Builder().url(url).get().build();
            httpRequest(request, observer);
        }
    }

    /**
     * GET请求， 请求为集合
     * @param url 地址
     * @param observer Observer
     * @param isAddToken 是否加token
     * @param <T> 泛型为对象
     */
    private <T> void getList(String url, Observer<List<T>> observer, boolean isAddToken) {
        if(isAddToken) {
            url = Url.splitJoinToken(url, C.TOKEN);
        }
        L.i("url = " + url);
        if(!TextUtils.isEmpty(url)) {
            Request request = new Request.Builder().url(url).get().build();
            httpRequestList(request, observer);
        }
    }

    /**
     * POST请求， 请求为对象
     * @param url 地址
     * @param builder 参数
     * @param observer Observer
     * @param isAddToken 是否加token
     * @param <T> 泛型为对象
     */
    private <T> void post(String url, FormBody.Builder builder, Observer<T> observer, boolean isAddToken) {
        if(TextUtils.isEmpty(url)) {
            return;
        }
        if(isAddToken) {
            builder.add("token", C.TOKEN);
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        httpRequest(request, observer);
    }

    /**
     * POST请求， 请求为集合
     * @param url 地址
     * @param builder 参数
     * @param observer Observer
     * @param isAddToken 是否加token
     * @param <T> 泛型为对象
     */
    private <T> void postList(String url, FormBody.Builder builder, Observer<List<T>> observer, boolean isAddToken) {
        if(TextUtils.isEmpty(url)) {
            return;
        }
        if(isAddToken) {
            builder.add("token", C.TOKEN);
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        httpRequestList(request, observer);
    }

    /**
     * 参数泛型类型
     * @param object 参数
     * @return 参数类型
     */
    private Type getParameterType(Object object) {
        ParameterizedType parameterizedType = (ParameterizedType) object.getClass().getGenericSuperclass();
        L.i("sub - type = " + Arrays.toString(parameterizedType.getActualTypeArguments()));
        return parameterizedType.getActualTypeArguments()[0];
    }

    private Type getMethodType(String methodName) {
        try {
            Class<?> clazz = Class.forName(getClassName(getCallerStackTraceElement()));

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if(method.getName().equals(methodName)) {

                    Type[] genericParameterTypes = method.getGenericParameterTypes();
                    L.i("genericParameterTypes : " + Arrays.toString(genericParameterTypes));
                    ParameterizedType parameterizedType = (ParameterizedType) genericParameterTypes[1];
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    L.i("genericParameterTypes : " + Arrays.toString(actualTypeArguments));
                    return actualTypeArguments[0];
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMethodName() {
        return getCallerStackTraceElement().getMethodName();
    }

    private String getClassName(StackTraceElement caller) {
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return callerClazzName;
    }

    private StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
}
