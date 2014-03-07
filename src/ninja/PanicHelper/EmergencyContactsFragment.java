package ninja.PanicHelper;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import ninja.PanicHelper.adapter.ListAdapter;

import java.util.List;

/**
 * Created by Cataaa on 3/6/14.
 */
public class EmergencyContactsFragment extends Fragment{
    public EmergencyContactsFragment(){}
    private EmergencyContactsFragment thisC;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView  = inflater.inflate(R.layout.fragment_emergency_contacts, container, false);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        ListView list = (ListView) getView().findViewById(R.id.testListView);
        String[] values = {"HEllo","Hello2"};
        ListAdapter adapter = new ListAdapter(getView().getContext(),values);
        list.setAdapter(adapter);

        thisC = this;

        //Listener pentru a porni activity add contacts. poti sa il rescrii cum vrei.
        ((Button) getView().findViewById(R.id.add_contact_button)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(thisC.getView().getContext(), ContactActivity.class);
                        thisC.startActivity(intent);
                    }
                }
        );
    }
}
