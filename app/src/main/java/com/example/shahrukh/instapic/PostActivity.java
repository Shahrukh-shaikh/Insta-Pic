package com.example.shahrukh.instapic;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

   private static final int Gallery_Request = 2;
   private Uri uri=null;
   private ImageButton imagebutton;
   private EditText name;
   private EditText desc;
   private StorageReference storageReference;
   private FirebaseDatabase database;
   private DatabaseReference databaseReference;
   private FirebaseAuth mAuth;
   private DatabaseReference mdatauser;
   private FirebaseUser mcurrentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

      name = (EditText)findViewById(R.id.caption);
      desc = (EditText)findViewById(R.id.desc);
      storageReference = FirebaseStorage.getInstance().getReference();
      databaseReference = FirebaseDatabase.getInstance().getReference().child("Instapic");
      mAuth = FirebaseAuth.getInstance();
      mcurrentuser = mAuth.getCurrentUser();
      mdatauser = FirebaseDatabase.getInstance().getReference().child("Users").child(mcurrentuser.getUid());

    }

    public void imagebutton(View view){
        Intent galleryintent = new Intent(Intent.ACTION_PICK);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,Gallery_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_Request && resultCode == RESULT_OK){
            uri=data.getData();
            imagebutton = (ImageButton)findViewById(R.id.image);
            imagebutton.setImageURI(uri);

        }


    }

    public void Submitclicked(View view) {

       final String title = name.getText().toString().trim();
       final String des = desc.getText().toString().trim();
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(des)){
        StorageReference filepath = storageReference.child("postimage").child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              final Uri downloadurl = taskSnapshot.getDownloadUrl();
                Toast.makeText(PostActivity.this,"Upload complete",Toast.LENGTH_LONG).show();
                final DatabaseReference newPost=databaseReference.push();
               mdatauser.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                       newPost.child("title").setValue(title);
                       newPost.child("Description").setValue(des);
                       newPost.child("image").setValue(downloadurl.toString());
                       newPost.child("uid").setValue(mcurrentuser.getUid());
                       String profileomg= (String) dataSnapshot.child("Profileimg").getValue();
                       newPost.child("Profileimg").setValue(profileomg);


                    newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                Intent mintent = new Intent(PostActivity.this,MainActivity.class);
                                startActivity(mintent);
                            }
                        }
                    });
                   }


                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });


            }
        });


        }
    }
}
