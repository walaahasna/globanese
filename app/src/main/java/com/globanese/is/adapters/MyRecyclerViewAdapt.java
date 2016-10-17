package com.globanese.is.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.globanese.is.R;
import com.globanese.is.model.AddFriendObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jit on 01/09/2015.
 */
public class MyRecyclerViewAdapt extends RecyclerView.Adapter<MyRecyclerViewAdapt.MyViewHolder> {
    Context context;
    List<AddFriendObject> Friends;
    MyRecyclerViewListener listener;
    ImageLoader imageLoader;

    public MyRecyclerViewAdapt() {
    }

    public List<AddFriendObject> getFriends() {
        return Friends;
    }

    public void setFriends(List<AddFriendObject> friends) {
        Friends = friends;
    }

    public MyRecyclerViewAdapt(Context context, List<AddFriendObject> Friends) {
        this.context = context;
        this.Friends = Friends;
    }

    public void setRecyclerViewListener(MyRecyclerViewListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_addfriend, parent, false);
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
        AddFriendObject s = Friends.get(position);
////////////////////city fragment/////////////////

        holder.tv1.setText(s.getName());
        holder.thumbNail.setImageResource(s.getPic());

    }

    @Override
    public int getItemCount() {
        return Friends.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView thumbNail;

        TextView tv1;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.Fname);
            thumbNail = (CircleImageView) itemView.findViewById(R.id.profile_image);
        }
    }

    public interface MyRecyclerViewListener {
        public void viewSelected(View v, int position);
    }
}

