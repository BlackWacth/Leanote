package com.bruce.leanote.entity;

/**
 * 用户信息
 * Created by Bruce on 2017/4/5.
 */
public class UserInfo {
    /**
     * UserId : 585f767dab64417326002873
     * Username : bruce-hua
     * Email : huazhongwei@yeah.net
     * Verified : true
     * Logo : https://leanote.com/public/upload/448/585f767dab64417326002873/images/logo/3705bb14ce9037e1c5c9a7cb7675c832.png
     */

    private String UserId;
    private String Username;
    private String Email;
    private boolean Verified;
    private String Logo;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public boolean isVerified() {
        return Verified;
    }

    public void setVerified(boolean Verified) {
        this.Verified = Verified;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "UserId='" + UserId + '\'' +
                ", Username='" + Username + '\'' +
                ", Email='" + Email + '\'' +
                ", Verified=" + Verified +
                ", Logo='" + Logo + '\'' +
                '}';
    }
}
