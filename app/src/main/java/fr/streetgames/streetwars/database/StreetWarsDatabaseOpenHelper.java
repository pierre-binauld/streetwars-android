package fr.streetgames.streetwars.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import fr.streetgames.streetwars.BuildConfig;
import fr.streetgames.streetwars.api.StreetWarsJobCategory;
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
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " + // Belle
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " + // Bulle
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " + // Rebelle
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[] {
                        0,
                        "Brayane",
                        "Chermitti",
                        "Belle",
                        0,
                        "http://www.streetwarslyon.fr/site/photo/gd/1lYhQ5aMO.jpg", //http://5.135.183.92:50100/res/iron_man.jpg",
                        "NEIOZXCV",
                        3,
                        0,
                        null,
                        0,
                        0,
                        null,
                        0,
                        0,
                        StreetWarsJobCategory.WORKER,
                        null,

                        1,
                        "Pierre",
                        "Binauld",
                        "Bulle",
                        1,
                        "http://www.streetwarslyon.fr/site/photo/gd/HA3kaw3Uec.jpg", //http://5.135.183.92:50100/res/captain_america.jpg",
                        "xdiqtvtc",
                        1,
                        0,
                        null,
                        0,
                        0,
                        null,
                        0,
                        0,
                        StreetWarsJobCategory.WORKER,
                        null,

                        2,
                        "Agathe",
                        "Pillot",
                        "Rebelle",
                        0,
                        "http://www.streetwarslyon.fr/site/photo/gd/1VrzcUpwo.jpg", //"http://www.streetwarslyon.fr/site/photo/gd/HA3kaw3Uec.jpg", //http://5.135.183.92:50100/res/captain_america.jpg",
                        "????????",
                        0,
                        0,
                        null,
                        0,
                        0,
                        null,
                        0,
                        0,
                        StreetWarsJobCategory.NO_jOB,
                        null,

                        3, // player id
                        "Pascal",
                        "CHEVAILLIER WULLSCHLEGER",
                        "Calou",
                        0, // is_me
                        "http://www.streetwarslyon.fr/site/photo/gd/9YjJaqyhbI.jpg", //"http://5.135.183.92:50100/res/jack_sparrow.jpg",
                        null, // Water Code
                        0, // kill count
                        1, // Team Id
                        "5 Avenue Du Commandant L'herminier, 69100 Villeurbanne", // Home
                        45.770315, // Home latitude
                        4.885160, // Home longitude
                        "27 Boulevard Du 11 Novembre, 69100 Villeurbanne", // Work/School
                        45.780723, // Work/School latitude
                        4.863510, // Work/School longitude
                        StreetWarsJobCategory.STUDENT, // job cat
                        "Filière :\tstaps \n" +
                                "Année d'étude :\tl1 \n" +
                                "Batiments :\tsapin \n" +
                                "Jours de présence :\tMardi, Mercredi, Jeudi, Vendredi \n" +
                                "\n" +
                                "\n" +
                                "Ce joueur a des vacances durant la période de jeu\n" +
                                "Dates de vacances :\t22/09/16 au 27/09/16 \n" +
                                "Noms de ses magasins favoris:\tCarrefour gratte ciel \n" +
                                "Lieux de sorties fréquents/prévus :\tvieux lyon, st jean, gerland ninkasi \n" +
                                "\n" +
                                "\n" +
                                "Trajets réguliers :\tvélo personel tram et métro \n",

                        4,
                        "Maxence",
                        "DEFOUR",
                        "Max le bg",
                        0,
                        "http://www.streetwarslyon.fr/site/photo/gd/123cQZuvw.jpg",//"http://5.135.183.92:50100/res/will_turner.png",
                        null,
                        1,
                        1,
                        "5 Avenue Du Commandant L'herminier, 69100 Villeurbanne", // Home
                        45.770315, // Home latitude
                        4.885160, // Home longitude
                        "47 Rue Du Sergent Michel Berthet, 69009 Lyon",
                        45.768472,
                        4.806027,
                        StreetWarsJobCategory.STUDENT,
                        "Filière :\tBts Gestion et Protection de la Nature \n" +
                                "Année d'étude :\t1ère année de BTS \n" +
                                "Batiments :\tCampus René Cassin \n" +
                                "Jours de présence :\tLundi, Mardi, Mercredi, Jeudi, Vendredi \n" +
                                "\n" +
                                "\n" +
                                "Trajets réguliers :\tTCL : Métro A de Gratte Ciel a Bellcour + Métro D de Bellcour a Gorge de Loup \n" +
                                "\n" +
                                "Notes :\tEventuels retour a Vienne 38200 un dimanche sur 2 \n" +
                                "\n" +
                                "Courage et RDV en final mes poulets ;) ",

                        5,
                        "Antoine",
                        "GUILLOIS",
                        "Yermath",
                        0,
                        "http://www.streetwarslyon.fr/site/photo/gd/1PoIa04cA.jpg",//"http://5.135.183.92:50100/res/elizabeth_swann.jpg",
                        null,
                        0,
                        1,
                        "139 Avenue Felix Faure, 69003 Lyon",
                        45.754407,
                        4.864159,
                        "47 Rue Du Sergent Michel Berthet, 69009 Lyon",
                        45.768472,
                        4.806027,
                        StreetWarsJobCategory.STUDENT,
                        "Filière :\tBTS gestion et protection de la nature \n" +
                                "Année d'étude :\t1er \n" +
                                "Batiments :\t1er \n" +
                                "Jours de présence :\tLundi, Mardi, Mercredi, Jeudi, Vendredi \n" +
                                "\n" +
                                "\n" +
                                "Trajets réguliers :\tDe chez moi au métro Garibaldi je le fait en skate et après je m'arrete à gorge de loup et je vais à mon école après "
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
                        "Les Supers Nanas",
                        TeamType.TEAM_MATE,
                        1,
                        "les canards enflamés",
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
                        4.8634936,
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
