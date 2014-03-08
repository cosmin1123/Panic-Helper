package ninja.PanicHelper.adapter;

/**
 * Created by Cataaa on 3/6/14.
 */
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ninja.PanicHelper.ContactActivity;
import ninja.PanicHelper.EmergencyContactsFragment;
import ninja.PanicHelper.R;
import ninja.PanicHelper.configurations.Configurations;

public class ListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ListAdapter(Context context, String[] values) {
        super(context, R.layout.list_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_adapter, parent, false);
        TextView contactName = (TextView) rowView.findViewById(R.id.contactName);
        TextView contactNr = (TextView) rowView.findViewById(R.id.contactNr);
        contactNr.setText((position + 1) + "");
        contactName.setText(values[position]);

        ImageButton delButton = (ImageButton) rowView.findViewById(R.id.deleteButton);

        final TextView tempContact = contactName;
        delButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = tempContact.getText().toString();

                Configurations.removeContact(name);

                String[] values = Configurations.getContactNames();
                ListAdapter adapter = new ListAdapter(EmergencyContactsFragment.context,values);
                EmergencyContactsFragment.list.setAdapter(adapter);
            }
        });

        ImageButton editButton = (ImageButton) rowView.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = tempContact.getText().toString();
                ContactActivity.contact = Configurations.getContactByName(name);
                ContactActivity.contactPosition = Configurations.getContactPositionByName(name);

                Intent intent = new Intent(EmergencyContactsFragment.thisC.getView().getContext(),
                        ContactActivity.class);
                EmergencyContactsFragment.thisC.startActivity(intent);

            }
        });

        return rowView;
    }
}