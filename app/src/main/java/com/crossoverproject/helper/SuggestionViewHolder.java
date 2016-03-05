package com.crossoverproject.helper;

import android.view.View;
import android.widget.TextView;

import com.crossoverproject.R;

/**
 * Created by aliabbasjaffri on 04/03/16.
 */
public class SuggestionViewHolder
{
    public final TextView topic;
    public final TextView summary;
    public final TextView location;
    public final TextView date;

    public SuggestionViewHolder(View view) {
        topic = (TextView)view.findViewById(R.id.adminRowTopicTextView);
        summary = (TextView)view.findViewById(R.id.adminRowSummaryTextView);
        location = (TextView) view.findViewById(R.id.adminRowLocationTextView);
        date = (TextView) view.findViewById(R.id.adminRowDateTextView);
    }
}
