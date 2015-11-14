package app.sunshine.sakr.com.findyourchild.utility;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by mohammad sakr on 02/05/2015.
 */
public class TrackerApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "mpOnbqpyvODTPBLSfd302pFleihdvhMDU6IVp0DG", "BhtT4MxGylohJ3C5hprM1hdSK9Kcnn2eKarG1Ha9");

        //PushService.setDefaultPushCallback(this, MainActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();


    } // end method onCreate

    public static void updateParseInstallation(ParseUser user)
    {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_OBJECT_ID,user.getObjectId());
        installation.saveInBackground();
    } // end method updateParseInstallation

} // end class TrackerApplication
