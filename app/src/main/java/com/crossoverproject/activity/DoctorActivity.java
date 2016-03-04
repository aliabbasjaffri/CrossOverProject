package com.crossoverproject.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.crossoverproject.R;
import com.crossoverproject.fragment.DoctorActivityFragment;
import com.crossoverproject.fragment.SignInFragment;
import com.crossoverproject.fragment.SignUpFragment;
import com.crossoverproject.provider.ConferenceContract;

public class DoctorActivity extends AppCompatActivity implements DoctorActivityFragment.Callback
{
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewSuggestion()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.popup_suggestion, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText topic = (EditText)promptView.findViewById(R.id.newSuggestionTopicEditText);
        final EditText summary = (EditText)promptView.findViewById(R.id.newSuggestionSummaryEditText);
        final EditText location = (EditText)promptView.findViewById(R.id.newSuggestionLocationEditText);
        final EditText date = (EditText)promptView.findViewById(R.id.newSuggestionDateEditText);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String sTopic = topic.getText().toString();
                        String sSummary = summary.getText().toString();
                        String sLocation = location.getText().toString();
                        String sDate = date.getText().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ConferenceContract.SuggestionEntry.COLUMN_USER_ID , "Ali123");
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
}
