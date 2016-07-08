package com.globanese.is.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.adapters.CommunityAdapterForAutocompltet;
import com.globanese.is.model.AboutProfileObject;
import com.globanese.is.model.CommunityObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCommunityActivity extends BaseActivity {
    List<CommunityObject> Communities;
    CommunityObject a;
    private SearchView mSearchView;
    private ListView mListView;
    TextView edit_community;
    LinearLayout search_layout;
    AutoCompleteTextView actv;
    CommunityAdapterForAutocompltet adapter;
    int id_nationality;
    TextView done;


    Context con;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_community);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));

        con = EditCommunityActivity.this;


        ImageView back = (ImageView) findViewById(R.id.back);
        done = (TextView) findViewById(R.id.done);
        final String token = getLogInUser().getAccess_token();
        edit_community = (TextView) findViewById(R.id.edit_community);


        if (AboutProfileActivity.country != null)
            edit_community.setText(AboutProfileActivity.country);


        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setThreshold(1);

        GetComuntity(token);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditCommunityActivity.this, AboutProfileActivity.class);
                startActivity(i);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCommunity(token, String.valueOf(id_nationality));

            }
        });


        edit_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_community.setText(null);
                actv.setVisibility(View.VISIBLE);
            }
        });

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                a = (CommunityObject) parent.getAdapter().getItem(position);
                String name = a.getCountry();
                id_nationality = a.getId();
                edit_community.setText(name);
                actv.setText(null);
                actv.setHint("Add Community");

            }
        });


    }

    public void GetComuntity(final String token) {
        String url = Project_Web_Functions.BASE_URL+"/communities?access_token=" + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getBoolean("status") == true) {

                        JSONArray items = result.getJSONArray("items");
                        int id = 0;
                        String name = null;
                        Communities = new ArrayList<>();

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject Comunity = items.getJSONObject(i);
                            CommunityObject o = new CommunityObject();
                            id = Comunity.getInt("id");
                            name = Comunity.getString("country");

                            o.setCountry(name);
                            o.setId(id);
                            Communities.add(o);

                        }


                        adapter = new CommunityAdapterForAutocompltet(getApplicationContext(), R.layout.fragment_community, R.id.list_item, Communities);
                        actv.setAdapter(adapter);

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

    public void AddCommunity(final String token, final String Comunity_id) {
        showProgressDialog();
        String url = Project_Web_Functions.BASE_URL+"/user";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                dismissProgressDialog();
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(con, AboutProfileActivity.class);
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
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("access_token", token);
                params.put("community_id", Comunity_id);

                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-HTTP-Method-Override", "PUT");

                return headers;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);

    }
}
