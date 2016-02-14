package fr.streetgames.streetwars.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StreetWarsDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "streetwars.db";

    public StreetWarsDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.PLAYER + " ("
                + PlayerColumns.ID + " INTEGER PRIMARY KEY,"
                + PlayerColumns.FIRST_NAME + " TEXT NOT NULL,"
                + PlayerColumns.LAST_NAME + " TEXT NOT NULL,"
                + PlayerColumns.ALIAS + " TEXT NOT NULL,"
                + PlayerColumns.PHOTO + " TEXT NOT NULL,"
                + PlayerColumns.WATER_CODE + " TEXT NOT NULL"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
