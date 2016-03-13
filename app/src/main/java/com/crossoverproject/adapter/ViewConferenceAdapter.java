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
    private Context context;
    public static StringBuilder globalString = new StringBuilder();
    ViewConferenceViewHolder viewHolder;

    String conferenceID;
    int updated;

    public ViewConferenceAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
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

        conferenceID = cursor.getString(DoctorActivity.COLUMN_CONFERENCE_ID);

        String sTopic = cursor.getString(DoctorActivity.COLUMN_TOPIC);
        viewHolder.topic.setText(sTopic);

        String sSummary = cursor.getString(DoctorActivity.COLUMN_SUMMARY);
        viewHolder.summary.setText(sSummary);

        String sLocation = cursor.getString(DoctorActivity.COLUMN_LOCATION);
        viewHolder.location.setText(sLocation);

        String sDate = cursor.getString(DoctorActivity.COLUMN_DATE);
        viewHolder.date.setText(sDate);
    }

    private void editConference( Cursor cursor ) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View promptView = layoutInflater.inflate(R.layout.popup_edit_conference, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        final EditText updateTopic = (EditText) promptView.findViewById(R.id.viewConferencePopupTopicEditText);
        final EditText updateSummary = (EditText) promptView.findViewById(R.id.viewConferencePopupSummaryEditText);
        final EditText updateLocation = (EditText) promptView.findViewById(R.id.viewConferencePopupLocationEditText);
        final TextView updateDate = (TextView) promptView.findViewById(R.id.viewConferencePopupDateTextView);
        final Button updateDateButton = (Button) promptView.findViewById(R.id.viewConferencePopupCalendarPickedDateButton);

        conferenceID = cursor.getString(DoctorActivity.COLUMN_CONFERENCE_ID);
        updateTopic.setText(cursor.getString(DoctorActivity.COLUMN_TOPIC));
        updateSummary.setText(cursor.getString(DoctorActivity.COLUMN_SUMMARY));
        updateLocation.setText(cursor.getString(DoctorActivity.COLUMN_LOCATION));
        updateDate.setText(cursor.getString(DoctorActivity.COLUMN_DATE));

        updateDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(((AdminActivity)context).getFragmentManager(),
                        DatePickerFragment.class.getSimpleName());
            }
        });

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String s_Topic = updateTopic.getText().toString();
                        String s_Summary = updateSummary.getText().toString();
                        String s_Location = updateLocation.getText().toString();
                        updateDate.setText(globalString.toString());
                        String s_Date = globalString.toString();

                        if (!s_Topic.equals("") && !s_Summary.equals("") && !s_Location.equals("") && !s_Date.equals("")) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_USER_ID, Settings.getUserID(context));
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_TOPIC, s_Topic);
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_SUMMARY, s_Summary);
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_LOCATION, s_Location);
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_DATE, s_Date);

                            updated = context.getContentResolver()
                                    .update(ConferenceContract.ConferenceEntry.CONTENT_URI, contentValues,
                                            ConferenceProvider.sConferenceIDSelection , new String [] {conferenceID} );

                            Toast.makeText(context, updated + " Conference is Updated.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else
                            Toast.makeText(context, "Please Enter all fields", Toast.LENGTH_SHORT)
                                    .show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            globalString = new StringBuilder().append(month+1).append("-").append(day).append("-").append(year).append(" ");
        }
    }

}
