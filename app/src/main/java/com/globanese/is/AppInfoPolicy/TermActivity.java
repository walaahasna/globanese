package com.globanese.is.AppInfoPolicy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.activities.BaseActivity;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TermActivity extends BaseActivity {
    TextView textView_about,ok;
    String lang;
    public static String app_term,app_policy,app_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        lang = Locale.getDefault().getLanguage();
        Log.d("deviceLocale", lang);
        ok=(TextView)findViewById(R.id.ok);
        textView_about=(TextView)findViewById(R.id.textview_about);
        showProgressDialog();
        getterm(lang);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    public void getterm(final String lang) {
        Log.d("lang",lang);



        String url = Project_Web_Functions.BASE_URL + "/term/"+lang;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {

                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);

                    if (result.getBoolean("status") == (true)) {

                        JSONObject items=   result.getJSONObject("items");

                        String term=  items.getString("text");
                        app_term=String.valueOf(Html.fromHtml(term));
                        textView_about.setText(app_term);
                        dismissProgressDialog();

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


                return params;
            }



        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }
}

