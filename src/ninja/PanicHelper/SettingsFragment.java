package ninja.PanicHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.facebook.Settings;
import ninja.PanicHelper.R;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.configurations.SeekBarPreference;

import java.util.HashMap;

/**
 * Created by Cataaa on 3/5/14.
 */
public class SettingsFragment extends PreferenceFragment {
    public SettingsFragment() {
    }

    public PreferenceCategory crashPref;
    public CheckBoxPreference crashServiceBox;
    public CheckBoxPreference crashYellServiceBox;
    public CheckBoxPreference crashLightServiceBox;
    public CheckBoxPreference buttonYellServiceBox;
    public CheckBoxPreference buttonLightServiceBox;
    public CheckBoxPreference sendGPSBox;

    public EditTextPreference crashMessage;
    public EditTextPreference buttonMessage;
    public HashMap<String, Integer> seekBarHM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.fragment_settings);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadSettings();
    }

    public void loadSettings() {

        // Finding preferences:
        crashPref = (PreferenceCategory) findPreference("crashPref");

        crashServiceBox = (CheckBoxPreference) findPreference("crashService");
        crashYellServiceBox = (CheckBoxPreference) findPreference("crashYellService");
        crashLightServiceBox = (CheckBoxPreference) findPreference("crashLightService");
        buttonYellServiceBox = (CheckBoxPreference) findPreference("buttonYellService");
        buttonLightServiceBox = (CheckBoxPreference) findPreference("buttonLightService");
        sendGPSBox = (CheckBoxPreference) findPreference("gpsService");

        crashMessage = (EditTextPreference) findPreference("crashMessage");
        buttonMessage = (EditTextPreference) findPreference("buttonMessage");

        // Loading initial settings:
        crashServiceBox.setChecked(Configurations.isCrashServiceActivated());
        if (!crashServiceBox.isChecked()) {
            // Disabling all other fields in the current preference:
            Log.d("checkBoxDebug", "\nCrash Service: " + Configurations.isCrashServiceActivated());
            crashPref.setEnabled(false);
            crashServiceBox.setEnabled(true);
        }

        crashYellServiceBox.setChecked(Configurations.isCrashYellService());
        crashLightServiceBox.setChecked(Configurations.isCrashLightService());
        buttonYellServiceBox.setChecked(Configurations.isButtonYellService());
        buttonLightServiceBox.setChecked(Configurations.isCrashLightService());
        sendGPSBox.setChecked(Configurations.isSendGPS());

        crashMessage.setText(Configurations.getCrashMessage());
        buttonMessage.setText(Configurations.getButtonMessage());

        // Dealing with listeners:
        crashServiceBox.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {

                // Disabling all other fields in the current preference:
                crashPref.setEnabled((Boolean) newValue == true);
                crashServiceBox.setEnabled(true);
                return true;
            }
        });

        sendGPSBox.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {

                // Updating message fields:
                Log.d("testgps", "updating ");
                Configurations.setSendGPS((Boolean) newValue == true);
                crashMessage.setText(Configurations.getCrashMessage());
                buttonMessage.setText(Configurations.getButtonMessage());
                return true;
            }
        });
    }

    public static String trimMessage(String s) {
        if (s.indexOf(Configurations.getMyLocationPattern()) == -1) {
            return s;
        }
        return s.substring(0, s.indexOf(Configurations.getMyLocationPattern()));
    }

    @Override
    public void onStop() {
        super.onStop();

        // Storing new value:
        Configurations.setCrashServiceActivated(crashServiceBox.isChecked());
        Configurations.setCrashYellService(crashYellServiceBox.isChecked());
        Configurations.setCrashLightService(crashLightServiceBox.isChecked());
        Configurations.setButtonYellService(buttonYellServiceBox.isChecked());
        Configurations.setButtonLightService(buttonLightServiceBox.isChecked());
        Configurations.setSendGPS(sendGPSBox.isChecked());

        Configurations.setCrashMessage(trimMessage(crashMessage.getText()));
        Configurations.setButtonMessage(trimMessage(buttonMessage.getText()));

        seekBarHM = new HashMap<String, Integer>();
        seekBarHM = SeekBarPreference.getSeekBarHM();

        if (seekBarHM.containsKey("crashWaitingTimeBar")) {
            Configurations.setCrashWaitingTime(seekBarHM.get("crashWaitingTimeBar"));
        }
        if (seekBarHM.containsKey("impactSensitivityBar")) {
            Configurations.setImpactSpeed(seekBarHM.get("impactSensitivityBar"));
        }
        if (seekBarHM.containsKey("buttonWaitingTimeBar")) {
            Configurations.setButtonWaitingTime(seekBarHM.get("buttonWaitingTimeBar"));
        }
        if (seekBarHM.containsKey("holdTimeBar")) {
            Configurations.setButtonHoldTime(seekBarHM.get("holdTimeBar"));
        }

        seekBarHM.clear();

        Log.d("checkConfig", "\nConfig:\n" + Configurations.getButtonHoldTime());

    }
}
