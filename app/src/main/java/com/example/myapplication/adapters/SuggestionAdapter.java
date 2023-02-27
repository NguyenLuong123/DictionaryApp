package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.myapplication.R;

import java.util.ArrayList;

import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class SuggestionAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<String> listWord = null;
    private ArrayList<String> arraylist;

    public SuggestionAdapter(Context context, ArrayList<String> listWord) {
        mContext = context;
        this.arraylist = listWord;
        inflater = LayoutInflater.from(mContext);
        this.listWord = new ArrayList<String>();
        this.listWord.addAll(listWord);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        if (listWord.size() <= 5) {
        return listWord.size();}
        else return 5;
    }

    @Override
    public String getItem(int position) {
        return listWord.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.hint_search_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.tv_item_search);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(listWord.get(position));
        return view;
    }

    // Filter Class
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        listWord.clear();
        if (charText.length() == 0) {
            listWord.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if (wp.toLowerCase().startsWith(charText)) {
                    listWord.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}