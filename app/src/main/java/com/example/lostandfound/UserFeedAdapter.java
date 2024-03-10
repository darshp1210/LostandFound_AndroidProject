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

public class UserFeedAdapter extends RecyclerView.Adapter<UserFeedAdapter.MyViewHolder>{

    Context context;
    ArrayList<PostFeed2> postFeedsList;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("postFeeds");
    Button resolve, edit, delete_post;
    String currentUid;




    public UserFeedAdapter (Context context, ArrayList<PostFeed2> postFeedsList) {
        this.context = context;
        this.postFeedsList = postFeedsList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_feed,parent,false);
//        if (v == null) {
        resolve = v.findViewById(R.id.resolve);
        edit = v.findViewById(R.id.edit);
        delete_post = v.findViewById(R.id.delete_post);
        resolve.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        delete_post.setVisibility(View.GONE);

//            resolve.setVisibility(View.VISIBLE);
//        } else {
//            MyViewHolder x = (MyViewHolder) v.getTag();
//        }
       /* resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(v.getContext(), Showownposts.class);
                intent.putExtra("PostID",post.getpid());
                context.startActivity(intent);
            }
        });*/
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
                            String itemDescription = ALL_USERS.child("itemDesc").getValue().toString();
                            Log.e("desc", itemDescription);
                            Log.e("current uid adapter", currentUid);
                            if (!(currentUid.equals(dbUserId))) {
                                Log.e("dbuser in IF",dbUserId);
                                resolve.setVisibility(View.GONE);
                                edit.setVisibility(View.GONE);
                                delete_post.setVisibility(View.GONE);
                            } else {
                                Log.e("dbuser in else", dbUserId);
                                resolve.setVisibility(View.VISIBLE);
                                edit.setVisibility(View.VISIBLE);
                                delete_post.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        PostFeed2 post = postFeedsList.get(position);
        //holder.username.setText(post.getUsername());
        holder.itemDesc.setText(post.getItemDesc());
        holder.value.setText(post.getValue());
        holder.contactInfo.setText(post.getcInfo());
        holder.pid.setText(post.getpid());


        holder.deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation").setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context, DeletePost.class);
                                intent.putExtra("PostID", post.getpid());
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                // Show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*holder.resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Showownposts.class);
                intent.putExtra("PostID",post.getpid());
                context.startActivity(intent);
            }
        });*/

        resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Showownposts.class);
                intent.putExtra("PostID",post.getpid());
                context.startActivity(intent);
            }
        });

        holder.editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditPost.class);
                intent.putExtra("PostID", post.getpid());
                context.startActivity(intent);
            }
        });
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




//            itemDesc.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), Showownposts.class);
//                    intent.putExtra("ItemDesc", itemDesc.getText().toString());
//                    view.getContext().startActivity(intent);
//                }
//            });
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


