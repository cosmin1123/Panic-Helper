package ninja.PanicHelper.adapter;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ninja.PanicHelper.R;

/**
 * Created by Cataaa on 3/5/14.
 */
public class SettingsFragment extends PreferenceFragment {
    public SettingsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.fragment_settings);
    }

}
