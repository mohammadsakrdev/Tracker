package app.sunshine.sakr.com.findyourchild.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import app.sunshine.sakr.com.findyourchild.adapter.MessagesAdapter;
import app.sunshine.sakr.com.findyourchild.utility.ParseConstants;
import app.sunshine.sakr.com.findyourchild.R;

/**
 * Created by mohammad sakr on 02/05/2015.
 */
public class InboxFragment extends ListFragment
{
    protected List<ParseObject> mMessages;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_inbox,container,false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.swipeRefresh1,
                R.color.swipeRefresh2,
                R.color.swipeRefresh3,
                R.color.swipeRefresh4);
        return rootView;
    } // end method onCreateView

    @Override
    public void onResume()
    {
        super.onResume();

        getActivity().setProgressBarIndeterminate(true);

        retrieveMessages();
    } // end method onResume

    private void retrieveMessages()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_ID, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> messages, ParseException e)
            {
                getActivity().setProgressBarIndeterminate(false);

                if(mSwipeRefreshLayout.isRefreshing())
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                } // end if

                if (e == null)
                {
                    mMessages = messages;

                    String[] userNames = new String[messages.size()];
                    for (int i = 0; i < messages.size(); i++)
                    {
                        userNames[i] = messages.get(i).getString(ParseConstants.KEY_SENDER_Name);

                    } // end for

                    if (getListView().getAdapter() == null)
                    {
                        MessagesAdapter adapter = new MessagesAdapter(getActivity(), mMessages);
                        setListAdapter(adapter);
                    } else
                    {
                        ((MessagesAdapter) getListView().getAdapter()).refill(mMessages);
                    } // end else
                } // end if
            } // end method done
        });
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id)
    {
        super.onListItemClick(list, view, position, id);

        ParseObject message = mMessages.get(position);
    } // end method onListItemClick

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
            retrieveMessages();
        } // end method onRefresh
    };

} // end class InboxFragment
