package com.example.saini.maproute;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class TrackList extends ArrayAdapter<Tracker> {

    private Activity context;
    private List<Tracker> list;

    public TrackList(Activity context, List<Tracker> list) {
        super(context,R.layout.list_layout,list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listItem = layoutInflater.inflate(R.layout.list_layout,null,true);

        TextView from_lay = (TextView) listItem.findViewById(R.id.from_lay);
        TextView to_lay = (TextView) listItem.findViewById(R.id.to_lay);

        Tracker tracker = list.get(position);

        from_lay.setText(tracker.getFrom());
        to_lay.setText(tracker.getTo());

        return listItem;
    }
}