package com.globanese.is.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.globanese.is.network.VolleySingleton;
import com.globanese.is.utils.SharedPrefSingleton;

/**
 * Created by hamdy on 6/6/15.
 */
public class ApplicationController extends MultiDexApplication {
    private static ApplicationController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SharedPrefSingleton.init(this);
        VolleySingleton.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }



}
