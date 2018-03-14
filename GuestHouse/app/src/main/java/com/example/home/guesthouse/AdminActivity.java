package com.example.home.guesthouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminActivity extends AppCompatActivity {

    private EditText mName,mRoomNum,mAdress,mHealthStatus;
    private Button mAddBtn;



    private ProgressDialog mProgr;

    DatabaseReference mDatabase;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mProgr = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();



        mName = (EditText)findViewById(R.id.editTextName);
        mRoomNum = (EditText)findViewById(R.id.editTextRoomNum);
        mAdress = (EditText)findViewById(R.id.editTextAdress);
        mHealthStatus = (EditText)findViewById(R.id.editTextHealthStatus);
        mAddBtn =(Button)findViewById(R.id.AddGuestBtn);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Guest");











        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddInfo();
            }
        });




    }

    // start add information
    private void startAddInfo() {



        mProgr.setMessage("Adding Guest ...");


        final String Name_val =mName.getText().toString().trim();
        final String RoomNum_val=mRoomNum.getText().toString().trim();
        final String Address_val=mAdress.getText().toString().trim();
        final String HealthStatus_val=mHealthStatus.getText().toString().trim();

        if(!TextUtils.isEmpty(Name_val) && !TextUtils.isEmpty(RoomNum_val) && !TextUtils.isEmpty(Address_val) && !TextUtils.isEmpty(HealthStatus_val) ){

            mProgr.show();


            final DatabaseReference Guest_info = mDatabase.push();

            Guest_info.child("Name").setValue(Name_val);
            Guest_info.child("RoomNum").setValue(RoomNum_val);
            Guest_info.child("Address").setValue(Address_val);
            Guest_info.child("HealthGuest").setValue(HealthStatus_val);

            mProgr.dismiss();

            Intent infoIntent = new Intent(AdminActivity.this, InfoGuest.class);
            infoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(infoIntent);










        }else{
            Toast.makeText(AdminActivity.this , "Enter data " , Toast.LENGTH_LONG).show();

        }


    }
}
