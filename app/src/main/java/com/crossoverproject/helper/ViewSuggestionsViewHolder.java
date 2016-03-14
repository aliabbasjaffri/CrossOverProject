package com.crossoverproject.helper;

import android.view.View;
import android.widget.TextView;

import com.crossoverproject.R;

/**
 * Created by aliabbasjaffri on 13/03/16.
 */
public class ViewSuggestionsViewHolder
{
    public final TextView topic;
    public final TextView summary;
    public final TextView location;
    public final TextView date;

    public ViewSuggestionsViewHolder(View view)
    {
        topic = (TextView)view.findViewById(R.id.viewSuggestionsRowTopicTextView);
        summary = (TextView)view.findViewById(R.id.viewSuggestionsRowSummaryTextView);
        location = (TextView) view.findViewById(R.id.viewSuggestionsRowLocationTextView);
        date = (TextView) view.findViewById(R.id.viewSuggestionsRowDateTextView);
    }
}
