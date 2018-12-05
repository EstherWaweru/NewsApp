package com.example.esther.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RugbyAdapter extends ArrayAdapter<Rugby> {
    public RugbyAdapter(Context context, List<Rugby> rugbies) {
        super(context, 0, rugbies);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.items, parent, false);
        }

        // Find the rugby news in the correct position in the list
        Rugby currentRugbyNews = getItem(position);

        // Find the TextView for the news title
        TextView titleView = (TextView) listItemView.findViewById(R.id.news_title);
        String title=currentRugbyNews.getNewsTitle();
        titleView.setText(title);


        TextView sectionView= (TextView) listItemView.findViewById(R.id.section_text);
        String section=currentRugbyNews.getNewsSection();
        sectionView.setText(section);

        // Find the TextView with view ID location offset
        TextView dateView= (TextView) listItemView.findViewById(R.id.publication_date);
        String date=currentRugbyNews.getPublicationDate();
        dateView.setText(date);


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
