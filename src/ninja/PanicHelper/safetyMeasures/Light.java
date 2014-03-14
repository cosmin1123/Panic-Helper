package ninja.PanicHelper.safetyMeasures;

import android.hardware.Camera;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: cosmin
 * Date: 3/5/14
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Light {
    static Camera camera = Camera.open();
    private static boolean cameraOn = false;
    private static boolean runninLight = false;
    private static CountDownTimer cm;

    public static void stopWarningLight() {
        runninLight = false;
        if(cm != null)
            cm.cancel();
        ledoff();
    }

    public static void startWarningLight() {
        int secondsRemaining = 1000;
        if(!runninLight)
            runninLight = true;
        else {
            stopWarningLight();
            return;
        }

        cm = new CountDownTimer(secondsRemaining * 1000, 250) {

            public void onTick(long millisUntilFinished) {
                toggleLed();
            }

            public void onFinish() {
                ledoff();
            }
        }.start();
    }

    public static void toggleLed() {
        Log.d("LED", "TOGGLELED " + cameraOn);
        if(cameraOn)
            ledoff();
        else
            ledon();

    }

    public static void ledon() {
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
        cameraOn = true;

    }

    public static void ledoff() {
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        camera.stopPreview();
        cameraOn = false;
    }
}
