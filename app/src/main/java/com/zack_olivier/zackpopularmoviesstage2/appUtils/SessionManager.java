package com.zack_olivier.zackpopularmoviesstage2.appUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pc on 08/09/2017.
 */

public class SessionManager {

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final static String PREFS_NAME = "popular_app_prefs";
    private final static int PRIVATE_MODE = 0;
    private final static String  IS_LOGGED = "islogged";
    private final static String  ID = "id";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private Context context;


    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME,PRIVATE_MODE);
        editor = prefs.edit();

    }
    public boolean isLogged(){
        return prefs.getBoolean(IS_LOGGED,false);
    }

    public boolean logged(){
        return prefs.getBoolean(IS_LOGGED,true);
    }

    public void setFirstTimeLaunch() {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getIPreferenceId(){
        return prefs.getString(ID, null);
    }
    public void addToFavoris(String id){
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(ID, id);

        editor.commit();
    }

    public void removeToFavoris(){
        editor.clear().commit();
    }

}
