package com.globanese.is.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.globanese.is.R;
import com.globanese.is.model.UserObject;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;
import com.globanese.is.utils.StaticClass;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends BaseActivity {

    @InjectView(R.id.logout) Button logout;
    @InjectView(R.id.view_profile) Button view_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this,this);

        StaticClass.overrideFonts(this,findViewById(android.R.id.content));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finish();
            }
        });


        view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
            }
        });

//////// if token is null///
        if(getLogInUser()==null){
           // not sigup
            logout.setVisibility(View.GONE);
            view_profile.setVisibility(View.GONE);
        }else{

            ///user is sign up
            getUserInfo();
        }

    }

////// user is sign up
    void getUserInfo(){
        showProgressDialog();
            new Project_Web_Functions().GetUserInfo(getLogInUser().getAccess_token(), new UniversalCallBack() {
            @Override
            public void onResponse(Object result) {
                ResponseObject responseObject=(ResponseObject)result;
                if(responseObject.getStatus()){
                    UserObject userObject=(UserObject)responseObject.getItems();

                    saveUserObject(userObject);
                    checkForSignUpCompletion(userObject);
                }else{

                }
            }

            @Override
            public void onFailure(Object result) {

            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }



    private void checkForSignUpCompletion(UserObject userObject) {
        if(userObject.getEmptyRequiredUserInfo().size()>0){

            startActivity(new Intent(HomeActivity.this,SignUpCompleteActivity.class));
        }
    }

}
