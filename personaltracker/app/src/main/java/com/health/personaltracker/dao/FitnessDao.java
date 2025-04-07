package com.health.personaltracker.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.health.personaltracker.entity.FitnessRecord;

import java.util.List;

@Dao
public interface FitnessDao {

    @Insert
    void insertRecord(FitnessRecord record);

    @Query("SELECT * FROM fitness_records ORDER BY id DESC")
    List<FitnessRecord> getAllRecords();

    @Query("SELECT AVG(steps) FROM fitness_records")
    float getAverageSteps();

    @Query("SELECT * FROM fitness_records WHERE exercise = 1")
    List<FitnessRecord> getExerciseDays();
}