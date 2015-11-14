package app.sunshine.sakr.com.findyourchild.ui;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import app.sunshine.sakr.com.findyourchild.adapter.UserAdapter;
import app.sunshine.sakr.com.findyourchild.utility.ParseConstants;
import app.sunshine.sakr.com.findyourchild.R;


public class EditFriendsActivity extends ActionBarActivity
{
    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    protected List<ParseUser> mUsers;
    protected GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);

        mGridView = (GridView)findViewById(R.id.friendsGrid);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mGridView.setOnItemClickListener(mOnItemClickListener);
        TextView emptyTextView = (TextView)findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyTextView);
    } // end method onCreate

    @Override
    protected void onResume()
    {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        setSupportProgressBarIndeterminate(true);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo(ParseConstants.KEY_OBJECT_ID, mCurrentUser.getObjectId());
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e)
            {
                setSupportProgressBarIndeterminate(false);
                if (e == null)
                {
                    //success
                    mUsers = parseUsers;
                    String[] userNames = new String[mUsers.size()];
                    for (int i = 0; i < userNames.length; i++)
                    {
                        userNames[i] = mUsers.get(i).getUsername();
                    }// end for

                    if(mGridView.getAdapter() == null)
                    {
                        UserAdapter adapter = new UserAdapter(EditFriendsActivity.this, mUsers);
                        mGridView.setAdapter(adapter);
                    } // end if
                    else
                    {
                        ((UserAdapter)mGridView.getAdapter()).refill(mUsers);
                    } // end else

                    addFriendsCheckmarks();
                } // end if

                else
                {
                    Log.e(TAG,e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage());
                    builder.setTitle(R.string.error_title);
                    builder.setPositiveButton(android.R.string.ok,null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } // end else
            }
        });
    } // end method onResume

    private void addFriendsCheckmarks()
    {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e)
            {
                if (e == null)
                {
                    //list returned - look for match
                    for (int i = 0; i < parseUsers.size(); i++)
                    {
                        ParseUser user = mUsers.get(i);

                        for (ParseUser friend : parseUsers)
                        {
                            if (friend.getObjectId().equals(user.getObjectId()))
                            {
                                mGridView.setItemChecked(i, true);
                            }
                        }
                    } // end for

                } //end if
                else
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    } // end method addFriendsCheckMarks

    protected AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            ImageView checkedImage = (ImageView)view.findViewById(R.id.checkedImageView);

            if (mGridView.isItemChecked(position))
            {
                //add friend
                mFriendsRelation.add(mUsers.get(position));
                checkedImage.setVisibility(View.VISIBLE);
            } else
            {
                //remove friend
                mFriendsRelation.remove(mUsers.get(position));
                checkedImage.setVisibility(View.INVISIBLE);

            }
            mCurrentUser.saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    if (e != null)
                    {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        } //end method onItemClick
    };

} // end class EditFriendsActivity
