package com.globanese.is.model;

import io.realm.RealmObject;

/**
 * Created by walaa on 28/07/16.
 */
public class UserAction extends RealmObject{

    String id;
    String likecount;
    Boolean greatelike;
    String userid;
    String like_count;
int liketype;

    public int getLiketype() {
        return liketype;
    }

    public void setLiketype(int liketype) {
        this.liketype = liketype;
    }

    String smile_count;

    public String getSmile_count() {
        return smile_count;
    }

    public void setSmile_count(String smile_count) {
        this.smile_count = smile_count;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getLove_count() {
        return love_count;
    }

    public void setLove_count(String love_count) {
        this.love_count = love_count;
    }

    public String getAngry_count() {
        return angry_count;
    }

    public void setAngry_count(String angry_count) {
        this.angry_count = angry_count;
    }

    public String getWow_count() {
        return wow_count;
    }

    public void setWow_count(String wow_count) {
        this.wow_count = wow_count;
    }

    String love_count;
    String angry_count;
    String wow_count;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Boolean getGreatelike() {
        return greatelike;
    }

    public void setGreatelike(Boolean greatelike) {
        this.greatelike = greatelike;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }
}
