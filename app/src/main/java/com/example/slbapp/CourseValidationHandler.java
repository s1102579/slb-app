package com.example.slbapp;

import com.google.android.material.textfield.TextInputLayout;

public class CourseValidationHandler {


    public TextInputLayout validateCourseName(TextInputLayout courseName, String courseNameInput) {

        if(courseNameInput.isEmpty()) {
            courseName.setError("Fields can't be empty");
        } else {
            courseName.setError(null);
        }

        return courseName;
    }

    public TextInputLayout validateYear(TextInputLayout year, String yearInput) {

        if(yearInput.isEmpty()) {
            year.setError("Fields can't be empty");
        } else {

            int num = Integer.parseInt(yearInput);

            if(num > 4) {
                year.setError("number cannot be higher than 4");
            } else if(num < 1) {
                year.setError("number cannot be lower than 1");
            } else {
                year.setError(null);
            }
        }

        return year;
    }

    public TextInputLayout validateEcts(TextInputLayout ects, String ectsInput) {

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

        return ects;
    }

    public TextInputLayout validateGrade(TextInputLayout grade, String gradeInput) {

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
                } else {
                    grade.setError(null);
                }

            } catch (NumberFormatException e) {
                grade.setError("only 'V' or 'O' or a number between 1 and 10");
            }

        }

        return grade;
    }

}
