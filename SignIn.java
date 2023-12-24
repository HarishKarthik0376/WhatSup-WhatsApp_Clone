package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsup.Models.Users;
import com.example.whatsup.databinding.ActivitySignInBinding;
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

import java.util.HashMap;

public class SignIn extends AppCompatActivity {
    ActivitySignInBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    Button Googleclick;
    ProgressDialog progressDialog;
    int RC_SIGN_IN = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        Googleclick = findViewById(R.id.signupgoogle);
        database = FirebaseDatabase.getInstance();
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Validating Credentials!!");

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

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.signupemail.getText().toString().isEmpty()&&!binding.signuppassword.getText().toString().equals(""))
                {
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(binding.signupemail.getText().toString(),binding.signuppassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                        }
                                    },4000);
                                    if(task.isSuccessful())
                                    {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(SignIn.this, "Entering WhatSup!!", Toast.LENGTH_SHORT).show();
                                                Intent log = new Intent(SignIn.this, MainActivity.class);
                                                startActivity(log);
                                                finish();
                                            }
                                        },4000);
                                    }
                                    else
                                    {   new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SignIn.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    },4000);

                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(SignIn.this, "Please Enter The Credentials!!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        if(mAuth.getCurrentUser()!=null)
        {
            Intent redirect = new Intent(SignIn.this,MainActivity.class);
            startActivity(redirect);
            finish();
        }

        TextView clicktosignup;
        clicktosignup = findViewById(R.id.signupnew);
        Intent signup;
        signup = new Intent(SignIn.this, Signup.class);
        clicktosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(signup);
            }
        });
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
                           Intent in = new Intent(SignIn.this, MainActivity.class);
                           startActivity(in);
                            Toast.makeText(SignIn.this, "Entering WhatSup", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(SignIn.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}