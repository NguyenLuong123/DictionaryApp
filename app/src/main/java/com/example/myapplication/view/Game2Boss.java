package com.example.myapplication.view;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class Game2Boss extends Fragment {

    TextView textScreen, textQuestion, textTitle, textBtn;
    ImageView bigboss;
    Animation smalltobig;

    public Game2Boss() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boss, container, false);

        smalltobig = AnimationUtils.loadAnimation(getContext(), R.anim.smalltobig);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/FredokaOneRegular.ttf");

        textQuestion = (TextView) view.findViewById(R.id.textQuestion);
        textScreen = (TextView) view.findViewById(R.id.textScreen);
        textTitle = (TextView) view.findViewById(R.id.textTitle);
        textBtn = (TextView) view.findViewById(R.id.textBtn);

        bigboss = (ImageView) view.findViewById(R.id.bigboss);
        bigboss.startAnimation(smalltobig);

        textQuestion.setTypeface(typeface);
        textScreen.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        textBtn.setTypeface(typeface);

        textBtn.setClickable(true);
        textBtn.setOnClickListener(v -> {
            Fragment reviewfragment = new ReviewFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.content_main, reviewfragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}