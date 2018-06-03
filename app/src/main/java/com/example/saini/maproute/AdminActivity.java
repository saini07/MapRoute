package com.example.saini.maproute;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    EditText adminid,password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        adminid=(EditText)findViewById(R.id.adminid);
        password=(EditText)findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(adminid.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())){
                    Intent i=new Intent(AdminActivity.this,MapsActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Fill in the details",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}