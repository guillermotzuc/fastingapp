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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.health.personaltracker.AppDatabase;
import com.health.personaltracker.R;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.databinding.FragmentHistoryBinding;
import com.health.personaltracker.entity.Fasting;
import com.health.personaltracker.model.FragmentBase;
import com.health.personaltracker.util.CSVHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryFragment extends FragmentBase {

    private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final FastingDao fastingDao = getFastingDao();
        final List<Fasting> fastingList = fastingDao.getAll()
                .stream().sorted(Comparator.comparing(Fasting::getUid).reversed())
                .collect(Collectors.toList());
        if (fastingList != null && !fastingList.isEmpty()) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
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
                    Toast.makeText(getContext(),  ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}