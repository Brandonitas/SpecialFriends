package com.example.brandon.specialfriends;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brandon.specialfriends.adapters.MyAdapter;
import com.example.brandon.specialfriends.adapters.MyAdapterFav;
import com.example.brandon.specialfriends.db.AppDatabase;
import com.example.brandon.specialfriends.pojo.User;
import com.example.brandon.specialfriends.pojo.UserFav;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameTxt;
    private CircleImageView profileImage;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    RecyclerView recyclerViewFav;
    RecyclerView.Adapter adapterFriends;

    List<UserFav> myUserFavList;

    AppDatabase db;
    List<User> list;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friends);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageView = (ImageView)findViewById(R.id.logout);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(FriendsActivity.this,MainActivity.class);
                finish();
                startActivity(intent);

            }
        });

        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"production")
                .allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();



        nameTxt = (TextView) findViewById(R.id.tv_name);
        profileImage = (CircleImageView) findViewById(R.id.image_profile);

        //CARGAR AMIGOS FAV
        myUserFavList = db.userDao().getFavUsers();

        //CARGAR TODOS LOS AMIGOS
        list = db.userDao().getAllUsers();


        //RECYCLER VIEW HORIZONTAL
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewFav = (RecyclerView) findViewById(R.id.recyclerFriends);
        recyclerViewFav.setLayoutManager(layoutManager);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();


        GraphRequest request = GraphRequest.newMeRequest(accessToken, mGraphCallBack);
        Bundle params = new Bundle();
        params.putString("fields", "id,name,picture.height(600),friends{picture,name}");
        request.setParameters(params);
        request.executeAsync();

    }

    protected GraphRequest.GraphJSONObjectCallback mGraphCallBack = new GraphRequest.GraphJSONObjectCallback() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            try{

                Picasso.get().load(response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url")).into(profileImage);
                nameTxt.setText(object.getString("name"));



                //ACTIVO PARA QUE SE ACTUALICEN LOS AMIGOS EN CASO DE AGREGAR NUEVOS
                int amigos = response.getJSONObject().getJSONObject("friends").getJSONArray("data").length();
                ArrayList<User> users = new ArrayList<>();
                for (int i = 0; i < amigos ; i++) {
                    String nombre = response.getJSONObject().getJSONObject("friends").getJSONArray("data").getJSONObject(i).getString("name");
                    String image = response.getJSONObject().getJSONObject("friends").getJSONArray("data").getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("url");
                    users.add(new User(image, nombre,false));
                }


                //ORDENA MI ARREGLO EN ORDEN ALFABÉTICO
                Collections.sort(users, new Comparator<User>() {
                    @Override
                    public int compare(User obj1, User obj2) {
                        return obj1.getName().compareTo(obj2.getName());
                    }
                });



                //VERIFICO SI TENGO AMIGOS NUEVOS Y AGREGO


                if(list.size()!=users.size()){
                    Log.e("MYLOG1","no son iguales");
                    for (int i = 0; i < users.size(); i++) {
                        Log.e("MYLOG2","no son iguales");
                        if(!db.userDao().getAllUsers().contains(users.get(i))){
                            Log.e("MYLOG3","no son iguales");
                            db.userDao().insertUser(users.get(i));
                        }
                    }
                }

                //CREO MI HASHMAP PARA DIVIDIR POR LETRAS
                HashMap<Character,Integer> myHash = createHash(list);


                //AGREGAR ADAPTERS A RECYCLER VIEW
                adapter = new MyAdapter(db.userDao().getAllUsers(),myHash);
                recyclerView.setAdapter(adapter);

                adapterFriends = new MyAdapterFav(myUserFavList);
                recyclerViewFav.setAdapter(adapterFriends);




            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HashMap<Character,Integer> createHash(List<User> user){
        HashMap<Character,Integer> myHash = new HashMap<>();

        for (int i = 0; i < user.size(); i++) {
            myHash.put(user.get(i).getFirstLetter(),myHash.getOrDefault(user.get(i).getFirstLetter(),0) + 1);

        }

        return myHash;
    }


}
