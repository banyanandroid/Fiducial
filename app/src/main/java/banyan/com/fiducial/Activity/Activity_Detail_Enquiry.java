package banyan.com.fiducial.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import banyan.com.fiducial.R;

public class Activity_Detail_Enquiry extends Activity
{
    Button btn_det_add,btn_det_contact_add,btn_pre_submit;
    Spinner spn_det_state,spn_det_city,spn_det_lead_ref,spn_det_idus_seg, spn_det_org_type,spn_det_vertical,spn_det_poly_dep,
            spn_det_poly_type,spn_det_insurance,spn_det_priority,spn_det_make,spn_det_model;
    EditText edt_det_name,edt_det_pan,edt_det_gst,edt_det_address,edt_det_zipcode,edt_det_landline,
            edt_det_mobile,edt_det_fax,edt_det_website,edt_det_email,edt_det_contact_person,edt_det_designation,
            edt_det_location,edt_det_remarks,edt_contact_name,edt_contact_design,edt_contact_address,edt_contact_mobile,
            edt_contact_email,edt_contact_dob,edt_contact_marriage,edt_contact_exp_date,edt_contact_premium,
            edt_contact_brokerage,edt_contact_vehicle_no,edt_contact_make_year,edt_contact_remark ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_enquiry);

        //Button
        btn_det_add = (Button)findViewById(R.id.btn_det_add);
        btn_det_contact_add = (Button)findViewById(R.id.btn_det_contact_add);
        btn_pre_submit = (Button)findViewById(R.id.btn_pre_submit);

//Spinner
        spn_det_state = (Spinner)findViewById(R.id.spn_det_state);
        spn_det_city= (Spinner)findViewById(R.id.spn_det_city);
        spn_det_lead_ref= (Spinner)findViewById(R.id.spn_det_lead_ref);
        spn_det_idus_seg= (Spinner)findViewById(R.id.spn_det_idus_seg);
        spn_det_org_type= (Spinner)findViewById(R.id.spn_det_org_type);
        spn_det_vertical= (Spinner)findViewById(R.id.spn_det_vertical);
        spn_det_poly_dep = (Spinner)findViewById(R.id.spn_det_poly_dep);
        spn_det_poly_type= (Spinner)findViewById(R.id.spn_det_poly_type);
        spn_det_insurance= (Spinner)findViewById(R.id.spn_det_insurance);
        spn_det_priority= (Spinner)findViewById(R.id.spn_det_priority);
        spn_det_make= (Spinner)findViewById(R.id.spn_det_make);
        spn_det_model= (Spinner)findViewById(R.id.spn_det_model);

// EditText

        edt_det_name = (EditText)findViewById(R.id.edt_det_name);
        edt_det_pan= (EditText)findViewById(R.id.edt_det_pan);
        edt_det_gst= (EditText)findViewById(R.id.edt_det_gst);
        edt_det_address= (EditText)findViewById(R.id.edt_det_address);
        edt_det_zipcode= (EditText)findViewById(R.id.edt_det_zipcode);
        edt_det_landline= (EditText)findViewById(R.id.edt_det_landline);
        edt_det_mobile= (EditText)findViewById(R.id.edt_det_mobile);
        edt_det_fax= (EditText)findViewById(R.id.edt_det_fax);
        edt_det_website= (EditText)findViewById(R.id.edt_det_website);
        edt_det_email= (EditText)findViewById(R.id.edt_det_email);
        edt_det_contact_person= (EditText)findViewById(R.id.edt_det_contact_person);
        edt_det_designation= (EditText)findViewById(R.id.edt_det_designation);
        edt_det_location= (EditText)findViewById(R.id.edt_det_location);
        edt_det_remarks= (EditText)findViewById(R.id.edt_det_remarks);
        edt_contact_name= (EditText)findViewById(R.id.edt_contact_name);
        edt_contact_design= (EditText)findViewById(R.id.edt_contact_design);
        edt_contact_address= (EditText)findViewById(R.id.edt_contact_address);
        edt_contact_mobile= (EditText)findViewById(R.id.edt_contact_mobile);
        edt_contact_email= (EditText)findViewById(R.id.edt_contact_email);
        edt_contact_dob= (EditText)findViewById(R.id.edt_contact_dob);
        edt_contact_marriage= (EditText)findViewById(R.id.edt_contact_marriage);
        edt_contact_exp_date= (EditText)findViewById(R.id.edt_contact_exp_date);
        edt_contact_premium= (EditText)findViewById(R.id.edt_contact_premium);
        edt_contact_brokerage= (EditText)findViewById(R.id.edt_contact_brokerage);
        edt_contact_vehicle_no= (EditText)findViewById(R.id.edt_contact_vehicle_no);
        edt_contact_make_year= (EditText)findViewById(R.id.edt_contact_make_year);
        edt_contact_remark = (EditText)findViewById(R.id.edt_contact_remark);

    }
}
