package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class search_post extends AppCompatActivity {
    ArrayList<PostFeed2> postFeedsList = new ArrayList<PostFeed2>();
    UserfeedAdapter2 userFeedAdapter;
    RecyclerView recyclerView;
    ListView listView;
    String name, userId;
    private String Item_Description;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("postFeeds");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_post);
        recyclerView = findViewById(R.id.feed_search);
        listView=findViewById(R.id.listview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        Intent intent = getIntent();
        Item_Description = intent.getStringExtra("ItemDescription");
        showOwnPostFeedssearch();

    }
    private void showOwnPostFeedssearch() {
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        for (DataSnapshot ALL_USERS : dataSnapshot.getChildren()) {
                            //String dbUserId = ALL_USERS.child("userId").getValue().toString();
                            String itemDescription = ALL_USERS.child("itemDesc").getValue().toString();
                            String value = ALL_USERS.child("value").getValue().toString();
                            String contactInformation = ALL_USERS.child("cInfo").getValue().toString();
                            String Status = ALL_USERS.child("status").getValue().toString();
                            String pid = ALL_USERS.child("pid").getValue().toString();
                            Log.e("data",Status);
                            if (itemDescription.equals(Item_Description)) {

                                postFeedsList.add(new PostFeed2(userId,itemDescription,value,contactInformation,pid));
                                userFeedAdapter = new UserfeedAdapter2(search_post.this,postFeedsList);
                                recyclerView.setAdapter(userFeedAdapter);

                            };
//
                        }
                    }
                } else {
                    Toast.makeText(search_post.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

