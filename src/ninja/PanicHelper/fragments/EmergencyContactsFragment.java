package ninja.PanicHelper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ninja.PanicHelper.contacts.ContactActivity;
import ninja.PanicHelper.R;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.adapter.ListAdapter;

/**
 * The class for generating the emergency contacts fragment view.
 **/
public class EmergencyContactsFragment extends Fragment{
    public EmergencyContactsFragment(){}
    public static EmergencyContactsFragment thisFragment;
    public static ListView list;
    public static Context context;
    public static Button addContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView  = inflater.inflate(R.layout.fragment_emergency_contacts, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        list = (ListView) getView().findViewById(R.id.testListView);
        String[] values = Configurations.getContactNames();
        ListAdapter adapter = new ListAdapter(getView().getContext(),values);
        list.setAdapter(adapter);
        thisFragment = this;

        addContact = ((Button) getView().findViewById(R.id.add_contact_button));
        /* Listener to start activity for creating new contact */
        addContact.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(thisFragment.getView().getContext(), ContactActivity.class);
                        thisFragment.startActivity(intent);
                    }
                }
        );

        context = getView().getContext();
        if(values.length >= 5) {
            addContact.setEnabled(false);
        } else
            addContact.setEnabled(true);

    }

    /* Save configurations on closing the page*/
    @Override
    public void onStop() {
        super.onStop();
        Configurations.save();
    }

}
