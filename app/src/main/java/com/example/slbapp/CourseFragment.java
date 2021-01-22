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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
        TextInputLayout courseName = (TextInputLayout) getView().findViewById(R.id.text_input_course);
            TextInputLayout year = (TextInputLayout) getView().findViewById(R.id.text_input_year);
            TextInputLayout ects = (TextInputLayout) getView().findViewById(R.id.text_input_ects);
            TextInputLayout grade = (TextInputLayout) getView().findViewById(R.id.text_input_grade);
            TextInputLayout isOptional = (TextInputLayout) getView().findViewById(R.id.text_input_isOptional);
            TextInputLayout period = (TextInputLayout) getView().findViewById(R.id.text_input_period);
            TextInputLayout notes = (TextInputLayout) getView().findViewById(R.id.text_input_notes);

        courseName.getEditText().setText(course.getName());
        year.getEditText().setText(course.getYear());
        ects.getEditText().setText(course.getEcts());
        grade.getEditText().setText(course.getGrade());
        period.getEditText().setText(course.getPeriod());
        notes.getEditText().setText(course.getNotes());

        if (course.isOptional()) {
            isOptional.getEditText().setText("keuzevak");
        } else {
            isOptional.getEditText().setText("verplicht vak");
        }
    }
}