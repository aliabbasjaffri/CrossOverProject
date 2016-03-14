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
import com.crossoverproject.R;
import com.crossoverproject.activity.AdminActivity;
import com.crossoverproject.adapter.ViewSuggestionsAdapter;
import com.crossoverproject.provider.ConferenceContract;
import com.crossoverproject.provider.ConferenceProvider;
import com.crossoverproject.utils.Settings;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSuggestions extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int FORECAST_LOADER = 0;
    public static final String LOG_TAG = ViewSuggestions.class.getSimpleName();

    ListView listView = null;
    ViewSuggestionsAdapter mViewSuggestionsAdapter = null;
    int mPosition = ListView.INVALID_POSITION;

    public ViewSuggestions()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_suggestions, container, false);
        listView = (ListView) view.findViewById(R.id.viewSuggestionsListView);
        mViewSuggestionsAdapter = new ViewSuggestionsAdapter(getActivity(), null, 0);
        listView.setAdapter(mViewSuggestionsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {
                    ((SuggestionsCallback) getActivity())
                            .onSuggestionsSelected(ConferenceContract.SuggestionEntry
                                            .buildSuggestionUri(cursor.getLong(AdminActivity.COLUMN_SUGGESTION_ID))
                            );
                }
            }
        });
        mViewSuggestionsAdapter.notifyDataSetChanged();

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
        String sortOrder = ConferenceContract.SuggestionEntry._ID+ " ASC";
        return new CursorLoader(getActivity(),
                ConferenceContract.SuggestionEntry.CONTENT_URI,
                AdminActivity.SUGGESTION_COLUMNS ,
                ConferenceProvider.sSuggestionByUserIDSelection,
                new String[]{""+ Settings.getUserID(getActivity())},
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mViewSuggestionsAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
            listView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mViewSuggestionsAdapter.swapCursor(null);
    }

    public interface SuggestionsCallback
    {
        void onSuggestionsSelected(Uri uri);
    }
}
