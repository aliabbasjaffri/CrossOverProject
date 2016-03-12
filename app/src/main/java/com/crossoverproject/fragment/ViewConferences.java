package com.crossoverproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.crossoverproject.R;
import com.crossoverproject.activity.AdminActivity;


public class ViewConferences extends Fragment
{
    ListView listView;
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

        return view;
    }
}
