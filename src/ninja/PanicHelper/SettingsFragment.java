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
    public CheckBoxPreference crashPostOnWallBox;
    public CheckBoxPreference crashVoiceRecBox;

    public CheckBoxPreference buttonYellServiceBox;
    public CheckBoxPreference buttonLightServiceBox;
    public CheckBoxPreference buttonPostOnWallBox;
    public CheckBoxPreference buttonVoiceRecBox;

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
        crashPostOnWallBox = (CheckBoxPreference) findPreference("crashWallPost");
        crashVoiceRecBox = (CheckBoxPreference) findPreference("crashVoiceRec");

        buttonYellServiceBox = (CheckBoxPreference) findPreference("buttonYellService");
        buttonLightServiceBox = (CheckBoxPreference) findPreference("buttonLightService");
        buttonPostOnWallBox = (CheckBoxPreference) findPreference("buttonWallPost");
        buttonVoiceRecBox = (CheckBoxPreference) findPreference("buttonVoiceRec");

        crashMessage = (EditTextPreference) findPreference("crashMessage");
        buttonMessage = (EditTextPreference) findPreference("buttonMessage");

        // Loading initial settings:
        crashServiceBox.setChecked(Configurations.isCrashServiceActivated());
        crashYellServiceBox.setChecked(Configurations.isCrashYellService());
        crashLightServiceBox.setChecked(Configurations.isCrashLightService());
        crashPostOnWallBox.setChecked(Configurations.isCrashPostOnWall());
        crashVoiceRecBox.setChecked(Configurations.isCrashVoiceRec());

        buttonYellServiceBox.setChecked(Configurations.isButtonYellService());
        buttonLightServiceBox.setChecked(Configurations.isCrashLightService());
        buttonPostOnWallBox.setChecked(Configurations.isButtonPostOnWall());
        buttonVoiceRecBox.setChecked(Configurations.isButtonVoiceRec());

        crashMessage.setText(Configurations.getCrashMessage());
        buttonMessage.setText(Configurations.getButtonMessage());

    }


    @Override
    public void onStop() {
        super.onStop();

        // Storing new value:
        Configurations.setCrashServiceActivated(crashServiceBox.isChecked());
        Configurations.setCrashYellService(crashYellServiceBox.isChecked());
        Configurations.setCrashLightService(crashLightServiceBox.isChecked());
        Configurations.setCrashPostOnWall(crashPostOnWallBox.isChecked());
        Configurations.setCrashVoiceRec(crashVoiceRecBox.isChecked());

        Configurations.setButtonYellService(buttonYellServiceBox.isChecked());
        Configurations.setButtonLightService(buttonLightServiceBox.isChecked());
        Configurations.setButtonPostOnWall(buttonPostOnWallBox.isChecked());
        Configurations.setButtonVoiceRec(buttonVoiceRecBox.isChecked());

        Configurations.setCrashMessage(crashMessage.getText());
        Configurations.setButtonMessage(buttonMessage.getText());

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

        MainActivity.refreshAccelerationService();
        Log.d("checkConfig", "\nConfig:\n" + Configurations.getButtonHoldTime());

    }
}
