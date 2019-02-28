package com.example.shahrukh.instapic;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.*;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private TextView hid;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText)findViewById(R.id.loginEmail);
        password = (EditText)findViewById(R.id.loginPass);

        hid = (TextView)findViewById(R.id.invalid);
        hid.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


    }

    public void loginButtonClicked(View view){
        String log = login.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(!TextUtils.isEmpty(log) && !TextUtils.isEmpty(pass)){
          mAuth.signInWithEmailAndPassword(log,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                      checkUserExists();
                  }else{
                      hid.setVisibility(View.VISIBLE);
                  }
              }
          });
        }

    }

    public void checkUserExists(){
        final String userid = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userid)){
                    Intent log = new Intent(LoginActivity.this,MainActivity.class);
                    log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(log);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Signup(View view){
        Intent reg = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(reg);

    }

    /*@Override
    public void onBackPressed(){
        System.gc();
        System.exit(0);
    }*/

    private Boolean exit = false;
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            makeText(this, "Press Back again to Exit.",
                    LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            }, 1000);
        }
    }
}
