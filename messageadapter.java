package com.example.whatsup.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsup.Models.message;
import com.example.whatsup.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class messageadapter extends RecyclerView.Adapter {
    ArrayList<message> messages = new ArrayList<>();
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    String recvid;
    RecyclerView recyclerView;

    public messageadapter(ArrayList<message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    public messageadapter() {
    }

    public messageadapter(ArrayList<message> messages, Context context, String recvid) {
        this.messages = messages;
        this.context = context;
        this.recvid = recvid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.samplesender1,parent,false);
            return new SendViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.samplereciever,parent,false);
            return new RecvViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


            message message = messages.get(position);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setMessage("Are You Sure You Want To Delete The Message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    String SenderRoom = FirebaseAuth.getInstance().getUid()+recvid;
                                    database.getReference().child("chats").child(SenderRoom)
                                            .child(message.getMessageid())
                                            .setValue(null);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                    return false;
                }
            });
            if(holder.getClass()==SendViewHolder.class)
            {
                ((SendViewHolder)holder).sendmsg.setText(message.getMessage());
                Date date = new Date(message.getTimestamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String thetime = simpleDateFormat.format(date);
                ((SendViewHolder)holder).sendtimer.setText(thetime);
            }
            else
            {
                ((RecvViewHolder)holder).recvmsg.setText(message.getMessage());
                Date date = new Date(message.getTimestamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String thetime = simpleDateFormat.format(date);
                ((RecvViewHolder)holder).recvtime.setText(thetime);
            }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class RecvViewHolder extends RecyclerView.ViewHolder
    {
        TextView recvmsg,recvtime;

        public RecvViewHolder(@NonNull View itemView) {
            super(itemView);
            recvmsg = itemView.findViewById(R.id.msgreciever);
            recvtime = itemView.findViewById(R.id.recvtimer);

        }
    }
    public class SendViewHolder extends RecyclerView.ViewHolder
    {
        TextView sendmsg,sendtimer;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            sendmsg = itemView.findViewById(R.id.msgsender1);
            sendtimer = itemView.findViewById(R.id.sendertimer);
        }
    }
}
