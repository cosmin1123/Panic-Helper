package ninja.PanicHelper.voice.control;

import java.util.Locale;


import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import ninja.PanicHelper.MainActivity;


public class VoiceSay implements TextToSpeech.OnInitListener
{
    public TextToSpeech tts;


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

    public void speakWords(String textHolder){


        tts.speak(textHolder, TextToSpeech.QUEUE_FLUSH, null);
        Log.d("Voice: ", "I said: " + textHolder);
    }
}