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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DriverSelectAdapter extends ArrayAdapter<Driver> {

    DatabaseReference databaseDriver;
    DatabaseReference databaseOrder;
    String id;
   String orderid;
    Context ctx;
    Driver select_driver;

    private List<Driver> cardList = new ArrayList<Driver>();

    static class CardViewHolder {
        TextView drivername;
        TextView phone;
        TextView load;
        TextView type;
        Button assign;

    }

    public DriverSelectAdapter(@NonNull Context context, int resource,String orderid) {
        super(context, resource);
        databaseDriver = FirebaseDatabase.getInstance().getReference("Driver");
        databaseOrder = FirebaseDatabase.getInstance().getReference("order");
        this.orderid = orderid;
        ctx = context;
    }

    @Override
    public void add(Driver object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Driver getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
      final CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.driver_select_a, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.drivername = (TextView) row.findViewById(R.id.drivername);
            viewHolder.phone = (TextView) row.findViewById(R.id.phonenumber);
            viewHolder.load = (TextView) row.findViewById(R.id.load);
            viewHolder.type = (TextView) row.findViewById(R.id.type);
            viewHolder.assign = (Button) row.findViewById(R.id.assign);


            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        Driver card = getItem(position);
        viewHolder.drivername.setText(card.getDriver_name());
        viewHolder.phone.setText(Long.toString(card.getPhone_no()));
        viewHolder.load.setText(Long.toString(card.getLoads()));
        viewHolder.type.setText(card.getVehicle_type());

        id = cardList.get(position).getId();


        viewHolder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select_driver = cardList.get(position);

                Toast.makeText(getContext(),"Order ssigned to driver "+ select_driver.getDriver_name(),Toast.LENGTH_LONG).show();
                databaseOrder.child(orderid).child("driver").setValue(select_driver);
                databaseOrder.child(orderid).child("accepted").setValue("forwarded");
                OrderAAdapter.driver_seletion_tag=1;
                //viewHolder.assign.setVisibility(View.GONE);




            }
        });





        return row;
    }
}
