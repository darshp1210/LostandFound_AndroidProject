package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lostandfound.databinding.ActivityNewpostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Newpost extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityNewpostBinding binding;


    public TextView banner;
    EditText Itemdescription, contactInfo;

    Spinner itemType;
    public Button submit;
    public String value, Status, Res_name , id, postid;
    String userId,name;
    FirebaseDatabase Fdatabase;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    PostFeed postFeed;


    DatabaseReference databaseRef = Fdatabase.getInstance().getReference("postFeeds");
    ArrayList<PostFeed> postFeedsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);

        postFeed = new PostFeed();
        banner = (TextView) findViewById(R.id.banner);
//        banner.setOnClickListener(this);

        Itemdescription = (EditText) findViewById(R.id.Itemdescription);
        contactInfo = (EditText) findViewById(R.id.contactInfo);

        Spinner spinner = findViewById(R.id.itemType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.itemType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 value=adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(AddPost.this,value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button submitB = (Button) findViewById(R.id.submit);

        submitB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if( TextUtils.isEmpty(Itemdescription.getText()) || TextUtils.isEmpty(contactInfo.getText())) {
                    Toast.makeText(getApplication(),"Field should not be empty", Toast.LENGTH_LONG).show();
                }
                else{
                    uploadPost();
                }
            }
        });

    }
    private void uploadPost(){
        //Add post ot Database
//        postFeedsList.add(new PostFeed(userId, Itemdescription.getText().toString(),value,contactInfo.getText().toString()));
//        Log.e("data",Itemdescription.getText().toString());
//        databaseRef.setValue(postFeedsList);
        id = databaseRef.push().getKey();
        postid = id;
        Log.e("Key", id);
        postFeed.setcInfo(contactInfo.getText().toString());
        postFeed.setValue(value);
        postFeed.setUserId(userId);
        postFeed.setItemDesc(Itemdescription.getText().toString());
        postFeed.setStatus("0");
        postFeed.setRes_name("0");
        postFeed.setpid(postid);

        databaseRef.child(id).setValue(postFeed);

        Intent intent = new Intent(Newpost.this, Dashboard.class);

        startActivity(intent);
        Toast.makeText(getApplication(),"Post added successfully",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}