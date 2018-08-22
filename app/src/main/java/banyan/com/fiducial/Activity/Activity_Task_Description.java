package banyan.com.fiducial.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import banyan.com.fiducial.Global.App_Config;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.R;

/**
 * Created by User on 9/27/2016.
 */
public class Activity_Task_Description extends AppCompatActivity {

    private Toolbar mToolbar;
    EditText edt_name, edt_description, edt_comment, edt_date;
    String str_user_id,str_user_type,str_user_role;
    String image_type = "",encodedstring="",  str_selected_image = "",listString="";

    String str_task_created_by, str_task_comment, str_task_id, str_task_name, str_task_description,
            str_task_created_date, str_task_status = "";

    Button btn_update,btn_add_task_image;

    TextView txt_view_image_uploaded;

    LinearLayout image_upload;

    ProgressDialog pDialog;
    public static RequestQueue queue;

    ArrayList<String> Arraylist_image_encode , Arraylist_dummy ;
    ArrayList<String> Arraylist_image = null;

    private ArrayList<com.nguyenhoanglam.imagepicker.model.Image> images = new ArrayList<>();

    String TAG = "Schedule";
    String str_new_note;

    // Session Manager Class
    Session_Manager session;


    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_TASK_ID = "task_id";
    public static final String TAG_TASK_IMAGE = "image";
    public static final String TAG_TASK_COMMENT = "comment";
    public static final String TAG_USER_TYPE = "user_type";

    private Spinner spnr_task_status;

    private int int_task_status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_plan_description);

        edt_name = (EditText) findViewById(R.id.schedule_edt_taskname);
        edt_name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edt_description = (EditText) findViewById(R.id.schedule_edt_taskdes);
        edt_description.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edt_comment = (EditText) findViewById(R.id.task_edt_comment);
        edt_comment.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edt_date = (EditText) findViewById(R.id.schedule_edt_taskupdateon);
        edt_date.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        btn_update = (Button) findViewById(R.id.myschedule_btn_update);
        btn_add_task_image = (Button)findViewById(R.id.btn_add_task_image);
        txt_view_image_uploaded = (TextView) findViewById(R.id.txt_view_image_uploaded);
        spnr_task_status = (Spinner)findViewById(R.id.spnr_task_status);

        image_upload = (LinearLayout)findViewById(R.id.image_upload);

        session = new Session_Manager(getApplicationContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        System.out.println("GET_USER_ID :"+str_user_id);

        str_user_type = user.get(Session_Manager.KEY_USER_TYPE);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);

        System.out.println("SESSION_DETAILS :"+str_user_role);

        /*image_upload.setVisibility(View.GONE);*/

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("TASK DESCRIPTION");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

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

        // IMG PIC
        Arraylist_image_encode = new ArrayList<String>();
        Arraylist_dummy = new ArrayList<String>();

        Arraylist_image = new ArrayList<String>();

        // ADD IMAGE FOR ENQUIRY
        btn_add_task_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_type = "Location photos";
                ImagePicker();

            }
        });

        // check while the user id admin or sales person


        // task status
        spnr_task_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                str_task_status = "";
                str_task_name = "";
                str_task_description = "";
                str_task_created_date = "";


                str_task_status = parent.getItemAtPosition(position).toString();

                if (str_task_status.equals("Not Completed")) {

                    str_task_status = "0";

                } else if (str_task_status.equals("Completed")) {

                    str_task_status = "1";


                } else {

                    str_task_status = "";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Activity_Task_Description.this);

        str_task_id = sharedPreferences.getString("task_id", "task_id");
        str_task_name = sharedPreferences.getString("task_name", "task_name");
        str_task_description = sharedPreferences.getString("task_description", "task_description");
        str_task_created_by = sharedPreferences.getString("task_created_by", "task_created_by");
        str_task_created_date = sharedPreferences.getString("task_created_date", "task_created_date");
        str_task_status = sharedPreferences.getString("task_status", "task_status");

        System.out.println("### str_task_status : " + str_task_status);

        try {

            edt_name.setText("" + str_task_name);
            edt_description.setText("" + str_task_description);
            edt_date.setText("" + str_task_created_date);


            spnr_task_status.setSelection(Integer.parseInt(str_task_status));


            System.out.println("### select status index :" + spnr_task_status.getSelectedItemPosition());


        } catch (Exception e) {
            System.out.println("### Exception ");
        }


        btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                str_task_name = edt_name.getText().toString();
                str_task_description = edt_description.getText().toString();
                str_task_created_date = edt_date.getText().toString();
                str_task_comment = edt_comment.getText().toString();

                if (str_task_name.equals("")) {
                    edt_name.setError("PLEASE ENTER TASK NAME");
                }else if (str_task_description.equals("")) {
                    edt_description.setError("PLEASE ENTER TASK DESCRIPTION");
                } else if (str_task_comment.equals("")) {
                    edt_comment.setError("PLEASE ENTER TASK COMMENT");
                }else if  (str_task_created_date.equals("")) {
                    edt_comment.setError("PLEASE ENTER ACTUAL PLAN DATE");
                } else if (str_task_status != null && str_task_status.equals("")) {
                    TastyToast.makeText(Activity_Task_Description.this, "PLEASE SELECT ACTUAL PLAN STATUS", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }  else {
                    pDialog = new ProgressDialog(Activity_Task_Description.this);
                    pDialog.setMessage("PLEASE WAIT...");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    queue = Volley.newRequestQueue(Activity_Task_Description.this);
                    Function_UpdateTask();
                }

            }
        });

        if(str_user_id.equals(1)){

            image_upload.setVisibility(View.GONE);
            Update_Task_Status();

        }else{
            image_upload.setVisibility(View.VISIBLE);
        }
    }

    /*******************************
           *  PIC UPLOADER
     * ***************************/

// Recomended builder
    public void ImagePicker () {

        ImagePicker.with(this)
                .setFolderMode(true) // set folder mode (false by default)
                .setFolderTitle("FOLDER") // folder selection title
                .setImageTitle("TAP TO SELECT") // image selection title
                .setMultipleMode(true) // multi mode (default mode)
                .setMaxSize(1)// max images can be selected (999 by default)
                .setKeepScreenOn(true)
                .start();

        com.nguyenhoanglam.imagepicker.model.Config config = new com.nguyenhoanglam.imagepicker.model.Config();

    }

    // GET IMAGE FROM IMAGE PICKER
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        if (requestCode == com.nguyenhoanglam.imagepicker.model.Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {

            images = data.getParcelableArrayListExtra(com.nguyenhoanglam.imagepicker.model.Config.EXTRA_IMAGES);

            StringBuilder sb = new StringBuilder();
            for (int i = 0, l = images.size(); i < l; i++) {

                String str_img_path = images.get(i).getPath();

                Bitmap bmBitmap = BitmapFactory.decodeFile(str_img_path);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bmBitmap.compress(Bitmap.CompressFormat.JPEG, 25, bao);
                byte[] ba = bao.toByteArray();
                encodedstring = Base64.encodeToString(ba, 0);
                Log.e("base64", "-----" + encodedstring);

                Arraylist_image_encode.add(encodedstring);

                txt_view_image_uploaded.setText("IMAGE ADDED SUCCESSFULLY.");
                btn_add_task_image.setText("CHANGE IMAGE");
            }

            Encode_Image1();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Encode_Image1 () {

        for (String s : Arraylist_image_encode) {
            listString += s + "IMAGE:";
        }
        str_selected_image = listString;
        System.out.print("###  Image Uploaded " + listString);
    }

    ////for 3 dots
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();


        // return true so that the menu pop up is opened
        return true;
    }

    ////for 3 dot item click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }


    /********************************
     * Function_Update Schedule
     *********************************/

    private void Function_UpdateTask() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_update_task, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, response.toString());
                Log.d("USER_REGISTER", response.toString());

                try {

                    JSONObject obj = new JSONObject(response);

                    int status = obj.getInt("status");

                    System.out.println("REG" + status);

                    if (status == 1) {

                        pDialog.hide();

                        TastyToast.makeText(getApplicationContext(), "TASK UPDATED SUCCESSFULLY", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        Intent intent = new Intent(Activity_Task_Description.this, Activity_Menu.class);
                        startActivity(intent);

                    } else {
                        pDialog.hide();

                        TastyToast.makeText(getApplicationContext(), "TASK UPDATE FAILED, PLEASE TRY AGAIN", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

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
                params.put("task_id", str_task_id);
                params.put("image", str_selected_image);
                params.put("comment", str_task_comment);
                params.put("user_type", str_user_type);

                System.out.println("user_id : " + str_user_id);
                System.out.println("task_id : " + str_task_id);
                System.out.println("image : " + str_selected_image);
                System.out.println("comment : " + str_task_comment);
                System.out.println("user_type : " + str_user_type);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     * Function_Update Status
     *********************************/

    private void Update_Task_Status() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_update_Task_Status, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, response.toString());
                Log.d("USER_REGISTER", response.toString());

                try {

                    JSONObject obj = new JSONObject(response);

                    int status = obj.getInt("status");

                    System.out.println("REG" + status);

                    if (status == 1) {

                        pDialog.hide();

                        TastyToast.makeText(getApplicationContext(), "TASK UPDATED SUCCESSFULLY", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        Intent intent = new Intent(Activity_Task_Description.this, Activity_Menu.class);
                        startActivity(intent);

                    } else {
                        pDialog.hide();

                        TastyToast.makeText(getApplicationContext(), "TASK UPDATE FAILED, PLEASE TRY AGAIN", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

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
                params.put("task_id", str_task_id);
                params.put("status", str_task_status);
                params.put("user_type", str_user_type);

                System.out.println("user_id : " + str_user_id);
                System.out.println("task_id : " + str_task_id);
                System.out.println("status :" + str_task_status);
                System.out.println("user_type : " + str_user_type);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

}
