package com.example.saini.maproute;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

public class PaymentActivity extends AppCompatActivity {

    EditText mailid,amount;
    Button pay;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mailid = (EditText) findViewById(R.id.email);
        amount = (EditText) findViewById(R.id.amount);
        pay = (Button) findViewById(R.id.pay);



        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Payment");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getFname = MapsActivity.customer.getCus_name();
                String getPhone = Long.toString(MapsActivity.customer.getPhone());
                String getEmail = mailid.getText().toString().trim();
                String getAmt   = amount.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(), PayMentGateWay.class);
                intent.putExtra("FIRST_NAME",getFname);
                intent.putExtra("PHONE_NUMBER",getPhone);
                intent.putExtra("EMAIL_ADDRESS",getEmail);
                intent.putExtra("RECHARGE_AMT",getAmt);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
