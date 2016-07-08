package com.globanese.is.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.globanese.is.R;
import com.globanese.is.model.AboutProfileObject;
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

public class EditJobActivity extends BaseActivity {
    EditText currentjob, company, website;
   public static Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fargment_job);

        TextView done = (TextView) findViewById(R.id.done);
        currentjob = (EditText) findViewById(R.id.textview_currentjob);
        company = (  EditText) findViewById(R.id.textview_company);
        website = (  EditText) findViewById(R.id.textview_website);
        con=EditJobActivity.this;



       if(AboutProfileActivity.company!=null)
           company.setText(AboutProfileActivity.company);


        if(AboutProfileActivity.postion!=null)
            currentjob.setText(AboutProfileActivity.postion);

        if(AboutProfileActivity.website!=null)
            website.setText(AboutProfileActivity.website);




////////////////////////////////////////////////////////////////////////////
        final String token = getLogInUser().getAccess_token();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current = currentjob.getText().toString();
                String companyy = company.getText().toString();
                String web = website.getText().toString();


                AddJob(token, current, companyy, web);
            }
        });


///////////////////////////////////////////////////////////////////////////////////////////

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditJobActivity.this, AboutProfileActivity.class);
                startActivity(i);
            }
        });


    }

    public void AddJob(final String token, final String company, final String position, final String website) {
        showProgressDialog();
        String url = Project_Web_Functions.BASE_URL+"/user";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(con, AboutProfileActivity.class);
                    startActivity(i);

                    dismissProgressDialog();

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
                params.put("company", company);
                params.put("website", website);
                params.put("position", position);


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
