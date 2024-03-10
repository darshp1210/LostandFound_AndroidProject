package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.text.Editable;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class Dashboard extends AppCompatActivity {


    RecyclerView recyclerView;
    View feed,searchfeed;
    ListView listView;
    SearchView searchView;
    String name, userId;
    String search_username;
    ArrayAdapter<String> arrayAdapter;
    boolean isSearching = false;
    ArrayList<String>  initName, xname, tempArr;
    UserFeedAdapter userFeedAdapter;

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    private FirebaseAuth mFireBaseAuth;
    PostFeed pf;
    DatabaseReference myRef = database.getReference("postFeeds");
    DatabaseReference dbref;
    private ArrayList<PostFeed2> mExampleList;
    private UserFeedAdapter mAdapter;
    String dbUserId, newId;



    ArrayList<PostFeed2> postFeedsList = new ArrayList<PostFeed2>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dashboard);




        initName = new ArrayList<>();
        xname = new ArrayList<>();
        tempArr = xname;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }



        TextView profile_name;
        ImageView profile_image;


        listView=findViewById(R.id.listview);
        recyclerView = findViewById(R.id.post_feeds);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pf = new PostFeed();
        this.showOwnPostFeeds();

        Button buttonNewPost = findViewById(R.id.newPost);
        buttonNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Dashboard.this, "You can create a new post now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this,Newpost.class));

            }
        });

        Button buttonViewProfile = findViewById(R.id.profile);
        buttonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Dashboard.this,"You can view your profile!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(Dashboard.this, ViewProfile.class));
            }
        });

        arrayAdapter =new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1,tempArr);


        listView.setAdapter(arrayAdapter);
        tempArr.clear();
        searchView = findViewById(R.id.searchinfo);
        listView.setVisibility(View.GONE);
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    Log.e("In null search", newText);
                    listView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    Log.e("In search", newText);
                    listView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tempArr.clear();
                    arrayAdapter.notifyDataSetChanged();
                    for (int i = 0; i < initName.size(); i++) {
                        if (initName.get(i).toLowerCase().contains(newText.toString().toLowerCase())) {
                            arrayAdapter.add(initName.get(i));
                        }
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.e("check",tempArr.get(i));

                                search_username = tempArr.get(i);
                                Log.e("Name",search_username);
                                String str = Arrays.toString(tempArr.toArray());
                                Intent intent = new Intent(view.getContext(), search_post.class);
                                intent.putExtra("ItemDescription", search_username);
                                view.getContext().startActivity(intent);


                        }
                    });
                }
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.onActionViewCollapsed();
//                feed.setVisibility(View.VISIBLE);
//                listView.setVisibility(View.GONE);
                return false;
            }
        });

//
        }



    private void showOwnPostFeeds() {
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        for (DataSnapshot ALL_USERS : dataSnapshot.getChildren()) {
                            dbUserId = ALL_USERS.child("userId").getValue().toString();
                            String itemDescription = ALL_USERS.child("itemDesc").getValue().toString();
                            String value = ALL_USERS.child("value").getValue().toString();
                            String contactInformation = ALL_USERS.child("cInfo").getValue().toString();
                            String Status = ALL_USERS.child("status").getValue().toString();
                            String pid = ALL_USERS.child("pid").getValue().toString();
                            initName.add(itemDescription);
                            xname.add(itemDescription);
                            Log.e("data",dbUserId);
                            Log.e("decs",itemDescription);
                            if (Status.equals("0")) {
                                Log.e("......", dbUserId);
                                postFeedsList.add(new PostFeed2(dbUserId,itemDescription,value,contactInformation,pid));
//                                postFeedsList.add(new PostFeed(userId,username,title,author,summary,genre,review,rating,status,location,coverpage));
                                userFeedAdapter = new UserFeedAdapter(Dashboard.this,postFeedsList);
//                                initName.add(username);
//                                name.add(username);
                                recyclerView.setAdapter(userFeedAdapter);

                            }
//
                        }
                    }
                } else {
                    Toast.makeText(Dashboard.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void logout(View view){
        mFireBaseAuth.signOut();
    }




}






