package com.example.slbapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.slbapp.models.Course;
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
    private boolean courseIsEmpty = false;

    TextInputLayout courseName;
    TextInputLayout year;
    TextInputLayout ects;
    TextInputLayout grade;
    TextInputLayout isOptional;
    TextInputLayout period;
    TextInputLayout notes;

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
        Log.d("onViewCreated", "is het hier");
        setupButtons();
        setupTextViews();
    }

    private void setupTextViews() {

        Log.d("setupTextViews", "test");

        courseName = (TextInputLayout) getView().findViewById(R.id.text_input_course);
        year = (TextInputLayout) getView().findViewById(R.id.text_input_year);
        ects = (TextInputLayout) getView().findViewById(R.id.text_input_ects);
        grade = (TextInputLayout) getView().findViewById(R.id.text_input_grade);
        isOptional = (TextInputLayout) getView().findViewById(R.id.text_input_isOptional);
        period = (TextInputLayout) getView().findViewById(R.id.text_input_period);
        notes = (TextInputLayout) getView().findViewById(R.id.text_input_notes);

        if (course.getName() != null) {
            courseName.getEditText().setText(course.getName());
            year.getEditText().setText(course.getYear());
            ects.getEditText().setText(course.getEcts());
            grade.getEditText().setText(course.getGrade());
            period.getEditText().setText(course.getPeriod());
            notes.getEditText().setText(course.getNotes());

            Log.d("courseName", courseName.getEditText().getText().toString());

            if (course.isOptional()) {
                isOptional.getEditText().setText("keuzevak");
            } else {
                isOptional.getEditText().setText("verplicht vak");
            }
        } else {
            courseIsEmpty = true;
        }

    }

    private void setupButtons() {
        Button button = (Button) getView().findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("knop ingedrukt", "save course");
                saveButtonHandler();
            }
        });
    }


    private void saveButtonHandler() {
        boolean optional;
        optional = !isOptional.getEditText().getText().toString().equals("verplicht vak");

        Course course = new Course(year.getEditText().getText().toString(),
                period.getEditText().getText().toString(),
                courseName.getEditText().getText().toString(),
                ects.getEditText().getText().toString(),
                optional,
                grade.getEditText().getText().toString(),
                notes.getEditText().getText().toString());

        if(courseIsEmpty) {
            ((MainActivity)getActivity()).addCourseToDatabase(course);
        } else {
            ((MainActivity)getActivity()).updateCourseToDatabase(course);
        }
    }
}