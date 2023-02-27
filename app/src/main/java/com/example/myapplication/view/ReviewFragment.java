package com.example.myapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseOpenHelper;
import com.example.myapplication.model.Word;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    private DatabaseOpenHelper mDataOpenHelper;
    public void setmDataOpenHelper(DatabaseOpenHelper mDataOpenHelper) {
        this.mDataOpenHelper = mDataOpenHelper;
    }
    private ArrayList<Word> words;
    public ReviewFragment() {
        // Required empty public constructor
    }
    private ImageButton mPlayGame1Bt, mPlayGame2Bt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        mPlayGame1Bt = view.findViewById(R.id.play_game1_bt);
        mPlayGame2Bt = view.findViewById(R.id.play_game2_bt);
        mPlayGame1Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game1Fragment game1 = new Game1Fragment(mDataOpenHelper);
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.content_main, game1)
                        .addToBackStack(null)
                        .commit();
            }
        });
        mPlayGame2Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game2Fragment game2 = new Game2Fragment(mDataOpenHelper);
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.content_main, game2)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}