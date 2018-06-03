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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.saini.maproute.FirstActivity.user;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonsignin;
    private TextView textView;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonsignin= (Button) findViewById(R.id.buttonsignin);

        textView = (TextView) findViewById(R.id.textview1);

        progressDialog = new ProgressDialog(this);
        textView.setOnClickListener(this);
        buttonsignin.setOnClickListener(this);
    }
    private void userlogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("signing in Please Wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"valid credentials",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),MapsActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"invalid Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view == textView){
            if( user == 1 ){
            Intent i=new Intent(this,UsersignupActivity.class);
            startActivity(i);
        }
        if(user == 2){
                Intent i=new Intent(this,DriverActivity.class);
                startActivity(i);
        }

        if(view == buttonsignin){
            userlogin();
        }

    }

}}

