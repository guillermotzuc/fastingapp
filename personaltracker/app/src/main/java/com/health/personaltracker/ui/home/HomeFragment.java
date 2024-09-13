package com.health.personaltracker.ui.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.health.personaltracker.AppDatabase;
import com.health.personaltracker.R;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.databinding.FragmentHomeBinding;
import com.health.personaltracker.model.Fasting;
import com.health.personaltracker.model.Phrase;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        textView.setText(getTodayPhrase());
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final ProgressBar progress = binding.fastingProgressBar;
        final TextView progressLabel = binding.progressLabel;

        final Button startFasting = binding.startFastingButton;
        final Button endFasting = binding.endFastingButton;
        endFasting.setVisibility(View.GONE);

        final AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        final FastingDao fastingDao = db.fastingDao();
        Fasting current = fastingDao.findActive();
        updateFasting(current, progress, progressLabel, startFasting, endFasting);

        startFasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();
                LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                int year = localDate.getYear();
                int month = localDate.getMonthValue();
                int day = localDate.getDayOfMonth();
                int hour = localDate.getHour();

                int uid = Integer.parseInt(String.format("%d%d%d%d", year, month, day, hour));
                FastingDao fastingDao = db.fastingDao();
                Fasting newFasting = new Fasting();
                newFasting.uid = uid;
                newFasting.start_datetime = localDate.toString();
                newFasting.end_datetime = "";
                newFasting.active = true;
                fastingDao.insertAll(newFasting);
                updateFasting(newFasting, progress, progressLabel, startFasting, endFasting);
            }
        });

        endFasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Optional.ofNullable(current).isPresent()) {
                    FastingDao fastingDao = db.fastingDao();
                    Date date = new Date();
                    LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    Period p = new Period(DateTime.parse(current.start_datetime), DateTime.now());
                    current.end_datetime = localDate.toString();
                    current.hours = p.getHours();
                    current.active = false;
                    fastingDao.update(current);
                    updateFasting(null, progress, progressLabel, startFasting, endFasting);
                }
            }
        });

        final FloatingActionButton refreshBtn = binding.btnRefresh;
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fasting current = fastingDao.findActive();
                updateFasting(current, progress, progressLabel, startFasting, endFasting);
            }
        });

        return root;
    }

    private void updateFasting(Fasting current, ProgressBar progressBar, TextView progressLabel, Button startFasting, Button endFasting ) {
        if(Optional.ofNullable(current).isPresent()) {
            startFasting.setVisibility(View.GONE);
            endFasting.setVisibility(View.VISIBLE);
            Period p = new Period(DateTime.parse(current.start_datetime), DateTime.now());
            int hours = p.getHours();

            if (hours == 0) {
                progressLabel.setText(p.getMinutes() + " m");
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            } else {
                int percentage = (hours == 0) ? 0 : (hours * 100) / 24;
                progressLabel.setText(hours + "h  " + percentage + " %");
                progressBar.setProgress(percentage, true);
                if (hours >= 12) {
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                }
            }
        } else {
            startFasting.setVisibility(View.VISIBLE);
            endFasting.setVisibility(View.GONE);
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            progressBar.setProgress(0, true);
            progressLabel.setText("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String getTodayPhrase() {

        //1 Takes your JSON file from the raw folder
        InputStream XmlFileInputStream = getResources().openRawResource(R.raw.stoicphrases);
        //2 This reads your JSON file
        String jsonString = readTextFile(XmlFileInputStream);

        // create a gson object
        Gson gson = new Gson();
        // read your json file into an array
        Phrase[] questions = gson.fromJson(jsonString, Phrase[].class);
        // convert your array to a list using the Arrays utility class

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        String seed = String.format("%d%d%d", year, month, day);
        Random rand = new Random(Integer.parseInt(seed));
        int low = 1;
        int high = questions.length - 1;
        int result = rand.nextInt(high - low) + low;
        return questions[result].getPhrase();
    }

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}