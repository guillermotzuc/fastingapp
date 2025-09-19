package com.health.personaltracker.ui.history;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.databinding.FragmentHistoryBinding;
import com.health.personaltracker.entity.Fasting;
import com.health.personaltracker.model.FragmentBase;
import com.health.personaltracker.model.HistoryItem;
import com.health.personaltracker.ui.dialogs.DeleteHistoryDialogFragment;
import com.health.personaltracker.util.ColorUtil;
import com.health.personaltracker.util.MathUtil;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryFragment extends FragmentBase {

    private FragmentHistoryBinding binding;
    private ListView fasting_list;
    private HistoryAdapter adapter;
    private ColorStateList colorStateListCompleted;
    private ColorStateList colorStateListDefault;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        colorStateListCompleted = ColorUtil.COMPLETION_COLOR;
        colorStateListDefault = ColorUtil.DEFAULT_COLOR.apply(getContext());
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fasting_list = binding.fastingList;
        final Button btnAllFilter = binding.btnAllFilter;
        final Button btnWeekFilter = binding.btnWeekFilter;
        btnAllFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryHistoryRecords(0);
                btnAllFilter.setBackgroundTintList(colorStateListCompleted);
                btnWeekFilter.setBackgroundTintList(colorStateListDefault);
            }
        });

        btnWeekFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryHistoryRecords(getMondayEpochSecond());
                btnAllFilter.setBackgroundTintList(colorStateListDefault);
                btnWeekFilter.setBackgroundTintList(colorStateListCompleted);
            }
        });

        queryHistoryRecords(getMondayEpochSecond());
        btnWeekFilter.setBackgroundTintList(colorStateListCompleted);

        fasting_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                HistoryItem historyItem = (HistoryItem) parent.getItemAtPosition(position);
                DeleteHistoryDialogFragment dialog = DeleteHistoryDialogFragment.newInstance(
                        historyItem.getId(),
                        position,
                        historyItem.getLabel()
                );
                dialog.show(getParentFragmentManager(), "delete_action");
                return true;
            }
        });

        return root;
    }

    private void queryHistoryRecords(long filter) {

        final FastingDao fastingDao = getFastingDao();
        final List<Fasting> fastingList = filter > 0 ? fastingDao.getAll(filter)
                : fastingDao.getAll()
                .stream()
                .filter(f -> f.uid > filter)
                .sorted(Comparator.comparing(Fasting::getUid).reversed())
                .collect(Collectors.toList());

        loadHistoryView(fastingList);
    }

    private void loadHistoryView(final List<Fasting> fastingList) {

        if (!fastingList.isEmpty()) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
            @SuppressLint("DefaultLocale")
            List<HistoryItem> fastingHistory = fastingList.stream()
                    .map(item -> {

                        String label = String.format("%s - %s (Hours: %d)",
                                DateTime.parse(item.start_datetime).toString(dtf),
                                DateTime.parse(item.end_datetime).toString(dtf),
                                item.hours);

                        return new HistoryItem(item.uid, label);

                    }).collect(Collectors.toList());

            List<Integer> hoursList = fastingList.stream().map(f -> f.hours)
                    .collect(Collectors.toList());

            int totalHours = fastingList.stream().mapToInt(f -> f.hours).sum();
            int average = totalHours / fastingList.size();
            int max = fastingList.stream().mapToInt(f -> f.hours).max().orElse(0);
            double media = MathUtil.getMedianHours(hoursList);

            fastingHistory.add(new HistoryItem(0, "Total de horas ayunadas : " + totalHours));
            fastingHistory.add(new HistoryItem(1, "Horas de ayuno promedio : " + average));
            fastingHistory.add(new HistoryItem(3, "Media de horas ayunadas: " + media));
            fastingHistory.add(new HistoryItem(2, "Ayunos completados: " + fastingList.size()));
            fastingHistory.add(new HistoryItem(3, "Tiempo Maximo: " + max));

            adapter = new HistoryAdapter(this.getContext(), fastingHistory);
            fasting_list.setAdapter(adapter);
            fasting_list.setClickable(true);
        }
    }

     private long getMondayEpochSecond() {
         // Zona horaria actual del sistema
         ZoneId zoneId = ZoneId.systemDefault();

         // Fecha y hora actual
         LocalDateTime now = LocalDateTime.now(zoneId);

         // Ir al lunes de esta semana
         LocalDate monday = now.toLocalDate()
                 .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

         // Poner hora en 00:00:00
         LocalDateTime mondayStart = monday.atStartOfDay();

         // Convertir a EpochSecond
         return mondayStart.atZone(zoneId).toEpochSecond();
     }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}