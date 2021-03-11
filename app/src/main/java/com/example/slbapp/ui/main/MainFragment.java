package com.example.slbapp.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.slbapp.MainActivity;
import com.example.slbapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makePieChart();

    }

    private void makePieChart() {
        ArrayList<PieEntry> pieEntries;
        PieChart chart = (PieChart) getView().findViewById(R.id.chart);

        int totalPointsSuccess = ((MainActivity)getActivity()).coursesStore.getPointsFromFinishedCourses();
        int totalPointsFail = 240 - totalPointsSuccess;

        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(totalPointsSuccess, "gehaald"));
        pieEntries.add(new PieEntry(totalPointsFail, "niet gehaald"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Aantal punten");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextSize(16);
        PieData pieData = new PieData(pieDataSet);
        chart.setData(pieData);

        Legend legend = chart.getLegend();
        legend.setTextSize(13);
        legend.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
        legend.setWordWrapEnabled(true);
        chart.animateXY(750, 750);
    }
}