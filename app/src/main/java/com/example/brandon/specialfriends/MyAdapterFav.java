package com.example.brandon.specialfriends;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyAdapterFav extends RecyclerView.Adapter<MyAdapterFav.ViewHolder> {

    List<UserFav> users;

    public MyAdapterFav(List<UserFav> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public MyAdapterFav.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_friends_layout,parent,false);
        return new MyAdapterFav.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterFav.ViewHolder holder, int position) {
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"production")
                .allowMainThreadQueries()
                .build();

        holder.name_fav_friend.setText(users.get(position).getName());
        Picasso.get().load(users.get(position).getImageUser().toString()).into(holder.image_fav_friend);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_fav_friend;
        public CircleImageView image_fav_friend;
        public ViewHolder(View itemView) {
            super(itemView);
            name_fav_friend = itemView.findViewById(R.id.name_fav_frriend);
            image_fav_friend = itemView.findViewById(R.id.image_fav_friend);
        }
    }
}
