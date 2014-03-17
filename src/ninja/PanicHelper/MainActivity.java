package ninja.PanicHelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.facebook.Session;
import ninja.PanicHelper.adapter.NavDrawerItem;
import ninja.PanicHelper.adapter.NavDrawerListAdapter;
import ninja.PanicHelper.configurations.Configurations;
import ninja.PanicHelper.detectors.Accelerometer;
import ninja.PanicHelper.fragments.EmergencyContactsFragment;
import ninja.PanicHelper.fragments.FacebookAccountFragment;
import ninja.PanicHelper.fragments.HomeFragment;
import ninja.PanicHelper.fragments.SettingsFragment;
import ninja.PanicHelper.safetyMeasures.Light;
import ninja.PanicHelper.voice.control.VoiceSay;

import java.util.ArrayList;

/**
 * The main activity that creates the side menu, loads and saves the Configurations,
 * initialises the voice to text and the acceleration service
 **/
public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    /* Navigation drawer title */
    private CharSequence mDrawerTitle;
    /* Application Title */
    private CharSequence mTitle;

    /* Slide menu items */
    private String[] navMenuTitles;

    static Context c;
    public static boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mTitle = getTitle();
        mDrawerTitle = getText(R.string.drawer_title);

        /* Load slide menu items */
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        /* Nav drawer icons from resources */
        TypedArray navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
        /* Adding nav drawer items to array */
        /* Home */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        /* Settings */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        /* Emergency Contacts */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        /* Facebook account */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        /* Recycle the typed array */
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        /* Setting the nav drawer list adapter */
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        /* Enabling action bar app icon and behaving it as toggle button */
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, /* Nav menu toggle icon */
                R.string.app_name, /* Nav drawer open */
                R.string.app_name /* Nav drawer close */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            /* On first time display view for Home fragment */
            displayView(0);
        }

        /* Initialise and add listeners */
        initialise();
    }
    /* Override onActivityResult to receive Facebook callback */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    public void initialise() {
        c = super.getApplicationContext();

        if(Accelerometer.isAccelerationServiceNull())
            Accelerometer.setAccelerationService(new Intent(this, Accelerometer.class));

        Configurations.load();
        VoiceSay.speakWords("");
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            /* Display view for selected nav drawer item */
            displayView(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Toggle nav drawer on selecting action bar app icon/title */
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     **/
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     **/
    private void displayView(int position) {
        /* Update the main content by replacing fragments */
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new SettingsFragment();
                break;
            case 2:
                fragment = new EmergencyContactsFragment();
                break;
            case 3:
                fragment = new FacebookAccountFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            /* Update selected item and title, then close the drawer */
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /* Sync the toggle state after onRestoreInstanceState has occurred. */
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /* Pass any configuration change to the drawer toggles */
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public static Context getAppContext() {
        return c;
    }

    /* Load configurations on start */
    @Override
    public void onStart() {
        super.onStart();
        running = true;
        Configurations.load();
    }


    public static void refreshAccelerationService() {

        if(Configurations.isCrashServiceActivated() && !Accelerometer.isServiceRunning())
            MainActivity.getAppContext().startService(Accelerometer.getAccelerationService());

        if(!Configurations.isCrashServiceActivated() && Accelerometer.isServiceRunning())
            MainActivity.getAppContext().stopService(Accelerometer.getAccelerationService());
    }

    /* Save configurations on closing the page*/
    @Override
    public void onStop() {
        super.onStop();
        running = false;
        Configurations.save();
        Light.stopWarningLight();
    }

}
