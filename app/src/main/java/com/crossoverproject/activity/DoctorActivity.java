package com.crossoverproject.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.crossoverproject.fragment.DoctorActivityFragment;
import com.crossoverproject.fragment.ViewSuggestions;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.provider.ConferenceProvider;
import com.crossoverproject.utils.Settings;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DoctorActivity extends AppCompatActivity implements DoctorActivityFragment.Callback , ViewSuggestions.SuggestionsCallback
{
    public static StringBuilder globalString = new StringBuilder();

    EditText topic;
    EditText summary;
    EditText location;
    Button datePicker;
    static TextView date;

    String conferenceID;
    String suggestionID;
    int updated;

    public static final String[] CONFERENCE_COLUMNS = {
            ConferenceContract.ConferenceEntry.TABLE_NAME + "." + ConferenceContract.ConferenceEntry._ID,
            ConferenceContract.ConferenceEntry.COLUMN_USER_ID,
            ConferenceContract.ConferenceEntry.COLUMN_TOPIC,
            ConferenceContract.ConferenceEntry.COLUMN_SUMMARY,
            ConferenceContract.ConferenceEntry.COLUMN_DATE,
            ConferenceContract.ConferenceEntry.COLUMN_LOCATION,
            ConferenceContract.ConferenceEntry.COLUMN_READ_TAG
    };

    public static final int COLUMN_CONFERENCE_ID = 0;
    public static final int COLUMN_USER_ID = 1;
    public static final int COLUMN_TOPIC = 2;
    public static final int COLUMN_SUMMARY = 3;
    public static final int COLUMN_DATE = 4;
    public static final int COLUMN_LOCATION = 5;
    public static final int COLUMN_READ_TAG = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
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
                .add(R.id.doctorActivityFrameLayout, new DoctorActivityFragment(), DoctorActivityFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_suggestion) {
            addNewSuggestion();
            return true;
        } else if (id == R.id.action_view_suggestion) {
            openSuggestions();
            return true;
        } else if (id == R.id.action_doctor_signOut) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void onItemSelected(final Uri uri) {

        Cursor cursor = getContentResolver().query(uri, CONFERENCE_COLUMNS, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();

            final String title = cursor.getString(DoctorActivity.COLUMN_TOPIC);
            final String description = cursor.getString(DoctorActivity.COLUMN_SUMMARY);
            final String location = cursor.getString(DoctorActivity.COLUMN_LOCATION);
            final String date = cursor.getString(DoctorActivity.COLUMN_DATE);
            final String readTag = cursor.getString(DoctorActivity.COLUMN_READ_TAG);

            String[] dateInParts = date.split("-");
            final Calendar cal = new GregorianCalendar(Integer.parseInt(dateInParts[2].trim()) , Integer.parseInt(dateInParts[0].trim())
                    , Integer.parseInt(dateInParts[0].trim()) );
            cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            final long startDate = cal.getTimeInMillis();

            if( readTag.equals("1") )
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.addToCalendar);

                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent intent = new Intent(Intent.ACTION_EDIT);
                                intent.setType("vnd.android.cursor.item/event");
                                intent.putExtra("beginTime", startDate);
                                intent.putExtra("allDay", true);
                                intent.putExtra("rrule", "FREQ=YEARLY");
                                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                                intent.putExtra("title", title);
                                intent.putExtra("description", description);
                                intent.putExtra("eventLocation", location);

                                startActivity(intent);

                                ContentValues values = new ContentValues();
                                values.put(ConferenceContract.ConferenceEntry.COLUMN_READ_TAG, "0");
                                conferenceID = Long.toString(ConferenceContract.ConferenceEntry.getConferenceIDFromUri(uri));
                                updated = getContentResolver()
                                        .update(ConferenceContract.ConferenceEntry.CONTENT_URI,
                                                values,
                                                ConferenceProvider.sConferenceIDSelection,
                                                new String[]{conferenceID});
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
            else{
                Toast.makeText(DoctorActivity.this, "This Conference has already been entered in Calendar.", Toast.LENGTH_SHORT)
                        .show();
            }



            cursor.close();
        }
    }

    @Override
    public void onSuggestionsSelected(Uri uri) {
        editSuggestion(uri);
    }

    private void signOut() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(this.getString(R.string.settings_userid_key)).apply();

        startActivity(new Intent(this, RegistrationLoginActivity.class));
        this.finish();
    }

    private void addNewSuggestion() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.popup_suggestion, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        topic = (EditText) promptView.findViewById(R.id.newSuggestionTopicEditText);
        summary = (EditText) promptView.findViewById(R.id.newSuggestionSummaryEditText);
        location = (EditText) promptView.findViewById(R.id.newSuggestionLocationEditText);
        date = (TextView) promptView.findViewById(R.id.newSuggestionDateTextView);
        datePicker = (Button)promptView.findViewById(R.id.newSuggestionsDatePickerButton);
        datePicker.setOnClickListener(new View.OnClickListener() {
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
                        date.setText(globalString.toString());
                        String sDate = globalString.toString();

                        if( !sTopic.equals("") && !sSummary.equals("") && !sLocation.equals("") && !sDate.equals("") )
                        {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_USER_ID, Settings.getUserID(DoctorActivity.this));
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_TOPIC, sTopic);
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_SUMMARY, sSummary);
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_LOCATION_PREFERENCE, sLocation);
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_AVAILABILITY_DATE, sDate);

                            DoctorActivity.this.getContentResolver()
                                    .insert(ConferenceContract.SuggestionEntry.CONTENT_URI, contentValues);

                            Toast.makeText(DoctorActivity.this, "Your Suggestion is logged. Thank you.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else
                            Toast.makeText(DoctorActivity.this, "Please Enter all fields", Toast.LENGTH_SHORT)
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

    private void openSuggestions() {
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.doctorActivityFrameLayout, new ViewSuggestions() , ViewSuggestions.class.getSimpleName())
                .addToBackStack(ViewSuggestions.class.getSimpleName()).commit();
    }

    private void editSuggestion(Uri uri){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.popup_edit_suggestion, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText updateTopic = (EditText) promptView.findViewById(R.id.viewSuggestionPopupTopicEditText);
        final EditText updateSummary = (EditText) promptView.findViewById(R.id.viewSuggestionPopupSummaryEditText);
        final EditText updateLocation = (EditText) promptView.findViewById(R.id.viewSuggestionPopupLocationEditText);
        final TextView updateDate = (TextView) promptView.findViewById(R.id.viewSuggestionPopupDateTextView);
        final Button updateDateButton = (Button) promptView.findViewById(R.id.viewSuggestionPopupCalendarPickedDateButton);

        Cursor cursor = getContentResolver().query(uri, AdminActivity.SUGGESTION_COLUMNS , null , null , null );

        if (cursor != null)
        {
            cursor.moveToFirst();
            suggestionID = Long.toString(ConferenceContract.SuggestionEntry.getSuggestionIDFromUri(uri));
            updateTopic.setText(cursor.getString(AdminActivity.COLUMN_TOPIC));
            updateSummary.setText(cursor.getString(AdminActivity.COLUMN_SUMMARY));
            updateLocation.setText(cursor.getString(AdminActivity.COLUMN_LOCATION_PREFERENCE));
            updateDate.setText(cursor.getString(AdminActivity.COLUMN_AVAILABILITY_DATE));
            cursor.close();
        }

        updateDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(getFragmentManager(), DatePickerFragment.class.getSimpleName());
            }
        });

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String s_Topic = updateTopic.getText().toString();
                        String s_Summary = updateSummary.getText().toString();
                        String s_Location = updateLocation.getText().toString();
                        String s_Date = updateDate.getText().toString().length() == 0 ? globalString.toString() : updateDate.getText().toString();

                        if (updateTopic.getText().toString().length() == 0 ||
                                updateSummary.getText().toString().length() == 0 ||
                                updateLocation.getText().toString().length() == 0 ||
                                s_Date.length() == 0)
                        {
                            Toast.makeText(DoctorActivity.this, "Please Enter all fields", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else{

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_USER_ID, Settings.getUserID(DoctorActivity.this));
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_TOPIC, s_Topic);
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_SUMMARY, s_Summary);
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_LOCATION_PREFERENCE, s_Location);
                            contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_AVAILABILITY_DATE, s_Date);

                            updated = getContentResolver()
                                    .update(ConferenceContract.SuggestionEntry.CONTENT_URI, contentValues,
                                            ConferenceProvider.sSuggestionIDSelection , new String [] {suggestionID} );

                            Toast.makeText(DoctorActivity.this, updated + " Suggestion is Updated.", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(DoctorActivity.this)
                                .setTitle("Delete entry")
                                .setMessage("Are you sure you want to delete this entry?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        updated = getContentResolver()
                                                .delete(ConferenceContract.SuggestionEntry.CONTENT_URI,
                                                        ConferenceProvider.sSuggestionIDSelection,
                                                        new String[]{suggestionID});

                                        Toast.makeText(DoctorActivity.this, updated + " Suggestion has been Deleted.",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
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

        public void onDateSet(DatePicker view, int year, int month, int day) {
            globalString = new StringBuilder().append(month + 1).append("-").append(day).append("-").append(year).append(" ");
        }
    }
}
