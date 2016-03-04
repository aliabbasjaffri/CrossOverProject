package com.crossoverproject.helper;

import android.view.View;
import android.widget.TextView;

import com.crossoverproject.R;

/**
 * Created by aliabbasjaffri on 04/03/16.
 */
public class ConferenceViewHolder
{
    public final TextView topic;
    public final TextView summary;
    public final TextView location;
    public final TextView date;


    public ConferenceViewHolder(View view) {
        topic = (TextView)view.findViewById(R.id.doctorRowTopicTextView);
        summary = (TextView)view.findViewById(R.id.doctorRowSummaryTextView);
        location = (TextView) view.findViewById(R.id.doctorRowLocationTextView);
        date = (TextView) view.findViewById(R.id.doctorRowDateTextView);
    }
}
