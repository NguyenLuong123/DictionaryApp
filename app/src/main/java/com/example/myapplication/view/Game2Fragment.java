package com.example.myapplication.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseOpenHelper;
import com.example.myapplication.model.Word;

import java.util.ArrayList;
import java.util.Random;


public class Game2Fragment extends Fragment {

    private DatabaseOpenHelper databaseOpenHelper;

    private int presCounter = 0;
    int temp = 0;
    //private ArrayList<String> textAnswer = new ArrayList<>();
    public static ArrayList<Word> ListQuestion = new ArrayList<>();
    private String textQues ;
    private String textAnswer ;
    private int maxPresCounter ;
    private String[] ans = new String[50];

    TextView textScreen, textQuestion, textTitle;
    Animation smallbigforth;


    public Game2Fragment(DatabaseOpenHelper databaseOpenHelper) {
        this.databaseOpenHelper = databaseOpenHelper;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_game2, container, false);
        ListQuestion = databaseOpenHelper.getListWord();
        textAnswer = ListQuestion.get(temp).getWord();
        for (int i = 0; i < textAnswer.length(); i++) {
            ans[i] = String.valueOf(textAnswer.charAt(i));
        }

        smallbigforth = AnimationUtils.loadAnimation(getContext(), R.anim.smallbigforth);

        maxPresCounter = textAnswer.length();
        ans = shuffeArray(ans,maxPresCounter);
        for(int i = 0; i < maxPresCounter; i++) {
            String an = ans[i];
            addView(((GridLayout) view.findViewById(R.id.layoutParent)), an, ((EditText) view.findViewById(R.id.editText)), view);
        }

        maxPresCounter = textAnswer.length();

        return view;
    }

    private String[] shuffeArray(String[] ar, int maxPresCounter) {
        Random rnd = new Random();
        for (int i = maxPresCounter - 1; i > 0; i--) {
            int index = rnd.nextInt( i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    private void addView(GridLayout viewParent, final String text, final EditText editText, View view) {
        GridLayout.LayoutParams gridLayoutParams = new GridLayout.LayoutParams(

        );

        gridLayoutParams.rightMargin = 30;
        gridLayoutParams.topMargin = 30;

        final TextView textView = new TextView(getContext());

        textView.setLayoutParams(gridLayoutParams);
        textView.setBackground(this.getResources().getDrawable(R.drawable.bgpink));
        textView.setTextColor(this.getResources().getColor(R.color.colorPurple));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(32);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/FredokaOneRegular.ttf");

        textQuestion = (TextView) view.findViewById(R.id.textQuestion);
        textQuestion.setText(ListQuestion.get(temp).getMean());
        textScreen = (TextView) view.findViewById(R.id.textScreen);
        textTitle = (TextView) view.findViewById(R.id.textTitle);

        textScreen.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        editText.setTypeface(typeface);
        textView.setTypeface(typeface);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presCounter < maxPresCounter) {
                    if (presCounter == 0)
                        editText.setText("");

                    editText.setText(editText.getText().toString() + text);
                    textView.startAnimation(smallbigforth);
                    textView.animate().alpha(0).setDuration(300);
                    textView.setClickable(false);
                    presCounter++;

                    if (presCounter == maxPresCounter) {
                        doValidate(view);
                    }
                }
            }
        });

        viewParent.addView(textView);

    }

    public Game2Fragment(int temp) {
        this.temp = temp;
    }

    private void doValidate(View view) {
        presCounter = 0;
        EditText editText = view.findViewById(R.id.editText);
        GridLayout gridLayout = view.findViewById(R.id.layoutParent);

        if (editText.getText().toString().equals(textAnswer)) {
            Toast.makeText(getContext(), "Right", Toast.LENGTH_LONG).show();
            if(temp == ListQuestion.size() - 1) {
                Fragment game2boss = new Game2Boss();
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.content_main, game2boss)
                        .addToBackStack(null)
                        .commit();
                editText.setText("");
            } else {
                temp++;
                maxPresCounter = ListQuestion.get(temp).getWord().length();
                textAnswer = ListQuestion.get(temp).getWord();
                gridLayout.removeAllViews();
                for (int i = 0; i < textAnswer.length(); i++) {
                    ans[i] = String.valueOf(textAnswer.charAt(i));
                }

                ans = shuffeArray(ans, maxPresCounter);

                gridLayout.removeAllViews();
                for (int i = 0; i < maxPresCounter; i++) {
                    String an = ans[i];
                    gridLayout.removeAllViews();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editText.setText("");
                            addView(gridLayout, an, editText, view);
                        }
                    }, 200);
                    gridLayout.removeAllViews();
                }
            }
        } else {
            Toast.makeText(getContext(), "Wrong", Toast.LENGTH_LONG).show();
            editText.setText("");
            ans = shuffeArray(ans, maxPresCounter);
            gridLayout.removeAllViews();
            for (int i = 0; i < maxPresCounter; i++) {
                String an = ans[i];
                addView(gridLayout, an, editText, view);
            }
        }
    }
}