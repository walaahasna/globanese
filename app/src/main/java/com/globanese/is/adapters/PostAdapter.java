package com.globanese.is.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.globanese.is.R;
import com.globanese.is.activities.CommentActivity;
import com.globanese.is.activities.EditJobActivity;
import com.globanese.is.activities.DeletePostActivity;
import com.globanese.is.model.AddFriendObject;
import com.globanese.is.model.PostObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostAdapter extends BaseAdapter {
    MyRecyclerViewAdapt adapter;
    RecyclerView profile_list;
    TextView comment;
    ImageView edit;
    public static List<PostObject> Post;

    public static Context context;

    public PostAdapter(Context context, List<PostObject> Post) {
        this.context = context;
        this.Post = Post;

    }


    @Override
    public int getCount() {
        return Post.size();
    }

    @Override
    public Object getItem(int position) {
        return Post.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        final PostObject s = Post.get(position);


        //////////////textpost=0
        if (s.getPosttype() == 0) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_text, parent, false);

            edit = (ImageView) convertView.findViewById(R.id.post_remove);
            comment = (TextView) convertView.findViewById(R.id.post_comment);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    //intent.putExtra("post_id", s.getPost_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);


                }
            });

            comment.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    // intent.putExtra("post_id", s.getPost_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });


            mViewHolder = new MyViewHolder(convertView, 0);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText("Craig Lopez");
            mViewHolder.profile_time.setText("2h");
            mViewHolder.profile_text.setText("Follow along with using jhy. Don't forget to use the filters to find the best Tweets first. ");
        }


        /////////// //////////////one_image_post=1
        else if (s.getPosttype() == 1) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_one_image, parent, false);
            edit = (ImageView) convertView.findViewById(R.id.post_remove);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    // intent.putExtra("post_id", s.getPost_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);


                }
            });


            mViewHolder = new MyViewHolder(convertView, 1);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText("Craig Lopez");
            mViewHolder.profile_time.setText("2h");
            mViewHolder.profile_text.setText("Follow along with using jhy. Don't forget to use the filters to find the best Tweets first. ");
            mViewHolder.profile_one_image.setImageResource(s.getId());
        }


        //////////////tow_image_post=2
        else if (s.getPosttype() == 2) {
            Log.e("here 2 ", 2 + "");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_tow_image, parent, false);
            edit = (ImageView) convertView.findViewById(R.id.post_remove);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    //intent.putExtra("post_id", s.getPost_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);


                }
            });

            mViewHolder = new MyViewHolder(convertView, 2);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText("Craig Lopez");
            mViewHolder.profile_time.setText("2h");
            mViewHolder.profile_text.setText("Follow along with using jhy. Don't forget to use the filters to find the best Tweets first. ");
            mViewHolder.profile_one_image.setImageResource(s.getId());
            mViewHolder.profile_tow_image.setImageResource(s.getId());

        }


//////////////three_image_post=3


        else if (s.getPosttype() == 3) {
            Log.e("here 3 ", 3 + "");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_three_image_extra, parent, false);
            edit = (ImageView) convertView.findViewById(R.id.post_remove);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    // intent.putExtra("post_id", s.getPost_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

            mViewHolder = new MyViewHolder(convertView, 3);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText("Craig Lopez");
            mViewHolder.profile_time.setText("2h");
            mViewHolder.profile_text.setText("Follow along with using jhy. Don't forget to use the filters to find the best Tweets first. ");


        }


////////////////////////////////////// post_video=4

        else if (s.getPosttype() == 4) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_video, parent, false);
            edit = (ImageView) convertView.findViewById(R.id.post_remove);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    //  intent.putExtra("post_id", s.getPost_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);


                }
            });

            mViewHolder = new MyViewHolder(convertView, 4);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText("walaa");
            mViewHolder.profile_time.setText("2h");
            mViewHolder.profile_text.setText("Follow along with using jhy. Don't forget to use the filters to find the best Tweets first. ");


            ////////////////////////post_type 5  add friend

        } else if (s.getPosttype() == 5) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_add_friend, parent, false);


            mViewHolder = new MyViewHolder(convertView, 5);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_title.setText("New members in Globanese");

            for (int i = 0; i < s.getFriends().size(); i++) {
                AddFriendObject f = s.getFriends().get(i);

                Log.d("item", f.getName());
            }
            adapter = new MyRecyclerViewAdapt(context, s.getFriends());

            mViewHolder.profile_list.setAdapter(adapter);


        }


        return convertView;
    }

    private class MyViewHolder {
        MyRecyclerViewAdapt adapter;
        RecyclerView profile_list;
        TextView profile_name, profile_time, profile_counntry, profile_text, profile_No_like, profile_No_comment, profile_No_share, profile_title, profile_group_name;
        CircleImageView profile_image;
        VideoView profile_viedo;
        ImageView profile_one_image, profile_tow_image, profile_three_image;

        public MyViewHolder(View item, int type) {
//////////////////////post_text

            if (type == 0) {


                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);

            }

///////////////////////////type post_one_image


            else if (type == 1) {
                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_one_image = (ImageView) item.findViewById(R.id.profile_one_image);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
            }


            //////////////////////////////post_tow_image


            else if (type == 2) {
                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_one_image = (ImageView) item.findViewById(R.id.profile_one_image);
                profile_tow_image = (ImageView) item.findViewById(R.id.profile_tow_image);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
            }
//////////////////////////post_three_image_extra

            else if (type == 3) {
                Log.d("here 3 ", 33 + "");


                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_one_image = (ImageView) item.findViewById(R.id.profile_one_image);
                profile_tow_image = (ImageView) item.findViewById(R.id.profile_tow_image);
                profile_three_image = (ImageView) item.findViewById(R.id.profile_three_image);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
            }
/////////////////////////////////////////////////Post_viedo


            else if (type == 4) {
                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                // profile_viedo = (VideoView) item.findViewById(R.id.profile_video);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
            }


            /////////////////////////////////////////////////Post_addfriend
            else if (type == 5) {
                profile_title = (TextView) item.findViewById(R.id.profile_add_friend_titel);
                profile_list = (RecyclerView) item.findViewById(R.id.recyclerView);
                profile_list.setLayoutManager(new LinearLayoutManager(EditJobActivity.con, LinearLayoutManager.HORIZONTAL, false));

            }

            ///////////////////////////post_group

            else if (type == 6) {
                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_group_name = (TextView) item.findViewById(R.id.profile_group_name);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
            }


        }


    }


}