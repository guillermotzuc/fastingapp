package com.health.personaltracker.ui.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.health.personaltracker.AppDatabase;
import com.health.personaltracker.R;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.databinding.FragmentHomeBinding;
import com.health.personaltracker.entity.Fasting;
import com.health.personaltracker.model.FragmentBase;
import com.health.personaltracker.util.PhraseHelper;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class HomeFragment extends FragmentBase {

    private FragmentHomeBinding binding;
    private ProgressBar progressBar;
    private TextView progressBarLabel;
    private TextView phraseTextView;
    private FloatingActionButton startStopFastingBtn;
    private FloatingActionButton refreshBtn;
    private FloatingActionButton btnShare;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        phraseTextView = binding.textHome;
        progressBar = binding.fastingProgressBar;
        progressBarLabel = binding.progressLabel;
        startStopFastingBtn = binding.btnStartStopFasting;
        refreshBtn = binding.btnRefresh;
        btnShare = binding.btnShare;

        InputStream XmlFileInputStream = getResources().openRawResource(R.raw.stoicphrases);
        phraseTextView.setText(PhraseHelper.getTodayPhrase(XmlFileInputStream));

        Fasting currentFasting = getCurrentFasting();
        updateFasting(currentFasting);
        startStopFastingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fasting currentFasting = getCurrentFasting();
                if (Optional.ofNullable(currentFasting).isPresent()) {
                    endFasting();
               } else {
                    createFasting();
                }
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fasting currentFasting = getCurrentFasting();
                updateFasting(currentFasting);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, phraseTextView.getText());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
        return root;
    }

    private void createFasting() {
        try {
            Date date = new Date();
            Instant dateInstant = date.toInstant();
            LocalDateTime localDate = dateInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            FastingDao fastingDao = getFastingDao();
            Fasting newFasting = new Fasting();
            newFasting.uid = dateInstant.getEpochSecond();
            newFasting.start_datetime = localDate.toString();
            newFasting.end_datetime = "";
            newFasting.active = true;
            fastingDao.insertAll(newFasting);
            updateFasting(newFasting);
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void endFasting() {
        try {
        Fasting current = getCurrentFasting();
        if (Optional.ofNullable(current).isPresent()) {
            FastingDao fastingDao = getFastingDao();
            Date date = new Date();
            LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Period p = new Period(DateTime.parse(current.start_datetime), DateTime.now());
            current.end_datetime = localDate.toString();
            current.hours = p.getHours();
            current.active = false;
            fastingDao.update(current);
            updateFasting(null);
        }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Fasting getCurrentFasting() {
        final AppDatabase db = this.getAppDatabase();
        final FastingDao fastingDao = db.fastingDao();
        return fastingDao.findActive();
    }

    private void updateFasting(Fasting current) {

        if (Optional.ofNullable(current).isPresent()) {
            progressBar.setVisibility(View.VISIBLE);
            Drawable image = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_black_stop_circle_24, getActivity().getTheme());
            startStopFastingBtn.setImageDrawable(image);

            Period p = new Period(DateTime.parse(current.start_datetime), DateTime.now());
            int hours = p.getHours();
            if (hours == 0) {
                progressBarLabel.setText(p.getMinutes() + " m");
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            } else {
                int percentage = (hours == 0) ? 0 : (hours * 100) / 24;
                progressBarLabel.setText(hours + "h  " + p.getMinutes() + "m " + percentage + " %");
                progressBar.setProgress(percentage, true);
                if (hours >= 12) {
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(169, 223, 191)));
                }
            }
        } else {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(237, 187, 153)));
            progressBar.setProgress(0, true);
            progressBar.setVisibility(View.GONE);
            progressBarLabel.setText("");
            Drawable image = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_black_timer_play_24dp, getActivity().getTheme());
            startStopFastingBtn.setImageDrawable(image);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}