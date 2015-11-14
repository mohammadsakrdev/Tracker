package app.sunshine.sakr.com.findyourchild.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

import app.sunshine.sakr.com.findyourchild.R;

/**
 * Created by mohammad sakr on 15/06/2015.
 */
public class UserAdapter extends ArrayAdapter<ParseUser>
{
    protected Context mContext;
    protected List<ParseUser> mUsers;

    public UserAdapter(Context context, List<ParseUser> users)
    {
        super(context, R.layout.user_item, users);
        mContext = context;
        mUsers = users;
    }// end public constructor

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.userImageView);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
            holder.checkedImageView = (ImageView)convertView.findViewById(R.id.checkedImageView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        ParseUser user = mUsers.get(position);
        holder.nameLabel.setText(user.getUsername());

        GridView gridView = (GridView)parent;
        if(gridView.isItemChecked(position))
        {
            holder.checkedImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.checkedImageView.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

    public void refill(List<ParseUser> users)
    {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    } // end method refill

    private static class ViewHolder
    {
        ImageView iconImageView;
        ImageView checkedImageView;
        TextView nameLabel;
    }

} // end class MessagesAdapter
