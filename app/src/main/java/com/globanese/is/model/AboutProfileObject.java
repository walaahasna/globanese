package com.globanese.is.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by walaa on 07/06/16.
 */
public class AboutProfileObject extends RealmObject implements Parcelable{

    String address;
    String community;
    String phone;
    String birth;
    String Language;
    String Nationality;
    String Job;

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public AboutProfileObject() {

    }


    protected AboutProfileObject(Parcel in) {
        address = in.readString();
        community = in.readString();
        phone = in.readString();
        birth = in.readString();
        Language = in.readString();
        Nationality = in.readString();
        Job = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(community);
        dest.writeString(phone);
        dest.writeString(birth);
        dest.writeString(Language);
        dest.writeString(Nationality);
        dest.writeString(Job);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AboutProfileObject> CREATOR = new Creator<AboutProfileObject>() {
        @Override
        public AboutProfileObject createFromParcel(Parcel in) {
            return new AboutProfileObject(in);
        }

        @Override
        public AboutProfileObject[] newArray(int size) {
            return new AboutProfileObject[size];
        }
    };
}
