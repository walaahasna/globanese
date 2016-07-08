package com.globanese.is.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import io.realm.RealmObject;

/**
 * Created by Hamdy on 19/5/2016.
 */
public class UserObject extends RealmObject implements Parcelable {

    private int id;
    private String fname;
    private String lname;
    private String dob;
    private String gender;
    private String photo;
    private String community_id;
    private String coverphoto;
    private String status;
    private String is_verified;
    private String verification_email;
    private String is_ambassador;
    private String user_type;
    private String email;
    private String deleted_at;
    private String created_at;
    private String updated_at;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCoverphoto() {
        return coverphoto;
    }

    public void setCoverphoto(String coverphoto) {
        this.coverphoto = coverphoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getVerification_email() {
        return verification_email;
    }

    public void setVerification_email(String verification_email) {
        this.verification_email = verification_email;
    }

    public String getIs_ambassador() {
        return is_ambassador;
    }

    public void setIs_ambassador(String is_ambassador) {
        this.is_ambassador = is_ambassador;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeString(this.dob);
        dest.writeString(this.gender);
        dest.writeString(this.photo);
        dest.writeString(this.coverphoto);
        dest.writeString(this.status);
        dest.writeString(this.is_verified);
        dest.writeString(this.verification_email);
        dest.writeString(this.is_ambassador);
        dest.writeString(this.user_type);
        dest.writeString(this.email);
        dest.writeString(this.community_id);
        dest.writeString(this.deleted_at);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public UserObject() {
    }

    protected UserObject(Parcel in) {
        this.id = in.readInt();
        this.fname = in.readString();
        this.lname = in.readString();
        this.dob = in.readString();
        this.gender = in.readString();
        this.photo = in.readString();
        this.coverphoto = in.readString();
        this.status = in.readString();
        this.is_verified = in.readString();
        this.verification_email = in.readString();
        this.is_ambassador = in.readString();
        this.user_type = in.readString();
        this.email = in.readString();
        this.community_id = in.readString();
        this.deleted_at = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<UserObject> CREATOR = new Parcelable.Creator<UserObject>() {
        @Override
        public UserObject createFromParcel(Parcel source) {
            return new UserObject(source);
        }

        @Override
        public UserObject[] newArray(int size) {
            return new UserObject[size];
        }
    };




    public ArrayList<String> getEmptyRequiredUserInfo() {
        ArrayList<String> tmp=new ArrayList<>();

        if(getGender()==null || getGender().isEmpty()){
            tmp.add("gender");
        }
        if(getPhoto()==null || getPhoto().isEmpty()){
            tmp.add("photo");
        }
        if(getDob()==null || getDob().isEmpty()){
            tmp.add("" + "dob");
        }
        if(getCommunity_id()==null || getCommunity_id().isEmpty() ){
            tmp.add("community");
        }
        return tmp;
    }
    public ArrayList<String> getinfo() {
        ArrayList<String> tmp=new ArrayList<>();

        if(getGender()==null || getGender().isEmpty()){
            tmp.add("gender");
        }
        if(getPhoto()==null || getPhoto().isEmpty()){
            tmp.add("photo");
        }
        if(getDob()==null || getDob().isEmpty()){
            tmp.add("" + "dob");
        }
        if(getCommunity_id()==null || getCommunity_id().isEmpty() ){
            tmp.add("community");
        }
        return tmp;
    }

}
