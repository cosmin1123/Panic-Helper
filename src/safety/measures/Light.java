package safety.measures;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import ninja.PanicHelper.MainActivity;

/**
 * Created with IntelliJ IDEA.
 * User: cosmin
 * Date: 3/5/14
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Light {
    static Camera cam;
    public static void ledon() {
        if(!MainActivity.getAppContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            return;

        cam = Camera.open();
        Camera.Parameters params = cam.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        cam.setParameters(params);
        cam.startPreview();

    }

    public static void ledoff() {
        cam.stopPreview();
        cam.release();
    }
}
