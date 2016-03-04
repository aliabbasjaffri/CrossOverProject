package com.crossoverproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.crossoverproject.R;

/**
 * Created by aliabbasjaffri on 03/03/16.
 */
public class Settings
{
    public static String getLoginRegistrationMode(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.settings_loginMode_key), context.getString(R.string.settings_loginMode_default));
    }
}
