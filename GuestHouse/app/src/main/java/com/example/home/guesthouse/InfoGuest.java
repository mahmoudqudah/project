package com.example.home.guesthouse;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class InfoGuest extends AppCompatActivity {

    private RecyclerView mGuestlist;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<GuestData , GuestViewHolder> firebaseRecyclerAdapter;

    String passAdmin =null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_guest);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Guest");

        mDatabase.keepSynced(true);

        mGuestlist = (RecyclerView) findViewById(R.id.Guest_list);
        mGuestlist.setHasFixedSize(true);
        mGuestlist.setLayoutManager(new LinearLayoutManager(this));





















    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }



    @Override
    protected void onStart() {

        super.onStart();






         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GuestData, GuestViewHolder>(
                GuestData.class
                ,R.layout.guest_row,
                GuestViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final GuestViewHolder viewHolder, final GuestData model, final int position) {





                final String post_key =getRef(position).getKey();


                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        viewHolder.setName("Guest Name :- "+ (String) dataSnapshot.child(post_key).child("Name").getValue());
                        viewHolder.setRoomNum("Room Number :- " +(String) dataSnapshot.child(post_key).child("RoomNum").getValue());
                        viewHolder.setAddress("Address :-" +(String) dataSnapshot.child(post_key).child("Address").getValue());
                        viewHolder.setHealth("Health Status :-"+ (String) dataSnapshot.child(post_key).child("HealthGuest").getValue());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });







                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //////////////////////////////// Toast.makeText(InfoGuest.this , " click",Toast.LENGTH_LONG).show();

                        final Dialog dialog =new Dialog(InfoGuest.this);
                        dialog.setTitle("Are you Admin ?");
                        dialog.setContentView(R.layout.dialog_layout);
                        dialog.show();

                        final EditText editText = (EditText)dialog.findViewById(R.id.loginAdminPassField);
                        final Button goInfo= (Button)dialog.findViewById(R.id.GoBtn);

                        goInfo.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                passAdmin = editText.getText().toString().trim();

                                if(!TextUtils.isEmpty(passAdmin)&& passAdmin.equals("Admin123321")){

                                    Intent removeInfo = new Intent(InfoGuest.this, Guest_Info_Single.class);
                                    removeInfo.putExtra("post_id",post_key);
                                    removeInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(removeInfo);
                                    dialog.cancel();

                                }
                                else {Toast.makeText(InfoGuest.this , "Password not valid try again",Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                }
                            }
                        });

                    }
                });




            }
        };

        mGuestlist.setAdapter(firebaseRecyclerAdapter);




    }






    public static class GuestViewHolder extends RecyclerView.ViewHolder{
        View mView;

        FirebaseAuth mAuth;






        public GuestViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mAuth = FirebaseAuth.getInstance();





        }



        // setName method
        public void setName(String name){
            TextView Guest_Name = (TextView) mView.findViewById(R.id.Guest_name);
            Guest_Name.setText(name);

        }
        // setRoom method
        public void setRoomNum(String roomNum){
            TextView Guest_room = (TextView) mView.findViewById(R.id.Guest_room);
            Guest_room.setText(roomNum);

        }
        // setAddress method
        public void setAddress(String address){
            TextView Guest_Address = (TextView) mView.findViewById(R.id.Guest_address);
            Guest_Address.setText(address);

        }
        // setHealth method
        public void setHealth(String healthStatus){
            TextView Guest_Health = (TextView) mView.findViewById(R.id.Guest_Health);
            Guest_Health.setText(healthStatus);

        }









    }///class GuestViewHolder



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.actiom_add){
            startActivity(new Intent(InfoGuest.this,PostActivity.class));

        }
        if(item.getItemId() == R.id.action_logout){

            logout();

        }
        if(item.getItemId() == R.id.action_about){

            Intent aboutAppIntent =new Intent(InfoGuest.this , AboutActivity.class);
            startActivity(aboutAppIntent);

        }
        if(item.getItemId() == R.id.action_settings){
            Intent setupIntent = new Intent(InfoGuest.this, SetupActivity.class);
            setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(setupIntent);
        }
        if(item.getItemId() == R.id.actiom_admin){

            /// hooon wsslana

            ///////////

            /////////////

            ///////////



            final Dialog dialog =new Dialog(InfoGuest.this);
            dialog.setTitle("Are you Admin ?");
            dialog.setContentView(R.layout.dialog_layout);
            dialog.show();

            final EditText editText = (EditText)dialog.findViewById(R.id.loginAdminPassField);
            final Button goInfo= (Button)dialog.findViewById(R.id.GoBtn);

            goInfo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    passAdmin = editText.getText().toString().trim();

                    if(!TextUtils.isEmpty(passAdmin)&& passAdmin.equals("Admin123321")){

                        Intent adminIntent = new Intent(InfoGuest.this, AdminActivity.class);
                        adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(adminIntent);
                        dialog.cancel();

                    }
                    else {Toast.makeText(InfoGuest.this , "Password not valid try again",Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                }
            });



        }
        if(item.getItemId() == R.id.actiom_info){
            Intent infoIntent = new Intent(InfoGuest.this, InfoGuest.class);
            infoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(infoIntent);
        }


        return super.onOptionsItemSelected(item);
    }


    private void logout() {

            mAuth.signOut();

    }



}
