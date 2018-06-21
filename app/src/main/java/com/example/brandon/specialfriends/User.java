package com.example.brandon.specialfriends;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "friend_id")
    private String idUser;

    @ColumnInfo(name = "friend_name")
    private String name;

    public User(String idUser, String name) {
        this.idUser = idUser;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setName(String name) {
        this.name = name;
    }




}
