package app.sunshine.sakr.com.findyourchild.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import app.sunshine.sakr.com.findyourchild.R;
import app.sunshine.sakr.com.findyourchild.ui.FriendsFragment;
import app.sunshine.sakr.com.findyourchild.ui.InboxFragment;

/**
 * Created by mohammad sakr on 02/05/2015.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    protected Context mContext;
    public SectionsPagerAdapter(Context context,FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }
    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new FriendsFragment();
            case 1:
                return new InboxFragment();
        } //end switch

        return null;
    } // end method getItem

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount()
    {
        return 2;
    } // end method getCount

    @Override
    public CharSequence getPageTitle(int position)
    {
        Locale l = Locale.getDefault();
        switch (position)
        {
            case 0:
                return  mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    } // end method getPageTitle

    public int getIcon(int position)
    {
        switch (position)
        {
            case 0:
                return R.mipmap.ic_tab_friends;
            case 1:
                return R.mipmap.ic_tab_inbox;
        }
        return R.mipmap.ic_tab_inbox;
    } // getIcon
}
