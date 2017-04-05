package com.bruce.leanote.entity;

/**
 * 通常指错误结果
 * Created by Bruce on 2017/3/30.
 */
public class Result {

    /**
     * Ok : false
     * Msg :
     */

    private boolean Ok;
    private String Msg;

    public boolean isOk() {
        return Ok;
    }

    public void setOk(boolean Ok) {
        this.Ok = Ok;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "Ok=" + Ok +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}
