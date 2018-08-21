package banyan.com.fiducial.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import banyan.com.fiducial.Activity.Activity_Schedule_Description;
import banyan.com.fiducial.Adapter.Adapter_Schedule;
import banyan.com.fiducial.Global.App_Config;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.R;


/**
 * Created by Jo on 05/03/2018.
 */

public class Fragment_Schedule extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab_addschedule;

    String str_user_name, str_user_id,str_user_type;
    String str_scedule_name, str_scedule_des,str_scedule_date ="";
    String str_user_role;

    ProgressDialog pDialog;
    public static RequestQueue queue;

    // Session Manager Class
    Session_Manager session;

    String TAG = Fragment_Schedule.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mytask_listView;

    public static final String TAG_SCHEDULE_ID = "schedule_id";
    public static final String TAG_SCHEDULE_TITLE = "schedule_title";
    public static final String TAG_SCHEDULE_DES = "schedule_description";
    public static final String TAG_SCHEDULE_NOTE = "schedule_note";
    public static final String TAG_SCHEDULE_DATE = "schedule_date";

    static ArrayList<HashMap<String, String>> complaint_list;

    HashMap<String, String> params = new HashMap<String, String>();

    public Adapter_Schedule adapter;

    String str_select_schedule_id;

    Button btn_set_date;

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, null);

        // Session Manager
        session = new Session_Manager(getActivity());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_user_name = user.get(Session_Manager.KEY_USER);
        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);

        fab_addschedule = (FloatingActionButton) rootView.findViewById(R.id.fab_add_schedule);

        mytask_listView = (ListView) rootView.findViewById(R.id.listView_mytask);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_MYTASK);
        swipeRefreshLayout.setOnRefreshListener(this);

        // Hashmap for ListView
        complaint_list = new ArrayList<HashMap<String, String>>();


        fab_addschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddScedule();
            }
        });

        swipeRefreshLayout.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        try {
                                            pDialog = new ProgressDialog(getActivity());
                                            pDialog.setMessage("PLEASE WAIT...");
                                            pDialog.setCancelable(true);
                                            queue = Volley.newRequestQueue(getActivity());
                                            GetMySchedule();

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                        }
                                    }
                                }
        );

        mytask_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String str_select_schedule_id = complaint_list.get(position).get(TAG_SCHEDULE_ID);
                String str_select_schedule_title = complaint_list.get(position).get(TAG_SCHEDULE_TITLE);
                String str_select_schedule_description = complaint_list.get(position).get(TAG_SCHEDULE_DES);
                String str_select_schedule_note = complaint_list.get(position).get(TAG_SCHEDULE_NOTE);
                String str_select_schedule_update = complaint_list.get(position).get(TAG_SCHEDULE_DATE);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("schedule_id", str_select_schedule_id);
                editor.putString("schedule_title", str_select_schedule_title);
                editor.putString("schedule_description", str_select_schedule_description);
                editor.putString("schedule_note", str_select_schedule_note);
                editor.putString("schedule_date", str_select_schedule_update);
                editor.commit();

                Intent i = new Intent(getContext(), Activity_Schedule_Description.class);
                startActivity(i);

            }

        });

        mytask_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // TODO Auto-generated method stub

                str_select_schedule_id = complaint_list.get(pos).get(TAG_SCHEDULE_ID);

                Delete_Task_alert();

                return true;

            }
        });

        return rootView;

}

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {

        try {
            complaint_list.clear();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("PLEASE WAIT...");
            pDialog.setCancelable(true);
            queue = Volley.newRequestQueue(getActivity());
            GetMySchedule();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    /*****************************
          * Add My Task Alert
     ***************************/

    public void AddScedule() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.activity_alert_addscedule, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("ADD SCHEDULE");

        alertDialogBuilder.setView(promptsView);

        btn_set_date = (Button) promptsView.findViewById(R.id.btn_set_date);

        final EditText edt_task_name = (EditText) promptsView.findViewById(R.id.alert_edt_taskname);
        edt_task_name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        final EditText edt_task_description = (EditText) promptsView.findViewById(R.id.alert_edt_taskdes);
        edt_task_description.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        final EditText edt_scedule_date = (EditText) promptsView.findViewById(R.id.edt_scedule_date);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        btn_set_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        edt_scedule_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
            });

        alertDialogBuilder.setCancelable(false)

                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                str_scedule_name = edt_task_name.getText().toString();
                                str_scedule_des = edt_task_description.getText().toString();
                                str_scedule_date = edt_scedule_date.getText().toString();

                                if (str_scedule_name.equals("")) {
                                    edt_task_name.setError("PLEASE ENTER MY PLAN NAME");
                                } else if (str_scedule_des.equals("")) {
                                    edt_task_name.setError("PLEASE ENTER MY PLAN DESCRIPTION");
                                } else {

                                    pDialog = new ProgressDialog(getActivity());
                                    pDialog.setMessage("PLEASE WAIT...");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    queue = Volley.newRequestQueue(getActivity());
                                    Function_addScedule();
                                    // dialog.cancel();
                                }

                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    /*****************************
             GET My Task
     ***************************/

    public void GetMySchedule() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_schedule, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 1) {

                        JSONArray arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString(TAG_SCHEDULE_ID);
                            String title = obj1.getString(TAG_SCHEDULE_TITLE);
                            String description = obj1.getString(TAG_SCHEDULE_DES);
                            String note = obj1.getString(TAG_SCHEDULE_NOTE);
                            String date = obj1.getString(TAG_SCHEDULE_DATE);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_SCHEDULE_ID, id);
                            map.put(TAG_SCHEDULE_TITLE, title.toUpperCase());
                            map.put(TAG_SCHEDULE_DES, description.toUpperCase());
                            map.put(TAG_SCHEDULE_NOTE, note.toUpperCase());
                            map.put(TAG_SCHEDULE_DATE, date);

                            complaint_list.add(map);

                            System.out.println("HASHMAP ARRAY" + complaint_list);

                        }

                        adapter = new Adapter_Schedule(getActivity(), complaint_list);
                        mytask_listView.setAdapter(adapter);

                    } else if (status == 0) {

                        adapter = new Adapter_Schedule(getActivity(), complaint_list);
                        mytask_listView.setAdapter(adapter);

                        TastyToast.makeText(getContext(),"No data available",TastyToast.LENGTH_SHORT,TastyToast.WARNING);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                //  pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id); // replace as str_id
                params.put("user_type", str_user_type);

                System.out.println("userid" + str_user_id);
                System.out.println("userType" + str_user_type);

                return params;

            }
        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     * Function_addTask
     ********************************/

    private void Function_addScedule() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_Add_Schedule, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Log.d("USER_REGISTER", response.toString());

                try {

                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    System.out.println("REG" + status);

                    if (status == 1) {

                        TastyToast.makeText(getContext(), "SCHEDULE  ADDED SUCCESSFULLY",  TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        pDialog.hide();

                        try {
                            complaint_list.clear();
                            queue = Volley.newRequestQueue(getActivity());
                            GetMySchedule();

                        } catch (Exception e) {
                            // TODO: handle exception

                        }

                    } else {

                        pDialog.hide();
                        TastyToast.makeText(getContext(), "SCHEDULE  ADDED UNSUCCESSFULLY",  TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
                params.put("schedule_title", str_scedule_name);
                params.put("schedule_description", str_scedule_des);
                params.put("schedule_date", str_scedule_date);
                params.put("user_type", str_user_type);
                params.put("user_role", str_user_role);

                System.out.println("user_id" + str_user_id);
                System.out.println("schedule_title" + str_scedule_name);
                System.out.println("schedule_description" + str_scedule_des);
                System.out.println("schedule_date" + str_scedule_date);
                System.out.println("user_type" + str_user_type);
                System.out.println("user_role" + str_user_role);

                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        queue.add(request);
    }


    /************************************
     * Delete My Task Alert Dialog
     ***********************************/

    private void Delete_Task_alert() {

        new AlertDialog.Builder(getActivity())
                .setTitle("FIDUCIAL")
                .setMessage("DO YOU WANT TO DELETE THIS SCHEDULE?")
                .setIcon(R.drawable.ic_action_delete)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                System.out.println("Schedule ID " + str_select_schedule_id);

                                pDialog = new ProgressDialog(getActivity());
                                pDialog.setMessage("PLEASE WAIT...");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                queue = Volley.newRequestQueue(getActivity());
                                Function_DeleteTask();

                            }
                        }).show();
    }


    /************************************
     * Delete My Task Function
     ***********************************/

    private void Function_DeleteTask() {

        StringRequest request = new StringRequest(Request.Method.POST,App_Config.url_delete_schedule, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, response.toString());
                Log.d("USER_REGISTER", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");

                    System.out.println("DELETEREG" + success);

                    if (success == 1) {

                        TastyToast.makeText(getContext(), "MY PLAN DELETED SUCCESSFULLY",  TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        pDialog.hide();

                        try {
                            complaint_list.clear();
                            queue = Volley.newRequestQueue(getActivity());
                            GetMySchedule();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    } else {

                        pDialog.hide();
                        TastyToast.makeText(getContext(), "MY PLAN DELETED UNSUCCESSFULLY",  TastyToast.LENGTH_LONG, TastyToast.WARNING);

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

                params.put("schedule_id", str_select_schedule_id);

                System.out.println("schedule_id" + str_select_schedule_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }
}
