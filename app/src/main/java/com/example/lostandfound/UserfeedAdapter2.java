package com.example.lostandfound;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserfeedAdapter2 extends RecyclerView.Adapter<UserfeedAdapter2.MyViewHolder>{

    Context context;
    ArrayList<PostFeed2> postFeedsList;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("postFeeds");

    String currentUid;




    public UserfeedAdapter2 (Context context, ArrayList<PostFeed2> postFeedsList) {
        this.context = context;
        this.postFeedsList = postFeedsList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_feed2,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        PostFeed2 post = postFeedsList.get(position);
        //holder.username.setText(post.getUsername());
        holder.itemDesc.setText(post.getItemDesc());
        holder.value.setText(post.getValue());
        holder.contactInfo.setText(post.getcInfo());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUid = user.getUid();
        Log.e("uid",currentUid);

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        for (DataSnapshot ALL_USERS : dataSnapshot.getChildren()) {
                            String dbUserId = ALL_USERS.child("userId").getValue().toString();
                            Log.e("dbuser",dbUserId);
                        }
                    }
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

//
    }

    @Override
    public int getItemCount() {
        return postFeedsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView username, itemDesc, value, contactInfo,pid;
        Button resolve;
        public View view, editPost, deletePost;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//
            itemDesc = itemView.findViewById(R.id.Item_Description);
            value = itemView.findViewById(R.id.Item_type);
            contactInfo = itemView.findViewById(R.id.Contact_Info);
            pid = itemView.findViewById(R.id.feedID);
            editPost = itemView.findViewById(R.id.edit);
            deletePost = itemView.findViewById(R.id.delete_post);

            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), Showownposts.class);
                    intent.putExtra("PostID", pid.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });




//
        }

        public void setData(Context context, String item_desc, String contact_info, String ItemValue ){

            TextView itemDesc, value, contactInfo;

            itemDesc = itemView.findViewById(R.id.Item_Description);
            itemDesc.setText(item_desc);
            contactInfo = itemView.findViewById(R.id.Contact_Info);
            contactInfo.setText(contact_info);
            value = itemView.findViewById(R.id.Item_type);
            value.setText(ItemValue);


        }
    }

}


