package com.globanese.is.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.globanese.is.adapters.PostAdapter;
import com.globanese.is.adapters.PostAdapterTimeline;
import com.globanese.is.app.ApplicationController;
import com.globanese.is.model.AddFriendObject;
import com.globanese.is.model.ImageObject;
import com.globanese.is.model.PostObject;
import com.globanese.is.model.UserObject;
import com.globanese.is.network.ErrorMsg;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;
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

public class ProfileActivity extends BaseActivity {
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
    IconAnimator Animator;
    PostAdapterTimeline p;
    CircleImageView cimage;
    ArrayList<PostObject> posts;
    String text, video, location, record_type_id, event_type_id, friend_request_status, id, user_id_from, post_type;
    ArrayList<String> image_araay;
    ArrayList<String> share_image_araay;
    public static String fname, lname;
    byte[] image;
    public static Activity con;
    Project_Web_Functions network;
    AlertDialog choosePicDialog;
    int REQUEST_IMAGE_CAPTURE = 333;
    int REQUEST_IMAGE_GALLERY = 100;

    int REQUEST_IMAGE_GALLERY_cover = 200;
    int REQUEST_IMAGE_CAPTURE_cover = 433;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this, this);
        con = ProfileActivity.this;
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));

        network = new Project_Web_Functions();

        String token = getLogInUser().getAccess_token();
        Get_Timeline(token);

        ImageView back = (ImageView) findViewById(R.id.back);
        contex = ProfileActivity.this;
        minHeaderHeight = getResources().getDimension(R.dimen.action_bar_height);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            View menu_status_bar1 = findViewById(R.id.status_bar);
            menu_status_bar1.getLayoutParams().height = StaticClass.getStatusBarHeight(this);
            minHeaderHeight += StaticClass.getStatusBarHeight(this);


        }
////////////////////////////////set style of list
        list_header = getLayoutInflater().inflate(R.layout.time_line_header, null);
        ImageView write_post = (ImageView) list_header.findViewById(R.id.write_post2);
        ImageView timeline_activity = (ImageView) list_header.findViewById(R.id.timeline_activity);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, TimeLineActivity.class);
                startActivity(i);

            }
        });




        LinearLayout about_linear = (LinearLayout) list_header.findViewById(R.id.about_linear);

        StaticClass.overrideFonts(this, list_header);

        initViews();
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        Animator = new IconAnimator(list_header);
        StikkyHeaderBuilder.stickTo(listView).setHeader(R.id.header, viewGroup)
                .minHeightHeader((int) minHeaderHeight)
                .animator(Animator)
                .build();


        listView.addHeaderView(list_header);


/////////////////////////////////////////////////////////////////////////////////////

        about_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, AboutProfileActivity.class);
                startActivity(i);
            }
        });


////////////////////////////////////
        write_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, WritePostActivity.class);
                startActivity(i);
            }
        });


        timeline_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, TimeLineActivity.class);
                startActivity(i);

            }
        });


    }


    private void initViews() {
        about_linear = list_header.findViewById(R.id.about_linear);


        about_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AboutProfileActivity.class));
            }
        });
    }





    public void Get_Timeline(final String token) {
        showProgressDialog();

        String url = Project_Web_Functions.BASE_URL + "/timeline/true?access_token=" + token;
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
                                Log.d("photo_user_from", photo + "k");

                                if (photo.equals("null")) {

                                    Log.d("null", "null");

                                } else {

                                    String imgeurl = "http://globanese.info/beta/uploads/users/" + user_id_from + "/" + photo;
                                    post.setPhoto(imgeurl);
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

    public void initPictureDialogCover(Context contex, final String type) {
        Log.d("init", type + "v");

        AlertDialog.Builder choosePic = new AlertDialog.Builder((Activity) contex);

        final View view = ((LayoutInflater) contex.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.photo_dialog_2, null);
        StaticClass.overrideFonts(this, view);


        TextView gallery = (TextView) view.findViewById(R.id.choose_pic_gallery);
        TextView camera = (TextView) view.findViewById(R.id.choose_pic_camera);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                setResult(RESULT_OK, intent);
                finish();


                con.startActivityForResult(intent.createChooser(intent, "Choose Image"), REQUEST_IMAGE_GALLERY_cover);
                choosePicDialog.cancel();

            }

        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                //intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);

                con.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_cover);

                choosePicDialog.cancel();

            }
        });

        choosePic.setView(view);
        choosePicDialog = choosePic.create();
        choosePicDialog.show();
    }


    public void initPictureDialogProfile(Context contex, final String type) {
        Log.d("init", type + "v");

        AlertDialog.Builder choosePic = new AlertDialog.Builder((Activity) contex);

        final View view = ((LayoutInflater) contex.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.photo_dialog_2, null);
        StaticClass.overrideFonts(this, view);


        TextView gallery = (TextView) view.findViewById(R.id.choose_pic_gallery);
        TextView camera = (TextView) view.findViewById(R.id.choose_pic_camera);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("type", type);
                setResult(RESULT_OK, intent);
                finish();


                con.startActivityForResult(intent.createChooser(intent, "Choose Image"), REQUEST_IMAGE_GALLERY);
                choosePicDialog.cancel();

            }

        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                //intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);

                con.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                choosePicDialog.cancel();

            }
        });

        choosePic.setView(view);
        choosePicDialog = choosePic.create();
        choosePicDialog.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(requestCode + "", resultCode + "");

        if (requestCode == REQUEST_IMAGE_GALLERY || requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                String type = extras.getString("type");


                if (extras != null) {

                    resultImage = extras.getParcelable("data");

                    image = convertBitmapToByte(resultImage);
                IconAnimator.i.setImageBitmap(resultImage);
                   IconAnimator.i2.setImageBitmap(resultImage);
                    //IconAnimator.prfilephoto.setImageBitmap(resultImage);
                    saveprofileimage();

                } else {
                    try {


                        Uri selectedimg = data.getData();
                        String realPath = new RealPathUtil().getPath(ProfileActivity.this, selectedimg);
                        Log.d("path", realPath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        resultImage = BitmapFactory.decodeFile(realPath, options);

                        if (resultImage != null) {
                            IconAnimator.cover.setImageBitmap(resultImage);
                            image = convertBitmapToByte(resultImage);
                            savecoverimage();

                        }
//

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                resultImage = null;
            }


        } else if (requestCode == REQUEST_IMAGE_GALLERY_cover || requestCode == REQUEST_IMAGE_CAPTURE_cover) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();


                if (extras != null) {

                    resultImage = extras.getParcelable("data");

                    image = convertBitmapToByte(resultImage);
                    IconAnimator.cover.setImageBitmap(resultImage);
                    savecoverimage();


                } else {
                    try {

                        Uri selectedimg = data.getData();
                        String realPath = new RealPathUtil().getPath(ProfileActivity.this, selectedimg);
                        Log.d("path", realPath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        resultImage = BitmapFactory.decodeFile(realPath, options);

                        if (resultImage != null) {
                            IconAnimator.cover.setImageBitmap(resultImage);
                            image = convertBitmapToByte(resultImage);
                            savecoverimage();

                        }
//

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                resultImage = null;
            }


        }

        ///here


//


    }

    public byte[] convertBitmapToByte(Bitmap bmp) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }


    public void savecoverimage() {
        Log.d("response cover image", "DOnEcover");
        showProgressDialog();
        String url = network.BASE_URL + "/image";
        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        Log.d("response cover image", response.toString());
                        try {


                            if (response.getBoolean("status") == (true)) {
                                Log.d("response", response.toString());
                                JSONObject photo_araay = response.getJSONObject("items");
                                String image = photo_araay.getString("photo");


                                Realm realm = RealmSingleton.getRealmInstance();
                                UserObject user = realm.where(UserObject.class).findFirst();
                                realm.beginTransaction();
                                dismissProgressDialog();
                                Log.d("image_url", image);
                                user.setCoverphoto(image);
                                realm.commitTransaction();


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
                        params.put("access_token", getLogInUser().getAccess_token());
                        params.put("type", "2");

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


    public void saveprofileimage() {
        Log.d("response  profile image", "DOnEprofile");
        showProgressDialog();
        String url = network.BASE_URL + "/image";
        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        Log.d("response cover image", response.toString());
                        try {


                            if (response.getBoolean("status") == (true)) {
                                Log.d("response", response.toString());
                                JSONObject photo_araay = response.getJSONObject("items");
                                String image = photo_araay.getString("photo");


                                Realm realm = RealmSingleton.getRealmInstance();
                                UserObject user = realm.where(UserObject.class).findFirst();
                                realm.beginTransaction();
                                // int id = user.getId();
                                dismissProgressDialog();
                                // String imgeurl = "http://globanese.info/beta/uploads/users/" + id + "/profile_" + image;
                                Log.d("image_url", image);
                                user.setPhoto(image);
                                realm.commitTransaction();


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
                        params.put("access_token", getLogInUser().getAccess_token());
                        params.put("type", "1");

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


////////////////////////////////////////////

class IconAnimator extends HeaderStikkyAnimator {
    public static ImageView cover;

    View list_header;
    TimeLineActivity time;
    TextView name, country;
    LinearLayout edit_coverphoto;
    ProfileActivity profile;
    public static CircleImageView i,i2;
    public static CircleImageView prfilephoto;

    IconAnimator(View list_header) {
        this.list_header = list_header;

    }


    @Override
    public AnimatorBuilder getAnimatorBuilder() {

        time = new TimeLineActivity();
        profile = new ProfileActivity();


        /////////////////name and country
        name = (TextView) list_header.findViewById(R.id.name);
        country = (TextView) list_header.findViewById(R.id.country);

        name.setText(time.fname + "" + time.lname);
        country.setText(time.country);


////////////////profile photo

       i = (CircleImageView) getHeader().findViewById(R.id.profile_image);
        i2 = (CircleImageView) list_header.findViewById(R.id.profile_image2);
        prfilephoto = (CircleImageView) list_header.findViewById(R.id.edit_profile_photo);
        prfilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.initPictureDialogProfile(ProfileActivity.con, "profile");
            }
        });

        if (time.imge_url != null)
        {
            Picasso.with(ProfileActivity.contex).load(time.imge_url).into(i);
            Picasso.with(ProfileActivity.contex).load(time.imge_url).into(i2);
        } else {

            i.setImageResource(R.drawable.user_image_placeholder);
            i2.setImageResource(R.drawable.user_image_placeholder);
        }


        ///////////////////////////////////////////////////////////////////////////
        //////////////////////////coverphoto//////////////////////////
        cover = (ImageView) getHeader().findViewById(R.id.cover_photo);

        edit_coverphoto = (LinearLayout) getHeader().findViewById(R.id.header_edit_layout);

       // edit_coverphoto.setVisibility(View.GONE);

        edit_coverphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.initPictureDialogCover(ProfileActivity.con, "cover");

            }
        });

        if (time.imge__cover_url != null)

        {
            Log.d("cover", time.imge__cover_url);
            Picasso.with(ProfileActivity.contex).load(time.imge__cover_url).into(cover);

        } else {
            cover.setImageResource(R.drawable.cover_placeholder);

        }
        /////////////////////////////////////////////////////////////////


        AnimatorBuilder animatorBuilder = AnimatorBuilder.create()

                .applyFade2(getHeader().findViewById(R.id.profile_image), 1, 0, new mi())
                .applyFade2(list_header.findViewById(R.id.profile_image2), 1, 0, new mi())
                .applyFade2(getHeader().findViewById(R.id.header_edit_layout), 1, 0, new mi());


        return animatorBuilder;
    }

    class mi implements Interpolator {

        @Override
        public float getInterpolation(float input) {
            return (input <= 0.9) ? 0 : (input - 0.9f) * 10f;
        }
    }


}
