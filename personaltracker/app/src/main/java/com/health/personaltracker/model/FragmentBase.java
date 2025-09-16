package com.health.personaltracker.model;

import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.health.personaltracker.AppDatabase;
import com.health.personaltracker.R;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.dao.FitnessDao;

public abstract class FragmentBase extends Fragment {

    protected AppDatabase getAppDatabase() {
        return Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, getString(R.string.app_database))
                .allowMainThreadQueries()
                .addMigrations(from1to2(), from2to3())
                .build();
    }

    // TODO APPLY THIS FOR MIGRATION
    private Migration from1to2() {
        return new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase db) {

                db.execSQL(
                        "CREATE TABLE fitness_records (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                "age INTEGER NOT NULL, " +
                                "weight REAL NOT NULL, " +
                                "steps INTEGER NOT NULL, " +
                                "exercise INTEGER NOT NULL)" // Room stores boolean as INTEGER (0 or 1)
                );
            }
        };
    }
    private Migration from2to3() {
        return new Migration(2, 3) {
            @Override
            public void migrate(SupportSQLiteDatabase db) {
                // Add new columns to the existing table
                db.execSQL("ALTER TABLE fitness_records ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0");
                db.execSQL("ALTER TABLE fitness_records ADD COLUMN dayOfWeek INTEGER NOT NULL DEFAULT 0");
            }
        };
    }

    protected FastingDao getFastingDao() {
        return getAppDatabase().fastingDao();
    }
    protected FitnessDao getFitnessDao() {
        return getAppDatabase().fitnessDao();
    }

}

