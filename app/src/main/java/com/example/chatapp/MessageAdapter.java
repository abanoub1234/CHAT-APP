package com.example.chatapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder>
{

    private Context context;
    private ArrayList<Message> Messages;
    private String senderImg , receiverImg;

    public MessageAdapter(Context context, ArrayList<Message> messages, String senderImg, String receiverImg)
    {
        this.context = context;
        Messages = messages;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
    }

    @NonNull
    @NotNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder , parent , false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.MessageHolder holder, int position)
    {
        //String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
       holder.textMessage.setText(Messages.get(position).getContent() );

       ConstraintLayout constraintLayout = holder.ccll;

       if(Messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
       {
           holder.textMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.message_input_background));
           Glide.with(context).load(senderImg).error(R.drawable.man).placeholder(R.drawable.man).into(holder.profImg);

           ConstraintSet constraintSet  = new ConstraintSet();
           constraintSet.clone(constraintLayout);
           constraintSet.clear(R.id.profileCardView , ConstraintSet.LEFT);
           constraintSet.clear(R.id.textMessage , ConstraintSet.LEFT);
           constraintSet.connect(R.id.profileCardView  , ConstraintSet.RIGHT , R.id.ccLayout , ConstraintSet.RIGHT , 0);
           constraintSet.connect(R.id.textMessage  , ConstraintSet.RIGHT , R.id.profileCardView , ConstraintSet.LEFT , 0);
           constraintSet.applyTo(constraintLayout);
       }
       else
       {
           holder.textMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.message_input_background_2));
           Glide.with(context).load(receiverImg).error(R.drawable.man).placeholder(R.drawable.man).into(holder.profImg);

           ConstraintSet constraintSet  = new ConstraintSet();
           constraintSet.clone(constraintLayout);
           constraintSet.clear(R.id.profileCardView , ConstraintSet.RIGHT);
           constraintSet.clear(R.id.textMessage , ConstraintSet.RIGHT);
           constraintSet.connect(R.id.profileCardView  , ConstraintSet.LEFT , R.id.ccLayout , ConstraintSet.LEFT , 0);
           constraintSet.connect(R.id.textMessage  , ConstraintSet.LEFT , R.id.profileCardView , ConstraintSet.RIGHT , 0);
           constraintSet.applyTo(constraintLayout);
       }
    }

    @Override
    public int getItemCount()
    {
        return Messages.size();
    }


    class MessageHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout ccll;
        TextView textMessage;
        ImageView profImg;


        public MessageHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);

            ccll = itemView.findViewById(R.id.ccLayout);
            textMessage = itemView.findViewById(R.id.textMessage);
            profImg = itemView.findViewById(R.id.smallProfileImage);
        }

    }


}
