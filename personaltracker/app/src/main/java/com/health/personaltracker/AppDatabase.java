package com.health.personaltracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.dao.FitnessDao;
import com.health.personaltracker.entity.Fasting;
import com.health.personaltracker.entity.FitnessRecord;

@Database(entities = { Fasting.class, FitnessRecord.class }, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FastingDao fastingDao();
    public abstract FitnessDao fitnessDao();
}