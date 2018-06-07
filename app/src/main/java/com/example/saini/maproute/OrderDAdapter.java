package com.example.saini.maproute;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDAdapter extends ArrayAdapter<OrderDcard> {

    DatabaseReference databaseOrder;
    String id;
    String stat;
    private List<OrderDcard> cardList = new ArrayList<OrderDcard>();

    static class CardViewHolder {
        TextView customername;
        TextView phone;
        TextView status;
        TextView feedback;
        Button accept;
        Button deny;
        Button complete;

    }

    public OrderDAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        databaseOrder = FirebaseDatabase.getInstance().getReference("order");

    }

    @Override
    public void add(OrderDcard object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public OrderDcard getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.orders_dr_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.customername = (TextView) row.findViewById(R.id.customername);
            viewHolder.phone = (TextView) row.findViewById(R.id.phonenumber);
            viewHolder.status = (TextView) row.findViewById(R.id.status);
            viewHolder.feedback = (TextView) row.findViewById(R.id.feedback);
            viewHolder.accept = (Button) row.findViewById(R.id.accept);
            viewHolder.deny = (Button) row.findViewById(R.id.deny);
            viewHolder.complete = (Button) row.findViewById(R.id.complete);

            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        OrderDcard card = getItem(position);
        viewHolder.customername.setText(card.getName());
        viewHolder.phone.setText(card.getPhone());
        viewHolder.status.setText(card.getStatus());
        viewHolder.feedback.setText(card.getFeedback());


        id = cardList.get(position).getId();

        if(cardList.get(position).getStatus().equals("processing")) {
            viewHolder.accept.setVisibility(View.GONE);
            viewHolder.deny.setVisibility(View.GONE);
            viewHolder.complete.setVisibility(View.VISIBLE);
        }





        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               id = cardList.get(position).getId();
               databaseOrder.child(id).child("status").setValue("processing");

               //databaseOrder.child(id).child("driver").setValue(MapsActivity.driver);

                cardList.clear();

               viewHolder.deny.setVisibility(View.GONE);
               viewHolder.complete.setVisibility(View.GONE);

            }
        });

        viewHolder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = cardList.get(position).getId();
                databaseOrder.child(id).child("accepted").setValue("default");

                databaseOrder.child(id).child("driver").setValue(null);
                cardList.clear();

                viewHolder.accept.setVisibility(View.GONE);
                viewHolder.complete.setVisibility(View.GONE);
                viewHolder.deny.setVisibility(View.GONE);

            }
        });

        viewHolder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = cardList.get(position).getId();

                databaseOrder.child(id).child("status").setValue("completed");
                cardList.clear();
                viewHolder.complete.setVisibility(View.GONE);

            }
        });

        return row;
    }


}
