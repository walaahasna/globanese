package com.globanese.is.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.globanese.is.R;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;
import com.globanese.is.utils.StaticClass;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ForgetPasswordActivity extends BaseActivity {


    @InjectView(R.id.email)
    EditText email;

    @InjectView(R.id.send)
    Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.inject(this, this);

        StaticClass.overrideFonts(this,findViewById(android.R.id.content));

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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail()) {
                    doSend();
                }
            }
        });

    }

    private void setEditTextError(EditText editText) {
        editText.setBackgroundResource(R.drawable.round_login_edittext_error);
    }

    private void clearEditTextError(EditText editText) {
        editText.setBackgroundResource(R.drawable.round_login_edittext);
    }


    private boolean validateEmail() {
        boolean ret = true;

        if (email.getText().toString().trim().isEmpty()) {
//            password_edittext.setError("empty password!");
            setEditTextError(email);
            ret = false;
        }

        return ret;
    }

    void doSend() {
        showProgressDialog();
        new Project_Web_Functions().ForgetPassword(
                email.getText().toString().trim(),
                new UniversalCallBack() {

                    @Override
                    public void onResponse(Object result) {
                        ResponseObject responseObject=(ResponseObject)result;
                        if(responseObject.getStatus()){
                            Toast.makeText(ForgetPasswordActivity.this,"please check your email for password reset instructions",Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(ForgetPasswordActivity.this,"email not found",Toast.LENGTH_LONG).show();
                            setEditTextError(email);
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
