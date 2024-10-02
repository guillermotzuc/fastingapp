package com.health.personaltracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.entity.Fasting;

@Database(entities = { Fasting.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FastingDao fastingDao();
}