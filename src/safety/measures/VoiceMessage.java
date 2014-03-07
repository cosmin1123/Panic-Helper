package safety.measures;

import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import ninja.PanicHelper.MainActivity;
import voice.control.VoiceSay;

/**
 * Created with IntelliJ IDEA.
 * User: cosmin
 * Date: 3/6/14
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class VoiceMessage{
    /*
        How to start:
                Intent d =     VoiceMessage.leaveMessage("0725025119");
                startActivity(d);
     */
    public static Intent leaveMessage(String telephoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+telephoneNumber));

        TelephonyManager telephony = (TelephonyManager) MainActivity.getAppContext().
                getSystemService(MainActivity.getAppContext().TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {

                switch(state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d("STATEOFTHECALLISI", state + "");
                        VoiceSay.stop();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d("STATEOFTHECALLISH", state + "");
                        VoiceSay.defaultHelpMessage();
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("STATEOFTHECALLISR", state + "");
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);


        return  intent;
    }
}
