package ninja.PanicHelper.contacts;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import ninja.PanicHelper.R;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.contacts.Contact;

/**
 * The class for adding and editing contacts from the emergency contacts.
 **/
public class ContactActivity extends Activity {

    public static Contact contact;
    public static Integer contactPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }

    @Override
    public void onStart() {
        super.onStart();
        addContactsListeners();

        if(contact != null) {
            ((EditText)findViewById(R.id.editText)).setText(contact.name);
            ((EditText)findViewById(R.id.editText4)).setText(contact.phoneNumber);
            ((EditText)findViewById(R.id.editText2)).setText(contact.facebookName);

            ((Switch)findViewById(R.id.switch1)).setChecked(contact.callContact);
            ((Switch)findViewById(R.id.switch2)).setChecked(contact.sendSms);
            ((Switch)findViewById(R.id.switch3)).setChecked(contact.sendPrivateMessage);

            (findViewById(R.id.switch1)).setSelected(contact.callContact);
            (findViewById(R.id.switch2)).setSelected(contact.sendSms);
            (findViewById(R.id.switch3)).setSelected(contact.sendPrivateMessage);
        }

    }

    public void addContactsListeners() {
        /* Save contact */
        (findViewById(R.id.button)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = ((EditText)findViewById(R.id.editText)).getText().toString();
                        String number = ((EditText)findViewById(R.id.editText4)).getText().toString();
                        String facebookUser = ((EditText)findViewById(R.id.editText2)).getText().toString();

                        Boolean callContact = ((Switch)findViewById(R.id.switch1)).isChecked();
                        Boolean sendSms = ((Switch)findViewById(R.id.switch2)).isChecked();
                        Boolean sendPrivateMessage = ((Switch)findViewById(R.id.switch3)).isChecked();

                        if(contact != null) {
                            Configurations.removeContact(contact.name);
                        }


                        if(contact == null && name.length() != 0 && Configurations.addContact(
                                new Contact(name,number, facebookUser, callContact, sendSms, sendPrivateMessage))) {
                            Configurations.save();
                            finish();

                        } else
                            if(contact != null && name.length() != 0 && Configurations.addContactAt(contactPosition,
                                    new Contact(name,number, facebookUser, callContact, sendSms, sendPrivateMessage))) {

                                Configurations.save();
                                finish();

                            }else
                                Toast.makeText(getApplicationContext(),
                                        "This name already exists", Toast.LENGTH_LONG).show();
                        contact = null;

                    }
                });

        /* Cancel */
        (findViewById(R.id.button2)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        contact = null;
                    }
                });
    }
}
