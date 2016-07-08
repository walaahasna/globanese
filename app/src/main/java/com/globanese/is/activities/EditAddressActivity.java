package com.globanese.is.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.globanese.is.R;
import com.globanese.is.model.AboutProfileObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.globanese.is.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditAddressActivity extends BaseActivity {
    EditText edit_address;
    TextView done;
    String token;
Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_address);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));


        con=EditAddressActivity.this;

        token = getLogInUser().getAccess_token();
        edit_address = (EditText) findViewById(R.id.edit_address);



        if (AboutProfileActivity.aadress1!=null)
            edit_address.setText(AboutProfileActivity.aadress1);


        done = (TextView) findViewById(R.id.done);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditAddressActivity.this, AboutProfileActivity.class);
                startActivity(i);
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edit_address.getText().toString();
                AddAddress(token, address);


            }
        });
    }


    public void AddAddress(final String token, final String address) {
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
                    Toast.makeText(con, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent Intent= new Intent(con, AboutProfileActivity.class);
                        startActivity(Intent);


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
                params.put("address", address);

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
