package com.example.brandon.specialfriends.pojo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "friend_id")
    private String imageUser;

    @ColumnInfo(name = "friend_name")
    private String name;

    private char firstLetter;

    @ColumnInfo(name = "isFav")
    private boolean isFav;

    public User(String imageUser, String name, boolean isFav) {
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

    public char getFirstLetter() {
        return name.toLowerCase().charAt(0);
    }

    public void setFirstLetter(char firstLetter) {
        this.firstLetter = firstLetter;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
