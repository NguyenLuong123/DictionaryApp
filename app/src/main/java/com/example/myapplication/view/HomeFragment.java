package com.example.myapplication.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Constant.ApiConstant;
import com.example.myapplication.R;
import com.example.myapplication.adapters.SuggestionAdapter;
import com.example.myapplication.api.ApiInterface;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.database.DatabaseOpenHelper;
import com.example.myapplication.listener.HomeListener;
import com.example.myapplication.model.Definition;
import com.example.myapplication.model.Meaning;
import com.example.myapplication.model.Phonetic;
import com.example.myapplication.model.ProcessData;
import com.example.myapplication.model.Word;
import com.example.myapplication.model.Word1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.embersoft.expandabletextview.ExpandableTextView;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher {
    private SearchView mSearchView;
    private TextView mWordTv, offline;
    private Word word = new Word();
    private Phonetic phonetic;
    private String example;
    private String meaning;
    private TextView mPhoneticTv;
    private ImageButton mPlaySound, mSaveWord;
    private ExpandableTextView mDefintionTv, mExampleTv, mSameMeanTv, mAntonymsTv;
    private DatabaseOpenHelper mDatabaseOpenHelper;
    private HomeListener mHomeListener;
    private LinearLayout mLinearLayout, wordll, morell;
    private CardView soundll;
    private String antonyms, synonyms;
    private String mType = "Anh-Việt";
    MediaPlayer mediaPlayer;
    public static ArrayList<Word> mWordList;

    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase myData = null;
    String DATABASE_NAME = "dictionary.db";
    ArrayList<String> listWord = new ArrayList<>();

    String[] items = {"Anh-Việt", "Việt-Anh", "Việt-Nhật", "Việt-Pháp"};

    public void setmDatabaseOpenHelper(DatabaseOpenHelper mDatabaseOpenHelper) {
        this.mDatabaseOpenHelper = mDatabaseOpenHelper;
    }

    public void setmHomeListener(HomeListener mHomeListener) {
        this.mHomeListener = mHomeListener;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment( DatabaseOpenHelper databaseOpenHelper) {
        this.mDatabaseOpenHelper = databaseOpenHelper;
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        offline = view.findViewById(R.id.offline_tv);
        mWordTv = view.findViewById(R.id.word_tv);
        mDefintionTv = view.findViewById(R.id.definition_tv);
        mExampleTv = view.findViewById(R.id.example_tv);
        mPhoneticTv = view.findViewById(R.id.phonetic_tv);
        mSearchView = view.findViewById(R.id.search_view);
        mPlaySound = view.findViewById(R.id.play_sound_bt);
        mSaveWord = view.findViewById(R.id.save_im);
        mLinearLayout =view.findViewById(R.id.show_info);
        mSameMeanTv = view.findViewById(R.id.samemean_tv);
        mLinearLayout.setVisibility(View.GONE);
        mAntonymsTv = view.findViewById(R.id.antonyms_tv);
        morell = view.findViewById(R.id.moreView);
        soundll = view.findViewById(R.id.sound_ll);
        wordll = view.findViewById(R.id.word_ll);
        Spinner spin = view.findViewById(R.id.spinner);
        ListView search_lv = view.findViewById(R.id.list_hint);
        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(getContext(), listWord);
        search_lv.setAdapter(suggestionAdapter);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                mType = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        processCopy();
        myData = getContext().openOrCreateDatabase("dictionary.db",MODE_PRIVATE, null);
        addList();
        mSaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!word.isSaved) {
                    view.setTag(word.getWord());
                    mSaveWord.setImageResource(R.drawable.ic_baseline_bookmark_added);
                    word.setMean(ProcessData.getmeanfirst(meaning));
                    mDatabaseOpenHelper.addWord(word);
                    word.isSaved = true;
                    mHomeListener.onUpdateFromHome(word);
                }
                else {
                    mSaveWord.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    word.isSaved = false;
                    mDatabaseOpenHelper.deleteWord(word.getWord());
                    mHomeListener.onUpdateFromHome(word);

                }

            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search_lv.setVisibility(View.GONE);
                mHomeListener.onUpdateFromSearch(s);
                if(isNetworkConnected()){
                    offline.setVisibility(view.GONE);
                    getWordFromApi(s);
                } else {
                    mLinearLayout.setVisibility(View.GONE);
                    Cursor cursor = myData.rawQuery("select * from tbl_edict where word = ?", new String[]{s});
                    cursor.moveToFirst();
                    String r;
                    try {
                        r = cursor.getString(2);
                        r = r.replace("<br />", "\n")
                                .replace("<C><F><I><N><Q>@", "")
                                .replace("</Q></N></I></F></C>", "")
                                .replace("(+", "--")
                                .replace("+", "\n  ").replace("=", "+  ")
                                .replace("--", "(+");
                        offline.setText(r.toString());
                        Log.d("Tag", r.toString());

                        offline.setVisibility(View.VISIBLE);

                        cursor.close();
                    } catch (Exception e){
                        Toast.makeText(getContext(), "Not found!", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (mType == items[0]) {
                    search_lv.setVisibility(View.VISIBLE);
                    String text = s;
                    suggestionAdapter.filter(text);}
                return false;
            }
        });

        mPlaySound.setVisibility(View.GONE);
        mPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio(phonetic.getAudio());
            }
        });
        return view;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    private void getWordFromApi(String wordSearch) {
        if (mType == items[0])
        {
            morell.setVisibility(View.VISIBLE);
            soundll.setVisibility(View.VISIBLE);
            wordll.setVisibility(View.VISIBLE);
            getEngWord(wordSearch);
            getWordAV(wordSearch);}
        if ( mType == items[1]) {
            getWordAV(wordSearch);
            morell.setVisibility(View.GONE);
            soundll.setVisibility(View.GONE);
            wordll.setVisibility(View.GONE);

        }
        if (mType == items[2]) {
            getWordJF(wordSearch, "dj");
            morell.setVisibility(View.GONE);
            soundll.setVisibility(View.GONE);
            wordll.setVisibility(View.GONE);
        }
        if (mType == items[3]) {
            getWordJF(wordSearch, "df");
            morell.setVisibility(View.GONE);
            soundll.setVisibility(View.GONE);
            wordll.setVisibility(View.GONE);
        }

    }
    public void playAudio(String audioUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onUpdateSaveBt(String word) {
        if (mWordTv.getText() == word) {
            mSaveWord.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void addList() {
        Cursor c = myData.query("tbl_edict", null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {String s = c.getString(1);
            if (!s.contains(" "))
                listWord.add(s);
            c.moveToNext();
        }
        c.close();
    }



    private void processCopy() {
        //private app
        File dbFile = getContext().getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(getContext(), "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getContext().getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;

    }

    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getContext().getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();
            File f = new File(getContext().getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getEngWord(String wordSearch) {
        ApiInterface retrofit = new RetrofitClient().getRetrofitInstance(ApiConstant.BaseURL0).create(ApiInterface.class);
        Call<ArrayList<Word>> call = retrofit.getWords(wordSearch);
        call.enqueue(new Callback<ArrayList<Word>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Word>> call, @NonNull Response<ArrayList<Word>> response) {
                word = response.body() != null ? response.body().get(0) : null;
                if (mLinearLayout.getVisibility() == View.VISIBLE) {
                    mWordTv.setText(word.getWord());
                    ArrayList<Phonetic> phonetics = word.getPhonetics();
                    try {
                        phonetic = phonetics.get(0);
                        for (int i = 0; i < phonetics.size(); i++) {
                            if (phonetics.get(i).getAudio() != null && phonetics.get(i).getText() != null) {
                                phonetic = phonetics.get(i);
                                break;
                            }

                        }
                        if (phonetic.getAudio() != null) mPlaySound.setVisibility(View.VISIBLE);
                        mPhoneticTv.setText(phonetic.getText());
                        word.setPhonetic_text(phonetic.getText());
                        word.setPhonetic_sound(phonetic.getAudio());
                    } catch (Exception e) {
                        mPlaySound.setVisibility(View.GONE);
                    }
                    if (mDatabaseOpenHelper.checkExist(word.getWord())) {
                        word.isSaved = true;
                        mSaveWord.setImageResource(R.drawable.ic_baseline_bookmark_added);
                    } else {
                        word.isSaved = false;
                        mSaveWord.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    }
                    example = "";
                    synonyms = "";
                    antonyms = "";

                    for (int i = 0; i < word.getMeanings().size(); i++) {
                        Meaning meaning_i = word.getMeanings().get(i);
                        ArrayList<Definition> definitions = meaning_i.getDefinitions();
                        for (int j = 0; j < definitions.size(); j++) {
                            String temp = definitions.get(j).getExample();
                            if (temp != null) {
                                example = example + "+ " + temp + "\n";
                            }
                        }
                        ArrayList<String> symnonyms_ = meaning_i.getSynonyms();
                        for (int j = 0; j < symnonyms_.size(); j++) {
                            synonyms += symnonyms_.get(j) + "\n";
                        }
                        ArrayList<String> antonyms_ = meaning_i.getAntonyms();
                        for (int j = 0; j < antonyms_.size(); j++) {
                            antonyms += antonyms_.get(j) + "\n";
                        }
                    }
                    mExampleTv.setText(example);
                    mSameMeanTv.setText(synonyms);
                    mAntonymsTv.setText(antonyms);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Word>> call, @NonNull Throwable t) {

            }
        });
    }
    private void getWordAV(String wordSearch){
        ApiInterface retrofit1 = new RetrofitClient().getRetrofitInstance(ApiConstant.BaseURL1).create(ApiInterface.class);
        String lang;
        if (mType == items[0]) lang = "en";
        else lang = "vi";
        Call<Word1> call1 = retrofit1.getWord(wordSearch, lang);
        call1.enqueue(new Callback<Word1>() {
            @Override
            public void onResponse(@NonNull Call<Word1> call, @NonNull Response<Word1> response) {
                try {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    meaning = response.body().getTraTu().get(0).getFields().getMeaning();
                    mDefintionTv.setText(ProcessData.processAV(meaning));
                } catch (Exception e) {
                    mLinearLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Word1> call, @NonNull Throwable t) {
            }
        });
    }
    private void getWordJF(String wordSearch, String lang){
        ApiInterface retrofit1 = new RetrofitClient().getRetrofitInstance(ApiConstant.BaseURL1).create(ApiInterface.class);
        Call<Word1> call2 = retrofit1.getWordJF(wordSearch, lang);
        call2.enqueue(new Callback<Word1>() {
            @Override
            public void onResponse(@NonNull Call<Word1> call, @NonNull Response<Word1> response) {
                try {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    meaning = response.body().getTraTu().get(0).getFields().getMeaning();
                    Log.d("tag", meaning);
                    if (lang == "dj") {
                        mDefintionTv.setText(ProcessData.processJanpan(meaning));
                    }
                    else mDefintionTv.setText(ProcessData.processFrance(meaning));
                } catch (Exception e) {
                    mLinearLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Not found japan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Word1> call, @NonNull Throwable t) {
            }
        });
    }
}