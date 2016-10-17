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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class EditphoneActivity extends BaseActivity {

    EditText edit_phone;
    TextView done;
    Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_phone);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));
        con = EditphoneActivity.this;
        edit_phone = (EditText) findViewById(R.id.edit_phone);

        if (AboutProfileActivity.phone != null)
            edit_phone.setText(AboutProfileActivity.phone);


        done = (TextView) findViewById(R.id.done);
        final String token = getLogInUser().getAccess_token();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edit_phone.getText().toString();
                ArrayList<String> phoneA = new ArrayList<String>();
                phoneA.add(phone);
                AddPhone(token, phoneA);


            }
        });


        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditphoneActivity.this, AboutProfileActivity.class);
                startActivity(i);
            }
        });
    }

    public void AddPhone(final String token, final List<String> Array) {
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
                int i = 0;
                for (String object : Array) {
                    params.put("phones[" + (i++) + "]", object);
                }

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