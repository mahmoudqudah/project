package com.example.home.guesthouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mConfPass;
    private Button mRegisterbtn;

    private FirebaseAuth mAuth;

    private ProgressDialog prog;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prog = new ProgressDialog(this);

        mAuth =FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mNameField = (EditText )findViewById(R.id.nameField);
        mEmailField = (EditText )findViewById(R.id.emailField);
        mPasswordField = (EditText )findViewById(R.id.passwordField);
        mConfPass = (EditText )findViewById(R.id.confirmPasswordField);

        mConfPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConfPass.setBackgroundColor(Color.GRAY);
            }
        });

        mRegisterbtn = (Button) findViewById(R.id.registerBtn);


        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });

    }

    private void startRegister() {
        final String name =mNameField.getText().toString().trim();
        String email =mEmailField.getText().toString().trim();
        String password =mPasswordField.getText().toString().trim();
        String confirm_password =mConfPass.getText().toString().trim();

        if(confirm_password.equals(password)) {

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                prog.setMessage("Signing Up ...");
                prog.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference cureent_user_db = mDatabase.child(user_id);

                            cureent_user_db.child("name").setValue(name);
                            cureent_user_db.child("image").setValue("defult...");

                            prog.dismiss();

                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);


                        }

                    }
                });


            }
            else{
                Toast.makeText(RegisterActivity.this,"Check Your Data",Toast.LENGTH_LONG).show();}
        }// confirm password
        else{mConfPass.setBackgroundColor(Color.RED);}
    }// register method


}// class RegisterActivity
