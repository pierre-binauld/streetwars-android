package com.streetgames.streetwars.debug;

import android.os.StrictMode;
import android.support.design.BuildConfig;

public class StrictModeUtils {

    public static void enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .build());
        }
    }

}