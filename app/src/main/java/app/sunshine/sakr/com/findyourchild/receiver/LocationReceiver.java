package app.sunshine.sakr.com.findyourchild.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import app.sunshine.sakr.com.findyourchild.utility.ParseConstants;
import app.sunshine.sakr.com.findyourchild.R;
import app.sunshine.sakr.com.findyourchild.utility.TrackerManager;

public class LocationReceiver extends BroadcastReceiver
{
    private ParseRelation<ParseUser> mFriendsRelation;
    private ParseUser mCurrentUser;
    private ArrayList<String> mRecipientIds;
    private List<ParseUser> mFriends;
    private String[] ids;
    private Location mLocation;


    private TrackerManager mTrackerMaanger;
    private Context mContext;
    public LocationReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        mContext = context;
        mTrackerMaanger = TrackerManager.getTrackerManager(context);
        mLocation = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if(mLocation != null)
        {
            onLocationRecieved(mLocation);
        } // end if
        else
        {
            Log.i("Location Receiver", "Location is null");
        }
        mTrackerMaanger.stopLocationUpdate();
    } // end method onReceive

    private void onLocationRecieved(Location location)
    {
        Log.i("location Receiver", "\nlocation: " + "\nLongitude: " + location.getLongitude()
                + "\nLatitude: " + location.getLatitude());
        send();

    } // end method onLocationReceived


    private void sendMessage(final String recipientId)
    {
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_Name,ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECIPIENT_ID,recipientId);
        message.put(ParseConstants.KEY_LOCATION_LONGITUDE, mLocation.getLongitude());
        message.put(ParseConstants.KEY_LOCATION_LATITUDE, mLocation.getLatitude());
        message.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if (e == null)
                {
                    Toast.makeText(mContext, "Location sent", Toast.LENGTH_LONG).show();
                    sendPushNotifications(recipientId);
                } // end if
                else
                {
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(mContext.getString(R.string.error_sending_message));
                    builder.setTitle(mContext.getString(R.string.error_title));
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } // end else

            } // end method done
        });
    } //end method sendMessage

    protected void send()
    {
        mRecipientIds = new ArrayList<String>();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);


        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.whereNotEqualTo(ParseConstants.KEY_OBJECT_ID, mCurrentUser.getObjectId());
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e)
            {
                if (e == null)
                {
                    for (int i = 0; i < parseUsers.size(); i++)
                    {
                        sendMessage(parseUsers.get(i).getObjectId());
                    } // end for

                } // end if

                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(e.getMessage());
                    builder.setTitle(R.string.error_title);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } // end else
            } // end method done
        });
    } // end method send

    private void sendPushNotifications(String recipientId)
    {
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereEqualTo(ParseConstants.KEY_OBJECT_ID,recipientId);

        //send push notification
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(mContext.getString(R.string.push_message,ParseUser.getCurrentUser().getUsername()));
        push.sendInBackground();
    } // end method sendPushNotifications


} // end class LocationReceiver
