package com.globanese.is.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by walaa on 03/06/16.
 */
public class CommentObject implements Parcelable {



    String name;
    int image;
     String text;
    String time;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
        image = in.readInt();
        text = in.readString();
        time = in.readString();
        no_of_like = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(image);
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
