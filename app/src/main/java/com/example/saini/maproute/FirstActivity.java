package com.example.saini.maproute;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button customer,driver,admin;
    public static int user =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        driver = (Button) findViewById(R.id.driver);
        admin = (Button) findViewById(R.id.admin);
        customer = (Button) findViewById(R.id.customer);


        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = 2;
                Intent i = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(i);
            }
        });


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = 1;
                Intent i = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(i);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = 0;
                Intent i = new Intent(getApplicationContext(),AdminActivity.class);
                startActivity(i);
            }
        });
    }
}
