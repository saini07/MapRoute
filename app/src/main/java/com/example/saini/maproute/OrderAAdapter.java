package com.example.saini.maproute;

import android.content.Context;
import android.content.Intent;
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

public class OrderAAdapter extends ArrayAdapter<OrderDcard> {

    DatabaseReference databaseOrder;
    String id;
    String stat;
    Context ctx;
    private List<OrderDcard> cardList = new ArrayList<OrderDcard>();
    public static int driver_seletion_tag =0;

    static class CardViewHolder {
        TextView customername;
        TextView phone;
        TextView status;
        TextView feedback;
        Button accept;
        Button deny;


    }

    public OrderAAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.ctx = context;
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





        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = cardList.get(position).getId();
               // databaseOrder.child(id).child("accepted").setValue("forwarded");




                Intent i = new Intent(getContext(),DriverSelection.class);
                i.putExtra("id",id);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);




               //viewHolder.accept.setVisibility(View.GONE);
              //  viewHolder.deny.setVisibility(View.GONE);


            }
        });

        viewHolder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = cardList.get(position).getId();
                databaseOrder.child(id).child("accepted").setValue("rejected");

                cardList.clear();
               // viewHolder.accept.setVisibility(View.GONE);

               //viewHolder.deny.setVisibility(View.GONE);

            }
        });



        return row;
    }
}
