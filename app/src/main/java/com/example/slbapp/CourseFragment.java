package com.example.slbapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.slbapp.models.Course;
import com.example.slbapp.ui.main.ItemFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;

public class CourseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_POSITION = "course";

    private int position;
    private Course course = new Course();
    private boolean courseIsEmpty = false;

    private TextInputLayout courseName;
    private TextInputLayout year;
    private TextInputLayout ects;
    private TextInputLayout grade;
    private TextInputLayout period;
    private TextInputLayout notes;
    private Button button;
    private ArrayList<String> allCourseNames;
    private CourseValidationHandler validator = new CourseValidationHandler();

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String courseNameInput = courseName.getEditText().getText().toString().trim();
            String yearInput = year.getEditText().getText().toString().trim();
            String ectsInput = ects.getEditText().getText().toString().trim();
            String gradeInput = grade.getEditText().getText().toString().trim();
            String periodInput = period.getEditText().getText().toString().trim();

            // validate inputs
            courseName = validator.validateCourseName(courseName, courseNameInput, allCourseNames);
            year = validator.validateYearOrPeriod(year, yearInput);
            ects = validator.validateEcts(ects, ectsInput);
            grade = validator.validateGrade(grade, gradeInput);
            period = validator.validateYearOrPeriod(period, periodInput);

            button.setEnabled(TextUtils.isEmpty(courseName.getError()) &&
                    TextUtils.isEmpty(year.getError()) &&
                    TextUtils.isEmpty(ects.getError()) &&
                    TextUtils.isEmpty(grade.getError()) &&
                    TextUtils.isEmpty(period.getError()) &&
                    !courseNameInput.isEmpty() &&
                    !yearInput.isEmpty() &&
                    !ectsInput.isEmpty() &&
                    !gradeInput.isEmpty() &&
                    !periodInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };


    public CourseFragment() {}

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
            course = ((MainActivity)getActivity()).coursesService.getFilteredCourse(position);
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

        setupButtons();
        setupTextInputs();
        setupSpinner();
        allCourseNames = ((MainActivity)getActivity()).coursesService.getAllCourseNames();

    }

    // deze twee methods zijn voor de Spinner

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        course.setOptional(position == 0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner_isOptional);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.isOptionalString, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (course.isOptional()) {
            spinner.setSelection(0);
        } else {
            spinner.setSelection(1);
        }
    }

    private void setupTextInputs() {

        courseName = (TextInputLayout) getView().findViewById(R.id.text_input_course);
        year = (TextInputLayout) getView().findViewById(R.id.text_input_year);
        ects = (TextInputLayout) getView().findViewById(R.id.text_input_ects);
        grade = (TextInputLayout) getView().findViewById(R.id.text_input_grade);
        period = (TextInputLayout) getView().findViewById(R.id.text_input_period);
        notes = (TextInputLayout) getView().findViewById(R.id.text_input_notes);

        courseName.getEditText().addTextChangedListener(textWatcher);
        year.getEditText().addTextChangedListener(textWatcher);
        ects.getEditText().addTextChangedListener(textWatcher);
        grade.getEditText().addTextChangedListener(textWatcher);
        period.getEditText().addTextChangedListener(textWatcher);

        if (course.getName() != null) {
            courseName.getEditText().setText(course.getName());
            year.getEditText().setText(course.getYear());
            ects.getEditText().setText(course.getEcts());
            grade.getEditText().setText(course.getGrade());
            period.getEditText().setText(course.getPeriod());
            notes.getEditText().setText(course.getNotes());

            Log.d("courseName", courseName.getEditText().getText().toString());

        } else {
            courseIsEmpty = true;
        }

    }

    private void setupButtons() {
        button = (Button) getView().findViewById(R.id.save_button);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("knop ingedrukt", "save course");
                saveButtonHandler();
            }
        });
    }

    private void saveButtonHandler() {

        Course tempCourse = new Course(year.getEditText().getText().toString(),
                period.getEditText().getText().toString(),
                courseName.getEditText().getText().toString(),
                ects.getEditText().getText().toString(),
                course.isOptional(),
                grade.getEditText().getText().toString(),
                notes.getEditText().getText().toString());

        long date = new Date().getTime();

        if(courseIsEmpty) {

            ((MainActivity)getActivity()).coursesService.addCourseToFireBase(tempCourse, date);
            ((MainActivity)getActivity()).coursesService.addCourseToDatabase(tempCourse, date);
            Snackbar.make(getView(), "added Course: " +
                    courseName.getEditText().getText().toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            ((MainActivity)getActivity()).coursesService.addCourseToFireBase(tempCourse, date);
            ((MainActivity)getActivity()).coursesService.updateCourseToDatabase(tempCourse, date);
            Snackbar.make(getView(), "updated Course: " +
                    courseName.getEditText().getText().toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        ((MainActivity)getActivity()).navigateToFragment(new ItemFragment());
    }
}