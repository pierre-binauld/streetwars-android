package fr.streetgames.streetwars.content;

import fr.streetgames.streetwars.database.Tables;
import fr.streetgames.streetwars.database.TargetColumns;
import fr.streetgames.streetwars.database.TeamColumns;
import fr.streetgames.streetwars.database.TypeColumns;

public abstract class StreetWarsContentProviderHelper {

    public static String getTargetFromStatement() {
        return "(SELECT " + TypeColumns.TYPE_TARGET + " AS " + TypeColumns.TYPE + ", " +
                Tables.TEAM + ".*, " +
                TargetColumns.ID + ", " +
                TargetColumns.ALIAS + ", " +
                TargetColumns.FIRST_NAME + ", " +
                TargetColumns.LAST_NAME + ", " +
                TargetColumns.PHOTO + ", " +
                TargetColumns.KILL_COUNT + ", " +
                TargetColumns.TEAM_ID + ", " +
                TargetColumns.HOME + ", " +
                TargetColumns.WORK + ", " +
                TargetColumns.JOB_CATEGORY + ", " +
                TargetColumns.EXTRA +
                " FROM " + Tables.TEAM +
                " JOIN " + Tables.TARGET +
                " ON " + TeamColumns.ID + " = " + TargetColumns.TEAM_ID +

                " UNION " +

                "SELECT " + TypeColumns.TYPE_TEAM + " AS " + TypeColumns.TYPE + ", " +
                Tables.TEAM + ".*, " +
                "null AS " + TargetColumns.ID + ", " +
                "null AS " + TargetColumns.ALIAS + ", " +
                "null AS " + TargetColumns.FIRST_NAME + ", " +
                "null AS " + TargetColumns.LAST_NAME + ", " +
                "null AS " + TargetColumns.PHOTO + ", " +
                "null AS " + TargetColumns.KILL_COUNT + ", " +
                "null AS " + TargetColumns.TEAM_ID + ", " +
                "null AS " + TargetColumns.HOME + ", " +
                "null AS " + TargetColumns.WORK + ", " +
                "null AS " + TargetColumns.JOB_CATEGORY + ", " +
                "null AS " + TargetColumns.EXTRA +
                " FROM " + Tables.TEAM +
                " ORDER BY " + TeamColumns.ID + ", " + TypeColumns.TYPE +
                ")";
    }
}
