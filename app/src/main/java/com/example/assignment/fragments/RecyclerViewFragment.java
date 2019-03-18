package com.example.assignment.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.assignment.R;

import com.example.assignment.WeatherActivity;


public class RecyclerViewFragment extends Fragment {


    public RecyclerViewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //create to transfer data
        Bundle bundle = this.getArguments();
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        WeatherActivity WeatherActivity = (WeatherActivity) getActivity();
        recyclerView.setAdapter(WeatherActivity.getAdapter(bundle.getInt("day")));
        return view;
    }

}
