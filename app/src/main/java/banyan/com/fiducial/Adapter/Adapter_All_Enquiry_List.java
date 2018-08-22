package banyan.com.fiducial.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import banyan.com.fiducial.R;

public class Adapter_All_Enquiry_List extends BaseAdapter
{
    CardView all_enquiry_cardview;
    TextView all_txt_username,all_txt_place;
    ArrayList<HashMap<String,String>> state_list =new ArrayList<>();
    private static LayoutInflater inflater = null;
    Context context;

    public Adapter_All_Enquiry_List(Context context,ArrayList<HashMap<String,String>> mstate) {
        // do something on here
        this.context = context;
        this.state_list = mstate;

    }

    @Override
    public int getCount() {
        return 0;
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
        View v = convertView;
          if(convertView == null)
              v =inflater.inflate(R.layout.adapter_all_enquiry,null);
              all_enquiry_cardview = (CardView)v.findViewById(R.id.all_enquiry_cardview);
              all_txt_username = (TextView)v.findViewById(R.id.all_txt_username);
              all_txt_place  = (TextView)v.findViewById(R.id.all_txt_place);

              // set the textview
              all_txt_username.setText(" : ");
              all_txt_place.setText(" : ");

        return v;
    }
}
