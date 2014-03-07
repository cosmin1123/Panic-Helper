package safety.measures;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import ninja.PanicHelper.MainActivity;

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

    public static void toggleLed() {
        Log.d("TEST", "TOGGLELED " + cameraOn);
        if(cameraOn)
            ledoff();
        else
            ledon();

    }

    public static void ledon() {

        Log.d("TEST", "HERE");

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
