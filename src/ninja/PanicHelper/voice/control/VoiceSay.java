package ninja.PanicHelper.voice.control;

import java.util.Locale;


import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import ninja.PanicHelper.MainActivity;
import safety.measures.GPSTracker;


public class VoiceSay implements TextToSpeech.OnInitListener
{
    public TextToSpeech tts;
    private static VoiceSay voice = new VoiceSay();
    private static String currentlyPlaying = null;
    private static String defaultSafetyMessage =
            "Panic alarm will start in 30 seconds, please press on the screen or say stop to cancel.";
    private static String defaultHelpMessage =
            "Help me, I am at: " + GPSTracker.getTwoDigitLatitude() + " " +
                    "latitude and " + GPSTracker.getTwoDigitLongitude() + " longitude";

    public VoiceSay()
    {
        tts = new TextToSpeech(MainActivity.getAppContext(), this);

    }

    @Override
    public void onInit(int status) {
        if(status != TextToSpeech.ERROR){
            tts.setLanguage(Locale.US);
            AudioManager am =
                    (AudioManager) MainActivity.getAppContext().getSystemService(Context.AUDIO_SERVICE);

            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);

            Log.d("Voice: ", "OK");
        }
        else {
            Log.d("Voice: ", "NOT OK");
            Toast.makeText(MainActivity.getAppContext(),
                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
        }

    }


    public static void speakWords(String textHolder){

        voice.tts.speak(textHolder, TextToSpeech.QUEUE_FLUSH, null);
        Log.d("Voice: ", "I said: " + textHolder);
    }

    public static void continouslySpeakWords(String textHolder) {
        String longText = "";
        while ((longText.length() + textHolder.length()) < 4000) {

            longText += textHolder;
        }
        speakWords(longText);
    }

    public static void stop(){
        voice.tts.stop();
        currentlyPlaying = null;
    }

    public static void defaultSafetyScreenMessage() {
        VoiceSay.speakWords(defaultSafetyMessage);
    }

    public static void defaultHelpMessage() {
        VoiceSay.continouslySpeakWords(defaultHelpMessage);
    }
}