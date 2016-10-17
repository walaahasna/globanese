package com.globanese.is.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.globanese.is.R;
import com.globanese.is.model.SearchObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jit on 01/09/2015.
 */
public class MyRecyclerViewAdaptfsearch extends RecyclerView.Adapter<MyRecyclerViewAdaptfsearch.MyViewHolder> {
    Context context;
    List<SearchObject> Friends;
    MyRecyclerViewListener listener;
    ImageLoader imageLoader;

    public MyRecyclerViewAdaptfsearch() {
    }

    public List<SearchObject> getFriends() {
        return Friends;
    }

    public void setFriends(List<SearchObject> friends) {
        Friends = friends;
    }

    public MyRecyclerViewAdaptfsearch(Context context, List<SearchObject> Friends) {
        this.context = context;
        this.Friends = Friends;
    }

    public void setRecyclerViewListener(MyRecyclerViewListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_search_friend, parent, false);
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        imageLoader = AppController.getInstance().getImageLoader();
        SearchObject s = Friends.get(position);
////////////////////city fragment/////////////////
        holder.name.setText(s.getPrfile_name());
        holder.country.setText(s.getProfile_country());

        if(!s.getProfile_pic().equals("n")){
            holder.cimage.setImageResource(R.drawable.user_image_placeholder);
        }
        else{
            Picasso.with(context).load(s.getProfile_pic()).into( holder.cimage);
        }




    }

    @Override
    public int getItemCount() {
        return Friends.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cimage;

     TextView country,name;
        ImageView friend_staues;
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
}

