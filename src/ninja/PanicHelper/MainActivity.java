package ninja.PanicHelper;

import detectors.Accelerometer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    static Context c;
    public static boolean running = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        c = super.getApplicationContext();

        if(Accelerometer.isAccelerationServiceNull())
            Accelerometer.setAccelerationService(new Intent(this, Accelerometer.class));

        Button buttonOne = (Button) findViewById(R.id.button1);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                toggleService();
            }
        });


    }

    public static Context getAppContext() {
        return c;

    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
    }

    public void toggleService() {
        if(!Accelerometer.isServiceRunning())
            startService(Accelerometer.getAccelerationService());
        else{
            stopService(Accelerometer.getAccelerationService());
        }
    }

}
