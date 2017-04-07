package com.bruce.leanote.net;

/**
 * 请求失败结果处理标记
 * Created by Bruce on 2017/4/6.
 */
public class FailedResultException extends Exception {

    public FailedResultException() {
    }

    public FailedResultException(String message) {
        super(message);
    }

    public FailedResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedResultException(Throwable cause) {
        super(cause);
    }

    public FailedResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
