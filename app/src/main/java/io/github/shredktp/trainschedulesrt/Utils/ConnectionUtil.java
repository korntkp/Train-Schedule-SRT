package io.github.shredktp.trainschedulesrt.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Korshreddern on 24-Feb-17.
 */

public class ConnectionUtil {
    public static boolean isConnected(Context context) {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            result = true;
        }
        return result;
    }
}
