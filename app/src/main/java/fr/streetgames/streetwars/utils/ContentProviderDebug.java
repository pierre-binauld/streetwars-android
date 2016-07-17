package fr.streetgames.streetwars.utils;

import android.util.Log;

import fr.streetgames.streetwars.BuildConfig;

public class ContentProviderDebug {

    private static final String TAG = "ContentProviderDebug";

    public static void logSql(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(BuildConfig.DEBUG) {
            String s = "SELECT ";
            if (null != projection) {
                for (String p : projection) {
                    s += p + ", ";
                }
                s = s.substring(0, s.length() - 2);
            }
            Log.d(TAG, "query - projection: " + s);
            Log.d(TAG, "query - selection: WHERE " + selection);
            s = "";
            if (null != selectionArgs) {
                for (String sa : selectionArgs) {
                    s += sa + " ";
                }
            }
            Log.d(TAG, "query - selectionArgs: " + s);
            Log.d(TAG, "query - sortOrder: " + sortOrder);
        }
    }
}
