package fr.streetgames.streetwars.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import fr.streetgames.streetwars.BuildConfig;
import fr.streetgames.streetwars.api.TeamType;

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
                + PlayerColumns.IS_ME + " INTEGER NOT NULL, "
                + PlayerColumns.PHOTO + " TEXT NOT NULL, "
                + PlayerColumns.WATER_CODE + " TEXT,"
                + PlayerColumns.KILL_COUNT + " INTEGER NOT NULL, "
                + PlayerColumns.TEAM_ID + " INTEGER NOT NULL, "
                + PlayerColumns.HOME + " TEXT, "
                + PlayerColumns.HOME_LATITUDE + " NUMERIC, "
                + PlayerColumns.HOME_LONGITUDE + " NUMERIC, "
                + PlayerColumns.WORK + " TEXT, "
                + PlayerColumns.WORK_LATITUDE + " NUMERIC, "
                + PlayerColumns.WORK_LONGITUDE + " NUMERIC, "
                + PlayerColumns.JOB_CATEGORY + " INTEGER, "
                + PlayerColumns.EXTRA + " TEXT "
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
                + TeamColumns.NAME + " TEXT NOT NULL, "
                + TeamColumns.TYPE + " INTEGER NOT NULL"
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
        //TODO
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
                        PlayerColumns.IS_ME + ", " +
                        PlayerColumns.PHOTO + ", " +
                        PlayerColumns.WATER_CODE + ", " +
                        PlayerColumns.KILL_COUNT + ", " +
                        PlayerColumns.TEAM_ID + ", " +
                        PlayerColumns.HOME + ", " +
                        PlayerColumns.HOME_LATITUDE + ", " +
                        PlayerColumns.HOME_LONGITUDE + ", " +
                        PlayerColumns.WORK + ", " +
                        PlayerColumns.WORK_LATITUDE + ", " +
                        PlayerColumns.WORK_LONGITUDE + ", " +
                        PlayerColumns.JOB_CATEGORY + ", " +
                        PlayerColumns.EXTRA +
                        ") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[] {
                        0,
                        "Tony",
                        "Stark",
                        "Iron Man",
                        1,
                        "http://5.135.183.92:50100/res/iron_man.jpg",
                        "NEIOZXCV",
                        4,
                        0,
                        null,
                        0,
                        0,
                        null,
                        0,
                        0,
                        0,
                        null,

                        1,
                        "Steve",
                        "Rogers",
                        "Captain America",
                        0,
                        "http://5.135.183.92:50100/res/captain_america.jpg",
                        null,
                        2,
                        0,
                        null,
                        0,
                        0,
                        null,
                        0,
                        0,
                        0,
                        null,

                        3,
                        "Jack",
                        "Sparrow",
                        "Captain",
                        0,
                        "http://5.135.183.92:50100/res/jack_sparrow.jpg",
                        null,
                        1,
                        1,
                        "34 rue Tupin, Lyon 2",
                        45.762836,
                        4.835285,
                        "50 quai Paul Sedaillan, Lyon 9",
                        45.788470,
                        4.814715,
                        0,
                        "To what point and purpose, young missy? The Black Pearl is gone and unless you have a rudder and a lot of sails hidden in that bodice - unlikely - young Mr. Turner will be dead long before you can reach him. To what point and purpose, young missy? The Black Pearl is gone and unless you have a rudder and a lot of sails hidden in that bodice - unlikely - young Mr. Turner will be dead long before you can reach him.",

                        4,
                        "Will",
                        "Turner",
                        "Sailor",
                        0,
                        "http://5.135.183.92:50100/res/will_turner.png",
                        null,
                        7,
                        1,
                        "21 rue Voltaire, Lyon 3",
                        45.759731,
                        4.848365,
                        "",
                        null,
                        null,
                        2,
                        "That's the Flying Dutchman? It doesn't look like much.",

                        5,
                        "Elisabeth",
                        "Swann",
                        "Lady",
                        0,
                        "http://5.135.183.92:50100/res/elizabeth_swann.jpg",
                        null,
                        0,
                        1,
                        "15 rue Sebastien Gryphe, Lyon 7",
                        45.752845,
                        4.844944,
                        "86 Rue Pasteur, 69007 Lyon",
                        45.749211,
                        4.836951,
                        1,
                        "I'm not entirely sure that I've had enough rum to allow that kind of talk."
                });

        Log.d(TAG, "insertLoopTestData: Insert team");
        db.execSQL("INSERT INTO " + Tables.TEAM + " (" +
                        TeamColumns.ID + ", " +
                        TeamColumns.NAME + ", " +
                        TeamColumns.TYPE +
                        ") " +
                        "VALUES (?, ?, ?), " +
                        "(?, ?, ?)",
                new Object[] {
                        0,
                        "Yeah Niggar",
                        TeamType.TEAM_MATE,
                        1,
                        "Black Pearl's Crew",
                        TeamType.TARGET
                });
    }

    private void insertFFATestData(SQLiteDatabase db) {
        insertLoopTestData(db);

        Log.d(TAG, "insertFFATestData: Insert team");
        db.execSQL("INSERT INTO " + Tables.TEAM + " (" +
                        TeamColumns.ID + ", " +
                        TeamColumns.NAME + ", " +
                        TeamColumns.TYPE +
                        ") " +
                        "VALUES (?, ?, ?)",
                new Object[] {
                        2,
                        "Straw Hat Pirates",
                        TeamType.TARGET
                });

        Log.d(TAG, "insertFFATestData: Insert targets");
        db.execSQL("INSERT INTO " + Tables.PLAYER + " (" +
                        PlayerColumns.ID + ", " +
                        PlayerColumns.FIRST_NAME + ", " +
                        PlayerColumns.LAST_NAME + ", " +
                        PlayerColumns.ALIAS + ", " +
                        PlayerColumns.IS_ME + ", " +
                        PlayerColumns.PHOTO + ", " +
                        PlayerColumns.WATER_CODE + ", " +
                        PlayerColumns.KILL_COUNT + ", " +
                        PlayerColumns.TEAM_ID + ", " +
                        PlayerColumns.HOME + ", " +
                        PlayerColumns.HOME_LATITUDE + ", " +
                        PlayerColumns.HOME_LONGITUDE + ", " +
                        PlayerColumns.WORK + ", " +
                        PlayerColumns.WORK_LATITUDE + ", " +
                        PlayerColumns.WORK_LONGITUDE + ", " +
                        PlayerColumns.JOB_CATEGORY + ", " +
                        PlayerColumns.EXTRA +
                        ") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)," +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[] {
                        6,
                        "Monkey D.",
                        "Luffy",
                        "Straw Hat Luffy",
                        0,
                        "http://5.135.183.92:50100/res/luffy.jpg",
                        null,
                        4,
                        2,
                        "32 rue de bourgogne, lyon 9",
                        45.777478,
                        4.803474,
                        "3 - 4 Place Charles Hernu, Villeurbanne, France",
                        45.770155,
                        4.863494,
                        0,
                        "If you don't take risks, you can't create a future.",

                        7,
                        "Roronoa",
                        "Zoro",
                        "Pirate Hunter Zoro",
                        0,
                        "http://5.135.183.92:50100/res/zoro.jpg",
                        null,
                        5,
                        2,
                        "34 rue Tupin, Lyon 2",
                        45.762836,
                        4.835285,
                        "",
                        null,
                        null,
                        2,
                        "I don't care what the society says. I've regretted doing anything. I will survive and do what I want to.\n",

                        8,
                        "Sanji",
                        "",
                        "Black Leg Sanji",
                        0,
                        "http://5.135.183.92:50100/res/sanji.jpg",
                        null,
                        2,
                        2,
                        "21 Route de Vienne, Lyon, France",
                        45.744202,
                        4.850469,
                        "148 Cours Emile Zola, Villeurbanne, France",
                        45.769116,
                        4.877422,
                        1,
                        "I don't care if you're a god. If you lay even one finger on Nami-san, I'll become the Devil of the Blue Sea!"
                });
    }
}
