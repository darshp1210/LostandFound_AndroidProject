package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPost extends AppCompatActivity {

    private EditText editItemDesc, editItemContactInfo, itemId;
    private FirebaseAuth authProfile;
    private DatabaseReference mDatabase;
    private String postId;
    Spinner editItemCategory;
    private String textItemDesc, textItemContactInfo, textItemCategory, currentUid;
    RecyclerView recyclerView;
    PostFeed postFeed;
    FirebaseDatabase database= FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        Intent intent = getIntent();
        postId = intent.getStringExtra("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference("postFeeds");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUid = user.getUid();
        // Initialize the EditText fields
        editItemDesc = findViewById(R.id.EditItemdescription);
        editItemContactInfo = findViewById(R.id.EditcontactInfo);
        //editItemCategory = findViewById(R.id.EdititemType);

        showPost();

        Button submitBtn = findViewById(R.id.btnEditPost);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(editItemDesc.getText()) || TextUtils.isEmpty(editItemContactInfo.getText())) {
                    Toast.makeText(getApplication(),"Field should not be empty", Toast.LENGTH_LONG).show();
                }
                else{
                    savePost();
                }
            }
        });

    }
    private void showPost(){
        mDatabase.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.e("postid",postId);
                    String itemDesc = snapshot.child("itemDesc").getValue(String.class);
                    String itemContactInfo = snapshot.child("cInfo").getValue(String.class);
                    String itemType = snapshot.child("value").getValue(String.class);

                    // Set the EditText fields to the post details
                    editItemDesc.setText(itemDesc);
                    editItemContactInfo.setText(itemContactInfo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*private void savePost(){
        editItemDesc = (EditText) findViewById(R.id.Itemdescription);
        editItemContactInfo = (EditText) findViewById(R.id.contactInfo);
        postFeed.setcInfo(editItemContactInfo.getText().toString());
        postFeed.setItemDesc(editItemDesc.getText().toString());
        mDatabase.child(postId).setValue(postFeed);

        Intent intent = new Intent(EditPost.this, Dashboard.class);

        startActivity(intent);
        Toast.makeText(getApplication(),"Post added successfully",Toast.LENGTH_LONG).show();

    }*/

    private void savePost(){
        String itemDesc = editItemDesc.getText().toString();
        String itemContactInfo = editItemContactInfo.getText().toString();

        if (TextUtils.isEmpty(itemDesc) || TextUtils.isEmpty(itemContactInfo)) {
            Toast.makeText(getApplication(),"Field should not be empty", Toast.LENGTH_LONG).show();
            return;
        }

        mDatabase.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    PostFeed postFeed = snapshot.getValue(PostFeed.class);
                    postFeed.setcInfo(itemContactInfo);
                    postFeed.setItemDesc(itemDesc);
                    mDatabase.child(postId).setValue(postFeed).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(EditPost.this, Dashboard.class);
                                startActivity(intent);
                                Toast.makeText(getApplication(),"Post updated successfully",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplication(),"Failed to update post",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(),"Failed to update post",Toast.LENGTH_LONG).show();
            }
        });
    }

}