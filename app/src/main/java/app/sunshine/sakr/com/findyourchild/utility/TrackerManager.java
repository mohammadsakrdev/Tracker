package app.sunshine.sakr.com.findyourchild.utility;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by mohammad sakr on 21/03/2015.
 */
public class TrackerManager
{
    public static final String ACTION_LOCATION = "com.findyourchild.trackermanager.ACTION_LOCATION";
    private static TrackerManager mTrackerManager;
    private Context mAppContext;
    private LocationManager mLocationManager;

    //private constructor forces users to use TrackerManager.getTrackerManager(context)
    private TrackerManager( Context appContext)
    {
        mAppContext = appContext;
        mLocationManager = (LocationManager)appContext.getSystemService(Context.LOCATION_SERVICE);
    } // end private constructor

    public static TrackerManager getTrackerManager(Context context)
    {
        if(mTrackerManager == null)
        {
            mTrackerManager = new TrackerManager(context.getApplicationContext());
        } // end if
        return mTrackerManager;
    } //end method getTrackerManager

    //create broad cast when location updates happens
    private PendingIntent getLocationPendingIntent(boolean shoulCreate)
    {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shoulCreate ? 0 :PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext,0,broadcast,flags);
    } // end getLocationPendingIntent

    public void startLocationUpdate()
    {
        String provider = mLocationManager.GPS_PROVIDER;

        //start update from location manager
        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(provider,0,0,pi);
    } //end method startLocationUpdate

    public void stopLocationUpdate()
    {
        PendingIntent pi = getLocationPendingIntent(false);
        if(pi != null)
        {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        } // end if
    } // end method stopLocationUpdate

    public boolean isTrackingRun()
    {
        return getLocationPendingIntent(false) != null;
    } // end method isTrackingRun

} // end class TrackerManager
