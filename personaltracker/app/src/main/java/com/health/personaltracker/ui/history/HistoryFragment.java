package com.health.personaltracker.ui.history;

import static androidx.core.content.FileProvider.getUriForFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.databinding.FragmentHistoryBinding;
import com.health.personaltracker.entity.Fasting;
import com.health.personaltracker.model.FragmentBase;
import com.health.personaltracker.model.HistoryItem;
import com.health.personaltracker.ui.dialogs.DeleteHistoryDialogFragment;
import com.health.personaltracker.util.CSVHelper;
import com.health.personaltracker.util.MathUtil;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fasting_list = binding.fastingList;

        Spinner spinner = binding.spinnerFilter;
        loadFilter(spinner);

        // Listener para cuando el usuario selecciona algo
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                long filter = selected.equals("Semana") ? getMondayEpochSecond() : 0;
                final FastingDao fastingDao = getFastingDao();
                final List<Fasting> fastingList = fastingDao.getAll()
                        .stream()
                        .filter(f -> f.uid > filter)
                        .sorted(Comparator.comparing(Fasting::getUid).reversed())
                        .collect(Collectors.toList());

                loadHistoryView(fastingList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada seleccionado
            }
        });

        final FastingDao fastingDao = getFastingDao();
        final List<Fasting> fastingList = fastingDao.getAll()
                .stream().sorted(Comparator.comparing(Fasting::getUid).reversed())
                .collect(Collectors.toList());

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

        final FloatingActionButton btnShare = binding.btnDownload;
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        CSVHelper.createCSVFile(getContext(), fastingList);

                        File imagePath = new File(getContext().getExternalCacheDir(), "MyCSVFiles");
                        File newFile = new File(imagePath, "data.csv");
                        Uri contentUri = getUriForFile(getContext(), "com.health.personaltracker.fileprovider", newFile);

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/csv");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

                        Intent shareIntent = Intent.createChooser(sendIntent, "CSV data");
                        startActivity(shareIntent);
                    }
                } catch (Exception ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
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

    private void loadFilter(Spinner spinner) {
        // Datos para el dropdown
        String[] items = {"Todos", "Semana"};

        // Adaptador para mostrar los datos
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                items
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asignar adaptador al spinner
        spinner.setAdapter(spinnerAdapter);
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