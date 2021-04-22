package com.example.slbapp;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CourseValidationHandler {

    public TextInputLayout validateCourseName(TextInputLayout courseName, String courseNameInput, ArrayList<String> allCourseNames) {
        if (courseName.hasFocus()) {
            if(courseNameInput.isEmpty()) {
                courseName.setError("Fields can't be empty");
            } else if(allCourseNames.contains(courseNameInput)) {
                courseName.setError("Course already exists");
            } else {
                courseName.setError(null);
            }
        }
        return courseName;
    }

    public TextInputLayout validateYearOrPeriod(TextInputLayout yearOrPeriod, String yearOrPeriodInput) {
        if(yearOrPeriod.hasFocus()) {
            if(yearOrPeriodInput.isEmpty()) {
                yearOrPeriod.setError("Fields can't be empty");
            } else {

                int num = Integer.parseInt(yearOrPeriodInput);

                if(num > 4) {
                    yearOrPeriod.setError("number cannot be higher than 4");
                } else if(num < 1) {
                    yearOrPeriod.setError("number cannot be lower than 1");
                } else {
                    yearOrPeriod.setError(null);
                }
            }
        }
        return yearOrPeriod;

    }

    public TextInputLayout validateEcts(TextInputLayout ects, String ectsInput) {
        if(ects.hasFocus()) {
            if(ectsInput.isEmpty()) {
                ects.setError("Fields can't be empty");
            } else {

                int num = Integer.parseInt(ectsInput);

                if(num < 1) {
                    ects.setError("number cannot be lower than 1");
                } else {
                    ects.setError(null);
                }
            }
        }
        return ects;

    }

    public TextInputLayout validateGrade(TextInputLayout grade, String gradeInput) {
        if(grade.hasFocus()) {
            if(gradeInput.isEmpty()) {
                grade.setError("Fields can't be empty");
            } else if(gradeInput.equals("V") || gradeInput.equals("O")) {
                grade.setError(null);
            } else {

                try {
                    double num = Double.parseDouble(gradeInput);

                    if(num < 1) {
                        grade.setError("number cannot be lower than 1");
                    } else if(num > 10) {
                        grade.setError("number cannot be higher than 10");
                    } else if((new BigDecimal(gradeInput).scale() > 1)) {
                        grade.setError("number cannot have more than 1 decimal");
                    } else {
                        grade.setError(null);
                    }

                } catch (NumberFormatException e) {
                    grade.setError("only 'V' or 'O' or a number between 1 and 10");
                }
            }
        }
        return grade;

    }

}
