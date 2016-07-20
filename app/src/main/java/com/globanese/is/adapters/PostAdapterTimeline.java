package com.globanese.is.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.activities.BaseActivity;
import com.globanese.is.activities.CommentActivity;
import com.globanese.is.activities.EditJobActivity;
import com.globanese.is.activities.Main2Activity;
import com.globanese.is.fragments.HorizontalDropDownIconMenu;
import com.globanese.is.model.AddFriendObject;
import com.globanese.is.model.PostObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.shamanland.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapterTimeline extends BaseAdapter {
    Animation fab_open;
    Animation fab_close;
    MyRecyclerViewAdapt adapter;
    RecyclerView profile_list;
   TextView comment;
    ImageView edit;
    ImageView  like;
    public static List<PostObject> Post;
    public static Context context;

    public PostAdapterTimeline(Context context, List<PostObject> Post) {
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
        final MyViewHolder mViewHolder;

        final PostObject s = Post.get(position);


        //////////////textpost=0


        if (s.getType().equals("text_post")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_text, parent, false);


            mViewHolder = new MyViewHolder(convertView, 0);
            convertView.setTag(mViewHolder);

            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_counntry.setText(s.getLocation());
            mViewHolder.profile_text.setText(s.getText());
            makeTextViewResizable( mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getPhoto()).into( mViewHolder.profile_image);


            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });



            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Main2Activity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));


                    mViewHolder.imgviewangry.startAnimation(fab_open);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////
                    mViewHolder.tlike.startAnimation(fab_open);
                    mViewHolder.tsmile.startAnimation(fab_open);
                    mViewHolder.tlove.startAnimation(fab_open);
                    mViewHolder.tangry.startAnimation(fab_open);
                    mViewHolder.twow.startAnimation(fab_open);



                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b=new BaseActivity();
                    String  token= b.getLogInUser().getAccess_token();
                   Create_like(token, String.valueOf(s.getId()),"0");



                }
            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    BaseActivity b=new BaseActivity();
                    String  token= b.getLogInUser().getAccess_token();
                    Create_like(token, String.valueOf(s.getId()),"1");

                }
            });

            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b=new BaseActivity();
                    String  token= b.getLogInUser().getAccess_token();
                    Create_like(token, String.valueOf(s.getId()),"2");


                }
            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b=new BaseActivity();
                    String  token= b.getLogInUser().getAccess_token();
                    Create_like(token, String.valueOf(s.getId()),"3");
                }
            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b=new BaseActivity();
                    String  token= b.getLogInUser().getAccess_token();
                    Create_like(token, String.valueOf(s.getId()),"4");

                }
            });








        }


        /////////// //////////////one_image_post=1
        else if (s.getType().equals("photo_post")&&s.getPhotocount()==1) {


            Log.e("here 1 ", 1 + "");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_one_image, parent, false);




            mViewHolder = new MyViewHolder(convertView, 1);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_counntry.setText(s.getLocation());
            mViewHolder.profile_text.setText(s.getText());
          //  Picasso.with(context).load(s.getPhoto()).into( mViewHolder.profile_one_image);
            Picasso.with(context).load(s.getPhoto()).into( mViewHolder.profile_image);
            for(int i=0;i<s.getPhoto_araay().size();i++){
                Log.d("lenght", String.valueOf(s.getPhoto_araay().size()));


                Picasso.with(context).load(s.getPhoto_araay().get(0)).into( mViewHolder.profile_one_image);


            }


            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });



            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Main2Activity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });

        }


        //////////////tow_image_post=2
        else if (s.getType().equals("photo_post")&&s.getPhotocount()==2) {
            Log.e("here 2 ", 2 + "");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_tow_image, parent, false);


            mViewHolder = new MyViewHolder(convertView, 2);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_text.setText(s.getText());
            mViewHolder.profile_counntry.setText(s.getLocation());
            Picasso.with(context).load(s.getPhoto()).into( mViewHolder.profile_image);



            for(int i=0;i<s.getPhoto_araay().size();i++){
                Log.d("lenght", String.valueOf(s.getPhoto_araay().size()));


                Picasso.with(context).load(s.getPhoto_araay().get(0)).into( mViewHolder.profile_one_image);
                Picasso.with(context).load(s.getPhoto_araay().get(1)).into( mViewHolder.profile_tow_image);

            }


            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });



            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Main2Activity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });

        }



//////////////three_image_post=3

         else if (s.getType().equals("photo_post")&&s.getPhotocount()==3) {
            Log.e("here 3 ", 3 + "");


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_three_image, parent, false);



            mViewHolder = new MyViewHolder(convertView, 3);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_text.setText(s.getText());
            mViewHolder.profile_counntry.setText(s.getLocation());
            Picasso.with(context).load(s.getPhoto()).into( mViewHolder.profile_image);
            for(int i=0;i<s.getPhoto_araay().size();i++){
                Log.d("lenght", String.valueOf(s.getPhoto_araay().size()));
                Picasso.with(context).load(s.getPhoto_araay().get(0)).into( mViewHolder.profile_one_image);
                Picasso.with(context).load(s.getPhoto_araay().get(1)).into( mViewHolder.profile_tow_image);
                 Picasso.with(context).load(s.getPhoto_araay().get(2)).into( mViewHolder.profile_three_image);
            }

            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });



            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Main2Activity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });

        }
//////////////////////////////////////////////post more 3 image

        else if (s.getType().equals("photo_post")&&s.getPhotocount()==4) {
            Log.e("here 3 ", 3 + "");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_three_image_extra, parent, false);



          mViewHolder = new MyViewHolder(convertView, 3);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_text.setText(s.getText());
            mViewHolder.profile_counntry.setText(s.getLocation());
            Picasso.with(context).load(s.getPhoto()).into( mViewHolder.profile_image);
            int num= s.getPhotocount()-3;
            mViewHolder.more_than_three_icon.setText("+"+" "+num);
            for(int i=0;i<s.getPhoto_araay().size();i++){
                Log.d("lenght", String.valueOf(s.getPhoto_araay().size()));
                Picasso.with(context).load(s.getPhoto_araay().get(0)).into( mViewHolder.profile_one_image);
                Picasso.with(context).load(s.getPhoto_araay().get(1)).into( mViewHolder.profile_tow_image);
                Picasso.with(context).load(s.getPhoto_araay().get(2)).into( mViewHolder.profile_three_image);
            }

            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });



            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Main2Activity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });


        }


////////////////////////////////////// post_video=4
        else if (s.getType().equals("video_post")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_video, parent, false);


            mViewHolder = new MyViewHolder(convertView, 4);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_text.setText(s.getText());
            mViewHolder.profile_counntry.setText(s.getLocation());
            Picasso.with(context).load(s.getPhoto()).into( mViewHolder.profile_image);
            mViewHolder.profile_viedo.getSettings().setJavaScriptEnabled(true);
            mViewHolder.profile_viedo.loadUrl(s.getVideo());
            mViewHolder.profile_viedo.setWebViewClient(new HelloWebViewClient());


            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Main2Activity.class);
                    String id= String.valueOf(s.getId());
                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });

        }

        else if (s.getPosttype() == 5) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_add_friend, parent, false);

            edit = (ImageView) convertView.findViewById(R.id.post_remove);
            comment = (TextView) convertView.findViewById(R.id.post_comment);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Main2Activity.class);
                   // intent.putExtra("post_id", s.getPost_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);


                }
            });


            comment.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id= String.valueOf(s.getId());                    intent.putExtra("post_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });





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
        TextView  share, profile_name,more_than_three_icon, profile_time, profile_counntry, profile_text, profile_No_like, profile_No_comment, profile_No_share, profile_title, profile_group_name,commentt;
        CircleImageView profile_image;
        WebView profile_viedo;
        ImageView profile_one_image, profile_tow_image, profile_three_image,likee, post_remove;
        FloatingActionButton imgviewlike,imgviewsmile,imgviewlove,imgviewangry,imgviewwow;
       TextView tlike,tsmile,tlove,tangry,twow;
        RelativeLayout layout;


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
                commentt = (TextView) item.findViewById(R.id.post_comment);
                 share = (TextView) item.findViewById(R.id.timline_share);
                  likee = (ImageView) item.findViewById(R.id.time_line_like);
                   post_remove = (ImageView) item.findViewById(R.id.post_remove);
               layout=  (RelativeLayout)item.findViewById(R.id.commnet_emotion);
                   ///////////////////////

                   imgviewlike = (FloatingActionButton)item. findViewById(R.id.imgeview_like);
                   imgviewsmile = (FloatingActionButton)item. findViewById(R.id.imgeview_smile);
                   imgviewlove = (FloatingActionButton)item. findViewById(R.id.imgeview_love);
                   imgviewangry = (FloatingActionButton)item. findViewById(R.id.imgeview_angry);
                   imgviewwow = (FloatingActionButton)item. findViewById(R.id.imgeview_wow);

                   tlike = (TextView) item.findViewById(R.id.textview_like);
                   tsmile = (TextView) item.findViewById(R.id.textview_smile);
                   tlove = (TextView) item.findViewById(R.id.textview_love);
                   tangry = (TextView) item.findViewById(R.id.textview_angry);
                   twow = (TextView) item.findViewById(R.id.textview_wow);

               }

///////////////////////////type post_one_image

            else if (type == 1) {
                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_one_image = (ImageView) item.findViewById(R.id.profile_one_image);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                   commentt = (TextView) item.findViewById(R.id.post_comment);
                   share = (TextView) item.findViewById(R.id.timline_share);
                   likee = (ImageView) item.findViewById(R.id.time_line_like);
                   post_remove = (ImageView) item.findViewById(R.id.post_remove);

               }

            //////////////////////////////post_tow_image

            else if (type == 2) {
                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_one_image = (ImageView) item.findViewById(R.id.profile_one_image);
                profile_tow_image = (ImageView) item.findViewById(R.id.profile_tow_image);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                   commentt = (TextView) item.findViewById(R.id.post_comment);
                   share = (TextView) item.findViewById(R.id.timline_share);
                   likee = (ImageView) item.findViewById(R.id.time_line_like);
                   post_remove = (ImageView) item.findViewById(R.id.post_remove);
               }
//////////////////////////post_three_image_extra

            else if (type == 3) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
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
                more_than_three_icon = (TextView) item.findViewById(R.id. more_than_three_icon);
                   commentt = (TextView) item.findViewById(R.id.post_comment);
                   share = (TextView) item.findViewById(R.id.timline_share);
                   likee = (ImageView) item.findViewById(R.id.time_line_like);
                   post_remove = (ImageView) item.findViewById(R.id.post_remove);

               }
/////////////////////////////////////////////////Post_viedo
            else if (type == 4) {
                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_viedo = (WebView) item.findViewById(R.id.profile_video);
                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                   commentt = (TextView) item.findViewById(R.id.post_comment);
                   share = (TextView) item.findViewById(R.id.timline_share);
                   likee = (ImageView) item.findViewById(R.id.time_line_like);
                   post_remove = (ImageView) item.findViewById(R.id.post_remove);

               }

            /////////////////////////////////////////////////Post_addfriend
            else if (type == 5) {
                profile_title = (TextView) item.findViewById(R.id.profile_add_friend_titel);
                profile_list = (RecyclerView) item.findViewById(R.id.recyclerView);
                profile_list.setLayoutManager(new LinearLayoutManager(EditJobActivity.con, LinearLayoutManager.HORIZONTAL, false));
                   commentt = (TextView) item.findViewById(R.id.post_comment);
                   share = (TextView) item.findViewById(R.id.timline_share);
                   likee = (ImageView) item.findViewById(R.id.time_line_like);
                   post_remove = (ImageView) item.findViewById(R.id.post_remove);

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

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url)
        {
            webview.loadUrl(url);
            return true;
        }}



    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }


        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);

                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                   // tv.setTextColor(Color.parseColor("#22bf62"));
                } /*else {


                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }*/
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);


        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());

                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Read Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);


                        tv.invalidate();
                        makeTextViewResizable(tv, 2, "Read More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

                    }
        return ssb;

    }



    private void Create_like(final String token, final String post_id, final String like_type) {
        Log.d("token", token);
        Log.d("id", post_id);
        Log.d("like_type", like_type);



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


                params.put("access_token",token);

                params.put("post_comment_id",post_id);
                params.put("type","0");
                params.put("type_form",like_type);

                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }



}