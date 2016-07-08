package com.globanese.is.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.globanese.is.R;
import com.globanese.is.adapters.PostAdapter;
import com.globanese.is.model.PostObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;

import java.util.ArrayList;
import java.util.List;


public class TimeLineActivity extends BaseActivity{
    ListView l;
      ImageButton write_post;
    List<PostObject> posts;
    PostAdapter p;
         protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
      final String   Token=getLogInUser().getAccess_token();
      final int   user_id= getUserObject().getId();
      final String viedo="gfgfgfg";
      final String location="palestine";
      final int privacy=0;
      final String photo="vbcbvcv v";
      final String text="palestine";








       write_post=(ImageButton)findViewById(R.id.wrire_post);


             write_post.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Greate_post(text,viedo,location,privacy,photo,user_id,Token);
                 }
             });

/////////////////////////////////////////////
  l=(ListView)findViewById(R.id.List_time_line);
 posts=new ArrayList<PostObject>();
       // PostObject p1=new PostObject();
       // p1.setPosttype(0);
        // PostObject p2=new PostObject();
       // p2.setPosttype(1);
       // p2.setId(R.drawable.piza);
      //  posts.add(p1);
       // posts.add(p2);
             // p=new PostAdapter(getApplicationContext(),posts);

             //   l.setAdapter(p);




    }



    void Greate_post(final String text,final String viedo,final String location,final int privacy,final String photo,final int user_id,final String Token) {
        {
            showProgressDialog();
            new Project_Web_Functions().GreatePost(text, viedo, location, privacy, photo, user_id, Token, new UniversalCallBack() {


                        @Override
                        public void onResponse(Object result) {
                            ResponseObject responseObject = (ResponseObject) result;
                             if (responseObject.getStatus()) {
                                PostObject post=(PostObject)responseObject.getItems();


                             } else {
                                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onFailure(Object result) {

                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                        }
                    }
            );

        }
    }
}
