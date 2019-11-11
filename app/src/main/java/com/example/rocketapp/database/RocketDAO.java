package com.example.rocketapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RocketDAO {

    @Query("SELECT * FROM rocket")
    List<Rocket> getAllRockets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRocket(Rocket rocket);

    @Query("SELECT * FROM rocket where name LIKE :firstName")
    Rocket findByName(String firstName);

    @Delete
    void deleteRocket(Rocket rocket);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRocket(Rocket rocket);
}