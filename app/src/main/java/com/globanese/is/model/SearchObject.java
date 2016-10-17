package com.globanese.is.model;

/**
 * Created by walaa on 26/09/16.
 */
public class SearchObject {
    String prfile_name;
    String profile_pic;
    String friend_status;

    public String getCover_photo() {
        return Cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        Cover_photo = cover_photo;
    }

    String Cover_photo;
    public String getFriend_status() {
        return friend_status;
    }

    public void setFriend_status(String friend_status) {
        this.friend_status = friend_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getPrfile_name() {
        return prfile_name;
    }

    public void setPrfile_name(String prfile_name) {
        this.prfile_name = prfile_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getProfile_country() {
        return profile_country;
    }

    public void setProfile_country(String profile_country) {
        this.profile_country = profile_country;
    }

    String profile_country;

    public SearchObject() {
    }
}
