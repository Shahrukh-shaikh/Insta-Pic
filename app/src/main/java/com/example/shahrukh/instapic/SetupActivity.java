package com.example.shahrukh.instapic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import ru.alexbykov.nopermission.PermissionHelper;

public class SetupActivity extends AppCompatActivity {

    private EditText name;
    private ImageButton image;
    private Uri Imageuri=null;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private StorageReference mstorage;
    private static final int Gallery_req=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        name = (EditText)findViewById(R.id.namepic);
        image = (ImageButton) findViewById(R.id.setUp);

        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mstorage = FirebaseStorage.getInstance().getReference().child("Profile_user");
    }

    public void profilepicButton(View view) {


        // Yaha daalna If permission already liya hua hai to niche wala code execute otherwise permission ka prompt






        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_req && resultCode == RESULT_OK){
            Uri imageuri=data.getData();
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode== RESULT_OK){
                Imageuri =result.getUri();
                image.setImageURI(Imageuri);
            } else if ((requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)) {

                Exception error = result.getError();
            }

        }
    }

    public void doneButtonClicked(View view){
       final String nameuser = name.getText().toString().trim();
        final String userid = mAuth.getCurrentUser().getUid();
        if (!TextUtils.isEmpty(nameuser) && Imageuri!=null){



            StorageReference filepath= mstorage.child(Imageuri.getLastPathSegment());
            filepath.putFile(Imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    String downloadurl = taskSnapshot.getDownloadUrl().toString();
                    mdatabase.child(userid).child("name").setValue(nameuser);
                    mdatabase.child(userid).child("Profileimg").setValue(downloadurl);


                    Intent main = new Intent(SetupActivity.this,LoginActivity.class);
                    startActivity(main);
                }
            });



        }



    }


}


