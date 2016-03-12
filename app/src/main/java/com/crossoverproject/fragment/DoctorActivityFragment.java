package com.crossoverproject.fragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crossoverproject.R;
import com.crossoverproject.adapter.ConferenceAdapter;
import com.crossoverproject.provider.ConferenceContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int FORECAST_LOADER = 0;
    public static final String LOG_TAG = DoctorActivityFragment.class.getSimpleName();

    private ListView listView = null;
    ConferenceAdapter mConferenceAdapter = null;
    private static final String SELECTED_KEY = "selectedPosition";
    int mPosition = ListView.INVALID_POSITION;

    private static final String[] CONFERENCE_COLUMNS = {
            ConferenceContract.ConferenceEntry.TABLE_NAME + "." + ConferenceContract.ConferenceEntry._ID,
            ConferenceContract.ConferenceEntry.COLUMN_USER_ID,
            ConferenceContract.ConferenceEntry.COLUMN_TOPIC,
            ConferenceContract.ConferenceEntry.COLUMN_SUMMARY,
            ConferenceContract.ConferenceEntry.COLUMN_DATE,
            ConferenceContract.ConferenceEntry.COLUMN_LOCATION,
            ConferenceContract.ConferenceEntry.COLUMN_READ_TAG
    };

    public static final int COLUMN_CONFERENCE_ID = 0;
    public static final int COLUMN_USER_ID = 1;
    public static final int COLUMN_TOPIC = 2;
    public static final int COLUMN_SUMMARY = 3;
    public static final int COLUMN_DATE = 4;
    public static final int COLUMN_LOCATION = 5;
    public static final int COLUMN_READ_TAG = 6;

    public DoctorActivityFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_doctor_activity, container, false);
        listView = (ListView)view.findViewById(R.id.doctorActivityListView);
        mConferenceAdapter = new ConferenceAdapter(getActivity(), null, 0);
        listView.setAdapter(mConferenceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if(cursor != null)
                {
                    ((Callback) getActivity())
                            .onItemSelected(ConferenceContract.ConferenceEntry
                                    .buildConferenceUri(cursor.getLong(COLUMN_CONFERENCE_ID))
                            );
                }

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = ConferenceContract.ConferenceEntry._ID+ " ASC";
        Uri ConferenceUri = ConferenceContract.ConferenceEntry.CONTENT_URI;
        return new CursorLoader(getActivity(), ConferenceUri, CONFERENCE_COLUMNS , null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mConferenceAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
            listView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mConferenceAdapter.swapCursor(null);
    }

    public interface Callback
    {
        void onItemSelected(Uri uri);
    }
}
