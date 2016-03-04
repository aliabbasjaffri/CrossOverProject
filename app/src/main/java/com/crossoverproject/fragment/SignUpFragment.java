package com.crossoverproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.crossoverproject.R;
import com.crossoverproject.activity.DoctorActivity;
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
                startActivity(new Intent(getActivity() , DoctorActivity.class));
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

}
