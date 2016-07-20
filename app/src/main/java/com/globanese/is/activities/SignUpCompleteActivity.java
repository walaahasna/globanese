package com.globanese.is.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.adapters.CommunitiesAdapter;
import com.globanese.is.adapters.StringAdapter;
import com.globanese.is.model.CommunityObject;
import com.globanese.is.model.UserObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;
import com.globanese.is.network.VolleyMultipartRequest;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.realm.RealmSingleton;
import com.globanese.is.utils.RealPathUtil;
import com.globanese.is.utils.StaticClass;
import com.kennyc.bottomsheet.BottomSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

/**
 * Created by walaa on 26/5/2016.
 */
public class SignUpCompleteActivity extends BaseActivity {
    LinearLayout c, n;
    byte[] image;
    View close, closee;
    CircleImageView profile_image;
    TextView gender_text;
    TextView dob_text;
    View community_layout;
    TextView community_text, community_text2, text;
    Button start_button, start_buttonn;
    View name_layout;
    EditText fname;
    EditText lname;
    EditText email;
    String gender;

    AlertDialog choosePicDialog;
    int REQUEST_IMAGE_CAPTURE = 333;
    int REQUEST_IMAGE_GALLERY = 100;
    Bitmap resultImage;

    ArrayList<CommunityObject> communityObjects;
    CommunityObject selected_community;
    FrameLayout dob, gendert, comuity;
    public boolean show_gender, show_DB, show_community, showImge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_complete);

        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
        dob = (FrameLayout) findViewById(R.id.dob_layoutt);
        gendert = (FrameLayout) findViewById(R.id.gender_layoutt);
        comuity = (FrameLayout) findViewById(R.id.community_layoutt);


        c = (LinearLayout) findViewById(R.id.complete);

        closee = (View) c.findViewById(R.id.closee);
        profile_image = (CircleImageView) c.findViewById(R.id.profile_imagee);
        gender_text = (TextView) c.findViewById(R.id.gender_textt);
        dob_text = (TextView) c.findViewById(R.id.dob_textt);
        community_text = (TextView) c.findViewById(R.id.community_textt);
        text = (TextView) c.findViewById(R.id.profile_image_font);
//
        TextView com2 = (TextView) c.findViewById(R.id.complete_profile_2);
        Typeface type2 = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        com2.setTypeface(type2);

        TextView com3 = (TextView) c.findViewById(R.id.profile_image_font);
        Typeface type3 = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        com3.setTypeface(type3);


        start_buttonn = (Button) c.findViewById(R.id.start_buttonn);


//////////////////////////////////////////////////////c_linear


        closee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

////////////////////////////////////////////////////////////////////////////////////////////
        gender_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(SignUpCompleteActivity.this);
                View view = li.inflate(R.layout.bottom_sheet_gender_layout, null);


                final BottomSheet bs = new BottomSheet.Builder(SignUpCompleteActivity.this)//.setStyle(R.style.BottomSheet)
                        .setView(view).create();
                bs.show();


                final ListView listView = (ListView) view.findViewById(R.id.list);
                final ArrayList<String> list = new ArrayList<>();
                list.add("Male");
                list.add("Female");
                list.add("Other");
                listView.setAdapter(new StringAdapter(SignUpCompleteActivity.this, R.layout.list_string_item, list));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((StringAdapter) listView.getAdapter()).setSelected_index(position);
                        if (list.get(((StringAdapter) listView.getAdapter()).getSelected_index()).equals("Male")) {
                            gender = "1";
                        } else {

                            gender = "0";
                        }

                        gender_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                        bs.dismiss();
                    }
                });


                View sheet_cancel = view.findViewById(R.id.sheet_cancel);
                View sheet_ok = view.findViewById(R.id.sheet_ok);


                sheet_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bs.dismiss();
                    }
                });

                sheet_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gender_text.setText(list.get(((StringAdapter) listView.getAdapter()).getSelected_index()));

                        if (list.get(((StringAdapter) listView.getAdapter()).getSelected_index()).equals("Male")) {
                            gender = "1";
                        } else {
                            gender = "0";
                        }

                        gender_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                        bs.dismiss();
                    }
                });
            }
        });

///////////////////////////////////////////////////////////////////////////////

        dob_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(SignUpCompleteActivity.this);
                View view = li.inflate(R.layout.bottom_sheet_dob_layout, null);
                View sheet_cancel = view.findViewById(R.id.sheet_cancel);
                View sheet_ok = view.findViewById(R.id.sheet_ok);

                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);

                final BottomSheet bs = new BottomSheet.Builder(SignUpCompleteActivity.this)
                        //.setStyle(R.style.BottomSheet)
                        .setView(view).create();
                bs.show();
                sheet_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bs.dismiss();
                    }
                });

                datePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        dob_text.setText(datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());
                        dob_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                        bs.dismiss();
                    }

                });
                sheet_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dob_text.setText(datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());
                        dob_text.setTextColor(getResources().getColor(R.color.colorPrimary));

                        bs.dismiss();

                    }
                });
            }
        });
/////////////////////////////////////////////////////////////////

        community_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (communityObjects == null) {
                    return;
                }

                LayoutInflater li = LayoutInflater.from(SignUpCompleteActivity.this);
                View view = li.inflate(R.layout.bottom_sheet_gender_layout, null);
                final ListView listView = (ListView) view.findViewById(R.id.list);

                final BottomSheet bs = new BottomSheet.Builder(SignUpCompleteActivity.this)

                        .setView(view).create();
                bs.show();
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText("Communities");

                listView.setAdapter(new CommunitiesAdapter(SignUpCompleteActivity.this, R.layout.list_string_item_2, communityObjects));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((CommunitiesAdapter) listView.getAdapter()).setSelected_index(position);
                        selected_community = communityObjects.
                                get(((CommunitiesAdapter) listView.getAdapter()).getSelected_index());
                        Log.e("is:", selected_community.getId() + "");
                        community_text.setText(selected_community.getName());

                        community_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                        bs.dismiss();
                    }
                });

                View sheet_cancel = view.findViewById(R.id.sheet_cancel);
                View sheet_ok = view.findViewById(R.id.sheet_ok);


                sheet_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bs.dismiss();


                    }
                });

                sheet_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected_community = communityObjects.
                                get(((CommunitiesAdapter) listView.getAdapter()).getSelected_index());
                        Log.e("is:", selected_community.getId() + "");
                        community_text.setText(selected_community.getName());
                        community_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                        bs.dismiss();

                    }
                });
            }
        });
///


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicDialog.show();
            }
        });


        initPictureDialog();


//////////////////////////////

        ////////////////////////////////Strat Button For Image ////////////////////
        start_buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showImge == true)
                saveProfileAccount();
                updateUserInfo();


            }
        });



        if (showImge == false && show_gender == false && show_DB == false)
            checkRequiredInfo();




    }



    private void checkRequiredInfo() {

        ArrayList<String> info = getUserObject().getEmptyRequiredUserInfo();

        if (info.size() == 0) {
            finish();
        } else {
            for (int i = 0; i < info.size(); i++) {
                if (info.get(i).equals("gender")) {
                    gendert.setVisibility(View.VISIBLE);
                    show_gender = true;

                    continue;
                }

                if (info.get(i).equals("dob")) {
                    dob.setVisibility(View.VISIBLE);
                    show_DB = true;

                    continue;
                }
                if (info.get(i).equals("community")) {
                    comuity.setVisibility(View.VISIBLE);
                    show_community = true;
                    getCommunities();
                }
                if (info.get(i).equals("photo")) {
                    profile_image.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    showImge = true;
                }


            }
        }
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
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
//                intent.putExtra("outputX", IMAGE_WIDTH);
//                intent.putExtra("outputY", IMAGE_HIGHT);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                choosePicDialog.cancel();
            }
        });


        choosePic.setView(view);
        choosePicDialog = choosePic.create();


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(requestCode + "", resultCode + "");

        if (requestCode == REQUEST_IMAGE_GALLERY || requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                if (extras != null) {
                    resultImage = extras.getParcelable("data");
                    profile_image.setImageBitmap(resultImage);
                    image = convertBitmapToString(resultImage);



                } else {
                    try {
                        Uri selectedimg = data.getData();
                        String realPath = new RealPathUtil().getPath(SignUpCompleteActivity.this, selectedimg);
                        Log.d("path", realPath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        resultImage = BitmapFactory.decodeFile(realPath, options);
//
                        if (resultImage != null) {
                            profile_image.setImageBitmap(resultImage);
                            image = convertBitmapToString(resultImage);
                            showImge = true;
                            show_gender = true;
                            show_DB = true;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                resultImage = null;
//                userImage.setImageDrawable(getResources().getDrawable(R.drawable.user));
            }


            showImge = true;
            show_gender = true;
            show_DB = true;
        }
//


    }

    public void getCommunities() {
        showProgressDialog();
        new Project_Web_Functions().GetCommunities(getLogInUser().getAccess_token(), new UniversalCallBack() {
            @Override
            public void onResponse(Object result) {
                ResponseObject responseObject = (ResponseObject) result;
                if (responseObject.getStatus()) {
                    communityObjects = (ArrayList<CommunityObject>) responseObject.getItems();

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

    private boolean validateComplete() {
        clearEditTextError(fname);
        clearEditTextError(lname);
        clearEditTextError(email);
        clearEditTextError(community_layout);


        boolean ret = true;
        if (name_layout.getVisibility() == View.VISIBLE) {
            if (fname.getText().toString().trim().isEmpty()) {
                setEditTextError(fname);
                ret = false;
            }
            if (lname.getText().toString().trim().isEmpty()) {
                setEditTextError(lname);
                ret = false;
            }
        }


        if (email.getVisibility() == View.VISIBLE) {
            if (email.getText().toString().trim().isEmpty()) {
                setEditTextError(email);
                ret = false;
            }
        }

        if (community_layout.getVisibility() == View.VISIBLE) {
            if (selected_community == null) {
                setEditTextError(community_layout);
                ret = false;
            }
        }

        return ret;
    }

    private void setEditTextError(View editText) {
        editText.setBackgroundResource(R.drawable.round_login_edittext_error);
    }

    private void clearEditTextError(View editText) {
        editText.setBackgroundResource(R.drawable.round_login_edittext);
    }


    private void updateUserInfo() {


        Map<String, String> map = new HashMap<>();

        if (show_community == true) {
            map.put("community_id", selected_community.getId() + "");

        }
        if (show_gender == true) {
            Log.e("visibilty", "yes");
            map.put("gender", gender);

        }


        if (show_DB == true) {
            map.put("dob", dob_text.getText().toString().trim());

        }


        new Project_Web_Functions().UpdateUserInfo(getLogInUser().getAccess_token(), map, new UniversalCallBack() {
            @Override
            public void onResponse(Object result) {
                ResponseObject responseObject = (ResponseObject) result;
                if (responseObject.getStatus()) {


                    Toast.makeText(SignUpCompleteActivity.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SignUpCompleteActivity.this, TimeLineActivity.class);
                    startActivity(i);
                    finish();
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

//////////////////////////////////////convert_method

    public byte[] convertBitmapToString(Bitmap bmp) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }


    private void saveProfileAccount() {
        showProgressDialog();
        String url = "http://globanese.info/beta/api/v1/image";
        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {


                            if (response.getBoolean("status") == (true)) {
                                Log.d("response", response.toString());
                                JSONObject photo_araay = response.getJSONObject("items");
                                String image = photo_araay.getString("photo");


                                Realm realm = RealmSingleton.getRealmInstance();
                                UserObject user = realm.where(UserObject.class).findFirst();
                                realm.beginTransaction();
                                int id = user.getId();

                               // String imgeurl = "http://globanese.info/beta/uploads/users/" + id + "/profile_" + image;
                                Log.d("image_url",image);
                                user.setPhoto(image );
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

                        if (showImge == true) {

                            params.put("photo", new DataPart("image.jpeg", image, "image/jpeg"));
                        }
                        return params;
                    }


                };


        VolleySingleton.getInstance().addToRequestQueue(multipartRequest);


    }


}
