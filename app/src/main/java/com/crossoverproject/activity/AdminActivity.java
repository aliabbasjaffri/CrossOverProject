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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.crossoverproject.R;
import com.crossoverproject.fragment.AdminActivityFragment;
import com.crossoverproject.fragment.ViewConferences;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.utils.Settings;

import java.util.Calendar;

public class AdminActivity extends AppCompatActivity implements AdminActivityFragment.Callback , ViewConferences.ConferenceCallback
{
    public static StringBuilder global_string = new StringBuilder();
    EditText topic;
    EditText summary;
    EditText location;
    static TextView date;
    Button dateButton;

    public static final String[] SUGGESTION_COLUMNS = {
            ConferenceContract.SuggestionEntry.TABLE_NAME + "." + ConferenceContract.SuggestionEntry._ID,
            ConferenceContract.SuggestionEntry.COLUMN_USER_ID,
            ConferenceContract.SuggestionEntry.COLUMN_TOPIC,
            ConferenceContract.SuggestionEntry.COLUMN_SUMMARY,
            ConferenceContract.SuggestionEntry.COLUMN_AVAILABILITY_DATE,
            ConferenceContract.SuggestionEntry.COLUMN_LOCATION_PREFERENCE,
            ConferenceContract.SuggestionEntry.COLUMN_READ_TAG
    };

    public static final int COLUMN_SUGGESTION_ID = 0;
    public static final int COLUMN_USER_ID = 1;
    public static final int COLUMN_TOPIC = 2;
    public static final int COLUMN_SUMMARY = 3;
    public static final int COLUMN_AVAILABILITY_DATE = 4;
    public static final int COLUMN_LOCATION_PREFERENCE = 5;
    public static final int COLUMN_READ_TAG = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.adminActivityFrameLayout, new AdminActivityFragment(), AdminActivityFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_add_conference) {
            addNewConference();
            return true;
        } else if (id == R.id.action_view_conference) {
            openConferences();
            return true;
        }else if (id == R.id.action_admin_signout) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewConference() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.popup_conference, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        topic = (EditText) promptView.findViewById(R.id.newConferenceTopicEditText);
        summary = (EditText) promptView.findViewById(R.id.newConferenceSummaryEditText);
        location = (EditText) promptView.findViewById(R.id.newConferenceLocationEditText);
        date = (TextView) promptView.findViewById(R.id.newConferenceDateTextView);
        dateButton = (Button) promptView.findViewById(R.id.newConferenceDatePickerButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(getFragmentManager(), "datePicker");
            }
        });

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String sTopic = topic.getText().toString();
                        String sSummary = summary.getText().toString();
                        String sLocation = location.getText().toString();
                        date.setText(global_string.toString());
                        String sDate = global_string.toString();

                        if (!sTopic.equals("") && !sSummary.equals("") && !sLocation.equals("") && !sDate.equals("")) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_USER_ID, Settings.getUserID(AdminActivity.this));
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_TOPIC, sTopic);
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_SUMMARY, sSummary);
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_LOCATION, sLocation);
                            contentValues.put(ConferenceContract.ConferenceEntry.COLUMN_DATE, sDate);

                            AdminActivity.this.getContentResolver()
                                    .insert(ConferenceContract.ConferenceEntry.CONTENT_URI, contentValues);

                            Toast.makeText(AdminActivity.this, "Your Conference is posted. Thank you.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else
                            Toast.makeText(AdminActivity.this, "Please Enter all fields", Toast.LENGTH_SHORT)
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

    private void signOut() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(this.getString(R.string.settings_userid_key)).apply();

        startActivity(new Intent(this, RegistrationLoginActivity.class));
        this.finish();
    }

    private void openConferences() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(ViewConferences.class.getSimpleName())
                .replace(R.id.adminActivityFrameLayout, new ViewConferences(), ViewConferences.class.getSimpleName())
                .commit();
    }

    @Override
    public void onItemSelected(Uri uri)
    {
        Toast.makeText(AdminActivity.this, uri.toString() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConferenceSelected(Uri uri) {
        Toast.makeText(AdminActivity.this, uri.toString() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed ()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStackImmediate();
        }
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
            global_string = new StringBuilder().append(month+1).append("-").append(day).append("-").append(year).append(" ");
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            date.setText(global_string.toString());
        }
    }
}
