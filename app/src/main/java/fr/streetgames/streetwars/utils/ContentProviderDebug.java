package fr.streetgames.streetwars.utils;

import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ContentProviderDebug {

    private static final String TAG = "ContentProviderDebug";

    public static void logSqlQuery(
            @NonNull Uri uri,
            @NonNull SQLiteQueryBuilder qb,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String groupBy,
            @Nullable String having,
            @Nullable String sortOrder,
            @Nullable String limit) {
        String sql = qb.buildQuery(projection, selection, groupBy, having, sortOrder, limit);
        if (selectionArgs != null) {
            for (String arg : selectionArgs) {
                sql = sql.replaceFirst("\\?", arg);
            }
        }
        Log.d(TAG, uri.toString());
        Log.d(TAG, sql);
    }
}
