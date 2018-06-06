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

public class UsersignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Emailid;
    private EditText userpassword;
    private Button button;
    private EditText Cus_name;
    private EditText phone;


    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseCustomer;
     Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersignup);
        firebaseAuth = FirebaseAuth.getInstance();
        Emailid = (EditText) findViewById(R.id.editText1);
        userpassword = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        Cus_name=(EditText)findViewById(R.id.cus_name);
        phone=(EditText)findViewById(R.id.phone);
        databaseCustomer = FirebaseDatabase.getInstance().getReference("Customer");

        progressDialog = new ProgressDialog(this);
        button.setOnClickListener(this);
    }

    private void registerUser(String email,String password){

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Registration success",Toast.LENGTH_LONG).show();
                            customer.setId(firebaseAuth.getUid());
                            MapsActivity.customer=customer;
                            databaseCustomer.child(firebaseAuth.getUid()).setValue(customer);
                            Intent i=new Intent(getApplicationContext(),MapsActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(),"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == button){
            String email = Emailid.getText().toString().trim();
            String password  = userpassword.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
                return;
            }
           /* if(!(TextUtils.isEmpty(Cus_name.getText().toString()))||!(TextUtils.isEmpty(phone.getText().toString()))) {
                Toast.makeText(this,"Enter correct details",Toast.LENGTH_LONG).show();
                return;
            }*/

            customer = new Customer(Cus_name.getText().toString(),Long.valueOf(phone.getText().toString()));

            registerUser(email,password);
        }
    }

}
