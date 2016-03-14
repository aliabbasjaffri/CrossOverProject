package com.crossoverproject.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.crossoverproject.R;
import com.crossoverproject.activity.DoctorActivity;
import com.crossoverproject.helper.ViewSuggestionsViewHolder;

/**
 * Created by aliabbasjaffri on 13/03/16.
 */
public class ViewSuggestionsAdapter extends CursorAdapter
{
    public ViewSuggestionsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_suggestions_listview, parent, false);

        ViewSuggestionsViewHolder viewSuggestionsHolder = new ViewSuggestionsViewHolder(view);
        view.setTag(viewSuggestionsHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor)
    {
        ViewSuggestionsViewHolder viewHolder = (ViewSuggestionsViewHolder) view.getTag();

        String sTopic = cursor.getString(DoctorActivity.COLUMN_TOPIC);
        viewHolder.topic.setText(sTopic);

        String sSummary = cursor.getString(DoctorActivity.COLUMN_SUMMARY);
        viewHolder.summary.setText(sSummary);

        String sLocation = cursor.getString(DoctorActivity.COLUMN_LOCATION);
        viewHolder.location.setText(sLocation);

        String sDate = cursor.getString(DoctorActivity.COLUMN_DATE);
        viewHolder.date.setText(sDate);
    }
}
