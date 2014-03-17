package ninja.PanicHelper.safetyMeasures;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import ninja.PanicHelper.MainActivity;
import ninja.PanicHelper.R;

/*
 * The class for starting a predefined sound.
 * The yell service
 **/
public class Sound {
    private static MediaPlayer mp;
    private static boolean running = false;
    /* Play sound */
    public static void start(Context context) {
        AudioManager audioManager = (AudioManager) MainActivity.getAppContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        if (!running) {
            mp = MediaPlayer.create(context, R.raw.siren_alarm);
            mp.start();
            running = true;
        } else {
            if (mp != null) {
                mp.stop();
            }
            running = false;
        }
    }
    /* Stop sound */
    public static void stop(){
        if (mp != null) {
            mp.stop();
        }
        running = false;
    }
}
