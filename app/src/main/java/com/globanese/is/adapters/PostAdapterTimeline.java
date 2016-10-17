package com.globanese.is.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.globanese.is.R;
import com.globanese.is.activities.BaseActivity;
import com.globanese.is.activities.CommentActivity;
import com.globanese.is.activities.DeletePostActivity;
import com.globanese.is.activities.ShareActivity;
import com.globanese.is.activities.TimeLineActivity;
import com.globanese.is.model.PostObject;
import com.globanese.is.model.UserAction;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.network.VolleyStringRequest;
import com.shamanland.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapterTimeline extends BaseAdapter {
    Animation fab_open_close, fab_open, fab_open_t, fab_open_tt, fab_open_ttt, fab_open_tttt;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        final PostObject s = Post.get(position);

        //////////////post_text=0

        if (s.getType().equals("text_post") && s.getEvent_type_id().equals("1")) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_text, parent, false);

            mViewHolder = new MyViewHolder(convertView, 0);

            convertView.setTag(mViewHolder);


            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getLocation());
            }

            mViewHolder.profile_text.setText(s.getText());


            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());


            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);

            if (s.getPhoto() != null) {
                Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_image);
                Log.d("not null photo", s.getPhoto());
            } else {
                ////////////////////image
                mViewHolder.profile_image.setImageResource(R.drawable.user_image_placeholder);
            }
            ////is_like_user_post


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }


            ////////////comment

            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////share


            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                    //share_post(token, String.valueOf(s.getId()), s.getUser_share_from());

                }
            });


//////////////////////////////////////like_type


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();

                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }

                }

            });

            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);


                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });

        }



        /////////// //////////////post_one_image_post=1
        else if (s.getType().equals("photo_post") && s.getPhotocount() == 1 && s.getEvent_type_id().equals("1")) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            Log.e("here 1 ", 1 + "");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_one_image, parent, false);
            mViewHolder = new MyViewHolder(convertView, 1);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getLocation());
            }

            mViewHolder.profile_text.setText(s.getText());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            //  Picasso.with(context).load(s.getPhoto()).into( mViewHolder.profile_one_image);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_image);
            for (int i = 0; i < s.getPhoto_araay().size(); i++) {
                Log.d("lenght", String.valueOf(s.getPhoto_araay().size()));


                Picasso.with(context).load(s.getPhoto_araay().get(0)).into(mViewHolder.profile_one_image);


            }

            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }
            ////////////comment

            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////
/////share

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

//////////////////////////////////////like_type
            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });

        }


        //////////////post_tow_image_post=2
        else if (s.getType().equals("photo_post") && s.getPhotocount() == 2 && s.getEvent_type_id().equals("1")) {


            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            Log.e("here 2 ", 2 + "");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_tow_image, parent, false);
            mViewHolder = new MyViewHolder(convertView, 2);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_text.setText(s.getText());
            if (s.getLocation() != null) {

                mViewHolder.profile_counntry.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getLocation());
            }

            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_image);


            for (int i = 0; i < s.getPhoto_araay().size(); i++) {
                Log.d("lenght", String.valueOf(s.getPhoto_araay().size()));


                Picasso.with(context).load(s.getPhoto_araay().get(0)).into(mViewHolder.profile_one_image);
                Picasso.with(context).load(s.getPhoto_araay().get(1)).into(mViewHolder.profile_tow_image);


            }


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }
            ////////////comment

            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                ///text


                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));


                    return false;
                }
            });
/////
/////share

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

//////////////////////////////////////like_type
            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


//////////////post_three_image_post=3

        else if (s.getType().equals("photo_post") && s.getPhotocount() == 3 && s.getEvent_type_id().equals("1")) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);

            Log.e("here 3 ", 3 + "");


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_three_image, parent, false);


            mViewHolder = new MyViewHolder(convertView, 3);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_text.setText(s.getText());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getLocation());
            }

            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_image);
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            for (int i = 0; i < s.getPhoto_araay().size(); i++) {
                Log.d("lenght", String.valueOf(s.getPhoto_araay().size()));
                Picasso.with(context).load(s.getPhoto_araay().get(0)).into(mViewHolder.profile_one_image);
                Picasso.with(context).load(s.getPhoto_araay().get(1)).into(mViewHolder.profile_tow_image);
                Picasso.with(context).load(s.getPhoto_araay().get(2)).into(mViewHolder.profile_three_image);
            }


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }
            ////////////comment

            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////
/////share

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

//////////////////////////////////////like_type
            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.layout.setVisibility(View.GONE);
                    mViewHolder.likee.setEnabled(true);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }
//////////////////////////////////////////////post_ more_three_image


        else if (s.getType().equals("photo_post") && s.getPhotocount() >= 4 & s.getEvent_type_id().equals("1")) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);

            Log.e("here 3 ", 3 + "");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_three_image_extra, parent, false);


            mViewHolder = new MyViewHolder(convertView, 3);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_text.setText(s.getText());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getLocation());
            }
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_image);
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            int num = s.getPhotocount() - 3;
            mViewHolder.more_than_three_icon.setText("+" + " " + num);
            for (int i = 0; i < s.getPhoto_araay().size(); i++) {
                Log.d("lenght", String.valueOf(s.getPhoto_araay().size()));
                Picasso.with(context).load(s.getPhoto_araay().get(0)).into(mViewHolder.profile_one_image);
                Picasso.with(context).load(s.getPhoto_araay().get(1)).into(mViewHolder.profile_tow_image);
                Picasso.with(context).load(s.getPhoto_araay().get(2)).into(mViewHolder.profile_three_image);

            }


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }
            ////////////comment

            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

//////////////////////////////////////like_type
            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


////////////////////////////////////// post_video=4
        else if (s.getType().equals("video_post") && s.getEvent_type_id().equals("1")) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_video, parent, false);


            mViewHolder = new MyViewHolder(convertView, 4);
            convertView.setTag(mViewHolder);
            mViewHolder.profile_name.setText(s.getName());
            mViewHolder.profile_time.setText(s.getCreated_at());
            mViewHolder.profile_text.setText(s.getText());

            if (s.getLocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getLocation());
            }
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_image);
            mViewHolder.profile_viedo.getSettings().setJavaScriptEnabled(true);
            mViewHolder.profile_viedo.loadUrl(s.getVideo());
            mViewHolder.profile_viedo.setWebViewClient(new HelloWebViewClient());
            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }
            ////////////comment

            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

//////////////////////////////////////like_type
            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));
                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


        ///////////////////////////share_text

        else if (s.getType().equals("share_post") && s.getEvent_type_id().equals("4") && s.getShare_type().equals("0")) {

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.share_post_text, parent, false);

            mViewHolder = new MyViewHolder(convertView, 5);
            convertView.setTag(mViewHolder);


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }


            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());

            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }

            mViewHolder.profile_text.setText(s.getSharetext());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);
            /////
            mViewHolder.profile_namee.setText(s.getName());
            mViewHolder.profile_timee.setText(s.getCreated_at());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }
            mViewHolder.profile_textt.setText(s.getText());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
            makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });

            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }

////share_one_image

        else if (s.getType().equals("share_post") && s.getEvent_type_id().equals("4") && s.getShare_type().equals("1") && s.getSharephotocount() == 1) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.share_post_one_image, parent, false);

            mViewHolder = new MyViewHolder(convertView, 6);

            convertView.setTag(mViewHolder);


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);

            for (int i = 0; i < s.getShare_photo_araay().size(); i++) {
                Log.d("lenght", String.valueOf(s.getShare_photo_araay().size()));

                Picasso.with(context).load(s.getShare_photo_araay().get(0)).into(mViewHolder.share_profile_one_image);


            }


            /////
            mViewHolder.profile_namee.setText(s.getName());
            mViewHolder.profile_timee.setText(s.getCreated_at());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }
            mViewHolder.profile_textt.setText(s.getText());
            mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
            makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////
            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


        ////share_tow_image

        else if (s.getType().equals("share_post") && s.getEvent_type_id().equals("4") && s.getShare_type().equals("1") && s.getSharephotocount() == 2) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.share_post_tow_image, parent, false);

            mViewHolder = new MyViewHolder(convertView, 7);

            convertView.setTag(mViewHolder);


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);

            for (int i = 0; i < s.getShare_photo_araay().size(); i++) {
                Log.d("lenght_share", String.valueOf(s.getShare_photo_araay().size()));

                Picasso.with(context).load(s.getShare_photo_araay().get(0)).into(mViewHolder.share_profile_one_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(1)).into(mViewHolder.share_profile_tow_image);


            }


            /////
            mViewHolder.profile_namee.setText(s.getName());
            mViewHolder.profile_timee.setText(s.getCreated_at());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }
            mViewHolder.profile_textt.setText(s.getText());
            mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
            makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


        /////////////////////share_three_image
        else if (s.getType().equals("share_post") && s.getEvent_type_id().equals("4") && s.getShare_type().equals("1") && s.getSharephotocount() == 3) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.share_post_three_image, parent, false);

            mViewHolder = new MyViewHolder(convertView, 8);

            convertView.setTag(mViewHolder);


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);

            for (int i = 0; i < s.getShare_photo_araay().size(); i++) {
                Log.d("lenght_share", String.valueOf(s.getShare_photo_araay().size()));

                Picasso.with(context).load(s.getShare_photo_araay().get(0)).into(mViewHolder.share_profile_one_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(1)).into(mViewHolder.share_profile_tow_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(2)).into(mViewHolder.share_profile_three_image);

            }


            /////
            mViewHolder.profile_namee.setText(s.getName());
            mViewHolder.profile_timee.setText(s.getCreated_at());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }
            mViewHolder.profile_textt.setText(s.getText());
            mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
            makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost

            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Log.d("userid", userid);
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });
            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });
        }
////share_three_image_extra


        else if (s.getType().equals("share_post") && s.getEvent_type_id().equals("4") && s.getShare_type().equals("1") && s.getSharephotocount() >= 4) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.share_post_three_image_extra, parent, false);

            mViewHolder = new MyViewHolder(convertView, 9);

            convertView.setTag(mViewHolder);
//////////

            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);
            int num = s.getSharephotocount() - 3;
            mViewHolder.share_more_than_three_icon.setText("+" + " " + num);

            for (int i = 0; i < s.getShare_photo_araay().size(); i++) {
                Log.d("lenght_share", String.valueOf(s.getShare_photo_araay().size()));

                Picasso.with(context).load(s.getShare_photo_araay().get(0)).into(mViewHolder.share_profile_one_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(1)).into(mViewHolder.share_profile_tow_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(2)).into(mViewHolder.share_profile_three_image);

            }


            /////
            mViewHolder.profile_namee.setText(s.getName());
            mViewHolder.profile_timee.setText(s.getCreated_at());
            if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }
            mViewHolder.profile_textt.setText(s.getText());
            mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
            makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////
            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


        //////////////share_video

    /* else if (s.getType().equals("share_post") && s.getEvent_type_id().equals("4") && s.getShare_type().equals("2")) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.share_post_video, parent, false);

Log.d("time",s.getCreated_at());
            Log.d("country",s.getSharelocation());
            Log.d("photo",s.getPhoto());


            mViewHolder = new MyViewHolder(convertView, 10);
            convertView.setTag(mViewHolder);

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);

    mViewHolder.profile_namee.setText(s.getName());
     mViewHolder.profile_timee.setText(s.getCreated_at());
       mViewHolder.profile_textt.setText(s.getText());
         if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {

                mViewHolder.profile_counntryy.setText(s.getLocation());
            }
            mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());

            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);
            mViewHolder.profile_viedo.getSettings().setJavaScriptEnabled(true);
            mViewHolder.profile_viedo.loadUrl(s.getSharevideo());
            mViewHolder.profile_viedo.setWebViewClient(new HelloWebViewClient());


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }


            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });

            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////share

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });

//////////////////////////////////////like_type
            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }*/

/////////////////// ///comment_text

        else if (s.getType().equals("comment_post") && s.getShare_type().equals("0")) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_post_text, parent, false);

            mViewHolder = new MyViewHolder(convertView, 5);

            convertView.setTag(mViewHolder);

                 if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }

            mViewHolder.profile_text.setText(s.getSharetext());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);
            /////
            mViewHolder.profile_namee.setText(s.getName());

            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);


            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }

                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


////comment_one_image

        else if (s.getType().equals("comment_post") && s.getShare_type().equals("1") && s.getSharephotocount() == 1) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_one_image, parent, false);

            mViewHolder = new MyViewHolder(convertView, 10);

            convertView.setTag(mViewHolder);


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }

            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);

            for (int i = 0; i < s.getShare_photo_araay().size(); i++) {
                Log.d("lenght", String.valueOf(s.getShare_photo_araay().size()));

                Picasso.with(context).load(s.getShare_photo_araay().get(0)).into(mViewHolder.share_profile_one_image);


            }


            /////
            mViewHolder.profile_namee.setText(s.getName());
           // mViewHolder.profile_timee.setText(s.getCreated_at());
           /* if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }
            */

           // mViewHolder.profile_textt.setText(s.getText());
          //  mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
          //  makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
         //   Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


        //////////////////////////////comment_tow_image

        else if (s.getType().equals("comment_post") && s.getSharephotocount() == 2) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_tow_image, parent, false);

            mViewHolder = new MyViewHolder(convertView, 11);

            convertView.setTag(mViewHolder);



            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);

            for (int i = 0; i < s.getShare_photo_araay().size(); i++) {
                Log.d("lenght_share", String.valueOf(s.getShare_photo_araay().size()));

                Picasso.with(context).load(s.getShare_photo_araay().get(0)).into(mViewHolder.share_profile_one_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(1)).into(mViewHolder.share_profile_tow_image);


            }


            /////
            mViewHolder.profile_namee.setText(s.getName());
          //  mViewHolder.profile_timee.setText(s.getCreated_at());
          /*  if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }*/
           // mViewHolder.profile_textt.setText(s.getText());
          //  mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
          //  makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
         //   Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }


/////////////////////////////////////////////comment_three_image

        else if (s.getType().equals("comment_post") && s.getSharephotocount() == 3) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_three_image, parent, false);

            mViewHolder = new MyViewHolder(convertView, 12);

            convertView.setTag(mViewHolder);


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);

            for (int i = 0; i < s.getShare_photo_araay().size(); i++) {
                Log.d("lenght_share", String.valueOf(s.getShare_photo_araay().size()));

                Picasso.with(context).load(s.getShare_photo_araay().get(0)).into(mViewHolder.share_profile_one_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(1)).into(mViewHolder.share_profile_tow_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(2)).into(mViewHolder.share_profile_three_image);

            }


            /////
            mViewHolder.profile_namee.setText(s.getName());
          //  mViewHolder.profile_timee.setText(s.getCreated_at());
           /* if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }*/
           // mViewHolder.profile_textt.setText(s.getText());
          //  mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
           // makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
           // Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost

            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Log.d("userid", userid);
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });
            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });
        }



///////////////////////////////////////////////comment_three_image_extra


        else if (s.getType().equals("comment_post") &&  s.getSharephotocount() >= 4) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_three_image_extra, parent, false);

            mViewHolder = new MyViewHolder(convertView, 13);

            convertView.setTag(mViewHolder);
//////////

            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
            mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);
            int num = s.getSharephotocount() - 3;
            mViewHolder.share_more_than_three_icon.setText("+" + " " + num);

            for (int i = 0; i < s.getShare_photo_araay().size(); i++) {
                Log.d("lenght_share", String.valueOf(s.getShare_photo_araay().size()));

                Picasso.with(context).load(s.getShare_photo_araay().get(0)).into(mViewHolder.share_profile_one_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(1)).into(mViewHolder.share_profile_tow_image);
                Picasso.with(context).load(s.getShare_photo_araay().get(2)).into(mViewHolder.share_profile_three_image);

            }

            /////
            mViewHolder.profile_namee.setText(s.getName());
          //  mViewHolder.profile_timee.setText(s.getCreated_at());
          /*  if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }*/
          //  mViewHolder.profile_textt.setText(s.getText());
          //  mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
          //  makeTextViewResizable(mViewHolder.profile_textt, 2, "Read More", true);
            //Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_imagee);

            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////
            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }

///////////////////////////////////////////////comment_video

        else if (s.getType().equals("comment_post") && s.getShare_type().equals("2")) {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_video, parent, false);


            mViewHolder = new MyViewHolder(convertView, 16);
            convertView.setTag(mViewHolder);

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getSharecreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getSharelocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getSharelocation());
            }
          //  mViewHolder.profile_text.setText(s.getSharetext());
            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            //Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_image);


            mViewHolder.profile_namee.setText(s.getName());
          //  mViewHolder.profile_timee.setText(s.getCreated_at());
          //  mViewHolder.profile_textt.setText(s.getText());
          /*  if (s.getLocation() != null) {
                mViewHolder.profile_counntryy.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntryy.setText(s.getLocation());
            }*/
           // mViewHolder.friend_share_name.setText(s.getName_of_user_ishare_from());
            Picasso.with(context).load(s.getSharephoto()).into(mViewHolder.profile_imagee);
            mViewHolder.profile_viedo.getSettings().setJavaScriptEnabled(true);
            mViewHolder.profile_viedo.loadUrl(s.getSharevideo());
            mViewHolder.profile_viedo.setWebViewClient(new HelloWebViewClient());


            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            }


            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////share

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });

//////////////////////////////////////like_type
            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });



            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }



/////////////////////////////////////////////////////change_profile
        else if(s.getType().equals("changeprofile_post")){
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_change_profile_image, parent, false);

            mViewHolder = new MyViewHolder(convertView, 17);

            convertView.setTag(mViewHolder);

            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }

            mViewHolder.profile_name.setText(s.getSharename());
        mViewHolder.profile_time.setText(s.getCreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getLocation());
            }

            mViewHolder.profile_text.setText(s.getSharetext());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_image);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder. share_profile_one_image);
            /////
          //  mViewHolder.profile_namee.setText(s.getName());

            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());



            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }

                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }
////////////////////////////////////////////////////change_cover


        else if(s.getType().equals("changecover_post")){
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.double_up_click);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_change_cover_image, parent, false);

            mViewHolder = new MyViewHolder(convertView, 17);

            convertView.setTag(mViewHolder);

            if (s.is_like_user_post() == true) {
                mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }

            mViewHolder.profile_name.setText(s.getSharename());
            mViewHolder.profile_time.setText(s.getCreated_at());
            if (s.getSharelocation() != null) {
                mViewHolder.profile_counntry.setText("- " + s.getLocation());

            } else {
                mViewHolder.profile_counntry.setText(s.getLocation());
            }

            mViewHolder.profile_text.setText(s.getSharetext());
            makeTextViewResizable(mViewHolder.profile_text, 2, "Read More", true);
            Picasso.with(context).load(s.getPhoto()).into(mViewHolder.profile_image);
            Picasso.with(context).load(s.getCover_photo()).into(mViewHolder. share_profile_one_image);
            /////
            //  mViewHolder.profile_namee.setText(s.getName());

            mViewHolder.profile_No_like.setText(s.getLikecount());
            mViewHolder.profile_No_comment.setText(s.getCommentcount());
            mViewHolder.profile_No_share.setText(s.getSharecount());



            ////comment
            mViewHolder.commentt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id", String.valueOf(s.getId()));
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("like", s.getLike_count());
                    intent.putExtra("smile", s.getSmile_count());
                    intent.putExtra("love", s.getLove_count());

                    intent.putExtra("angry", s.getAngry_count());

                    intent.putExtra("wow", s.getWow_count());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////removepost
            mViewHolder.post_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeletePostActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("timeline_id", s.getTimeline_post_id());
                    intent.putExtra("post_id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            /////////////////like
            mViewHolder.likee.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mViewHolder.likee.setEnabled(false);
                    mViewHolder.layout.setVisibility(View.VISIBLE);
                    fab_open_close = AnimationUtils.loadAnimation(context, R.anim.fab_open_close);
                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_open_t = AnimationUtils.loadAnimation(context, R.anim.fab_open_t);
                    fab_open_tt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tt);
                    fab_open_ttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_ttt);
                    fab_open_tttt = AnimationUtils.loadAnimation(context, R.anim.fab_open_tttt);

                    mViewHolder.close.startAnimation(fab_open_close);

                    mViewHolder.imgviewlike.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlike.startAnimation(fab_open);


                    mViewHolder.imgviewsmile.startAnimation(fab_open_t);
                    mViewHolder.imgviewsmile.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewsmile.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewlove.startAnimation(fab_open_tt);
                    mViewHolder.imgviewlove.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewlove.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewangry.startAnimation(fab_open_ttt);
                    mViewHolder.imgviewangry.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewangry.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    mViewHolder.imgviewwow.startAnimation(fab_open_tttt);
                    mViewHolder.imgviewwow.setColor(context.getResources().getColor(R.color.status_bar_color));
                    mViewHolder.imgviewwow.setBackgroundColor(context.getResources().getColor(R.color.status_bar_color));

                    /////////////////////////


                    return false;
                }
            });
/////

            mViewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity b = new BaseActivity();
                    Intent intent = new Intent(v.getContext(), ShareActivity.class);
                    String id = String.valueOf(s.getId());
                    intent.putExtra("post_id", id);
                    intent.putExtra("User_share_from", s.getUser_share_from());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });


            mViewHolder.likee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    mViewHolder.likee.startAnimation(zoom);
                    mp.start();
                    mViewHolder.layout.setVisibility(View.GONE);
                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());

                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "short");
                    if (action.getGreatelike() == true) {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }
            });


            mViewHolder.imgviewlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    //Create_like(token, String.valueOf(s.getId()), "0");

                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 0, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }

                }

            });


            mViewHolder.imgviewsmile.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 1, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewlove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 2, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });


            mViewHolder.imgviewangry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 3, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {

                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.imgviewwow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                    BaseActivity b = new BaseActivity();
                    String token = b.getLogInUser().getAccess_token();
                    String userid = String.valueOf(b.getUserObject().getId());
                    UserAction action = b.like(token, String.valueOf(s.getId()), userid, 4, "long");
                    if (action.getGreatelike() == true) {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());


                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        Log.d("likecount", s.getLikecount());

                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like2);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    } else {
                        Post.get(position).setLike_count(action.getLike_count());
                        Post.get(position).setSmile_count(action.getSmile_count());
                        Post.get(position).setLove_count(action.getLove_count());
                        Post.get(position).setAngry_count(action.getAngry_count());
                        Post.get(position).setWow_count(action.getWow_count());
                        Post.get(position).setLikecount(action.getLikecount());
                        mViewHolder.profile_No_like.setText(s.getLikecount());
                        mViewHolder.profile_No_like.setTextColor(context.getResources().getColor(R.color.like));
                        mViewHolder.likee.setImageResource(R.drawable.time_line_like);
                        mViewHolder.lisk_post.setTextColor(context.getResources().getColor(R.color.like));

                    }


                }

            });

            mViewHolder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.likee.setEnabled(true);
                    mViewHolder.layout.setVisibility(View.GONE);

                }
            });


        }





        return convertView;
    }




    private class MyViewHolder {
        MyRecyclerViewAdapt adapter;
        RecyclerView profile_list;
        TextView lisk_post, share, profile_namee, profile_name, more_than_three_icon, profile_timee, profile_time, profile_counntry, profile_counntryy, profile_text, profile_textt, profile_No_like, profile_No_comment, profile_No_share, profile_title, profile_group_name, commentt;
        CircleImageView profile_image, profile_imagee;
        WebView profile_viedo;
        ImageView profile_one_image, profile_tow_image, profile_three_image, likee, post_remove, close, share_profile_one_image, share_profile_tow_image, share_profile_three_image;
        FloatingActionButton imgviewlike, imgviewsmile, imgviewlove, imgviewangry, imgviewwow;
        TextView tlike, tsmile, tlove, tangry, twow, friend_share_name, share_more_than_three_icon;
        RelativeLayout layout;
        FrameLayout frame;

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
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


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

                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


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


                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);
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

                more_than_three_icon = (TextView) item.findViewById(R.id.more_than_three_icon);
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);

                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);

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


                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);
            }


///////////////////////// share_text
            else if (type == 5) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_timee = (TextView) item.findViewById(R.id.profile_timee);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);

                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


            }


            //////share_one_image
            else if (type == 6) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);


                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_timee = (TextView) item.findViewById(R.id.profile_timee);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);

                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////


                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


            }
////////////////////////////////////////////comment_one_image

            else if (type == 10) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
              ///  profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);


                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
               // profile_timee = (TextView) item.findViewById(R.id.profile_timee);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
               // profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);

                profile_text = (TextView) item.findViewById(R.id.profile_text);
               // profile_textt = (TextView) item.findViewById(R.id.profile_textt);
               // friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////


                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


            }


////////////////////////////////change_profile

            else if (type == 17) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                ///  profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

              //  profile_one_image = (ImageView) item.findViewById(R.id.profile_one_image);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
               // profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
                // profile_timee = (TextView) item.findViewById(R.id.profile_timee);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                // profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);

                profile_text = (TextView) item.findViewById(R.id.profile_text);
                // profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                // friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

              share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////


                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


            }

            ////////////////share_tow_image
            else if (type == 7) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_timee = (TextView) item.findViewById(R.id.profile_timee);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);

                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);
                share_profile_tow_image = (ImageView) item.findViewById(R.id.share_profile_tow_image);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);

            }


           /// comment_tow_image

            else if (type == 11) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
               // profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
              //  profile_timee = (TextView) item.findViewById(R.id.profile_timee);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
              //  profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);

                profile_text = (TextView) item.findViewById(R.id.profile_text);
              //  profile_textt = (TextView) item.findViewById(R.id.profile_textt);
              //  friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);
                share_profile_tow_image = (ImageView) item.findViewById(R.id.share_profile_tow_image);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);

            }





            /////share_three_image

            else if (type == 8) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_timee = (TextView) item.findViewById(R.id.profile_timee);


                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);

                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);
                share_profile_tow_image = (ImageView) item.findViewById(R.id.share_profile_tow_image);
                share_profile_three_image = (ImageView) item.findViewById(R.id.share_profile_three_image);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);

            }


/////////////////////////////////////comment_three_image
            else if (type ==12) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                //profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
               // profile_timee = (TextView) item.findViewById(R.id.profile_timee);


                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                //profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);

                profile_text = (TextView) item.findViewById(R.id.profile_text);
               // profile_textt = (TextView) item.findViewById(R.id.profile_textt);
              //  friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);
                share_profile_tow_image = (ImageView) item.findViewById(R.id.share_profile_tow_image);
                share_profile_three_image = (ImageView) item.findViewById(R.id.share_profile_three_image);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


            }


/////share_three_image_extra
            else if (type == 9) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
                profile_timee = (TextView) item.findViewById(R.id.profile_timee);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);
                share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);
                share_profile_tow_image = (ImageView) item.findViewById(R.id.share_profile_tow_image);
                share_profile_three_image = (ImageView) item.findViewById(R.id.share_profile_three_image);
                share_more_than_three_icon = (TextView) item.findViewById(R.id.share_more_than_three_icon);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////
                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


            }
////////////////////////////////comment_three_image_extra

            else if (type == 13) {

                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
               // profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_time = (TextView) item.findViewById(R.id.profile_time);
              //  profile_timee = (TextView) item.findViewById(R.id.profile_timee);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
              //  profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);
                profile_text = (TextView) item.findViewById(R.id.profile_text);
              //  profile_textt = (TextView) item.findViewById(R.id.profile_textt);
              //  friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);
                share_profile_one_image = (ImageView) item.findViewById(R.id.share_profile_one_image);
                share_profile_tow_image = (ImageView) item.findViewById(R.id.share_profile_tow_image);
                share_profile_three_image = (ImageView) item.findViewById(R.id.share_profile_three_image);
                share_more_than_three_icon = (TextView) item.findViewById(R.id.share_more_than_three_icon);


                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                /////////////////////////////////
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////
                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);


            }






            /////share_video
            else if (type == 10) {
                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
                profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

                profile_timee = (TextView) item.findViewById(R.id.profile_timee);
                profile_time = (TextView) item.findViewById(R.id.profile_time);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
                profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);


                profile_text = (TextView) item.findViewById(R.id.profile_text);
                profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                profile_viedo = (WebView) item.findViewById(R.id.profile_video);
                friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);


                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);
            }

            //////////////////////comment_video


            else if (type == 16) {

                profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

              //  profile_timee = (TextView) item.findViewById(R.id.profile_timee);
                profile_time = (TextView) item.findViewById(R.id.profile_time);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
              //  profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);


                profile_text = (TextView) item.findViewById(R.id.profile_text);
               // profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                profile_viedo = (WebView) item.findViewById(R.id.profile_video);
              //  friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);


                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);
            }







            else if (type == 14) {
                profile_image = (CircleImageView) item.findViewById(R.id.profile_image);
               // profile_imagee = (CircleImageView) item.findViewById(R.id.profile_imagee);

                profile_name = (TextView) item.findViewById(R.id.profile_name);
                profile_namee = (TextView) item.findViewById(R.id.profile_namee);

               // profile_timee = (TextView) item.findViewById(R.id.profile_timee);
                profile_time = (TextView) item.findViewById(R.id.profile_time);

                profile_counntry = (TextView) item.findViewById(R.id.profile_country);
               // profile_counntryy = (TextView) item.findViewById(R.id.profile_countryy);


                profile_text = (TextView) item.findViewById(R.id.profile_text);
               // profile_textt = (TextView) item.findViewById(R.id.profile_textt);
                profile_viedo = (WebView) item.findViewById(R.id.profile_video);
               // friend_share_name = (TextView) item.findViewById(R.id.friend_share_name);

                profile_No_like = (TextView) item.findViewById(R.id.profile_No_like);
                profile_No_comment = (TextView) item.findViewById(R.id.profile_No_comment);
                profile_No_share = (TextView) item.findViewById(R.id.profile_No_share);
                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);


                commentt = (TextView) item.findViewById(R.id.post_comment);
                share = (TextView) item.findViewById(R.id.timline_share);
                likee = (ImageView) item.findViewById(R.id.time_line_like);
                post_remove = (ImageView) item.findViewById(R.id.post_remove);
                layout = (RelativeLayout) item.findViewById(R.id.commnet_emotion);

                close = (ImageView) item.findViewById(R.id.close);
                lisk_post = (TextView) item.findViewById(R.id.like_post);
                ///////////////////////

                imgviewlike = (FloatingActionButton) item.findViewById(R.id.imgeview_like);
                imgviewsmile = (FloatingActionButton) item.findViewById(R.id.imgeview_smile);
                imgviewlove = (FloatingActionButton) item.findViewById(R.id.imgeview_love);
                imgviewangry = (FloatingActionButton) item.findViewById(R.id.imgeview_angry);
                imgviewwow = (FloatingActionButton) item.findViewById(R.id.imgeview_wow);
            }

        }





    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url);
            return true;
        }
    }


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
            /////////////
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


    public static void Create_like(final String token, final String post_id, final String like_type) {
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

                params.put("access_token", token);
                params.put("post_comment_id", post_id);
                params.put("type", "0");
                params.put("type_form", like_type);
                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }

    private void share_post(final String token, final String post_id, final String user_id_from) {
        Log.d("token", token);
        Log.d("id", post_id);
        // Log.d("text", text+"k");
        //Log.d("user_id_from", user_id_from);


        String url = Project_Web_Functions.BASE_URL + "/share";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response.toString());
                try {
                    JSONObject result = new JSONObject(response);


                    if (result.getBoolean("status") == (true)) {

                        Toast.makeText(context, "share  Post success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, TimeLineActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

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
                params.put("access_token", token);
                params.put("post_id", post_id);
                //  params.put("text", text);
                //params.put("user_id_from", user_id_from);
                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);


    }


}