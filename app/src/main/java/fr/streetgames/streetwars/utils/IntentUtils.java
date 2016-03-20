package fr.streetgames.streetwars.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentUtils {

    private static String GEO_QUERY = "geo:0,0?q=";

    public static boolean startGeoActivity(Context context, String address) {
        Uri geo = Uri.parse(GEO_QUERY + address);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geo);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        }
        else {
            return false;
        }
    }
}
