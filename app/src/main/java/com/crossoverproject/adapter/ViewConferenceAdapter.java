package com.crossoverproject.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crossoverproject.R;
import com.crossoverproject.activity.AdminActivity;
import com.crossoverproject.activity.DoctorActivity;
import com.crossoverproject.helper.ViewConferenceViewHolder;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.provider.ConferenceProvider;
import com.crossoverproject.utils.Settings;

import java.util.Calendar;

/**
 * Created by aliabbasjaffri on 13/03/16.
 */
public class ViewConferenceAdapter extends CursorAdapter
{
    ViewConferenceViewHolder viewHolder;

    public ViewConferenceAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conference_listview, parent, false);

        ViewConferenceViewHolder viewConferenceHolder = new ViewConferenceViewHolder(view);
        view.setTag(viewConferenceHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor)
    {
        viewHolder = (ViewConferenceViewHolder) view.getTag();

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
