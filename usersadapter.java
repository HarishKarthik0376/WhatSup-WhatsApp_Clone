package com.example.whatsup.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsup.Models.Users;
import com.example.whatsup.R;
import com.example.whatsup.chatscreen;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class usersadapter extends RecyclerView.Adapter<usersadapter.ViewHolder> {
    ArrayList<Users> arrlist;
    Context context;


    public usersadapter(ArrayList<Users> arrlist, Context context) {
        this.arrlist = arrlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chats,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = arrlist.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.avatar1).into(holder.profile);
        holder.name.setText(users.getUsernaame());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatscreennew = new Intent(context ,chatscreen.class);
                chatscreennew.putExtra("UserId",users.getUserid());
                chatscreennew.putExtra("Profile",users.getProfilepic());
                chatscreennew.putExtra("UserName",users.getUsernaame());
                context.startActivity(chatscreennew);

            }
        });
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid()+users.getUserid())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.hasChildren())
                       {
                           for (DataSnapshot snapshot1:snapshot.getChildren())
                           {
                               holder.msg.setText(snapshot1.child("message").getValue().toString());
                           }
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return arrlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.proiflepicdisplay);
            name = itemView.findViewById(R.id.nametext);
            msg = itemView.findViewById(R.id.chatlastmessages);

        }
    }
}
