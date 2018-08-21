package banyan.com.fiducial.Global;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by Vijay on 3/9/2018.
 */

public class Toast_Show
{
    // toast display
    public static void displayToast(Context context,String s)
    {
        if(context!=null) {
            Toast t1 = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            t1.setGravity(Gravity.CENTER,0,0);
            t1.show();
        }
    }

    // tasty toast success
    public static void displaySuccessToast(Context context,String s)
    {
        if(context!=null) {
            Toast t1 = TastyToast.makeText(context, s, TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
        }
    }

    // tasty toast error
    public static void displayErrorToast(Context context,String s)
    {
        if(context!=null) {
            Toast t1 = TastyToast.makeText(context, s, TastyToast.LENGTH_SHORT,TastyToast.ERROR);
        }
    }

    // tasty toast warning
    public static void displayWarningToast(Context context,String s)
    {
        if(context!=null) {
            Toast t1 = TastyToast.makeText(context, s, TastyToast.LENGTH_SHORT,TastyToast.WARNING);
        }
    }

}