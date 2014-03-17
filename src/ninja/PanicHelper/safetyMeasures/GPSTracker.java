package ninja.PanicHelper.safetyMeasures;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import ninja.PanicHelper.MainActivity;
/**
 * The class that gets the current latitude and longitude and also, it can provide
 * a link to google maps with the current latitude and longitude.
 **/
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    /* Flag for GPS status */
    boolean isGPSEnabled = false;
    private static GPSTracker gps = new GPSTracker(MainActivity.getAppContext());

    /* Flag for network status */
    boolean isNetworkEnabled = false;

    /* Flag for GPS status */
    boolean canGetLocation = false;

    Location location; /* Location */
    double latitude; /* Latitude */
    double longitude; /* Longitude */

    /* The minimum distance to change Updates in meters */
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; /* 10 meters */

    /* The minimum time between updates in milliseconds */
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; /* 1 minute */

    /* Declaring a Location Manager */
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            /* Getting GPS status */
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            /* Getting network status */
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                /* No network provider is enabled */
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                /* If GPS Enabled get lat/long using GPS Services */
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public static double getLatitude(){
        if(gps.location != null){
            gps.latitude = gps.location.getLatitude();
        }

        return gps.latitude;
    }

    /**
     * Function to get longitude
     * */
    public static double getLongitude(){
        if(gps.location != null){
            gps.longitude = gps.location.getLongitude();
        }

        return gps.longitude;
    }

    public static double getTwoDigitLatitude(){
        if(gps.location != null){
            gps.latitude = gps.location.getLatitude();
        }

        /* return latitude */
        return Math.floor(gps.latitude * 100) / 100;
    }

    /**
     * Function to get longitude
     * */
    public static double getTwoDigitLongitude(){
        if(gps.location != null){
            gps.longitude = gps.location.getLongitude();
        }

        /* return longitude */
        return Math.floor(gps.longitude * 100) / 100;
    }

    public static String getLocationLink() {
        return  "https://maps.google.ro/maps?q=" + getLatitude() + "," + getLongitude() + "&z=16";
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        /* Setting Dialog Title */
        alertDialog.setTitle("GPS is settings");

        /* Setting Dialog Message */
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        /* On pressing Settings button */
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        /* On pressing cancel button */
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        /* Showing Alert Message */
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}