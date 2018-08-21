package banyan.com.fiducial.Global;

/*****
 *
 * Created by Banyan
 */
public class App_Config
{
    public static String BASE_URL = "http://fiducial.in/demo/Apicontroller";
    // login
    public static String url_login = BASE_URL + "/login";
    public static String url_forget_password = BASE_URL + "/forgot_password";   // emp id not working
    public static String url_change_password = BASE_URL + "/change_password";   // emp id not working

    // logout
    public static String User_Logout =BASE_URL+ "/logout";
    // list schedule
    public static String url_schedule =BASE_URL+ "/schedule_list";
    // add schedule
    public static String url_Add_Schedule=BASE_URL+ "/add_schedule";
    // update schedule
    public static String url_update_schedule=BASE_URL+ "/update_schedule";
    // delete the schedule
    public static String url_delete_schedule=BASE_URL+ "/schedule_delete";
    // preliminary enquiry
    public static String url_preliminary_enquiry =BASE_URL+ "/add_preliminary_enquiry";
    // state list
    public static String url_state = BASE_URL+ "/list_state";
    // list task
    public static String url_list_Task = BASE_URL+ "/list_task";
    // list addtask
    public static String url_addTask = BASE_URL+ "/add_task";
    // user_List
    public static String url_user_List =BASE_URL+ "/users_list";
    // update task
    public static String url_update_task =BASE_URL+ "/update_task";
    // update atsk status
    public static String url_update_Task_Status =BASE_URL+ "/update_task_status";
    // list_single_Task usingtimeline viewer
    public static String url_list_single_Task =BASE_URL+ "/list_single_task";

    //gps
    public static String url_update_location = BASE_URL + "/locationupdate";
}
