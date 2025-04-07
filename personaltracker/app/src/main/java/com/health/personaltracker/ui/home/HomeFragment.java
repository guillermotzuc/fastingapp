package com.health.personaltracker.ui.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    private FloatingActionButton refreshFastingBtn;
    private FloatingActionButton btnSharePhrase;
    private Button btnDiscardFasting;

    private void loadControlsFromBinding(FragmentHomeBinding binding) {

        phraseTextView = binding.textHome;
        progressBar = binding.fastingProgressBar;
        progressBarLabel = binding.progressLabel;
        startStopFastingBtn = binding.btnStartStopFasting;
        refreshFastingBtn = binding.btnRefresh;
        btnSharePhrase = binding.btnShare;
        btnDiscardFasting = binding.btnCancel;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Bind global objects
        loadControlsFromBinding(binding);

        // Load today's phrase
        InputStream XmlFileInputStream = getResources().openRawResource(R.raw.stoicphrases);
        phraseTextView.setText(PhraseHelper.getTodayPhrase(XmlFileInputStream));

        // Initial fasting status
        Fasting currentFasting = getCurrentFasting();
        updateFasting(currentFasting);

        // Bind click events
        setOnClickListenerForStartFasting();
        setOnClickListenerForUpdateFasting();
        setOnClickListenerForSharePhrase();
        setOnClickListenerForDiscardFasting();

        return binding.getRoot();
    }

    private void setOnClickListenerForDiscardFasting() {
        btnDiscardFasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardFasting();
            }
        });
    }

    private void setOnClickListenerForSharePhrase() {
        btnSharePhrase.setOnClickListener(new View.OnClickListener() {
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
    }

    private void setOnClickListenerForUpdateFasting() {
        refreshFastingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fasting currentFasting = getCurrentFasting();
                updateFasting(currentFasting);
            }
        });
    }

    private void setOnClickListenerForStartFasting() {
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

    private void discardFasting() {
        try {
            Fasting current = getCurrentFasting();
            if (Optional.ofNullable(current).isPresent()) {
                FastingDao fastingDao = getFastingDao();
                fastingDao.delete(current);
                updateFasting(null);
            }
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
            btnDiscardFasting.setVisibility(View.VISIBLE);
            Drawable image = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_black_stop_circle_24, getActivity().getTheme());
            startStopFastingBtn.setImageDrawable(image);

            DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
            String startDate = DateTime.parse(current.start_datetime).toString(dtf);

            Period period = new Period(DateTime.parse(current.start_datetime), DateTime.now());
            int hours = period.getHours();
            if (hours == 0) {
                String statusMessage = String.format("%s, %sm",
                        startDate, period.getMinutes());
                progressBarLabel.setText(statusMessage);
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            } else {
                int percentage = (hours * 100) / 24;
                String statusMessage = String.format("%s, %sh %sm [%s%%]",
                        startDate, hours, period.getMinutes(), percentage);
                progressBarLabel.setText(statusMessage);
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
            btnDiscardFasting.setVisibility(View.GONE);
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