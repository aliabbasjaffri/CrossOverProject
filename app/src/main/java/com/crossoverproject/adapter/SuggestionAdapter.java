package com.crossoverproject.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.crossoverproject.R;
import com.crossoverproject.activity.AdminActivity;
import com.crossoverproject.helper.SuggestionViewHolder;

/**
 * Created by aliabbasjaffri on 04/03/16.
 */
public class SuggestionAdapter extends CursorAdapter
{
    public SuggestionAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_admin_listview, parent, false);

        SuggestionViewHolder viewHolder = new SuggestionViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        SuggestionViewHolder viewHolder = (SuggestionViewHolder) view.getTag();

        String topic = cursor.getString(AdminActivity.COLUMN_TOPIC);
        viewHolder.topic.setText(topic);

        String summary = cursor.getString(AdminActivity.COLUMN_SUMMARY);
        viewHolder.summary.setText(summary);

        String location = cursor.getString(AdminActivity.COLUMN_LOCATION_PREFERENCE);
        viewHolder.location.setText(location);

        String date = cursor.getString(AdminActivity.COLUMN_AVAILABILITY_DATE);
        viewHolder.date.setText(date);
    }
}
