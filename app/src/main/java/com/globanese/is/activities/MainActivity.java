package com.globanese.is.activities;

import android.os.Bundle;

import com.globanese.is.R;
import com.globanese.is.utils.StaticClass;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this,this);

        StaticClass.overrideFonts(this,findViewById(android.R.id.content));
    }


}
