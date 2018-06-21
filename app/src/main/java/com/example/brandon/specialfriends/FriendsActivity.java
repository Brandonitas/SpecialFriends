package com.example.brandon.specialfriends;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private TextView nameTxt;
    private CircleImageView profileImage;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friends);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"production")
                .allowMainThreadQueries()
                .build();


        nameTxt = (TextView) findViewById(R.id.tv_name);
        profileImage = (CircleImageView) findViewById(R.id.image_profile);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, mGraphCallBack);
        Bundle params = new Bundle();
        params.putString("fields", "id,name,picture.height(600),friends{picture,name}");
        request.setParameters(params);
        request.executeAsync();

    }

    protected GraphRequest.GraphJSONObjectCallback mGraphCallBack = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            try{

                Picasso.get().load(response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url")).into(profileImage);

                nameTxt.setText(object.getString("name"));
                Log.e("MYLOG1",object.getString("id"));
                Log.e("MYLOG",response.getJSONObject().getJSONObject("friends").getJSONArray("data").length()+"");

                int amigos = response.getJSONObject().getJSONObject("friends").getJSONArray("data").length();
                ArrayList<User> users = new ArrayList<>();
                for (int i = 0; i < amigos ; i++) {
                    String nombre = response.getJSONObject().getJSONObject("friends").getJSONArray("data").getJSONObject(i).getString("name");
                    String image = response.getJSONObject().getJSONObject("friends").getJSONArray("data").getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("url");
                    users.add(new User(image, nombre));

                }

                //ORDENA MI ARREGLO EN ORDEN ALFABÉTICO
                Collections.sort(users, new Comparator<User>() {
                    @Override
                    public int compare(User obj1, User obj2) {
                        return obj1.getName().compareTo(obj2.getName());
                    }
                });

                adapter = new MyAdapter(users);
                recyclerView.setAdapter(adapter);


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };


}
