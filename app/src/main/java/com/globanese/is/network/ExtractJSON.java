package com.globanese.is.network;

import com.google.gson.Gson;
import com.globanese.is.model.CommunityObject;
import com.globanese.is.model.PostDetailObject;
import com.globanese.is.model.PostObject;
import com.globanese.is.model.UserObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ExtractJSON {



    public ResponseObject extractLoginResponse(String st){
        if(st==null || st.trim().isEmpty())
            return null;

        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONObject items=ob.getJSONObject("items");
                HashMap<String,String> itemsMap=new HashMap<>();
                itemsMap.put("access_token",items.getString("access_token"));
                itemsMap.put("token_type",items.getString("token_type"));
                itemsMap.put("expires_in",items.getString("expires_in"));
                itemsMap.put("refresh_token",items.getString("refresh_token"));
                responseObject.setItems(itemsMap);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }


    public ResponseObject extractSignUpResponse(String st){
        if(st==null || st.trim().isEmpty())
            return null;

        ResponseObject responseObject=new ResponseObject();

        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONObject items=ob.getJSONObject("items");
//                        "access_token": "i6MxzDe1PFFo8inCncTUdlBBqZs5c2rLi8jLFfJW",
//                        "token_type": "Bearer",
//                        "expires_in": 3600,
//                        "refresh_token": "bCc26a5vBTNHspFQzsP68vzhphqJhx4mEC7jy1f3"
                HashMap<String,String> itemsMap=new HashMap<>();
                itemsMap.put("access_token",items.getString("access_token"));
                itemsMap.put("token_type",items.getString("token_type"));
                itemsMap.put("expires_in",items.getString("expires_in"));
                itemsMap.put("refresh_token",items.getString("refresh_token"));

                responseObject.setItems(itemsMap);
            }else{
                ArrayList<ErrorMsg> errorMsgs=new ArrayList<>();

                JSONArray errors=ob.getJSONArray("errors");
                for (int i = 0; i < errors.length(); i++) {
                    JSONObject errorObject=errors.getJSONObject(i);
                    errorMsgs.add(new ErrorMsg(errorObject.optString("fieldname"),errorObject.optString("message")));
                }
                responseObject.setErrors(errorMsgs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject extractForgetPasswordResponse(String st){
        if(st==null || st.trim().isEmpty())
            return null;

        ResponseObject responseObject=new ResponseObject();

        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            try {
                if (responseObject.getStatus()) {
                    responseObject.setMessage(ob.optString("message"));
                } else {
                    ArrayList<ErrorMsg> errorMsgs = new ArrayList<>();
                    JSONArray errors = ob.getJSONArray("errors");

                    for (int i = 0; i < errors.length(); i++) {
                        JSONObject errorObject = errors.getJSONObject(i);
                        errorMsgs.add(new ErrorMsg(errorObject.optString("fieldname"), errorObject.optString("message")));
                    }
                    responseObject.setErrors(errorMsgs);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject extractUserInfo(String st){
        if(st==null || st.trim().isEmpty())
            return null;


        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONObject items=ob.getJSONObject("items");
                UserObject user=new Gson().fromJson(items.toString(),UserObject.class);
                responseObject.setItems(user);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject extractAccount(String st){
        if(st==null || st.trim().isEmpty())
            return null;

        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONArray items=ob.getJSONArray("items");
                ArrayList<UserObject> accountobject=new ArrayList<>();
                for (int i = 0; i < items.length(); i++) {
                    accountobject.add(new Gson().fromJson(items.get(i).toString(),UserObject.class));
                }

                responseObject.setItems(accountobject);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject extractCommunities(String st){
        if(st==null || st.trim().isEmpty())
            return null;

        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONArray items=ob.getJSONArray("items");
                ArrayList<CommunityObject> communityObjects=new ArrayList<>();
                for (int i = 0; i < items.length(); i++) {
                    communityObjects.add(new Gson().fromJson(items.get(i).toString(),CommunityObject.class));
                }
                responseObject.setItems(communityObjects);
            }

            else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }
////////////////////////////////////////////////////////////////////////////////post//////////////
    public ResponseObject extractGetPostinfo(String st){
        if(st==null || st.trim().isEmpty())
            return null;
        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONArray items=ob.getJSONArray("items");
                ArrayList<PostObject> postobject=new ArrayList<>();
                for (int i = 0; i < items.length(); i++) {
                    postobject.add(new Gson().fromJson(items.get(i).toString(),PostObject.class));
                }
                responseObject.setItems(postobject);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject extractGreatePost(String st){
        if(st==null || st.trim().isEmpty())
            return null;


        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONObject items=ob.getJSONObject("items");
               PostObject user=new Gson().fromJson(items.toString(),PostObject.class);
                responseObject.setItems(user);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject extractUpdatePost(String st){
        if(st==null || st.trim().isEmpty())
            return null;


        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONObject items=ob.getJSONObject("items");
                PostObject user=new Gson().fromJson(items.toString(),PostObject.class);
                responseObject.setItems(user);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }
    public ResponseObject extractDeletePost(String st){
        if(st==null || st.trim().isEmpty())
            return null;


        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONObject items=ob.getJSONObject("items");
                PostObject user=new Gson().fromJson(items.toString(),PostObject.class);
                responseObject.setItems(user);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }
///////////////////////////////////////////////////////////////////////////comment////////////////

    public ResponseObject extractGreateComment(String st){
        if(st==null || st.trim().isEmpty())
            return null;


        ResponseObject responseObject=new ResponseObject();
        try{

            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONObject items=ob.getJSONObject("items");
                PostDetailObject user=new Gson().fromJson(items.toString(),PostDetailObject.class);

                responseObject.setItems(user);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }


    public ResponseObject extractGetComment(String st){
        if(st==null || st.trim().isEmpty())
            return null;
        ResponseObject responseObject=new ResponseObject();
        try{

            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                JSONObject items=ob.getJSONObject("items");
                PostDetailObject user=new Gson().fromJson(items.toString(),PostDetailObject.class);

                responseObject.setItems(user);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject getimage(String st){
        if(st==null || st.trim().isEmpty())
            return null;

        ResponseObject responseObject=new ResponseObject();
        try{
            JSONObject ob=new JSONObject(st);
            responseObject.setStatus(ob.optBoolean("status"));
            if(responseObject.getStatus()){
                responseObject.setMessage(ob.optString("message"));
                String items=ob.getString("items");
               System.out.print(items);
                responseObject.setItems(items);
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

}
