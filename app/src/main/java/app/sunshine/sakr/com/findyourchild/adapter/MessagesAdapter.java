package app.sunshine.sakr.com.findyourchild.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

import app.sunshine.sakr.com.findyourchild.utility.ParseConstants;
import app.sunshine.sakr.com.findyourchild.R;

/**
 * Created by mohammad sakr on 15/06/2015.
 */
public class MessagesAdapter extends ArrayAdapter<ParseObject>
{
    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessagesAdapter(Context context,List<ParseObject> list)
    {
        super(context, R.layout.message_item,list);
        mContext = context;
        mMessages = list;
    }// end public constructor

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.message_icon);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.sender_text);
            holder.messageText = (TextView)convertView.findViewById(R.id.message_text);
            holder.dateText = (TextView)convertView.findViewById(R.id.date_text);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        ParseObject message = mMessages.get(position);
        holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_Name));
        holder.messageText.setText(message.getDouble(ParseConstants.KEY_LOCATION_LATITUDE) + "," +
        message.getDouble(ParseConstants.KEY_LOCATION_LONGITUDE));
        holder.iconImageView.setImageResource(R.drawable.ic_inbox_grey600_24dp);
        holder.dateText.setText(getFormattedDate(message));


        return convertView;
    }

    private static class ViewHolder
    {
        ImageView iconImageView;
        TextView nameLabel;
        TextView messageText;
        TextView dateText;
    }

    public void refill(List<ParseObject> messages)
    {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    } // end method refill

    private String getFormattedDate(ParseObject message)
    {
        Date createdDate = message.getCreatedAt();
        long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(
                createdDate.getTime(),
                now,
                DateUtils.SECOND_IN_MILLIS).toString();
        return convertedDate;

    } // end method getFormattedDate


} // end class MessagesAdapter
