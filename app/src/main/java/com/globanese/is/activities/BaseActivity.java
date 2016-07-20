package com.globanese.is.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.globanese.is.model.AboutObject;
import com.globanese.is.model.PostObject;
import com.globanese.is.model.UserObject;
import com.globanese.is.realm.RealmSingleton;
import com.globanese.is.model.LogInUser;
import com.globanese.is.utils.StaticClass;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Hamdy on 10/5/2016.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticClass.overrideFonts(this,findViewById(android.R.id.content));

    }


    public void showProgressDialog() {
        dismissProgressDialog();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.show();
    }

    public void dismissProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public LogInUser getLogInUser() {
        return RealmSingleton.getRealmInstance().where(LogInUser.class).findFirst();
    }

    public void logOutUser() {
        Realm realm = RealmSingleton.getRealmInstance();
        realm.beginTransaction();
        LogInUser user = getLogInUser();
        if (user != null) {

            if (user.getLogin_type() == LogInUser.LOGIN_TYPE_FACEBOOK) {
                try {
                    FacebookSdk.clearLoggingBehaviors();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    LoginManager.getInstance().logOut();
                    LoginManager.getInstance().setLoginBehavior(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (user.getLogin_type() == LogInUser.LOGIN_TYPE_LINKEDIN) {
                try {
                    //LISessionManager.getInstance(getApplicationContext()).clearSession();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            user.deleteFromRealm();
        }

        realm.commitTransaction();


    }


    public void saveLogInUser(LogInUser user) {
        logOutUser();
        Realm realm = RealmSingleton.getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
    }

    /////////////////

    public UserObject getUserObject() {
        return RealmSingleton.getRealmInstance().where(UserObject.class).findFirst();
    }

    public void saveUserObject(UserObject user) {
        removeUserObject();
        Realm realm = RealmSingleton.getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
    }

    public void removeUserObject() {
        Realm realm = RealmSingleton.getRealmInstance();
        realm.beginTransaction();
        UserObject user = getUserObject();
        if (user != null) {
            user.deleteFromRealm();
        }
        realm.commitTransaction();
    }





    public void saveAboutObject(AboutObject About) {

        Realm realm = RealmSingleton.getRealmInstance();
        realm.beginTransaction();

       realm.copyToRealm(About);
        realm.commitTransaction();
    }



    public RealmResults<AboutObject> getAboutObject() {

        Realm realm = RealmSingleton.getRealmInstance();
        realm.beginTransaction();

        RealmResults<AboutObject> o=   realm.where(AboutObject.class).findAll();
        realm.commitTransaction();

        return o;
    }
   public String getprofilephoto() {

        UserObject user = getUserObject();
        String imge_url = null;
        if (user.getPhoto()!= null) {

            imge_url = "http://globanese.info/beta/uploads/users/"+ user.getId() + "/profile_" + user.getPhoto();
            Log.d("photo", imge_url);
        }
        return imge_url;

    }

    public String getcoverphoto() {

        UserObject user = getUserObject();

        String imge_url = null;
        if (user.getCoverphoto() != null) {

            imge_url = "http://globanese.info/beta/uploads/users/"+ user.getId() + "/coverpage_" + user.getCoverphoto();
            Log.d("photo", imge_url);
        }
        return imge_url;

    }
    public String getnormaolphoto() {

        UserObject user = getUserObject();

        String imge_url = null;
        if (user.getCoverphoto() != null) {
            imge_url = "http://globanese.info/beta/uploads/users/"+ user.getId() + "/normal_" + user.getCoverphoto();
            Log.d("photo", imge_url);
        }
        return imge_url;

    }


}