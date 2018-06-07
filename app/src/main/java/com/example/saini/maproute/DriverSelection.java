package com.example.saini.maproute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverSelection extends AppCompatActivity {

    private DriverSelectAdapter driverSelectAdapter;
    private ListView listView;
    private String orderid;
    Toolbar toolbar;

    DatabaseReference databaseDrivers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_selection);
        listView = (ListView) findViewById(R.id.a_driver_select_list);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Drivers");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        orderid = getIntent().getStringExtra("id");



        databaseDrivers = FirebaseDatabase.getInstance().getReference("Driver");




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        driverSelectAdapter = new DriverSelectAdapter(getApplicationContext(), R.layout.driver_select_a,orderid);
        reload();
    }

    private void reload() {
        databaseDrivers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot drivers : dataSnapshot.getChildren()) {
                    Driver driver = drivers.getValue(Driver.class);

                    if(driver.getIsavailable().equals("true")) {
                        driverSelectAdapter.add(driver);
                    }


                }

                listView.setAdapter(driverSelectAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
