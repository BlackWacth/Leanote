package com.bruce.leanote.net;

import android.content.Context;

import com.bruce.leanote.ui.widgets.LoadingDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 减少不必要的Observer方法
 * Created by Bruce on 2017/4/6.
 */
public abstract class ObserverAdapter<T> implements Observer<T> {

    private LoadingDialog mLoadingDialog;

    protected ObserverAdapter(Context context, boolean isShowProgress) {
        if(isShowProgress) {
            mLoadingDialog = new LoadingDialog(context);
            mLoadingDialog.show();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        hideProgressBar();
        if(e instanceof FailedResultException) {
            requestFailed(e.getMessage());


        }
    }

    @Override
    public void onComplete() {
        hideProgressBar();
    }

    public void requestFailed(String msg) {

    }

    private void hideProgressBar() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
