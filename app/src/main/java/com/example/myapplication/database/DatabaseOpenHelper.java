package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.myapplication.model.Word;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    private static final String SAVED_WORD_TABLE = "saved_word_table";
    private static final String WORD = "word";
    private static final String PHONETIC_TEXT = "phonetic_text";
    private static final String PHONETIC_AUDIO = "phonetic_audio";
    private static final String MEANING = "meaning";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createWordTable =  "CREATE TABLE " + SAVED_WORD_TABLE + " (" +
                WORD + " TEXT PRIMARY KEY," + PHONETIC_TEXT + " TEXT," +
                PHONETIC_AUDIO + " TEXT," + MEANING + " TEXT )";
        sqLiteDatabase.execSQL(createWordTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTableWord = "DROP TABLE IF EXISTS " + SAVED_WORD_TABLE;
        sqLiteDatabase.execSQL(dropTableWord);
        onCreate(sqLiteDatabase);
    }
    public void addWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORD, word.getWord());
        values.put(PHONETIC_TEXT, word.getPhonetic_text());
        values.put(PHONETIC_AUDIO, word.getPhonetic_sound());
        values.put(MEANING, word.getMean());
        db.insert(SAVED_WORD_TABLE, null, values);
        db.close();
    }

    public void deleteWord(String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORD, word);
        db.delete(SAVED_WORD_TABLE, WORD + " = " +"'" + word + "'", null);
        db.close();
    }

    public ArrayList<Word> getListWord() {
        ArrayList<Word> words = new ArrayList<>();
        Word word = new Word();
        String query = "SELECT * FROM " + SAVED_WORD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                word = new Word(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3)
                );
                words.add(word);
            } while (cursor.moveToNext());
        }

        return words;
    }
    public Boolean checkExist(String word) {
        ArrayList<Word> mSavedWord = new ArrayList();
        String selectQuery = "SELECT * FROM " + SAVED_WORD_TABLE + " WHERE " + WORD + " = '" + word +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        Word wordItem;
        try {
            cursor = db.rawQuery(selectQuery, null);
        } catch (SQLiteException e) {
            db.execSQL(selectQuery);
            return false;
        }
        if (cursor.moveToFirst()) {
            do {
                wordItem = new Word(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                mSavedWord.add(wordItem);
                if (mSavedWord.size() > 0) return true;
            } while (cursor.moveToNext());
        }
        return false;

    }


}
