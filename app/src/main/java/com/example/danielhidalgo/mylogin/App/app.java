package com.example.danielhidalgo.mylogin.App;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by Daniel Hidalgo on 13/07/2017.
 */

public class app extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
