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

public class History extends AppCompatActivity {

    ListView listView;
    DatabaseReference databaseReference;
    List<Tracker> retrievalList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        databaseReference =  FirebaseDatabase.getInstance().getReference("track");
        retrievalList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewTrack);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                retrievalList.clear();

                for(DataSnapshot tracks: dataSnapshot.getChildren()) {
                    Tracker tracker = tracks.getValue(Tracker.class);
                    retrievalList.add(tracker);

                }

                TrackList adapter = new TrackList(History.this,retrievalList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Tracker tracker = retrievalList.get(i);

                        String origin = tracker.getFrom();
                        String dest = tracker.getTo();
                        //Toast.makeText(getApplicationContext(),origin+dest,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        intent.putExtra("origin",origin);
                        intent.putExtra("dest",dest);
                        MapsActivity.flag = 1;
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
