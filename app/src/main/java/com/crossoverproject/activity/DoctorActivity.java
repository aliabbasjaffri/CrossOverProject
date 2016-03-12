package com.crossoverproject.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crossoverproject.R;
import com.crossoverproject.fragment.DoctorActivityFragment;
import com.crossoverproject.fragment.SignInFragment;
import com.crossoverproject.fragment.SignUpFragment;
import com.crossoverproject.fragment.ViewConferences;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.utils.Settings;

import java.util.Calendar;

public class DoctorActivity extends AppCompatActivity implements DoctorActivityFragment.Callback
{
    public static StringBuilder global_string = new StringBuilder();
    EditText topic;
    EditText summary;
    EditText location;
    static TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_add_suggestion)
        {
            addNewSuggestion();
            return true;
        } else if( id == R.id.action_view_suggestion ) {
            openSuggestions();
            return true;
        }else if( id == R.id.action_doctor_signOut ) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewSuggestion()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.popup_suggestion, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        topic = (EditText)promptView.findViewById(R.id.newSuggestionTopicEditText);
        summary = (EditText)promptView.findViewById(R.id.newSuggestionSummaryEditText);
        location = (EditText)promptView.findViewById(R.id.newSuggestionLocationEditText);
        date = (TextView)promptView.findViewById(R.id.newSuggestionDateTextView);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String sTopic = topic.getText().toString();
                        String sSummary = summary.getText().toString();
                        String sLocation = location.getText().toString();
                        date.setText(global_string.toString());
                        String sDate = global_string.toString();


                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_USER_ID , Settings.getUserID(DoctorActivity.this));
                        contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_TOPIC , sTopic);
                        contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_SUMMARY , sSummary);
                        contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_LOCATION_PREFERENCE, sLocation);
                        contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_AVAILABILITY_DATE, sDate);

                        DoctorActivity.this.getContentResolver()
                                .insert(ConferenceContract.SuggestionEntry.CONTENT_URI, contentValues);

                        Toast.makeText(DoctorActivity.this , "Your Suggestion is logged. Thank you." , Toast.LENGTH_SHORT )
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

    @Override
    public void onItemSelected(Uri uri)
    {

    }

    private void signOut()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(this.getString(R.string.settings_userid_key)).apply();

        startActivity(new Intent(this, RegistrationLoginActivity.class));
        this.finish();
    }

    private void openSuggestions()
    {

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
            global_string= new StringBuilder().append(month+1).append("-").append(day).append("-").append(year).append(" ");
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            date.setText(global_string.toString());
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
