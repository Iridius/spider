package com.astra.spider.ui;

import android.R.layout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.astra.spider.R;
import com.astra.spider.dao.Entity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleListFragment extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple, container, false);

        MainActivity activity = (MainActivity) getActivity();
        List<Entity> entities = activity.getData();

        ArrayAdapter listViewAdapter = new ArrayAdapter<Entity>(getActivity(), layout.simple_list_item_1, android.R.id.text1, entities);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(listViewAdapter);

        return view;
    }
}
