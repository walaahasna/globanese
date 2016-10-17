package com.globanese.is.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by walaa on 03/06/16.
 */
public class CommentObject implements Parcelable {



    String name;
   String image;
     String text;
    String time;
    String  comment_id;
    Boolean is_like_user_comment;
    String like_count;
    String ceated_at;

    public String getCeated_at() {
        return ceated_at;
    }

    public void setCeated_at(String ceated_at) {
        this.ceated_at = ceated_at;
    }



    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public Boolean getIs_like_user_comment() {
        return is_like_user_comment;
    }

    public void setIs_like_user_comment(Boolean is_like_user_comment) {
        this.is_like_user_comment = is_like_user_comment;
    }

    public String getComment_id() {

        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String no_of_like;




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNo_of_like() {
        return no_of_like;
    }

    public void setNo_of_like(String no_of_like) {
        this.no_of_like = no_of_like;
    }




    public CommentObject() {
    }



    protected CommentObject(Parcel in) {
        name = in.readString();

        text = in.readString();
        time = in.readString();
        no_of_like = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);

        dest.writeString(text);
        dest.writeString(time);
        dest.writeString(no_of_like);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentObject> CREATOR = new Creator<CommentObject>() {
        @Override
        public CommentObject createFromParcel(Parcel in) {
            return new CommentObject(in);
        }

        @Override
        public CommentObject[] newArray(int size) {
            return new CommentObject[size];
        }
    };
}
