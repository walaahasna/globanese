package com.globanese.is.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
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

public class Search2Activity extends BaseActivity {
    MyRecyclerViewAdaptfsearch2 adapter;
    List<SearchObject> friends = new ArrayList<>();
    String token;
    RecyclerView rv;
    ImageView back;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        String text_serach = getIntent().getStringExtra("text_serach");
        back = (ImageView) findViewById(R.id.back);

        token = getLogInUser().getAccess_token();

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyRecyclerViewAdaptfsearch2(getApplicationContext(), friends);

        adapter.setRecyclerViewListener(new MyRecyclerViewAdaptfsearch2.MyRecyclerViewListener() {
                                            @Override
                                            public void viewSelected(View v, int position) {

                                                String friend_id = friends.get(position).getId();
                                                String profile_photo = friends.get(position).getProfile_pic();
                                                String cover_photo = friends.get(position).getCover_photo();
                                                String country = friends.get(position).getProfile_country();
                                                String name = friends.get(position).getPrfile_name();
                                                String friend_status = friends.get(position).getFriend_status();

                                                Intent i = new Intent(Search2Activity.this, ProfileFriendActivity.class);
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
        find_friends(token, text_serach);


        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(2000);
        animator.setAddDuration(2000);
        rv.setItemAnimator(animator);
        final LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());


        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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




                            if(!object.isNull("photo")){
                                String photo = object.getString("photo");
                                String url = "http://globanese.info/beta/uploads/users/" + id + "/" + photo;
                                s.setProfile_pic(url);

                            }

                            else{
                                s.setProfile_pic("n");

                            }


                            if(!object.isNull("coverphoto")){
                                String coverphoto = object.getString("coverphoto");

                                String url_cover = "http://globanese.info/beta/uploads/users/" + id + "/" + coverphoto;
                                s.setCover_photo(url_cover);

                            }

                            else{

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
