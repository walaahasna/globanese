package com.globanese.is.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.choota.dev.ctimeago.TimeAgo;
import com.globanese.is.R;
import com.globanese.is.adapters.MyRecyclerViewAdaptfsearch;
import com.globanese.is.adapters.PostAdapterTimeline;
import com.globanese.is.model.PostObject;
import com.globanese.is.model.SearchObject;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class TimeLineActivity extends BaseActivity {
    ListView l;
  public  static String like_count,smile_count,love_count,angry_count,wow_count;
    public static String imge_url,imge__cover_url;
    int hh, h, m, mm;
    ImageButton write_post,logout,search_button,wrire_post_header ;
    String user_id_to;
    ListView list;
    PostAdapterTimeline p;
    CircleImageView cimage;
    ArrayList<PostObject> posts;
    String text, video, location, record_type_id, event_type_id, friend_request_status, id, user_id_from, post_type;
    ArrayList<String> image_araay;
    ArrayList<String> share_image_araay;
    public static String fname, lname, country;
    AutoCompleteTextView actv;
    List<SearchObject> friends = new ArrayList<>();
    ImageView search, cancel;

    MyRecyclerViewAdaptfsearch adapter;
    String token;
    RecyclerView rv;
    ImageView back;


     RelativeLayout timeline_hedar1,timeline_hedar2,writepost;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
        token = getLogInUser().getAccess_token();
        Get_Timeline(token);
        getUserInfo();


    timeline_hedar1 = (RelativeLayout) findViewById(R.id.time_line_header);
    timeline_hedar2= (RelativeLayout) findViewById(R.id.time_line_header2);
   //writepost = (RelativeLayout) findViewById(R.id.wrire_post);




        list = (ListView) findViewById(R.id.profile_list);

      write_post = (ImageButton) findViewById(R.id.wrire_post2);
        logout = (ImageButton) findViewById(R.id.home_button);

        search_button = (ImageButton) findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeline_hedar1.setVisibility(View.GONE);
                timeline_hedar2.setVisibility(View.VISIBLE);

                //Intent i=new Intent(TimeLineActivity.this,SearchActivity.class);
                //startActivity(i);
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
        cimage = (CircleImageView) findViewById(R.id.profile_image);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity b = new BaseActivity();
                b.logOutUser();

                startActivity(new Intent(TimeLineActivity.this, LoginActivity.class));
                finish();
            }
        });




        cimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeLineActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });


        ////////////////////////////////////////////////////// search activity
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setThreshold(1);


        actv.setDropDownWidth(400);

        search = (ImageView) findViewById(R.id.search);
        cancel = (ImageView) findViewById(R.id.cancel);
        wrire_post_header= (ImageButton) findViewById(R.id.write_post_hedaer2);


        rv = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyRecyclerViewAdaptfsearch(getApplicationContext(), friends);
        adapter.setRecyclerViewListener(new MyRecyclerViewAdaptfsearch.MyRecyclerViewListener() {
                                            @Override
                                            public void viewSelected(View v, int position) {

                                                String friend_id = friends.get(position).getId();

                                                String profile_photo = friends.get(position).getProfile_pic();
                                              Log.d("photonotnull",profile_photo);
                                                String cover_photo = friends.get(position).getCover_photo();
                                                Log.d("cover_photonotnull",cover_photo);
                                                String country = friends.get(position).getProfile_country();
                                                String name = friends.get(position).getPrfile_name();
                                                String friend_status = friends.get(position).getFriend_status();
                                                Intent i = new Intent(TimeLineActivity.this, ProfileFriendActivity.class);
                                                i.putExtra("user_id", friend_id);
                                                i.putExtra("profile_photo", profile_photo);
                                                i.putExtra("cover_profile", cover_photo);
                                                i.putExtra("country", country);
                                                i.putExtra("name", name);
                                                i.putExtra("friend_status", friend_status);
                                                startActivity(i);
                                            }
                                        }

        );
//////////////////////////////////////////////////////////////////////////////////////////
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(actv.getText());
                Intent i = new Intent(TimeLineActivity.this, Search2Activity.class);
                i.putExtra("text_serach", text);
                startActivity(i);
            }
        });

///////////////////////////////////////////////////////////////////////////////
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actv.setText("");

            }
        });
/////////////////////////////////////////////////////////////////////////////////////
        wrire_post_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeLineActivity.this, WritePostActivity.class);
                startActivity(i);
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////
        actv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("text", String.valueOf(count));

                if (actv.getText().length() >= 2) {
                    find_friends(token, String.valueOf(actv.getText()));
                    list.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    timeline_hedar1.setVisibility(View.GONE);
                    timeline_hedar2.setVisibility(View.VISIBLE);

                }
             else {
                    list.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                    timeline_hedar2.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("text", String.valueOf(count));
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("text", String.valueOf(s));
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////


        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SearchObject s = (SearchObject) parent.getAdapter().getItem(position);
                Intent i = new Intent(TimeLineActivity.this, ProfileActivity.class);
                i.putExtra("id", s.getId());
                startActivity(i);


                actv.setHint("Search Here");
            }
        });

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(2000);
        animator.setAddDuration(2000);
        rv.setItemAnimator(animator);
        final LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());


        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);



    }


    void getUserInfo() {
        showProgressDialog();
        new Project_Web_Functions().GetUserInfo(getLogInUser().getAccess_token(), new UniversalCallBack() {
            @Override
            public void onResponse(Object result) {

                ResponseObject responseObject = (ResponseObject) result;
                if (responseObject.getStatus()) {
                    UserObject userObject = (UserObject) responseObject.getItems();
                  if(userObject.getCommunity()!=null&&!userObject.getCommunity().equals(null)){

                      country= userObject.getCommunity().getCountry();

                         if(country!=null&&!country.equals(null)){
                          Log.d("country",country);
                      }

                  }


                    fname = userObject.getFname();
                    lname = userObject.getLname();

                    if (userObject.getPhoto() != null)
                    {

                        imge_url = "http://globanese.info/beta/uploads/users/" + userObject.getId() + "/" + userObject.getPhoto();

                        Picasso.with(TimeLineActivity.this).load(imge_url).into(cimage);
                        Log.d("photo", imge_url);
                    } else {

                        cimage.setImageResource(R.drawable.user_image_placeholder);
                    }

//////////////////////////////cover
                    if (userObject.getCoverphoto() != null)
                    {

                    imge__cover_url = "http://globanese.info/beta/uploads/users/" + userObject.getId() + "/" + userObject.getCoverphoto();


                    }
                    else{
                        imge__cover_url=null;


                    }


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
                                            String imgeurl_share = "http://globanese.info/beta/uploads/users/" + user_id_to +"/" + imgee;
                                            share_image_araay.add(imgeurl_share);
                                            Log.d("shareimageshare", imgeurl_share);

                                            post.setShare_photo_araay(share_image_araay);
                                            post.setSharephotocount(sharephotocount);


                                        }

                                    }


                                }


                            }


///////////////////////////////////////////////////////////////////////////////end of datashare


                            //////////////////// reaction data
                            if (!item.isNull("reactions")) {
                              JSONObject reaction = item.getJSONObject("reactions");
                            like_count=  reaction.getString("like_count");
                               smile_count=  reaction.getString("smile_count");
                               love_count=  reaction.getString("love_count");
                                angry_count=  reaction.getString("angry_count");
                                wow_count=  reaction.getString("wow_count");
                                post.setLike_count( like_count);
                                post.setSmile_count(smile_count);
                                post.setLove_count(  love_count);
                                post.setAngry_count(angry_count);
                                post.setWow_count( wow_count);
                            }

                            /////////////////////commentdata




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
//////////////////////////////////////////الشخص الي عمل البوست

                            if (!item.isNull("user_from")) {
                                JSONObject user_from = item.getJSONObject("user_from");
                                String fname = user_from.getString("fname");
                                String lname = user_from.getString("lname");
                                String photo = user_from.getString("photo");
                                String coverphoto= user_from.getString("coverphoto");
                                Log.d("photo_user_from", photo + "k");
String cover_url="http://globanese.info/beta/uploads/users/" + user_id_from + "/" + coverphoto;
                                if (photo.equals("null")) {

                                    Log.d("null","null");

                                }

                                else {

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

                                    Log.d("null","null");

                                }

                                else {
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

    void find_friends(String token, String text) {

        if(friends.size()!=0){

            friends.clear();
            adapter.notifyDataSetChanged();
            rv.setAdapter(adapter);

        }

        String url = Project_Web_Functions.BASE_URL + "/find-friends/" + text + "?access_token=" + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getBoolean("status") == true) {

                        JSONArray items = result.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            SearchObject s = new SearchObject();
                            JSONObject object = items.getJSONObject(i);
                            id = object.getString("id");
                            String fname = object.getString("fname");
                            String lname = object.getString("lname");
                            String photo = object.getString("photo");

                            String coverphoto = object.getString("coverphoto");

                            if (photo!=null) {
                                String url = "http://globanese.info/beta/uploads/users/" + id + "/" + photo;
                                s.setProfile_pic(url);
                                Log.d("photo",url);
                            }
                            else {
                                s.setProfile_pic("n");
                            }

                            if (coverphoto!=null) {
                                String url_cover = "http://globanese.info/beta/uploads/users/" + id + "/" + coverphoto;
                                s.setCover_photo(url_cover);

                            } else {
                                s.setCover_photo("n");
                            }


                            if (!object.isNull("friend_status")) {
                                String friend_status = object.getString("friend_status");
                                Log.d("friend_status", friend_status);
                                s.setFriend_status(friend_status);
                            }


                            if (!object.isNull("community")) {
                                JSONObject community = object.getJSONObject("community");
                                String country = community.getString("country");
                                s.setProfile_country(country);
                            }

                            s.setPrfile_name(fname + "" + lname);
                            Log.d("name", fname);

                            s.setId(id);

                            Log.d("id", id);

                            friends.add(s);
                            Log.d("size", String.valueOf(friends.size()));
                        }

                        adapter.notifyDataSetChanged();
                        rv.setAdapter(adapter);
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
}
