package ninja.PanicHelper.safetyMeasures;

import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import ninja.PanicHelper.MainActivity;
import ninja.PanicHelper.configurations.Configurations;

/*
The class for calling a list of persons defined by the user in the emergency contacts.
 */
public class VoiceMessage{
    /*
        How to start:
                Intent d =     VoiceMessage.leaveMessage("0725025119");
                startActivity(d);
     */
    static int prevState = -1;
    static boolean callFinished = false;
    static int currentContact = 1;

    public static Intent leaveMessage(String telephoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:"+telephoneNumber));

        callFinished = false;

        TelephonyManager telephony = (TelephonyManager) MainActivity.getAppContext().
                getSystemService(MainActivity.getAppContext().TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {

                switch(state) {
                    case TelephonyManager.CALL_STATE_IDLE: {
                        if(prevState == TelephonyManager.CALL_STATE_OFFHOOK &&
                                currentContact < Configurations.getCallContactTelephoneNumbers().length) {
                            MainAlarm.alarmInstance.startActivity(
                                    VoiceMessage.leaveMessage(Configurations.
                                            getCallContactTelephoneNumbers()[currentContact++]));
                        }
                        prevState = TelephonyManager.CALL_STATE_IDLE;
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        prevState = TelephonyManager.CALL_STATE_OFFHOOK;
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);


        return  intent;
    }
}
