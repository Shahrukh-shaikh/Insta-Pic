package com.example.shahrukh.instapic;

import android.content.Intent;
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

    private EditText nameField;
    private EditText emailField;
    private EditText passField;
    private Button register;
    private FirebaseAuth mauth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameField = (EditText)findViewById(R.id.nameField);

        emailField = (EditText)findViewById(R.id.emailField);

        passField = (EditText)findViewById(R.id.pass);

        mauth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        

        }

    public void registerButton(View view){
        final String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String pass = passField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

            mauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
                        String userid = mauth.getCurrentUser().getUid();
                        DatabaseReference currentuser = mDatabase.child(userid);
                        currentuser.child("Name").setValue(name);
                        currentuser.child("Image").setValue("default");

                        Intent mainIntent = new Intent(RegisterActivity.this,SetupActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    }

                }
            });
        }

    }

    public void clickhere(View view){
        Intent log= new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(log);
    }
}
