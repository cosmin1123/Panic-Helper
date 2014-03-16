package ninja.PanicHelper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.adapter.ListAdapter;

/*
The class for generating the emergency contacts fragment view.
 */
public class EmergencyContactsFragment extends Fragment{
    public EmergencyContactsFragment(){}
    public static EmergencyContactsFragment thisC;
    public static ListView list;
    public static Context context;
    public static Button addContact;

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

        addContact = ((Button) getView().findViewById(R.id.add_contact_button));
        //Listener pentru a porni activity add contacts. poti sa il rescrii cum vrei.
        addContact.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(thisC.getView().getContext(), ContactActivity.class);
                        thisC.startActivity(intent);
                    }
                }
        );

        context = getView().getContext();

        if(values.length >= 5) {
            addContact.setEnabled(false);
        } else
            addContact.setEnabled(true);

    }

    @Override
    public void onStop() {
        super.onStop();
        Configurations.save();
    }

}
