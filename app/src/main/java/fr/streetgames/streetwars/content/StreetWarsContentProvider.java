package fr.streetgames.streetwars.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import fr.streetgames.streetwars.BuildConfig;
import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.database.StreetWarsDatabaseOpenHelper;
import fr.streetgames.streetwars.database.Tables;
import fr.streetgames.streetwars.utils.ContentProviderDebug;

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
                qb.setTables(StreetWarsContentProviderHelper.getPlayerStatement());
                break;
            case R.id.content_uri_rules:
                qb.setTables(Tables.RULE);
                break;
            case R.id.content_uri_targets:
                if (uri.getBooleanQueryParameter(StreetWarsContract.Target.PARAM_SHOW_TEAM, true)) {
                    qb.setTables(StreetWarsContentProviderHelper.getTeamAndTargetStatement());
                }
                else {
                    qb.setTables(StreetWarsContentProviderHelper.getTargetStatement());
                }
                break;
            case R.id.content_uri_addresses:
                qb.setTables(StreetWarsContentProviderHelper.getAddressStatement());
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown uri: %s", uri));
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        if (BuildConfig.DEBUG && BuildConfig.LOG_SQL) {
            ContentProviderDebug.logSqlQuery(uri, qb, projection, selection, selectionArgs, null, null,sortOrder, null);
        }

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

        assert null != getContext();
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_player:
                return StreetWarsContract.Player.CONTENT_ITEM_TYPE;
            case R.id.content_uri_rules:
                return StreetWarsContract.Rule.CONTENT_TYPE;
            case R.id.content_uri_targets:
                return StreetWarsContract.Target.CONTENT_TYPE;
            case R.id.content_uri_addresses:
                return StreetWarsContract.Address.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: ", match));
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = mUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String table;
        Uri baseUri;
        switch (match) {
            case R.id.content_uri_player:
                table = Tables.PLAYER;
                baseUri = StreetWarsContract.Player.CONTENT_URI;
                break;
            case R.id.content_uri_rules:
                table = Tables.RULE;
                baseUri = StreetWarsContract.Rule.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: ", match));
        }

        long rowID = db.insert(table, null, values);
        Uri newUri = ContentUris.withAppendedId(baseUri, rowID);
        // getContext() not null because called after onCreate()
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(newUri, null);
        Log.d(TAG, "insert: Notify " + newUri);
        // TODO check notify change.

        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String table;
        Uri baseUri;
        switch (match) {
            case R.id.content_uri_player:
                table = Tables.PLAYER;
                baseUri = StreetWarsContract.Player.CONTENT_URI;
                break;
            case R.id.content_uri_rules:
                table = Tables.RULE;
                baseUri = StreetWarsContract.Rule.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: ", match));
        }

        int n = db.delete(table, selection, selectionArgs);
        Log.d(TAG, "delete: Delete " + n + " rows in " + table);

        // getContext() not null because called after onCreate()
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(baseUri, null);
        Log.d(TAG, "delete: Notify " + baseUri);
        // TODO check notify change.

        return n;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String table;
        Uri baseUri;
        switch (match) {
            case R.id.content_uri_player:
                table = Tables.PLAYER;
                baseUri = StreetWarsContract.Player.CONTENT_URI;
                break;
            case R.id.content_uri_rules:
                table = Tables.RULE;
                baseUri = StreetWarsContract.Rule.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: ", match));
        }

        int n = db.update(table, values, selection, selectionArgs);
        Log.d(TAG, "update: Update " + n + " rows in " + table);

        // getContext() not null because called after onCreate()
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(baseUri, null);
        Log.d(TAG, "update: Notify " + baseUri);
        // TODO check notify change.

        return n;
    }

    private void setupUriMatcher() {
        addUriToUriMatcher(
                mUriMatcher,
                StreetWarsContract.AUTHORITY,
                StreetWarsContract.Player.CONTENT_URI.getPath(),
                R.id.content_uri_player
        );
        addUriToUriMatcher(
                mUriMatcher,
                StreetWarsContract.AUTHORITY,
                StreetWarsContract.Rule.CONTENT_URI.getPath(),
                R.id.content_uri_rules
        );
        addUriToUriMatcher(
                mUriMatcher,
                StreetWarsContract.AUTHORITY,
                StreetWarsContract.Target.CONTENT_URI.getPath(),
                R.id.content_uri_targets
        );
        addUriToUriMatcher(
                mUriMatcher,
                StreetWarsContract.AUTHORITY,
                StreetWarsContract.Address.CONTENT_URI.getPath(),
                R.id.content_uri_addresses
        );
    }

    private static void addUriToUriMatcher(
            @NonNull UriMatcher uriMatcher,
            @NonNull String authority,
            @NonNull String path,
            int code) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            path = path.substring(1);
        }
        uriMatcher.addURI(authority, path, code);
    }

}
