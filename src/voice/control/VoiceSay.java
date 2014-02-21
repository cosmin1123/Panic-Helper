package voice.control;

import java.util.Locale;


import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;


public class VoiceSay implements TextToSpeech.OnInitListener
{
    public TextToSpeech tts;

    public VoiceSay(Context c)
    {
        tts = new TextToSpeech(c, this);
    }

    @Override
    public void onInit(int status) {
        if(status != TextToSpeech.ERROR){
            tts.setLanguage(Locale.US);
        }
    }

    public void speakWords(String textHolder){
        tts.speak(textHolder, TextToSpeech.QUEUE_FLUSH, null);
        Log.d("Voice: ", "I just said " + textHolder);
    }
}