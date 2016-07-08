package com.globanese.is.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;


import com.globanese.is.adapters.HorizantalListAdapterforUploadImage;
import com.globanese.is.model.ImageObject;
import com.globanese.is.model.UserObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.RequestClass;
import com.globanese.is.network.VolleyMultipartRequest;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.realm.RealmSingleton;
import com.globanese.is.utils.RealPathUtil;
import com.globanese.is.utils.StaticClass;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class WritePostActivity extends BaseActivity {



    ImageButton send_post;
    RequestClass req;
    byte[] image;
    String text;
    TextView  profile_name;
    ImageButton upload_photo, upload_video, upload_location, cancel;
    AlertDialog choosePicDialog;
    int REQUEST_IMAGE_CAPTURE = 333;
    int REQUEST_IMAGE_GALLERY = 100;
    Bitmap resultImage;
    public static List<ImageObject> List_photos;
    String location;
    HorizantalListAdapterforUploadImage b;
    ImageObject o;
    RecyclerView rv;
    LinearLayout layout_video, layout_image, layout_camera, layout_video2;

    Map<String, String> map;
    Boolean showimage, showvideo,sendpost = false;

    EditText video_link, profile_text;
    List<byte[]> Array_photo;
    ArrayList<String> Array_photo_id;
    String token;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
        map = new HashMap<>();
        req = new RequestClass();
        List_photos = new ArrayList<>();

       showvideo=false;
        showimage=false;
        sendpost=false;
        Intent i = getIntent();
        location = i.getStringExtra("loaction");


        Array_photo = new ArrayList<>();
        Array_photo_id = new ArrayList<>();


        rv = (RecyclerView) findViewById(R.id.recyclerView);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(2000);
        animator.setAddDuration(2000);
        rv.setItemAnimator(animator);
        final LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        b = new HorizantalListAdapterforUploadImage(getApplicationContext(), List_photos);
        b.notifyDataSetChanged();
        rv.setAdapter(b);


        b.setRecyclerViewListener(new HorizantalListAdapterforUploadImage.MyRecyclerViewListener() {
            @Override
            public void viewSelected(View v, int position) {

                List_photos.remove(position);
                b.notifyItemRemoved(position);
                b.notifyItemRangeChanged(position, List_photos.size());
                b.notifyDataSetChanged();
                Log.d("List_photos", String.valueOf(List_photos.size()));

            }
        });



        token = getLogInUser().getAccess_token();
        profile_name= (TextView)findViewById(R.id.profile_name);
        video_link = (EditText) findViewById(R.id.video_link);
        profile_text = (EditText) findViewById(R.id.profile_text);
        layout_video = (LinearLayout) findViewById(R.id.layout_video);
        layout_image = (LinearLayout) findViewById(R.id.layout_image);
        layout_camera = (LinearLayout) findViewById(R.id.layout_camera);
        layout_video2 = (LinearLayout) findViewById(R.id.layout_video2);
        upload_location = (ImageButton) findViewById(R.id.writepost_upload_loaction);
        send_post = (ImageButton) findViewById(R.id.send_post);
        cancel = (ImageButton) findViewById(R.id.back);




///////////////////////////////////////////////////////////////////////////user _information

        CircleImageView cimage = (CircleImageView) findViewById(R.id.profile_image);

        BaseActivity b = new BaseActivity();
        UserObject user = b.getUserObject();
        String imge_url=b.getprofilephoto();

        if (imge_url != null)

        {
            Picasso.with(WritePostActivity.this).load(imge_url).into(cimage);
            Log.d("photo",imge_url);
        } else {

            cimage.setImageResource(R.drawable.user_image_placeholder);
       }



        if (user.getFname() != null&&user.getLname() != null)

        profile_name.setText(user.getFname()+""+user.getLname());


        if (user.getFname() != null)

            profile_name.setText(user.getFname());





        upload_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WritePostActivity.this, LocationActivity.class);
                startActivity(i);
            }
        });


        layout_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_link.setText(null);
                layout_image.setVisibility(View.VISIBLE);
                layout_video.setVisibility(View.GONE);
                showimage = true;
                choosePicDialog.show();
            }
        });

        initPictureDialog();

        layout_video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Array_photo_id.clear();
                Array_photo.clear();

                layout_video.setVisibility(View.VISIBLE);
                layout_image.setVisibility(View.GONE);
                showvideo = true;

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WritePostActivity.this, ProfileActivity.class);
                startActivity(i);

            }
        });


        send_post.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
            text = profile_text.getText().toString();

            boolean status= checkvalidate();
             if(status==true){


                if (Array_photo.size() > 0) {
                    for (int i = 0; i < Array_photo.size(); i++) {
                        upload_image(token, Array_photo.get(i));
                    }
                }

                else {


                    map.put("privacy", "1");

                    if (profile_text.getText().toString() != null)
                        Log.d("text", text);
                    map.put("text", text);


                    if (location != null && !location.isEmpty()) {
                        map.put("location", location);
                        Log.d("location", location);
                    }
                    Log.d("boolean",showvideo.toString());

                    if (showvideo == true) {
                        map.put("video", video_link.getText().toString());
                    }


                    Send_Post(token, map);
                }



             }
                else{

                 Toast.makeText(getApplicationContext(),"Enter Text or video link or Photo to make Post",Toast.LENGTH_SHORT).show();





             }






            }
        });

    }

    private boolean checkvalidate() {

        if( profile_text.getText().toString()!=null|(showvideo==true&&video_link.getText().toString()!=null)|(showimage==true &&Array_photo.size() > 0)){

            sendpost=true;


        }
        return sendpost;



    }



    public void initPictureDialog() {

        AlertDialog.Builder choosePic = new AlertDialog.Builder(this);

        final View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.photo_dialog, null);
        StaticClass.overrideFonts(this, view);


        LinearLayout gallery = (LinearLayout) view.findViewById(R.id.choose_pic_gallery);
        LinearLayout camera = (LinearLayout) view.findViewById(R.id.choose_pic_camera);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);

                startActivityForResult(intent.createChooser(intent, "Choose Image"), REQUEST_IMAGE_GALLERY);
                choosePicDialog.cancel();
            }

        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//


                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
//
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                choosePicDialog.cancel();
            }
        });


        choosePic.setView(view);
        choosePicDialog = choosePic.create();


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(requestCode + "", resultCode + ":" + List_photos.size());

        if (requestCode == REQUEST_IMAGE_GALLERY || requestCode == REQUEST_IMAGE_CAPTURE) {

            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                if (extras != null) {

                    resultImage = extras.getParcelable("data");
                    image = convertBitmapToByte(resultImage);
                    Array_photo.add(image);

                    Log.e("array size", List_photos.size() + " ");

                } else {
                    try {

                        Uri selectedimg = data.getData();
                        String realPath = new RealPathUtil().getPath(WritePostActivity.this, selectedimg);
                        Log.d("path", realPath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        resultImage = BitmapFactory.decodeFile(realPath, options);
//

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                resultImage = null;
            }


            if (resultImage != null) {
                o = new ImageObject();
                o.setPhoto(resultImage);
                List_photos.add(o);


            }

            Log.e("array size 2", List_photos.size() + "");
            b.notifyDataSetChanged();


        }
//


    }


    public byte[] convertBitmapToByte(Bitmap bmp) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }

    public void upload_image(final String token, final byte[] image) {
        showProgressDialog();
        String url =   Project_Web_Functions.BASE_URL+"/image";
        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") == (true)) {
                                Log.d("response", response.toString());
                                JSONObject photo_araay = response.getJSONObject("items");
                                String image = photo_araay.getString("photo");
                                String image_id = photo_araay.getString("id");


                                Realm realm = RealmSingleton.getRealmInstance();
                                UserObject user = realm.where(UserObject.class).findFirst();
                                realm.beginTransaction();
                                int id = user.getId();
                                realm.commitTransaction();

                                String imgeurl = "http://globanese.info/beta/uploads/users/" + id + "/profile_" + image;
                                Log.d("image_url", imgeurl);

                                Array_photo_id.add(image_id);
                                MakePostReqest(Array_photo_id);


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
                        params.put("type", "0");
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

    public void MakePostReqest(ArrayList<String> array) {

        map.put("privacy", "1");

        if (profile_text.getText().toString() != null)
            Log.d("text", text);
        map.put("text", text);


        if (location != null && !location.isEmpty()) {
            map.put("location", location);
            Log.d("location", location);
        }


        if (showimage == true) {
            int i = 0;
            for (String object : array) {
                map.put("photo_ids[" + (i++) + "]", object);
                Log.d("photo", object);
            }

        }
        if (showvideo == true) {
            map.put("video", video_link.getText().toString());

        }


        Send_Post(token, map);

    }






    public void Send_Post(final String token, final Map<String, String> map) {

        String url =    Project_Web_Functions.BASE_URL+"/post";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getBoolean("status") == (true)) {
                        dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), "Post Successfully ", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(WritePostActivity.this, ProfileActivity.class);
                        startActivity(i);

                    }
                    else{
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
                params.put("access_token", token);
                params.putAll(map);

                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);





    }





}
