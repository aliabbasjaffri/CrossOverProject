package com.crossoverproject.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import com.crossoverproject.R;
import android.widget.EditText;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.crossoverproject.activity.AdminActivity;
import com.crossoverproject.activity.DoctorActivity;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.utils.Settings;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment
{
    TextView headerText;
    EditText userName;
    EditText password;
    Button loginButton;

    String sUsername;
    String sPassword;

    boolean proceed;

    SharedPreferences prefs;

    private static final String[] ADMIN_PROJECTION = new String[] {
            ConferenceContract.AdminEntry.TABLE_NAME + "." + ConferenceContract.AdminEntry._ID,
            ConferenceContract.AdminEntry.COLUMN_USERNAME,
            ConferenceContract.AdminEntry.COLUMN_PASSWORD,
    };

    private static final String[] DOCTOR_PROJECTION = new String[] {
            ConferenceContract.DoctorEntry.TABLE_NAME + "." + ConferenceContract.DoctorEntry._ID,
            ConferenceContract.DoctorEntry.COLUMN_USERNAME,
            ConferenceContract.DoctorEntry.COLUMN_PASSWORD,
    };

    private static final int ADMIN_ID = 0;
    private static final int ADMIN_USERNAME = 1;
    private static final int ADMIN_PASSWORD = 2;

    private static final int DOCTOR_ID = 0;
    private static final int DOCTOR_USERNAME = 1;
    private static final int DOCTOR_PASSWORD = 2;

    public SignInFragment() {
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        proceed = false;

        headerText = (TextView) view.findViewById(R.id.signInFragmentHeaderTextView);
        userName = (EditText) view.findViewById(R.id.signInFragmentUsernameEditText);
        password = (EditText) view.findViewById(R.id.signInFragmentPasswordEditText);
        loginButton = (Button) view.findViewById(R.id.signInFragmentLoginButton);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sText = Settings.getLoginRegistrationMode(getActivity()) + " Login";
        headerText.setText(sText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sUsername = userName.getText().toString();
                sPassword = password.getText().toString();

                if (validateUser())
                    startActivity(new Intent(getActivity(), ((Settings.getLoginRegistrationMode(getActivity())).equals(getActivity().getString(R.string.doctor))) ? DoctorActivity.class : AdminActivity.class));
            }
        });

        return view;
    }

    private boolean validateUser()
    {
        if ((Settings.getLoginRegistrationMode(getActivity())).equals( getContext().getString(R.string.admin)))
        {
            Uri adminUri = ConferenceContract.AdminEntry.CONTENT_URI;
            Cursor cursor = getContext().getContentResolver().query(adminUri, ADMIN_PROJECTION, null, null, null);

            int adminId = 0;
            String username;
            String password;

            if (cursor == null) {
                Toast.makeText(getActivity(), "Please register as an Admin prior to Logging In.", Toast.LENGTH_SHORT).show();
                return false;
            } else
            {
                while(cursor.moveToNext())
                {
                    adminId = cursor.getInt(ADMIN_ID);
                    username = cursor.getString(ADMIN_USERNAME);
                    password = cursor.getString(ADMIN_PASSWORD);

                    if (sUsername.equals(username) && sPassword.equals(password))
                    {
                        proceed = true;
                        break;
                    }
                }
            }

            if( proceed ) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(getContext().getString(R.string.settings_userid_key), adminId).apply();
                cursor.close();
                return true;
            }
            else
            {
                Toast.makeText(getActivity(), "No such user exists.", Toast.LENGTH_SHORT).show();
                cursor.close();
                return false;
            }


        }
        else if (((Settings.getLoginRegistrationMode(getActivity())).equals( getContext().getString(R.string.doctor))))
        {
            Uri doctorUri = ConferenceContract.DoctorEntry.CONTENT_URI;
            Cursor cursor = getContext().getContentResolver().query(doctorUri, DOCTOR_PROJECTION, null, null, null);

            int doctorId = 0;
            String username;
            String password;


            if (cursor == null) {
                Toast.makeText(getActivity(), "Please register as a Doctor prior to Logging In.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                while(cursor.moveToNext()) {
                    doctorId = cursor.getInt(DOCTOR_ID);
                    username = cursor.getString(DOCTOR_USERNAME);
                    password = cursor.getString(DOCTOR_PASSWORD);

                    if (sUsername.equals(username) && sPassword.equals(password))
                    {
                        proceed = true;
                        break;
                    }
                }
            }

            if (proceed)
            {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(getContext().getString(R.string.settings_userid_key), doctorId).apply();
                cursor.close();
                return true;
            }
            else {
                Toast.makeText(getActivity(), "No such user exists.", Toast.LENGTH_SHORT).show();
                cursor.close();
                return false;
            }


        }

        return false;
    }

}
