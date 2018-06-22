package com.example.brandon.specialfriends.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.brandon.specialfriends.pojo.User;
import com.example.brandon.specialfriends.pojo.UserFav;

@Database(entities =  {User.class, UserFav.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
