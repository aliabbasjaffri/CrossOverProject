package com.crossoverproject.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crossoverproject.R;
import com.crossoverproject.fragment.RegistrationLoginActivityFragment;
import com.crossoverproject.fragment.SignInFragment;

public class RegistrationLoginActivity extends AppCompatActivity
{
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarHomeAccess(false);

        getSupportFragmentManager()
                .beginTransaction().add(R.id.registrationLoginFragment, RegistrationLoginActivityFragment.newInstance(), RegistrationLoginActivityFragment.class.getName())
                .addToBackStack(RegistrationLoginActivityFragment.class.getName()).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setToolbarHomeAccess(Boolean enable)
    {
        if(enable)
            toolbar.setVisibility(View.VISIBLE);
        else
            toolbar.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        getSupportActionBar().setDisplayShowHomeEnabled(enable);
    }

}
