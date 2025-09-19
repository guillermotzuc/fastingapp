package com.health.personaltracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.health.personaltracker.entity.Fasting;

import java.util.List;

@Dao
public interface FastingDao {
    @Query("SELECT * FROM fasting WHERE active = 0 AND hours > 11")
    List<Fasting> getAll();

    @Query("SELECT * FROM fasting WHERE active = 0 AND uid > :id AND hours > 11")
    List<Fasting> getAll(long id);

    @Query("SELECT * FROM fasting WHERE uid IN (:fastingIds)")
    List<Fasting> loadAllByIds(int[] fastingIds);

    @Query("SELECT * FROM fasting WHERE active LIMIT 1")
    Fasting findActive();

    @Query("DELETE FROM fasting WHERE uid = :id")
    void deleteById(long id);

    @Insert
    void insertAll(Fasting... fasting);

    @Delete
    void delete(Fasting fasting);

    @Update()
    void update(Fasting fasting);
}
