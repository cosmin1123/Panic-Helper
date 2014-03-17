package ninja.PanicHelper.safetyMeasures;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.CountDownTimer;

import java.io.IOException;

/**
 * The class for starting the light service.
 * It works by starting a count down timer and turning the camera light on and off at a given interval.
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
