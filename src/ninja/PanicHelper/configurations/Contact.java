package ninja.PanicHelper.configurations;

import java.io.Serializable;

/**
 * The class that describes a contact  and the methods to contact that person
 * phone, sms or private facebook message.
 */
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;
    public String phoneNumber;
    public String facebookName;

    public Boolean callContact = false;
    public Boolean sendSms= false;
    public Boolean sendPrivateMessage = false;

    public Contact(String name, String phoneNumber, String facebookName, Boolean callContact,
                   Boolean sendSms, Boolean sendPrivateMessage) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.facebookName = facebookName;

        this.callContact = callContact;
        this.sendSms = sendSms;
        this.sendPrivateMessage = sendPrivateMessage;
    }
}
