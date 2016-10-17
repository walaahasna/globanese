package com.globanese.is.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.adapters.LanguageAdapter;
import com.globanese.is.model.AboutProfileObject;
import com.globanese.is.model.LanguageObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;





public class EdiLanguageActivity extends BaseActivity  {
    TextView  done;





    TextView edit_language;
    ImageView back;
    AutoCompleteTextView actv;
    public static LanguageObject a;
    public static List<LanguageObject> ListNASH;
    String token;
    List<LanguageObject> Languages;
    LinearLayout search_layout;
    TagContainerLayout mTagContainerLayout;
    List<String> list;
    Context con;
    LanguageAdapter adapter;
    @Override



    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
       setContentView(R.layout.fragment_language);

        con=EdiLanguageActivity.this;

        ListNASH=new ArrayList<>();
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
        list = new ArrayList<String>();
        con=EdiLanguageActivity.this;
        token=getLogInUser().getAccess_token();
        done=(TextView)findViewById(R.id.done);
        search_layout =(LinearLayout)findViewById(R.id.Layout_tag);
        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        back=(ImageView)findViewById(R.id.back);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setThreshold(1);
        con=EdiLanguageActivity.this;

        getUserInfo( token);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EdiLanguageActivity.this,AboutProfileActivity.class);
                startActivity(i);
            }
        });



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int idt = 0;
                for (LanguageObject str : ListNASH) {
                    idt = str.getId();
                    list.add(String.valueOf(idt));


                }
                AddLanguage(token,list);

            }
        });

        String url = Project_Web_Functions.BASE_URL+"/languages?access_token="+token;
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
                        Languages =new ArrayList<>();

                             for (int i = 0; i < items.length(); i++) {
                            JSONObject nationality = items.getJSONObject(i);
                            LanguageObject o=new LanguageObject();
                            id = nationality.getInt("id");
                            name = nationality.getString("name");
                            o.setName(name);
                            o.setId(id);
                            Languages.add(o);

                        }

                        Log.d("id", String.valueOf(id));
                        Log.d("name", name);

                        adapter = new LanguageAdapter
                                (con , R.layout.fragment_language, R.id.list_item, Languages);
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

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);





        /////////////////////////

        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                String texttag = mTagContainerLayout.getTagText(position);
                mTagContainerLayout.removeTag(position);

                Iterator<LanguageObject> i = ListNASH.iterator();
                while (i.hasNext()) {
                    LanguageObject o = i.next();
                    if (o.getName().equals(texttag))
                    {

                        //

                        i.remove();
                    }

                }




            }

            @Override
            public void onTagLongClick(int position, String text) {
                String texttag = mTagContainerLayout.getTagText(position);
                mTagContainerLayout.removeTag(position);

                Iterator<LanguageObject> i = ListNASH.iterator();
                while (i.hasNext()) {
                    LanguageObject o = i.next();
                    if (o.getName().equals(texttag))
                    {
                        i.remove();
                    }

                }

            }
        });
//////////////////////////////////////////////////////////////////////////////


        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                a = (LanguageObject) parent.getAdapter().getItem(position);
                String name= a.getName();
                int id_nationality =a.getId();

                ListNASH.add(a);
                mTagContainerLayout.setVisibility(View.VISIBLE);
                int green = Color.parseColor("#22bf62");
                mTagContainerLayout.setTagBackgroundColor(green);
                mTagContainerLayout.setTagTextColor(0xffffffff);
                mTagContainerLayout.setBackgroundColor(0xffffffff);
                mTagContainerLayout.setTagBorderRadius(4);
                mTagContainerLayout.setTagTextSize(20);
                mTagContainerLayout.setTagBorderColor(0xffffffff);
                mTagContainerLayout.setBorderWidth(0);
                mTagContainerLayout.setBorderColor(0xffffffff);
                mTagContainerLayout.addTag(name);
                actv.setText(null);
                actv.setHint("Add Language");
            }
        });



    }


    public void AddLanguage(final String token,final List<String> Array_language ) {
        showProgressDialog();
        String url = "http://globanese.info/beta/api/v1/user";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                dismissProgressDialog();
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result=new JSONObject(response);
                    Toast.makeText(getApplicationContext(),"Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(con,AboutProfileActivity.class);
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

                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", token);
                int i=0;
                for(String object:Array_language){
                    params.put("languages_id["+(i++)+"]", object);
                }

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






    void getUserInfo(String token){


        showProgressDialog();
        String url = "http://globanese.info/globanese/api/v1/user?access_token="+token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                dismissProgressDialog();
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result=new JSONObject(response);
                    if (result.getBoolean("status")==true) {

                        JSONObject items = result.getJSONObject("items");

                        JSONArray nationality_araay = items.getJSONArray("language");
                        String nationality = null;
                        for (int ii = 0; ii < nationality_araay.length(); ii++) {
                            nationality = nationality_araay.getJSONObject(ii).getString("name");
                            String id= nationality_araay.getJSONObject(ii).getString("id");
                           LanguageObject o=new LanguageObject();
                            o.setId(Integer.parseInt(id));
                            o.setName( nationality);
                            ListNASH.add(o);

                            int green = Color.parseColor("#22bf62");
                            mTagContainerLayout.setTagBackgroundColor(green);
                            mTagContainerLayout.setTagTextColor(0xffffffff);
                            mTagContainerLayout.setBackgroundColor(0xffffffff);
                            mTagContainerLayout.setTagBorderRadius(4);
                            mTagContainerLayout.setTagTextSize(20);
                            mTagContainerLayout.setTagBorderColor(0xffffffff);
                            mTagContainerLayout.setBorderWidth(0);
                            mTagContainerLayout.setBorderColor(0xffffffff);
                            mTagContainerLayout.addTag(nationality);

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
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);






    }
}
