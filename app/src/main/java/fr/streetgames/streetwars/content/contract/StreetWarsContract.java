package fr.streetgames.streetwars.content.contract;

import android.net.Uri;

import fr.streetgames.streetwars.database.PlayerColumns;
import fr.streetgames.streetwars.database.RuleColumns;

public class StreetWarsContract {

    public static final String CONTENT_TYPE_APP_BASE = "fr.streetgames.streetwars.";

    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String AUTHORITY = "fr.streetgames.streetwars.game";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_PLAYER = "player";

    public static final String PATH_RULE = "rule";

    public static final String PATH_WATER_CODE = "water_code";

    public interface Player extends PlayerColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_PLAYER)
                .build();

        String CONTENT_ITEM_TYPE = CONTENT_ITEM_TYPE_BASE + PATH_WATER_CODE;
    }

    public interface Rule extends RuleColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_RULE)
                .build();

        String CONTENT_ITEM_TYPE = CONTENT_TYPE_BASE + PATH_RULE;
    }
}
