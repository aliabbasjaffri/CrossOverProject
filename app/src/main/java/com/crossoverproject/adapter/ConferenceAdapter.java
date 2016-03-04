package com.crossoverproject.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.crossoverproject.R;
import com.crossoverproject.activity.DoctorActivity;
import com.crossoverproject.fragment.DoctorActivityFragment;
import com.crossoverproject.helper.ConferenceViewHolder;
import com.crossoverproject.provider.ConferenceContract;

/**
 * Created by aliabbasjaffri on 04/03/16.
 */
public class ConferenceAdapter extends CursorAdapter
{
    public ConferenceAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_doctor_listview, parent, false);

        ConferenceViewHolder viewHolder = new ConferenceViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ConferenceViewHolder viewHolder = (ConferenceViewHolder) view.getTag();

        String topic = cursor.getString(DoctorActivityFragment.COLUMN_TOPIC);
        viewHolder.topic.setText(topic);

        String summary = cursor.getString(DoctorActivityFragment.COLUMN_SUMMARY);
        viewHolder.summary.setText(summary);

        String location = cursor.getString(DoctorActivityFragment.COLUMN_LOCATION);
        viewHolder.location.setText(location);

        String date = cursor.getString(DoctorActivityFragment.COLUMN_DATE);
        viewHolder.date.setText(date);
    }
}
