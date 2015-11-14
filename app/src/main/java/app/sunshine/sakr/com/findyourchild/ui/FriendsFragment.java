package app.sunshine.sakr.com.findyourchild.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

import app.sunshine.sakr.com.findyourchild.R;
import app.sunshine.sakr.com.findyourchild.adapter.UserAdapter;
import app.sunshine.sakr.com.findyourchild.utility.ParseConstants;

/**
 * Created by mohammad sakr on 02/05/2015.
 */
public class FriendsFragment extends Fragment
{
    public static final String TAG = FriendsFragment.class.getSimpleName();

    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected List<ParseUser> mFriends;
    protected GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_friends,container,false);

        mGridView = (GridView)rootView.findViewById(R.id.friendsGrid);
        TextView emptyTextView = (TextView)rootView.findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyTextView);
        return rootView;
    }// end method onCreateView

    @Override
    public void onResume()
    {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        getActivity().setProgressBarIndeterminate(true);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.whereNotEqualTo(ParseConstants.KEY_OBJECT_ID,mCurrentUser.getObjectId());
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e)
            {
                getActivity().setProgressBarIndeterminate(false);

                if(e == null)
                {
                    mFriends = parseUsers;
                    String[] userNames = new String[parseUsers.size()];

                    for(int i = 0; i < parseUsers.size(); i++)
                    {
                        userNames[i] = mFriends.get(i).getUsername();
                    } // end for

                    if(mGridView.getAdapter() == null)
                    {
                        UserAdapter adapter = new UserAdapter(getActivity(), mFriends);
                        mGridView.setAdapter(adapter);
                    } // end if
                    else
                    {
                        ((UserAdapter)mGridView.getAdapter()).refill(mFriends);
                    } // end else

                } // end if

                else
                {
                    Log.e(TAG,e.getMessage());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage());
                    builder.setTitle(R.string.error_title);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } // end else
            } // end method done
        });
    } // end method onResume
} // end class FriendsFragment
