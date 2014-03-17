package ninja.PanicHelper.safetyMeasures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.*;
import ninja.PanicHelper.fragments.HomeFragment;
import ninja.PanicHelper.MainActivity;
import ninja.PanicHelper.R;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.detectors.Accelerometer;
import ninja.PanicHelper.voiceControl.VoiceSay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *  The activity that represents the Main Alarm when the Help Button has been pressed
 *  or when a car accident has been detected.
 **/

public class MainAlarm extends Activity {
    private TextView secondsView,titleView;
    private boolean colorChanged;
    private int secondsRemaining;
    private LinearLayout layout;
    public static MainAlarm alarmInstance;
    boolean said = false;
    boolean listened = false;
    CountDownTimer alarmTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danger_layout);
        layout = (LinearLayout) findViewById(R.id.dangerLayout);
        secondsView = (TextView) findViewById(R.id.seconds_textView);
        titleView = (TextView) findViewById(R.id.danger_title);
        colorChanged = false;
        secondsRemaining = 30;
        alarmInstance = this;

        layout.setBackgroundResource(R.drawable.danger_background_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.start();

        if(Accelerometer.fired) {
            secondsRemaining = Configurations.getCrashWaitingTime();
        } else
            secondsRemaining = Configurations.getButtonWaitingTime();

        if(HomeFragment.buttonHold)
            secondsRemaining = 1;


        alarmTimer = new CountDownTimer(secondsRemaining * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                secondsView.setText("seconds remaining: " + millisUntilFinished / 1000);

                if( (secondsRemaining -  (millisUntilFinished / 1000)) >= 7 && !listened &&
                        ((Configurations.isCrashVoiceRec() &&  Accelerometer.fired) ||
                        (!Accelerometer.fired && Configurations.isButtonVoiceRec()))) {
                    voiceRecognitionStart();
                    listened = true;
                }

                if(!said) {
                    said = true;
                    VoiceSay.defaultSafetyScreenMessage();
                }
            }

            public void onFinish() {
                secondsView.setText("Applying safety measures");
                if (Configurations.isButtonVoiceRec() || Configurations.isCrashVoiceRec()){
                    finishActivity(1);
                }
                vibratePhone();
                startPanicMeasures();

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
        Intent voiceRecognition = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceRecognition.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(voiceRecognition, 1);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1  && resultCode == RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(thingsYouSaid.get(0).contains("stop")){
                finish();
            }
            else{
                finishActivity(1);
                voiceRecognitionStart();
            }
        }
        else{
            finishActivity(1);
            voiceRecognitionStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Accelerometer.fired = false;
        finishActivity(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VoiceSay.stop();
        alarmTimer.cancel();
        Light.stopWarningLight();
        Sound.stop();
        HomeFragment.holdCounterTextView.setText("");
    }

    public void startPanicMeasures() {
        vibratePhone();

        /* Check if light service is activated */
        if(Configurations.isButtonLightService() && !Accelerometer.fired) {
            Light.startWarningLight();
        }

        if(Configurations.isCrashLightService() && Accelerometer.fired) {
            Light.startWarningLight();
        }

        /* Check if yell service is activated */
        if(Configurations.isCrashYellService() && Accelerometer.fired) {
            Sound.start(MainActivity.getAppContext());
        }

        if(Configurations.isButtonYellService() && !Accelerometer.fired) {
            Sound.start(MainActivity.getAppContext());
        }

        /* Post to Facebook wall */
        if (Configurations.isButtonPostOnWall() && !Accelerometer.fired){
            postToWall();
        }

        if (Configurations.isCrashPostOnWall() && Accelerometer.fired){
            postToWall();
        }

        /* Send Facebook Private Messages */
        if (Configurations.getContactFbUserNames().length > 0){
            sendFbMessages(Configurations.getContactFbUserNames());
        }

        /* Send sms */
        if(Configurations.getSmsContactTelephoneNumbers().length >= 1) {
            Sms.sendSMS(Configurations.getSmsContactTelephoneNumbers()[0]);
        }

        /* Start calling */
        if(Configurations.getCallContactTelephoneNumbers().length >= 1) {
            startActivityForResult(
                    VoiceMessage.leaveMessage(Configurations.getCallContactTelephoneNumbers()[0]), 2);
        }
    }

    private void sendFbMessages(String[] userNames){
        if (Configurations.isLoggedIn()){
            Session session = Session.getActiveSession();
            if(session==null){
                /* Try to restore from cache */
                session = Session.openActiveSessionFromCache(this);
                if (session == null){
                    return;
                }
            }
            if (session != null){
                String accessToken = session.getAccessToken();
                FacebookChatAPI chatAPI = new FacebookChatAPI(accessToken);
                    if (Accelerometer.fired){
                        chatAPI.sendMessage(userNames,Configurations.getCrashMessage());
                    }
                    else{
                        chatAPI.sendMessage(userNames,Configurations.getButtonMessage());
                    }
            }
        }
    }

    private void postToWall() {
        if (Configurations.isLoggedIn()){
            Session session = Session.getActiveSession();
            if(session==null){
                /* Try to restore from cache */
                session = Session.openActiveSessionFromCache(this);
                if (session == null){
                    return;
                }
            }
            if (session != null){
                /* Check for publish permissions */
                List<String> permissions = session.getPermissions();
                List<String> PERMISSIONS = Arrays.asList("publish_actions");
                if (!isSubsetOf(PERMISSIONS, permissions)) {
                    Session.NewPermissionsRequest newPermissionsRequest = new Session
                            .NewPermissionsRequest(this, PERMISSIONS);
                    session.requestNewPublishPermissions(newPermissionsRequest);
                    return;
                }

                Bundle postParams = new Bundle();
                postParams.putString("name", Configurations.getFacebookName() + " is in DANGER!");
                postParams.putString("caption", "Click on this link to see his position on the map.");
                if (Accelerometer.fired){
                    postParams.putString("description", Configurations.getCrashMessage());
                }
                else{
                    postParams.putString("description", Configurations.getButtonMessage());
                }
                postParams.putString("link", GPSTracker.getLocationLink());
                postParams.putString("picture", "https://awareofyourcare.com/blog/wp-content/uploads/2011/03/Help-Button.jpg");

                Request request = new Request(session, "me/feed", postParams,
                        HttpMethod.POST);

                RequestAsyncTask task = new RequestAsyncTask(request);
                task.execute();
            }
        }

    }

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

    private void vibratePhone(){
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(2000);
    }
}