package ninja.PanicHelper;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import configurations.Configurations;
import configurations.Contact;

/**
 * Created by Cataaa on 3/4/14.
 */
public class HomeFragment extends Fragment {
    public static View fragmentView;
    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        //initialiseContactTable();
        return fragmentView;
    }

    public static View getViewById(int target) {
        return fragmentView.findViewById(target);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialiseContactTable();
    }

    public void initialiseContactTable() {
        Contact c = Configurations.getFirstFive().get(0);
        Log.d("TESTT", c.name)    ;
        ((TextView)fragmentView.findViewById(R.id.textView6)).setText(c.name);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox)).setChecked(c.callContact);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox2)).setChecked(c.sendSms);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox3)).setChecked(c.sendPrivateMessage);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox2)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox3)).setEnabled(false);

        c = Configurations.getFirstFive().get(1);
        ((TextView)fragmentView.findViewById(R.id.textView8)).setText(c.name);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox4)).setChecked(c.callContact);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox5)).setChecked(c.sendSms);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox6)).setChecked(c.sendPrivateMessage);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox4)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox5)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox6)).setEnabled(false);

        c = Configurations.getFirstFive().get(2);
        ((TextView)fragmentView.findViewById(R.id.textView3)).setText(c.name);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox7)).setChecked(c.callContact);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox8)).setChecked(c.sendSms);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox9)).setChecked(c.sendPrivateMessage);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox7)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox8)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox9)).setEnabled(false);

        c = Configurations.getFirstFive().get(3);
        ((TextView)fragmentView.findViewById(R.id.textView10)).setText(c.name);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox10)).setChecked(c.callContact);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox11)).setChecked(c.sendSms);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox12)).setChecked(c.sendPrivateMessage);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox10)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox11)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox12)).setEnabled(false);

        c = Configurations.getFirstFive().get(4);
        ((TextView)fragmentView.findViewById(R.id.textView12)).setText(c.name);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox13)).setChecked(c.callContact);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox14)).setChecked(c.sendSms);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox15)).setChecked(c.sendPrivateMessage);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox13)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox14)).setEnabled(false);
        ((CheckBox)fragmentView.findViewById(R.id.checkBox15)).setEnabled(false);

    }



}
