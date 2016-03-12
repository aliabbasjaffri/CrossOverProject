package com.crossoverproject.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crossoverproject.R;
import com.crossoverproject.activity.AdminActivity;
import com.crossoverproject.adapter.SuggestionAdapter;
import com.crossoverproject.provider.ConferenceContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class AdminActivityFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int FORECAST_LOADER = 1;
    public static final String LOG_TAG = AdminActivityFragment.class.getSimpleName();

    private ListView listView = null;
    private SharedPreferences sharedPref = null;
    SuggestionAdapter mSuggestionAdapter = null;
    int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selectedPosition";

    public AdminActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_admin_activity, container, false);
        listView = (ListView)view.findViewById(R.id.adminActivityListView);
        mSuggestionAdapter = new SuggestionAdapter(getActivity(), null, 0);
        listView.setAdapter(mSuggestionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if(cursor != null)
                {
                    ((Callback) getActivity())
                            .onItemSelected(ConferenceContract.SuggestionEntry
                                            .buildSuggestionUri(cursor.getLong(AdminActivity.COLUMN_SUGGESTION_ID))
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String sortOrder = ConferenceContract.SuggestionEntry._ID + " ASC";
        Uri SuggestionUri = ConferenceContract.SuggestionEntry.CONTENT_URI;
        return new CursorLoader(getActivity(), SuggestionUri, AdminActivity.SUGGESTION_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSuggestionAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
            listView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSuggestionAdapter.swapCursor(null);
    }

    public interface Callback
    {
        void onItemSelected(Uri uri);
    }
}
