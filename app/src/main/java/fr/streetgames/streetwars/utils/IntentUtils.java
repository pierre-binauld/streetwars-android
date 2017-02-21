package fr.streetgames.streetwars.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import fr.streetgames.streetwars.app.activities.ContractEliminationActivity;

public class IntentUtils {

    private static String GEO_QUERY = "geo:0,0?q=";

    public static boolean startGeoActivity(Context context, String address) {
        Uri geo = Uri.parse(GEO_QUERY + address);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geo);
        return startActivity(context, intent);
    }

    public static boolean startImageActivity(Context context, String url) {
        Uri file = Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "image/*");
        return startActivity(context, intent);
    }

    private static boolean startActivity(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        }
        else {
            //TODO Snackbar: No app found to open address
            return false;
        }
    }

    public static void startContractEliminationActivity(Context context) {
        context.startActivity(
                new Intent(context, ContractEliminationActivity.class)
        );
    }
}
