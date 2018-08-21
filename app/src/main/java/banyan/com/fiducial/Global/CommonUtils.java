package banyan.com.fiducial.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CommonUtils {

    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public CommonUtils(Context context){
        this.context =context;
        sharedPreferences = getCommonSharePreference();
        editor = sharedPreferences.edit();
    }

    public SharedPreferences getCommonSharePreference(){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setBoolean(String key, Boolean value){

        editor.putBoolean(key, value);
    }

}

