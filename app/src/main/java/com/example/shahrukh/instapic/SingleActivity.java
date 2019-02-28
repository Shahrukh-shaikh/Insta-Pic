package com.example.shahrukh.instapic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleActivity extends AppCompatActivity {

    private String postkey = null;
    private DatabaseReference mdatabase;
    private ImageView singleimage;
    private TextView singletitle;
    private TextView singledesc;
    private Button deletebutton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        postkey = getIntent().getExtras().getString("Postid");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Instapic");
        mAuth=FirebaseAuth.getInstance();

        singleimage = (ImageView) findViewById(R.id.singleImage);
        singletitle = (TextView)findViewById(R.id.singleTitle);
        singledesc = (TextView)findViewById(R.id.singleDec);

        deletebutton = (Button) findViewById(R.id.deletebutton);
        deletebutton.setVisibility(View.INVISIBLE);

        mdatabase.child(postkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String posttitle= (String) dataSnapshot.child("title").getValue();
                String postdes = (String) dataSnapshot.child("Description").getValue();
                String postuid = (String) dataSnapshot.child("uid").getValue();
                String postimage = (String) dataSnapshot.child("image").getValue();

                singletitle.setText(posttitle);
                singledesc.setText(postdes);
                Picasso.with(SingleActivity.this).load(postimage).into(singleimage);

                if (mAuth.getCurrentUser().getUid().equals(postuid)){

                    deletebutton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

        public void deleteButtonClicked(View view){
        mdatabase.child(postkey).removeValue();
            Intent mainintent = new Intent(SingleActivity.this,MainActivity.class);
            startActivity(mainintent);
        }



}
