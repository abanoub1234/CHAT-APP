package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.gesture.GestureLibraries;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity
{
    private MessageAdapter messageAdapter;

    private RecyclerView recyclerView;
    private ImageView profImg;
    private ImageView imgSendMessage;
    private ProgressBar progressBar;
    private TextView txtChattingWith;
    private EditText edtMessageInput;

    private ArrayList<Message> Messages;

    private String usernameOfTheRoommate , emailOfRoommate , chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().hide();

        usernameOfTheRoommate = getIntent().getStringExtra("username_of_roommate");
        emailOfRoommate = getIntent().getStringExtra("email_of_roommate");


        recyclerView = findViewById(R.id.recycler_messages);
        edtMessageInput = findViewById(R.id.edt_txt);
        progressBar = findViewById(R.id.progress_messages);
        txtChattingWith = findViewById(R.id.txt_ChattingWith);
        profImg = findViewById(R.id.img_toolbar);
        imgSendMessage = findViewById(R.id.imgSendMessage);

        txtChattingWith.setText(usernameOfTheRoommate);

        Messages = new ArrayList<>();

        imgSendMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail() , emailOfRoommate , edtMessageInput.getText().toString()));
                edtMessageInput.setText("");
            }
        });

        messageAdapter  = new MessageAdapter(MessageActivity.this, Messages, getIntent().getStringExtra("my_img"), getIntent().getStringExtra("pic_of_roommate"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("pic_of_roommate")).placeholder(R.drawable.man).error(R.drawable.man).into(profImg);
        setUpChatRoom();

    }


    private void setUpChatRoom()
    {
        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
            {
                String myUsername = snapshot.getValue(User.class).getUsername();

                if(usernameOfTheRoommate.compareTo(myUsername) > 0)
                {
                   chatRoomId =  myUsername + usernameOfTheRoommate;
                }
                else if(usernameOfTheRoommate.compareTo(myUsername) == 0)
                {
                    chatRoomId =  myUsername + usernameOfTheRoommate;
                }
                else
                {
                    chatRoomId = usernameOfTheRoommate + myUsername;
                }

                attachMessageListener(chatRoomId);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error)
            {

            }
        });
    }




    private void attachMessageListener(String chatRoomId)
    {
        //هنا استخدمنا ValueEventListener علشان كل ما توصل رساله يعمل تحديث لان لو اخترنا السينجل مش هيعمل غير مره واحده بس
        FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
            {
                Messages.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren() )
                {
                    Messages.add(dataSnapshot.getValue(Message.class));
                }

                messageAdapter.notifyDataSetChanged();

                recyclerView.scrollToPosition(Messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error)
            {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.feature_menu, menu);
        return true;
    }
}