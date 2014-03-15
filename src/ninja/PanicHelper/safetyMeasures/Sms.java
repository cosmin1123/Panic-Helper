package ninja.PanicHelper.safetyMeasures;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import ninja.PanicHelper.MainActivity;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.detectors.Accelerometer;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: cosmin
 * Date: 3/14/14
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sms extends Activity{
    static int currentSms = 1;

    public static void sendSMS(String phoneNumber)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(MainAlarm.alarmInstance, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(MainAlarm.alarmInstance, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        MainAlarm.alarmInstance.getApplication().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                System.out.println("Sending sms number " + currentSms);
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        if(currentSms >= Configurations.getSmsContactTelephoneNumbers().length) {
                            return;
                        }
                        Sms.sendSMS(Configurations.getSmsContactTelephoneNumbers()[currentSms++]);
                        Toast.makeText(MainAlarm.alarmInstance.getBaseContext(), "Sent Sms #" + currentSms ,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainAlarm.alarmInstance.getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainAlarm.alarmInstance.getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainAlarm.alarmInstance.getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainAlarm.alarmInstance.getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        MainAlarm.alarmInstance.getApplication().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:


                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainAlarm.alarmInstance.getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        Log.d("EXCEPTIONsdasdasdasd", Configurations.getButtonMessage());
        SmsManager sms = SmsManager.getDefault();

        if(Accelerometer.fired)
            sms.sendTextMessage(phoneNumber, null, Configurations.getCrashMessage(), sentPI, deliveredPI);
        else
            sms.sendTextMessage(phoneNumber, null, Configurations.getButtonMessage(), sentPI, deliveredPI);


    }
}
