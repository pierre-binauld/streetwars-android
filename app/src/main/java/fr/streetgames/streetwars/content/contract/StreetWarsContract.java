package fr.streetgames.streetwars.content.contract;

import android.net.Uri;

import fr.streetgames.streetwars.BuildConfig;
import fr.streetgames.streetwars.database.PlayerColumns;
import fr.streetgames.streetwars.database.RuleColumns;
import fr.streetgames.streetwars.database.TeamColumns;
import fr.streetgames.streetwars.database.RowTypeColumns;

public class StreetWarsContract {

    public static final String CONTENT_TYPE_APP_BASE = BuildConfig.APPLICATION_ID;

    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String AUTHORITY = BuildConfig.CONTENT_AUTHORITY;

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_PLAYER = "player";

    public static final String PATH_RULE = "rule";

    public static final String PATH_TEAM = "team";

    public static final String PATH_TEAM_MATE = "team";

    public static final String PATH_TARGET = "target";

    public static final String PATH_WATER_CODE = "water_code";

    public interface Player extends PlayerColumns, TeamColumns {

        String ID = PlayerColumns.ID;

        String TEAM_ID = TeamColumns.ID;

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_PLAYER)
                .build();

        String CONTENT_ITEM_TYPE = CONTENT_ITEM_TYPE_BASE + PATH_WATER_CODE;
    }

    public interface Rule extends RuleColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_RULE)
                .build();

        String CONTENT_TYPE = CONTENT_TYPE_BASE + PATH_RULE;
    }

    public interface Team extends TeamColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_TEAM)
                .build();

        String CONTENT_TYPE = CONTENT_TYPE_BASE + PATH_TEAM;
    }

    public interface Target extends PlayerColumns, RowTypeColumns {

        String PARAM_SHOW_TEAM = "show_team";

        String TEAM_NAME = TeamColumns.NAME;

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_TARGET)
                .build();

        String CONTENT_TYPE = CONTENT_TYPE_BASE + PATH_TARGET;

    }

}
