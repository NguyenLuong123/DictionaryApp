package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseOpenHelper;
import com.example.myapplication.listener.AdapterListener;
import com.example.myapplication.model.Word;

import java.io.IOException;
import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{

    private ArrayList<Word> words;
    private DatabaseOpenHelper databaseOpenHelper;
    private AdapterListener adapterListener;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mWordTv, mPhonenicTv;
        private final ImageButton mPlaySoundIb, mDeleteIm;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            mWordTv = view.findViewById(R.id.word_tv);
            mPhonenicTv = view.findViewById(R.id.phonetic_tv);
            mPlaySoundIb = view.findViewById(R.id.play_sound_bt);
            mDeleteIm = view.findViewById(R.id.delete_im);
        }

        public ImageButton getmPlaySoundIb() {
            return mPlaySoundIb;
        }

        public TextView getmWordTv() {
            return mWordTv;
        }

        public TextView getmPhonenicTv() {
            return mPhonenicTv;
        }

        public ImageButton getmDeleteIm() {
            return mDeleteIm;
        }
    }

    public WordAdapter(ArrayList<Word> words, DatabaseOpenHelper databaseOpenHelper, AdapterListener adapterListener) {
        this.words = words;
        this.databaseOpenHelper = databaseOpenHelper;
        this.adapterListener = adapterListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fav_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Word word = words.get(position);
        viewHolder.getmWordTv().setText(word.getWord());
        viewHolder.getmPhonenicTv().setText(word.getPhonetic_text());
        viewHolder.getmPlaySoundIb().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio(word.getPhonetic_sound());
            }
        });
        viewHolder.getmDeleteIm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
                adapterListener.onUpdateFromAdapter(word.getWord());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
       return words.size();
    }
    public void playAudio(String audioUrl) {
        MediaPlayer mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void removeItem(int position) {
        databaseOpenHelper.deleteWord(words.get(position).getWord());
        words.remove(position);
        notifyItemRemoved(position);
    }
}
