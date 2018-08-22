package banyan.com.fiducial.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import banyan.com.fiducial.R;

public class Fragment_Enquiry_Home extends Fragment
{
    GridView home_enquiry_layout;
    Adapter_Home_Enquiry adapter_home_enquiry;
    ImageView enquiry_Img;
    TextView enquiry_text;
    public static String Home_Enquiry_Text[]={"All ENQUIRY","MY ENQUIRY","SUCCESS ENQUIRY","DROPPED ENQUIRY","MY ALERT ENQUIRY"};
    public static int Home_Enquiry_img[] ={R.drawable.ic_enquiry,R.drawable.ic_enquiry,R.drawable.ic_enquiry,R.drawable.ic_enquiry,R.drawable.ic_enquiry};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootview = inflater.inflate(R.layout.fragment_enquiry_home,null);
       home_enquiry_layout = (GridView)rootview.findViewById(R.id.enquiry_grid);
       adapter_home_enquiry = new Adapter_Home_Enquiry();
       home_enquiry_layout.setAdapter(adapter_home_enquiry);

        return rootview;

    }
    class Adapter_Home_Enquiry extends BaseAdapter
    {

        @Override
        public int getCount() {
            return Home_Enquiry_Text.length;
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
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.fragment_enquiryhome,null);
            enquiry_Img  = (ImageView)v.findViewById(R.id.grid_homeImg);
            enquiry_text = (TextView)v.findViewById(R.id.user_text);

            // set text
            enquiry_Img.setImageResource(Home_Enquiry_img[position]);
            enquiry_text.setText(Home_Enquiry_Text[position]);

            return v;
        }
    }
}
