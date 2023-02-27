package com.example.myapplication.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class TranslateFragment extends Fragment {
    public TranslateFragment() {
        // Required empty public constructor
    }
    private EditText edtLanguage;
    private TextView translateLanguageTV;
    private Button translateLanguageBtn;

    // create a variable for our
    // firebase language translator.
    FirebaseTranslator englishVietTranslator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        // below line we are specifying our source language.
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        // in below line we are displaying our target language.
                        .setTargetLanguage(FirebaseTranslateLanguage.VI)
                        // after that we are building our options.
                        .build();
        englishVietTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        edtLanguage = view.findViewById(R.id.idEdtLanguage);
        translateLanguageTV = view.findViewById(R.id.idTVTranslatedLanguage);
        translateLanguageBtn = view.findViewById(R.id.idBtnTranslateLanguage);
        translateLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to download language
                // modal to which we have to translate.
                String string = edtLanguage.getText().toString();
                downloadModal(string);
            }
        });
        return view;
    }
    private void downloadModal(String input) {
        // below line is use to download the modal which
        // we will require to translate in german language
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().requireWifi().build();

        // below line is use to download our modal.
        englishVietTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void aVoid) {

                // this method is called when modal is downloaded successfully.
                Toast.makeText(getContext(), "Please wait language modal is being downloaded.", Toast.LENGTH_SHORT).show();

                // calling method to translate our entered text.
                translateLanguage(input);

            }
        }).addOnFailureListener(new OnFailureListener() {

            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail to download modal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void translateLanguage(String input) {
        englishVietTranslator.translate(input).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                translateLanguageTV.setText(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail to translate", Toast.LENGTH_SHORT).show();
            }
        });
    }
}