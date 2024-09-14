package com.health.personaltracker.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.health.personaltracker.AppDatabase;
import com.health.personaltracker.R;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.databinding.FragmentDashboardBinding;
import com.health.personaltracker.model.Fasting;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        final FastingDao fastingDao = db.fastingDao();
        List<Fasting> fastings = fastingDao.getAll();
        Map<Integer, Long> hoursMap = fastings.stream()
                .collect(Collectors.groupingBy(f -> f.hours, Collectors.counting()));

        TextView fasting168 = binding.fasting168;
        TextView fasting159 = binding.fasting159;
        TextView fasting1410 = binding.fasting1410;
        TextView fasting1311 = binding.fasting1311;
        TextView fasting1212 = binding.fasting1212;

        Map<Integer, TextView> fastingHourToTextView = Map.of(16, fasting168
        , 15, fasting159
        , 14, fasting1410
        , 13, fasting1311
        , 12, fasting1212);

        fastingHourToTextView.values().forEach(tv -> tv.setVisibility(View.GONE));
        for (Integer fastingHour : hoursMap.keySet()) {
            int eatingWindow = 24 - fastingHour;
            TextView fastingItemControl = fastingHourToTextView.get(fastingHour);
            fastingItemControl.setText(String.format("(%d) %d/%d", hoursMap.get(fastingHour), fastingHour, eatingWindow));
            fastingItemControl.setVisibility(View.VISIBLE);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}