package com.crossoverproject.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crossoverproject.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationLoginActivityFragment extends Fragment {

    public RegistrationLoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_login, container, false);
    }
}
