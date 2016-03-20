package fr.streetgames.streetwars.content;

import fr.streetgames.streetwars.database.Tables;
import fr.streetgames.streetwars.database.TargetColumns;
import fr.streetgames.streetwars.database.TeamColumns;

public abstract class StreetWarsContentProviderHelper {

    public static final String TYPE = "Type";

    public static final int TYPE_TEAM = 0;

    public static final int TYPE_TARGET = 1;

    public static String getTargetFromStatement() {
        return "(SELECT " + TYPE_TARGET + " AS " + TYPE + ", " +
                Tables.TEAM + ".*, " +
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
                " FROM " + Tables.TEAM +
                " JOIN " + Tables.TARGET +
                " ON " + TeamColumns.ID + " = " + TargetColumns.TEAM_ID +

                " UNION " +

                "SELECT " + TYPE_TEAM + " AS " + TYPE + ", " +
                Tables.TEAM + ".*, " +
                "null AS " + TargetColumns.ID + ", " +
                "null AS " + TargetColumns.ALIAS + ", " +
                "null AS " + TargetColumns.FIRST_NAME + ", " +
                "null AS " + TargetColumns.LAST_NAME + ", " +
                "null AS " + TargetColumns.PHOTO + ", " +
                "null AS " + TargetColumns.TEAM_ID + ", " +
                "null AS " + TargetColumns.HOME + ", " +
                "null AS " + TargetColumns.WORK + ", " +
                "null AS " + TargetColumns.JOB_CATEGORY + ", " +
                "null AS " + TargetColumns.EXTRA +
                " FROM " + Tables.TEAM +
                " ORDER BY " + TeamColumns.ID + ", " + TYPE +
                ")";
    }
}
