package configurations;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cosmin
 * Date: 3/7/14
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
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
