package com.globanese.is.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by walaa-p on 29/05/2016.
 */
public class PostObject  {



    private  String name;

    private int id;
    private int posttype;
    private String text;
    private String video;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String user_share_from;
    private String getCreated_at;
    private String location;
    private String created_at;
    private String updated_at;
    private String photo;

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    private int user_id;
    private int group_id;
    private String post_id;
    private  int photocount;
    private int privacy;
    private int album_id;
    private int event_type_id;
    String type;
    private ArrayList<AddFriendObject> friends=new ArrayList<>();
    private ArrayList<String> photo_araay=new ArrayList<>();

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public ArrayList<String> getPhoto_araay() {
        return photo_araay;
    }

    public void setPhoto_araay(ArrayList<String> photo_araay) {
        this.photo_araay = photo_araay;
    }

    public int getPhotocount() {
        return photocount;
    }

    public void setPhotocount(int photocount) {
        this.photocount = photocount;
    }





    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getEvent_type_id() {
        return event_type_id;
    }

    public void setEvent_type_id(int event_type_id) {
        this.event_type_id = event_type_id;
    }

    public int getRecord_type_id() {
        return record_type_id;
    }

    public void setRecord_type_id(int record_type_id) {
        this.record_type_id = record_type_id;
    }

    private int record_type_id ;



    public ArrayList<AddFriendObject> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<AddFriendObject> friends) {
        this.friends = friends;
    }





    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser_share_from() {
        return user_share_from;
    }

    public void setUser_share_from(String user_share_from) {
        this.user_share_from = user_share_from;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getGetCreated_at() {
        return getCreated_at;
    }

    public void setGetCreated_at(String getCreated_at) {
        this.getCreated_at = getCreated_at;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }



    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosttype() {
        return posttype;
    }

    public void setPosttype(int posttype) {
        this.posttype = posttype;
    }




    public PostObject() {
    }







}
