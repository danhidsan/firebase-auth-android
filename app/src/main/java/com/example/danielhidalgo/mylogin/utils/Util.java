package com.example.danielhidalgo.mylogin.utils;

import android.content.SharedPreferences;

/**
 * Created by Daniel Hidalgo on 13/07/2017.
 */

public class Util {

    public static String getUserMailPrefs(SharedPreferences preferences){
        return preferences.getString("email","");
    }

    public static String getUserPassPrefs(SharedPreferences preferences){
        return preferences.getString("pass","");
    }

    public static void removeMailAndPass(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("email");
        editor.remove("pass");
        editor.apply();
    }
}
