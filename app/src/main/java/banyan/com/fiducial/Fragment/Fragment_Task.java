package banyan.com.fiducial.Fragment;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

import banyan.com.fiducial.Activity.Activity_Task_Description;
import banyan.com.fiducial.Adapter.Adapter_Task;
import banyan.com.fiducial.Global.App_Config;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.R;


/**
 * Created by Jo on 05/03/2018.
 */
public class Fragment_Task extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

    SearchableSpinner spn_spinner;

    FloatingActionButton fab_addtask;

    String TAG = "reg";
    public static final String TAG_TASK_ID = "taskId";
    public static final String TAG_TASK_TITLE = "task_title";
    public static final String TAG_TASK_DES = "task_description";
    public static final String TAG_TASK_CREATED_BY = "task_createdby";
    public static final String TAG_TASK_CREATED_DATE = "task_createddate";
    public static final String TAG_TASK_STATUS = "task_status";

    public static final String TAG_USER_NAME = "name";
    public static final String TAG_USER_ID = "id";

    String str_user_id,str_user_type;
    String str_user_role;
    String str_task_name,str_task_description,str_task_assignedto;

    ProgressDialog pDialog;
    public static RequestQueue queue;

    static ArrayList<HashMap<String, String>> complaint_list;

    ArrayList<String> Lname,lId;

    public Adapter_Task adapter;

    // Session Manager Class
    Session_Manager session;

    String str_name,  str_emp_id;
    String user_list_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_task, null);

        session = new Session_Manager(getActivity());
        session.checkLogin();


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_name = user.get(Session_Manager.KEY_USER);
        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE);
        str_emp_id = user.get(Session_Manager.KEY_EMP_ID);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);

        listView = (ListView) rootView.findViewById(R.id.listView_task);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_task);

        fab_addtask= (FloatingActionButton) rootView.findViewById(R.id.fab_add_task);

        // Hashmap for ListView
        complaint_list = new ArrayList<HashMap<String, String>>();

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        try {
                                            pDialog = new ProgressDialog(getActivity());
                                            pDialog.setMessage("PLEASE WAIT...");
                                            pDialog.setCancelable(true);
                                            complaint_list.clear();
                                            queue = Volley.newRequestQueue(getActivity());
                                            GetMyTask();

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                        }
                                    }
                                }
        );

        fab_addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str_select_task_id = complaint_list.get(position).get(TAG_TASK_ID);
                String str_select_task_name = complaint_list.get(position).get(TAG_TASK_TITLE);
                String str_select_task_description = complaint_list.get(position).get(TAG_TASK_DES);
                String str_select_task_created_by = complaint_list.get(position).get(TAG_TASK_CREATED_BY);
                String str_select_task_created_date = complaint_list.get(position).get(TAG_TASK_CREATED_DATE);
                String str_select_task_status = complaint_list.get(position).get(TAG_TASK_STATUS);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("task_id", str_select_task_id);
                editor.putString("task_name", str_select_task_name);
                editor.putString("task_description", str_select_task_description);
                editor.putString("task_created_by", str_select_task_created_by);
                editor.putString("task_created_date", str_select_task_created_date);
                editor.putString("task_status", str_select_task_status);
                editor.commit();

                Intent i = new Intent(getActivity(), Activity_Task_Description.class);
                startActivity(i);

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
            //complaint_list.clear();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("PLEASE WAIT...");
            pDialog.setCancelable(true);
            queue = Volley.newRequestQueue(getActivity());
            GetMyTask();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /*****************************
         * Add My Task Alert *
     ***************************/

    public void AddTask() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.activity_alert_addtask, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("ADD TASK");

        alertDialogBuilder.setView(promptsView);

        final EditText edt_task_name = (EditText) promptsView.findViewById(R.id.alert_edt_taskname);
        edt_task_name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        final EditText edt_task_description = (EditText) promptsView.findViewById(R.id.alert_edt_taskdes);
        edt_task_description.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        spn_spinner = (SearchableSpinner)promptsView.findViewById(R.id.alert_src_spinner);

        //get user_list
        try {

            //complaint_list = new ArrayList<>();
            Lname = new ArrayList<>();
            lId = new ArrayList<>();

            queue = Volley.newRequestQueue(getActivity());
            GetUsersList ();

        } catch (Exception e) {

        }

        spn_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String assigned_user_name = parent.getItemAtPosition(position).toString();

                user_list_id = lId.get(position);
                System.out.println("get_user_id : "+user_list_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertDialogBuilder.setCancelable(false)

                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        str_task_name = edt_task_name.getText().toString();
                        str_task_description = edt_task_description.getText().toString();
                        str_task_assignedto = spn_spinner.getSelectedItem().toString();

                        if (str_task_name.equals("")) {
                            edt_task_name.setError("PLEASE ENTER TASK NAME");
                        } else if (str_task_description.equals("")) {
                            edt_task_name.setError("PLEASE ENTER TASK DESCRIPTION");
                        } else if (str_task_assignedto.equals("")) {
                            Toast.makeText(getActivity(),"PLEASE SELECT ASSIGNED USER",Toast.LENGTH_SHORT).show();
                        } else {

                            pDialog = new ProgressDialog(getActivity());
                            pDialog.setMessage("PLEASE WAIT...");
                            pDialog.show();
                            pDialog.setCancelable(false);
                            queue = Volley.newRequestQueue(getActivity());
                            Function_addTask();

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
            * GET TASK
     ***************************/

    public void GetMyTask() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_list_Task, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                System.out.println("GER_RESPONSE :"+response);
                try {

                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString(TAG_TASK_ID);
                            String title = obj1.getString(TAG_TASK_TITLE);
                            String description = obj1.getString(TAG_TASK_DES);
                            String created_by = obj1.getString(TAG_TASK_CREATED_BY);
                            String date = obj1.getString(TAG_TASK_CREATED_DATE);
                            String task_status = obj1.getString(TAG_TASK_STATUS);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_TASK_ID, id);
                            map.put(TAG_TASK_TITLE, title.toUpperCase());
                            map.put(TAG_TASK_DES, description.toUpperCase());
                            map.put(TAG_TASK_CREATED_BY, created_by.toUpperCase());
                            map.put(TAG_TASK_CREATED_DATE, date);
                            map.put(TAG_TASK_STATUS, task_status);

                            complaint_list.add(map);

                        }

                        System.out.println("TASK HASHMAP ARRAY" + complaint_list);

                        adapter = new Adapter_Task( getActivity(), complaint_list);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else if (status == 0){

                        adapter = new Adapter_Task(getActivity(), complaint_list);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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

                params.put("user_id", str_user_id);
                params.put("user_type",str_user_type);

                System.out.println("task_user_id" + str_user_id);
                System.out.println("task_user_type" + str_user_type);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     * Function_addTask
     ********************************/

    private void Function_addTask() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_addTask, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, response.toString());
                Log.d("USER_REGISTER", response.toString());

                try {

                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    System.out.println("ADD_REG" + status);

                    if (status == 1) {

                        TastyToast.makeText(getContext(), "TASK  ADDED SUCCESSFULLY",  TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        pDialog.hide();

                        try {

                            complaint_list.clear();
                            queue = Volley.newRequestQueue(getActivity());
                            GetMyTask();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    } else {

                        pDialog.hide();
                        TastyToast.makeText(getContext(), "TASK  ADDED UNSUCCESSFULLY",  TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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

                params.put("taskName",str_task_name);
                params.put("taskDescription", str_task_description);
                params.put("user_id", str_user_id);
                params.put("taskAssignedTo",  user_list_id);
                params.put("user_type", str_user_type);
                params.put("user_role", str_user_role);

                System.out.println("taskName " + str_task_name);
                System.out.println("taskDescription " + str_task_description);
                System.out.println("user_id " + str_user_id);
                System.out.println("taskAssignedTo " + user_list_id);
                System.out.println("user_type " + str_user_type);
                System.out.println("user_role " + str_user_role);

                return params;
            }

        };


        // Adding request to request queue
        queue.add(request);
    }

    /*****************************
     * GET TASK
     ***************************/

    public void GetUsersList() {

        StringRequest request = new StringRequest(Request.Method.POST, App_Config.url_user_List, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {

                    System.out.println("USER_GET_RESPONSE :"+response);

                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String name = obj1.getString(TAG_USER_NAME);
                            String id = obj1.getString(TAG_USER_ID);

                            System.out.println("printId :"+id);
                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_USER_NAME,name );
                            map.put(TAG_USER_ID, id);

                            complaint_list.add(map);
                            Lname.add(name);
                            lId.add(id);

                        }

                        System.out.println("TASK HASHMAP ARRAY" + complaint_list);

                        try {
                            spn_spinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_dropdown_item, Lname));

                        } catch (Exception e) {

                        }


                        adapter = new Adapter_Task( getActivity(), complaint_list);
                        listView.setAdapter(adapter);

                    }else if (status == 0){

                        adapter = new Adapter_Task(getActivity(), complaint_list);
                        listView.setAdapter(adapter);

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

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

}
