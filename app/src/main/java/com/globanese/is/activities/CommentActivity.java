package com.globanese.is.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.choota.dev.ctimeago.TimeAgo;
import com.globanese.is.R;
import com.globanese.is.adapters.CommentAdapters;
import com.globanese.is.model.CommentObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class CommentActivity extends BaseActivity {
    ImageButton Write_commnet, close;
    String token;
    EditText edittext_writepost;
    String text;
    List<CommentObject> Comments;
    TextView textview_like, textview_smile, textview_wow, textview_angry, textview_love;
    ListView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));

        textview_like = (TextView) findViewById(R.id.textview_like);
        textview_smile = (TextView) findViewById(R.id.textview_smile);
        textview_love = (TextView) findViewById(R.id.textview_love);
        textview_angry = (TextView) findViewById(R.id.textview_angry);
        textview_wow = (TextView) findViewById(R.id.textview_wow);


        Write_commnet = (ImageButton) findViewById(R.id.write_comment);
        close = (ImageButton) findViewById(R.id.close);
        token = getLogInUser().getAccess_token();
        edittext_writepost = (EditText) findViewById(R.id.edittext_commnet);
        rv = (ListView) findViewById(R.id.recyclerView);

        Intent b = getIntent();
        final String id = b.getStringExtra("post_id");

        Log.d("id5", id + "k");
        GetComment(token, id);

        Write_commnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();

                text = edittext_writepost.getText().toString();
                Log.d("text", text);
                WriteComment(token, text, id);
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommentActivity.this, TimeLineActivity.class);
                startActivity(i);
            }
        });


    }


    private void WriteComment(final String token, final String text, final String post_id) {
        Log.d("token", token);
        Log.d("token", text);


        String url = Project_Web_Functions.BASE_URL + "/comment";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {

                    JSONObject result = new JSONObject(response);


                    if (result.getBoolean("status") == (true)) {
                        dismissProgressDialog();
                        edittext_writepost.setText(null);


                        String id = null;
                        JSONObject item = result.getJSONObject("items");
                        Comments = new ArrayList<CommentObject>();
                        CommentObject comment = new CommentObject();
                        if (!item.isNull("text")) {
                            String text = item.getString("text");
                            comment.setText(text);

                        }
                        if (!item.isNull("post")) {
                            JSONObject post = item.getJSONObject("post");
                            String post_id = post.getString("id");
                            GetComment(token, post_id);
                        }


                        if (!item.isNull("user")) {

                            JSONObject user = item.getJSONObject("user");


                            String fname = user.getString("fname");
                            String lname = user.getString("lname");
                            comment.setName(fname + "" + lname);

                            if (!user.isNull("id")) {

                                id = user.getString("id");
                            }


                            if (!user.isNull("photo")) {
                                String photo = user.getString("photo");
                                String imgeurl = "http://globanese.info/beta/uploads/users/" + id + "/profile_" + photo;
                                Log.d("imgeurl", imgeurl);
                                comment.setImage(imgeurl);

                            }
                            comment.setIs_like_user_comment(false);


                        }
                        //Comments.add(comment);
                        // Log.d("sizecomment", String.valueOf(Comments.size()));
                        // CommentAdapters adapter = new CommentAdapters(getApplicationContext(), Comments);
                        // adapter.notifyDataSetChanged();
                        // rv.setAdapter(adapter);

                        Toast.makeText(getApplicationContext(), "Comment Successfully ", Toast.LENGTH_SHORT).show();

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
                params.put("text", text);
                params.put("post_id", post_id);
                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }

    private void GetComment(final String token, final String post_id) {


        String url = Project_Web_Functions.BASE_URL + "/comments/" + post_id + "?access_token=" + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    String user_id = null;
                    JSONObject result = new JSONObject(response);

                    if (result.getBoolean("status") == (true)) {
                        edittext_writepost.setText(null);
                        dismissProgressDialog();

                        JSONObject item = result.getJSONObject("items");

                        if (!item.isNull("reactions")) {

                            JSONObject reactions = item.getJSONObject("reactions");

                            String like_count = reactions.getString("like_count");
                            String smile_count = reactions.getString("smile_count");
                            String love_count = reactions.getString("love_count");
                            String angry_count = reactions.getString("angry_count");
                            String wow_count = reactions.getString("wow_count");
                            textview_like.setText(like_count);
                            textview_smile.setText(smile_count);
                            textview_love.setText(love_count);
                            textview_wow.setText(angry_count);
                            textview_angry.setText(wow_count);
                        }


                        if (!item.isNull("comments")) {
                            Comments = new ArrayList<CommentObject>();
                            JSONArray com_array = item.getJSONArray("comments");

                            for (int i = 0; i < com_array.length(); i++) {

                                JSONObject com = com_array.getJSONObject(i);
                                CommentObject comment = new CommentObject();

                                if (!com.isNull("id")) {
                                    String comment_id = com.getString("id");
                                    comment.setComment_id(comment_id);

                                }


                                if (!com.isNull("likes_count")) {
                                    String likes_count = com.getString("likes_count");
                                    comment.setLike_count(likes_count);

                                }
                                if (!com.isNull("created_at")) {
                                    String created_at = com.getString("created_at");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                                    Date currentdate = null;
                                    try {


                                        currentdate = dateFormat.parse(created_at);
                                        TimeAgo timeAgo = new TimeAgo();
                                        String time = timeAgo.getTimeAgo(currentdate);
                                        comment.setCeated_at(time);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                }


                                if (!com.isNull("is_like_user_comment")) {
                                    Boolean is_like_user_comment = com.getBoolean("is_like_user_comment");
                                    comment.setIs_like_user_comment(is_like_user_comment);

                                }


                                if (!com.isNull("text")) {


                                    String text = com.getString("text");
                                    comment.setText(text);
                                    Log.d("text", text);
                                }


                                if (!com.isNull("user")) {

                                    JSONObject user = com.getJSONObject("user");
                                    String fname = user.getString("fname");
                                    String lname = user.getString("lname");
                                    comment.setName(fname + "" + lname);

                                    if (!user.isNull("id")) {
                                        user_id = user.getString("id");

                                    }


                                    if (!user.isNull("photo")) {
                                        String photo = user.getString("photo");
                                        String imgeurl = "http://globanese.info/beta/uploads/users/" + user_id + "/profile_" + photo;
                                        Log.d("imgeurl", imgeurl);
                                        comment.setImage(imgeurl);

                                    }


                                }
                                Comments.add(comment);

                            }

                            Log.d("sizecomment", String.valueOf(Comments.size()));
                            CommentAdapters adapter = new CommentAdapters(getApplicationContext(), Comments);
                            adapter.notifyDataSetChanged();
                            rv.setAdapter(adapter);
                        }


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

                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }


}
