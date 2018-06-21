package com.example.brandon.specialfriends;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    List<User> users;
    HashMap<Character,Integer> myHash;
    Context context;


    public MyAdapter(List<User> users, HashMap<Character, Integer> myHash) {
        this.users = users;
        this.myHash = myHash;
    }


    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.ViewHolder holder, final int position) {
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"production")
                .allowMainThreadQueries()
                .build();

        holder.user_name.setText(users.get(position).getName());


        Picasso.get().load(users.get(position).getImageUser().toString()).into(holder.my_image_view);

        if(users.get(position).isFav()){
            holder.image_star.setImageResource(R.drawable.star);
        }else{
            holder.image_star.setImageResource(R.drawable.starempty);
        }

        holder.image_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(users.get(position).isFav()){
                    Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                    users.get(position).setFav(false);
                    holder.image_star.setImageResource(R.drawable.starempty);

                    String image = users.get(position).getImageUser();
                    String name = users.get(position).getName();
                    boolean isFav = users.get(position).isFav();

                    UserFav userFav = new UserFav(image, name, isFav);

                    db.userDao().deleteById(userFav.getImageUser());


                    db.userDao().update(users.get(position));
                    Log.e("Despues de eliminar",db.userDao().getFavUsers().size()+"");
                    Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                    getApplicationContext().startActivity(intent);

                }else {
                    Toast.makeText(getApplicationContext(), "GUARDADO", Toast.LENGTH_SHORT).show();
                    users.get(position).setFav(true);
                    holder.image_star.setImageResource(R.drawable.star);

                    String image = users.get(position).getImageUser();
                    String name = users.get(position).getName();
                    boolean isFav = users.get(position).isFav();

                    UserFav userFav = new UserFav(image, name, isFav);

                    db.userDao().insertFavUser(userFav);

                    //ACTUALIZO MI USUARIO EN MI TABLA USER
                    db.userDao().update(users.get(position));

                    Log.e("Agregando",db.userDao().getFavUsers().size()+"");
                    Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                    getApplicationContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name;
        public CircleImageView my_image_view;
        public ImageView image_star;


        public ViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.name);
            my_image_view = itemView.findViewById(R.id.my_image_view);
            image_star = itemView.findViewById(R.id.image_star);
        }
    }



    //TWO VIEWS
    /*@NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        for(int i= viewType;i==0;i--){
            if(i==viewType){
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.letter_layout,parent,false);
                return new ViewHolderLetter(view);
            }else{
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.user_row,parent,false);
                return new ViewHolderLetter(view);
            }
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_row,parent,false);
        return new ViewHolderLetter(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


      @Override
    public int getItemViewType(int position) {
        Iterator ite = this.myHash.entrySet().iterator();
        while (ite.hasNext()) {
            HashMap.Entry e = (HashMap.Entry) ite.next();
            int temp = (int) e.getValue();
            if (temp != 0) {
                //e.setValue(temp--);
                return temp;
            } else {
                return 0;
            }
        }
        return 0;
    }

     class ViewHolderLetter extends RecyclerView.ViewHolder {
        public Button button;

        public ViewHolderLetter(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button_letter);
        }
    }

    */




}
