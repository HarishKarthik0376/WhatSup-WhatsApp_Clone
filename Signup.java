package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsup.Models.Users;
import com.example.whatsup.databinding.ActivitySignupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        setContentView(binding.getRoot());
        TextView clicktologin;
        clicktologin = findViewById(R.id.signupexist);
        Intent existing;
        existing = new Intent(Signup.this, SignIn.class);
        clicktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(existing);
            }
        });
        progressDialog = new ProgressDialog(Signup.this);
        progressDialog.setTitle("Creating An Account");
        progressDialog.setMessage("We're Creating Your Account.");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        binding.signupgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        binding.signupclick.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if(!binding.signupname.getText().toString().equals("")&&!binding.signupemail.getText().toString().equals("")&&!binding.signuppassword.getText().toString().equals(""))
                {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.signupemail.getText().toString(),binding.signuppassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            },4000);
                            if(task.isComplete())
                            {
                                Users users = new Users(binding.signupname.getText().toString(),binding.signupemail.getText().toString(),binding.signuppassword.getText().toString());
                                String uid = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(uid).setValue(users);

                                new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Signup.this, "SignUp Successfull!!", Toast.LENGTH_SHORT).show();
                                    Intent log = new Intent(Signup.this, SignIn.class);
                                    startActivity(log);
                                    finish();
                                }
                            },4000);


                            }
                            else
                            {   new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Signup.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            },4000);

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Signup.this, "Please Enter The Credentials!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if(mAuth.getCurrentUser()!=null)
        {
            Intent redirect = new Intent(Signup.this, MainActivity.class);
            startActivity(redirect);
            finish();
        }
    }
    private void googleSignIn() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Users users = new Users();
                            String uid = task.getResult().getUser().getUid();
                            users.setUserid(user.getUid());
                            users.setEmail(user.getEmail());
                            users.setProfilepic(user.getPhotoUrl().toString());
                            users.setUsernaame(user.getDisplayName());
                            database.getReference().child("Users").child(uid).setValue(users);
                            Intent in = new Intent(Signup.this, MainActivity.class);
                            startActivity(in);
                            Toast.makeText(Signup.this, "Entering WhatSup", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(Signup.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}