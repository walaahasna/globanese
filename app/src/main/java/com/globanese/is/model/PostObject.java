package com.globanese.is.model;

import java.util.ArrayList;

/**
 * Created by walaa-p on 29/05/2016.
 */
public class PostObject {
String timeline_post_id;
    String sharecount;
    private String name;

    public String getTimeline_post_id() {
        return timeline_post_id;
    }

    public void setTimeline_post_id(String timeline_post_id) {
        this.timeline_post_id = timeline_post_id;
    }

    private String location;
    private String created_at;
    private String text;
    private String video;
    private String user_share_from;
    private String getCreated_at;
    private String updated_at;
    private String photo;
    private int user_id;
    private int group_id;
    private int photocount;
    private int privacy;
    private int album_id;
    private String event_type_id;
    String likecount;
    String commentcount;
  int textcolor;
    int imageresource;
    String like_count;
    String smile_count;
    String love_count;

    public String getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    String cover_photo;

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

    public String getWow_count() {
        return wow_count;
    }

    public void setWow_count(String wow_count) {
        this.wow_count = wow_count;
    }

    public String getAngry_count() {
        return angry_count;
    }

    public void setAngry_count(String angry_count) {
        this.angry_count = angry_count;
    }

    String angry_count;
    String wow_count;

    public int getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(int textcolor) {
        this.textcolor = textcolor;
    }

    public int getImageresource() {
        return imageresource;
    }

    public void setImageresource(int imageresource) {
        this.imageresource = imageresource;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public String getSharecount() {
        return sharecount;
    }

    public void setSharecount(String sharecount) {
        this.sharecount = sharecount;
    }



    public String getShare_type() {
        return share_type;
    }

    public void setShare_type(String share_type) {
        this.share_type = share_type;
    }

    String type;
    private int id;
    private int posttype;
    private String sharename;
    private String sharelocation;
    private String sharecreated_at;
    private String sharetext;
    private String sharevideo;
private  String share_type;
    public int getSharephotocount() {
        return sharephotocount;
    }

    public void setSharephotocount(int sharephotocount) {
        this.sharephotocount = sharephotocount;
    }

    private int sharephotocount;

    public ArrayList<String> getShare_photo_araay() {
        return share_photo_araay;
    }

    public void setShare_photo_araay(ArrayList<String> share_photo_araay) {
        this.share_photo_araay = share_photo_araay;
    }

    private String sharephoto;
    private  String name_of_user_ishare_from;
    private ArrayList<String> photo_araay = new ArrayList<>();

    private ArrayList<String>share_photo_araay = new ArrayList<>();



    public String getSharelocation() {
        return sharelocation;
    }

    public void setSharelocation(String sharelocation) {
        this.sharelocation = sharelocation;
    }

    public String getSharecreated_at() {
        return sharecreated_at;
    }

    public void setSharecreated_at(String sharecreated_at) {
        this.sharecreated_at = sharecreated_at;
    }

    public String getSharename() {
        return sharename;
    }

    public void setSharename(String sharename) {
        this.sharename = sharename;
    }

    public String getSharetext() {
        return sharetext;
    }

    public void setSharetext(String sharetext) {
        this.sharetext = sharetext;
    }

    public String getSharevideo() {
        return sharevideo;
    }

    public void setSharevideo(String sharevideo) {
        this.sharevideo = sharevideo;
    }



    public String getName_of_user_ishare_from() {
        return name_of_user_ishare_from;
    }

    public void setName_of_user_ishare_from(String name_of_user_ishare_from) {
        this.name_of_user_ishare_from = name_of_user_ishare_from;
    }

    public String getSharephoto() {
        return sharephoto;
    }

    public void setSharephoto(String sharephoto) {
        this.sharephoto = sharephoto;
    }



    public boolean is_like_user_post() {
        return is_like_user_post;
    }

    public void setIs_like_user_post(boolean is_like_user_post) {
        this.is_like_user_post = is_like_user_post;
    }

    private boolean is_like_user_post;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEvent_type_id() {
        return event_type_id;
    }

    public void setEvent_type_id(String event_type_id) {
        this.event_type_id = event_type_id;
    }


    private ArrayList<AddFriendObject> friends = new ArrayList<>();

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


    public int getRecord_type_id() {
        return record_type_id;
    }

    public void setRecord_type_id(int record_type_id) {
        this.record_type_id = record_type_id;
    }

    private int record_type_id;


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
