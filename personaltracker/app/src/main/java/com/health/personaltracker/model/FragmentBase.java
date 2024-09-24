package com.health.personaltracker.model;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.health.personaltracker.AppDatabase;
import com.health.personaltracker.dao.FastingDao;

public abstract class FragmentBase extends Fragment {

    protected AppDatabase getAppDatabase() {
        return Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
    }

    protected FastingDao getFastingDao() {
        return getAppDatabase().fastingDao();
    }
}

