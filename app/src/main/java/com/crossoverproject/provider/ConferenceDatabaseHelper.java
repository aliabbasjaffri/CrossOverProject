package com.crossoverproject.provider;

import android.content.Context;
import com.crossoverproject.provider.ConferenceContract.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aliabbasjaffri on 29/02/16.
 */
public class ConferenceDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "crossoverdatabase.db";
    private static final int DATABASE_VERSION = 1;

    public ConferenceDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_USER_TABLE =
                "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                        UserEntry._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," +
                        UserEntry.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL, " +
                        UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                        UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                        " UNIQUE (" + UserEntry.COLUMN_USERNAME + ") " +
                        " );";

        final String SQL_CREATE_ADMIN_TABLE =
                "CREATE TABLE " + AdminEntry.TABLE_NAME + " (" +
                        AdminEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        AdminEntry.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL, " +
                        AdminEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        AdminEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
                        AdminEntry.COLUMN_SEX + " TEXT NOT NULL, " +
                        " FOREIGN KEY (" + AdminEntry.COLUMN_USER_ID + ") REFERENCES " +
                        UserEntry.TABLE_NAME + " (" + UserEntry.COLUMN_USER_ID + ") " +
                        " );";

        final String SQL_CREATE_DOCTOR_TABLE =
                "CREATE TABLE " + DoctorEntry.TABLE_NAME + " (" +
                        DoctorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DoctorEntry.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL, " +
                        DoctorEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        DoctorEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
                        DoctorEntry.COLUMN_SEX + " TEXT NOT NULL, " +
                        DoctorEntry.COLUMN_PRACTISE_YEARS + " INTEGER NOT NULL, " +
                        DoctorEntry.COLUMN_SPECIALIZATION_AREA + " TEXT NOT NULL, " +
                        DoctorEntry.COLUMN_CURRENT_LOCATION + " TEXT NOT NULL, " +
                        " FOREIGN KEY (" + DoctorEntry.COLUMN_USER_ID + ") REFERENCES " +
                        UserEntry.TABLE_NAME + " (" + UserEntry.COLUMN_USER_ID + ") " +
                        " );";

        final String SQL_CREATE_CONFERENCE_TABLE =
                "CREATE TABLE " + ConferenceEntry.TABLE_NAME + " (" +
                        ConferenceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        ConferenceEntry.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL, " +
                        ConferenceEntry.COLUMN_CONFERENCE_ID + " TEXT NOT NULL, " +
                        ConferenceEntry.COLUMN_TOPIC + " TEXT NOT NULL, " +
                        ConferenceEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                        ConferenceEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                        ConferenceEntry.COLUMN_LOCATION + " TEXT NOT NULL " +
                        " );";

        final String SQL_CREATE_SUGGESTION_TABLE =
                "CREATE TABLE " + SuggestionEntry.TABLE_NAME + " (" +
                        SuggestionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SuggestionEntry.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL, " +
                        SuggestionEntry.COLUMN_SUGGESTION_ID + " TEXT NOT NULL, " +
                        SuggestionEntry.COLUMN_TOPIC + " TEXT NOT NULL, " +
                        SuggestionEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                        SuggestionEntry.COLUMN_AVAILABILITY_DATE + " INTEGER NOT NULL, " +
                        SuggestionEntry.COLUMN_LOCATION_PREFERENCE + " TEXT NOT NULL " +
                        " );";

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_ADMIN_TABLE);
        db.execSQL(SQL_CREATE_DOCTOR_TABLE);
        db.execSQL(SQL_CREATE_CONFERENCE_TABLE);
        db.execSQL(SQL_CREATE_SUGGESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AdminEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DoctorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ConferenceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SuggestionEntry.TABLE_NAME);
        onCreate(db);
    }
}
