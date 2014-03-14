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
import com.facebook.*;
import ninja.PanicHelper.MainActivity;
import ninja.PanicHelper.detectors.Accelerometer;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.R;
import org.json.JSONException;
import org.json.JSONObject;
import ninja.PanicHelper.voice.control.VoiceSay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainAlarm extends Activity {
    private static final int DELAY = 100;
    private TextView secondsView,titleView;
    private boolean colorChanged;
    private int secondsRemaining;
    private LinearLayout layout;
    private Handler handler;
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



        alarmTimer = new CountDownTimer(secondsRemaining * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondsView.setText("seconds remaining: " + millisUntilFinished / 1000);

                if( (secondsRemaining -  (millisUntilFinished / 1000)) >= 5 && !listened) {
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

        Log.d("ActivityResult", requestCode + " " + resultCode);
        if (requestCode == 1  && resultCode == RESULT_OK) {
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

        finishActivity(1);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VoiceSay.stop();
        alarmTimer.cancel();
        Light.stopWarningLight();
    }

    private void postToWall() throws JSONException {
        System.out.println("Checking session.");
        Session session = Session.getActiveSession();

        if(session==null){
            // try to restore from cache
            session = Session.openActiveSessionFromCache(this);
        }
        if (session != null){

            // Check for publish permissions
            List<String> permissions = session.getPermissions();
            List<String> PERMISSIONS = Arrays.asList("publish_actions");
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                boolean pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }

            Bundle postParams = new Bundle();
            postParams.putString("name", "Facebook SDK");
            postParams.putString("caption", "Build great social apps and get more installs.");
            postParams.putString("description", "Help me, I am in danger");
            postParams.putString("link", GPSTracker.getLocationLink());
            postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", "SELF");
            postParams.putString("privacy",  jsonObject.toString());

            System.out.println("Starting.!");

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response
                            .getGraphObject()
                            .getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {

                    }

                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                postId,
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
            System.out.println("DONE POST WALL>!");
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

    public void startPanicMeasures() {

        // start calling
        if(Configurations.getCallContactTelephoneNumbers().length >= 1) {
            startActivityForResult(
                VoiceMessage.leaveMessage(Configurations.getCallContactTelephoneNumbers()[0]), 2);
        }
        // check if ligh service is activated
        if(Configurations.isButtonLightService() && !Accelerometer.fired)
            Light.startWarningLight();

        if(Configurations.isCrashLightService() && Accelerometer.fired)
            Light.startWarningLight();

        // check if yell service is activated
        if(Configurations.isButtonYellService() && !Accelerometer.fired)
            Sound.start(MainActivity.getAppContext());


        if(Configurations.isButtonYellService() && !Accelerometer.fired)
            Sound.start(MainActivity.getAppContext());

    }
}