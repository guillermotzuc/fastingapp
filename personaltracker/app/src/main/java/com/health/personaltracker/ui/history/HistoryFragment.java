package com.health.personaltracker.ui.history;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.health.personaltracker.AppDatabase;
import com.health.personaltracker.R;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.databinding.FragmentHistoryBinding;
import com.health.personaltracker.model.Fasting;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        final FastingDao fastingDao = db.fastingDao();
        List<Fasting> fastingList = fastingDao.getAll();
        if (fastingList != null && !fastingList.isEmpty()) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH");
            @SuppressLint("DefaultLocale") String[] fastingHistory = fastingList.stream()
                    .map(item -> String.format("%s - %s (Hours: %d)",
                            DateTime.parse(item.start_datetime).toString(dtf),
                            DateTime.parse(item.end_datetime).toString(dtf),
                            item.hours)).toArray(String[]::new);

            ListView fasting_list = binding.fastingList;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                    R.layout.activity_listview, fastingHistory);

            fasting_list.setAdapter(adapter);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}