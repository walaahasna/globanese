package com.globanese.is.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.globanese.is.R;
import com.globanese.is.network.ErrorMsg;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;
import com.globanese.is.model.LogInUser;
import com.globanese.is.utils.StaticClass;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUpActivity extends BaseActivity {

    public static SignUpActivity instance;

    @InjectView(R.id.first_name)
    EditText first_name;
    @InjectView(R.id.last_name)
    EditText last_name;
    @InjectView(R.id.email)
    EditText email;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.password_confirm)
    EditText password_confirm;

    @InjectView(R.id.signup)
    Button signup;

    @InjectView(R.id.terms)
    View terms;
    @InjectView(R.id.data_policy)
    View data_policy;
    @InjectView(R.id.cookie_policy)
    View cookie_policy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this, this);
        if(!LoginActivity.email_edittext.getText().toString().isEmpty()){

            email.setText(LoginActivity.email_edittext.getText().toString());
        }
        if(!LoginActivity.password_edittext.getText().toString().isEmpty()){

           password.setText(LoginActivity.password_edittext.getText().toString());
        }

        StaticClass.overrideFonts(this,findViewById(android.R.id.content));

        instance=this;

        first_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEditTextError(first_name);
                }
            }
        });
        last_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEditTextError(last_name);
                }
            }
        });
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEditTextError(email);
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEditTextError(password);
                }
            }
        });
        password_confirm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEditTextError(password_confirm);
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSignup()) {
                    doSignup();
                }
            }
        });


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TermsDialogFragment.newInstance("Terms",getResources().getString(R.string.terms)).show(getSupportFragmentManager(), "terms");
                getTerms();
            }
        });

        data_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TermsDialogFragment.newInstance("Data Policy",getResources().getString(R.string.terms)).show(getSupportFragmentManager(), "data_policy");
                getDataTerms();
            }
        });

        cookie_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCookieTerms();
            }
        });


    }

    private void setEditTextError(EditText editText) {
        editText.setBackgroundResource(R.drawable.round_login_edittext_error);
    }

    private void clearEditTextError(EditText editText) {
        editText.setBackgroundResource(R.drawable.round_login_edittext);
    }


    private boolean validateSignup() {
        boolean ret = true;

        if (first_name.getText().toString().trim().isEmpty()) {
//            email_edittext.setError("empty email!");
            setEditTextError(first_name);
            ret = false;
        }

        if (last_name.getText().toString().trim().isEmpty()) {
//            password_edittext.setError("empty password!");
            setEditTextError(last_name);
            ret = false;
        }

        if (email.getText().toString().trim().isEmpty()) {
//            password_edittext.setError("empty password!");
            setEditTextError(email);
            ret = false;
        }

        if (password.getText().toString().trim().isEmpty()) {
//            password_edittext.setError("empty password!");
            setEditTextError(password);
            ret = false;
        } else if (password.getText().toString().length()<6) {
//            password_edittext.setError("empty password!");
            setEditTextError(password);
            ret = false;
        }


//        if(password_confirm.getText().toString().trim().isEmpty()){
////            password_edittext.setError("empty password!");
//            setEditTextError(password_confirm);
//            ret=false;
//        }

        if (!password.getText().toString().equals(password_confirm.getText().toString())) {
            //password_edittext.setError("empty password!");
            setEditTextError(password);
            setEditTextError(password_confirm);
            ret = false;
        }

        return ret;
    }

    void doSignup() {
        showProgressDialog();
        new Project_Web_Functions().SignUpByEmail(
                first_name.getText().toString().trim(),
                last_name.getText().toString().trim(),
                email.getText().toString().trim(),
                password.getText().toString(),
                new UniversalCallBack() {
                    @Override
                    public void onResponse(Object result) {
                        ResponseObject responseObject=(ResponseObject)result;
                        if(responseObject.getStatus()){
                            LogInUser user=new LogInUser();
                            user.setLogin_type(LogInUser.LOGIN_TYPE_EMAIL);
                            user.setEmail(email.getText().toString());
                            user.setPassword(password.getText().toString());

                            HashMap<String,String> items=(HashMap<String,String>)responseObject.getItems();
                            user.setAccess_token(items.get("access_token"));
                            user.setRefresh_token(items.get("refresh_token"));
                            saveLogInUser(user);

                            startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                            finish();
                            if(LoginActivity.instance!=null){
                                LoginActivity.instance.finish();
                            }
                        }else{
                            ArrayList<ErrorMsg> errors=responseObject.getErrors();
                            for(ErrorMsg errorMsg:errors){
                                switch (errorMsg.getFieldName()){
                                    case "fname":{
                                        setEditTextError(first_name);
                                        break;
                                    }
                                    case "lname":{
                                        setEditTextError(last_name);
                                        break;
                                    }
                                    case "email":{
                                        setEditTextError(email);
                                        break;
                                    }
                                    case "password":{
                                        setEditTextError(password);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Object result) {
                        Toast.makeText(getApplicationContext(),"error :(",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                }
        );

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance=null;
    }

    private void getTerms(){
        showDialog("Terms",getResources().getString(R.string.terms));
    }

    private void getDataTerms(){
        showDialog("Data Policy",getResources().getString(R.string.terms));
    }

    private void getCookieTerms(){
        showDialog("Cookie Policy",getResources().getString(R.string.terms));
    }



    private void showDialog(String title,String body){
        TextView textView=new TextView(SignUpActivity.this);
        textView.setText(title);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        textView.setGravity(Gravity.CENTER);
        textView.setMinHeight(StaticClass.convertDpToPixel(getResources().getDimension(R.dimen.action_bar_height),SignUpActivity.this));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        AlertDialog dialog= new AlertDialog.Builder(SignUpActivity.this)
//                .setTitle(title)
                .setCustomTitle(textView)
                .setMessage(body)
                .setPositiveButton("OK", null)
//                        .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }



}
