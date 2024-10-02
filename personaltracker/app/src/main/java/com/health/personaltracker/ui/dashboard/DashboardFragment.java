package com.health.personaltracker.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.health.personaltracker.R;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.databinding.FragmentDashboardBinding;
import com.health.personaltracker.entity.Fasting;
import com.health.personaltracker.model.FastingCardModel;
import com.health.personaltracker.model.FragmentBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DashboardFragment extends FragmentBase {

    private FragmentDashboardBinding binding;
    private GridView coursesGV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final FastingDao fastingDao = getFastingDao();
        List<Fasting> fastings = fastingDao.getAll();
        Map<Integer, Long> hoursMap = fastings.stream()
                .collect(Collectors.groupingBy(f -> f.hours, Collectors.counting()));
        ArrayList<FastingCardModel> courseModelArrayList = new ArrayList<>();
        FastingCardGVAdapter adapter = new FastingCardGVAdapter(getContext(), courseModelArrayList);
        coursesGV = binding.idGVcourses;

        AtomicInteger counter = new AtomicInteger(16);
        courseModelArrayList.add(newFastingCard(hoursMap, counter.getAndDecrement(), R.drawable.ic_rewarded_yellow_40dp)); // 16h
        courseModelArrayList.add(newFastingCard(hoursMap, counter.getAndDecrement(), R.drawable.ic_rewarded_gray_40dp)); // 15h
        courseModelArrayList.add(newFastingCard(hoursMap, counter.getAndDecrement(), R.drawable.ic_rewarded_orange_24dp)); // 14h
        courseModelArrayList.add(newFastingCard(hoursMap, counter.getAndDecrement(), R.drawable.ic_orange_whatshot_40dp)); // 13h
        courseModelArrayList.add(newFastingCard(hoursMap, counter.getAndDecrement(), R.drawable.ic_orange_whatshot_40dp)); // 12h
        coursesGV.setAdapter(adapter);

        return root;
    }

    private FastingCardModel newFastingCard(Map<Integer, Long> hoursMap, int fastingHours, int drawableImg) {

        Long fastingCount = hoursMap.getOrDefault(fastingHours, 0L);
        return new FastingCardModel(buildFastingLabel(fastingHours, fastingCount, 24 - fastingHours), drawableImg);
    }

    private String buildFastingLabel(int fastingHour, Long fastingCount, int eatingWindow) {

        return String.format("(%d) %d/%d", fastingCount, fastingHour, eatingWindow);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}