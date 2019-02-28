package com.example.shahrukh.instapic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView)findViewById(R.id.imgv);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.myanim);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        final Intent it = new Intent(BaseActivity.this,MainActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(it);
                    finish();
                }
            }
        };
        timer.start();
    }




}
