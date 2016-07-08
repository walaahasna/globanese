package com.globanese.is.realm;

import android.util.Base64;

import com.globanese.is.app.ApplicationController;
import com.globanese.is.utils.SharedPrefSingleton;

import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Hamdy on 11/5/2016.
 */
public class RealmSingleton {
    private Realm realm;
    private RealmConfiguration realmConfig;
    private static RealmSingleton instance;

    private RealmSingleton(){
        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(ApplicationController.getInstance().getApplicationContext())
                .name("realm_db")
                .schemaVersion(1)
                .encryptionKey(getKey())
                .deleteRealmIfMigrationNeeded()
                .build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);
    }

    public static Realm getRealmInstance(){
        if(instance==null){
            instance=new RealmSingleton();
        }
        return Realm.getInstance(instance.realmConfig);
    }

    private byte[] getKey() {
        String st=SharedPrefSingleton.getInstance().getPrefs().getString("realm_key","");
        if(st.isEmpty()){
            byte[] key = new byte[64];
            new SecureRandom().nextBytes(key);
            String saveThis = Base64.encodeToString(key, Base64.DEFAULT);
            SharedPrefSingleton.getInstance().getPrefs().edit().putString("realm_key",saveThis).apply();
            return key;
        }
        return Base64.decode(st, Base64.DEFAULT);
    }

}
