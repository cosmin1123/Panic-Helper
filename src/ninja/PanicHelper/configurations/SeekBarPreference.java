package ninja.PanicHelper.configurations;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import ninja.PanicHelper.R;

import java.util.HashMap;

/**
 * The class for getting and modifying the information from the seek bars.
 **/
public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {

    private static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";
    private static final String ROBOBUNNYNS = "http://robobunny.com";
    private static final int DEFAULT_VALUE = 50;

    private int mMaxValue = 100;
    private int mMinValue = 0;
    private int mInterval = 1;
    private int mCurrentValue;
    private String mUnitsLeft = "";
    private String mUnitsRight = "";
    private String key = "";
    private SeekBar mSeekBar;

    private TextView mStatusText;

    private static HashMap<String, Integer> seekBarHM = new HashMap<String, Integer>();

    public static HashMap<String, Integer> getSeekBarHM() {
        return seekBarHM;
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPreference(context, attrs);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPreference(context, attrs);
    }

    private void initPreference(Context context, AttributeSet attrs) {
        setValuesFromXml(attrs);
        mSeekBar = new SeekBar(context, attrs);
        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void setValuesFromXml(AttributeSet attrs) {

        /* Retrieving key */
        key = getAttributeStringValue(attrs, ANDROIDNS, "key", "");

        mMaxValue = attrs.getAttributeIntValue(ANDROIDNS, "max", 100);
        mMinValue = attrs.getAttributeIntValue(ROBOBUNNYNS, "min", 0);

        mUnitsLeft = getAttributeStringValue(attrs, ROBOBUNNYNS, "unitsLeft", "");
        String units = getAttributeStringValue(attrs, ROBOBUNNYNS, "units", "");
        mUnitsRight = getAttributeStringValue(attrs, ROBOBUNNYNS, "unitsRight", units);

        try {
            String newInterval = attrs.getAttributeValue(ROBOBUNNYNS, "interval");
            if (newInterval != null)
                mInterval = Integer.parseInt(newInterval);
        } catch (Exception e) {
        }

    }

    private String getAttributeStringValue(AttributeSet attrs, String namespace, String name, String defaultValue) {
        String value = attrs.getAttributeValue(namespace, name);
        if (value == null)
            value = defaultValue;

        return value;
    }

    public SeekBar getmSeekBar() {
        return mSeekBar;
    }

    public void setmSeekBar(SeekBar mSeekBar) {
        this.mSeekBar = mSeekBar;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        RelativeLayout layout = null;
        try {
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            layout = (RelativeLayout) mInflater.inflate(R.layout.seek_bar_preference, parent, false);
        } catch (Exception e) {
        }
        return layout;

    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);

        try {
            /* Move our seekbar to the new view we've been given */
            ViewParent oldContainer = mSeekBar.getParent();
            ViewGroup newContainer = (ViewGroup) view.findViewById(R.id.seekBarPrefBarContainer);

            if (oldContainer != newContainer) {
                /* Remove the seekbar from the old view */
                if (oldContainer != null) {
                    ((ViewGroup) oldContainer).removeView(mSeekBar);
                }
                /* Remove the existing seekbar (there may not be one) and add ours */
                newContainer.removeAllViews();
                newContainer.addView(mSeekBar, ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } catch (Exception ex) {
        }

        updateView(view);
    }

    /**
     * Update a SeekBarPreference view with our current state
     * @param view
     */
    protected void updateView(View view) {

        try {
            RelativeLayout layout = (RelativeLayout) view;
            mStatusText = (TextView) layout.findViewById(R.id.seekBarPrefValue);
            mStatusText.setText(String.valueOf(mCurrentValue));
            mStatusText.setMinimumWidth(30);

            mSeekBar.setProgress(mCurrentValue - mMinValue);

            TextView unitsRight = (TextView) layout.findViewById(R.id.seekBarPrefUnitsRight);
            unitsRight.setText(mUnitsRight);

            TextView unitsLeft = (TextView) layout.findViewById(R.id.seekBarPrefUnitsLeft);
            unitsLeft.setText(mUnitsLeft);
        } catch (Exception e) {
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int newValue = progress + mMinValue;

        if (newValue > mMaxValue)
            newValue = mMaxValue;
        else if (newValue < mMinValue)
            newValue = mMinValue;
        else if (mInterval != 1 && newValue % mInterval != 0)
            newValue = Math.round(((float) newValue) / mInterval) * mInterval;

        /* Change rejected, revert to the previous value */
        if (!callChangeListener(newValue)) {
            seekBar.setProgress(mCurrentValue - mMinValue);
            return;
        }

        /* Change accepted, store it */
        mCurrentValue = newValue;

        /* Saving data in a static hashmap */
        seekBarHM.put(key, mCurrentValue);

        mStatusText.setText(String.valueOf(newValue));
        persistInt(newValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        notifyChanged();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray ta, int index) {
        int defaultValue = ta.getInt(index, DEFAULT_VALUE);
        return defaultValue;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            if (key != null) {
                if (key.compareToIgnoreCase("crashWaitingTimeBar") == 0) {
                    mCurrentValue = Configurations.getCrashWaitingTime();
                }
                if (key.compareToIgnoreCase("impactSensitivityBar") == 0) {
                    mCurrentValue = Configurations.getImpactSpeed();
                }
                if (key.compareToIgnoreCase("buttonWaitingTimeBar") == 0) {
                    mCurrentValue = Configurations.getButtonWaitingTime();
                }
                if (key.compareToIgnoreCase("holdTimeBar") == 0) {
                    mCurrentValue = Configurations.getButtonHoldTime();
                }
            }
        } else {
            int temp = 0;
            try {
                temp = (Integer) defaultValue;
            } catch (Exception ex) {
            }

            persistInt(temp);
            mCurrentValue = temp;
        }
    }
}