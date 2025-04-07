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
import android.widget.ListView;
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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
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
        final FastingDao fastingDao = getFastingDao();
        final List<Fasting> fastingList = fastingDao.getAll()
                .stream().sorted(Comparator.comparing(Fasting::getUid).reversed())
                .collect(Collectors.toList());
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

            fasting_list = binding.fastingList;
            adapter = new HistoryAdapter(this.getContext(), fastingHistory);

            fasting_list.setAdapter(adapter);
            fasting_list.setClickable(true);
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
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void deleteHistoryRecord(long id, int position) {

        FastingDao fastingDao = getFastingDao();
        fastingDao.deleteById(id);
        fasting_list.removeViewAt(position);
        ((HistoryAdapter) fasting_list.getAdapter()).notifyDataSetChanged();
        Toast.makeText(getActivity(), "Registro eliminado", Toast.LENGTH_SHORT).show();
    }
}