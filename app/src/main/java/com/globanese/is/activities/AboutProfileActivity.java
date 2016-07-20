package com.globanese.is.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutProfileActivity extends BaseActivity {

    public static TextView edit_address, edit_community, edit_phone, edit_dob, edit_language, edit_nationality, edit_job;
    public static TextView textview_dobcountry, textView_address, textView_community, textview_dob, textview_phone, textView_language, textView_nationality, textview_job_name, textview_job_place;
    public static String aadress1, country, dob, phone, postion, company, language, nationality, website, country_dob;
    @InjectView(R.id.back)
    View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_about);
        ButterKnife.inject(this, this);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
        final String token = getLogInUser().getAccess_token();


        getUserInfo(token);


        edit_address = (TextView) findViewById(R.id.edit_address);
        edit_community = (TextView) findViewById(R.id.edit_community);
        edit_phone = (TextView) findViewById(R.id.edit_phone);
        edit_dob = (TextView) findViewById(R.id.edit_dob);
        edit_language = (TextView) findViewById(R.id.edit_language);
        edit_nationality = (TextView) findViewById(R.id.edit_nationality);
        edit_job = (TextView) findViewById(R.id.edit_job);
////////////////////////////////////////////

        textView_address = (TextView) findViewById(R.id.textview_address);
        textView_community = (TextView) findViewById(R.id.textview_community);
        textview_phone = (TextView) findViewById(R.id.textview_phone);
        textview_dob = (TextView) findViewById(R.id.textview_dob);
        textView_language = (TextView) findViewById(R.id.textview_language);
        textView_nationality = (TextView) findViewById(R.id.textview_nationality);
        textview_job_name = (TextView) findViewById(R.id.textview_job_name);
        textview_job_place = (TextView) findViewById(R.id.textview_job_place);
        textview_dobcountry = (TextView) findViewById(R.id.textview_dob2);

        //getUserInfo();


     /*   RealmResults<AboutObject> o = getAboutObject();

        String address = null;
        String   date = null;
        String  country=null;
        String community = null;

        for (AboutObject i : o) {
            address = i.getAddress();
            community = i.getCommunity();
             date=  i.getDob_date();
            country=i.getDob_country()   ;
        }

        System.out.print( address+"\n+"+community+"\n+"+date+"\n"+  country);*/


//////////////////////////////////////////////////////////////


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutProfileActivity.this,ProfileActivity.class);
                startActivity(i);
            }
        });
        ///////////////////////////////////////////
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent address = new Intent(AboutProfileActivity.this, EditAddressActivity.class);

                startActivity(address);

            }
        });
///////////////////////////////////////////////////
        edit_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent community = new Intent(AboutProfileActivity.this, EditCommunityActivity.class);
                startActivity(community);

            }
        });


        /////////////////////////////////
        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutProfileActivity.this, EditphoneActivity.class);
                startActivity(i);

            }
        });
        /////////////////////////////
        edit_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutProfileActivity.this, EditDobActivity.class);
                startActivity(i);

            }
        });
        //////////////////////////

        edit_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutProfileActivity.this, EdiLanguageActivity.class);
                startActivity(i);

            }
        });


        ////////////////////////
        edit_nationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutProfileActivity.this, EditNationalityActivity.class);
                startActivity(i);

            }
        });


        ///////////////////////////////
        edit_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutProfileActivity.this, EditJobActivity.class);
                startActivity(i);

            }
        });


        textview_job_place.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://"+website));
                startActivity(intent);
            }
        });


    }


    ////////////////////////////////sebd request to server to get data about

    void getUserInfo(String token) {


        showProgressDialog();

        String url = Project_Web_Functions.BASE_URL+"/user?access_token=" + token;
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
                        if (!items.isNull("address")) {
                            aadress1 = items.getString("address");
                            textView_address.setText(aadress1);
                            Log.d("adress", aadress1);
                        }


                        if (!items.isNull("country_d_o_b")) {
                            JSONObject countrydob = items.getJSONObject("country_d_o_b");
                            if (!countrydob.isNull("name")) {
                                country_dob = countrydob.getString("name");
                                textview_dobcountry.setText(" , in " + country_dob);
                            }

                        }



                        if (!items.isNull("dob")) {

                            dob = items.getString("dob");
                            textview_dob.setText(dob);

                        }


                        JSONArray phone_araay = items.getJSONArray("user_phone");
                        final int phone_length = phone_araay.length();
                        for (int i = 0; i < phone_length; i++) {
                            JSONObject phoneobject = phone_araay.getJSONObject(i);
                            if (!phoneobject.isNull("phone")) {
                                phone = phoneobject.getString("phone");
                                textview_phone.setText(phone);
                            }
                        }


                        JSONArray job_araay = items.getJSONArray("user_job");
                        final int user_length = job_araay.length();
                        for (int ii = 0; ii < user_length; ii++) {
                            JSONObject jobobject = job_araay.getJSONObject(ii);


                            if (!jobobject.isNull("position") && !jobobject.isNull("company") && !jobobject.isNull("website")) {
                                postion = jobobject.getString("position");
                                company = jobobject.getString("company");
                                website = jobobject.getString("website");

                                textview_job_name.setText(company);
                                textview_job_place.setText(postion);
                            }


                        }


                        JSONArray language_araay = items.getJSONArray("language");
                        String temp2 = "";
                        final int lan_length = language_araay.length();
                        for (int ii = 0; ii < lan_length; ii++) {

                            JSONObject jobobject = language_araay.getJSONObject(ii);
                            language = jobobject.getString("name");

                            textView_language.setText(null);
                            temp2 = temp2 + language + " , ";
                            textView_language.setText(temp2);
                        }


                        JSONArray nationality_araay = items.getJSONArray("nationality");
                        String temp = "";
                        final int nat_length = nationality_araay.length();
                        for (int ii = 0; ii < nat_length; ii++) {

                            JSONObject jobobject = nationality_araay.getJSONObject(ii);
                            nationality = jobobject.getString("name");

                            textView_nationality.setText(null);
                            temp = temp + nationality + " , ";
                            textView_nationality.setText(temp);
                        }


                        if(!items.isNull("community")){

                        JSONObject comunit = items.getJSONObject("community");
                        if (!comunit.isNull("country"))
                            country = comunit.getString("country");

                        textView_community.setText(country);

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


}
