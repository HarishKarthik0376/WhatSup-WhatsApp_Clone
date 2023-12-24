package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsup.Adapter.messageadapter;
import com.example.whatsup.Fragments.ChatsFragment;
import com.example.whatsup.Models.message;
import com.example.whatsup.databinding.ActivityChatscreenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatscreen extends AppCompatActivity {
        ActivityChatscreenBinding binding;
        private FirebaseAuth mAuth;
         FirebaseDatabase database;
         RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatscreen);
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityChatscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final String senderid = mAuth.getUid();
        String recvid = getIntent().getStringExtra("UserId");
        String username = getIntent().getStringExtra("UserName");
        String profile = getIntent().getStringExtra("Profile");
        binding.chatusername.setText(username);
        recyclerView = binding.chatscreenrecyclerview;
        Picasso.get().load(profile).placeholder(R.drawable.avatar1).into(binding.chatprofilepic);
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(chatscreen.this, SignIn.class);
                startActivity(goback);
            }
        });

        final ArrayList<message> arrmsg = new ArrayList<>();
        final String senderroom = senderid+recvid;
        final String recvroom = recvid+senderid;

        final messageadapter msgadapter = new messageadapter(arrmsg,this,recvid);
        binding.chatscreenrecyclerview.setAdapter(msgadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatscreenrecyclerview.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(arrmsg.size()-1);

        database.getReference().child("chats")
                .child(senderroom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                arrmsg.clear();
                                for(DataSnapshot snapshot1 : snapshot.getChildren())
                                {
                                    message msg = snapshot1.getValue(message.class);
                                    msg.setMessageid(snapshot1.getKey());
                                    arrmsg.add(msg);
                                }
                                msgadapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.msggo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actualmsg = binding.msgvalue.getText().toString();
                if(actualmsg.equals(""))
                {
                    binding.msgvalue.setText("");
                }
                else {
                    final message message = new message(actualmsg, senderid);
                    message.setTimestamp(new Date().getTime());
                    binding.msgvalue.setText("");

                    database.getReference().child("chats")
                            .child(senderroom)
                            .push()
                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference().child("chats")
                                            .child(recvroom)
                                            .push()
                                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }

                                            });
                                }

                            });
                    recyclerView.smoothScrollToPosition(arrmsg.size()-1);

                }
            }
        });

    }
}