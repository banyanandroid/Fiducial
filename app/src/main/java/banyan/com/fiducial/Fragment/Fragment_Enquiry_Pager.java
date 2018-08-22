package banyan.com.fiducial.Fragment;

/***
 *Created by vijay
 *
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import banyan.com.fiducial.R;

public class Fragment_Enquiry_Pager extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 8;

    public Fragment_Enquiry_Pager() {
        // empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_enquiry_pager,null);
        tabLayout = (TabLayout) rootview.findViewById(R.id.tabs_enquiry);
        viewPager = (ViewPager) rootview.findViewById(R.id.viewpager_enquiry);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return rootview;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new Fragment_AllEnquiry_List();
                case 1 : return new Fragment_Preliminary_Enquiry_list();
                case 2 : return new Fragment_Assigned_Enquiry();
                case 3 : return new Fragment_Secondary_Enquiry();
                case 4 : return new Fragment_Dropped_Enquiry();
                case 5 : return new Fragment_Schedule_Enquiry();
                case 6 : return new Fragment_Deepth_Enquiry();
                case 7 : return new Fragment_Successful_Enquiry();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "All ENQUIRY"; // only sales manager
                case 1 :
                    return "MY ENQUIRY"; // particular person for preliminary enquiry
                case 2:
                    return "ASSIGNED ENQUIRY";
                case 3 :
                    return "SECONDARY ENQUIRY"; // another option for prelinnary enquiry
                case 4:
                    return "DROP ENQUIRY";
                case 5:
                    return "SCHEDULE ENQUIRY";
                case 6:
                    return "DEEPTH ENQUIRY";
                case 7:
                    return "SUCESSFULL ENQUIRY";
            }
            return null;
        }
    }
}
