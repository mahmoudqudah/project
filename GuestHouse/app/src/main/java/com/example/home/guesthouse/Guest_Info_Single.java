package com.example.home.guesthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Guest_Info_Single extends AppCompatActivity {

    private TextView editName ;
    private TextView editRoom ;
    private TextView editAddres ;
    private TextView editHealth;
    Button mRemove;

    private FirebaseAuth mAuth;

    private String mPost_key =null ;
    private DatabaseReference mDatabase ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest__info__single);

        mAuth=FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Guest");

        mPost_key =getIntent().getExtras().getString("post_id");



        editName =(TextView)findViewById(R.id.singleGuestName);
        editRoom =(TextView)findViewById(R.id.singleRoomNum);
        editAddres =(TextView)findViewById(R.id.singleGuestAddress);
        editHealth =(TextView)findViewById(R.id.singleGuestHealth);

        mRemove =(Button)findViewById(R.id.removeBtn);



        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String guest_name = (String) dataSnapshot.child("Name").getValue();
                String guest_room =(String) dataSnapshot.child("RoomNum").getValue();
                String guest_address =(String) dataSnapshot.child("Address").getValue();
                String guest_health =(String) dataSnapshot.child("HealthGuest").getValue();

                editName.setText(guest_name);
                editRoom.setText(guest_room);
                editAddres.setText(guest_address);
                editHealth.setText(guest_health);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(mPost_key).removeValue();
                Intent infoIntent =new Intent(Guest_Info_Single.this,InfoGuest.class);
                startActivity(infoIntent);
            }
        });










    }
}
