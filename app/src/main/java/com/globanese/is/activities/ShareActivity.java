package com.globanese.is.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShareActivity extends BaseActivity {
    TextView share_now, writepost, copylink,cancel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        final String token = getLogInUser().getAccess_token();

        share_now = (TextView) findViewById(R.id.share_now);
        writepost = (TextView) findViewById(R.id.write);
        copylink = (TextView) findViewById(R.id.copy_link);
       cancel= (TextView) findViewById(R.id.cancel);

        Intent b = getIntent();
        final String User_share_from = b.getStringExtra("User_share_from");
        final String id = b.getStringExtra("post_id");

        share_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                share_post(token, id, User_share_from);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void share_post(final String token, final String post_id, final String user_id_from) {
        Log.d("token", token);
        Log.d("id", post_id);
        // Log.d("text", text+"k");
       // Log.d("user_id_from", user_id_from);

        String url = Project_Web_Functions.BASE_URL + "/share";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    dismissProgressDialog();
                    JSONObject result = new JSONObject(response);

                    if (result.getBoolean("status") == (true)) {

                        Toast.makeText(getApplicationContext(), "share  Post success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), TimeLineActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);

                    } else {


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
                params.put("post_id", post_id);
                //  params.put("text", text);
                //params.put("user_id_from", user_id_from);

                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }


}
