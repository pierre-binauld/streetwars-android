package fr.streetgames.streetwars.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import fr.streetgames.streetwars.BuildConfig;

public class StreetWarsDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "SWDatabaseOpenHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "streetwars.db";

    public StreetWarsDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Create table " + Tables.PLAYER);
        db.execSQL("CREATE TABLE " + Tables.PLAYER + " ("
                + PlayerColumns.ID + " INTEGER PRIMARY KEY,"
                + PlayerColumns.FIRST_NAME + " TEXT NOT NULL,"
                + PlayerColumns.LAST_NAME + " TEXT NOT NULL,"
                + PlayerColumns.ALIAS + " TEXT NOT NULL,"
                + PlayerColumns.PHOTO + " TEXT NOT NULL,"
                + PlayerColumns.WATER_CODE + " TEXT NOT NULL"
                + ")");

        Log.d(TAG, "onCreate: Create table " + Tables.RULE);
        db.execSQL("CREATE TABLE " + Tables.RULE + " ("
                + RuleColumns.ID + " INTEGER PRIMARY KEY,"
                + RuleColumns.GROUP + " TEXT NOT NULL,"
                + RuleColumns.RULE + " TEXT NOT NULL"
                + ")");

        if(BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: Insert rule #1");
            Log.d(TAG, "onCreate: Insert rule #2");
            Log.d(TAG, "onCreate: Insert rule #3");
            Log.d(TAG, "onCreate: Insert rule #4");
            db.execSQL("INSERT INTO " + Tables.RULE + " " +
                    "(" + RuleColumns.ID + ", " + RuleColumns.GROUP + ", " + RuleColumns.RULE + ") " +
                    "VALUES (?, ?, ?), (?, ?, ?), (?, ?, ?), (?, ?, ?)",
                    new Object[] {
                            0,
                            "Water Code",
                            "Le Watercode doit être saisi le plus tôt possible afin de ne pas déséquilibrer le jeu.",
                            1,
                            "Water Code",
                            "Lors d’un échange de Watercode, personne n’est en zone sûre.",
                            2,
                            "Water Code",
                            "Les éliminations sont irréversibles. En cas de litige, n’échangez pas de Watercode et utilisez le système de tickets.",
                            3,
                            "Water Code",
                            "Ne donner pas votre Watercode si quelqu’un vous arrose un samedi."
                    });
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
