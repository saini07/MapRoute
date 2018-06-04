package com.example.saini.maproute;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TripDAdapter extends ArrayAdapter<OrderCard> {

    private List<OrderCard> cardList = new ArrayList<OrderCard>();

    static class CardViewHolder {
        TextView drivername;
        TextView phone;
        TextView status;
        TextView load;
        TextView feedback;

    }

    public TripDAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(OrderCard object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public OrderCard getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.orders_cus_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.drivername = (TextView) row.findViewById(R.id.drivername);
            viewHolder.phone = (TextView) row.findViewById(R.id.phonenumber);
            viewHolder.load = (TextView) row.findViewById(R.id.load);
            viewHolder.status = (TextView) row.findViewById(R.id.status);
            viewHolder.feedback = (TextView) row.findViewById(R.id.feedback);

            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        OrderCard card = getItem(position);
        viewHolder.drivername.setText(card.getName());
        viewHolder.phone.setText(card.getPhone());
        viewHolder.load.setText(card.getLoads());
        viewHolder.status.setText(card.getStatus());
        viewHolder.feedback.setText(card.getFeedback());

        return row;
    }
}
