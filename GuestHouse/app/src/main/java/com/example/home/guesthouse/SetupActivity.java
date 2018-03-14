package com.example.home.guesthouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {

    private ImageButton mSetupImageBtn;
    private EditText mNameField;
    private Button mSubmetBtn;
    private Uri imageUri =null;
    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth mAuth ;

    private StorageReference mStorageImage;

    private ProgressDialog progressDialog;


    private static final int GALLERY_REQUEST = 1 ;

    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_setup);

            progressDialog = new ProgressDialog(this);

            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

            mAuth = FirebaseAuth.getInstance();

            mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_Images");
            prog = new ProgressDialog(this);


            mSetupImageBtn = (ImageButton) findViewById(R.id.setupImageBtn);
            mNameField = (EditText) findViewById(R.id.setupNameField);
            mSubmetBtn = (Button) findViewById(R.id.setupSubmetBtn);
        }catch (Exception e){
            progressDialog.setMessage(""+e);
            progressDialog.show();
        }

        //on click submet Button
        try {


            mSubmetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startSetupAccount();
                }
            });



        // mSetupImageBtn on cleck
        mSetupImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent =new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });// mSetupImageBtn on cleck




        }catch (Exception e){

            progressDialog.setMessage(""+e);
            progressDialog.show();

        }
    }

    // startSetupAccount method
    private void startSetupAccount() {
        try{
        final String name = mNameField.getText().toString().trim();
        final String user_id = mAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(name) && imageUri != null) {
            prog.setMessage("Finishing Setuo ...");
            prog.show();

            StorageReference filepath = mStorageImage.child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    mDatabaseUsers.child(user_id).child("name").setValue(name);
                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);

                    prog.dismiss();

                    Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }
            });


        }
    } catch (Exception e){

            progressDialog.setMessage(""+e);
            progressDialog.show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();

                mSetupImageBtn.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }catch (Exception e){

        progressDialog.setMessage(""+e);
        progressDialog.show();

    }


    }
}
