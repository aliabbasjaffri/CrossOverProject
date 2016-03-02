package com.crossoverproject.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.crossoverproject.model.ModelClass;

import java.util.List;

import nl.littlerobots.cupboard.tools.provider.UriHelper;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;
import nl.littlerobots.cupboard.tools.provider.CupboardContentProvider;

/**
 * Created by aliabbasjaffri on 29/02/16.
 */
public class ConferenceProvider extends ContentProvider
{
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ConferenceDatabaseHelper mConferenceDatabaseHelper;

    static final int USER = 100;

    static final int ADMIN = 200;
    static final int ADMIN_WITH_ID = 201;

    static final int DOCTOR = 300;
    static final int DOCTOR_WITH_ID = 301;

    static final int CONFERENCE = 400;

    static final int SUGGESTION = 500;

    private static final SQLiteQueryBuilder sUserwithAdmin;
    private static final SQLiteQueryBuilder sUserwithDoctor;

    static
    {
        sUserwithAdmin = new SQLiteQueryBuilder();
        sUserwithDoctor = new SQLiteQueryBuilder();

        sUserwithAdmin.setTables(
                ConferenceContract.AdminEntry.TABLE_NAME + " INNER JOIN " +
                        ConferenceContract.UserEntry.TABLE_NAME + " ON " +

                        ConferenceContract.AdminEntry.TABLE_NAME  + "." +
                        ConferenceContract.AdminEntry.COLUMN_USER_ID +
                        " = " +
                        ConferenceContract.UserEntry.TABLE_NAME  + "." +
                        ConferenceContract.UserEntry.COLUMN_USER_ID
        );

        sUserwithDoctor.setTables(
                ConferenceContract.DoctorEntry.TABLE_NAME + " INNER JOIN " +
                        ConferenceContract.DoctorEntry.TABLE_NAME + " ON " +

                        ConferenceContract.DoctorEntry.TABLE_NAME  + "." +
                        ConferenceContract.DoctorEntry.COLUMN_USER_ID +
                        " = " +
                        ConferenceContract.UserEntry.TABLE_NAME  + "." +
                        ConferenceContract.UserEntry.COLUMN_USER_ID
        );
    }

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ConferenceContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ConferenceContract.PATH_USER, USER);

        matcher.addURI(authority, ConferenceContract.PATH_ADMIN, ADMIN);
        matcher.addURI(authority, ConferenceContract.PATH_ADMIN + "/*", ADMIN_WITH_ID);

        matcher.addURI(authority, ConferenceContract.PATH_DOCTOR, DOCTOR);
        matcher.addURI(authority, ConferenceContract.PATH_DOCTOR + "/*", DOCTOR_WITH_ID);

        matcher.addURI(authority, ConferenceContract.PATH_CONFERENCE, CONFERENCE);

        matcher.addURI(authority, ConferenceContract.PATH_SUGGESTION, SUGGESTION);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mConferenceDatabaseHelper = new ConferenceDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Cursor retCursor;
        switch (sUriMatcher.match(uri))
        {
            case USER:
            {
                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
                break;
            }

            case ADMIN_WITH_ID: {
                retCursor = getAdminByID(uri, projection, sortOrder);
                break;
            }

            case DOCTOR_WITH_ID: {
                retCursor = getDoctorByID(uri, projection, sortOrder);
                break;
            }

            case CONFERENCE: {
                retCursor = mConferenceDatabaseHelper.getReadableDatabase().query(
                        ConferenceContract.ConferenceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case SUGGESTION: {
                retCursor = mConferenceDatabaseHelper.getReadableDatabase().query(
                        ConferenceContract.SuggestionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case USER:
                return ConferenceContract.UserEntry.CONTENT_ITEM_TYPE;
            case ADMIN:
                return ConferenceContract.AdminEntry.CONTENT_TYPE;
            case DOCTOR:
                return ConferenceContract.DoctorEntry.CONTENT_TYPE;
            case CONFERENCE:
                return ConferenceContract.ConferenceEntry.CONTENT_TYPE;
            case SUGGESTION:
                return ConferenceContract.SuggestionEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
