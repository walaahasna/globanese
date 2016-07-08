package com.globanese.is.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.globanese.is.R;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        de.hdodenhof.circleimageview.CircleImageView h=new de.hdodenhof.circleimageview.CircleImageView(this);
    }
}
