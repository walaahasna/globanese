package com.globanese.is.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.globanese.is.R;
import com.globanese.is.adapters.CommentAdapters;
import com.globanese.is.model.CommentObject;
import com.globanese.is.utils.StaticClass;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
TextView Write_commnet;
    ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        StaticClass.overrideFonts(this, findViewById(android.R.id.content));

        Write_commnet=(TextView) findViewById(R.id.write_commnet);
        close= (ImageButton)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        List<CommentObject> Comments =new ArrayList<CommentObject>();
        CommentObject a=new   CommentObject();
        a.setName("Craig Lopez");
        a.setImage(R.drawable.profile);

        CommentObject a1=new   CommentObject();
        a1.setName("Craig Lopez");
        a.setImage(R.drawable.profile);

        CommentObject a2=new   CommentObject();
        a2.setName("Craig Lopez");
        a.setImage(R.drawable.profile);
        CommentObject a3=new   CommentObject();
        a3.setName("Craig Lopez");
        a.setImage(R.drawable.profile);
        CommentObject a4=new   CommentObject();
        a4.setName("Craig Lopez");
        a.setImage(R.drawable.profile);

        Comments.add(a);
        Comments.add(a1);
        Comments.add(a2);
        Comments.add(a3);


       ListView rv = (ListView)findViewById(R.id.recyclerView);
        CommentAdapters adapter = new CommentAdapters(getApplicationContext(), Comments);

        rv.setAdapter(adapter);






    }
}
