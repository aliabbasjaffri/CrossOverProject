package com.crossoverproject.helper;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.crossoverproject.R;

/**
 * Created by aliabbasjaffri on 13/03/16.
 */
public class ViewConferenceViewHolder
{
    public final TextView topic;
    public final TextView summary;
    public final TextView location;
    public final TextView date;

    public ViewConferenceViewHolder(View view)
    {
        topic = (TextView)view.findViewById(R.id.viewConferenceRowTopicTextView);
        summary = (TextView)view.findViewById(R.id.viewConferenceRowSummaryTextView);
        location = (TextView) view.findViewById(R.id.viewConferenceRowLocationTextView);
        date = (TextView) view.findViewById(R.id.viewConferenceRowDateTextView);
    }
}
