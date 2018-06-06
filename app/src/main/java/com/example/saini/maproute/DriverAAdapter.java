package com.example.saini.maproute;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DriverAAdapter extends ArrayAdapter<Driver> {

    DatabaseReference databaseDriver;
    String id;
    String stat;
    Context ctx;
    private List<Driver> cardList = new ArrayList<Driver>();

    static class CardViewHolder {
        TextView drivername;
        TextView phone;
        TextView load;
        TextView type;
        Button feedback;

    }

    public DriverAAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        databaseDriver = FirebaseDatabase.getInstance().getReference("Driver");
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
            row = inflater.inflate(R.layout.drivers_a_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.drivername = (TextView) row.findViewById(R.id.drivername);
            viewHolder.phone = (TextView) row.findViewById(R.id.phonenumber);
            viewHolder.load = (TextView) row.findViewById(R.id.load);
            viewHolder.type = (TextView) row.findViewById(R.id.type);
            viewHolder.feedback = (Button) row.findViewById(R.id.feedback);


            row.setTag(viewHolder);
        } else {
            viewHolder = (DriverAAdapter.CardViewHolder)row.getTag();
        }
        Driver card = getItem(position);
        viewHolder.drivername.setText(card.getDriver_name());
        viewHolder.phone.setText(Long.toString(card.getPhone_no()));
        viewHolder.load.setText(Long.toString(card.getLoads()));
        viewHolder.type.setText(card.getVehicle_type());

        id = cardList.get(position).getId();


        viewHolder.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = cardList.get(position).getId();
                Intent i = new Intent(getContext(),FeedbackDisplay.class);
                i.putExtra("id",id);
                ctx.startActivity(i);

                Toast.makeText(getContext(),"feedback to be displayed", Toast.LENGTH_LONG).show();

            }
        });





        return row;
    }
}
