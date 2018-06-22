package com.example.brandon.specialfriends.adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandon.specialfriends.FriendsActivity;
import com.example.brandon.specialfriends.R;
import com.example.brandon.specialfriends.db.AppDatabase;
import com.example.brandon.specialfriends.pojo.User;
import com.example.brandon.specialfriends.pojo.UserFav;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    List<User> users;
    HashMap<Character,Integer> myHash;


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

                //VALIDA SI TENEMOS INTERNET O NO
                //SI NO TENEMOS NO ME DEJA GUARDAR A MIS FAVORITOS
                if(!Utility.isNetworkAvailable(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), "No WIFI", Toast.LENGTH_SHORT).show();
                    return;
                }

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


    //METODO PARA VALIDAR SI TENEMOS INTERNET
    public static class Utility {
        public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }


//-----INTENTO DE MULTIPLE VIEW TYPE--------//

/*
    class ViewHolderLetter extends RecyclerView.ViewHolder {
        public Button button;

        public ViewHolderLetter(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button_letter);
        }
    }



    @Override
    public int getItemCount() {
        return users.size()+myHash.size();
    }

     @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()){
            case 0:
                ViewHolderLetter viewHolderLetter = (ViewHolderLetter)holder;
                User user = users.get(position);
                viewHolderLetter.setIsRecyclable(false);
                viewHolderLetter.button.setText(user.getFirstLetter()+"");

                break;
            case 1:
                ViewHolderFriend viewHolderFriend = (ViewHolderFriend)holder;
                //User user2 = users.get(position);
                viewHolderFriend.setIsRecyclable(false);

                //Aca
                final AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"production")
                        .allowMainThreadQueries()
                        .build();

                viewHolderFriend.user_name.setText(users.get(position).getName());

                Picasso.get().load(users.get(position).getImageUser().toString()).into(viewHolderFriend.my_image_view);

                if(users.get(position).isFav()){
                    viewHolderFriend.image_star.setImageResource(R.drawable.star);
                }else{
                    viewHolderFriend.image_star.setImageResource(R.drawable.starempty);
                }

                viewHolderFriend.image_star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(users.get(position).isFav()){
                            Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                            users.get(position).setFav(false);
                            viewHolderFriend.image_star.setImageResource(R.drawable.starempty);

                            String image = users.get(position).getImageUser();
                            String name = users.get(position).getName();
                            boolean isFav = users.get(position).isFav();

                            UserFav userFav = new UserFav(image, name, isFav);

                            db.userDao().deleteById(userFav.getImageUser());

                            db.userDao().update(users.get(position));

                            Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                            getApplicationContext().startActivity(intent);

                        }else {
                            Toast.makeText(getApplicationContext(), "GUARDADO", Toast.LENGTH_SHORT).show();
                            users.get(position).setFav(true);
                            viewHolderFriend.image_star.setImageResource(R.drawable.star);

                            String image = users.get(position).getImageUser();
                            String name = users.get(position).getName();
                            boolean isFav = users.get(position).isFav();

                            UserFav userFav = new UserFav(image, name, isFav);

                            db.userDao().insertFavUser(userFav);

                            //ACTUALIZO MI USUARIO EN MI TABLA USER
                            db.userDao().update(users.get(position));


                            Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                            getApplicationContext().startActivity(intent);
                        }
                    }
                });



                break;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if(viewType==0) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.letter_layout, parent, false);
            return new ViewHolderLetter(view);
        }else{
            LayoutInflater inflater2 = LayoutInflater.from(context);
            View view2 = inflater2.inflate(R.layout.user_row,parent,false);
            return new ViewHolderLetter(view2);

        }
    }


      int temp;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemViewType(int position) {
        Iterator ite = this.myHash.entrySet().iterator();
        while (ite.hasNext()) {
            HashMap.Entry e = (HashMap.Entry) ite.next();
            temp = (int) e.getValue();
            for(int i=temp;i>=temp;i--){
                if(i==0){
                    Log.e("MYLOG","regreso 0");
                    return 0;
                }else{
                    Log.e("MYLOG","regreso 1");
                    return 1;
                }
            }
        }
        return 0;
    }

*/



}
