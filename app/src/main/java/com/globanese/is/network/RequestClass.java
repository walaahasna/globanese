package com.globanese.is.network;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.activities.BaseActivity;
import com.globanese.is.activities.EdiLanguageActivity;
import com.globanese.is.activities.EditDobActivity;
import com.globanese.is.activities.EditJobActivity;
import com.globanese.is.model.UserObject;
import com.globanese.is.realm.RealmSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by walaa on 28/06/16.
 */
public class RequestClass extends BaseActivity {

    static String BASE_URL = "http://globanese.info/beta/api/v1";
    static String greate_post = BASE_URL + "/post";
    static String upload_image = BASE_URL + "/image";


    public void Send_Post(final String token, final Map<String, String> map) {

        String url = greate_post;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getBoolean("status") == (true)) {
                    dismissProgressDialog();


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
                params.putAll(map);

                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);

    }


    public void upload_image(final String token, final byte[] image) {

        String url = upload_image;
        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") == (true)) {
                                Log.d("response", response.toString());
                                JSONObject photo_araay = response.getJSONObject("items");
                                String image = photo_araay.getString("image");


                                Realm realm = RealmSingleton.getRealmInstance();
                                UserObject user = realm.where(UserObject.class).findFirst();
                                realm.beginTransaction();
                                int id = user.getId();
                                realm.commitTransaction();

                                String imgeurl = "http://globanese.info/beta/uploads/users/" + id + "/profile_" + image;
                                Log.d("image_url", imgeurl);
                                dismissProgressDialog();
                                Toast.makeText(EditJobActivity.con, "Post Success", Toast.LENGTH_SHORT).show();


                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }


                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("access_token", token);

                        return params;
                    }

                    protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();

                        params.put("photo", new DataPart("image.jpeg", image, "image/jpeg"));
                        return params;
                    }


                };


        VolleySingleton.getInstance().addToRequestQueue(multipartRequest);


    }

}
