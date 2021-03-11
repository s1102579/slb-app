package com.example.slbapp.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.slbapp.MainActivity;
import com.example.slbapp.R;
import com.example.slbapp.models.Course;
import com.github.mikephil.charting.charts.BarChart;
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

//        Course course = new Course("2016", "1","IOPR1", "3",true, "7", "android ontwikkeling");
//        ((MainActivity)getActivity()).addCourseToDatabase(course);
//        ((MainActivity)getActivity()).getCoursesFromDatabase();
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setupButtons();
        makePieChart();


    }

    private void makePieChart() {
        ArrayList<PieEntry> pieEntries;
        PieChart chart = (PieChart) getView().findViewById(R.id.chart);

        int totalPointsSuccess = ((MainActivity)getActivity()).getPointsFromFinishedCourses();
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

//    private void setupButtons() {
//        // navigates naar list fragment
//        Button button = (Button) getView().findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("knop ingedrukt", "Navigeer naar CourseListActivity");
//                ((MainActivity)getActivity()).navigateToFragment(new ItemFragment());
//            }
//        });
//    }
}