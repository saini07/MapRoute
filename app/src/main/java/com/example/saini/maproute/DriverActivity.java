package com.example.saini.maproute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends NavigateActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    EditText driver_name, loads, phone_no,  vehicle_no, license, mailid, driverpassword;
    Button b1;
    Spinner vehicle_type;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
     Driver driver;
    DatabaseReference databaseDriver;
    String veh_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseDriver = FirebaseDatabase.getInstance().getReference("Driver");
        b1 = (Button) findViewById(R.id.submit_button);
        driver_name = (EditText) findViewById(R.id.driver_name);
        loads = (EditText) findViewById(R.id.loads);
        phone_no = (EditText) findViewById(R.id.phoneno);
        vehicle_type = (Spinner) findViewById(R.id.vehicle_type);
        vehicle_no = (EditText) findViewById(R.id.vehicle_no);
        license = (EditText) findViewById(R.id.license);
        mailid = (EditText) findViewById(R.id.mailid);
        driverpassword = (EditText) findViewById(R.id.driverpassword);
        vehicle_type.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Truck");
        categories.add("DCM");
        categories.add("MiniTruck");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        vehicle_type.setAdapter(dataAdapter);

        progressDialog = new ProgressDialog(this);
        b1.setOnClickListener(this);
    }

    private void registerUser() {
        String email = mailid.getText().toString().trim();
        String password = driverpassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_LONG).show();
                            //firebaseAuth.getUid();
                            driver.setId(firebaseAuth.getUid());
                            databaseDriver.child(firebaseAuth.getUid()).setValue(driver);
                            MapsActivity.driver = driver;
                            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if (view == b1) {
            if (!TextUtils.isEmpty(driver_name.getText().toString()) &&
                    !TextUtils.isEmpty(loads.getText().toString()) &&
                    !TextUtils.isEmpty(phone_no.getText().toString()) &&
                    !TextUtils.isEmpty(veh_type) &&
                    !TextUtils.isEmpty(vehicle_no.getText().toString()) &&
                    !TextUtils.isEmpty(mailid.getText().toString()) &&
                    !TextUtils.isEmpty(driverpassword.getText().toString()) &&
                    !TextUtils.isEmpty(license.getText().toString())) {

                driver = new Driver(driver_name.getText().toString(),veh_type, license.getText().toString(),
                        vehicle_no.getText().toString(), Long.valueOf(loads.getText().toString()), Long.valueOf(phone_no.getText().toString()));

                registerUser();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         veh_type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
