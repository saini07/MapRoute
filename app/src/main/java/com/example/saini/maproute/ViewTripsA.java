package com.example.saini.maproute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewTripsA extends AppCompatActivity {

    private TripAAdapter tripAAdapter;
    private ListView listView;
    DatabaseReference databaseOrders;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips_a);
        listView = (ListView) findViewById(R.id.a_trip_list);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Trips");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        databaseOrders = FirebaseDatabase.getInstance().getReference("order");



    }

    @Override
    protected void onResume() {
        super.onResume();
        tripAAdapter = new TripAAdapter(getApplicationContext(), R.layout.trips_a_card);
        reload();
    }

    private void reload() {
        databaseOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot orders : dataSnapshot.getChildren()) {
                    Order order = orders.getValue(Order.class);
                    Driver temp_driver = order.getDriver();
                    Customer temp_cus = order.getCustomer();

                    Log.e("bvshi order trip" , order.getId());

                    if(order.getAccepted().equals("forwarded") ) {
                        Order temp_order = order;
                        Log.e("bvshi order trip",order.getCustomer().getCus_name()+"");
                        tripAAdapter.add(temp_order);
                    }

                }


                listView.setAdapter(tripAAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
