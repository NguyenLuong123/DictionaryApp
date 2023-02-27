package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class HistoryAdapter extends BaseAdapter {
    private ArrayList<String> arraylist;
    Context mContext;
    LayoutInflater inflater;
    public HistoryAdapter(Context context, ArrayList<String> historyLisWord) {
        mContext = context;
        this.arraylist = historyLisWord;
        inflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
       return arraylist.size();
    }

    @Override
    public String getItem(int position) {
        return arraylist.get(position);
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
        holder.name.setText(arraylist.get(position));
        return view;
    }

    // Filter Class
    public void searchNewWord(String charText) {
        arraylist.add(charText);
        if (arraylist.size() > 10) {
            arraylist.remove(10);
        }
        notifyDataSetChanged();
    }

}
