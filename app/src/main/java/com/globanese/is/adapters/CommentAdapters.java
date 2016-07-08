package com.globanese.is.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.globanese.is.R;
import com.globanese.is.model.CommentObject;

import java.util.List;

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
        MyViewHolder mViewHolder;


        if (convertView == null) {
            CommentObject s = Comments.get(position);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_comment_cell, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
                mViewHolder.profile_name.setText("Craig Lopez");
                mViewHolder.profile_time.setText("2h");
                mViewHolder.profile_text.setText("Follow along with using jhy. Don't forget to use the filters to find the best Tweets first. ");
            mViewHolder.profile_image.setImageResource(s.getImage());





        }
        else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private class MyViewHolder {
        TextView profile_name,profile_time,profile_text,profile_No_like;
        CircleImageView profile_image;

        public MyViewHolder(View item) {

            profile_image= (CircleImageView)item.findViewById(R.id.profile_image);
                profile_name= (TextView)item.findViewById(R.id.profile_name);
                profile_time= (TextView)item.findViewById(R.id.profile_time);
                profile_text= (TextView)item.findViewById(R.id.profile_text);
                profile_No_like= (TextView)item.findViewById(R.id.profile_No_like);



        }
    }
}