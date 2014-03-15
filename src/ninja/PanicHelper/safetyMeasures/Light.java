package ninja.PanicHelper.safetyMeasures;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: cosmin
 * Date: 3/5/14
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Light {
    static Camera camera;
    private static boolean cameraOn = false;
    private static boolean runninLight = false;
    private static CountDownTimer cm;
    private static Camera.Parameters p;

    public static void stopWarningLight() {
        runninLight = false;
        if(cm != null)
            cm.cancel();

        if(camera == null)
            return;
        ledoff();
        camera.stopPreview();
        camera.release();
        camera = null;

    }

    public static void startWarningLight() {
        int secondsRemaining = 1000;

        if(!runninLight) {
            runninLight = true;
            camera = Camera.open();
            p = camera.getParameters();
        }
        else {
            stopWarningLight();
            return;
        }

        try {
            camera.setPreviewTexture(new SurfaceTexture(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

        cm = new CountDownTimer(secondsRemaining * 1000, 75) {

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
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        cameraOn = true;

    }

    public static void ledoff() {
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);

        cameraOn = false;
    }
}
