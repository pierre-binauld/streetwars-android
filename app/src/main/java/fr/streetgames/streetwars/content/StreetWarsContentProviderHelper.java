package fr.streetgames.streetwars.content;

import fr.streetgames.streetwars.api.TeamType;
import fr.streetgames.streetwars.content.contract.AddressColumns;
import fr.streetgames.streetwars.database.PlayerColumns;
import fr.streetgames.streetwars.database.Tables;
import fr.streetgames.streetwars.database.TeamColumns;
import fr.streetgames.streetwars.content.contract.RowTypeColumns;

public abstract class StreetWarsContentProviderHelper {

    public static String getPlayerStatement() {
        return Tables.PLAYER
                + " JOIN " + Tables.TEAM
                + " ON " + TeamColumns.ID + " = " + PlayerColumns.TEAM_ID;
    }

    public static String getTeamAndTargetStatement() {
        return "(SELECT " + RowTypeColumns.TYPE_TARGET + " AS " + RowTypeColumns.ROW_TYPE + ", " +
                Tables.TEAM + ".*, " +
                PlayerColumns.ID + ", " +
                PlayerColumns.ALIAS + ", " +
                PlayerColumns.FIRST_NAME + ", " +
                PlayerColumns.LAST_NAME + ", " +
                PlayerColumns.PHOTO + ", " +
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
                " FROM " + Tables.TEAM +
                " JOIN " + Tables.PLAYER +
                " ON " + TeamColumns.ID + " = " + PlayerColumns.TEAM_ID +
                " WHERE " + TeamColumns.TYPE + " = " + TeamType.TARGET +

                " UNION " +

                "SELECT " + RowTypeColumns.TYPE_TEAM + " AS " + RowTypeColumns.ROW_TYPE + ", " +
                Tables.TEAM + ".*, " +
                "null AS " + PlayerColumns.ID + ", " +
                "null AS " + PlayerColumns.ALIAS + ", " +
                "null AS " + PlayerColumns.FIRST_NAME + ", " +
                "null AS " + PlayerColumns.LAST_NAME + ", " +
                "null AS " + PlayerColumns.PHOTO + ", " +
                "null AS " + PlayerColumns.KILL_COUNT + ", " +
                "null AS " + PlayerColumns.TEAM_ID + ", " +
                "null AS " + PlayerColumns.HOME + ", " +
                "null AS " + PlayerColumns.HOME_LATITUDE + ", " +
                "null AS " + PlayerColumns.HOME_LONGITUDE + ", " +
                "null AS " + PlayerColumns.WORK + ", " +
                "null AS " + PlayerColumns.WORK_LATITUDE + ", " +
                "null AS " + PlayerColumns.WORK_LONGITUDE + ", " +
                "null AS " + PlayerColumns.JOB_CATEGORY + ", " +
                "null AS " + PlayerColumns.EXTRA +
                " FROM " + Tables.TEAM +
                " WHERE " + TeamColumns.TYPE + " = " + TeamType.TARGET +
                " ORDER BY " + TeamColumns.ID + ", " + RowTypeColumns.ROW_TYPE +
                ")";
    }

    public static String getTargetStatement() {
        return "(SELECT " +
                Tables.TEAM + ".*, " +
                PlayerColumns.ID + ", " +
                PlayerColumns.ALIAS + ", " +
                PlayerColumns.FIRST_NAME + ", " +
                PlayerColumns.LAST_NAME + ", " +
                PlayerColumns.PHOTO + ", " +
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
                " FROM " + Tables.TEAM +
                " JOIN " + Tables.PLAYER +
                " ON " + TeamColumns.ID + " = " + PlayerColumns.TEAM_ID +
                ")";
    }

    public static String getAddressStatement() {
        return "(SELECT * " +
                " FROM (SELECT " +
                PlayerColumns.HOME + " AS " + AddressColumns.ADDRESS + ", " +
                PlayerColumns.HOME_LONGITUDE + " AS " + AddressColumns.ADDRESS_LONGITUDE + ", " +
                PlayerColumns.HOME_LATITUDE + " AS " + AddressColumns.ADDRESS_LATITUDE +
                " FROM " + Tables.PLAYER +

                " UNION " +

                "SELECT " +
                PlayerColumns.WORK + " AS " + AddressColumns.ADDRESS + ", " +
                PlayerColumns.WORK_LONGITUDE + " AS " + AddressColumns.ADDRESS_LONGITUDE + ", " +
                PlayerColumns.HOME_LATITUDE + " AS " + AddressColumns.ADDRESS_LATITUDE +
                " FROM " + Tables.PLAYER +
                ")" +
                " GROUP BY " + AddressColumns.ADDRESS_LONGITUDE + ", " + AddressColumns.ADDRESS_LATITUDE +
                ")";

    }
}
