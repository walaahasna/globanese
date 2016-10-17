package com.globanese.is.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by walaa on 09/06/16.
 */
public class AboutObject extends RealmObject implements Parcelable {

    String address;
    String community;
    String phone;
    String dob;
    String dob_date;
    String dob_country;

    protected AboutObject(Parcel in) {
        address = in.readString();
        community = in.readString();
        phone = in.readString();
        dob = in.readString();
        dob_date = in.readString();
        dob_country = in.readString();
        language = in.readString();
        nationality = in.readString();
        job_place = in.readString();
        job_name = in.readString();
    }


    public static final Creator<AboutObject> CREATOR = new Creator<AboutObject>() {
        @Override
        public AboutObject createFromParcel(Parcel in) {
            return new AboutObject(in);
        }

        @Override
        public AboutObject[] newArray(int size) {
            return new AboutObject[size];
        }
    };

    public String getDob_date() {
        return dob_date;
    }

    public void setDob_date(String dob_date) {
        this.dob_date = dob_date;
    }

    public String getDob_country() {
        return dob_country;
    }

    public void setDob_country(String dob_country) {
        this.dob_country = dob_country;
    }

    public String getJob_place() {
        return job_place;
    }

    public void setJob_place(String job_place) {
        this.job_place = job_place;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    String language;
    String nationality;

    String job_place;
    String job_name;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    public AboutObject() {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(community);
        dest.writeString(phone);
        dest.writeString(dob);
        dest.writeString(dob_date);
        dest.writeString(dob_country);
        dest.writeString(language);
        dest.writeString(nationality);
        dest.writeString(job_place);
        dest.writeString(job_name);
    }
}
