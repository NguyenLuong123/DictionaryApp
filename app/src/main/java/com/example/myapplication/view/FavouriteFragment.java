package com.example.myapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.myapplication.R;
import com.example.myapplication.adapters.WordAdapter;
import com.example.myapplication.database.DatabaseOpenHelper;
import com.example.myapplication.listener.AdapterListener;
import com.example.myapplication.model.ProcessData;
import com.example.myapplication.model.Word;

import java.util.ArrayList;
import java.util.Objects;

public class FavouriteFragment extends Fragment{
    private DatabaseOpenHelper mDataOpenHelper;
    private RecyclerView mRecyclerView;
    public static ArrayList<Word> mWordList;
    private WordAdapter adapter;
    private AdapterListener adapterListener;

    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public FavouriteFragment() {
    }

    public void setmDataOpenHelper(DatabaseOpenHelper mDataOpenHelper) {
        this.mDataOpenHelper = mDataOpenHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        mRecyclerView = view.findViewById(R.id.fav_rv);
        mRecyclerView.setBackground(this.getResources().getDrawable(R.color.cyan_50));
        loadSavedList();
        return view;

    }
    public void updateSavedList(Word word) {
        if (word.isSaved) mWordList.add(word);
        else {
            int position = -1;
            Word wordItem = new Word();
            for (int i = 0; i < mWordList.size(); i++) {
                wordItem = mWordList.get(i);
                if (word.getWord().equals(wordItem.getWord())){
                    position = i;
                    break;
                }
            }
            mDataOpenHelper.deleteWord(word.getWord());
            mWordList.remove(position);
            adapter.notifyItemRemoved(position);

        }
        adapter.notifyDataSetChanged();

    }
    public void loadSavedList() {
        mWordList = mDataOpenHelper.getListWord();
        adapter = new WordAdapter(mWordList, mDataOpenHelper, adapterListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

}