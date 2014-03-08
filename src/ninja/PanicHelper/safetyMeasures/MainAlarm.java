package ninja.PanicHelper.safetyMeasures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ninja.PanicHelper.detectors.Accelerometer;
import ninja.PanicHelper.R;

import java.util.ArrayList;

public class MainAlarm extends Activity {
    private static final int DELAY = 100;
    private TextView secondsView,titleView;
    private boolean colorChanged;
    private int secondsRemaining;
    private LinearLayout layout;
    private Handler handler;
    private static MainAlarm alarmInstance = new MainAlarm();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danger_layout);
        layout = (LinearLayout) findViewById(R.id.dangerLayout);
        secondsView = (TextView) findViewById(R.id.seconds_textView);
        titleView = (TextView) findViewById(R.id.danger_title);
        colorChanged = false;
        secondsRemaining = 30;

        layout.setBackgroundResource(R.drawable.danger_background_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.start();

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondsView.setText("seconds remaining: " + millisUntilFinished / 1000);

                if((millisUntilFinished / 1000) == 23 && Accelerometer.fired)
                    voiceRecognitionStart();
            }

            public void onFinish() {
                secondsView.setText("done!");
            }
        }.start();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void voiceRecognitionStart() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(i, 1);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1  && resultCode==RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d("Main", thingsYouSaid.get(0));

            if(thingsYouSaid.get(0).compareTo("stop") == 0)
                finish();

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        Accelerometer.fired = false;
    }



}