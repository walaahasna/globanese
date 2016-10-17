package com.globanese.is.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by Hamdy on 19/5/2016.
 */
public class CommunityObject  extends RealmObject implements Parcelable {
    private int id;
    private String country;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }




    public String getName(){
        return country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.country);

    }

    public CommunityObject() {
    }

    protected CommunityObject(Parcel in) {
        this.id = in.readInt();
        this.country = in.readString();

    }

    public static final Parcelable.Creator<CommunityObject> CREATOR = new Parcelable.Creator<CommunityObject>() {
        @Override
        public CommunityObject createFromParcel(Parcel source) {
            return new CommunityObject(source);
        }

        @Override
        public CommunityObject[] newArray(int size) {
            return new CommunityObject[size];
        }
    };
}
