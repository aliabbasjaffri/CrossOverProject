package com.crossoverproject.fragment;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crossoverproject.R;
import com.crossoverproject.activity.AdminActivity;
import com.crossoverproject.activity.DoctorActivity;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.utils.Settings;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment
{
    TextView headerTextView;
    EditText userName;
    EditText password;
    EditText name;
    EditText age;
    EditText yearsOfPractise;
    EditText areaOfExpertise;
    EditText location;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    Button register;

    String sHeaderText;
    String sUsername;
    String sPassword;
    String sName;
    String sAge;
    String sSex;
    String sYearsOfPractise;
    String sAreaOfExpertise;
    String sLocation;

    public SignUpFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        headerTextView = (TextView) view.findViewById(R.id.signUpHeaderTextView);
        userName = (EditText) view.findViewById(R.id.signUpUsernameEditText);
        password = (EditText) view.findViewById(R.id.signUpPasswordEditText);
        name = (EditText) view.findViewById(R.id.signUpNameEditText);
        age = (EditText) view.findViewById(R.id.signUpAgeEditText);
        yearsOfPractise = (EditText) view.findViewById(R.id.signUpYearsOfPractiseEditText);
        areaOfExpertise = (EditText) view.findViewById(R.id.signUpSpecializationAreaEditText);
        location = (EditText) view.findViewById(R.id.signUpCurrentLocationEditText);

        if( Settings.getLoginRegistrationMode(getActivity()) == getActivity().getString(R.string.admin) )
        {
            yearsOfPractise.setVisibility(View.GONE);
            areaOfExpertise.setVisibility(View.GONE);
            location.setVisibility(View.GONE);
        }
        else
        {
            yearsOfPractise.setVisibility(View.VISIBLE);
            areaOfExpertise.setVisibility(View.VISIBLE);
            location.setVisibility(View.VISIBLE);
        }

        sHeaderText = Settings.getLoginRegistrationMode(getActivity()) + " Registration";
        headerTextView.setText(sHeaderText);

        radioSexGroup = (RadioGroup) view.findViewById(R.id.signUpSexRadioGroup);

        register = (Button) view.findViewById(R.id.signUpCreateUserButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getDataFromEditTextFields();
                radioSexButton = (RadioButton) view.findViewById(radioSexGroup.getCheckedRadioButtonId());
                sSex = radioSexButton.getText().toString();

                if(validations())
                {
                    storeToDatabase();
                    startActivity(new Intent(getActivity()
                            , (Settings.getLoginRegistrationMode(getActivity()) == getActivity().getString(R.string.doctor)) ?
                            DoctorActivity.class : AdminActivity.class));
                }
            }
        });

        return view;
    }

    private void getDataFromEditTextFields()
    {
        sUsername = userName.getText().toString();
        sPassword = password.getText().toString();
        sName = name.getText().toString();
        sAge = age.getText().toString();

        if( Settings.getLoginRegistrationMode(getActivity()) == getActivity().getString(R.string.doctor) )
        {
            sYearsOfPractise = yearsOfPractise.getText().toString();
            sAreaOfExpertise = areaOfExpertise.getText().toString();
            sLocation = location.getText().toString();
        }
    }

    private boolean validations()
    {
        if( Settings.getLoginRegistrationMode(getActivity()) == getActivity().getString(R.string.doctor))
        {
            if ( sYearsOfPractise.isEmpty() || sAreaOfExpertise.isEmpty() || sLocation.isEmpty() ) {
                Toast.makeText(getActivity(), "Please fill in all Fields", Toast.LENGTH_SHORT).show();
                return false;
            }

            int experienceYears = Integer.parseInt(sYearsOfPractise);
            if( experienceYears < 1 || experienceYears > 40 ) {
                Toast.makeText(getActivity(), "Please enter a experience between 1 - 40", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (sUsername.isEmpty() || sPassword.isEmpty() || sName.isEmpty() || sAge.isEmpty() )
        {
            Toast.makeText(getActivity(), "Please fill in all Fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!sUsername.matches("[a-zA-Z.? ]*")) {
            Toast.makeText(getActivity(), "Username should not contains special Characters or Digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sUsername.length() > 6) {
            Toast.makeText(getActivity(), "Username should only be 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sPassword.length() > 6) {
            Toast.makeText(getActivity(), "Password should be 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }


        int age = Integer.parseInt(sAge);
        if( age < 22 || age > 80 ) {
            Toast.makeText(getActivity(), "Please enter a valid age between 22 - 80", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void storeToDatabase()
    {
        ContentValues contentValues = new ContentValues();

        if(Settings.getLoginRegistrationMode(getActivity()) == getActivity().getString(R.string.admin))
        {
            contentValues.put(ConferenceContract.AdminEntry.COLUMN_USERNAME , sUsername);
            contentValues.put(ConferenceContract.AdminEntry.COLUMN_PASSWORD , sPassword);
            contentValues.put(ConferenceContract.AdminEntry.COLUMN_NAME, sName);
            contentValues.put(ConferenceContract.AdminEntry.COLUMN_AGE , sAge);
            contentValues.put(ConferenceContract.AdminEntry.COLUMN_SEX, sSex);

            Uri uri = getActivity().getContentResolver()
                    .insert(ConferenceContract.AdminEntry.CONTENT_URI, contentValues);
            Toast.makeText(getActivity() , uri != null ? uri.toString() : "--NA--", Toast.LENGTH_SHORT).show();
        }
        else if( Settings.getLoginRegistrationMode(getActivity()) == getActivity().getString(R.string.doctor) )
        {
            contentValues.put(ConferenceContract.DoctorEntry.COLUMN_USERNAME , sUsername);
            contentValues.put(ConferenceContract.DoctorEntry.COLUMN_PASSWORD , sPassword);
            contentValues.put(ConferenceContract.DoctorEntry.COLUMN_NAME, sName);
            contentValues.put(ConferenceContract.DoctorEntry.COLUMN_AGE , sAge);
            contentValues.put(ConferenceContract.DoctorEntry.COLUMN_SEX, sSex);
            contentValues.put(ConferenceContract.DoctorEntry.COLUMN_SPECIALIZATION_AREA , sAreaOfExpertise);
            contentValues.put(ConferenceContract.DoctorEntry.COLUMN_PRACTISE_YEARS , sYearsOfPractise);
            contentValues.put(ConferenceContract.DoctorEntry.COLUMN_CURRENT_LOCATION , sLocation);

            Uri uri = getActivity().getContentResolver()
                    .insert(ConferenceContract.DoctorEntry.CONTENT_URI, contentValues);
            Toast.makeText(getActivity(), uri != null ? uri.toString() : "--NA--", Toast.LENGTH_SHORT).show();
        }
    }

}
