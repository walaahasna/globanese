package com.globanese.is.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.choota.dev.ctimeago.TimeAgo;
import com.globanese.is.R;
import com.globanese.is.adapters.PostAdapterTimeline;
import com.globanese.is.model.PostObject;
import com.globanese.is.model.SearchObject;
import com.globanese.is.model.UserObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleyMultipartRequest;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.realm.RealmSingleton;
import com.globanese.is.utils.RealPathUtil;
import com.globanese.is.utils.StaticClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

public class ProfileFriendActivity extends BaseActivity {
    String t;
    public static Context contex;
    @InjectView(R.id.profile_list)
    ListView listView;
    @InjectView(R.id.root)
    View rootView;
    List<PostObject> PostArray;
    Bitmap resultImage;
    float minHeaderHeight;
    View list_header;
    View about_linear;
    List<byte[]> Array_photo;
    public static String imge_url;
    int hh, h, m, mm;
    ImageButton write_post;
    String user_id_to;
    IconAnimator2 Animator;
    PostAdapterTimeline p;
    CircleImageView cimage;
    ArrayList<PostObject> posts;
    String text, video, location, record_type_id, event_type_id, friend_request_status, id, user_id_from, post_type;
    ArrayList<String> image_araay;
    ArrayList<String> share_image_araay;
    public static String fname, lname;
    byte[] image;
    TextView textview_status_color;
    public static Activity con;
    String token;
    Project_Web_Functions network;
    ImageView icon_status_color;
    public static String profile_photo, cover_photo, profile_name, country, friend_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        ButterKnife.inject(this, this);
        con = ProfileFriendActivity.this;
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));

        network = new Project_Web_Functions();






        /////////////////////////////////////////////
        String friend_id = getIntent().getStringExtra("user_id");
        profile_photo = getIntent().getStringExtra("profile_photo");
        cover_photo = getIntent().getStringExtra("cover_profile");
        country = getIntent().getStringExtra("country");
        profile_name = getIntent().getStringExtra("name");
        friend_status = getIntent().getStringExtra("friend_status");


        Log.d("profile_photo", profile_photo + "k");
        Log.d("cover_profile", cover_photo + "k");

        token = getLogInUser().getAccess_token();

        Get_Timeline(token, friend_id);

        ////////////////////////////////////////////////////////////




        ImageView back = (ImageView) findViewById(R.id.back);
        contex = ProfileFriendActivity.this;
        minHeaderHeight = getResources().getDimension(R.dimen.action_bar_height);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            View menu_status_bar1 = findViewById(R.id.status_bar);
            menu_status_bar1.getLayoutParams().height = StaticClass.getStatusBarHeight(this);
            minHeaderHeight += StaticClass.getStatusBarHeight(this);
        }


////////////////////////////////set style of list
        list_header = getLayoutInflater().inflate(R.layout.time_line_header_2, null);

        icon_status_color = (ImageView) list_header.findViewById(R.id.icon_status_color);
        textview_status_color = (TextView) list_header.findViewById(R.id.textview_status_color);

        LinearLayout layout_friend = (LinearLayout) list_header.findViewById(R.id.layout_friend);
        LinearLayout layout_album = (LinearLayout) list_header.findViewById(R.id.layout_album);

        LinearLayout message_linear = (LinearLayout) list_header.findViewById(R.id.message);
        LinearLayout write_post = (LinearLayout) list_header.findViewById(R.id.write_post_linear);
        LinearLayout about_linear = (LinearLayout) list_header.findViewById(R.id.about_linear);


        layout_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getallfriend(token);
            }
        });


        write_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFriendActivity.this, WritePostActivity.class);
                startActivity(i);
            }
        });


        about_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFriendActivity.this, AboutProfileActivity.class);
                startActivity(i);
            }
        });


        if (friend_status.equals("1")) {
            icon_status_color.setImageResource(R.drawable.is_friend_icon1);
            textview_status_color.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
            textview_status_color.setText("Connected");
        } else if (friend_status.equals("2")) {
            // icon_status_color.setImageResource(R.drawable.add_person);
            icon_status_color.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint_friend));
            textview_status_color.setTextColor(getApplicationContext().getResources().getColor(R.color.tint_friend));
            textview_status_color.setText("Add Friend");
        } else {
            icon_status_color.setColorFilter(getApplicationContext().getResources().getColor(R.color.color_yellow));

            //icon_status_color.setImageResource(R.drawable.pending_friend);
            textview_status_color.setTextColor(getApplicationContext().getResources().getColor(R.color.color_yellow));
            textview_status_color.setText("Pending");
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFriendActivity.this, TimeLineActivity.class);
                startActivity(i);

            }
        });


        StaticClass.overrideFonts(this, list_header);

        // initViews();
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        Animator = new IconAnimator2(list_header);
        StikkyHeaderBuilder.stickTo(listView).setHeader(R.id.header, viewGroup)
                .minHeightHeader((int) minHeaderHeight)
                .animator(Animator)
                .build();
        listView.addHeaderView(list_header);
/////////////////////////////////////////////////////////////////////////////////////


    }

    void getallfriend(String token) {


        String url = Project_Web_Functions.BASE_URL + "/friends/" + text + "?access_token=" + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getBoolean("status") == true) {

                        JSONObject items = result.getJSONObject("items");
                        JSONArray user_pending = items.getJSONArray("user_pending");
                        for (int i = 0; i < user_pending.length(); i++) {

                            JSONObject user_p = user_pending.getJSONObject(i);
                            SearchObject s = new SearchObject();


                            String fname = user_p.getString("fname");
                            String lname = user_p.getString("lname");

                            if (!user_p.isNull("photo")) {
                                String photo = user_p.getString("photo");
                                String url = "http://globanese.info/beta/uploads/users/" + id + "/" + photo;
                                s.setProfile_pic(url);


                            }
                            else {
                                s.setProfile_pic("n");

                            }

                            if (!user_p.isNull("coverphoto")) {
                                String coverphoto = user_p.getString("coverphoto");

                                String url_cover = "http://globanese.info/beta/uploads/users/" + id + "/" + coverphoto;
                                s.setCover_photo(url_cover);
                            } else {
                                s.setCover_photo("n");
                            }

                            if (!user_p.isNull("friend_status")) {
                                String friend_status = user_p.getString("friend_status");
                                Log.d("friend_status", friend_status);
                                s.setFriend_status(friend_status);
                            }


                            if (!user_p.isNull("community")) {
                                JSONObject community = user_p.getJSONObject("community");
                                String country = community.getString("country");
                                s.setProfile_country(country);
                            }

                        }


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
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }


    private void initViews() {
        // about_linear = list_header.findViewById(R.id.about_linear);


       /* about_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileFriendActivity.this, AboutProfileActivity.class));
            }
        });*/
    }


    public void Get_Timeline(final String token, String user_id) {
        showProgressDialog();

        String url = Project_Web_Functions.BASE_URL + "/timeline/true/" + user_id + "?access_token=" + token;
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
                                post.setEvent_type_id(event_type_id);

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

                            ////share_data

                            if (!item.isNull("orignalpost")) {
                                JSONArray original_post = item.getJSONArray("orignalpost");
                                for (int k = 0; k < original_post.length(); k++) {

                                    JSONObject postt = original_post.getJSONObject(k);
                                    if (!item.isNull("user_id_to")) {
                                        user_id_to = item.getString("user_id_to");


                                    }


                                    if (!postt.isNull("video")) {
                                        video = postt.getString("video");
                                        post.setSharevideo(video);
                                    }

                                    if (!postt.isNull("post_type")) {
                                        post_type = postt.getString("post_type");
                                        post.setShare_type(post_type);
                                    }


                                    if (!postt.isNull("text")) {
                                        String textt = postt.getString("text");
                                        post.setSharetext(textt);


                                    }


                                    if (!postt.isNull("location")) {
                                        String locationn = postt.getString("location");
                                        post.setSharelocation(locationn);
                                    }


                                    if (!postt.isNull("created_at")) {
                                        String created_at = postt.getString("created_at");
                                        Log.d("sharecreatedat", created_at);
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                                        Date currentdate = null;
                                        try {


                                            currentdate = dateFormat.parse(created_at);
                                            TimeAgo timeAgo = new TimeAgo();
                                            String time = timeAgo.getTimeAgo(currentdate);
                                            post.setSharecreated_at(time);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }


                                    ///////////////////////////////share_photo
                                    if (!postt.isNull("photo")) {
                                        JSONArray postphotoss = postt.getJSONArray("photo");
                                        int sharephotocount = postphotoss.length();
                                        Log.d("countt", String.valueOf(sharephotocount));
                                        share_image_araay = new ArrayList<>();

                                        for (int ii = 0; ii < postphotoss.length(); ii++) {
                                            JSONObject photo_object = postphotoss.getJSONObject(ii);
                                            String imgee = photo_object.getString("photo");
                                            String imgeurl_share = "http://globanese.info/beta/uploads/users/" + user_id_to + "/" + imgee;
                                            share_image_araay.add(imgeurl_share);
                                            Log.d("shareimageshare", imgeurl_share);
                                            post.setShare_photo_araay(share_image_araay);
                                            post.setSharephotocount(sharephotocount);

                                        }

                                    }


                                }


                            }


///////////////////////////////////////////////////////////////////////////////end of datashare

                            if (!item.isNull("count_comment")) {
                                String count_comment = item.getString("count_comment");
                                post.setCommentcount(count_comment);

                                Log.d("count_comment", count_comment);
                            }


                            if (!item.isNull("id")) {
                                String timeline_post_id = item.getString("id");
                                post.setTimeline_post_id(timeline_post_id);

                                Log.d(" timeline_post_id", timeline_post_id);
                            }

                            if (!item.isNull("count_share")) {
                                String count_share = item.getString("count_share");
                                post.setSharecount(count_share);
                                Log.d("count_share", count_share);
                            }

                            if (!item.isNull("count_reactions")) {
                                String count_reactions = item.getString("count_reactions");
                                post.setLikecount(count_reactions);
                                Log.d("count_reactions", count_reactions);
                            }


                            if (!item.isNull("video")) {
                                video = item.getString("video");
                                post.setVideo(video);
                            }


                            if (!item.isNull("recorder_id")) {
                                String id = item.getString("recorder_id");
                                post.setId(Integer.parseInt(id));
                                Log.d("id", id);
                            }


                            if (!item.isNull("is_like_user_post")) {
                                Boolean is_like_user_post = item.getBoolean("is_like_user_post");

                                Log.d("is_like_user_post", String.valueOf(is_like_user_post));
                                post.setIs_like_user_post(is_like_user_post);

                            }


                            //////////////////////////////////////////////////////////////////////////


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

                            //////////////////////////////user_info
                            if (!item.isNull("user_id_from")) {
                                user_id_from = item.getString("user_id_from");
                                post.setUser_share_from(user_id_from);
                                Log.d("user_id_from", user_id_from);

                            }


                            if (!item.isNull("user_from")) {
                                JSONObject user_from = item.getJSONObject("user_from");
                                String fname = user_from.getString("fname");
                                String lname = user_from.getString("lname");
                                String photo = user_from.getString("photo");
                                String coverphoto = user_from.getString("coverphoto");
                                Log.d("photo_user_from", photo + "k");
                                String cover_url = "http://globanese.info/beta/uploads/users/" + user_id_from + "/" + coverphoto;
                                if (photo.equals("null")) {

                                    Log.d("null", "null");

                                } else {

                                    String imgeurl = "http://globanese.info/beta/uploads/users/" + user_id_from + "/" + photo;
                                    post.setPhoto(imgeurl);
                                    post.setCover_photo(cover_url);
                                    Log.d("photo_user_from", imgeurl);
                                }


                                post.setName(fname + "" + lname);

                            }


                            if (!item.isNull("user_to")) {
                                JSONObject user_from = item.getJSONObject("user_to");
                                String fnamee = user_from.getString("fname");
                                String lnamee = user_from.getString("lname");
                                String photoo = user_from.getString("photo");

                                if (photoo.equals("null")) {

                                    Log.d("null", "null");

                                } else {
                                    String imgeurl = "http://globanese.info/beta/uploads/users/" + user_id_to + "/" + photoo;

                                    post.setSharephoto(imgeurl);


                                }


                                post.setSharename(fnamee + "" + lnamee);
                                post.setName_of_user_ishare_from(fnamee + "" + lnamee);


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
                                                imgeurl = "http://globanese.info/beta/uploads/users/" + user_id_from + "/" + imge;
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
                        listView.setAdapter(p);
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


class IconAnimator2 extends HeaderStikkyAnimator {
    public static ImageView cover;

    View list_header;

    TextView name, country;
    LinearLayout edit_coverphoto;
    ProfileActivity profile;
    public static CircleImageView i, i2;
    public static CircleImageView prfilephoto;

    IconAnimator2(View list_header) {
        this.list_header = list_header;

    }


    @Override
    public AnimatorBuilder getAnimatorBuilder() {

        /////////////////name and country
        name = (TextView) list_header.findViewById(R.id.name);
        country = (TextView) list_header.findViewById(R.id.country);


        name.setText(ProfileFriendActivity.profile_name);
        country.setText(ProfileFriendActivity.country);


////////////////profile photo

        i = (CircleImageView) getHeader().findViewById(R.id.profile_image);
        i2 = (CircleImageView) list_header.findViewById(R.id.profile_image2);


        if (!ProfileFriendActivity.profile_photo.equals("n"))

        {
            Picasso.with(ProfileActivity.contex).load(ProfileFriendActivity.profile_photo).into(i);
            Picasso.with(ProfileActivity.contex).load(ProfileFriendActivity.profile_photo).into(i2);
        } else {

            i.setImageResource(R.drawable.user_image_placeholder);
            i2.setImageResource(R.drawable.user_image_placeholder);
        }


        ///////////////////////////////////////////////////////////////////////////
        //////////////////////////coverphoto//////////////////////////
        cover = (ImageView) getHeader().findViewById(R.id.cover_photo);


        if (!ProfileFriendActivity.cover_photo.equals("n"))

        {
            Picasso.with(ProfileActivity.contex).load(ProfileFriendActivity.cover_photo).into(cover);

        } else {
            cover.setImageResource(R.drawable.cover_placeholder);

        }
        /////////////////////////////////////////////////////////////////


        AnimatorBuilder animatorBuilder = AnimatorBuilder.create()

                .applyFade2(getHeader().findViewById(R.id.profile_image), 1, 0, new mi())
                .applyFade2(list_header.findViewById(R.id.profile_image2), 1, 0, new mi());


        return animatorBuilder;
    }

    class mi implements Interpolator {

        @Override
        public float getInterpolation(float input) {
            return (input <= 0.9) ? 0 : (input - 0.9f) * 10f;
        }
    }


}




