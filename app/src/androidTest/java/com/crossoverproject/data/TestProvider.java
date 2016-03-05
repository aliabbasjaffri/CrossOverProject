package com.crossoverproject.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.provider.ConferenceDatabaseHelper;
import com.crossoverproject.provider.ConferenceProvider;

import java.util.Map;
import java.util.Set;

/**
 * Created by aliabbasjaffri on 05/03/16.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                ConferenceContract.AdminEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ConferenceContract.DoctorEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                ConferenceContract.AdminEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Admin table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                ConferenceContract.DoctorEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Doctor table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }


    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                ConferenceProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: ConferenceProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + ConferenceContract.CONTENT_AUTHORITY,
                    providerInfo.authority, ConferenceContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: ConferenceProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    public void testGetType()
    {
        String type = mContext.getContentResolver().getType(ConferenceContract.AdminEntry.CONTENT_URI);

        assertEquals("Error: the AdminEntry CONTENT_URI should return AdminEntry.CONTENT_TYPE",
                ConferenceContract.AdminEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(
                ConferenceContract.AdminEntry.buildAdminUri(10));
    }


    public void testBasicAdminQuery() {
        // insert our test records into the database
        ConferenceDatabaseHelper dbHelper = new ConferenceDatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues adminValues = createAdminValues();
        db.insert(ConferenceContract.AdminEntry.TABLE_NAME , null , adminValues);

        db.close();

        Cursor adminCursor = mContext.getContentResolver().query(
                ConferenceContract.AdminEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor("testBasicAdminQuery", adminCursor, adminValues);
    }

    public void testUpdateAdmin() {
        // Create a new map of values, where column names are the keys
        ContentValues values = createAdminValues();

        Uri adminUri = mContext.getContentResolver().
                insert(ConferenceContract.AdminEntry.CONTENT_URI, values);
        long adminRowId = ContentUris.parseId(adminUri);

        // Verify we got a row back.
        assertTrue(adminRowId != -1);
        Log.d(LOG_TAG, "New row id: " + adminRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(ConferenceContract.AdminEntry._ID, adminRowId);
        updatedValues.put(ConferenceContract.AdminEntry.COLUMN_NAME, "Mr. Joe");

        Cursor locationCursor = mContext.getContentResolver().query(ConferenceContract.AdminEntry.CONTENT_URI, null, null, null, null);

        int count = mContext.getContentResolver().update(
                ConferenceContract.AdminEntry.CONTENT_URI, updatedValues, ConferenceContract.AdminEntry._ID + "= ?",
                new String[] { Long.toString(adminRowId)});
        assertEquals(count, 1);


        assert locationCursor != null;
        locationCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                ConferenceContract.AdminEntry.CONTENT_URI,
                null,   // projection
                ConferenceContract.AdminEntry._ID + " = " + adminRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        validateCursor("testUpdateAdmin.  Error validating Admin entry update.",
                cursor, updatedValues);

        assert cursor != null;
        cursor.close();
    }

    static ContentValues createAdminValues()
    {
        ContentValues adminValues = new ContentValues();
        adminValues.put(ConferenceContract.AdminEntry.COLUMN_USERNAME, "Helloa");
        adminValues.put(ConferenceContract.AdminEntry.COLUMN_PASSWORD, "Helloa");
        adminValues.put(ConferenceContract.AdminEntry.COLUMN_NAME, "Aliabbas");
        adminValues.put(ConferenceContract.AdminEntry.COLUMN_AGE, 25);
        adminValues.put(ConferenceContract.AdminEntry.COLUMN_SEX, "Male");


        return adminValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }
}
