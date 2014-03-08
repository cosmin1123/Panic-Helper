package ninja.PanicHelper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import configurations.Configurations;
import ninja.PanicHelper.adapter.ListAdapter;

/**
 * Created by Cataaa on 3/6/14.
 */
public class EmergencyContactsFragment extends Fragment{
    public EmergencyContactsFragment(){}
    public static EmergencyContactsFragment thisC;
    public static ListView list;
    public static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView  = inflater.inflate(R.layout.fragment_emergency_contacts, container, false);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        list = (ListView) getView().findViewById(R.id.testListView);
        String[] values = Configurations.getContactNames();
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

        context = getView().getContext();
    }

}
