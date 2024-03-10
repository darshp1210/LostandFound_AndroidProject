package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Showownposts extends AppCompatActivity {

    PostFeed postFeed;
    FirebaseDatabase Fdatabase;
    EditText res_name;
    Button Res_submit;

    String postId;
    //DatabaseReference databaseRef = Fdatabase.getInstance().getReference("postFeeds");


    private DatabaseReference mDatabase;
//    String key = mDatabase.child("postFeeds").push().getKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showownposts);

        Intent intent = getIntent();
        postId = intent.getStringExtra("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference("postFeeds");
        postFeed = new PostFeed();


        res_name = findViewById(R.id.userInfo);
        Res_submit = findViewById(R.id.submit);
        Log.e("name", res_name.getText().toString());
        /*Res_submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.e("click","click");
                  if( TextUtils.isEmpty(res_name.getText().toString())) {
                       Toast.makeText(getApplication(),"Field should not be empty", Toast.LENGTH_LONG).show();
                   }
                   else{
                       resolvePost();
                   }
               }

       });*/
        Res_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(res_name.getText().toString())) {
                    Toast.makeText(getApplication(),"Field should not be empty", Toast.LENGTH_LONG).show();
                }
                else{
                    resolvePost();
                }
            }
        });
    }

    /*private void resolvePost(){

        postFeed.setRes_name(res_name.getText().toString());
        Log.e("name", res_name.getText().toString());

        postFeed.setStatus("1");


        String id = databaseRef.push().getKey();
        databaseRef.child(id).setValue(postFeed);
        Log.e("KEY:",id);

//        Intent intent = new Intent(Showownposts.this, Dashboard.class);
//        startActivity(intent);
        Toast.makeText(getApplication(),"Post resolved successfully",Toast.LENGTH_LONG).show();
    }*/

    /*private void resolvePost(){
        String resName = res_name.getText().toString();
        mDatabase.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    PostFeed postFeed = snapshot.getValue(PostFeed.class);
                    postFeed.setRes_name(resName);
                    postFeed.setStatus("1");
                    mDatabase.child(postId).setValue(postFeed).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Showownposts.this, Dashboard.class);
                                startActivity(intent);
                                Toast.makeText(getApplication(),"Post resolved successfully",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplication(),"Failed to resolve post",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(),"Failed to resolve post",Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void resolvePost(){
        String resName = res_name.getText().toString();
        mDatabase.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    PostFeed postFeed = snapshot.getValue(PostFeed.class);
                    postFeed.setRes_name(resName);
                    postFeed.setStatus("1");
                    mDatabase.child(postId).setValue(postFeed).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Showownposts.this, Dashboard.class);
                                startActivity(intent);
                                Toast.makeText(getApplication(),"Post resolved successfully",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplication(),"Failed to resolve post",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(),"Failed to resolve post",Toast.LENGTH_LONG).show();
            }
        });
    }


}