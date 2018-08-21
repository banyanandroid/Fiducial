package banyan.com.fiducial.Adapter;

/**
 * Created By Vijay
 *
 */
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import banyan.com.fiducial.R;

public class Adapter_Schedule_Enquiry extends BaseAdapter
{
    CardView schedule_cardview;
    private TextView txt_username,txt_place,txt_date;

    private static LayoutInflater inflater = null;

    public Adapter_Schedule_Enquiry() {
        // do somthing here
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

        if (convertView == null)
            v = inflater.inflate(R.layout.adapter_fragment_schedule_enquiry, null);
        schedule_cardview = (CardView) v.findViewById(R.id.schedule_cardview);
        txt_username = (TextView) v.findViewById(R.id.txt_username);
        txt_place = (TextView) v.findViewById(R.id.txt_place);
        txt_date = (TextView) v.findViewById(R.id.txt_date);

        // set the text data
        txt_username.setText(" : ");
        txt_place.setText(" : ");
        txt_date.setText(" : ");

        return v;
    }
}
