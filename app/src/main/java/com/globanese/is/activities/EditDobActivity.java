package com.globanese.is.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.adapters.CommunityAdapterForAutocompltet;
import com.globanese.is.adapters.NationalityAdapter;
import com.globanese.is.model.AboutProfileObject;
import com.globanese.is.model.NationalityObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.kennyc.bottomsheet.BottomSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class EditDobActivity extends BaseActivity  {
TextView edit_dob;
    List<String> list;
    String date,a;
    NationalityObject aa;
    public static List<NationalityObject> ListNASH;
    List<NationalityObject> Nationalites;
    AutoCompleteTextView actv;
    Context con;
    NationalityAdapter adapter;
    TextView  done;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dob);
        ListNASH=new ArrayList<>();
        list = new ArrayList<String>();
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setThreshold(1);

        con=EditDobActivity.this;

        edit_dob=(TextView)findViewById(R.id.edit_dob) ;

        if(AboutProfileActivity.dob!=null)
            edit_dob.setText(AboutProfileActivity.dob);

        if(AboutProfileActivity.country_dob!=null)
        actv.setText(AboutProfileActivity.country_dob);



////////////////////////////
      final String  token=getLogInUser().getAccess_token();

     // edit_dob.setText(AboutProfileActivity.dob);
       // actv.setText(AboutProfileActivity.country_dob);
        done=(TextView)findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int idd = 0;
                for (NationalityObject str : ListNASH) {
                    idd = str.getId();


                }
                Adddob(token,edit_dob.getText().toString(), String.valueOf(idd));

            }
        });


        /////////////////////////////////////////////////





        ImageView back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EditDobActivity.this,AboutProfileActivity.class);
                startActivity(i);
            }
        });









        /////////////////////////////////////////////////////////////////////////
       edit_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 LayoutInflater li = LayoutInflater.from(EditDobActivity.this);
                View view=li.inflate(R.layout.bottom_sheet_dob_layout,null);
                View sheet_cancel=view.findViewById(R.id.sheet_cancel);
                View sheet_ok=view.findViewById(R.id.sheet_ok);

                final DatePicker datePicker=(DatePicker) view.findViewById(R.id.datePicker);

                final BottomSheet bs=new BottomSheet.Builder(EditDobActivity.this)
                        //.setStyle(R.style.BottomSheet)
                        .setView(view).create();
                bs.show();
                sheet_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bs.dismiss();
                    }
                });
                sheet_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        date=datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getYear();
                        edit_dob.setText(date);
                       // edit_dob.setTextColor(getResources().getColor(R.color.colorPrimary));
                        bs.dismiss();
                    }
                });

            }
        });
//////////////////////////////////////////////////////////////////////////////////////////

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override



            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListNASH.clear();
                aa = (NationalityObject) parent.getAdapter().getItem(position);
                String name= aa.getName();
                int id_nationality =aa.getId();
                Log.d("id", String.valueOf(id_nationality));
                ListNASH.add(aa);
               actv.setText(name);

            }
        });



        String url = Project_Web_Functions.BASE_URL+"/nationalities?access_token="+token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result=new JSONObject(response);
                    if (result.getBoolean("status")==true) {

                        JSONArray items = result.getJSONArray("items");
                        int id = 0;
                        String name = null;
                        Nationalites=new ArrayList<>();

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject nationality = items.getJSONObject(i);
                            NationalityObject o=new NationalityObject();
                            id = nationality.getInt("id");
                            name = nationality.getString("name");

                            o.setName(name);
                            o.setId(id);
                            Nationalites.add(o);

                        }


                        Log.d("id", String.valueOf(id));
                        Log.d("name", name);

                        adapter = new NationalityAdapter
                                (EditDobActivity.this, R.layout.fragment_nationality, R.id.list_item, Nationalites);
                        actv .setAdapter(adapter);

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
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };


        VolleySingleton.getInstance().addToRequestQueue(stringRequest);}


    public void Adddob(final String token, final String dob,final String country_dob_id) {
        showProgressDialog();
        String url = Project_Web_Functions.BASE_URL+"/user";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                dismissProgressDialog();
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result=new JSONObject(response);
                    Toast.makeText(getApplicationContext(),"Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(con, AboutProfileActivity.class);
                    startActivity(i);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("access_token", token);
                params.put("dob",dob);
                params.put("country_dob", country_dob_id);



                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-HTTP-Method-Override","PUT");

                return headers;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);

    }


}