package com.crossoverproject.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.provider.ConferenceDatabaseHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by aliabbasjaffri on 05/03/16.
 */
public class TestDB extends AndroidTestCase {

    public static final String LOG_TAG = TestDB.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(ConferenceDatabaseHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }


    public void testCreateDb() throws Throwable
    {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(ConferenceContract.AdminEntry.TABLE_NAME);
        tableNameHashSet.add(ConferenceContract.DoctorEntry.TABLE_NAME);
        tableNameHashSet.add(ConferenceContract.ConferenceEntry.TABLE_NAME);
        tableNameHashSet.add(ConferenceContract.SuggestionEntry.TABLE_NAME);

        mContext.deleteDatabase(ConferenceDatabaseHelper.DATABASE_NAME);
        SQLiteDatabase db = new ConferenceDatabaseHelper
                (
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        assertTrue("Error: Your database was created without both the admin entry and doctor entry tables",
                tableNameHashSet.isEmpty());

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        db.close();
    }

    public void testAdminTable() {

        ConferenceDatabaseHelper dbHelper = new ConferenceDatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues adminValues = createAdminValues();

        long adminRowId = db.insert(ConferenceContract.AdminEntry.TABLE_NAME, null, adminValues);
        assertTrue(adminRowId != -1);

        Cursor adminCursor = db.query(
                ConferenceContract.AdminEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        assertTrue( "Error: No Records returned from Admin query", adminCursor.moveToFirst() );

        validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                adminCursor, adminValues);

        assertFalse( "Error: More than one record returned from weather query",
                adminCursor.moveToNext() );

        adminCursor.close();
        dbHelper.close();
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
}
