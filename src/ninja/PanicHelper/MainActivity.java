package ninja.PanicHelper;

import android.widget.ImageButton;
import configurations.Configurations;
import detectors.Accelerometer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import safety.measures.MainAlarm;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    static Context c;
    public static boolean running = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        c = super.getApplicationContext();

        if(Accelerometer.isAccelerationServiceNull())
            Accelerometer.setAccelerationService(new Intent(this, Accelerometer.class));

       ImageButton buttonOne = (ImageButton) findViewById(R.id.imageButton);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onHelp();
            }
        });

        /*
        ImageButton buttonTwo = (ImageButton) findViewById(R.id.imageButton2);
        buttonTwo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                toggleService();
            }
        });
        */

    }

    public static Context getAppContext() {
        return c;

    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;

        Configurations.load();
    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
        Configurations.save();
    }

    public void toggleService() {
        if(!Accelerometer.isServiceRunning())
            startService(Accelerometer.getAccelerationService());
        else{
            stopService(Accelerometer.getAccelerationService());
        }
    }

    public void onHelp() {
        Intent dialogIntent = new Intent(MainActivity.getAppContext(), MainAlarm.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);

        MainActivity.running = true;
    }

}
