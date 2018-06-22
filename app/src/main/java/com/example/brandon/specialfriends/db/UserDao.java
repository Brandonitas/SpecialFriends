package com.example.brandon.specialfriends.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.brandon.specialfriends.pojo.User;
import com.example.brandon.specialfriends.pojo.UserFav;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM userfav")
    List<UserFav> getFavUsers();

    @Insert
    void insertFavUser(UserFav... users);

    @Insert
    void insertUser(User... users);

    @Delete
    public void deleteUser(UserFav... users);

    @Update
    public void update(User... user);

    @Query("DELETE FROM  userfav   WHERE userfav.friend_id LIKE :id")
    void deleteById(String id);

    @Query("DELETE FROM user")
    void delete();
}
