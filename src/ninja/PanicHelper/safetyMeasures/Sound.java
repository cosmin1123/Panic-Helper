package ninja.PanicHelper.safetyMeasures;

import android.content.Context;
import android.media.MediaPlayer;
import ninja.PanicHelper.R;

/**
 * Created by Buga on 3/11/14.
 */
public class Sound {

    private static MediaPlayer mp;
    private static boolean running = false;

    public static void start(Context context) {
     /*   if (!running) {
            mp = MediaPlayer.create(context, R.raw.siren_alarm);
            mp.start();
            running = true;
        } else {
            mp.stop();
            running = false;
        }*/
    }
}
