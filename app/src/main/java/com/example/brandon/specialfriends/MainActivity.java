package com.example.brandon.specialfriends;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile","user_friends");
        callbackManager = CallbackManager.Factory.create();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(isLoggedIn){
            Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        }

        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"production")
                .allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if(!accessToken.isExpired()){
                    GraphRequest request = GraphRequest.newMeRequest(accessToken, mGraphCallBack);
                    Bundle params = new Bundle();
                    params.putString("fields", "id,name,picture.height(600),friends{picture,name}");
                    request.setParameters(params);
                    request.executeAsync();
                    finish();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    public void onClick(View v) {
            loginButton.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected GraphRequest.GraphJSONObjectCallback mGraphCallBack = new GraphRequest.GraphJSONObjectCallback() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
            try{


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

                if(db.userDao().getAllUsers().size()!=users.size()){
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
                HashMap<Character,Integer> myHash = createHash(users);

                startActivity(intent);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HashMap<Character,Integer> createHash(ArrayList<User> user){
        HashMap<Character,Integer> myHash = new HashMap<>();

        for (int i = 0; i < user.size(); i++) {
            myHash.put(user.get(i).getFirstLetter(),myHash.getOrDefault(user.get(i).getFirstLetter(),0) + 1);

        }

        return myHash;
    }



}
