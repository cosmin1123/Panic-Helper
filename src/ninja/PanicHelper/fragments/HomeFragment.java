package ninja.PanicHelper.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import ninja.PanicHelper.MainActivity;
import ninja.PanicHelper.R;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.contacts.Contact;
import ninja.PanicHelper.safetyMeasures.Light;
import ninja.PanicHelper.safetyMeasures.MainAlarm;
import ninja.PanicHelper.safetyMeasures.Sound;

/**
 * The class for generating the home fragment view.
 **/
public class HomeFragment extends Fragment {
    public static View fragmentView;
    public static boolean buttonHold;
    public static TextView holdCounterTextView;
    private CountDownTimer timer;
    private long startTime = 0;
    private long stopTime = 0;
    private int holdTime = 0;
    private boolean buttonUp = false;
    private boolean actionDone = false;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        return fragmentView;
    }

    public static View getViewById(int target) {
        return fragmentView.findViewById(target);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialiseContactTable();
        addListeners();
        MainActivity.refreshAccelerationService();
    }

    /* Initialize emergency contact table */
    public void initialiseContactTable() {
        Contact c = Configurations.getFirstFive().get(0);

        ((TextView) fragmentView.findViewById(R.id.textView6)).setText(c.name);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox)).setChecked(c.callContact);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox2)).setChecked(c.sendSms);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox3)).setChecked(c.sendPrivateMessage);
        (fragmentView.findViewById(R.id.checkBox)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox2)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox3)).setEnabled(false);

        c = Configurations.getFirstFive().get(1);
        ((TextView) fragmentView.findViewById(R.id.textView8)).setText(c.name);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox4)).setChecked(c.callContact);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox5)).setChecked(c.sendSms);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox6)).setChecked(c.sendPrivateMessage);
        (fragmentView.findViewById(R.id.checkBox4)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox5)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox6)).setEnabled(false);

        c = Configurations.getFirstFive().get(2);
        ((TextView) fragmentView.findViewById(R.id.textView3)).setText(c.name);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox7)).setChecked(c.callContact);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox8)).setChecked(c.sendSms);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox9)).setChecked(c.sendPrivateMessage);
        (fragmentView.findViewById(R.id.checkBox7)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox8)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox9)).setEnabled(false);

        c = Configurations.getFirstFive().get(3);
        ((TextView) fragmentView.findViewById(R.id.textView10)).setText(c.name);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox10)).setChecked(c.callContact);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox11)).setChecked(c.sendSms);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox12)).setChecked(c.sendPrivateMessage);
        (fragmentView.findViewById(R.id.checkBox10)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox11)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox12)).setEnabled(false);

        c = Configurations.getFirstFive().get(4);
        ((TextView) fragmentView.findViewById(R.id.textView12)).setText(c.name);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox13)).setChecked(c.callContact);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox14)).setChecked(c.sendSms);
        ((CheckBox) fragmentView.findViewById(R.id.checkBox15)).setChecked(c.sendPrivateMessage);
        (fragmentView.findViewById(R.id.checkBox13)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox14)).setEnabled(false);
        (fragmentView.findViewById(R.id.checkBox15)).setEnabled(false);

        /* Updating holding time */
        TextView holdTimeView = (TextView) HomeFragment.getViewById(R.id.textView15);
        holdTimeView.setText("Hold " + Configurations.getButtonHoldTime() + " seconds for instant help");

    }

    /* Button listeners */
    public void addListeners() {

        ImageButton buttonOne = (ImageButton) HomeFragment.getViewById(R.id.help_button);
        holdCounterTextView = (TextView) HomeFragment.getViewById(R.id.holdCounterText);
        buttonOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonHold = false;
                    buttonUp = false;
                    startTime = System.currentTimeMillis();
                    holdTime = Configurations.getButtonHoldTime();
                    holdCounterTextView.setText(holdTime + "");
                    timer = new CountDownTimer(holdTime * 1000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            holdCounterTextView.setText(((int) millisUntilFinished/1000) + "");
                        }
                        @Override
                        public void onFinish() {
                            holdCounterTextView.setText("0");
                            stopTime = System.currentTimeMillis();
                            if ((stopTime - startTime) >= ((long) holdTime * 1000) && !buttonUp) {
                                // held holdTime seconds => start panic measures directly
                                buttonHold = true;
                                onHelp();
                            }
                        }
                    }.start();
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    buttonUp = true;
                    if (!actionDone) {
                        /* Didn't hold holdTime seconds => the usual help measures */
                        onHelp();
                        timer.cancel();
                    }

                    actionDone = false;
                }

                return false;
            }
        });

        Button buttonTwo = (Button) HomeFragment.getViewById(R.id.button);
        buttonTwo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Sound.start(getActivity().getApplicationContext());
            }
        });

        Button buttonThree = (Button) HomeFragment.getViewById(R.id.button2);
        buttonThree.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Light.startWarningLight();
            }
        });

    }

    /* Start panic measures activity */
    public void onHelp() {
        Intent dialogIntent = new Intent(MainActivity.getAppContext(), MainAlarm.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().getApplication().startActivity(dialogIntent);
    }

}
