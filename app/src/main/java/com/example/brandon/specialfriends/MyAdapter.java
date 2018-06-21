package com.example.brandon.specialfriends;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    ArrayList<User> users;

    public MyAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        holder.user_name.setText(users.get(position).getName());


        Picasso.get().load(users.get(position).getIdUser().toString()).into(holder.my_image_view);




    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name;
        public CircleImageView my_image_view;


        public ViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.name);
            my_image_view = itemView.findViewById(R.id.my_image_view);


        }
    }
}
