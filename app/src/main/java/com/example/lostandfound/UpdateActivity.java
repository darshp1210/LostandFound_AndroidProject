package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextEmail, editTextstuId;
    private String textFullName, textEmail, textstuId;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        /*getSupportActionBar().setTitle("Update Profile");*/

        progressBar = findViewById(R.id.progressBar);
        editTextFullName = findViewById(R.id.editTextfullName);
        editTextEmail = findViewById(R.id.editTextemail);
        editTextstuId = findViewById(R.id.editTextstuID);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showProfile(firebaseUser);

        Button buttonUpdateProfile = findViewById(R.id.buttonUpdate);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);
            }
        });

    }
    private void showProfile(FirebaseUser firebaseUser){
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Regitered user");

        progressBar.setVisibility(View.VISIBLE);
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user!=null){
                    textFullName = user.fullName;
                    textEmail = firebaseUser.getEmail();
                    textstuId = user.stuID;

                    editTextFullName.setText(textFullName);
                    editTextEmail.setText(textEmail);
                    editTextstuId.setText(textstuId);

                }else{
                    Toast.makeText(UpdateActivity.this, "Something went wrong!",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateActivity.this, "Something went wrong!",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void updateProfile(FirebaseUser firebaseUser){
        String fullName = editTextFullName.getText().toString().trim();
        String stuID = editTextstuId.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is Required");
            editTextFullName.requestFocus();
            return;
        }

        if (stuID.isEmpty()) {
            editTextstuId.setError("Student ID is Required");
            editTextstuId.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }
        User user = new User(fullName,stuID,email);
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Regitered user");
        String userId = firebaseUser.getUid();
        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);
                    Toast.makeText(UpdateActivity.this, "Update successfully!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(UpdateActivity.this, ViewProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else{
                    try{
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}