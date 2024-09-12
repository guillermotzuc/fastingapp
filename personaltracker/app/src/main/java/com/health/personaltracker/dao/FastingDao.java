package com.health.personaltracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.health.personaltracker.model.Fasting;

import java.util.List;

@Dao
public interface FastingDao {
    @Query("SELECT * FROM fasting")
    List<Fasting> getAll();

    @Query("SELECT * FROM fasting WHERE uid IN (:fastingIds)")
    List<Fasting> loadAllByIds(int[] fastingIds);

    @Query("SELECT * FROM fasting WHERE active LIMIT 1")
    Fasting findActive();

    @Insert
    void insertAll(Fasting... fasting);

    @Delete
    void delete(Fasting fasting);

    @Update()
    void update(Fasting fasting);
}
