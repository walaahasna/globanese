package com.globanese.is.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

import com.globanese.is.R;

public class SplashScreen extends BaseActivity {

    Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_splash);
        con = this;



//        final TextView version_tv = (TextView) findViewById(R.id.splash_version);
//        try {
//            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            //version_tv.setText(pInfo.versionName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }




        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(getLogInUser()==null) {
                    startActivity(new Intent(con,LoginActivity.class));
                }else{
                    startActivity(new Intent(con, HomeActivity.class));
                }
                finish();
            }
        }.start();

    }

}
