package banyan.com.fiducial.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import banyan.com.fiducial.Fragment.Fragment_Customers;
import banyan.com.fiducial.Fragment.Fragment_MyProfile;
import banyan.com.fiducial.Fragment.Fragment_Task_Home;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.R;

public class Activity_Menu extends AppCompatActivity {

    private Session_Manager session;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_task:
                    fragment = new Fragment_Task_Home();
                    break;
                case R.id.navigation_enquiry:
                    /*fragment = new Fragment_Enquiry_Pager()*/;
                    Intent intent = new Intent(getApplicationContext(),Actvitity_Customize_Grid.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                case R.id.navigation_Customer:
                    fragment = new Fragment_Customers();
                    break;
                case R.id.navigation_profile:
                    fragment = new Fragment_MyProfile();
                   break;
                   default:
                       break;
            }

            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_);

        loadFragment(new Fragment_Task_Home());

        session = new Session_Manager(getApplicationContext());
        session.checkLogin();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
