package banyan.com.fiducial.Adapter;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import banyan.com.fiducial.R;

public class Adapter_AllEnquiry_List extends BaseAdapter
{
    CardView all_enquiry_cardview;
    TextView all_txt_username,all_txt_place;

    private static LayoutInflater inflater = null;

    public Adapter_AllEnquiry_List() {
        // do something on here
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
