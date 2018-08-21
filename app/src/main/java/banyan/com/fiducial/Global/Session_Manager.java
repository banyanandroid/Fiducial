package banyan.com.fiducial.Global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import banyan.com.fiducial.Activity.Activity_Login;

/**
 * Created by Android on 1/24/2018.
 */

public class Session_Manager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "SimtaERP";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USER_ID = "str_user_id";
    public static final String KEY_USER_NAME = "str_user_name";
    public static final String KEY_USER_TYPE = "str_user_type";
    public static final String KEY_USER_ROLE ="str_admin";
    public static final String KEY_USER_DEPTID = "str_deptId";

    public static final String KEY_USER_PHOTO = "str_user_photo";
    public static final String KEY_EMP_ID = "str_emp_id";
    public static final String KEY_CUSTOMER_ID ="str_customer_id";
    public static final String KEY_USER_STATE ="str_user_state";

    public static final String KEY_USER = "name";

    // Constructor
    public Session_Manager(Context context) {

        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */

    public void createLoginSession( String str_user_id, String str_user_name, String str_user_type,String str_admin,String str_deptId) {

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID,str_user_id);
        editor.putString(KEY_USER_NAME,str_user_name);
        editor.putString(KEY_USER_TYPE,str_user_type);
        editor.putString(KEY_USER_ROLE,str_admin);
        editor.putString(KEY_USER_DEPTID,str_deptId);

        // commit changes
        editor.commit();

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */

    public void checkLogin() {

        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Activity_Login.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_TYPE, pref.getString(KEY_USER_TYPE, null));
        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Activity_Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
// Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void updateCustomerId(String customerId){

        editor.putString(KEY_CUSTOMER_ID,customerId);
        // commit changes
        editor.commit();

    }
}


