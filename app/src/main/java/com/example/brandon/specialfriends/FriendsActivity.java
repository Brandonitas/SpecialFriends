package com.example.brandon.specialfriends;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private TextView nameTxt;
    private ProfilePictureView profileImage;
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


        nameTxt = (TextView) findViewById(R.id.tv_name);
        profileImage = (ProfilePictureView) findViewById(R.id.image_profile);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, mGraphCallBack);
        Bundle params = new Bundle();
        params.putString("fields", "id,name,picture.height(600),friends.limit(4)");
        request.setParameters(params);
        request.executeAsync();
    }

    protected GraphRequest.GraphJSONObjectCallback mGraphCallBack = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            try{

                profileImage.setProfileId(object.getString("id"));
                nameTxt.setText(object.getString("name"));
                Log.e("MYLOG1","hola");
                Log.e("MYLOG",response.getJSONObject().getJSONObject("friends").getJSONArray("data").length()+"");

                int amigos = response.getJSONObject().getJSONObject("friends").getJSONArray("data").length();
                ArrayList<User> users = new ArrayList<>();
                for (int i = 0; i < amigos ; i++) {
                    String nombre = response.getJSONObject().getJSONObject("friends").getJSONArray("data").getJSONObject(i).getString("name");
                    int id = response.getJSONObject().getJSONObject("friends").getJSONArray("data").getJSONObject(i).getInt("id");
                    users.add(new User(id, nombre));
                }

                adapter = new MyAdapter(users);
                recyclerView.setAdapter(adapter);
                //nameTxt.setText("Welcome"+object.getString("name"));
                //JSONArray rawName = response.getJSONObject().getJSONObject("friends").getJSONArray("data");

                /*JSONArray rawName = object.getJSONObject("friends").getJSONArray("data");

                ArrayList<String> friends = new ArrayList<>();
                try{
                    for(int i = 0;i<rawName.length();i++){
                        friends.add(rawName.getJSONObject(i).getString("name"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }*/
                //nameTxt.setText("hola"+friends.get(0));

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
