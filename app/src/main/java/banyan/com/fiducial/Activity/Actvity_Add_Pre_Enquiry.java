package banyan.com.fiducial.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import banyan.com.fiducial.Adapter.Adapter_All_Enquiry_List;
import banyan.com.fiducial.Adapter.Adapter_Task;
import banyan.com.fiducial.Global.App_Config;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.Global.Toast_Show;
import banyan.com.fiducial.R;
import dmax.dialog.SpotsDialog;

public class Actvity_Add_Pre_Enquiry extends AppCompatActivity implements View.OnClickListener {
    public static String TAG_CUSTOMER_NAME = "customer_name";
    public static String TAG_Address1 = "address1";
    public static String TAG_ADDRESS2 = "address2";
    public static String TAG_CITY = "city";
    public static String TAG_STATE = "state";
    public static String TAG_COUNTRY = "country";
    public static String TAG_POSTALCODE = "postal_code";
    public static String TAG_PHO_NO = "phone_no";
    public static String TAG_MOB_NO = "mobile_no";
    public static String TAG_EMAIl = "email";
    public static String TAG_USERID = "user_id";
    public static String TAG_USERTYPE = "user_type";

    String str_userId, str_userTpye;
    Session_Manager session_manager;
    Toolbar mToolbar;
    SpotsDialog dialog;

    EditText edt_pre_name, edt_pre_mobile, edt_pre_zipcode, edt_pre_address,
            edt_pre_email, edt_pre_landline;
    // state parames
    public static final String TAG_STATE_NAME = "state_name";
    public static final String TAG_STATE_ID = "state_id";

    public static final String TAG_CITY_NAME = "city_name";
    public static final String TAG_CITY_ID = "city_id";

    Button btn_summit;

    ArrayList<String> Arraylist_state_id = null;
    ArrayList<String> Arraylist_state_name = null;

    ArrayList<String> Arraylist_city_id = null;
    ArrayList<String> Arraylist_city_name = null;

    public static RequestQueue queue;
    Spinner spn_pre_state, spn_pre_city;

    TextView t1;
    String str_state, str_state_id, str_city_id, str_city_name;

    // edittext field string name
    String customer_name, address,zipcode, landline, mobileNo, Email, UserId, UserType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pre_enquiry);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("ADD ENQUIRY");
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Menu.class);
                startActivity(i);
                finish();

            }
        });

        session_manager = new Session_Manager(getApplicationContext());
        session_manager.checkLogin();
        HashMap<String, String> user = session_manager.getUserDetails();
        str_userId = user.get(Session_Manager.KEY_USER_ID);
        str_userTpye = user.get(Session_Manager.KEY_USER_TYPE);

        edt_pre_name = (EditText) findViewById(R.id.edt_pre_name);
        edt_pre_mobile = (EditText) findViewById(R.id.edt_pre_mobile);
        edt_pre_zipcode = (EditText) findViewById(R.id.edt_pre_zipcode);
        edt_pre_address = (EditText) findViewById(R.id.edt_pre_address);
        edt_pre_email = (EditText) findViewById(R.id.edt_pre_email);
        edt_pre_landline = (EditText) findViewById(R.id.edt_pre_landline);
        btn_summit = (Button) findViewById(R.id.btn_pre_submit);

        spn_pre_state = (SearchableSpinner) findViewById(R.id.spn_pre_state); // state
        spn_pre_city = (SearchableSpinner) findViewById(R.id.spn_pre_city); // city

        btn_summit.setOnClickListener(this);

        Arraylist_state_id = new ArrayList<String>();
        Arraylist_state_name = new ArrayList<String>();

        Arraylist_city_id = new ArrayList<String>();
        Arraylist_city_name = new ArrayList<String>();

        spn_pre_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Group");

                t1 = (TextView) view;
                str_state = t1.getText().toString();
                str_state_id = Arraylist_state_id.get(position);

                System.out.println("State ID : " + Arraylist_state_id);

                Arraylist_city_id.clear();
                Arraylist_city_name.clear();

                if (str_state_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select a State", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {
                    dialog = new SpotsDialog(Actvity_Add_Pre_Enquiry.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Actvity_Add_Pre_Enquiry.this);
                    Function_Get_City();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spn_pre_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Product");

                t1 = (TextView) view;

                str_city_name = t1.getText().toString();
                str_city_id = Arraylist_city_id.get(position);

                System.out.println("City ID : " + str_city_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        try {

            Arraylist_state_name.clear();
            Arraylist_state_id.clear();
            dialog = new SpotsDialog(Actvity_Add_Pre_Enquiry.this);
            dialog.show();
            queue = Volley.newRequestQueue(getApplicationContext());
            Function_Get_State();

        } catch (Exception e) {

        }
    }

    /***************************
     * GET State Info
     ***************************/

    public void Function_Get_State() {

        System.out.println("### AppConfig.url_user_shop_list " + App_Config.url_state);
        StringRequest request = new StringRequest(Request.Method.POST,
                App_Config.url_state, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG STATE", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString(TAG_STATE_ID);
                            String name = obj1.getString(TAG_STATE_NAME);

                            Arraylist_state_name.add(name);
                            Arraylist_state_id.add(id);

                            try {
                                spn_pre_state
                                        .setAdapter(new ArrayAdapter<String>(Actvity_Add_Pre_Enquiry.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_state_name));

                            } catch (Exception e) {

                            }
                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "No State Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                TastyToast.makeText(getApplicationContext(), "Internal Error !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET City Info
     ***************************/

    public void Function_Get_City() {

        System.out.println("### AppConfig.url_user_city_list " + App_Config.url_city);
        StringRequest request = new StringRequest(Request.Method.POST,
                App_Config.url_city, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG City", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString(TAG_CITY_ID);
                            String name = obj1.getString(TAG_CITY_NAME);

                            Arraylist_city_name.add(name);
                            Arraylist_city_id.add(id);

                            try {
                                spn_pre_city
                                        .setAdapter(new ArrayAdapter<String>(Actvity_Add_Pre_Enquiry.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_city_name));

                            } catch (Exception e) {

                            }
                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "No City Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                TastyToast.makeText(getApplicationContext(), "Internal Error !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("state_id", str_state_id);

                System.out.println("SELECTED STATE ID" + str_state_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    // send all enquiry data to server
    public void Prelinimanary_EnquiryDetails() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_preliminary_enquiry, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj_one = new JSONObject(response);

                    int status = obj_one.getInt("status");
                    String message = obj_one.getString("message");

                    if (status == 1) {
                        Toast_Show.displayToast(getApplicationContext(),message);

                        /*Intent intent = new Intent(Actvity_Add_Pre_Enquiry.this, Activity_Menu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_CUSTOMER_NAME, customer_name);
                params.put(TAG_Address1, address);
                params.put(TAG_ADDRESS2, "");
                params.put(TAG_CITY, str_city_id);
                params.put(TAG_STATE, str_state_id);
                params.put(TAG_COUNTRY, "INDIA");
                params.put(TAG_POSTALCODE, zipcode);
                params.put(TAG_PHO_NO, landline);
                params.put(TAG_MOB_NO, mobileNo);
                params.put(TAG_EMAIl, Email);
                params.put(TAG_USERID, str_userId);
                params.put(TAG_USERTYPE, str_userTpye);

                System.out.println(TAG_CUSTOMER_NAME + customer_name);
                System.out.println(TAG_Address1 + address);
                System.out.println(TAG_ADDRESS2 + "");
                System.out.println(TAG_CITY + str_city_id);
                System.out.println(TAG_STATE + str_state_id);
                System.out.println(TAG_COUNTRY + "INDIA");
                System.out.println(TAG_POSTALCODE + zipcode);
                System.out.println(TAG_PHO_NO + landline);
                System.out.println(TAG_MOB_NO + mobileNo);
                System.out.println(TAG_EMAIl + Email);
                System.out.println(TAG_USERID + str_userId);
                System.out.println(TAG_USERTYPE + str_userTpye);


                return checkParams(params);

            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }

        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    @Override
    public void onClick(View v) {
        customer_name = edt_pre_name.getText().toString();
        mobileNo = edt_pre_mobile.getText().toString();
        address = edt_pre_address.getText().toString();
        zipcode = edt_pre_zipcode.getText().toString();
        landline = edt_pre_landline.getText().toString();
        Email = edt_pre_email.getText().toString();
        if (customer_name.isEmpty()) {
            Toast_Show.displayToast(getApplicationContext(), "Please Enter the Customer name");
        } else if (mobileNo.isEmpty()) {
            Toast_Show.displayToast(getApplicationContext(), "Please Enter mobile number");
        }
        else if(mobileNo.length() != 10 || mobileNo.length()>10) {
            Toast_Show.displayToast(getApplicationContext(),"Please enter 10 digit mobile number");
        }
        else
         {
             dialog = new SpotsDialog(Actvity_Add_Pre_Enquiry.this);
             dialog.show();
             queue = Volley.newRequestQueue(getApplicationContext());
             Prelinimanary_EnquiryDetails();

        }
    }
}

