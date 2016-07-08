package com.globanese.is.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.globanese.is.R;
import com.globanese.is.adapters.PostAdapter;
import com.globanese.is.model.AddFriendObject;
import com.globanese.is.model.PostObject;
import com.globanese.is.network.ErrorMsg;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;
import com.globanese.is.utils.StaticClass;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

public class ProfileActivity extends BaseActivity {

    public static Context contex;
    @InjectView(R.id.profile_list)
    ListView listView;
    @InjectView(R.id.root)
    View rootView;
    List<PostObject> PostArray;
    @InjectView(R.id.back)
    View back;
    float minHeaderHeight;
    View list_header;
    View about_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this, this);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));

        contex = ProfileActivity.this;
        minHeaderHeight = getResources().getDimension(R.dimen.action_bar_height);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            View menu_status_bar1 = findViewById(R.id.status_bar);
            menu_status_bar1.getLayoutParams().height = StaticClass.getStatusBarHeight(this);
            minHeaderHeight += StaticClass.getStatusBarHeight(this);


        }
////////////////////////////////set style of list
        list_header = getLayoutInflater().inflate(R.layout.time_line_header, null);
        ImageView write_post = (ImageView) list_header.findViewById(R.id.write_post2);


        LinearLayout about_linear = (LinearLayout) list_header.findViewById(R.id.about_linear);

        StaticClass.overrideFonts(this, list_header);

        initViews();
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        StikkyHeaderBuilder.stickTo(listView).setHeader(R.id.header, viewGroup)
                .minHeightHeader((int) minHeaderHeight)
                .animator(new IconAnimator(list_header))
                .build();


        //////////////////////////////////////////get post requset
        ArrayList<PostObject> posts = new ArrayList<PostObject>();

        PostObject p0 = new PostObject();
        p0.setPosttype(0);

        PostObject p1 = new PostObject();
        p1.setPosttype(1);
        p1.setId(R.drawable.piza);


        PostObject p2 = new PostObject();
        p2.setId(R.drawable.piza);
        p2.setPosttype(2);


        PostObject p3 = new PostObject();
        p3.setId(R.drawable.piza);
        p3.setPosttype(3);


        PostObject p4 = new PostObject();
        p4.setPosttype(4);


    PostObject p5=new PostObject();
        List<AddFriendObject> AddFreinds=new ArrayList<AddFriendObject>();
        AddFriendObject a=new AddFriendObject();
        a.setName("Craig Lopez");
        a.setPic(R.drawable.profile);


        AddFriendObject a1=new AddFriendObject();
        a1.setName("Craig Lopez");
        a1.setPic(R.drawable.profile);


        AddFriendObject a2=new AddFriendObject();
        a2.setName("Craig Lopez");
        a2.setPic(R.drawable.profile);

        AddFriendObject a3=new AddFriendObject();
        a3.setName("Craig Lopez");
        a3.setPic(R.drawable.profile);




        AddFreinds.add(a);
        AddFreinds.add(a1);
        AddFreinds.add(a2);
        AddFreinds.add(a3);
        p5.setPosttype(5);
        p5.setFriends((ArrayList<AddFriendObject>) AddFreinds);





        posts.add(p0);
        posts.add(p1);
        posts.add(p2);
        posts.add(p3);
        posts.add(p4);
        posts.add(p5);


        PostAdapter p = new PostAdapter(getApplicationContext(), posts);


        listView.addHeaderView(list_header);
        listView.setAdapter(p);


/////////////////////////////////////////////////////////////////////////////////////

        about_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, AboutProfileActivity.class);
                startActivity(i);
            }
        });


        write_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, WritePostActivity.class);
                startActivity(i);
            }
        });

    }


    public void getPost() {
        new Project_Web_Functions().GetPost(getLogInUser().getAccess_token(), new UniversalCallBack() {
                    @Override
                    public void onResponse(Object result) {
                        ResponseObject responseObject = (ResponseObject) result;
                        if (responseObject.getStatus()) {
                            //PostObject user=new PostObject();

                            if (responseObject.getStatus()) {
                                PostArray = (ArrayList<PostObject>) responseObject.getItems();
                            }

                        } else {
                            ArrayList<ErrorMsg> errors = responseObject.getErrors();
                            for (ErrorMsg errorMsg : errors) {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Object result) {
                        Toast.makeText(getApplicationContext(), "error :(", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                }
        );
    }


    private void initViews() {
        about_linear = list_header.findViewById(R.id.about_linear);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        about_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AboutProfileActivity.class));
            }
        });
    }
}

class IconAnimator extends HeaderStikkyAnimator {

    boolean animationRunning;
    View list_header;

    IconAnimator(View list_header) {
        this.list_header = list_header;
    }

    @Override
    public AnimatorBuilder getAnimatorBuilder() {


     /* CircleImageView i= (CircleImageView) getHeader().findViewById(R.id.profile_image);
      CircleImageView i2= (CircleImageView)list_header.findViewById(R.id.profile_image2);
       BaseActivity b=new BaseActivity();
      UserObject image= b.getUserObject();
      if(!image.equals(null)) {
            Picasso.with(ProfileActivity.contex).load(image.getPhoto()).into(i);
            Picasso.with(ProfileActivity.contex).load(image.getPhoto()).into(i2);
        }*/


        AnimatorBuilder animatorBuilder = AnimatorBuilder.create()

                .applyFade2(getHeader().findViewById(R.id.profile_image), 1, 0, new mi())
                .applyFade2(list_header.findViewById(R.id.profile_image2), 1, 0, new mi())
                .applyFade2(getHeader().findViewById(R.id.header_edit_layout), 1, 0, new mi());

        return animatorBuilder;
    }

    class mi implements Interpolator {

        @Override
        public float getInterpolation(float input) {
            return (input <= 0.9) ? 0 : (input - 0.9f) * 10f;
        }
    }

//    @Override
//    public void onScroll(int scrolledY) {
//        super.onScroll(scrolledY);
////        Log.d("onScroll", getBoundedTranslatedRatio() + "");
////        Log.d("scrolledY", scrolledY + "");
////        Log.d("hh", getMinHeightHeader() + "");
//
//        Log.d("ann",animationRunning+"");
//
//        if (getBoundedTranslatedRatio() == 1) {
////            getHeader().findViewById(R.id.profile_2).setVisibility(View.VISIBLE);
//            if (!animationRunning) {
//                showHeader();
//            }
//        } else {
////            getHeader().findViewById(R.id.profile_2).setVisibility(View.GONE);
//            if (!animationRunning) {
//                hideHeader();
//            }
//        }
//
//    }


//
//    void showHeader() {
//        Log.d("sss","hhhh");
//        animationRunning = true;
//        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
//        fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
//        fadeOut.setDuration(150);
//        fadeOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.d("fo","start");
//            }
//

//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.d("fo","end");
//                getHeader().findViewById(R.id.animate_1).setVisibility(View.INVISIBLE);
//                animationRunning = false;
//                AlphaAnimation fadein = new AlphaAnimation(0,1);
//                fadein.setInterpolator(new DecelerateInterpolator()); //add this
//                fadein.setDuration(150);
//                fadein.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        getHeader().findViewById(R.id.profile_2).setVisibility(View.VISIBLE);
//                        animationRunning = false;
//                        if (getBoundedTranslatedRatio() == 1) {
//                            showHeader();
//
//                        } else {
//                            hideHeader();
//                        }
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//                getHeader().findViewById(R.id.profile_2).startAnimation(fadein);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        getHeader().findViewById(R.id.animate_1).startAnimation(fadeOut);
//    }
//
//    void hideHeader() {
//        animationRunning = true;
//        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
//        //fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
//        fadeOut.setDuration(150);
//        fadeOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                getHeader().findViewById(R.id.profile_2).setVisibility(View.GONE);
//                animationRunning = false;
//                AlphaAnimation fadein = new AlphaAnimation(0, 1);
//                //fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
//                fadein.setDuration(150);
//                fadein.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//

//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        getHeader().findViewById(R.id.animate_1).setVisibility(View.VISIBLE);
//                        animationRunning = false;
//                        if (getBoundedTranslatedRatio() == 1) {
//                            showHeader();
//
//                        } else {
//                            hideHeader();
//                        }
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//                getHeader().findViewById(R.id.animate_1).startAnimation(fadein);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        getHeader().findViewById(R.id.profile_2).startAnimation(fadeOut);
//
//    }

}
