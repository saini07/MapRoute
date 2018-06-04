package com.example.saini.maproute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverActivity extends NavigateActivity implements View.OnClickListener {

    EditText driver_name, loads, phone_no, vehicle_type, vehicle_no, license, mailid, driverpassword;
    Button b1;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
     Driver driver;
    DatabaseReference databaseDriver;

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
        vehicle_type = (EditText) findViewById(R.id.vehicle_type);
        vehicle_no = (EditText) findViewById(R.id.vehicle_no);
        license = (EditText) findViewById(R.id.license);
        mailid = (EditText) findViewById(R.id.mailid);
        driverpassword = (EditText) findViewById(R.id.driverpassword);
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
                    !TextUtils.isEmpty(vehicle_type.getText().toString()) &&
                    !TextUtils.isEmpty(vehicle_no.getText().toString()) &&
                    !TextUtils.isEmpty(mailid.getText().toString()) &&
                    !TextUtils.isEmpty(driverpassword.getText().toString()) &&
                    !TextUtils.isEmpty(license.getText().toString())) {

                driver = new Driver(driver_name.getText().toString(), vehicle_type.getText().toString(), license.getText().toString(),
                        vehicle_no.getText().toString(), Long.valueOf(loads.getText().toString()), Long.valueOf(phone_no.getText().toString()));

                registerUser();
            }
        }
    }
}
