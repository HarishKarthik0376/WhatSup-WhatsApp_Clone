package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


import com.example.whatsup.Adapter.fragmentadapter;
import com.example.whatsup.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseAuth mAuth;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.viewpagers.setAdapter(new fragmentadapter(getSupportFragmentManager()));
        binding.maintab.setupWithViewPager(binding.viewpagers);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Logging Out");
        progressDialog.setMessage("Please Wait!!");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menuoptions,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menusettings) {
            Toast.makeText(this, "Settings!!", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.GroupChat)
        {
            Toast.makeText(this, "Settings!!", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.Logout) {

            mAuth.signOut();
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Logged Out!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent logout = new Intent(MainActivity.this,SignIn.class);
                    startActivity(logout);
                    finish();
                }
            },4000);

        }
        return super.onOptionsItemSelected(item);
    }
}