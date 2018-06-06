package com.example.saini.maproute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewDriversA extends AppCompatActivity {
    private DriverAAdapter driverAAdapter;
    private ListView listView;

    DatabaseReference databaseDrivers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drivers_a);
        listView = (ListView) findViewById(R.id.a_driver_list);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        driverAAdapter = new DriverAAdapter(getApplicationContext(), R.layout.drivers_a_card);

        databaseDrivers = FirebaseDatabase.getInstance().getReference("Driver");

        databaseDrivers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot drivers : dataSnapshot.getChildren()) {
                    Driver driver = drivers.getValue(Driver.class);


                        driverAAdapter.add(driver);



                }

                listView.setAdapter(driverAAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
