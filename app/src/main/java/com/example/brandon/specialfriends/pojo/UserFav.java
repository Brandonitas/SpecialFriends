package com.example.brandon.specialfriends.pojo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class UserFav {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "friend_id")
    private String imageUser;

    @ColumnInfo(name = "friend_name")
    private String name;


    @ColumnInfo(name = "isFav")
    private boolean isFav;

    public UserFav(String imageUser, String name, boolean isFav) {
        this.imageUser = imageUser;
        this.name = name;
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public String getImageUser() {
        return imageUser;
    }

    public String getName() {
        return name;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
