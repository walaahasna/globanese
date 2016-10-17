package com.globanese.is.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
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
import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;


public class WritePostActivity extends BaseActivity {
    Boolean endofarray;
    ImageButton send_post;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    RequestClass req;
    byte[] image;
    String text;
    TextView profile_name;
    ImageButton upload_photo, upload_video, upload_location;
    ImageView cancel;
    AlertDialog choosePicDialog;
    int REQUEST_IMAGE_CAPTURE = 333;
    int REQUEST_IMAGE_GALLERY = 100;
    Bitmap resultImage;
    public static List<ImageObject> List_photos;
    public static String location;
    HorizantalListAdapterforUploadImage b;
    ImageObject o;
    RecyclerView rv;
    LinearLayout layout_video, layout_image, layout_camera, layout_video2, layout_location;
    TextView locationn;
    Map<String, String> map;
    Boolean showimage, showvideo, sendpost = false;

    EditText video_link, profile_text;
    List<byte[]> Array_photo;
    ArrayList<String> Array_photo_id;
    String token;
    TimeLineActivity time;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
 time=new TimeLineActivity();

        map = new HashMap<>();
        req = new RequestClass();
        List_photos = new ArrayList<>();

        showvideo = false;
        showimage = false;
        sendpost = false;
        endofarray = false;


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
                Array_photo.remove(position);
                b.notifyItemRemoved(position);
                b.notifyItemRangeChanged(position, List_photos.size());
                b.notifyDataSetChanged();
                Log.d("List_photos", String.valueOf(List_photos.size()));

            }
        });


        token = getLogInUser().getAccess_token();
        profile_name = (TextView) findViewById(R.id.profile_name);
        locationn = (TextView) findViewById(R.id.location);
        TextView in = (TextView) findViewById(R.id.in);
        video_link = (EditText) findViewById(R.id.video_link);
        profile_text = (EditText) findViewById(R.id.profile_text);
        layout_video = (LinearLayout) findViewById(R.id.layout_video);
        layout_image = (LinearLayout) findViewById(R.id.layout_image);
        layout_camera = (LinearLayout) findViewById(R.id.layout_camera);
        layout_video2 = (LinearLayout) findViewById(R.id.layout_video2);
        layout_location = (LinearLayout) findViewById(R.id.layout_location);
        upload_location = (ImageButton) findViewById(R.id.writepost_upload_loaction);
        send_post = (ImageButton) findViewById(R.id.send_post);
        cancel = (ImageView) findViewById(R.id.closee);

        Intent i = getIntent();
        location = i.getStringExtra("loaction");


        if (location != null) {
            in.setVisibility(View.VISIBLE);
            locationn.setVisibility(View.VISIBLE);
            locationn.setText(location);
        }

///////////////////////////////////////////////////////////////////////////user _information

        CircleImageView cimage = (CircleImageView) findViewById(R.id.profile_image);

       BaseActivity b = new BaseActivity();
        UserObject user = b.getUserObject();
        //String imge_url = b.getprofilephoto();
//Log.d("imge_face",imge_url);

        if (time.imge_url!= null)

        {
            Picasso.with(WritePostActivity.this).load(time.imge_url).into(cimage);
            Log.d("photo", time.imge_url);
        } else {

            cimage.setImageResource(R.drawable.user_image_placeholder);
        }


////////////////////////////////////////////////////////////////////////////////////
        if (user.getFname() != null && user.getLname() != null)

            profile_name.setText(user.getFname() + "" + user.getLname());
        Log.d("name",user.getFname() + "" + user.getLname());


        if (user.getFname() != null)

      profile_name.setText(user.getFname());


        layout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WritePostActivity.this, LocationActivity.class);
                startActivity(i);
            }
        });


        layout_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
                video_link.setText(null);
                layout_image.setVisibility(View.VISIBLE);
                layout_video.setVisibility(View.GONE);
                showimage = true;
               // choosePicDialog.show();
            }
        });

        initPictureDialog();


        layout_video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_link.requestFocus();
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

//////////////////////button

        send_post.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                showProgressDialog();
                text = profile_text.getText().toString();


                    if (Array_photo.size() > 0) {

                        Log.d("Array_photo.size()", String.valueOf(Array_photo.size()));
                        for (int i = 0; i < Array_photo.size(); i++) {


                            upload_image(token, Array_photo.get(i));
                            Log.d("Array_photo.size()", String.valueOf(i));
                        }


                    } else {


                        map.put("privacy", "1");

                        if (profile_text.getText().toString() != null)
                            Log.d("text", text);
                        map.put("text", text);


                        if (location != null && !location.isEmpty()) {
                            map.put("location", location);
                            Log.d("location", location);
                        }
                        Log.d("boolean", showvideo.toString());

                        if (showvideo == true) {
                            map.put("video", video_link.getText().toString());
                        }


                        Send_Post(token, map);
                    }




            }
        });

    }


    private boolean checkvalidate() {

        if (profile_text.getText().toString() != null | (showvideo == true && video_link.getText().toString() != null) | (showimage == true && Array_photo.size() > 0)) {

            sendpost = true;


        }
        return sendpost;


    }


    public void initPictureDialog() {

        AlertDialog.Builder choosePic = new AlertDialog.Builder(this);

        final View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.photo_dialog_2, null);
        StaticClass.overrideFonts(this, view);


        //  LinearLayout gallery = (LinearLayout) view.findViewById(R.id.choose_pic_gallery);
        //  LinearLayout camera = (LinearLayout) view.findViewById(R.id.choose_pic_camera);

        TextView gallery = (TextView) view.findViewById(R.id.choose_pic_gallery);
        TextView camera = (TextView) view.findViewById(R.id.choose_pic_camera);


        /////croup

            gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent   intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
               intent.setType("image/jpg");
                intent.putExtra("crop", "false");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
//              intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                choosePicDialog.cancel();

            }
        });



        choosePic.setView(view);
        choosePicDialog = choosePic.create();


    }


  /*  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(requestCode + "", resultCode + "");

        if (requestCode == REQUEST_IMAGE_GALLERY || requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();

                if (extras != null) {
                    resultImage = extras.getParcelable("data");
                    image = convertBitmapToByte(resultImage);
                    Array_photo.add(image);
                    ArrayList<Uri>  image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream( image_uris.get(0)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


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
*/


  protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
      super.onActivityResult(requestCode, resuleCode, intent);

      if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK ) {

          ArrayList<Uri>  image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

Log.d("sizenew", String.valueOf(image_uris.size()));

        for(int i=0;i<image_uris.size();i++) {


            Bitmap bm = null;
            //bm = BitmapFactory.decodeStream(getContentResolver().openInputStream( image_uris.get(i)));
            bm = BitmapFactory.decodeFile(String.valueOf(image_uris.get(i)));

            // bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), image_uris.get(i));


            image = convertBitmapToByte(bm);
            Array_photo.add(image);

            if (image_uris.get(i) != null) {
                o = new ImageObject();
                o.setPhoto(bm);
                List_photos.add(o);
                b.notifyDataSetChanged();

                Log.d("sizeHORI", String.valueOf( List_photos.size()));

            }
        }



      }
  }

    public byte[] convertBitmapToByte(Bitmap bmp) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }

    static int uploadedImagesCount = 0;




    ////upload
    public void upload_image(final String token, final byte[] image) {
        showProgressDialog();
        Log.d("image", image.length + "");

        String url = Project_Web_Functions.BASE_URL + "/image";
        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") == (true)) {
                                uploadedImagesCount=uploadedImagesCount+1;
                                Log.d("response", response.toString());
                                JSONObject photo_araay = response.getJSONObject("items");
                                String image = photo_araay.getString("photo");
                                String image_id = photo_araay.getString("id");



                                Realm realm = RealmSingleton.getRealmInstance();
                                UserObject user = realm.where(UserObject.class).findFirst();
                                realm.beginTransaction();
                                int id = user.getId();
                                realm.commitTransaction();


                                String imgeurl = "http://globanese.info/beta/uploads/users/" + id + "/" + image;
                                Log.d("image_url", imgeurl);
                                Log.d("image_id", image_id);
                                Array_photo_id.add(image_id);
                               Log.d("Array_photo_id size", Array_photo_id.size() + ": k");
                               Log.d("uploadedImagesCount_b", String.valueOf(uploadedImagesCount));
                               Log.d("size_photo_b", String.valueOf(Array_photo.size()));
                               Log.d("size_photo_id_b", String.valueOf(Array_photo_id.size()));


                             if (Array_photo.size()==Array_photo_id.size()) {
                                    Log.d("uploadedImagesCount", String.valueOf(uploadedImagesCount));
                                    Log.d("size_photo_uplaod", String.valueOf(Array_photo.size()));
                                    Log.d("size_photo_id_upload", String.valueOf(Array_photo_id.size()));
                                    MakePostReqest(Array_photo_id);

                                }

                               /* else{
                                    dismissProgressDialog();
                                    Toast.makeText(getApplicationContext(),"Falid to upload image Tray Again",Toast.LENGTH_SHORT).show();

                                }*/

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
///////makepost
    public void MakePostReqest(ArrayList<String> array) {

        map.put("privacy", "1");


        if (profile_text.getText().toString() != null)
            Log.d("text", text);
        map.put("text", text);


        if (location != null && !location.isEmpty()) {
            map.put("location", location);
            Log.d("location", location);
        }

        Log.d("arraypost", array.size() + "");



        if (showimage == true) {
            int i = 0;
            for (String object : array) {
                Log.d("photojj", object + " " + i);
                map.put("photo_ids[" + (i++) + "]", object);
            }

        }
        if (showvideo == true) {
            map.put("video", video_link.getText().toString());

        }


        Send_Post(token, map);

    }

///////////////final
    public void Send_Post(final String token, final Map<String, String> map) {

        String url = Project_Web_Functions.BASE_URL + "/post";
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
                        Intent i = new Intent(WritePostActivity.this, TimeLineActivity.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(getApplicationContext(), "error ", Toast.LENGTH_SHORT).show();

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


      int socketTimeout = 30000;//30 seconds - change to what you want
       RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance().addToRequestQueue(stringRequest);

    }

    private void getImages() {

        Intent intent  = new Intent(this, ImagePickerActivity.class);
        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);

    }

}
