package banyan.com.fiducial.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import banyan.com.fiducial.Global.App_Config;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.R;


/**
 * Created by User on 9/26/2016.
 */
public class Activity_Schedule_Description extends AppCompatActivity {

    private Toolbar mToolbar;
    EditText edt_name, edt_description, edt_note, edt_date;
    Button btn_update, btn_cancel;

    String str_select_schedule_id,str_select_schedule_title,str_select_schedule_description,
            str_select_schedule_note,str_select_schedule_update,str_user_type;

    String  str_user_role;

    ProgressDialog pDialog;
    public static RequestQueue queue;

    public static final String TAG_SCHEDULE_ID = "user_id";
    public static final String TAG_SCHEDULE_NOTE = "schedule_note";
    public static final String TAG_USER_TYPE = "user_type";
    public static final String TAG_USER_ROLE = "user_role";


    String TAG = "add task";

    // Session Manager Class
    Session_Manager session;

    String str_name, str_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_description);

        session = new Session_Manager(Activity_Schedule_Description.this);
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_name = user.get(Session_Manager.KEY_USER);
        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);

        //findviewbyid
        edt_name = (EditText) findViewById(R.id.mytask_edt_taskname);
        edt_name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edt_description = (EditText) findViewById(R.id.mytask_edt_taskdes);
        edt_description.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edt_note = (EditText) findViewById(R.id.mytask_edt_tasknote);
        edt_note.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edt_date = (EditText) findViewById(R.id.mytask_edt_taskupdateon);

        btn_update = (Button) findViewById(R.id.mytask_btn_update);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("SCHEDULE DESCRIPTION");
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Activity_Schedule_Description.this);

        str_select_schedule_id = sharedPreferences.getString("schedule_id", "schedule_id");
        str_select_schedule_title = sharedPreferences.getString("schedule_title", "schedule_title");
        str_select_schedule_description = sharedPreferences.getString("schedule_description", "schedule_description");
        str_select_schedule_note = sharedPreferences.getString("schedule_note", "schedule_note");
        str_select_schedule_update = sharedPreferences.getString("schedule_date", "schedule_date");

        try {
            edt_name.setText("" + str_select_schedule_title);
            edt_description.setText("" + str_select_schedule_description);
            edt_note.setText("" + str_select_schedule_note);
            edt_date.setText("" + str_select_schedule_update);
        } catch (Exception e) {

        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_select_schedule_title = edt_name.getText().toString();
                str_select_schedule_description = edt_description.getText().toString();
                str_select_schedule_note = edt_note.getText().toString();

                if (str_select_schedule_title.equals("")) {
                    edt_name.setError("PLEASE ENTER SCHEDULE NAME");
                } else if (str_select_schedule_description.equals("")) {
                    edt_description.setError("PLEASE ENTER SCHEDULE DESCRIPTION");
                } else if (str_select_schedule_note.equals("")) {
                    edt_note.setError("PLEASE ENTER SCHEDULE NOTE");
                }  else if (str_select_schedule_update.equals("")) {
                        edt_note.setError("PLEASE ENTER DATE");
                } else {

                    pDialog = new ProgressDialog(Activity_Schedule_Description.this);
                    pDialog.setMessage("PLEASE WAIT...");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    queue = Volley.newRequestQueue(Activity_Schedule_Description.this);
                    Function_UpdateSchedule();
                }

            }
        });

    }

    /********************************
     * Function_UpdateSchedule
     *********************************/

    private void Function_UpdateSchedule() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_update_schedule, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, response.toString());
                Log.d("USER_REGISTER", response.toString());

                try {

                    JSONObject obj = new JSONObject(response);

                    int status = obj.getInt("status");

                    System.out.println("UPDATE REG" + status);

                    if (status == 1) {

                        pDialog.hide();

                        TastyToast.makeText(getApplicationContext(),"MY PLAN UPDATED SUCCESSFULLY",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);

                        Intent intent = new Intent(Activity_Schedule_Description.this,Activity_Menu.class);
                        startActivity(intent);

                    } else {

                        pDialog.hide();

                        TastyToast.makeText(getApplicationContext(),"MY PLAN UPDATE FAILED PLEASE TRY AGAIN",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);
                params.put("schedule_id", str_select_schedule_id);
                params.put("schedule_note", str_select_schedule_note);
                params.put("user_type", str_user_type);
                params.put("user_role", str_user_role);

                System.out.println("user_id" + str_user_id);
                System.out.println("schedule_id" + str_select_schedule_id);
                System.out.println("schedule_note" + str_select_schedule_note);
                System.out.println("user_type" + str_user_type);
                System.out.println("user_role" + str_user_role);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

}
