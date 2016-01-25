package fr.streetgames.streetwars.content.contract;

import android.net.Uri;

public class StreetWarsContract {

    public static final String CONTENT_TYPE_APP_BASE = "streetgames.streetwars.";

    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String AUTHORITY = "com.streetgames.streetwars.game";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
}
