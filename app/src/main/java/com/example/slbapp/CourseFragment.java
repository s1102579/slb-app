package com.example.slbapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.slbapp.models.Course;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_POSITION = "course";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int position;
    private Course course = new Course();

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(int position) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d("onCreate", "CourseFragment onCreate if statement");
            position = getArguments().getInt(ARG_POSITION);
            course = ((MainActivity)getActivity()).getFilteredCourse(position);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((MainActivity)getActivity()).setupTextViewsCourseFragment(course);
//        Log.d("cursusNaam", course.getName());
        setupTextViews();
    }

        private void setupTextViews() {
        TextView courseName = (TextView) getView().findViewById(R.id.tv_courseName);
        TextView year = (TextView) getView().findViewById(R.id.tv_year);
        TextView ects = (TextView) getView().findViewById(R.id.tv_ects);
        TextView isOptional = (TextView) getView().findViewById(R.id.tv_isOptional);
        TextView period = (TextView) getView().findViewById(R.id.tv_period);
        TextView notes = (TextView) getView().findViewById(R.id.tv_notes);

        courseName.setText(course.getName());
        year.setText(course.getYear());
        ects.setText(course.getEcts());
        period.setText(course.getPeriod());
        notes.setText(course.getNotes());

        if (course.isOptional()) {
            isOptional.setText("keuzevak");
        } else {
            isOptional.setText("verplicht vak");
        }
    }
}