package com.example.myapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.HistoryAdapter;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    ArrayList<String> listHistory = new ArrayList<>();
    HistoryAdapter adapter;

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void updateData(String word) {
        listHistory.add(word);
        if (adapter != null) adapter.searchNewWord(word);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ListView listView = view.findViewById(R.id.history_lv);
        adapter = new HistoryAdapter(getContext(), listHistory);
        listView.setAdapter(adapter);
        return view;
    }
}