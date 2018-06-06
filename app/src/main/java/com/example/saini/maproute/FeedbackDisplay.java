package com.example.saini.maproute;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedbackDisplay extends AppCompatActivity {

    ListView listView;
    DatabaseReference databaseOrder;
    List<Order> feedbacklist;
    String driverid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_display);
        databaseOrder =  FirebaseDatabase.getInstance().getReference("order");
        feedbacklist = new ArrayList<>();
        listView = (ListView) findViewById(R.id.a_feedback_list);
        driverid = getIntent().getStringExtra("id");
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                feedbacklist.clear();

                for(DataSnapshot orders: dataSnapshot.getChildren()) {
                    Order order = orders.getValue(Order.class);
                    if(order.getDriver().getId().equals(driverid)) {
                        feedbacklist.add(order);
                    }

                }

                FeedbackList adapter = new FeedbackList(FeedbackDisplay.this,feedbacklist);
                listView.setAdapter(adapter);


//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Order order = feedbacklist.get(i);
//
//                        String id = tracker.getFrom();
//                        String dest = tracker.getTo();
//                        //Toast.makeText(getApplicationContext(),origin+dest,Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//                        intent.putExtra("origin",origin);
//                        intent.putExtra("dest",dest);
//                        MapsActivity.flag = 1;
//                        startActivity(intent);
//                    }
//                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
