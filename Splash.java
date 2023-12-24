package com.example.whatsup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        if(mAuth.getCurrentUser()!=null)
        {
            Intent redirect = new Intent(Splash.this,MainActivity.class);
            startActivity(redirect);
            finish();
        }
        else
        {
            Intent splash = new Intent(Splash.this,Signup.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(splash);
                    finish();
                }
            },3000);
        }
    }

}