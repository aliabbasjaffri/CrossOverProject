package com.crossoverproject.fragment;


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
import android.widget.Toast;

import com.crossoverproject.R;
import com.crossoverproject.activity.AdminActivity;
import com.crossoverproject.activity.DoctorActivity;
import com.crossoverproject.adapter.ConferenceAdapter;
import com.crossoverproject.adapter.ViewConferenceAdapter;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.provider.ConferenceProvider;
import com.crossoverproject.utils.Settings;


public class ViewConferences extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int FORECAST_LOADER = 0;
    public static final String LOG_TAG = ViewConferences.class.getSimpleName();

    ListView listView = null;
    ViewConferenceAdapter mViewConferenceAdapter = null;
    int mPosition = ListView.INVALID_POSITION;

    public ViewConferences() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_view_conferences, container, false);
        listView = (ListView) view.findViewById(R.id.viewConferencesListView);
        mViewConferenceAdapter = new ViewConferenceAdapter(getActivity(), null, 0);
        listView.setAdapter(mViewConferenceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {
                    ((ConferenceCallback) getActivity())
                            .onConferenceSelected(ConferenceContract.ConferenceEntry
                                            .buildConferenceUri(cursor.getLong(DoctorActivity.COLUMN_CONFERENCE_ID))
                            );
                }
            }
        });
        mViewConferenceAdapter.notifyDataSetChanged();

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
        return new CursorLoader(getActivity(),
                ConferenceContract.ConferenceEntry.CONTENT_URI,
                DoctorActivity.CONFERENCE_COLUMNS ,
                ConferenceProvider.sConferenceByUserIDSelection,
                new String[]{""+Settings.getUserID(getActivity())},
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mViewConferenceAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
            listView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mViewConferenceAdapter.swapCursor(null);
    }

    public interface ConferenceCallback
    {
        void onConferenceSelected(Uri uri);
    }
}
