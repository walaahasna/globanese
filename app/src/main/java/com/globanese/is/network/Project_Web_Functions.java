package com.globanese.is.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;


public class Project_Web_Functions {
  public static String BASE_URL = "http://globanese.info/beta/api/v1";
    static String login_by_email = BASE_URL + "/oauth/access_token";
    static String signup_by_email = BASE_URL + "/user";
    static String forget_password = BASE_URL + "/sendEmailToReset";
    static String facebook_login = BASE_URL + "/facebookuser/";
    static String google_login = BASE_URL + "/googleuser/";
    static String linkedin_login = BASE_URL + "/linkedinuser/";
    static String get_own_user_info = BASE_URL + "/user?access_token=";
    static String get_communities = BASE_URL + "/communities?access_token=";
    static String update_user = BASE_URL+ "/user";
    static String greate_post = BASE_URL + "/post";
    static String get_post = BASE_URL + "/posts/[id]";



 /*static String BASE_URL="http://192.168.1.51/globanese-clone/api/v1";
    static String login_by_email = BASE_URL+"/oauth/access_token";
    static String signup_by_email = BASE_URL+"/user";
    static String forget_password = BASE_URL+"/sendEmailToReset";
    static String facebook_login = BASE_URL+"/facebookuser/";
    static String google_login = BASE_URL+"/googleuser/";
    static String linkedin_login = BASE_URL+"/linkedinuser/";
    static String get_own_user_info = BASE_URL+"/user?access_token=";
    static String get_communities = BASE_URL+"/communities?access_token=";
    static String update_user="http://192.168.1.51/globanese-clone/api/v1"+"/user";
    static String greate_post=BASE_URL+"/post";
    static String get_post=BASE_URL+"/posts/[id]";*/




    public void LoginByEmail(final String username, final String password, final UniversalCallBack callBack) {
        String url = login_by_email;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractLoginResponse(response));

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
//                error.networkResponse.statusCode
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("grant_type", "password");
                params.put("client_id", "F3d259ddd3ed8ff3843839b");
                params.put("client_secret", "4c7f6f8fa93d59c45502c0ae8c4a95b");
                return params;
            }


        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void SignUpByEmail(final String f_name, final String l_name, final String email, final String password, final UniversalCallBack callBack) {
        String url = signup_by_email;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractSignUpResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFinish();
                callBack.onFailure(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fname", f_name);
                params.put("lname", l_name);
                params.put("email", email);
                params.put("password", password);
                params.put("grant_type", "password");
                params.put("client_id", "F3d259ddd3ed8ff3843839b");
                params.put("client_secret", "4c7f6f8fa93d59c45502c0ae8c4a95b");

              return  params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void ForgetPassword(final String email, final UniversalCallBack callBack) {
        String url = forget_password;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractForgetPasswordResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
//                error.networkResponse.statusCode
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
//                params.put("client_id","F3d259ddd3ed8ff3843839b");
//                params.put("client_secret","4c7f6f8fa93d59c45502c0ae8c4a95b");
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void LogInByFaceBook(final String token, final UniversalCallBack callBack) {
        String url = facebook_login + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractLoginResponse(response));
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("grant_type", "password");
                params.put("client_id", "F3d259ddd3ed8ff3843839b");
                params.put("client_secret","4c7f6f8fa93d59c45502c0ae8c4a95b");
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void LogInByGoogle(final String token, final UniversalCallBack callBack) {
        String url = google_login + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractLoginResponse(response));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("grant_type", "password");
                params.put("client_id", "F3d259ddd3ed8ff3843839b");
                params.put("client_secret", "4c7f6f8fa93d59c45502c0ae8c4a95b");
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void LogInByLinkedIn(final String token, final UniversalCallBack callBack) {
        String url = linkedin_login + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractLoginResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("grant_type", "password");
                params.put("client_id", "F3d259ddd3ed8ff3843839b");
                params.put("client_secret", "4c7f6f8fa93d59c45502c0ae8c4a95b");
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }



    public void GetUserInfo(final String token, final UniversalCallBack callBack) {
        String url = get_own_user_info + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractUserInfo(response));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params=new HashMap<>();
//
//                params.put("grant_type","password");
//                params.put("client_id","F3d259ddd3ed8ff3843839b");
//                params.put("client_secret","4c7f6f8fa93d59c45502c0ae8c4a95b");
//                return params;
//            }
//        }
                ;

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }


    public void UpdateUserInfo(final String token, final Map<String, String> map, final UniversalCallBack callBack) {
        String url = update_user;

        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractUserInfo(response));
            }


                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", token);
                params.putAll(map);
//
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-HTTP-Method-Override","PUT");

                return headers;
            }

        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void GetCommunities(final String token, final UniversalCallBack callBack) {
        String url = get_communities + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();

                callBack.onResponse(new ExtractJSON().extractCommunities(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        });

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }




    public void GetPost(final String token, final UniversalCallBack callBack) {
        String url = get_post + token;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractGreatePost(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        })
//
                ;

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void UpdatePost(final String token, final Map<String, String> map, final UniversalCallBack callBack) {
        String url = get_post;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().extractGetPostinfo(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", token);
                params.putAll(map);
//                for(String key:map.keySet()){
//                    params.put(key,map.get(key));
//                }
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void GreatePost(final String text, final String viedo, final String location, final int privacy, final String photo, final int user_id, final String token, final UniversalCallBack universalCallBack) {

        String url = greate_post;
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                universalCallBack.onFinish();
                universalCallBack.onResponse(new ExtractJSON(). extractGreatePost(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                universalCallBack.onFinish();
                universalCallBack.onFailure(null);
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("text",text);
                params.put("video",viedo);
                params.put(" location",location);
                params.put(" privacy", String.valueOf(privacy));
                params.put(" photo",photo);

                params.put(" access_token",token);
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }


    public void Getphoto(final String token,final String image, final UniversalCallBack callBack) {
        String url = "http://globanese.info/globanese/api/v1/image";
        Log.d(Project_Web_Functions.class.getName(), url);
        VolleyStringRequest stringRequest = new VolleyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.d(Project_Web_Functions.class.getName(), response);
                callBack.onFinish();
                callBack.onResponse(new ExtractJSON().getimage(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFinish();
                callBack.onFailure(null);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("photo",image);
                params.put("access_token",token);


                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }
}
