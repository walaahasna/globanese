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
import com.globanese.is.model.AddFriendObject;
import com.globanese.is.model.ImageObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jit on 01/09/2015.
 */
public class HorizantalListAdapterforUploadImage extends RecyclerView.Adapter<HorizantalListAdapterforUploadImage.MyViewHolder> {
    Context context;
    List<ImageObject> Images;
    MyRecyclerViewListener listener;
    ImageLoader imageLoader;

    public HorizantalListAdapterforUploadImage() {
    }

    public List<ImageObject> getFriends() {
        return Images;
    }

    public void setFriends(List<ImageObject> friends) {
        Images = friends;
    }

    public HorizantalListAdapterforUploadImage(Context context, List<ImageObject> Friends) {
        this.context = context;
        this.Images = Friends;
    }

    public void setRecyclerViewListener(MyRecyclerViewListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_imgeview_cell, parent, false);
        final MyViewHolder vh = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.getAdapterPosition() >= 0 && vh.getAdapterPosition() < Images.size())
                    listener.viewSelected(v, vh.getAdapterPosition());

                //  Images.remove(vh.getAdapterPosition());
                //   notifyItemRemoved(vh.getAdapterPosition());
                //  notifyItemRangeChanged(vh.getAdapterPosition(), Images.size());
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        imageLoader = AppController.getInstance().getImageLoader();
        ImageObject s = Images.get(position);
////////////////////city fragment/////////////////

        holder.thumbNail.setImageBitmap(s.getPhoto());
    }

    @Override
    public int getItemCount() {
        return Images.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbNail;

        public MyViewHolder(View itemView) {
            super(itemView);

            thumbNail = (ImageView) itemView.findViewById(R.id.imageviewmain);
        }
    }

    public interface MyRecyclerViewListener {
        public void viewSelected(View v, int position);
    }
}

