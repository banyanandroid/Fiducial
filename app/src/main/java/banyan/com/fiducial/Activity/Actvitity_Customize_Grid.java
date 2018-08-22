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
import android.widget.GridLayout;
import android.widget.Toast;

import banyan.com.fiducial.Activity.Actvity_Add_Pre_Enquiry;
import banyan.com.fiducial.R;

public class Actvitity_Customize_Grid extends Activity implements View.OnClickListener {
    GridLayout gridLayout;
    FloatingActionButton fabenquiry;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_grid);
        gridLayout=(GridLayout)findViewById(R.id.mainGrid);
        fabenquiry = (FloatingActionButton)findViewById(R.id.fabgrid_enquiry);
        fabenquiry.setOnClickListener(this);
        setSingleEvent(gridLayout);
    }

    private void setSingleEvent(GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Clicked at index "+ finalI,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fabgrid_enquiry:
                Intent add_preEnquiry = new Intent(getApplicationContext(), Actvity_Add_Pre_Enquiry.class);
                add_preEnquiry.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(add_preEnquiry);
                break;
                default:
                    break;
        }
    }
}
