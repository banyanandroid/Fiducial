package banyan.com.fiducial.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import banyan.com.fiducial.Activity.Actvity_Add_Pre_Enquiry;
import banyan.com.fiducial.Global.Session_Manager;
import banyan.com.fiducial.Global.Toast_Show;
import banyan.com.fiducial.R;

public class Actvitity_Customize_Grid extends Activity implements View.OnClickListener {
    GridView gridView;
    FloatingActionButton fabenquiry;
    public static String text[]={"ALL ENQUIRY","MY ENQUIRY","SUCCESS ENQUIRY","DROPPED ENQUIRY","MY ALERT ENQUIRY"};
    public static int img[]={R.drawable.ic_enquiry,R.drawable.ic_enquiry,R.drawable.ic_enquiry,R.drawable.ic_enquiry,R.drawable.ic_enquiry};
    TextView title;
    ImageView pic;
    Session_Manager session_manager;
    String str_userId, str_userTpye;

    GridAdapterEnquiry gridAdapterEnquiry;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_float_grid);

        session_manager = new Session_Manager(getApplicationContext());
        session_manager.checkLogin();
        HashMap<String, String> user = session_manager.getUserDetails();
        str_userId = user.get(Session_Manager.KEY_USER_ID);
        str_userTpye = user.get(Session_Manager.KEY_USER_TYPE);

        System.out.println("GETUSER_DETAILS :"+str_userId +" "+str_userTpye);

        gridView = (GridView) findViewById(R.id.grid_enquiry);
        gridAdapterEnquiry  =new GridAdapterEnquiry();
        gridView.setAdapter(gridAdapterEnquiry);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    Toast_Show.displayToast(getApplicationContext(),"Position :"+position);
                }else if(position ==1)
                {
                    Toast_Show.displayToast(getApplicationContext(),"Position :"+position);

                }else if(position == 2)
                {
                    Toast_Show.displayToast(getApplicationContext(),"Position :"+position);

                }else if(position== 3)
                {
                    Toast_Show.displayToast(getApplicationContext(),"Position :"+position);

                }else if(position == 4)
                {
                    Toast_Show.displayToast(getApplicationContext(),"Position :"+position);

                }
            }
        });
        fabenquiry = (FloatingActionButton) findViewById(R.id.fabgrid_enquiry);
        fabenquiry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabgrid_enquiry:
                Intent add_preEnquiry = new Intent(getApplicationContext(), Actvity_Add_Pre_Enquiry.class);
                add_preEnquiry.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(add_preEnquiry);
                break;
            default:
                break;
        }
    }

    class GridAdapterEnquiry extends BaseAdapter
    {

        @Override
        public int getCount() {
            return text.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View v=inflater.inflate(R.layout.enquiry_gridlayout,null);
            title = (TextView)v.findViewById(R.id.enquiry_text);
            pic = (ImageView)v.findViewById(R.id.enquiry_img);
            pic.setImageResource(img[position]);
            title.setText(text[position]);

            return v;
        }
    }
}
