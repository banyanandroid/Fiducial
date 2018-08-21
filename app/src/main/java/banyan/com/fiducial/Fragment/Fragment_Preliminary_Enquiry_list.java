package banyan.com.fiducial.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import banyan.com.fiducial.R;

public class Fragment_Preliminary_Enquiry_list extends Fragment {
    EditText username,mobileNumber,email,address;
    Button submit_primaryEnquiry;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_addpreliminary_enquiry,null);

        return rootview;
    }

}
