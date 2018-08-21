package banyan.com.fiducial.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import banyan.com.fiducial.Adapter.Adapter_AllEnquiry_List;
import banyan.com.fiducial.R;

public class Fragment_AllEnquiry_List extends Fragment
{
    ListView all_enquiry_list;
    SwipeRefreshLayout refreshLayout;
    FloatingActionButton fab_enquiry;
    Adapter_AllEnquiry_List adapter_allEnquiry_list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_allenquiry,null);
        all_enquiry_list = (ListView)v.findViewById(R.id.list_enquiry_list);
        refreshLayout  = (SwipeRefreshLayout)v.findViewById(R.id.enquiry_list_swipe_refresh_layout);
        fab_enquiry = (FloatingActionButton)v.findViewById(R.id.fab_enquiry);
        adapter_allEnquiry_list = new Adapter_AllEnquiry_List();

        return v;
    }
}
