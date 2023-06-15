package com.example.denicruise.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.denicruise.R;

import org.w3c.dom.Text;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<String> countryList;
    List<String> dateList;
    List<Integer> flags;
    LayoutInflater inflter;


    public CustomAdapter(Context applicationContext, List<String> countryList, List<Integer> flags, List<String> dateList) {
        this.context = context;
        this.countryList = countryList;
        this.flags = flags;
        this.dateList = dateList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView country = (TextView) view.findViewById(R.id.textView);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        country.setText(countryList.get(i) + " - " + dateList.get(i));
        icon.setImageResource(flags.get(i));
        return view;
    }
}