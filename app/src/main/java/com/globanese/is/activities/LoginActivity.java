package com.globanese.is.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.globanese.is.model.UserObject;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.plus.Plus;
import com.globanese.is.R;
import com.globanese.is.network.Project_Web_Functions;
import com.globanese.is.network.ResponseObject;
import com.globanese.is.network.UniversalCallBack;
import com.globanese.is.model.LogInUser;
import com.globanese.is.utils.StaticClass;
import com.linkedin.LinkedInLoginActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    public static LoginActivity instance;
    public static Bitmap bm;
    @InjectView(R.id.sign_up_button)
    Button sign_up_button;
    @InjectView(R.id.login_button)
    Button login_button;

    @InjectView(R.id.forget_pass)
    View forget_pass;

    @InjectView(R.id.login_fb)
    View login_fb;
    @InjectView(R.id.login_g)
    View login_g;
    @InjectView(R.id.login_ln)
    View login_ln;

    @InjectView(R.id.skip)
    View skip;
   public static EditText  email_edittext,password_edittext;

    private CallbackManager callbackManager;
    private LoginManager loginManager;


    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this, this);
        instance = this;
        StaticClass.overrideFonts(this,findViewById(android.R.id.content));
          email_edittext=( EditText )findViewById(R.id.email_edittext);
       password_edittext=( EditText )findViewById(R.id.password_edittext);





//        callbackManager = CallbackManager.Factory.create();
//        loginManager = LoginManager.getInstance();
//
//        loginManager.registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(final LoginResult loginResult) {
//                        //Toast.makeText(getApplicationContext(), loginResult.getAccessToken().getToken(), Toast.LENGTH_SHORT).show();
////                        Log.d("fb_token",loginResult.getAccessToken().getToken());
//                        final String token = loginResult.getAccessToken().getToken();
//                        Log.d("fb_token", token);
//                        doLoginFacebook(token);
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
//                        exception.printStackTrace();
//                        dismissProgressDialog();
//                    }
//                }
//        );



                final Collection<String> permissions = Arrays.asList(
                "public_profile"
                , "user_friends","email","user_birthday"

        );

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,TimeLineActivity.class));
                finish();
//                if(SignUpActivity.instance!=null){
//                    SignUpActivity.instance.finish();
//                }
            }
        });

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLogin()) {
                    doLogin();
                }
            }
        });

        email_edittext.addTextChangedListener(new TextWatcher() {

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
                    clearEditTextError(email_edittext);
                }
            }
        });

        password_edittext.addTextChangedListener(new TextWatcher() {

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
                    clearEditTextError(password_edittext);
                }
            }
        });

        login_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.logInWithReadPermissions(LoginActivity.this, permissions);
            }
        });

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        login_ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, LinkedInLoginActivity.class),1001);

//                LISessionManager.getInstance(getApplicationContext()).init(LoginActivity.this, buildScope(), new AuthListener() {
//                    @Override
//                    public void onAuthSuccess() {
//                        String token=LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue();
//                        Log.d("success", token + "");
//                        //Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
//                        new Project_Web_Functions().LogInByLinkedIn(token, new UniversalCallBack() {
//                            @Override
//                            public void onResponse(Object result) {
//
//                            }
//
//                            @Override
//                            public void onFailure(Object result) {
//
//                            }
//
//                            @Override
//                            public void onFinish() {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onAuthError(LIAuthError error) {
//                        Log.d("error", error.toString() + "");
//                        Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }, true);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken("357595176166-og7uan26mlj1q5fp6srops0q8bnsalft.apps.googleusercontent.com")
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();


        login_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode==RESULT_OK) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        if(requestCode==1001){//linkedin
            if(resultCode==RESULT_OK){
                String token=data.getStringExtra("token");
                long expires=data.getLongExtra("expires",0);
                doLoginLinkedIn(token,expires);
            }
        }

        if(requestCode==1234 && resultCode==RESULT_OK){
            getGoogleAccessToken();
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d("result","google");
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.d("id", result.getSignInAccount().getId() + "");
            Log.d("name", result.getSignInAccount().getDisplayName() + "");
            Log.d("email", result.getSignInAccount().getEmail() + "");
            Log.d("idtoken", result.getSignInAccount().getIdToken() + "");

            getGoogleAccessToken();

//            acct.getDisplayName();
        } else {
            // Signed out, show unauthenticated UI.
            Log.d("error","signout");
        }
    }


    void getGoogleAccessToken(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String accessToken = "";
                try {
                    //URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo");
                    // get Access Token with Scopes.PLUS_PROFILE
                    String sAccessToken;
                    sAccessToken = GoogleAuthUtil.getToken(
                            LoginActivity.this,
                            Plus.AccountApi.getAccountName(mGoogleApiClient) + "",
                            "oauth2:"
                                    + Scopes.PLUS_LOGIN + " "
                                    + Scopes.DRIVE_APPFOLDER + " "
                                    + "https://www.googleapis.com/auth/plus.login" + " "
                                    + "https://www.googleapis.com/auth/userinfo.profile"+" "
                                    + "https://www.googleapis.com/auth/plus.profile.emails.read");

                    Log.d("tokennnnnnn",sAccessToken+"");

                    return sAccessToken;

                } catch (UserRecoverableAuthException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Intent recover = e.getIntent();
                    startActivityForResult(recover, 1234);
                    return null;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String sAccessToken) {
                if(sAccessToken!=null) {
                    doLoginGoogle(sAccessToken);
                }

            }
        }.execute();
    }


    @Override
    protected void onStop() {
        dismissProgressDialog();
        super.onStop();
    }

    private boolean validateLogin() {
        boolean ret = true;

        if (email_edittext.getText().toString().trim().isEmpty()) {
//            email_edittext.setError("empty email!");
            setEditTextError(email_edittext);
            ret = false;
        }

        if (password_edittext.getText().toString().trim().isEmpty()) {
//            password_edittext.setError("empty password!");
            setEditTextError(password_edittext);
            ret = false;
        }

        return ret;
    }

    private void doLogin() {

        showProgressDialog();
        new Project_Web_Functions().LoginByEmail(
                email_edittext.getText().toString().trim(),
                password_edittext.getText().toString(),
                new UniversalCallBack() {
                    @Override
                    public void onResponse(Object result) {
                        ResponseObject responseObject = (ResponseObject) result;
                        if (responseObject.getStatus()) {
                            LogInUser user = new LogInUser();
                            user.setLogin_type(LogInUser.LOGIN_TYPE_EMAIL);
                            user.setEmail(email_edittext.getText().toString());
                            user.setPassword(password_edittext.getText().toString());

                            HashMap<String, String> items = (HashMap<String, String>) responseObject.getItems();
                            user.setAccess_token(items.get("access_token"));
                            user.setRefresh_token(items.get("refresh_token"));
                            saveLogInUser(user);

                            startActivity(new Intent(LoginActivity.this, TimeLineActivity.class));
                            finish();
                            if (SignUpActivity.instance != null) {
                                SignUpActivity.instance.finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "login false :(", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Object result) {
                        Toast.makeText(getApplicationContext(), "login failed :(", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });

    }

    private void doLoginFacebook(final String token) {
        showProgressDialog();
        new Project_Web_Functions().LogInByFaceBook(token, new UniversalCallBack() {
            @Override
            public void onResponse(Object result) {
                ResponseObject responseObject = (ResponseObject) result;
                if (responseObject.getStatus()) {
                    LogInUser user = new LogInUser();
                    UserObject o= new UserObject();
                    o.setEmail("wah_2008@hotmail.com");
                    o.setLname("hasna");
                    saveUserObject(o);

                    user.setLogin_type(LogInUser.LOGIN_TYPE_FACEBOOK);
//                                    user.setEmail(email_edittext.getText().toString());
//                                    user.setPassword(password_edittext.getText().toString());
                    user.setFb_token(token);
                    HashMap<String, String> items = (HashMap<String, String>) responseObject.getItems();
                    user.setAccess_token(items.get("access_token"));
                    user.setRefresh_token(items.get("refresh_token"));
                    saveLogInUser(user);


                    startActivity(new Intent(LoginActivity.this, TimeLineActivity.class));
                    finish();
                     if (SignUpActivity.instance != null) {
                        SignUpActivity.instance.finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "login false :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Object result) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });

    }

    private void doLoginGoogle(final String token) {
        showProgressDialog();
        new Project_Web_Functions().LogInByGoogle(token, new UniversalCallBack() {
            @Override
            public void onResponse(Object result) {
                ResponseObject responseObject = (ResponseObject) result;
                if (responseObject.getStatus()) {
                    LogInUser user = new LogInUser();
                    user.setLogin_type(LogInUser.LOGIN_TYPE_GOOGLE);
//                                    user.setEmail(email_edittext.getText().toString());
//                                    user.setPassword(password_edittext.getText().toString());
                    user.setG_token(token);
                    HashMap<String, String> items = (HashMap<String, String>) responseObject.getItems();
                    user.setAccess_token(items.get("access_token"));
                    user.setRefresh_token(items.get("refresh_token"));
                    saveLogInUser(user);

                    startActivity(new Intent(LoginActivity.this, TimeLineActivity.class));
                    finish();
                    if (SignUpActivity.instance != null) {
                        SignUpActivity.instance.finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "login failed :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Object result) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });

    }

    private void doLoginLinkedIn(final String token, long expires) {
        showProgressDialog();
        new Project_Web_Functions().LogInByLinkedIn(token, new UniversalCallBack() {
            @Override
            public void onResponse(Object result) {
                ResponseObject responseObject = (ResponseObject) result;
                if (responseObject.getStatus()) {
                    LogInUser user = new LogInUser();
                    user.setLogin_type(LogInUser.LOGIN_TYPE_LINKEDIN);
//                                    user.setEmail(email_edittext.getText().toString());
//                                    user.setPassword(password_edittext.getText().toString());
                    user.setLn_token(token);
                    HashMap<String, String> items = (HashMap<String, String>) responseObject.getItems();
                    user.setAccess_token(items.get("access_token"));
                    user.setRefresh_token(items.get("refresh_token"));
                    saveLogInUser(user);

                    startActivity(new Intent(LoginActivity.this, TimeLineActivity.class));
                    finish();
                    if (SignUpActivity.instance != null) {
                        SignUpActivity.instance.finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "login failed:(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Object result) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });

    }

    private void setEditTextError(EditText editText) {
        editText.setBackgroundResource(R.drawable.round_login_edittext_error);
    }

    private void clearEditTextError(EditText editText) {
        editText.setBackgroundResource(R.drawable.round_login_edittext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("g_error", connectionResult.getErrorMessage() + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        final String token = loginResult.getAccessToken().getToken();


                       /* Profile profile = Profile.getCurrentProfile();
                        profile.getName();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                         bm = getFacebookProfilePicture(loginResult.getAccessToken().getUserId());}}).start();

                        Log.d("fb_token", token);
                        Log.d("name", profile.getName());
                        Log.d("bm", String.valueOf(bm));
                        */
                        doLoginFacebook(token);
                    }


                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                        dismissProgressDialog();
                    }
                }
        );

    }
    public static Bitmap getFacebookProfilePicture(String userID){
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
