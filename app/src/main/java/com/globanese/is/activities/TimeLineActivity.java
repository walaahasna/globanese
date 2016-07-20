package com.globanese.is.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.choota.dev.ctimeago.TimeAgo;
import com.globanese.is.R;
import com.globanese.is.adapters.PostAdapterTimeline;
import com.globanese.is.model.PostObject;
import com.globanese.is.model.UserObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.utils.StaticClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class TimeLineActivity extends BaseActivity {
    ListView l;
    int hh, h, m, mm;
    ImageButton write_post;
    ListView list;
    PostAdapterTimeline p;
    ArrayList<PostObject> posts;
    String text, video, location, record_type_id, event_type_id, friend_request_status, id, user_id_from;
    ArrayList<String> image_araay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
        RelativeLayout writepost = (RelativeLayout) findViewById(R.id.wrire_post);


        getUserInfo();

        String token = getLogInUser().getAccess_token();
        Get_Timeline(token);
        list = (ListView) findViewById(R.id.profile_list);

        write_post = (ImageButton) findViewById(R.id.wrire_post2);

        writepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeLineActivity.this, WritePostActivity.class);
                startActivity(i);

            }
        });
        write_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeLineActivity.this, WritePostActivity.class);
                startActivity(i);

            }
        });

//////////////////////////////////////////////////////
        CircleImageView cimage = (CircleImageView) findViewById(R.id.profile_image);

   /* BaseActivity b = new BaseActivity();
        UserObject user = b.getUserObject();
        String imge_url=b.getprofilephoto();

        Log.d("photo",imge_url);
        if (imge_url != null)

        {

            Picasso.with(TimeLineActivity.this).load(imge_url).into(cimage);
            Log.d("photo",imge_url);
        } else {

            cimage.setImageResource(R.drawable.user_image_placeholder);
        }
*/

        cimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeLineActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

    }

    void getUserInfo() {
        showProgressDialog();
        new Project_Web_Functions().GetUserInfo(getLogInUser().getAccess_token(), new UniversalCallBack() {
            @Override
            public void onResponse(Object result) {
                ResponseObject responseObject = (ResponseObject) result;
                if (responseObject.getStatus()) {
                    UserObject userObject = (UserObject) responseObject.getItems();

                    saveUserObject(userObject);
                    checkForSignUpCompletion(userObject);
                } else {

                }
            }

            @Override
            public void onFailure(Object result) {

            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void checkForSignUpCompletion(UserObject userObject) {
        if (userObject.getEmptyRequiredUserInfo().size() > 0) {

            startActivity(new Intent(TimeLineActivity.this, SignUpCompleteActivity.class));
        }
    }


    public void Get_Timeline(final String token) {
        showProgressDialog();


        String url = Project_Web_Functions.BASE_URL + "/timeline?access_token=" + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getBoolean("status") == (true)) {

                        posts = new ArrayList<PostObject>();

                        JSONArray items = result.getJSONArray("items");
                        Log.d("coun11t", String.valueOf(items.length()));

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            PostObject post = new PostObject();


                            if (!item.isNull("event_type_id")) {
                                event_type_id = item.getString("event_type_id");


                                Log.d("enventypeid", event_type_id);


                                if (event_type_id.equals("1")) {

                                    if (!item.isNull("record_type_id")) {
                                        record_type_id = item.getString("record_type_id");
                                        Log.d("record_type_id", record_type_id);

                                        if (record_type_id.equals("3")) {
                                            post.setType("text_post");

                                        } else if (record_type_id.equals("4")) {
                                            post.setType("photo_post");
                                            Log.d("photo_post", "photo");
                                        } else if (record_type_id.equals("5")) {
                                            post.setType("video_post");

                                        }
                                    }

                                }

                                /////////////////////////////////////
                                else if (event_type_id.equals("2")) {

                                    post.setType("comment_post");

                                } else if (event_type_id.equals("3")) {

                                    post.setType("like_post");

                                } else if (event_type_id.equals("4")) {

                                    post.setType("share_post");

                                } else if (event_type_id.equals("5")) {

                                    post.setType("newfriend_post");

                                } else if (event_type_id.equals("6")) {

                                    post.setType("newmember_post");

                                } else if (event_type_id.equals("7")) {

                                    post.setType("changeprofile_post");

                                } else if (event_type_id.equals("8")) {

                                    post.setType("changecover_post");

                                }

                            }

////////////////////////////////////////////////////////////////////////////////

                            if (!item.isNull("video")) {
                                video = item.getString("video");
                                post.setVideo(video);
                            }

                            if (!item.isNull("recorder_id")) {
                                String id = item.getString("recorder_id");
                                post.setId(Integer.parseInt(id));
                                Log.d("id", id);
                            }


                            //////////////////////////////// time

                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            String test = sdf.format(cal.getTime());
                            try {
                                Date currentdatee = sdf.parse(test);
                                h = currentdatee.getHours();
                                m = currentdatee.getMinutes();
                                Log.d("server", String.valueOf(h));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("TEST", test);


                            if (!item.isNull("created_at")) {
                                String created_at = item.getString("created_at");
                                Log.d("hour/server", created_at);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                                Date currentdate = null;
                                try {


                                    currentdate = dateFormat.parse(created_at);
                                    TimeAgo timeAgo = new TimeAgo();
                                    String time = timeAgo.getTimeAgo(currentdate);
                                    post.setCreated_at(time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            if (!item.isNull("user_id_from")) {
                                user_id_from = item.getString("user_id_from");


                            }


                            if (!item.isNull("user_from")) {
                                JSONObject user_from = item.getJSONObject("user_from");
                                String fname = user_from.getString("fname");
                                String lname = user_from.getString("lname");
                                String photo = user_from.getString("photo");

                                String imgeurl = "http://globanese.info/beta/uploads/users/" + user_id_from + "/profile_" + photo;

                                post.setName(fname + "" + lname);
                                post.setPhoto(imgeurl);
                                Log.d("photo", imgeurl);

                            }


                            if (!item.isNull("location")) {
                                location = item.getString("location");
                                post.setLocation(location);
                                Log.d("location", location);
                            }


                            if (!item.isNull("text")) {
                                text = item.getString("text");
                                post.setText(text);
                                Log.d("text", text);

                            }


//////////////////////////// //////////////////////////////////photo


                            if (!item.isNull("postphotos")) {
                                JSONArray postphotos = item.getJSONArray("postphotos");
                                int photocount = postphotos.length();
                                Log.d("coun00t", String.valueOf(photocount));
                                image_araay = new ArrayList<>();
                                String imgeurl = null;
                                for (int ii = 0; ii < postphotos.length(); ii++) {
                                    JSONObject photo_object = postphotos.getJSONObject(ii);
                                    if (!photo_object.isNull("photo")) {

                                        JSONArray photo_araay = photo_object.getJSONArray("photo");
                                        for (int e = 0; e < photo_araay.length(); e++) {
                                            JSONObject photo = photo_araay.getJSONObject(e);

                                            if (!photo.isNull("photo")) {
                                                String imge = photo.getString("photo");
                                                imgeurl = "http://globanese.info/beta/uploads/users/" + user_id_from + "/normal_" + imge;
                                                image_araay.add(imgeurl);

                                            }

                                            post.setPhoto_araay(image_araay);
                                            post.setPhotocount(photocount);
                                            Log.d("size", String.valueOf(image_araay.size()));

                                        }


                                    }


                                }

                            }
                            posts.add(post);
                        }
                        p = new PostAdapterTimeline(getApplicationContext(), posts);
                        p.notifyDataSetChanged();
                        list.setAdapter(p);
                        dismissProgressDialog();

                    } else {
                        Toast.makeText(getApplicationContext(), "error ", Toast.LENGTH_SHORT).show();


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


                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }


}
