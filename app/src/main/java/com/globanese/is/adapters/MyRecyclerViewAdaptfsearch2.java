package com.globanese.is.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.globanese.is.R;
import com.globanese.is.activities.BaseActivity;
import com.globanese.is.model.SearchObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by jit on 01/09/2015.
 */
public class MyRecyclerViewAdaptfsearch2 extends RecyclerView.Adapter<MyRecyclerViewAdaptfsearch2.MyViewHolder> {
    Context context;
    List<SearchObject> Friends;
    MyRecyclerViewListener listener;
    String token;
    private boolean status;

    public MyRecyclerViewAdaptfsearch2() {
    }

    public List<SearchObject> getFriends() {
        return Friends;
    }

    public void setFriends(List<SearchObject> friends) {
        Friends = friends;
    }

    public MyRecyclerViewAdaptfsearch2(Context context, List<SearchObject> Friends) {
        this.context = context;
        this.Friends = Friends;
    }

    public void setRecyclerViewListener(MyRecyclerViewListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_search_friend2, parent, false);
        final MyViewHolder vh = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.getAdapterPosition() >= 0 && vh.getAdapterPosition() < Friends.size())
                    listener.viewSelected(v, vh.getAdapterPosition());
            }
        });
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        imageLoader = AppController.getInstance().getImageLoader();
        final SearchObject s = Friends.get(position);
        BaseActivity b = new BaseActivity();
      token = b.getLogInUser().getAccess_token();
////////////////////city fragment/////////////////
        holder.name.setText(s.getPrfile_name());
        holder.country.setText(s.getProfile_country());

        if(!s.getProfile_pic().equals("n")){
        Picasso.with(context).load(s.getProfile_pic()).into( holder.cimage);

        }
        else{

            holder.cimage.setImageResource(R.drawable.user_image_placeholder);
        }

if( s.getFriend_status().equals("1")){
    holder.friend_staues.setImageResource(R.drawable.is_friend);

////////////////comment
}


   else if(s.getFriend_status().equals("2")){

    holder.friend_staues.setImageResource(R.drawable.add_person);
}

        else{
    holder.friend_staues.setImageResource(R.drawable.pending_friend);


}

        holder.friend_staues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         Boolean status= send_friendrequest(s.getId());


                if(status==true){
                    holder.friend_staues.setImageResource(R.drawable.pending_friend);
                    Toast.makeText(context,"Friend Request has been sent",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }

                else{


                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return Friends.size();
    }


     public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cimage;

     TextView country,name;
        public ImageView friend_staues;
        public MyViewHolder(View itemView) {
            super(itemView);
           name = (TextView)itemView.findViewById(R.id.prfile_name);
          country = (TextView)itemView.findViewById(R.id.profile_country);
             cimage = (CircleImageView) itemView.findViewById(R.id.profile_image);
         friend_staues = (ImageView)itemView.findViewById(R.id.friend_request);

        }
    }
    public interface MyRecyclerViewListener {
        public void viewSelected(View v, int position);
    }


public Boolean send_friendrequest(String user_id ){
    String url = Project_Web_Functions.BASE_URL + "/friend/" +user_id ;
    Log.d(Project_Web_Functions.class.getName(), url);
    VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Log.d(Project_Web_Functions.class.getName(), response.toString());
            try {
                JSONObject result = new JSONObject(response);
                if (result.getBoolean("status") == true) {

               status=true;

                }

                else{

                  status=false;
                    
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
            params.put("access_token", token);
            return params;
        }
    };

    VolleySingleton.getInstance().addToRequestQueue(stringRequest);

    return status;
}


}

