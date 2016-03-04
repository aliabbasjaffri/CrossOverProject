package com.crossoverproject.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.crossoverproject.R;
import com.crossoverproject.utils.Settings;

/**
 * Created by aliabbasjaffri on 29/02/16.
 */
public class ConferenceProvider extends ContentProvider
{
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ConferenceDatabaseHelper mConferenceDatabaseHelper;

    static final int ADMIN = 200;
    static final int ADMIN_WITH_ID = 201;
    static final int ADMIN_WITH_USERNAME = 202;

    static final int DOCTOR = 300;
    static final int DOCTOR_WITH_ID = 301;
    static final int DOCTOR_WITH_USERNAME = 302;

    static final int CONFERENCE = 400;

    static final int SUGGESTION = 500;

    private static final SQLiteQueryBuilder sUserwithAdmin;
    private static final SQLiteQueryBuilder sUserwithDoctor;

    static
    {
        sUserwithAdmin = new SQLiteQueryBuilder();
        sUserwithDoctor = new SQLiteQueryBuilder();

        sUserwithAdmin.setTables(
                ConferenceContract.AdminEntry.TABLE_NAME
        );

        sUserwithDoctor.setTables(
                ConferenceContract.DoctorEntry.TABLE_NAME
        );
    }

    private static final String sAdminIDSelection =
            ConferenceContract.AdminEntry.TABLE_NAME + "." + ConferenceContract.AdminEntry._ID + " = ? ";

    private static final String sAdminNameSelection =
            ConferenceContract.AdminEntry.TABLE_NAME + "." + ConferenceContract.AdminEntry.COLUMN_USERNAME + " = ? ";

    private static final String sDoctorIDSelection =
            ConferenceContract.DoctorEntry.TABLE_NAME + "." + ConferenceContract.DoctorEntry._ID + " = ? ";

    private static final String sDoctorNameSelection =
            ConferenceContract.DoctorEntry.TABLE_NAME + "." + ConferenceContract.DoctorEntry.COLUMN_USERNAME + " = ? ";

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ConferenceContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ConferenceContract.PATH_ADMIN, ADMIN);
        matcher.addURI(authority, ConferenceContract.PATH_ADMIN + "/*", ADMIN_WITH_ID);
        matcher.addURI(authority, ConferenceContract.PATH_ADMIN + "/*/#", ADMIN_WITH_USERNAME);

        matcher.addURI(authority, ConferenceContract.PATH_DOCTOR, DOCTOR);
        matcher.addURI(authority, ConferenceContract.PATH_DOCTOR + "/*", DOCTOR_WITH_ID);
        matcher.addURI(authority, ConferenceContract.PATH_DOCTOR + "/*/#", DOCTOR_WITH_USERNAME);

        matcher.addURI(authority, ConferenceContract.PATH_CONFERENCE, CONFERENCE);

        matcher.addURI(authority, ConferenceContract.PATH_SUGGESTION, SUGGESTION);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mConferenceDatabaseHelper = new ConferenceDatabaseHelper(getContext());
        return true;
    }

    private Cursor getAdminByUserName(Uri uri, String[] projection, String sortOrder)
    {
        String username = ConferenceContract.AdminEntry.getAdminNameFromUri(uri);

        return sUserwithAdmin.query(mConferenceDatabaseHelper.getReadableDatabase(),
                projection,
                sAdminNameSelection,
                new String[]{username},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDoctorByUserName(Uri uri, String[] projection, String sortOrder)
    {
        String username = ConferenceContract.DoctorEntry.getDoctorNameFromUri(uri);

        return sUserwithDoctor.query(mConferenceDatabaseHelper.getReadableDatabase(),
                projection,
                sDoctorNameSelection,
                new String[]{username},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAdminByID(Uri uri, String[] projection, String sortOrder)
    {
        long id = ConferenceContract.AdminEntry.getAdminIDFromUri(uri);
        return sUserwithAdmin.query(mConferenceDatabaseHelper.getReadableDatabase(),
                projection,
                sAdminIDSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDoctorByID(Uri uri, String[] projection, String sortOrder)
    {
        long id = ConferenceContract.DoctorEntry.getDoctorIDFromUri(uri);
        return sUserwithDoctor.query(mConferenceDatabaseHelper.getReadableDatabase(),
                projection,
                sDoctorIDSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Cursor retCursor;
        switch (sUriMatcher.match(uri))
        {
            case ADMIN:{
                retCursor = mConferenceDatabaseHelper.getReadableDatabase().query(
                        ConferenceContract.AdminEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case ADMIN_WITH_ID: {
                retCursor = getAdminByID(uri, projection, sortOrder);
                break;
            }

            case ADMIN_WITH_USERNAME:{
                retCursor = getAdminByUserName(uri, projection, sortOrder);
                break;
            }

            case DOCTOR:{
                retCursor = mConferenceDatabaseHelper.getReadableDatabase().query(
                        ConferenceContract.DoctorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case DOCTOR_WITH_ID: {
                retCursor = getDoctorByID(uri, projection, sortOrder);
                break;
            }

            case DOCTOR_WITH_USERNAME:{
                retCursor = getDoctorByUserName(uri, projection, sortOrder);
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
            case ADMIN:
                return ConferenceContract.AdminEntry.CONTENT_TYPE;
            case ADMIN_WITH_ID:
                return ConferenceContract.AdminEntry.CONTENT_ITEM_TYPE;
            case ADMIN_WITH_USERNAME:
                return ConferenceContract.AdminEntry.CONTENT_ITEM_TYPE;
            case DOCTOR:
                return ConferenceContract.DoctorEntry.CONTENT_TYPE;
            case DOCTOR_WITH_ID:
                return ConferenceContract.DoctorEntry.CONTENT_ITEM_TYPE;
            case DOCTOR_WITH_USERNAME:
                return ConferenceContract.DoctorEntry.CONTENT_ITEM_TYPE;
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
    public Uri insert(Uri uri, ContentValues values)
    {
        final SQLiteDatabase db = mConferenceDatabaseHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri))
        {
            case ADMIN:
            {
                long _id = db.insert(ConferenceContract.AdminEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ConferenceContract.AdminEntry.buildAdminUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case DOCTOR:
            {
                long _id = db.insert(ConferenceContract.DoctorEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ConferenceContract.DoctorEntry.buildDoctorUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CONFERENCE:
            {
                long _id = db.insert(ConferenceContract.ConferenceEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ConferenceContract.ConferenceEntry.buildConferenceUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SUGGESTION:
            {
                long _id = db.insert(ConferenceContract.SuggestionEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ConferenceContract.SuggestionEntry.buildSuggestionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mConferenceDatabaseHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (sUriMatcher.match(uri))
        {
            case CONFERENCE:
                rowsDeleted = db.delete(
                        ConferenceContract.ConferenceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SUGGESTION:
                rowsDeleted = db.delete(
                        ConferenceContract.ConferenceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mConferenceDatabaseHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri))
        {
            case ADMIN:
                rowsUpdated = db.update(ConferenceContract.AdminEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case DOCTOR:
                rowsUpdated = db.update(ConferenceContract.DoctorEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CONFERENCE:
                rowsUpdated = db.update(ConferenceContract.ConferenceEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SUGGESTION:
                rowsUpdated = db.update(ConferenceContract.SuggestionEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
