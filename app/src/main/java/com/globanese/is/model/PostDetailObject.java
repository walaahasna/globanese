package com.globanese.is.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by walaa on 04/06/16.
 */
public class PostDetailObject implements Parcelable {

    private String text;
    private String video;
    private String user_share_from;
    private String getCreated_at;
    private String location;
    private String created_at;
    private String updated_at;
    private String photo1;
    private int privacy;
    private int album_id;
    private int user_id;
    private int group_id;
    private int post_id;
    private int id;
    private int posttype;
    List<CommentObject> Comments;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getUser_share_from() {
        return user_share_from;
    }

    public void setUser_share_from(String user_share_from) {
        this.user_share_from = user_share_from;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
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

    public List<CommentObject> getComments() {
        return Comments;
    }

    public void setComments(List<CommentObject> comments) {
        Comments = comments;
    }



    public PostDetailObject() {
    }




    protected PostDetailObject(Parcel in) {
        text = in.readString();
        video = in.readString();
        user_share_from = in.readString();
        getCreated_at = in.readString();
        location = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        photo1 = in.readString();
        privacy = in.readInt();
        album_id = in.readInt();
        user_id = in.readInt();
        group_id = in.readInt();
        post_id = in.readInt();
        id = in.readInt();
        posttype = in.readInt();
        Comments = in.createTypedArrayList(CommentObject.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(video);
        dest.writeString(user_share_from);
        dest.writeString(getCreated_at);
        dest.writeString(location);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(photo1);
        dest.writeInt(privacy);
        dest.writeInt(album_id);
        dest.writeInt(user_id);
        dest.writeInt(group_id);
        dest.writeInt(post_id);
        dest.writeInt(id);
        dest.writeInt(posttype);
        dest.writeTypedList(Comments);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostDetailObject> CREATOR = new Creator<PostDetailObject>() {
        @Override
        public PostDetailObject createFromParcel(Parcel in) {
            return new PostDetailObject(in);
        }

        @Override
        public PostDetailObject[] newArray(int size) {
            return new PostDetailObject[size];
        }
    };
}
