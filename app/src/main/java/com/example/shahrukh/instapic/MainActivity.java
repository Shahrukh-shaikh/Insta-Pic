package com.example.shahrukh.instapic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    private RecyclerView minstalist;
    private DatabaseReference mdatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        minstalist =(RecyclerView) findViewById(R.id.insta_list);
        minstalist.setHasFixedSize(true);
        minstalist.setLayoutManager(new LinearLayoutManager(this));
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Instapic");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser()==null){
                Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);

            }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter <Insta,InstaViewHolder> FBRA = new FirebaseRecyclerAdapter<Insta, InstaViewHolder>(

                Insta.class,
                R.layout.insta_row,
                InstaViewHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(InstaViewHolder viewHolder, Insta model, int position) {

                final String postkey = getRef(position).getKey().toString();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setusername(model.getUsername());

                viewHolder.setProfileimg(getApplicationContext(),model.getProfileimg());

                viewHolder.setImage(getApplicationContext(),model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleactivity = new Intent(MainActivity.this,SingleActivity.class);
                        singleactivity.putExtra("Postid",postkey);
                        startActivity(singleactivity);
                    }
                });


            }
        };
        minstalist.setAdapter(FBRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static class InstaViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public InstaViewHolder(View itemView){
            super(itemView);
        mView = itemView;

        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.texttitle);
           post_title.setText(title);
        }

        public void setusername(String username){
            TextView post_username = (TextView)mView.findViewById(R.id.username);
            post_username.setText(username);
        }
        public void setDesc(String desc){
            TextView post_desc = (TextView)mView.findViewById(R.id.description);
            post_desc.setText(desc);
        }

        public void setProfileimg(Context ctx,String Profileimg){
            ImageView img = (ImageView)mView.findViewById(R.id.profile);
            Picasso.with(ctx).load(Profileimg).into(img);
        }

        public void setImage(Context ctx,String image){
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.addicon){
            Intent intent = new Intent(MainActivity.this,PostActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.Logout){
            mAuth.signOut();

        }

        return super.onOptionsItemSelected(item);
    }

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
