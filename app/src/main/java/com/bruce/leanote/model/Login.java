package com.bruce.leanote.model;

/**
 * 登录信息
 * Created by Bruce on 2017/2/10.
 */
public class Login {


    /**
     * Ok : true
     * Msg : 用户名或密码有误
     * Token : 589d7ed9ab6441287f002e8c
     * UserId : 585f767dab64417326002873
     * Email : huazhongwei@yeah.net
     * Username : bruce-hua
     */

    private boolean Ok;
    private String Msg;
    private String Token;
    private String UserId;
    private String Email;
    private String Username;

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

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    @Override
    public String toString() {
        return "Login{" +
                "Ok=" + Ok +
                ", Msg='" + Msg + '\'' +
                ", Token='" + Token + '\'' +
                ", UserId='" + UserId + '\'' +
                ", Email='" + Email + '\'' +
                ", Username='" + Username + '\'' +
                '}';
    }
}
