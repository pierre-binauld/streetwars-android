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
                + PlayerColumns.ID + " INTEGER PRIMARY KEY, "
                + PlayerColumns.FIRST_NAME + " TEXT NOT NULL, "
                + PlayerColumns.LAST_NAME + " TEXT NOT NULL, "
                + PlayerColumns.ALIAS + " TEXT NOT NULL, "
                + PlayerColumns.PHOTO + " TEXT NOT NULL, "
                + PlayerColumns.WATER_CODE + " TEXT NOT NULL"
                + ")");

        Log.d(TAG, "onCreate: Create table " + Tables.RULE);
        db.execSQL("CREATE TABLE " + Tables.RULE + " ("
                + RuleColumns.ID + " INTEGER PRIMARY KEY, "
                + RuleColumns.GROUP + " TEXT NOT NULL, "
                + RuleColumns.RULE + " TEXT NOT NULL"
                + ")");

        Log.d(TAG, "onCreate: Create table " + Tables.TEAM);
        db.execSQL("CREATE TABLE " + Tables.TEAM + " ("
                + TeamColumns.ID + " INTEGER PRIMARY KEY, "
                + TeamColumns.NAME + " TEXT NOT NULL"
                + ")");

        Log.d(TAG, "onCreate: Create table " + Tables.TARGET);
        db.execSQL("CREATE TABLE " + Tables.TARGET + " ("
                + TargetColumns.ID + " INTEGER PRIMARY KEY, "
                + TargetColumns.ALIAS + " TEXT NOT NULL, "
                + TargetColumns.FIRST_NAME + " TEXT NOT NULL, "
                + TargetColumns.LAST_NAME + " TEXT NOT NULL, "
                + TargetColumns.PHOTO + " TEXT NOT NULL, "
                + TargetColumns.TEAM_ID + " INTEGER NOT NULL, "
                + TargetColumns.HOME + " TEXT NOT NULL, "
                + TargetColumns.WORK + " TEXT, "
                + TargetColumns.JOB_CATEGORY + " INTEGER NOT NULL, "
                + TargetColumns.EXTRA + " TEXT NOT NULL "
                + ")");

        if(BuildConfig.DEBUG) {
            switch (BuildConfig.GAME_MODE) {
                default:
                case BuildConfig.LOOP:
                    insertLoopTestData(db);
                    break;
                case BuildConfig.FFA:
                    insertFFATestData(db);
                    break;
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertLoopTestData(SQLiteDatabase db) {
        Log.d(TAG, "insertLoopTestData: Insert rules #1 ~ #4");
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

        Log.d(TAG, "insertLoopTestData: Insert player");
        db.execSQL("INSERT INTO " + Tables.PLAYER + " (" +
                        PlayerColumns.ID + ", " +
                        PlayerColumns.FIRST_NAME + ", " +
                        PlayerColumns.LAST_NAME + ", " +
                        PlayerColumns.ALIAS + ", " +
                        PlayerColumns.PHOTO + ", " +
                        PlayerColumns.WATER_CODE +
                        ") " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                new Object[] {
                        0,
                        "Pierre",
                        "Binauld",
                        "Zxcv",
                        "http://5.135.183.92:50220/res/davy_jones",
                        "NEIOZXCV"
                });

        Log.d(TAG, "insertLoopTestData: Insert team");
        db.execSQL("INSERT INTO " + Tables.TEAM + " (" +
                        TeamColumns.ID + ", " +
                        TeamColumns.NAME +
                        ") " +
                        "VALUES (?, ?)",
                new Object[] {
                        0,
                        "Black Pearl's Crew"
                });

        Log.d(TAG, "insertLoopTestData: Insert targets");
        db.execSQL("INSERT INTO " + Tables.TARGET + " (" +
                        TargetColumns.ID + ", " +
                        TargetColumns.ALIAS + ", " +
                        TargetColumns.FIRST_NAME + ", " +
                        TargetColumns.LAST_NAME + ", " +
                        TargetColumns.PHOTO + ", " +
                        TargetColumns.TEAM_ID + ", " +
                        TargetColumns.HOME + ", " +
                        TargetColumns.WORK + ", " +
                        TargetColumns.JOB_CATEGORY + ", " +
                        TargetColumns.EXTRA +
                        ") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[] {
                        0,
                        "Captain",
                        "Jack",
                        "Sparrow",
                        "http://5.135.183.92:50220/res/jack_sparrow",
                        0,
                        "34 rue Tupin, Lyon 2",
                        "50 quai Paul Sedaillan, Lyon 9",
                        0,
                        "To what point and purpose, young missy? The Black Pearl is gone and unless you have a rudder and a lot of sails hidden in that bodice - unlikely - young Mr. Turner will be dead long before you can reach him.",

                        1,
                        "Sailor",
                        "Will",
                        "Turner",
                        "http://5.135.183.92:50220/res/will_turner",
                        0,
                        "21 rue Voltaire, Lyon 3",
                        "",
                        2,
                        "That's the Flying Dutchman? It doesn't look like much.",

                        2,
                        "Lady",
                        "Elisabeth",
                        "Swann",
                        "http://5.135.183.92:50220/res/elizabeth_swann",
                        0,
                        "15 rue Sebastien Gryphe, Lyon 7",
                        "86 Rue Pasteur, 69007 Lyon",
                        1,
                        "I'm not entirely sure that I've had enough rum to allow that kind of talk."
                });
    }

    private void insertFFATestData(SQLiteDatabase db) {
        insertLoopTestData(db);

        Log.d(TAG, "insertFFATestData: Insert team");
        db.execSQL("INSERT INTO " + Tables.TEAM + " (" +
                        TeamColumns.ID + ", " +
                        TeamColumns.NAME +
                        ") " +
                        "VALUES (?, ?)",
                new Object[] {
                        1,
                        "Straw Hat Pirates"
                });

        Log.d(TAG, "insertFFATestData: Insert targets");
        db.execSQL("INSERT INTO " + Tables.TARGET + " (" +
                        TargetColumns.ID + ", " +
                        TargetColumns.ALIAS + ", " +
                        TargetColumns.FIRST_NAME + ", " +
                        TargetColumns.LAST_NAME + ", " +
                        TargetColumns.PHOTO + ", " +
                        TargetColumns.TEAM_ID + ", " +
                        TargetColumns.HOME + ", " +
                        TargetColumns.WORK + ", " +
                        TargetColumns.JOB_CATEGORY + ", " +
                        TargetColumns.EXTRA +
                        ") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[] {
                        3,
                        "Straw Hat Luffy",
                        "Monkey D.",
                        "Luffy",
                        "http://5.135.183.92:50220/res/luffy",
                        1,
                        "34 rue Tupin, Lyon 2",
                        "50 quai Paul Sedaillan, Lyon 9",
                        0,
                        "If you don't take risks, you can't create a future.",

                        4,
                        "Pirate Hunter Zoro",
                        "Roronoa",
                        "Zoro",
                        "http://5.135.183.92:50220/res/zoro",
                        1,
                        "21 rue Voltaire, Lyon 3",
                        "",
                        2,
                        "I don't care what the society says. I've regretted doing anything. I will survive and do what I want to.\n",

                        5,
                        "Black Leg Sanji",
                        "Sanji",
                        "",
                        "http://5.135.183.92:50220/res/sanji",
                        1,
                        "15 rue Sebastien Gryphe, Lyon 7",
                        "86 Rue Pasteur, 69007 Lyon",
                        1,
                        "I don't care if you're a god. If you lay even one finger on Nami-san, I'll become the Devil of the Blue Sea!"
                });
    }
}
