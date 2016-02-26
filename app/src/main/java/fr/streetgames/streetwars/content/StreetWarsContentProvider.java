package fr.streetgames.streetwars.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import fr.streetgames.streetwars.R;

import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.database.StreetWarsDatabaseOpenHelper;
import fr.streetgames.streetwars.database.Tables;

public class StreetWarsContentProvider extends ContentProvider {

    private static final String TAG = "SWContentProvider";

    private StreetWarsDatabaseOpenHelper mOpenHelper;

    private UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        mOpenHelper = new StreetWarsDatabaseOpenHelper(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        setupUriMatcher();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: " + uri.toString());
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_player:
                qb.setTables(Tables.PLAYER);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown uri: %s", uri));
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor = qb.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                null
        );

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_player
                    :
                return StreetWarsContract.Player.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: ", match));
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_player:

            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: ", match));
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_player:

            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: ", match));
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_player:

            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: ", match));
        }
    }

    private void setupUriMatcher() {
        mUriMatcher.addURI(StreetWarsContract.AUTHORITY,
                StreetWarsContract.Player.CONTENT_URI.getPath(),
                R.id.content_uri_player
        );
    }
}
