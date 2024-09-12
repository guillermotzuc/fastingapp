package com.health.personaltracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.dao.UserDao;
import com.health.personaltracker.model.Fasting;
import com.health.personaltracker.model.User;

@Database(entities = {User.class, Fasting.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract FastingDao fastingDao();
}