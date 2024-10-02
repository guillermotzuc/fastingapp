package com.health.personaltracker.model;

import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.health.personaltracker.AppDatabase;
import com.health.personaltracker.dao.FastingDao;

public abstract class FragmentBase extends Fragment {

    protected AppDatabase getAppDatabase() {
        return Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "ayuno-app-database")
                .allowMainThreadQueries()
                .build();
    }

    // TODO APPLY THIS FOR MIGRATION
    private Migration from1to2() {
        return new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase db) {
                super.migrate(db);
            }
        };
    }
    protected FastingDao getFastingDao() {
        return getAppDatabase().fastingDao();
    }
}

