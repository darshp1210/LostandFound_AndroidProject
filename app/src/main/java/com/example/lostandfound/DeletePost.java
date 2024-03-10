package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeletePost extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the post ID from the intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("PostID");

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("postFeeds");

        deletePost();
    }

    private void deletePost() {
        // Get the current user's ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        // Delete the post from the Firebase database
        mDatabase.child(postId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Post deleted successfully, return to user feed activity
                Intent intent = new Intent(DeletePost.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error occurred while deleting the post
                Toast.makeText(DeletePost.this, "Error deleting post", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
