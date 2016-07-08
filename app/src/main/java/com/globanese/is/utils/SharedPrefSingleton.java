package com.globanese.is.utils;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hamdy on 23/12/2014.
 */



public class SharedPrefSingleton {

    private SharedPreferences prefs;
    private static SharedPrefSingleton instance;

    private SharedPrefSingleton(Context context){
        prefs=context.getSharedPreferences(SharedPrefSingleton.class.getName(), Activity.MODE_PRIVATE);
    }
    public static SharedPrefSingleton getInstance(){
        return instance;
    }

    public static void init(Context context){
        if(instance==null){
            instance=new SharedPrefSingleton(context);
        }
    }

    public SharedPreferences getPrefs(){
        return prefs;
    }


}
