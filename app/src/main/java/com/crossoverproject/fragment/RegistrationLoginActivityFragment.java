package com.crossoverproject.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.crossoverproject.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationLoginActivityFragment extends Fragment
{
    Button signInButton;
    Button signUpButton;
    RadioGroup radioRoleGroup;
    RadioButton radioRoleButton;

    String selectedRole;

    public RegistrationLoginActivityFragment()
    {

    }

    public static RegistrationLoginActivityFragment newInstance() {
        return new RegistrationLoginActivityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_registration_login, container, false);
        signInButton = (Button) view.findViewById(R.id.signInButton);
        signUpButton = (Button) view.findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                viewPopUp(true);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                viewPopUp(false);
            }
        });

        return view;
    }

    private void viewPopUp(final boolean checker) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View promptView = layoutInflater.inflate(R.layout.popup_registration_login, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = prefs.edit();

        radioRoleGroup = (RadioGroup) promptView.findViewById(R.id.loginRole);
        radioRoleButton = (RadioButton) promptView.findViewById(radioRoleGroup.getCheckedRadioButtonId());

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        radioRoleButton = (RadioButton) promptView.findViewById(radioRoleGroup.getCheckedRadioButtonId());
                        selectedRole = radioRoleButton.getText().toString();

                        editor.putString(getActivity().getString(R.string.settings_loginMode_key) , selectedRole).apply();
                        Toast.makeText(getContext() , selectedRole , Toast.LENGTH_SHORT).show();

                        if(checker)
                        {
                            getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.registrationLoginFragment, new SignInFragment(), SignInFragment.class.getSimpleName())
                                .addToBackStack(SignInFragment.class.getSimpleName()).commit();
                        }
                        else
                        {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction().replace(R.id.registrationLoginFragment, new SignUpFragment(), SignUpFragment.class.getSimpleName())
                                    .addToBackStack(SignUpFragment.class.getSimpleName()).commit();
                        }
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
}
