package com.example.myapplication.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseOpenHelper;
import com.example.myapplication.model.Question;
import com.example.myapplication.model.Word;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Game1Fragment extends Fragment implements View.OnClickListener{
    private DatabaseOpenHelper databaseOpenHelper;

    ArrayList<Word> list = new ArrayList<>();
    Boolean[] added;
    ArrayList<Question> questions = new ArrayList<>();
    int currentquestion = 0;
    Question temtquestion;
    int totalQuestion;
    int score = 0;
    TextView ans1, ans2, ans3, ans4;
    TextView orderquestion, contentquestion, tvscore;

    public Game1Fragment(DatabaseOpenHelper databaseOpenHelper) {
        this.databaseOpenHelper = databaseOpenHelper;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game1, container, false);
        list = databaseOpenHelper.getListWord();
        totalQuestion = list.size();
        added = new Boolean[list.size()];
        Arrays.fill(added, Boolean.FALSE);
        Collections.shuffle(list);
        ans1 = view.findViewById(R.id.tvans1);
        ans2 = view.findViewById(R.id.tvans2);
        ans3 = view.findViewById(R.id.tvans3);
        ans4 = view.findViewById(R.id.tvans4);
        tvscore = view.findViewById(R.id.tvscore);
        orderquestion = view.findViewById(R.id.tvorder);
        contentquestion = view.findViewById(R.id.tvquestion);
        if(list.size() < 4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Bạn cần ít nhất 4 từ để chơi game");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return view;
        }
        getQuestion();

        if (questions.isEmpty()) return view;
        loadQuestion(questions.get(currentquestion));
        return view;
    }
    private void loadQuestion(Question question) {
        if(question == null) return;
        temtquestion = question;
        String numberquestion = "Question " + (currentquestion  + 1) + ":";
        orderquestion.setText(numberquestion);
        tvscore.setText("Điểm số: " +  score+"/" + totalQuestion);
        ans1.setBackgroundResource(R.drawable.purple_corner);
        ans2.setBackgroundResource(R.drawable.purple_corner);
        ans3.setBackgroundResource(R.drawable.purple_corner);
        ans4.setBackgroundResource(R.drawable.purple_corner);
        contentquestion.setText(list.get(currentquestion).getMean());
        ans1.setText(questions.get(currentquestion).getAnswer().get(0));
        ans2.setText(questions.get(currentquestion).getAnswer().get(1));
        ans3.setText(questions.get(currentquestion).getAnswer().get(2));
        ans4.setText(questions.get(currentquestion).getAnswer().get(3));
        ans1.setOnClickListener(this);
        ans2.setOnClickListener(this);
        ans3.setOnClickListener(this);
        ans4.setOnClickListener(this);

    }
    private ArrayList<Question> getQuestion() {
        for (int i = 0; i < totalQuestion; i++) {
            Question qu = creatQuestion(list.get(i), i);
            questions.add(qu);
        }
        return questions;
    }

    private Question creatQuestion(Word word,int j) {
        Question question;
        ArrayList<String>listans = new ArrayList<>();
        listans.add(word.getWord());
        Random random = new Random();
        int randomPos = random.nextInt(totalQuestion);
        Arrays.fill(added,Boolean.FALSE);
        added[j] =  true;
        for(int i = 0; i < 3; i++){
            while (randomPos == totalQuestion || added[randomPos])
                randomPos = random.nextInt(list.size());
            listans.add(list.get(randomPos).getWord());
            added[randomPos] = true;
        }
        Collections.shuffle(listans);
        question = new Question(listans, word);
        return question;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvans1:
                ans1.setBackgroundResource(R.drawable.yellow_corner);
                checkAnswer(ans1, temtquestion, temtquestion.getAnswer().get(0));
                break;
            case R.id.tvans2:
                ans2.setBackgroundResource(R.drawable.yellow_corner);
                checkAnswer(ans2, temtquestion, temtquestion.getAnswer().get(1));
                break;
            case R.id.tvans3:
                ans3.setBackgroundResource(R.drawable.yellow_corner);
                checkAnswer(ans3, temtquestion, temtquestion.getAnswer().get(2));
                break;
            case R.id.tvans4:
                ans4.setBackgroundResource(R.drawable.yellow_corner);
                checkAnswer(ans4, temtquestion, temtquestion.getAnswer().get(3));
                break;
        }


    }
    private void checkAnswer(TextView textView, Question question, String answer) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(answer.equals(question.getContent().getWord())) {
                    textView.setBackgroundResource(R.drawable.green_corner);
                    score ++;
                    tvscore.setText("Điểm số: " + score + "/" + totalQuestion);
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.correct);
                    mp.start();
                } else {
                    textView.setBackgroundResource(R.drawable.red_corner);
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.incorrect);
                    mp.start();
                    showAnswerCorrect(temtquestion);

                }
                nextQuestion();
            }
        }, 500);
    }

    private void showAnswerCorrect(Question question) {
        if (question == null || question.getAnswer() == null) {
            return;
        }
        int i = question.findCorrect();
        if (i == 0) {
            ans1.setBackgroundResource(R.drawable.green_corner);
        } else if(i == 1) {
            ans2.setBackgroundResource(R.drawable.green_corner);
        } else if (i == 2) {
            ans3.setBackgroundResource(R.drawable.green_corner);
        } else if (i == 3) {
            ans4.setBackgroundResource(R.drawable.green_corner);
        }
    }

    private void nextQuestion() {
        if (currentquestion == totalQuestion - 1) {
            showDialog("end game");
        } else {
            currentquestion++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadQuestion(questions.get(currentquestion));
                }
            }, 2000);

        }
    }
    private void  showDialog(String mess){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(mess).setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                currentquestion = 0;
                score = 0;
                loadQuestion(questions.get(currentquestion));
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}