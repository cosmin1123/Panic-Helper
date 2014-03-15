package ninja.PanicHelper.detectors;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import ninja.PanicHelper.MainActivity;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.safetyMeasures.MainAlarm;
import ninja.PanicHelper.voice.control.VoiceSay;

public class Accelerometer extends Service implements SensorEventListener{
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private static boolean accelerometerRunning = false;
    private static Intent accelerationService = null;
    private static VoiceSay voice = new VoiceSay();
    public static boolean fired = false;

    boolean flag=false;
    public static final String TAG = "Acceleration";
    SensorManager sensorManager;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    public void onCreate()
    {
        flag=true;
        Log.d(TAG + "F", "onCreate");
        super.onCreate();

    }
    public void onDestroy() {
        accelerometerRunning = false;
        sensorManager.unregisterListener(this);
        flag=false;
        Log.d(TAG + "F", "onDestroy");
        stopForeground(false);
        super.onDestroy();

    }
    public void onStart(Intent intent, int startId)
    {
        Log.d(TAG + "F", "onStart");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        startForeground(1, new Notification());
        accelerometerRunning = true;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
    private void getAccelerometer(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            mAccel = Math.abs(mAccel);

            mAccel = mAccel / 9.81f;

            if(Configurations.isAccident(mAccel) ) {
                startSafetyMeasures();
            }
            Log.d(TAG, "Current accel is: " + mAccel);


        }
    }
    public void onSensorChanged(SensorEvent event) {

        getAccelerometer(event);


    }



    public static Boolean isServiceRunning() {
        return accelerometerRunning;
    }

    public static Boolean isAccelerationServiceNull(){
        return accelerationService == null;
    }

    public static void setAccelerationService(Intent intentService) {
        accelerationService = intentService;
    }

    public static Intent getAccelerationService() {
        return accelerationService;
    }

    private void startSafetyMeasures() {
        fired = true;
        if(!MainActivity.running)
            startMainActivity();
        startAlarm();
    }

    private void startAlarm() {
        Intent dialogIntent = new Intent(MainActivity.getAppContext(), MainAlarm.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);
    }

    private void startMainActivity(){
        Intent dialogIntent = new Intent(getBaseContext(), MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);
        MainActivity.running = true;
    }

}