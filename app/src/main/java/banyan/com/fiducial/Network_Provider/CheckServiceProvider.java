package banyan.com.fiducial.Network_Provider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.sdsmdg.tastytoast.TastyToast;


public class CheckServiceProvider {
    public static boolean IsNetworkAvailable(Context context) {
        boolean networkAvailable = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (connectivityManager != null && networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                networkAvailable = true;
            } else {
                networkAvailable = false;
                TastyToast.makeText(context,"Network is Not Available",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return networkAvailable;
    }
}