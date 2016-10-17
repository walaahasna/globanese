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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.adapters.MyRecyclerViewAdaptfsearch;
import com.globanese.is.adapters.MyRecyclerViewAdaptfsearch2;
import com.globanese.is.model.SearchObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends BaseActivity {
    // String token;
    AutoCompleteTextView actv;
    // SearchAdapter adapter;
    List<SearchObject> friends = new ArrayList<>();
    ImageView search, cancel;
    ImageButton wrire_post2;
    MyRecyclerViewAdaptfsearch adapter;
    //List<SearchObject> friends = new ArrayList<>();
    String token;
    RecyclerView rv;
    ImageView back;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setThreshold(1);
        token = getLogInUser().getAccess_token();

        actv.setDropDownWidth(400);

        search = (ImageView) findViewById(R.id.search);
        cancel = (ImageView) findViewById(R.id.cancel);
        wrire_post2 = (ImageButton) findViewById(R.id.wrire_post2);
//////////////////////////////////////////////////////////////////////////////rv
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyRecyclerViewAdaptfsearch(getApplicationContext(), friends);


        adapter.setRecyclerViewListener(new MyRecyclerViewAdaptfsearch.MyRecyclerViewListener() {
                                            @Override
                                            public void viewSelected(View v, int position) {

                                                String friend_id = friends.get(position).getId();
                                                String profile_photo = friends.get(position).getProfile_pic();
                                                String cover_photo = friends.get(position).getCover_photo();
                                                String country = friends.get(position).getProfile_country();
                                                String name = friends.get(position).getPrfile_name();
                                                String friend_status = friends.get(position).getFriend_status();

                                                Intent i = new Intent(SearchActivity.this, ProfileFriendActivity.class);
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
                Intent i = new Intent(SearchActivity.this, Search2Activity.class);
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
        wrire_post2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, WritePostActivity.class);
                startActivity(i);
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////
        actv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("text", String.valueOf(count));

                if (actv.getText().length() == 2) {
                    find_friends(token, String.valueOf(actv.getText()));
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
                Intent i = new Intent(SearchActivity.this, ProfileActivity.class);
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


   /* void find_friends(String token, Editable text) {

        String url = Project_Web_Functions.BASE_URL + "/find-friends/" + text + "?access_token=" + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getBoolean("status") == true) {

                        JSONArray items = result.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            SearchObject s = new SearchObject();
                            JSONObject object = items.getJSONObject(i);
                            String id = object.getString("id");
                            String fname = object.getString("fname");
                            String lname = object.getString("lname");
                            String photo = object.getString("photo");


                            String url = "http://globanese.info/beta/uploads/users/" + id + "/" + photo;

                            if (!object.isNull("community")) {
                                JSONObject community = object.getJSONObject("community");
                                String country = community.getString("country");
                                s.setProfile_country(country);
                            }

                            s.setPrfile_name(fname + "" + lname);
                            s.setProfile_pic(url);
                            s.setId(id);
                            friends.add(s);
                        }

                        adapter = new SearchAdapter(getApplicationContext(), R.layout.activity_search, R.id.list_item, friends);
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


    }*/


    void find_friends(String token, String text) {

        if (friends.size() != 0) {

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


                            if (!object.isNull("photo")) {
                                String photo = object.getString("photo");
                                String url = "http://globanese.info/beta/uploads/users/" + id + "/" + photo;
                                s.setProfile_pic(url);

                            } else {
                                s.setProfile_pic("n");

                            }


                            if (!object.isNull("coverphoto")) {
                                String coverphoto = object.getString("coverphoto");

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
