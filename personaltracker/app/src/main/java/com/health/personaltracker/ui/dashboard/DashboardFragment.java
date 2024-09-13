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
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

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

        fasting159.setVisibility(View.GONE);
        fasting1410.setVisibility(View.GONE);
        fasting1311.setVisibility(View.GONE);
        fasting1212.setVisibility(View.GONE);

        for (Integer key : hoursMap.keySet()) {
            switch (key) {
                case 16 :
                    fasting168.setText(String.format("(%d) 16/8", hoursMap.get(key)));
                    break;
                case 15 :
                    fasting159.setText(String.format("(%d) 15/9", hoursMap.get(key)));
                    fasting159.setVisibility(View.VISIBLE);
                    break;
                case 14 :
                    fasting1410.setText(String.format("(%d) 14/10", hoursMap.get(key)));
                    fasting1410.setVisibility(View.VISIBLE);
                    break;
                case 13 :
                    fasting1311.setText(String.format("(%d) 13/11", hoursMap.get(key)));
                    fasting1311.setVisibility(View.VISIBLE);
                    break;
                case 12 :
                    fasting1212.setText(String.format("(%d) 12/12", hoursMap.get(key)));
                    fasting1212.setVisibility(View.VISIBLE);
                    break;
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}