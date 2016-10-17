package com.globanese.is.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.activities.BaseActivity;
import com.globanese.is.model.CommentObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapters  extends BaseAdapter {

    public static List<CommentObject> Comments;
    public static Context context;

    public CommentAdapters (Context context, List<CommentObject> Comments) {
        this.context = context;
        this.Comments = Comments;

    }



    @Override
    public int getCount() {
        return Comments.size();
    }

    @Override
    public Object getItem(int position) {
        return Comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;


            Log.d("sizeadapter", String.valueOf(Comments.size())+"s");
            final CommentObject s = Comments.get(position);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_comment_cell, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
                mViewHolder.profile_name.setText(s.getName());
                mViewHolder.profile_time.setText(s.getCeated_at());
                mViewHolder.profile_text.setText(s.getText());
                mViewHolder.profile_No_like.setText("("+s.getLike_count()+")");
                Picasso.with(context).load(s.getImage()).into( mViewHolder.profile_image);


        if(s.getIs_like_user_comment()==true)
            mViewHolder.comment_button.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        mViewHolder.comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.comment_button.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                BaseActivity b=new BaseActivity();
              String  token= b.getLogInUser().getAccess_token();
                Create_like(token,s.getComment_id());
               int like_num= Integer.parseInt(s.getLike_count().toString());
                String like= String.valueOf(like_num+1);
                mViewHolder.profile_No_like.setText("("+like+")");

            }
        });




        return convertView;
    }


    private class MyViewHolder {
        TextView profile_name,profile_time,profile_text,profile_No_like,comment_button;
        CircleImageView profile_image;

        public MyViewHolder(View item) {

                profile_image= (CircleImageView)item.findViewById(R.id.profile_image);
                profile_name= (TextView)item.findViewById(R.id.profile_name);
                profile_time= (TextView)item.findViewById(R.id.profile_time);
                profile_text= (TextView)item.findViewById(R.id.profile_text);
                profile_No_like= (TextView)item.findViewById(R.id.profile_No_like);
               comment_button= (TextView)item.findViewById(R.id.comment_button);

        }
    }

    private void Create_like(final String token, final String comment_id) {
        Log.d("token", token);
        Log.d("token", comment_id);

        String url = Project_Web_Functions.BASE_URL + "/reaction";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override



            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);


                    if (result.getBoolean("status") == (true)) {

                    }


                    else {


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
                params.put("post_comment_id", comment_id);
                params.put("type","1");
                params.put("type_form", "0");

                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }
}