package banyan.com.fiducial.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.util.Map;

import banyan.com.fiducial.Adapter.Adapter_All_Enquiry_List;
import banyan.com.fiducial.Adapter.Adapter_Task;
import banyan.com.fiducial.Global.App_Config;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.Global.Toast_Show;
import banyan.com.fiducial.R;

public class Actvity_Add_Pre_Enquiry extends AppCompatActivity implements View.OnClickListener{
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
    String str_userId,str_userTpye;
    Session_Manager session_manager;
    Toolbar mToolbar;
    EditText edt_pre_name,edt_pre_mobile,edt_pre_zipcode,edt_pre_address,
            edt_pre_email,edt_pre_landline;
  // state parames
    public static final String TAG_STATE_NAME = "state_name";
    public static final String TAG_STATE_ID = "state_id";

    public static final String TAG_CITY_NAME = "city_name";
    public static final String TAG_CITY_ID = "city_id";

    Button btn_summit;

    ArrayList<HashMap<String, String>> state_list;
    ArrayList<HashMap<String, String>> city_list;
    ArrayList<String> state_name, state_id;
    ArrayList<String> city_name, city_id;

    ArrayAdapter<String> mState,mCity;
    public static RequestQueue queue;
    Spinner spn_pre_state,spn_pre_city;

    // edittext field string name
    String customer_name,address1,address2,city,state,country,postalcode,phonoNo,mobileNo,Email,UserId,UserType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pre_enquiry);

        state_name = new ArrayList<>();
        state_id = new ArrayList<>();
        city_name = new ArrayList<>();
        city_id = new ArrayList<>();

        session_manager=new Session_Manager(getApplicationContext());
        session_manager.checkLogin();
        HashMap<String,String> user=session_manager.getUserDetails();
        str_userId = user.get(Session_Manager.KEY_USER_ID);
        str_userTpye = user.get(Session_Manager.KEY_USER_TYPE);

        edt_pre_name = (EditText) findViewById(R.id.edt_pre_name);
        edt_pre_mobile = (EditText) findViewById(R.id.edt_pre_mobile);
        edt_pre_zipcode = (EditText) findViewById(R.id.edt_pre_zipcode);
        edt_pre_address = (EditText) findViewById(R.id.edt_pre_address);
        edt_pre_email = (EditText) findViewById(R.id.edt_pre_email);
        edt_pre_landline = (EditText) findViewById(R.id.edt_pre_landline);
        btn_summit = (Button) findViewById(R.id.btn_pre_submit);
        btn_summit.setOnClickListener(this);
        spn_pre_state =(SearchableSpinner)findViewById(R.id.spn_pre_state); // state
        spn_pre_city =(SearchableSpinner)findViewById(R.id.spn_pre_city); // city

        mState = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,state_name);
        mState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_pre_state.setAdapter(mState);

        mCity = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,city_name);
        mCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_pre_city.setAdapter(mCity);

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

        try {

            state_list = new ArrayList<>();
            state_name = new ArrayList<>();
            state_id = new ArrayList<>();

            queue = Volley.newRequestQueue(getApplicationContext());
            GetStateList ();

        } catch (Exception e) {

        }
        try {

            city_list = new ArrayList<>();
            city_name = new ArrayList<>();
            city_id = new ArrayList<>();

            queue = Volley.newRequestQueue(getApplicationContext());
            GetCityList ();

        } catch (Exception e) {

        }
    }

    /**
     * state list api
     */
    public void GetStateList() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_state, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {

                    System.out.println("USER_GET_RESPONSE :"+response);

                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String name = obj1.getString(TAG_STATE_NAME);
                            String id = obj1.getString(TAG_STATE_ID);

                            System.out.println("printId :"+id+" "+name);
                            state_name.add(name);
                            state_id.add(id);

                        }

                        System.out.println("TASK HASHMAP ARRAY" + state_list);

                        try {

                            spn_pre_state.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_dropdown_item, state_name));

                        } catch (Exception e) {

                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /**
     * city api
     */
    public void GetCityList() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_city, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {

                    System.out.println("USER_GET_RESPONSE :"+response);

                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String name = obj1.getString(TAG_CITY_NAME);
                            String id = obj1.getString(TAG_CITY_ID);

                            System.out.println("printId :" + id);

                            city_name.add(name);
                            city_id.add(id);

                        }

                        System.out.println("TASK HASHMAP ARRAY" + city_list);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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

                params.put("state_id", state_id.toString());

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

                    if(status == 1)
                    {
                        TastyToast.makeText(getApplicationContext(),"Enquiry Added Successfully",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        Intent back_enquiry = new Intent(getApplicationContext(),Actvitity_Customize_Grid.class);
                        back_enquiry.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(back_enquiry);
                    }
                    else
                    {
                        TastyToast.makeText(getApplicationContext(),"Enquiry Added Unsuccessfully",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
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
                params.put(TAG_Address1, "");
                params.put(TAG_ADDRESS2,"");
                params.put(TAG_CITY,city_name.toString());
                params.put(TAG_STATE,state_name.toString());
                params.put(TAG_COUNTRY,"INDIA");
                params.put(TAG_POSTALCODE,"");
                params.put(TAG_PHO_NO,"");
                params.put(TAG_MOB_NO,mobileNo);
                params.put(TAG_EMAIl,"");
                params.put(TAG_USERID,str_userId);
                params.put(TAG_USERTYPE,str_userTpye);

                return params;

            }

        };
    }

    @Override
    public void onClick(View v) {
        customer_name = edt_pre_name.getText().toString();
        mobileNo = edt_pre_mobile.getText().toString();
        if(customer_name.isEmpty())
        {
            Toast_Show.displayToast(getApplicationContext(),"Please Enter the Customer name");
        }
        else if(mobileNo.isEmpty() || mobileNo.length()<10)
        {
            Toast_Show.displayToast(getApplicationContext(),"Please Enter the Valid mobile number");
        }else
        {
            Prelinimanary_EnquiryDetails();
            Intent intent=new Intent(Actvity_Add_Pre_Enquiry.this,Activity_Menu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }
}

