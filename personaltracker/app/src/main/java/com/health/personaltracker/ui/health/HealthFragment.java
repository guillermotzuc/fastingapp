package com.health.personaltracker.ui.health;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.health.personaltracker.R;
import com.health.personaltracker.databinding.FragmentHealthBinding;
import com.health.personaltracker.databinding.FragmentHomeBinding;
import com.health.personaltracker.model.FragmentBase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthFragment extends FragmentBase {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentHealthBinding binding;
    private BarChart stepsChart;
    private TextInputEditText etEdad, etPeso, etPasos;
    private MaterialCheckBox cbExercise;
    private MaterialButton btnGuardar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthFragment newInstance(String param1, String param2) {
        HealthFragment fragment = new HealthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHealthBinding.inflate(inflater, container, false);

        this.etEdad = binding.etEdad;
        this.etPeso = binding.etPeso;
        this.etPasos = binding.etPasos;
        this.cbExercise = binding.cbExercise;
        this.btnGuardar = binding.btnGuardar;
        this.stepsChart = binding.stepsChart;
        setupChart();
        loadStepAverages();

        //setOnClickListenerForSaveFitnessRecord();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void setOnClickListenerForSaveFitnessRecord() {
        this.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdad.setText("");
                etPeso.setText("");
                etPasos.setText("");
                cbExercise.setEnabled(false);
            }
        });
    }

    private void setupChart() {
        // Basic chart configuration
        stepsChart.getDescription().setEnabled(false);
        stepsChart.setDrawGridBackground(false);
        stepsChart.setTouchEnabled(true);
        stepsChart.setDragEnabled(true);
        stepsChart.setScaleEnabled(true);
        stepsChart.setPinchZoom(true);

        // X-Axis setup
        XAxis xAxis = stepsChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new DayAxisValueFormatter());

        // Left Y-Axis setup
        YAxis leftAxis = stepsChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1000f);

        // Disable right Y-Axis
        stepsChart.getAxisRight().setEnabled(false);

        // Disable legend
        stepsChart.getLegend().setEnabled(false);
    }

    private void loadStepAverages() {
        // Mock data - replace with your actual data source
        List<DayAverage> weeklyAverages = new ArrayList<>();
        weeklyAverages.add(new DayAverage(0, 4500)); // Monday
        weeklyAverages.add(new DayAverage(1, 6200)); // Tuesday
        weeklyAverages.add(new DayAverage(2, 3800)); // Wednesday
        weeklyAverages.add(new DayAverage(3, 7400)); // Thursday
        weeklyAverages.add(new DayAverage(4, 5300)); // Friday
        weeklyAverages.add(new DayAverage(5, 8900)); // Saturday
        weeklyAverages.add(new DayAverage(6, 4100)); // Sunday

        updateChart(weeklyAverages);
    }

    private void updateChart(List<DayAverage> averages) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < averages.size(); i++) {
            DayAverage day = averages.get(i);
            entries.add(new BarEntry(i, day.getAverageSteps()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Average Steps");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.chart_bar_color));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new StepsValueFormatter());

        BarData barData = new BarData(dataSet);
        stepsChart.setData(barData);

        // Configure X-axis labels
        stepsChart.getXAxis().setLabelCount(averages.size());
        stepsChart.invalidate(); // Refresh chart
    }

    // Helper Classes
    private static class DayAxisValueFormatter extends ValueFormatter {
        private final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        @Override
        public String getFormattedValue(float value) {
            int index = (int) value;
            if (index >= 0 && index < days.length) {
                return days[index];
            }
            return "";
        }
    }

    private static class StepsValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }

    // Data model
    private static class DayAverage {
        private final int dayOfWeek;
        private final int averageSteps;

        public DayAverage(int dayOfWeek, int averageSteps) {
            this.dayOfWeek = dayOfWeek;
            this.averageSteps = averageSteps;
        }

        public int getAverageSteps() {
            return averageSteps;
        }
    }
}