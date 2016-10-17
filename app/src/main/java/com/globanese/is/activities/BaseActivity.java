package com.globanese.is.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.globanese.is.model.AboutObject;
import com.globanese.is.model.PostObject;
import com.globanese.is.model.UserAction;
import com.globanese.is.model.UserObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.realm.RealmSingleton;
import com.globanese.is.model.LogInUser;
import com.globanese.is.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
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

        RealmResults<AboutObject> o = realm.where(AboutObject.class).findAll();
        realm.commitTransaction();

        return o;
    }

    public String getprofilephoto() {

        UserObject user = getUserObject();
        String imge_url = null;
        if (user.getPhoto() != null) {

            imge_url = "http://globanese.info/beta/uploads/users/" + user.getId() + "/profile_" + user.getPhoto();
            Log.d("photo", imge_url);
        }
        return imge_url;

    }

    public String getcoverphoto() {

        UserObject user = getUserObject();

        String imge_url = null;
        if (user.getCoverphoto() != null) {

            imge_url = "http://globanese.info/beta/uploads/users/" + user.getId() + "/coverpage_" + user.getCoverphoto();
            Log.d("photo", imge_url);
        }
        return imge_url;

    }

    public String getnormaolphoto() {

        UserObject user = getUserObject();

        String imge_url = null;
        if (user.getCoverphoto() != null) {
            imge_url = "http://globanese.info/beta/uploads/users/" + user.getId() + "/normal_" + user.getCoverphoto();
            Log.d("photo", imge_url);
        }
        return imge_url;

    }


    public String insertlike(String token, String postid) {

        Realm realm = RealmSingleton.getRealmInstance();
        UserAction result2 = realm.where(UserAction.class).equalTo("token", token).equalTo("id", postid).findFirst();


        realm.beginTransaction();



        if (result2 == null) {
            UserAction user = realm.createObject(UserAction.class); // Create a new object
            user.setToken(token);
            user.setLikecount("1");
            user.setId(postid);
            Log.d("insert", "insert");


        } else {
            Log.d("nothing", "nothing");
            Log.d("post", result2.getId() + "k");

        }
        realm.commitTransaction();
        long count = realm.where(UserAction.class).equalTo("id", postid).equalTo("likecount", "1").count();
        String countlike = String.valueOf(count);

        return countlike;

    }
    public UserAction like(String token, String postid, String userid, int liketype, String presstype) {
        Log.d(" token",token);
        Log.d("postid",postid);
        Log.d(" userid", userid);
        Log.d("liketype", String.valueOf(liketype));
        Log.d("presstype",presstype);


        UserAction action = new UserAction();
        Realm realm = RealmSingleton.getRealmInstance();
        UserAction result2 = realm.where(UserAction.class).equalTo("userid", userid).equalTo("id", postid).findFirst();


        realm.beginTransaction();
///no
        UserAction user =  realm.createObject(UserAction.class);
        if (result2 == null) {

            user.setToken(token);
            user.setLiketype(liketype);
            user.setUserid(userid);
            user.setId(postid);

            if (liketype == 0) {
                user.setLike_count("1");
            } else if (liketype == 1) {
                user.setSmile_count("1");
            } else if (liketype == 2) {
                user.setLove_count("1");
            } else if (liketype == 3) {
                user.setAngry_count("1");
            } else if (liketype == 4) {
                user.setWow_count("1");
            }



            Log.d("insert", "insert");
            Create_like(token, postid, String.valueOf(liketype));
            action.setGreatelike(true);
        }

        ///update

        else if (!(result2 == null) && presstype.equals("short")&&liketype==0) {

            Log.d("wowcat","0" );
            Log.d("result2.getLiketype()", String.valueOf(result2.getLiketype()));

           // Log.d("result", result2.getId() + "/n" + result2.getToken() + "/n" + result2.getLikecount());
            delete_like(token, postid);
            result2.deleteFromRealm();
            Log.d("delelte", "delelte");
            action.setGreatelike(false);


        }
        else {
            Log.d("token", token + "k");
            result2.deleteFromRealm();
            user.setToken(token);
            user.setLiketype(liketype);
            user.setUserid(userid);
            user.setId(postid);
            if (liketype == 0) {
                Log.d("liketypefinal","0");
                user.setLike_count("1");
                user.setSmile_count("0");
                user.setLove_count("0");
                user.setAngry_count("0");
                user.setWow_count("0");


            } else if (liketype == 1) {
                Log.d("smiletype","1");
                user.setLike_count("0");
                user.setSmile_count("1");
                user.setLove_count("0");
                user.setAngry_count("0");
                user.setWow_count("0");

            } else if (liketype == 2) {
                user.setLike_count(String.valueOf(0));
                user.setSmile_count(String.valueOf(0));
                user.setLove_count(String.valueOf(1));
                user.setAngry_count(String.valueOf(0));
                user.setWow_count(String.valueOf(0));
            }
            else if (liketype == 3) {
                user.setLike_count("0");
                user.setSmile_count("0");
                user.setLove_count("0");
                user.setAngry_count(String.valueOf(1));
                user.setWow_count("0");
            } else  {
                 user.setLike_count("0");
                user.setSmile_count("0");
                user.setLove_count("0");
                user.setAngry_count("0");
                user.setWow_count("1");
            }
            Log.d("insert", "insert");
            Create_like(token, postid, String.valueOf(liketype));
            action.setGreatelike(true);
        }

        long like = realm.where(UserAction.class).equalTo("id", postid).equalTo("like_count", "1").count();
        long smile = realm.where(UserAction.class).equalTo("id", postid).equalTo("smile_count", "1").count();
        long love = realm.where(UserAction.class).equalTo("id", postid).equalTo("love_count", "1").count();
        long angry = realm.where(UserAction.class).equalTo("id", postid).equalTo("angry_count", "1").count();
        long wow = realm.where(UserAction.class).equalTo("id", postid).equalTo("wow_count", "1").count();
        action.setLike_count(String.valueOf(like));
        action.setSmile_count(String.valueOf(smile));
        action.setLove_count(String.valueOf(love));
        action.setAngry_count(String.valueOf(angry));
        action.setWow_count(String.valueOf(wow));
        action.setLikecount(String.valueOf(like + smile + love + angry + wow));
        realm.commitTransaction();
        return action;

    }


    public static void Create_like(final String token, final String post_id, final String like_type) {
        Log.d("token", token);
        Log.d("id", post_id);
        Log.d("like_type", like_type);


        String url = Project_Web_Functions.BASE_URL + "/reaction";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);


                    if (result.getBoolean("status") == (true)) {


                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("access_token", token);
                params.put("post_comment_id", post_id);
                params.put("type", "0");
                params.put("type_form", like_type);
                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }


    public static void delete_like(final String token, final String post_id) {
        Log.d("token", token);
        Log.d("id", post_id);


        String url = Project_Web_Functions.BASE_URL + "/reaction";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);


                    if (result.getBoolean("status") == (true)) {


                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("access_token", token);
                params.put("post_comment_id", post_id);
                params.put("type", "0");
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-HTTP-Method-Override", "DELETE");

                return headers;
            }

        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }
}