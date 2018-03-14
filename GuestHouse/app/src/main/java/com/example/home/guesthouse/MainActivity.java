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

public class MainActivity extends AppCompatActivity {

    private RecyclerView mBloglist;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;

    private DatabaseReference mDatabaseLike;

    String passAdmin =null;

    FirebaseRecyclerAdapter <Blog , BlogViewHolder> firebaseRecyclerAdapter;






    private boolean mProcessLike =false ;
     String LikeButton;
    private int count =0 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);


                }
            }
        };


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase.keepSynced(true);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLike.keepSynced(true);


        mBloglist = (RecyclerView) findViewById(R.id.blog_list);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));




            checkUserExist();







    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.actiom_add){
            startActivity(new Intent(MainActivity.this,PostActivity.class));

        }
        if(item.getItemId() == R.id.action_logout){

            logout();

        }
        if(item.getItemId() == R.id.action_about){

            Intent aboutAppIntent =new Intent(MainActivity.this , AboutActivity.class);
            startActivity(aboutAppIntent);

        }
        if(item.getItemId() == R.id.action_settings){
            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
            setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(setupIntent);
        }
        if(item.getItemId() == R.id.actiom_admin){

            /// hooon wsslana

            ///////////

            /////////////

            ///////////



            final Dialog dialog =new Dialog(MainActivity.this);
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

                        Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
                        adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(adminIntent);
                        dialog.cancel();

                    }
                    else {Toast.makeText(MainActivity.this , "Password not valid try again",Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    }
                }
            });



        }
        if(item.getItemId() == R.id.actiom_info){
            Intent infoIntent = new Intent(MainActivity.this, InfoGuest.class);
            infoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(infoIntent);
        }


        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        try {
            mAuth.signOut();
        }catch(Exception e){
            progressDialog.setMessage(""+e);
            progressDialog.show();
        }
    }

    @Override
    protected void onStart() {

        super.onStart();






        mAuth.addAuthStateListener(mAuthListener);



        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class
                ,R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, Blog model, final int position) {

                final String post_key =getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setLikeBtn(post_key);
                viewHolder.mLikeNumber.setText("Likes "+count);





                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(MainActivity.this , post_key,Toast.LENGTH_LONG).show();
                        Intent singleBlogIntent =new Intent(MainActivity.this , BlogSingleActivity.class);
                        singleBlogIntent.putExtra("Blog_id", post_key);
                        startActivity(singleBlogIntent);
                    }
                });

                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mProcessLike =true ;
                       viewHolder.mLikeNumber.setText("Likes "+count);




                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(mProcessLike) {

                                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {



                                            //////////////////////
                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            //count = (int) dataSnapshot.child(post_key).getChildrenCount();
                                            mProcessLike = false;



                                        } else {
                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");

                                            mProcessLike = false;





                                        }

                                        count = (int) dataSnapshot.child(post_key).getChildrenCount();








                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                    }


                    ///////////////////////////////////////////////////////



                });


            }
        };

        mBloglist.setAdapter(firebaseRecyclerAdapter);




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }

    private void checkUserExist() {

            if (mAuth.getCurrentUser() != null){
                final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(user_id)) {
                        Intent mainIntent = new Intent(MainActivity.this, SetupActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


      }
    }



    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;


        TextView mLikeNumber;
        ImageButton mLikeBtn ;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mLikeNumber = (TextView) mView.findViewById(R.id.editTextNumOfLike);

            mLikeBtn = (ImageButton) mView.findViewById(R.id.likebtn);

            mDatabaseLike =FirebaseDatabase.getInstance().getReference().child("Likes");

            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);



        }

        public void setLikeBtn(final String post_key ){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                            mLikeBtn.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                            mLikeBtn.setBackgroundColor(Color.GRAY);
                            mLikeNumber.setTextColor(Color.RED);


                        } else {
                            mLikeBtn.setImageResource(R.mipmap.ic_thumb_up_white_24dp);
                            mLikeNumber.setTextColor(Color.WHITE);

                        }

                   /* if(processLike == true){
                        numberOfLike++;
                    }else{numberOfLike--;}

                    mDatabaseLike.child(post_key).child("numLike").setValue(numberOfLike);
                    */

                    }catch (Exception e){}


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }// setTitle method

        public void setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }// setDesc method
        public void setUsername(String username){
            TextView post_username = (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(username);

        }



        public void setImage(final Context ctx, final String image){
            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            // Picasso.with(ctx).load(image).into(post_image);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(image).into(post_image);

                }
            });


        }// setDesc method








    }///class BlogViewHolder


}
