package com.globanese.is.model;


import io.realm.RealmObject;

/**
 * Created by Hamdy on 11/5/2016.
 */
public class LogInUser extends RealmObject {

    public static final int LOGIN_TYPE_EMAIL=1;
    public static final int LOGIN_TYPE_FACEBOOK=2;
    public static final int LOGIN_TYPE_GOOGLE=3;
    public static final int LOGIN_TYPE_LINKEDIN=4;

    private String email;
    private String password;
    private int login_type;
    private String fb_token;
    private String g_token;
    private String ln_token;

    private String access_token;
    private String refresh_token;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public String getFb_token() {
        return fb_token;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }

    public String getG_token() {
        return g_token;
    }

    public void setG_token(String g_token) {
        this.g_token = g_token;
    }

    public String getLn_token() {
        return ln_token;
    }

    public void setLn_token(String ln_token) {
        this.ln_token = ln_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
