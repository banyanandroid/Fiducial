package banyan.com.fiducial.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import banyan.com.fiducial.Adapter.Adapter_Schedule_Enquiry;
import banyan.com.fiducial.R;

public class Fragment_Schedule_Enquiry extends Fragment
{
private Adapter_Schedule_Enquiry schedule_adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule_enquiry,null);

        return v;
    }
}
