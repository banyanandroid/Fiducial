package banyan.com.fiducial.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import banyan.com.fiducial.Global.App_Config;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.Global.Toast_Show;
import banyan.com.fiducial.Network_Provider.CheckServiceProvider;
import banyan.com.fiducial.R;
import dmax.dialog.SpotsDialog;
import pugman.com.simplelocationgetter.SimpleLocationGetter;

public class Activity_Login extends Activity implements SimpleLocationGetter.OnLocationGetListener{
    // password visible and invisible
    private int passwordIsNotVisible = 1;
    private static final String TAG = Activity_Login.class.getSimpleName();
    // session manager
    String str_user_id, str_user_name;

    Session_Manager session;
    // location tracker
    private LocationRequest mLocationRequest;
    private PendingResult<LocationSettingsResult> result;
    GoogleApiClient client;

    EditText edt_current_password, edt_new_password;
    String str_login_email, str_login_password, str_current_password, str_new_password;

    MaterialEditText edt_email, edt_password;

    Button btn_login;

    TextView txt_forgot;

    ImageView password_view;
    // location getIt
    private double latitude, longitude;
    private String str_lat, str_long;
    // USER DETAILS
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_USER_NAME = "username";
    private static final String TAG_USER_ROLE = "user_role";
    private static final String TAG_USER_TYPE = "user_type";
    private static final String TAG_USER_DEPTID = "dept_id";

    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();

    // 6.0 Location & Call
    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;
    static final Integer GPS_SETTINGS = 0x7;
    // alert dialog
    SpotsDialog dialog, dialog_popup;
    public static RequestQueue queue;
    // email validate
    private final Pattern emailAddressPattern = Pattern.compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private static final String TAG_LOGIN = "Login";
    private static final String TAG_SENT_PASSWORD = "Sent Password";
    private static long back_pressed;

    private EditText edt_forget_email;

    private String str_forget_email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new Session_Manager(getApplicationContext());

        edt_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_password = (MaterialEditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        txt_forgot = (TextView) findViewById(R.id.txt_forgot);

        //ON FORGET PASSWORD SHOW DIALOG
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(Activity_Login.this);
                View view = li.inflate(R.layout.activity_forget_password, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_Login.this);
                alertDialogBuilder.setView(view);

                edt_forget_email = (EditText) view.findViewById(R.id.edt_forget_email);
                edt_forget_email.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

                // SET DIALOG MESSAGE
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("SUBMIT",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        // get user input and set it to result
                                        str_forget_email = edt_forget_email.getText().toString();

                                        if (str_forget_email == "") {
                                            Toast_Show.displaySuccessToast(getApplicationContext(),"ENTER EMAIL ID");
                                            //TastyToast.makeText(getApplicationContext(), "ENTER EMAIL ID", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                                            edt_current_password.setError("EMAIL ID CANNOT BE EMPTY");

                                        } else {

                                            try {

                                                dialog_popup = new SpotsDialog(Activity_Login.this);
//                                                dialog_popup.show();
                                                queue = Volley.newRequestQueue(Activity_Login.this);
                                                Forgot_Password();

                                            } catch (Exception e) {

                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setTitle("FORGET PASSWORD");
                alertDialog.show();

            }
        });

        try {

            NewLocation();

        }catch (Exception e) {

        }

        // ON LOGIN
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_login_email = edt_email.getText().toString();
                str_login_password = edt_password.getText().toString();
                if (CheckServiceProvider.IsNetworkAvailable(getApplicationContext()))

                    if (str_login_email.isEmpty() || str_login_email.equals(null)) {
                        edt_email.setError("ENTER EMAIL ID");
                    } else if (!(isValidEmail(str_login_email))) {
                        edt_email.setError("INVALID EMAILID");

                    } else if (str_login_password.equals("")) {

                        edt_password.setError("PASSWORD CANNOT BE EMPTY");

                    } else {

                        try {

                            dialog = new SpotsDialog(Activity_Login.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Login.this);
                            User_Login();

                        } catch (Exception e) {

                        }
                    }
            }
        });
        // Password show
        password_view = (ImageView) findViewById(R.id.password_view);
        password_view.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {
                if (passwordIsNotVisible == 1) {
                    edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password_view.setImageResource(R.drawable.open_eyes);
                    passwordIsNotVisible = 0;
                } else {
                    edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_view.setImageResource(R.drawable.close_eyes);
                    passwordIsNotVisible = 1;
                }
                edt_password.setSelection(edt_password.length());
            }
        });

        // Location Service
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            System.out.println("Called");
            turnGPSOn();
            System.out.println("Done");
        } catch (Exception e) {

        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {
            showGPSDisabledAlertToUser();
        }

    }


    /*******************************
     * Enable GPS
     ******************************/

    private void turnGPSOn() {

        System.out.println("Inside GPS");
        System.out.println("Inside GPS 0 ");

        String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.getApplicationContext().sendBroadcast(poke);

            System.out.println("Inside GPS 1");
        }
    }

    /********************************
     * Check GPS Connection is Enabled
     *********************************/

    private void showGPSDisabledAlertToUser() {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS IS DISABLED IN YOUR DEVICE. WOULD YOU LIKE TO ENABLE IT?")
                .setCancelable(false)
                .setPositiveButton("GOTO SETTTINGS PAGE TO ENABLE GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        finishAffinity();
                    }
                });
        android.support.v7.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public boolean isValidEmail(String target) {
        return emailAddressPattern.matcher(target).matches();

    }

    /************************************

     //////// * USER LOGIN  * ////////

     ************************************/

    private void User_Login() {


        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_login, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG_LOGIN, response.toString());
                Log.d("USER_LOGIN", response.toString());

                try {

                    JSONObject obj = new JSONObject(response);

                    System.out.println("REG 00" + obj);

                    int status = obj.getInt("status");

                    String message = obj.getString("message");
                    System.out.println("LoginStatus :" + message);

                    System.out.println("REG" + status);

                    if (status == 1) {

                        dialog.dismiss();


                        for (int i = 0; obj.length() > i; i++) {

                            JSONObject obj_one = obj.getJSONObject("data");

                            String str_user_id = obj_one.getString(TAG_USER_ID);
                            String str_user_name = obj_one.getString(TAG_USER_NAME);
                            String str_user_role = obj_one.getString(TAG_USER_ROLE);
                            String str_user_type = obj_one.getString(TAG_USER_TYPE);
                            String str_user_state = obj_one.getString(TAG_USER_DEPTID);

                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            session.createLoginSession(str_user_id, str_user_name, str_user_type, str_user_role, str_user_state);
                            HashMap<String, String> user = session.getUserDetails();

                            //  ADD THE USER DETAILS IN SHARED PREFERENCE
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("str_user_id", str_user_id);
                            editor.putString("str_user_name", str_user_name);
                            editor.putString("str_user_role", str_user_role);
                            editor.putString("str_user_type", str_user_type);
                            editor.putString("str_user_type", str_user_type);
                            editor.putString(Session_Manager.KEY_USER_STATE, str_user_state);

                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), Activity_Menu.class);
                            startActivity(intent);

                        }

                    } else {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "LOGIN FAILED", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    }

                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();


                params.put("user_name", str_login_email.toLowerCase());
                params.put("password", str_login_password);
                params.put("latitude", str_lat);
                params.put("longtitude", str_long);
                System.out.println("LATITUDE_AND_LONGITUDE_LOCATION :" + str_lat + " " + str_long);
                System.out.println("salespersonEmail" + str_login_email.toLowerCase());
                System.out.println("password" + str_login_password);

                return params;
            }
        };
        // Adding request to request queue
        queue.add(request);
    }

    /************************************

     //////// * CHANGE PASSWORD  * ////////

     ************************************/

    private void Forgot_Password() {


        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_forget_password, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG_SENT_PASSWORD, response.toString());
                Log.d("Sent_Password", response.toString());


                try {

                    JSONObject obj_one = new JSONObject(response);

                    System.out.println("REG 00" + obj_one);


                    int status = obj_one.getInt("status");
                    String message = obj_one.getString("message");

                    System.out.println("REG" + status);

                    if (status == 1) {

                        dialog_popup.dismiss();
                        TastyToast.makeText(getApplicationContext(), "PASSWORD SENT TO YOUR EMAIL SUCCESSFULLY", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    } else {

                        TastyToast.makeText(getApplicationContext(), "OOPS...! FAILED TO SENT PASSWORD TO YOUR MAIL :( ", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", str_forget_email.toLowerCase());

                System.out.println("email" + str_forget_email.toLowerCase());

                return params;
            }
        };
        // Adding request to request queue
        queue.add(request);
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {

            this.moveTaskToBack(true);
        } else {
             Toast_Show.displayToast(getApplicationContext(),"EXIT");
           /* Toast.makeText(getBaseContext(), "Exit!", Toast.LENGTH_SHORT).show();*/
            finishAffinity();

        }

        back_pressed = System.currentTimeMillis();
    }

    // location code
    public void NewLocation() {

        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        SimpleLocationGetter getter = new SimpleLocationGetter(this, this);
        getter.getLastLocation();
    }

    @Override
    public void onLocationReady(Location location) {
        Log.d("LOCATION", "onLocationReady: lat=" + location.getLatitude() + " lon=" + location.getLongitude());

        System.out.println("LOCATION 1 :: " + location.getLatitude());
        System.out.println("LOCATION 2 :: " + location.getLongitude());

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        str_lat = String.valueOf(latitude);
        str_long = String.valueOf(longitude);
    }

    @Override
    public void onError(String error) {
        Log.e("LOCATION", "Error: " + error);
    }


    /*********************************
     * For Loaction
     ********************************/
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Activity_Login.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity_Login.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Activity_Login.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Activity_Login.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    askForGPS();
                    break;
                //Call
                case 2:

                    break;
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }

    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(Activity_Login.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

}
