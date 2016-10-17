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

public class DeletePostActivity extends BaseActivity {
    TextView delete_post, report_textview, cancel;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_remove);
        delete_post = (TextView) findViewById(R.id.post_delete);
        report_textview = (TextView) findViewById(R.id.report);
        cancel = (TextView) findViewById(R.id.cancel);

        token = getLogInUser().getAccess_token();

        Intent b = getIntent();
        final String timeline_id = b.getStringExtra("timeline_id");
        final String id = b.getStringExtra("post_id");

        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                Delete_post(token, id,timeline_id);

            }
        });


      cancel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

           finish();

          }
      });


    }

    private void Delete_post(final String token, final String post_id, String timeline_id) {
        Log.d("token", token);
        Log.d("id", post_id);


        String url = Project_Web_Functions.BASE_URL + "/timeline" + "/" +timeline_id;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);


                    if (result.getBoolean("status") == (true)) {
                        dismissProgressDialog();

                        Intent i = new Intent(DeletePostActivity.this, TimeLineActivity.class);
                        startActivity(i);
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


                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-HTTP-Method-Override", "DELETE");

                return headers;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }


}
