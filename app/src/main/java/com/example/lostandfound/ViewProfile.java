package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;


public class ViewProfile extends AppCompatActivity {

    private static final String TAG="ViewProfile";
    private TextView getProfileFullname, getProfileUtaId, getProfileEmail;
    private String fullName, utaId, eMail;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

/*
        getSupportActionBar().setTitle("Profile");
*/

        Button buttonUpdateAccount = findViewById(R.id.EditProfile);
        buttonUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewProfile.this, "Edit your profile!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ViewProfile.this, UpdateActivity.class));

            }
        });

        Button buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);
        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewProfile.this, "Delete account", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Regitered user");
                mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            deleteUserData();
                            Toast.makeText(ViewProfile.this, "User has been deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewProfile.this, ForgotPasswordActivity.class);
                            startActivity(intent);
                            mAuth.signOut();
                            finish();
                        }

                    }
                });


            }
        });

        getProfileFullname = findViewById(R.id.ProfilefullName);
        getProfileEmail = findViewById(R.id.ProfileEmail);
        getProfileUtaId = findViewById(R.id.ProfilestuID);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(ViewProfile.this, "Something went wrong!",
                    Toast.LENGTH_LONG).show();
        }else{
            showUserProfile(firebaseUser);
        }

    }

    private void deleteUserData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Regitered user");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        databaseReference.child(mAuth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Log.d(TAG,"OnSuccess:User Data Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.getMessage());
                Toast.makeText(ViewProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser){
        String userId = firebaseUser.getUid();

        //Extracting user data from Realtime database
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Regitered user");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User readWriteUserDetails = snapshot.getValue(User.class);

                if(readWriteUserDetails!= null){
                    fullName = readWriteUserDetails.fullName;
                    eMail = readWriteUserDetails.email;
                    utaId = readWriteUserDetails.stuID;

                    getProfileFullname.setText(fullName);
                    getProfileEmail.setText(eMail);
                    getProfileUtaId.setText(utaId);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}