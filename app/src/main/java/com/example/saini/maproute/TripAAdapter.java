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

public class TripAAdapter extends ArrayAdapter<Order> {

    private List<Order> cardList = new ArrayList<Order>();

    static class CardViewHolder {
        TextView drivername;
        TextView customername;
        TextView cus_phone;
        TextView status;
        TextView driv_phone;
        TextView feedback;

    }

    public TripAAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Order object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Order getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.trips_a_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.customername = (TextView) row.findViewById(R.id.customername);
            viewHolder.drivername = (TextView) row.findViewById(R.id.drivername);
            viewHolder.cus_phone = (TextView) row.findViewById(R.id.cus_phonenumber);
            viewHolder.driv_phone = (TextView) row.findViewById(R.id.d_phonenumber);
            viewHolder.status = (TextView) row.findViewById(R.id.status);
            viewHolder.feedback = (TextView) row.findViewById(R.id.feedback);

            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }
        Order card = getItem(position);
        viewHolder.customername.setText(card.getCustomer().getCus_name());
        viewHolder.cus_phone.setText(Long.toString(card.getCustomer().getPhone()));
        viewHolder.status.setText(card.getStatus());
        viewHolder.feedback.setText(card.getFeedback());

        if(card.getDriver()!=null) {
            viewHolder.drivername.setText(card.getDriver().getDriver_name());
            viewHolder.driv_phone.setText(Long.toString(card.getDriver().getPhone_no()));
        }

        return row;
    }
}
