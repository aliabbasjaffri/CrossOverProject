package com.crossoverproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import com.crossoverproject.R;
import android.widget.EditText;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;

import com.crossoverproject.activity.AdminActivity;
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

        headerText = (TextView) view.findViewById(R.id.signInFragmentHeaderTextView);
        userName = (EditText) view.findViewById(R.id.signInFragmentUsernameEditText);
        password = (EditText) view.findViewById(R.id.signInFragmentPasswordEditText);
        loginButton = (Button) view.findViewById(R.id.signInFragmentLoginButton);

        String sText = Settings.getLoginRegistrationMode(getActivity()) + " Login";

        headerText.setText(sText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sUsername = userName.getText().toString();
                sPassword = password.getText().toString();

                if( Settings.getLoginRegistrationMode(getActivity()) == getActivity().getString(R.string.admin) )
                {

                }

                startActivity(new Intent(getActivity(), AdminActivity.class));
            }
        });

        return view;
    }

}
