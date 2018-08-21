package banyan.com.fiducial.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;

import banyan.com.fiducial.Fragment.Fragment_Task;
import banyan.com.fiducial.R;

import static banyan.com.fiducial.Fragment.Fragment_Task.TAG_TASK_TITLE;


public class Adapter_Task extends BaseAdapter {

	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    private TextDrawable.IBuilder mDrawableBuilder;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    public Adapter_Task(Activity a, ArrayList<HashMap<String, String>> d) {

        activity = a;
        data=d;
        bgColors = activity.getResources().getStringArray(R.array.movie_serial_bg);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
          
        public View getView(int position, View convertView, ViewGroup parent) {
            View v=convertView;

            if(convertView==null)
                v = inflater.inflate(R.layout.adapter_task, null);

            ImageView imageView = (ImageView)v.findViewById(R.id.my_task_image);
            TextView title = (TextView)v.findViewById(R.id.txt_task_title);
            TextView description = (TextView)v.findViewById(R.id.txt_task_description);

            TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(String.valueOf(data.get(position).get(TAG_TASK_TITLE)).charAt(0)), Color.parseColor(new String(bgColors[position % bgColors.length])));
            imageView.setImageDrawable(drawable);

            drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.BLACK)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(10) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound("a", Color.TRANSPARENT);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            title.setText(result.get(TAG_TASK_TITLE));
            description.setText(result.get(Fragment_Task.TAG_TASK_DES));

            return v;

    }
    
}